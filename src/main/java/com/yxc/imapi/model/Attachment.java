package com.yxc.imapi.model;

/**
 * @author nw
 * @title: Attachment
 * @projectName api
 * @description: TODO
 * @date 2020/10/1215:09
 */
public class Attachment {
    private int id;

    //创建文件
    private long createTime;

    //文件名
    private String name;

    //size
    private long size;

    //扩展名
    private String ext;

    //上传路径
    private String realPath;

    //文件类型
    private String mediaType;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
