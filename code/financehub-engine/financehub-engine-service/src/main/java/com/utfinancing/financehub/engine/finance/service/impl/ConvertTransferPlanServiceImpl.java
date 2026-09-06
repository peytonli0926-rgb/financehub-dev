package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferPlanDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferPlanVO;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferPlanEntity;
import com.utfinancing.financehub.engine.finance.mapper.ConvertTransferPlanMapper;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Objects;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description :  ConvertTransferPlan服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ConvertTransferPlanServiceImpl extends ServiceImpl<ConvertTransferPlanMapper, ConvertTransferPlanEntity> implements IConvertTransferPlanService {

    private final ConvertTransferPlanMapper convertTransferPlanMapper;

    @Override
    public Long saveConvertTransferPlan(ConvertTransferPlanDTO dto) {
        ConvertTransferPlanEntity entity = BeanUtil.copyProperties(dto, ConvertTransferPlanEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateConvertTransferPlan(Long id, ConvertTransferPlanDTO dto) {
        ConvertTransferPlanEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ConvertTransferPlanDTO getConvertTransferPlanDTOById(Long id) {
        ConvertTransferPlanEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ConvertTransferPlanDTO.class);
    }

    @Override
    public IPage<ConvertTransferPlanVO> selectPage(ConvertTransferPlanQueryDTO queryDTO) {
        LambdaQueryWrapper<ConvertTransferPlanEntity> queryWrapper = Wrappers.lambdaQuery();
        // 这里注入查询条件
        queryWrapper.eq(ConvertTransferPlanEntity::getConvertTransferId, queryDTO.getConvertTransferId())
                .eq(Objects.nonNull(queryDTO.getOldContractCode()), ConvertTransferPlanEntity::getOldContractCode, queryDTO.getOldContractCode());
        IPage<ConvertTransferPlanEntity> entityIPage = convertTransferPlanMapper.selectPage(new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ConvertTransferPlanVO.class);
    }

    /**
     * 根据折价转让id 删除租金计划
     *
     * @param convertTransferIds
     */
    @Override
    public void deleteByConvertTransferId(List<Long> convertTransferIds) {
        remove(new LambdaQueryWrapper<ConvertTransferPlanEntity>().in(ConvertTransferPlanEntity::getConvertTransferId, convertTransferIds));
    }

}

