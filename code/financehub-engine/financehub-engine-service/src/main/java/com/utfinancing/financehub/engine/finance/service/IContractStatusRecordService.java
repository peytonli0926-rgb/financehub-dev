package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ContractStatusRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractStatusRecordDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractStatusRecordSaveDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FileRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractStatusRecordVO;
import com.utfinancing.financehub.engine.finance.entity.ContractStatusRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-09
 * @Description : ContractStatusRecord服务类接口
 * @Modified :
 */
public interface IContractStatusRecordService extends IService<ContractStatusRecordEntity> {

    Long saveContractStatusRecord(ContractStatusRecordSaveDTO dto);

    Long updateContractStatusRecord(Long id, ContractStatusRecordSaveDTO dto);

    ContractStatusRecordDTO getContractStatusRecordDTOById(Long id);

    IPage<ContractStatusRecordVO> selectPage(ContractStatusRecordQueryDTO queryDTO);

    List<ContractStatusRecordVO> selectList(ContractStatusRecordQueryDTO queryDTO);

    IPage<ContractStatusRecordVO> pageDetail(ContractStatusRecordQueryDTO queryDTO);

    List<ContractStatusRecordVO> listDetail(ContractStatusRecordQueryDTO queryDTO);

    String importData(List<ContractStatusRecordSaveDTO> list, String operName);
    String importData(List<ContractStatusRecordSaveDTO> list);

    Void submit(List<Long> ids);

    Void withdraw(List<Long> ids);

    Void pass(List<Long> ids);

    Void fail(List<Long> ids);

    List<ContractStatusRecordVO> exportSummaryData(ContractStatusRecordQueryDTO queryDTO);

    Map<String, String> export(ContractStatusRecordQueryDTO queryDTO);

    Boolean updateProcessStatus(CommonApproveDTO approveDTO);

    Boolean deleteByIds(List<Long> ids);

    IPage<FileRecordEntity> selectSpecialContractFileList(FileRecordQueryDTO queryDTO);

    ContractStatusRecordDTO getLastByContractCodeAndOrgId(String contractCode, String orgId);

    List<ContractStatusRecordEntity> listFinContractStatusByContractCodes(List<String> contractCodes, List<String> finContractStatus);

}
