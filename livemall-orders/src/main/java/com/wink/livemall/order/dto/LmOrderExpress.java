package com.wink.livemall.order.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "lm_order_express")
public class LmOrderExpress {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private String orderid;
  private int deliverytype;
  private int expressid;
  private String expressorderid;
  private String sendname;
  private String sendphone;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getOrderid() {
    return orderid;
  }

  public void setOrderid(String orderid) {
    this.orderid = orderid;
  }


  public int getDeliverytype() {
    return deliverytype;
  }

  public void setDeliverytype(int deliverytype) {
    this.deliverytype = deliverytype;
  }


  public int getExpressid() {
    return expressid;
  }

  public void setExpressid(int expressid) {
    this.expressid = expressid;
  }


  public String getExpressorderid() {
    return expressorderid;
  }

  public void setExpressorderid(String expressorderid) {
    this.expressorderid = expressorderid;
  }


  public String getSendname() {
    return sendname;
  }

  public void setSendname(String sendname) {
    this.sendname = sendname;
  }


  public String getSendphone() {
    return sendphone;
  }

  public void setSendphone(String sendphone) {
    this.sendphone = sendphone;
  }

}
