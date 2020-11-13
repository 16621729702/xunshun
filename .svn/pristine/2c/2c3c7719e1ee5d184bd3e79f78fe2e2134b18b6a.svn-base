package com.wink.livemall.sys.role.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="lm_sys_role")
public class Role {
    public static String ACTIVE="ACTIVE";
    public static String INACTIVE="INACTIVE";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private String name;
    private String code;
    private String status;
    private int orderno;

    public Role() {
    }

    public Role(int id, String name, String code, String status, int orderno) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.status = status;
        this.orderno = orderno;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrderno() {
        return orderno;
    }

    public void setOrderno(int orderno) {
        this.orderno = orderno;
    }


}
