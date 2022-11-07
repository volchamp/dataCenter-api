package com.yxc.imapi.utils.enums;

/*
 * @Description : 接口数据操作类型
 * @Author : kj-wang
 * @Date: 2020-07-17
 */
public enum IntfDataOpTypeEnum {
    QUERYFORVOUCH(1, "记账查询"),
    QUERYFORVOUCHDATA(2, "记账凭证数据"),
    REWRITEFORVOUCH(3, "记账重写"),
    REWRITEFRODEVOUCH(4, "反记账重写"),
    QUERYFORPAY(11, "支付查询"),
    REWRITEFORPAY(12, "支付重写"),
    QUERYDATATOKEN(21, "获取接口数据令牌"),
    QUERYTOKENDATA(22, "获取令牌数据");

    private IntfDataOpTypeEnum(Integer code, String data) {
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

    public static IntfDataOpTypeEnum valueOf(int value) {
        switch (value) {
            case 1:
                return QUERYFORVOUCH;
            case 2:
                return QUERYFORVOUCHDATA;
            case 3:
                return REWRITEFORVOUCH;
            case 4:
                return REWRITEFRODEVOUCH;
            case 11:
                return QUERYFORPAY;
            case 12:
                return REWRITEFORPAY;
            case 21:
                return QUERYDATATOKEN;
            case 22:
                return QUERYTOKENDATA;
            default:
                return null;
        }
    }
}
