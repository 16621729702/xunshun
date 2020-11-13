package com.wink.livemall.merch.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "lm_merch_admin")
public class LmMerchAdmin {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
	private int id;
	private int merchid;
	private int memberid;

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

	public int getMemberid() {
		return memberid;
	}

	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}
}
