package com.myth.common.exception;

import com.myth.common.api.ResultCode;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author zhangguixing Email:guixingzhang@qq.com
 */
@Getter
public class BusinessException extends Exception {

    private final long code;

    public BusinessException(long code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(Throwable cause) {
        this(ResultCode.FAILED.getCode(), cause);
    }

    public BusinessException(long code, Throwable cause) {
        super(cause);
        this.code = code;
    }

}
