package com.plat.auto.test.autotest.entity;

import lombok.Data;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/4
 * @description
 */
@Data
public class Project {

    /**
     * 项目ID
     */
    private int id;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 项目描述
     */
    private String desc;
    /**
     * 根地址(线上)
     */
    private String baseProjectUrl;
    /**
     * 根地址(预发布)
     */
    private String basePpeUrl;
    /**
     * 根地址(测试)
     */
    private String baseQaUrl;
    /**
     * 业务ID
     */
    private int bizId;
}
