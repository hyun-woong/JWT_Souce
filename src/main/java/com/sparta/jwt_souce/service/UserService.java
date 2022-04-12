package com.miniproject.spring.service;

import com.miniproject.spring.dto.SignUpRequestDto;
import com.miniproject.spring.dto.UserRequestDto;
import com.miniproject.spring.exception.ErrorCode;
import com.miniproject.spring.exception.HanghaeMiniException;
import com.miniproject.spring.model.User;
import com.miniproject.spring.model.UserRoleEnum;
import com.miniproject.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }




    public User registerUser(SignUpRequestDto requestDto) throws HanghaeMiniException {

        // 패스워드 암호화
        String pw = passwordEncoder.encode(requestDto.getPw());

        //가입 email(id) 중복체크
        String email = requestDto.getEmail();
        Optional<User> found = userRepository.findByEmail(email);
        if (found.isPresent()){
            throw new HanghaeMiniException(ErrorCode.EMAIL_DUPLICATE);
        }


        //가입 nickname 중복체크
        String nickname = requestDto.getNickname();
        Optional<User> found2 = userRepository.findByNickname(nickname);
        if (found2.isPresent()) {
            throw new HanghaeMiniException(ErrorCode.NICKNAME_DUPLICATE);
        }

        //비밀번호확인
        String password = requestDto.getPw();
        String passwordCheck = requestDto.getPwCheck();

        if (!password.isEmpty() && !passwordCheck.isEmpty()) {
            if (password.length() >= 6 && password.length() <= 20) {
                if (!password.equals(passwordCheck)) {
                    throw new HanghaeMiniException(ErrorCode.USER_NOT_FOUND);
                }
            } else {
                throw new HanghaeMiniException(ErrorCode.PASSWORD_PATTERN_LENGTH);

            }
        } else {
            throw new HanghaeMiniException(ErrorCode.PASSWORD_ENTER);
        }



        //사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new HanghaeMiniException(ErrorCode.ADMIN_PASSWORD_DISCORDANCE);
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(email, pw, nickname, role);
        return userRepository.save(user);
    }

    public User login(UserRequestDto requestDto) throws HanghaeMiniException {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new HanghaeMiniException(ErrorCode.USER_NOT_FOUND)
        );

        // 패스워드 암호화
        if (!passwordEncoder.matches(requestDto.getPw(), user.getPw())) {
            throw new HanghaeMiniException(ErrorCode.USER_NOT_FOUND);
        }

        return user;
    }

    // 로그인 중복 email
    public Map<String, String> duplicateId(UserRequestDto userRequestDto) {
        User user = userRepository.findByEmail(userRequestDto.getEmail()).orElse(null);

        Map<String, String> result = new HashMap<>();
        if (user == null) {
            result.put("result", "success");
            return result;
        }

        result.put("result", "fail");
        result.put("message", "중복된 email이 존재합니다.");
        return result;
    }

    //로그인 중복 닉네임
    public Map<String, String> duplicateNickname(SignUpRequestDto signUpRequestDto) {
        User user = userRepository.findByNickname(signUpRequestDto.getNickname()).orElse(null);
        Map<String, String> result = new HashMap<>();
        if (user == null) {
            result.put("result", "success");
            return result;
        }

        result.put("result", "fail");
        result.put("message", "중복된 닉네임이 있습니다.");
        return result;
    }
}



