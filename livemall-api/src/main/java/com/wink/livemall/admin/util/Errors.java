package com.wink.livemall.admin.util;

public enum Errors {
    wx_system_busy(-1,"系统繁忙，请稍后再试"),

    ok(200,"OK")

    ;





    private int code;
    private String msg;

    Errors(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
