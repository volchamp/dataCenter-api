package com.yxc.imapi.utils.enums;

/*
 * @Description : 凭证来源
 * @Author : kj-wang
 * @Date: 2020-07-17
 */
public enum VouSourTypeEnum {
    MANUAL(0, "手工录入"),
    INTERFACE(1, "接口凭证");

    private VouSourTypeEnum(Integer code, String data) {
        this.code = code;
        this.data = data;
    }

    private Integer code;
    private String data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static VouSourTypeEnum valueOf(int value) {
        switch (value) {
            case 0:
                return MANUAL;
            case 1:
                return INTERFACE;
            default:
                return null;
        }
    }
}
