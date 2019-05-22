package com.imooc.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imooc.enums.ProductStatusEnum;
import com.imooc.utils.EnumUtil;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品表
 */
@Entity
@Data
@DynamicUpdate
public class ProductInfo implements Serializable {

    private static final long serialVersionUID = -2473098383350688255L;
    //商品id
    @Id
    private String productId;
    //商品名字
    private String productName;
    //商品价格
    private BigDecimal productPrice;
    //商品库存
    private Integer productStock;
    //商品描述
    private String productDescription;
    //商品小图
    private String productIcon;
    //商品状态 0正常 1下架
    private Integer productStatus = ProductStatusEnum.UP.getCode();
    //商品类目编号
    private Integer categoryType;

    private Date createTime;
    private Date updateTime;

    @JsonIgnore
    public  ProductStatusEnum getProductStatusEnum(){
        return EnumUtil.getByCode(productStatus,ProductStatusEnum.class);
    }
}

