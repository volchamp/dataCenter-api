package com.yxc.imapi.utils;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AESUtil {
    private static String KEY_ALGORITHM = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDpeMDxTK0TexHfqKFB9ggrReSArWNVpu57pMH2l1CG3pJbmxaPaTrFiFiZTEOAlRkdMhR/LI+MMCgv3/+R3DVySgEltHy4P3y4gDeq0636HXNe4gDUtxWtItc7xSE/weaW5YuHyjFkAfmkZ0Xyq1er7rxSk4LHMl7XwAWf2OTSMQIDAQAB";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法

    public static String getKeyAlgorithm() {
        return KEY_ALGORITHM;
    }

    public static void setKeyAlgorithm(String keyAlgorithm) {
        KEY_ALGORITHM = keyAlgorithm;
    }

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

            byte[] byteContent = content.getBytes("utf-8");

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密
            String strd= Base64Utils.encodeToString(result); //通过Base64转码返回
            return Base64EnOut.Encode(strd);
        } catch (Exception ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) {

        try {
            //实例化
            String strd= Base64EnOut.Decode(content);
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));

            //执行操作
            byte[] result = cipher.doFinal(Base64Utils.decodeFromString(strd));

            return new String(result, "utf-8");
        } catch (Exception ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(String key) throws NoSuchAlgorithmException {
        if (null == key || key.length() == 0) {
            throw new NullPointerException("key not is null");
        }
        SecretKeySpec key2 = null;
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(key.getBytes());
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            key2 = new SecretKeySpec(enCodeFormat, "AES");
        } catch (NoSuchAlgorithmException ex) {
            throw new NoSuchAlgorithmException();
        }
        return key2;
    }

//    public static void main(String[] args) throws UnsupportedEncodingException {
//        //生成公钥和私钥
//        //genKeyPair();
////        加密字符串
//        String privateKey = KEY_ALGORITHM;
//        String areacode="871";
//        String username="康靖宇";
//        String tel="18908803522";
//        String sys="1";
//        String userid="text";
//        String psssword="ynedw@2019";
//        Date date = new Date();
//        String timestamp = String.valueOf(date.getTime());
//        String data="{\"ad\":\""+areacode+"\",\"un\":\""+username+"\",\"tel\":\""+tel+"\",\"sys\":\""+sys+"\",\"ud\":\""+userid+"\",\"pd\":\""+psssword+"\",\"tp \":\""+timestamp+"\"}";
//        //String message = "{\"AREA\":971,\"USE_TIME\":\"2019/10/25 14:34\",\"PASSWORD\":\"ynedw@2019\",\"SEND_USER\":\"小李\",\"PHONE\":\"13567891234\"}";
////        System.out.println("随机生成的公钥为:" + keyMap.get(0));
////        System.out.println("随机生成的私钥为:" + keyMap.get(1));
//        String messageEn = encrypt(data, privateKey);
//        System.out.println("加密后的字符串为:" + messageEn);
//        //String messageEn = "0PU5m8NNvBp4RIJlSjuN938MqQpL+UApW07spi8GC4slufsrYa1JL+cQY/ix9X7koemKKUXrdXCpvWl3ujOFW/VRftMPW5NBJLIGl6hW9GKZSQRtLu/zigwuuNF25Jxg0xFPwxVG8EXRU9uwR9TWPvPmU0fkkMEJSA/txfh3HMU=";
//        String messageDe = decrypt(messageEn, privateKey);
//        System.out.println("还原后的字符串为:" + messageDe);
//        Map maps= JSON.parseObject(messageDe,Map.class);
//        System.out.printf(maps.toString());
//    }

}
