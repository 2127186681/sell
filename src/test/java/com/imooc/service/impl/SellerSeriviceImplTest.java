package com.imooc.service.impl;

import com.imooc.pojo.SellerInfo;
import com.imooc.service.SellerSerivice;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SellerSeriviceImplTest {
    @Autowired
    private SellerSerivice serivice;

    @Test
    public void findSellerInfoByOrderId() {
          SellerInfo sellerInfo =  serivice.findSellerInfoByOrderId("123");
        Assert.assertEquals("123",sellerInfo.getOpenid());
    }
}