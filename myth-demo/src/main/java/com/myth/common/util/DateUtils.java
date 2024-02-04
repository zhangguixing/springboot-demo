package com.myth.common.util;


import com.myth.common.constant.GlobalConstants;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期转换工具类
 *
 * @author zhangguixing Email:guixingzhang@qq.com
 */
public class DateUtils {
    public static final String FULL = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String SIMPLE_DATETIME = "yyyyMMddHHmmss";
    public static final String DATE = "yyyy-MM-dd";
    public static final String SIMPLE_DATE = "yyyyMMdd";
    public static final String TIME = "HH:mm:ss";
    public static final String FULL_TIME = "HHmmssSSS";
    public static final String YEAR = "yyyy";
    public static final String FULL_DATETIME = "yyyyMMddHHmmssSSS";

    private static final Map<String, ThreadLocal<DateFormat>> dfMap = new HashMap<>();

    public static String format(long date, String pattern) {
        return getDateFormat(pattern).format(new Date(date));
    }

    public static String format(Date date, String pattern) {
        if (date == null) {
            return GlobalConstants.EMPTY_STR;
        }
        return getDateFormat(pattern).format(date);
    }

    public static String formatToString(String date, String pattern) {
        if (!StringUtils.hasText(date)) {
            return GlobalConstants.EMPTY_STR;
        }
        DateFormat dateFormat = getDateFormat(pattern);
        try {
            Date parse = dateFormat.parse(date);
            return dateFormat.format(parse);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String format(Timestamp timestamp, String pattern) {
        if (timestamp == null) {
            return "";
        }
        Date date = new Date(timestamp.getTime());
        return format(date, pattern);
    }

    public static Date parse(String dateValue, String pattern) {
        if (!StringUtils.hasText(dateValue)) {
            return null;
        }
        try {
            return getDateFormat(pattern).parse(dateValue);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getTimeNowWith(String pattern) {
        return format(currentDate(), pattern);
    }

    public static String convertPattern(String dateValue, String from, String to) {
        if (!StringUtils.hasText(dateValue)) {
            return null;
        }
        Date date = parse(dateValue, from);
        if (date == null) {
            return null;
        }
        return format(date, to);
    }

    private static Date currentDate() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 通过指定的格式获取SimpleDateFormat类的实例，考虑线程安全
     *
     * @param pattern 时间格式
     * @return SimpleDateFormat类的实例
     */
    private static DateFormat getDateFormat(final String pattern) {
        ThreadLocal<DateFormat> dfThreadLocal = dfMap.get(pattern);
        if (dfThreadLocal == null) {
            synchronized (DateUtils.class) {
                dfThreadLocal = new ThreadLocal<DateFormat>() {
                    @Override
                    protected DateFormat initialValue() {
                        return new SimpleDateFormat(pattern);
                    }
                };
                dfMap.put(pattern, dfThreadLocal);
            }
        }
        return dfThreadLocal.get();
    }
}

