package com.wink.livemall.merch.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "lm_merch_apply_info")
public class LmMerchApplyInfo {

  //审核通过
  public static int active = 1;
  //审核不通过
  public static int inactive = 2;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private int merchType;
  private int mainBody;
  private String mainCategorys;
  private String trueName;
  private String idcard;
  private String faceImg;
  private String emblemImg;
  private String handImg;
  private java.util.Date createAt;
  private java.util.Date updateAt;
  private int applyState;
  private int merchId;
  private String reason;

  public LmMerchApplyInfo() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public int getMerchType() {
    return merchType;
  }

  public void setMerchType(int merchType) {
    this.merchType = merchType;
  }


  public int getMainBody() {
    return mainBody;
  }

  public void setMainBody(int mainBody) {
    this.mainBody = mainBody;
  }


  public String getMainCategorys() {
    return mainCategorys;
  }

  public void setMainCategorys(String mainCategorys) {
    this.mainCategorys = mainCategorys;
  }


  public String getTrueName() {
    return trueName;
  }

  public void setTrueName(String trueName) {
    this.trueName = trueName;
  }


  public String getIdcard() {
    return idcard;
  }

  public void setIdcard(String idcard) {
    this.idcard = idcard;
  }


  public String getFaceImg() {
    return faceImg;
  }

  public void setFaceImg(String faceImg) {
    this.faceImg = faceImg;
  }


  public String getEmblemImg() {
    return emblemImg;
  }

  public void setEmblemImg(String emblemImg) {
    this.emblemImg = emblemImg;
  }


  public String getHandImg() {
    return handImg;
  }

  public void setHandImg(String handImg) {
    this.handImg = handImg;
  }


  public java.util.Date getCreateAt() {
    return createAt;
  }

  public void setCreateAt(java.util.Date createAt) {
    this.createAt = createAt;
  }


  public java.util.Date getUpdateAt() {
    return updateAt;
  }

  public void setUpdateAt(java.util.Date updateAt) {
    this.updateAt = updateAt;
  }


  public int getApplyState() {
    return applyState;
  }

  public void setApplyState(int applyState) {
    this.applyState = applyState;
  }


  public int getMerchId() {
    return merchId;
  }

  public void setMerchId(int merchId) {
    this.merchId = merchId;
  }


  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

}
