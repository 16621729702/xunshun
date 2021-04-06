package com.wink.livemall.member.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.Date;

@Table(name = "lm_member")
public class LmMember {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private String nickname;
  private String avatar;
  private String realname;
  private String mobile;
  private String idcard;
  private String gender;
  private String province;
  private String city;
  private String ip;
  private int state;
  private int usertype;
  private String country;
  private int credit;
  private Date updated_at;
  private Date created_at;
  private Date deleted_at;
  private String username;
  private String password;
  private int growth_value;
  private int level_id;
  private Date level_valid_time;
  private String paypassword;
  private BigDecimal credit2;
  private String open_id;
  private BigDecimal blance;

  public BigDecimal getBlance() {
    return blance;
  }

  public void setBlance(BigDecimal blance) {
    this.blance = blance;
  }

  public String getOpen_id() {
    return open_id;
  }

  public void setOpen_id(String open_id) {
    this.open_id = open_id;
  }

  public BigDecimal getCredit2() {
	return credit2;
}

public void setCredit2(BigDecimal credit2) {
	this.credit2 = credit2;
}

public LmMember() {
  }

  public String getPaypassword() {
    return paypassword;
  }

  public void setPaypassword(String paypassword) {
    this.paypassword = paypassword;
  }

  public int getGrowth_value() {
    return growth_value;
  }

  public void setGrowth_value(int growth_value) {
    this.growth_value = growth_value;
  }

  public int getLevel_id() {
    return level_id;
  }

  public void setLevel_id(int level_id) {
    this.level_id = level_id;
  }

  public Date getLevel_valid_time() {
    return level_valid_time;
  }

  public void setLevel_valid_time(Date level_valid_time) {
    this.level_valid_time = level_valid_time;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getRealname() {
    return realname;
  }

  public void setRealname(String realname) {
    this.realname = realname;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getIdcard() {
    return idcard;
  }

  public void setIdcard(String idcard) {
    this.idcard = idcard;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public int getUsertype() {
    return usertype;
  }

  public void setUsertype(int usertype) {
    this.usertype = usertype;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public int getCredit() {
    return credit;
  }

  public void setCredit(int credit) {
    this.credit = credit;
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

  public Date getDeleted_at() {
    return deleted_at;
  }

  public void setDeleted_at(Date deleted_at) {
    this.deleted_at = deleted_at;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


}
