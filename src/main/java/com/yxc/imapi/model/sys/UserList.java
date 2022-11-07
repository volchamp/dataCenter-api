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
 * @date 2020/12/08 20:32
 */
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class UserList {
    @NotNull
    private int page_size;
    @NotNull
    private int current_page;
    @NotNull
    private String keyword;

}
