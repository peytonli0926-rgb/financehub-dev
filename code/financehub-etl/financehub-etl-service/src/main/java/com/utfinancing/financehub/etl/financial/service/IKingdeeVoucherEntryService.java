package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.KingdeeVoucherEntryQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeVoucherEntryDTO;
import com.utfinancing.financehub.etl.financial.model.vo.KingdeeVoucherEntryVO;
import com.utfinancing.financehub.etl.financial.entity.KingdeeVoucherEntryEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-15
 * @Description : KingdeeVoucherEntry服务类接口
 * @Modified :
 */
public interface IKingdeeVoucherEntryService extends IService<KingdeeVoucherEntryEntity> {

    Long saveKingdeeVoucherEntry(KingdeeVoucherEntryDTO dto);

    Long updateKingdeeVoucherEntry(Long id, KingdeeVoucherEntryDTO dto);

    KingdeeVoucherEntryDTO getKingdeeVoucherEntryDTOById(Long id);

    IPage<KingdeeVoucherEntryVO> selectPage(KingdeeVoucherEntryQueryDTO queryDTO);

}
