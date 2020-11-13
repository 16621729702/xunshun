package com.wink.livemall.goods.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "lm_good_material")
public class LmGoodMaterial {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private String name;
  private int code;
  private String status;
  private int orderno;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public int getOrderno() {
    return orderno;
  }

  public void setOrderno(int orderno) {
    this.orderno = orderno;
  }

}
