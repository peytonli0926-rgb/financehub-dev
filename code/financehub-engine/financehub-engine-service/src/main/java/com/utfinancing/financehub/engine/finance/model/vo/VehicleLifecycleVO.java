package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.engine.finance.entity.ContractBalanceEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanEntity;
import com.utfinancing.financehub.engine.finance.entity.VehicleBusinessModelEntity;
import com.utfinancing.financehub.engine.finance.entity.VoucherEntity;
import lombok.Data;

import java.util.List;

@Data
public class VehicleLifecycleVO {
    private ContractEntity contract;
    private VehicleBusinessModelEntity businessModel;
    private List<RepaymentPlanEntity> repaymentPlans;
    private List<ContractBalanceEntity> balances;
    private List<VoucherEntity> vouchers;
}
