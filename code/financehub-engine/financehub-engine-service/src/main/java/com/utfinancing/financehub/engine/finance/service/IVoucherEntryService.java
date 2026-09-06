package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.VoucherEntryQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherEntryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherEntryVO;
import com.utfinancing.financehub.engine.finance.entity.VoucherEntryEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-01
 * @Description : VoucherEntry服务类接口
 * @Modified :
 */
public interface IVoucherEntryService extends IService<VoucherEntryEntity> {

    Long saveVoucherEntry(VoucherEntryDTO dto);

    Long updateVoucherEntry(Long id, VoucherEntryDTO dto);

    VoucherEntryDTO getVoucherEntryDTOById(Long id);

    IPage<VoucherEntryVO> selectPage(VoucherEntryQueryDTO queryDTO);

    List<VoucherEntryVO> selectCourtCostByCondition(VoucherEntryQueryDTO queryDTO);

    Long modifyVoucherEntry(Long id, VoucherEntryDTO dto);

    /**
     * 凭证分录取得
     */
    public List<VoucherEntryEntity> selectByVoucherId(Long voucherId);
}
