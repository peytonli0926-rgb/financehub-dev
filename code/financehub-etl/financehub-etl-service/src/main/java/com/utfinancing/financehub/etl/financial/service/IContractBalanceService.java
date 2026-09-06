package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.entity.AccountEntity;
import com.utfinancing.financehub.etl.financial.model.dto.ContractBalanceQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.ContractBalanceDTO;
import com.utfinancing.financehub.etl.financial.model.vo.ContractBalanceVO;
import com.utfinancing.financehub.etl.financial.entity.ContractBalanceEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-16
 * @Description : ContractBalance服务类接口
 * @Modified :
 */
public interface IContractBalanceService extends IService<ContractBalanceEntity> {

    Long saveContractBalance(ContractBalanceDTO dto);

    Long updateContractBalance(Long id, ContractBalanceDTO dto);

    ContractBalanceDTO getContractBalanceDTOById(Long id);

    IPage<ContractBalanceVO> selectPage(ContractBalanceQueryDTO queryDTO);

    void insertMap(Map<String, Object> rowMap);

    Map<String, Object> getLastBalanceMap(String businessCode,String orgId, String clientCode, String contractCode, String billContractCode);

    void insertContractBalance(String sceneCode, String systemCode, Long voucherId, String businessCode,List<AccountEntity> accountEntityList, Map<String, Object> fundTypeMap, Map<String, Object> lastBalanceMap, String orgId, String contractCode, String clientCode, String voucherDate, String easVoucherId, Integer periodCode, String billContractCode);

}
