package cn.river.im.service;

import cn.river.im.entity.Friend;
import cn.river.im.vo.ContactVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 阳名
* @description 针对表【friend(好友关系表)】的数据库操作Service
* @createDate 2022-11-03 15:25:46
*/
public interface FriendService extends IService<Friend> {
    boolean checkWhetherItIsAFriend(String userId,String friendId);
    List<ContactVo> getFriendList(String uid);
}
