package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.MarginStatusEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ContractRepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanHisVO;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanHisEntity;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanHisMapper;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanHisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-31
 * @Description :  RepaymentPlanHis服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class RepaymentPlanHisServiceImpl extends ServiceImpl<RepaymentPlanHisMapper, RepaymentPlanHisEntity>
        implements IRepaymentPlanHisService {

    private final RepaymentPlanHisMapper repaymentPlanHisMapper;

    @Override
    public Long saveRepaymentPlanHis(RepaymentPlanHisDTO dto) {
        RepaymentPlanHisEntity entity = BeanUtil.copyProperties(dto, RepaymentPlanHisEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateRepaymentPlanHis(Long id, RepaymentPlanHisDTO dto) {
        RepaymentPlanHisEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public RepaymentPlanHisDTO getRepaymentPlanHisDTOById(Long id) {
        RepaymentPlanHisEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, RepaymentPlanHisDTO.class);
    }

    @Override
    public IPage<RepaymentPlanHisVO> selectPage(RepaymentPlanHisQueryDTO queryDTO) {
        LambdaQueryWrapper<RepaymentPlanHisEntity> queryWrapper = Wrappers.<RepaymentPlanHisEntity>lambdaQuery();
        //这里注入查询条件
        IPage<RepaymentPlanHisEntity> entityIPage = repaymentPlanHisMapper.selectPage(new Page<RepaymentPlanHisEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, RepaymentPlanHisVO.class);
    }

    @Override
    public List<RepaymentPlanHisVO> selectLastGroupList(ContractHisQueryDTO queryDTO) {
        List<RepaymentPlanHisEntity> contractBalanceEntities = repaymentPlanHisMapper.selectGroupByContractCode(queryDTO);
        return BeanUtil.copyToList(contractBalanceEntities, RepaymentPlanHisVO.class);
    }

    @Override
    public void saveRepaymentPlanHisBatch(List<RepaymentPlanSaveDTO> dtos, Date changeDate) {
        if (CollectionUtils.isNotEmpty(dtos) || changeDate == null) {
            return;
        }

        // 如果当月的历史记录已经存在，则不再保存(如果每月出现多次偿还计划变更，则只存储第一次历史记录)
        Date firstDayOfMonth = DateUtil.beginOfMonth(changeDate);
        LambdaQueryWrapper<RepaymentPlanHisEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepaymentPlanHisEntity::getContractCode, dtos.get(0).getContractCode());
        wrapper.ge(RepaymentPlanHisEntity::getCreateTime, firstDayOfMonth);
        wrapper.eq(RepaymentPlanHisEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        List<RepaymentPlanHisEntity> hisEntityList = repaymentPlanHisMapper.selectList(wrapper);
        if (hisEntityList != null && hisEntityList.isEmpty()) {
            return ;
        }

        List<RepaymentPlanHisEntity> hisEntities = BeanUtil.copyToList(dtos, RepaymentPlanHisEntity.class);
        //查最新记录的版本号
        Integer version = repaymentPlanHisMapper.selectNewVersionByContractCode(dtos.get(0).getContractCode());
        if (null == version) {
            version = 1;
        }
        // 版本号+1
        Integer finalVersion = version;
        hisEntities.forEach(e -> {
            e.setVersion(finalVersion + 1);
            e.setProcessStatus(MarginStatusEnum.PASS.getCode());
            e.setManualChangeMark(YesOrNoEnum.NO.getCode());
        });
        this.saveBatch(hisEntities);
    }

    @Override
    public void saveRepaymentPlanHisBatchByEntity(List<RepaymentPlanHisEntity> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        //查最新记录的版本号
        Integer version = repaymentPlanHisMapper.selectNewVersionByContractCode(entities.get(0).getContractCode());
        if (null == version) {
            version = 1;
        }
        // 版本号+1
        Integer finalVersion = version;
        entities.forEach(e->e.setVersion(finalVersion + 1));
        // 保存
        this.saveOrUpdateBatch(entities);
    }

    @Override
    public IPage<ContractRepaymentPlanVO> selectPageByContractCode(ContractQueryInfoDTO queryDTO) {
        Page page = new Page(queryDTO.getPageNum(),queryDTO.getPageSize());
        //根据
        if (StringUtils.isEmpty(queryDTO.getContractCode())) {
            throw new ServiceException("合同编码不可以为空");
        }
        return repaymentPlanHisMapper.selectPageByContractCode(page,queryDTO);
    }

    @Override
    public List<String> getLatestVersionDate(String contractCode) {
        ContractQueryInfoDTO dto = new ContractQueryInfoDTO();
        dto.setContractCode(contractCode);
        List<ContractRepaymentPlanVO> planHisEntityList = this.selectByContractCode(dto);
        if (CollectionUtils.isEmpty(planHisEntityList)) {
            return null;
        }
        List<String> existDateList = Lists.newArrayList();
        planHisEntityList.stream().forEach(v -> {
            String dateString = DateUtil.format(v.getCreateTime(),"yyyy-MM-dd");
            if (!existDateList.contains(dateString)) {
                existDateList.add(dateString);
            }
        });
        return existDateList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    @Override
    public List<ContractRepaymentPlanVO> selectByContractCode(ContractQueryInfoDTO queryDTO) {
        return repaymentPlanHisMapper.selectByContractCode(queryDTO);
    }

    @Override
    public List<RepaymentPlanHisVO> selectPlanAmountByCondition(RepaymentPlanHisQueryDTO queryDTO) {
        return repaymentPlanHisMapper.selectPlanAmountByCondition(queryDTO);
    }

}

