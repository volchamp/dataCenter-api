package com.yxc.imapi.base;

import cn.hutool.core.util.StrUtil;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;

public class BaseNController {
    /**
     * 多表关联分页结果集合返回驼峰属性命名分页格式数据，主要为前端提供符合驼峰命名的属性数据
     *
     * @param page the page
     *
     * @return the map
     *
     * @author liwx
     * @serialData 2020 -03-23 21:14
     */
    public Map<String,Object> pageToMap(Page<Record> page){
        Map<String,Object> map = new HashMap<>();
        map.put("total",page.getTotalRow());
        map.put("rows",recordsToObject(page.getList()));
        return map;
    }

    /**
     * record集合对象返回驼峰属性命名数据集合，主要为前端提供符合驼峰命名的属性数据
     *
     * @param list the list
     *
     * @return the list
     *
     * @author liwx
     * @serialData 2020 -03-23 21:14
     */
    public List<Object> recordsToObject(List list) {
        List<Object> resultList = new ArrayList<>();
        for (Object obj : list) {
            Record record = (Record) obj;
            Map<String, Object> map = recordToMap(record);
            resultList.add(map);
        }
        return resultList;
    }

    /**
     * Record 对象返回驼峰命名属性对象，主要为前端提供符合驼峰命名的属性数据
     *
     * @param record the record
     *
     * @return the map
     *
     * @author liwx
     * @serialData 2020 -03-23 21:14
     */
    public Map<String,Object> recordToMap(Record record){
        Map<String, Object> map = new HashMap<>();
        Map<String,Object> objectMap=record.getColumns();
        Set<Map.Entry<String, Object>> entrySet = objectMap.entrySet();
        for (Map.Entry<String, Object> e : entrySet) {
            String key = e.getKey();
            Object value = e.getValue();
            map.put(CamelUtils.toCamelCase(key), value);
        }
        return map;
    }

    public String getMessage(String msg) {
        if (StrUtil.isNotEmpty(msg)) {
            return msg.replace("java.lang.Exception:", "");
        } else {
            return msg;
        }
    }
}
