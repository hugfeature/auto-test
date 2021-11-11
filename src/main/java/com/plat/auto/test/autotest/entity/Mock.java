package com.plat.auto.test.autotest.entity;

import lombok.Data;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/4
 * @description
 */
@Data
public class Mock {
    private int id;
    /**
     * 接口ID
     */
    private int documentId;
    private String uuid;
    /**
     * Response Content-type：如JSON、XML、HTML、TEXT、JSONP
     */
    private String respType;
    /**
     * Response Content
     */
    private String respExample;
}
