package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.entity.VoucherDimensionValueEntity;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherEntryDimensionValueQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherEntryDimensionValueDTO;
import com.utfinancing.financehub.etl.financial.model.vo.VoucherEntryDimensionValueVO;
import com.utfinancing.financehub.etl.financial.entity.VoucherEntryDimensionValueEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-19
 * @Description : VoucherEntryDimensionValue服务类接口
 * @Modified :
 */
public interface IVoucherEntryDimensionValueService extends IService<VoucherEntryDimensionValueEntity> {

    Long saveVoucherEntryDimensionValue(VoucherEntryDimensionValueDTO dto);

    Long updateVoucherEntryDimensionValue(Long id, VoucherEntryDimensionValueDTO dto);

    VoucherEntryDimensionValueDTO getVoucherEntryDimensionValueDTOById(Long id);

    IPage<VoucherEntryDimensionValueVO> selectPage(VoucherEntryDimensionValueQueryDTO queryDTO);

    /**
     * 取得业务主键
     */
    String getBusinessKey(VoucherEntryDimensionValueEntity params);

}
