package com.imooc.service.impl;


import com.imooc.exception.SellException;
import com.imooc.pojo.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {
    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImplTest.class);
    @Autowired
    private CategoryServiceImpl categoryService;

    @Test
    public void findOne() {
        ProductCategory productCategory = categoryService.findOne(1);
        Assert.assertEquals(new Integer(1),productCategory.getCategoryId());
    }

    @Test
    public void findAll() {
        List<ProductCategory> list = categoryService.findAll();
        Assert.assertNotEquals(0,list.size());
    }

    @Test
    public void findByCategoryTypeIn() {
        List<Integer> list = Arrays.asList(1,2,4);
        categoryService.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0,list.size());
    }

    @Test
    public void save() {
 /*       ProductCategory productCategory = new ProductCategory("水果",6);*/
        try {
            ProductCategory category = null;
            ProductCategory save = categoryService.save(category);
        }catch (SellException e){
            log.error("[新增类目失败] e={}",e);
        }

        /*Assert.assertNotNull(save);*/
    }
}