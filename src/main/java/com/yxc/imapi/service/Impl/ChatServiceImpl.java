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
     * @param user_id
     * @param keyword
     * @return
     */
    @Override
    public List<Record> getContactList(String user_id, String keyword) {
        String sql="select U.* from user_contacts UC,users U\n" +
                "where UC.user_id='"+user_id+"'\n" +
                "and UC.friend_id=U.user_id\n" +
                "and U.nick_name like '%"+keyword+"%'\n" +
                "and UC.state<>0\n" +
                "and U.state<>0";
        return Db.find(sql);
    }

    /**
     * 获取会话列表
     * @param user_id
     * @param keyword
     * @return
     */
    @Override
    public List<Record> getCusMsgLatestOneList(String user_id, String keyword) {
        String sql="select U.user_name,U.nick_name,U.head_url,LI.* from user_latest_info LI,user_contacts UC,users U\n" +
                "where LI.user_id='"+user_id+"'\n" +
                "and LI.friend_id=UC.friend_id\n" +
                "and LI.user_id=UC.user_id\n" +
                "and LI.friend_id=U.user_id\n" +
                "and U.nick_name like '%"+keyword+"%'\n" +
                "and LI.state<>0\n" +
                "and UC.state<>0\n" +
                "and U.state<>0";
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
    public Page<Message> getMessageHis(int pageNumber,int pageSize,String user_id,String friend_id) {
        String sqlExceptSelect="from message\n" +
                "where sendUser='" + user_id + "' and receiver='"+friend_id+"'\n" +
                "or receiver='" + user_id + "' and sendUser='"+friend_id+"'\n" +
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
    public boolean addLetestMessage(UserLatestInfo userLatestInfo, int from) {
        String sendUserId = userLatestInfo.getUserId();
        String receiverUserId=userLatestInfo.getFriendId();
        List<UserLatestInfo> latestInfoList = userLatestInfo.find("select * from user_latest_info where user_id='"+sendUserId+"' and friend_id='"+receiverUserId+"' and state<>0");
        if (latestInfoList != null && latestInfoList.size() > 0) {
            UserLatestInfo info = latestInfoList.get(0);

            int unreadCount = info.getUnreadCount();
            if (from == 1) {
                //获取未读消息数+1
                unreadCount = unreadCount + 1;
            }
            info.setUserId(userLatestInfo.getUserId());
            info.setFriendId(userLatestInfo.getFriendId());
            info.setUnreadCount(unreadCount);
            info.setMessage(userLatestInfo.getMessage());
            info.setSendDate(userLatestInfo.getSendDate());
            info.setChannel(userLatestInfo.getChannel());
            info.setSendUser(userLatestInfo.getSendUser());
            info.setReceiver(userLatestInfo.getReceiver());
            info.setContentType(userLatestInfo.getContentType());
            info.setPictureUrl(userLatestInfo.getPictureUrl());
            info.setLongitude(userLatestInfo.getLongitude());
            info.setLatitude(userLatestInfo.getLatitude());
            info.setCoordinateType(userLatestInfo.getCoordinateType());
            info.setLastUpdateTime(userLatestInfo.getLastUpdateTime());
            info.setState(userLatestInfo.getState());
            return info.update();
        } else {
            return userLatestInfo.save();
        }
    }

    /**
     * 更新好友未读消息数
     *
     * @param unreadCount 未读数
     * @return
     */
    @Override
    public boolean updateUnreadCount(String user_id, int unreadCount,String friend_id) {
        String sql = "update user_latest_info set unreadCount="+unreadCount+" where user_id='"+user_id+"' and friend_id='"+friend_id+"' and state<>0";
        int flag = Db.update(sql);
        if (flag > 0) {
            return true;
        } else {
            return false;
        }
    }
}
