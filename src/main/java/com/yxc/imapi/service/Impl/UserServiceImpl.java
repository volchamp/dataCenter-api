package com.yxc.imapi.service.Impl;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yxc.imapi.model.UserContacts;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.model.login.Login;
import com.yxc.imapi.service.LoginService;
import com.yxc.imapi.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {



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
        return userContacts.save();
    }
}
