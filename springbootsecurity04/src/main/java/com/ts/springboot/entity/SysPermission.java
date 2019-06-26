package com.ts.springboot.entity;

import java.util.Arrays;
import java.util.List;

public class SysPermission {
    static final long serialVersionUID = 1L;

    private Integer id;

    private String url;

    private Integer roleId;

    private String permission;

    private List permissions;

    // 省略除permissions外的getter/setter

    public List getPermissions() {
        return Arrays.asList(this.permission.trim().split(","));
    }

    public void setPermissions(List permissions) {
        this.permissions = permissions;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public String getPermission() {
        return permission;
    }
}
