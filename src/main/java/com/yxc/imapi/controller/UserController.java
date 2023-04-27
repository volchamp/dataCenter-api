package com.yxc.imapi.controller;

import java.util.Date;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yxc.imapi.base.BaseNController;
import com.yxc.imapi.base.RedisDao;
import com.yxc.imapi.core.WebSocketServer;
import com.yxc.imapi.mapper.AvatarHistoryDao;
import com.yxc.imapi.model.*;
import com.yxc.imapi.model.sys.AddUser;
import com.yxc.imapi.model.sys.UserList;
import com.yxc.imapi.service.ChatService;
import com.yxc.imapi.service.UserService;
import com.yxc.imapi.util.JwtUtil;
import com.yxc.imapi.utils.Result;
import com.yxc.imapi.utils.UuidUtil;
import com.yxc.imapi.utils.enums.ResultEnum;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/user")
public class UserController extends BaseNController {
    static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    WebSocketServer webSocketServer;

    @Autowired
    ChatService chatService;
    @Autowired
    UserService userService;
    @Autowired
    RedisDao redisDao;

    @Autowired
    AvatarHistoryDao avatarHistoryDao;


    @RequestMapping(value = "/getUser")
    public Map<String, Object> getUser(@RequestHeader(value = "v_token", required = true) String v_token,
                                       HttpServletRequest request, HttpServletResponse hresponse) {
        Map<String, Object> remap = new HashMap<>();
        Users users = JwtUtil.getCurrUserFromToken(v_token);
        List<Users> usersList = userService.getUser(users.getUserId());
        if (usersList != null && usersList.size() > 0) {
            Users user = usersList.get(0);
            Map<String, Object> usermap = new HashMap<>();
            usermap.put("userId", user.getUserId());
            usermap.put("userName", user.getUserName());
            usermap.put("userPhone", user.getUserPhone());
            usermap.put("headUrl", user.getHeadUrl());
            usermap.put("nickName", user.getNickName());
            usermap.put("bgCover", user.getBgCover());
            usermap.put("personalSign", user.getPersonalSign());
            usermap.put("email", user.getEmail());
            usermap.put("sex", user.getSex());
            usermap.put("age", user.getAge());

            remap.put("userinfo", usermap);
            remap.put("result", "SUCCESS");
            remap.put("msg", "成功");
            return remap;
        } else {
            remap.put("result", "ERROR");
            remap.put("msg", "未获取到用户信息");
            return remap;
        }
    }


    /**
     * 删除好友
     *
     * @param v_token
     * @param addUser
     * @param request
     * @param hresponse
     * @return
     */
    @RequestMapping(value = "/deleteFriend")
    public Result deleteFriend(@RequestHeader(value = "v_token", required = true) String v_token,
                               @RequestBody @Validated @ApiParam(value = "{json对象}") AddUser addUser,
                               HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        //获取登录人信息
        Users users = JwtUtil.getCurrUserFromToken(v_token);
        String user_id = users.getUserId();

        String friend_id = addUser.getFriend_id();

        int flag = Db.update("update user_contacts set state=0 where (user_id=? and friend_id=?) or (user_id=? and friend_id=?)", user_id, friend_id, friend_id, user_id);

        if (flag > 0) {
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg("删除成功");
        } else {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("删除失败");
        }

        return result;
    }

    /**
     * 更换头像
     *
     * @param avatar
     * @param request
     * @param hresponse
     * @return
     */
    @RequestMapping(value = "/updateAvatar")
    public Result updateAvatar(@RequestHeader(value = "v_token", required = true) String v_token,
                               @RequestBody @Validated @ApiParam(value = "{json对象}") Avatar avatar,
                               HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        //获取登录人信息
        Users users = JwtUtil.getCurrUserFromToken(v_token);
        String user_id = users.getUserId();

        String avartarUrl = avatar.getAvartarUrl();

        int flag = Db.update("update users set head_url=? where user_id=? and state=1", avartarUrl, user_id);

        if (flag > 0) {
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg("操作成功");

            //这里用的mybatisplus插入数据库
            AvatarHistory avatarHistory=new AvatarHistory();
            String uuid=UuidUtil.getUuid();
            avatarHistory.setId(uuid);
            avatarHistory.setUser_id(user_id);
            avatarHistory.setAvatar(avartarUrl);
            avatarHistory.setState(1);
            avatarHistory.setCreate_time(new Date());
            avatarHistory.setCreate_user(user_id);
            avatarHistoryDao.insert(avatarHistory);

        } else {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("操作失败");
        }

        return result;
    }

    /**
     * 更换背景封面
     *
     * @param bgCover
     * @param request
     * @param hresponse
     * @return
     */
    @RequestMapping(value = "/updateBgCover")
    public Result updateBgCover(@RequestHeader(value = "v_token", required = true) String v_token,
                                @RequestBody @Validated @ApiParam(value = "{json对象}") BgCover bgCover,
                                HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        //获取登录人信息
        Users users = JwtUtil.getCurrUserFromToken(v_token);
        String user_id = users.getUserId();

        int flag = Db.update("update users set bgCover=? where user_id=? and state=1", bgCover.getBgCover(), user_id);

        if (flag > 0) {
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg("操作成功");
        } else {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("操作失败");
        }

        return result;
    }

    /**
     * 更换个性签名
     *
     * @param personalSign
     * @param request
     * @param hresponse
     * @return
     */
    @RequestMapping(value = "/updatePersonalSign")
    public Result updatePersonalSign(@RequestHeader(value = "v_token", required = true) String v_token,
                                     @RequestBody @Validated @ApiParam(value = "{json对象}") PersonalSign personalSign,
                                     HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        //获取登录人信息
        Users users = JwtUtil.getCurrUserFromToken(v_token);
        String user_id = users.getUserId();

        int flag = Db.update("update users set personalSign=? where user_id=? and state=1", personalSign.getPersonalSign(), user_id);

        if (flag > 0) {
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg("操作成功");
        } else {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("操作失败");
        }

        return result;
    }

    /**
     * 删除用户
     *
     * @param v_token
     * @param request
     * @param hresponse
     * @return
     */
    @RequestMapping(value = "/deleteUser")
    public Result deleteUser(@RequestHeader(value = "v_token", required = true) String v_token,
                             HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        Users users = JwtUtil.getCurrUserFromToken(v_token);
        String user_id = users.getUserId();

        int flag = Db.update("update users set state=0 where user_id=?", user_id);

        if (flag > 0) {
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg("删除成功");
        } else {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("删除失败");
        }

        return result;
    }

    /**
     * 获取系统所有用户列表
     *
     * @param v_token
     * @param userList
     * @param request
     * @param hresponse
     * @return
     */
    @RequestMapping(value = "/getUserList")
    public Result getUserList(@RequestHeader(value = "v_token", required = true) String v_token,
                              @RequestBody @Validated @ApiParam(value = "{json对象}") UserList userList,
                              HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        int page_size = userList.getPage_size();
        int current_page = userList.getCurrent_page();
        String keyword = userList.getKeyword();
        Page<Record> page = userService.getUserList(page_size, current_page, keyword);
        BaseController baseController = new BaseController();

        if (null == page) {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("获取数据失败！");
            return result;
        }
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg("获取数据成功");
        result.setData(baseController.pageToMap(page));
        return result;
    }

    /**
     * 获取用户角色
     *
     * @param v_token
     * @param request
     * @param hresponse
     * @return
     */
    @RequestMapping(value = "/getRoleByUserId")
    public Result getRoleByUserId(@RequestHeader(value = "v_token", required = true) String v_token,
                                  HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        Users users = JwtUtil.getCurrUserFromToken(v_token);
        String user_id = users.getUserId();
        List<Record> list = Db.find("select * from sys_user_role where user_id=? and status<>0", user_id);

        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg("获取数据成功");
        result.setData(recordsToObject(list));
        return result;
    }

}


