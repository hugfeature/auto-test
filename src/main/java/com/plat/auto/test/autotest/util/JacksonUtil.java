package com.plat.auto.test.autotest.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wangzhaoxian
 * @date: 2021/10/29
 * @description
 */
@Slf4j
public class JacksonUtil {
    private final  static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public  static  ObjectMapper getInstance(){
        return OBJECT_MAPPER;
    }

    public static  String writeValueAsString(Object object){
        try {
            return  getInstance().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static <T> T readValue(String jsonStr, Class<T> tClass){
        try{
            return getInstance().readValue(jsonStr, tClass);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }
    public  static <T> T readValueRefer(String jsonStr, TypeReference typeReference){
        try{
            return (T) getInstance().readValue(jsonStr, typeReference);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static void main(String[] args){
        try{
            Map<String, String> map = new HashMap<String, String>();
            map.put("aaaa", "aaa");
            map.put("1111", "222");
            String json = writeValueAsString(map);
            System.out.println(json);
            System.out.println(readValue(json, Map.class));
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
