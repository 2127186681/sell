package com.imooc.service.impl;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.pojo.OrderDetail;
import com.imooc.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {

    private final String orderId = "1555036843860598598";
    private final  String openID = "11011";
    @Autowired
    private OrderServiceImpl orderService;
    private final  String OPENID = "11011";
    @Test
    public void create() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("李师兄");
        orderDTO.setBuyerAddress("慕课网");
        orderDTO.setBuyerOpenid(OPENID);
        orderDTO.setBuyerPhone("13542156");

        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProductQuantity(2);
        orderDetail.setProductId("123456789");

        OrderDetail o2 = new OrderDetail();
        o2.setProductQuantity(2);
        o2.setProductId("12345");
        orderDetailList.add(orderDetail);
        orderDetailList.add(o2);

        orderDTO.setOrderDetailList(orderDetailList);
        OrderDTO result = orderService.create(orderDTO);

        log.info("[订单创建] result ={}",result);
    }

    @Test
    public void findOne() {
        OrderDTO orderDTO =  orderService.findOne("1");
        log.info("查询单个订单 result={}",orderDTO);
       /* Assert.assertEquals(orderId,orderDTO.getOrderId());*/
    }

    @Test
    public void findList() {
        PageRequest pageRequest = new PageRequest(0,2);
        Page<OrderDTO> orderDTOPage = orderService.findList(openID,pageRequest);

        Assert.assertNotEquals(0,orderDTOPage.getTotalElements());
    }


    @Test
    public void cancel() {
        OrderDTO orderDTO =  orderService.findOne(orderId);
        OrderDTO  result = orderService.cancel(orderDTO);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(),result.getOrderStatus());
    }

    @Test
    public void finish() {
        OrderDTO orderDTO =  orderService.findOne(orderId);
        OrderDTO  result = orderService.finish(orderDTO);
        Assert.assertEquals(OrderStatusEnum.FINISHEN.getCode(),result.getOrderStatus());
    }

    @Test
    public void pay() {
        OrderDTO orderDTO =  orderService.findOne(orderId);
        OrderDTO  result = orderService.pay(orderDTO);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(),result.getPayStatus());
    }

    @Test
    public void findall(){
        PageRequest pageRequest = new PageRequest(0,10);
        Page<OrderDTO> orderDTOPage =  orderService.findList(pageRequest);
        //Assert.assertNotEquals(0,orderDTOPage.getTotalElements());
        Assert.assertTrue("查询所有订单",orderDTOPage.getTotalElements()>0);//第一个参数是信息，第二个参数是判断条件，trun则通过测试
    }
}