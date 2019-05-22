package com.imooc.repository;

import com.imooc.pojo.SellerInfo;
import com.imooc.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SellerInfoRepositoryTest {
    @Autowired
    private SellerInfoRepository repository;

    @Test
    public void save(){
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setOpenid("123");
        sellerInfo.setSellerId(KeyUtil.genUniqueKey());
        sellerInfo.setUsername("张三");
        sellerInfo.setPassword("123456");
        Assert.assertNotNull(repository.save(sellerInfo));
    }
    @Test
    public void findByopenId(){
        SellerInfo sellerInfo =  repository.findByOpenid("123");
        Assert.assertEquals("123",sellerInfo.getOpenid());
    }

}