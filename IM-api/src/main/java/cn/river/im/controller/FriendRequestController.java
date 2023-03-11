package cn.river.im.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.river.im.annotation.AuthCheck;
import cn.river.im.dto.FeedbackDto;
import cn.river.im.entity.Friend;
import cn.river.im.entity.FriendRequest;
import cn.river.im.entity.FriendRequestsFeedback;
import cn.river.im.enums.ApiCode;
import cn.river.im.exception.BusinessException;
import cn.river.im.local.LocalUser;
import cn.river.im.result.Result;
import cn.river.im.service.FriendRequestService;
import cn.river.im.service.FriendRequestsFeedbackService;
import cn.river.im.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/friendRequest")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    private final FriendRequestsFeedbackService feedbackService;
    private final FriendService friendService;
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

        if(friendRequestService.checkForPresence(friendRequest.getFromId(), friendRequest.getToId())){
            throw new BusinessException(ApiCode.FAIL.getCode(), "对方还未处理您的请求，请勿重复发送");
        }else {
            friendRequestService.save(friendRequest);
        }
        return Result.ok();
    }


    /**
     * 对收到的好友请求进行反馈
     * @param dto
     * @return
     */
    @PostMapping("/feedback")
    @AuthCheck
    public Result<Boolean> addFeedback(@RequestBody FeedbackDto dto){
        String uid = LocalUser.getUser().getId();
        dto.setCreationTime(DateUtil.date());
        FriendRequestsFeedback feedback = new FriendRequestsFeedback();
        BeanUtil.copyProperties(dto,feedback);
        feedback.setFromId(uid);
        feedbackService.save(feedback);

        //判断请求是否通过
        if(ObjectUtil.equal("0",dto.getIsAgree())){
            //向好友表插入一条记录
            Friend friend = new Friend();
            friend.setUserId(uid);
            friend.setFriendId(dto.getToId());
            friend.setRemarks(dto.getRemarks());
            friend.setCreationTime(DateUtil.date());
            if(friendService.checkWhetherItIsAFriend(friend.getUserId(), friend.getFriendId())){
                throw new BusinessException(ApiCode.FAIL.getCode(), "该用户已是您的好友");
            }else {
                friendService.save(friend);
            }
        }
        return Result.ok();
    }



}
