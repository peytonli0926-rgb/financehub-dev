package com.utfinancing.financehub.engine.dw.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.dw.entity.TInfoPerformanceAttributionSyncEntity;
import com.utfinancing.financehub.engine.dw.model.dto.TInfoPerformanceAttributionSyncQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractVO;
import com.utfinancing.financehub.engine.finance.model.vo.TInfoPerformanceAttributionSyncVO;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description : TInfoPerformanceAttributionSync服务类接口
 * @Modified :
 */
public interface ITInfoPerformanceAttributionSyncService extends IService<TInfoPerformanceAttributionSyncEntity> {

    IPage<TInfoPerformanceAttributionSyncVO> selectPage(TInfoPerformanceAttributionSyncQueryDTO queryDTO);

}
