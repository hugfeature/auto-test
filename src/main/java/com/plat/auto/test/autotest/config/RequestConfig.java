package com.plat.auto.test.autotest.config;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/5
 * @description
 */
public class RequestConfig {
    /***
     * @Description: RequestMethod
     * @Date: 2021/11/5
     */
    public enum RequestMethodEnum {
        // RequestMethod
        POST, GET, PUT, DELETE, HEAD, OPTIONS, PATCH;
    }

    /***
     * @Description: requestHeaders
     * @Date: 2021/11/5
     */
    public static List<String> requestHeadersEnum = new LinkedList<String>();

    static {
        requestHeadersEnum.add("Accept");
        requestHeadersEnum.add("Accept-Charset");
        requestHeadersEnum.add("Accept-Encoding");
        requestHeadersEnum.add("Accept-Language");
        requestHeadersEnum.add("Accept-Ranges");
        requestHeadersEnum.add("Authorization");
        requestHeadersEnum.add("Cache-Control");
        requestHeadersEnum.add("Connection");
        requestHeadersEnum.add("Cookie");
        requestHeadersEnum.add("Content-Length");
        requestHeadersEnum.add("Content-Type");
        requestHeadersEnum.add("Date");
        requestHeadersEnum.add("Expect");
        requestHeadersEnum.add("From");
        requestHeadersEnum.add("Host");
        requestHeadersEnum.add("If-Match");
        requestHeadersEnum.add("If-Modified-Since");
        requestHeadersEnum.add("If-None-Match");
        requestHeadersEnum.add("If-Range");
        requestHeadersEnum.add("If-Unmodified-Since");
        requestHeadersEnum.add("Max-Forwards");
        requestHeadersEnum.add("Pragma");
        requestHeadersEnum.add("Proxy-Authorization");
        requestHeadersEnum.add("Range");
        requestHeadersEnum.add("Referer");
        requestHeadersEnum.add("TE");
        requestHeadersEnum.add("Upgrade");
        requestHeadersEnum.add("User-Agent");
        requestHeadersEnum.add("Via");
        requestHeadersEnum.add("Warning");
    }

    /***
     * @Description: query param type
     * @Author: wangzhaoxian
     * @Date: 2021/11/5
     */
    public enum QueryParamTypeEnum {
        // query param type
        STRING("string"),
        BOOLEAN("boolean"),
        SHORT("short"),
        INT("int"),
        LONG("long"),
        FLOAT("float"),
        DOUBLE("double"),
        DATE("date"),
        DATETIME("datetime"),
        JSON("json"),
        BYTE("byte");
        public String title;

        QueryParamTypeEnum(String title) {
            this.title = title;
        }
    }

    /**
     * @Description: QueryParam
     * @Author: wangzhaoxian
     * @Date: 2021/11/6
     */
    @Data
    public static class QueryParam {
        private boolean notNull;
        private String name;
        private String type;
        private String desc;

        public boolean isNotNull() {
            return notNull;
        }
    }

    public enum ResponseContentType {
        // ResponseContentType
        JSON("application/json;charset=UTF-8"),
        XML("text/xml"),
        HTML("text/html;"),
        TEXT("text/plain"),
        JSONP("application/javascript");
        public final String type;

        ResponseContentType(String type) {
            this.type = type;
        }

        public static ResponseContentType match(String name) {
            if (name != null) {
                for (ResponseContentType item : ResponseContentType.values()) {
                    if (name.equals(item.name())) {
                        return item;
                    }
                }
            }
            return null;
        }
    }

}
