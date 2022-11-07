package com.yxc.imapi.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yxc.imapi.utils.model.CurrUser;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class JwtUtil {

    //密钥
    public static final String SECRET = "KunMingCtSoft.Lmt";
    //过期时间:秒
    public static final int EXPIRE = 30*60;


    public static String createToken(String userId,String userCode,String userName,
                                     String phone,String wechat,String userSkin,
                                     String roleCode,
                                     String orgCode,String orgName,
                                     String admDvs,
                                     String setCode,String setName,Integer yr,String openid) throws Exception {
        CurrUser currUser=new CurrUser();
        currUser.setUserId(userId);
        currUser.setUserCode(userCode);
        currUser.setUserName(userName);
        currUser.setPhone(phone);
        currUser.setWechat(wechat);
        currUser.setUserSkin(userSkin);
        currUser.setRoleCode(roleCode);
        currUser.setOrgCode(orgCode);
        currUser.setOrgName(orgName);
        currUser.setAdmDvs(admDvs);
        currUser.setSetCode(setCode);
        currUser.setSetName(setName);
        currUser.setYr(yr);
        currUser.setOpenid(openid);
        String userJson = JSONObject.toJSONString(currUser);
        byte[] userJsonByte = Base64.getEncoder().encode(userJson.getBytes("utf-8"));
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.SECOND, EXPIRE);
        Date expireDate = nowTime.getTime();
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(map)
                .withClaim("currUser", new String(userJsonByte))
                .withIssuedAt(new Date())
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    public static Map<String, Claim> verifyToken(String token)throws Exception{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        }catch (Exception e){
            throw new RuntimeException("凭证已过期，请重新登录");
        }
        return jwt.getClaims();
    }

    public static CurrUser getCurrUserFromToken(String token){
        CurrUser currUser=new CurrUser();
        try{
            DecodedJWT decodedJWT= JWT.decode(token);
            String str=decodedJWT.getPayload();
            byte[] orignB= Base64.getUrlDecoder().decode(str.getBytes("utf-8"));
            String orgin=new String(orignB,"utf-8");
            JSONObject jsonObject= JSON.parseObject(orgin);
            String json = jsonObject.getString("currUser");
            byte[] userInfoJson = Base64.getDecoder().decode(json.getBytes("utf-8"));
            currUser = JSONObject.parseObject(new String(userInfoJson,"utf-8"),CurrUser.class);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return currUser;
    }

    public static String getSECRET() {
        return SECRET;
    }

    public static int getEXPIRE() {
        return EXPIRE;
    }

    /*
     * Token 是否过期验证
     */
    public boolean isTokenExpired (Date expirationTime) {
        return expirationTime.before(new Date());
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

        String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJvcmdOYW1lIjoi5LqR5Y2X55yB5Lq65aSn5bi45aeU5Lya5Yqe5YWs5Y6F5py65YWz5pyN5Yqh5Lit5b-DIiwib3JnQ29kZSI6IjE0MjAwNSIsInJvbGVDb2RlIjoiMTAwMCIsInJvbGVOYW1lIjoi57O757uf566h55CG5ZGYIiwidXNlck5hbWUiOiI1OTM2NTIzODVAcXEuY29tIiwiZXhwIjoxNTgxODQ1OTQ0LCJ1c2VySWQiOiIxODA4Nzc2MTIxMSIsImlhdCI6MTU4MTg0NDE0NH0.-h0Z6TFSe4WoMkHgOnQ67jmD-hUhU7DNw7ATAz76mHQ";
        String token1="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NOdW1iZXIiOiIxODk4Nzg3MTUwMCIsInJvbGVfaWQiOiIxMDAwIiwiZGVwYXJ0X2lkIjoiMTAwMCIsInJvbGVOYW1lIjoi566h55CG5ZGYIiwidXNlck5hbWUiOiLnrqHnkIblkZgiLCJleHAiOjE1Nzc2NzUxOTIsInVzZXJJZCI6ImFkbWluIiwiaWF0IjoxNTc3NjczMzkyLCJkZXBhcnROYW1lIjoi5LqR5Y2X55yB5pys57qnIn0.RncViE4vJ4i216xR6elsSErsWuoz7ah0E38iSzxClKo";
        String token2="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NOdW1iZXIiOiIxODk4Nzg3MTUwMCIsInJvbGVfaWQiOiIxMDExIiwiZGVwYXJ0X2lkIjoiMTAwMCIsInJvbGVOYW1lIjoi5rWL6K-VIiwidXNlck5hbWUiOiLnrqHnkIblkZgiLCJleHAiOjE1Nzc2Nzc2ODAsInVzZXJJZCI6ImFkbWluIiwiaWF0IjoxNTc3Njc1ODgwLCJkZXBhcnROYW1lIjoi5LqR5Y2X55yB5pys57qnIn0.sIo8enivRTHhcQeDH8HLaXZ0dYu704GA-6tzEqAgUQQ";
        String token3="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NOdW1iZXIiOiIxODEwODcxOTk5NCIsInJvbGVfaWQiOiIxMDAwIiwiZGVwYXJ0X2lkIjoiMTAwMCIsInJvbGVOYW1lIjoi566h55CG5ZGYIiwidXNlck5hbWUiOiJ4eTEiLCJleHAiOjE1Nzc2ODY4NjAsInVzZXJJZCI6Inh5IiwiaWF0IjoxNTc3Njg1MDYwLCJkZXBhcnROYW1lIjoi5LqR5Y2X55yB5pys57qnIn0.RdMLJ7453FI1bWj-X8Dkth_39xHf970-Bm7bJF-dmMU";

        String token4="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJvcmdOYW1lIjoi6LSi5Yqh5aSEIiwidGVuYW50Q29kZSI6IjE0MjAwNSIsInVzZXJOYW1lIjoi57uv6I2k57K657ug77yE5oKK6Y2b77-9IiwidXNlcklkIjoiYzIzZGVjNWU0YmU3MTFlYTgzMjcwMDBhZjc5MWZhNWIiLCJ1c2VyQ29kZSI6ImFkbWluIiwidGVuYW50TmFtZSI6Iua1nOaIneW0oemQquS9t-axiea-ttGD54i25r-u5pa-57Sw6Y2U54Ky5Y-V6Y2Y5ZGu5rqA6Y2P6Jmr5rmH6Y2U4oCy6IWR6LmH77-9IiwicGhvbmUiOiIxODA4Nzc2MTIxMSIsIm9yZ0NvZGUiOiIxNDIwMDUwMDEiLCJyb2xlQ29kZSI6IjEwMDAsMTAwMiwxMDAzLDEwMDQsMTAwNSwxMDA2LDEwMDciLCJyb2xlTmFtZSI6Iue7r-iNpOeyuue7oO-8hOaCiumNm--_vSzmvrbli6vuhbvngLnihIPnibMs6Y2S5ZeZ7oW45qOw5ZeX7oex54C54oSD54mzLOa1vOaws-6FuOeAueKEg-eJsyznkpDjiKDlp5_nkpDnhrvnn5fmtZzlk4Tuhbjpj43vv70s6Y2X5pua57aF5qOw5ZeX7oex54C54oSD54mzLOeSp-WLr-WZvuWok-WRr-eVuyIsImV4cCI6MTU4NTI3NDM3NCwiaWF0IjoxNTg1MjcyNTc0LCJlbWFpbCI6IjU5MzY1MjM4NUBxcS5jb20ifQ.35NQMH3GPagaXqC_pl4ugDvtoQDRQVL-OswizwhaYIM";
        try{
            //token4=createToken("", "admin","管理员","1000","1011","工单发起人","140200","财务处","5300","云南省本级","","云南省本级");
        }catch (Exception e){
            e.printStackTrace();
        }
        CurrUser currUser=new CurrUser();
        currUser = getCurrUserFromToken(token4);
        /*DecodedJWT decodedJWT= JWT.decode(token4);
        String str=decodedJWT.getPayload();
        System.out.println(str);
        byte[] orignB= Base64.getUrlDecoder().decode(str.getBytes("utf-8"));
        String orgin=new String(orignB,"utf-8");
        JSONObject jsonObject= JSON.parseObject(orgin);
        String json = jsonObject.getString("userInfo");
        byte[] userInfoJson = Base64.getDecoder().decode(json.getBytes("utf-8"));
        userInfo = JSONObject.parseObject(new String(userInfoJson,"utf-8"),UserInfo.class);*/
        System.out.println(currUser.toString());

    }


}
