package com.demo.springbootcxfdemo.service.impl;

import com.demo.springbootcxfdemo.service.UserService;
import org.springframework.stereotype.Component;

/**
 * @Author Guixing
 * @Date 2019/1/17 14:00
 * @Description
 */
@Component
public class UserServiceImpl implements UserService {

    @Override
    public String getHello(String name) {
        return "hello "+name;
    }
}
