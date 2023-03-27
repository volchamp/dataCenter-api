package com.yxc.imapi.controller;

import com.qcloud.cos.model.COSObjectInputStream;
import com.yxc.imapi.model.Attachment;
import com.yxc.imapi.service.FileUploadService;
import com.yxc.imapi.util.QCloudCosUtils;
import com.yxc.imapi.utils.Base64EnOut;
import com.yxc.imapi.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.yxc.imapi.utils.Base64EnOut.Base64Encode;

/**
 * 这个类如果图片和文件上传用了腾讯的cos对象存储的话可以使用，不然可以不要
 * @author yxc
 * @title: im
 * @projectName api
 * @description: TODO
 * @date 2021/11/11 17:02
 */
@RestController
@RequestMapping("/cos")
@Api(value = "cos测试")
public class CosController {
    private Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private QCloudCosUtils qCloudCosUtils;

    @Autowired
    FileUploadService fileUploadService;

    @ResponseBody
    @RequestMapping(value = "/tencentCosUpload", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public Result tencentCosUpload(@RequestParam(value = "file", required = false) MultipartFile[] file1,
                                   HttpServletRequest request) throws Exception {
        MultipartFile file = file1[0];
        //获取文件名
        String fileName = file.getOriginalFilename();

        Map<String, Object> result = fileUploadService.upload(file);
        String viewUrl = result.get("url").toString();
        String key = result.get("key").toString();
        String presignDownloadUrl = qCloudCosUtils.getPresignDownloadUrl(key);

        Map<String, Object> map = new HashMap<>();
        try {
            String url = "http://127.0.0.1:8088/api-im/cos/getCosContent?key=" + key;

            map.put("state", "SUCCESS");
            map.put("url", url);
            map.put("viewUrl", viewUrl);
            map.put("presignDownloadUrl", presignDownloadUrl);//预签名下载链接,30分钟后失效
            map.put("title", "");
            map.put("original", fileName);
            map.put("remark", "presignDownloadUrl是预签名下载链接,30分钟后失效。viewUrl是文件预览地址，只有公有读权限可以查看。url是大佬超写的接口，返回的是文件流");
            request.getParameterMap().keySet().forEach(d -> {
                if (!map.containsKey(d.toString())) {
                    map.put(d.toString(), request.getParameter(d.toString()));
                }
            });
        } catch (Exception e) {
            LOGGER.info("upload error:", e);
            map.put("state", "上传失败");
        }
        return Result.success(map);

    }

    /**
     * 根据服务平台接口改造为对象存储
     *
     * @param
     * @return
     * @author yxc
     * @date 2021/11/13 16:26
     */
    @CrossOrigin
    @ResponseBody
    @SuppressWarnings("all")
    @RequestMapping(value = "/uploadFileNewCos")
    public String uploadResourceCos(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        //获取文件名
        String fileName = file.getOriginalFilename();

        Map<String, Object> r = fileUploadService.upload(file);
        String viewUrl = r.get("url").toString();
        String key = r.get("key").toString();
//        String presignDownloadUrl=qCloudCosUtils.getPresignDownloadUrl(key);

        //获取文件的文件名字(后面要用到)
        String filename = file.getOriginalFilename();
        Object object = null;
        Base64EnOut base64EnOut = new Base64EnOut();
        String result = null;
//        JSONObject map = null;
        Map<String, Object> map = new HashMap<>();
        try {
            String url = "cos/getCosContent?key=" + key;

            map.put("state", "SUCCESS");
            map.put("url", url);
//            map.put("viewUrl", viewUrl);
//            map.put("presignDownloadUrl",presignDownloadUrl);//预签名下载链接,30分钟后失效
            map.put("title", "");
            map.put("original", fileName);
//            map.put("remark", "presignDownloadUrl是预签名下载链接,30分钟后失效。viewUrl是文件预览地址，只有公有读权限可以查看。url是大佬超写的接口，返回的是文件流");
            request.getParameterMap().keySet().forEach(d -> {
                if (!map.containsKey(d.toString())) {
                    map.put(d.toString(), request.getParameter(d.toString()));
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format(e.toString()));
            e.printStackTrace();
            map.put("state", "上传失败");

//            result= "<script>window.location.href=\"http://172.17.5.18/FrontManage/upload.html?res=" + base64EnOut.Encode(map.toString()) + "\"</script>";
            result = "<script>window.location.href=\"../../FrontManage/upload.html?res=" + base64EnOut.Encode(map.toString()) + "\"</script>";

        }
        map.put("original", filename);
        //// System.out.print(map.toJSONString());
//        result= "<script>window.location.href=\"http://172.17.5.18/FrontManage/upload.html?res=" + base64EnOut.Encode(map.toString()) + "\"</script>";
        result = "<script>window.location.href=\"../../FrontManage/upload.html?res=" + base64EnOut.Encode(map.toString()) + "\"</script>";
        //// System.out.print(result);
        return result;
    }


    @GetMapping(value = "getCosContent")
    public void getCosContent(@RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> map = new HashMap<>();
        try {
            byte[] bytes = qCloudCosUtils.download(key);
            response.setHeader("content-type", "application/octet-stream");

            response.setContentType("application/octet-stream");

            response.setHeader("Content-Disposition", "attachment;filename=" + key);
            response.getOutputStream().write(bytes);

        } catch (Exception e) {
            LOGGER.info("upload error:", e);
            map.put("state", "下载失败");
        }
//        return null;

    }

    /**
     * 通过获取预签名地址，在拿这个地址取读取文件流返回
     *
     * @param key
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "getCosInputStream")
    public void getCosInputStream(@RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> map = new HashMap<>();
        try {
            String presignDownloadUrl = qCloudCosUtils.getPresignDownloadUrl(key);
            InputStream inputStream = getInputStreamFromUrl(presignDownloadUrl);
        } catch (Exception e) {
            LOGGER.info("upload error:", e);
            map.put("state", "下载失败");
        }

    }


    @GetMapping(value = "getCRC64")
    public String getCRC64(@RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> map = new HashMap<>();
        try {
            String crc64 = qCloudCosUtils.getCRC64(key);
            return crc64;
        } catch (Exception e) {
            LOGGER.info("upload error:", e);
            map.put("state", "下载失败");
        }
        return null;

    }


    /**
     * 根据url下载文件流
     *
     * @param urlStr
     * @return
     */
    public InputStream getInputStreamFromUrl(String urlStr) {
        InputStream inputStream = null;
        try {
            //url解码
            URL url = new URL(java.net.URLDecoder.decode(urlStr, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(5 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            inputStream = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }


}
