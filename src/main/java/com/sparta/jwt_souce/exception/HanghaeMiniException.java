package com.sparta.jwt_souce.exception;

import lombok.Getter;

@Getter
public class HanghaeMiniException extends Exception {

    private final ErrorCode errorCode;

    public HanghaeMiniException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
