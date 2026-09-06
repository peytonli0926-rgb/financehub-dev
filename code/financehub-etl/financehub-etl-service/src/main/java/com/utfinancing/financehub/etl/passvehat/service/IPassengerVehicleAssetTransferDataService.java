package com.utfinancing.financehub.etl.passvehat.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.utfinancing.financehub.etl.financial.model.dto.RepaymentPlanSaveDTO;
import com.utfinancing.financehub.etl.financial.service.ICommonService;
import com.utfinancing.financehub.etl.passvehat.model.SelectContractCodeByPageDTO;
import com.utfinancing.financehub.etl.passvehat.model.QueryRepaymentPlanDTO;
import com.utfinancing.financehub.etl.passvehat.model.SelectReceiveRepaymentDTO;
import com.utfinancing.financehub.etl.passvehat.model.SelectRepaymentFromCYCXTDTO;
import com.utfinancing.financehub.etl.passvehat.model.SelectRepaymentFromCYCXTInputDTO;

import java.util.List;

@DS("slave_cyc_at")
public interface IPassengerVehicleAssetTransferDataService extends ICommonService {

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
}
