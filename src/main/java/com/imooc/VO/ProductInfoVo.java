package com.imooc.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 返回前端的商品详情
 */
@Data
public class ProductInfoVo implements Serializable {

    private static final long serialVersionUID = 3842460487143376065L;
    @JsonProperty("id")
    private String productId;

    @JsonProperty("name")
    private String productName;

    @JsonProperty("price")
    private BigDecimal productPrice;

    @JsonProperty("desctiption")
    private String productDescription;

    @JsonProperty("icon")
    private String productIcon;
}
