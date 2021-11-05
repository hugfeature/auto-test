package com.plat.auto.test.autotest.entity;

import lombok.Data;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/3
 * @description
 */
@Data
public class ApiUser {
    /**
     * 用户ID
     */
    private int id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String passWord;
    /**
     * 用户类型：0-普通用户，1-管理员
     */
    private int type;
    /**
     * 业务线权限，使用逗号分割
     */
    private String permissionBiz;
}
