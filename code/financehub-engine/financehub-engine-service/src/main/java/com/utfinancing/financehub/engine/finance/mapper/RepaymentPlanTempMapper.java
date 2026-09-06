package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanEntity;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanTempEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeQueryDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-12-20
 */
public interface RepaymentPlanTempMapper extends BaseMapper<RepaymentPlanTempEntity> {

    int deleteAllCompleteData();

    int countContractCode();

    int copyDataFromRepaymentPlan(@Param("param")LeaseIncomeQueryDTO queryDTO);

    List<RepaymentPlanEntity> listByOffsetAndLimit(@Param("offset") int offset, @Param("limit") int limit);

    void updateProcessStatusByContractCode(List<String> updateContractCodeList);

    @Update("TRUNCATE TABLE eg_repayment_plan_temp")
    public void truncateTable();
}
