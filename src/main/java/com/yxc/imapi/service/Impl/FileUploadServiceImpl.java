package com.yxc.imapi.service.Impl;

import com.yxc.imapi.service.FileUploadService;
import com.yxc.imapi.util.QCloudCosUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private QCloudCosUtils qCloudCosUtils;
    @Override
    public Map<String, Object> upload(MultipartFile multipartFile) {
        return qCloudCosUtils.upload(multipartFile);
    }

    @Override
    public String upload(File file) {
        return qCloudCosUtils.upload(file);
    }
}


