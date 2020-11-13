package com.wink.livemall.goods.dto;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "lm_lots_info")
public class LmLotsInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
	private int id;
	private String imgs;
	private String videos;
	private int goodsid;
	private Date createtime;
	private int merchid;
	private int liveid;
	private int type;//1原料展示 2开料结果 3毛坯结果 4抛光结果 5成品结果
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getMerchid() {
		return merchid;
	}
	public void setMerchid(int merchid) {
		this.merchid = merchid;
	}
	public int getLiveid() {
		return liveid;
	}
	public void setLiveid(int liveid) {
		this.liveid = liveid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImgs() {
		return imgs;
	}
	public void setImgs(String imgs) {
		this.imgs = imgs;
	}
	public String getVideos() {
		return videos;
	}
	public void setVideos(String videos) {
		this.videos = videos;
	}
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	
	
	
}
