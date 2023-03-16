package com.yxc.imapi.util;
import java.util.Date;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yxc.imapi.model.Users;
import org.h2.engine.User;

import java.util.*;

/**
 * The type Jwt util.
 *
 * @author liwx
 */
public class JwtUtil {
    public static void main(String[] args) {
        Users users = new Users();
        users.setId(1);
        users.setUserId("yxc");
        users.setUserPassword("RTEwQURDMzk0OUJBNTlBQkJFNTZFMDU3RjIwRjg4M0U=");
        users.setUserName("大佬超");
        users.setUserPhone("18468182835");
        users.setEmail("");
        users.setNickName("");
        users.setHeadUrl("");
        users.setSex(0);
        users.setState(0);
        users.setCreateTime(new Date());
        users.setCreateUser("");
        users.setLastUpdateTime(new Date());

        try {
            String token = createToken(users);
            System.out.println(token);

//            Map<String, Claim> map = verifyToken(token);
//            int length=map.size();

            Users users1= getCurrUserFromToken(token);
            String userId=users1.getUserId();
            System.out.println(userId);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 密钥
     */
    public static final String SECRET = "DaLaoChaoSoft.Lmt";
    /**
     * 过期时间:秒
     */
    public static final int EXPIRE = 30 * 60;

    public static String createToken(Users localCurrentUser) throws Exception {
        String userJson = JSONObject.toJSONString(localCurrentUser);
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

    public static Map<String, Claim> verifyToken(String token) throws Exception {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        } catch (Exception e) {
            throw new RuntimeException("凭证已过期，请重新登录");
        }
        return jwt.getClaims();
    }

    public static Users getCurrUserFromToken(String token) {
        Users currUser = new Users();
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            String str = decodedJWT.getPayload();
            byte[] orignB = Base64.getUrlDecoder().decode(str.getBytes("utf-8"));
            String orgin = new String(orignB, "utf-8");
            JSONObject jsonObject = JSON.parseObject(orgin);
            String json = jsonObject.getString("currUser");
            byte[] userInfoJson = Base64.getDecoder().decode(json.getBytes("utf-8"));
            currUser = JSONObject.parseObject(new String(userInfoJson, "utf-8"), Users.class);
        } catch (Exception e) {
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
    public boolean isTokenExpired(Date expirationTime) {
        return expirationTime.before(new Date());
    }
}
