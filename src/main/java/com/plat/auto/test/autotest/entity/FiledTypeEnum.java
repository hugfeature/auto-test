package com.plat.auto.test.autotest.entity;

/**
 *   文本类型的枚举
 * @author wangzhaoxian
 */
public enum FiledTypeEnum {
    // 定义文本类型枚举
    DEFAULT(0, "默认"),
    ARRAY(1, "数组");

    private int value;
    private String title;
    private FiledTypeEnum(int value, String title){
        this.value = value;
        this.title =title;
    }

    public int getValue(){
        return value;
    }

    public String getTitle(){
        return title;
    }

    public  static FiledTypeEnum match(int value){
        for (FiledTypeEnum item : FiledTypeEnum.values()){
            if (item.getValue() == value){
                return item;
            }
        }
        return null;
    }
}
