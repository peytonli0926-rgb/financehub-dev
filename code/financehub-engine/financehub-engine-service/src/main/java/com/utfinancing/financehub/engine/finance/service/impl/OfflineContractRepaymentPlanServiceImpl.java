package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.OfflineContractRepaymentPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OfflineContractRepaymentPlanDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractRepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.entity.OfflineContractRepaymentPlanEntity;
import com.utfinancing.financehub.engine.finance.mapper.OfflineContractRepaymentPlanMapper;
import com.utfinancing.financehub.engine.finance.service.IOfflineContractRepaymentPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description :  OfflineContractRepaymentPlan服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class OfflineContractRepaymentPlanServiceImpl extends ServiceImpl<OfflineContractRepaymentPlanMapper, OfflineContractRepaymentPlanEntity> implements IOfflineContractRepaymentPlanService {

    private final OfflineContractRepaymentPlanMapper offlineContractRepaymentPlanMapper;

    @Override
    public Long saveOfflineContractRepaymentPlan(OfflineContractRepaymentPlanDTO dto) {
        OfflineContractRepaymentPlanEntity entity = BeanUtil.copyProperties(dto, OfflineContractRepaymentPlanEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateOfflineContractRepaymentPlan(Long id, OfflineContractRepaymentPlanDTO dto) {
        OfflineContractRepaymentPlanEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public OfflineContractRepaymentPlanDTO getOfflineContractRepaymentPlanDTOById(Long id) {
        OfflineContractRepaymentPlanEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, OfflineContractRepaymentPlanDTO.class);
    }

    @Override
    public void deleteByContractCodeList(List<String> ids) {
        LambdaUpdateWrapper<OfflineContractRepaymentPlanEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .in(OfflineContractRepaymentPlanEntity::getContractCode, ids)
                .set(OfflineContractRepaymentPlanEntity::getUpdateBy, SecurityUtils.getUsername())
                .set(OfflineContractRepaymentPlanEntity::getUpdateTime, LocalDateTime.now())
                .set(OfflineContractRepaymentPlanEntity::getDelFlag, YesOrNoEnum.YES.getCode());
        this.update(updateChainWrapper);
    }

    @Override
    public IPage<OfflineContractRepaymentPlanVO> selectPage(OfflineContractRepaymentPlanQueryDTO queryDTO) {
        LambdaQueryWrapper<OfflineContractRepaymentPlanEntity> queryWrapper = Wrappers.<OfflineContractRepaymentPlanEntity>lambdaQuery();
        //这里注入查询条件
        IPage<OfflineContractRepaymentPlanEntity> entityIPage = offlineContractRepaymentPlanMapper.selectPage(new Page<OfflineContractRepaymentPlanEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, OfflineContractRepaymentPlanVO.class);
    }

    @Override
    public List<OfflineContractRepaymentPlanVO> selectList(OfflineContractRepaymentPlanQueryDTO queryDTO) {
        LambdaQueryWrapper<OfflineContractRepaymentPlanEntity> queryWrapper = Wrappers.<OfflineContractRepaymentPlanEntity>lambdaQuery();
        //这里注入查询条件
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getContractCode()), OfflineContractRepaymentPlanEntity::getContractCode, queryDTO.getContractCode());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getContractCodeList()), OfflineContractRepaymentPlanEntity::getContractCode, queryDTO.getContractCodeList());
        queryWrapper.orderByAsc(OfflineContractRepaymentPlanEntity::getPeriod);
        List<OfflineContractRepaymentPlanEntity> entityIPage = offlineContractRepaymentPlanMapper.selectList(queryWrapper);
        List<OfflineContractRepaymentPlanVO> offlineContractRepaymentPlanVOS = ListBeanUtil.copyList(entityIPage, OfflineContractRepaymentPlanVO.class);
        offlineContractRepaymentPlanVOS.forEach(e -> e.setPeriods(e.getPeriod()));
        return offlineContractRepaymentPlanVOS;
    }

}

