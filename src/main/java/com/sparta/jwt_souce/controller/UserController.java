package com.miniproject.spring.controller;

import com.miniproject.spring.dto.SignUpRequestDto;
import com.miniproject.spring.dto.UserRequestDto;
import com.miniproject.spring.exception.ErrorCode;
import com.miniproject.spring.exception.HanghaeMiniException;
import com.miniproject.spring.model.User;
import com.miniproject.spring.security.UserDetailsImpl;
import com.miniproject.spring.security.jwt.JwtTokenProvider;
import com.miniproject.spring.service.UserService;
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
