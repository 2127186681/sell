package com.imooc.constant;

/**
 * ridis常量
 */
public interface RedisContant {
    //redis过期时间
    Integer EXPIRE = 7200;
    String TOKEN_PREFIX="token_%s";  //token前缀
}
