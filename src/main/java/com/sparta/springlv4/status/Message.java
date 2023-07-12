package com.sparta.springlv4.status;

import lombok.Data;

@Data
public class Message {

    private int statusCode;
    private String message;

    public Message() {
        this.statusCode = 200;
        this.message = null;
    }

    public Message(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.message = msg;
    }
}