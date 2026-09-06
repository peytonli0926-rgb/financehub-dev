package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.KingdeeVoucherEntryInnerDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeVoucherQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeVoucherDTO;
import com.utfinancing.financehub.etl.financial.model.vo.KingdeeVoucherVO;
import com.utfinancing.financehub.etl.financial.entity.KingdeeVoucherEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-14
 * @Description : KingdeeVoucher服务类接口
 * @Modified :
 */
public interface IKingdeeVoucherService extends IService<KingdeeVoucherEntity> {

    Long saveKingdeeVoucher(KingdeeVoucherDTO dto);

    Long updateKingdeeVoucher(Long id, KingdeeVoucherDTO dto);

    KingdeeVoucherDTO getKingdeeVoucherDTOById(Long id);

    IPage<KingdeeVoucherVO> selectPage(KingdeeVoucherQueryDTO queryDTO);


    List<KingdeeVoucherEntryInnerDTO> selectKingdeeVoucherEntrySumByPeriod(Integer periodCode,List<String> contractCodeList);
}
