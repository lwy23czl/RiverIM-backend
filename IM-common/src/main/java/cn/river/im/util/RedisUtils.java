package cn.river.im.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.river.im.enums.ApiCode;
import cn.river.im.exception.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisUtils {

    private final StringRedisTemplate stringRedisTemplate;

    /*存入redis*/
    public boolean set(String key,Object value){
        //序列化
        String jsonStr = JSONUtil.toJsonStr(value);
        try {
            stringRedisTemplate.opsForValue().set(key,jsonStr);
        }catch (Exception e){
            throw new SystemException(ApiCode.REDIS_EXCEPTION.getCode(), ApiCode.REGISTER_EXCEPTION.getMessage());
        }
        return true;
    }

    /*存入redis并设置过期时间,单位默认秒*/
    public boolean set(String key,Object value,long time){
        //将值进行序列化
        String json = JSONUtil.toJsonStr(value);
        if(time>0L){
            try {
                stringRedisTemplate.opsForValue().set(key,json,time, TimeUnit.SECONDS);
            }catch (Exception e){
                throw new SystemException(ApiCode.REDIS_EXCEPTION.getCode(), ApiCode.REGISTER_EXCEPTION.getMessage());
            }
        }else {
            this.set(key,value);
        }
        return true;
    }

    /*存入redis并设置过期时间,自行设置时间单位*/
    public boolean set(String key,Object value,long time,TimeUnit timeUnit){
        //将值进行序列化
        String json = JSONUtil.toJsonStr(value);
        if(time>0L){
            try {
                stringRedisTemplate.opsForValue().set(key,json,time, timeUnit);
            }catch (Exception e){
                throw new SystemException(ApiCode.REDIS_EXCEPTION.getCode(), ApiCode.REGISTER_EXCEPTION.getMessage());
            }
        }else {
            this.set(key,value);
        }
        return true;
    }

    /*根据key获取值(String)*/
    public String get(String key){
        try {
            String value = stringRedisTemplate.opsForValue().get(key);
            return value;
        }catch (Exception e){
            throw new SystemException(ApiCode.REDIS_EXCEPTION.getCode(), ApiCode.REGISTER_EXCEPTION.getMessage());
        }
    }

    /*根据key获取值(Object)并进行反序列化*/
    public <T> T get(String key,Class<T> beanClass){
        try {
            String json = stringRedisTemplate.opsForValue().get(key);
            return JSONUtil.toBean(json, beanClass);
        }catch (Exception e){
            throw new SystemException(ApiCode.REDIS_EXCEPTION.getCode(), ApiCode.REGISTER_EXCEPTION.getMessage());
        }
    }

    /*删除*/
    public boolean del(String key){
        try {
            Boolean delete = stringRedisTemplate.delete(key);
            return delete;
        }catch (Exception e){
            throw new SystemException(ApiCode.REDIS_EXCEPTION.getCode(), ApiCode.REGISTER_EXCEPTION.getMessage());
        }
    }

    /*多级目录的模糊删除,输入目录删除该目录下的所有key*/
    public boolean likeDel(String catalogue){
        try {
            Set<String> keys = stringRedisTemplate.keys(catalogue+":" + "*");
            Long delete = stringRedisTemplate.delete(keys);
            return delete>0?true:false;
        }catch (Exception e){
            throw new SystemException(ApiCode.REDIS_EXCEPTION.getCode(), ApiCode.REGISTER_EXCEPTION.getMessage());
        }
    }

    /*判断key是否存在*/
    public boolean isExist(String key){
        try {
            Boolean hasKey = stringRedisTemplate.hasKey(key);
            return hasKey;
        }catch (Exception e){
            throw new SystemException(ApiCode.REDIS_EXCEPTION.getCode(), ApiCode.REGISTER_EXCEPTION.getMessage());
        }
    }


}
