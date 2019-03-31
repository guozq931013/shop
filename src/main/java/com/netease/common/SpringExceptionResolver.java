package com.netease.common;

import com.netease.exception.ParamException;
import com.netease.exception.PermissionException;
import com.netease.vo.Response;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SpringExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {

        ModelAndView modelAndView;

        String defaultEcpMsg = "系统异常，请稍后访问";

        if (e instanceof PermissionException || e instanceof ParamException) {
            modelAndView = new ModelAndView("exception", Response.error(e.getMessage()).toMap());
        } else {
            modelAndView = new ModelAndView("exception", Response.error(defaultEcpMsg).toMap());
        }

        return modelAndView;
    }
}