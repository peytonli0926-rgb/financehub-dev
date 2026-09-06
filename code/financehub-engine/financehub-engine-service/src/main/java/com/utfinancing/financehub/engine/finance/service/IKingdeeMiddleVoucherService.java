package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.KingdeeMiddleVoucherQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeMiddleVoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.KingdeeMiddleVoucherVO;
import com.utfinancing.financehub.engine.finance.entity.KingdeeMiddleVoucherEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-04
 * @Description : KingdeeMiddleVoucher服务类接口
 * @Modified :
 */
public interface IKingdeeMiddleVoucherService extends IService<KingdeeMiddleVoucherEntity> {

    Long saveKingdeeMiddleVoucher(KingdeeMiddleVoucherDTO dto);

    Long updateKingdeeMiddleVoucher(Long id, KingdeeMiddleVoucherDTO dto);

    KingdeeMiddleVoucherDTO getKingdeeMiddleVoucherDTOById(Long id);

    IPage<KingdeeMiddleVoucherVO> selectPage(KingdeeMiddleVoucherQueryDTO queryDTO);

}
