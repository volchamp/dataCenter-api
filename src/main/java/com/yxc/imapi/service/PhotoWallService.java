package com.yxc.imapi.service;

import java.util.List;
import java.util.Map;

public interface PhotoWallService {
    int insertPhotoWall(List<Map<String, Object>> list);
    List<Map<String,Object>> getPhotoWallList(Map<String, Object> map);
}
