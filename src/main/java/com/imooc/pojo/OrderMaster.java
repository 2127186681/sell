package com.imooc.pojo;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单
 */
@Entity
@Data
@DynamicUpdate //自动更新updatetime
public class OrderMaster {
    //买家id
    @Id
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
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();

    //支付状态，默认0未支付
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    //订单创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;


}
