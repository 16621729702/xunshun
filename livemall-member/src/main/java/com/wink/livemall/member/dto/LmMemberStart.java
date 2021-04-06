package com.wink.livemall.member.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "lm_member_start")
public class LmMemberStart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private String mobile;
    private String margin;
    private String coupon_price;
    private int type;
    private int isstart;
    private int businessid;

    public int getBusinessid() {
        return businessid;
    }

    public void setBusinessid(int businessid) {
        this.businessid = businessid;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(String coupon_price) {
        this.coupon_price = coupon_price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsstart() {
        return isstart;
    }

    public void setIsstart(int isstart) {
        this.isstart = isstart;
    }
}
