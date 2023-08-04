package com.sparta.springlv4.controller;

import com.sparta.springlv4.dto.UserRequestDto;
import com.sparta.springlv4.error.MessageDto;
import com.sparta.springlv4.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<MessageDto> signup(@RequestBody @Validated UserRequestDto.SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.status(201).body(new MessageDto("회원가입 성공", HttpStatus.CREATED.value()));
    }
    
}