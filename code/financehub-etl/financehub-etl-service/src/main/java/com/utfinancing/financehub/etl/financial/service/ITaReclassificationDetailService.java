package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.financial.entity.TaReclassificationDetailEntity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description : TaReclassificationDetail服务类接口
 * @Modified :
 */
public interface ITaReclassificationDetailService extends IService<TaReclassificationDetailEntity> {

    List<TaReclassificationDetailEntity> selectDetailDataAndContractInfo(String queryDateStr);

    List<TaReclassificationDetailEntity> selectTYPTInfo(String queryDate, String queryDateNextDay, String code);

}
