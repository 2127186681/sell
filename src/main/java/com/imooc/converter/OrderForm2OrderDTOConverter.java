package com.imooc.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.form.OrderForm;
import com.imooc.pojo.OrderDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 将orderform 对象转换成 orderdto对象
 */
@Slf4j
public class OrderForm2OrderDTOConverter {
    public static OrderDTO conver(OrderForm orderForm){
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();

         orderDTO.setBuyerName(orderForm.getName());
         orderDTO.setBuyerPhone(orderForm.getPhone());
         orderDTO.setBuyerAddress(orderForm.getAddress());
         orderDTO.setBuyerOpenid(orderForm.getOpenid());

         List<OrderDetail> orderDetailList = new ArrayList<>();
         try{
            orderDetailList = gson.fromJson(orderForm.getItems(),//要转换的对象
                     new TypeToken<List<OrderDetail>>(){}.getType());//转换的目标对象
         }catch (Exception e){
            log.error("【对象转换】错误，string={}",orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_REEOR);
         }

         orderDTO.setOrderDetailList(orderDetailList);
         return orderDTO;

    }
}
