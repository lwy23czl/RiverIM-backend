package cn.river.im.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.river.im.entity.Friend;
import cn.river.im.entity.User;
import cn.river.im.mapper.FriendMapper;
import cn.river.im.service.FriendService;
import cn.river.im.service.UserService;
import cn.river.im.vo.ContactVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private final UserService userService;

    /**
     * 判断对方是否已是自己好友
     * @param userId
     * @param friendId
     * @return
     */
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

    @Override
    public List<ContactVo> getFriendList(String uid) {
        LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Friend::getUserId,uid).or().eq(Friend::getFriendId,uid).select(Friend::getUserId,Friend::getFriendId,Friend::getRemarks);
        List<Friend> friendList = friendMapper.selectList(wrapper);
        List<ContactVo> vos =new ArrayList<>();
        for (Friend friend : friendList) {
            ContactVo vo = new ContactVo();
            if(friend.getFriendId().equals(uid)){
                User userData = userService.getById(friend.getUserId());
                vo.setHeadPortrait(userData.getHeadPortrait());
                vo.setId(friend.getUserId());
                if(StrUtil.isEmpty(friend.getRemarks())){
                    vo.setNickName(userData.getNickName());
                }else {
                    vo.setNickName(friend.getRemarks());
                }
            }else {
                User userData = userService.getById(friend.getFriendId());
                vo.setHeadPortrait(userData.getHeadPortrait());
                vo.setId(friend.getFriendId());
                if(StrUtil.isEmpty(friend.getRemarks())){
                    vo.setNickName(userData.getNickName());
                }else {
                    vo.setNickName(friend.getRemarks());
                }
            }
            //设置假的在线信息，需要从redis中取在线人数再进行比对
            vo.setStatus("在线");
            vos.add(vo);


        }
        return vos;
    }
}




