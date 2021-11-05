package com.plat.auto.test.autotest.entity;

import lombok.Data;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/5
 * @description
 */
@Data
public class Biz {
    private int id;
    /**
     * 业务名称
     */
    private String bizName;
    private int order;
}
