package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.KingdeeCostcenterEntity;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeCostcenterDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeOptionQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeePersonDTO;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-31
 * @Description : KingdeeCostcenter服务类接口
 * @Modified :
 */
public interface IKingdeeCostcenterService extends IService<KingdeeCostcenterEntity> {

    IPage<KingdeeCostcenterDTO> selectPage(KingdeeOptionQueryDTO queryDTO);

    List<KingdeeCostcenterDTO> selectAll();
}
