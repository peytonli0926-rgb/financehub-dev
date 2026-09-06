package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.EasExchangeRateQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.EasExchangeRateDTO;
import com.utfinancing.financehub.engine.finance.model.vo.EasExchangeRateVO;
import com.utfinancing.financehub.engine.finance.entity.EasExchangeRateEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-16
 * @Description : EasExchangeRate服务类接口
 * @Modified :
 */
public interface IEasExchangeRateService extends IService<EasExchangeRateEntity> {

    Long saveEasExchangeRate(EasExchangeRateDTO dto);

    Long updateEasExchangeRate(Long id, EasExchangeRateDTO dto);

    EasExchangeRateDTO getEasExchangeRateDTOById(Long id);

    IPage<EasExchangeRateVO> selectPage(EasExchangeRateQueryDTO queryDTO);

    BigDecimal getRateBySourceNameAndTargetName(String originCurrency, String targetCurrency);
}
