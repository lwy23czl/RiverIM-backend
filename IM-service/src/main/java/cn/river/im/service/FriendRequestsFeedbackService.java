package cn.river.im.service;

import cn.river.im.entity.FriendRequestsFeedback;
import cn.river.im.vo.FRFeedbackVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 阳名
* @description 针对表【friend_requests_feedback】的数据库操作Service
* @createDate 2022-11-07 16:49:03
*/
public interface FriendRequestsFeedbackService extends IService<FriendRequestsFeedback> {
    List<FRFeedbackVo> getFeedbackList(String uid);
}
