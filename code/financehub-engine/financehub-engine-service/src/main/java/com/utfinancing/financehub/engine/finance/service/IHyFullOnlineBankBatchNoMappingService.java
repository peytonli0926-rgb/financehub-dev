package com.utfinancing.financehub.engine.finance.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.HyFullOnlineBankBatchNoMappingQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.HyFullOnlineBankBatchNoMappingDTO;
import com.utfinancing.financehub.engine.finance.model.vo.HyFullOnlineBankBatchNoMappingVO;
import com.utfinancing.financehub.engine.finance.entity.HyFullOnlineBankBatchNoMappingEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 2024-03-21
 * @Description : HyFullOnlineBankBatchNoMapping服务类接口
 * @Modified :
 */
public interface IHyFullOnlineBankBatchNoMappingService extends IService<HyFullOnlineBankBatchNoMappingEntity> {

    Long saveHyFullOnlineBankBatchNoMapping(HyFullOnlineBankBatchNoMappingDTO dto);

    Long updateHyFullOnlineBankBatchNoMapping(Long id, HyFullOnlineBankBatchNoMappingDTO dto);

    HyFullOnlineBankBatchNoMappingDTO getHyFullOnlineBankBatchNoMappingDTOById(Long id);

    IPage<HyFullOnlineBankBatchNoMappingVO> selectPage(HyFullOnlineBankBatchNoMappingQueryDTO queryDTO);

    /**
     * 接收消息队列中恒运网银编号和批扣号的映射关系数据
     */
    public void receiveBusinessDataFromMQ(String messageId, JSONObject jsonObject);


    /**
     * 根据条件查询业务网银编号与批扣号得映射关系
     */
    public List<HyFullOnlineBankBatchNoMappingEntity> selectMappingDataByCon(HyFullOnlineBankBatchNoMappingEntity params);


    /**
     * 根据网银编号查询批扣记录
     */
    public List<HyFullOnlineBankBatchNoMappingEntity> selectBusinessOnlineBankNoBatchNoMapping(String onlineBankNo);
}
