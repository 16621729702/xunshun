package com.wink.livemall.member.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "lm_member_trace")
public class LmMemberTrace {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private int trace_id;
  private int trace_type;
  private java.util.Date trace_time;
  private int member_id;

  public LmMemberTrace() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getTrace_id() {
    return trace_id;
  }

  public void setTrace_id(int trace_id) {
    this.trace_id = trace_id;
  }

  public int getTrace_type() {
    return trace_type;
  }

  public void setTrace_type(int trace_type) {
    this.trace_type = trace_type;
  }

  public Date getTrace_time() {
    return trace_time;
  }

  public void setTrace_time(Date trace_time) {
    this.trace_time = trace_time;
  }

  public int getMember_id() {
    return member_id;
  }

  public void setMember_id(int member_id) {
    this.member_id = member_id;
  }
}
