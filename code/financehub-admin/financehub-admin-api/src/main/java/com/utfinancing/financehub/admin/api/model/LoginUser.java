package com.utfinancing.financehub.admin.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@Data
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户名id
     */
    private Long userid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 角色列表
     */
    private Set<String> roles;

    /**
     * 用户信息
     */
    private SysUser sysUser;

    // 以下为客户Users中的字段
    String staffCode;
    String staffName;
    String gender;
    String workplace;
    String staffType;
    String deptmentId;
    String deptmentName;
    String deptmentSecId;
    String deptmentSecName;
    String deptmentThrId;
    String deptmentThrName;
    String positionId;
    String positionName;
    String stat;
    String email;
    String phoneNumber;
    // accessToken字段
    private String access_token;
    private String refresh_token;
    private String token_type;
    private long expires_in;
    private long refresh_expires_in;

    /**
     * 数据权限列表 org_id
     */
    private List<String> orgIds;

}
