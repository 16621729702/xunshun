package com.wink.livemall.live.dto;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="lm_live_log")
public class LmLiveLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private int merchid;
    private Date starttime;
    private Date endtime;
    private int diff;
    private int status;
    private int liveid;
    
	public int getLiveid() {
		return liveid;
	}
	public void setLiveid(int liveid) {
		this.liveid = liveid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
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
	public int getDiff() {
		return diff;
	}
	public void setDiff(int diff) {
		this.diff = diff;
	}
    
    
   
}
