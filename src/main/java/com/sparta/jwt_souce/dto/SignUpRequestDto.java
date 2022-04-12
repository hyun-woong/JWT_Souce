package com.miniproject.spring.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    private String email;
    private String pw;
    private String pwCheck;
    private String nickname;
    private boolean admin = false;
    private String adminToken = "";

}
