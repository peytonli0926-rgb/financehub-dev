package com.utfinancing.financehub.engine.rule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.rule.model.dto.MqErrorMessageQueryDTO;
import com.utfinancing.financehub.engine.rule.model.dto.MqErrorMessageDTO;
import com.utfinancing.financehub.engine.rule.model.vo.MqErrorMessageVO;
import com.utfinancing.financehub.engine.rule.entity.MqErrorMessageEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-08
 * @Description : MqErrorMessage服务类接口
 * @Modified :
 */
public interface IMqErrorMessageService extends IService<MqErrorMessageEntity> {

    Long saveMqErrorMessage(MqErrorMessageDTO dto);

    Long updateMqErrorMessage(Long id, MqErrorMessageDTO dto);

    MqErrorMessageDTO getMqErrorMessageDTOById(Long id);

    Boolean rePushMessage(List<Long> messageIdList);

    IPage<MqErrorMessageVO> selectPage(MqErrorMessageQueryDTO queryDTO);

}
