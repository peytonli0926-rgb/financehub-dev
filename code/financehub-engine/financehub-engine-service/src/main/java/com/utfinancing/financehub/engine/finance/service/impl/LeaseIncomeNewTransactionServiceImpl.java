package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.ClientEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceLatestEntity;
import com.utfinancing.financehub.engine.finance.entity.LeaseIncomeDetailsEntity;
import com.utfinancing.financehub.engine.finance.entity.LeaseIncomeEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ContractDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class LeaseIncomeNewTransactionServiceImpl implements ILeaseIncomeNewTransactionService {

    private final ILeaseIncomeDetailsService detailsService;

    private final IRuleService iRuleService;

    private final BatchRuleService batchRuleService;

    /**
     * 收益计提生成凭证
     */
    public boolean genVoucher(String isSubmit, List<LeaseIncomeDetailsEntity> detailsEntities,
                           Map<String, ContractDTO> contractMap) {
        boolean isSuccessGenerateVoucher = true;

        // 合同编码列表
        List<String> contractCodeList = detailsEntities.stream().map(e -> e.getContractCode()).distinct().
                collect(Collectors.toList());

        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        for (LeaseIncomeDetailsEntity entity : detailsEntities) {
            ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
            commonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            commonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            commonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
            commonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
            commonDTO.setOrderId(entity.getId().toString());
            commonDTO.setContractCode(entity.getContractCode());
            ContractDTO contractDTO = contractMap.get(entity.getContractCode().concat("|").concat(entity.getOrgId()));
            if (null != contractDTO) {
                commonDTO.setContractName(contractDTO.getContractName());
                commonDTO.setCurrencyType(StringUtils.isNotEmpty(contractDTO.getCurrencyType()) ?
                        contractDTO.getCurrencyType() : FundCurrencyTypeEnum.CNY.getCode());
            }
            commonDTO.setClientCode(entity.getClientCode());
            commonDTO.setClientName(entity.getClientName());
            commonDTO.setSceneCode(SceneEnum.SYJT.getCode());
            commonDTO.setSceneName(SceneEnum.SYJT.getDesc());
            commonDTO.setOrgId(entity.getOrgId());
            commonDTO.setBusinessDate(entity.getBusinessDate());
            commonDTO.setBatchId(entity.getLeaseIncomeId());
            commonDTO.setBatchType(BatchTypeEnum.SYJT.getCode());
            Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);
            // 是否计提
            if (YesOrNoEnum.YES.getCode().equals(entity.getAccrued())) {
                commonMap.put("isAccrual", YesOrNoEnum.YES.getDesc());
            } else {
                commonMap.put("isAccrual", YesOrNoEnum.NO.getDesc());
            }
            // 收益计提金额
            commonMap.put("incomeAccural", entity.getRentalIncomeOnBalance());
            // 分润费分摊额：冲减合同应收息中已经包含的分润收入
            commonMap.put("incomeAdjust", entity.getProfitSharingAllocationAmount() == null
                    ? BigDecimal.ZERO : entity.getProfitSharingAllocationAmount());
            // 表外计提金额
            commonMap.put("incomeOther", entity.getRentalIncomeOffBalance());
            commonMap.put(RuleConstant.IS_SUBMIT, isSubmit);
            commonMap.put("intableTransferOuttableAmount", entity.getIntableTransferOuttableAmount());
            commonMap.put("outtableTransferIntableAmount", entity.getOuttableTransferIntableAmount());
            commonMap.put("accuralMonth", DateUtils.parseDateToStr(DateUtils.YYYY_MM, entity.getBusinessDate()));
            voucherMapList.add(commonMap);
        }
        log.info("生成凭证参数：{}", JSON.toJSONString(voucherMapList));
        List<VoucherInfoVO> voucherInfoList = iRuleService.batchExecuteRule(voucherMapList);

        if (voucherInfoList == null || voucherInfoList.isEmpty()) {
            log.error("收益计提 生成凭证失败");
        } else {
            for (VoucherInfoVO entry : voucherInfoList) {
                List<VoucherDTO> value = entry.getVoucherDTOList();
                if (CollectionUtils.isNotEmpty(value)) {
                    if (StringUtils.isNotEmpty(entry.getErrorInfo())) {
                        isSuccessGenerateVoucher = false;
                    }

                    String vouchIds = value.stream().map(VoucherDTO::getId).map(String::valueOf).collect(
                            Collectors.joining(","));
                    detailsService.lambdaUpdate()
                            .set(LeaseIncomeDetailsEntity::getVoucherId, vouchIds)
                            .set(LeaseIncomeDetailsEntity::getAccountDate, DateUtil.beginOfDay(new Date()))
                            .set(LeaseIncomeDetailsEntity::getIsGenerateVoucher, YesOrNoEnum.YES.getCode())
                            .set(LeaseIncomeDetailsEntity::getExceptionType, entry.getErrorInfo())
                            .eq(LeaseIncomeDetailsEntity::getId, Long.parseLong(entry.getOrderId()))
                            .update();
                    log.info("收益计提DetailId:" + entry.getOrderId() + "凭证已经生成!");
                }
            }
        }
        return isSuccessGenerateVoucher;
    }


    /**
     * 收益计提生成凭证
     */
    public boolean genVoucher1(String isSubmit, List<LeaseIncomeDetailsEntity> detailsEntities,
                               Map<String, Object> params) {

        boolean isSuccessGenerateVoucher = true;
        Map<String, ContractDTO> contractMap = (Map<String, ContractDTO>) params.get("contractMap");

        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        for (LeaseIncomeDetailsEntity entity : detailsEntities) {
            ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
            commonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            commonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            commonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
            commonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
            commonDTO.setOrderId(entity.getId().toString());
            commonDTO.setContractCode(entity.getContractCode());
            ContractDTO contractDTO = contractMap.get(entity.getContractCode().concat("|").concat(entity.getOrgId()));
            if (null != contractDTO) {
                commonDTO.setContractName(contractDTO.getContractName());
                commonDTO.setCurrencyType(StringUtils.isNotEmpty(contractDTO.getCurrencyType()) ?
                        contractDTO.getCurrencyType() : FundCurrencyTypeEnum.CNY.getCode());
            }
            commonDTO.setClientCode(entity.getClientCode());
            commonDTO.setClientName(entity.getClientName());
            commonDTO.setSceneCode(SceneEnum.SYJT.getCode());
            commonDTO.setSceneName(SceneEnum.SYJT.getDesc());
            commonDTO.setOrgId(entity.getOrgId());
            commonDTO.setBusinessDate(entity.getBusinessDate());
            commonDTO.setBatchId(entity.getLeaseIncomeId());
            commonDTO.setBatchType(BatchTypeEnum.SYJT.getCode());
            Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);
            // 是否计提
            if (YesOrNoEnum.YES.getCode().equals(entity.getAccrued())) {
                commonMap.put("isAccrual", YesOrNoEnum.YES.getDesc());
            } else {
                commonMap.put("isAccrual", YesOrNoEnum.NO.getDesc());
            }
            // 收益计提金额
            commonMap.put("incomeAccural", entity.getRentalIncomeOnBalance() == null ? new BigDecimal(0) : entity.getRentalIncomeOnBalance());
            // 分润费分摊额：冲减合同应收息中已经包含的分润收入
            commonMap.put("incomeAdjust", entity.getProfitSharingAllocationAmount() == null
                    ? BigDecimal.ZERO : entity.getProfitSharingAllocationAmount());
            // 表外计提金额
            commonMap.put("incomeOther", entity.getRentalIncomeOffBalance() == null ? new BigDecimal(0) : entity.getRentalIncomeOffBalance());
            commonMap.put(RuleConstant.IS_SUBMIT, isSubmit);
            commonMap.put("intableTransferOuttableAmount", entity.getIntableTransferOuttableAmount() == null ?
                    new BigDecimal(0) : entity.getIntableTransferOuttableAmount());
            commonMap.put("outtableTransferIntableAmount", entity.getOuttableTransferIntableAmount() == null ?
                    new BigDecimal(0) : entity.getOuttableTransferIntableAmount());
            commonMap.put("accuralMonth", DateUtils.parseDateToStr(DateUtils.YYYY_MM, entity.getBusinessDate()));
            voucherMapList.add(commonMap);
        }
        log.info("生成凭证参数：{}", JSON.toJSONString(voucherMapList));
        List<VoucherInfoVO> voucherInfoList = batchRuleService.executeRule(voucherMapList, params);

        if (voucherInfoList == null || voucherInfoList.isEmpty()) {
            log.error("收益计提 生成凭证失败");
        } else {
            for (VoucherInfoVO entry : voucherInfoList) {
                List<VoucherDTO> value = entry.getVoucherDTOList();
                if (CollectionUtils.isNotEmpty(value)) {
                    if (StringUtils.isNotEmpty(entry.getErrorInfo())) {
                        isSuccessGenerateVoucher = false;
                    }

                    String vouchIds = value.stream().map(VoucherDTO::getId).map(String::valueOf).collect(
                            Collectors.joining(","));
                    detailsService.lambdaUpdate()
                            .set(LeaseIncomeDetailsEntity::getVoucherId, vouchIds)
                            .set(LeaseIncomeDetailsEntity::getAccountDate, DateUtil.beginOfDay(new Date()))
                            .set(LeaseIncomeDetailsEntity::getIsGenerateVoucher, YesOrNoEnum.YES.getCode())
                            .set(LeaseIncomeDetailsEntity::getExceptionType, entry.getErrorInfo())
                            .eq(LeaseIncomeDetailsEntity::getId, Long.parseLong(entry.getOrderId()))
                            .update();
                    log.info("收益计提DetailId:" + entry.getOrderId() + "凭证已经生成!");
                }
            }
        }
        return isSuccessGenerateVoucher;
    }
}
