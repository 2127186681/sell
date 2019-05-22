package com.imooc.controller;

import com.imooc.config.ProjectUrlConfig;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WecharController {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;
    @Autowired
    private WxMpService wxOpenService;

    @Autowired
    private WxMpService wxMpService;
    @GetMapping("/authorize") //微信平台获取openid
    public String  authorize(@RequestParam("returnUrl")String returnUrl){
        String url = projectUrlConfig+"/sell/wechat/userInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_BASE, URLEncoder.encode(returnUrl));
        log.info("[微信网页授权] 获取code,result={}",redirectUrl);
        return "redirect:"+redirectUrl;
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code")String code,
                           @RequestParam("state")String retureUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try{
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        }catch (WxErrorException e){
            log.error("[微信网页授权] {}",e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(),e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("openid={}",openId);
        return "redirect:" + retureUrl + "?openid="+openId;
    }

    @GetMapping("/qrAuthorize") //扫描二维码获取openid  微信开放平台
    public String qrAuthorize(@RequestParam("returnUrl")String returnUrl){
        String url = projectUrlConfig+"/sell/wechat/qrUerInfo";
        String redirect = wxOpenService.buildQrConnectUrl(url,WxConsts.QRCONNECT_SCOPE_SNSAPI_LOGIN,URLEncoder.encode(returnUrl));
        return "redirect:"+redirect;
    }

    @GetMapping("/qrUerInfo")
    public String qrUerInfo(@RequestParam("code")String code,
                             @RequestParam("state")String retureUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try{
            wxMpOAuth2AccessToken =  wxOpenService.oauth2getAccessToken(code);

        }catch (WxErrorException e){
            log.error("[微信扫码授权] {}",e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(),e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("openid={}",openId);
        return "redirect:" + retureUrl + "?openid="+openId;

    }
}
