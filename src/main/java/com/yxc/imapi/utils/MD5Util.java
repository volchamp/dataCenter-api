package com.yxc.imapi.utils;

import java.security.MessageDigest;

public class MD5Util {
	public static String md5Encode(String inString) throws Exception{
		MessageDigest md5=null;
		try {
			md5=MessageDigest.getInstance("MD5");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		byte[] byteArray=inString.getBytes("UTF-8");
		byte[] md5Bytes=md5.digest(byteArray);
		StringBuffer hexValue=new StringBuffer();
		for(int i=0;i<md5Bytes.length;i++){
			int val=((int)md5Bytes[i])&0xff;
			if(val<16){
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString().toUpperCase();
	}
	public static void main(String[] args) throws Exception{
//		String xx="ynedwkmvc";
//		System.out.println("签名前数据："+xx);
//		System.out.println("签名后数据："+md5Encode(xx));
	}
}
