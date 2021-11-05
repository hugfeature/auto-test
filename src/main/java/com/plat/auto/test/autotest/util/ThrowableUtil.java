/*
 * @Author: 丑牛
 * @Date: 2021-10-26 16:16:09
 * @LastEditors: 丑牛
 * @LastEditTime: 2021-10-26 16:16:10
 * @Description: 抛出异常处理
 */
package com.plat.auto.test.autotest.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ThrowableUtil {
    public static String toString(Throwable e){
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        String errorMsg = stringWriter.toString();
        return errorMsg;
    }
    
}

