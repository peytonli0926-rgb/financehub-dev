package com.utfinancing.financehub.etl.financial.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanEntity;
import com.utfinancing.financehub.etl.financial.mapper.RepaymentPlanMapper;
import com.utfinancing.financehub.etl.financial.service.IRepaymentPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Author : hzhao
 * @Date : Create in 2023-09-12
 * @Description :  RepaymentPlan服务实现类
 * @Modified :
 */
@Service
@Transactional
@Slf4j
public class RepaymentPlanServiceImpl extends ServiceImpl<RepaymentPlanMapper, RepaymentPlanEntity>
        implements IRepaymentPlanService {

    @Resource
    private RepaymentPlanMapper repaymentPlanMapper;


    @Override
    public List<RepaymentPlanEntity> selectByContractCode(String contractCode) {
        LambdaQueryWrapper<RepaymentPlanEntity> queryWrapper = Wrappers.<RepaymentPlanEntity>lambdaQuery();
        queryWrapper.eq(RepaymentPlanEntity::getContractCode, contractCode);
        queryWrapper.orderByAsc(RepaymentPlanEntity::getPlanDate);
        //这里注入查询条件
        List<RepaymentPlanEntity> businessEntities = getBaseMapper().selectList(queryWrapper);
        return businessEntities;
    }

    /**
     * 根据合同编码删除偿还计划
     */
    public void delRepaymentPlanByContractCode(List<String> contractCodeList) {
        LambdaUpdateWrapper<RepaymentPlanEntity> wrapper = new LambdaUpdateWrapper();
        wrapper.in(RepaymentPlanEntity::getContractCode, contractCodeList);
        repaymentPlanMapper.delete(wrapper);
    }
}
