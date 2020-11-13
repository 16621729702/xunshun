package com.wink.livemall.coupon.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "lm_coupons")
public class LmCoupons {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private int type;
  private int state;
  private String name;
  private int num;
  private int left_num;
  private BigDecimal fill_price;
  private BigDecimal discount_price;
  private java.util.Date start_date;
  private java.util.Date end_date;
  private String description;
  private java.util.Date updated_at;
  private java.util.Date created_at;
  private java.util.Date deleted_at;
  private int use_num;
  private int merch_id;
  private BigDecimal useprice;
  private BigDecimal rate;

  public BigDecimal getUseprice() {
	return useprice;
}

public void setUseprice(BigDecimal useprice) {
	this.useprice = useprice;
}

public BigDecimal getRate() {
	return rate;
}

public void setRate(BigDecimal rate) {
	this.rate = rate;
}

public LmCoupons() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getNum() {
    return num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public int getLeft_num() {
    return left_num;
  }

  public void setLeft_num(int left_num) {
    this.left_num = left_num;
  }

  public BigDecimal getFill_price() {
    return fill_price;
  }

  public void setFill_price(BigDecimal fill_price) {
    this.fill_price = fill_price;
  }

  public BigDecimal getDiscount_price() {
    return discount_price;
  }

  public void setDiscount_price(BigDecimal discount_price) {
    this.discount_price = discount_price;
  }

  public Date getStart_date() {
    return start_date;
  }

  public void setStart_date(Date start_date) {
    this.start_date = start_date;
  }

  public Date getEnd_date() {
    return end_date;
  }

  public void setEnd_date(Date end_date) {
    this.end_date = end_date;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(Date updated_at) {
    this.updated_at = updated_at;
  }

  public Date getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Date created_at) {
    this.created_at = created_at;
  }

  public Date getDeleted_at() {
    return deleted_at;
  }

  public void setDeleted_at(Date deleted_at) {
    this.deleted_at = deleted_at;
  }

  public int getUse_num() {
    return use_num;
  }

  public void setUse_num(int use_num) {
    this.use_num = use_num;
  }

  public int getMerch_id() {
    return merch_id;
  }

  public void setMerch_id(int merch_id) {
    this.merch_id = merch_id;
  }
}
