package com.wink.livemall.order.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "lm_order_comment")
public class LmOrderComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private int order_id;
  private double score;
  private String comment;
  private String img;
  private int isgood;
  private int merchid;
  private int goodid;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getOrder_id() {
    return order_id;
  }

  public void setOrder_id(int order_id) {
    this.order_id = order_id;
  }

  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public int getIsgood() {
    return isgood;
  }

  public void setIsgood(int isgood) {
    this.isgood = isgood;
  }

  public int getMerchid() {
    return merchid;
  }

  public void setMerchid(int merchid) {
    this.merchid = merchid;
  }

  public int getGoodid() {
    return goodid;
  }

  public void setGoodid(int goodid) {
    this.goodid = goodid;
  }
}
