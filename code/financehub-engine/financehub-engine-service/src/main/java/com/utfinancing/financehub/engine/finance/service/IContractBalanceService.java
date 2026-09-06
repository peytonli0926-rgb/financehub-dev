package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ContractAccountBalanceVO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractBalanceVO;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import feign.Contract;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-23
 * @Description : ContractBalance服务类接口
 * @Modified :
 */
public interface IContractBalanceService extends IService<ContractBalanceEntity> {

    Long saveContractBalance(ContractBalanceDTO dto);

    Long updateContractBalance(Long id, ContractBalanceDTO dto);

    ContractBalanceDTO getContractBalanceDTOById(Long id);

    IPage<ContractBalanceVO> selectPage(ContractBalanceQueryDTO queryDTO);

    Map<String, Object> getMap(Long id);

    Map<String, Object> getLastBalanceMap(String businessCode, String clientCode, String contractCode,String orgId,String billContractCode);

    /**
     * 根据凭证保存合同余额表
     * @param voucherDTO
     */
    void saveContractBalanceFromVoucher(VoucherDTO voucherDTO);

    /**
     * 根据凭证保存合同余额表
     * @param voucherDTO
     */
    void saveMonualContractBalanceFromVoucher(VoucherDTO voucherDTO);

    String importDataMargin(List<ImportContractBalanceExcel> list);

    void importData(List<ImportContractBalanceExcel> list3);

    void importData1(List<ImportContractExcel1> list);

    ContractBalanceDTO getLastBySceneCode(String businessCode, String contractCode, String sceneCode);


    /**
     * 获取原始最新的合同余额表数据
     * @param businessCode 业务编码
     * @param clientCode 客户编码
     * @param contractCode 合同编码
     * @return
     */
    Map<String, Object> getOriginalLastBalanceMap(String businessCode, String clientCode, String contractCode);

    void insertMap(Map<String, Object> rowMap);

    IPage<ContractBalanceVO> selectLatestBalance(ContractBalanceQueryDTO contractBalanceQueryDTO);


    /**
     * 校验最新余额数据分页接口
     * @param queryDTO
     * @return
     */
    IPage<ContractBalanceVO> selectCheckPage(ContractBalanceCheckQueryDTO queryDTO);

    List<ContractBalanceVO> selectLatestBalanceByCondition(ContractBalanceQueryDTO contractBalanceQueryDTO);

    IPage<ContractAccountBalanceVO> sumAccountBalancePage(ContractBalanceQueryDTO params);


    List<ContractBalanceEntity> getLastContract(ContractBalanceLastQueryDTO param);

    List<ContractBalanceVO> selectLatestBalanceByOrgIdContractCodeList(TransferContractBalanceQueryDTO dto);

    List<ContractBalanceEntity> selectContractBalanceAfterJZRGroupByOrgIdContractScene(
            Map<String, List<String>> orgContractMap,
            List<String> sceneCode,
            LocalDate localDate,
            int periodCode
    );
}
