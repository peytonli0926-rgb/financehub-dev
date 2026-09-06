package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionUploadTaskQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionUploadTaskDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionUploadTaskVO;
import com.utfinancing.financehub.engine.finance.entity.ImpairmentProvisionUploadTaskEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-28
 * @Description : ImpairmentProvisionUploadTask服务类接口
 * @Modified :
 */
public interface IImpairmentProvisionUploadTaskService extends IService<ImpairmentProvisionUploadTaskEntity> {

    Long saveImpairmentProvisionUploadTask(ImpairmentProvisionUploadTaskDTO dto);

    Long updateImpairmentProvisionUploadTask(Long id, ImpairmentProvisionUploadTaskDTO dto);

    ImpairmentProvisionUploadTaskDTO getImpairmentProvisionUploadTaskDTOById(Long id);

    IPage<ImpairmentProvisionUploadTaskVO> selectPage(ImpairmentProvisionUploadTaskQueryDTO queryDTO);

}
