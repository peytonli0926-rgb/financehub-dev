package com.utfinancing.financehub.etl.micro.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.utfinancing.financehub.etl.financial.entity.TaReclassificationDetailEntity;
import com.utfinancing.financehub.etl.financial.model.dto.RepaymentPlanSaveDTO;
import com.utfinancing.financehub.etl.financial.service.ICommonService;
import com.utfinancing.financehub.etl.micro.model.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@DS("slave_xw")
public interface IMicroDataService extends ICommonService {
    /**
     * 取得偿还计划-xirr
     */
    public List<SelectRepaymentFromXWXTDTO> selectRepaymentFromXWXTInit(SelectRepaymentFromXWXTInputDTO dto);


    /**
     * 取得偿还计划-irr
     */
    public List<SelectRepaymentFromXWXTDTO> selectRepaymentFromXWXTGroupInit(SelectRepaymentFromXWXTInputDTO dto);

    /**
     * 取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(SelectRepaymentFromXWXTInputDTO dto);

    /**
     * 从小微系统取得最新的偿还计划
     */
    public List<RepaymentPlanSaveDTO> getRepaymentPlanFromXWXT(QueryRepaymentPlanDTO params);

    List<TaReclassificationDetailEntity> getTaReclassificationDetailList(String queryDate, Map<String, String> orgMap);

    /**
     * 取得业务系统的未确认金额
     */
    public List<SelectNonConfirmAmountOutputDTO> queryNonConfirmAmount(Date queryDate);
}
