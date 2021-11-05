package com.plat.auto.test.autotest.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/4
 * @description
 */
@Data
public class Document {
    /**
     * 接口ID
     */
    private int id;
    /**
     * 项目ID
     */
    private int projectId;
    /**
     * 分组ID
     */
    private int groupId;
    /**
     * 接口名称
     */
    private String name;
    /**
     * 状态：0-启用、1-维护、2-废弃
     */
    private int status;
    /**
     * 星标等级：0-普通接口、1-一星接口
     */
    private int starLevel;
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
    private String requetHeaders;
    /**
     * Query String Parameters：VO-JSON格式字符串
     */
    private String queryParams;
    /**
     * 响应数据类型ID
     */
    private int responseDataType;
    /**
     * Response Parameters：VO-JSON格式字符串
     */
    private String responseParams;
    /**
     * Response Content-type：成功接口，如JSON、XML、HTML、TEXT
     */
    private String successRespType;
    /**
     * Response Content：成功接口
     */
    private String successRespExample;
    /**
     * Response Content-type：失败接口
     */
    private String failRespType;
    /**
     * Response Content：失败接口
     */
    private String failRespExample;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date addTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
