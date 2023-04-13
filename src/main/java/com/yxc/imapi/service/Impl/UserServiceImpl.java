package com.yxc.imapi.service.Impl;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yxc.imapi.model.UserContacts;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.model.login.Login;
import com.yxc.imapi.service.LoginService;
import com.yxc.imapi.service.UserService;
import com.yxc.imapi.utils.enums.ResultEnum;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    /**
     * 获取好友请求列表
     * @param user_id
     * @param keyword
     * @return
     */
    @Override
    public List<Record> getNewFriendList(String user_id, String keyword) {
        String sql="select A.*,B.user_name,B.user_phone,B.nick_name,B.sex,B.age,B.head_url,B.personalSign from user_contacts A,Users B\n" +
                "where A.friend_id='"+user_id+"'\n" +
                "and A.friend_add_direction='out'\n" +
                "and A.user_id=B.user_id\n" +
                "and A.state=1\n" +
                "and B.state=1";
        return Db.find(sql);
    }

    /**
     * 获取好友请求数量
     * @param user_id
     * @param keyword
     * @return
     */
    @Override
    public List<Record> getFriendApplyNum(String user_id, String keyword) {
        String sql="select * from user_contacts \n" +
                "where friend_id='"+user_id+"'\n" +
                "and friend_add_direction='out'\n" +
                "and state=1\n" +
                "and friend_status=0";
        return Db.find(sql);
    }

    @Override
    public List<Users> getUser(String user_id) {
        String sql = "select * from users where user_id='" + user_id + "' and state<>0";
        return Users.dao.find(sql);
    }

    @Override
    public Page<Record> getUserList(int page_size, int current_page, String keyword) {
        return Db.paginate(current_page,page_size , "select *"," from users where (instr(user_id,?) or instr(nick_name,?) or instr(user_phone,?)) and state<>0", keyword, keyword, keyword);
    }

    @Override
    public boolean addUser(UserContacts userContacts) {
        List<UserContacts> list=UserContacts.dao.find("select * from user_contacts where user_id='" + userContacts.getUserId() + "' and friend_id='" + userContacts.getFriendId() + "' and state<>0");
        if (null != list && list.size() > 0) {
            int id=list.get(0).getId();
            userContacts.setId(id);
            return userContacts.update();
        }
        return userContacts.save();
    }

    @Override
    public boolean updateFriendStatus(int id,int friend_status) {
        int flag= Db.update("update user_contacts set friend_status=? where id=? and state=1",friend_status,id);
        if (flag>0) {
           return true;
        } else {
            return false;
        }
    }
}
