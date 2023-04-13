package com.yxc.imapi.model.sys;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


/**
 * @author yxc
 * @title:
 * @projectName imapi
 * @description: TODO
 * @date 2023/04/10 20:32
 */
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class NewFriend {
    @NotNull
    private int id;//好友申请记录ID
    @NotNull
    private String friend_id;//好友申请人id
    @NotNull
    private int friend_status;//关系状态，0请求加对方为好友，1正常（同意），2黑名单，3拒绝

    private String friend_message;//留言

    private String apply_message;//被申请人处理时，申请人的留言

}
