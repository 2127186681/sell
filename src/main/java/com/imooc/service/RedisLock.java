package com.imooc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * redis分布式锁
 */
@Component
@Slf4j

public class RedisLock {
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 加锁
     * @param key  商品id
     * @param value  当前时间 + 超时时间
     */
    public boolean lock(String key,String value){
        //是否存在key，如果存在不操作，不存在返回true
        if(redisTemplate.opsForValue().setIfAbsent(key,value)){
            return true;
        }
        //判断时间是否超时
        String currentValue = redisTemplate.opsForValue().get(key);//get此时key中的value
        if(!StringUtils.isEmpty(currentValue)
             && Long.parseLong(currentValue) < System.currentTimeMillis()){

            //getAndSet存入一个新值，返回上一个值，判断该值是否与上一次get的值是否一致
            String lodValue = redisTemplate.opsForValue().getAndSet(key,value);
            if(!StringUtils.isEmpty(lodValue) && lodValue.equals(currentValue)){
                return true;
            }
        }

        return false;
    }

    /**
     * 解锁
     * @param key
     * @param value
     */
    public void unLock(String key,String value){
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }catch (Exception e){
            log.error("[redis分布式锁] 解锁异常，{}",e);
        }

    }
}
