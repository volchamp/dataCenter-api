package com.yxc.imapi.model.chat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author yxc
 * @title: picture
 * @projectName api
 * @description: TODO
 * @date 2020/11/02 20:24
 */
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class picture {
    //要求说明：Name Type Length Required Description
    private String pictureUrl;// String 图片地址
}
