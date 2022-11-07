package com.yxc.imapi.utils.enums;

/*
 * @Description : 支付操作状态
 * @Author : kj-wang
 * @Date: 2020-07-17
 */
public enum PayOpStatusEnum {
    NOOP(0, "未开始支付"),
    CREATEDBATCH(1, "汇总成批次"),
    CREATEDFILE(2, "生成批文件"),
    UPLOADFILE(3, "上传批文件"),
    COMMIT(4, "提交到银行"),
    BANKREPLAY(5, "银行已反馈");

    private PayOpStatusEnum(Integer code, String data) {
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

    public static PayOpStatusEnum valueOf(int value) {
        switch (value) {
            case 0:
                return NOOP;
            case 1:
                return CREATEDBATCH;
            case 2:
                return CREATEDFILE;
            case 3:
                return UPLOADFILE;
            case 4:
                return COMMIT;
            case 5:
                return BANKREPLAY;
            default:
                return null;
        }
    }
}
