package com.yxc.imapi.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.utils.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 腾讯云对象存储工具类
 */
public class QCloudCosUtils {
    //API密钥secretId
    private String secretId;
    //API密钥secretKey
    private String secretKey;
    //存储桶所属地域
    private String region;
    //存储桶空间名称
    private String bucketName;
    //存储桶访问域名
    private String url;
    //上传文件前缀路径(eg:/images/)
    private String prefix;

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 上传File类型的文件
     *
     * @param file
     * @return 上传文件在存储桶的链接
     */
    public String upload(File file) {
        //生成唯一文件名
        String newFileName = generateUniqueName(file.getName());
        //文件在存储桶中的key
        String key = prefix + newFileName;
        //声明客户端
        COSClient cosClient = null;
        try {
            //生成cos客户端
            cosClient = getCOSClient();
            //创建存储对象的请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
            //执行上传并返回结果信息
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            return url + key;
        } catch (CosClientException e) {
            e.printStackTrace();
        } finally {
            cosClient.shutdown();
        }
        return null;
    }

    /**
     * upload()重载方法
     *
     * @param multipartFile
     * @return 上传文件在存储桶的链接
     */
    public Map<String, Object> upload(MultipartFile multipartFile) {
        Map<String, Object> map = new HashMap<>();
        System.out.println(multipartFile);
        //生成唯一文件名
        String newFileName = generateUniqueName(multipartFile.getOriginalFilename());
        //文件在存储桶中的key
        String key = prefix + newFileName;
        //声明客户端
        COSClient cosClient = null;
        //准备将MultipartFile类型转为File类型
        File file = null;
        try {
            //生成临时文件
            file = File.createTempFile("temp", null);
            //将MultipartFile类型转为File类型
            multipartFile.transferTo(file);

            //生成cos客户端
            cosClient = getCOSClient();
            //创建存储对象的请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
            //执行上传并返回结果信息
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            String urlStr = url + key;
            map.put("url", urlStr);
            map.put("key", key);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cosClient.shutdown();
        }
        return null;
    }

    /**
     * 根据UUID生成唯一文件名
     *
     * @param originalName
     * @return
     */
    public String generateUniqueName(String originalName) {
        return UUID.randomUUID() + originalName.substring(originalName.lastIndexOf("."));
    }

    /**
     * 获取cos客户端
     *
     * @return
     */
    private COSClient getCOSClient() {
        //声明客户端
        COSClient cosClient = null;
        //初始化用户身份信息(secretId,secretKey)
        COSCredentials cosCredentials = new BasicCOSCredentials(secretId, secretKey);
        //设置bucket的区域
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        //生成cos客户端
        cosClient = new COSClient(cosCredentials, clientConfig);
        return cosClient;
    }


    /**
     * 下载对象
     */
    public void getObject(String key) throws InterruptedException, IOException, NoSuchAlgorithmException {
        //声明客户端
        COSClient cosClient = getCOSClient();

        // 方法2 下载文件到本地
        String outputFilePath = "exampleobject";
        File downFile = new File(outputFilePath);
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
    }

    //传入key以获取文件下载流
    public byte[] download(String key) {
        COSObjectInputStream cosObjectInput = null;
        byte[] bytes = null;
        try {
            COSClient cosClient = getCOSClient();
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);//根据桶和key获取文件请求
            System.out.println("文件请求：" + getObjectRequest);
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            cosObjectInput = cosObject.getObjectContent();
            System.out.println("输出流：" + cosObjectInput);
            // 下载对象的 CRC64
            String crc64Ecma = cosObject.getObjectMetadata().getCrc64Ecma();

            // 读取流

            try {
                bytes = IOUtils.toByteArray(cosObjectInput);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                cosObjectInput.close();
            }

            // 关闭输入流
            cosObjectInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    //传入key以获取文件下载流(返回CRC64)
    public String getCRC64(String key) {
        COSObjectInputStream cosObjectInput = null;
        String crc64Ecma = "";
        try {
            COSClient cosClient = getCOSClient();
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);//根据桶和key获取文件请求
            System.out.println("文件请求：" + getObjectRequest);
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            cosObjectInput = cosObject.getObjectContent();
            System.out.println("输出流：" + cosObjectInput);
            // 下载对象的 CRC64
            crc64Ecma = cosObject.getObjectMetadata().getCrc64Ecma();
            // 关闭输入流
            cosObjectInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return crc64Ecma;
    }


    /**
     * 获取预签名下载链接
     *
     * @return
     * @throws CosClientException
     */
    public URL generatePresignedUrl(String key, String picUrl) throws CosClientException {
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
        // 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(1小时)
        // 这里设置签名在半个小时后过期
        Date expirationDate = new Date(System.currentTimeMillis() + 30L * 60L * 1000L);
        req.setExpiration(expirationDate);
        COSClient cosClient = getCOSClient();
        URL url = cosClient.generatePresignedUrl(req);
        cosClient.shutdown();
        return url;
    }

    /**
     * 获取预签名下载链接
     */
    public String getPresignDownloadUrl(String key) throws InterruptedException, IOException, NoSuchAlgorithmException {
        // 生成 cos 客户端。
        COSClient cosClient = getCOSClient();
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
//        String bucketName = "examplebucket-1250000000";
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
        // 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(1小时)
        // 这里设置签名在半个小时后过期
        Date expirationDate = new Date(System.currentTimeMillis() + 30L * 60L * 1000L);
        req.setExpiration(expirationDate);
        URL url = cosClient.generatePresignedUrl(req);
        System.out.println(url.toString());
        cosClient.shutdown();
        return url.toString();
    }

}

