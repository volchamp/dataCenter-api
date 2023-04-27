package com.yxc.imapi.service.Impl;

import com.yxc.imapi.mapper.PhotoWallDao;
import com.yxc.imapi.service.PhotoWallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PhotoWallServiceImpl implements PhotoWallService {
    @Autowired
    PhotoWallDao photoWallDao;

    @Override
    public int insertPhotoWall(List<Map<String, Object>> list) {
        return photoWallDao.insertPhotoWall(list);
    }

    @Override
    public List<Map<String,Object>> getPhotoWallList(Map<String, Object> map) {
        return photoWallDao.getPhotoWallList(map);
    }
}
