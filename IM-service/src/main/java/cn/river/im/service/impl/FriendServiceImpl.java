package cn.river.im.service.impl;

import cn.river.im.entity.Friend;
import cn.river.im.mapper.FriendMapper;
import cn.river.im.service.FriendService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 阳名
* @description 针对表【friend(好友关系表)】的数据库操作Service实现
* @createDate 2022-11-03 15:25:46
*/
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
    implements FriendService{


    private final FriendMapper friendMapper;

    @Override
    public boolean checkWhetherItIsAFriend(String userId, String friendId) {
        LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Friend::getFriendId,friendId).eq(Friend::getUserId,userId);
        Long aLong = friendMapper.selectCount(wrapper);
        if(aLong>0){
            return false;
        }else {
            return true;
        }

    }
}




