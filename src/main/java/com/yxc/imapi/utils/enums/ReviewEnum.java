package com.yxc.imapi.utils.enums;

public enum ReviewEnum {
    WAIT("0", "待审核"),SUCCESS("1", "通过"),FAILURE("2","不通过");

    private ReviewEnum(String code, String data) {
        this.code = code;
        this.data = data;
    }

    private String code;
    private String data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
