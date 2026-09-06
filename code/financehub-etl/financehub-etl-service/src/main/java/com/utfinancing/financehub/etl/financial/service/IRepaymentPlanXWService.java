package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanXWEntity;
import com.utfinancing.financehub.etl.financial.model.dto.DataInitDTO;
import com.utfinancing.financehub.etl.micro.model.SelectRepaymentFromXWXTDTO;

import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 2023-09-12
 * @Description : RepaymentPlan服务类接口
 * @Modified :
 */
public interface IRepaymentPlanXWService extends IService<RepaymentPlanXWEntity> {

    /**
     * 期初数据同步
     */
    public String dataInit(DataInitDTO params);

    /**
     * 回笼数据同步
     */
    public String receivedAmountSync(DataInitDTO params);

    /**
     * 保存业务系统过来的偿还计划
     */
    public void saveRepaymentPlanForXW(List<SelectRepaymentFromXWXTDTO> repaymentList);
}
