package com.myth.common.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 重写ServletRequest、ServletResponse
 * 支持请求体和响应结果重复读取
 *
 * @author zhangguixing Email:guixingzhang@qq.com
 */
@WebFilter(filterName = "httpServletFilter", urlPatterns = "/*")
public class HttpServletFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        CustomHttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        CustomHttpServletResponseWrapper responseWrapper = new CustomHttpServletResponseWrapper((HttpServletResponse) servletResponse);
        filterChain.doFilter(requestWrapper, responseWrapper);
    }
}