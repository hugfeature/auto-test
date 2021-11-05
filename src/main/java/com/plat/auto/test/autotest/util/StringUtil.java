/*
 * @Author: 丑牛
 * @Date: 2021-10-26 17:07:16
 * @LastEditors: 丑牛
 * @LastEditTime: 2021-10-26 17:07:17
 * @Description: ①final修饰的基本数据类型的值是不能够改变的；
 *               ② 字符串工具类
 */
package com.plat.auto.test.autotest.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {
    public static final String EMPTY = "";
    public static boolean isBlank(final String string){
        return string == null || string.trim().length() == 0;
    }
    
    public static boolean isNotBlank(final String string){
        return !isBlank(string);
    }

    public static String[] split (final String string , final String separatorChars){
        if (isBlank(string)) {
            return null;
        }
        if (isBlank(separatorChars)) {
            return new String[]{string.trim()};
        }
        List<String> list = new ArrayList<>();
        for (String item : string.split(separatorChars)) {
            if (isNotBlank(item)) {
                list.add(item.trim());
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public static String join (final String[] strings, String separator){
        if (strings == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (i > 0) {
                stringBuilder.append(separator);
            }
            if (strings[i] != null) {
                stringBuilder.append(strings[i]);
            }
        }
        return stringBuilder.toString();
    }
    
}
