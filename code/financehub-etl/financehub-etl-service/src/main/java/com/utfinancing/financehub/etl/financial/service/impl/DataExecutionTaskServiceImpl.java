package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.enums.DataExecutionTaskStatusEnum;
import com.utfinancing.financehub.etl.financial.model.dto.DataExecutionTaskQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.DataExecutionTaskDTO;
import com.utfinancing.financehub.etl.financial.model.vo.DataExecutionTaskVO;
import com.utfinancing.financehub.etl.financial.entity.DataExecutionTaskEntity;
import com.utfinancing.financehub.etl.financial.mapper.DataExecutionTaskMapper;
import com.utfinancing.financehub.etl.financial.service.IDataExecutionTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2024-04-07
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
    public IPage<DataExecutionTaskVO> selectPage(DataExecutionTaskQueryDTO queryDTO) {
        LambdaQueryWrapper<DataExecutionTaskEntity> queryWrapper = Wrappers.<DataExecutionTaskEntity>lambdaQuery();
        //这里注入查询条件
        IPage<DataExecutionTaskEntity> entityIPage = dataExecutionTaskMapper.selectPage(new Page<DataExecutionTaskEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, DataExecutionTaskVO.class);
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

}

