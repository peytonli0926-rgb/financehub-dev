package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.VoucherEntryQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherEntryDTO;
import com.utfinancing.financehub.etl.financial.model.vo.VoucherEntryVO;
import com.utfinancing.financehub.etl.financial.entity.VoucherEntryEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-16
 * @Description : VoucherEntry服务类接口
 * @Modified :
 */
public interface IVoucherEntryService extends IService<VoucherEntryEntity> {

    Long saveVoucherEntry(VoucherEntryDTO dto);

    Long updateVoucherEntry(Long id, VoucherEntryDTO dto);

    VoucherEntryDTO getVoucherEntryDTOById(Long id);

    IPage<VoucherEntryVO> selectPage(VoucherEntryQueryDTO queryDTO);

}
