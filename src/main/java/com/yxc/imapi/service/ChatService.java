package com.yxc.imapi.service;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yxc.imapi.model.*;

import java.util.List;

public interface ChatService {
    List<Record> getContactList(String user_id,String keyword);
    List<Record> getCusMsgLatestOneList(String user_id,String keyword);
    Page<Message> getMessageHis(int pageNumber,int pageSize,String user_id,String friend_id);
    boolean addMessage(Message message);
    boolean addLetestMessage(UserLatestInfo imUserLatestInfo,int from);
    boolean updateUnreadCount(String user_id,int unreadCount,String friend_id);
}
