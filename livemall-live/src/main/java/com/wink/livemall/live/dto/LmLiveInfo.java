package com.wink.livemall.live.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="lm_live_info")
public class LmLiveInfo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private int live_id;
    private int watchnum;
    private int focusnum;
    private Date create_time;
    private int addnum;

    public int getAddnum() {
        return addnum;
    }

    public void setAddnum(int addnum) {
        this.addnum = addnum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLive_id() {
        return live_id;
    }

    public void setLive_id(int live_id) {
        this.live_id = live_id;
    }

    public int getWatchnum() {
        return watchnum;
    }

    public void setWatchnum(int watchnum) {
        this.watchnum = watchnum;
    }

    public int getFocusnum() {
        return focusnum;
    }

    public void setFocusnum(int focusnum) {
        this.focusnum = focusnum;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
