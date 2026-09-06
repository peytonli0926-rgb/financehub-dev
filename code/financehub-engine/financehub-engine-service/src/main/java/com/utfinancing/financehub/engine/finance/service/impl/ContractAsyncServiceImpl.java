package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.engine.dw.entity.DwBzHetjbxxDEntity;
import com.utfinancing.financehub.engine.dw.entity.DwsBzHetjyjgxxDEntity;
import com.utfinancing.financehub.engine.dw.service.IDwBzHetjbxxDService;
import com.utfinancing.financehub.engine.dw.service.IDwsBzHetjyjgxxDService;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.DwsBzHetjyjgfyxDEntity;
import com.utfinancing.financehub.engine.finance.mapper.ContractMapper;
import com.utfinancing.financehub.engine.finance.model.vo.SelectContractByPageVO;
import com.utfinancing.financehub.engine.finance.service.IContractAsyncService;
import com.utfinancing.financehub.engine.finance.service.IContractNewTransactionService;
import com.utfinancing.financehub.engine.finance.service.IDwsBzHetjyjgfyxDService;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContractAsyncServiceImpl implements IContractAsyncService {

    @Resource
    private IDwBzHetjbxxDService dwBzHetjbxxDService;

    @Resource
    private IDwsBzHetjyjgxxDService dwsBzHetjyjgxxDService;

    @Resource
    private IDwsBzHetjyjgfyxDService dwsBzHetjyjgfyxDService;
    @Resource
    private ContractMapper contractMapper;

    @Resource
    private IContractNewTransactionService contractNewTransactionService;

    @Resource
    @Lazy
    private IRepaymentPlanService repaymentPlanService;

    @Async
    public void contractInfoAsync(int startIndex, int endIndex, String leaseDateStart) {
        // 每次取200条数据进行处理
        int eachProcessNumber = 200;
        int totalNumber = endIndex - startIndex;  // 待处理的总数
        int processBatchNumber = totalNumber / eachProcessNumber;  // 总共处理次数
        int modNumber = totalNumber % eachProcessNumber; // 余数
        if (modNumber != 0) {
            processBatchNumber = processBatchNumber + 1;
        }

        for (int i = 0; i < processBatchNumber; i++) {
            log.info("合同基本信息同步 LoopCount:" + i);
            SelectContractByPageVO param = new SelectContractByPageVO();
            param.setOffset(startIndex + i * eachProcessNumber);
            if (i == processBatchNumber - 1) {
                param.setPageSize(modNumber);
            } else {
                param.setPageSize(eachProcessNumber);
            }
            param.setLeaseDateStart(leaseDateStart);
            // 取得待处理的合同数据
            List<ContractEntity> contractEntityList = contractMapper.selectContractByPage(param);
            // 取得源数据
            List<String> contractCodeList = contractEntityList.stream().map(ContractEntity::getContractCode).
                    collect(Collectors.toList());
            if (CollectionUtils.isEmpty(contractCodeList)){
                continue;
            }
            // 取得合同基本信息
            Map<String, DwsBzHetjyjgxxDEntity> hetjyjgxxDEntityMap = dwsBzHetjyjgxxDService.getDwsBzHetjyjgxxDMapByContractCode(contractCodeList);
            Map<String, DwBzHetjbxxDEntity> hetjbxxDEntityMap = dwBzHetjbxxDService.getDwBzHetjbxxDMapByContractCode(contractCodeList);
            Map<String, BigDecimal> hetjyjgfyxDEntityMap = dwsBzHetjyjgfyxDService.selectHetjyjgfyxByContractCode(contractCodeList);
//            List<RepaymentPlanVO> repaymentPlanVOList = repaymentPlanService.selectByContractCodeList(contractCodeList);
//            Map<String, String> incomeProvisionMap = repaymentPlanVOList.stream().collect(Collectors.toMap(e->e.getContractCode(), e->e.getIncomeProvisionMethod(), (a,b)->b));

            for (ContractEntity entity : contractEntityList) {
                DwsBzHetjyjgxxDEntity dwsBzHetjyjgxxDEntity = hetjyjgxxDEntityMap.get(entity.getContractCode());
                DwBzHetjbxxDEntity dwBzHetjbxxDEntity = hetjbxxDEntityMap.get(entity.getContractCode());
                // 合同约定到期日
                if (dwBzHetjbxxDEntity != null) {
                    // 合同状态
                    entity.setContractStatus(dwBzHetjbxxDEntity.getVcHetzt());

                    // 起租日
//                    if (StringUtils.isNotEmpty(dwBzHetjbxxDEntity.getDtKuaijqzr())) {
//                        Date leaseDateStart = DateUtils.parseDate(dwBzHetjbxxDEntity.getDtKuaijqzr());
//                        entity.setLeaseDateStart(leaseDateStart);
//                    }
//
//                    // 到期日
//                    if (StringUtils.isNotEmpty(dwBzHetjbxxDEntity.getDtHetsjjsrq())) {
//                        Date dtHetsjjsrq = DateUtils.parseDate(dwBzHetjbxxDEntity.getDtHetsjjsrq());
//                        entity.setLeaseDateEnd(dtHetsjjsrq);
//                    } else if (StringUtils.isNotEmpty(dwBzHetjbxxDEntity.getDtYuedjsr())) {
//                        entity.setLeaseDateEnd(DateUtils.parseDate(dwBzHetjbxxDEntity.getDtYuedjsr()));
//                    }
                }

                if (dwsBzHetjyjgxxDEntity != null) {
                    // 期初还是期末
                    entity.setPayMethod(dwsBzHetjyjgxxDEntity.getVcQicqmzf());
                    // 还款节奏/还租方式
                    entity.setReturnType(dwsBzHetjyjgxxDEntity.getNuHuanzfs());
                    // 收益计提方式
//                    if (StringUtils.isEmpty(entity.getIncomeProvisionMethod())) {
//                        entity.setIncomeProvisionMethod(incomeProvisionMap.get(entity.getContractCode()));
//                    }

                    // 交易结构信息
                    // 应付设备款
                    if (StringUtils.isNotEmpty(dwsBzHetjyjgxxDEntity.getDecShebjg())) {
                        entity.setPayableDeviceAmount(new BigDecimal(dwsBzHetjyjgxxDEntity.getDecShebjg()));
                    } else {
                        entity.setPayableDeviceAmount(BigDecimal.ZERO);
                    }

                    // 租赁合同总金额
                    if (StringUtils.isNotEmpty(dwsBzHetjyjgxxDEntity.getDecZulhtzje())) {
                        entity.setRentContractTotal(new BigDecimal(dwsBzHetjyjgxxDEntity.getDecZulhtzje()));
                    } else {
                        entity.setRentContractTotal(BigDecimal.ZERO);
                    }
                }

                // 应收首付款=首付款
                entity.setReceivableFirstAmount(this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "101", "100002"));
                // 出租人保险费=保险费支出
                entity.setLessorInsuranceAmount(this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "202"));
                // 应收承租人履约保证金 = 承租人保证金/租赁保证金
                BigDecimal receivableMarginAmount = this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "108");
                if (receivableMarginAmount == null || BigDecimal.ZERO.compareTo(receivableMarginAmount) == 0) {
                    receivableMarginAmount = this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "100005");
                }
                entity.setReceivableMarginAmount(receivableMarginAmount);
                // 渠道费用=渠道费支出
                entity.setChannelFees(this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "213"));
                // 应收手续费收入=手续费
                entity.setReceivableProcedureAmount(this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "103", "100001", "03"));
                // 出租人其他成本= （GPS费用（起租）/GPS费用）+其他费用
                BigDecimal gpsFee = this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "100028");
                if (gpsFee == null || BigDecimal.ZERO.compareTo(gpsFee) == 0) {
                    gpsFee = this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "100027");
                }
                BigDecimal otherFee = this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "204");
                entity.setLessorOtherCosts(gpsFee.add(otherFee));
                // 名义留购价 = 留购价
                entity.setRetainedPrice(this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "110", "100015"));
                // 应收保险费 = 保险费收入
                entity.setReceivableInsuranceAmount(this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "102"));
                // 其他收入 (含增值税) / 应收其他= 其他费用
                entity.setReceivableOther(otherFee);
                // 供应商履约保证金 / 供应商保证金 =
                entity.setVendorMarginAmount(this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "107"));
                // 应收服务费
                BigDecimal serviceFee = this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "120");
                if (serviceFee == null || BigDecimal.ZERO.compareTo(serviceFee) == 0) {
                    serviceFee = this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "100012");
                }
                entity.setReceivableServiceAmount(serviceFee);
                // 应收厂商返利
                entity.setReceivableFirmRebate(this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "105"));
                // 期末残值
                entity.setLastCost(entity.getRetainedPrice());
                // 起租前已收租金
                entity.setLeaseBeforeReceviedAmount(BigDecimal.ZERO);
//                entity.setPayableInsuranceAmount();
//                entity.setDeductionDeviceAmount();
                entity.setProcedure801(this.getAmount(hetjyjgfyxDEntityMap, entity.getContractCode(), "801"));
            }
            contractNewTransactionService.batchUpdateById(contractEntityList);
        }
        log.info("-------------合同基本信息同步任务 线程End----------------");
    }

    /**
     * 取得金额
     */
    private BigDecimal getAmount(Map<String, BigDecimal> hetjyjgfyxDEntityMap, String contractCode,
                                 String... amountTypeList) {
        for (String amountType : amountTypeList) {
            BigDecimal fee = hetjyjgfyxDEntityMap.
                    get(dwsBzHetjyjgfyxDService.getBusinessKey(contractCode, amountType));
            if (fee != null && BigDecimal.ZERO.compareTo(fee) != 0) {
                return fee;
            }
        }
        return BigDecimal.ZERO;
    }
}
