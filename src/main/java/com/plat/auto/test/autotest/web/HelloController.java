/*
 * @Author: 丑牛
 * @Date: 2021-09-22 15:43:59
 * @LastEditors: 丑牛
 * @LastEditTime: 2021-09-22 15:43:59
 * @Description: file content
 */
package com.plat.auto.test.autotest.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/")
    public String index() {
        return "hello spring";
    }
    
}

