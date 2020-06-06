package com.zsl.upmall.exception;

/**
 * @ClassName BusinessException
 * @Description 业务异常
 * @Version 1.0
 **/
public class BusinessException extends Exception{

    private Integer statusCode;

    public BusinessException() {
        super();
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(Integer statusCode , String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
