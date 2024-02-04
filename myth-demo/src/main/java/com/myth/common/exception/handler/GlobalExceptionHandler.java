package com.myth.common.exception.handler;

import com.myth.common.api.CommonResult;
import com.myth.common.api.ResultCode;
import com.myth.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * 全局异常处理
 *
 * @author zhangguixing Email:guixingzhang@qq.com
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public CommonResult handleBusinessException(BusinessException ex, WebRequest request) {
        log.error("[全局异常处理] - 业务异常，请求信息：{}, 异常信息：", request.getDescription(true), ex);
        return CommonResult.failed(ex.getCode(), ex.getMessage());
    }

    /**
     * Validated注解参数校验 返回
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public CommonResult MethodArgumentNotValidExceptionHandler(BindException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return CommonResult.failed(ResultCode.VALIDATE_FAILED.getCode(), objectError.getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public CommonResult handleException(Exception ex, WebRequest request) {
        log.error("[全局异常处理] - 请求信息：{}, 异常信息：", request.getDescription(true), ex);
        return CommonResult.failed(ex.getMessage());
    }
}
