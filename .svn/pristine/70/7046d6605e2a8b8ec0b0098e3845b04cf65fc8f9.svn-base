package com.wink.livemall.sys.setting.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 快递公司管理
 */
@Table(name = "lm_setting_expresses")
public class Express {
    //显示
    public static int active = 1;
    //隐藏
    public static int inactive = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;

    private String name;

    private String code;

    private int sort;

    private int is_show;

    public Express() {
    }

    public Express(int id, String name, String code, int sort, int is_show) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.sort = sort;
        this.is_show = is_show;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }
}
