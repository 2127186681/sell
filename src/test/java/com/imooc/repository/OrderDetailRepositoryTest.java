package com.imooc.repository;

import com.imooc.pojo.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {
    @Autowired
    private OrderDetailRepository repository;
    @Test
    public void save(){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("123");
        orderDetail.setOrderId("123456");
        orderDetail.setProductIcon("http://XXX.com");
        orderDetail.setProductId("12345");
        orderDetail.setProductName("皮蛋粥");
        orderDetail.setProductPrice(new BigDecimal(1.2));
        orderDetail.setProductQuantity(1);
        OrderDetail resutl = repository.save(orderDetail);
    }

    @Test
    public void findByOrderId(){
        List<OrderDetail> result = repository.findByOrderId("123456");
        Assert.assertNotEquals(0,result.size());
    }
}