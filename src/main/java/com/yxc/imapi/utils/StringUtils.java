package com.yxc.imapi.utils;

/**
 * @author dyl
 * @title StringUtils
 * @projectName HBJOAV1
 * @description: TODO
 * @date 2019/7/229:53
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author liwuze
 * @ClassName StringUtils
 * @Description TOO
 * @Date 2019/6/11 17:01
 * @Version 1.0
 * 替换特殊字符
 **/
public class StringUtils {
    /**
     * @author 替换特殊字符
     * @Description
     * @Date 2019/6/11 17:17
     * @Param [str]
     * @return java.lang.String
     **/
    public static String stringFilter(String str) throws PatternSyntaxException {
        // 过滤普通输入框的特殊字符和关键字
        String regEx = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|[”’<>()&+{}*\\\\[\\\\]^]|"
                + "(\\b(input|button|select|update|from|table|limit|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute|script|<%)\\b)";
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    //过滤文本域的特殊字符和关键字，文本域内容不过滤<>
    public static String stringFilterSP(String str) throws PatternSyntaxException {
        String regEx = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|[”’()&+%/{}*^]|"
                + "(\\b(input|button|select|update|from|table|limit|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute|script|<%)\\b)";
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * @author 替换特殊字符
     * @Description
     * @Date 2019/08/26 17:17
     * @Param [str]
     * @return java.lang.String
     **/
    public static String stringFilterUserInfo(String str) throws PatternSyntaxException {
        // 清除掉所有特殊字符
        String regEx="[`~!@#$^&*()=|{}':;',\\\\[\\\\].<>/?~！@#￥……&*（）——|{_}【】‘；：”“'。，、？%]";
        //String regEx="[<>？%{}!?*]";
        //String regEx="[？%{}!?*]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static void main(String[] args){

        //测试字符串过滤
        String t_str1=" sd <>g sd";
        System.out.println("过滤前："+t_str1);
        System.out.println("过滤后："+ StringUtils.stringFilter(t_str1));
        //测试合并字符串
        /*String[] t_str_arr1={"PG_1","PG_2","PG_3"};
        String t_str2=StringUtils.stringConnect(t_str_arr1,",");
        System.out.println(t_str2);
        //测试拆分字符串
        String[] t_str_arr2=StringUtils.stringSpilit(t_str2,",");
        for(int i=0;i<t_str_arr2.length;i++){
            System.out.println(t_str_arr2[i]);
        }*/
    }

    public static String ClobToString(Clob clob) throws SQLException, IOException {
        String reString = new String();
        Reader is = clob.getCharacterStream(); // 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            sb.append(s);
            s = br.readLine();
        }
        reString = sb.toString();
        return reString;
    }


    public static String ZeroFill(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);// 左补0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

    /**
     * 将逗号分隔的字符串前后增加单引号
     * @param str
     * @return
     */
    public static String spilt(String str) {
        StringBuffer sb = new StringBuffer();
        String[] temp = str.split(",");
        for (int i = 0; i < temp.length; i++) {
            if (!"".equals(temp[i]) && temp[i] != null)
                sb.append("'" + temp[i] + "',");
        }
        String result = sb.toString();
        String tp = result.substring(result.length() - 1, result.length());
        if (",".equals(tp))
            return result.substring(0, result.length() - 1);
        else
            return result;
    }

}

