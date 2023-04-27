package com.yxc.imapi.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "avatar_history")
public class AvatarHistory {

    private String id;
    @TableField(value = "user_id")
    private String user_id;

    private String avatar;//'头像路径',
    private int state;//'状态 1有效 0无效',
    @TableField(value = "create_time")
    private Date create_time;//'创建时间',
    @TableField(value = "create_user")
    private String create_user;//'创建人'
}
