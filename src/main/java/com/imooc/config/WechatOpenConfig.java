package com.imooc.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 配置开放平台
 */
@Component
public class WechatOpenConfig {
    @Autowired
    private WechatAccountConfig accountConfig;
    @Bean
    public WxMpService wxOpenService(){
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxOpenConfigStorage());
        return wxMpService;

    }

    @Bean
    public WxMpConfigStorage wxOpenConfigStorage(){
        WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpInMemoryConfigStorage.setAppId(accountConfig.getOpenAppId());
        wxMpInMemoryConfigStorage.setSecret(accountConfig.getOpenAppSecret());
        return wxMpInMemoryConfigStorage;
    }
}
