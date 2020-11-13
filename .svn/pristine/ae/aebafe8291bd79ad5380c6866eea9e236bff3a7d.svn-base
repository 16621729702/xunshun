package com.wink.livemall.goods.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 商品规格项
 */
@Table(name = "lm_goods_spec_items")
public class GoodSpecItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private int uniacid;
    private String title;
    private String thumb;
    private int isshow;
    private int spec_id;
    private int goods_id;
    private int displayorder;
    private Date updated_at;
    private Date created_at;

    public GoodSpecItem() {
    }


    public GoodSpecItem(int id, int uniacid, String title, String thumb, int isshow, int spec_id, int goods_id, int displayorder, Date updated_at, Date created_at) {
        this.id = id;
        this.uniacid = uniacid;
        this.title = title;
        this.thumb = thumb;
        this.isshow = isshow;
        this.spec_id = spec_id;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getIsshow() {
        return isshow;
    }

    public void setIsshow(int isshow) {
        this.isshow = isshow;
    }

    public int getSpec_id() {
        return spec_id;
    }

    public void setSpec_id(int spec_id) {
        this.spec_id = spec_id;
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
