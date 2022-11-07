package com.yxc.imapi.utils.enums;

/*
 * @Description : 凭证模板税费科目
 * @Author : kj-wang
 * @Date: 2020-07-17
 */
public enum VouModelTaxSubjectEnum {
    EXCLUDE(0, "不包含税费过渡科目"),
    CONTAIN(1, "包含税费过渡科目");

    private VouModelTaxSubjectEnum(Integer code, String data) {
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

    public static VouModelTaxSubjectEnum valueOf(int value) {
        switch (value) {
            case 0:
                return EXCLUDE;
            case 1:
                return CONTAIN;
            default:
                return  null;
        }
    }
}
