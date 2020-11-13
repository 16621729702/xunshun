package com.wink.livemall.goods.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "lm_good_comment")
public class LmGoodComment {
  //很差
  public static int goodqualitylow =1;
  //一般
  public static int goodqualitygeneral =2;
  //满意
  public static int goodqualitygood =3;
  //非常满意
  public static int goodqualityverygood =4;
  //满意
  public static int goodqualityperfect=5;

  //很差
  public static int servicelow =1;
  //一般
  public static int servicegeneral =2;
  //满意
  public static int servicegood =3;
  //非常满意
  public static int serviceverygood =4;
  //满意
  public static int serviceperfect=5;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private int goodid;
  private String content;
  private int isreplay;
  private int goodquality;
  private int serviceattitude;
  private int userid;
  private String replaycontent;
  private String img;
  private String adminreplay;
  private java.util.Date replaydate;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public int getGoodid() {
    return goodid;
  }

  public void setGoodid(int goodid) {
    this.goodid = goodid;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public int getIsreplay() {
    return isreplay;
  }

  public void setIsreplay(int isreplay) {
    this.isreplay = isreplay;
  }


  public int getGoodquality() {
    return goodquality;
  }

  public void setGoodquality(int goodquality) {
    this.goodquality = goodquality;
  }


  public int getServiceattitude() {
    return serviceattitude;
  }

  public void setServiceattitude(int serviceattitude) {
    this.serviceattitude = serviceattitude;
  }


  public int getUserid() {
    return userid;
  }

  public void setUserid(int userid) {
    this.userid = userid;
  }


  public String getReplaycontent() {
    return replaycontent;
  }

  public void setReplaycontent(String replaycontent) {
    this.replaycontent = replaycontent;
  }


  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }


  public String getAdminreplay() {
    return adminreplay;
  }

  public void setAdminreplay(String adminreplay) {
    this.adminreplay = adminreplay;
  }


  public java.util.Date getReplaydate() {
    return replaydate;
  }

  public void setReplaydate(java.util.Date replaydate) {
    this.replaydate = replaydate;
  }


  public static String goodqualitparsetochinese(String code){
    if("1".equals(code)){
      return "很差";
    }
    if("2".equals(code)){
      return "一般";
    }
    if("3".equals(code)){
      return "满意";
    }
    if("4".equals(code)){
      return "非常满意";
    }
    if("5".equals(code)){
      return "无可挑剔";
    }
    return "";
  }

  public static String serviceparsetochinese(String code){
    if("1".equals(code)){
      return "很差";
    }
    if("2".equals(code)){
      return "一般";
    }
    if("3".equals(code)){
      return "满意";
    }
    if("4".equals(code)){
      return "非常满意";
    }
    if("5".equals(code)){
      return "无可挑剔";
    }
    return "";
  }
}
