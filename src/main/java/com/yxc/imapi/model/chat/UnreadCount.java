package com.yxc.imapi.model.chat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


/**
 * @author yxc
 * @title: UnreadCount
 * @projectName imapi
 * @description: TODO
 * @date 2020/11/07 20:32
 */
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class UnreadCount {
    @NotNull
    private String user_id;
    @NotNull
    private int unreadCount;
    @NotNull
    private String friend_id;

}
