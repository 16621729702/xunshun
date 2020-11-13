package com.wink.livemall.sys.setting.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="lm_setting_tags")
public class Tag {
    //显示
    public static int active = 1;
    //隐藏
    public static int inactive = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private int uniacid;
    private String name;
    private String title;
    private int isshow;
    private int sort;
    private int type;
    private int style;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;

    public Tag() {
    }

    public Tag(int id, int uniacid, String name, String title, int isshow, int sort, int type, int style, Date created_at, Date updated_at, Date deleted_at) {
        this.id = id;
        this.uniacid = uniacid;
        this.name = name;
        this.title = title;
        this.isshow = isshow;
        this.sort = sort;
        this.type = type;
        this.style = style;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsshow() {
        return isshow;
    }

    public void setIsshow(int isshow) {
        this.isshow = isshow;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
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

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }
}
