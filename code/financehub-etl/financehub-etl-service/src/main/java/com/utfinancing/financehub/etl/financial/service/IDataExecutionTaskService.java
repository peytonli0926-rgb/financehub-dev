package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.DataExecutionTaskQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.DataExecutionTaskDTO;
import com.utfinancing.financehub.etl.financial.model.vo.DataExecutionTaskVO;
import com.utfinancing.financehub.etl.financial.entity.DataExecutionTaskEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-04-07
 * @Description : DataExecutionTask服务类接口
 * @Modified :
 */
public interface IDataExecutionTaskService extends IService<DataExecutionTaskEntity> {

    Long saveDataExecutionTask(DataExecutionTaskDTO dto);

    Long updateDataExecutionTask(Long id, DataExecutionTaskDTO dto);

    DataExecutionTaskDTO getDataExecutionTaskDTOById(Long id);

    IPage<DataExecutionTaskVO> selectPage(DataExecutionTaskQueryDTO queryDTO);

    DataExecutionTaskDTO getRunningTaskBySystem(String systemCode);

    Long createNewTask(String systemCode, String taskStatus, int dataSize, LocalDateTime businessDateStart, LocalDateTime businessDateEnd);

    Long finishedTask(Long taskId, String taskStatus, int successSize, int failedSize);
}
