package com.wink.livemall.goods.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "lm_livegood")
public class LivedGood {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
	private int id;
	private String name;
	private BigDecimal price;
	private int type;
	private int status;
	private Date starttime;
	private Date endtime;
	private BigDecimal stepprice;
	private int liveid;
	private int tomemberid;
	private String img;
	private int delaytime;//延时时间

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public BigDecimal getStepprice() {
		return stepprice;
	}

	public void setStepprice(BigDecimal stepprice) {
		this.stepprice = stepprice;
	}

	public int getLiveid() {
		return liveid;
	}

	public void setLiveid(int liveid) {
		this.liveid = liveid;
	}

	public int getTomemberid() {
		return tomemberid;
	}

	public void setTomemberid(int tomemberid) {
		this.tomemberid = tomemberid;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getDelaytime() {
		return delaytime;
	}

	public void setDelaytime(int delaytime) {
		this.delaytime = delaytime;
	}
}
