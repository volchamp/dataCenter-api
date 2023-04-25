package com.yxc.imapi.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "photo_wall")
public class PhotoWall {
    private String id;
}
