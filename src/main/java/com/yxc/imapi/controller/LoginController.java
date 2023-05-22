package com.yxc.imapi.controller;

import cn.hutool.core.codec.Base64Encoder;
import com.yxc.imapi.base.BaseNController;
import com.yxc.imapi.base.RedisDao;
import com.yxc.imapi.core.WebSocketServer;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.model.login.Login;
import com.yxc.imapi.model.login.LoginResult;
import com.yxc.imapi.service.LoginService;
import com.yxc.imapi.util.JwtUtil;
import com.yxc.imapi.utils.Result;
import com.yxc.imapi.utils.ValidateCode;
import com.yxc.imapi.utils.enums.ResultEnum;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Value("${myserver.name}")
    private String serverID;


    @RequestMapping(value = "/login")
    public Map<String, Object> Login(@RequestHeader(value = "v_token", required = true) String v_token,
                                     @RequestBody @Validated @ApiParam(value = "{json对象}") Login login,
                                     HttpServletRequest request, HttpServletResponse hresponse) {
        Map<String, Object> remap = new HashMap<>();
        try {
            String sessionID = login.getSessionID();
            if (!redisDao.existsKey(sessionID)) {
                remap.put("code",-1);
                remap.put("msg","验证码过期!");
                return remap;
            }
            String captcha = login.getCaptcha().toUpperCase();
            String redisCaptcha = (String) redisDao.vGet(sessionID);
            //判断验证码是否正确
            if (!captcha.equals(redisCaptcha)) {
                remap.put("code",-7);
                remap.put("msg","验证码错误！");
                return remap;
            }
            redisDao.delete(sessionID);

            List<Users> usersList = loginService.login(login);
            if (usersList != null && usersList.size() > 0) {
                Users user = usersList.get(0);
                Map<String, Object> usermap = new HashMap<>();
                usermap.put("userId", user.getUserId());
                usermap.put("userName", user.getUserName());
                usermap.put("headUrl", user.getHeadUrl());


                String token = JwtUtil.createToken(user);
                redisDao.vSet(token, user.getUserId(),3*60*60L);

                remap.put("userinfo", usermap);
                remap.put("tokeninfo", token);
                remap.put("data",token);
                remap.put("code", 200);
                remap.put("msg", "登录成功");
                return remap;
            } else {
                remap.put("code", -1);
                remap.put("msg", "账号或密码不正确");
                return remap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            remap.put("code", 500);
            remap.put("msg", "系统异常");
            return remap;
        }
    }


    @ResponseBody
    @ApiOperation(value = "获取图片验证码")
    @RequestMapping(value = "/getValidateCode", method = RequestMethod.GET)
    @ApiImplicitParam(name = "timestamp", paramType = "query", value = "参入时间戳，防止验证码不刷新", dataType = "string", required = true)
    public Result getValidateCode(HttpServletRequest request, HttpServletResponse response) {
        Result result = new Result();
        result.setServerID(serverID);
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        ValidateCode vCode = new ValidateCode(120, 30, 4, 50);//生成图片验证码
        String sessionID = request.getSession().getId();
        //System.out.println(sessionID);
        response.addHeader("sessionID", sessionID);
        response.addHeader("serverID", serverID);
        long exireTime = 120;
        Boolean savet = redisDao.vSet(sessionID, vCode.getCode(), exireTime);//写入缓存
        if (savet) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(vCode.getBuffImg(),"png",baos);
                String img = Base64Encoder.encode(baos.toByteArray());
                //vCode.write(response.getOutputStream());
                //response.getOutputStream();
                Map<String,String> msg = new HashMap<>();
                msg.put("sessionId",sessionID);
                msg.put("img","data:image/jpg;base64,"+img);
                result.setCode(ResultEnum.SUCCESS.getCode());
                result.setData(msg);
                result.setMsg("验证码获取成功!");
            } catch (Exception ex) {
                ex.printStackTrace();
                result.setCode(ResultEnum.EREOR.getCode());
                result.setMsg("验证码获取异常");
            }
        } else {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("图形验证码写入redis失败");
        }
        return result;
    }


    @GetMapping(value = "/GetValidateCode")
    public String validateCode(HttpServletRequest request, HttpServletResponse response, String sessionid) throws Exception {
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        ValidateCode vCode = new ValidateCode(120, 30, 4, 50);//生成图片验证码
//        String sessionid = request.getSession().getId();

        long exireTime = 120;
        Boolean savet = redisDao.vSet(sessionid, vCode.getCode(), exireTime);//写入缓存
        if (savet) {
            vCode.write(response.getOutputStream());
            logger.info("图形验证码写入redis成功");
        } else {
            logger.info("图形验证码写入redis失败");
        }
        return null;
    }

}


