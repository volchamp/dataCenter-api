package com.yxc.imapi.utils;

import com.yxc.imapi.utils.enums.ResultEnum;

import java.io.Serializable;

/**
 * 返回的对象(统一返回)
 *
 * @author yxc
 */
public class Result implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3337439376898084639L;

    /**
     * 处理状态
     */
    private Integer code;

    /**
     * 处理信息
     */
    private String msg;

    private String serverID;

    /**
     * 返回值
     */
    private Object data;

    private int total;
    private Object rows;
    /**
     * 成功，传入data（使用最多）
     *
     * @param data
     * @return
     */
    public static Result success(Object data) {
        return Result.success(data,"请求成功！");
    }

    /**
     * 成功，传入data 和 msg
     * @param data
     * @param msg
     * @return
     */
    public static Result success(Object data, String msg) {
        Result result = new Result();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     * 失败
     * @return
     */
    public static Result error() {
        return Result.error("请求失败！");
    }

    /**
     * 失败 传入 msg
     * @param msg
     * @return
     */
    public static Result error(String msg) {
        return  Result.error(msg,ResultEnum.FAILURE);
    }

    public static Result error(String msg ,ResultEnum resultEnum){
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(msg);
        return result;
    }

    public static Result error(Object data) {
        Result result = new Result();
        result.setCode(ResultEnum.UNKNOWNERROR.getCode());
        result.setMsg(ResultEnum.UNKNOWNERROR.getData());
        result.setData(data);
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", serverID='" + serverID + '\'' +
                ", data=" + data +
                ", total=" + total +
                ", rows=" + rows +
                '}';
    }
}




