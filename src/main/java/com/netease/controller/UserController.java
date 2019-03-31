package com.netease.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netease.service.UserService;
import com.netease.util.JsonUtils;
import com.netease.vo.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(HttpServletRequest request) throws JsonProcessingException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Response response = userService.doLogin(username, password, request);
        return JsonUtils.obj2String(response);
    }

    //将login放到WEB-INF路径下客户端不能直接使用a访问，需要服务器重定向
    @RequestMapping("/toLogin")
    public ModelAndView getLoginPage() {
        ModelAndView view = new ModelAndView("login");
        return view;
    }

    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        request.getSession().invalidate();
        ModelAndView view = new ModelAndView("login");
        return view;
    }
}
