package com.yxc.imapi.utils;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author 杨成孟
 * @version 昆明财通科技有限公司 V1.0
 * @Title: cbms
 * @Package com.util
 * @Description:
 * @date 2017/3/24
 */
public abstract class ExcelKit implements RowHandler {
    /**
     * 读取Excel文件数据
     * @param bookFile      源文件
     * @param sheetIndex    sheet页
     * @return
     */
    public static List<Map<String,Object>> getExcelData(File bookFile,int sheetIndex){
        ExcelReader reader = ExcelUtil.getReader(bookFile,sheetIndex);
        List<Map<String,Object>> readAll = reader.readAll();
        return readAll;
    }

    public static List<List<Object>> getExcelData(File bookFile,int sheetIndex,int startRowIndex){
        ExcelReader reader = ExcelUtil.getReader(bookFile,sheetIndex);
        List<List<Object>> readAll = reader.read(startRowIndex);
        return readAll;
    }
}
