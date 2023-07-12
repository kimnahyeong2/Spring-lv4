package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.UserRequestDto;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.entity.UserRoleEnum;
import com.sparta.springlv4.repository.UserRepository;
import com.sparta.springlv4.status.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public ResponseEntity<Message> signup(UserRequestDto.SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Message message = new Message();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            message.setStatusCode(400);
            message.setMessage("중복된 username 입니다.");
            return new ResponseEntity<Message>(message, HttpStatus.BAD_REQUEST);
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if(requestDto.isAdmin()){
            if(!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                message.setStatusCode(400);
                message.setMessage("관리자 암호가 틀려 등록이 불가능합니다.");
                return new ResponseEntity<Message>(message, HttpStatus.BAD_REQUEST);
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, role);
        userRepository.save(user);
        message.setMessage("회원가입에 성공하였습니다");

        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }
}