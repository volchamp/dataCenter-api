package com.yxc.imapi.util;

import com.jfinal.plugin.activerecord.generator.MetaBuilder;

import javax.sql.DataSource;

/**
 * @author nw
 * @title: _MetaBuilder
 * @projectName api
 * @description: TODO
 * @date 2020/11/416:42
 */
public class _MetaBuilder extends MetaBuilder{
    public _MetaBuilder(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected boolean isSkipTable(String tableName) {
        String[] tableArray = {"users","message","user_contacts","user_latest_info","role","permission"};//此处填入需要生成model的表
        for(int i=0;i<tableArray.length;i++){
            if(tableName.equals(tableArray[i])){
                return false;
            }
        }
        return true;
    }
}
