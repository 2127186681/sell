package com.imooc.handler;

import com.imooc.VO.ResultVo;
import com.imooc.config.ProjectUrlConfig;
import com.imooc.exception.SellException;
import com.imooc.exception.SellerAutroizeException;
import com.imooc.utils.ResultVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class SellExceptionHandler {
    @Autowired
    private ProjectUrlConfig projectUrlConfig;
    //处理拦截登陆异常
    @ExceptionHandler(value = SellerAutroizeException.class)
    public ModelAndView handlerAuthorizeException() {

    return new ModelAndView("redirect:".concat(projectUrlConfig.getWxChatMpAuthroize()).
            concat("/sell/wechat/qrAuthorize")
            .concat("?returnUrl=")
            .concat(projectUrlConfig.getSell())
            .concat("/sell/seller/login"));

    }

    @ResponseBody
    @ExceptionHandler(value = SellException.class)
    //@ResponseStatus(HttpStatus.FORBIDDEN)  如果想要改变响应的状态码用这个注解，如果不改默认返回的是200状态码
    public ResultVo sellException(SellException e){
        return ResultVoUtil.error(e.getCode(),e.getMessage());
    }
}
