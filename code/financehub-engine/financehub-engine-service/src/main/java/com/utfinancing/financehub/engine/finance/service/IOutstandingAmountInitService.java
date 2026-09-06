package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountInitQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountInitDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OutstandingAmountInitVO;
import com.utfinancing.financehub.engine.finance.entity.OutstandingAmountInitEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author : robjiang
 * @Date : Create in 2025-05-15
 * @Description : OutstandingAmountInit服务类接口
 * @Modified :
 */
public interface IOutstandingAmountInitService extends IService<OutstandingAmountInitEntity> {

    Long saveOutstandingAmountInit(OutstandingAmountInitDTO dto);

    Long updateOutstandingAmountInit(Long id, OutstandingAmountInitDTO dto);

    OutstandingAmountInitDTO getOutstandingAmountInitDTOById(Long id);

    IPage<OutstandingAmountInitVO> selectPage(OutstandingAmountInitQueryDTO queryDTO);

    /**
     * 查询合同的未实现收益总额
     */
    public Map<String, BigDecimal> selectEndBalFor(List<String> contractCodeList);

}
