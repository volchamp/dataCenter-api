package com.yxc.imapi.utils.enums;

public enum YesOrNoEnum {
    No(0, "否"), Yes(1, "是");

    private YesOrNoEnum(Integer code, String data) {
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
