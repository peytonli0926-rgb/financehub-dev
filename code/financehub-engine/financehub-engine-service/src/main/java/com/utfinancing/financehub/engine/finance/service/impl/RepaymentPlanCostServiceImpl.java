package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.RepaymentPlanCostQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RepaymentPlanCostDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanCostVO;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanCostEntity;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanCostMapper;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanCostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-12-28
 * @Description :  RepaymentPlanCost服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class RepaymentPlanCostServiceImpl extends ServiceImpl<RepaymentPlanCostMapper, RepaymentPlanCostEntity> implements IRepaymentPlanCostService {

    private final RepaymentPlanCostMapper repaymentPlanCostMapper;

    @Override
    public Long saveRepaymentPlanCost(RepaymentPlanCostDTO dto) {
        RepaymentPlanCostEntity entity = BeanUtil.copyProperties(dto, RepaymentPlanCostEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateRepaymentPlanCost(Long id, RepaymentPlanCostDTO dto) {
        RepaymentPlanCostEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public RepaymentPlanCostDTO getRepaymentPlanCostDTOById(Long id) {
        RepaymentPlanCostEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, RepaymentPlanCostDTO.class);
    }

    @Override
    public IPage<RepaymentPlanCostVO> selectPage(RepaymentPlanCostQueryDTO queryDTO) {
        LambdaQueryWrapper<RepaymentPlanCostEntity> queryWrapper = Wrappers.<RepaymentPlanCostEntity>lambdaQuery();
        //这里注入查询条件
        IPage<RepaymentPlanCostEntity> entityIPage = repaymentPlanCostMapper.selectPage(new Page<RepaymentPlanCostEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, RepaymentPlanCostVO.class);
    }

    @Override
    public List<RepaymentPlanCostEntity> selectList(RepaymentPlanCostQueryDTO queryDTO) {
        LambdaQueryWrapper<RepaymentPlanCostEntity> queryWrapper = Wrappers.<RepaymentPlanCostEntity>lambdaQuery();
        //这里注入查询条件
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getContractCodeList()), RepaymentPlanCostEntity::getContractCode, queryDTO.getContractCodeList());
        List<RepaymentPlanCostEntity> entityIPage = repaymentPlanCostMapper.selectList(queryWrapper);
        return entityIPage;
    }

}

