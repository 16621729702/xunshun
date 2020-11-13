package com.wink.livemall.order.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "lm_express")
public class LmExpress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
	private int id;
	private String name;
	private String express;
	private int status;
	private int displayorder;
	private String code;
	private String coding;

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public int getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(int displayorder) {
		this.displayorder = displayorder;
	}

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



	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}



	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}

}
