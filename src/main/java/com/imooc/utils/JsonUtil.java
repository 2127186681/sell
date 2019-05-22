package com.imooc.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//json格式化工具
public class JsonUtil {

    public static String toJson(Object o){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson =  gsonBuilder.create();
        return gson.toJson(o);
    }
}
