package cn.river.im.local;

import cn.river.im.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局User
 */
public class LocalUser {
    private static ThreadLocal<Map<String,Object>> threadLocal =new ThreadLocal<>();


    /**
     * 设置全局User
     * @param user
     */
    public static void set(User user){
        HashMap<String, Object> map = new HashMap<>();
        map.put("user",user);
        LocalUser.threadLocal.set(map);
    }


    /**
     * 清除全局User
     */
    public static void clear(){
        LocalUser.threadLocal.remove();
    }

    /**
     * 获取user
     * @return
     */
    public static User getUser(){
        Map<String, Object> map = LocalUser.threadLocal.get();
        User user = (User) map.get("user");
        return user;
    }

}
