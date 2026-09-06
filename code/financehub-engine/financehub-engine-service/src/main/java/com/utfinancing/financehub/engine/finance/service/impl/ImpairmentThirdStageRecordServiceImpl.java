package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanEntity;
import com.utfinancing.financehub.engine.finance.entity.SpecialContractOverdueRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentThirdStageRecordExcel;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentThirdStageRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentThirdStageRecordDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SpecialContractOverdueRecordExcel;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentThirdStageRecordVO;
import com.utfinancing.financehub.engine.finance.entity.ImpairmentThirdStageRecordEntity;
import com.utfinancing.financehub.engine.finance.mapper.ImpairmentThirdStageRecordMapper;
import com.utfinancing.financehub.engine.finance.service.IImpairmentThirdStageRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @Author : robjiang
 * @Date : Create in 2025-12-26
 * @Description :  ImpairmentThirdStageRecord服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ImpairmentThirdStageRecordServiceImpl extends ServiceImpl<ImpairmentThirdStageRecordMapper,
        ImpairmentThirdStageRecordEntity> implements IImpairmentThirdStageRecordService {

    private final ImpairmentThirdStageRecordMapper impairmentThirdStageRecordMapper;
    private final IRepaymentPlanService repaymentPlanService;

    @Override
    public Long saveImpairmentThirdStageRecord(ImpairmentThirdStageRecordDTO dto) {
        ImpairmentThirdStageRecordEntity entity = BeanUtil.copyProperties(dto, ImpairmentThirdStageRecordEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateImpairmentThirdStageRecord(Long id, ImpairmentThirdStageRecordDTO dto) {
        ImpairmentThirdStageRecordEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ImpairmentThirdStageRecordDTO getImpairmentThirdStageRecordDTOById(Long id) {
        ImpairmentThirdStageRecordEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ImpairmentThirdStageRecordDTO.class);
    }

    @Override
    public IPage<ImpairmentThirdStageRecordVO> selectPage(ImpairmentThirdStageRecordQueryDTO queryDTO) {
        LambdaQueryWrapper<ImpairmentThirdStageRecordEntity> queryWrapper = Wrappers.<ImpairmentThirdStageRecordEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ImpairmentThirdStageRecordEntity> entityIPage = impairmentThirdStageRecordMapper.selectPage(new Page<ImpairmentThirdStageRecordEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ImpairmentThirdStageRecordVO.class);
    }

    @Override
    public R<String> importData(List<ImpairmentThirdStageRecordExcel> list) {
        List<ImpairmentThirdStageRecordEntity> entities = new ArrayList<>();
        for (ImpairmentThirdStageRecordExcel incomeImport : list) {
            ImpairmentThirdStageRecordEntity entity = BeanUtil.copyProperties(incomeImport, ImpairmentThirdStageRecordEntity.class);
            entity.setId(IdWorker.getId());
            entity.setCreateBy(SecurityUtils.getUsername());
            entity.setCreateTime(DateUtil.date());
            entity.setUpdateBy(SecurityUtils.getUsername());
            entity.setUpdateTime(DateUtil.date());
            entities.add(entity);

            String contractCode = incomeImport.getContractCode();
            if (StringUtils.isBlank(contractCode)) {
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
    private void handelImportRepaymentPlan(ImpairmentThirdStageRecordExcel incomeImport,
                                           List<RepaymentPlanEntity> repaymentPlanEntities) {
        // 该计提月份之后 偿还计划
        if (CollectionUtils.isNotEmpty(repaymentPlanEntities)) {
            for (RepaymentPlanEntity entity : repaymentPlanEntities) {
                Date uploadPeriods = DateUtils.parseDate(incomeImport.getUploadPeriods().concat("-01"), "yyyy-MM-dd");
                if (entity.getPlanDate().compareTo(uploadPeriods) >= 0) {
                    entity.setImpairmentThirdStage(YesOrNoEnum.YES.getCode());
                }
            }
            repaymentPlanService.saveOrUpdateBatch(repaymentPlanEntities);
        }
    }
}

