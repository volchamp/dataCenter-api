package com.yxc.imapi.utils.enums;

/**
 * @Description : 余额方向
 * @Author : cqh
 * @Date: 2020-07-11
 */
public enum BalanceDirEnum {
    DEBIT(1, "借"), CREDIT(2, "贷");

    private BalanceDirEnum(Integer code, String data) {
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
