package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.enums.AccrualMethodEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.SpecialContractOverdueRecordVO;
import com.utfinancing.financehub.engine.finance.mapper.SpecialContractOverdueRecordMapper;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanService;
import com.utfinancing.financehub.engine.finance.service.ISpecialContractOverdueRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @Author : robjiang
 * @Date : Create in 2025-11-21
 * @Description :  SpecialContractOverdueRecord服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class SpecialContractOverdueRecordServiceImpl extends ServiceImpl<SpecialContractOverdueRecordMapper, SpecialContractOverdueRecordEntity> implements ISpecialContractOverdueRecordService {

    private final SpecialContractOverdueRecordMapper specialContractOverdueRecordMapper;
    private final IRepaymentPlanService repaymentPlanService;

    @Override
    public Long saveSpecialContractOverdueRecord(SpecialContractOverdueRecordDTO dto) {
        SpecialContractOverdueRecordEntity entity = BeanUtil.copyProperties(dto, SpecialContractOverdueRecordEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateSpecialContractOverdueRecord(Long id, SpecialContractOverdueRecordDTO dto) {
        SpecialContractOverdueRecordEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public SpecialContractOverdueRecordDTO getSpecialContractOverdueRecordDTOById(Long id) {
        SpecialContractOverdueRecordEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, SpecialContractOverdueRecordDTO.class);
    }

    @Override
    public IPage<SpecialContractOverdueRecordVO> selectPage(SpecialContractOverdueRecordQueryDTO queryDTO) {
        LambdaQueryWrapper<SpecialContractOverdueRecordEntity> queryWrapper = Wrappers.<SpecialContractOverdueRecordEntity>lambdaQuery();
        //这里注入查询条件
        IPage<SpecialContractOverdueRecordEntity> entityIPage = specialContractOverdueRecordMapper.selectPage(new Page<SpecialContractOverdueRecordEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, SpecialContractOverdueRecordVO.class);
    }

    @Override
    public R<String> importData(List<SpecialContractOverdueRecordExcel> list) {
        List<SpecialContractOverdueRecordEntity> entities = new ArrayList<>();
        for (SpecialContractOverdueRecordExcel incomeImport : list) {
            SpecialContractOverdueRecordEntity entity = BeanUtil.copyProperties(incomeImport, SpecialContractOverdueRecordEntity.class);
            entity.setId(IdWorker.getId());
            entity.setCreateBy(SecurityUtils.getUsername());
            entity.setCreateTime(DateUtil.date());
            entity.setUpdateBy(SecurityUtils.getUsername());
            entity.setUpdateTime(DateUtil.date());
            entities.add(entity);

            Date queryDate = incomeImport.getBusinessDate();
            String contractCode = incomeImport.getContractCode();
            if (null == queryDate || StringUtils.isBlank(contractCode)) {
                continue;
            }

            // 处理 偿还计划
            List<RepaymentPlanEntity> repaymentPlanEntities = repaymentPlanService.getBaseMapper().selectList(Wrappers.<RepaymentPlanEntity>lambdaQuery()
                    .eq(RepaymentPlanEntity::getContractCode, contractCode));
            handelImportRepaymentPlan(incomeImport, repaymentPlanEntities);
        }

        // 保存上传记录
        this.saveBatch(entities);
        return R.ok("上传完成!");
    }

    /**
     * 更新偿还计划的数据
     */
    private void handelImportRepaymentPlan(SpecialContractOverdueRecordExcel incomeImport,
                                           List<RepaymentPlanEntity> repaymentPlanEntities) {
        // 该计提月份之后 偿还计划
        if (CollectionUtils.isNotEmpty(repaymentPlanEntities)) {
            for (RepaymentPlanEntity entity : repaymentPlanEntities) {
                Date uploadPeriods = DateUtils.parseDate(incomeImport.getUploadPeriods().concat("-01"), "yyyy-MM-dd");
                if (entity.getPlanDate().compareTo(uploadPeriods) >= 0) {
                    if (StringUtils.isNotEmpty(incomeImport.getLaborOverdueMark())) {
                        if (YesOrNoEnum.YES.getCode().equals(incomeImport.getLaborOverdueMark())) {
                            entity.setLaborOverdueMark(incomeImport.getLaborOverdueMark());
                            if (entity.getObservedExpirationDate() == null) {
                                entity.setObservedExpirationDate(uploadPeriods);
                            }
                        } else {
                            entity.setLaborOverdueMark(incomeImport.getLaborOverdueMark());
                            entity.setObservedExpirationDate(null);
                        }
                    }
                    if (incomeImport.getPreviousPaidPeriod() != null) {
                        entity.setPreviousPaidPeriod(incomeImport.getPreviousPaidPeriod());
                    }
                }
            }
            repaymentPlanService.saveOrUpdateBatch(repaymentPlanEntities);
        }
    }
}

