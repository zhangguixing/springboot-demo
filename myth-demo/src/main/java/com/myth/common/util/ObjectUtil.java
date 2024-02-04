package com.myth.common.util;

import java.util.Collection;
import java.util.Map;

/**
 * Object工具类
 *
 * @author zhangguixing Email:guixingzhang@qq.com
 */
public class ObjectUtil {
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            return ((String) o).trim().length() == 0;
        } else if (o instanceof Collection) {
            return ((Collection) o).isEmpty();
        } else if (o instanceof Map) {
            return ((Map) o).isEmpty();
        } else if (o.getClass().isArray()) {
            return ((Object[]) o).length == 0;
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }
}