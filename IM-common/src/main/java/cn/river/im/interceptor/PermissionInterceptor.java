package cn.river.im.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.river.im.annotation.AuthCheck;
import cn.river.im.local.LocalUser;
import cn.river.im.constants.IMConstants;
import cn.river.im.entity.User;
import cn.river.im.enums.ApiCode;
import cn.river.im.exception.BusinessException;
import cn.river.im.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Value("${jwt.online}")
    private String onlineKey;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("请求被拦截");
        //获取注解对象
        Optional<AuthCheck> authCheck = this.getAuthCheck(handler);
        if (!authCheck.isPresent()){
            return true;//放行
        }

        //获取请求头的token
        String bearerToken = request.getHeader("Authorization");
        if(StrUtil.isEmpty(bearerToken)){
            throw new BusinessException(ApiCode.UNAUTHORIZED.getCode(), ApiCode.UNAUTHORIZED.getMessage());
        }
        if(!bearerToken.startsWith("Bearer")){
            throw new BusinessException(ApiCode.UNAUTHORIZED.getCode(), ApiCode.UNAUTHORIZED.getMessage());
        }
        //将Authorization的值转换成数组
        String[] tokens = bearerToken.split(" ");
        if(!(tokens.length==2)){
            throw new BusinessException(ApiCode.UNAUTHORIZED.getCode(), ApiCode.UNAUTHORIZED.getMessage());
        }
        String token=tokens[1];
        //验证token
        if(!JWTUtil.verify(token,onlineKey.getBytes())){
            throw new BusinessException(ApiCode.UNAUTHORIZED.getCode(), ApiCode.UNAUTHORIZED.getMessage());
        }
        //解析token,获取id
        JWT jwt = JWTUtil.parseToken(token);
        String uid = (String) jwt.getPayload("uid");
        //查询redis获取用户信息
        User user = redisUtils.get(IMConstants.IM_REDIS_ONLINE_TOKEN + uid + ":" + token, User.class);
        if(BeanUtil.isEmpty(user)){
            //身份过期
            throw new BusinessException(ApiCode.TOKEN_EXPIRATION.getCode(), ApiCode.TOKEN_EXPIRATION.getMessage());
        }
        //比较权限
        if(this.hasPermission(authCheck.get(),user.getPower())){
            LocalUser.set(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LocalUser.clear();
        System.out.println("请求执行完成");
    }

    /**
     * 获取注解对象
     */
    private Optional<AuthCheck> getAuthCheck(Object handler){
        //判断接收参数是否是HandlerMethod的对象或者子类对象
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod=(HandlerMethod) handler;
            AuthCheck authCheck = handlerMethod.getMethod().getAnnotation(AuthCheck.class);
            if(authCheck==null){
                return Optional.empty();
            }
            return Optional.of(authCheck);
        }
        return Optional.empty();
    }

    /**
     * 比较权限
     */
    private boolean hasPermission(AuthCheck authCheck,Integer power){
        int value = authCheck.value();
        if(value>power){
            throw new BusinessException(ApiCode.NOT_PERMISSION.getCode(),ApiCode.NOT_PERMISSION.getMessage());
        }
        return true;
    }
}
