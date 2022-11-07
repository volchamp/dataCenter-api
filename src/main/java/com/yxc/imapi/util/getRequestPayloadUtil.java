package com.yxc.imapi.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class getRequestPayloadUtil {
    public static JsonObject getRequestPayload(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = req.getReader();) {
            char[]buff = new char[1024];
            int len;
            while((len = reader.read(buff)) != -1) {
                sb.append(buff,0, len);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        Gson json=new Gson();
        JsonObject obj = json.fromJson(sb.toString(), JsonObject.class);
//        JSONObject obj= JSON.parseObject(sb.toString());
        return obj;
    }

    public static Map<String, Object> getParamerMap(HttpServletRequest httpServletRequest) {
        Map<String,String[]> map=httpServletRequest.getParameterMap();
        Map<String,Object> valuesMap=new HashMap<String, Object>();
        Set<Map.Entry<String, String[]>> set=map.entrySet();
        for(Map.Entry<String,String[]> entry:set)
        {
            String text=entry.getValue()[0];
            String key = entry.getKey();
            valuesMap.put(key, text);
        }
        return valuesMap;
    }
}
