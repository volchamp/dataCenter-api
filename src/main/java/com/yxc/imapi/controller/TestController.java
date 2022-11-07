package com.yxc.imapi.controller;

import com.alibaba.fastjson.JSON;
import com.yxc.imapi.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yxc
 * @title: im
 * @projectName im-api
 * @description: TODO
 * @date 2020/11/04 17:02
 */
@RestController
@RequestMapping("/test")
@Api(value = "用来模拟途家数据返回")
public class TestController {
    @PostMapping("/getcustomermsg")
    @ApiOperation(value = "获取客人的聊天 id 等信息（注：两个参数必须要至少填一个）",notes = "",response = Result.class)
    public Object getcustomermsg(){
        String str="{ \n" +
                "    \"data\": { \n" +
                "        \"head_url\": \"https://staticfile1.fvt.tujia.com/IM/Images/Avatar/user.png\", \n" +
                "        \"isSupportUnitCard\": true, \n" +
                "        \"nick_name\": \"马化腾\", \n" +
                "        \"partner_oid\": \"11111\", \n" +
                "        \"sex\": 0, \n" +
                "        \"user_chat_id\": \"4567895am\" \n" +
                "    }, \n" +
                "    \"ret_code\": 0 \n" +
                "} \n";
        Object urbanDictionary= JSON.parse(str);
        return urbanDictionary;
    }

    @PostMapping(value = "/merchantmsg")
    public Object testPost() {
        String str="{ \n" +
                " \t\"ret_code\":0, \n" +
                " \t\"ret_message\":\"发送成功\" \n" +
                "} \n";
        Object urbanDictionary= JSON.parse(str);
        return urbanDictionary;
    }
}
