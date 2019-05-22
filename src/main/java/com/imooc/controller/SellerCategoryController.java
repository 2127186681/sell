package com.imooc.controller;

import com.imooc.exception.SellException;
import com.imooc.form.CategoryForm;
import com.imooc.pojo.ProductCategory;
import com.imooc.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 *
 * 卖家类目
 */
@Controller
@RequestMapping("/seller/category")
@Slf4j

public class SellerCategoryController {
    @Autowired
    private CategoryService categoryService;



    /**
     * 查询所有类目
     * @param map
     * @return
     */
    @GetMapping("/List")
    public ModelAndView list(Map<String,Object> map){
       List<ProductCategory> productCategoryList  =categoryService.findAll();
       map.put("productCategoryList",productCategoryList);
       return new ModelAndView("category/list",map);
    }

    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "categoryId",required = false)Integer categoryId,
                              Map<String,Object> map){
        if(!StringUtils.isEmpty(categoryId)){
             ProductCategory category =  categoryService.findOne(categoryId);
             map.put("category",category);

        }
        return new ModelAndView("category/index",map);

    }

    @PostMapping("/save")
    public ModelAndView save(@Valid CategoryForm categoryForm,
                             BindingResult bindingResult,
                             Map<String,Object> map){
        if(bindingResult.hasErrors()){
            map.put("msg",bindingResult.getFieldError().getDefaultMessage());
            map.put("url","/sell/seller/category/index");
            return new ModelAndView("/common/error",map);
        }
        ProductCategory category = new ProductCategory();
        try {
            if(!StringUtils.isEmpty(categoryForm.getCategoryId())){
                category =  categoryService.findOne(categoryForm.getCategoryId());
            }
            BeanUtils.copyProperties(categoryForm,category);
            categoryService.save(category);
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/category/index");
            return new ModelAndView("/common/error",map);
        }


        map.put("url","/sell/seller/category/List");
        return new ModelAndView("/common/success",map);
    }
}
