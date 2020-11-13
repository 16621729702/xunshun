package com.wink.livemall.order.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "lm_orders_log")
public class LmOrderLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private String orderid;
  private String operate;
  private java.util.Date operatedate;


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


  public String getOperate() {
    return operate;
  }

  public void setOperate(String operate) {
    this.operate = operate;
  }


  public java.util.Date getOperatedate() {
    return operatedate;
  }

  public void setOperatedate(java.util.Date operatedate) {
    this.operatedate = operatedate;
  }

}
