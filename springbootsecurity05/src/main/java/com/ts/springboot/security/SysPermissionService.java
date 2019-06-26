package com.ts.springboot.security;

import com.ts.springboot.entity.SysPermission;
import com.ts.springboot.mapper.SysPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPermissionService {
    @Autowired
    private SysPermissionMapper permissionMapper;

    public List<SysPermission> listByRoleId(Integer roleId){
        return permissionMapper.listByRoleId(roleId);
    }
}
