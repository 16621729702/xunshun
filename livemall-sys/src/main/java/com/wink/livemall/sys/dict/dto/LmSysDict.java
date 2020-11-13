package com.wink.livemall.sys.dict.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="lm_sys_dict")
public class LmSysDict {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private String code;
  private String name;
  private String remark;
  private int state;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
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

}
