package com.yxc.imapi.model;



import java.io.Serializable;

public class UserInfo implements Serializable {
    private  String userId;
    private  String userName;
    private  String userPwd;
    private  String realAccNbr;
    private  String email;
    private  String departId;
    private  String roleId;
    private  String roleName;
    private  String departName;
    private  String departLevel;
    private  String areaCode;
    private String parentDepartId;
    private String userDepartId;
    private String ID;
    private String mainPage;
    private String imgUrl;
    private String userSkin;
    private String realDepartId;
    private String departType;
    private String departClass;

    private String realUserId;
    private String zhUserId;
    private String kjUserId;

    private String mofDivCode;
    private String fiscalYear;
    private String acctSetId;

    private String acctSetName;
    private String date;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getRealAccNbr() {
        return realAccNbr;
    }

    public void setRealAccNbr(String realAccNbr) {
        this.realAccNbr = realAccNbr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getDepartLevel() {
        return departLevel;
    }

    public void setDepartLevel(String departLevel) {
        this.departLevel = departLevel;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getParentDepartId() {
        return parentDepartId;
    }

    public void setParentDepartId(String parentDepartId) {
        this.parentDepartId = parentDepartId;
    }

    public String getUserDepartId() {
        return userDepartId;
    }

    public void setUserDepartId(String userDepartId) {
        this.userDepartId = userDepartId;
    }

    public String getMainPage() {
        return mainPage;
    }

    public void setMainPage(String mainPage) {
        this.mainPage = mainPage;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUserSkin() {
        return userSkin;
    }

    public void setUserSkin(String userSkin) {
        this.userSkin = userSkin;
    }

    public String getRealDepartId() {
        return realDepartId;
    }

    public void setRealDepartId(String realDepartId) {
        this.realDepartId = realDepartId;
    }

    public String getDepartType() {
        return departType;
    }

    public void setDepartType(String departType) {
        this.departType = departType;
    }

    public String getDepartClass() {
        return departClass;
    }

    public void setDepartClass(String departClass) {
        this.departClass = departClass;
    }

    public String getRealUserId() {
        return realUserId;
    }

    public void setRealUserId(String realUserId) {
        this.realUserId = realUserId;
    }

    public String getZhUserId() {
        return zhUserId;
    }

    public void setZhUserId(String zhUserId) {
        this.zhUserId = zhUserId;
    }

    public String getKjUserId() {
        return kjUserId;
    }

    public void setKjUserId(String kjUserId) {
        this.kjUserId = kjUserId;
    }

    public String getMofDivCode() {
        return mofDivCode;
    }

    public void setMofDivCode(String mofDivCode) {
        this.mofDivCode = mofDivCode;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getAcctSetId() {
        return acctSetId;
    }

    public void setAcctSetId(String acctSetId) {
        this.acctSetId = acctSetId;
    }

    public String getAcctSetName() {
        return acctSetName;
    }

    public void setAcctSetName(String acctSetName) {
        this.acctSetName = acctSetName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", realAccNbr='" + realAccNbr + '\'' +
                ", email='" + email + '\'' +
                ", departId='" + departId + '\'' +
                ", roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", departName='" + departName + '\'' +
                ", departLevel='" + departLevel + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", parentDepartId='" + parentDepartId + '\'' +
                ", userDepartId='" + userDepartId + '\'' +
                ", ID='" + ID + '\'' +
                ", mainPage='" + mainPage + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", userSkin='" + userSkin + '\'' +
                ", realDepartId='" + realDepartId + '\'' +
                ", departType='" + departType + '\'' +
                ", departClass='" + departClass + '\'' +
                ", realUserId='" + realUserId + '\'' +
                ", zhUserId='" + zhUserId + '\'' +
                ", kjUserId='" + kjUserId + '\'' +
                ", mofDivCode='" + mofDivCode + '\'' +
                ", fiscalYear='" + fiscalYear + '\'' +
                ", acctSetId='" + acctSetId + '\'' +
                ", acctSetName='" + acctSetName + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
