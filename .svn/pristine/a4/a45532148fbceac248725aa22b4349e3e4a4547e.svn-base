package com.wink.livemall.video.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="lm_video_category")
public class LmVideoCategoary {
  public static int active = 0;
  public static int inactive = 1;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private int status;
  private String name;
  private int orderno;
  private int pid;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public int getOrderno() {
    return orderno;
  }

  public void setOrderno(int orderno) {
    this.orderno = orderno;
  }

  public int getPid() {
    return pid;
  }

  public void setPid(int pid) {
    this.pid = pid;
  }
}
