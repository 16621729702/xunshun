package com.wink.livemall.merch.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "lm_merch_category")
public class LmMerchCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private String name;
  private int sort;
  private int isshow;
  private java.util.Date updatedAt;
  private java.util.Date createdAt;
  //暂时不用
  private int isquality;//是否优选 0否 1是
  private int islive;//能否直播 0否 1是
  private int ischipped;//能否合买 0否 1是
  
  

  public int getIsquality() {
	return isquality;
}

public void setIsquality(int isquality) {
	this.isquality = isquality;
}

public int getIslive() {
	return islive;
}

public void setIslive(int islive) {
	this.islive = islive;
}

public int getIschipped() {
	return ischipped;
}

public void setIschipped(int ischipped) {
	this.ischipped = ischipped;
}

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


  public int getSort() {
    return sort;
  }

  public void setSort(int sort) {
    this.sort = sort;
  }


  public int getIsshow() {
    return isshow;
  }

  public void setIsshow(int isshow) {
    this.isshow = isshow;
  }


  public java.util.Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(java.util.Date updatedAt) {
    this.updatedAt = updatedAt;
  }


  public java.util.Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(java.util.Date createdAt) {
    this.createdAt = createdAt;
  }

}
