package com.wink.livemall.sys.image.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="lm_image_group")
public class LmImageGroup {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private String name;
  private int uniacid;
  private int uid;
  private int type;


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


  public int getUniacid() {
    return uniacid;
  }

  public void setUniacid(int uniacid) {
    this.uniacid = uniacid;
  }


  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }


  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

}
