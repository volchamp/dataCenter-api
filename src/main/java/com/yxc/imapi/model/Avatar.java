package com.yxc.imapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


/**
 * @author yxc
 * @title:
 * @projectName imapi
 * @description: TODO
 * @date 2023/04/06 16:32
 */
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class Avatar {
    @NotNull
    private String avartarUrl;

}
