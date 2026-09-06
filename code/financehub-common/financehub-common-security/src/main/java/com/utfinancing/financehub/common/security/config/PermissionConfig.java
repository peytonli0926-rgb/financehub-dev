package com.utfinancing.financehub.common.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 权限系统对接配置
 *
 * @author ruoyi
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "sso.permission")
public class PermissionConfig {
    private String bpmDomain;
    private String bpmPermissionUrlWeb;
    private String bpmSyscode;
    private String bpmToken;
    private String bpmPermissionUrlVerify;

    public String getBpmDomain() {
        return bpmDomain;
    }

    public void setBpmDomain(String bpmDomain) {
        this.bpmDomain = bpmDomain;
    }

    public String getBpmPermissionUrlWeb() {
        return bpmPermissionUrlWeb;
    }

    public void setBpmPermissionUrlWeb(String bpmPermissionUrlWeb) {
        this.bpmPermissionUrlWeb = bpmPermissionUrlWeb;
    }

    public String getBpmSyscode() {
        return bpmSyscode;
    }

    public void setBpmSyscode(String bpmSyscode) {
        this.bpmSyscode = bpmSyscode;
    }

    public String getBpmToken() {
        return bpmToken;
    }

    public void setBpmToken(String bpmToken) {
        this.bpmToken = bpmToken;
    }

    public String getBpmPermissionUrlVerify() {
        return bpmPermissionUrlVerify;
    }

    public void setBpmPermissionUrlVerify(String bpmPermissionUrlVerify) {
        this.bpmPermissionUrlVerify = bpmPermissionUrlVerify;
    }
}
