package com.utfinancing.financehub.engine.integration.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.integration.model.dto.ExternalVoucherQueryDTO;
import com.utfinancing.financehub.engine.integration.model.dto.ExternalVoucherDTO;
import com.utfinancing.financehub.engine.integration.model.eas.dto.EasVoucherDTO;
import com.utfinancing.financehub.engine.integration.model.eas.dto.EasVoucherRespDTO;
import com.utfinancing.financehub.engine.integration.model.vo.ExternalVoucherVO;
import com.utfinancing.financehub.engine.integration.entity.ExternalVoucherEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-31
 * @Description : ExternalVoucher服务类接口
 * @Modified :
 */
public interface IExternalVoucherService extends IService<ExternalVoucherEntity> {

    /**
     * 添加外部凭证到金蝶
     * @param voucherEntryList
     * @return
     */
    List<EasVoucherRespDTO> addVoucherToEas(String systemCode, List<EasVoucherDTO> voucherEntryList);

}
