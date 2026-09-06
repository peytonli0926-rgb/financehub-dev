package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.KingdeeHybVoucherQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeHybVoucherDTO;
import com.utfinancing.financehub.etl.financial.model.vo.KingdeeHybVoucherVO;
import com.utfinancing.financehub.etl.financial.entity.KingdeeHybVoucherEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-01
 * @Description : KingdeeHybVoucher服务类接口
 * @Modified :
 */
public interface IKingdeeHybVoucherService extends IService<KingdeeHybVoucherEntity> {

    Long saveKingdeeHybVoucher(KingdeeHybVoucherDTO dto);

    Long updateKingdeeHybVoucher(Long id, KingdeeHybVoucherDTO dto);

    KingdeeHybVoucherDTO getKingdeeHybVoucherDTOById(Long id);

    IPage<KingdeeHybVoucherVO> selectPage(KingdeeHybVoucherQueryDTO queryDTO);

}
