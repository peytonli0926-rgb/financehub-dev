package com.utfinancing.financehub.engine.file.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.utfinancing.financehub.engine.file.model.dto.TenantConfigDto;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *
 * @author iwen
 * @date 2023/3/31 11:01
 */
@EnableConfigurationProperties
@RefreshScope
@Data
@Configuration
@ConfigurationProperties(prefix = "attachment")
public class AttachmentInfoConfig {
    private List<TenantConfigDto> tenants;

    //限制一些格式的文件不能上传
    private String[] fileExts;

    private Long batchMaxCount;

    private Long batchMaxFileSize;

    private Long batchMaxTotalSize;

    /**
     * 移动失败重试次数限制
     */
    private Integer retryMaxNum;

    /**
     * 根据租户编号查询配置信息
     * @param tenantCode
     * @return
     */
    public TenantConfigDto getByTenantCode(String tenantCode){
        if(StrUtil.isBlank(tenantCode)){
            throw new RuntimeException("tenantCode不能为空!");
        }

        if(CollUtil.isNotEmpty(tenants)){
            for (TenantConfigDto config : tenants) {
                if(StrUtil.equals(tenantCode,config.getTenantCode())){
                    return config;
                }
            }
        }
        return null;
    }

    /**
     * 该租户是否具有账号配置
     * @param tenantCode
     * @return
     */
    public boolean check(String tenantCode){
        if(StrUtil.isBlank(tenantCode)){
            throw new RuntimeException("tenantCode字段不能为空!");
        }
        if(CollUtil.isNotEmpty(tenants)){
            for (TenantConfigDto config : tenants) {
                if(StrUtil.equals(tenantCode,config.getTenantCode())){
                    return true;
                }
            }
        }
        String msg = "tenantCode:" + tenantCode + ",该租户尚未进行账号配置!";
        throw new RuntimeException(msg);
    }

    public String getBatchMaxFileSizeStr(){
        if(ObjectUtil.isEmpty(batchMaxFileSize)){
            return 0 + "MB";
        }

        return batchMaxFileSize / 1024 /1024 +"MB";
    }

    public String getBatchMaxTotalSizeStr(){
        if(ObjectUtil.isEmpty(batchMaxTotalSize)){
            return 0 + "MB";
        }

        return batchMaxTotalSize / 1024 /1024 +"MB";
    }
}
