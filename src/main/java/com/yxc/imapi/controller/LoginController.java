package com.yxc.imapi.controller;

import cn.hutool.system.UserInfo;
import com.jfinal.plugin.activerecord.Record;
import com.yxc.imapi.base.BaseNController;
import com.yxc.imapi.base.RedisDao;
import com.yxc.imapi.core.WebSocketServer;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.model.login.Login;
import com.yxc.imapi.model.login.LoginResult;
import com.yxc.imapi.service.ChatService;
import com.yxc.imapi.service.LoginService;
import com.yxc.imapi.util.SecretUtil;
import com.yxc.imapi.utils.Base64EnOut;
import com.yxc.imapi.utils.JwtUtil;
import com.yxc.imapi.utils.Result;
import com.yxc.imapi.utils.ValidateCode;
import com.yxc.imapi.utils.enums.ResultEnum;
import com.yxc.imapi.utils.model.CurrUser;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.internal.objects.NativeString.toLowerCase;


/**
 * @author yxc
 * @title: im
 * @projectName im-api
 * @description: TODO
 * @date 2020/12/08 17:02
 */
@RestController
@RequestMapping("/login")
public class LoginController extends BaseNController {
    static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    WebSocketServer webSocketServer;

    @Autowired
    LoginService loginService;
    @Autowired
    RedisDao redisDao;


    @RequestMapping(value = "/login")
    public Map<String, Object> Login(@RequestHeader(value = "v_token", required = true) String v_token,
                                     @RequestBody @Validated @ApiParam(value = "{json对象}") Login login,
                                     HttpServletRequest request, HttpServletResponse hresponse) {

        Map<String, Object> remap = new HashMap<>();
        LoginResult loginResult = new LoginResult();
        List<Users> usersList=loginService.login(login);
        if(usersList!=null&&usersList.size()>0){
            Users user=usersList.get(0);
            Map<String, Object> usermap = new HashMap<>();
            usermap.put("userId", user.getUserId());
            usermap.put("userName", user.getUserName());
            usermap.put("headUrl", user.getHeadUrl());

            remap.put("userinfo", usermap);
            remap.put("tokeninfo", loginResult.getTokenInfo());
            remap.put("result", "SUCCESS");
            remap.put("msg", "登录成功");
            return remap;
        }else{
            remap.put("result", "ERROR");
            remap.put("msg", "账号或密码不正确");
            return remap;
        }
    }

}


