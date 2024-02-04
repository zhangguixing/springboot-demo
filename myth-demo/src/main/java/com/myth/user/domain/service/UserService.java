package com.myth.user.domain.service;

import com.myth.user.infrastructure.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author MyBatis Plus Generator
 */
public interface UserService extends IService<User> {

    List<User> list(Integer pageNum, Integer pageSize);
}
