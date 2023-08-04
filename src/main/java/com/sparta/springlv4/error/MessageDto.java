package com.sparta.springlv4.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDto {
    private String message;
    private Integer statusCode;

    public MessageDto(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}