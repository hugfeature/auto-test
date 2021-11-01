package com.plat.auto.test.autotest.entity;

import lombok.Data;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/1
 * @description
 */
@Data
public class ApiDataTypeToFiled {
    private int id;
    /**
     *
     */
    private int parentDataTypeId;
    private String filedName;
    private String filedAbout;

}
