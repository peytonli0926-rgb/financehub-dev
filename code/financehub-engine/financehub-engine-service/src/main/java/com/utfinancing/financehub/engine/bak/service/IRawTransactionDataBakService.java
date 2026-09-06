package com.utfinancing.financehub.engine.bak.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.bak.model.dto.RawTransactionDataBakQueryDTO;
import com.utfinancing.financehub.engine.bak.model.dto.RawTransactionDataBakDTO;
import com.utfinancing.financehub.engine.bak.model.vo.RawTransactionDataBakVO;
import com.utfinancing.financehub.engine.bak.entity.RawTransactionDataBakEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-18
 * @Description : RawTransactionDataBak服务类接口
 * @Modified :
 */
public interface IRawTransactionDataBakService extends IService<RawTransactionDataBakEntity> {

    Long saveRawTransactionDataBak(RawTransactionDataBakDTO dto);

    Long updateRawTransactionDataBak(Long id, RawTransactionDataBakDTO dto);

    RawTransactionDataBakDTO getRawTransactionDataBakDTOById(Long id);

    IPage<RawTransactionDataBakVO> selectPage(RawTransactionDataBakQueryDTO queryDTO);

    Boolean batchRunVoucher(String sceneCodes, String systemCode);

}
