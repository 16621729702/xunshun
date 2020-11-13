package com.wink.livemall.live.dto;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="lm_lives")
public class LmLive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private int categoryid;
    private int merch_id;
    private String name;
    private String pushurl;
    private String readurl;
    private int watchnum;
    private String img;
    private int isrecommend;//是否被推荐
    private int status;
    private int isstart;
    private String livegroupid;//直播间群组id
    private int type;


    public int getIsstart() {
		return isstart;
	}

	public void setIsstart(int isstart) {
		this.isstart = isstart;
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

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getMerch_id() {
        return merch_id;
    }

    public void setMerch_id(int merch_id) {
        this.merch_id = merch_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPushurl() {
        return pushurl;
    }

    public void setPushurl(String pushurl) {
        this.pushurl = pushurl;
    }

    public String getReadurl() {
        return readurl;
    }

    public void setReadurl(String readurl) {
        this.readurl = readurl;
    }

    public int getWatchnum() {
        return watchnum;
    }

    public void setWatchnum(int watchnum) {
        this.watchnum = watchnum;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(int isrecommend) {
        this.isrecommend = isrecommend;
    }

    public String getLivegroupid() {
        return livegroupid;
    }

    public void setLivegroupid(String livegroupid) {
        this.livegroupid = livegroupid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
