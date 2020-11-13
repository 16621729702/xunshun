package com.wink.livemall.member.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "lm_member_fav")
public class LmMemberFav {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private int member_id;
  private java.util.Date create_time;
  private int goods_id;
  private java.util.Date cancel_time;
  private int state;
  private int video_id;

  public LmMemberFav() {
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

  public Date getCreate_time() {
    return create_time;
  }

  public void setCreate_time(Date create_time) {
    this.create_time = create_time;
  }

  public int getGoods_id() {
    return goods_id;
  }

  public void setGoods_id(int goods_id) {
    this.goods_id = goods_id;
  }

  public Date getCancel_time() {
    return cancel_time;
  }

  public void setCancel_time(Date cancel_time) {
    this.cancel_time = cancel_time;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public int getVideo_id() {
    return video_id;
  }

  public void setVideo_id(int video_id) {
    this.video_id = video_id;
  }
}
