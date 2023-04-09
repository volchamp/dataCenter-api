package com.yxc.imapi.service.Impl;

import com.yxc.imapi.model.SysUserRole;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.service.RegisterService;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {


    @Override
    public boolean register(Users users) {
        return users.save();
    }
    @Override
    public boolean addPermission(SysUserRole sysUserRole) {
        return sysUserRole.save();
    }

}
