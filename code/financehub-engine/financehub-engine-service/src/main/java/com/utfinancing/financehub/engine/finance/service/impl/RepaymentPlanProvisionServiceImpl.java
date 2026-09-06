package com.utfinancing.financehub.engine.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanEntity;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanProvisionEntity;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanMapper;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanProvisionMapper;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanProvisionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : robjiang
 * @Date : Create in 2024-01-30
 * @Description :  RepaymentPlanProvision服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class RepaymentPlanProvisionServiceImpl extends ServiceImpl<RepaymentPlanProvisionMapper,
        RepaymentPlanProvisionEntity> implements IRepaymentPlanProvisionService {

    private final RepaymentPlanProvisionMapper repaymentPlanProvisionMapper;


    private final RepaymentPlanMapper repaymentPlanMapper;

    @Override
    public void deleteAllDate() {
//        repaymentPlanProvisionMapper.truncateTable();
        repaymentPlanProvisionMapper.delete(new LambdaQueryWrapper<>());
    }

    @Override
    public void repaymentPlanDataBackup() {
//        repaymentPlanProvisionMapper.delete(new LambdaQueryWrapper<>());
        repaymentPlanProvisionMapper.truncateTable();
        repaymentPlanProvisionMapper.repaymentPlanDataBackup();
    }

    @Override
    public void repaymentPlanDataRecovery() {
//        repaymentPlanMapper.delete(new LambdaQueryWrapper<>());
        repaymentPlanProvisionMapper.truncateTable();
        repaymentPlanProvisionMapper.repaymentPlanDataRecovery();
    }

    /**
     * 删除临时生成的偿还计划数据
     */
    @Override
    public void repaymentPlanDataByIds(List<RepaymentPlanProvisionEntity> repaymentPlanProvisionEntityList) {
        if (repaymentPlanProvisionEntityList == null || repaymentPlanProvisionEntityList.isEmpty()) {
            return;
        }
        List<String> contractCodeList = repaymentPlanProvisionEntityList.stream().map(RepaymentPlanProvisionEntity::getContractCode).distinct().
                collect(Collectors.toList());
        LambdaQueryWrapper<RepaymentPlanProvisionEntity> wrapper = new LambdaQueryWrapper();
        wrapper.in(RepaymentPlanProvisionEntity::getContractCode, contractCodeList);
        repaymentPlanProvisionMapper.delete(wrapper);
    }

    /**
     * 根据合同编码查询计提产生的偿还计划
     */
    @Override
    public List<RepaymentPlanProvisionEntity> selectByContractCode(String contractCode) {
        LambdaQueryWrapper<RepaymentPlanProvisionEntity> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(RepaymentPlanProvisionEntity::getContractCode, contractCode);
        queryWrapper.eq(RepaymentPlanProvisionEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        queryWrapper.orderByAsc(RepaymentPlanProvisionEntity::getPlanDate);
        //这里注入查询条件
        List<RepaymentPlanProvisionEntity> businessEntities = getBaseMapper().selectList(queryWrapper);
        return businessEntities;
    }

    /**
     * 将计提数据更新到偿还计划中
     */
    public void updateProvisionData() {
        repaymentPlanProvisionMapper.updateProvisionData();
    }

    /**
     * 将计提数据更新到偿还计划中-根据签约主体
     */
    public void updateProvisionData(List<String> orgIds) {
        // 更新原偿还计划
        repaymentPlanProvisionMapper.updateProvisionDataByOrgIds(orgIds);
        // 删除临时生成的偿还计划
        LambdaQueryWrapper<RepaymentPlanProvisionEntity> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(RepaymentPlanProvisionEntity::getOrgId, orgIds);
        repaymentPlanProvisionMapper.delete(queryWrapper);
    }
}

