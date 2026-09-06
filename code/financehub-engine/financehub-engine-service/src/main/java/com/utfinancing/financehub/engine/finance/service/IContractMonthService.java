package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractMonthEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ContractDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractMonthDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractMonthQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractMonthVO;

import java.util.List;

import java.util.Map;


/**
 * @Author : robjiang
 * @Date : Create in 2025-04-21
 * @Description : ContractMonth服务类接口
 * @Modified :
 */
public interface IContractMonthService extends IService<ContractMonthEntity> {

    Long saveContractMonth(ContractMonthDTO dto);

    Long updateContractMonth(Long id, ContractMonthDTO dto);

    ContractMonthDTO getContractMonthDTOById(Long id);

    IPage<ContractMonthVO> selectPage(ContractMonthQueryDTO queryDTO);
    public ContractMonthDTO getContractDTOByCode(String contractCode, String orgId);
    public ContractMonthDTO getTranStatusDTOByCode(String contractCode, String orgId);

    Map<String, Object> getContractMap(String contractCode, String orgId);

    public List<ContractMonthEntity> getContractDTOByCode(String contractCode);

    public void dataSync();

    void updateByContractCodeM(ContractMonthEntity updateContractVo);

    void updateBatchByContractCode(List<ContractMonthEntity> updateContractMonthEntities);

    List<ContractMonthEntity> getTranStatusList(List<String> contractCodeList);
}
