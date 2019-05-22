package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.pojo.OrderDetail;
import com.imooc.utils.EnumUtil;
import com.imooc.utils.serializer.Date2LongSerializer;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.apache.commons.lang3.EnumUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 数据传输对象，用来各个层传输数据
 */

@Data
/*@JsonInclude(JsonInclude.Include.NON_NULL) //当返回前端的字段为null时，会不显示该字段  可在全局配置中配置，对多个类有效*/
public class OrderDTO {

    //买家id

    private String orderId;

    //买家名字
    private String buyerName;

    //买家电话
    private String  buyerPhone;

    //地址
    private String buyerAddress;

    //微信id
    private String buyerOpenid;

    //订单总金额
    private BigDecimal orderAmount;

    //订单状态 默认0新下单
    private Integer orderStatus;

    //支付状态，默认0未支付
    private Integer payStatus;

    //订单创建时间
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    //更新时间
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    List<OrderDetail> orderDetailList;

    @JsonIgnore  //对象转json 使用这个注解会忽略这个方法，否则会带上该字段
    public  OrderStatusEnum getOrderStatusEnum(){
        return EnumUtil.getByCode(orderStatus,OrderStatusEnum.class);
    }


    @JsonIgnore
    public  PayStatusEnum getPayStatusEnum(){
    return  EnumUtil.getByCode(payStatus,PayStatusEnum.class);
    }
}
