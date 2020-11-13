package com.wink.livemall.goods.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "lm_share_good")
public class LmShareGood {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
	private int id;
	private String name;
	private BigDecimal price;
	private String material;
	private int chipped_num;
	private BigDecimal chipped_price;
	private int status;
	private Date auction_start_time;
	private Date auction_end_time;
	private Date create_at;
	private int liveid;
	private String img;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public int getChipped_num() {
		return chipped_num;
	}

	public void setChipped_num(int chipped_num) {
		this.chipped_num = chipped_num;
	}

	public BigDecimal getChipped_price() {
		return chipped_price;
	}

	public void setChipped_price(BigDecimal chipped_price) {
		this.chipped_price = chipped_price;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getAuction_start_time() {
		return auction_start_time;
	}

	public void setAuction_start_time(Date auction_start_time) {
		this.auction_start_time = auction_start_time;
	}

	public Date getAuction_end_time() {
		return auction_end_time;
	}

	public void setAuction_end_time(Date auction_end_time) {
		this.auction_end_time = auction_end_time;
	}

	public Date getCreate_at() {
		return create_at;
	}

	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}

	public int getLiveid() {
		return liveid;
	}

	public void setLiveid(int liveid) {
		this.liveid = liveid;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
}
