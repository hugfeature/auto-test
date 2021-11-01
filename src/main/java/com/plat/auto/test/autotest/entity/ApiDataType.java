package com.plat.auto.test.autotest.entity;

import lombok.Data;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/1
 * @description
 */
@Data
public class ApiDataType {
    private  int id;
    /**
     * 数据类型名称
     */
    private  String name;
    /**
     * 数据类型描述
     */
    private  String about;
    /**
     * 业务线ID，为0表示公共
     */
    private  int bizId;
}
