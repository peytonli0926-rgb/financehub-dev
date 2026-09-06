package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.BatchModifyTemplateQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.BatchModifyTemplateDTO;
import com.utfinancing.financehub.engine.finance.model.vo.BatchModifyTemplateVO;
import com.utfinancing.financehub.engine.finance.entity.BatchModifyTemplateEntity;
import com.utfinancing.financehub.engine.finance.mapper.BatchModifyTemplateMapper;
import com.utfinancing.financehub.engine.finance.service.IBatchModifyTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : robjiang
 * @Date : Create in 2025-06-23
 * @Description :  BatchModifyTemplate服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class BatchModifyTemplateServiceImpl extends ServiceImpl<BatchModifyTemplateMapper, BatchModifyTemplateEntity> implements IBatchModifyTemplateService {

    private final BatchModifyTemplateMapper batchModifyTemplateMapper;

    @Override
    public Long saveBatchModifyTemplate(BatchModifyTemplateDTO dto) {
        BatchModifyTemplateEntity entity = BeanUtil.copyProperties(dto, BatchModifyTemplateEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateBatchModifyTemplate(Long id, BatchModifyTemplateDTO dto) {
        BatchModifyTemplateEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public BatchModifyTemplateDTO getBatchModifyTemplateDTOById(Long id) {
        BatchModifyTemplateEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, BatchModifyTemplateDTO.class);
    }

    @Override
    public IPage<BatchModifyTemplateVO> selectPage(BatchModifyTemplateQueryDTO queryDTO) {
        LambdaQueryWrapper<BatchModifyTemplateEntity> queryWrapper = Wrappers.<BatchModifyTemplateEntity>lambdaQuery();
        //这里注入查询条件
        IPage<BatchModifyTemplateEntity> entityIPage = batchModifyTemplateMapper.selectPage(new Page<BatchModifyTemplateEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, BatchModifyTemplateVO.class);
    }

    public void clearTableData() {
        batchModifyTemplateMapper.delete(new LambdaQueryWrapper<>());
    }
}

