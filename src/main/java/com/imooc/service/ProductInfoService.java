package com.imooc.service;

import com.imooc.dto.CartDTO;
import com.imooc.pojo.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductInfoService {
    ProductInfo findOne(String productId);

    /**
     * 查询所有在架商品
     */
    List<ProductInfo> findUpAll();
    /**
     * 分页展示商品
     */
    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo pageductInfo);

    //加库存
    void increaseStock(List<CartDTO> cartDTOList);

    //减库存
    void decreaseStock(List<CartDTO> cartDTOList);

    //上架
    ProductInfo onSale(String productId);
    //下架
    ProductInfo offSale(String productId);
}
