package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.KingdeeAccountQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeAccountDTO;
import com.utfinancing.financehub.etl.financial.model.vo.KingdeeAccountVO;
import com.utfinancing.financehub.etl.financial.entity.KingdeeAccountEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : KingdeeAccount服务类接口
 * @Modified :
 */
public interface IKingdeeAccountService extends IService<KingdeeAccountEntity> {

    Long saveKingdeeAccount(KingdeeAccountDTO dto);

    Long updateKingdeeAccount(Long id, KingdeeAccountDTO dto);

    KingdeeAccountDTO getKingdeeAccountDTOById(Long id);

    IPage<KingdeeAccountVO> selectPage(KingdeeAccountQueryDTO queryDTO);

}
