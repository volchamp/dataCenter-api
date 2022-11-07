package com.yxc.imapi.utils.enums;

/*
 * @Description : 凭证类型
 * @Author : kj-wang
 * @Date: 2020-07-17
 */
public enum VouTypeEnum {
    KEEP(1, "记账凭证"),
    TRANS(2, "转账凭证");

    private VouTypeEnum(Integer code, String data) {
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

    public static VouTypeEnum valueOf(int value) {
        switch (value) {
            case 1:
                return KEEP;
            case 2:
                return TRANS;
            default:
                return null;
        }
    }
}
