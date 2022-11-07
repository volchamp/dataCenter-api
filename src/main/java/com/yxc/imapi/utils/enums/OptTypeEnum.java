package com.yxc.imapi.utils.enums;

public enum OptTypeEnum {
    NEW(0, "创建"),EDIT(1, "编辑"),DELETE(2,"删除"),AUDIT(3,"审核"),CHANGE(4,"变更"),CLOSE(5,"关闭"),ADDREMARK(6,"添加备注"),ACTIVATION(7,"激活");

    private OptTypeEnum(Integer code, String data) {
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
