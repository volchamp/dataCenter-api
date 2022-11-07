package com.yxc.imapi.utils.enums;

/*
 * @Description : 支付结果
 * @Author : kj-wang
 * @Date: 2020-07-17
 */
public enum PayStatusEnum {
    NONE(0, "未知"),
    SUCCESS(1, "成功"),
    FAIL(2, "失败"),
    WAIT(3, "处理中");

    private PayStatusEnum(Integer code, String data) {
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

    public PayStatusEnum valueOf(int value) {
        switch (value) {
            case 0:
                return NONE;
            case 1:
                return SUCCESS;
            case 2:
                return FAIL;
            case 3:
                return WAIT;
            default:
                return null;
        }
    }
}
