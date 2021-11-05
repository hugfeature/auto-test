package com.plat.auto.test.autotest.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/5
 * @description
 */
@Data
public class ReturnT<T> implements Serializable {
    public static final long SERIALVERSIONUID = 42L;
    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;
    public static final ReturnT<String> SUCCESS = new ReturnT<>(null);
    public static final ReturnT<String> FAIL = new ReturnT<>(FAIL_CODE, null);
    private int code;
    private String msg;
    private T content;
    public ReturnT(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
    public ReturnT(T content){
        this.code = SUCCESS_CODE;
        this.content = content;
    }
    @Override
    public String toString(){
        return "ReturnT [code=" + code + ", msg=" + msg + ", content=" + content + "]";
    }
}
