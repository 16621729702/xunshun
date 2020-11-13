package com.wink.livemall.sys.setting.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="lm_setting_lideshows")
public class Lideshow {
    //首页
    public static int INDEX = 1;
    //商户详细信息
    public static int MERCH = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private int uniacid;
    private int type;
    private int tag_id;
    private int merchid;
    private String name;
    private String wxappurl;
    private String pic;
    private int sort;
    private int link_type;
    private String link_data;
    private Date created_at;
    private Date updated_at;

    public Lideshow() {
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

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWxappurl() {
        return wxappurl;
    }

    public void setWxappurl(String wxappurl) {
        this.wxappurl = wxappurl;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getLink_type() {
        return link_type;
    }

    public void setLink_type(int link_type) {
        this.link_type = link_type;
    }

    public String getLink_data() {
        return link_data;
    }

    public void setLink_data(String link_data) {
        this.link_data = link_data;
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

    public int getMerchid() {
        return merchid;
    }

    public void setMerchid(int merchid) {
        this.merchid = merchid;
    }
}
