package com.netease.service;

import com.netease.model.User;
import com.netease.vo.Response;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    Response doLogin(String username, String password, HttpServletRequest request);

    User getByUsernameAndPassword(String username, String password);
}
