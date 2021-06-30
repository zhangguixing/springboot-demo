package com.gxzhang.test;

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
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * memcache to redis transfer plus
 *
 * @author zhangguixing Email:zhangguixing@co-mall.com
 * @since 2021-06-30 下午 14:26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class Memcache2RedisTransferPlus {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemcachedClient memcachedClient;

    @Value("${urls}")
    private String urls;

    /**
     * 迁移key列表
     */
//    public static final String KEY_PATTERN = "^(cart#).*$";
//    session设置过期时间,consume中设置过期时间
    public static final String KEY_PATTERN = "^(session#).*$";

    /**
     * 队列
     */
    private BlockingQueue<String> keyQueue = new ArrayBlockingQueue<>(2000);

    /**
     * 结束心跳
     */
    public static final String FINISH_HEARTBEAT = "FINISH_HEARTBEAT";

    /**
     * 消费者 数量
     */
    public static final int CONSUMER_COUNT = Runtime.getRuntime().availableProcessors() * 4;

    public static AtomicInteger count = new AtomicInteger(0);

    @Test
    public void test() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(CONSUMER_COUNT);

        Instant start = Instant.now();
        // 生产
        product();
        // 消费
        consume(countDownLatch);
        // 等待执行结束
        countDownLatch.await();
        System.out.printf("总key数：%d ,总耗时：%ds\n", count.get(), Duration.between(start,Instant.now()).getSeconds());
    }

    /**
     * 生产者
     */
    private void product() {
        // 异步读
        new Thread(() -> {
            String[] urlArr = urls.split(",");
            // 筛选满足条件key
            Arrays.stream(urlArr).parallel().forEach(url -> {
                try {
                    // 读
                    KeyIterator it = memcachedClient.getKeyIterator(AddrUtil.getOneAddress(url));
                    it.setOpTimeout(100000);
                    int count=0;
                    while (it.hasNext()) {
                        String key = it.next();
                        if (Pattern.matches(KEY_PATTERN, key)) {
                            keyQueue.put(key);
                        }
                        if(count>10){
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            try {
                // 发送结束心跳
                for (int i = 0; i < CONSUMER_COUNT; i++) {
                    keyQueue.put(FINISH_HEARTBEAT);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 消费者
     *
     * @param countDownLatch
     */
    private void consume(CountDownLatch countDownLatch) {
        // 异步写
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            new Thread(() -> {
                try {
                    while (true) {
                        String key = keyQueue.take();
                        if (FINISH_HEARTBEAT.equals(key)) {
                            return;
                        }
                        // 购物车迁移
//                        Cart cart = memcachedClient.get(key);
//                        if(cart != null && !CollectionUtils.isEmpty(cart.getBasketMap())){
//                            redisTemplate.opsForValue().set(key, memcachedClient.get(key));
//                        }

                        // session 迁移
                        redisTemplate.opsForValue().set(key, memcachedClient.get(key),2, TimeUnit.MINUTES);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            }).start();
        }
    }

}