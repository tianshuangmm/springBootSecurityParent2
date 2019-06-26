package com.ts.springboot.security;

import com.ts.springboot.entity.SysPermission;
import com.ts.springboot.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
@Component//5-权限控制
public class CustomPermissionEvaluator implements PermissionEvaluator {
    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    /*第一个参数代表用户的权限身份，参数2和3分别和@PreAuthorize("hasPermission('/admin','r')")相对应即访问
    url和权限
          通过authentication 获取所有role
          遍历  role 获取  permission
          遍历 permission  比较url  和permission 返回true


    */
    public boolean hasPermission(Authentication authentication, Object o, Object o1) {
        //获取loadUserByUsername() 方法的结果
        User user = (User)authentication.getPrincipal();
        //获取loadUserByUsername() 中注入的角色权限
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        //遍历用户所有的角色
        List<SysPermission> sysPermissions =null;
        if(authorities!=null&&authorities.size()>=1){
            for (GrantedAuthority authority: authorities) {
                String roleName = authority.getAuthority();
                Integer roleId = sysRoleService.selectByName(roleName).getId();
                permissionService.listByRoleId(roleId);

            }
        }
        //遍历用户的权限
        if(sysPermissions!=null&&sysPermissions.size()>=1){
            for (SysPermission sysPermission: sysPermissions) {
                List permissions = sysPermission.getPermissions();
                if(o.equals(sysPermission.getUrl())&&o1.equals(sysPermission)){
                    return true;
                }

            }
        }




        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
