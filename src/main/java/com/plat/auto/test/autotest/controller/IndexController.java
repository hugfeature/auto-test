package com.plat.auto.test.autotest.controller;

import com.plat.auto.test.autotest.controller.annotation.PermessionLimit;
import com.plat.auto.test.autotest.entity.ReturnT;
import com.plat.auto.test.autotest.entity.User;
import com.plat.auto.test.autotest.service.LoginService;
import com.plat.auto.test.autotest.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/15
 * @description
 */
@Controller
@Api(tags = "1.0.0-SNAPSHOT", value = "初始登录接口")
@Slf4j
public class IndexController {
    @Resource
    private LoginService loginService;

    @RequestMapping("/")
    @PermessionLimit(limit = false)
    @ApiOperation(value = "初始接口", notes = "初始化")
    public String index(Model model, HttpServletRequest request) {
        User loginUser = loginService.ifLogin(request);
        if (loginUser == null) {
            return "redirect:/toLogin";
        }
        return "redirect:/project";
    }

    @RequestMapping("/toLogin")
    @PermessionLimit(limit = false)
    @ApiOperation(value = "去登录", notes = "去登录")
    public String toLogin(Model model, HttpServletRequest request) {
        User loginUser = loginService.ifLogin(request);
        if (loginUser != null) {
            return "redirect:/";
        }
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    @PermessionLimit(limit = false)
    @ApiOperation(value = "登录操作", notes = "登录操作")
    public ReturnT<String> loginDo(HttpServletRequest request, HttpServletResponse response, String ifRemember,
                                   String userName, String passWord) {
        boolean ifRem = false;
        if (StringUtil.isNotBlank(ifRemember) && "on".equals(ifRemember)) {
            ifRem = true;
        }
        ReturnT<String> loginRet = loginService.login(response, userName, passWord, ifRem);
        return loginRet;
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    @PermessionLimit(limit = false)
    @ApiOperation(value = "退出登录", notes = "退出登录")
    public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response) {
        loginService.logout(request, response);
        return ReturnT.SUCCESS;
    }

    @RequestMapping("/help")
    @ResponseBody
    @PermessionLimit(limit = false)
    @ApiOperation(value = "帮助", notes = "帮助")
    public String help() {
        return "help";
    }


}
