package cn.river.im.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.river.im.entity.FriendRequestsFeedback;
import cn.river.im.entity.User;
import cn.river.im.mapper.FriendRequestsFeedbackMapper;
import cn.river.im.mapper.UserMapper;
import cn.river.im.service.FriendRequestsFeedbackService;
import cn.river.im.vo.FRFeedbackVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 阳名
* @description 针对表【friend_requests_feedback】的数据库操作Service实现
* @createDate 2022-11-07 16:49:03
*/
@Service
public class FriendRequestsFeedbackServiceImpl extends ServiceImpl<FriendRequestsFeedbackMapper, FriendRequestsFeedback>
    implements FriendRequestsFeedbackService{

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取反馈列表
     * @param uid
     * @return
     */
    @Override
    public List<FRFeedbackVo> getFeedbackList(String uid) {
        LambdaQueryWrapper<FriendRequestsFeedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FriendRequestsFeedback::getToId,uid);
        List<FriendRequestsFeedback> list = this.baseMapper.selectList(wrapper);
        List<FRFeedbackVo> vos=new ArrayList<>();

        for (FriendRequestsFeedback feedback : list) {
            FRFeedbackVo vo = new FRFeedbackVo();
            User user = userMapper.selectById(feedback.getFromId());
            vo.setHeadPortrait(user.getHeadPortrait());
            vo.setNickName(user.getNickName());
            vo.setFeedbackMsg(feedback.getFeedbackMsg());
            vo.setFromId(feedback.getFromId());
            vo.setTime(DateUtil.format(feedback.getCreationTime(),"yyyy/MM/dd HH:mm"));
            vo.setIsAgree(feedback.getIsAgree());
            vos.add(vo);
        }
        return vos;
    }
}




