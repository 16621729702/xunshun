package com.wink.livemall.goods.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "lm_good_auction")
public class LmGoodAuction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private int goodid;
  private int memberid;
  private BigDecimal price;
  private Date createtime;
  private String status;
  private int type;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getGoodid() {
    return goodid;
  }

  public void setGoodid(int goodid) {
    this.goodid = goodid;
  }

  public int getMemberid() {
    return memberid;
  }

  public void setMemberid(int memberid) {
    this.memberid = memberid;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Date getCreatetime() {
    return createtime;
  }

  public void setCreatetime(Date createtime) {
    this.createtime = createtime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}
