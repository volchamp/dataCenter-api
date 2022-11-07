package com.yxc.imapi.model.chat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author yxc
 * @title: location
 * @projectName api
 * @description: TODO
 * @date 2020/11/02 20:25
 */
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class location {
    //要求说明：Name Type Length Required Description
    private Double longitude;// Double 经度
    private Double latitude;// Double 纬度
    private String coordinateType;// String 经纬度类型 wgs84,gcj02, bd09ll
}
