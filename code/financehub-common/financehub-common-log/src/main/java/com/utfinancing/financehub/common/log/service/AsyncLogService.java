package com.utfinancing.financehub.common.log.service;

import com.utfinancing.financehub.common.core.constant.SecurityConstants;
import com.utfinancing.financehub.admin.api.RemoteLogService;
import com.utfinancing.financehub.admin.api.model.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用日志服务
 *
 * @author ruoyi
 */
@Service
public class AsyncLogService {
    @Autowired
    private RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(SysOperLog sysOperLog) throws Exception {
        remoteLogService.saveLog(sysOperLog, SecurityConstants.INNER);
    }
}
