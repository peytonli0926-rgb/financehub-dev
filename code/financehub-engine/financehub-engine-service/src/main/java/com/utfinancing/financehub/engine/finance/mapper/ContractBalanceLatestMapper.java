package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.ContractBalanceLatestEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-11-30
 */
public interface ContractBalanceLatestMapper extends BaseMapper<ContractBalanceLatestEntity> {

    boolean insertContractBalanceLatest(@Param("columns")List<String> columns, @Param("values")List<Object> values);

    List<ContractBalanceLatestEntity> listReceivableRentBalanceByContractCodes(@Param("contractCodeList")List<String> contractCodeList);

    boolean updateContractBalance(@Param("paramMap") Map<String, Object> paramMap, @Param("id")Long id);

    /**
     * 根据合同编号和签约主体查询应收租金余额
     * @param contractCodeList
     * @param orgIdList
     * @return
     */
    List<ContractBalanceLatestEntity> listReceivableRentBalanceByContractCodeAndOrgId(@Param("contractCodeList") List<String> contractCodeList, @Param("orgIdList") List<String> orgIdList);
}
