package com.wink.livemall.member.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "lm_member_follow")
public class LmMemberFollow {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private int follow_id;
  private int follow_type;
  private java.util.Date follow_time;
  private java.util.Date cancel_time;
  private int state;
  private int member_id;

  public LmMemberFollow() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getFollow_id() {
    return follow_id;
  }

  public void setFollow_id(int follow_id) {
    this.follow_id = follow_id;
  }

  public int getFollow_type() {
    return follow_type;
  }

  public void setFollow_type(int follow_type) {
    this.follow_type = follow_type;
  }

  public Date getFollow_time() {
    return follow_time;
  }

  public void setFollow_time(Date follow_time) {
    this.follow_time = follow_time;
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

  public int getMember_id() {
    return member_id;
  }

  public void setMember_id(int member_id) {
    this.member_id = member_id;
  }
}
