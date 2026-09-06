package com.utfinancing.financehub.etl.financial.service;

import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.EasVoucherRespDTO;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * 金蝶服务集成接口
 */
public interface IEasIntegrationService {

    /**
     * 登录EAS
     * @return sessionId
     */
    String login();

    void logout(String sessionId);

//    String login2();

//    void logout2(String sessionId);

    @Async
    public void importVoucherToEAS(List<EasVoucherDTO> voucherEntryList,String systemCode) throws Exception;

    /**
     * 添加凭证
     * @param voucherEntryList 凭证分录集合
     * @return
     */
    List<EasVoucherRespDTO> addVouchers(List<EasVoucherDTO> voucherEntryList);
}
