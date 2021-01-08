package com.wink.livemall.order.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "lm_order_goods")
public class LmOrderGoods {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private int orderid;
  private int goodid;
  private int goodnum;
  //0是普通商品1是直播商品 2合买
  private int goodstype;
  private BigDecimal goodprice;

  public int getGoodstype() {
    return goodstype;
  }

  public void setGoodstype(int goodstype) {
    this.goodstype = goodstype;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getOrderid() {
    return orderid;
  }

  public void setOrderid(int orderid) {
    this.orderid = orderid;
  }

  public int getGoodid() {
    return goodid;
  }

  public void setGoodid(int goodid) {
    this.goodid = goodid;
  }

  public int getGoodnum() {
    return goodnum;
  }

  public void setGoodnum(int goodnum) {
    this.goodnum = goodnum;
  }

  public BigDecimal getGoodprice() {
    return goodprice;
  }

  public void setGoodprice(BigDecimal goodprice) {
    this.goodprice = goodprice;
  }
}
