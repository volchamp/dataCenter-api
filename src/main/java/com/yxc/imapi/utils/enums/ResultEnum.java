package com.yxc.imapi.utils.enums;

/**
 * 返回状态
 */
public enum ResultEnum {
    /**
     * 200 OK     //客户端请求成功 
     * 202 NODATE //暂无数据
     * 400 Bad Request  //客户端请求有语法错误，不能被服务器所理解 
     * 401 Unauthorized //请求未经授权，这个状态代码必须和 WWW-Authenticate 报头域一起使用
     * 403 Forbidden  //服务器收到请求，但是拒绝提供服务 
     * 404 Not Found  //请求资源不存在，eg：输入了错误的 URL 
     * 500 Internal Server Error //服务器发生不可预期的错误 
     * 503 Server Unavailable  //服务器当前不能处理客户端的请求，一段时间后可能恢复正常 
     */
    SUCCESS(200, "请求成功"),
    NODATE(202, "暂无数据"),
    BADREQUEST(400, "客户端请求有语法错误，不能被服务器所理解"),
    EREOR(401, "请求未经授权"),
    FORBIDDEN(403, "服务器收到请求，但是拒绝提供服务"),
    FAILURE(404, "请求的网页不存在"),
    UNKNOWNERROR(500, "服务器发生不可预期的错误"),
    INVALID(503, "服务不可用");

    private ResultEnum(Integer code, String data) {
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
