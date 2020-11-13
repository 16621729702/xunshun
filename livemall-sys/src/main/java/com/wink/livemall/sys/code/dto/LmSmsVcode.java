package com.wink.livemall.sys.code.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="lm_sms_vcode")
public class LmSmsVcode {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private String mobile;
  private String vcode;
  private java.util.Date creatdate;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }


  public String getVcode() {
    return vcode;
  }

  public void setVcode(String vcode) {
    this.vcode = vcode;
  }


  public java.util.Date getCreatdate() {
    return creatdate;
  }

  public void setCreatdate(java.util.Date creatdate) {
    this.creatdate = creatdate;
  }

}
