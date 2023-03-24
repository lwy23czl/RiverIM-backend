package cn.river.im.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.river.im.annotation.AuthCheck;
import cn.river.im.entity.FriendRequest;
import cn.river.im.entity.User;
import cn.river.im.mapper.FriendRequestMapper;
import cn.river.im.mapper.UserMapper;
import cn.river.im.service.FriendRequestService;
import cn.river.im.vo.FriendRequestVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 阳名
* @description 针对表【friend_request(好友请求表)】的数据库操作Service实现
* @createDate 2022-11-07 16:48:57
*/
@Service
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestMapper, FriendRequest>
    implements FriendRequestService{

    @Autowired
    private UserMapper userMapper;
    /**
     * 检查是否发送过好友请求，还未被处理
     * @param toId
     * @param fromId
     * @return
     */
    @Override
    public boolean checkForPresence(String fromId, String toId) {
        LambdaQueryWrapper<FriendRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FriendRequest::getFromId,fromId).eq(FriendRequest::getToId,toId);
        Long aLong = this.baseMapper.selectCount(wrapper);
        if(aLong>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<FriendRequestVo> getFRList(User user) {
        //根据toId获取列表
        LambdaQueryWrapper<FriendRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FriendRequest::getToId,user.getId());
        List<FriendRequest> requestList = this.baseMapper.selectList(wrapper);
        List<FriendRequestVo> vos=new ArrayList<>();
        for (FriendRequest friendRequest : requestList) {
            FriendRequestVo vo = new FriendRequestVo();
            vo.setFromId(friendRequest.getFromId());
            vo.setTime(DateUtil.format(friendRequest.getCreationTime(),"yyyy/MM/dd HH:mm"));
            vo.setValidationMessage(friendRequest.getValidationMessage());
            User selectById = userMapper.selectById(friendRequest.getFromId());
            vo.setNickName(selectById.getNickName());
            vo.setHeadPortrait(selectById.getHeadPortrait());
            vos.add(vo);
        }
        return vos;
    }
}




