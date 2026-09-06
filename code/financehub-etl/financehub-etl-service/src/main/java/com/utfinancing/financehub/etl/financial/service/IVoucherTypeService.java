package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.VoucherTypeQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherTypeDTO;
import com.utfinancing.financehub.etl.financial.model.vo.VoucherTypeVO;
import com.utfinancing.financehub.etl.financial.entity.VoucherTypeEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : VoucherType服务类接口
 * @Modified :
 */
public interface IVoucherTypeService extends IService<VoucherTypeEntity> {

    Long saveVoucherType(VoucherTypeDTO dto);

    Long updateVoucherType(Long id, VoucherTypeDTO dto);

    VoucherTypeDTO getVoucherTypeDTOById(Long id);

    IPage<VoucherTypeVO> selectPage(VoucherTypeQueryDTO queryDTO);

}
