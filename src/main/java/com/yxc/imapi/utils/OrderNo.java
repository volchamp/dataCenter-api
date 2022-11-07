package com.yxc.imapi.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
public class OrderNo {

    public static String CreateOrderNo() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String DateStr = simpleDateFormat.format(new Date());
        Random random = new Random();
        Double aDouble = random.nextDouble() * 89999 + 10000;//产生5位随机数
        String randomStr = aDouble.toString().substring(0, 5);
        String OrderNo = DateStr;
        for (int i = 0; i < randomStr.length(); i++) {
            Random random1 = new Random();
            Integer index = random1.nextInt(OrderNo.length() - 1);//产生随机数
            OrderNo = OrderNo.substring(0, index).concat(String.valueOf(randomStr.charAt(i))).concat(OrderNo.substring(index));
        }
        return OrderNo;
    }
}
