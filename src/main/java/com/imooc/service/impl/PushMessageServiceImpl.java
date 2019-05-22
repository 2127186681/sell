package com.imooc.service.impl;

import com.imooc.config.WechatAccountConfig;
import com.imooc.dto.OrderDTO;
import com.imooc.service.PushMessageService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 微信模板消息推送
 */
@Service
@Slf4j
public class PushMessageServiceImpl implements PushMessageService {
    @Autowired
    private WxMpService wxMpService;//使用第三方sdk推送消息

    @Autowired
    private WechatAccountConfig accountConfig;
    @Override
    public void orderStatus(OrderDTO orderDTO){
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();//创建消息模板
        templateMessage.setTemplateId(accountConfig.getTemplateId().get("orderStatus")); //设置模板ID
        templateMessage.setToUser("o46hG56TI-mWcnxryFMzk5Fawhh8");//发送对象的openid
        List<WxMpTemplateData> data = Arrays.asList(           //设置模板消息内容
                new WxMpTemplateData("first","亲，记得收货呦"),
                new WxMpTemplateData("keyword1","微信点餐"),
                new WxMpTemplateData("keyword2","1345354616"),
                new WxMpTemplateData("keyword3",orderDTO.getOrderId()),
                new WxMpTemplateData("keyword4",orderDTO.getOrderStatusEnum().getMessage()),
                new WxMpTemplateData("keyword5","￥"+orderDTO.getOrderAmount()),
                new WxMpTemplateData("remark","欢迎再次光临")
        );
        templateMessage.setData(data); //将模板消息内容设置到模板中
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);//发送模板消息
        }catch (WxErrorException e){
            log.error("[微信模板消息] 发送失败 e={}",e);

        }


    }
}
