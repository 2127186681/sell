package com.imooc.service;

import com.imooc.pojo.SellerInfo;

public interface SellerSerivice {
    /**
     * 通过opendi查找买家端信息
     * @return
     */
    SellerInfo findSellerInfoByOrderId(String openId);
}
