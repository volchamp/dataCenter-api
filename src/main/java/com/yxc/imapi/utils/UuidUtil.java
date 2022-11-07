package com.yxc.imapi.utils;

import cn.hutool.core.util.StrUtil;

/**
 * ID 工具类
 */
public class UuidUtil {
    /**
     * 获取随机ID
     * @return
     */
    public static String getUuid(){
        return StrUtil.uuid().replace("-","");
    }
}
