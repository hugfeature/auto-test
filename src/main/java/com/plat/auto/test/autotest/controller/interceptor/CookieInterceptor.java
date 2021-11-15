package com.plat.auto.test.autotest.controller.interceptor;

import com.plat.auto.test.autotest.util.ArrayUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/11
 * @description push cookies to model as cookieMap
 */
@Component
public class CookieInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object hadler,
                           ModelAndView modelAndView) throws Exception{
        if (modelAndView != null && ArrayUtil.isNotEmpt(request.getCookies())){
            HashMap<String, Cookie> cookieHashMap = new HashMap<String, Cookie>();
            for (Cookie cookie: request.getCookies()
                 ) {
                cookieHashMap.put(cookie.getName(), cookie);
            }
            modelAndView.addObject("cookieHashMap", cookieHashMap);
        }
        HandlerInterceptor.super.postHandle(request, response, hadler, modelAndView);
    }
}
