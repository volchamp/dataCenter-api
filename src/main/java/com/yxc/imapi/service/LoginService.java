package com.yxc.imapi.service;

import com.jfinal.plugin.activerecord.Record;
import com.yxc.imapi.model.Message;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.model.login.Login;

import java.util.List;

public interface LoginService {
    List<Users> login(Login login);
}
