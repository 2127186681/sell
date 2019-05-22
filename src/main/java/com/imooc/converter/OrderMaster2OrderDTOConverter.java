package com.imooc.converter;

import com.imooc.dto.OrderDTO;
import com.imooc.pojo.OrderMaster;
import org.hibernate.criterion.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类型转换  ordermaster->OrderDTO
 */
public class OrderMaster2OrderDTOConverter {

    public static OrderDTO conver(OrderMaster orderMaster){
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        return orderDTO;
    }

    public static List<OrderDTO> conver(List<OrderMaster> orderMasterList){
       return orderMasterList.stream().map(e->conver(e) ).collect(Collectors.toList());
    }
}
