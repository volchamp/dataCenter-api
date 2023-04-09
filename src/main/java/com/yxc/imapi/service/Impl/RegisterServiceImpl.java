package com.yxc.imapi.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.yxc.imapi.model.SysUserRole;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.service.RegisterService;
import com.yxc.imapi.utils.UuidUtil;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {


    @Override
    public boolean register(Users users) {
        return users.save();
    }
    @Override
    public boolean addPermission(SysUserRole sysUserRole) {
        if(StrUtil.isEmpty(sysUserRole.getId())){
            sysUserRole.setId(UuidUtil.getUuid());
        }
        return sysUserRole.save();
    }

}
