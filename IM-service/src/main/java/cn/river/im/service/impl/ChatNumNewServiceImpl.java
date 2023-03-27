package cn.river.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.river.im.entity.ChatNumNew;
import cn.river.im.service.ChatNumNewService;
import cn.river.im.mapper.ChatNumNewMapper;
import org.springframework.stereotype.Service;

/**
* @author 阳名
* @description 针对表【chat_num_new(未查看聊天信息数量表)】的数据库操作Service实现
* @createDate 2023-03-26 10:01:19
*/
@Service
public class ChatNumNewServiceImpl extends ServiceImpl<ChatNumNewMapper, ChatNumNew>
    implements ChatNumNewService{

    /**
     * 获取与当前用户的未查看新记录数量
     * @param uid
     * @param friendId
     * @return
     */
    @Override
    public int getChatNumItem(String uid, String friendId) {
        LambdaQueryWrapper<ChatNumNew> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatNumNew::getToId,uid).eq(ChatNumNew::getFromId,friendId).select(ChatNumNew::getNum);
        Integer num = this.baseMapper.selectOne(wrapper).getNum();
        return num;
    }

    /**
     * 查看所有未查看的新记录数量
     * @param uid
     * @return
     */
    @Override
    public int getChatNumSum(String uid) {
        LambdaQueryWrapper<ChatNumNew> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatNumNew::getToId,uid);
        int sum = this.baseMapper.selectList(wrapper).stream().mapToInt(ChatNumNew::getNum).sum();
        return sum;
    }

    /**
     * 修改未查看信息数量为0
     * @param uid
     * @param friendId
     * @return
     */
    @Override
    public boolean upNum(String uid, String friendId) {
        LambdaUpdateWrapper<ChatNumNew> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ChatNumNew::getToId,uid).eq(ChatNumNew::getFromId,friendId);
        ChatNumNew chatNumNew = new ChatNumNew();
        chatNumNew.setNum(0);
        if(this.baseMapper.update(chatNumNew,wrapper)==1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean addNum(String uid, String friendId) {
        LambdaQueryWrapper<ChatNumNew> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatNumNew::getFromId,uid).eq(ChatNumNew::getToId,friendId).select(ChatNumNew::getNum);
        ChatNumNew numNew = this.baseMapper.selectOne(queryWrapper);
        Integer num = numNew.getNum();
        LambdaUpdateWrapper<ChatNumNew> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ChatNumNew::getToId,friendId).eq(ChatNumNew::getFromId,uid);
        ChatNumNew chatNumNew = new ChatNumNew();
        chatNumNew.setNum(++num);
        if(this.baseMapper.update(chatNumNew,wrapper)==1){
            return true;
        }else {
            return false;
        }
    }
}




