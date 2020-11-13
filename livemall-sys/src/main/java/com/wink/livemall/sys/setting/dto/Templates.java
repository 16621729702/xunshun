package com.wink.livemall.sys.setting.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="lm_setting_templates")
public class Templates {
    //启用
    public static int active = 1;
    //禁用
    public static int inactive = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private int uniacid;
    private String name;
    private String tempkey;
    private String content;
    private String tempid;
    private int status;
    private Date created_at;
    private Date updated_at;

    public Templates() {
    }

    public Templates(int id, int uniacid, String name, String tempkey, String content, String tempid, int status, Date created_at, Date updated_at) {
        this.id = id;
        this.uniacid = uniacid;
        this.name = name;
        this.tempkey = tempkey;
        this.content = content;
        this.tempid = tempid;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static int getActive() {
        return active;
    }

    public static void setActive(int active) {
        Templates.active = active;
    }

    public static int getInactive() {
        return inactive;
    }

    public static void setInactive(int inactive) {
        Templates.inactive = inactive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUniacid() {
        return uniacid;
    }

    public void setUniacid(int uniacid) {
        this.uniacid = uniacid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTempkey() {
        return tempkey;
    }

    public void setTempkey(String tempkey) {
        this.tempkey = tempkey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTempid() {
        return tempid;
    }

    public void setTempid(String tempid) {
        this.tempid = tempid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
