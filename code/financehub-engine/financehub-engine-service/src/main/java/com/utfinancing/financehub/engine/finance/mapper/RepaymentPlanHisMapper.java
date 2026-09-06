package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanHisEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ContractHisQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractQueryInfoDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RepaymentPlanHisQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractRepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanHisVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 偿还计划测算历史表 Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-10-31
 */
public interface RepaymentPlanHisMapper extends BaseMapper<RepaymentPlanHisEntity> {

    List<RepaymentPlanHisEntity> selectGroupByContractCode(@Param("param") ContractHisQueryDTO queryDTO);

    List<RepaymentPlanHisEntity> selectThisMonthMaxVersionGroup(@Param("param") String param);

    Integer selectNewVersionByContractCode(@Param("contractCode") String contractCode);

    IPage<ContractRepaymentPlanVO> selectPageByContractCode(Page page, @Param("param") ContractQueryInfoDTO queryDTO);

    List<ContractRepaymentPlanVO> selectByContractCode(@Param("param") ContractQueryInfoDTO queryDTO);

    List<RepaymentPlanHisVO> selectPlanAmountByCondition(@Param("param") RepaymentPlanHisQueryDTO queryDTO);

}
