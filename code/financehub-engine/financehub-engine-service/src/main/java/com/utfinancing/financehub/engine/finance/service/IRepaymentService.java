package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanEntity;
import com.utfinancing.financehub.engine.finance.model.dto.DataInitDTO;
import com.utfinancing.financehub.engine.finance.model.dto.GeneratePaymentDataProcessDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountCashFlowSumDTO;

import java.util.List;

public interface IRepaymentService<T> extends IService<T> {
    /**
     * 源数据初始化
     */
    public R<Boolean> dataInit(DataInitDTO params);

    /**
     * 生成偿还计划
     */
    public R<String> generateRepaymentPlanService(GeneratePaymentDataProcessDTO params);

    /**
     * 根据合同取得偿还计划
     */
    public List<RepaymentPlanEntity> selectRepaymentByContract(String contractCode);

    /**
     * 取得合同的未实现收益
     */
    public List<OutstandingAmountCashFlowSumDTO> outstandingAmountCashFlowSum(String initDate, String contractCode);
}
