package com.myth.common.util;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简单对象转换工具类
 *
 * @author zhangguixing Email:guixingzhang@qq.com
 */
public final class BeanConverter {

    public static <T, S> T to(S source, Class<T> t) {
        T target = null;
        if (source == null) {
            return null;
        }
        try {
            target = t.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
        return target;
    }


    public static <T, S> List<T> to(List<S> source, Class<T> t) {

        if (source == null)
            return null;

        List<T> targets = new ArrayList<>(source.size());
        for (S s : source) {
            targets.add(to(s, t));
        }
        return targets;
    }

    /**
     * 拷贝map对象value值
     *
     * @param source
     * @param t
     * @param <K>    key   键类型
     * @param <VT>   value 目标值类型
     * @param <VS>   value 源值类型
     * @return
     */
    public static <K, VT, VS> Map<K, VT> to(Map<K, VS> source, Class<VT> t) {

        if (source == null)
            return null;

        Map<K, VT> targets = new HashMap<>(source.size());

        for (Map.Entry<K, VS> entry : source.entrySet()) {
            targets.put(entry.getKey(), to(entry.getValue(), t));
        }

        return targets;
    }
}