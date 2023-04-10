package com.yxc.imapi.controller;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yxc.imapi.base.BaseNController;
import com.yxc.imapi.base.RedisDao;
import com.yxc.imapi.core.WebSocketServer;
import com.yxc.imapi.model.*;
import com.yxc.imapi.model.chat.Contact;
import com.yxc.imapi.model.sys.AddUser;
import com.yxc.imapi.model.sys.NewFriend;
import com.yxc.imapi.model.sys.UserList;
import com.yxc.imapi.service.ChatService;
import com.yxc.imapi.service.UserService;
import com.yxc.imapi.util.JwtUtil;
import com.yxc.imapi.utils.Result;
import com.yxc.imapi.utils.enums.ResultEnum;
import io.swagger.annotations.ApiOperation;
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
     * 获取好友请求列表
     *
     * @param v_token 系统token
     * @return
     * @author yxc
     * @date 2023/04/10 18:22
     */
    @PostMapping("/getNewFriendList")
    @ApiOperation(value = "获取好友请求列表", notes = "", response = Result.class)
    public Result getNewFriendList(@RequestHeader(value = "v_token", required = true) String v_token,
                                   @RequestBody @Validated @ApiParam(value = "{json对象}") Contact contact,
                                   HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        Users users = JwtUtil.getCurrUserFromToken(v_token);

        List<Record> list = userService.getNewFriendList(users.getUserId(), contact.getKeyword());
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg("获取数据成功");
        result.setData(recordsToObject(list));
        return result;
    }

    @RequestMapping(value = "/addUser")
    @ApiOperation(value = "添加好友", notes = "", response = Result.class)
    public Result addUser(@RequestHeader(value = "v_token", required = true) String v_token,
                          @RequestBody @Validated @ApiParam(value = "{json对象}") AddUser addUser,
                          HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        //获取登录人信息
        Users users = JwtUtil.getCurrUserFromToken(v_token);
        String user_id = users.getUserId();

        String friend_id = addUser.getFriend_id();
        String message=addUser.getFriend_message();

        if (user_id.equals(friend_id)) {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("您不能添加自己为好友哟，亲！");
            return result;
        }

        String sql = "select * from user_contacts where user_id='" + user_id + "' and friend_id='" + friend_id + "' and state<>0";
        List<UserContacts> list = UserContacts.dao.find(sql);
        if (null != list && list.size() > 0) {
            UserContacts userContacts=list.get(0);//关系状态，0请求加对方为好友，1正常（同意），2黑名单，3拒绝
            int status=userContacts.getFriendStatus();
            switch (status){
                case 1:
                    result.setCode(ResultEnum.EREOR.getCode());
                    result.setMsg("你们已经是好友了！");
                    return result;
                case 0:
                    result.setCode(ResultEnum.EREOR.getCode());
                    result.setMsg("你已经发送过请求了，请等待对方同意哟！");
                    return result;
                case 2:
                    break;
                case 3:
                    break;
            }

        }

        UserContacts userContacts = new UserContacts();
        userContacts.setUserId(user_id);
        userContacts.setFriendId(friend_id);
        userContacts.setFriendStatus(0);//关系状态，0请求加对方为好友，1正常（同意），2黑名单，3拒绝
        userContacts.setFriendAddDirection("out");//添加方式 in：别人加我  out：我加别人
        userContacts.setFriendMessage(message);//添加留言
        userContacts.setState(1);
        userContacts.setCreateTime(new Date());
        userContacts.setCreateUser(user_id);


        boolean flag = userService.addUser(userContacts);
        if (flag) {
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg("好友申请发送成功");
            //即时通讯推送给被申请人
            //将消息通过即时通讯返回，系统即认为发送成功
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", message);
            jsonObject.put("sendUser", user_id);
            jsonObject.put("receiver", friend_id);
            jsonObject.put("sendDate", new Date());
            jsonObject.put("contentType", 6);//消息类型，每次消息只能传 1 种类型（发送对应消息类型内容不能为空）1文本 2房屋卡片 3图片 4 位置 5文件 6好友申请
            jsonObject.put("pictureUrl", "");//图片
            jsonObject.put("fileName", "");//图片
            jsonObject.put("fileDownloadUrl", "");//图片
            if (webSocketServer.isOnline(friend_id)) {
                webSocketServer.sendToUser(friend_id, jsonObject.toString());
            } else {
                System.out.println(friend_id + "不在线");
            }

            Date date = new Date();
            //将消息记录存入数据库
            Message imMessage = new Message();
            imMessage.setUserId(user_id);
            imMessage.setMessage(message);
            imMessage.setSendDate(date);
            imMessage.setChannel("");
            imMessage.setSendUser(user_id);
            imMessage.setReceiver(friend_id);
            imMessage.setContentType(6);
            imMessage.setPictureUrl("");
            imMessage.setLongitude("");
            imMessage.setLatitude("");
            imMessage.setCoordinateType("");
            imMessage.setIsRead(0);
            imMessage.setMsgType(0);
            imMessage.setCreateTime(date);
            imMessage.setState(1);
            imMessage.setFileName("");
            imMessage.setFileDownloadUrl("");
            boolean flagSaveRecord = chatService.addMessage(imMessage);

        } else {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("好友申请发送失败");
        }

        return result;
    }

    @RequestMapping(value = "/newFrendManage")
    public Result newFrendManage(@RequestHeader(value = "v_token", required = true) String v_token,
                                 @RequestBody @Validated @ApiParam(value = "{json对象}") NewFriend newFriend,
                                 HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        //获取登录人信息
        Users users = JwtUtil.getCurrUserFromToken(v_token);
        String user_id = users.getUserId();

        int id = newFriend.getId();
        String friend_id = newFriend.getFriend_id();//申请人ID
        int friend_status = newFriend.getFriend_status();
        String friend_message = newFriend.getFriend_message();

        //更改请求人好友状态
        boolean flagStatus = userService.updateFriendStatus(id, friend_status);
        if (!flagStatus) {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("发送失败，请重试！");
            return result;
        }
        if (friend_status == 1) {
            //如果是同意，给被申请人增加一条通讯录信息
            UserContacts userContacts = new UserContacts();
            userContacts.setUserId(user_id);
            userContacts.setFriendId(friend_id);
            userContacts.setFriendStatus(1);//关系状态，0请求加对方为好友，1正常（同意），2黑名单，3拒绝
            userContacts.setFriendAddDirection("in");//添加方式 in：别人加我  out：我加别人
            userContacts.setFriendMessage(friend_message);//添加留言
            userContacts.setState(1);
            userContacts.setCreateTime(new Date());
            userContacts.setCreateUser(user_id);
            boolean flag = userService.addUser(userContacts);

            if (flag) {
                result.setCode(ResultEnum.SUCCESS.getCode());
                result.setMsg("操作成功");
                return result;
            } else {
                result.setCode(ResultEnum.EREOR.getCode());
                result.setMsg("操作失败");
                return result;
            }
        }

        //推送消息并入库

        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg("操作成功");
        return result;
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


