package com.yxc.imapi.service.Impl;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yxc.imapi.model.Message;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.model.login.Login;
import com.yxc.imapi.service.ChatService;
import com.yxc.imapi.service.LoginService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {


    @Override
    public List<Users> login(Login login) {
        String sql="select * from users where user_id='"+login.getUser_id()+"' and user_password='"+login.getUser_password()+"' and state<>0";
        return Users.dao.find(sql);
    }
}
