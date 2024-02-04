package com.myth.common.interceptor;

import com.myth.common.filter.CustomHttpServletRequestWrapper;
import com.myth.common.filter.CustomHttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;

/**
 * 日志打印拦截器
 *
 * @author zhangguixing Email:guixingzhang@qq.com
 */
@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Value("${log.interceptor.enabled:false}")
    private boolean enabled;

    @Value("${log.interceptor.response-print:false}")
    private boolean responsePrint;

    private final ThreadLocal<Instant> instantThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        instantThreadLocal.set(Instant.now());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            if (!enabled || !(request instanceof CustomHttpServletRequestWrapper) || !(response instanceof CustomHttpServletResponseWrapper)) {
                HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
                return;
            }
            CustomHttpServletRequestWrapper requestWrapper = (CustomHttpServletRequestWrapper) request;
            CustomHttpServletResponseWrapper responseWrapper = (CustomHttpServletResponseWrapper) response;
            String method = requestWrapper.getMethod();
            String requestParam;
            String responseData;
            if ((HttpMethod.POST.name().equalsIgnoreCase(method) || HttpMethod.PUT.name().equalsIgnoreCase(method)) && "application/json".equalsIgnoreCase(requestWrapper.getContentType())) {
                requestParam = requestWrapper.getBody().replace("\n", "").replace("\r", "").replace("\t", "");
            } else {
                requestParam = requestWrapper.getQueryString();
            }
            Instant startInstant = instantThreadLocal.get();
            if (responsePrint) {
                responseData = responseWrapper.getBody().replace("\n", "").replace("\r", "").replace("\t", "");
                log.info("[请求日志]-URL: {}, 请求方式:{}, 请求参数: {}, 响应参数: {}, 耗时: {}ms", requestWrapper.getRequestURL(), method, requestParam, responseData, Duration.between(startInstant, Instant.now()).toMillis());
            } else {
                log.info("[请求日志]-URL: {}, 请求方式:{}, 请求参数: {}, 耗时: {}ms", requestWrapper.getRequestURL(), method, requestParam, Duration.between(startInstant, Instant.now()).toMillis());
            }
            HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        } finally {
            instantThreadLocal.remove();
        }
    }
}
