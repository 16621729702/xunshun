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

    public HttpJsonResult(String errorMessage) {
        this.success = true;
        this.message = errorMessage;
    }

    private Boolean success = true;

    public Boolean getSuccess() {
        return this.success;
    }

    public Boolean setSuccess(Boolean result){
    	this.success=result;
    	return success;
    }

    private T data;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private String message;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.success = false;
        this.message = message;
    }

    private int state;
    private int id;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
