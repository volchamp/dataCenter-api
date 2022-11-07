
package com.yxc.imapi.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author yxc
 * @ServerEndpoint 将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketServer {
    static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
//    private static ConcurrentHashMap<String, WebSocketServer> webSocketSet = new ConcurrentHashMap<String, WebSocketServer>();
    private static MultiValueMap<String, WebSocketServer> webSocketServerMultiValueMap = new LinkedMultiValueMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session WebSocketsession;
    //当前发消息的人员userId
    private String userId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam(value = "userId") String param, Session WebSocketsession, EndpointConfig config) {
        userId = param;
        logger.info(userId + "加入通讯！");
//        logger.info("当前session：" + WebSocketsession);
//        log.info("authKey:{}",authKey);
        this.WebSocketsession = WebSocketsession;
//        webSocketSet.put(param, this);//加入map中

        webSocketServerMultiValueMap.add(param, this);
//        Set<String> keySet = webSocketServerMultiValueMap.keySet();
//        for (String key : keySet) {
//            List<WebSocketServer> values = webSocketServerMultiValueMap.get(key);
//            System.out.println(values + ":" + key);
//        }

        int cnt = OnlineCount.incrementAndGet(); // 在线数加1
        logger.info("有连接加入，当前连接数为：{}", cnt);
//        sendMessage(this.WebSocketsession, "连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (!userId.equals("")) {
//            logger.info(userId + "退出通讯！");
            //先查出该用户的对象列表
            List<WebSocketServer> oldList = webSocketServerMultiValueMap.get(userId);
            if (oldList != null && oldList.size() > 0) {
                //先清掉
                webSocketServerMultiValueMap.remove(userId);
                for (WebSocketServer server : oldList) {
                    if (server.WebSocketsession != WebSocketsession) {
                        //再重新插入
                        webSocketServerMultiValueMap.add(userId, server);
                    } else {
                        logger.info(userId+"退出通讯，对象为：" + server);
                    }
                }
            }

            int cnt = OnlineCount.decrementAndGet();
            logger.info("有连接关闭，当前连接数为：{}", cnt);

//            Set<String> keySet2 = webSocketServerMultiValueMap.keySet();
//            for (String key : keySet2) {
//                List<WebSocketServer> values = webSocketServerMultiValueMap.get(key);
//                System.out.println("退出通讯后对象：" + values + ":" + key);
//            }
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("来自客户端的消息：{}", message);
        sendMessage(session, "收到消息，消息内容：" + message);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
        error.printStackTrace();
    }

    /**
     * 发送消息，每次浏览器刷新，session会发生变化。
     *
     * @param message
     */
    public void sendMessage(Session session, String message) {
        try {
//            session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)",message,session.getId()));
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     *
     * @param message
     * @throws IOException
     */
    public void broadCastInfo(String message) {
        for (String key : webSocketServerMultiValueMap.keySet()) {
            List<WebSocketServer> webSocketServerList = webSocketServerMultiValueMap.get(key);
            if (webSocketServerList != null && webSocketServerList.size() > 0) {
                for (WebSocketServer webSocketServer : webSocketServerList) {
                    Session session = webSocketServer.WebSocketsession;
                    if (session != null && session.isOpen() && !userId.equals(key)) {
                        sendMessage(session, message);
                    }
                }
            } else {
                logger.warn("未找到当前用户的客户端：" + userId);
            }
        }
    }

    /**
     * 指定Session发送消息
     *
     * @param message
     * @throws IOException
     */
    public void sendToUser(String userId, String message) {
        List<WebSocketServer> webSocketServerList = webSocketServerMultiValueMap.get(userId);
        if (webSocketServerList != null && webSocketServerList.size() > 0) {
            for (WebSocketServer webSocketServer : webSocketServerList) {
                if (webSocketServer != null && webSocketServer.WebSocketsession.isOpen()) {
                    sendMessage(webSocketServer.WebSocketsession, message);
                } else {
                    logger.warn("当前用户不在线：{}", userId + webSocketServer.toString());
                }
            }
        } else {
            logger.warn("未找到当前用户的客户端：" + userId);
        }
    }

    /**
     * 判断该用户是否在线
     *
     * @param userId 用户id
     * @return true在线 false不在线
     */
    public boolean isOnline(String userId) {
        boolean flag = false;
        List<WebSocketServer> webSocketServerList = webSocketServerMultiValueMap.get(userId);
        if (webSocketServerList != null && webSocketServerList.size() > 0) {
            for (WebSocketServer webSocketServer : webSocketServerList) {
                if (webSocketServer != null && webSocketServer.WebSocketsession.isOpen()) {
                    flag = true;
                }
            }
        } else {
            logger.warn("未找到当前用户的客户端：" + userId);
        }
        return flag;
    }
}
