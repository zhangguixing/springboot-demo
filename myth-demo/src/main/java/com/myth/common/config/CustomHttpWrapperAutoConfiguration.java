package com.myth.common.config;

import com.myth.common.filter.HttpServletFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangguixing Email:guixingzhang@qq.com
 */
@Configuration
public class CustomHttpWrapperAutoConfiguration {
    @Bean
    @ConditionalOnProperty(name = "servlet.http-wrapper.enabled", havingValue = "true")
    public HttpServletFilter logFilter() {
        return new HttpServletFilter();
    }
}