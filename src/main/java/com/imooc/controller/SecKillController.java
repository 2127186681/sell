package com.imooc.controller;

import com.imooc.service.SecKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品秒杀
 */
@RestController
@RequestMapping("/skill")
@Slf4j
/**
 * 查询秒杀活动特价商品信息
 */
public class SecKillController {
    /**
     *
     */
    @Autowired
    private SecKillService secKillService;
    @GetMapping("/query/{productId}")
    public String query(@PathVariable("productId")String productId){
        return secKillService.querySecKillProductInfo(productId);
    }

    @GetMapping("/order/{productId}")
    public String order(@PathVariable("productId")String productId){
        log.info("@skill request,productId:" +productId);
        secKillService.orderSecKillProductDiffUser(productId);
        return secKillService.querySecKillProductInfo(productId);
    }
}
