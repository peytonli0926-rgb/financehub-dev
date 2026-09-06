package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.ContractQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.ContractDTO;
import com.utfinancing.financehub.etl.financial.model.vo.ContractVO;
import com.utfinancing.financehub.etl.financial.entity.ContractEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-02
 * @Description : Contract服务类接口
 * @Modified :
 */
@DS("master")
public interface IContractService extends IService<ContractEntity> {

    Long saveContract(ContractDTO dto);

    Long updateContract(Long id, ContractDTO dto);

    ContractDTO getContractDTOById(Long id);

    IPage<ContractVO> selectPage(ContractQueryDTO queryDTO);

    public ContractDTO getContractDTOByCode(String contractCode,String orgId);

    public List<ContractDTO> listContractDTOByCodeList(List<String> contractCodeList);
}
