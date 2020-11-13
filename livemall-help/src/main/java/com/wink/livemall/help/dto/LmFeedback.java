package com.wink.livemall.help.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "lm_feedback")
public class LmFeedback {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private int member_id;
  private int type;
  private String content;
  private String images;
  private int state;
  private Date create_at;
  private Date update_at;
  private String reply_content;
  private Date reply_time;

  public LmFeedback() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getMember_id() {
    return member_id;
  }

  public void setMember_id(int member_id) {
    this.member_id = member_id;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getImages() {
    return images;
  }

  public void setImages(String images) {
    this.images = images;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public Date getCreate_at() {
    return create_at;
  }

  public void setCreate_at(Date create_at) {
    this.create_at = create_at;
  }

  public Date getUpdate_at() {
    return update_at;
  }

  public void setUpdate_at(Date update_at) {
    this.update_at = update_at;
  }

  public String getReply_content() {
    return reply_content;
  }

  public void setReply_content(String reply_content) {
    this.reply_content = reply_content;
  }

  public Date getReply_time() {
    return reply_time;
  }

  public void setReply_time(Date reply_time) {
    this.reply_time = reply_time;
  }

  public static String parsetochinese(int type){
    if(type==0){
      return "性能体验";
    }
    if(type==1){
      return "功能异常";
    }
    if(type==2){
      return "产品建议";
    }
    if(type==3){
      return "其他问题";
    }
    return "";
  }
}
