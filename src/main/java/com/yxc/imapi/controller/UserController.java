package com.yxc.imapi.controller;
import java.util.Date;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yxc.imapi.base.BaseNController;
import com.yxc.imapi.base.RedisDao;
import com.yxc.imapi.core.WebSocketServer;
import com.yxc.imapi.model.UserContacts;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.model.sys.AddUser;
import com.yxc.imapi.model.sys.UserList;
import com.yxc.imapi.service.UserService;
import com.yxc.imapi.util.JwtUtil;
import com.yxc.imapi.utils.Result;
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
    UserService userService;
    @Autowired
    RedisDao redisDao;


    @RequestMapping(value = "/getUser")
    public Map<String, Object> getUser(@RequestHeader(value = "v_token", required = true) String v_token,
                                     HttpServletRequest request, HttpServletResponse hresponse) {
        Map<String, Object> remap = new HashMap<>();
        Users users=JwtUtil.getCurrUserFromToken(v_token);
        List<Users> usersList=userService.getUser(users.getUserId());
        if(usersList!=null&&usersList.size()>0){
            Users user=usersList.get(0);
            Map<String, Object> usermap = new HashMap<>();
            usermap.put("userId", user.getUserId());
            usermap.put("userName", user.getUserName());
            usermap.put("headUrl", user.getHeadUrl());
            usermap.put("nickName",user.getNickName());

            remap.put("userinfo", usermap);
            remap.put("result", "SUCCESS");
            remap.put("msg", "成功");
            return remap;
        }else{
            remap.put("result", "ERROR");
            remap.put("msg", "未获取到用户信息");
            return remap;
        }
    }

    @RequestMapping(value = "/addUser")
    public Result addUser(@RequestHeader(value = "v_token", required = true) String v_token,
                                       @RequestBody @Validated @ApiParam(value = "{json对象}") AddUser addUser,
                                       HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        String user_id = addUser.getUser_id();
        String friend_id = addUser.getFriend_id();

        if(user_id.equals(friend_id)){
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("您不能添加自己为好友哟，亲！");
            return result;
        }

        String sql="select * from user_contacts where user_id='"+user_id+"' and friend_id='"+friend_id+"' and state<>0";
        List<Users> list=Users.dao.find(sql);
        if(null!=list&&list.size()>0){
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("您已添加他为好友了，请勿重复操作！");
            return result;
        }

        //先强行添加为好友
        UserContacts userContacts1=new UserContacts();
        userContacts1.setUserId(user_id);
        userContacts1.setFriendId(friend_id);
        userContacts1.setState(1);
        userContacts1.setCreateTime(new Date());

        UserContacts userContacts2=new UserContacts();
        userContacts2.setUserId(friend_id);
        userContacts2.setFriendId(user_id);
        userContacts2.setState(1);
        userContacts2.setCreateTime(new Date());


        boolean flag = userService.addUser(userContacts1);
        boolean flag2 = userService.addUser(userContacts2);
        if (flag) {
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg("添加成功");
        } else {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("添加失败");
        }

        return result;
    }

    /**
     * 删除好友
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
        String user_id = addUser.getUser_id();
        String friend_id = addUser.getFriend_id();

        int flag=Db.update("update user_contacts set state=0 where (user_id=? and friend_id=?) or (user_id=? and friend_id=?)",user_id,friend_id,friend_id,user_id);

        if (flag>0) {
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg("删除成功");
        } else {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("删除失败");
        }

        return result;
    }

    /**
     * 删除用户
     * @param v_token
     * @param request
     * @param hresponse
     * @return
     */
    @RequestMapping(value = "/deleteUser")
    public Result deleteUser(@RequestHeader(value = "v_token", required = true) String v_token,
                               HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        Users users=JwtUtil.getCurrUserFromToken(v_token);
        String user_id = users.getUserId();

        int flag=Db.update("update users set state=0 where user_id=?",user_id);

        if (flag>0) {
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
        int page_size=userList.getPage_size();
        int current_page=userList.getCurrent_page();
        String keyword=userList.getKeyword();
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
     * @param v_token
     * @param request
     * @param hresponse
     * @return
     */
    @RequestMapping(value = "/getRoleByUserId")
    public Result getRoleByUserId(@RequestHeader(value = "v_token", required = true) String v_token,
                              HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        Users users=JwtUtil.getCurrUserFromToken(v_token);
        String user_id=users.getUserId();
        List<Record> list=Db.find("select * from permission where user_id=? and state<>0",user_id);

        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg("获取数据成功");
        result.setData(recordsToObject(list));
        return result;
    }

}


