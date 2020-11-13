package com.wink.livemall.goods.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 商品分类
 */
@Table(name = "lm_goods_categories")
public class GoodCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private int uniacid;
    private int parent_id;
    private String pic;
    private String name;
    private String home_pic;
    private String intro;
    private int sort;
    private int isshow;
    private int merchshow;
    private Date updated_at;
    private Date created_at;
    private Date deleted_at;
    //是否推荐 1是
    private int isrecommend;

    public int getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(int isrecommend) {
        this.isrecommend = isrecommend;
    }

    public GoodCategory() {
    }

    public int getMerchshow() {
        return merchshow;
    }

    public void setMerchshow(int merchshow) {
        this.merchshow = merchshow;
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

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHome_pic() {
        return home_pic;
    }

    public void setHome_pic(String home_pic) {
        this.home_pic = home_pic;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIsshow() {
        return isshow;
    }

    public void setIsshow(int isshow) {
        this.isshow = isshow;
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

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }
}
