package com.yxc.imapi.model.login;

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
public class Login {
    @NotNull
    private String user_id;
    @NotNull
    private String user_password;

}
