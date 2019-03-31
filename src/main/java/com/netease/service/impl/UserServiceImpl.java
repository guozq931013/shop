package com.netease.service.impl;

import com.netease.constant.ResponseCode;
import com.netease.constant.ResponseMsg;
import com.netease.dao.UserMapper;
import com.netease.model.User;
import com.netease.service.UserService;
import com.netease.vo.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;


    @Override
    public Response doLogin(String username, String password, HttpServletRequest request) {

        Response result = new Response();
        if (username == null || username.trim().equals("")) {
            result.setCode(ResponseCode.LOGIN_USERNAME_EMPTY);
            result.setMessage(ResponseMsg.USERNAME_IS_EMPTY);
            return result;
        }

        if (password == null || password.trim().equals("")) {
            result.setCode(ResponseCode.LOGIN_PASSWORD_EMPTY);
            result.setMessage(ResponseMsg.PASSWORD_IS_EMPTY);
            return result;
        }

        User user=getByUsernameAndPassword(username,password);
        if(user == null){
            result.setCode(ResponseCode.LOGIN_FAILED_CODE);
            result.setMessage(ResponseMsg.USERNAME_OR_PWD_ERROR);
        }else{
            request.getSession().setAttribute("user",user);
            result.setCode(ResponseCode.LOGIN_SUCCESS_CODE);
            result.setMessage(ResponseMsg.LOGIN_SUCCESS_MESSAGE);
        }

        return result;
    }

    @Override
    public User getByUsernameAndPassword(String username, String password) {
        return userMapper.selectByUsernameAndPassword(username, password);
    }
}
