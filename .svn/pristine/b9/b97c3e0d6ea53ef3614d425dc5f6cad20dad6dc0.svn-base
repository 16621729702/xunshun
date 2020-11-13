package com.wink.livemall.goods.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 商品规格
 */
@Table(name = "lm_goods_specs")
public class GoodSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private int uniacid;
    private String title;
    private String description;
    private int displaytype;
    private String content;
    private int goods_id;
    private int displayorder;
    private Date updated_at;
    private Date created_at;

    public GoodSpec() {
    }

    public GoodSpec(int id, int uniacid, String title, String description, int displaytype, String content, int goods_id, int displayorder, Date updated_at, Date created_at) {
        this.id = id;
        this.uniacid = uniacid;
        this.title = title;
        this.description = description;
        this.displaytype = displaytype;
        this.content = content;
        this.goods_id = goods_id;
        this.displayorder = displayorder;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisplaytype() {
        return displaytype;
    }

    public void setDisplaytype(int displaytype) {
        this.displaytype = displaytype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(int displayorder) {
        this.displayorder = displayorder;
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
