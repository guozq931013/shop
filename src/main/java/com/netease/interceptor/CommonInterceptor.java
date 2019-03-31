package com.netease.interceptor;


import com.netease.model.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            response.sendRedirect("/login.jsp");
            return false;
        }

        String requestURI = request.getRequestURI();
        boolean isUriContainBoughtGoods = requestURI.contains("/goods/bought");
        boolean isUriContainOrder = requestURI.contains("/order");
        boolean isUriContainCart = requestURI.contains("/cart");
        if ((isUriContainBoughtGoods || isUriContainOrder || isUriContainCart) && user.getUsertype() == 0){
            return true;
        }

        boolean isUriContainGoods = requestURI.contains("/goods");
        if (isUriContainGoods && !isUriContainBoughtGoods && user.getUsertype() == 1) {
            return true;
        }

        response.sendRedirect("/login.jsp");

        return true;
    }
}
