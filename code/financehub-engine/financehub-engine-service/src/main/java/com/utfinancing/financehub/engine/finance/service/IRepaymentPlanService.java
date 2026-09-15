package com.utfinancing.financehub.engine.finance.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractRepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.model.dto.TGLVoucherInitDTO;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-12
 * @Description : RepaymentPlan服务类接口
 * @Modified :
 */
public interface IRepaymentPlanService extends IService<RepaymentPlanEntity> {

    Long saveRepaymentPlan(RepaymentPlanSaveDTO dto);

    @Async
    void saveFromInterfaceData(Map<String, Object> dataMap);

    /** Save the repayment plan carried by a Huaxia lease-start event. */
    void saveLeaseStartPlanFromInterfaceData(Map<String, Object> dataMap);

    /**
     * Calculates HTQZ rule variables from raw contract, asset and repayment-plan data.
     */
    void prepareLeaseStartCalculatedFields(Map<String, Object> dataMap);

    void saveRepaymentPlanAndHisBatch(List<RepaymentPlanSaveDTO> dtos);

    Long updateRepaymentPlan(Long id, RepaymentPlanDTO dto);

    void deleteGEChangeDataByCode(Date changeDate, String contractCode);

    Long updateRepaymentPlan(Long id, RepaymentPlanSaveDTO dto);

    RepaymentPlanDTO getRepaymentPlanDTOById(Long id);

    IPage<RepaymentPlanVO> selectPage(RepaymentPlanQueryDTO queryDTO);

    List<RepaymentPlanVO> selectByContractCode(String contractCode);

    RepaymentPlanSaveDTO selectByContractCodeAndPeriods(String contractCode, Integer periods);

    List<RepaymentPlanVO> selectList(RepaymentPlanQueryDTO queryDTO);

    List<RepaymentPlanEntity> selectByContractCodeAndSystemCode(String contractCode, String systemCode);

    List<RepaymentPlanSaveDTO> handleLease(ContractDTO contractDTO, List<OfflineContractRepaymentPlanVO> repaymentPlanVOS);

    List<RepaymentPlanSaveDTO> handleIrr(List<RepaymentPlanSaveDTO> repaymentPlanVOS);

    void saveRawData(String messageId, JSONObject jsonObject);

    void importDataXWXT(List<ImportRepaymentPlanXWXTExcel> list, String opt);

    void importDataXWXTAsync(List<ImportRepaymentPlanXWXTExcel> list, String opt);

    void importDataHY(List<ImportRepaymentPlanHYExcel> list, String systemCode);

    void monthlyChangeTask(String dateString);

    List<RepaymentPlanVO> selectPlanAmountBuCondition(RepaymentPlanQueryDTO queryDTO);

    List<RepaymentPlanVO> selectByContractCodeList(List<String> contractCodeList);

    List<RepaymentPlanVO> selectMinPlanDate(List<String> contractCodeList);


    /**
     * 根据合同生成XIRR偿还计划
     */
    public R<String> generateXirrPaymentDataProcess(GenerateXirrPaymentDataProcessDTO params);

    /**
     * XIRR递归进行分摊
     */
    public void generateRepayments(Date dateFrom, Date dateUntil, List<RepaymentPlanSaveDTO> repayments,
                                   List<Date> allPlanDate, Iterator<RepaymentPlanSaveDTO> iterator,
                                   Double xirrRate, RepaymentPlanSaveDTO lease);

    /**
     * XIRR的偿还计划计算
     */
    public List<RepaymentPlanSaveDTO> generateXirrRepayments(List<RepaymentPlanSaveDTO> repayments, Double xirrRate);

    /**
     * 获取实际付款日期最大的偿还计划
     * @param replaymentDate
     * @return
     */
    RepaymentPlanVO getPlanByMaxActualRepaymentDate(Date replaymentDate,String contractCode);


    /**
     * 合同起租定时任务处理
     */
    public void contractOnHireTask();


    /**
     * 更新特殊合同的回笼状态
     */
    public void updateRepaymentPlanForSpecialContract(LeaseIncomeQueryDTO queryDTO);

    List<RepaymentPlanEntity> selectListPrioritySnapshot(List<String> contractCodeList);


    /**
     * 计提方式变更
     */
    public String incomeProvisionMethodChange(LeaseIncomeImport incomeImport);

    /**
     * irr转实收/实收回笼时/期初生成偿还计划时-偿还计划变更
     */
    public List<RepaymentPlanEntity> irrTransferToReceipt(
            LeaseIncomeImport incomeImport, List<RepaymentPlanEntity> repaymentPlanEntities);
}
