package cn.river.im.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.river.im.entity.ChatRecord;
import cn.river.im.entity.User;
import cn.river.im.mapper.ChatRecordMapper;
import cn.river.im.result.Result;
import cn.river.im.service.ChatNumNewService;
import cn.river.im.service.ChatRecordService;
import cn.river.im.service.UserService;
import cn.river.im.vo.ChatCardVo;
import cn.river.im.vo.ChatMsgItemVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 阳名
* @description 针对表【chat_record(聊天记录表)】的数据库操作Service实现
* @createDate 2022-11-03 15:25:26
*/
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord>
    implements ChatRecordService{

    private final ChatRecordMapper chatRecordMapper;
    private final UserService userService;
    private final ChatNumNewService chatNumNewService;
    /**
     * 插入一条聊天记录
     * 传入双方id,聊天内容
     * 设置当前时间
     * @param chatRecord
     * @return
     */
    @Override
    public boolean addSingleChatMsg(ChatRecord chatRecord) {
        chatRecord.setCreationTime(DateUtil.date());
        int insert = chatRecordMapper.insert(chatRecord);
        return insert>0?true:false;
    }

    @Override
    public List<ChatMsgItemVo> findRecordList(String toId, String uid) {
        LambdaQueryWrapper<ChatRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatRecord::getToId,toId).eq(ChatRecord::getFromId,uid).or().eq(ChatRecord::getToId,uid).eq(ChatRecord::getFromId,toId).orderByAsc(ChatRecord::getCreationTime);
        List<ChatRecord> list = chatRecordMapper.selectList(wrapper);
        List<ChatMsgItemVo> vos =new ArrayList<>();
        for (ChatRecord chatRecord : list) {
            ChatMsgItemVo vo = new ChatMsgItemVo();
            if(chatRecord.getFromId().equals(uid)){
                vo.setMe(true);
            }else {
                vo.setMe(false);
            }
            User user = userService.getById(chatRecord.getFromId());
            vo.setAvatar(user.getHeadPortrait());
            vo.setNickName(user.getNickName());
            vo.setTime(DateUtil.format(chatRecord.getCreationTime(),"yyyy/MM/dd HH:mm"));
            vo.setContent(chatRecord.getContent());
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<ChatCardVo> getChatObjectCardList(String uid) {
        //查询表，获取与当前用户有聊天记录的用户的id列表
        LambdaQueryWrapper<ChatRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatRecord::getToId,uid).or().eq(ChatRecord::getFromId,uid).select(ChatRecord::getToId,ChatRecord::getFromId);
        List<ChatRecord> recordList = chatRecordMapper.selectList(wrapper);
        List<ChatRecord> list = CollUtil.distinct(recordList, chatRecord -> {
            String fromId = chatRecord.getFromId();
            String toId = chatRecord.getToId();
            return fromId.compareTo(toId) <= 0 ? fromId + "_" + toId : toId + "_" + fromId;
        }, true);

        List<ChatCardVo> vos=new ArrayList<>();
        //遍历列表，查询当fromId与toId分别取用户id或聊天对象id时，最后的聊天时间，与聊天内容
        for (ChatRecord chatRecord : list) {
            ChatCardVo vo = new ChatCardVo();
            LambdaQueryWrapper<ChatRecord> wrapper1 = new LambdaQueryWrapper<>();
            if(uid.equals(chatRecord.getFromId())){
                int chatNumItem = chatNumNewService.getChatNumItem(uid, chatRecord.getToId());
                vo.setChatNumItem(chatNumItem);
                wrapper1.eq(ChatRecord::getFromId,uid).eq(ChatRecord::getToId,chatRecord.getToId()).select(ChatRecord::getCreationTime,ChatRecord::getContent).orderByDesc(ChatRecord::getCreationTime).last("limit 1");
                //获取聊天对象头像，昵称
                User user = userService.getById(chatRecord.getToId());
                vo.setObjectId(chatRecord.getToId());
                vo.setObjectName(user.getNickName());
                vo.setImgUrl(user.getHeadPortrait());
            }else {
                int chatNumItem = chatNumNewService.getChatNumItem(uid, chatRecord.getFromId());
                vo.setChatNumItem(chatNumItem);
                wrapper1.eq(ChatRecord::getToId,uid).eq(ChatRecord::getFromId,chatRecord.getFromId()).select(ChatRecord::getCreationTime,ChatRecord::getContent).orderByDesc(ChatRecord::getCreationTime).last("limit 1");
                //获取聊天对象头像，昵称
                User user = userService.getById(chatRecord.getFromId());
                vo.setObjectId(chatRecord.getFromId());
                vo.setObjectName(user.getNickName());
                vo.setImgUrl(user.getHeadPortrait());
            }
            ChatRecord record = chatRecordMapper.selectOne(wrapper1);
            vo.setLastContent(record.getContent());
            vo.setLastTime(DateUtil.format(record.getCreationTime(), "yyyy/MM/dd HH:mm"));
            vos.add(vo);
        }
        return vos;
    }
}




