package com.wink.livemall.goods.dto;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "lm_lots_log")
public class LmLotsLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
	private int id;
	private int memberid;
	private int goodsid;
	private int lot_no;
	private Date createtime;
	private int infoid;
	
	public int getInfoid() {
		return infoid;
	}
	public void setInfoid(int infoid) {
		this.infoid = infoid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMemberid() {
		return memberid;
	}
	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public int getLot_no() {
		return lot_no;
	}
	public void setLot_no(int lot_no) {
		this.lot_no = lot_no;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	
	
	
}
