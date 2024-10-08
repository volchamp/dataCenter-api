package com.yxc.imapi.service.Impl;

import java.util.Date;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yxc.imapi.model.*;
import com.yxc.imapi.service.ChatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    /**
     * 获取用户通讯录
     *
     * @param user_id
     * @param keyword
     * @return
     */
    @Override
    public List<Record> getContactList(String user_id, String keyword) {
        String sql = "select U.* from user_contacts UC,users U\n" +
                "where UC.user_id='" + user_id + "'\n" +
                "and UC.friend_id=U.user_id\n" +
                "and U.nick_name like '%" + keyword + "%'\n" +
                "and UC.state<>0\n" +
                "and UC.friend_status=1\n" +
                "and U.state<>0\n" +
                "order by U.nick_name desc";
        return Db.find(sql);
    }


    /**
     * 获取会话列表
     *
     * @param user_id
     * @param keyword
     * @return
     */
    @Override
    public List<Record> getChatSessionList(String user_id, String keyword) {
        String sql = "select U.user_name,U.nick_name,U.head_url,UCS.* from user_chat_session UCS\n" +
                "left join users U\n" +
                "on UCS.friend_id=U.user_id and UCS.state<>0 and U.state<>0\n" +
                "where UCS.user_id='" + user_id + "'\n" +
                "and U.nick_name like '%" + keyword + "%'";//order by UCS.lastUpdateTime desc
        return Db.find(sql);
    }

    /**
     * 获取会话列表总未读数
     *
     * @param user_id
     * @param keyword
     * @return
     */
    @Override
    public List<Record> getChatSessionUnreadSum(String user_id, String keyword) {
        String sql = "select sum(unreadCount) unreadSum from user_chat_session A\n" +
                "where A.user_id='" + user_id + "'\n" +
                "and A.state<>0";
        return Db.find(sql);
    }

    /**
     * 分页查询某个好友的消息记录
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public Page<Message> getMessageHis(int pageNumber, int pageSize, String user_id, String friend_id) {
        String sqlExceptSelect = "from message\n" +
                "where sendUser='" + user_id + "' and receiver='" + friend_id + "'\n" +
                "or receiver='" + user_id + "' and sendUser='" + friend_id + "'\n" +
                "and state<>0\n" +
                "order by create_time desc";
        Page<Message> list = Message.dao.paginate(pageNumber, pageSize, "select *", sqlExceptSelect);
        return list;
    }

    /**
     * 添加聊天记录
     *
     * @param message
     * @return
     */
    @Override
    public boolean addMessage(Message message) {
        boolean flag = message.save();
        return flag;
    }

    /**
     * 更新最新一条聊天记录信息和未读消息数
     *
     * @param from
     * @return
     */
    @Override
    public boolean addLetestMessage(UserChatSession userChatSession, int from) {
        String sendUserId = userChatSession.getUserId();
        String receiverUserId = userChatSession.getFriendId();
        List<UserChatSession> latestInfoList = userChatSession.find("select * from user_chat_session where user_id='" + sendUserId + "' and friend_id='" + receiverUserId + "' and state<>0");
        if (latestInfoList != null && latestInfoList.size() > 0) {
            UserChatSession info = latestInfoList.get(0);

            int unreadCount = info.getUnreadCount();
            if (from == 1) {
                //获取未读消息数+1
                unreadCount = unreadCount + 1;
            }
            info.setUserId(userChatSession.getUserId());
            info.setFriendId(userChatSession.getFriendId());
            info.setUnreadCount(unreadCount);
            info.setMessage(userChatSession.getMessage());
            info.setSendDate(userChatSession.getSendDate());
            info.setChannel(userChatSession.getChannel());
            info.setSendUser(userChatSession.getSendUser());
            info.setReceiver(userChatSession.getReceiver());
            info.setContentType(userChatSession.getContentType());
            info.setPictureUrl(userChatSession.getPictureUrl());
            info.setLongitude(userChatSession.getLongitude());
            info.setLatitude(userChatSession.getLatitude());
            info.setCoordinateType(userChatSession.getCoordinateType());
            info.setLastUpdateTime(userChatSession.getLastUpdateTime());
            info.setState(userChatSession.getState());
            return info.update();
        } else {
            return userChatSession.save();
        }
    }

    /**
     * 更新好友未读消息数
     *
     * @param unreadCount 未读数
     * @return
     */
    @Override
    public boolean updateUnreadCount(String user_id, int unreadCount, String friend_id) {
        String sql = "update user_chat_session set unreadCount=" + unreadCount + " where user_id='" + user_id + "' and friend_id='" + friend_id + "' and state<>0";
        int flag = Db.update(sql);
        if (flag > 0) {
            return true;
        } else {
            return false;
        }
    }
}
