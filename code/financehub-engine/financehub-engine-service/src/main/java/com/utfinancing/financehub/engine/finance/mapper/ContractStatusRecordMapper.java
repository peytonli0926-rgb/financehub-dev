package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractStatusRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ContractStatusRecordDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractStatusRecordQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 合同状态记录表 Mapper 接口
 * </p>
 *
 * @author hzhao
 * @since 2023-10-09
 */
public interface ContractStatusRecordMapper extends BaseMapper<ContractStatusRecordEntity> {

    List<ContractStatusRecordEntity> selectContractInfo(Page page, @Param("param") ContractStatusRecordQueryDTO param);

    List<ContractStatusRecordEntity> selectContractInfo(@Param("param") ContractStatusRecordQueryDTO param);

    List<ContractStatusRecordEntity> summaryList(@Param("param") ContractStatusRecordQueryDTO param);

    ContractStatusRecordDTO getLastByContractCodeAndOrgId(@Param("contractCode") String contractCode, @Param("orgId") String orgId);
}
