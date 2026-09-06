package com.utfinancing.financehub.engine.scene.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.scene.model.dto.TaxRateQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.TaxRateDTO;
import com.utfinancing.financehub.engine.scene.model.dto.TaxRateSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.TaxRateVO;
import com.utfinancing.financehub.engine.scene.entity.TaxRateEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-18
 * @Description : TaxRate服务类接口
 * @Modified :
 */
public interface ITaxRateService extends IService<TaxRateEntity> {

    Long saveTaxRate(TaxRateSaveDTO dto);

    Long updateTaxRate(Long id, TaxRateSaveDTO dto);

    TaxRateDTO getTaxRateDTOById(Long id);

    IPage<TaxRateVO> selectPage(TaxRateQueryDTO queryDTO);

    /**
     * 根据业务类型和金额类型查询可用的税率，没有返回空
     * @param businessCode
     * @param fundType
     * @return
     */
    BigDecimal getValidTaxRateByCode(String businessCode, String fundType);

    BigDecimal getGeneralValidTaxRateByLeaseType(String businessCode, String leaseType);

    List<TaxRateDTO> queryAllForEditor();


}
