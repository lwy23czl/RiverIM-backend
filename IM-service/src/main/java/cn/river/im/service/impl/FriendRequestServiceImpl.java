package cn.river.im.service.impl;

import cn.river.im.entity.FriendRequest;
import cn.river.im.mapper.FriendRequestMapper;
import cn.river.im.service.FriendRequestService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author 阳名
* @description 针对表【friend_request(好友请求表)】的数据库操作Service实现
* @createDate 2022-11-07 16:48:57
*/
@Service
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestMapper, FriendRequest>
    implements FriendRequestService{

    /**
     * 检查是否发送过好友请求，还未被处理
     * @param toId
     * @param fromId
     * @return
     */
    @Override
    public boolean checkForPresence(String toId, String fromId) {
        LambdaQueryWrapper<FriendRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FriendRequest::getFromId,fromId).eq(FriendRequest::getToId,toId);
        Long aLong = this.baseMapper.selectCount(wrapper);
        if(aLong>0){
            return false;
        }else {
            return true;
        }
    }
}




