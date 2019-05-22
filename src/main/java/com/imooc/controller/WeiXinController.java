package com.imooc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeiXinController {
    private String code= "061CZQu62BDvEQ0vZ0r62PXUu62CZQu0";
    @GetMapping("/auth")
    public void auth(@RequestParam("code")String code){
        log.info("auth--------");
       log.info("code={}",code);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx2e337d5ca4237d8b&secret=8d1f9980e1ac84ea02a12ad18daf6fac&code="+code+"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url,String.class);
        log.info("response={}",response);
    }
}
