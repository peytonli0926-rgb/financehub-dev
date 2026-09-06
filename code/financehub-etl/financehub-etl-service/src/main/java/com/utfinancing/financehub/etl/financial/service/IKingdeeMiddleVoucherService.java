package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.KingdeeMiddleVoucherQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeMiddleVoucherDTO;
import com.utfinancing.financehub.etl.financial.model.vo.KingdeeMiddleVoucherVO;
import com.utfinancing.financehub.etl.financial.entity.KingdeeMiddleVoucherEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-03
 * @Description : KingdeeMiddleVoucher服务类接口
 * @Modified :
 */
public interface IKingdeeMiddleVoucherService extends IService<KingdeeMiddleVoucherEntity> {

    Long saveKingdeeMiddleVoucher(KingdeeMiddleVoucherDTO dto);

    Long updateKingdeeMiddleVoucher(Long id, KingdeeMiddleVoucherDTO dto);

    KingdeeMiddleVoucherDTO getKingdeeMiddleVoucherDTOById(Long id);

    IPage<KingdeeMiddleVoucherVO> selectPage(KingdeeMiddleVoucherQueryDTO queryDTO);

}
