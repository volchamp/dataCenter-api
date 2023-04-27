package com.yxc.imapi.model.login;

import com.yxc.imapi.model.Users;

public class LoginResult {
    private  String RESULT;
    private  String DSC;
    private Users userInfo;
    private  TokenInfo tokenInfo;


    public Users getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Users userInfo) {
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
