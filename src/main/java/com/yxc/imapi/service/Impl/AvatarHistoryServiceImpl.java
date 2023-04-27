package com.yxc.imapi.service.Impl;

import com.yxc.imapi.mapper.AvatarHistoryDao;
import com.yxc.imapi.service.AvatarHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvatarHistoryServiceImpl implements AvatarHistoryService {
    @Autowired
    AvatarHistoryDao avatarHistoryDao;
}
