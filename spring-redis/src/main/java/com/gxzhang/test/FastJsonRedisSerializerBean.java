package com.gxzhang.test;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;

/**
 * FastJsonRedisSerializer定义
 *
 * @author zhangguixing Email:zhangguixing@co-mall.com
 * @since 2021-05-24 下午 14:26
 */
public class FastJsonRedisSerializerBean extends FastJsonRedisSerializer {
    public FastJsonRedisSerializerBean(Class type) {
        super(type);
    }
}