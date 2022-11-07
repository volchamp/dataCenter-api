package com.yxc.imapi.utils.enums;

/**
 * 删除状态
 */
public enum DeletedEnum {
    UNDELETED(0, "未删除"), DELETED(1, "已删除");

    private DeletedEnum(Integer code, String data) {
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
}
