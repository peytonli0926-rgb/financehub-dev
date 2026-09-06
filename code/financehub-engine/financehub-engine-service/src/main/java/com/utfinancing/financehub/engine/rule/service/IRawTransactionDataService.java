package com.utfinancing.financehub.engine.rule.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.rule.entity.RawTransactionDataEntity;
import com.utfinancing.financehub.engine.rule.model.dto.RawTransactionDataQueryDTO;
import com.utfinancing.financehub.engine.rule.model.dto.RawTransactionDataDTO;
import com.utfinancing.financehub.engine.rule.model.vo.RawTransactionDataDuplicateVo;
import com.utfinancing.financehub.engine.rule.model.vo.RawTransactionDataVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-16
 * @Description : RawTransationData服务类接口
 * @Modified :
 */
public interface IRawTransactionDataService extends IService<RawTransactionDataEntity> {

    Long saveRawTransationData(RawTransactionDataDTO dto);

    Long updateRawTransationData(Long id, RawTransactionDataDTO dto);

    Boolean updateStatus(Long id, String status, String errorInfo);

    RawTransactionDataDTO getRawTransationDataDTOById(Long id);

    IPage<RawTransactionDataVO> selectPage(RawTransactionDataQueryDTO queryDTO);


    RawTransactionDataEntity saveRawData(Map<String, Object> dataMap);

    RawTransactionDataEntity saveRawData(String messageId, JSONObject jsonData);

    List<RawTransactionDataDuplicateVo> getDuplicateData(String systemCode, List<String> ignoreRepeatDataSceneCode);
}
