package com.imooc.controller;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.ProductStatusEnum;
import com.imooc.exception.SellException;
import com.imooc.form.ProductForm;
import com.imooc.pojo.ProductCategory;
import com.imooc.pojo.ProductInfo;
import com.imooc.service.CategoryService;
import com.imooc.service.ProductInfoService;
import com.imooc.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Select;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 卖家商品api
 */
@Controller
@Slf4j
@RequestMapping("/seller/product")
public class SellerProductController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductInfoService productInfoService;

    @RequestMapping("/List")
    public ModelAndView list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                             @RequestParam(value = "size",defaultValue = "10")Integer size,
                             Map<String,Object> map){

        PageRequest pageRequest = new PageRequest(page - 1 ,size);
        Page<ProductInfo> productInfoPage =  productInfoService.findAll(pageRequest);
        map.put("productInfoPage",productInfoPage);
        map.put("currentPage",page);//当前页
        map.put("size",size);
        return new ModelAndView("product/list", map);

    }
    /**
     * 上架
     */
    @RequestMapping("/on_sale")
    public ModelAndView onSale(@RequestParam("productId")String productId,
                               Map<String,Object> map){
        try {
            productInfoService.onSale(productId);
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/product/List");
            return new ModelAndView("common/error",map);
        }

        map.put("url","/sell/seller/product/List");
        return new ModelAndView("common/success",map);
    }

    /**
     * 上架
     */
    @RequestMapping("/off_sale")
    public ModelAndView offSale(@RequestParam("productId")String productId,
                               Map<String,Object> map){
        try {
            productInfoService.offSale(productId);
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/product/List");
            return new ModelAndView("common/error",map);
        }

        map.put("url","/sell/seller/product/List");
        return new ModelAndView("common/success",map);
    }

    /**
     *跳转到商品更新 或新增页面
     * @param productId
     * @param map
     * @return
     */
    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "productId",required = false)String productId,
                              Map<String,Object> map){

       if(!StringUtils.isEmpty(productId)){
           ProductInfo productInfo =   productInfoService.findOne(productId);
           map.put("productInfo",productInfo);

       }

       //查询所有类目
       List<ProductCategory> productCategoryList = categoryService.findAll();
       map.put("productCategoryList",productCategoryList);
       return new ModelAndView("product/index",map);
    }

    /**
     * 添加商品/更新商品
     * @param productForm
     * @param bindingResult
     * @param map
     * @return
     */
    @PostMapping("/save")
/*    @CachePut(cacheNames = "product",key = "123")*/
    @CacheEvict(cacheNames = "product",key = "123") // 访问这个方法之后清除缓存
    public ModelAndView save(@Valid ProductForm productForm,
                             BindingResult bindingResult,
                             Map<String,Object> map){
    if(bindingResult.hasErrors()){
        map.put("msg",bindingResult.getFieldError().getDefaultMessage());
        map.put("url","/sell/seller/product/index");
        return new ModelAndView("/common/error",map);
    }

             ProductInfo productInfo = new ProductInfo();
        try{//如果productid不为空为新增商品
            if(!StringUtils.isEmpty(productForm.getProductId())){
                productInfo = productInfoService.findOne(productForm.getProductId());
            }else{
                productForm.setProductId(KeyUtil.genUniqueKey());
            }

            BeanUtils.copyProperties(productForm,productInfo);
            productInfoService.save(productInfo);

        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/product/index");
            return new ModelAndView("/common/error",map);
        }

        map.put("url","/sell/seller/product/List");
        return new ModelAndView("/common/success",map);

    }
}
