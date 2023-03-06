package com.sparta.hanghaehomework2.repository;

import com.sparta.hanghaehomework2.dto.PostResponseDto;
import com.sparta.hanghaehomework2.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

     List<PostResponseDto> findAllByOrderByCreatedAtDesc();
    //Optional<Post> findByIdAndUserId(Long id, Long userId);
}
