package com.myth.common.exception;


/**
 * @author duanxinyuan
 * 2019/4/10 22:35
 */
public class JacksonException extends RuntimeException {
    public JacksonException() {
        super();
    }

    public JacksonException(String message) {
        super(message);
    }

    public JacksonException(Throwable cause) {
        super(cause);
    }
}