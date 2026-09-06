package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.engine.finance.entity.LongApportionEntity;
import com.utfinancing.financehub.engine.finance.entity.LongReceivableRegisterEntity;
import com.utfinancing.financehub.engine.finance.mapper.LongReceivableRegisterMapper;
import com.utfinancing.financehub.engine.finance.model.dto.LongRepaymentPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LongRepaymentPlanDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LongRepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.entity.LongRepaymentPlanEntity;
import com.utfinancing.financehub.engine.finance.mapper.LongRepaymentPlanMapper;
import com.utfinancing.financehub.engine.finance.service.ILongRepaymentPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-12
 * @Description :  LongRepaymentPlan服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class LongRepaymentPlanServiceImpl extends ServiceImpl<LongRepaymentPlanMapper, LongRepaymentPlanEntity> implements ILongRepaymentPlanService {

    private final LongRepaymentPlanMapper longRepaymentPlanMapper;
    private final LongReceivableRegisterMapper longReceivableRegisterMapper;

    @Override
    public Long saveLongRepaymentPlan(LongRepaymentPlanDTO dto) {
        LongRepaymentPlanEntity entity = BeanUtil.copyProperties(dto, LongRepaymentPlanEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateLongRepaymentPlan(Long id, LongRepaymentPlanDTO dto) {
        LongRepaymentPlanEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public LongRepaymentPlanDTO getLongRepaymentPlanDTOById(Long id) {
        LongRepaymentPlanEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, LongRepaymentPlanDTO.class);
    }

    @Override
    public IPage<LongRepaymentPlanVO> selectPage(LongRepaymentPlanQueryDTO queryDTO) {
        Page page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        // 这里注入查询条件
        IPage<LongRepaymentPlanVO> entityIPage = longRepaymentPlanMapper.selectPageByMapper(page, queryDTO);
        return entityIPage;
    }

    @Override
    public List<LongRepaymentPlanVO> selectList(LongRepaymentPlanQueryDTO queryDTO) {
        // 这里注入查询条件
        List<LongRepaymentPlanVO> list = longRepaymentPlanMapper.selectPageByMapper(queryDTO);
        return list;
    }

    /**
     * 根据长期应收款id删除
     *
     * @param longRegisterIdList
     */
    @Override
    public void removeByLongRegisterIdList(List<Long> longRegisterIdList) {
        if (CollUtil.isNotEmpty(longRegisterIdList)) {
            remove(new LambdaUpdateWrapper<LongRepaymentPlanEntity>().in(LongRepaymentPlanEntity::getLongRegisterId, longRegisterIdList));
        }
    }

    /**
     * 根据长期应收款id查询
     *
     * @param longRegisterIdList
     * @return
     */
    @Override
    public List<LongRepaymentPlanEntity> getByLongRegisterIdList(List<Long> longRegisterIdList) {
        if (CollUtil.isEmpty(longRegisterIdList)) {
            return Lists.newArrayList();
        }
        return list(new LambdaUpdateWrapper<LongRepaymentPlanEntity>().in(LongRepaymentPlanEntity::getLongRegisterId, longRegisterIdList));
    }

}

