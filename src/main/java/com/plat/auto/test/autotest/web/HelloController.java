/*
 * @Author: 丑牛
 * @Date: 2021-09-22 15:43:59
 * @LastEditors: 丑牛
 * @LastEditTime: 2021-09-22 15:43:59
 * @Description: file content
 */
package com.plat.auto.test.autotest.web;

import com.battcn.boot.swagger.model.DataType;
import com.battcn.boot.swagger.model.ParamType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@RestController
@Api(tags = "1.0.0-SNAPSHOT", description = "用户管理", value = "用户管理")
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    @ApiOperation(value = "huoquhello", notes = "备注")
    @ApiImplicitParams({@ApiImplicitParam(name = "who", value = "用户名", dataType = DataType.STRING, paramType = ParamType.QUERY, defaultValue = "xxx")})
    public String index(@RequestParam(required = false, name = "who")  String who) {
        log.info("输入的用户名为{}" , who);
        if (StrUtil.isBlank(who)) {
            who = "World";
        }
        return StrUtil.format("Hello, {}!", who);
    }
    
}

