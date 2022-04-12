package com.sparta.jwt_souce.model;


import com.sparta.jwt_souce.dto.SignUpRequestDto;
import com.sparta.jwt_souce.dto.SignUpRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class User {

    // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String pw;

    @Column(nullable = false,unique = true)
    private String nickname;

    @Column(nullable = true)
    @Enumerated(value = EnumType.STRING)
    private com.sparta.jwt_souce.model.UserRoleEnum role;


    @Column(unique = true)
    private Long kakaoId;


    public User(String email, String pw, String nickname, com.sparta.jwt_souce.model.UserRoleEnum role, Long kakaoId) {
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    public User(String email, String pw, String nickname, com.sparta.jwt_souce.model.UserRoleEnum role) {
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.role = role;
        this.kakaoId = null;
    }

    public User(SignUpRequestDto signUpRequestDto) {
        this.email = signUpRequestDto.getEmail();
        this.pw = signUpRequestDto.getPw();
        this.nickname = signUpRequestDto.getNickname();
        this.kakaoId = null;
    }
}
