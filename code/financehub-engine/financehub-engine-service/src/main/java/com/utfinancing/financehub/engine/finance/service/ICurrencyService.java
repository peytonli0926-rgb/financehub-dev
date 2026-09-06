package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.CurrencyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CurrencyDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CurrencyVO;
import com.utfinancing.financehub.engine.finance.entity.CurrencyEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-11
 * @Description : Currency服务类接口
 * @Modified :
 */
public interface ICurrencyService extends IService<CurrencyEntity> {

    Long saveCurrency(CurrencyDTO dto);

    Long updateCurrency(Long id, CurrencyDTO dto);

    CurrencyDTO getCurrencyDTOById(Long id);

    IPage<CurrencyVO> selectPage(CurrencyQueryDTO queryDTO);

}
