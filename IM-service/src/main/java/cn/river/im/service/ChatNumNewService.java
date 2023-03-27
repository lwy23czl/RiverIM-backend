package cn.river.im.service;

import cn.river.im.entity.ChatNumNew;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 阳名
* @description 针对表【chat_num_new(未查看聊天信息数量表)】的数据库操作Service
* @createDate 2023-03-26 10:01:19
*/
public interface ChatNumNewService extends IService<ChatNumNew> {
    int getChatNumItem(String uid,String friendId);
    int getChatNumSum(String uid);
    boolean upNum(String uid,String friendId);
    boolean addNum(String uid,String friendId);

}
