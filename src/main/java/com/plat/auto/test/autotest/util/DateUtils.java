
package com.plat.auto.test.autotest.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/*
 *  @Author: 丑牛
 *  @Date: 2021-09-23 10:15:01
 *  @LastEditors: 丑牛
 *  @LastEditTime: 2021-09-23 10:15:02
 *  @Description: 日期时间格式化 final修饰的基本数据类型的值是不能够改变的
 */
@Slf4j
public class DateUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static ThreadLocal<Map<String, DateFormat>> dateFormatThreadLocal = new ThreadLocal<Map<String, DateFormat>>();

    /***
     * @Description: getDateFormat
     * @Param: [pattern]
     * @return: java.text.DateFormat
     * @Author:
     * @Date: 2021/11/5
     */
    private static DateFormat getDateFormat(String pattern) {
        if (pattern == null || pattern.trim().length() == 0) {
            throw new IllegalArgumentException("pattern cannot be empty.");
        }

        Map<String, DateFormat> dateFormatMap = dateFormatThreadLocal.get();
        if (dateFormatMap != null && dateFormatMap.containsKey(pattern)) {
            return dateFormatMap.get(pattern);
        }

        synchronized (dateFormatThreadLocal) {
            if (dateFormatMap == null) {
                dateFormatMap = new HashMap<String, DateFormat>();
            }
            dateFormatMap.put(pattern, new SimpleDateFormat(pattern));
            dateFormatThreadLocal.set(dateFormatMap);
        }
        dateFormatThreadLocal.remove();
        return dateFormatMap.get(pattern);
    }

    /**
     * @Description: formatDate
     * @Param: [date]
     * @return: java.lang.String
     * @Author:
     * @Date: 2021/11/5
     */
    public static String formatDate(Date date) {
        return format(date, DATE_FORMAT);
    }

    /**
     * @Description: formatDateTime
     * @Param: [date]
     * @return: java.lang.String
     * @Author:
     * @Date: 2021/11/5
     */
    public static String formatDateTime(Date date) {
        return format(date, DATETIME_FORMAT);
    }

    /**
     * @Description: format
     * @Param: [date, patten]
     * @return: java.lang.String
     * @Author:
     * @Date: 2021/11/5
     */
    public static String format(Date date, String patten) {
        return getDateFormat(patten).format(date);
    }

    /**
     * @Description: parseDate
     * @Param: [dateString]
     * @return: java.util.Date
     * @Author:
     * @Date: 2021/11/5
     */
    public static Date parseDate(String dateString) {
        return parse(dateString, DATE_FORMAT);
    }

    /**
     * @Description: parseDateTime
     * @Param: [dateString]
     * @return: java.util.Date
     * @Author:
     * @Date: 2021/11/5
     */
    public static Date parseDateTime(String dateString) {
        return parse(dateString, DATETIME_FORMAT);
    }

    /**
     * @Description: parse
     * @Param: [dateString, pattern]
     * @return: java.util.Date
     * @Author:
     * @Date: 2021/11/5
     */
    public static Date parse(String dateString, String pattern) {
        try {
            Date date = getDateFormat(pattern).parse(dateString);
            return date;
        } catch (Exception e) {
            log.warn("parse date error, dateString = {}, pattern={}; errorMsg = ", dateString, pattern, e.getMessage());
            return null;
        }
    }


    // ---------------------- add date ----------------------

    /**
     * @Description: addDays
     * @Param: [date, amount]
     * @return: java.util.Date
     * @Author:
     * @Date: 2021/11/5
     */
    public static Date addDays(final Date date, final int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    /**
     * @Description: addYears
     * @Param: [date, amount]
     * @return: java.util.Date
     * @Author:
     * @Date: 2021/11/5
     */
    public static Date addYears(final Date date, final int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    /**
     * @Description: addMonths
     * @Param: [date, amount]
     * @return: java.util.Date
     * @Author:
     * @Date: 2021/11/5
     */
    public static Date addMonths(final Date date, final int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    /**
     * @Description: add
     * @Param: [date, calendarField, amount]
     * @return: java.util.Date
     * @Author:
     * @Date: 2021/11/5
     */
    private static Date add(final Date date, final int calendarField, final int amount) {
        if (date == null) {
            return null;
        }
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

}



