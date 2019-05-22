package com.imooc.service.impl;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.BuyerService;
import com.imooc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {
    @Autowired
    private OrderService orderService;
    @Override
    public OrderDTO findOrderOne(String openid, String orderId) {
        return checkOrderOwner(openid,orderId);
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderId) {
        OrderDTO orderDTO = checkOrderOwner(openid,orderId);
        if(orderDTO == null){
            log.error("[取消订单] 订单不存组 orderod={}",orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        return orderService.cancel(orderDTO);
    }
    //校验当前用户与订单用户是否一致
    private OrderDTO checkOrderOwner(String openid, String orderId){
        OrderDTO orderDTO = orderService.findOne(orderId);
        if(orderDTO == null){
            return null;
        }
        if(!orderDTO.getBuyerOpenid().equals(orderId)){
            log.error("[查询订单] 订单openid不一致  orderId={},orderdto={}",openid,orderDTO);
            throw new SellException(ResultEnum.ORDER_OWNER_EMPTY);
        }

        return orderDTO;

    }
}
