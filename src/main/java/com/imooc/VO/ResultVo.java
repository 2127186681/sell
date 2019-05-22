package com.imooc.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * http返回前端的对象
 */
@Data
public class ResultVo<T> implements Serializable {

    private static final long serialVersionUID = 6866921590711160769L;
    //错误码
    private Integer code;
    //提示信息
    private String msg;
    //返回的具体内容
    private T data;
}
