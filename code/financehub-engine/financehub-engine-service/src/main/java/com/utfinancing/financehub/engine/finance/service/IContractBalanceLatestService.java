package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ContractBalanceLatestQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractBalanceLatestDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractBalanceLatestVO;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceLatestEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-30
 * @Description : ContractBalanceLatest服务类接口
 * @Modified :
 */
public interface IContractBalanceLatestService extends IService<ContractBalanceLatestEntity> {

    Long saveContractBalanceLatest(ContractBalanceLatestDTO dto);

    Long updateContractBalanceLatest(Long id, ContractBalanceLatestDTO dto);

    ContractBalanceLatestDTO getContractBalanceLatestDTOById(Long id);

    IPage<ContractBalanceLatestVO> selectPage(ContractBalanceLatestQueryDTO queryDTO);

    public List<ContractBalanceLatestEntity> selectContractBalanceLatestByCode(List<String> contractCode);

    public Map<String, ContractBalanceLatestEntity> selectContractBalanceLatestMap(List<String> contractCode);

    public String getBusKey(ContractBalanceLatestEntity entity);


    /**
     * 获取最新的合同余额表数据
     * @param businessCode 业务编码
     * @param clientCode 客户编码
     * @param contractCode 合同编码
     * @return
     */
    Map<String, Object> getLastBalanceMap(String businessCode, String clientCode, String contractCode,String orgId,String billContractCode);

    /**
     * 保存余额表数据
     * @param insertMap
     * @return
     */
    Boolean insertMap(VoucherDTO voucherDTO, Map<String, Object> insertMap);
    Boolean insertMap(Map<String, Object> insertMap);

    /**
     * 根据维度，查询全量科目余额
     *
     */
    Map<String, Object> queryFullLastBalanceMap(String businessCode, String orgId, String contractCode, String clientCode, String billContractCode, List<String> assistFlags);

    //更新余额
    void updateLastBalanceMap(Map<String, Object> rowMap);

    public void fillBalanceZero(Map<String, Object> emptyMap);

    public Map<String, Object> replaceBalanceMapKey(Map<String, Object> balanceMap, String keySuffix);
}
