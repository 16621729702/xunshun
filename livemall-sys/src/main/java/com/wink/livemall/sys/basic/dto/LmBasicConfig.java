package com.wink.livemall.sys.basic.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="lm_basic_config")
public class LmBasicConfig {
  //启用
  public static String ACTIVE = "active";
  //禁用
  public static String INACTIVE = "inactive";

  //隐私政策
  public static String YSZC = "1";
  //用户协议
  public static String YHXY = "2";



  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private String type;
  private String comment;
  private String status;
  private String name;



  public static String parsetochinesetype(String type) {
    if("1".equals(type)){
      return "隐私政策";
    }
    if("2".equals(type)){
      return "用户协议";
    }
    return "";
  }


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
