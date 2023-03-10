package cn.river.im.service;

import cn.hutool.json.JSONObject;
import cn.river.im.entity.ChatRecord;
import cn.river.im.vo.ChatCardVo;
import cn.river.im.vo.ChatMsgItemVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 阳名
* @description 针对表【chat_record(聊天记录表)】的数据库操作Service
* @createDate 2022-11-03 15:25:26
*/
public interface ChatRecordService extends IService<ChatRecord> {
    boolean addSingleChatMsg(ChatRecord chatRecord);

    List<ChatMsgItemVo> findRecordList(String toId, String uid);

    List<ChatCardVo> getChatObjectCardList(String uid);
}
