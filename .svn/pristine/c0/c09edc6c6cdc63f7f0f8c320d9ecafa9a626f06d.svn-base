package com.wink.livemall.sys.permissionrole.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="lm_sys_permissionrole")
public class PermissionRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private int roleid;
    private int permissionid;

    public PermissionRole() {
    }

    public PermissionRole(int id, int roleid, int permissionid) {
        this.id = id;
        this.roleid = roleid;
        this.permissionid = permissionid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public int getPermissionid() {
        return permissionid;
    }

    public void setPermissionid(int permissionid) {
        this.permissionid = permissionid;
    }
}
