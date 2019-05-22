package com.imooc.controller;

import com.imooc.config.ProjectUrlConfig;
import com.imooc.constant.CookieConstant;
import com.imooc.constant.RedisContant;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.pojo.SellerInfo;
import com.imooc.service.SellerSerivice;
import com.imooc.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 卖家用户
 */
@Controller
@Slf4j
@RequestMapping("/seller")
public class SellerUserController {
    @Autowired
    private ProjectUrlConfig projectUrlConfig;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SellerSerivice sellerSerivice;

    @GetMapping("/login")
    public ModelAndView  login(@RequestParam("openId")String openId,
                       HttpServletResponse response,
                       Map<String,Object> map){
        //1.将拿到的openid和数据库中的openid匹配
        SellerInfo sellerInfo =  sellerSerivice.findSellerInfoByOrderId(openId);
        if(sellerInfo == null){
            log.error("[卖家登陆] 卖家信息不存在 sellerInfo={}",sellerInfo);
            map.put("msg",ResultEnum.LOGIN_FAIL.getMessage());
            map.put("url","/sell/seller/order/List");
            return new ModelAndView("common/error",map);

        }
        //2。设置token到redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisContant.EXPIRE;           //redis的 key                  value  过期时间   时间类型：秒
        redisTemplate.opsForValue().set(String.format(RedisContant.TOKEN_PREFIX,token),openId,expire, TimeUnit.SECONDS);

        //3.设置token到cookie
       /* Cookie cookie = new Cookie("token",token);
        cookie.setPath("/");
        cookie.setMaxAge(7200);//cookie过期时间
        response.addCookie(cookie);//响应到本地*/
        CookieUtil.setCookie(response, CookieConstant.Token,token,expire);
        return new ModelAndView("redirect:"+projectUrlConfig.getSell()+"/sell/seller/order/List");
    }
    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Map<String,Object> map){
            //获取cookie
            Cookie cookie =  CookieUtil.getCookie(request,CookieConstant.Token);

            if(cookie!= null){
                //清楚redis
                redisTemplate.opsForValue().getOperations().delete(String.format(RedisContant.TOKEN_PREFIX,cookie.getValue()));
                //清除cookie
                CookieUtil.setCookie(response,CookieConstant.Token,null,0);
            }
            map.put("msg",ResultEnum.LOGOUT_SUCCESS.getMessage());
            map.put("url","/sell/seller/order/List");
            return new ModelAndView("common/success",map);

    }
}
