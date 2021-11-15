package com.plat.auto.test.autotest.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/10
 * @description 权限控制
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermessionLimit {
    /**
     * @Description: 要求用户登录
     * @Author: wangzhaoxian
     * @Date: 2021/11/10
     */
    boolean limit() default true;
    /**
     * @Description: 要求管理员权限
     * @Author: wangzhaoxian
     * @Date: 2021/11/10
     */
    boolean superUser() default false;
}
