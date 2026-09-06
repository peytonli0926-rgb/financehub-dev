package com.utfinancing.financehub.engine.rule.service;

import com.utfinancing.financehub.engine.rule.model.dto.DataExecutionTaskDTO;
import com.utfinancing.financehub.engine.rule.entity.DataExecutionTaskEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2024-02-25
 * @Description : DataExecutionTask服务类接口
 * @Modified :
 */
public interface IDataExecutionTaskService extends IService<DataExecutionTaskEntity> {

    Long saveDataExecutionTask(DataExecutionTaskDTO dto);

    Long updateDataExecutionTask(Long id, DataExecutionTaskDTO dto);

    DataExecutionTaskDTO getDataExecutionTaskDTOById(Long id);

    DataExecutionTaskDTO getRunningTaskBySystem(String systemCode);

    Long createNewTask(String systemCode, String taskStatus, int dataSize, LocalDateTime businessDateStart, LocalDateTime businessDateEnd);
    Long startTask(String systemCode, String taskStatus, int dataSize, LocalDateTime businessDateStart, LocalDateTime businessDateEnd);

    Long finishedTask(Long taskId, String taskStatus, int successSize, int failedSize);

    void errorTask(Long taskId, String taskStatus, int successSize, int failedSize, String message);
    Long checkAndCreateTask(String systemCode);
}
