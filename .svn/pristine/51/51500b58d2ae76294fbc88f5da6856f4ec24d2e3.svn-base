package com.wink.livemall.sys.msg.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="lm_pushmsg")
public class LmPushmsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private String type;
    private String content;
    private java.util.Date createtime;
    private int sendid;
    private int recevieid;
    private int isread;
    private int ismerch;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public int getSendid() {
        return sendid;
    }

    public int getRecevieid() {
        return recevieid;
    }

    public void setRecevieid(int recevieid) {
        this.recevieid = recevieid;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }

    public void setSendid(int sendid) {
        this.sendid = sendid;
    }

    public int getIsmerch() {
        return ismerch;
    }

    public void setIsmerch(int ismerch) {
        this.ismerch = ismerch;
    }
}
