package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ContractRepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanHisVO;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanHisEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-31
 * @Description : RepaymentPlanHis服务类接口
 * @Modified :
 */
public interface IRepaymentPlanHisService extends IService<RepaymentPlanHisEntity> {

    Long saveRepaymentPlanHis(RepaymentPlanHisDTO dto);

    Long updateRepaymentPlanHis(Long id, RepaymentPlanHisDTO dto);

    RepaymentPlanHisDTO getRepaymentPlanHisDTOById(Long id);

    IPage<RepaymentPlanHisVO> selectPage(RepaymentPlanHisQueryDTO queryDTO);

    List<RepaymentPlanHisVO> selectLastGroupList(ContractHisQueryDTO queryDTO);

    void saveRepaymentPlanHisBatch(List<RepaymentPlanSaveDTO> repaymentPlanList, Date changeDate);

    void saveRepaymentPlanHisBatchByEntity(List<RepaymentPlanHisEntity> repaymentPlanList);

    IPage<ContractRepaymentPlanVO> selectPageByContractCode(ContractQueryInfoDTO queryDTO);

    List<String> getLatestVersionDate(String contractCode);

    List<ContractRepaymentPlanVO> selectByContractCode(ContractQueryInfoDTO queryDTO);

    /**
     * 按照签约主体+合同编码获取赎回起算日本金金额
     * @return
     */
    List<RepaymentPlanHisVO> selectPlanAmountByCondition(RepaymentPlanHisQueryDTO queryDTO);

}
