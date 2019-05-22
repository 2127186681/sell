package com.imooc.controller;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 卖家端订单
 */
@Controller
@RequestMapping("/seller/order")
@Slf4j
    public class SellOrderController {


    @Autowired
    private OrderService orderService;

    @GetMapping("/List")//page当前页  从第一页开始  ，
    public ModelAndView list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                             @RequestParam(value = "size",defaultValue = "10")Integer size,
                             Map<String,Object> map){
        PageRequest pageRequest = new PageRequest(page - 1 ,size);
        Page<OrderDTO> orderDTOPage =  orderService.findList(pageRequest);
        map.put("orderDtoPage",orderDTOPage);
        map.put("currentPage",page);//当前页
        map.put("size",size);
        return new ModelAndView("order/list", map);
    }

    @GetMapping("/cancel") //卖家取消订单
    public ModelAndView cancel(@RequestParam("orderId") String orderId,
                               Map<String,Object> map){

        try{
            OrderDTO orderDTO=  orderService.findOne(orderId);
                orderService.cancel(orderDTO);
        }catch (SellException e){
            log.error("[卖家端取消订单] 发生异常 e={}",e);
            map.put("url","/sell/seller/order/List");
            map.put("msg",e.getMessage());
            return new ModelAndView("common/error",map);
        }


        map.put("url","/sell/seller/order/List");
        map.put("msg",ResultEnum.ORDER_CANCEL_SUCCESS.getMessage());

        return new ModelAndView("common/success");
    }

    @GetMapping("/detail")
    public ModelAndView detail(@RequestParam("orderId")String orderId,
                               Map<String,Object> map){
        OrderDTO  orderDTO = new OrderDTO();
        try{
            orderDTO = orderService.findOne(orderId);
        }catch (SellException e){
            log.error("[卖家端查询订单详情] 发生异常 e={}",e);
            map.put("url","/sell/seller/order/List");
            map.put("msg",e.getMessage());
            return new ModelAndView("common/error",map);
        }
        map.put("orderDTO",orderDTO);
        return  new ModelAndView("order/detail",map);
    }

    @GetMapping("/finish")
    public ModelAndView finish(@RequestParam("orderId") String orderId,
                               Map<String,Object> map){
        try{
            OrderDTO orderDTO=  orderService.findOne(orderId);
            orderService.finish(orderDTO);
        }catch (SellException e){
            log.error("[卖家端完结订单] 发生异常 e={}",e);
            map.put("url","/sell/seller/order/List");
            map.put("msg",e.getMessage());
            return new ModelAndView("common/error",map);
        }


        map.put("url","/sell/seller/order/List");
        map.put("msg",ResultEnum.ORDER_FINISH_SUCCESS.getMessage());

        return new ModelAndView("common/success");
    }
}
