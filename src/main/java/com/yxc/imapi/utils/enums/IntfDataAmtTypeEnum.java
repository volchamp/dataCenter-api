package com.yxc.imapi.utils.enums;

/*
 * @Description : 接口数据金额类型
 * @Author : kj-wang
 * @Date: 2020-07-17
 */
public enum IntfDataAmtTypeEnum {
    CONTAINTAX(1, "包含税费"),
    TAX(2, "税费"),
    EXCLUDETAX(3, "排除税费");

    private IntfDataAmtTypeEnum(Integer code, String data) {
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

    public static IntfDataAmtTypeEnum valueOf(int value) {
        switch (value) {
            case 1:
                return CONTAINTAX;
            case 2:
                return TAX;
            case 3:
                return EXCLUDETAX;
            default:
                return null;
        }
    }
}
