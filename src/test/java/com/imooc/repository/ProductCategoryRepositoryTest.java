package com.imooc.repository;

import com.imooc.pojo.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository repository;

    @Test
    public void findOneTest(){
        ProductCategory productCategory = repository.findOne(1);
        System.out.println(productCategory.toString());

    }

    @Test
    @Transactional //测试成功数据不会保留在数据库
    public void saveTest(){
        ProductCategory productCategory = new ProductCategory("美食", 1);
        ProductCategory result = repository.save(productCategory);
        Assert.assertNotNull(result);


    }

    @Test
    public void updateTest(){
        ProductCategory productCategory = repository.findOne(2);


        productCategory.setCategoryType(5);
        repository.save(productCategory);
    }

    @Test
    public void findByCategoryTypeInTest(){
        List<Integer> list = Arrays.asList(1,2,4);

        List<ProductCategory> ls = repository.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0,ls.size());
    }



}