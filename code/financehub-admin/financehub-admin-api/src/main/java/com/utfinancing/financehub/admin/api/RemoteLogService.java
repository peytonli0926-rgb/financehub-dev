package com.utfinancing.financehub.admin.api;

import com.utfinancing.financehub.common.core.constant.SecurityConstants;
import com.utfinancing.financehub.common.core.constant.ServiceNameConstants;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.admin.api.model.SysOperLog;
import com.utfinancing.financehub.admin.api.factory.RemoteLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 日志服务
 *
 * @author ruoyi
 */
@FeignClient(contextId = "remoteLogService", value = ServiceNameConstants.ADMIN_SERVICE, fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogService {
    /**
     * 保存系统日志
     *
     * @param sysOperLog 日志实体
     * @param source     请求来源
     * @return 结果
     */
    @PostMapping("/operlog")
    public R<Boolean> saveLog(@RequestBody SysOperLog sysOperLog, @RequestHeader(SecurityConstants.FROM_SOURCE) String source) throws Exception;

}
