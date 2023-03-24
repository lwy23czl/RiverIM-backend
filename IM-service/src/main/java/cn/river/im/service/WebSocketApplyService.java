package cn.river.im.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.river.im.vo.SingleChatMsgVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/websocket/apply/{userId}")
@Component
@Slf4j
public class WebSocketApplyService {

    private static ConcurrentHashMap<String, WebSocketApplyService> webSocketApplyMap =new ConcurrentHashMap<String, WebSocketApplyService>();
    //客户端的连接会话 session
    private Session SocketSession;
    //当前连接服务的的用户id
    private String userId;

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session){
        this.userId=userId;
        this.SocketSession=session;
        //判断map中是否存在此key
        if (webSocketApplyMap.containsKey(userId)){
            //存在，将原来的删除
            webSocketApplyMap.remove(userId);
            //重新存入
            webSocketApplyMap.put(userId,this);
        }else {
            //不存在，直接存入
            webSocketApplyMap.put(userId,this);
        }
    }
    @OnClose
    public void onClose(){
        if (webSocketApplyMap.containsKey(userId)) {
            //从map中移除当前用户的连接
            webSocketApplyMap.remove(userId);
        }
    }

    @OnMessage
    public void onMessage(String msg,Session session) throws IOException {
        JSONObject parseObj = JSONUtil.parseObj(msg);
        this.sendMsg(parseObj.getStr("toId"),parseObj.getStr("type"));
    }

    /**
     * 发生异常调用方法
     */
    @OnError
    public void onError(Session session, Throwable error){
        log.error("失误");
        error.printStackTrace();
    }

    /**
     * 发送信息到客户端
     */
    private void sendMsg(String toId,String type) throws IOException {
        webSocketApplyMap.get(toId).SocketSession.getBasicRemote().sendText(type);
        log.info("apply信息已发送");
    }
}
