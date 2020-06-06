package com.zsl.upmall.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName WebSocket
 * @Description WebSocket
 * @Author binggleW
 * @Date 2020-01-11 10:55
 * @Version 1.0
 **/
@Component
@ServerEndpoint("/websocket/{username}")
public class WebSocket {
    private final static Logger logger = LoggerFactory.getLogger(WebSocket.class);

    private static int onlineCount = 0;
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
    private Session session;
    private String username;

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) throws IOException {
        this.username = username;
        this.session = session;

        addOnlineCount();
        clients.put(username, this);
        logger.info("已连接" + username);
    }

    @OnClose
    public void onClose() throws IOException {
        clients.remove(username);
        subOnlineCount();
    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        // 发送数据给服务端
        sendMessageAll(message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public static void sendMessageAll(String message){
        System.out.println("发送得消息" + message);
        logger.info("发送得消息" + message);
        try{
            for (WebSocket item : clients.values()) {
                item.session.getAsyncRemote().sendText(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    public static synchronized Map<String, WebSocket> getClients() {
        return clients;
    }
}
