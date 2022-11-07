package com.yxc.imapi.service;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yxc.imapi.model.UserContacts;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.model.login.Login;

import java.util.List;

public interface UserService {
    List<Users> getUser(String user_id);
    Page<Record> getUserList(int page_size, int current_page, String keyword);
    boolean addUser(UserContacts userContacts);
}
