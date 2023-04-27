package com.yxc.imapi.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "photo_wall")
public class PhotoWall {
    private String id;//id主键',
    private String user_id;//'用户ID',
    private String url;//varchar(500) comment '图片或视频路径',
    private Integer type;//int default 1 comment '文件类型 1图片 2视频',
    private Integer sort;//int default 1 comment '排序',
    private Integer state;//int default 1 comment '状态 1有效 0无效',
    private Date create_time;//datetime comment '创建时间',
    private String create_user;//varchar(50) comment '创建人'
}
