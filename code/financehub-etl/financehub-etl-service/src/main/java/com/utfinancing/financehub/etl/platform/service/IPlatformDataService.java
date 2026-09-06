package com.utfinancing.financehub.etl.platform.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.utfinancing.financehub.etl.financial.entity.TaReclassificationDetailEntity;
import com.utfinancing.financehub.etl.financial.model.dto.RepaymentPlanSaveDTO;
import com.utfinancing.financehub.etl.financial.service.ICommonService;
import com.utfinancing.financehub.etl.platform.model.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@DS("slave_typt")
public interface IPlatformDataService extends ICommonService {

    /**
     * 取得偿还计划-XIRR-期初数据
     */
    public List<SelectRepaymentFromTYPTDTO> selectRepaymentFromTYPTInit(SelectRepaymentFromTYPTInputDTO dto);

    /**
     * 取得偿还计划-irr-期初数据
     */
    public List<SelectRepaymentFromTYPTDTO> selectRepaymentFromTYPTGroupInit(SelectRepaymentFromTYPTInputDTO dto);

    /**
     * 取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(SelectRepaymentFromTYPTInputDTO dto);

    /**
     * 从统一平台取得最新的偿还计划
     */
    public List<RepaymentPlanSaveDTO> getRepaymentPlanFromTYPT(QueryRepaymentPlanDTO params);


    List<TaReclassificationDetailEntity> getTaReclassificationDetailList(String queryDate, Map<String, String> orgMap);

    /**
     * 查询ta余额
     */
    public List<SelectTaAmountDTO> queryTaAmount();

    /**
     * 取得业务系统的未确认金额
     */
    public List<SelectNonConfirmAmountOutputDTO> queryNonConfirmAmount(Date queryDate);
}
