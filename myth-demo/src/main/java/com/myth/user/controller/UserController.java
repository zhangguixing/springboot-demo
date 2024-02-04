package com.myth.user.controller;

import com.myth.common.api.CommonPage;
import com.myth.common.api.CommonResult;
import com.myth.user.application.UserApplication;
import com.myth.user.infrastructure.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author MyBatis Plus Generator
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserApplication userApplication;


    @GetMapping("list")
    public CommonResult<CommonPage<User>> page(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<User> list = userApplication.list(pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @PostMapping
    public CommonResult<User> add(@RequestBody User user){
        return CommonResult.success(user);
    }
}
