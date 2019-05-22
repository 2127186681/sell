package com.imooc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * websocket通信
 */
@Component
@Slf4j
@ServerEndpoint("/webSocket")
public class WebSocket {
    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session){ //打开websocket 连接
        this.session = session;
        webSocketSet.add(this);
        log.info("[websocket消息] 有新的连接 总数={}",webSocketSet.size());
    }

    @OnClose // 关闭websocike连接
    public void onClose(){
        webSocketSet.remove(this);
        log.info("[websocket消息] 连接断开 总数={}",webSocketSet.size());
    }

    @OnMessage
    public void OmMessage(String message){ //接受前端发送的消息
        log.info("[websocket消息] 收到客户端发来的消息：{}",message);
    }

    public void sendMessage(String message){ //向前端发送消息
        for(WebSocket webSocket :webSocketSet){
            log.info("[websocket消息] 广播消息，message={}",message);
            try {
                webSocket.session.getBasicRemote().sendText(message); //向前端发送消息
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
