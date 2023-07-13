package com.sparta.springlv4.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Message {
    private String message;
    private int statusCode;
}