package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.commveh.model.SelectReceiveRepaymentDTO;
import com.utfinancing.financehub.etl.commveh.model.SelectRepaymentFromSYCXTDTO;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanHYEntity;
import com.utfinancing.financehub.etl.financial.model.dto.DataInitDTO;

import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 2023-09-12
 * @Description : RepaymentPlan服务类接口
 * @Modified :
 */
public interface IRepaymentPlanSYCService extends IService<RepaymentPlanHYEntity> {

    /**
     * 期初数据同步
     */
    public String dataInit(DataInitDTO params);

    /**
     * 回笼数据同步
     */
    public String receivedAmountSync(DataInitDTO params);

    /**
     * 保存收到的偿还计划-业务系统的偿还计划
     */
    public void saveRepaymentPlanForSYC(List<SelectRepaymentFromSYCXTDTO> repaymentList);

    /**
     * 保存收到的回笼数据
     */
    public void saveReceiveRepayment(List<SelectReceiveRepaymentDTO> receiveRepaymentDTOList);

}
