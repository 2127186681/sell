package com.imooc.service.impl;

import com.imooc.exception.SellException;
import com.imooc.service.RedisLock;
import com.imooc.service.SecKillService;
import com.imooc.utils.KeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SecKillServiceImpl implements SecKillService {
    @Autowired
    private RedisLock redisLock;

    private static final int TIMEOUT =  10*1000;//过期时间 10s
    static Map<String,Integer> products;
    static Map<String,Integer> stock;
    static Map<String,String> orders;
    static {

        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("123",10000);
        stock.put("123",10000);
    }

    private String queryMap(String productId){
        return "国庆活动，皮蛋粥特价，限量份"+products.get(productId)
                +" 还剩："+stock.get(productId)+"份"+
                " 下单成功用户数量："+orders.size()+"人";
    }
    @Override
    public String querySecKillProductInfo(String productId) {
        return queryMap(productId);
    }

    /**
     * 模拟下单
     * @param productId
     */
    @Override
    public  void orderSecKillProductDiffUser(String productId) {

        //加锁
       long time =  System.currentTimeMillis() + TIMEOUT;
        if(!redisLock.lock(productId,String.valueOf(time))){
            throw new SellException(101,"换个姿势试试");
        }

        Integer stocknum = stock.get(productId);
        if(stocknum == 0){
            throw new SellException(100,"活动结束");
        }else{
            //模拟下单
            orders.put(KeyUtil.genUniqueKey(),productId);
            //减库存
             stocknum = stock.get(productId) - 1;
                     try{
                         Thread.sleep(100);
                     }catch (InterruptedException e){
                        e.printStackTrace();
                     }
                     stock.put(productId,stocknum);
        }

        //解锁
        redisLock.unLock(productId,String.valueOf(time));

    }
}
