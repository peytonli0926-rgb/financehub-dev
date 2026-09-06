package com.utfinancing.financehub.admin.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * token信息
 *
 * @author ruoyi
 */
@Data
public class AccessToken implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;

    // accessToken字段
    private String access_token;
    private String refresh_token;
    private String token_type;
    private long expires_in;
    private long refresh_expires_in;

}
