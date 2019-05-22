package com.imooc.aspect;

import com.imooc.constant.CookieConstant;
import com.imooc.constant.RedisContant;
import com.imooc.exception.SellerAutroizeException;
import com.imooc.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户访问权限校验
 */
@Aspect
@Component
@Slf4j
public class SellerAuthorizeAspect {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Pointcut("execution(public * com.imooc.controller.Sell*.*(..))"+
            "&& !execution(public * com.imooc.controller.SellerUserController.*(..))")//配置切入点
    public void verify(){};

    //具体的实现
    @Before("verify()") //切入点之前执行操作
    public void doVerify(){
    //获取request
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //查询cookie
        Cookie cookie = CookieUtil.getCookie(request, CookieConstant.Token);
        if(cookie==null){
            log.info("[登陆校验] cookie中查不到token");
            throw new SellerAutroizeException();
        }

        //redis中查询
        String tokenValue = redisTemplate.opsForValue().get(String.format(RedisContant.TOKEN_PREFIX,cookie.getValue()));
        if(StringUtils.isEmpty(tokenValue)){
        log.info("[登陆校验] redis没有token");
        throw new SellerAutroizeException();
        }
    }
}
