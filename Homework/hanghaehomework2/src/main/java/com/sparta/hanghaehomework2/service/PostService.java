package com.sparta.hanghaehomework2.service;

import com.sparta.hanghaehomework2.dto.PostRequestDto;
import com.sparta.hanghaehomework2.dto.PostResponseDto;
import com.sparta.hanghaehomework2.dto.ResponseDto;
import com.sparta.hanghaehomework2.entity.Post;
import com.sparta.hanghaehomework2.entity.User;
import com.sparta.hanghaehomework2.jwt.JwtUtil;
import com.sparta.hanghaehomework2.repository.PostRepository;
import com.sparta.hanghaehomework2.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시물 작성 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("토큰 에러");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Post post = postRepository.saveAndFlush(new Post(postRequestDto, user.getUsername()));

            return new PostResponseDto(post);
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("조회하려는 게시물이 존재하지 않습니다.")
        );
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, HttpServletRequest request) {
        User user = userInfo(request);
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );
        if (post.getUsername().equals(user.getUsername())) {
            post.update(postRequestDto);
        }
        else {
            throw new IllegalArgumentException("등록한 게시물만 수정할 수 있습니다.");
        }
        return new PostResponseDto(post);
    }

    @Transactional
    public ResponseDto deletePost(Long id, HttpServletRequest request) {
        User user = userInfo(request);
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );
        if (post.getUsername().equals(user.getUsername())) {
            postRepository.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("등록한 게시물만 삭제할 수 있습니다.");
        }
        return new ResponseDto("삭제 완료", HttpStatus.OK.value());
    }

    private User userInfo(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if(token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            }
            else {
                throw new IllegalArgumentException("토큰 에러");
            }
            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            return user;
        } else {
            throw new IllegalArgumentException("해당 토큰값과 일치하는 정보가 없습니다.");
        }
    }
}
