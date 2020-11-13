package com.wink.livemall.member.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.Date;

@Table(name = "lm_member_log")
public class LmMemberLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
	private int id;
	private Date createtime;
	private int memberid;
	private String mark;
	private int type;
	private BigDecimal num;
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
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public BigDecimal getNum() {
		return num;
	}
	public void setNum(BigDecimal num) {
		this.num = num;
	}
	
	

}
