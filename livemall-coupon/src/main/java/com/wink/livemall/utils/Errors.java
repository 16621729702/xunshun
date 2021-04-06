package com.wink.livemall.utils;

public enum Errors {
    wx_system_busy(-1,"系统繁忙，请稍后再试"),

    SUCCESS(200,"成功"),

    ERROR(500,"发生错误信息"),

    LOGIN(1024,"登录失效"),
    coupon_error(500,"优惠券结束时间未到，不能删除"),
    get_coupon_error(501,"您已达到优惠券领取次数，不能再领取"),
    is_can_use(501,"优惠劵已领取还未使用，不能再次领取"),
    // token异常
    TOKEN_PAST(301, "token过期"), TOKEN_ERROR(302, "token异常")
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
