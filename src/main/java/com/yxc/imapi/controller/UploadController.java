package com.yxc.imapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.yxc.imapi.model.Attachment;
import com.yxc.imapi.model.upload.ImgUpload;
import com.yxc.imapi.utils.Base64EnOut;
import com.yxc.imapi.utils.Base64MultipartFile;
import com.yxc.imapi.utils.Result;
import io.swagger.annotations.ApiParam;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.yxc.imapi.utils.Base64EnOut.Base64Decode;
import static com.yxc.imapi.utils.Base64EnOut.Base64Encode;

/**
 * @author nw
 * @title: UploadController
 * @projectName api
 * @description: TODO
 * @date 2020/10/1214:48
 */
@Controller
@RequestMapping("attach")
@PropertySource("classpath:upload.properties")
public class UploadController {
    private Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @Resource
    Environment env;

    @ResponseBody
    @RequestMapping(value = "/uploadFile")
    public String uploadFile(@RequestParam(value = "upfile", required = false) MultipartFile file, HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        //获取文件的文件名字(后面要用到)
        String filename = file.getOriginalFilename();
        //这个url是要上传到另一个服务器上接口
        String url = String.format("http://10.124.13.37:8080/fileupload/attach/uploadFileNew");
        Object object = null;
        Base64EnOut base64EnOut = new Base64EnOut();
        String result = null;
        JSONObject map = null;
        //创建HttpClients实体类
        CloseableHttpClient aDefault = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //这里就是自己踩的坑,需要使用file.getBytes()
            //builder.addBinaryBody("file",file.getInputStream(),ContentType.create("multipart/form-data"),filename);
            //使用这个，另一个服务就可以接收到这个file文件了
            //// System.out.print(filename);
            builder.addBinaryBody("file", file.getBytes(), ContentType.create("multipart/form-data"), filename).setCharset(CharsetUtils.get("UTF-8"));
            HttpEntity entity = builder.build();

            httpPost.setEntity(entity);
            ResponseHandler<Object> rh = new ResponseHandler<Object>() {
                @Override
                public Object handleResponse(HttpResponse response) throws IOException {
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity, "UTF-8");
                    return result;
                }
            };
            aDefault = HttpClients.createDefault();
            object = aDefault.execute(httpPost, rh);
            map = JSONObject.parseObject(object.toString());
        } catch (Exception e) {
            LOGGER.error(String.format(e.toString()));
            map.put("state", "上传失败");
        } finally {
            aDefault.close();
        }
        map.put("original", filename);
        return map.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/uploadAtt", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public Result uploadhostFile(@RequestParam(value = "file", required = false) MultipartFile[] file1,
                                 HttpServletRequest request) throws Exception {
        Attachment attach = new Attachment();

        //获取文件保存后面的动态路径
        String backPath = this.getPath();
        //服务器地址
        StringBuilder serverPath = new StringBuilder();
        serverPath.append("http://");
        serverPath.append(request.getServerName()).append(":").append(request.getServerPort());
        serverPath.append(request.getContextPath());
        //文件保存的完整路径
        String path = env.getProperty("fileBaseStoreDIR") + "/" + backPath;

        MultipartFile file = file1[0];
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取转换后的uuid文件名
        String uuidFileName = this.getUUIDFileName(fileName);
        //完善附件对象信息,如果需要设置
        attach.setCreateTime(System.currentTimeMillis());
        attach.setSize(file.getSize());
        attach.setName(uuidFileName);
        attach.setExt(this.getExtName(fileName, '.'));
        attach.setRealPath(backPath + "/" + uuidFileName);

        File targetFile = new File(path);
        //创建文件夹
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        Map<String, Object> map = new HashMap<>();
        try {
            //分装百度上传信息
            File targetFile2 = new File(path, uuidFileName);
            file.transferTo(targetFile2);

            long size = file.getSize();
            double scale = 1.0d;
            if (size >= 200 * 1024) {
                scale = (200 * 1024f) / size;
                if (scale < 0.1f){
                    scale = scale * 10;
                }
            }

            try {
                //added by chenshun 2016-3-22 注释掉之前长宽的方式，改用大小
//            Thumbnails.of(filePathName).size(width, height).toFile(thumbnailFilePathName);
                String thpath=env.getProperty("fileBaseStoreDIR") + "/thumb/" +backPath;
                File thfile=new File(thpath);
                //创建文件夹
                if (!thfile.exists()) {
                    thfile.mkdirs();
                }

                String thumbnailFilePath = thpath + "/thum_" + uuidFileName;
                if (size < 200 * 1024) {
                    Thumbnails.of(targetFile2).scale(1f).toFile(thumbnailFilePath);
                } else {
                    Thumbnails.of(targetFile2).scale(scale).toFile(thumbnailFilePath);
                }
                map.put("thumbUrl", "thumb/" + backPath + "/thum_" + uuidFileName);

            } catch (Exception e1) {
                map.put("thummsg", "图片压缩失败" + e1.getMessage());
            }

            map.put("state", "SUCCESS");
            map.put("url", serverPath + env.getProperty("downloadApi") + uuidFileName + "&basepath=" + Base64Encode(backPath));
            map.put("viewUrl", backPath + "/" + uuidFileName);
            map.put("title", "");
            map.put("original", fileName);
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
     * 上传文件，base64位格式
     * @param v_token
     * @param imgUpload
     * @param request
     * @param hresponse
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/uploadByBase64", method = RequestMethod.POST)
    public Result uploadByBase64(@RequestHeader(value = "v_token", required = true) String v_token,
                                 @RequestBody @Validated @ApiParam(value = "{json对象}") ImgUpload imgUpload,
                                 HttpServletRequest request, HttpServletResponse hresponse) throws Exception {
        Attachment attach = new Attachment();

        //获取文件保存后面的动态路径
        String backPath = this.getPath();
        //服务器地址
        StringBuilder serverPath = new StringBuilder();
        serverPath.append("http://");
        serverPath.append(request.getServerName()).append(":").append(request.getServerPort());
        serverPath.append(request.getContextPath());
        //文件保存的完整路径
        String path = env.getProperty("fileBaseStoreDIR") + "/" + backPath;

        MultipartFile file = base64toMultipart(imgUpload.getFileBase64(),imgUpload.getFileName());
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取转换后的uuid文件名
        String uuidFileName = this.getUUIDFileName(fileName);
        //完善附件对象信息,如果需要设置
        attach.setCreateTime(System.currentTimeMillis());
        attach.setSize(file.getSize());
        attach.setName(uuidFileName);
        attach.setExt(this.getExtName(fileName, '.'));
        attach.setRealPath(backPath + "/" + uuidFileName);

        File targetFile = new File(path);
        //创建文件夹
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        Map<String, Object> map = new HashMap<>();
        try {
            //分装百度上传信息
            File targetFile2 = new File(path, uuidFileName);
            file.transferTo(targetFile2);

            long size = file.getSize();
            double scale = 1.0d;
            if (size >= 200 * 1024) {
                scale = (200 * 1024f) / size;
                if (scale < 0.1f){
                    scale = scale * 10;
                }
            }

            try {
                //added by chenshun 2016-3-22 注释掉之前长宽的方式，改用大小
//            Thumbnails.of(filePathName).size(width, height).toFile(thumbnailFilePathName);
                String thpath=env.getProperty("fileBaseStoreDIR") + "/thumb/" +backPath;
                File thfile=new File(thpath);
                //创建文件夹
                if (!thfile.exists()) {
                    thfile.mkdirs();
                }

                String thumbnailFilePath = thpath + "/thum_" + uuidFileName;
                if (size < 200 * 1024) {
                    Thumbnails.of(targetFile2).scale(1f).toFile(thumbnailFilePath);
                } else {
                    Thumbnails.of(targetFile2).scale(scale).toFile(thumbnailFilePath);
                }
                map.put("thumbUrl", "thumb/" + backPath + "/thum_" + uuidFileName);

            } catch (Exception e1) {
                map.put("thummsg", "图片压缩失败" + e1.getMessage());
            }

            map.put("state", "SUCCESS");
            map.put("url", serverPath + env.getProperty("downloadApi") + uuidFileName + "&basepath=" + Base64Encode(backPath));
            map.put("viewUrl", backPath + "/" + uuidFileName);
            map.put("title", "");
            map.put("original", fileName);
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
     * Base64转化为MultipartFile
     *
     * @param fileBase64 前台传过来base64的编码
     * @param fileName 图片的文件名
     * @return
     */
    public MultipartFile base64toMultipart(String fileBase64, String fileName) {
        try {
            String[] baseStrs = fileBase64.split(",");
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(baseStrs[0]);
            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return new Base64MultipartFile(b, baseStrs[0] , fileName);
        } catch (IOException e) {
            LOGGER.info("upload error:", e);
        }
        return null;
    }

    /**
     * @Description: 附件下载
     * @UpdateUser: 从未被修改
     * @UpdateDate: 从未被修改
     * @UpdateRemark:
     * @Version: 1.0
     * @Param: [response, name, basepath]
     * @return: void
     **/
    @CrossOrigin
    @RequestMapping("/download")
    public void download(HttpServletResponse response, String name, String basepath) throws IOException {
        if (StringUtils.hasText(name)) {
            response.reset();
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("utf-8"), "iso-8859-1"));  //转码之后下载的文件名不会出现中文乱码
            response.setContentType("application/octet-stream;charset=UTF-8");
            //String backPath = this.getPath();
            //文件保存的完整路径
            String path = env.getProperty("fileBaseStoreDIR") + "/" + Base64Decode(basepath) + "/" + name;
            //读取文件
            InputStream in = new FileInputStream(path);
            OutputStream out = response.getOutputStream();
            //写文件
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
            out.close();
        }
    }

    /**
     * @Description: getUUIDFileName:把文件名转换成uuid表示，防止文件名上传重复
     * @UpdateUser: 从未被修改
     * @UpdateDate: 从未被修改
     * @UpdateRemark:
     * @Version: 1.0
     * @Param: [fileName]
     * @return: java.lang.String
     **/
    private String getUUIDFileName(String fileName) {
        UUID uuid = UUID.randomUUID();
        StringBuilder sb = new StringBuilder(100);
        sb.append(uuid.toString()).append(".").append(this.getExtName(fileName, '.'));
        return sb.toString();
    }

    /**
     * @Description: getExtName:获取文件后缀名
     * @UpdateUser: 从未被修改
     * @UpdateDate: 从未被修改
     * @UpdateRemark:
     * @Version: 1.0
     * @Param: [s, split]
     * @return: java.lang.String
     **/
    private String getExtName(String s, char split) {
        int i = s.lastIndexOf(split);
        int leg = s.length();
        return i > 0 ? (i + 1) == leg ? " " : s.substring(i + 1, s.length()) : " ";
    }

    /**
     * @Description: 根据年月日生成文件路径
     * @UpdateUser: 从未被修改
     * @UpdateDate: 从未被修改
     * @UpdateRemark:
     * @Version: 1.0
     * @Param: []
     * @return: java.lang.String
     **/
    private String getPath() {
        //获取年月日
        Calendar a = Calendar.getInstance();
        String year = String.valueOf(a.get(Calendar.YEAR));
        String month = String.valueOf(a.get(Calendar.MONTH) + 1);
        String day = String.valueOf(a.get(Calendar.DAY_OF_MONTH));
        StringBuilder backPath = new StringBuilder(128);
        backPath.append(year).append("/").append(month).append("/").append(day);
        return backPath.toString();
    }

    //上传图片
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Result upload(MultipartFile file, HttpServletRequest request) throws IOException {

        String backPath = this.getPath();
        //服务器地址
        StringBuilder serverPath = new StringBuilder();
        serverPath.append("http://");
        serverPath.append(request.getServerName()).append(":").append(request.getServerPort());
        serverPath.append(request.getContextPath());
        //文件保存的完整路径
        String path = env.getProperty("fileBaseStoreDIR") + "/" + backPath;
        String name = file.getOriginalFilename();
        name = name.substring(name.lastIndexOf("."));
        name = UUID.randomUUID().toString().replace("-", "") + name;

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file.transferTo(Paths.get(path, name));
        return Result.success(name);
    }

}
