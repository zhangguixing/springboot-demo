package com.myth.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.myth.common.util.JSONUtils;
import feign.Request;
import feign.RequestInterceptor;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.optionals.OptionalDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author zhangguixing Email:guixingzhang@qq.com
 */
@Slf4j
@Configuration
@EnableFeignClients
public class GlobalFeignConfiguration {

    private static final ThreadLocal<Instant> INSTANT_THREADLOCAL = new ThreadLocal<>();

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            INSTANT_THREADLOCAL.set(Instant.now());
        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (s, response) -> {
            try {
                Request request = response.request();
                String url = request.url();
                int status = response.status();
                String header = JSONUtils.to(request.headers());
                String method = request.requestTemplate().method();
                String requestBody;
                if (HttpMethod.POST.name().equals(method) || HttpMethod.PUT.name().equals(method)) {
                    requestBody = new String(request.body(), StandardCharsets.UTF_8);
                } else {
                    requestBody = request.requestTemplate().queryLine();
                }
                String responseBody = IOUtils.toString(response.body().asInputStream(), String.valueOf(StandardCharsets.UTF_8));
                long timeConsuming = timeConsuming();
                log.error("feign error url:{}, status:{}, header:{}, request:{}, response:{}, time-consuming:{}", url, status, header, requestBody, responseBody, timeConsuming);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ErrorDecoder.Default().decode(s, response);
        };
    }

    @Bean
    public Decoder feignDecoder() {
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper()));
        return new ResultStatusDecoder(new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(objectFactory))));
    }

    public static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.registerModule(assembleJavaTimeModule());
        return objectMapper;
    }

    private static JavaTimeModule assembleJavaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_PATTERN)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_PATTERN)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(TIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATETIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_PATTERN)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(TIME_PATTERN)));
        return javaTimeModule;
    }

    /**
     * feign返回值拦截
     */
    static class ResultStatusDecoder implements Decoder {

        final Decoder delegate;

        public ResultStatusDecoder(Decoder delegate) {
            Objects.requireNonNull(delegate, "Decoder must not be null. ");
            this.delegate = delegate;
        }

        @Override
        public Object decode(Response response, Type type) throws IOException {
            // 输出返回信息
            Request request = response.request();
            String url = request.url();
            int status = response.status();
            String header = JSONUtils.to(request.headers());
            String method = request.requestTemplate().method();
            String requestBody;
            if (HttpMethod.POST.name().equals(method) || HttpMethod.PUT.name().equals(method)) {
                requestBody = new String(request.body(), StandardCharsets.UTF_8);
            } else {
                requestBody = request.requestTemplate().queryLine();
            }
            String responseBody = IOUtils.toString(response.body().asInputStream(), String.valueOf(StandardCharsets.UTF_8));
            long timeConsuming = timeConsuming();
            log.info("feign url:{}, status:{}, header:{}, request:{}, response:{}, time-consuming:{}", url, status, header, requestBody, responseBody, timeConsuming);
            return delegate.decode(response.toBuilder().body(responseBody, StandardCharsets.UTF_8).build(), type);
        }
    }

    /**
     * 耗时计算
     *
     * @return 耗时
     */
    public static long timeConsuming() {
        Instant startInstant = INSTANT_THREADLOCAL.get();
        if (startInstant == null) {
            return 0;
        }
        try {
            return Duration.between(startInstant, Instant.now()).toMillis();
        } finally {
            INSTANT_THREADLOCAL.remove();
        }
    }
}