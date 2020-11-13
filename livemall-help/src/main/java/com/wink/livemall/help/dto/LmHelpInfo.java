package com.wink.livemall.help.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "lm_help_info")
public class LmHelpInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;
  private int hCategoryId;
  private String title;
  private String content;
  private int state;
  private int sort;
  private Date createAt;
  private Date updateAt;

  public LmHelpInfo() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int gethCategoryId() {
    return hCategoryId;
  }

  public void sethCategoryId(int hCategoryId) {
    this.hCategoryId = hCategoryId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public int getSort() {
    return sort;
  }

  public void setSort(int sort) {
    this.sort = sort;
  }

  public Date getCreateAt() {
    return createAt;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

  public Date getUpdateAt() {
    return updateAt;
  }

  public void setUpdateAt(Date updateAt) {
    this.updateAt = updateAt;
  }
}
