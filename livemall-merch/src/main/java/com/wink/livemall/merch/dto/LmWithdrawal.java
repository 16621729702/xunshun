package com.wink.livemall.merch.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "lm_withdrawal")
public class LmWithdrawal {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
	private int id;
	private int merchid;
	private BigDecimal money;
	private Date createdate;
	private Date applydate;
	private int status;
	private int checkid;
	private String name; //提现人名称
	private String type; //银行类型
	private String card; //银行卡号
	private BigDecimal poundage;
	private BigDecimal realmoney;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMerchid() {
		return merchid;
	}

	public void setMerchid(int merchid) {
		this.merchid = merchid;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getApplydate() {
		return applydate;
	}

	public void setApplydate(Date applydate) {
		this.applydate = applydate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCheckid() {
		return checkid;
	}

	public void setCheckid(int checkid) {
		this.checkid = checkid;
	}



	public BigDecimal getPoundage() {
		return poundage;
	}

	public void setPoundage(BigDecimal poundage) {
		this.poundage = poundage;
	}

	public BigDecimal getRealmoney() {
		return realmoney;
	}

	public void setRealmoney(BigDecimal realmoney) {
		this.realmoney = realmoney;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}
}
