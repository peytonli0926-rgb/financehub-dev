package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountCashFlowSumDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OutstandingAmountCashFlowSumInputDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RepaymentPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectNoRecaptureByContractDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 偿还计划测算表 Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-09-12
 */
public interface RepaymentPlanMapper extends BaseMapper<RepaymentPlanEntity> {
    public void deleteOldData(@Param("systemCode") String systemCode);

    /**
     * 查询未回笼的偿还计划数据
     */
    public List<RepaymentPlanEntity> selectNoRecaptureByContract(SelectNoRecaptureByContractDTO params);

    List<RepaymentPlanVO> selectPlanAmountBuCondition(@Param("param") RepaymentPlanQueryDTO queryDTO);

    /**
     * 根据合同号查询最小的计划日期
     * @param contractCodeList
     * @return
     */
    List<RepaymentPlanVO> selectMinPlanDate(@Param("contractCodeList") List<String> contractCodeList);

    RepaymentPlanVO getPlanByMaxActualRepaymentDate(@Param("replaymentDate") Date replaymentDate,@Param("contractCode") String contractCode);

    List<RepaymentPlanEntity> selectListPrioritySnapshot(@Param("contractCodeList") List<String> contractCodeList);

    /**
     * 汇总未偿还现金流总和
     */
    public List<OutstandingAmountCashFlowSumDTO> outstandingAmountCashFlowSum(@Param("params") OutstandingAmountCashFlowSumInputDTO params);
}
