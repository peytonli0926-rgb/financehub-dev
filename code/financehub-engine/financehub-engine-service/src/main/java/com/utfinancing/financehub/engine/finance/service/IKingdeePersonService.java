package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.KingdeePersonEntity;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeOptionQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeePersonDTO;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-31
 * @Description : KingdeePerson服务类接口
 * @Modified :
 */
public interface IKingdeePersonService extends IService<KingdeePersonEntity> {

    IPage<KingdeePersonDTO> selectPage(KingdeeOptionQueryDTO queryDTO);

}
