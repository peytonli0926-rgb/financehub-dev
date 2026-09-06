package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.financial.model.dto.SendEas2ResultQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.SendEas2ResultDTO;
import com.utfinancing.financehub.etl.financial.model.vo.SendEas2ResultVO;
import com.utfinancing.financehub.etl.financial.entity.SendEas2ResultEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : bruyang
 * @Date : Create in 2024-04-10
 * @Description : SendEas2Result服务类接口
 * @Modified :
 */
public interface ISendEas2ResultService extends IService<SendEas2ResultEntity> {

    Long saveSendEas2Result(SendEas2ResultDTO dto);

    Long updateSendEas2Result(Long id, SendEas2ResultDTO dto);

    SendEas2ResultDTO getSendEas2ResultDTOById(Long id);

    IPage<SendEas2ResultVO> selectPage(SendEas2ResultQueryDTO queryDTO);

    void saveFidSendEas2Result(List<EasVoucherDTO> easVoucherDTOList, Map resultMap,String systemCode,String batchUuid);

}
