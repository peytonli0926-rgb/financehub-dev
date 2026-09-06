package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-13
 * @Description : Contract服务类接口
 * @Modified :
 */
public interface IContractService extends IService<ContractEntity> {

    Long saveContract(ContractDTO dto);
    Long saveContractAndHis(ContractDTO dto);

    Long updateContract(Long id, ContractDTO dto);

    ContractDTO getContractDTOById(Long id);

    IPage<ContractVO> selectPage(ContractQueryDTO queryDTO);

    /**
     * 根据接口数据保存合同数据
     * @param interfaceDataMap 接口数据
     * @return
     */
    String saveOrUpdateContract(Map<String, Object> interfaceDataMap);

    /**
     * 根据合同编码查询合同
     * @param contractCode
     * @return
     */
    ContractDTO getContractDTOByCode(String contractCode,String orgId);

    ContractDTO getTranStatusDTOByCode(String contractCode,String orgId);
    List<ContractDTO> listContractDTOByCodeList(List<String> contractCodeList);


    /**
     * 根据合同编号查询合同详情（给到会计引擎使用）
     * @param contractCode
     * @return
     */
    Map<String, Object> getContractMap(String contractCode,String orgId);

    String importData(List<ImportContractExcel> list);

    void importData1(List<ImportContractExcel1> list);

    void importData2(List<ImportContractExcel> list2);

    /**
     * 获取合同详情（包含数仓的字段）
     */
    ContractDetailDTO getContractDetail(Long id);

    IPage<ContractRepaymentPlanVO> selectPageByContractCode(ContractQueryInfoDTO queryDTO);

    List<String> getLatestVersionDate(Long id);

    IPage<ContractTransactionVO> transactionByPage(ContractQueryInfoDTO queryDTO);

    IPage<ContractTInfoPerformanceAttributionVO> salesBonusPage(ContractQueryInfoDTO queryDTO);

    List<ContractRepaymentPlanVO> selectByContractCode(ContractQueryInfoDTO queryDTO);

    IPage<ContractVO> allContractPage(ContractQueryDTO queryDTO);

    List<ContractVO> allContractList(ContractQueryDTO queryDTO);

    /**
     * 根据合同编码模糊查询合同信息
     */
    public List<ContractEntity> getContractDTOByCode(String contractCode);

    List<ContractExcelVO> export(ContractQueryDTO queryDTO);


    /**
     * 更新合同表 财务合同状态
     *
     * @param contractCode
     * @param orgId
     * @param financialContractStatus
     */
    void updateContractFinancialStatus(String contractCode, String orgId, String financialContractStatus);

    /**
     * 生成新的合同
     *
     * @param contractCode
     * @param orgId
     * @param newContractCode
     * @param newOrgId
     */
    void copyContract(String contractCode, String orgId, String newContractCode, String newOrgId);

    /**
     * 保存特殊合同状态记录
     *
     * @param list
     */
    void saveRecordList(List<ContractVO> list);

    IPage<ContractAccountBalanceVO> accountBalanceByPage(ContractQueryInfoDTO queryDTO);

    /**
     * 核销保存特殊合同状态记录
     *
     * @param list
     */
    void verificationSaveRecordList(List<ContractVO> list);

    /**
     * 取得待分摊服务费的合同信息
     */
    public List<ContractEntity> selectContractForReceviceServiceAmount(List<String> notGenerateOrgIds);

    /**
     * 合同基本信息同步
     */
    public void basicDataSync(String leaseDateStart);

    String getClientCode(String contractCode,String clientName);

    void updateContractABasicDataSync();

    void syncLossData(String startData);

	List<ContractEntity> listClientInfoByCodeList(List<String> contractCodes);

    void updateServiceShareFlagByContractCode(List<ContractEntity> contractEntities);

    void updateContractById(ContractEntityUpdateVo contractEntityUpdateVo);

    void updateByContractCode(ContractEntity contractEntity);
    void updateByContractCodeM(ContractEntity contractEntity);

    void updateBatchByContractCode(List<ContractEntity> updateContractEntities);
}
