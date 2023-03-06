package com.sparta.hanghaehomework2.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SignupRequestDto {
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z]).{4,10}$", message = "아이디는 알파벳 소문자와 숫자를 포함한 4~10자리여야 합니다.")
    //@Size(min = 4, max = 10, message = "최소 4자 이상, 10자 이하여야만 합니다.")
    private String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&]).{8,15}$", message = "비밀번호는 알파벳 대소문자와 숫자, 특수문자를 포함한 8~15자리여야 합니다.")
    //@Size(min = 8, max = 15, message = "최소 8자 이상, 15자 이하여야만 합니다.")
    private String password;
}
