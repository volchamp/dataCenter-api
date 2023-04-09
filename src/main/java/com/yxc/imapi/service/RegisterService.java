package com.yxc.imapi.service;

import com.yxc.imapi.model.SysUserRole;
import com.yxc.imapi.model.Users;

public interface RegisterService {
    boolean register(Users users);
    boolean addPermission(SysUserRole sysUserRole);
}
