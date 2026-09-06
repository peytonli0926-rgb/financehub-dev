package com.utfinancing.financehub.etl.xxljob;

import com.utfinancing.financehub.etl.financial.service.IKingdeeDataSyncService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class KingdeeBasicDataJobHandler {

    @Resource
    private IKingdeeDataSyncService kingdeeDataSyncService;

    /**
     * 每天十二点刷新一次金蝶借款合同编号等数据
     */
    @XxlJob(value = "generalKingdeelAsst")
    public void generalKingdeelAsst(){
         log.info("刷新金蝶借款合同编号数据开始");
         kingdeeDataSyncService.syncGeneralAsstAll();
        log.info("刷新金蝶借款合同编号数据结束");
    }

    /**
     * 每天十二点同步一次银行账号信息
     */
    @XxlJob(value = "syncBankAccountAll")
    public void syncBankAccountAll(){
        log.info("刷新金蝶银行账号数据开始");
        kingdeeDataSyncService.syncBankAccountAll();
        log.info("刷新金蝶银行账号数据结束");
    }

}
