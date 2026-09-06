package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanProvisionEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 2024-01-30
 * @Description : RepaymentPlanProvision服务类接口
 * @Modified :
 */
public interface IRepaymentPlanProvisionService extends IService<RepaymentPlanProvisionEntity> {

    public void deleteAllDate();

    /**
     * 偿还计划数据备份
     */
    public void repaymentPlanDataBackup();

    /**
     * 偿还计划数据恢复
     */
    public void repaymentPlanDataRecovery();

    /**
     * 删除临时生成的偿还计划数据
     */
    public void repaymentPlanDataByIds(List<RepaymentPlanProvisionEntity> repaymentPlanProvisionEntityList);

    /**
     * 根据合同编码查询计提产生的偿还计划
     */
    public List<RepaymentPlanProvisionEntity> selectByContractCode(String contractCode);

    /**
     * 将计提数据更新到偿还计划中
     */
    public void updateProvisionData();

    /**
     * 将计提数据更新到偿还计划中-根据签约主体
     */
    public void updateProvisionData(List<String> orgIds);
}
