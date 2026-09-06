package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.CollectionTypeEnum;
import com.utfinancing.financehub.engine.finance.model.dto.BatchTaskQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.BatchTaskDTO;
import com.utfinancing.financehub.engine.finance.model.vo.BatchTaskVO;
import com.utfinancing.financehub.engine.finance.entity.BatchTaskEntity;
import com.utfinancing.financehub.engine.finance.mapper.BatchTaskMapper;
import com.utfinancing.financehub.engine.finance.service.IBatchTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2024-06-25
 * @Description :  BatchTask服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class BatchTaskServiceImpl extends ServiceImpl<BatchTaskMapper, BatchTaskEntity> implements IBatchTaskService {

    private final BatchTaskMapper batchTaskMapper;

    @Override
    public Long saveBatchTask(BatchTaskDTO dto) {
        BatchTaskEntity entity = BeanUtil.copyProperties(dto, BatchTaskEntity.class);
        entity.setStartTime(LocalDateTime.now());
        entity.setStatus("1");
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateBatchTask(Long id, BatchTaskDTO dto) {
        BatchTaskEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public BatchTaskDTO getBatchTaskDTOById(Long id) {
        BatchTaskEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, BatchTaskDTO.class);
    }

    @Override
    public IPage<BatchTaskVO> selectPage(BatchTaskQueryDTO queryDTO) {
        LambdaQueryWrapper<BatchTaskEntity> queryWrapper = Wrappers.<BatchTaskEntity>lambdaQuery();
        //这里注入查询条件
        IPage<BatchTaskEntity> entityIPage = batchTaskMapper.selectPage(new Page<BatchTaskEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, BatchTaskVO.class);
    }

    @Override
    public Boolean isExistTask(Long businessId, String businessType) {
        return this.lambdaQuery().eq(BatchTaskEntity::getBusinessId,businessId)
                .eq(BatchTaskEntity::getBusinessType,businessType)
                .eq(BatchTaskEntity::getStatus,"1").exists();
    }

    @Override
    public void updateBatchTask(List<Long> idList,String status) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        this.lambdaUpdate().set(BatchTaskEntity::getStatus,status).set(BatchTaskEntity::getEndTime,LocalDateTime.now()).in(BatchTaskEntity::getId,idList).update();
    }

}

