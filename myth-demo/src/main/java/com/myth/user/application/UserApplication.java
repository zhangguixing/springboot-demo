package com.myth.user.application;

import com.myth.user.domain.service.UserService;
import com.myth.user.infrastructure.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangguixing Email:guixingzhang@qq.com
 */
@Service
public class UserApplication {

//    @Autowired
//    private UserService userService;
//
    public List<User> list(Integer pageNum, Integer pageSize){
//        return userService.list(pageNum, pageSize);
        return null;
    }
}
