package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.KingdeeGeneralAsstEntity;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeGeneralAsstDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeOptionQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeePersonDTO;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-31
 * @Description : KingdeeGeneralAsst服务类接口
 * @Modified :
 */
public interface IKingdeeGeneralAsstService extends IService<KingdeeGeneralAsstEntity> {

    IPage<KingdeeGeneralAsstDTO> selectPage(KingdeeOptionQueryDTO queryDTO);
}
