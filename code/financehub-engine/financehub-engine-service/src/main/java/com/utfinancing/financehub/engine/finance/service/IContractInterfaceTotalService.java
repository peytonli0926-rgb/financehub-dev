package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ContractInterfaceTotalQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractInterfaceTotalDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractInterfaceTotalSaveDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractInterfaceTotalVO;
import com.utfinancing.financehub.engine.finance.entity.ContractInterfaceTotalEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-09
 * @Description : ContractInterfaceTotal服务类接口
 * @Modified :
 */
public interface IContractInterfaceTotalService extends IService<ContractInterfaceTotalEntity> {

    Long saveContractInterfaceTotal(ContractInterfaceTotalSaveDTO dto);

    Long updateContractInterfaceTotal(Long id, ContractInterfaceTotalDTO dto);

    Long updateEntityById(ContractInterfaceTotalDTO dto);

    ContractInterfaceTotalDTO getContractInterfaceTotalDTOById(Long id);

    IPage<ContractInterfaceTotalVO> selectPage(ContractInterfaceTotalQueryDTO queryDTO);

    ContractInterfaceTotalDTO getByContractCode(String contractCode);

    void saveFromInterfaceData(Map<String, Object> dataMap);

}
