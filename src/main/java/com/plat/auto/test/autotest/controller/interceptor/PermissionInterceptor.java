package com.plat.auto.test.autotest.controller.interceptor;

import com.plat.auto.test.autotest.controller.annotation.PermessionLimit;
import com.plat.auto.test.autotest.entity.User;
import com.plat.auto.test.autotest.service.LoginService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/11
 * @description 权限拦截
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {
    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
        // if need login
        boolean needLogin = true;
        boolean needAdminUser = false;
        HandlerMethod method = (HandlerMethod) handler;
        PermessionLimit permessionLimit = method.getMethodAnnotation(PermessionLimit.class);
        if (permessionLimit != null) {
            needLogin = permessionLimit.limit();
            needAdminUser = permessionLimit.superUser();
        }
        // if pass
        if (needLogin) {
            User loginUser = loginService.ifLogin(request);
            if (loginUser == null) {
                response.sendRedirect(request.getContextPath() + "/toLogin");
                return false;
            }
            if (needAdminUser && loginUser.getType() != 1) {
                throw new RuntimeException("权限不足");
            }
            request.setAttribute(LoginService.LOGIN_IDENTITY, loginUser);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
