package com.imooc.service.impl;

import com.imooc.pojo.SellerInfo;
import com.imooc.repository.SellerInfoRepository;
import com.imooc.service.SellerSerivice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerSeriviceImpl implements SellerSerivice {
    @Autowired
    private SellerInfoRepository repository;
    @Override
    public SellerInfo findSellerInfoByOrderId(String openId) {
        return repository.findByOpenid(openId);

    }
}
