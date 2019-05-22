package com.imooc.controller;

import com.imooc.VO.ProductInfoVo;
import com.imooc.VO.ProductVo;
import com.imooc.VO.ResultVo;

import com.imooc.pojo.ProductCategory;
import com.imooc.pojo.ProductInfo;
import com.imooc.service.CategoryService;
import com.imooc.service.ProductInfoService;

import com.imooc.utils.ResultVoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 买家商品api
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {
    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
   @Cacheable(cacheNames = "product",key = "123") //设置缓存，第一次访问会将数据放入缓存，之后再访问直接访问缓存，除非缓存清除才会再次访问该方法
    public ResultVo list(){
        //1.查询所有上架的商品
        List<ProductInfo> productInfoList =  productInfoService.findUpAll();
        //2.查询类目（一次性查询，保证性能）
       /* List<Integer> cotegoryTypeList = new ArrayList<>();*/
        //查询类目传统方法
       /* for(ProductInfo productInfo:productInfoList){
            cotegoryTypeList.add(productInfo.getCategoryType());
        }*/
        //精简方法--使用java8新特性lamdba
        List<Integer> cotegoryTypeList = productInfoList.stream().
                map(e -> e.getCategoryType()).
                collect(Collectors.toList());
        List<ProductCategory> productCategoryList= categoryService.findByCategoryTypeIn(cotegoryTypeList);
        //3.数据拼装
        List<ProductVo> productVoList = new ArrayList<>();

        for(ProductCategory productCategory:productCategoryList){
            ProductVo productVo = new ProductVo();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVo> productInfoVoList = new ArrayList<>();
            for(ProductInfo productInfo : productInfoList){
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVo productInfoVo = new ProductInfoVo();
                    BeanUtils.copyProperties(productInfo,productInfoVo);
                    productInfoVoList.add(productInfoVo);
                }

            }
            productVo.setProductInfoVoList(productInfoVoList);
            productVoList.add(productVo);


        }

        return ResultVoUtil.success(productVoList);
    }
}
