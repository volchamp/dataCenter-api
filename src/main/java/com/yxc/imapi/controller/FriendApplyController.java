package com.yxc.imapi.controller;

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
import java.util.Date;
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
@RequestMapping("/friend")
public class FriendApplyController extends BaseNController {
    static Logger logger = LoggerFactory.getLogger(FriendApplyController.class);

    @Autowired
    WebSocketServer webSocketServer;

    @Autowired
    ChatService chatService;
    @Autowired
    UserService userService;
    @Autowired
    RedisDao redisDao;


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

    /**
     * 获取好友请求数量
     *
     * @param v_token 系统token
     * @return
     * @author yxc
     * @date 2023/04/10 18:22
     */
    @PostMapping("/getFriendApplyNum")
    @ApiOperation(value = "获取好友请求数量", notes = "", response = Result.class)
    public Map<String, Object> getFriendApplyNum(@RequestHeader(value = "v_token", required = true) String v_token,
                                                 @RequestBody @Validated @ApiParam(value = "{json对象}") Contact contact,
                                                 HttpServletRequest request, HttpServletResponse hresponse) {
        Map<String, Object> rmap = new HashMap<>();
        try {
            Users users = JwtUtil.getCurrUserFromToken(v_token);

            List<Record> list = userService.getFriendApplyNum(users.getUserId(), contact.getKeyword());
            rmap.put("code", 200);
            rmap.put("msg", "获取数据成功");
            rmap.put("data", list == null ? 0 : list.size());
            return rmap;
        } catch (Exception e) {
            e.printStackTrace();
            rmap.put("code", -1);
            rmap.put("msg", "程序异常，请联系大佬超");
            rmap.put("data", null);
            return rmap;
        }

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
        String message = addUser.getFriend_message();

        if (user_id.equals(friend_id)) {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("您不能添加自己为好友哟，亲！");
            return result;
        }

        String sql = "select * from user_contacts where user_id='" + user_id + "' and friend_id='" + friend_id + "' and state<>0";
        List<UserContacts> list = UserContacts.dao.find(sql);
        if (null != list && list.size() > 0) {
            UserContacts userContacts = list.get(0);//关系状态，0请求加对方为好友，1正常（同意），2黑名单，3拒绝
            int status = userContacts.getFriendStatus();
            switch (status) {
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
            //将消息通过即时通讯返回
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
            Message imMessage2 = new Message();
            imMessage2.setUserId(user_id);
            imMessage2.setMessage(message);
            imMessage2.setSendDate(date);
            imMessage2.setChannel("");
            imMessage2.setSendUser(user_id);
            imMessage2.setReceiver(friend_id);
            imMessage2.setContentType(1);
            imMessage2.setPictureUrl("");
            imMessage2.setLongitude("");
            imMessage2.setLatitude("");
            imMessage2.setCoordinateType("");
            imMessage2.setIsRead(0);
            imMessage2.setMsgType(0);
            imMessage2.setCreateTime(date);
            imMessage2.setState(1);
            imMessage2.setFileName("");
            imMessage2.setFileDownloadUrl("");
            boolean flagSaveRecord2 = chatService.addMessage(imMessage2);

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
        String nike_name = users.getNickName();

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
                //更新会话
                //申请人(对方)：
                Date date=new Date();
                UserChatSession imUserChatSession2 = new UserChatSession();
                imUserChatSession2.setUserId(friend_id);
                imUserChatSession2.setFriendId(user_id);
                imUserChatSession2.setUnreadCount(0);
                imUserChatSession2.setMessage(friend_message);
                imUserChatSession2.setSendDate(date);
                imUserChatSession2.setChannel("");
                imUserChatSession2.setSendUser(user_id);
                imUserChatSession2.setReceiver(friend_id);
                imUserChatSession2.setContentType(6);
                imUserChatSession2.setPictureUrl("");
                imUserChatSession2.setLongitude("");
                imUserChatSession2.setLatitude("");
                imUserChatSession2.setCoordinateType("");
                imUserChatSession2.setState(1);
                imUserChatSession2.setLastUpdateTime(date);
                imUserChatSession2.setCreateTime(date);
                boolean latestFlag2 = chatService.addLetestMessage(imUserChatSession2, 1);

                //被申请人：
                String apply_message=newFriend.getApply_message();//申请人的留言
                UserChatSession imUserChatSession = new UserChatSession();
                imUserChatSession.setUserId(user_id);
                imUserChatSession.setFriendId(friend_id);
                imUserChatSession.setUnreadCount(1);
                imUserChatSession.setMessage(apply_message);
                imUserChatSession.setSendDate(date);
                imUserChatSession.setChannel("");
                imUserChatSession.setSendUser(user_id);
                imUserChatSession.setReceiver(friend_id);
                imUserChatSession.setContentType(6);
                imUserChatSession.setPictureUrl("");
                imUserChatSession.setLongitude("");
                imUserChatSession.setLatitude("");
                imUserChatSession.setCoordinateType("");
                imUserChatSession.setState(1);
                imUserChatSession.setLastUpdateTime(date);
                imUserChatSession.setCreateTime(date);
                boolean latestFlag = chatService.addLetestMessage(imUserChatSession, 1);

                //将给申请人的消息记录存入数据库
                Message imMessage2 = new Message();
                imMessage2.setUserId(user_id);
                imMessage2.setMessage(friend_message);
                imMessage2.setSendDate(date);
                imMessage2.setChannel("");
                imMessage2.setSendUser(user_id);
                imMessage2.setReceiver(friend_id);
                imMessage2.setContentType(1);
                imMessage2.setIsRead(0);
                imMessage2.setMsgType(0);
                imMessage2.setCreateTime(date);
                imMessage2.setState(1);
                boolean mflag = chatService.addMessage(imMessage2);

                //给双方存入添加好友成功消息
                Message imMessage3 = new Message();
                imMessage3.setUserId(user_id);
                imMessage3.setMessage("你已添加了"+nike_name+",现在可以聊天了。");
                imMessage3.setSendDate(date);
                imMessage3.setChannel("sysytem");
                imMessage3.setSendUser(user_id);
                imMessage3.setReceiver(friend_id);
                imMessage3.setContentType(6);
                imMessage3.setIsRead(0);
                imMessage3.setMsgType(0);
                imMessage3.setCreateTime(date);
                imMessage3.setState(1);
                boolean flagSaveRecord3 = chatService.addMessage(imMessage3);

                Message imMessage = new Message();
                imMessage.setUserId(friend_id);
                imMessage.setMessage("你已添加了对方,现在可以聊天了。");
                imMessage.setSendDate(date);
                imMessage.setChannel("sysytem");
                imMessage.setSendUser(friend_id);
                imMessage.setReceiver(user_id);
                imMessage.setContentType(6);
                imMessage.setIsRead(0);
                imMessage.setMsgType(0);
                imMessage.setCreateTime(date);
                imMessage.setState(1);
                boolean flagSaveRecord = chatService.addMessage(imMessage);

                //将消息通过即时通讯返回
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("message", apply_message);
                jsonObject2.put("sendUser", friend_id);
                jsonObject2.put("receiver", user_id);
                jsonObject2.put("sendDate", date);
                jsonObject2.put("contentType", 1);//消息类型，每次消息只能传 1 种类型（发送对应消息类型内容不能为空）1 文本2 房屋卡片3 图片4 位置 5文件 6好友申请
                if (webSocketServer.isOnline(user_id)) {
                    webSocketServer.sendToUser(user_id, jsonObject2.toString());
                } else {
                    System.out.println(user_id + "离线");
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("message", friend_message);
                jsonObject.put("sendUser", user_id);
                jsonObject.put("receiver", friend_id);
                jsonObject.put("sendDate", date);
                jsonObject.put("contentType", 1);//消息类型，每次消息只能传 1 种类型（发送对应消息类型内容不能为空）1 文本2 房屋卡片3 图片4 位置 5文件 6好友申请
                if (webSocketServer.isOnline(friend_id)) {
                    webSocketServer.sendToUser(friend_id, jsonObject.toString());
                } else {
                    System.out.println(friend_id + "离线");
                }

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

}


