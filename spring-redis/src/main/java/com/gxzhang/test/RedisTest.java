package com.gxzhang.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * redis整合测试
 *
 * @author zhangguixing Email:zhangguixing@co-mall.com
 * @since 2021-05-19 下午 16:26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void test() throws InterruptedException, JsonProcessingException {
        User user = new User();
        Car car = new Car("car1",null);
        Car car2 = new Car("car1",car);
        car.setCar(car2);
        user.setCar(car);
        car.setUser(user);
        redisTemplate.opsForValue().set("test",user);
        user = (User) redisTemplate.opsForValue().get("test");
        System.out.println(user.getCar());
    }
}