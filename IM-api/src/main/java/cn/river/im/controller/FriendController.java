package cn.river.im.controller;

import cn.hutool.core.date.DateUtil;
import cn.river.im.annotation.AuthCheck;
import cn.river.im.entity.Friend;
import cn.river.im.entity.User;
import cn.river.im.enums.ApiCode;
import cn.river.im.exception.BusinessException;
import cn.river.im.local.LocalUser;
import cn.river.im.result.Result;
import cn.river.im.service.FriendService;
import cn.river.im.vo.ContactVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/friend")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendController {

    private final FriendService friendService;



    /**
     * 获取好友列表
     * @return
     */
    @GetMapping("/list")
    @AuthCheck
    public Result<List<ContactVo>> getFriendList(){
        String uid = LocalUser.getUser().getId();
        List<ContactVo> friendList = friendService.getFriendList(uid);
        return Result.ok(friendList);
    }






}
