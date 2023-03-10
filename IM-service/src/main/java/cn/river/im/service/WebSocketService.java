package cn.river.im.service;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import cn.river.im.dto.SendMsgDto;
import cn.river.im.entity.ChatRecord;
import cn.river.im.entity.User;
import cn.river.im.vo.SingleChatMsgVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/websocket/chat/{userId}")
@Component
@Slf4j
public class WebSocketService {
    //记录在线人数
    private static int onlineCount=0;
    //存储每个客户端的websocket对象
    private static ConcurrentHashMap<String, WebSocketService> webSocketMap =new ConcurrentHashMap<String, WebSocketService>();
    //客户端的连接会话 session
    private Session SocketSession;
    //当前连接服务的的用户id
    private String userId;

    private static ChatRecordService chatRecordService;
    @Autowired
    public void  setChatRecordService(ChatRecordService chatRecordService){
        WebSocketService.chatRecordService=chatRecordService;
    }
    private static UserService userService;
    @Autowired
    public void  setChatRecordService(UserService userService){
        WebSocketService.userService=userService;
    }




    /**
     * 获取在线人数
     */
    public static synchronized int getOnlineCount(){
        return onlineCount;
    }

    /**
     * 增加在线人数
     */
    public static synchronized void addOnlineCount(){
        WebSocketService.onlineCount++;
    }

    /**
     * 减少在线人数
     */
    public static synchronized void subOnlineCount(){
        WebSocketService.onlineCount--;
    }


    /**
     * 建立连接成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session){
        //在线人数加一
        addOnlineCount();
        this.userId=userId;
        this.SocketSession=session;
        //判断map中是否存在此key
        if (webSocketMap.containsKey(userId)){
            //存在，将原来的删除
            webSocketMap.remove(userId);
            WebSocketService.subOnlineCount();
            //重新存入
            webSocketMap.put(userId,this);
        }else {
            //不存在，直接存入
            webSocketMap.put(userId,this);
        }
        log.info("连接成功，欢迎用户：{}  当前在线用户：{}",userId,getOnlineCount());
    }

    /**
     *连接关闭调用方法
     */
    @OnClose
    public void onClose(){
        if (webSocketMap.containsKey(userId)) {
            //从map中移除当前用户的连接
            webSocketMap.remove(userId);
            //在线人数减一
            subOnlineCount();
            log.info("用户：{} 已下线，当前在线用户：{}",userId,getOnlineCount());
        }
    }

    /**
     * 客户端发送信息后调用的方法
     * @param msg
     * @param session
     */
    @OnMessage
    public void onMessage(String msg,Session session) throws IOException {
        SendMsgDto msgDto = JSONUtil.toBean(msg, SendMsgDto.class);
        log.info(msg);
        //存入数据库
        ChatRecord chatRecord = new ChatRecord();
        chatRecord.setToId(msgDto.getToId());
        chatRecord.setFromId(msgDto.getFromId());
        chatRecord.setContent(msgDto.getMsg());
        chatRecordService.addSingleChatMsg(chatRecord);
        //判断接收方是否在线，在线就发送
        if(webSocketMap.containsKey(msgDto.getToId())){
            //设置消息模板
            User user = userService.getById(msgDto.getFromId());
            SingleChatMsgVo msgVo = new SingleChatMsgVo();
            msgVo.setMsg(msgDto.getMsg());
            msgVo.setToId(msgDto.getToId());
            msgVo.setSendingTime(DateUtil.now());
            msgVo.setFromId(msgDto.getFromId());
            msgVo.setHeadPortrait(user.getHeadPortrait());
            msgVo.setNickName(user.getNickName());
            this.sendMsg(msgVo);
        }
    }

    /**
     * 发送信息到客户端
     */
    private void sendMsg(SingleChatMsgVo singleChatMsgVo) throws IOException {
        String jsonStr = JSONUtil.toJsonStr(singleChatMsgVo);
        webSocketMap.get(singleChatMsgVo.getToId()).SocketSession.getBasicRemote().sendText(jsonStr);
        log.info("消息已发送,{}",jsonStr);
    }



    /**
     * 发生异常调用方法
     */
    @OnError
    public void onError(Session session, Throwable error){
        log.error("失误");
        error.printStackTrace();
    }




}
