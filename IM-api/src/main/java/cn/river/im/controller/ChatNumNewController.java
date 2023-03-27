package cn.river.im.controller;

import cn.river.im.annotation.AuthCheck;
import cn.river.im.local.LocalUser;
import cn.river.im.result.Result;
import cn.river.im.service.ChatNumNewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/chatNum")
public class ChatNumNewController {
    private final ChatNumNewService chatNumNewService;

    /**
     * 获取未查看信息的数量
     */
    @GetMapping("/sum")
    @AuthCheck
    public Result<Integer> getSum(){
        String uid = LocalUser.getUser().getId();
        int numSum = chatNumNewService.getChatNumSum(uid);
        return Result.ok(numSum);
    }

    /**
     * 将与当前用户的未查看新纪录清零
     */
    @GetMapping("/upNum")
    @AuthCheck
    public Result<Boolean> upNum(@RequestParam("friendId") String friendId){
        String uid = LocalUser.getUser().getId();
        return Result.ok(chatNumNewService.upNum(uid,friendId));
    }
    /**
     * 将聊天数量加一
     */
    @GetMapping("/addNum")
    @AuthCheck
    public Result<Boolean> addNum(@RequestParam("friendId") String friendId){
        String uid = LocalUser.getUser().getId();
        return Result.ok(chatNumNewService.addNum(uid,friendId));
    }

}
