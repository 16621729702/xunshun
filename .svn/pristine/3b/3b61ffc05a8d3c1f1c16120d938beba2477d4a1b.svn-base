package com.wink.livemall.member.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "lm_member_level")
public class LmMemberLevel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private int growth_value;
  private String name;
  private String code;
  private String remark;
  private int state;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getGrowth_value() {
    return growth_value;
  }

  public void setGrowth_value(int growth_value) {
    this.growth_value = growth_value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
