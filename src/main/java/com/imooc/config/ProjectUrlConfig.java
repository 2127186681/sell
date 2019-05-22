package com.imooc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
//
@Data
@ConfigurationProperties(prefix = "projectUrl")
@Component
public class ProjectUrlConfig {
    //微信公众平台授权url
    private String wxChatMpAuthroize;
    //微信开放平台收取Url
    private String wxChatOpenAuthroize;
    //点餐系统url
    private String sell;
}
