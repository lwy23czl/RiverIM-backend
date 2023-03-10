package cn.river.im.controller;

import cn.hutool.json.JSONObject;
import cn.river.im.annotation.AuthCheck;
import cn.river.im.entity.ChatRecord;
import cn.river.im.entity.User;
import cn.river.im.local.LocalUser;
import cn.river.im.result.Result;
import cn.river.im.service.ChatRecordService;
import cn.river.im.service.UserService;
import cn.river.im.vo.ChatCardVo;
import cn.river.im.vo.ChatMsgItemVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/chat")
public class ChatRecordController {

    private final ChatRecordService chatRecordService;
    private final UserService userService;
    /**
     * 获取聊天记录
     */
    @GetMapping("/list")
    @AuthCheck
    public Result<List<ChatMsgItemVo>> getChatList(@RequestParam("toId")String toId){
        String uid = LocalUser.getUser().getId();
        List<ChatMsgItemVo> recordList = chatRecordService.findRecordList(toId, uid);


        return Result.ok(recordList);
    }

    /**
     * 获取聊天对象卡片列表
     */
    @GetMapping("/cardList")
    @AuthCheck
    public Result<List<ChatCardVo>> getChatObjectCardList(){
        String uid = LocalUser.getUser().getId();
        List<ChatCardVo> list = chatRecordService.getChatObjectCardList(uid);
        return Result.ok(list);
    }
}
