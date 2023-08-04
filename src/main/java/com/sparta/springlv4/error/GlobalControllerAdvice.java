package com.sparta.springlv4.error;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j(topic = "Global 예외처리")
public class GlobalControllerAdvice {
    // CustomException
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageDto> notFoundException(NotFoundException ex) {
        log.info("NotFoundException : " + ex.getMessage());
        MessageDto messageDto = new MessageDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                messageDto,
                HttpStatus.BAD_REQUEST
        );
    }

    // 400
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<MessageDto> runtimeException(RuntimeException ex) {
        log.info("RuntimeException : " + ex.getMessage());
        MessageDto messageDto = new MessageDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                messageDto,
                HttpStatus.BAD_REQUEST
        );
    }

    // 401
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<MessageDto> handleAccessDeniedException(final AccessDeniedException ex) {
        log.info("AccessDeniedException : " + ex.getMessage());

        MessageDto messageDto = new MessageDto(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(
                messageDto,
                HttpStatus.UNAUTHORIZED
        );
    }

    // 500
    @ExceptionHandler({Exception.class})
    public ResponseEntity<MessageDto> handleAll(final Exception ex) {
        log.info("Exception : " + ex.getMessage());

        MessageDto messageDto = new MessageDto(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(
                messageDto,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<MessageDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.info("IllegalArgumentException : " + ex.getMessage());

        MessageDto messageDto = new MessageDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                messageDto,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({DuplicateRequestException.class})
    public ResponseEntity<MessageDto> duplicateRequestException(DuplicateRequestException ex) {
        log.info("DuplicateRequestException : " + ex.getMessage());

        MessageDto messageDto = new MessageDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // Http Body
                messageDto,
                // Http Status Code
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<MessageDto> nullPointerExceptionHandler(NullPointerException ex) {
        log.info("NullPointerException : " + ex.getMessage());

        MessageDto messageDto = new MessageDto(ex.getMessage(), HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(
                messageDto,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageDto> processValidationError(MethodArgumentNotValidException ex) {
        log.info("MethodArgumentNotValidException : " + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());

        MessageDto messageDto = new MessageDto(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(
                messageDto,
                HttpStatus.BAD_REQUEST
        );
    }

}