package com.yxc.imapi.util;

import java.util.HashMap;
import java.util.Map;

public class SecretUtil {
    static Map<String, Object> appmap = new HashMap<>();
    public SecretUtil(){
        appmap.put("1000002","ZqdP96AF7sdzpvpL66wQfRR5RkggY1-087jziIRIRIw");
        appmap.put("1000003","sdfkjfsldfjdkfsaegksddfdkkkkkdkkssssssssss");
    }

    public static String getSecret(String AgentId){
        return appmap.get(AgentId).toString();
    }

    public static void main(String[] args) {
        String value=new SecretUtil().getSecret("1000002");
        System.out.println(value);
    }
}
