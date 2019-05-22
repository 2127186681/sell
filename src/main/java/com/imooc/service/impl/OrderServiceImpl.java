package com.imooc.service.impl;

import com.imooc.converter.OrderForm2OrderDTOConverter;
import com.imooc.converter.OrderMaster2OrderDTOConverter;
import com.imooc.dto.CartDTO;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.form.OrderForm;
import com.imooc.pojo.OrderDetail;
import com.imooc.pojo.OrderMaster;
import com.imooc.pojo.ProductInfo;
import com.imooc.repository.OrderDetailRepository;
import com.imooc.repository.OrderMasterRepository;
import com.imooc.service.OrderService;
import com.imooc.service.PayService;
import com.imooc.service.ProductInfoService;
import com.imooc.service.PushMessageService;
import com.imooc.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private WebSocket webSocket;

    @Autowired
    private PushMessageService messageService;

    @Autowired
    private OrderDetailRepository detailRepository;

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private OrderMasterRepository masterRepository;

    @Autowired
    private PayService payService;
    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);//定义商品总价
        //1.查询商品的数量和价格（为保证安全，价格不可以从前端传过来）
        for(OrderDetail orderDetail:orderDTO.getOrderDetailList()){
            ProductInfo productInfo = productInfoService.findOne(orderDetail.getProductId());
            if(productInfo==null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //2.计算总价
           orderAmount= productInfo.getProductPrice()
                   .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                   .add(orderAmount);
            //订单详情入库

            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            detailRepository.save(orderDetail);
        }


        //3.写入数据库（ordermaster和orderDetail）
        OrderMaster orderMaster = new OrderMaster();

        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderAmount(orderAmount);

        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        masterRepository.save(orderMaster);
        //减库存
         List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().
                map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
         productInfoService.decreaseStock(cartDTOList);

         //wensocket向前端发送消息
        webSocket.sendMessage("创建新的订单");
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId){

        OrderMaster orderMaster =  masterRepository.findOne(orderId);


                if(orderMaster==null) {
                    throw new SellException(ResultEnum.ORDER_NOT_EXIST);
                }





        List<OrderDetail> orderDetailList = detailRepository.findByOrderId(orderId);
        if(orderDetailList==null){
            throw  new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String openId, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = masterRepository.findByBuyerOpenid(openId,pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.conver(orderMasterPage.getContent());
        return  new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());

    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();

        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.info("[订单状态] 订单状态不正确 取消失败 orderStatus={}",orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态

        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult =masterRepository.save(orderMaster);
        if(updateResult==null){
            log.info("【订单取消】 更新失败 ordermaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIT);
        }
        //返还库存
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.info("[取消订单] 订单详情为空 orderDto={}",orderDTO);
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        List<CartDTO> cartDTOList =orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());
        productInfoService.increaseStock(cartDTOList);
        //如果已支付，退款
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
        //
            payService.refund(orderDTO);
        }
        return orderDTO;
    }

    @Override
    @Transactional //完结订单时推送微信模板消息
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
        log.info("[订单完结] 订单状态不正确 orderStatus={}",orderDTO.getOrderStatus());
        throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHEN.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster result = masterRepository.save(orderMaster);
        if(result==null){
            log.info("【订单完结】 更新失败 ordermaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIT);
        }
        //推送微信模板消息
        messageService.orderStatus(orderDTO);

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO pay(OrderDTO orderDTO) {
        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.info("[订单支付] 订单状态不正确 orderStatus={}",orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断支付状态
        if(!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
        log.info("[订单支付] 支付状态不正确 orderdto={}",orderDTO );
        throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster result = masterRepository.save(orderMaster);
        if(result==null){
            log.info("【订单支付】 更新失败 ordermaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIT);
        }
        return orderDTO;
    }

    @Override  //查询所有订单以列表形式
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage =  masterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList =  OrderMaster2OrderDTOConverter.conver(orderMasterPage.getContent());
        return new PageImpl<>(orderDTOList,pageable,orderMasterPage.getTotalElements());
    }
}
