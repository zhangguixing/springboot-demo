package com.myth;

import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
        RabbitAutoConfiguration.class, // 禁用rabbitmq
        RedissonAutoConfiguration.class, // 禁用redisson
        DataSourceAutoConfiguration.class, // 禁用mysql
})
public class MythApplication {

    public static void main(String[] args) {
        SpringApplication.run(MythApplication.class, args);
    }

}
