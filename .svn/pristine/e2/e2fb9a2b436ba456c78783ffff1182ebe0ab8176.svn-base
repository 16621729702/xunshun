package com.wink.livemall.sys.dict.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="lm_sys_dict_item")
public class LmSysDictItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private String dict_code;
  private String code;
  private String name;
  private String value;
  private String remark;
  private int state;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDict_code() {
    return dict_code;
  }

  public void setDict_code(String dict_code) {
    this.dict_code = dict_code;
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

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
