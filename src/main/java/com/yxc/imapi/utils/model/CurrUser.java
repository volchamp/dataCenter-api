package com.yxc.imapi.utils.model;


//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.IntData;

public class CurrUser {

    String userId;

    private String userCode;

    private String userName;

    private String phone;

    private String wechat;

    private String roleCode;

    private String userSkin;

    private String orgCode;

    private String orgName;

    private String admDvs;

    private String setCode;

    private String setName;

    private Integer yr;

    private String openid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getUserSkin() {
        return userSkin;
    }

    public void setUserSkin(String userSkin) {
        this.userSkin = userSkin;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getAdmDvs() {
        return admDvs;
    }

    public void setAdmDvs(String admDvs) {
        this.admDvs = admDvs;
    }

    public String getSetCode() {
        return setCode;
    }

    public void setSetCode(String setCode) {
        this.setCode = setCode;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public Integer getYr() {
        return yr;
    }

    public void setYr(Integer yr) {
        this.yr = yr;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        return "CurrUser{" +
                "userId='" + userId + '\'' +
                ", userCode='" + userCode + '\'' +
                ", userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", wechat='" + wechat + '\'' +
                ", roleCode='" + roleCode + '\'' +
                ", userSkin='" + userSkin + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", orgName='" + orgName + '\'' +
                ", admDvs='" + admDvs + '\'' +
                ", setCode='" + setCode + '\'' +
                ", setName='" + setName + '\'' +
                ", yr=" + yr +
                ", openid='" + openid + '\'' +
                '}';
    }
}
