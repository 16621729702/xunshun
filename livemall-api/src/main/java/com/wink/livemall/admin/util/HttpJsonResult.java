package com.wink.livemall.admin.util;

import java.io.Serializable;


/**
 * 
 *                       
 * @Filename: HttpJsonResult.java
 * @Version: 1.0
 * @Author: 
 * @Email: 
 *
 */
public class HttpJsonResult<T> implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -8637111820477625638L;

    public HttpJsonResult() {

    }
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HttpJsonResult(String errorMessage) {
        this.msg = errorMessage;
    }
    private T data;
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
