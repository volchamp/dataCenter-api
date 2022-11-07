package com.yxc.imapi.utils.enums;

/*
 * @Description : 凭证创建方式
 * @Author : kj-wang
 * @Date: 2020-07-17
 */
public enum VouDetailCreateActionEnum {
    MIN(1, "开始"),
    ONETOONE(1, "一借一贷"),
    ONETOMUL(2, "一借多贷"),
    MULTOONE(3, "多借一贷"),
    MULTOMUL(4, "多借多贷"),
    SUBJECT(5, "辅助核算"),
    MAX(5, "结束");

    private VouDetailCreateActionEnum(Integer code, String data) {
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

    public static VouDetailCreateActionEnum valueOf(int value) {
        switch (value) {
            case 1:
                return ONETOONE;
            case 2:
                return ONETOMUL;
            case 3:
                return MULTOONE;
            case 4:
                return MULTOMUL;
            case 5:
                return SUBJECT;
            default:
                return null;
        }
    }
}
