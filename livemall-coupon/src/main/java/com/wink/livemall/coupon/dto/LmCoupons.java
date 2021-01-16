package com.wink.livemall.coupon.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "lm_coupons")
public class LmCoupons {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
  private int id;

  private Integer sellerId;

  private String couponName;

  private BigDecimal couponValue;

  private BigDecimal minAmount;

  private Date sendStartTime;

  private Date sendEndTime;

  private String useStartTime;

  private String useEndTime;

  private Integer personLimitNum;

  private Integer totalLimitNum;

  private Integer receivedNum;

  private Integer type;

  private Integer channel;

  private Integer status;

  private String remark;

  private Date createTime;

  private Date updateTime;

  private String productIds;

  private Integer productType;

  public Integer getProductType() {
    return productType;
  }

  public void setProductType(Integer productType) {
    this.productType = productType;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Integer getSellerId() {
    return sellerId;
  }

  public void setSellerId(Integer sellerId) {
    this.sellerId = sellerId;
  }

  public String getCouponName() {
    return couponName;
  }

  public void setCouponName(String couponName) {
    this.couponName = couponName;
  }

  public BigDecimal getCouponValue() {
    return couponValue;
  }

  public void setCouponValue(BigDecimal couponValue) {
    this.couponValue = couponValue;
  }

  public BigDecimal getMinAmount() {
    return minAmount;
  }

  public void setMinAmount(BigDecimal minAmount) {
    this.minAmount = minAmount;
  }

  public Date getSendStartTime() {
    return sendStartTime;
  }

  public void setSendStartTime(Date sendStartTime) {
    this.sendStartTime = sendStartTime;
  }

  public Date getSendEndTime() {
    return sendEndTime;
  }

  public void setSendEndTime(Date sendEndTime) {
    this.sendEndTime = sendEndTime;
  }

  public String getUseStartTime() {
    return useStartTime;
  }

  public void setUseStartTime(String useStartTime) {
    this.useStartTime = useStartTime;
  }

  public String getUseEndTime() {
    return useEndTime;
  }

  public void setUseEndTime(String useEndTime) {
    this.useEndTime = useEndTime;
  }

  public Integer getPersonLimitNum() {
    return personLimitNum;
  }

  public void setPersonLimitNum(Integer personLimitNum) {
    this.personLimitNum = personLimitNum;
  }

  public Integer getTotalLimitNum() {
    return totalLimitNum;
  }

  public void setTotalLimitNum(Integer totalLimitNum) {
    this.totalLimitNum = totalLimitNum;
  }

  public Integer getReceivedNum() {
    return receivedNum;
  }

  public void setReceivedNum(Integer receivedNum) {
    this.receivedNum = receivedNum;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getChannel() {
    return channel;
  }

  public void setChannel(Integer channel) {
    this.channel = channel;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getProductIds() {
    return productIds;
  }

  public void setProductIds(String productIds) {
    this.productIds = productIds;
  }
}
