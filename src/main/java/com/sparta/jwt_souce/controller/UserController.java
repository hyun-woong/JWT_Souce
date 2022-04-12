package com.sparta.jwt_souce.controller;

import com.sparta.jwt_souce.dto.SignUpRequestDto;
import com.sparta.jwt_souce.dto.UserRequestDto;
import com.sparta.jwt_souce.exception.ErrorCode;
import com.sparta.jwt_souce.exception.HanghaeMiniException;
import com.sparta.jwt_souce.model.User;
import com.sparta.jwt_souce.security.UserDetailsImpl;
import com.sparta.jwt_souce.security.jwt.JwtTokenProvider;
import com.sparta.jwt_souce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider){
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //가입 요청 처리
    @PostMapping("/signup")
    public Map<String, String> registerUser(@RequestBody SignUpRequestDto requestDto) throws HanghaeMiniException {
        User user = userService.registerUser(requestDto);
        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
        result.put("id", String.valueOf(user.getId()));
        result.put("email", user.getEmail());
        result.put("nickname", user.getNickname());

        return result;
    }


    // 로그인
    @PostMapping("/login")
    public Map<String,String> login(@RequestBody UserRequestDto requestDto) throws HanghaeMiniException {
        User user = userService.login(requestDto);

        Map<String,String> result =new HashMap<>();
        result.put("token",jwtTokenProvider.createToken(user.getEmail(), user.getEmail(), user.getNickname())); // "username" : {username}
        result.put("email", user.getEmail());
        result.put("nickname", user.getNickname());
        result.put("result", "success");

        return result;
    }

    @PostMapping("/signup/duplicate_id")
    public Map<String, String> duplicateId(@RequestBody UserRequestDto userRequestDto) {
        return userService.duplicateId(userRequestDto);
    }

    @PostMapping("/signup/duplicate_nickname")
    public Map<String, String> duplicateNickname(@RequestBody SignUpRequestDto signUpRequestDto) {
        return userService.duplicateNickname(signUpRequestDto);
    }


    @GetMapping("/auth")
    public Map<String, String> loginCheck(@AuthenticationPrincipal UserDetailsImpl userDetails) throws HanghaeMiniException {
        if (userDetails == null) {
            throw new HanghaeMiniException(ErrorCode.LOGIN_TOKEN_EXPIRE);
        }
        Map<String, String> result = new HashMap<>();
        result.put("email", userDetails.getUser().getEmail());
        result.put("nickname", userDetails.getUser().getNickname());
        result.put("result", "success");

        return result;
    }
}
