package com.plat.auto.test.autotest.controller.excetionCommon;

import com.plat.auto.test.autotest.entity.ReturnT;
import com.plat.auto.test.autotest.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/13
 * @description
 */
@Component
@Slf4j
public class WebException implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handle, Exception e) {
        log.error("ExceptionResolver:{}", e);
        // if is json
        boolean isJson = false;
        HandlerMethod method = (HandlerMethod) handle;
        ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
        if (responseBody != null) {
            isJson = true;
        }

        // result
        ReturnT<String> errorResult = new ReturnT<String>(ReturnT.FAIL.getCode(),
                e.toString().replaceAll("\n", "<br/>"));
        //reponse
        ModelAndView modelAndView = new ModelAndView();
        if (isJson) {
            try {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().print(JacksonUtil.writeValueAsString(errorResult));
            } catch (IOException ioException) {
                log.error(ioException.getMessage(), ioException);
            }
            return modelAndView;
        } else {
            modelAndView.addObject("exceptionMsg", errorResult.getMsg());
            modelAndView.setViewName("/common/common.exception");
            return modelAndView;
        }

    }
}
