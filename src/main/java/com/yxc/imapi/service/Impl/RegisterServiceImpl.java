package com.yxc.imapi.service.Impl;

import com.yxc.imapi.model.Permission;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.model.login.Login;
import com.yxc.imapi.model.register.Register;
import com.yxc.imapi.service.LoginService;
import com.yxc.imapi.service.RegisterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {


    @Override
    public boolean register(Users users) {
        return users.save();
    }
    @Override
    public boolean addPermission(Permission permission) {
        return permission.save();
    }

}
