package com.yxc.imapi.model.chat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


/**
 * @author yxc
 * @title:
 * @projectName imapi
 * @description: TODO
 * @date 2020/12/08 20:32
 */
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class MsgHis {
    @NotNull
    private String friend_id;
    @NotNull
    private int pageNumber;
    @NotNull
    private int pageSize;
}
