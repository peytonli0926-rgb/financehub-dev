package com.utfinancing.financehub.engine.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.finance.entity.OrgCompanyEntity;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanEntity;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanPLEntity;
import com.utfinancing.financehub.engine.finance.mapper.OrgCompanyMapper;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanMapper;
import com.utfinancing.financehub.engine.finance.model.dto.DataInitDTO;
import com.utfinancing.financehub.engine.finance.model.dto.GeneratePaymentDataProcessDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountCashFlowSumDTO;
import com.utfinancing.financehub.engine.finance.service.IRepaymentAsyncSerivice;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

public abstract class RepaymentAbstractService<M extends BaseMapper<T>, T>
        extends ServiceImpl<M, T> {

    @Resource
    private Map<String, IRepaymentAsyncSerivice> repaymentAsyncService;

    @Resource
    private RepaymentPlanMapper repaymentPlanMapper;

    /**
     * 创建指定日期的现金流初始化数据
     */
    public R<Boolean> dataInit(DataInitDTO params) {
        // 1.删除临时创建的初始化现金流
        this.outstandingAmountInitCashFlowDel();
        // 2.根据合同分组，取得指定日期之后的现金流汇总数据
        List<OutstandingAmountCashFlowSumDTO> cashFlowSumDTOList = this.outstandingAmountCashFlowSum(params.getInitDate(), null);
        // 3.创建现金流数据
        this.createInitCashFlowData(cashFlowSumDTOList, params);
        return R.ok(true);
    }

    /**
     * 删除创建的初始化现金流
     */
    protected abstract void outstandingAmountInitCashFlowDel();

    /**
     * 根据合同分组，取得指定日期之后的现金流汇总数据
     */
    protected abstract List<OutstandingAmountCashFlowSumDTO> outstandingAmountCashFlowSum(String initDate, String contractCode);

    /**
     * 创建初始化的现金流数据
     */
    protected abstract void createInitCashFlowData(
            List<OutstandingAmountCashFlowSumDTO> cashFlowSumDTOList, DataInitDTO params);

    /**
     * 生成偿还计划
     */
    public R<String> generateRepaymentPlanService(GeneratePaymentDataProcessDTO params) {
        // 0.删除已经生成的偿还计算数据
        this.delRepaymentData(params);
        // 1.取得待生成数据数量
        Integer dataCount = this.getDataCount(params.getSystemCode());
        if (dataCount == null || dataCount.intValue() == 0) {
            return R.fail("没有待处理数据!");
        }

        // 2.线程分析
        int threadCount = this.getThreadCount(dataCount);
        // 每个线程待处理数据分解
        int eachThreadDataCount = dataCount / threadCount;

        // 3.启动线程
        for (int i = 0; i < threadCount; i++) {
            GeneratePaymentDataProcessDTO threadParmas = new GeneratePaymentDataProcessDTO();
            BeanUtils.copyProperties(params, threadParmas);
            if (i == threadCount - 1) {
                threadParmas.setDataStartIndex(i * eachThreadDataCount);
                threadParmas.setDataCount(dataCount - i * eachThreadDataCount);
            } else {
                threadParmas.setDataStartIndex(i * eachThreadDataCount + params.getDataStartIndex());
                threadParmas.setDataCount(eachThreadDataCount);
            }

            getRepaymentAsyncSerivice().generatePaymentDataProcess(threadParmas);
        }

        // 4.将数据(合同最后期数小于2023-01-01)直接保存至偿还计划表中
        delExpirationOfContractData(params);
        saveExpirationOfContractData();
        return R.ok("任务已启动!");
    }

    public int getThreadCount (Integer dataCount) {
        int threadCount = 1;
        if (dataCount <= 100) {
            threadCount = 1;
        } else if (dataCount > 100 && dataCount <= 1000) {
            threadCount = 5;
        } else if (dataCount > 1000 && dataCount <= 10000) {
            threadCount = 10;
        } else if (dataCount > 10000) {
            threadCount = 20;
        }
        return threadCount;
    }

    /**
     * 取得待处理数据的数据量
     */
    protected abstract Integer getDataCount(String sytemCode);

    /**
     * 取得处理的服务类
     */
    protected abstract IRepaymentAsyncSerivice getRepaymentAsyncSerivice();

    /**
     * 删除已经生成的偿还计划数据
     */
    protected void delRepaymentData(GeneratePaymentDataProcessDTO params) {
        if ("hy".equals(params.getTransferSystemCode())) {
            repaymentPlanMapper.deleteOldData(SystemEnum.SYCXT.getCode());
            repaymentPlanMapper.deleteOldData(SystemEnum.CYCXT.getCode());
        } else {
            repaymentPlanMapper.deleteOldData(params.getSystemCode());
        }
    }

    /**
     * 保存合同在2023-01-01之前到期的偿还计划数据
     */
    protected abstract void saveExpirationOfContractData();

    protected void delExpirationOfContractData(GeneratePaymentDataProcessDTO params) {
        LambdaQueryWrapper<RepaymentPlanEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepaymentPlanEntity::getExceptionType, "合同最后期数小于2023-01-01");
        if (SystemEnum.XWXT.getCode().equals(params.getSystemCode())) {
            wrapper.eq(RepaymentPlanEntity::getSystemCode, SystemEnum.XWXT.getCode());
        } else if (SystemEnum.TYPT.getCode().equals(params.getSystemCode())) {
            wrapper.eq(RepaymentPlanEntity::getSystemCode, SystemEnum.TYPT.getCode());
        } else {
            wrapper.in(RepaymentPlanEntity::getSystemCode, SystemEnum.SYCXT.getCode(), SystemEnum.CYCXT.getCode());
        }
        repaymentPlanMapper.delete(wrapper);
    }

    /**
     * 根据合同取得偿还计划
     */
    protected abstract List<RepaymentPlanEntity> selectRepaymentByContract(String contractCode);
}
