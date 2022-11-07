package com.yxc.imapi.service;

import com.yxc.imapi.model.Permission;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.model.login.Login;
import com.yxc.imapi.model.register.Register;

import java.util.List;

public interface RegisterService {
    boolean register(Users users);
    boolean addPermission(Permission permission);
}
