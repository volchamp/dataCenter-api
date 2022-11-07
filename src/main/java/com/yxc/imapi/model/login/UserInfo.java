package com.yxc.imapi.model.login;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private  String USER_ID;//
	private  String USER_NAME;//
	private  String USER_PWD;
	private  String REAL_ACC_NBR;//
	private  String EMAIL;//
	private  String DEPART_ID;
	private  String ROLE_ID;
	private  String ROLE_NAME;
	private  String DEPART_NAME;
	private  String DEPART_LEVEL;
	private  String AREA_CODE;
	private String PARENT_DEPART_ID;
	private String USER_DEPART_ID;
	private String ID;
	private String MainPage;
	private String IMG_URL;
	private String USER_OA;
	private String REAL_DEPART_ID;

	public String getIMG_URL() {
		return IMG_URL;
	}

	public void setIMG_URL(String IMG_URL) {
		this.IMG_URL = IMG_URL;
	}

	public String getMainPage() {
		return MainPage;
	}

	public void setMainPage(String mainPage) {
		MainPage = mainPage;
	}

	@Override
	public String toString() {
		return "UserInfo{" +
				"USER_ID='" + USER_ID + '\'' +
				", USER_NAME='" + USER_NAME + '\'' +
				", USER_PWD='" + USER_PWD + '\'' +
				", REAL_ACC_NBR='" + REAL_ACC_NBR + '\'' +
				", EMAIL='" + EMAIL + '\'' +
				", DEPART_ID='" + DEPART_ID + '\'' +
				", ROLE_ID='" + ROLE_ID + '\'' +
				", ROLE_NAME='" + ROLE_NAME + '\'' +
				", DEPART_NAME='" + DEPART_NAME + '\'' +
				", DEPART_LEVEL='" + DEPART_LEVEL + '\'' +
				", AREA_CODE='" + AREA_CODE + '\'' +
				", PARENT_DEPART_ID='" + PARENT_DEPART_ID + '\'' +
				", USER_DEPART_ID='" + USER_DEPART_ID + '\'' +
				", ID='" + ID + '\'' +
				", MainPage='" + MainPage + '\'' +
				", IMG_URL='" + IMG_URL + '\'' +
				", USER_OA='" + USER_OA + '\'' +
				", REAL_DEPART_ID='" + REAL_DEPART_ID + '\'' +
				'}';
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String USER_ID) {
		this.USER_ID = USER_ID;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String USER_NAME) {
		this.USER_NAME = USER_NAME;
	}

	public String getUSER_PWD() {
		return USER_PWD;
	}

	public void setUSER_PWD(String USER_PWD) {
		this.USER_PWD = USER_PWD;
	}

	public String getREAL_ACC_NBR() {
		return REAL_ACC_NBR;
	}

	public void setREAL_ACC_NBR(String REAL_ACC_NBR) {
		this.REAL_ACC_NBR = REAL_ACC_NBR;
	}

	public String getEMAIL() {
		return EMAIL;
	}

	public void setEMAIL(String EMAIL) {
		this.EMAIL = EMAIL;
	}

	public String getDEPART_ID() {
		return DEPART_ID;
	}

	public void setDEPART_ID(String DEPART_ID) {
		this.DEPART_ID = DEPART_ID;
	}

	public String getROLE_ID() {
		return ROLE_ID;
	}

	public void setROLE_ID(String ROLE_ID) {
		this.ROLE_ID = ROLE_ID;
	}

	public String getROLE_NAME() {
		return ROLE_NAME;
	}

	public void setROLE_NAME(String ROLE_NAME) {
		this.ROLE_NAME = ROLE_NAME;
	}

	public String getDEPART_NAME() {
		return DEPART_NAME;
	}

	public void setDEPART_NAME(String DEPART_NAME) {
		this.DEPART_NAME = DEPART_NAME;
	}

	public String getDEPART_LEVEL() {
		return DEPART_LEVEL;
	}

	public void setDEPART_LEVEL(String DEPART_LEVEL) {
		this.DEPART_LEVEL = DEPART_LEVEL;
	}

	public String getAREA_CODE() {
		return AREA_CODE;
	}

	public void setAREA_CODE(String AREA_CODE) {
		this.AREA_CODE = AREA_CODE;
	}

	public String getPARENT_DEPART_ID() {
		return PARENT_DEPART_ID;
	}

	public void setPARENT_DEPART_ID(String PARENT_DEPART_ID) {
		this.PARENT_DEPART_ID = PARENT_DEPART_ID;
	}

	public String getUSER_DEPART_ID() {
		return USER_DEPART_ID;
	}

	public void setUSER_DEPART_ID(String USER_DEPART_ID) {
		this.USER_DEPART_ID = USER_DEPART_ID;
	}

	public String getUSER_OA() {
		return USER_OA;
	}

	public void setUSER_OA(String USER_OA) {
		this.USER_OA = USER_OA;
	}

	public String getREAL_DEPART_ID() {
		return REAL_DEPART_ID;
	}

	public void setREAL_DEPART_ID(String REAL_DEPART_ID) {
		this.REAL_DEPART_ID = REAL_DEPART_ID;
	}
}
