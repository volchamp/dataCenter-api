package com.yxc.imapi.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public class ListMapToTree {

    @SuppressWarnings("all")
    public JSONArray treeRecursionDataList(List<Map<String,Object>> treeList, String parentId) {
        JSONArray childMenu = new JSONArray();
        for (Object object : treeList) {
            JSONObject jsonMenu = JSONObject.parseObject(JSON.toJSONString(object));
            String  menuId = jsonMenu.get("node_id").toString();
            String pid = jsonMenu.get("pid").toString();
            if (parentId.equals(pid)) {
                JSONArray c_node = treeRecursionDataList(treeList, menuId);
                if(c_node.size()>0){
                    jsonMenu.put("nodes", c_node);
                }
                childMenu.add(jsonMenu);
            }
        }
        return childMenu;
    }

@SuppressWarnings("all")
    public JSONArray treeEasyuiDataList(List<Map<String,Object>> treeList, String parentId) {
        JSONArray childMenu = new JSONArray();
        for (Object object : treeList) {
            JSONObject jsonMenu = JSONObject.parseObject(JSON.toJSONString(object));
            String  menuId = jsonMenu.get("id").toString();
            String pid = jsonMenu.get("pid").toString();
            if (parentId.equals(pid)) {
                JSONArray c_node = treeEasyuiDataList(treeList, menuId);
                if(c_node.size()>0){
                    jsonMenu.put("state","closed");
                    jsonMenu.put("children", c_node);
                }else{
                    jsonMenu.put("state","open");
                }
                childMenu.add(jsonMenu);
            }
        }
        return childMenu;
    }

    @SuppressWarnings("all")
    public JSONArray treeVueDataList(List<Map<String,Object>> treeList, String parentId) {
        JSONArray childMenu = new JSONArray();
        for (Object object : treeList) {
            JSONObject jsonMenu = JSONObject.parseObject(JSON.toJSONString(object));
            String  menuId = jsonMenu.get("id").toString();
            String pid = jsonMenu.get("pid").toString();
            if (parentId.equals(pid)) {
                JSONArray c_node = treeVueDataList(treeList, menuId);
                if(c_node.size()>0){
                    jsonMenu.put("isLeaf","false");
                    jsonMenu.put("children", c_node);
                }else{
                    jsonMenu.put("isLeaf","true");
                }
                childMenu.add(jsonMenu);
            }
        }
        return childMenu;
    }


}
