package com.yxc.imapi.controller;
import java.util.Date;

import com.jfinal.plugin.activerecord.Record;
import com.yxc.imapi.base.BaseNController;
import com.yxc.imapi.core.WebSocketServer;
import com.yxc.imapi.model.Permission;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.model.register.Register;
import com.yxc.imapi.service.ChatService;
import com.yxc.imapi.service.RegisterService;
import com.yxc.imapi.utils.JwtUtil;
import com.yxc.imapi.utils.Result;
import com.yxc.imapi.utils.enums.ResultEnum;
import com.yxc.imapi.utils.model.CurrUser;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author yxc
 * @title: im
 * @projectName im-api
 * @description: TODO
 * @date 2020/12/08 17:02
 */
@RestController
@RequestMapping("register")
public class RegisterController extends BaseNController {
    static Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    WebSocketServer webSocketServer;

    @Autowired
    RegisterService registerService;


    /**
     *  账号注册
     * @author 程序员大佬超
     * @date 2021/04/13 23:22
     * @param v_token 系统token
     * @return
     */
    @PostMapping("/accountRegister")
    @ApiOperation(value = "账号注册", notes = "", response = Result.class)
    public Result accountRegister(@RequestHeader(value = "v_token", required = true) String v_token, @RequestBody @Validated @ApiParam(value = "{json对象}") Register register, HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        String user_id = register.getUser_id();
        String nick_name = register.getNick_name();
        String user_password = register.getUser_password();

        String sql="select * from users where user_id='"+user_id+"'";
        List<Users> list=Users.dao.find(sql);
        if(null!=list&&list.size()>0){
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("该账号已存在，请重新填写");
            return result;
        }

        Users users=new Users();
        users.setUserId(user_id);
        users.setUserPassword(user_password);
        users.setUserName("");
        users.setUserPhone("");
        users.setEmail("");
        users.setNickName(nick_name);
        users.setHeadUrl("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2149183452,3867978643&fm=26&gp=0.jpg");
        users.setSex(1);
        users.setState(1);
        users.setCreateTime(new Date());
        users.setCreateUser("");
        users.setLastUpdateTime(new Date());

        Permission permission=new Permission();
        permission.setUserId(user_id);
        permission.setRoleId(1001);
        permission.setSTATE(1);
        permission.setCreateTime(new Date());


        //注册
        boolean flag=registerService.register(users);
        //赋予角色
        boolean flagP=registerService.addPermission(permission);
        if(flag){
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg("注册成功");
        }else{
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("注册失败");
        }

        return result;
    }
}


