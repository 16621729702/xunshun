package com.wink.livemall.merch.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.Date;

@Table(name = "lm_auction_deposit")
public class LmAuctionDeposit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
	private int id;
	private Date createtime;
	private int memberid;
	private int merchid;
	private int goodsid;
	private BigDecimal money;
	private int status;
	private int type;
	private BigDecimal price;
	private BigDecimal rate; 
	private String sn;
	private int ispay;
	private Date paytime;
	private int paytype;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public int getMemberid() {
		return memberid;
	}
	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}
	public int getMerchid() {
		return merchid;
	}
	public void setMerchid(int merchid) {
		this.merchid = merchid;
	}
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public int getIspay() {
		return ispay;
	}
	public void setIspay(int ispay) {
		this.ispay = ispay;
	}
	public Date getPaytime() {
		return paytime;
	}
	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}
	public int getPaytype() {
		return paytype;
	}
	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}

	

}
