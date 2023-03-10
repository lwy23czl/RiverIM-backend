package cn.river.im.controller;

import cn.hutool.core.date.DateUtil;
import cn.river.im.annotation.AuthCheck;
import cn.river.im.entity.Friend;
import cn.river.im.enums.ApiCode;
import cn.river.im.exception.BusinessException;
import cn.river.im.local.LocalUser;
import cn.river.im.result.Result;
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
@RequestMapping("/friend")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/add")
    @AuthCheck
    public Result<Boolean> addFriend(@RequestBody Friend friend){
        friend.setCreationTime(DateUtil.date());
        friend.setUserId(LocalUser.getUser().getId());
        if(friendService.checkWhetherItIsAFriend(friend.getUserId(), friend.getFriendId())){
            throw new BusinessException(ApiCode.FAIL.getCode(), "该用户已是您的好友");
        }else {
            friendService.save(friend);
        }
        return Result.ok();
    }



}
