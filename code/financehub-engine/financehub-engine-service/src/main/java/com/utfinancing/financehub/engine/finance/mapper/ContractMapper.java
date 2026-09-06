package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractMonthEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ContractQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractStatusRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractEntityUpdateVo;
import com.utfinancing.financehub.engine.finance.model.vo.ContractVO;
import com.utfinancing.financehub.engine.finance.model.vo.SelectContractByPageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 合同 Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-09-13
 */
public interface ContractMapper extends BaseMapper<ContractEntity> {

    List<ContractEntity> selectContractInfo(Page page, @Param("param") ContractStatusRecordQueryDTO param);

    List<ContractEntity> selectContractInfo(@Param("param") ContractStatusRecordQueryDTO param);

    List<ContractEntity> selectContractInfoByTempTable();

    IPage<ContractVO> selectAllContractPage(Page page, @Param("param") ContractQueryDTO param);

    List<ContractVO> selectAllContract(@Param("param") ContractQueryDTO param);

    List<ContractVO> selectContract(@Param("param") ContractQueryDTO param);

    List<ContractEntity> selectContractByPage(@Param("param") SelectContractByPageVO param);

    String getClientCode(@Param("contractCode") String contractCode,@Param("clientName") String clentName);

    void updateContractById(@Param("param")  ContractEntityUpdateVo contractEntityUpdateVo);

    List<ContractEntity> listClientInfoByCodeList(@Param("contractCodes") List<String> contractCodes);

    List<ContractEntity> listClientInfoByOrgContractCodeMap(@Param("orgContractCodeMap")Map<String, Set<String>> orgContractCodeMap);

    void updateBatchByContractCode(ContractEntity updateContractMonthEntities);
}
