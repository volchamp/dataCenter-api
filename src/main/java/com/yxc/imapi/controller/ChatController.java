package com.yxc.imapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yxc.imapi.base.BaseNController;
import com.yxc.imapi.core.WebSocketServer;
import com.yxc.imapi.model.Message;
import com.yxc.imapi.model.UserLatestInfo;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.model.chat.*;
import com.yxc.imapi.service.ChatService;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * @author yxc
 * @title: im
 * @projectName im-api
 * @description: TODO
 * @date 2020/11/04 17:02
 */
@RestController
@RequestMapping("chat")
public class ChatController extends BaseNController {
    static Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    WebSocketServer webSocketServer;

    @Autowired
    ChatService chatService;

    /**
     * 群发消息内容
     *
     * @return
     */
    @RequestMapping(value = "sendAll", method = RequestMethod.POST)
    public String sendAllMessage(@RequestHeader(value = "v_token", required = true) String v_token, @RequestParam(value = "message", required = true) String message,
                                 HttpServletRequest request, HttpServletResponse hresponse) {
//        JsonObject requestPayload = getRequestPayloadUtil.getRequestPayload(request);
//        String str = requestPayload.toString();
//        JSONObject obj = JSON.parseObject(str);
//        String message = obj.getString("message");//消息内容
        webSocketServer.broadCastInfo(message);
        return "success";
    }

    /**
     * 指定会话ID发消息
     *
     * @return
     */
    @RequestMapping(value = "sendOne", method = RequestMethod.POST)
    public String sendOneMessage(@RequestHeader(value = "v_token", required = true) String v_token, @RequestParam(value = "userId", required = true) String userId, @RequestParam(value = "message", required = true) String message,
                                 HttpServletRequest request, HttpServletResponse hresponse) {
        webSocketServer.sendToUser(userId, message);
        return "success";
    }

    /**
     * 获取通讯录
     *
     * @param v_token 系统token
     * @return
     * @author yxc
     * @date 2020/11/05 18:22
     */
    @PostMapping("/getContactList")
    @ApiOperation(value = "获取登录人通讯录", notes = "", response = Result.class)
    public Result getContactList(@RequestHeader(value = "v_token", required = true) String v_token,
                                 @RequestBody @Validated @ApiParam(value = "{json对象}") Contact contact,
                                 HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        Users users=JwtUtil.getCurrUserFromToken(v_token);

        List<Record> list = chatService.getContactList(users.getUserId(), contact.getKeyword());
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg("获取数据成功");
        result.setData(recordsToObject(list));
        return result;
    }

    /**
     * 获取会话列表
     *
     * @param v_token 系统token
     * @return
     * @author yxc
     * @date 2020/11/06 10:25
     */
    @PostMapping("/getCusMsgLatestOneList")
    @ApiOperation(value = "获取会话列表", notes = "", response = Result.class)
    public Result getCusMsgLatestOneList(@RequestHeader(value = "v_token", required = true) String v_token,
                                         @RequestBody @Validated @ApiParam(value = "{json对象}") Contact contact,
                                         HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        Users users=JwtUtil.getCurrUserFromToken(v_token);

        List<Record> list = chatService.getCusMsgLatestOneList(users.getUserId(), contact.getKeyword());
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg("获取数据成功");
        result.setData(recordsToObject(list));
        return result;
    }

    /**
     * 更新未读消息数
     *
     * @param v_token 系统token
     *                unreadCount 未读消息数
     * @return
     * @author yxc
     * @date 2020/11/07 20:20
     */
    @PostMapping("/updateUnreadCount")
    @ApiOperation(value = "更新未读消息数", notes = "", response = Result.class)
    public Result updateUnreadCount(@RequestHeader(value = "v_token", required = true) String v_token,
                                    @RequestBody @Validated @ApiParam(value = "{json对象}") UnreadCount unreadCount) {
        Result result = new Result();
        String user_id = unreadCount.getUser_id();
        String friend_id = unreadCount.getFriend_id();
        int count = unreadCount.getUnreadCount();  //未读消息数


        boolean flag = chatService.updateUnreadCount(user_id, count, friend_id);
        if (flag) {
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg("未读消息数更新成功");
        } else {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("未读消息数更新失败");
        }

        return result;
    }

    /**
     * 获取某个好友的聊天记录
     *
     * @param v_token
     * @param request
     * @param hresponse
     * @return
     */
    @ApiOperation(value = "获取某个好友的聊天记录")
    @PostMapping(value = "getMessageHis")
    private Page<Message> getMessageHis(@RequestHeader(value = "v_token", required = true) String v_token,
                                        @RequestBody @Validated @ApiParam(value = "{json对象}") MsgHis msgHis,
                                        HttpServletRequest request, HttpServletResponse hresponse) {
        Users users=JwtUtil.getCurrUserFromToken(v_token);
        Page<Message> page = chatService.getMessageHis(msgHis.getPageNumber(), msgHis.getPageSize(), users.getUserId(), msgHis.getFriend_id());
        List<Message> list = page.getList();
        Collections.reverse(list);//将列表反转
        page.setList(list);
        return page;
    }


    @PostMapping("/merchantmsg")
    @ApiOperation(value = "发送消息", notes = "", response = Result.class)
    public Result merchantmsg(@RequestHeader(value = "v_token", required = true) String v_token,
                              @RequestBody @Validated @ApiParam(value = "{json对象}") merchantmsg merchantmsg, HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        Users users=JwtUtil.getCurrUserFromToken(v_token);
        String sendUserId=users.getUserId();
//        String sendUserId = merchantmsg.getSendUserId();
        String receiverUserId = merchantmsg.getReceiverUserId();
        String message = merchantmsg.getMessage();
        int contentType = merchantmsg.getContentType();
        picture picture = merchantmsg.getPicture();// Object 图片信息
        String pictureUrl = "";
        if (picture != null) {
            pictureUrl = picture.getPictureUrl();// String 图片地址
        }
        String fileDownloadUrl=merchantmsg.getFileDownloadUrl();//文件下载地址
        String fileName=merchantmsg.getFileName();//文件名称

        try {

            Date date = new Date();
            //将消息记录存入数据库
            Message imMessage = new Message();
            imMessage.setUserId(sendUserId);
            imMessage.setMessage(message);
            imMessage.setSendDate(date);
            imMessage.setChannel("");
            imMessage.setSendUser(sendUserId);
            imMessage.setReceiver(receiverUserId);
            imMessage.setContentType(contentType);
            imMessage.setPictureUrl(pictureUrl);
            imMessage.setLongitude("");
            imMessage.setLatitude("");
            imMessage.setCoordinateType("");
            imMessage.setIsRead(0);
            imMessage.setMsgType(0);
            imMessage.setCreateTime(date);
            imMessage.setState(1);
            imMessage.setFileName(fileName);
            imMessage.setFileDownloadUrl(fileDownloadUrl);
            boolean flag = chatService.addMessage(imMessage);

            //更新会话
            //先给自己加一条，再给对方加一条
            //发送人：
            UserLatestInfo imUserLatestInfo2 = new UserLatestInfo();
            imUserLatestInfo2.setUserId(sendUserId);
            imUserLatestInfo2.setFriendId(receiverUserId);
            imUserLatestInfo2.setUnreadCount(0);
            imUserLatestInfo2.setMessage(message);
            imUserLatestInfo2.setSendDate(date);
            imUserLatestInfo2.setChannel("");
            imUserLatestInfo2.setSendUser(sendUserId);
            imUserLatestInfo2.setReceiver(receiverUserId);
            imUserLatestInfo2.setContentType(contentType);
            imUserLatestInfo2.setPictureUrl(pictureUrl);
            imUserLatestInfo2.setLongitude("");
            imUserLatestInfo2.setLatitude("");
            imUserLatestInfo2.setCoordinateType("");
            imUserLatestInfo2.setState(1);
            imUserLatestInfo2.setLastUpdateTime(date);
            imUserLatestInfo2.setCreateTime(date);
            boolean latestFlag2 = chatService.addLetestMessage(imUserLatestInfo2, 0);

            //接收人：
            UserLatestInfo imUserLatestInfo = new UserLatestInfo();
            imUserLatestInfo.setUserId(receiverUserId);
            imUserLatestInfo.setFriendId(sendUserId);
            imUserLatestInfo.setUnreadCount(1);
            imUserLatestInfo.setMessage(message);
            imUserLatestInfo.setSendDate(date);
            imUserLatestInfo.setChannel("");
            imUserLatestInfo.setSendUser(sendUserId);
            imUserLatestInfo.setReceiver(receiverUserId);
            imUserLatestInfo.setContentType(contentType);
            imUserLatestInfo.setPictureUrl(pictureUrl);
            imUserLatestInfo.setLongitude("");
            imUserLatestInfo.setLatitude("");
            imUserLatestInfo.setCoordinateType("");
            imUserLatestInfo.setState(1);
            imUserLatestInfo.setLastUpdateTime(date);
            imUserLatestInfo.setCreateTime(date);
            boolean latestFlag = chatService.addLetestMessage(imUserLatestInfo, 1);

            //将消息通过即时通讯返回，系统即认为发送成功
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", message);
            jsonObject.put("sendUser", sendUserId);
            jsonObject.put("receiver", receiverUserId);
            jsonObject.put("sendDate", date);
            jsonObject.put("contentType", contentType);//消息类型，每次消息只能传 1 种类型（发送对应消息类型内容不能为空）1 文本2 房屋卡片3 图片4 位置 5文件
            jsonObject.put("pictureUrl", pictureUrl);//图片
            jsonObject.put("fileName", fileName);//图片
            jsonObject.put("fileDownloadUrl", fileDownloadUrl);//图片
//                    jsonObject.put("longitude",longitude);// Double 经度
//                    jsonObject.put("latitude",latitude);// Double 纬度
//                    jsonObject.put("coordinateType",coordinateType);//String 经纬度类型 wgs84,gcj02, bd09ll
            if (webSocketServer.isOnline(sendUserId)) {
                webSocketServer.sendToUser(sendUserId, jsonObject.toString());
            } else {
                System.out.println(sendUserId + "这家伙就不在线");
            }
            if (webSocketServer.isOnline(receiverUserId)) {
                webSocketServer.sendToUser(receiverUserId, jsonObject.toString());
            } else {
                System.out.println(receiverUserId + "这家伙就不在线");
            }

            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg("发送成功");

        } catch (Exception e) {
            result.setCode(ResultEnum.UNKNOWNERROR.getCode());
            result.setMsg("发送出现异常");
        }
        return result;
    }

}


