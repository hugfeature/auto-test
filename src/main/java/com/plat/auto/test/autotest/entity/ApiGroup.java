package com.plat.auto.test.autotest.entity;

import lombok.Data;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/4
 * @description
 */
@Data
public class ApiGroup {
    private int id;
    /**
     * 项目ID
     */
    private int projectId;
    /**
     * 分组名称
     */
    private String name;
    /**
     * 分组排序
     */
    private int order;
}
