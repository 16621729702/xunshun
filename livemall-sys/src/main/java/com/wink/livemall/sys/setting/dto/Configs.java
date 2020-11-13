package com.wink.livemall.sys.setting.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="lm_setting_configs")
public class Configs {
    //基础配置
    public static int type_basic=1;
    //推流配置
    public static int type_flow=2;
    //聊天配置
    public static int type_chat=3;
    //短信配置
    public static int type_sms=4;
    //支付配置
    public static int type_pay=5;
    //物流配置
    public static int type_logistics=6;
    //交易配置
    public static int type_trading=7;
    //直播间配置
    public static int type_live=8;
    //上传配置
    public static int type_upload=9;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;

    private int uniacid;

    private int type;

    private String config;

    private Date updated_at;

    private Date created_at;

    public Configs() {
    }

    public Configs(int id, int uniacid, int type, String config, Date updated_at, Date created_at) {
        this.id = id;
        this.uniacid = uniacid;
        this.type = type;
        this.config = config;
        this.updated_at = updated_at;
        this.created_at = created_at;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
