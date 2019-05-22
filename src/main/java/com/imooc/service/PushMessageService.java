package com.imooc.service;

import com.imooc.dto.OrderDTO;

/**
 * 消息推送
 */
public interface PushMessageService {
    //订单状态更改消息
    void orderStatus(OrderDTO orderDTO);

}
