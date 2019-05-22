package com.imooc.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * cookie工具类
 */
public class CookieUtil {
    public static  void  setCookie(HttpServletResponse response,
                           String name,
                           String value,
                           Integer maxAge){
        Cookie cookie = new Cookie(name,value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);//cookie过期时间
        response.addCookie(cookie);//响应到本地
    }
    //获取cookie
    public static Cookie  getCookie(HttpServletRequest request,
                                  String name){
        Map<String,Cookie> cookieMap = readCookieMap(request);
        if(cookieMap.containsKey(name)){
            return cookieMap.get(name);
        }else {
            return null;
        }
    }
    //将cookie数组转换成map
    private static Map<String,Cookie> readCookieMap(HttpServletRequest request){
        Map<String,Cookie> cookieMap = new HashMap<>();
        Cookie[] cookits = request.getCookies();
        if(cookits!= null) {
            for (Cookie cookie : cookits) {

                cookieMap.put(cookie.getName(), cookie);

            }
        }
        return cookieMap;
    }
}
