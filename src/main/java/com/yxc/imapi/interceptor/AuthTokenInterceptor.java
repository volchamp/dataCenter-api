package com.yxc.imapi.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.yxc.imapi.base.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author liwx
 * @date 2021-05-25 16:01.
 */
@Component
public class AuthTokenInterceptor implements HandlerInterceptor{
    @Autowired
    RedisDao redisDao;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception{
        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)){
            token = request.getParameter("token");
        }
        if(StrUtil.isEmpty(token)){
            //未包含token，直接输出401
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            JSONObject res = new JSONObject();
            res.put("code", -1);
            res.put("type", "");
            res.put("message", "没有访问权限，请重新登录!");
            res.put("data", "");
            out.append(res.toString());
            out.flush();
            out.close();
            return false;
        }

        if(!redisDao.existsKey(token)){
            //Redis已过期
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            JSONObject res = new JSONObject();
            res.put("code", -1);
            res.put("type", "");
            res.put("message", "账号登录信息过期，请重新登录!");
            res.put("data", "");
            out.append(res.toString());
            out.flush();
            out.close();
            return false;
        }
        //判断过期时间，如果小于60分钟，延迟过期时间
        long expire = redisDao.getExpire(token);
        if(expire/60 < 60){
            redisDao.vSet(token, "延迟过期",300*60L);
        }
        //用户登录信息存入线程变量
//        LocalCurrentUser currentUser = JwtUtil.getCurrUserFromToken(token);
//        LocalUserContext.setCurrentUser(currentUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        LocalUserContext.removeCurrentUser();
    }
}
