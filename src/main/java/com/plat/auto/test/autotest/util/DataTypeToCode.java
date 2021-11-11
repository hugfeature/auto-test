package com.plat.auto.test.autotest.util;

import com.plat.auto.test.autotest.entity.DataType;
import com.plat.auto.test.autotest.entity.DataTypeField;

import java.util.*;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/2
 * @description
 */
public class DataTypeToCode {
    /**
     *  首字母大写
     * @param str 传入的字符串
     * @return 传入字符串首字母变为大写
     */
    public static String upFirst(String str){
        char[] chars = str.toCharArray();
        char lowCharFirst = 'a';
        char lowCharEnd = 'z';
        if (chars[0] >= lowCharFirst && chars[0] <= lowCharEnd){
            chars[0] = (char)(chars[0] - 32);
        }
        return new String(chars);
    }

    /**
     *  返回java数据类型
     * @param paramDataType 传入参数类型
     * @return paramDataType 匹配后的java数据类型
     */
    public static String matchJavaType(String paramDataType) {
        switch (paramDataType){
            case "string":
                return "String";
            case "boolean":
                return "boolean";
            case "short":
                return "short";
            case "int":
                return "int";
            case "long":
                return "long";
            case "float":
                return "float";
            case "double":
                return "double";
            case "date":
            case "datetime":
                return "Date";
            case "byte":
                return "byte";
            default:
                return paramDataType;
        }

    }
    public static String parseDataTypeToCode(DataType dataType){
        StringBuilder stringBuilder= new StringBuilder();
        // package
        stringBuilder.append("\r\n");
        // import
//        Set<String> importSet = new HashSet<>();
        if (dataType.getFieldList() != null && dataType.getFieldList().size() > 0){
            for (DataTypeField field: dataType.getFieldList()){
                String fieldTypeImportItem = field.getFieldDataType().getName();
                if ("date".equalsIgnoreCase(fieldTypeImportItem)) {
                    String importItem = "import java.util.Date;";
//                    importSet.add(importItem);
                    stringBuilder.append(importItem).append("\r\n");
                }
                if (field.getFieldType() == 1) {
                    String importItem = "import java.util.List;";
//                    importSet.add(importItem);
                    stringBuilder.append(importItem).append("\r\n");
                }
            }
            String importLombok =  "import lombok.Data";
            stringBuilder.append(importLombok).append("\r\n");
            stringBuilder.append("\r\n");
        }
        // 类注释
        stringBuilder.append("/**\r\n");
        stringBuilder.append("*\t").append(dataType.getAbout()).append("\r\n");
        stringBuilder.append("*\r\n");
        stringBuilder.append("*\tCreated by wzx on ").append(DateUtils.formatDate(new Date())).append(".\r\n");
        stringBuilder.append("*/ \r\n");

        // 实体部分
        stringBuilder.append("@Data").append("\r\n");
        stringBuilder.append("public class ").append(upFirst(dataType.getName())).append("Response {");
        stringBuilder.append("\r\n\r\n");
        // field
        if (dataType.getFieldList()!=null && dataType.getFieldList().size()>0) {
            for (DataTypeField field: dataType.getFieldList()) {
                String fieldTypeItem = matchJavaType(field.getFieldDataType().getName());
                String fieldNameItem = field.getFieldName();
                if (field.getFieldType() == 1) {
                    fieldTypeItem = "List<"+ fieldTypeItem +">";
                }
                stringBuilder.append("\tprivate ").append(fieldTypeItem).append(" ").append(fieldNameItem).append(";\r\n");

            }
            stringBuilder.append("\r\n");
        }
        stringBuilder.append("}\r\n");
        return stringBuilder.toString();
    }


    public static void main(String[] args){
        DataType stringType = new DataType();
        stringType.setName("String");
        stringType.setAbout("字符串类型");

        List<DataTypeField> fieldList = new ArrayList<>();
        DataTypeField field1 = new DataTypeField();
        field1.setFieldName("name");
        field1.setFieldAbout("姓名");
        field1.setFieldType(0);
        field1.setFieldDataType(stringType);
        fieldList.add(field1);

        DataTypeField field2 = new DataTypeField();
        field2.setFieldName("otherNames");
        field2.setFieldAbout("其他别名");
        field2.setFieldType(1);
        field2.setFieldDataType(stringType);
        fieldList.add(field2);

        DataType dataTypeDTO = new DataType();
        dataTypeDTO.setName("User");
        dataTypeDTO.setAbout("用户信息");
        dataTypeDTO.setFieldList(fieldList);

        String content = parseDataTypeToCode(dataTypeDTO);
        System.out.println(content);
    }


}
