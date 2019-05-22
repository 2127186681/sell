package com.imooc.service.impl;

import com.imooc.enums.ProductStatusEnum;
import com.imooc.pojo.ProductInfo;
import com.imooc.service.ProductInfoService;
import com.sun.xml.internal.ws.policy.AssertionSet;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoServiceImplTest {
    @Autowired
    private ProductInfoService productInfoService;
    @Test
    public void findOne() {
        ProductInfo productInfo = productInfoService.findOne("123456");
        Assert.assertEquals("123456",productInfo.getProductId());
    }

    @Test
    public void findUpAll() {
        List<ProductInfo> list = productInfoService.findUpAll();
        Assert.assertNotEquals(0,list.size());
    }

    @Test
    public void findAll() {
        PageRequest pageRequest = new PageRequest(0,2);
        Page<ProductInfo> page = productInfoService.findAll(pageRequest);
        List<ProductInfo> productInfoList =  page.getContent();
        String message =  productInfoList.get(0).getProductStatusEnum().getMessage();
       /* System.out.println(page.getTotalElements());*/
        Assert.assertNotEquals(0,page.getTotalElements());
    }

    @Test
    public void save() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("12345");
        productInfo.setProductName("小龙虾");
        productInfo.setProductPrice(new BigDecimal(3.2));
        productInfo.setProductDescription("好吃的粥");
        productInfo.setProductIcon("http//:aaaa.com");
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        productInfo.setCategoryType(2);
        productInfo.setProductStock(100);
        ProductInfo result = productInfoService.save(productInfo);
        Assert.assertNotEquals(null,result);
    }

    @Test
    public void onSale(){
       ProductInfo productInfo = productInfoService.onSale("123");
       Assert.assertEquals(ProductStatusEnum.UP,productInfo.getProductStatusEnum());
    }

    @Test
    public void offSale(){
        ProductInfo productInfo = productInfoService.offSale("123");
        Assert.assertEquals(ProductStatusEnum.DOWN,productInfo.getProductStatusEnum());
    }
}