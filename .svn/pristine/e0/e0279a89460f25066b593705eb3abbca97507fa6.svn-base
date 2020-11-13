package com.wink.livemall.merch.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "lm_merch_configs")
public class LmMerchConfigs {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private int uniacid;
  private String type;
  private String config;
  private java.util.Date updated_at;
  private java.util.Date created_at;

  public LmMerchConfigs() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUniacid() {
    return uniacid;
  }

  public void setUniacid(int uniacid) {
    this.uniacid = uniacid;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getConfig() {
    return config;
  }

  public void setConfig(String config) {
    this.config = config;
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
}
