package com.yxc.imapi.service.Impl;

import com.yxc.imapi.dao.PhotoWallDao;
import com.yxc.imapi.service.PhotoWallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoWallServiceImpl implements PhotoWallService {
    @Autowired
    PhotoWallDao photoWallDao;
}
