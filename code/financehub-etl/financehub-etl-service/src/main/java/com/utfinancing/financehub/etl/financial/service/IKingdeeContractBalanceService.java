package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.KingdeeContractBalanceQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeContractBalanceDTO;
import com.utfinancing.financehub.etl.financial.model.vo.KingdeeContractBalanceVO;
import com.utfinancing.financehub.etl.financial.entity.KingdeeContractBalanceEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-19
 * @Description : KingdeeContractBalance服务类接口
 * @Modified :
 */
public interface IKingdeeContractBalanceService extends IService<KingdeeContractBalanceEntity> {

    Long saveKingdeeContractBalance(KingdeeContractBalanceDTO dto);

    Long updateKingdeeContractBalance(Long id, KingdeeContractBalanceDTO dto);

    KingdeeContractBalanceDTO getKingdeeContractBalanceDTOById(Long id);

    IPage<KingdeeContractBalanceVO> selectPage(KingdeeContractBalanceQueryDTO queryDTO);

}
