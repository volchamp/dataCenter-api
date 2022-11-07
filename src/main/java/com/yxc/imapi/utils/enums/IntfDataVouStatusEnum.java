package com.yxc.imapi.utils.enums;

/*
 * @Description : 接口数据凭证状态
 * @Author : kj-wang
 * @Date: 2020-07-17
 */
public enum IntfDataVouStatusEnum {
    UNCREATE(1, "未生成"),
    CREATED(2, "已生成");

    private IntfDataVouStatusEnum(Integer code, String data) {
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

    public static IntfDataVouStatusEnum valueOf(int value) {
        switch (value) {
            case 1:
                return UNCREATE;
            case 2:
                return CREATED;
            default:
                return null;
        }
    }
}
