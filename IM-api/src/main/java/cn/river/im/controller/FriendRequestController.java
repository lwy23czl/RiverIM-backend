package cn.river.im.controller;

import cn.hutool.core.date.DateUtil;
import cn.river.im.annotation.AuthCheck;
import cn.river.im.dto.FriendRequestDto;
import cn.river.im.entity.FriendRequest;
import cn.river.im.enums.ApiCode;
import cn.river.im.exception.BusinessException;
import cn.river.im.local.LocalUser;
import cn.river.im.result.Result;
import cn.river.im.service.FriendRequestService;
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
}
