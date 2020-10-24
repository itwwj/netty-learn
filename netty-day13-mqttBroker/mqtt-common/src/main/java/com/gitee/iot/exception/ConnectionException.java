package com.gitee.iot.exception;

/**
 * <p>连接异常处理
 * @author jie
 */
public class ConnectionException extends RuntimeException{
    public ConnectionException(String message) {
        super(message);
    }
}
