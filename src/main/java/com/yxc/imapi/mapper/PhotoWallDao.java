package com.yxc.imapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxc.imapi.model.PhotoWall;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PhotoWallDao extends BaseMapper<PhotoWall> {
    int insertPhotoWall(List<Map<String, Object>> list);
    List<Map<String,Object>> getPhotoWallList(Map<String, Object> map);
}
