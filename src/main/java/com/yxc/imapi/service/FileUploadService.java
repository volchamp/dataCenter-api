package com.yxc.imapi.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

/**
 *上传文件接口
 */
public interface FileUploadService {
    /**
     * 处理浏览器文件上传请求
     * @param multipartFile
     * @return
     */
    Map<String, Object> upload(MultipartFile multipartFile);

    /**
     * 处理普通文件上传
     * @param file
     * @return
     */
    String upload(File file);
}

