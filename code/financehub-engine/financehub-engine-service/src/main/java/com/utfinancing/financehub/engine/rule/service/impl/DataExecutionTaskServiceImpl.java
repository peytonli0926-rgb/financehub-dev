package com.utfinancing.financehub.engine.rule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.utfinancing.financehub.engine.enums.DataExecutionTaskStatusEnum;
import com.utfinancing.financehub.engine.rule.model.dto.DataExecutionTaskDTO;
import com.utfinancing.financehub.engine.rule.entity.DataExecutionTaskEntity;
import com.utfinancing.financehub.engine.rule.mapper.DataExecutionTaskMapper;
import com.utfinancing.financehub.engine.rule.service.IDataExecutionTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2024-02-25
 * @Description :  DataExecutionTask服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class DataExecutionTaskServiceImpl extends ServiceImpl<DataExecutionTaskMapper, DataExecutionTaskEntity> implements IDataExecutionTaskService {

    private final DataExecutionTaskMapper dataExecutionTaskMapper;

    @Override
    public Long saveDataExecutionTask(DataExecutionTaskDTO dto) {
        DataExecutionTaskEntity entity = BeanUtil.copyProperties(dto, DataExecutionTaskEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateDataExecutionTask(Long id, DataExecutionTaskDTO dto) {
        DataExecutionTaskEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public DataExecutionTaskDTO getDataExecutionTaskDTOById(Long id) {
        DataExecutionTaskEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, DataExecutionTaskDTO.class);
    }

    @Override
    public DataExecutionTaskDTO getRunningTaskBySystem(String systemCode) {
        DataExecutionTaskEntity taskEntity = this.getOne(Wrappers.<DataExecutionTaskEntity>lambdaQuery()
                .eq(DataExecutionTaskEntity::getStatus, DataExecutionTaskStatusEnum.RUNNING.getCode())
                .eq(DataExecutionTaskEntity::getSystemCode, systemCode), false);
        if (taskEntity == null){
            return null;
        }
        return BeanUtil.copyProperties(taskEntity, DataExecutionTaskDTO.class);
    }

    @Override
    public Long createNewTask(String systemCode, String taskStatus, int dataSize, LocalDateTime businessDateStart, LocalDateTime businessDateEnd) {
        DataExecutionTaskEntity entity = new DataExecutionTaskEntity();
        entity.setSystemCode(systemCode);
        entity.setStatus(taskStatus);
        entity.setDataSize(dataSize);
        entity.setBusinessDateStart(businessDateStart);
        entity.setBusinessDateEnd(businessDateEnd);
        entity.setTaskStartTime(LocalDateTime.now());
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long startTask(String systemCode, String taskStatus, int dataSize, LocalDateTime businessDateStart, LocalDateTime businessDateEnd) {
        return 0L;
    }

    @Override
    public Long finishedTask(Long taskId, String taskStatus, int successSize, int failedSize) {
        DataExecutionTaskEntity taskEntity = new DataExecutionTaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setStatus(taskStatus);
        taskEntity.setDataSuccessSize(successSize);
        taskEntity.setDataFailedSize(failedSize);
        taskEntity.setTaskEndTime(LocalDateTime.now());
        this.updateById(taskEntity);
        return taskId;
    }

    @Override
    public void errorTask(Long taskId, String taskStatus, int successSize, int failedSize, String message) {
        DataExecutionTaskEntity taskEntity = new DataExecutionTaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setStatus(taskStatus);
        taskEntity.setDataSuccessSize(successSize);
        taskEntity.setDataFailedSize(failedSize);
        taskEntity.setTaskEndTime(LocalDateTime.now());
        taskEntity.setErrorMessage(message);
        this.updateById(taskEntity);
    }

    @Override
    public Long checkAndCreateTask(String systemCode) {
        // 查询是否有正在进行的任务
        DataExecutionTaskDTO taskDTO = getRunningTaskBySystem(systemCode);
        if (taskDTO != null) {
            throw new ServiceException("存在正在运行的任务，请稍后再试");
        }
        // 生成任务
        return createNewTask(systemCode, DataExecutionTaskStatusEnum.RUNNING.getCode(), 1, null, null);
    }


}

