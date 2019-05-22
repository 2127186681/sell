package com.imooc.utils;

public class MathUtil {
    private static final Double MONEY_RANGE = 0.01;
    //金额校验
    public static Boolean eauals(Double d1 , double d2){
        Double result = Math.abs(d1-d1);
        if(result < MONEY_RANGE){
            return true;
        }else{
            return false;
        }
    }
}
