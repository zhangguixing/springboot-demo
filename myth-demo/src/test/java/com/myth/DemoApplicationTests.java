package com.myth;

import com.myth.model.A;
import com.myth.model.C;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private Environment environment;

    @Test
    void contextLoads() {
        System.out.println(environment.getProperty("spring.redis.redisson.config"));
        System.out.println(environment.getProperty("spring.rabbitmq.host"));
        System.out.println(environment.getProperty("spring.datasource.username"));
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 递归引用案例
     */
//    @Test
    public void testRedis() {
        A<C> a = new A<>();
        a.setContent("testaa");
//        B b = new B();
//        a.setB(b);
//        b.setA(a);
        C c = new C();
        c.setName("zzz");
        a.setList(Arrays.asList(c));
        List<A<C>> list = new ArrayList<>();
        list.add(a);
        redisTemplate.opsForValue().set("test-a", list);
        list = (List<A<C>>) redisTemplate.opsForValue().get("test-a");
        System.out.println(list);
    }
}
