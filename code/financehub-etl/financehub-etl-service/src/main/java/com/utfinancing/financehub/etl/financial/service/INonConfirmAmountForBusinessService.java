package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.NonConfirmAmountForBusinessQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.NonConfirmAmountForBusinessDTO;
import com.utfinancing.financehub.etl.financial.model.vo.NonConfirmAmountForBusinessVO;
import com.utfinancing.financehub.etl.financial.entity.NonConfirmAmountForBusinessEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 2024-07-02
 * @Description : NonConfirmAmountForBusiness服务类接口
 * @Modified :
 */
public interface INonConfirmAmountForBusinessService extends IService<NonConfirmAmountForBusinessEntity> {

    Long saveNonConfirmAmountForBusiness(NonConfirmAmountForBusinessDTO dto);

    Long updateNonConfirmAmountForBusiness(Long id, NonConfirmAmountForBusinessDTO dto);

    NonConfirmAmountForBusinessDTO getNonConfirmAmountForBusinessDTOById(Long id);

    IPage<NonConfirmAmountForBusinessVO> selectPage(NonConfirmAmountForBusinessQueryDTO queryDTO);

    /**
     * 从业务系统同步未确认金额
     */
    public void nonConfirmAmountSyncJob();
}
