package com.gxzhang.test;

import com.alibaba.fastjson.JSON;
import net.rubyeye.xmemcached.KeyIterator;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * memcache to redis transfer
 *
 * @author zhangguixing Email:zhangguixing@co-mall.com
 * @since 2021-06-27 下午 16:26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class Memcache2RedisTransfer {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemcachedClient memcachedClient;

    @Value("${urls}")
    private String urls;

    @Test
    public void test() throws Exception {
        String[] urlArr = urls.split(",");
        CountDownLatch countDownLatch = new CountDownLatch(urlArr.length);
        Instant start = Instant.now();
        Arrays.stream(urlArr).parallel().forEach(url -> {
            List<String> keyList = new ArrayList<>();
            try {
                // 读
                KeyIterator it = memcachedClient.getKeyIterator(AddrUtil.getOneAddress(url));
                it.setOpTimeout(100000);
                while (it.hasNext()) {
                    String key = it.next();
                    if (key.startsWith("cart#")) {
                        keyList.add(key);
                        System.out.println(url + ":" + key);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 异步写
            List<String> finalKeyList = keyList;
            new Thread(() -> {
                finalKeyList.parallelStream().forEach(key -> {
                    try {
//                        System.out.println(JSON.toJSONString(memcachedClient.get(key)));
                        redisTemplate.opsForValue().set(key, memcachedClient.get(key));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                countDownLatch.countDown();
            }).start();
        });
        countDownLatch.await();
        System.out.printf("总耗时 %d s\n", Duration.between(start, Instant.now()).getSeconds());
    }


}