package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.KingdeeBankEntity;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeBankDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeOptionQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeePersonDTO;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-31
 * @Description : KingdeeBank服务类接口
 * @Modified :
 */
public interface IKingdeeBankService extends IService<KingdeeBankEntity> {

    IPage<KingdeeBankDTO> selectPage(KingdeeOptionQueryDTO queryDTO);

}
