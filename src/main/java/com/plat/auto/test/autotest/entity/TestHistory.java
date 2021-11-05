package com.plat.auto.test.autotest.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/4
 * @description
 */
@Data
public class TestHistory {
    private int id;
    /**
     * 接口ID
     */
    private int doumentId;
    /**
     * 创建时间
     */
    private Date addTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * Request URL：相对地址
     */
    private String requestUrl;
    /**
     * Request Method：如POST、GET
     */
    private String requestMethod;
    /**
     * Request Headers：Map-JSON格式字符串
     */
    private String requestHeaders;
    /**
     * Query String Parameters：VO-JSON格式字符串
     */
    private String queryParams;
    /**
     * Response Content-type
     */
    private String respType;;
}
