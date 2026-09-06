package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanExceldataEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.model.dto.ContractDTO;
import com.utfinancing.financehub.etl.model.dto.SelectReceiveRepaymentDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author : robjiang
 * @Date : Create in 2024-01-23
 * @Description : RepaymentPlanExceldata服务类接口
 * @Modified :
 */
public interface IRepaymentPlanExceldataService extends IService<RepaymentPlanExceldataEntity> {

    /**
     * 保存最新的回笼数据
     */
    public void saveNewReceivedRepayment(List<SelectReceiveRepaymentDTO> receivedRepaymentDTOList);


    /**
     * 回笼数据合并
     */
    public List<RepaymentPlanExceldataEntity> receivedRepaymentMerge(List<RepaymentPlanExceldataEntity> receivedRepaymentDTOList);


    /**
     * 回笼的总金额计算
     */
    public BigDecimal receivedAmountCompute(
            List<RepaymentPlanExceldataEntity> exceldataEntityList, ContractDTO contractDTO);

    /**
     * 取得合同回笼金额的总数
     */
    public Map<String, BigDecimal> getReceivedMoneyByContract(Set<String> contractCodeList,
                                                              Map<String, ContractDTO> contractDTOMap);
}
