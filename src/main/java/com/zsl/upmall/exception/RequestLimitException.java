package com.zsl.upmall.exception;

import lombok.Getter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public class RequestLimitException extends RuntimeException {
    private static final long serialVersionUID = 1364225358754654702L;
    private Integer code = BAD_REQUEST.value();

    public RequestLimitException(){
        super("HTTP请求超出设定的限制");
    }

    public RequestLimitException(String message){
        super(message);
    }
}
