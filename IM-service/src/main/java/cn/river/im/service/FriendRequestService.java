package cn.river.im.service;

import cn.river.im.entity.FriendRequest;
import cn.river.im.entity.User;
import cn.river.im.vo.FriendRequestVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 阳名
* @description 针对表【friend_request(好友请求表)】的数据库操作Service
* @createDate 2022-11-07 16:48:57
*/
public interface FriendRequestService extends IService<FriendRequest> {
    boolean checkForPresence(String toId,String fromId);
    List<FriendRequestVo> getFRList(String uid);
}
