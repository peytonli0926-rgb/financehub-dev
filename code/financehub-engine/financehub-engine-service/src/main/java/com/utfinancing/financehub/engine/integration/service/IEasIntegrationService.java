package com.utfinancing.financehub.engine.integration.service;

import com.utfinancing.financehub.engine.integration.model.eas.dto.EasVoucherDTO;
import com.utfinancing.financehub.engine.integration.model.eas.dto.EasVoucherRespDTO;

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

    /**
     * 添加凭证
     * @param voucherEntryList 凭证分录集合
     * @return
     */
    List<EasVoucherRespDTO> addVouchers(List<EasVoucherDTO> voucherEntryList);
}
