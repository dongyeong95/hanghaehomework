package com.sparta.hanghaehomework2.controller;

import com.sparta.hanghaehomework2.dto.LoginRequestDto;
import com.sparta.hanghaehomework2.dto.ResponseDto;
import com.sparta.hanghaehomework2.dto.SignupRequestDto;
import com.sparta.hanghaehomework2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return new ResponseDto("회원가입 성공", HttpStatus.OK.value());
    }

    @PostMapping("/login")
    public ResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return new ResponseDto("로그인 성공", HttpStatus.OK.value());
    }
}
