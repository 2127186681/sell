package com.imooc.controller;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.OrderService;
import com.imooc.service.PayService;
import com.imooc.service.impl.PayServiceImpl;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/pay")
public class PayController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;
    @GetMapping("/create")//动态注入参数发起支付，使用了faeemarker模板
    public ModelAndView create(@RequestParam("orderId")String orderId,
                               @RequestParam("returnUrl")String returnUrl,
                               Map<String,Object> map) throws Exception {
        //查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if(orderDTO == null){
            log.error("[订单支付] 订单不存在 orderdto={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //发起支付

            PayResponse payResponse =  payService.create(orderDTO);
            map.put("payResponse",payResponse);
            map.put("returnUrl",returnUrl);


        return new ModelAndView("pay/create",map);

    }
                            //异步通知地址是自己在配置里面配的
    @PostMapping("notify")//支付时 微信异步通知发来一些信息，如订单id，支付流水号，异步通知结果不一定是支付成功了
    public ModelAndView notify(@RequestBody String notifyData){
        payService.notify(notifyData);
        //返回给微信处理结果

        return  new ModelAndView("pay/success");
    }
}
