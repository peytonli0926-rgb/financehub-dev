package com.utfinancing.financehub.etl.passveh.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.utfinancing.financehub.etl.financial.entity.TaReclassificationDetailEntity;
import com.utfinancing.financehub.etl.passveh.model.*;
import com.utfinancing.financehub.etl.financial.model.dto.RepaymentPlanSaveDTO;
import com.utfinancing.financehub.etl.financial.service.ICommonService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@DS("slave_cyc")
public interface IPassengerVehicleDataService extends ICommonService {

    /**
     * 分页查询合同列表
     */
    public List<String> selectContractCodeByPage(SelectContractCodeByPageDTO params);

    /**
     * 取得偿还计划-xirr
     */
    public List<SelectRepaymentFromCYCXTDTO> selectRepaymentFromCYCXTInit(SelectRepaymentFromCYCXTInputDTO dto);

    /**
     * 取得偿还计划-irr
     */
    public List<SelectRepaymentFromCYCXTDTO> selectRepaymentFromCYCXTGroupInit(SelectRepaymentFromCYCXTInputDTO dto);

    /**
     * 取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(SelectRepaymentFromCYCXTInputDTO dto);


    /**
     * 从业务系统取得最新的偿还计划
     */
    public List<RepaymentPlanSaveDTO> getRepaymentPlanFromCYCXT(QueryRepaymentPlanDTO params);

    List<TaReclassificationDetailEntity> getTaReclassificationDetailList(String queryDate, Map<String, String> orgMap);


    /**
     * 取得业务系统的未确认金额
     */
    public List<SelectNonConfirmAmountOutputDTO> queryNonConfirmAmount(Date queryDate);
}
