package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.KingdeeHybVoucherQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeHybVoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.KingdeeHybVoucherVO;
import com.utfinancing.financehub.engine.finance.entity.KingdeeHybVoucherEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-02
 * @Description : KingdeeHybVoucher服务类接口
 * @Modified :
 */
public interface IKingdeeHybVoucherService extends IService<KingdeeHybVoucherEntity> {

    Long saveKingdeeHybVoucher(KingdeeHybVoucherDTO dto);

    Long updateKingdeeHybVoucher(Long id, KingdeeHybVoucherDTO dto);

    KingdeeHybVoucherDTO getKingdeeHybVoucherDTOById(Long id);

    IPage<KingdeeHybVoucherVO> selectPage(KingdeeHybVoucherQueryDTO queryDTO);


    void generateKingdeeHybVoucher();

    void generateKingdeeMiddleVoucher();

}
