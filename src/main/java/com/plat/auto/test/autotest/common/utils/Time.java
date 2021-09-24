/*
 * @Author: 丑牛
 * @Date: 2021-09-23 10:15:01
 * @LastEditors: 丑牛
 * @LastEditTime: 2021-09-23 10:15:02
 * @Description: 日期时间格式化
 */
package com.plat.auto.test.autotest.common.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Time {

   
    public   String getDateWithMilss(){
        DateTime dateMills = DateUtil.date(System.currentTimeMillis());
        String dateString = DateUtil.format(dateMills, "yyyy-MM-dd HH:mm:ss.SSS");
        return dateString;
    }
    
    public static  String getDate(){
        String dateString = DateUtil.now();
        return dateString;
    }

    public static void main(String[] args){
        log.info(getDate());
    }
}



