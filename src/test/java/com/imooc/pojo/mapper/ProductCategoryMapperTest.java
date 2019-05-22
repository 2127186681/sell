package com.imooc.pojo.mapper;

import com.imooc.pojo.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductCategoryMapperTest {
    @Autowired
    private ProductCategoryMapper mapper;

    @Test
    public void insert(){
        Map<String,Object> map = new HashMap<>();
        map.put("category_name","wozuiai");
        map.put("category_type",30);
        int result = mapper.insertByMap(map);
        Assert.assertEquals(1,result);
    }

    @Test
    public void insertBy(){
        ProductCategory category = new ProductCategory();
        category.setCategoryType(52);
        category.setCategoryName("好吃的");
        int result = mapper.insertBy(category);
        Assert.assertEquals(1,result);
    }

    @Test
    public void findByCategoryType(){
        ProductCategory category = mapper.findByCategoryType(1);
        Assert.assertNotNull(category);
    }

    @Test
    public void updateByCategoryType(){
        int result = mapper.updateByCategoryType("xiao", 51);
        Assert.assertEquals(1,result);
    }

}