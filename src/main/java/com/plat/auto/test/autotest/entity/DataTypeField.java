package com.plat.auto.test.autotest.entity;

import lombok.Data;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/1
 * @description
 */
@Data
public class DataTypeField {
    private int id;
    /**
     * 所属数据类型ID
     */
    private int parentDataTypeId;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段描述
     */
    private String fieldAbout;
    /**
     * 字段数据类型
     */
    private  int fieldDataTypeId;
    /**
     * 字段形式：0=默认、1=数组   @see FieldTypeEnum
     */
    private  int fieldType;
    /**
     * fieldDatatype dto
     */
    private DataType fieldDataType;
}
