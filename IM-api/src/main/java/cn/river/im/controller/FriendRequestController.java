package cn.river.im.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.river.im.annotation.AuthCheck;
import cn.river.im.dto.FeedbackDto;
import cn.river.im.entity.*;
import cn.river.im.enums.ApiCode;
import cn.river.im.exception.BusinessException;
import cn.river.im.local.LocalUser;
import cn.river.im.result.Result;
import cn.river.im.service.ChatNumNewService;
import cn.river.im.service.FriendRequestService;
import cn.river.im.service.FriendRequestsFeedbackService;
import cn.river.im.service.FriendService;
import cn.river.im.vo.FRFeedbackVo;
import cn.river.im.vo.FriendRequestVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/friendRequest")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    private final FriendRequestsFeedbackService feedbackService;
    private final FriendService friendService;
    private final ChatNumNewService chatNumNewService;
    /**
     * 发送好友请求
     * @param friendRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck
    public Result<Boolean> addRequest(@RequestBody FriendRequest friendRequest){
        friendRequest.setCreationTime(DateUtil.date());
        friendRequest.setFromId(LocalUser.getUser().getId());
        if(friendService.checkWhetherItIsAFriend(friendRequest.getFromId(), friendRequest.getToId())){
            throw new BusinessException(ApiCode.FAIL.getCode(), "该用户已是您的好友");
        }
        if(friendRequestService.checkForPresence(friendRequest.getFromId(), friendRequest.getToId())){
            throw new BusinessException(ApiCode.FAIL.getCode(), "对方还未处理您的请求，请勿重复发送");
        }else {
            friendRequestService.save(friendRequest);
        }
        return Result.ok();
    }
    /**
     * 获取反馈信息列表
     */
    @GetMapping("/feedbackList")
    @AuthCheck
    public Result<List<FRFeedbackVo>> getFeedbackList(){
        User user = LocalUser.getUser();
        return Result.ok(feedbackService.getFeedbackList(user.getId()));
    }


    /**
     * 对收到的好友请求进行反馈
     * @param dto
     * @return
     */
    @PostMapping("/feedback")
    @AuthCheck
    public Result<Boolean> giveFeedback(@RequestBody FeedbackDto dto){
        String uid = LocalUser.getUser().getId();
        dto.setCreationTime(DateUtil.date());
        FriendRequestsFeedback feedback = new FriendRequestsFeedback();
        BeanUtil.copyProperties(dto,feedback);
        feedback.setFromId(uid);
        feedbackService.save(feedback);

        //判断请求是否通过
        if(0==dto.getIsAgree()){
            //向好友表插入一条记录
            Friend friend = new Friend();
            friend.setUserId(uid);
            friend.setFriendId(dto.getToId());
            //暂时取消备注模块
            friend.setRemarks(dto.getRemarks());
            friend.setCreationTime(DateUtil.date());
            if(friendService.checkWhetherItIsAFriend(friend.getUserId(), friend.getFriendId())){
                throw new BusinessException(ApiCode.FAIL.getCode(), "该用户已是您的好友");
            }else {
                friendService.save(friend);
                //插入好友表的同时在聊天记录数量表插入数据
                ChatNumNew num1 = new ChatNumNew();
                ChatNumNew num2 = new ChatNumNew();
                num1.setNum(0);
                num1.setFromId(Long.valueOf(friend.getFriendId()));
                num1.setToId(Long.valueOf(friend.getUserId()));
                num2.setNum(0);
                num2.setFromId(Long.valueOf(friend.getUserId()));
                num2.setToId(Long.valueOf(friend.getFriendId()));
                chatNumNewService.save(num1);
                chatNumNewService.save(num2);
            }
        }
        //无论同意还是拒绝都删除friendRequest表数据
        LambdaQueryWrapper<FriendRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FriendRequest::getToId,uid).eq(FriendRequest::getFromId,dto.getToId());
        friendRequestService.remove(wrapper);

        return Result.ok();
    }

    /**
     * 获取未处理好友请求数量
     */
    @GetMapping("/applyCount")
    @AuthCheck
    public Result<Long> getUnApplyCount(){
        LambdaQueryWrapper<FriendRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FriendRequest::getToId,LocalUser.getUser().getId());
        Long count = friendRequestService.getBaseMapper().selectCount(wrapper);
        return Result.ok(count);
    }

    /**
     * 获取好友请求列表
     */
    @GetMapping("/list")
    @AuthCheck
    public Result<List<FriendRequestVo>> getList(){
        User user = LocalUser.getUser();
        return Result.ok(friendRequestService.getFRList(user.getId()));
    }





}
