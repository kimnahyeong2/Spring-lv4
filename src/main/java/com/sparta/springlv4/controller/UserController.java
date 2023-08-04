package com.sparta.springlv4.controller;

import com.sparta.springlv4.dto.UserRequestDto;
import com.sparta.springlv4.error.MessageDto;
import com.sparta.springlv4.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<MessageDto> signup(@RequestBody @Valid UserRequestDto.SignupRequestDto requestDto, BindingResult bindingResult) {
        try {
            userService.signup(requestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageDto("중복된 username 입니다.", HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.status(201).body(new MessageDto("회원가입 성공", HttpStatus.CREATED.value()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<MessageDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        MessageDto restApiException = new MessageDto(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }
}