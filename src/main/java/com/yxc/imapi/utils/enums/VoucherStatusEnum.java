package com.yxc.imapi.utils.enums;

public enum VoucherStatusEnum {

    DRAFT(0, "制单未审核"), AUDIT(1, "审核未记账"), BOOKKEEPED(2,"记账");

    private VoucherStatusEnum(Integer code, String data) {
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
