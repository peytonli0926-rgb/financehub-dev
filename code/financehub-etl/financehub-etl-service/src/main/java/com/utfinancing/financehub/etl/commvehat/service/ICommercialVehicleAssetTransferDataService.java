package com.utfinancing.financehub.etl.commvehat.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.utfinancing.financehub.etl.commvehat.model.*;
import com.utfinancing.financehub.etl.financial.model.dto.RepaymentPlanSaveDTO;
import com.utfinancing.financehub.etl.financial.service.ICommonService;

import java.util.List;

@DS("slave_syc_at")
public interface ICommercialVehicleAssetTransferDataService extends ICommonService {

    /**
     * 分页查询合同列表
     */
    public List<String> selectContractCodeByPage(SelectContractCodeByPageDTO params);

    /**
     * 取得偿还计划-xirr
     */
    public List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTInit(SelectRepaymentFromSYCXTInputDTO dto);

    /**
     * 取得偿还计划-irr
     */
    public List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTGroupInit(SelectRepaymentFromSYCXTInputDTO dto);

    /**
     * 取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(SelectRepaymentFromSYCXTInputDTO dto);


    /**
     * 从业务系统取得最新的偿还计划
     */
    public List<RepaymentPlanSaveDTO> getRepaymentPlanFromSYCXT(QueryRepaymentPlanDTO params);
}
