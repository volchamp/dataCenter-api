package com.yxc.imapi.model.login;

public class LoginResult {
    private  String RESULT;
    private  String DSC;
    private  UserInfo userInfo;
    private  TokenInfo tokenInfo;


    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public String getDSC() {
        return DSC;
    }

    public void setDSC(String DSC) {
        this.DSC = DSC;
    }
}
