package com.yxc.imapi.model;

public class TokenInfo {

    private Long validate;
    private Long expires;
    private String token;

    public Long getValidate() {
        return validate;
    }

    public void setValidate(Long validate) {
        this.validate = validate;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
