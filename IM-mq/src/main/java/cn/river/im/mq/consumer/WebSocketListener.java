package cn.river.im.mq.consumer;

import cn.hutool.core.date.DateUtil;
import cn.river.im.dto.SendMsgDto;
import cn.river.im.entity.ChatRecord;
import cn.river.im.entity.FriendRequest;
import cn.river.im.service.ChatRecordService;
import cn.river.im.service.FriendRequestService;
import cn.river.im.service.UserService;
import cn.river.im.vo.SingleChatMsgVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class WebSocketListener {
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private ChatRecordService chatRecordService;
//
//    @Autowired
//    private FriendRequestService friendRequestService;
//
//    /**
//     * 监听好友请求,并发送
//     */
//    /*@RabbitListener(bindings = @QueueBinding(
//            value =@Queue("onMessage.apply"),
//            exchange = @Exchange(value = "webSocket.onMessage",type = ExchangeTypes.TOPIC),
//            key ="apply.#"
//    ))
//    public void listenApplyQueue(SendMsgDto sendMsgDto){
//        log.info("监听到好友请求，{}",sendMsgDto);
//
//    }*/
//
//    /**
//     * 监听好友请求,并写入数据库
//     */
//    @RabbitListener(bindings = @QueueBinding(
//            value =@Queue("onMessage.applyPersistence"),
//            exchange = @Exchange(value = "webSocket.onMessage",type = ExchangeTypes.TOPIC),
//            key ="apply.applyPersistence"
//    ))
//    public void listenApplyQueuePersistence(SendMsgDto sendMsgDto){
//        log.info("监听到好友请求，正在写入数据库,{}",sendMsgDto);
//        FriendRequest friendRequest = new FriendRequest();
//        friendRequest.setFromId(sendMsgDto.getFromId());
//        friendRequest.setToId(sendMsgDto.getToId());
//        friendRequest.setCreationTime(DateUtil.date());
//        friendRequest.setValidationMessage(sendMsgDto.getMsg());
//        friendRequestService.save(friendRequest);
//    }
//
//
//    /**
//     * 监听好友聊天,并发送
//     */
//    @RabbitListener(bindings = @QueueBinding(
//            value =@Queue("onMessage.chat"),
//            exchange = @Exchange(value = "webSocket.onMessage",type = ExchangeTypes.TOPIC),
//            key ="chat.#"
//    ))
//    public void listenChatQueue(SendMsgDto sendMsgDto){
//        log.info("监听到好友聊天，{}",sendMsgDto);
//        String exchangeName="webSocket.send";
//        String routingKey="send";
//        SingleChatMsgVo singleChatMsgVo = new SingleChatMsgVo();
//        singleChatMsgVo.setMsg(sendMsgDto.getMsg());
//        singleChatMsgVo.setFromId(sendMsgDto.getFromId());
//        singleChatMsgVo.setToId(sendMsgDto.getToId());
//        singleChatMsgVo.setSendingTime(DateUtil.now());
//        String headPortrait = userService.getHeadPortrait(sendMsgDto.getFromId());
//        singleChatMsgVo.setHeadPortrait(headPortrait);
//        rabbitTemplate.convertAndSend(exchangeName,routingKey,singleChatMsgVo);
//    }
//
//    /**
//     * 监听好友聊天,并写入数据库
//     */
//    @RabbitListener(bindings = @QueueBinding(
//            value =@Queue("onMessage.chatPersistence"),
//            exchange = @Exchange(value = "webSocket.onMessage",type = ExchangeTypes.TOPIC),
//            key ="chat.chatPersistence"
//    ))
//    public void listenChatQueuePersistence(SendMsgDto sendMsgDto){
//        log.info("监听到好友聊天，正在写入数据库,{}",sendMsgDto);
//        ChatRecord chatRecord = new ChatRecord();
//        chatRecord.setFromId(sendMsgDto.getFromId());
//        chatRecord.setToId(sendMsgDto.getToId());
//        chatRecord.setContent(sendMsgDto.getMsg());
//        chatRecordService.addSingleChatMsg(chatRecord);
//    }
}
