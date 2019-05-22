package com.imooc.service;

import com.imooc.dto.OrderDTO;
import com.imooc.pojo.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    //创建订单
    OrderDTO create(OrderDTO orderDTO);
    //查询单个订单
    OrderDTO findOne(String orderId);
    //查寻订单列表
    Page<OrderDTO> findList(String openId, Pageable pageable);
    //取消订单
    OrderDTO cancel(OrderDTO orderDTO);
    //完结订单
    OrderDTO finish(OrderDTO orderDTO);
    //支付订单
    OrderDTO pay(OrderDTO orderDTO);
    //查寻所有订单分页展示  卖家端
    Page<OrderDTO> findList( Pageable pageable);
}
