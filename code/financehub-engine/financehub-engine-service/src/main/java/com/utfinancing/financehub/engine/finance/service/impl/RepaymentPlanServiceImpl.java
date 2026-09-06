package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.constant.GenConstants;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSpecialQueryDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderCostVo;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderSpecialService;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanHisMapper;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractRepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.entity.InterfaceDataEntity;
import com.utfinancing.financehub.engine.rule.service.IInterfaceDataService;
import com.utfinancing.financehub.engine.utils.IRRUtils;
import com.utfinancing.financehub.engine.utils.XirrUtils;
import com.utfinancing.financehub.etl.api.*;
import com.utfinancing.financehub.etl.model.dto.QueryRepaymentPlanDTO;
import com.utfinancing.financehub.etl.model.dto.SelectReceiveRepaymentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


/**
 * @Author : hzhao
 * @Date : Create in 2023-09-12
 * @Description :  RepaymentPlan服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class RepaymentPlanServiceImpl extends ServiceImpl<RepaymentPlanMapper, RepaymentPlanEntity>
        implements IRepaymentPlanService {

    private final RepaymentPlanMapper repaymentPlanMapper;
    private final IRepaymentPlanHisService hisService;
    private final RepaymentPlanHisMapper hisMapper;
    private final IContractService contractService;
    private final IOrgCompanyService orgCompanyService;
    private final IClaimOrderSpecialService claimOrderSpecialService;
    private final IRepaymentPlanCostService costService;

    private final IContractTaAmountService contractTaAmountService;

    private final IContractMonthService contractMonthService;

    @Resource
    private IRepaymentPlanExceldataService repaymentPlanExceldataService;

    @Resource
    private TyptBusinessFacade typtBusinessFacade;
    @Resource
    private OperationBusinessFacade operationBusinessFacade;

    @Resource
    private XwxtBusinessFacade xwxtBusinessFacade;

    @Resource
    private CommvehatBusinessFacade commvehatBusinessFacade;

    @Resource
    private CommvehBusinessFacade commvehBusinessFacade;

    @Resource
    private PassvehatBusinessFacade passvehatBusinessFacade;

    @Resource
    private PassvehBusinessFacade passvehBusinessFacade;
    @Resource
    @Lazy
    private Map<String, IRepaymentService> repaymentService;

    @Resource
    private IRepaymentPlanChangeRecordsService repaymentPlanChangeRecordsService;

    @Resource
    private IInterfaceDataService interfaceDataService;

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Resource
    private IServiceNoAmortizationService serviceNoAmortizationService;

    @Resource
    private IOutstandingAmountInitService outstandingAmountInitService;

    @Override
    public Long saveRepaymentPlan(RepaymentPlanSaveDTO dto) {
        RepaymentPlanEntity entity = BeanUtil.copyProperties(dto, RepaymentPlanEntity.class);
        entity.setPlanDatePeriod(Integer.valueOf(DateUtil.format(entity.getPlanDate(), DatePattern.SIMPLE_MONTH_PATTERN)));
        this.save(entity);
        return entity.getId();
    }

    public void saveRepaymentPlan(List<RepaymentPlanSaveDTO> dtos) {
        //第一期为变更日的 把老变更日及以后的删除,再新增这部分数据
        LambdaUpdateWrapper<RepaymentPlanEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .ge(RepaymentPlanEntity::getPlanDate, dtos.get(0).getPlanDate())
                .eq(RepaymentPlanEntity::getContractCode, dtos.get(0).getContractCode());
//                .set(RepaymentPlanEntity::getUpdateTime, LocalDateTime.now())
//                .set(RepaymentPlanEntity::getDelFlag, YesOrNoEnum.YES.getCode());
        this.remove(updateChainWrapper);

        List<RepaymentPlanEntity> entities = BeanUtil.copyToList(dtos, RepaymentPlanEntity.class);
        entities.forEach(e -> {
            e.setPlanDatePeriod(Integer.valueOf(DateUtil.format(e.getPlanDate(),
                    DatePattern.SIMPLE_MONTH_PATTERN)));
            e.setChangeAfterEndingAmortizedCost(e.getEndingAmortizedCost());
            e.setChangeAfterRentalIncome(e.getRentalIncome());
        });
        this.saveBatch(entities);
    }

    /**
     * 更新现金流、租金、本金、利息
     */
    private void updateCashFlowForRepayment(List<RepaymentPlanSaveDTO> newRepaymentsPlan) {
        if (newRepaymentsPlan == null || newRepaymentsPlan.isEmpty()) {
            return;
        }

        for (RepaymentPlanSaveDTO dto : newRepaymentsPlan) {
            LambdaUpdateWrapper<RepaymentPlanEntity> wrapper = new LambdaUpdateWrapper();
            wrapper.eq(RepaymentPlanEntity::getContractCode, dto.getContractCode());
            wrapper.eq(RepaymentPlanEntity::getPlanDate, dto.getPlanDate());
            wrapper.set(RepaymentPlanEntity::getCashFlow, dto.getCashFlow());
            wrapper.set(RepaymentPlanEntity::getRentAmount, dto.getRentAmount());
            wrapper.set(RepaymentPlanEntity::getPrincipalAmount, dto.getPrincipalAmount());
            wrapper.set(RepaymentPlanEntity::getInterestAmount, dto.getInterestAmount());
            wrapper.set(RepaymentPlanEntity::getChangeAfterEndingAmortizedCost, dto.getChangeAfterEndingAmortizedCost());
            wrapper.set(RepaymentPlanEntity::getChangeAfterRentalIncome, dto.getChangeAfterRentalIncome());
            this.update(wrapper);
        }
    }

    /**
     * 偿还计划变更时处理
     */
    @Override
    public void saveRawData(String messageId, JSONObject jsonObject) {
        log.info("偿还计划队列开始:{}", jsonObject);
        String contractCode = jsonObject.getString("contractCode");
        String systemCode = jsonObject.getString("systemCode");
        String orgId = jsonObject.getString("orgId");

        if (StringUtils.isEmpty(contractCode)) {
            log.error("起租或者偿还计划变更事件，合同编码不能为空");
            return;
        }
        if (StringUtils.isEmpty(systemCode)) {
            log.error("起租或者偿还计划变更事件，系统编码不能为空; 合同编码：" + contractCode);
            return;
        }
        if (StringUtils.isEmpty(orgId)) {
            log.error("起租或者偿还计划变更事件，签约主体编码不能为空; 合同编码：" + contractCode);
            return;
        }

        ContractMonthDTO contractMonthDTO = contractMonthService.getContractDTOByCode(contractCode, orgId);
        ContractDTO contractDTO = BeanUtil.copyProperties(contractMonthDTO, ContractDTO.class);
        if (contractDTO == null) {
            log.error("合同信息不存在; 合同编码：" + contractCode);
            return;
        } else {
            // 合同计提方式如果为空，则默认采用irr计提--上线以后的新合同按照XIRR进行计提
            if (StringUtils.isEmpty(contractDTO.getIncomeProvisionMethod())) {
                contractDTO.setIncomeProvisionMethod(AccrualMethodEnum.IRR.getCode());
            }
        }

        // 查询数据有则修改偿还计划,没有则返回
        List<RepaymentPlanEntity> oldRepaymentPlanEntityList = this.selectByContractCodeAndSystemCode(contractCode, systemCode);
        if (CollectionUtils.isEmpty(oldRepaymentPlanEntityList)) {
            return;
        }

        // 从业务系统取得最新的偿还计划
        List<RepaymentPlanSaveDTO> repaymentPlanList = new ArrayList<>();
        if (SystemEnum.CWZT.getCode().equals(systemCode)) {
            JSONArray repaymentPlanListJson = jsonObject.getJSONArray("repaymentPlanList");
            if (CollectionUtils.isEmpty(repaymentPlanListJson)) {
                throw new ServiceException("数据格式错误");
            }

            for (int i = 0; i < repaymentPlanListJson.size(); i++) {
                JSONObject object = repaymentPlanListJson.getJSONObject(i);
                RepaymentPlanSaveDTO javaObject = object.toJavaObject(RepaymentPlanSaveDTO.class);
                javaObject.setRecaptureStatus(RecaptureStatusEnum.NOTRECOVERED.getCode());
                javaObject.setContractCode(contractCode);
                javaObject.setSystemCode(systemCode);
                javaObject.setOrgId(orgId);
                javaObject.setManualChangeMark(YesOrNoEnum.YES.getCode());
                repaymentPlanList.add(javaObject);
            }
        } else {
            repaymentPlanList = this.getNewRepaymentPlan(contractDTO, contractDTO.getIncomeProvisionMethod());
        }

        // 重新计算现金流
        this.cashflowRecompute(repaymentPlanList, contractDTO, oldRepaymentPlanEntityList);

//        if (CollectionUtils.isEmpty(oldRepaymentPlanEntityList)) {
        // 新增,历史表,主表
//            for (int i = 0; i < repaymentPlanListJson.size(); i++) {
//                JSONObject object = repaymentPlanListJson.getJSONObject(i);
//                if (SystemEnum.SYCXT.getCode().equals(systemCode) || SystemEnum.CYCXT.getCode().equals(systemCode)) {
//                    if (null == object.getInteger("periods") || 0 == object.getInteger("periods")) {
//                        continue;
//                    }
//                    DateTime planDate = DateUtil.parseDate(object.getString("planDate"));
//                    object.put("planDate", planDate.getTime());
//                }
//                RepaymentPlanSaveDTO javaObject = object.toJavaObject(RepaymentPlanSaveDTO.class);
//                javaObject.setRecaptureStatus(RecaptureStatusEnum.NOTRECOVERED.getCode());
//                javaObject.setContractCode(contractCode);
//                javaObject.setSystemCode(systemCode);
//                repaymentPlanList.add(javaObject);
//            }

//            repaymentPlanList = this.makeRepaymentPlan(contractDTO, repaymentPlanList);
//            this.saveRepaymentPlan(repaymentPlanList);
//        } else {
        // 修改的情况 记录businessDate为修改时间
//            Date businessDate = new Date();
//            if (SystemEnum.TYPT.getCode().equals(systemCode)) {
//                businessDate = jsonObject.getDate("businessDate");
//            } else if (SystemEnum.XWXT.getCode().equals(systemCode)) {
//                String businessDateFormat = jsonObject.getString("businessDate");
//                if (StringUtils.isBlank(businessDateFormat)) {
//                    businessDate = DateUtil.beginOfDay(new Date());
//                } else {
//                    businessDate = DateUtil.parseDate(businessDateFormat);
//                }
//            } else {
//                String businessDateFormat = jsonObject.getString("businessDate");
//                if (StringUtils.isBlank(businessDateFormat)) {
//                    businessDate = DateUtil.beginOfDay(new Date());
//                } else {
//                    businessDate = DateUtil.parseDate(businessDateFormat);
//                }
//            }
        // 修改
//            List<RepaymentPlanSaveDTO> repaymentPlanList = new ArrayList<>();
//            for (int i = 0; i < repaymentPlanListJson.size(); i++) {
//                JSONObject object = repaymentPlanListJson.getJSONObject(i);
//                if (SystemEnum.SYCXT.getCode().equals(systemCode) || SystemEnum.CYCXT.getCode().equals(systemCode)) {
//                    if (null == object.getInteger("periods") || 0 == object.getInteger("periods")) {
//                        continue;
//                    }
//                    DateTime planDate = DateUtil.parseDate(object.getString("planDate"));
//                    object.put("planDate", planDate.getTime());
//                }
//                RepaymentPlanSaveDTO javaObject = object.toJavaObject(RepaymentPlanSaveDTO.class);
////                javaObject.setRecaptureStatus(object.getString("status"));
//                javaObject.setContractCode(contractCode);
//                javaObject.setSystemCode(systemCode);
//                repaymentPlanList.add(javaObject);
//            }

        // 取得变更的日期
        Date finalBusinessDate = DateUtil.beginOfDay(DateUtil.endOfMonth(DateUtils.parseDate(DateUtil.now())));

        // 重新进行偿还计划测算
        List<RepaymentPlanSaveDTO> newRepaymentPlanList = this.repaymentPlanChange(contractDTO,
                oldRepaymentPlanEntityList.get(0).getSourceIrrRate(), repaymentPlanList);
        // 保存历史偿还计划
        List<RepaymentPlanSaveDTO> hisBatchList = BeanUtil.copyToList(oldRepaymentPlanEntityList,
                RepaymentPlanSaveDTO.class);
        hisService.saveRepaymentPlanHisBatch(hisBatchList, finalBusinessDate);

        // 存储最新的偿还计划（保存大于等于变更日期的记录，保存小于变更日期之前的现金流、租金、本金和利息）
        List<RepaymentPlanSaveDTO> saveRepaymentPlanList = newRepaymentPlanList.stream().
                filter(e -> e.getPlanDate().compareTo(finalBusinessDate) >= 0).collect(Collectors.toList());
        List<RepaymentPlanSaveDTO> updateRepaymentPlanList = newRepaymentPlanList.stream().
                filter(e -> e.getPlanDate().compareTo(finalBusinessDate) < 0).collect(Collectors.toList());
        List<RepaymentPlanEntity> oldChangeBeforeRepaymentList = oldRepaymentPlanEntityList.stream().
                filter(e -> e.getPlanDate().compareTo(finalBusinessDate) < 0).collect(Collectors.toList());

        // 改变变更日期后第一条数据的XIRR Rate=原偿还计划的利率
        saveRepaymentPlanList.get(0).setXirrRate(oldRepaymentPlanEntityList.get(0).getXirrRate());
        // 变更日期后的偿还计划保存
        // 调整额计算
        BigDecimal oldChangeBeforeRentalIncome = oldChangeBeforeRepaymentList.stream().map(
                RepaymentPlanEntity::getRentalIncome).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal newChangeBeforeRentalIncome = updateRepaymentPlanList.stream().map(
                RepaymentPlanSaveDTO::getRentalIncome).reduce(BigDecimal.ZERO, BigDecimal::add);
        saveRepaymentPlanList.get(0).setAdjustmentAmount(newChangeBeforeRentalIncome.subtract(oldChangeBeforeRentalIncome));
        this.saveRepaymentPlan(saveRepaymentPlanList);

        // 变更日期前的偿还计划数据更新
        this.updateCashFlowForRepayment(updateRepaymentPlanList);

        // 保存收到的偿还计划起租或者变更记录
        RepaymentPlanChangeRecordsEntity changeRecordsEntity = new RepaymentPlanChangeRecordsEntity();
        changeRecordsEntity.setId(IdWorker.getId());
        changeRecordsEntity.setChangeTime(LocalDateTime.now());
        changeRecordsEntity.setContractCode(contractCode);
        changeRecordsEntity.setOrgId(orgId);
        changeRecordsEntity.setSystemCode(contractDTO.getSystemCode());
        changeRecordsEntity.setLeaseStartOrChange(YesOrNoEnum.YES.getCode());
        repaymentPlanChangeRecordsService.save(changeRecordsEntity);

        // 要将合同的字段[是否有过偿还计划变更]更新为是
        contractDTO.setIsChangeRepayment(YesOrNoEnum.YES.getCode());
        contractService.updateContract(contractDTO.getId(), contractDTO);
//        }
    }

    /**
     * 合同起租定时任务处理
     */
    public void contractOnHireTask() {
        // 取得过去一年的起租数据
        List<InterfaceDataEntity> interfaceDataEntityList = interfaceDataService.queryOnHireContract();
        if (interfaceDataEntityList == null || interfaceDataEntityList.isEmpty()) {
            return;
        }

        Map<String, List<InterfaceDataEntity>> contractAndOrgMap = interfaceDataEntityList.stream().collect(
                Collectors.groupingBy(e -> this.getContractKey(e.getContractCode(), e.getOrgId())));
        int m = 1;
        for (String contractAndOrg : contractAndOrgMap.keySet()) {
            log.info("total:{}, cur record:{}", contractAndOrgMap.keySet().size(), m);
            m++;
            try {
                String contractCode = contractAndOrg.split("\\|")[0];
                log.info("合同{}起租任务开始", contractCode);
                String orgId = contractAndOrg.split("\\|")[1];
                List<InterfaceDataEntity> interfaceDataEntities = contractAndOrgMap.get(contractAndOrg);

                // 验证中台是否已经存在该合同的偿还计划-存在的情况下，则不再创建偿还计划
                List<RepaymentPlanEntity> repaymentPlanEntityList = this.selectByContractCodeAndSystemCode(
                        contractCode, interfaceDataEntities.get(0).getSystemCode());
                if (repaymentPlanEntityList != null && !repaymentPlanEntityList.isEmpty()) {
                    log.info("合同{}已经存在偿还计划，无需生成偿还计划", contractCode);
                    continue;
                }

                // 取得合同信息
                ContractDTO contractDTO = contractService.getTranStatusDTOByCode(contractCode, orgId);
                if (contractDTO == null) {
                    log.info("合同{}表数据不存在", contractCode);
                    continue;
                } else {
                    contractDTO.setIncomeProvisionMethod(AccrualMethodEnum.XIRR.getCode());
                }

                // 取得最新的偿还计划
                List<RepaymentPlanSaveDTO> repaymentPlanSaveDTOList = this.getNewRepaymentPlanFromTable(
                        contractDTO, interfaceDataEntities.get(0).getSystemCode());
                if (repaymentPlanSaveDTOList == null || repaymentPlanSaveDTOList.isEmpty()) {
                    log.info("合同{}取最新偿还计划失败!", contractCode);
                    continue;
                }
                // 从偿还计划列表中找到第一期的记录
                Optional<RepaymentPlanSaveDTO> firstPeriodPlanOpt = repaymentPlanSaveDTOList.stream()
                        .filter(item -> Objects.nonNull(item.getPeriods()) && item.getPeriods() == 1)
                        .findFirst();
                // 如果找到第一期偿还计划且起租日期与第一期计划日期不在同一月份,或者租金为0的时候,则将首期租金设为0
                if (firstPeriodPlanOpt.isPresent()) {
                    RepaymentPlanSaveDTO repaymentPlanSaveDTO = firstPeriodPlanOpt.get();
                    DateTime leaseStartDate = DateUtil.date(contractDTO.getLeaseDateStart());
                    if (!DateUtil.isSameMonth(repaymentPlanSaveDTO.getPlanDate(), leaseStartDate)
                            || repaymentPlanSaveDTO.getRentAmount().compareTo(BigDecimal.ZERO) == 0) {
                        contractDTO.setFirstRent(BigDecimal.ZERO);
                    }
                }

                // 计算现金流出
                BigDecimal cashflowOut = this.cashflowOutCompute(contractDTO);
                log.info("contractCode:{}, cashflowout:{}", contractDTO.getContractCode(), cashflowOut.toString());

                // 设置现金流出
                Date leaseDateStart = DateUtil.date(contractDTO.getLeaseDateStart());
                RepaymentPlanSaveDTO repaymentPlanSaveDTO = repaymentPlanSaveDTOList.get(0);
                Date planDate = repaymentPlanSaveDTO.getPlanDate();
                // 判断合同起租日和第一期计划日是否在同一个月,如果在同一个月则更新第一条现金流出
                if (DateUtil.isSameMonth(leaseDateStart, planDate)) {
                    repaymentPlanSaveDTOList.get(0).setCashFlow(cashflowOut);
                } else {
                    RepaymentPlanSaveDTO firstRepaymentPlanSaveDTO = this.createFirstRepaymentPlan(contractDTO, cashflowOut);
                    repaymentPlanSaveDTOList.add(0, firstRepaymentPlanSaveDTO);
                }

                // 重新计算现金流入
                for (int i = 0; i < repaymentPlanSaveDTOList.size(); i++) {
                    RepaymentPlanSaveDTO dto = repaymentPlanSaveDTOList.get(i);
                    if (dto.getPrincipalAmount() == null) {
                        dto.setPrincipalAmount(BigDecimal.ZERO);
                    }
                    if (dto.getInterestAmount() == null) {
                        dto.setInterestAmount(BigDecimal.ZERO);
                    }

                    // 第一条现金流出不再计算
                    if (i == 0) {
                        continue;
                    }

                    if (LeaseTypeEnum.DIRECT.getCode().equals(contractDTO.getLeaseType())) {
                        BigDecimal principalAmount = dto.getPrincipalAmount().divide(
                                Constants.DIRECT_RATE, 2, RoundingMode.HALF_UP);
                        BigDecimal interestAmount = dto.getInterestAmount().divide(
                                Constants.DIRECT_RATE, 2, RoundingMode.HALF_UP);
                        dto.setCashFlow(principalAmount.add(interestAmount));
                    } else {
                        BigDecimal interestAmount = dto.getInterestAmount().divide(
                                Constants.LEASEBACK_RATE, 2, RoundingMode.HALF_UP);
                        dto.setCashFlow(dto.getPrincipalAmount().add(interestAmount));
                    }

                    // 最后一条现金流需要加上残值
                    if (i == repaymentPlanSaveDTOList.size() - 1) {
                        // 名义留购价
                        BigDecimal retainedPrice = BigDecimal.ZERO;
                        if (LeaseTypeEnum.DIRECT.getCode().equals(contractDTO.getLeaseType())) {
                            if (contractDTO.getRetainedPrice() != null) {
                                retainedPrice = contractDTO.getRetainedPrice().divide(
                                        Constants.DIRECT_RATE, 2, RoundingMode.HALF_UP);
                            }
                        } else {
                            if (contractDTO.getRetainedPrice() != null) {
                                retainedPrice = contractDTO.getRetainedPrice().divide(
                                        Constants.LEASEBACK_RATE, 2, RoundingMode.HALF_UP);
                            }
                        }
                        dto.setCashFlow(dto.getCashFlow().add(retainedPrice));
                    }
                }

                // 生成新的偿还计划，并保存
                List<RepaymentPlanSaveDTO> newRepaymentPlanList = this.makeRepaymentPlan(contractDTO, repaymentPlanSaveDTOList);
                this.saveRepaymentPlan(newRepaymentPlanList);

                // 要将合同的字段[是否有过偿还计划变更]更新为是
                contractDTO.setIsChangeRepayment(YesOrNoEnum.YES.getCode());
                contractService.updateContract(contractDTO.getId(), contractDTO);
                log.info("合同{}起租任务结束....", contractCode);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("合同{}起租任务失败", contractAndOrg);
            }
        }
    }

    /**
     * 创建现金流出的偿还计划对象
     */
    private RepaymentPlanSaveDTO createFirstRepaymentPlan(ContractDTO contractDTO, BigDecimal cashflowOut) {
        RepaymentPlanSaveDTO dto = new RepaymentPlanSaveDTO();
        dto.setId(IdWorker.getId());
        dto.setPlanDate(DateUtil.date(contractDTO.getLeaseDateStart()));
        dto.setCashFlow(cashflowOut);
        dto.setOrgId(contractDTO.getOrgId());
        dto.setContractCode(contractDTO.getContractCode());
        dto.setRentAmount(BigDecimal.ZERO);
        dto.setPrincipalAmount(BigDecimal.ZERO);
        dto.setInterestAmount(BigDecimal.ZERO);
        dto.setRentalIncome(BigDecimal.ZERO);
        dto.setPeriods(0);
        dto.setRecaptureStatus(RecaptureStatusEnum.RETURNED.getCode());
        dto.setSystemCode(contractDTO.getSystemCode());
        dto.setIncomeProvisionMethod(contractDTO.getIncomeProvisionMethod());
        return dto;
    }

    /**
     * 从表里取得偿还计划
     */
    private List<RepaymentPlanSaveDTO> getNewRepaymentPlanFromTable(ContractDTO contractDTO, String systemCode) {
        String transferSystemCode = StringUtils.EMPTY;
        if (SystemEnum.SYCXT.getCode().equals(systemCode)) {
            transferSystemCode = "hy";

        } else if (SystemEnum.TYPT.getCode().equals(systemCode)) {
            transferSystemCode = "pl";

        } else if (SystemEnum.XWXT.getCode().equals(systemCode)) {
            transferSystemCode = "xw";

        } else if (SystemEnum.CYCXT.getCode().equals(systemCode)) {
            transferSystemCode = "hy";
        } else if (SystemEnum.YYPT.getCode().equals(systemCode)) {
            transferSystemCode = "yy";
        } else {
            return null;
        }
        List<RepaymentPlanEntity> repaymentPlanEntities = repaymentService.get(transferSystemCode).
                selectRepaymentByContract(contractDTO.getContractCode());
        List<RepaymentPlanSaveDTO> repaymentPlanList = BeanUtil.copyToList(repaymentPlanEntities, RepaymentPlanSaveDTO.class);
        return repaymentPlanList;
    }

    /**
     * 取得最新的偿还计划
     */
    private List<RepaymentPlanSaveDTO> getNewRepaymentPlan(ContractDTO contractDTO, String systemCode) {

        // 从业务系统取得最新的偿还计划
        List<RepaymentPlanSaveDTO> repaymentPlanList = new ArrayList<>();

        QueryRepaymentPlanDTO queryRepaymentPlanDTO = new QueryRepaymentPlanDTO();
        queryRepaymentPlanDTO.setContractCode(contractDTO.getContractCode());
        queryRepaymentPlanDTO.setIncomeProvisionMethod(contractDTO.getIncomeProvisionMethod());
        if (SystemEnum.SYCXT.getCode().equals(systemCode)) {
            R<List<com.utfinancing.financehub.etl.model.dto.RepaymentPlanSaveDTO>> repaymentPlanFromSycxtList =
                    commvehBusinessFacade.queryRepaymentPlan(queryRepaymentPlanDTO);
            if (repaymentPlanFromSycxtList.getData() == null || repaymentPlanFromSycxtList.getData().isEmpty()) {
                repaymentPlanFromSycxtList = commvehatBusinessFacade.queryRepaymentPlan(queryRepaymentPlanDTO);
                if (repaymentPlanFromSycxtList.getData() == null || repaymentPlanFromSycxtList.getData().isEmpty()) {
                    log.info("合同：" + contractDTO.getContractCode() + "未能从业务系统取得最新的偿还计划!");
                    return new ArrayList<>();
                }
            }
            repaymentPlanList = BeanUtil.copyToList(repaymentPlanFromSycxtList.getData(), RepaymentPlanSaveDTO.class);

            // 存储到偿还计划原始数据表
            List<RepaymentPlanHYEntity> repaymentPlanHYEntityList = BeanUtil.copyToList(
                    repaymentPlanList, RepaymentPlanHYEntity.class);
            LambdaQueryWrapper<RepaymentPlanHYEntity> wrapper = new LambdaQueryWrapper();
            wrapper.eq(RepaymentPlanHYEntity::getContractCode, contractDTO.getContractCode());
            repaymentService.get("hy").remove(wrapper);
            repaymentService.get("hy").saveBatch(repaymentPlanHYEntityList);


        } else if (SystemEnum.TYPT.getCode().equals(systemCode)) {
            R<List<com.utfinancing.financehub.etl.model.dto.RepaymentPlanSaveDTO>> repaymentPlanFromTyptList =
                    typtBusinessFacade.queryRepaymentPlan(queryRepaymentPlanDTO);
            if (repaymentPlanFromTyptList.getData() == null || repaymentPlanFromTyptList.getData().isEmpty()) {
                log.info("合同：" + contractDTO.getContractCode() + "未能从业务系统取得最新的偿还计划!");
                return new ArrayList<>();
            }
            repaymentPlanList = BeanUtil.copyToList(repaymentPlanFromTyptList.getData(), RepaymentPlanSaveDTO.class);

            // 存储到偿还计划原始数据表
            LambdaQueryWrapper<RepaymentPlanPLEntity> wrapper = new LambdaQueryWrapper();
            wrapper.eq(RepaymentPlanPLEntity::getContractCode, contractDTO.getContractCode());
            repaymentService.get("pl").remove(wrapper);
            List<RepaymentPlanPLEntity> repaymentPlanPLEntityList = BeanUtil.copyToList(
                    repaymentPlanList, RepaymentPlanPLEntity.class);
            repaymentService.get("pl").saveBatch(repaymentPlanPLEntityList);


        } else if (systemCode.contains(SystemEnum.YYPT.getCode())) {
            R<List<com.utfinancing.financehub.etl.model.dto.RepaymentPlanSaveDTO>> repaymentPlanFromTyptList =
                    operationBusinessFacade.queryRepaymentPlan(queryRepaymentPlanDTO);
            if (repaymentPlanFromTyptList.getData() == null || repaymentPlanFromTyptList.getData().isEmpty()) {
                log.info("合同：" + contractDTO.getContractCode() + "未能从业务系统取得最新的偿还计划!");
                return new ArrayList<>();
            }
            repaymentPlanList = BeanUtil.copyToList(repaymentPlanFromTyptList.getData(), RepaymentPlanSaveDTO.class);

            // 存储到偿还计划原始数据表
            LambdaQueryWrapper<RepaymentPlanPLEntity> wrapper = new LambdaQueryWrapper();
            wrapper.eq(RepaymentPlanPLEntity::getContractCode, contractDTO.getContractCode());
            repaymentService.get("yy").remove(wrapper);
            List<RepaymentPlanPLEntity> repaymentPlanPLEntityList = BeanUtil.copyToList(
                    repaymentPlanList, RepaymentPlanPLEntity.class);
            repaymentService.get("yy").saveBatch(repaymentPlanPLEntityList);

        } else if (SystemEnum.XWXT.getCode().equals(systemCode)) {
            R<List<com.utfinancing.financehub.etl.model.dto.RepaymentPlanSaveDTO>> repaymentPlanFromXwxtList =
                    xwxtBusinessFacade.queryRepaymentPlan(queryRepaymentPlanDTO);
            if (repaymentPlanFromXwxtList.getData() == null || repaymentPlanFromXwxtList.getData().isEmpty()) {
                log.info("合同：" + contractDTO.getContractCode() + "未能从业务系统取得最新的偿还计划!");
                return new ArrayList<>();
            }
            repaymentPlanList = BeanUtil.copyToList(repaymentPlanFromXwxtList.getData(), RepaymentPlanSaveDTO.class);

            // 存储到偿还计划原始数据表
            List<RepaymentPlanXWEntity> repaymentPlanXWEntityList = BeanUtil.copyToList(
                    repaymentPlanList, RepaymentPlanXWEntity.class);
            LambdaQueryWrapper<RepaymentPlanXWEntity> wrapper = new LambdaQueryWrapper();
            wrapper.eq(RepaymentPlanXWEntity::getContractCode, contractDTO.getContractCode());
            repaymentService.get("xw").remove(wrapper);
            repaymentService.get("xw").saveBatch(repaymentPlanXWEntityList);


        } else if (SystemEnum.CYCXT.getCode().equals(systemCode)) {
            R<List<com.utfinancing.financehub.etl.model.dto.RepaymentPlanSaveDTO>> repaymentPlanFromCycxtList =
                    passvehBusinessFacade.queryRepaymentPlan(queryRepaymentPlanDTO);
            if (repaymentPlanFromCycxtList.getData() == null || repaymentPlanFromCycxtList.getData().isEmpty()) {
                repaymentPlanFromCycxtList = passvehatBusinessFacade.queryRepaymentPlan(queryRepaymentPlanDTO);
                if (repaymentPlanFromCycxtList.getData() == null || repaymentPlanFromCycxtList.getData().isEmpty()) {
                    log.info("合同：" + contractDTO.getContractCode() + "未能从业务系统取得最新的偿还计划!");
                    return new ArrayList<>();
                }
            }
            repaymentPlanList = BeanUtil.copyToList(repaymentPlanFromCycxtList.getData(), RepaymentPlanSaveDTO.class);

            // 存储到偿还计划原始数据表
            List<RepaymentPlanHYEntity> repaymentPlanHYEntityList = BeanUtil.copyToList(
                    repaymentPlanList, RepaymentPlanHYEntity.class);
            LambdaQueryWrapper<RepaymentPlanHYEntity> wrapper = new LambdaQueryWrapper();
            wrapper.eq(RepaymentPlanHYEntity::getContractCode, contractDTO.getContractCode());
            repaymentService.get("hy").remove(wrapper);
            repaymentService.get("hy").saveBatch(repaymentPlanHYEntityList);

        }
        return repaymentPlanList.stream().sorted(Comparator.comparing(e -> e.getPlanDate())).collect(Collectors.toList());
    }


    /**
     * 取得最新的回笼数据
     */
    private List<SelectReceiveRepaymentDTO> getNewReturnedAmount(ContractDTO contractDTO, String systemCode) {

        QueryRepaymentPlanDTO queryRepaymentPlanDTO = new QueryRepaymentPlanDTO();
        queryRepaymentPlanDTO.setContractCode(contractDTO.getContractCode());

        R<List<SelectReceiveRepaymentDTO>> receivedRepaymentListR = null;
        if (SystemEnum.SYCXT.getCode().equals(systemCode)) {
            receivedRepaymentListR = commvehBusinessFacade.queryReceivedRepaymentPlan(queryRepaymentPlanDTO);
            if (receivedRepaymentListR == null || receivedRepaymentListR.getData() == null
                    || receivedRepaymentListR.getData().isEmpty()) {
                receivedRepaymentListR = commvehatBusinessFacade.queryReceivedRepaymentPlan(queryRepaymentPlanDTO);
            }

        } else if (SystemEnum.TYPT.getCode().equals(systemCode)) {
            receivedRepaymentListR = typtBusinessFacade.queryReceivedRepaymentPlan(queryRepaymentPlanDTO);

        } else if (SystemEnum.XWXT.getCode().equals(systemCode)) {
            receivedRepaymentListR = xwxtBusinessFacade.queryReceivedRepaymentPlan(queryRepaymentPlanDTO);

        } else if (SystemEnum.CYCXT.getCode().equals(systemCode)) {
            receivedRepaymentListR = passvehBusinessFacade.queryReceivedRepaymentPlan(queryRepaymentPlanDTO);
            if (receivedRepaymentListR == null || receivedRepaymentListR.getData() == null
                    || receivedRepaymentListR.getData().isEmpty()) {
                receivedRepaymentListR = passvehatBusinessFacade.queryReceivedRepaymentPlan(queryRepaymentPlanDTO);
            }
        }

        if (receivedRepaymentListR == null) {
            return new ArrayList<>();
        }
        return receivedRepaymentListR.getData();
    }

    /**
     * 取得合同的业务主键
     */
    private String getContractKey(String contractCode, String orgId) {
        StringBuffer result = new StringBuffer(contractCode).append("|");
        if (StringUtils.isNotEmpty(orgId)) {
            result = result.append(orgId);
        }
        return result.toString();
    }


    /**
     * 重新计算现金流
     */
    private void cashflowRecompute(List<RepaymentPlanSaveDTO> repaymentPlanList, ContractDTO contractDTO,
                                   List<RepaymentPlanEntity> oldRepaymentPlanEntityList) {

        // 交易结构变更-现金流不变
        if (DateUtil.isSameDay(oldRepaymentPlanEntityList.get(0).getPlanDate(),
                repaymentPlanList.get(0).getPlanDate())) {
            repaymentPlanList.get(0).setCashFlow(oldRepaymentPlanEntityList.get(0).getCashFlow());
        } else {
            RepaymentPlanSaveDTO insertEntity = BeanUtil.copyProperties(
                    oldRepaymentPlanEntityList.get(0), RepaymentPlanSaveDTO.class);
            repaymentPlanList.add(0, insertEntity);
        }

        for (int i = 0; i < repaymentPlanList.size(); i++) {
            RepaymentPlanSaveDTO dto = repaymentPlanList.get(i);
            if (dto.getCashFlow().compareTo(BigDecimal.ZERO) < 0) {
                continue;
            }
            dto.setRentAmount(dto.getInterestAmount().add(dto.getPrincipalAmount()));
            BigDecimal retainedPrice = BigDecimal.ZERO;
            if (LeaseTypeEnum.DIRECT.getCode().equals(contractDTO.getLeaseType())) {
                dto.setCashFlow(dto.getInterestAmount().divide(Constants.DIRECT_RATE, 2, BigDecimal.ROUND_HALF_UP).
                        add(dto.getPrincipalAmount().divide(Constants.DIRECT_RATE, 2, BigDecimal.ROUND_HALF_UP)));
                retainedPrice = contractDTO.getRetainedPrice().divide(Constants.DIRECT_RATE, 2, BigDecimal.ROUND_HALF_UP);
            } else {
                dto.setCashFlow(dto.getInterestAmount().divide(
                        Constants.LEASEBACK_RATE, 2, BigDecimal.ROUND_HALF_UP).add(dto.getPrincipalAmount()));
                retainedPrice = contractDTO.getRetainedPrice().divide(Constants.LEASEBACK_RATE, 2, BigDecimal.ROUND_HALF_UP);
            }
            if (i == repaymentPlanList.size() - 1) {
                dto.setCashFlow(dto.getCashFlow().add(retainedPrice));
            }
        }
    }

    /**
     * 现金流出计算
     */
    private BigDecimal cashflowOutCompute(ContractDTO contractDTO) {

        if (contractDTO.getPayableDeviceAmount() == null) {
            contractDTO.setPayableDeviceAmount(BigDecimal.ZERO);
        }
        if (contractDTO.getLessorInsuranceAmount() == null) {
            contractDTO.setLessorInsuranceAmount(BigDecimal.ZERO);
        }
        if (contractDTO.getChannelFees() == null) {
            contractDTO.setChannelFees(BigDecimal.ZERO);
        }
        if (contractDTO.getEstimateGPSExpense() == null) {
            contractDTO.setEstimateGPSExpense(BigDecimal.ZERO);
        }
        if (contractDTO.getPayableOtherAmount() == null) {
            contractDTO.setPayableOtherAmount(BigDecimal.ZERO);
        }
        if (contractDTO.getReceivableInsuranceAmount() == null) {
            contractDTO.setReceivableInsuranceAmount(BigDecimal.ZERO);
        }
        if (contractDTO.getPayableService() == null) {
            contractDTO.setPayableService(BigDecimal.ZERO);
        }
        if (contractDTO.getReceivableFirstAmount() == null) {
            contractDTO.setReceivableFirstAmount(BigDecimal.ZERO);
        }
        if (contractDTO.getReceivableProcedureAmount() == null) {
            contractDTO.setReceivableProcedureAmount(BigDecimal.ZERO);
        }
        if (contractDTO.getPayableInsuranceAmount() == null) {
            contractDTO.setPayableInsuranceAmount(BigDecimal.ZERO);
        }
        if (contractDTO.getFirstRent() == null) {
            contractDTO.setFirstRent(BigDecimal.ZERO);
        }
        if (contractDTO.getProcedure801() == null) {
            contractDTO.setProcedure801(BigDecimal.ZERO);
        }

        // 出租人保险费用
        BigDecimal lessorInsuranceAmount = contractDTO.getLessorInsuranceAmount().divide(
                Constants.LEASEBACK_RATE, 2, BigDecimal.ROUND_HALF_UP);
        // 渠道费用
        BigDecimal channelFee = contractDTO.getChannelFees().divide(
                Constants.LEASEBACK_RATE, 2, BigDecimal.ROUND_HALF_UP);
        // 出租人其它成本-GPS
        BigDecimal estimateGPSExpense = contractDTO.getEstimateGPSExpense().divide(
                Constants.DIRECT_RATE, 2, BigDecimal.ROUND_HALF_UP);
        // 出租人其它成本
        BigDecimal payableOtherAmount = contractDTO.getPayableOtherAmount().divide(
                Constants.LEASEBACK_RATE, 2, BigDecimal.ROUND_HALF_UP);
        // 其他收入(含增值税)
        BigDecimal receivableOther = contractDTO.getReceivableOther().divide(
                Constants.DIRECT_RATE, 2, BigDecimal.ROUND_HALF_UP);
        // 经销商服务费
        BigDecimal payableService = contractDTO.getPayableService().divide(
                Constants.LEASEBACK_RATE, 2, BigDecimal.ROUND_HALF_UP);
                // 应付保险费
        BigDecimal payableInsuranceAmount = contractDTO.getPayableInsuranceAmount().divide(
                Constants.LEASEBACK_RATE, 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal cashflowOut = BigDecimal.ZERO;
        if (LeaseTypeEnum.DIRECT.getCode().equals(contractDTO.getLeaseType())) {
            // 设备价格
            BigDecimal payableDeviceAmount = contractDTO.getPayableDeviceAmount().divide(
                    Constants.DIRECT_RATE, 2, BigDecimal.ROUND_HALF_UP);
            // 首付款
            BigDecimal receivableFirstAmount = contractDTO.getReceivableFirstAmount().divide(
                    Constants.DIRECT_RATE, 2, BigDecimal.ROUND_HALF_UP);
            // 应收保险费
            BigDecimal receivableInsuranceAmount = contractDTO.getReceivableInsuranceAmount().divide(
                    Constants.DIRECT_RATE, 2, BigDecimal.ROUND_HALF_UP);
            // 费用类型801
            BigDecimal procedure801 = contractDTO.getProcedure801().divide(
                    Constants.DIRECT_RATE, 2, BigDecimal.ROUND_HALF_UP);
            // 抵扣的设备款
            BigDecimal firstRent = contractDTO.getFirstRent().divide(
                    Constants.DIRECT_RATE, 2, BigDecimal.ROUND_HALF_UP);
            // 税后资金流出=设备款/1.13-首付款/1.13+应付保险费/1.06-应收保险费/1.13-抵扣的设备款/1.13+GPS/1.13-手续费/1.13(费用类型801)
            cashflowOut = payableDeviceAmount.subtract(receivableFirstAmount).
                    add(payableInsuranceAmount).subtract(receivableInsuranceAmount).
                    subtract(firstRent).add(estimateGPSExpense).subtract(procedure801);
        } else {
            // 应收保险费
            BigDecimal receivableInsuranceAmount = contractDTO.getReceivableInsuranceAmount().divide(
                    Constants.LEASEBACK_RATE, 2, BigDecimal.ROUND_HALF_UP);
            // 费用类型801
            BigDecimal procedure801 = contractDTO.getProcedure801().divide(
                    Constants.LEASEBACK_RATE, 2, BigDecimal.ROUND_HALF_UP);
            // 期初税后资金流出=设备款-首付款+应付保险费/1.06-应收保险费/1.06-抵扣的设备款+GPS/1.13-手续费/1.06(费用类型801)
            BigDecimal firstAmount = contractDTO.getPayableDeviceAmount().subtract(contractDTO.getReceivableFirstAmount());
            cashflowOut = firstAmount.add(payableInsuranceAmount).subtract(receivableInsuranceAmount).
                    subtract(contractDTO.getFirstRent()).add(estimateGPSExpense).
                    subtract(procedure801);
        }
        return cashflowOut.multiply(new BigDecimal(-1));
    }

    /**
     * 取得偿还计划变更日期(变更后记录到当月月底，所以取月底日期)
     */
//    private Date getRepaymentChangeDate() {
//        for (int i = 0; i < oldRepaymentPlanEntityList.size(); i++) {
//
//            if (i < newRepaymentPlanEntityList.size() && oldRepaymentPlanEntityList.get(i).getPlanDate().compareTo(
//                    newRepaymentPlanEntityList.get(i).getPlanDate()) != 0) {
//                return DateUtil.beginOfMonth(newRepaymentPlanEntityList.get(i).getPlanDate());
//            }
//
//            if (i >= newRepaymentPlanEntityList.size()) {
//                return DateUtil.beginOfMonth(oldRepaymentPlanEntityList.get(i).getPlanDate());
//            }
//        }
//
//        if (newRepaymentPlanEntityList.size() > oldRepaymentPlanEntityList.size()) {
//            return DateUtil.beginOfMonth(newRepaymentPlanEntityList.get(oldRepaymentPlanEntityList.size()).getPlanDate());
//        } else {
//            return null;
//        }
//        return DateUtil.endOfMonth(DateUtils.parseDate(DateUtil.now()));
//    }

    /**
     * 制定偿还计划-起租
     */
    private List<RepaymentPlanSaveDTO> makeRepaymentPlan(ContractDTO contractDTO, List<RepaymentPlanSaveDTO> repaymentPlanList) {
        // 如果计提方式为空,则更新计提方式为XIRR计提
        if (StringUtils.isEmpty(contractDTO.getIncomeProvisionMethod())) {
            contractDTO.setIncomeProvisionMethod(AccrualMethodEnum.XIRR.getCode());
        }

        // 按照plandate进行排序
        List<String> plandates = repaymentPlanList.stream().map(e -> DateUtil.format(e.getPlanDate(), "yyyy-MM-dd")).collect(Collectors.toList());
//        log.info("plandates: {}", JSON.toJSONString(plandates));
        repaymentPlanList = repaymentPlanList.stream()
//                .filter(e -> null != e.getPlanDate() && null != e.getRentAmount() && null != e.getPrincipalAmount())
                .sorted(Comparator.comparing(RepaymentPlanSaveDTO::getPlanDate))
                .collect(Collectors.toList());

        // 计算XIRR Rate
        List<Double> cashFlowList = repaymentPlanList.stream().map(e -> e.getCashFlow().doubleValue()).collect(Collectors.toList());
        List<Date> allPlanDate = repaymentPlanList.stream().map(e -> e.getPlanDate()).collect(Collectors.toList());
        List<String> formattedDates = allPlanDate.stream().map(date -> DateUtil.format(date, "yyyy-MM-dd")).collect(Collectors.toList());
        log.info("cashFlowList: {}, allPlanDate: {}", JSON.toJSONString(cashFlowList), JSON.toJSONString(formattedDates));
        Double incomeRateDouble = XirrUtils.xirr(cashFlowList, allPlanDate);

        // 期初摊余和期末摊余计算
        repaymentPlanList = this.generateXirrRepayments(repaymentPlanList, incomeRateDouble);

        // 其余字段修改
        BigDecimal totalUnrealizedRevenue = repaymentPlanList.stream().map(e -> e.getRentalIncome()).filter(
                Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal rentalIncomeBeforeTotal = BigDecimal.ZERO;
        BigDecimal rentalIncomeAfterTotal = totalUnrealizedRevenue;

        for (int i = 0; i < repaymentPlanList.size(); i++) {
            RepaymentPlanSaveDTO saveDTO = repaymentPlanList.get(i);
            saveDTO.setXirrRate(NumberUtil.mul(incomeRateDouble, new BigDecimal(100)).
                    setScale(5, RoundingMode.HALF_UP));
            saveDTO.setUnrealizedRevenue(totalUnrealizedRevenue);
            saveDTO.setRentalIncomeBeforeTotal(rentalIncomeBeforeTotal);
            rentalIncomeBeforeTotal = NumberUtil.add(rentalIncomeBeforeTotal, saveDTO.getRentalIncome());
            rentalIncomeAfterTotal = NumberUtil.sub(rentalIncomeAfterTotal, saveDTO.getRentalIncome());
            saveDTO.setRentalIncomeAfterTotal(rentalIncomeAfterTotal);
            saveDTO.setAccrued(YesOrNoEnum.YES.getCode());
            saveDTO.setIncomeProvisionMethod(AccrualMethodEnum.XIRR.getCode());
            saveDTO.setContractCode(contractDTO.getContractCode());
            saveDTO.setOrgId(contractDTO.getOrgId());
            saveDTO.setClientCode(contractDTO.getClientCode());
            saveDTO.setClientName(contractDTO.getClientName());
            saveDTO.setSystemCode(contractDTO.getSystemCode());
            saveDTO.setPlanDatePeriod(Integer.valueOf(DateUtil.format(saveDTO.getPlanDate(), DatePattern.SIMPLE_MONTH_PATTERN)));
            if (saveDTO.getRentalIncome() == null) {
                saveDTO.setServiceFeeAmortizationRate(BigDecimal.ZERO);
            } else {
                saveDTO.setServiceFeeAmortizationRate(saveDTO.getRentalIncome().divide(totalUnrealizedRevenue, 6, RoundingMode.HALF_UP));
            }
        }
        return repaymentPlanList;
    }

    /**
     * 是否连续分月判断
     */
    private boolean isContinuousMonth(List<RepaymentPlanSaveDTO> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            RepaymentPlanSaveDTO entity = dataList.get(i);
            if (i == dataList.size() - 1) {
                return true;
            } else {
                if (!DateUtil.isSameMonth(entity.getPlanDate(), dataList.get(i + 1).getPlanDate())
                        && !DateUtil.isSameMonth(DateUtil.offsetMonth(entity.getPlanDate(), 1), dataList.get(i + 1).getPlanDate())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 创建每月偿还计划数据
     */
    private List<RepaymentPlanSaveDTO> createEmptyRepaymentsData(List<RepaymentPlanSaveDTO> dataList) {

        List<RepaymentPlanSaveDTO> repayments = new ArrayList<>();
        Date lastDate = dataList.get(0).getPlanDate();
        Date compareDate = DateUtil.offsetMonth(lastDate, 1);

        for (int i = 0; i < dataList.size(); i++) {
            if (i == 0) {
                repayments.add(dataList.get(i));
            } else {
                while (true) {
                    if (DateUtil.isSameMonth(compareDate, dataList.get(i).getPlanDate())) {
                        repayments.add(dataList.get(i));
                        compareDate = DateUtil.offsetMonth(compareDate, 1);
                        break;
                    } else if (DateUtil.compare(compareDate, dataList.get(i).getPlanDate()) > 0) {
                        break;
                    } else {
                        repayments.add(this.initRepaymentPlan(compareDate));
                        compareDate = DateUtil.offsetMonth(compareDate, 1);
                    }
                }
            }
        }
        return repayments;
    }

    /**
     * 偿还计划变更
     */
    private List<RepaymentPlanSaveDTO> repaymentPlanChange(ContractDTO contractDTO, BigDecimal sourceIrrRate,
                                                           List<RepaymentPlanSaveDTO> repaymentPlanList) {

        Double incomeRateDouble = 0.00;
        if (AccrualMethodEnum.IRR.getCode().equals(contractDTO.getIncomeProvisionMethod())) {
            incomeRateDouble = IRRUtils.calculateIRRYear(
                    repaymentPlanList.stream().map(e -> e.getCashFlow().doubleValue()).collect(Collectors.toList()));
        } else if (AccrualMethodEnum.XIRR.getCode().equals(contractDTO.getIncomeProvisionMethod())) {
            List<Double> cashFlowList = repaymentPlanList.stream().map(e -> e.getCashFlow().doubleValue()).collect(Collectors.toList());
            List<Date> allPlanDate = repaymentPlanList.stream().map(e -> e.getPlanDate()).collect(Collectors.toList());
            incomeRateDouble = XirrUtils.xirr(cashFlowList, allPlanDate);
        }
        if (Double.isNaN(incomeRateDouble)) {
            repaymentPlanList.forEach(e -> e.setExceptionType("irr计算错误"));
            return repaymentPlanList;
        }

        // 偿还计划数据
        // 1.补齐合同从起租到结清的每月数据
        // 判断原数据是否为每月都有数据，如否，则需要补齐每月的数据对象，如是，则不用补齐
        if (!this.isContinuousMonth(repaymentPlanList)) {
            repaymentPlanList = this.createEmptyRepaymentsData(repaymentPlanList);
        }

        List<RepaymentPlanSaveDTO> newRepayments = new ArrayList<>();
        if (AccrualMethodEnum.IRR.getCode().equals(contractDTO.getIncomeProvisionMethod())) {
            List<RepaymentPlanEntity> repayments = BeanUtil.copyToList(repaymentPlanList, RepaymentPlanEntity.class);
            newRepayments = this.irrApportion(repayments, incomeRateDouble);
            if (newRepayments == null || newRepayments.isEmpty()) {
                return new ArrayList<>();
            }
        } else if (AccrualMethodEnum.XIRR.getCode().equals(contractDTO.getIncomeProvisionMethod())) {
            newRepayments = this.generateXirrRepayments(repaymentPlanList, incomeRateDouble);
        }

        // 5 数据处理
        BigDecimal totalUnrealizedRevenue = newRepayments.stream().map(e -> e.getRentalIncome()).filter(
                Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal rentalIncomeBeforeTotal = BigDecimal.ZERO;
        BigDecimal rentalIncomeAfterTotal = totalUnrealizedRevenue;

        for (int i = 0; i < newRepayments.size(); i++) {
            RepaymentPlanSaveDTO saveDTO = newRepayments.get(i);
            saveDTO.setXirrRate(NumberUtil.mul(incomeRateDouble, new BigDecimal(100)).
                    setScale(5, RoundingMode.HALF_UP));
            saveDTO.setUnrealizedRevenue(totalUnrealizedRevenue);
            saveDTO.setRentalIncomeBeforeTotal(rentalIncomeBeforeTotal);
            rentalIncomeBeforeTotal = NumberUtil.add(rentalIncomeBeforeTotal, saveDTO.getRentalIncome());
            rentalIncomeAfterTotal = NumberUtil.sub(rentalIncomeAfterTotal, saveDTO.getRentalIncome());
            saveDTO.setRentalIncomeAfterTotal(rentalIncomeAfterTotal);
            saveDTO.setAccrued(YesOrNoEnum.YES.getCode());
            saveDTO.setIncomeProvisionMethod(contractDTO.getIncomeProvisionMethod());
            saveDTO.setContractCode(contractDTO.getContractCode());
            saveDTO.setOrgId(contractDTO.getOrgId());
            saveDTO.setClientCode(contractDTO.getClientCode());
            saveDTO.setClientName(contractDTO.getClientName());
            saveDTO.setSystemCode(contractDTO.getSystemCode());
            saveDTO.setSourceIrrRate(sourceIrrRate);
            saveDTO.setPlanDatePeriod(Integer.valueOf(DateUtil.format(saveDTO.getPlanDate(), DatePattern.SIMPLE_MONTH_PATTERN)));
            saveDTO.setChangeAfterRentalIncome(saveDTO.getRentalIncome());
            saveDTO.setChangeAfterEndingAmortizedCost(saveDTO.getChangeAfterEndingAmortizedCost());
            if (saveDTO.getRentalIncome() == null) {
                saveDTO.setServiceFeeAmortizationRate(BigDecimal.ZERO);
            } else {
                saveDTO.setServiceFeeAmortizationRate(saveDTO.getRentalIncome().divide(totalUnrealizedRevenue, 6, RoundingMode.HALF_UP));
            }
            if (saveDTO.getAdjustmentAmount() == null) {
                saveDTO.setAdjustmentAmount(BigDecimal.ZERO);
            }
        }
        return newRepayments;
    }

    /**
     * IRR通用分摊方式
     */
    private List<RepaymentPlanSaveDTO> irrApportion(List<RepaymentPlanEntity> repayments, Double incomeRateDouble) {

        // 3.计算当月分摊金额
        for (int i = 0; i < repayments.size(); i++) {
            RepaymentPlanEntity saveDTO = repayments.get(i);

            if (i == 0) {
                saveDTO.setOpeningAmortizedCost(saveDTO.getCashFlow().negate());
                saveDTO.setEndingAmortizedCost(saveDTO.getOpeningAmortizedCost());
            } else {
                RepaymentPlanEntity lastSaveDTO = repayments.get(i - 1);
                saveDTO.setOpeningAmortizedCost(lastSaveDTO.getEndingAmortizedCost());
                BigDecimal rentalIncome = NumberUtil.mul(saveDTO.getOpeningAmortizedCost(), incomeRateDouble / 12).
                        setScale(2, RoundingMode.HALF_UP);
                saveDTO.setRentalIncome(rentalIncome);
                saveDTO.setEndingAmortizedCost(
                        NumberUtil.add(saveDTO.getOpeningAmortizedCost(), rentalIncome, saveDTO.getCashFlow().negate()));
                if (i == repayments.size() - 1) {
                    // 最后一期差补
                    BigDecimal compensation = saveDTO.getEndingAmortizedCost().negate();
                    saveDTO.setRentalIncome(NumberUtil.add(saveDTO.getRentalIncome(), compensation));
                    saveDTO.setEndingAmortizedCost(NumberUtil.add(saveDTO.getEndingAmortizedCost(), compensation));
                }
            }
        }

        // 4.继续拆分(是月底,不拆分)
        List<RepaymentPlanSaveDTO> newRepayments = new ArrayList<>();
        for (int i = 0; i < repayments.size(); i++) {
            RepaymentPlanSaveDTO saveDTO = new RepaymentPlanSaveDTO();
            BeanUtils.copyProperties(repayments.get(i), saveDTO);
            saveDTO.setIrrMark(true);
            newRepayments.add(saveDTO);
            // 最后一期不拆分
            if (i == repayments.size() - 1) {
                continue;
            }
            // 判断是否月末,不是则拆分
            if (!DateUtil.isLastDayOfMonth(saveDTO.getPlanDate())) {
                RepaymentPlanSaveDTO endOfMonthSaveDTO = new RepaymentPlanSaveDTO();
                RepaymentPlanSaveDTO entity = initRepaymentPlan(DateUtil.beginOfDay(DateUtil.endOfMonth(saveDTO.getPlanDate())));
                BeanUtils.copyProperties(entity, endOfMonthSaveDTO);
                endOfMonthSaveDTO.setOpeningAmortizedCost(saveDTO.getEndingAmortizedCost());
                endOfMonthSaveDTO.setEndingAmortizedCost(saveDTO.getEndingAmortizedCost());
                endOfMonthSaveDTO.setIrrMark(false);
                newRepayments.add(endOfMonthSaveDTO);
            }
        }

        // 拆分分摊金额
        for (int i = 1; i < newRepayments.size(); i++) {
            RepaymentPlanSaveDTO thisPlan = newRepayments.get(i);
            if (!thisPlan.getIrrMark()) {
                // 拆分的数据
                RepaymentPlanSaveDTO lastPlan = newRepayments.get(i - 1);
                RepaymentPlanSaveDTO nextPlan = newRepayments.get(i + 1);
                long betweenDay = DateUtil.betweenDay(nextPlan.getPlanDate(), lastPlan.getPlanDate(), true);
                if (betweenDay == 0) {
                    repayments.forEach(e -> e.setExceptionType("存在不同期数相同时间"));
                    return null;
                }
                BigDecimal newRentalIncome = NumberUtil.mul(nextPlan.getRentalIncome(),
                        NumberUtil.div(DateUtil.betweenDay(thisPlan.getPlanDate(), lastPlan.getPlanDate(), true),
                                betweenDay)).setScale(2, RoundingMode.HALF_UP);
                thisPlan.setRentalIncome(newRentalIncome);
            } else {
                // 非拆分的数据 上一期为非拆分的数据，则不处理，上一期为拆分的数据则处理
                RepaymentPlanSaveDTO lastPlan = newRepayments.get(i - 1);
                if (!lastPlan.getIrrMark()) {
                    RepaymentPlanSaveDTO lastTwoPlan = newRepayments.get(i - 2);
                    long betweenDay = DateUtil.betweenDay(thisPlan.getPlanDate(), lastTwoPlan.getPlanDate(), true);
                    if (betweenDay == 0) {
                        repayments.forEach(e -> e.setExceptionType("存在不同期数相同时间"));
                        return null;
                    }
                    BigDecimal newRentalIncome = NumberUtil.mul(thisPlan.getRentalIncome(),
                            NumberUtil.div(DateUtil.betweenDay(thisPlan.getPlanDate(), lastPlan.getPlanDate(), true),
                                    betweenDay)).setScale(2, RoundingMode.HALF_UP);
                    thisPlan.setRentalIncome(newRentalIncome);
                }
            }
        }
        return newRepayments;
    }

    /**
     * 回笼数据处理
     */
    /**
     * Persists the schedule carried by a Huaxia lease-start event and reuses the
     * existing XIRR amortized-cost engine to prepare income for every period.
     */
    @Override
    public void saveLeaseStartPlanFromInterfaceData(Map<String, Object> dataMap) {
        String sceneCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE);
        if (!SceneEnum.HTQZ.getCode().equals(sceneCode)) {
            return;
        }

        JSONObject payload = JSONObject.parseObject(JSON.toJSONString(dataMap));
        JSONArray planArray = payload.getJSONArray("repayment_plan");
        if (CollectionUtils.isEmpty(planArray)) {
            planArray = payload.getJSONArray("repaymentPlan");
        }
        if (CollectionUtils.isEmpty(planArray)) {
            throw new ServiceException("起租接口 repayment_plan 不能为空");
        }

        String contractCode = firstNotBlank(payload.getString("contractCode"), payload.getString("contract_no"));
        String orgId = firstNotBlank(payload.getString("orgId"), payload.getString("accounting_org_code"));
        String systemCode = firstNotBlank(payload.getString("systemCode"), payload.getString("source_system"));
        String clientCode = firstNotBlank(payload.getString("clientCode"), payload.getString("customer_no"));
        String clientName = firstNotBlank(payload.getString("clientName"), payload.getString("customer_name"));
        if (StringUtils.isAnyBlank(contractCode, orgId, systemCode)) {
            throw new ServiceException("起租还款计划缺少合同编号、核算主体或系统来源");
        }

        Date leaseStartDate = parseRequiredDate(payload, "lease_start_date", "businessDate", "business_date");
        BigDecimal actualDisbursement = firstDecimal(payload, "actual_disbursement", "finance_amount", "lease_principal");
        if (actualDisbursement == null || actualDisbursement.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("起租接口实际投放金额必须大于0");
        }

        BigDecimal taxRate = firstDecimal(payload, "tax_rate");
        if (taxRate == null) {
            taxRate = new BigDecimal("0.06");
        } else if (taxRate.compareTo(BigDecimal.ONE) > 0) {
            taxRate = taxRate.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP);
        }
        BigDecimal taxFactor = BigDecimal.ONE.add(taxRate);
        BigDecimal serviceFee = defaultZero(firstDecimal(payload, "service_fee"));

        List<RepaymentPlanSaveDTO> sourcePlans = new ArrayList<>();
        RepaymentPlanSaveDTO initialPlan = initRepaymentPlan(leaseStartDate);
        initialPlan.setPeriods(0);
        initialPlan.setOutflowAmount(actualDisbursement);
        initialPlan.setCashFlow(actualDisbursement.negate().add(serviceFee.divide(taxFactor, 2, RoundingMode.HALF_UP)));
        sourcePlans.add(initialPlan);

        Set<Integer> termNumbers = new HashSet<>();
        Date previousDate = leaseStartDate;
        for (int i = 0; i < planArray.size(); i++) {
            JSONObject row = planArray.getJSONObject(i);
            Integer termNo = row.getInteger("term_no");
            Date dueDate = parseRequiredDate(row, "due_date");
            BigDecimal rentAmount = defaultZero(row.getBigDecimal("rent_amount"));
            BigDecimal principalAmount = defaultZero(row.getBigDecimal("principal_amount"));
            BigDecimal interestAmount = defaultZero(row.getBigDecimal("interest_amount"));
            BigDecimal residualValue = defaultZero(row.getBigDecimal("residual_value"));
            BigDecimal otherAmount = defaultZero(row.getBigDecimal("other_amount"));
            if (termNo == null || termNo <= 0 || !termNumbers.add(termNo)) {
                throw new ServiceException("起租还款计划期次必须为不重复的正整数");
            }
            if (!dueDate.after(previousDate)) {
                throw new ServiceException("起租还款计划应还日期必须按期次递增");
            }
            if (rentAmount.signum() < 0 || principalAmount.signum() < 0 || interestAmount.signum() < 0) {
                throw new ServiceException("起租还款计划金额不能为负数");
            }

            RepaymentPlanSaveDTO plan = initRepaymentPlan(dueDate);
            plan.setPeriods(termNo);
            plan.setRentAmount(rentAmount);
            plan.setPrincipalAmount(principalAmount);
            plan.setInterestAmount(interestAmount);
            plan.setPlannedPrincipal(principalAmount);
            BigDecimal interestTax = interestAmount.multiply(taxRate).divide(taxFactor, 2, RoundingMode.HALF_UP);
            plan.setInterestTax(interestTax);
            BigDecimal plannedInterest = interestAmount.subtract(interestTax);
            plan.setPlannedInterest(plannedInterest);
            BigDecimal netResidual = residualValue.divide(taxFactor, 2, RoundingMode.HALF_UP);
            BigDecimal netOther = otherAmount.divide(taxFactor, 2, RoundingMode.HALF_UP);
            plan.setCashFlow(principalAmount.add(plannedInterest).add(netResidual).add(netOther));
            sourcePlans.add(plan);
            previousDate = dueDate;
        }

        List<Double> cashFlows = sourcePlans.stream().map(p -> p.getCashFlow().doubleValue()).collect(Collectors.toList());
        List<Date> cashFlowDates = sourcePlans.stream().map(RepaymentPlanSaveDTO::getPlanDate).collect(Collectors.toList());
        double xirr = XirrUtils.xirr(cashFlows, cashFlowDates);
        if (Double.isNaN(xirr) || Double.isInfinite(xirr) || xirr <= -1D) {
            throw new ServiceException("起租还款计划无法计算有效的XIRR，请检查投放金额、还款金额和日期");
        }

        List<RepaymentPlanSaveDTO> calculatedPlans = generateXirrRepayments(sourcePlans, xirr);
        BigDecimal totalIncome = calculatedPlans.stream().map(RepaymentPlanSaveDTO::getRentalIncome)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal incomeBefore = BigDecimal.ZERO;
        BigDecimal sourceIrr = defaultZero(firstDecimal(payload, "irr"));
        if (sourceIrr.abs().compareTo(BigDecimal.ONE) <= 0) {
            sourceIrr = sourceIrr.multiply(new BigDecimal("100"));
        }
        for (RepaymentPlanSaveDTO plan : calculatedPlans) {
            plan.setContractCode(contractCode);
            plan.setContractName(contractCode);
            plan.setOrgId(orgId);
            plan.setSystemCode(systemCode);
            plan.setClientCode(clientCode);
            plan.setClientName(clientName);
            plan.setMessageId(firstNotBlank(payload.getString("messageId"), payload.getString("event_id")));
            plan.setXirrRate(BigDecimal.valueOf(xirr).multiply(new BigDecimal("100")).setScale(6, RoundingMode.HALF_UP));
            plan.setSourceIrrRate(sourceIrr.setScale(6, RoundingMode.HALF_UP));
            plan.setUnrealizedRevenue(totalIncome);
            plan.setRentalIncomeBeforeTotal(incomeBefore);
            incomeBefore = incomeBefore.add(defaultZero(plan.getRentalIncome()));
            plan.setRentalIncomeAfterTotal(totalIncome.subtract(incomeBefore));
            plan.setRecaptureStatus(RecaptureStatusEnum.NOTRECOVERED.getCode());
            plan.setAccrued(YesOrNoEnum.YES.getCode());
            plan.setIncomeProvisionMethod(AccrualMethodEnum.XIRR.getCode());
            plan.setAmortized(YesOrNoEnum.NO.getCode());
            plan.setOnAndOffBalanceSheet(OnOrOffBalanceSheetEnum.ON.getCode());
            plan.setImpairmentThirdStage(YesOrNoEnum.NO.getCode());
            plan.setOverdueDays(0);
            plan.setPlanDatePeriod(Integer.valueOf(DateUtil.format(plan.getPlanDate(), DatePattern.SIMPLE_MONTH_PATTERN)));
            plan.setChangeAfterEndingAmortizedCost(plan.getEndingAmortizedCost());
            plan.setChangeAfterRentalIncome(plan.getRentalIncome());
            plan.setDelFlag(YesOrNoEnum.NO.getCode());
        }

        this.remove(Wrappers.<RepaymentPlanEntity>lambdaQuery()
                .eq(RepaymentPlanEntity::getContractCode, contractCode)
                .eq(RepaymentPlanEntity::getSystemCode, systemCode));
        this.saveBatch(BeanUtil.copyToList(calculatedPlans, RepaymentPlanEntity.class));
        syncHuaxiaContractForAccrual(payload, contractCode, orgId, systemCode, clientCode, clientName,
                leaseStartDate, taxRate);
        log.info("Huaxia lease-start repayment plan saved: contractCode={}, sourceRows={}, calculatedRows={}, xirr={}",
                contractCode, planArray.size(), calculatedPlans.size(), xirr);
    }

    private void syncHuaxiaContractForAccrual(JSONObject payload, String contractCode, String orgId,
                                               String systemCode, String clientCode, String clientName,
                                               Date leaseStartDate, BigDecimal taxRate) {
        Date leaseEndDate = parseOptionalDate(payload, "contract_end_date", "original_maturity_date");
        BigDecimal contractAmount = firstDecimal(payload, "contract_amount", "total_lease_receivable");
        BigDecimal annualRate = defaultZero(firstDecimal(payload, "current_interest_rate"));
        if (annualRate.abs().compareTo(BigDecimal.ONE) <= 0) {
            annualRate = annualRate.multiply(new BigDecimal("100"));
        }

        ContractEntity contract = contractService.lambdaQuery()
                .eq(ContractEntity::getContractCode, contractCode)
                .eq(ContractEntity::getOrgId, orgId)
                .orderByDesc(ContractEntity::getId).last("limit 1").one();
        if (contract == null) {
            contract = new ContractEntity();
            contract.setContractCode(contractCode);
            contract.setOrgId(orgId);
        }
        contract.setContractName(firstNotBlank(payload.getString("product_name"), contractCode));
        contract.setClientCode(clientCode);
        contract.setClientName(clientName);
        contract.setSystemCode(systemCode);
        contract.setLeaseDateStart(leaseStartDate);
        contract.setLeaseDateEnd(leaseEndDate);
        contract.setBusinessCode("ZLYW");
        contract.setBusinessName("融资租赁业务");
        contract.setBusinessPlate(payload.getString("business_line"));
        contract.setLeaseType(payload.getString("lease_category"));
        contract.setReturnType(payload.getString("lease_method"));
        contract.setContractStatus(firstNotBlank(payload.getString("contractStatus"), "正常"));
        contract.setInterestRateType(payload.getString("interest_rate_type"));
        contract.setCurrencyType(payload.getString("currency"));
        contract.setClassificationFive(firstNotBlank(payload.getString("five_class"), "正常"));
        contract.setContractAmount(contractAmount);
        contract.setLeaseInterestRateYear(annualRate);
        contract.setTaxRate(taxRate.multiply(new BigDecimal("100")));
        contract.setIncomeCalculate(YesOrNoEnum.YES.getCode());
        contract.setIncomeProvisionMethod(AccrualMethodEnum.XIRR.getCode());
        contract.setRetainedPrice(defaultZero(firstDecimal(payload, "residual_value")));
        contract.setDelFlag(YesOrNoEnum.NO.getCode());
        contractService.saveOrUpdate(contract);

        ContractMonthEntity month = contractMonthService.lambdaQuery()
                .eq(ContractMonthEntity::getContractCode, contractCode)
                .eq(ContractMonthEntity::getOrgId, orgId)
                .orderByDesc(ContractMonthEntity::getId).last("limit 1").one();
        if (month == null) {
            month = new ContractMonthEntity();
            month.setContractCode(contractCode);
            month.setOrgId(orgId);
        }
        month.setContractName(contract.getContractName());
        month.setClientCode(clientCode);
        month.setClientName(clientName);
        month.setSystemCode(systemCode);
        month.setLeaseDateStart(leaseStartDate);
        month.setLeaseDateEnd(leaseEndDate);
        month.setBusinessDate(DateUtil.toLocalDateTime(leaseStartDate));
        month.setBusinessCode("ZLYW");
        month.setBusinessName("融资租赁业务");
        month.setBusinessPlate(payload.getString("business_line"));
        month.setLeaseType(payload.getString("lease_category"));
        month.setReturnType(payload.getString("lease_method"));
        month.setContractStatus(contract.getContractStatus());
        month.setInterestRateType(contract.getInterestRateType());
        month.setCurrencyType(contract.getCurrencyType());
        month.setClassificationFive(contract.getClassificationFive());
        month.setContractAmount(contractAmount == null ? null : contractAmount.toPlainString());
        month.setLeaseInterestRateYear(annualRate.toPlainString());
        month.setTaxRate(taxRate.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString());
        month.setIncomeCalculate(YesOrNoEnum.YES.getCode());
        month.setIncomeProvisionMethod(AccrualMethodEnum.XIRR.getCode());
        month.setRetainedPrice(contract.getRetainedPrice());
        month.setDelFlag(YesOrNoEnum.NO.getCode());
        contractMonthService.saveOrUpdate(month);
    }

    private String firstNotBlank(String... values) {
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    private BigDecimal firstDecimal(JSONObject payload, String... fields) {
        for (String field : fields) {
            BigDecimal value = payload.getBigDecimal(field);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private Date parseRequiredDate(JSONObject payload, String... fields) {
        Date date = parseOptionalDate(payload, fields);
        if (date == null) {
            throw new ServiceException("起租还款计划缺少日期字段: " + String.join("/", fields));
        }
        return date;
    }

    private Date parseOptionalDate(JSONObject payload, String... fields) {
        for (String field : fields) {
            String value = payload.getString(field);
            if (StringUtils.isNotBlank(value)) {
                return DateUtil.parse(value);
            }
        }
        return null;
    }

    @Override
    @Async
    public void saveFromInterfaceData(Map<String, Object> dataMap) {
        String sceneCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE);
        // 期数
//        Integer withdrawalPeriods = MapUtil.getInt(dataMap, "withdrawalPeriods");
        //租赁收款场景累计实收租金,利息
        if (SceneEnum.ZLSK.getCode().equals(sceneCode)) {

            String contractCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE);
            String orgId = MapUtil.getStr(dataMap, RuleConstant.FIELD_ORG_ID);
            String systemCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SYSTEM_CODE);
            if (StringUtils.isEmpty(contractCode)) {
                log.error("回笼数据事件，合同编码不能为空");
                return;
            }

            if (StringUtils.isEmpty(orgId)) {
                log.error("回笼数据事件，签约主体不能为空");
                return;
            }

            ContractInterfaceTotalSaveDTO saveDTO = BeanUtil.copyProperties(dataMap, ContractInterfaceTotalSaveDTO.class);
            // 查询合同信息
            ContractDTO contractDTO = contractService.getContractDTOByCode(contractCode, orgId);
            if (contractDTO.getPaidHandlingFees() == null) {
                contractDTO.setPaidHandlingFees(BigDecimal.ZERO);
            }
            if (contractDTO.getOtherIncome() == null) {
                contractDTO.setOtherIncome(BigDecimal.ZERO);
            }

            // 更新合同的实收手续费(含税) 和 其他收入(含税)
            boolean isUpdateContract = false;
            if (saveDTO.getReceiveProcedureAmount() != null &&
                    saveDTO.getReceiveProcedureAmount().compareTo(new BigDecimal(0)) != 0) {
                contractDTO.setPaidHandlingFees(contractDTO.getPaidHandlingFees().add(saveDTO.getReceiveProcedureAmount()));
                isUpdateContract = true;
            }
            if (saveDTO.getReceiveOtherRevenues() != null &&
                    saveDTO.getReceiveProcedureAmount().compareTo(new BigDecimal(0)) != 0) {
                contractDTO.setOtherIncome(contractDTO.getOtherIncome().add(saveDTO.getReceiveOtherRevenues()));
                isUpdateContract = true;
            }
            if (isUpdateContract) {
                contractService.updateContract(contractDTO.getId(), contractDTO);
            }

            // ta金额和回款金额不会同时传递，所以如果ta金额不为空或者0，则表示该数据只为传递ta金额
            if (saveDTO.getReceiveTA() != null && saveDTO.getReceiveTA().compareTo(new BigDecimal(0)) != 0) {
                // 存储ta金额
                this.saveTaAmount(saveDTO);
                return;
            }

//            RepaymentPlanSaveDTO oldDto = this.selectByContractCodeAndPeriods(contractCode, withdrawalPeriods);
//            if (oldDto == null) {
//                //如果不存在，则不处理
//                throw new ServiceException("未找到合同["+ contractCode +"]第"+withdrawalPeriods+"期偿还计划!");
//            }
//            //如果存在，更新累计金额
//            oldDto.setActualRepaymentDate(new Date());
//            oldDto.setActualRepaymentPrincipalAmount(NumberUtil.add(oldDto.getActualRepaymentPrincipalAmount(), saveDTO.getRecyclePrincipalAmount()));
//            oldDto.setActualRepaymentInteresAmount(NumberUtil.add(oldDto.getActualRepaymentInteresAmount(), saveDTO.getRecycleInterestAmount()));
//            if (saveDTO.getReceiveServiceAmount() != null) {
//                BigDecimal receiveServiceAmountNoTax = saveDTO.getReceiveServiceAmount().divide(new BigDecimal(1.06));
//                oldDto.setServiceFeeReceived(NumberUtil.add(oldDto.getServiceFeeReceived(), receiveServiceAmountNoTax));
//            }
//            // 汇总 及 回笼判断
//            oldDto.setActualRepaymentRentAmount(NumberUtil.add(oldDto.getActualRepaymentPrincipalAmount(), oldDto.getActualRepaymentInteresAmount()));
//            if (oldDto.getActualRepaymentRentAmount().compareTo(oldDto.getRentAmount()) >= 0) {
//                oldDto.setRecaptureStatus(RecaptureStatusEnum.RETURNED.getCode());
//            } else {
//                oldDto.setRecaptureStatus(RecaptureStatusEnum.PARTIALRECOVERY.getCode());
//            }
//            //如果是ZLSK的数据需要根据本金/利息任意一个不为空时，更新归还日期、归还本金、归还利息，【ebankSerialNumber】更新偿还计划表【网银编号】，【settlementWay】
//            RepaymentPlanVO newPlanVO = BeanUtil.copyProperties(dataMap,RepaymentPlanVO.class);
//            if (null != newPlanVO.getRecyclePrincipalAmount() || null!=newPlanVO.getRecycleInterestAmount()) {
//                oldDto.setActualRepaymentDate(newPlanVO.getBusinessDate());
//                oldDto.setPrincipalAmount(newPlanVO.getRecyclePrincipalAmount());
//                oldDto.setInterestAmount(newPlanVO.getRecycleInterestAmount());
//                oldDto.setSettlementWay(newPlanVO.getSettlementWay());
//                oldDto.setEbankSerialNumber(newPlanVO.getEbankSerialNumber());
//            }
//            this.updateRepaymentPlan(oldDto.getId(), oldDto);

            // 从业务系统取得合同最新的回笼数据
            List<SelectReceiveRepaymentDTO> receivedRepaymentList = this.getNewReturnedAmount(contractDTO, systemCode);
            if (receivedRepaymentList == null || receivedRepaymentList.isEmpty()) {
                log.error("合同：" + contractCode + "未能从业务系统取得最新的回笼数据!");
                return;
            }

            // 删除原回笼数据，保存最新的回笼数据
            repaymentPlanExceldataService.saveNewReceivedRepayment(receivedRepaymentList);
            // 将回笼数据更新至偿还计划
            List<RepaymentPlanVO> repaymentPlanVOList = this.selectByContractCode(contractCode);
            List<RepaymentPlanExceldataEntity> repaymentPlanExceldataEntityList = BeanUtil.copyToList(
                    receivedRepaymentList, RepaymentPlanExceldataEntity.class);

            // 回笼总计金额计算
            BigDecimal receivedRentAmount =
                    repaymentPlanExceldataService.receivedAmountCompute(repaymentPlanExceldataEntityList, contractDTO);

            // 将回笼的数据及回笼状态更新至最新偿还计划
            BigDecimal remainReceivedMoney = receivedRentAmount;
            for (RepaymentPlanVO vo : repaymentPlanVOList) {
                if (vo.getRentAmount() == null) {
                    vo.setRentAmount(BigDecimal.ZERO);
                }

                RepaymentPlanEntity entity = BeanUtil.copyProperties(vo, RepaymentPlanEntity.class);
                if (entity.getPeriods() != null) {
                    if (remainReceivedMoney.compareTo(entity.getRentAmount()) >= 0) {
                        // 还款日期
                        entity.setActualRepaymentDate(DateUtils.getNowDate());
                        // 归还本金
                        entity.setActualRepaymentPrincipalAmount(entity.getPrincipalAmount());
                        // 归还利息
                        entity.setActualRepaymentInteresAmount(entity.getInterestAmount());
                        // 归还租金
                        entity.setActualRepaymentRentAmount(entity.getRentAmount());
                        // 回笼状态
                        entity.setRecaptureStatus(RecaptureStatusEnum.RETURNED.getCode());
                        remainReceivedMoney = remainReceivedMoney.subtract(entity.getRentAmount());
                    } else {
                        // 还款日期
                        entity.setActualRepaymentDate(DateUtils.getNowDate());
                        // 归还本金
                        entity.setActualRepaymentPrincipalAmount(remainReceivedMoney);
                        // 归还利息
                        entity.setActualRepaymentInteresAmount(BigDecimal.ZERO);
                        // 归还租金
                        entity.setActualRepaymentRentAmount(remainReceivedMoney);
                        // 回笼状态
                        if (remainReceivedMoney.compareTo(BigDecimal.ZERO) > 0) {
                            entity.setRecaptureStatus(RecaptureStatusEnum.PARTIALRECOVERY.getCode());
                        } else {
                            entity.setRecaptureStatus(RecaptureStatusEnum.NOTRECOVERED.getCode());
                        }
                    }
                    this.updateById(entity);
                }
            }

            // 如果是实收回笼，还需要更新偿还计划
            if (AccrualMethodEnum.RECEIPT.getCode().equals(contractDTO.getIncomeProvisionMethod())) {
                LeaseIncomeImport incomeImport = new LeaseIncomeImport();
                incomeImport.setBusinessDate(DateUtils.getNowDate());
                List<RepaymentPlanEntity> repaymentPlanEntities = BeanUtil.copyToList(repaymentPlanVOList, RepaymentPlanEntity.class);
                repaymentPlanEntities = this.irrTransferToReceipt(incomeImport, repaymentPlanEntities);
                this.saveOrUpdateBatch(repaymentPlanEntities);
            }
        }
    }

    /**
     * 更新回笼的状态
     */
    public void updateRecaptureStatus(RepaymentPlanExceldataEntity exceldataEntity, RepaymentPlanVO repaymentPlanVO) {
        RepaymentPlanEntity entity = BeanUtil.copyProperties(repaymentPlanVO, RepaymentPlanEntity.class);
        if (StringUtils.equals(exceldataEntity.getContractCode(), entity.getContractCode())
                && exceldataEntity.getPeriods().intValue() == entity.getPeriods().intValue()) {
            // 还款日期
            entity.setActualRepaymentDate(exceldataEntity.getPlanDate());
            // 归还本金
            entity.setActualRepaymentPrincipalAmount(exceldataEntity.getActualRepaymentPrincipalAmount());
            // 归还利息
            entity.setActualRepaymentInteresAmount(exceldataEntity.getActualRepaymentInteresAmount());
            // 归还租金
            entity.setActualRepaymentRentAmount(exceldataEntity.getActualRepaymentRentAmount());
            // 回笼状态
            if (entity.getActualRepaymentRentAmount() != null
                    && entity.getActualRepaymentRentAmount().compareTo(entity.getRentAmount()) >= 0) {
                entity.setRecaptureStatus(RecaptureStatusEnum.RETURNED.getCode());
            } else if (entity.getActualRepaymentRentAmount() != null
                    && entity.getActualRepaymentRentAmount().compareTo(BigDecimal.ZERO) > 0) {
                entity.setRecaptureStatus(RecaptureStatusEnum.PARTIALRECOVERY.getCode());
            } else {
                entity.setRecaptureStatus(RecaptureStatusEnum.NOTRECOVERED.getCode());
            }
            this.updateById(entity);
        }
    }

    /**
     * 保存ta收款
     */
    private void saveTaAmount(ContractInterfaceTotalSaveDTO saveDTO) {
        ContractTaAmountEntity entity = new ContractTaAmountEntity();
        entity.setId(IdWorker.getId());
        entity.setContractCode(saveDTO.getContractCode());
        entity.setTaAmount(saveDTO.getReceiveTA());
        entity.setTransactionDate(new Date());
        entity.setOrgId(saveDTO.getOrgId());
        contractTaAmountService.getBaseMapper().insert(entity);
    }

    @Override
    public void saveRepaymentPlanAndHisBatch(List<RepaymentPlanSaveDTO> dtos) {
        List<RepaymentPlanEntity> entity = BeanUtil.copyToList(dtos, RepaymentPlanEntity.class);
        entity.forEach(e -> e.setPlanDatePeriod(Integer.valueOf(DateUtil.format(e.getPlanDate(), DatePattern.SIMPLE_MONTH_PATTERN))));
        this.saveBatch(entity);

        List<RepaymentPlanHisEntity> hisEntities = BeanUtil.copyToList(
                dtos.stream().filter(e -> null != e.getPeriods()).collect(Collectors.toList()),
                RepaymentPlanHisEntity.class);
        hisEntities.forEach(e -> {
            e.setProcessStatus(MarginStatusEnum.PASS.getCode());
            e.setVersion(1);
        });
        hisService.saveBatch(hisEntities);
    }

    @Override
    public Long updateRepaymentPlan(Long id, RepaymentPlanDTO dto) {
        RepaymentPlanEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public void deleteGEChangeDataByCode(Date changeDate, String contractCode) {
        LambdaUpdateWrapper<RepaymentPlanEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .ge(RepaymentPlanEntity::getPlanDate, changeDate)
                .eq(RepaymentPlanEntity::getContractCode, contractCode)
                .isNull(RepaymentPlanEntity::getPeriods)
                .set(RepaymentPlanEntity::getUpdateTime, LocalDateTime.now())
                .set(RepaymentPlanEntity::getDelFlag, YesOrNoEnum.YES.getCode());
        this.update(updateChainWrapper);
    }

    @Override
    public Long updateRepaymentPlan(Long id, RepaymentPlanSaveDTO dto) {
        RepaymentPlanEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public RepaymentPlanDTO getRepaymentPlanDTOById(Long id) {
        RepaymentPlanEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, RepaymentPlanDTO.class);
    }

    @Override
    public IPage<RepaymentPlanVO> selectPage(RepaymentPlanQueryDTO queryDTO) {
        LambdaQueryWrapper<RepaymentPlanEntity> queryWrapper = Wrappers.<RepaymentPlanEntity>lambdaQuery();
        //这里注入查询条件
        IPage<RepaymentPlanEntity> entityIPage = repaymentPlanMapper.selectPage(new Page<RepaymentPlanEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, RepaymentPlanVO.class);
    }

    @Override
    public List<RepaymentPlanVO> selectByContractCode(String contractCode) {
        LambdaQueryWrapper<RepaymentPlanEntity> queryWrapper = Wrappers.<RepaymentPlanEntity>lambdaQuery();
//        queryWrapper.ne(RepaymentPlanEntity::getCashFlow, BigDecimal.ZERO);
        queryWrapper.eq(RepaymentPlanEntity::getContractCode, contractCode);
        queryWrapper.eq(RepaymentPlanEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        queryWrapper.orderByAsc(RepaymentPlanEntity::getPlanDate);
        //这里注入查询条件
        List<RepaymentPlanEntity> businessEntities = getBaseMapper().selectList(queryWrapper);
        return ListBeanUtil.copyList(businessEntities, RepaymentPlanVO.class);
    }

    @Override
    public RepaymentPlanSaveDTO selectByContractCodeAndPeriods(String contractCode, Integer periods) {
        LambdaQueryWrapper<RepaymentPlanEntity> queryWrapper = Wrappers.<RepaymentPlanEntity>lambdaQuery();
        queryWrapper.eq(RepaymentPlanEntity::getPeriods, periods);
        queryWrapper.eq(RepaymentPlanEntity::getContractCode, contractCode);
        queryWrapper.last("limit 1");
        //这里注入查询条件
        RepaymentPlanEntity businessEntities = getBaseMapper().selectOne(queryWrapper);
        return BeanUtil.copyProperties(businessEntities, RepaymentPlanSaveDTO.class);
    }

    @Override
    public List<RepaymentPlanVO> selectList(RepaymentPlanQueryDTO queryDTO) {
        LambdaQueryWrapper<RepaymentPlanEntity> queryWrapper = Wrappers.<RepaymentPlanEntity>lambdaQuery();
        //这里注入查询条件
        queryWrapper.eq(null != queryDTO.getPeriods(), RepaymentPlanEntity::getPeriods, queryDTO.getPeriods());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getContractCode()), RepaymentPlanEntity::getContractCode, queryDTO.getContractCode());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getContractCodeList()), RepaymentPlanEntity::getContractCode, queryDTO.getContractCodeList());
        queryWrapper.eq(null != queryDTO.getPlanDatePeriod(), RepaymentPlanEntity::getPlanDatePeriod, queryDTO.getPlanDatePeriod());
        queryWrapper.le(null != queryDTO.getPlanDateBeforeEq(), RepaymentPlanEntity::getPlanDate, queryDTO.getPlanDateBeforeEq());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getRecaptureStatusList()), RepaymentPlanEntity::getRecaptureStatus, queryDTO.getRecaptureStatusList());
        if (null != queryDTO.getHasPeriods()) {
            if (queryDTO.getHasPeriods()) {
                queryWrapper.isNotNull(RepaymentPlanEntity::getPeriods);
            } else {
                queryWrapper.isNull(RepaymentPlanEntity::getPeriods);
            }
        }
        queryWrapper.orderByAsc(RepaymentPlanEntity::getPlanDate);
        List<RepaymentPlanEntity> entityIPage = repaymentPlanMapper.selectList(queryWrapper);
        return ListBeanUtil.copyList(entityIPage, RepaymentPlanVO.class);
    }


    @Override
    public List<RepaymentPlanEntity> selectByContractCodeAndSystemCode(String contractCode, String systemCode) {
        if (StringUtils.isNotEmpty(systemCode) && systemCode.contains(SystemEnum.YYPT.getCode())) {
            systemCode = SystemEnum.YYPT.getCode();
        }
        LambdaQueryWrapper<RepaymentPlanEntity> queryWrapper = Wrappers.<RepaymentPlanEntity>lambdaQuery();
//        queryWrapper.ne(RepaymentPlanEntity::getCashFlow, BigDecimal.ZERO);
        queryWrapper.eq(RepaymentPlanEntity::getSystemCode, systemCode);
        queryWrapper.eq(RepaymentPlanEntity::getContractCode, contractCode);
        queryWrapper.eq(RepaymentPlanEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        queryWrapper.orderByAsc(RepaymentPlanEntity::getPlanDate);
        //这里注入查询条件
        List<RepaymentPlanEntity> businessEntities = getBaseMapper().selectList(queryWrapper);
        return businessEntities;
    }

    //-------------生成处理
    @Override
    public List<RepaymentPlanSaveDTO> handleLease(ContractDTO contractDTO, List<OfflineContractRepaymentPlanVO> repaymentPlanVOS) {
        List<RepaymentPlanSaveDTO> repaymentPlanList = BeanUtil.copyToList(repaymentPlanVOS, RepaymentPlanSaveDTO.class);
        log.info("handleLease contractDTO={},repaymentPlanVOS={}", JSON.toJSONString(contractDTO), JSON.toJSONString(repaymentPlanVOS));
        List<RepaymentPlanSaveDTO> repaymentPlanSaveDTOS = handleXirr(contractDTO, repaymentPlanList);
        return repaymentPlanSaveDTOS;
    }

    @Override
    public List<RepaymentPlanSaveDTO> handleIrr(List<RepaymentPlanSaveDTO> repaymentPlanVOS) {
        // 查询L100合同
        ContractDTO contractDTO = contractService.getContractDTOByCode("L100", "");

        BigDecimal inputTaxRatePer = new BigDecimal(13);
        BigDecimal outputTaxRatePer = new BigDecimal(6);
        BigDecimal J9 = contractDTO.getReceivableFirstAmount();//租金首付款：
        BigDecimal J11 = contractDTO.getReceivableProcedureAmount();//手续费收入(含增值税)：
        BigDecimal F9 = contractDTO.getPayableDeviceAmount();//设备价格：
        BigDecimal F10 = contractDTO.getLessorInsuranceAmount();//出租人保险费
        BigDecimal F11 = contractDTO.getChannelFees();//渠道费用：
        BigDecimal F12 = contractDTO.getLessorOtherCosts();//出租人其它成本：
        BigDecimal F13 = contractDTO.getReceivableInsuranceAmount();//承租人保险费
        BigDecimal F14 = contractDTO.getReceivableOther();//其他收入 (含增值税)：
        BigDecimal residualAmount = contractDTO.getRetainedPrice();//应收残值
//        Date startDate = DateUtil.date(contractDTO.getLeaseDateStart());
        Date startDate = DateUtil.parseDate("2022-11-14");

        // 计算期初数据 资金流出 + 收入
        BigDecimal inputTaxRate = (new BigDecimal(100).add(inputTaxRatePer)).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
        BigDecimal outputTaxRate = (new BigDecimal(100).add(outputTaxRatePer)).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
        BigDecimal outflowAmount = NumberUtil.add(F9, F10, F11).divide(inputTaxRate, 2, RoundingMode.HALF_UP).add(
                NumberUtil.add(F12, F13, F14.negate()).divide(outputTaxRate, 2, RoundingMode.HALF_UP));
        BigDecimal incomeAmount = NumberUtil.add(J9, J11, F13).divide(inputTaxRate, 2, RoundingMode.HALF_UP);
        RepaymentPlanSaveDTO firstRepayment = handleFirstRepayment(outflowAmount, incomeAmount, startDate);

        repaymentPlanVOS.forEach(e -> e.setInterestAmount(NumberUtil.sub(e.getRentAmount(), e.getPrincipalAmount()).setScale(2, RoundingMode.HALF_UP)));

        List<RepaymentPlanSaveDTO> repaymentPlanSaveDTOS = handleLease(repaymentPlanVOS, inputTaxRatePer, startDate, residualAmount, firstRepayment, AccrualMethodEnum.IRR.getCode(), null);
        repaymentPlanSaveDTOS.forEach(dto -> {
            dto.setClientCode(contractDTO.getClientCode());
            dto.setClientName(contractDTO.getClientName());
            dto.setContractCode(contractDTO.getContractCode());
            dto.setContractName(contractDTO.getContractName());
            dto.setOrgId(contractDTO.getOrgId());
            dto.setSystemCode(contractDTO.getSystemCode());
            this.saveRepaymentPlan(dto);
        });
//        this.saveRepaymentPlanAndHisBatch(repaymentPlanSaveDTOS);
        log.info("repaymentPlanSaveDTOS:{}", JSON.toJSONString(repaymentPlanSaveDTOS));
        return repaymentPlanSaveDTOS;
    }

    private List<RepaymentPlanSaveDTO> handleIrr(ContractEntity contractDTO, List<RepaymentPlanSaveDTO> repaymentPlanVOS, List<RepaymentPlanCostEntity> costs) {
        // 查询L100合同
        BigDecimal inputTaxRatePer = new BigDecimal(13);
        BigDecimal outputTaxRatePer = new BigDecimal(6);
        BigDecimal J9 = contractDTO.getReceivableFirstAmount();//租金首付款：
        BigDecimal J11 = contractDTO.getReceivableProcedureAmount();//手续费收入(含增值税)：
        BigDecimal F9 = contractDTO.getPayableDeviceAmount();//设备价格：
        BigDecimal F10 = contractDTO.getLessorInsuranceAmount();//出租人保险费
        BigDecimal F11 = contractDTO.getChannelFees();//渠道费用：
        BigDecimal F12 = contractDTO.getLessorOtherCosts();//出租人其它成本：
        BigDecimal F13 = contractDTO.getReceivableInsuranceAmount();//承租人保险费
        BigDecimal F14 = contractDTO.getReceivableOther();//其他收入 (含增值税)：
        BigDecimal residualAmount = contractDTO.getRetainedPrice();//应收残值
        Date startDate = DateUtil.date(contractDTO.getLeaseDateStart());

        // 计算期初数据 资金流出 + 收入
        BigDecimal inputTaxRate = (new BigDecimal(100).add(inputTaxRatePer)).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
        BigDecimal outputTaxRate = (new BigDecimal(100).add(outputTaxRatePer)).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
        BigDecimal outflowAmount = NumberUtil.add(F9, F10, F11).divide(inputTaxRate, 2, RoundingMode.HALF_UP).add(
                NumberUtil.add(F12, F13, F14.negate()).divide(outputTaxRate, 2, RoundingMode.HALF_UP));
        BigDecimal incomeAmount = NumberUtil.add(J9, J11, F13).divide(inputTaxRate, 2, RoundingMode.HALF_UP);
        RepaymentPlanSaveDTO firstRepayment = handleFirstRepayment(outflowAmount, incomeAmount, startDate);

        repaymentPlanVOS.forEach(e -> e.setInterestAmount(NumberUtil.sub(e.getRentAmount(), e.getPrincipalAmount()).setScale(2, RoundingMode.HALF_UP)));

        List<RepaymentPlanSaveDTO> repaymentPlanSaveDTOS = handleLease(repaymentPlanVOS, inputTaxRatePer, startDate, residualAmount, firstRepayment, AccrualMethodEnum.IRR.getCode(), costs);
        repaymentPlanSaveDTOS.forEach(dto -> {
            dto.setClientCode(contractDTO.getClientCode());
            dto.setClientName(contractDTO.getClientName());
            dto.setContractCode(contractDTO.getContractCode());
            dto.setContractName(contractDTO.getContractName());
            dto.setOrgId(contractDTO.getOrgId());
            dto.setSystemCode(contractDTO.getSystemCode());
        });
        return repaymentPlanSaveDTOS;
    }

    /**
     * 根据合同生成XIRR偿还计划
     */
    public R<String> generateXirrPaymentDataProcess(GenerateXirrPaymentDataProcessDTO params) {
        if (StringUtils.isEmpty(params.getSystemCode())) {
            return R.fail("系统编码不能为空！");
        }

        String transferSystemCode = StringUtils.EMPTY;
        if (SystemEnum.XWXT.getCode().equals(params.getSystemCode())) {
            transferSystemCode = "xw";
        } else if (SystemEnum.SYCXT.getCode().equals(params.getSystemCode())
                || SystemEnum.CYCXT.getCode().equals(params.getSystemCode())) {
            transferSystemCode = "hy";
        } else if (SystemEnum.TYPT.getCode().equals(params.getSystemCode())) {
            transferSystemCode = "pl";
        }

        for (String contractCode : params.getContractCodeList()) {
            List<RepaymentPlanEntity> repaymentPlanEntityList = repaymentService.get(transferSystemCode).
                    selectRepaymentByContract(contractCode);
            if (repaymentPlanEntityList == null || repaymentPlanEntityList.isEmpty()) {
                throw new ServiceException("合同编码:" + contractCode + "未找到偿还计划信息");
            } else {
                ContractDTO contractDTO = contractService.getContractDTOByCode(contractCode,
                        repaymentPlanEntityList.get(0).getOrgId());
                if (contractDTO == null) {
                    throw new ServiceException("合同编码:" + contractCode + "未找到合同信息");
                }
                List<RepaymentPlanSaveDTO> repaymentPlanList = BeanUtil.copyToList(repaymentPlanEntityList,
                        RepaymentPlanSaveDTO.class);
                List<RepaymentPlanSaveDTO> repaymentPlanSaveDTOList = this.handleXirr(contractDTO, repaymentPlanList);
                List<RepaymentPlanEntity> dbRepaymentPlanList = BeanUtil.copyToList(repaymentPlanSaveDTOList,
                        RepaymentPlanEntity.class);
                this.saveBatch(dbRepaymentPlanList);
            }
        }
        return R.ok();
    }

    private List<RepaymentPlanSaveDTO> handleXirr(ContractDTO contractDTO, List<RepaymentPlanSaveDTO> repaymentPlanList) {
        BigDecimal inputTaxRatePer = new BigDecimal(13);
        BigDecimal outputTaxRatePer = new BigDecimal(6);
        BigDecimal J9 = contractDTO.getReceivableFirstAmount();//租金首付款：
        BigDecimal J11 = contractDTO.getReceivableProcedureAmount();//手续费收入(含增值税)：
        BigDecimal F9 = contractDTO.getPayableDeviceAmount();//设备价格：
        BigDecimal F10 = contractDTO.getLessorInsuranceAmount();//出租人保险费
        BigDecimal F11 = contractDTO.getChannelFees();//渠道费用：
        BigDecimal F12 = contractDTO.getLessorOtherCosts();//出租人其它成本：
        BigDecimal F13 = contractDTO.getReceivableInsuranceAmount();//承租人保险费
        BigDecimal F14 = contractDTO.getReceivableOther();//其他收入 (含增值税)：
        BigDecimal residualAmount = contractDTO.getRetainedPrice();//应收残值
        Date startDate = repaymentPlanList.get(0).getPlanDate();

        // 计算期初数据 资金流出 + 收入
        BigDecimal inputTaxRate = (new BigDecimal(100).add(inputTaxRatePer)).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
        BigDecimal outputTaxRate = (new BigDecimal(100).add(outputTaxRatePer)).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
        BigDecimal outflowAmount = NumberUtil.add(F9, F10, F11).divide(inputTaxRate, 2, RoundingMode.HALF_UP).add(
                NumberUtil.add(F12, F13, NumberUtil.toBigDecimal(F14).negate()).divide(outputTaxRate, 2, RoundingMode.HALF_UP));
        BigDecimal incomeAmount = NumberUtil.add(J9, J11, F13).divide(inputTaxRate, 2, RoundingMode.HALF_UP);
        RepaymentPlanSaveDTO firstRepayment = handleFirstRepayment(outflowAmount, incomeAmount, startDate);

        List<RepaymentPlanSaveDTO> repaymentPlanSaveDTOS = handleLease(repaymentPlanList, inputTaxRatePer, startDate, residualAmount, firstRepayment, AccrualMethodEnum.XIRR.getCode(), null);
        repaymentPlanSaveDTOS.forEach(dto -> {
            dto.setClientCode(contractDTO.getClientCode());
            dto.setClientName(contractDTO.getClientName());
            dto.setContractCode(contractDTO.getContractCode());
            dto.setContractName(contractDTO.getContractName());
            dto.setOrgId(contractDTO.getOrgId());
            dto.setSystemCode(contractDTO.getSystemCode());
        });
        log.info("repaymentPlanSaveDTOS:{}", JSON.toJSONString(repaymentPlanSaveDTOS));
        return repaymentPlanSaveDTOS;
    }


    public List<RepaymentPlanSaveDTO> handleLease(List<RepaymentPlanSaveDTO> repaymentPlanList, BigDecimal inputTaxRatePer, Date startDate,
                                                  BigDecimal residualAmount, RepaymentPlanSaveDTO firstRepayment, String accrualMethod, List<RepaymentPlanCostEntity> costs) {
        // 数据不全是否生成?
        repaymentPlanList = repaymentPlanList.stream()
                .filter(e -> null != e.getPlanDate() && null != e.getRentAmount() && null != e.getPrincipalAmount())
                .sorted(Comparator.comparing(RepaymentPlanSaveDTO::getPlanDate))
                .collect(Collectors.toList());
        //1. 计算利息,本金,不含税金额
//        for (int i = 0; i < repaymentPlanList.size(); i++) {
//            RepaymentPlanSaveDTO e = repaymentPlanList.get(i);
//            BigDecimal interestAmount = e.getInterestAmount();
//            if (i == repaymentPlanList.size() - 1) {
//                // 应收残值补充
//                interestAmount = NumberUtil.add(interestAmount, residualAmount);
//            }
//            BigDecimal principalAmountTaxes = NumberUtil.div(e.getPrincipalAmount(), new BigDecimal(100).add(inputTaxRatePer)).multiply(inputTaxRatePer);
//            BigDecimal interestAmountTaxes = NumberUtil.div(interestAmount, new BigDecimal(100).add(inputTaxRatePer)).multiply(inputTaxRatePer);
//            BigDecimal principalAmountNoTax = NumberUtil.sub(e.getPrincipalAmount(), principalAmountTaxes);
//            BigDecimal interestAmountNoTax = NumberUtil.sub(interestAmount, interestAmountTaxes);
//            BigDecimal cashFlow = NumberUtil.add(principalAmountNoTax, interestAmountNoTax);
//            e.setInterestAmount(interestAmount.setScale(2, RoundingMode.HALF_UP));
//            e.setPrincipalTax(principalAmountTaxes.setScale(2, RoundingMode.HALF_UP));
//            e.setInterestTax(interestAmountTaxes.setScale(2, RoundingMode.HALF_UP));
//            e.setPlannedPrincipal(principalAmountNoTax.setScale(2, RoundingMode.HALF_UP));
//            e.setPlannedInterest(interestAmountNoTax.setScale(2, RoundingMode.HALF_UP));
//            e.setCashFlow(cashFlow.setScale(2, RoundingMode.HALF_UP));
//        }
        log.info("repaymentPlanLeaseDTOS:{}", JSON.toJSONString(repaymentPlanList));

        return getRepaymentPlanSaveDTOS(repaymentPlanList, startDate, firstRepayment, accrualMethod, costs);
    }

    private List<RepaymentPlanSaveDTO> getRepaymentPlanSaveDTOS(List<RepaymentPlanSaveDTO> repaymentPlanList, Date startDate, RepaymentPlanSaveDTO firstRepayment, String accrualMethod, List<RepaymentPlanCostEntity> costs) {
        //2.生成偿还计划测算
        List<RepaymentPlanSaveDTO> repayments = new ArrayList<>();

        // 2.1 放入期初数据
        if (null != firstRepayment) {
            repayments.add(firstRepayment);
        } else {
            firstRepayment = repaymentPlanList.get(0);
            repayments.add(repaymentPlanList.get(0));
            repaymentPlanList.remove(0);
        }

        //2.2. 计算 年化内含报酬率

        Double incomeRateDouble = Double.NaN;
        if (AccrualMethodEnum.IRR.getCode().equals(accrualMethod)) {
            Date startPlanDate = firstRepayment.getPlanDate();
            if ((DateUtil.isSameMonth(DateUtil.offsetMonth(startPlanDate, 1), repaymentPlanList.get(0).getPlanDate())
                    || DateUtil.isSameMonth(startPlanDate, repaymentPlanList.get(0).getPlanDate())) &&
                    DateUtil.isSameMonth(DateUtil.offsetMonth(repaymentPlanList.get(0).getPlanDate(), 1), repaymentPlanList.get(1).getPlanDate())) {
                // 月结合同,不再处理
                repayments.addAll(repaymentPlanList);
            } else {
                // 非月结合同,需要处理
                Iterator<RepaymentPlanSaveDTO> leaseIterator = repaymentPlanList.iterator();
                RepaymentPlanSaveDTO next = leaseIterator.next();
                splitPlanDate(startPlanDate, next, repayments, 1, leaseIterator);
            }

            if (CollectionUtils.isNotEmpty(costs)) {
                Map<Date, BigDecimal> costMap = costs.stream().collect(Collectors.toMap(e -> e.getPlanDate(), e -> e.getCashFlow()));
                repayments.forEach(e -> {
                    BigDecimal cashFlow = costMap.get(e.getPlanDate());
                    if (null != cashFlow) {
                        e.setCashFlow(NumberUtil.add(e.getCashFlow(), cashFlow));
                    }
                });
            }


            // 2.计算IRR
            incomeRateDouble = IRRUtils.calculateIRRYear(repayments.stream().map(e -> e.getCashFlow().doubleValue()).collect(Collectors.toList()));
            log.info("年化内含报酬率irrRate:{}", incomeRateDouble);
            if (Double.isNaN(incomeRateDouble)) {
                repayments.forEach(e -> e.setExceptionType("irr计算错误"));
                return repayments;
            }
            RepaymentPlanSaveDTO last = repayments.get(repayments.size() - 1);
            if (RecaptureStatusEnum.RETURNED.getCode().equals(last.getRecaptureStatus()) || DateUtil.parseDate("2023-1-1").isAfter(last.getPlanDate())) {
                Double finalIncomeRateDouble = incomeRateDouble;
                repayments.forEach(e -> {
                    e.setXirrRate(NumberUtil.mul(finalIncomeRateDouble, new BigDecimal(100)).setScale(5, RoundingMode.HALF_UP));
                    e.setIncomeProvisionMethod(accrualMethod);
                });
                return repayments;
            }
            // 3.计算当月分摊金额
            for (int i = 0; i < repayments.size(); i++) {
                RepaymentPlanSaveDTO saveDTO = repayments.get(i);
                if (i == 0) {
                    saveDTO.setOpeningAmortizedCost(saveDTO.getCashFlow().negate());
                    saveDTO.setEndingAmortizedCost(saveDTO.getOpeningAmortizedCost());
                } else {
                    RepaymentPlanSaveDTO lastSaveDTO = repayments.get(i - 1);
                    saveDTO.setOpeningAmortizedCost(lastSaveDTO.getEndingAmortizedCost());
                    BigDecimal rentalIncome = NumberUtil.mul(saveDTO.getOpeningAmortizedCost(), incomeRateDouble / 12).setScale(2, RoundingMode.HALF_UP);
                    saveDTO.setRentalIncome(rentalIncome);
                    saveDTO.setEndingAmortizedCost(NumberUtil.add(saveDTO.getOpeningAmortizedCost(), rentalIncome, saveDTO.getCashFlow().negate()));
                    if (i == repayments.size() - 1) {
                        // 最后一期差补
                        BigDecimal compensation = saveDTO.getEndingAmortizedCost().negate();
                        log.info("差补额为:{}", compensation);
                        saveDTO.setRentalIncome(NumberUtil.add(saveDTO.getRentalIncome(), compensation));
                        saveDTO.setEndingAmortizedCost(NumberUtil.add(saveDTO.getEndingAmortizedCost(), compensation));
                    }
                }
            }
            // 4.继续拆分(是月底,不拆分)
            List<RepaymentPlanSaveDTO> newRepayments = new ArrayList<>();
            for (int i = 0; i < repayments.size(); i++) {
                RepaymentPlanSaveDTO saveDTO = repayments.get(i);
                saveDTO.setIrrMark(true);
                newRepayments.add(saveDTO);
                // 最后一期不拆分
                if (i == repayments.size() - 1) {
                    continue;
                }
                // 判断是否月末,不是则拆分
                if (!DateUtil.isLastDayOfMonth(saveDTO.getPlanDate())) {
                    RepaymentPlanSaveDTO endOfMonthSaveDTO = initRepaymentPlan(DateUtil.beginOfDay(DateUtil.endOfMonth(saveDTO.getPlanDate())));
                    endOfMonthSaveDTO.setOpeningAmortizedCost(saveDTO.getEndingAmortizedCost());
                    endOfMonthSaveDTO.setEndingAmortizedCost(saveDTO.getEndingAmortizedCost());
                    endOfMonthSaveDTO.setIrrMark(false);
                    newRepayments.add(endOfMonthSaveDTO);
                }
            }
            // 拆分分摊金额
            for (int i = 1; i < newRepayments.size(); i++) {
                RepaymentPlanSaveDTO thisPlan = newRepayments.get(i);
//                log.info("thisplan" + JSON.toJSONString(thisPlan));
                if (!thisPlan.getIrrMark()) {
                    // 拆分的数据
                    RepaymentPlanSaveDTO lastPlan = newRepayments.get(i - 1);
                    RepaymentPlanSaveDTO nextPlan = newRepayments.get(i + 1);
                    long betweenDay = DateUtil.betweenDay(nextPlan.getPlanDate(), lastPlan.getPlanDate(), true);
                    if (betweenDay == 0) {
                        repayments.forEach(e -> e.setExceptionType("存在不同期数相同时间"));
                        return repayments;
                    }
                    BigDecimal newRentalIncome = NumberUtil.mul(nextPlan.getRentalIncome(),
                            NumberUtil.div(DateUtil.betweenDay(thisPlan.getPlanDate(), lastPlan.getPlanDate(), true),
                                    betweenDay)).setScale(2, RoundingMode.HALF_UP);
                    thisPlan.setRentalIncome(newRentalIncome);
                } else {
                    // 非拆分的数据 上一期为非拆分的数据，则不处理，上一期为拆分的数据则处理
                    RepaymentPlanSaveDTO lastPlan = newRepayments.get(i - 1);
                    if (!lastPlan.getIrrMark()) {
                        RepaymentPlanSaveDTO lastTwoPlan = newRepayments.get(i - 2);
                        long betweenDay = DateUtil.betweenDay(thisPlan.getPlanDate(), lastTwoPlan.getPlanDate(), true);
                        if (betweenDay == 0) {
                            repayments.forEach(e -> e.setExceptionType("存在不同期数相同时间"));
                            return repayments;
                        }
                        BigDecimal newRentalIncome = NumberUtil.mul(thisPlan.getRentalIncome(),
                                NumberUtil.div(DateUtil.betweenDay(thisPlan.getPlanDate(), lastPlan.getPlanDate(), true),
                                        betweenDay)).setScale(2, RoundingMode.HALF_UP);
                        thisPlan.setRentalIncome(newRentalIncome);
                    }
                }
            }
            repayments = new ArrayList<>(newRepayments);
        } else {
            List<Date> allPlanDate = repaymentPlanList.stream().map(RepaymentPlanSaveDTO::getPlanDate).collect(Collectors.toList());
            List<Double> cashFlowList = repaymentPlanList.stream().map(e -> NumberUtil.toDouble(e.getCashFlow())).collect(Collectors.toList());
//            allPlanDate.add(startDate);
//            cashFlowList.add(repayments.get(0).getCashFlow().doubleValue());
            incomeRateDouble = XirrUtils.xirr(cashFlowList, allPlanDate);
            log.info("年化内含报酬率xirrRate:{}", incomeRateDouble);
            if (Double.isNaN(incomeRateDouble) || incomeRateDouble < 0) {
                repayments.addAll(repaymentPlanList);
                repayments.forEach(e -> e.setExceptionType("xirr计算错误"));
                return repayments;
            }
            // 2.3 后续数据递归生成
            Iterator<RepaymentPlanSaveDTO> leaseIterator = repaymentPlanList.iterator();
            RepaymentPlanSaveDTO next = leaseIterator.next();
            generateRepayments(startDate, next.getPlanDate(), repayments, allPlanDate, leaseIterator, incomeRateDouble, next);
        }
        // 3 数据处理
        BigDecimal totalUnrealizedRevenue = repayments.stream().map(e -> e.getRentalIncome()).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal rentalIncomeBeforeTotal = BigDecimal.ZERO;
        BigDecimal rentalIncomeAfterTotal = totalUnrealizedRevenue;

        for (int i = 0; i < repayments.size(); i++) {
            RepaymentPlanSaveDTO saveDTO = repayments.get(i);
            saveDTO.setXirrRate(NumberUtil.mul(incomeRateDouble, new BigDecimal(100)).setScale(5, RoundingMode.HALF_UP));
            saveDTO.setUnrealizedRevenue(totalUnrealizedRevenue);
            saveDTO.setRentalIncomeBeforeTotal(rentalIncomeBeforeTotal);
            rentalIncomeBeforeTotal = NumberUtil.add(rentalIncomeBeforeTotal, saveDTO.getRentalIncome());
            rentalIncomeAfterTotal = NumberUtil.sub(rentalIncomeAfterTotal, saveDTO.getRentalIncome());
            saveDTO.setRentalIncomeAfterTotal(rentalIncomeAfterTotal);
            saveDTO.setAccrued(YesOrNoEnum.YES.getCode());
            saveDTO.setIncomeProvisionMethod(accrualMethod);
        }
        return repayments;
    }

    private void splitPlanDate(Date startDate, RepaymentPlanSaveDTO plan, List<RepaymentPlanSaveDTO> repayments, int offsetMonth, Iterator<RepaymentPlanSaveDTO> leaseIterator) {
        Date planDate = plan.getPlanDate();
        DateTime compareDate = DateUtil.offsetMonth(startDate, offsetMonth);
        if (DateUtil.isSameMonth(compareDate, planDate)) {
            // 新增planDate数据
            repayments.add(plan);
            // 是否还有下一期
            if (leaseIterator.hasNext()) {
                RepaymentPlanSaveDTO next = leaseIterator.next();
                offsetMonth = offsetMonth + 1;
                splitPlanDate(startDate, next, repayments, offsetMonth, leaseIterator);
            }
        } else {
            // 新增compareDate数据
            RepaymentPlanSaveDTO saveDTO = initRepaymentPlan(compareDate);
            repayments.add(saveDTO);
            offsetMonth = offsetMonth + 1;
            splitPlanDate(startDate, plan, repayments, offsetMonth, leaseIterator);
        }
    }

    /**
     * XIRR的偿还计划计算
     */
    public List<RepaymentPlanSaveDTO> generateXirrRepayments(List<RepaymentPlanSaveDTO> repayments, Double xirrRate) {
        List<RepaymentPlanSaveDTO> result = new ArrayList<>();
        String contractCode = repayments.get(0).getContractCode();
        for (int i = 0; i < repayments.size(); i++) {
            RepaymentPlanSaveDTO dto = BeanUtil.copyProperties(repayments.get(i), RepaymentPlanSaveDTO.class);
            if (i == 0) {
                dto.setOpeningAmortizedCost(dto.getCashFlow().negate());
                dto.setEndingAmortizedCost(dto.getCashFlow().negate());
                dto.setRentalIncome(BigDecimal.ZERO);
                result.add(dto);
                continue;
            }

            // 判断上一条偿还计划记录是否为月底最后一天，不是则需要补充月底记录
            RepaymentPlanSaveDTO lastDto = result.get(result.size() - 1);
            if (!DateUtil.isLastDayOfMonth(lastDto.getPlanDate()) && !DateUtil.isSameMonth(lastDto.getPlanDate(), dto.getPlanDate())) {
                // 月末节点只表达核算日期，不应携带 23:59:59。JVM 与数据库时区不一致时，
                // 月末最后一秒会被转换成次月 1 日，因此统一归一为月末当天 00:00:00。
                DateTime endDayOfMonth = DateUtil.beginOfDay(DateUtil.endOfMonth(lastDto.getPlanDate()));

                // 非月底的记录时，需要新增一条月底的偿还计划
                dto = this.initRepaymentPlan(endDayOfMonth);
                double betweenDay = this.getBetweenDay(lastDto.getPlanDate(), endDayOfMonth);
                double dailyRateDou = Math.pow(xirrRate + 1, betweenDay / 365) - 1;
                log.info("合同=" + contractCode + ";dailyRateDou=" + dailyRateDou + ";xirrRate=" + xirrRate + ";betweenDay=" + betweenDay);
                dto.setActualDailyRate(NumberUtil.mul(dailyRateDou, new BigDecimal(100)).setScale(5, RoundingMode.HALF_UP));

                dto.setOpeningAmortizedCost(lastDto.getEndingAmortizedCost());
                dto.setRentalIncome(NumberUtil.mul(dto.getOpeningAmortizedCost(), dailyRateDou).setScale(2, RoundingMode.HALF_UP));
                dto.setEndingAmortizedCost(NumberUtil.add(dto.getOpeningAmortizedCost(), dto.getRentalIncome(), dto.getCashFlow().negate()));
                result.add(dto);
            }

            // 计划当前记录的偿还计划
            lastDto = result.get(result.size() - 1);
            dto = BeanUtil.copyProperties(repayments.get(i), RepaymentPlanSaveDTO.class);
            double betweenDay = this.getBetweenDay(lastDto.getPlanDate(), dto.getPlanDate());
            double dailyRateDou = Math.pow(xirrRate + 1, betweenDay / 365) - 1;
//            log.info("合同="+dto.getContractCode()+";dailyRateDou=" + dailyRateDou);
            dto.setActualDailyRate(NumberUtil.mul(dailyRateDou, new BigDecimal(100)).setScale(5, RoundingMode.HALF_UP));

            dto.setOpeningAmortizedCost(lastDto.getEndingAmortizedCost());
            dto.setRentalIncome(NumberUtil.mul(dto.getOpeningAmortizedCost(), dailyRateDou).setScale(2, RoundingMode.HALF_UP));
            dto.setEndingAmortizedCost(NumberUtil.add(dto.getOpeningAmortizedCost(), dto.getRentalIncome(), dto.getCashFlow().negate()));

            // 如果是最后一条，则需要轧差
            if (i == repayments.size() - 1) {
                BigDecimal compensation = dto.getEndingAmortizedCost().negate();
                log.info("差补额为:{}", compensation);
                dto.setRentalIncome(NumberUtil.add(dto.getRentalIncome(), compensation));
                dto.setEndingAmortizedCost(NumberUtil.add(dto.getEndingAmortizedCost(), compensation));
            }
            result.add(dto);
        }
        return result;
    }

    @Override
    public RepaymentPlanVO getPlanByMaxActualRepaymentDate(Date replaymentDate, String contractCode) {
        return repaymentPlanMapper.getPlanByMaxActualRepaymentDate(replaymentDate, contractCode);
    }

    private double getBetweenDay(Date startDate, Date endDate) {
        if (DateUtil.isSameMonth(startDate, endDate)) {
            return DateUtil.betweenDay(startDate, endDate, true) + 1;
        } else {
            if (DateUtil.isLastDayOfMonth(startDate) && DateUtil.isLastDayOfMonth(endDate)) {
                return DateUtil.betweenDay(startDate, endDate, true);
            } else {
                return DateUtil.betweenDay(startDate, endDate, true) - 1;
            }
        }
    }

    public void generateRepayments(Date dateFrom, Date dateUntil, List<RepaymentPlanSaveDTO> repayments,
                                   List<Date> allPlanDate, Iterator<RepaymentPlanSaveDTO> iterator, Double xirrRate, RepaymentPlanSaveDTO lease) {
        // 开始时间
        DateTime startDate = DateUtil.date(dateFrom);
        boolean lastDayOfMonth = DateUtil.isLastDayOfMonth(startDate);
        // 结束时间
        DateTime endDate = lastDayOfMonth ? DateUtil.endOfMonth(DateUtil.offsetMonth(startDate, 1)) :  //下个月月末
                DateUtil.offsetDay(DateUtil.beginOfMonth(DateUtil.offsetMonth(startDate, 1)), -1); //开始时间当月末
        if (endDate.isBefore(dateUntil)) {
            long betweenDay = DateUtil.betweenDay(startDate, endDate, true);
            if (allPlanDate.contains(startDate)) {
                betweenDay = betweenDay + 1;
            }
            // 生成偿还计划
            addRepayments(repayments, xirrRate, endDate, null, betweenDay, true);
            // 下一个
            generateRepayments(endDate, dateUntil, repayments, allPlanDate, iterator, xirrRate, lease);
        } else {
            boolean hasNext = iterator.hasNext();
            if (!DateUtil.isSameDay(endDate, dateUntil)) {
                endDate = DateUtil.date(dateUntil);
            }
            // 生成偿还计划
            long betweenDay = DateUtil.betweenDay(startDate, endDate, true) - 1;
            addRepayments(repayments, xirrRate, endDate, lease, betweenDay, hasNext);
            // 是否有 下一个还款日
            if (hasNext) {
                RepaymentPlanSaveDTO next = iterator.next();
                generateRepayments(endDate, next.getPlanDate(), repayments, allPlanDate, iterator, xirrRate, next);
            }
        }
    }

    private void addRepayments(List<RepaymentPlanSaveDTO> repayments, Double xirrRate, DateTime endDate, RepaymentPlanSaveDTO lease, double betweenDay, boolean hasNext) {
        RepaymentPlanSaveDTO last = repayments.get(repayments.size() - 1);
        RepaymentPlanSaveDTO repayment = initRepaymentPlan(endDate);
        if (null != lease) {
            BeanUtil.copyProperties(lease, repayment, CopyOptions.create().setIgnoreNullValue(true));
        }
        repayment.setOpeningAmortizedCost(last.getEndingAmortizedCost());
        double dailyRateDou = Math.pow(xirrRate + 1, betweenDay / 365) - 1;
        repayment.setRentalIncome(NumberUtil.mul(repayment.getOpeningAmortizedCost(), dailyRateDou).setScale(2, RoundingMode.HALF_UP));
        repayment.setEndingAmortizedCost(NumberUtil.add(repayment.getOpeningAmortizedCost(), repayment.getRentalIncome(), repayment.getCashFlow().negate()));
        repayment.setActualDailyRate(NumberUtil.mul(dailyRateDou, new BigDecimal(100)).setScale(5, RoundingMode.HALF_UP));
        if (!hasNext) {
            // 差补 todo 用总额轧差
            BigDecimal compensation = repayment.getEndingAmortizedCost().negate();
            log.info("差补额为:{}", compensation);
            repayment.setRentalIncome(NumberUtil.add(repayment.getRentalIncome(), compensation));
            repayment.setEndingAmortizedCost(NumberUtil.add(repayment.getEndingAmortizedCost(), compensation));
        }
        repayments.add(repayment);
    }

    private RepaymentPlanSaveDTO handleFirstRepayment(BigDecimal firstOutflowAmount, BigDecimal firstIncomeAmount, Date startDate) {
        RepaymentPlanSaveDTO firstRepayment = initRepaymentPlan(startDate);
        firstRepayment.setOutflowAmount(firstOutflowAmount);
        firstRepayment.setPlannedInterest(firstIncomeAmount);
        firstRepayment.setCashFlow(NumberUtil.add(firstRepayment.getPlannedInterest(), firstRepayment.getPrincipalAmount(), firstRepayment.getOutflowAmount().negate()));
        firstRepayment.setOpeningAmortizedCost(firstRepayment.getCashFlow().negate());
        firstRepayment.setEndingAmortizedCost(firstRepayment.getOpeningAmortizedCost());
        return firstRepayment;
    }

    private RepaymentPlanSaveDTO handleFirstRepayment(BigDecimal j9, BigDecimal j11, BigDecimal f9, BigDecimal f10, BigDecimal f11, BigDecimal f12, BigDecimal f13, BigDecimal f14, Date startDate, BigDecimal inputTaxRate, BigDecimal outputTaxRate) {
        BigDecimal firstOutflowAmount = BigDecimal.ZERO;
        BigDecimal firstIncomeAmount = BigDecimal.ZERO;
        firstOutflowAmount = NumberUtil.add(f9, f10, f11).divide(inputTaxRate, 2, RoundingMode.HALF_UP).add(
                NumberUtil.add(f12, f13, f14.negate()).divide(outputTaxRate, 2, RoundingMode.HALF_UP)
        );
        firstIncomeAmount = NumberUtil.add(j9, j11, f13).divide(inputTaxRate, 2, RoundingMode.HALF_UP);// 文档中的监管费没看到
        RepaymentPlanSaveDTO firstRepayment = initRepaymentPlan(startDate);
        firstRepayment.setOutflowAmount(firstOutflowAmount);
        firstRepayment.setPlannedInterest(firstIncomeAmount);
        firstRepayment.setCashFlow(NumberUtil.add(firstRepayment.getPlannedInterest(), firstRepayment.getPrincipalAmount(), firstRepayment.getOutflowAmount().negate()));
        firstRepayment.setOpeningAmortizedCost(firstRepayment.getCashFlow().negate());
        firstRepayment.setEndingAmortizedCost(firstRepayment.getOpeningAmortizedCost());
        return firstRepayment;
    }

    private RepaymentPlanSaveDTO initRepaymentPlan(Date planDate) {
        RepaymentPlanSaveDTO dto = new RepaymentPlanSaveDTO();
        dto.setPlanDate(planDate);
        dto.setRentAmount(BigDecimal.ZERO);
        dto.setPrincipalAmount(BigDecimal.ZERO);
        dto.setInterestAmount(BigDecimal.ZERO);
        dto.setPrincipalTax(BigDecimal.ZERO);
        dto.setInterestTax(BigDecimal.ZERO);
        dto.setOutflowAmount(BigDecimal.ZERO);
        dto.setPlannedInterest(BigDecimal.ZERO);
        dto.setPlannedPrincipal(BigDecimal.ZERO);
        dto.setCashFlow(BigDecimal.ZERO);
        dto.setOpeningAmortizedCost(BigDecimal.ZERO);
        dto.setEndingAmortizedCost(BigDecimal.ZERO);
        dto.setActualDailyRate(BigDecimal.ZERO);
        dto.setRentalIncome(BigDecimal.ZERO);
        dto.setServiceFeeAmortizationIncome(BigDecimal.ZERO);
        return dto;
    }


    //----------------用例 按变更时间 打折的变更逻辑
    public List<RepaymentPlanSaveDTO> oldHandleChange(Date changeDate, BigDecimal discountRate, String contractCode, BigDecimal inputTaxRatePer) {
        //1.select by contractCode
        List<RepaymentPlanVO> repaymentPlanVOS = selectByContractCode(contractCode);
        //获取有现金流的计划用于计算新回报率
        List<RepaymentPlanVO> hasCashFlowPlan = repaymentPlanVOS.stream().filter(e -> null != e.getCashFlow() && BigDecimal.ZERO.compareTo(e.getCashFlow()) != 0).collect(Collectors.toList());
        //2. 计算打折后价格+折算金额
        double totalRate = 1;
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;
        RepaymentPlanSaveDTO repaymentPlanChangeDate = null;
        List<RepaymentPlanSaveDTO> afterChangePlan = new ArrayList<>();
        for (int i = 0; i < hasCashFlowPlan.size(); i++) {
            RepaymentPlanVO repaymentPlan = hasCashFlowPlan.get(i);
            if (i != 0) {
                RepaymentPlanVO lastPlan = hasCashFlowPlan.get(i - 1);
                double rate = 1 + NumberUtil.div(repaymentPlan.getXirrRate().doubleValue(), 100);
                rate = Math.pow(rate, (double) DateUtil.betweenDay(repaymentPlan.getPlanDate(), lastPlan.getPlanDate(), true) / 365);
                // 计算当前的折扣率
                totalRate = NumberUtil.mul(totalRate, rate);
                log.info("当前日期:{},折扣率:{}", DateUtil.formatDate(repaymentPlan.getPlanDate()), totalRate);
            }
            //判断日期,在changeDate之后的都打折,并且累计金额
            if (changeDate.before(repaymentPlan.getPlanDate())) {
                BigDecimal oldCashFlow = repaymentPlan.getCashFlow();
                BigDecimal newCashFlow = NumberUtil.mul(oldCashFlow, discountRate).setScale(2, RoundingMode.HALF_UP);
                repaymentPlan.setCashFlow(newCashFlow);
                // 处理其他原始数据
                repaymentPlan.setRentAmount(NumberUtil.mul(repaymentPlan.getRentAmount(), discountRate).setScale(2, RoundingMode.HALF_UP));
                repaymentPlan.setPrincipalAmount(NumberUtil.mul(repaymentPlan.getPrincipalAmount(), discountRate).setScale(2, RoundingMode.HALF_UP));
                repaymentPlan.setInterestAmount(NumberUtil.mul(repaymentPlan.getInterestAmount(), discountRate).setScale(2, RoundingMode.HALF_UP));
                BigDecimal principalAmountTaxes = NumberUtil.div(repaymentPlan.getPrincipalAmount(), new BigDecimal(100).add(inputTaxRatePer)).multiply(inputTaxRatePer);
                BigDecimal interestAmountTaxes = NumberUtil.div(repaymentPlan.getInterestAmount(), new BigDecimal(100).add(inputTaxRatePer)).multiply(inputTaxRatePer);
                BigDecimal principalAmountNoTax = NumberUtil.sub(repaymentPlan.getPrincipalAmount(), principalAmountTaxes);
                BigDecimal interestAmountNoTax = NumberUtil.sub(repaymentPlan.getInterestAmount(), interestAmountTaxes);
                BigDecimal cashFlow = NumberUtil.add(principalAmountNoTax, interestAmountNoTax);
                repaymentPlan.setPrincipalTax(principalAmountTaxes.setScale(2, RoundingMode.HALF_UP));
                repaymentPlan.setInterestTax(interestAmountTaxes.setScale(2, RoundingMode.HALF_UP));
                repaymentPlan.setPlannedPrincipal(principalAmountNoTax.setScale(2, RoundingMode.HALF_UP));
                repaymentPlan.setPlannedInterest(interestAmountNoTax.setScale(2, RoundingMode.HALF_UP));
                repaymentPlan.setCashFlow(cashFlow.setScale(2, RoundingMode.HALF_UP));


                BigDecimal subtract = oldCashFlow.subtract(newCashFlow);
                BigDecimal discountAmount = NumberUtil.div(subtract, totalRate);
                log.info("DiscountAmount 差额:{}", discountAmount);
                totalDiscountAmount = NumberUtil.add(totalDiscountAmount, discountAmount);
                afterChangePlan.add(repaymentPlan);
            } else if (DateUtil.isSameDay(changeDate, repaymentPlan.getPlanDate())) {
                repaymentPlanChangeDate = repaymentPlan;
            }
        }
        log.info("totalDiscountAmount 差额:{}", totalDiscountAmount);
        // 没有变更，直接返回
        if (totalDiscountAmount.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        // 变更日前所有计划
        List<RepaymentPlanVO> beforeChangePlan = repaymentPlanVOS.stream().filter(e -> changeDate.after(e.getPlanDate())).collect(Collectors.toList());

        // 3.生成第一天金额
        if (null == repaymentPlanChangeDate) {
            repaymentPlanChangeDate = initRepaymentPlan(changeDate);
            RepaymentPlanVO lastBeforeChange = beforeChangePlan.get(beforeChangePlan.size() - 1);
            repaymentPlanChangeDate.setOpeningAmortizedCost(lastBeforeChange.getEndingAmortizedCost());
            repaymentPlanChangeDate.setXirrRate(lastBeforeChange.getXirrRate());
            double dailyRateDou = Math.pow(lastBeforeChange.getXirrRate().doubleValue() / 100 + 1, (double) DateUtil.betweenDay(changeDate, lastBeforeChange.getPlanDate(), true) / 365) - 1;
            repaymentPlanChangeDate.setRentalIncome(NumberUtil.mul(repaymentPlanChangeDate.getOpeningAmortizedCost(), dailyRateDou).setScale(2, RoundingMode.HALF_UP));
            repaymentPlanChangeDate.setEndingAmortizedCost(NumberUtil.add(repaymentPlanChangeDate.getOpeningAmortizedCost(), repaymentPlanChangeDate.getRentalIncome(), repaymentPlanChangeDate.getCashFlow().negate(), totalDiscountAmount.negate()).setScale(2, RoundingMode.HALF_UP));
            repaymentPlanChangeDate.setActualDailyRate(NumberUtil.mul(dailyRateDou, new BigDecimal(100)).setScale(5, RoundingMode.HALF_UP));
        } else {
            repaymentPlanChangeDate.setEndingAmortizedCost(NumberUtil.add(repaymentPlanChangeDate.getEndingAmortizedCost(), totalDiscountAmount.negate()).setScale(2, RoundingMode.HALF_UP));
        }

        // 4.计算新回报率
        List<Date> changeDates = afterChangePlan.stream().map(RepaymentPlanSaveDTO::getPlanDate).collect(Collectors.toList());
        List<Double> changeCashFlows = afterChangePlan.stream().map(e -> e.getCashFlow().doubleValue()).collect(Collectors.toList());
        changeDates.add(changeDate);
        changeCashFlows.add(repaymentPlanChangeDate.getEndingAmortizedCost().negate().doubleValue());
        Double newXirr = XirrUtils.xirr(changeCashFlows, changeDates);
        log.info("newXirr:{}", newXirr);

        List<RepaymentPlanSaveDTO> saveRepayments = new ArrayList<>();
        saveRepayments.add(repaymentPlanChangeDate);
        Iterator<RepaymentPlanSaveDTO> leaseIterator = afterChangePlan.iterator();
        RepaymentPlanSaveDTO next = leaseIterator.next();
        // 5.生成新计划
        generateRepayments(changeDate, next.getPlanDate(), saveRepayments, changeDates, leaseIterator, newXirr, next);
        // 6.处理数据
        saveRepayments.forEach(e -> {
            if (null == e.getXirrRate()) {
                e.setXirrRate(NumberUtil.mul(newXirr, new BigDecimal(100)).setScale(5, RoundingMode.HALF_UP));
            }
        });
        log.info("beforeChangePlan:{}", JSON.toJSONString(beforeChangePlan));
        log.info("saveRepayments:{}", JSON.toJSONString(saveRepayments));
        // 7.删除changeDate之后的,新增changedate之后的
        LambdaUpdateWrapper<RepaymentPlanEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .ge(RepaymentPlanEntity::getPlanDate, changeDate)
                .eq(RepaymentPlanEntity::getContractCode, contractCode)
                .set(RepaymentPlanEntity::getUpdateTime, LocalDateTime.now())
                .set(RepaymentPlanEntity::getDelFlag, YesOrNoEnum.YES.getCode());
        this.update(updateChainWrapper);
        List<RepaymentPlanEntity> afterRepaymentPlanEntities = BeanUtil.copyToList(saveRepayments, RepaymentPlanEntity.class);
        this.saveBatch(afterRepaymentPlanEntities);

        return saveRepayments;
    }

    @Override
    public void importDataXWXT(List<ImportRepaymentPlanXWXTExcel> list, String opt) {
        Map<String, List<ImportRepaymentPlanXWXTExcel>> collect = list.stream().sorted(Comparator.comparing(ImportRepaymentPlanXWXTExcel::getPlanDate)
                        .thenComparing(ImportRepaymentPlanXWXTExcel::getCashFlow))
                .collect(Collectors.groupingBy(e -> e.getContractCode() + e.getClientCode() + e.getOrgId()));

        List<RepaymentPlanEntity> all = new ArrayList<>();

        for (Map.Entry<String, List<ImportRepaymentPlanXWXTExcel>> entry : collect.entrySet()) {
            String key = entry.getKey();
            long startTime = System.currentTimeMillis();
            log.info("start " + key);
            List<RepaymentPlanEntity> entity = getRepaymentPlanEntities(entry.getValue(), opt);
            all.addAll(entity);
            long time1 = System.currentTimeMillis();
            log.info("end " + key + "代码执行时间" + (time1 - startTime));
        }
        long startTimesave = System.currentTimeMillis();
        this.saveBatch(all);
        log.info("保存时间" + (System.currentTimeMillis() - startTimesave));


    }

    @Override
    public void importDataXWXTAsync(List<ImportRepaymentPlanXWXTExcel> list, String opt) {
        Map<String, List<ImportRepaymentPlanXWXTExcel>> collect = list.stream().sorted(Comparator.comparing(ImportRepaymentPlanXWXTExcel::getPlanDate)
                        .thenComparing(ImportRepaymentPlanXWXTExcel::getCashFlow))
                .collect(Collectors.groupingBy(e -> e.getContractCode() + e.getClientCode() + e.getOrgId()));

        long startDate = System.currentTimeMillis();
        log.info("小微系统偿还计划期初数据导入开始时间：{}", startDate);

        List<CompletableFuture<List<RepaymentPlanEntity>>> futureList = new ArrayList<>();
        for (Map.Entry<String, List<ImportRepaymentPlanXWXTExcel>> entry : collect.entrySet()) {
            CompletableFuture<List<RepaymentPlanEntity>> future = CompletableFuture.supplyAsync(() -> {
                return getRepaymentPlanEntities(entry.getValue(), opt);
            }, asyncTaskExecutor);
            try {
                log.info("返回值：{},dd:{}", JSONObject.toJSONString(future), JSONObject.toJSONString(future.get()));
            } catch (Exception e) {
                throw new ServiceException(entry.getKey() + "批量生成凭证失败，失败原因:" + e.getMessage());
            }
            futureList.add(future);
        }
        List<RepaymentPlanEntity> all = new ArrayList<>();
        for (CompletableFuture<List<RepaymentPlanEntity>> resultFuture : futureList) {
            if (resultFuture.isDone() && !resultFuture.isCancelled()) {
                try {
                    all.addAll(resultFuture.get());
                } catch (InterruptedException e) {
                    throw new ServiceException("小微系统偿还计划期初数据导入失败，失败原因:" + e.getMessage());
                } catch (ExecutionException exception) {
                    throw new ServiceException("小微系统偿还计划期初数据导入失败，失败原因:" + exception.getMessage());
                }
            }
        }

        long endDate = System.currentTimeMillis();
        log.info("小微系统偿还计划期初数据导入结束时间：{},共花费时间：{}", endDate, endDate - startDate);


    }

    @Override
    public void importDataHY(List<ImportRepaymentPlanHYExcel> list, String systemCode) {
        Map<String, List<ImportRepaymentPlanHYExcel>> collect = list.stream().sorted(Comparator.comparing(ImportRepaymentPlanHYExcel::getPlanDate)
                        .thenComparing(ImportRepaymentPlanHYExcel::getRentAmount))
                .collect(Collectors.groupingBy(e -> e.getContractCode() + e.getClientCode() + e.getOrgId()));

        List<RepaymentPlanEntity> all = new ArrayList<>();

        for (Map.Entry<String, List<ImportRepaymentPlanHYExcel>> entry : collect.entrySet()) {
            String key = entry.getKey();
            log.info("start " + key);
            List<ImportRepaymentPlanHYExcel> excels = entry.getValue();
            long startTime = System.currentTimeMillis();
            Integer index = 1;

            // 处理
            List<RepaymentPlanSaveDTO> excelPlan = new ArrayList<>();
            for (int i = 0; i < excels.size(); i++) {
                ImportRepaymentPlanHYExcel excel = excels.get(i);
                excel.setRecaptureStatus(RecaptureStatusEnum.getCodeByDescHY(excel.getRecaptureStatus()));
                RepaymentPlanSaveDTO dto = initRepaymentPlan(excel.getPlanDate());
                BeanUtil.copyProperties(excel, dto);
                BigDecimal rentAmount = dto.getRentAmount();
                BigDecimal principalAmount = dto.getPrincipalAmount();
                if (i == 0 && rentAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    if (null == principalAmount || BigDecimal.ZERO.compareTo(principalAmount) == 0) {
                        dto.setPeriods(null);
                        dto.setCashFlow(dto.getRentAmount());
                        dto.setRentAmount(BigDecimal.ZERO);
                    } else {
                        dto.setCashFlow(dto.getRentAmount());
                        dto.setPeriods(index);
                        dto.setRentAmount(NumberUtil.add(principalAmount, dto.getInterestAmount()));
                        index++;
                    }
                } else {
                    dto.setPeriods(index);
                    dto.setCashFlow(dto.getRentAmount());
                    index++;
                }
                excelPlan.add(dto);
            }

            RepaymentPlanSaveDTO saveDTO = excelPlan.get(0);
            List<RepaymentPlanSaveDTO> repaymentPlanSaveDTOS = null;
            if (saveDTO.getCashFlow().compareTo(BigDecimal.ZERO) == 0) {
                repaymentPlanSaveDTOS = excelPlan;
                repaymentPlanSaveDTOS.forEach(e -> e.setExceptionType("无现金流数据"));
            } else {

                repaymentPlanSaveDTOS = getRepaymentPlanSaveDTOS(excelPlan,
                        saveDTO.getPlanDate(), null, AccrualMethodEnum.IRR.getCode(), null);
            }

            List<RepaymentPlanEntity> entity = BeanUtil.copyToList(repaymentPlanSaveDTOS, RepaymentPlanEntity.class);
            entity.forEach(e -> {
                e.setContractCode(saveDTO.getContractCode());
                e.setOrgId(saveDTO.getOrgId());
                e.setClientCode(saveDTO.getClientCode());
                e.setClientName(saveDTO.getClientName());
                e.setMessageId(systemCode);
                e.setSystemCode(systemCode);
                e.setPlanDatePeriod(Integer.valueOf(DateUtil.format(e.getPlanDate(), DatePattern.SIMPLE_MONTH_PATTERN)));
            });
            log.info(saveDTO.getContractCode() + "处理时间" + (System.currentTimeMillis() - startTime));

            all.addAll(entity);
        }
        long startTimesave = System.currentTimeMillis();
        this.saveBatch(all);
        log.info("保存时间" + (System.currentTimeMillis() - startTimesave));
    }

    private List<RepaymentPlanEntity> getRepaymentPlanEntities(List<ImportRepaymentPlanXWXTExcel> excels, String opt) {
        long startTime = System.currentTimeMillis();
        Integer index = 1;

        for (int i = 0; i < excels.size(); i++) {
            ImportRepaymentPlanXWXTExcel excel = excels.get(i);
            excel.setRecaptureStatus(RecaptureStatusEnum.getCodeByDesc(excel.getRecaptureStatus()));
            if (i == 0 && excel.getRentAmount().compareTo(BigDecimal.ZERO) == 0) {
                excel.setPeriods(null);
            } else {
                excel.setPeriods(index);
                index++;
            }
        }
        // 处理 保存
        ImportRepaymentPlanXWXTExcel saveDTO = excels.get(0);
        List<RepaymentPlanSaveDTO> excelPlan = excels.stream().map(e -> {
            RepaymentPlanSaveDTO dto = initRepaymentPlan(e.getPlanDate());
            BeanUtil.copyProperties(e, dto);
            return dto;
        }).collect(Collectors.toList());
        List<RepaymentPlanSaveDTO> repaymentPlanSaveDTOS = getRepaymentPlanSaveDTOS(excelPlan,
                saveDTO.getPlanDate(), null, AccrualMethodEnum.IRR.getCode(), null);
        List<RepaymentPlanEntity> entity = BeanUtil.copyToList(repaymentPlanSaveDTOS, RepaymentPlanEntity.class);
        entity.forEach(e -> {
            e.setContractCode(saveDTO.getContractCode());
            e.setOrgId(saveDTO.getOrgId());
            e.setClientCode(saveDTO.getClientCode());
            e.setClientName(saveDTO.getClientName());
            e.setMessageId("XWXT");
            e.setSystemCode("XWXT");
            e.setPlanDatePeriod(Integer.valueOf(DateUtil.format(e.getPlanDate(), DatePattern.SIMPLE_MONTH_PATTERN)));
        });
        log.info(saveDTO.getContractCode() + "处理时间" + (System.currentTimeMillis() - startTime));

        if ("1".equals(opt)) {
            long startTimesave = System.currentTimeMillis();
            this.saveBatch(entity);
            log.info(saveDTO.getContractCode() + "保存时间" + (System.currentTimeMillis() - startTimesave));
        }
        return entity;
    }

    @Override
    public void monthlyChangeTask(String dateString) {
        Date date = StringUtils.isNotBlank(dateString) ? DateUtil.parseDate(dateString) : new Date();
        String month = DateUtil.format(date, "yyyy-M");
        String day = DateUtil.format(date, "yyyy-MM-dd");
        Integer planDatePeriod = Integer.valueOf(DateUtil.format(date, DatePattern.SIMPLE_MONTH_PATTERN));
        // 获取 当月变更最新版本数据
        List<RepaymentPlanHisEntity> planHisList = hisMapper.selectThisMonthMaxVersionGroup(day);
        // 获取 当月成本类
        ClaimOrderSpecialQueryDTO claimOrderSpecialQueryDTO = new ClaimOrderSpecialQueryDTO();
        claimOrderSpecialQueryDTO.setAccountDate(month);
        List<ClaimOrderCostVo> claimOrderCostVos = claimOrderSpecialService.statisticalCost(claimOrderSpecialQueryDTO);

        List<String> contractCodeList = new ArrayList<>();
        List<String> changeContractCodeList = planHisList.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
        contractCodeList.addAll(changeContractCodeList);
        contractCodeList.addAll(claimOrderCostVos.stream().map(e -> e.getContractNum()).collect(Collectors.toList()));
        contractCodeList = contractCodeList.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(contractCodeList)) {
            log.info("每月变更任务 无待处理数据");
            return;
        }
        // 获取所有处理合同的历史成本类数据
        RepaymentPlanCostQueryDTO repaymentPlanCostQueryDTO = new RepaymentPlanCostQueryDTO();
        repaymentPlanCostQueryDTO.setContractCodeList(contractCodeList);
        List<RepaymentPlanCostEntity> allPlanCost = costService.selectList(repaymentPlanCostQueryDTO);
        // 获取所有处理合同的偿还计划
        List<RepaymentPlanEntity> planList = repaymentPlanMapper.selectList(Wrappers.<RepaymentPlanEntity>lambdaQuery()
                .in(RepaymentPlanEntity::getContractCode, contractCodeList)
                .orderByAsc(RepaymentPlanEntity::getPlanDate));
        // 获去所有处理合同信息
        List<ContractEntity> contractEntities = contractService.getBaseMapper().selectList(Wrappers.<ContractEntity>lambdaQuery()
                .in(ContractEntity::getContractCode, contractCodeList));
        // 有成本无变更的数据
        List<String> costWithNochangeCodeList = contractCodeList.stream().filter(e -> !changeContractCodeList.contains(e)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(costWithNochangeCodeList)) {
            ContractHisQueryDTO contractHisQueryDTO = new ContractHisQueryDTO();
            contractHisQueryDTO.setContractCodeList(costWithNochangeCodeList);
            List<RepaymentPlanHisEntity> noChangePlanHis = hisMapper.selectGroupByContractCode(contractHisQueryDTO);
            noChangePlanHis.forEach(e -> e.setChangeDate(null));
            planHisList.addAll(noChangePlanHis);
        }

        Map<String, List<RepaymentPlanHisEntity>> planHisContractMap = planHisList.stream().collect(Collectors.groupingBy(e -> e.getContractCode()));
        Map<String, List<RepaymentPlanEntity>> planContractMap = planList.stream().collect(Collectors.groupingBy(e -> e.getContractCode()));
        // 处理当月成本类相关数据
        List<RepaymentPlanCostEntity> repaymentPlanCostEntities = new ArrayList<>();
        for (ClaimOrderCostVo claimOrderCostVo : claimOrderCostVos) {
            String contractCode = claimOrderCostVo.getContractNum();
            if (StringUtils.isBlank(contractCode)) {
                log.info("每月变更任务 当月成本类 无合同号" + JSON.toJSONString(claimOrderCostVo));
                continue;
            }
            // 先拿版本表 没有则说明这个合同没有相关偿还计划,不处理
            List<RepaymentPlanHisEntity> planHis = planHisContractMap.get(contractCode);
            if (CollectionUtils.isEmpty(planHis)) {
                log.info("每月变更任务 合同无相关偿还计划" + JSON.toJSONString(claimOrderCostVo));
                continue;
            }
            Date planDate = null;
            // 是否变更数据,不是再查询偿还计划数据确定是哪一天,
            Date changeDate = planHis.get(0).getChangeDate();
            if (null == changeDate) {
                List<RepaymentPlanEntity> repaymentPlanEntities = planContractMap.get(contractCode);
                if (CollectionUtils.isEmpty(repaymentPlanEntities)) {
                    log.info("每月变更任务 合同无相关偿还计划" + JSON.toJSONString(claimOrderCostVo));
                    continue;
                }
                Optional<RepaymentPlanEntity> first = repaymentPlanEntities.stream().filter(e -> planDatePeriod.equals(e.getPlanDatePeriod())).findFirst();
                if (!first.isPresent()) {
                    log.info("每月变更任务 合同偿还计划当月无计划" + JSON.toJSONString(claimOrderCostVo));
                    continue;
                } else {
                    planDate = first.get().getPlanDate();
                    List<RepaymentPlanHisEntity> hisPlans = planHisContractMap.get(contractCode);
                    hisPlans.forEach(e -> e.setChangeDate(first.get().getPlanDate()));
                }
            } else {
                planDate = changeDate;
            }

            RepaymentPlanCostEntity repaymentPlanCostEntity = new RepaymentPlanCostEntity();
            repaymentPlanCostEntity.setOrgId(claimOrderCostVo.getOrgId());
            repaymentPlanCostEntity.setContractCode(claimOrderCostVo.getContractNum());
            repaymentPlanCostEntity.setAccountDate(claimOrderCostVo.getAccountDate());
            repaymentPlanCostEntity.setPlanDate(planDate);
            repaymentPlanCostEntity.setCashFlow(claimOrderCostVo.getAmount().negate());
            repaymentPlanCostEntities.add(repaymentPlanCostEntity);
        }
        allPlanCost.addAll(repaymentPlanCostEntities);

        Map<String, ContractEntity> contractEntityMap = contractEntities.stream().collect(Collectors.toMap(e -> e.getContractCode(), e -> e, (a, b) -> b));
        Map<String, List<RepaymentPlanCostEntity>> planCostMap = allPlanCost.stream().collect(Collectors.groupingBy(e -> e.getContractCode()));
        // 处理变更逻辑 版本 偿还计划 所有月份的成本类金额 合同信息
        for (String contractCode : contractCodeList) {
            List<RepaymentPlanEntity> plans = planContractMap.get(contractCode);
            if (CollectionUtils.isEmpty(plans)) {
                log.info("每月变更任务 无偿还计划" + contractCode);
                continue;
            }
            // 实收的计划不处理
            String incomeProvisionMethod = plans.get(0).getIncomeProvisionMethod();
            if (AccrualMethodEnum.RECEIPT.getCode().equals(incomeProvisionMethod)) {
                log.info("每月变更任务 偿还计划实收" + contractCode);
                continue;
            }
            List<RepaymentPlanHisEntity> hisPlans = planHisContractMap.get(contractCode);
            if (CollectionUtils.isEmpty(hisPlans)) {
                log.info("每月变更任务 无偿还计划版本记录" + contractCode);
                continue;
            }
            ContractEntity contract = contractEntityMap.get(contractCode);
            if (Objects.isNull(contract)) {
                log.info("每月变更任务 无相关合同信息" + contractCode);
                continue;
            }
            List<RepaymentPlanCostEntity> costs = planCostMap.get(contractCode);
            List<RepaymentPlanSaveDTO> change = change(plans, hisPlans, contract, costs, incomeProvisionMethod);

            saveRepaymentPlan(change);
        }

        costService.saveBatch(repaymentPlanCostEntities);
    }

    @Override
    public List<RepaymentPlanVO> selectPlanAmountBuCondition(RepaymentPlanQueryDTO queryDTO) {
        return repaymentPlanMapper.selectPlanAmountBuCondition(queryDTO);
    }

    @Override
    public List<RepaymentPlanVO> selectByContractCodeList(List<String> contractCodeList) {
        List<RepaymentPlanEntity> list = list(new LambdaQueryWrapper<RepaymentPlanEntity>()
                .in(RepaymentPlanEntity::getContractCode, contractCodeList)
                .isNotNull(RepaymentPlanEntity::getPeriods));
        return ListBeanUtil.copyList(list, RepaymentPlanVO.class);
    }

    /**
     * 根据合同号查询最小的计划日期
     * @param contractCodeList
     * @return
     */
    @Override
    public List<RepaymentPlanVO> selectMinPlanDate(List<String> contractCodeList) {
        return baseMapper.selectMinPlanDate(contractCodeList);
    }

    private List<RepaymentPlanSaveDTO> change(List<RepaymentPlanEntity> plans, List<RepaymentPlanHisEntity> hisPlans, ContractEntity contract, List<RepaymentPlanCostEntity> costs, String incomeProvisionMethod) {
        List<RepaymentPlanSaveDTO> repaymentPlanSaveDTOS = new ArrayList<>();
        if (AccrualMethodEnum.XIRR.getCode().equals(incomeProvisionMethod)) {
            repaymentPlanSaveDTOS = handleChangeXIRR(contract, hisPlans, plans, costs);
        }
        if (AccrualMethodEnum.IRR.getCode().equals(incomeProvisionMethod)) {
            repaymentPlanSaveDTOS = handleChangeIRR(contract, hisPlans, plans, costs);
        }
        return repaymentPlanSaveDTOS;
    }

    private List<RepaymentPlanSaveDTO> handleChangeIRR(ContractEntity contract, List<RepaymentPlanHisEntity> hisPlans, List<RepaymentPlanEntity> plans, List<RepaymentPlanCostEntity> costs) {
        Date changeDate = hisPlans.get(0).getChangeDate();
        // 1.原始数据到变更日的 分摊
        List<RepaymentPlanEntity> oldBeforeChangePlan = plans.stream().filter(e -> !e.getPlanDate().after(changeDate)).collect(Collectors.toList());
        // 变更前最后一条数据
        RepaymentPlanEntity oldLastBeforeChange = oldBeforeChangePlan.get(oldBeforeChangePlan.size() - 1);
        // 如果是changeDate,则直接使用,否则拿下一期做拆分,算出变更日的数据
        BigDecimal oldOpeningAmortizedCost = BigDecimal.ZERO;
        BigDecimal oldRentIncome = BigDecimal.ZERO;
        if (oldLastBeforeChange.getPlanDate().compareTo(changeDate) == 0) {
            oldOpeningAmortizedCost = oldLastBeforeChange.getOpeningAmortizedCost();
            oldRentIncome = oldLastBeforeChange.getRentalIncome();
        } else {
            Optional<RepaymentPlanEntity> first = plans.stream().filter(e -> e.getPlanDate().after(changeDate)).findFirst();
            if (first.isPresent()) {
                RepaymentPlanEntity afterChangePlan = first.get();
                oldOpeningAmortizedCost = afterChangePlan.getOpeningAmortizedCost();
                oldRentIncome = NumberUtil.mul(afterChangePlan.getRentalIncome(),
                        NumberUtil.div(DateUtil.betweenDay(changeDate, oldLastBeforeChange.getPlanDate(), true),
                                DateUtil.betweenDay(afterChangePlan.getPlanDate(), oldLastBeforeChange.getPlanDate(), true))).setScale(2, RoundingMode.HALF_UP);
            } else {
                return new ArrayList<>();
            }
        }
        //
        // 2.计算新IRR的偿还计划    到变更日的分摊
        List<RepaymentPlanSaveDTO> newPlans = handleIrr(contract, BeanUtil.copyToList(hisPlans, RepaymentPlanSaveDTO.class), costs);
        List<RepaymentPlanSaveDTO> newBeforeChangePlan = newPlans.stream().filter(e -> !e.getPlanDate().after(changeDate)).collect(Collectors.toList());
        // 变更前最后一条数据
        RepaymentPlanSaveDTO newLastBeforeChange = newBeforeChangePlan.get(newBeforeChangePlan.size() - 1);
        // 如果是changeDate,则直接使用,否则拿下一期做拆分,算出变更日的数据
        BigDecimal newOpeningAmortizedCost = BigDecimal.ZERO;
        BigDecimal newRentIncome = BigDecimal.ZERO;

        List<RepaymentPlanSaveDTO> result = new ArrayList<>();

        if (newLastBeforeChange.getPlanDate().compareTo(changeDate) == 0) {
            newOpeningAmortizedCost = newLastBeforeChange.getOpeningAmortizedCost();
            newRentIncome = newLastBeforeChange.getRentalIncome();
            newLastBeforeChange.setOpeningAmortizedCost(oldLastBeforeChange.getOpeningAmortizedCost());

            // 返回变更日及变更日以后的数据,变更日期初数据设置为 老偿还计划的期初
            result.add(newLastBeforeChange);
            result.addAll(newPlans.stream().filter(e -> e.getPlanDate().compareTo(changeDate) > 0).collect(Collectors.toList()));
        } else {
            Optional<RepaymentPlanSaveDTO> first = newPlans.stream().filter(e -> e.getPlanDate().after(changeDate)).findFirst();
            if (first.isPresent()) {
                RepaymentPlanSaveDTO firstAfterChangePlan = first.get();
                newOpeningAmortizedCost = firstAfterChangePlan.getOpeningAmortizedCost();
                newRentIncome = NumberUtil.mul(firstAfterChangePlan.getRentalIncome(),
                        NumberUtil.div(DateUtil.betweenDay(changeDate, newLastBeforeChange.getPlanDate(), true),
                                DateUtil.betweenDay(firstAfterChangePlan.getPlanDate(), newLastBeforeChange.getPlanDate(), true))).setScale(2, RoundingMode.HALF_UP);

                RepaymentPlanSaveDTO changePlan = BeanUtil.copyProperties(firstAfterChangePlan, RepaymentPlanSaveDTO.class);
                changePlan.setPlanDate(changeDate);
                changePlan.setOpeningAmortizedCost(oldLastBeforeChange.getOpeningAmortizedCost());
                changePlan.setRentalIncome(newRentIncome);
                firstAfterChangePlan.setRentalIncome(NumberUtil.sub(firstAfterChangePlan.getRentalIncome(), newRentIncome));
                result.add(changePlan);
                result.add(firstAfterChangePlan);
                result.addAll(newPlans.stream().filter(e -> e.getPlanDate().after(firstAfterChangePlan.getPlanDate())).collect(Collectors.toList()));
            } else {
                return new ArrayList<>();
            }
        }
        // 3. 新老分摊的差异
        BigDecimal subCost = NumberUtil.sub(oldOpeningAmortizedCost.add(oldRentIncome), newOpeningAmortizedCost.add(newRentIncome)).setScale(2, RoundingMode.HALF_UP);
        log.info("租赁收益调整额==：{}", subCost);
        if (BigDecimal.ZERO.compareTo(subCost) == 0) {
            return new ArrayList<>();
        }

        return result;
    }

    public List<RepaymentPlanSaveDTO> handleChangeXIRR(ContractEntity contract, List<RepaymentPlanHisEntity> planHisEntities, List<RepaymentPlanEntity> plans, List<RepaymentPlanCostEntity> costs) {
        // planHisEntities 全量新变更合同
        Date changeDate = planHisEntities.get(0).getChangeDate();
        BigDecimal inputTaxRatePer = new BigDecimal(13);
        BigDecimal outputTaxRatePer = new BigDecimal(6);
        BigDecimal J9 = contract.getReceivableFirstAmount();//租金首付款：
        BigDecimal J11 = contract.getReceivableProcedureAmount();//手续费收入(含增值税)：
        BigDecimal F9 = contract.getPayableDeviceAmount();//设备价格：
        BigDecimal F10 = contract.getLessorInsuranceAmount();//出租人保险费
        BigDecimal F11 = contract.getChannelFees();//渠道费用：
        BigDecimal F12 = contract.getLessorOtherCosts();//出租人其它成本：
        BigDecimal F13 = contract.getReceivableInsuranceAmount();//承租人保险费
        BigDecimal F14 = contract.getReceivableOther();//其他收入 (含增值税)：
        BigDecimal residualAmount = contract.getRetainedPrice();//应收残值
        Date startDate = DateUtil.date(contract.getLeaseDateStart());

        List<RepaymentPlanSaveDTO> repaymentPlanSaveDTOS = handleChange(changeDate, planHisEntities, inputTaxRatePer, outputTaxRatePer,
                J9, J11, F9, F10, F11, F12, F13, F14, residualAmount, startDate, plans, costs);
        repaymentPlanSaveDTOS.forEach(dto -> {
            dto.setClientCode(contract.getClientCode());
            dto.setClientName(contract.getClientName());
            dto.setContractCode(contract.getContractCode());
            dto.setContractName(contract.getContractName());
            dto.setOrgId(contract.getOrgId());
            dto.setSystemCode(contract.getSystemCode());
        });
        log.info("repaymentPlanSaveDTOS:{}", JSON.toJSONString(repaymentPlanSaveDTOS));
        return repaymentPlanSaveDTOS;

    }

    public List<RepaymentPlanSaveDTO> handleChange(Date changeDate, List<RepaymentPlanHisEntity> newData, BigDecimal inputTaxRatePer,
                                                   BigDecimal outputTaxRatePer, BigDecimal j9, BigDecimal j11, BigDecimal f9, BigDecimal f10, BigDecimal f11,
                                                   BigDecimal f12, BigDecimal f13, BigDecimal f14, BigDecimal residualAmount, Date startDate,
                                                   List<RepaymentPlanEntity> plans, List<RepaymentPlanCostEntity> costs) {
        BigDecimal inputTaxRate = (new BigDecimal(100).add(inputTaxRatePer)).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
        BigDecimal outputTaxRate = (new BigDecimal(100).add(outputTaxRatePer)).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
        newData = newData.stream()
                .filter(e -> null != e.getPlanDate() && null != e.getRentAmount() && null != e.getPrincipalAmount())
                .collect(Collectors.toList());
        newData.sort(Comparator.comparing(RepaymentPlanHisEntity::getPlanDate));
        //1. 计算利息,本金,不含税金额
        generateBaseInfo(newData, inputTaxRatePer, residualAmount);
        log.info("newData:{}", JSON.toJSONString(newData));

        // 重新计算期初现金流
        RepaymentPlanSaveDTO newFirstRepayment = handleFirstRepayment(j9, j11, f9, f10, f11, f12, f13, f14, changeDate, inputTaxRate, outputTaxRate);
        // 新回报率
        Double newXirr = getNewXirr(newData, startDate, newFirstRepayment, costs);
        log.info("修改后年化内含报酬率xirrRate:{}", newXirr);
        if (Double.isNaN(newXirr) || newXirr < 0) {
            return new ArrayList<>();
        }

        List<RepaymentPlanEntity> beforeChangePlan = plans.stream().filter(e -> !e.getPlanDate().after(changeDate)).collect(Collectors.toList());
        log.info("beforeChangePlan:{}", JSON.toJSONString(beforeChangePlan));

        // 变更前最后一条数据
        RepaymentPlanEntity lastBeforeChange = beforeChangePlan.get(beforeChangePlan.size() - 1);
        // 生成变更日数据
        RepaymentPlanSaveDTO ChangeDatePlan = getChangeDatePlan(changeDate, lastBeforeChange);
        // 变更日 原计划、原利率现值
        BigDecimal endingAmortizedCost = ChangeDatePlan.getEndingAmortizedCost();
        // 变更日 新计划、新利率现值
        List<RepaymentPlanHisEntity> newRepaymentPlanHisEntities = newData.stream().filter(e -> e.getPlanDate().after(changeDate)).collect(Collectors.toList());
        List<Date> changeDates = newRepaymentPlanHisEntities.stream().map(e -> e.getPlanDate()).collect(Collectors.toList());
        changeDates.add(changeDate);
        List<Double> changeCashFlows = newRepaymentPlanHisEntities.stream().map(e -> e.getCashFlow().doubleValue()).collect(Collectors.toList());
        changeCashFlows.add(0D);
        BigDecimal newPV = XirrUtils.calXNPV(newXirr, changeDates, changeCashFlows);
        // 调整额
        BigDecimal totalDiscountAmount = NumberUtil.sub(endingAmortizedCost, newPV);
        log.info("租赁收益调整额==：{}", totalDiscountAmount);
        // 没有变更，直接返回
        if (totalDiscountAmount.compareTo(BigDecimal.ZERO) == 0) {
            return new ArrayList<>();
        }

        // 调整变更日 期末摊余成本 成本信息的引入
        ChangeDatePlan.setEndingAmortizedCost(NumberUtil.add(ChangeDatePlan.getEndingAmortizedCost(), totalDiscountAmount.negate()).setScale(2, RoundingMode.HALF_UP));
        Optional<RepaymentPlanCostEntity> first = costs.stream().filter(e -> DateUtil.compare(e.getPlanDate(), changeDate) == 0).findFirst();
        first.ifPresent(repaymentPlanCostEntity -> ChangeDatePlan.setEndingAmortizedCost(NumberUtil.add(ChangeDatePlan.getEndingAmortizedCost(), repaymentPlanCostEntity.getCashFlow()).setScale(2, RoundingMode.HALF_UP)));
        // 变更日后所有新计划
        List<RepaymentPlanSaveDTO> newAfterChangePlan = newRepaymentPlanHisEntities.stream().map(e ->
                BeanUtil.copyProperties(e, RepaymentPlanSaveDTO.class, GenConstants.BASE_ENTITY)
        ).collect(Collectors.toList());

        List<RepaymentPlanSaveDTO> saveRepayments = new ArrayList<>();
        saveRepayments.add(ChangeDatePlan);
        Iterator<RepaymentPlanSaveDTO> leaseIterator = newAfterChangePlan.iterator();
        RepaymentPlanSaveDTO next = leaseIterator.next();
        // 5.生成新计划
        generateRepayments(changeDate, next.getPlanDate(), saveRepayments, changeDates, leaseIterator, newXirr, next);

        // 6.处理数据
        BigDecimal totalUnrealizedRevenue = NumberUtil.add(newData.stream().map(e -> e.getCashFlow()).reduce(BigDecimal.ZERO, BigDecimal::add), newFirstRepayment.getCashFlow());
        BigDecimal rentalIncomeBeforeTotal = NumberUtil.add(lastBeforeChange.getRentalIncomeBeforeTotal(), lastBeforeChange.getRentalIncome());

        for (int i = 0; i < saveRepayments.size(); i++) {
            RepaymentPlanSaveDTO saveDTO = saveRepayments.get(i);
            saveDTO.setXirrRate(NumberUtil.mul(newXirr, new BigDecimal(100)).setScale(5, RoundingMode.HALF_UP));
            if (i == 0) {
                // 调整变更日 租赁收入
                saveDTO.setRentalIncome(NumberUtil.add(saveDTO.getRentalIncome(), totalDiscountAmount.negate()).setScale(2, RoundingMode.HALF_UP));
            }
            saveDTO.setRentalIncomeBeforeTotal(rentalIncomeBeforeTotal);
            BigDecimal rentalIncomeAfterTotal = NumberUtil.sub(totalUnrealizedRevenue, saveDTO.getRentalIncome(), rentalIncomeBeforeTotal);
            saveDTO.setRentalIncomeAfterTotal(rentalIncomeAfterTotal);
            saveDTO.setAccrued(YesOrNoEnum.YES.getCode());
            saveDTO.setIncomeProvisionMethod(AccrualMethodEnum.XIRR.getCode());
            saveDTO.setUnrealizedRevenue(totalUnrealizedRevenue);
            rentalIncomeBeforeTotal = NumberUtil.add(rentalIncomeBeforeTotal, saveDTO.getRentalIncome());
        }
        // 覆盖 变更日前所有老计划 的基本信息
//        Map<Integer, RepaymentPlanHisEntity> newMapByPeriods = newData.stream().collect(Collectors.toMap(e -> e.getPeriods(), e -> e));
//        handleBeforeChangeDateBaseInfo(changeDate, beforeChangePlan, newFirstRepayment, newMapByPeriods, saveRepayments);
        log.info("saveRepayments:{}", JSON.toJSONString(saveRepayments));
        return saveRepayments;//第一期负现金流数据+所有有期数的数据+变更日及以后的无期数数据
    }

    private void handleBeforeChangeDateBaseInfo(Date changeDate, List<RepaymentPlanEntity> oldBeforeChangeHasCashPlan, RepaymentPlanSaveDTO newFirstRepayment, Map<Integer, RepaymentPlanHisEntity> newMapByPeriods, List<RepaymentPlanSaveDTO> saveRepayments) {
        // 第一期
        RepaymentPlanEntity oldFirst = oldBeforeChangeHasCashPlan.get(0);
        oldFirst.setOutflowAmount(newFirstRepayment.getOutflowAmount());
        oldFirst.setPlannedInterest(newFirstRepayment.getPlannedInterest());
        oldFirst.setCashFlow(newFirstRepayment.getCashFlow());
        // 不是变更日的数据
        List<RepaymentPlanEntity> oldBeforeChangeHasPerPlan = oldBeforeChangeHasCashPlan.stream()
                .filter(e -> null != e.getPeriods() && e.getPlanDate().compareTo(changeDate) != 0).collect(Collectors.toList());
        oldBeforeChangeHasPerPlan.forEach(e -> {
            RepaymentPlanHisEntity repaymentPlanHisEntity = newMapByPeriods.get(e.getPeriods());
            if (null != repaymentPlanHisEntity) {
                e.setRentAmount(repaymentPlanHisEntity.getRentAmount());
                e.setPrincipalAmount(repaymentPlanHisEntity.getPrincipalAmount());
                e.setInterestAmount(repaymentPlanHisEntity.getInterestAmount());
                e.setPrincipalTax(repaymentPlanHisEntity.getPrincipalTax());
                e.setInterestTax(repaymentPlanHisEntity.getInterestTax());
                e.setPlannedPrincipal(repaymentPlanHisEntity.getPlannedPrincipal());
                e.setPlannedInterest(repaymentPlanHisEntity.getPlannedInterest());
                e.setCashFlow(repaymentPlanHisEntity.getCashFlow());
            }
        });
//        saveRepayments.add(oldFirst);
//        saveRepayments.addAll(oldBeforeChangeHasPerPlan);
    }

    private RepaymentPlanSaveDTO getChangeDatePlan(Date changeDate, RepaymentPlanEntity lastBeforeChange) {
        RepaymentPlanSaveDTO ChangeDatePlan = new RepaymentPlanSaveDTO();
        if (lastBeforeChange.getPlanDate().compareTo(changeDate) == 0) {
            BeanUtil.copyProperties(lastBeforeChange, ChangeDatePlan, GenConstants.BASE_ENTITY);
        } else {
            ChangeDatePlan = initRepaymentPlan(changeDate);
            ChangeDatePlan.setOpeningAmortizedCost(lastBeforeChange.getEndingAmortizedCost());
            ChangeDatePlan.setXirrRate(lastBeforeChange.getXirrRate());
            double dailyRateDou = Math.pow(lastBeforeChange.getXirrRate().doubleValue() / 100 + 1, (double) (DateUtil.betweenDay(changeDate, lastBeforeChange.getPlanDate(), true) - 1) / 365) - 1;
            ChangeDatePlan.setRentalIncome(NumberUtil.mul(ChangeDatePlan.getOpeningAmortizedCost(), dailyRateDou).setScale(2, RoundingMode.HALF_UP));
            ChangeDatePlan.setEndingAmortizedCost(NumberUtil.add(ChangeDatePlan.getOpeningAmortizedCost(), ChangeDatePlan.getRentalIncome(), ChangeDatePlan.getCashFlow().negate()).setScale(2, RoundingMode.HALF_UP));
            ChangeDatePlan.setActualDailyRate(NumberUtil.mul(dailyRateDou, new BigDecimal(100)).setScale(5, RoundingMode.HALF_UP));
        }
        return ChangeDatePlan;
    }

    private Double getNewXirr(List<RepaymentPlanHisEntity> newData, Date startDate, RepaymentPlanSaveDTO newFirstRepayment, List<RepaymentPlanCostEntity> costs) {
        List<Date> allPlanDate = newData.stream().map(e -> e.getPlanDate()).collect(Collectors.toList());
        List<Double> cashFlowList = newData.stream().map(e -> NumberUtil.toDouble(e.getCashFlow())).collect(Collectors.toList());
        allPlanDate.add(startDate);
        cashFlowList.add(newFirstRepayment.getCashFlow().doubleValue());
        if (CollectionUtils.isNotEmpty(costs)) {
            allPlanDate.addAll(costs.stream().map(e -> e.getPlanDate()).collect(Collectors.toList()));
            cashFlowList.addAll(costs.stream().map(e -> NumberUtil.toDouble(e.getCashFlow())).collect(Collectors.toList()));
        }
        //计算 年化内含报酬率
        Double newXirr = XirrUtils.xirr(cashFlowList, allPlanDate);
        return newXirr;
    }

    private void generateBaseInfo(List<RepaymentPlanHisEntity> newData, BigDecimal inputTaxRatePer, BigDecimal residualAmount) {
        for (int i = 0; i < newData.size(); i++) {
            RepaymentPlanHisEntity e = newData.get(i);
            BigDecimal interestAmount = e.getInterestAmount();
            if (i == newData.size() - 1) {
                // 应收残值补充
                interestAmount = NumberUtil.add(interestAmount, residualAmount);
            }
            BigDecimal principalAmountTaxes = NumberUtil.div(e.getPrincipalAmount(), new BigDecimal(100).add(inputTaxRatePer)).multiply(inputTaxRatePer);
            BigDecimal interestAmountTaxes = NumberUtil.div(interestAmount, new BigDecimal(100).add(inputTaxRatePer)).multiply(inputTaxRatePer);
            BigDecimal principalAmountNoTax = NumberUtil.sub(e.getPrincipalAmount(), principalAmountTaxes);
            BigDecimal interestAmountNoTax = NumberUtil.sub(interestAmount, interestAmountTaxes);
            BigDecimal cashFlow = NumberUtil.add(principalAmountNoTax, interestAmountNoTax);
            e.setInterestAmount(interestAmount.setScale(2, RoundingMode.HALF_UP));
            e.setPrincipalTax(principalAmountTaxes.setScale(2, RoundingMode.HALF_UP));
            e.setInterestTax(interestAmountTaxes.setScale(2, RoundingMode.HALF_UP));
            e.setPlannedPrincipal(principalAmountNoTax.setScale(2, RoundingMode.HALF_UP));
            e.setPlannedInterest(interestAmountNoTax.setScale(2, RoundingMode.HALF_UP));
            e.setCashFlow(cashFlow.setScale(2, RoundingMode.HALF_UP));
        }
    }

    /**
     * 更新特殊合同的回笼状态
     */
    public void updateRepaymentPlanForSpecialContract(LeaseIncomeQueryDTO queryDTO) {
        if (null == queryDTO.getBusinessDate()) {
            queryDTO.setBusinessDate(DateUtils.getNowDate());
        }

        Date provisionDate = DateUtil.endOfDay(DateUtil.endOfMonth(queryDTO.getBusinessDate()));

        LambdaUpdateWrapper<RepaymentPlanEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.like(RepaymentPlanEntity::getContractCode, "D%");
        wrapper.le(RepaymentPlanEntity::getPlanDate, provisionDate);
        wrapper.set(RepaymentPlanEntity::getRecaptureStatus, RecaptureStatusEnum.RETURNED.getCode());
        this.update(wrapper);
    }

    @Override
    public List<RepaymentPlanEntity> selectListPrioritySnapshot(List<String> contractCodeList) {
        return repaymentPlanMapper.selectListPrioritySnapshot(contractCodeList);
    }

    /**
     * 计提方式变更
     */
    public String incomeProvisionMethodChange(LeaseIncomeImport incomeImport) {
        List<RepaymentPlanEntity> repaymentPlanEntities = this.getBaseMapper().selectList(Wrappers.<RepaymentPlanEntity>lambdaQuery()
                .eq(RepaymentPlanEntity::getContractCode, incomeImport.getContractCode())
                .orderByAsc(RepaymentPlanEntity::getPlanDate));
        if (repaymentPlanEntities == null || repaymentPlanEntities.isEmpty()) {
            log.warn(incomeImport.getContractCode() + ",未找到该合同的偿还计划!");
            return "ok";
        }

        // 1.判断计提月份是否符合要求
//        Integer queryDate = Integer.parseInt(DateUtil.format(incomeImport.getBusinessDate(), "yyyyMM"));
//        Integer curMonth = Integer.parseInt(DateUtil.format(DateUtils.getNowDate(), "yyyyMM"));
//        if (queryDate < curMonth) {
//            return incomeImport.getContractCode() + ",计提月份应大于当前月份!";
//        }


        // 2.判断是否修改了计提方式
        ContractDTO contractDTO = contractService.getContractDTOByCode(incomeImport.getContractCode(),
                repaymentPlanEntities.get(0).getOrgId());
        if (StringUtils.isNotEmpty(incomeImport.getIncomeProvisionMethod()) &&
                !StringUtils.equals(contractDTO.getIncomeProvisionMethod(),
                        AccrualMethodEnum.getCodeByDesc(incomeImport.getIncomeProvisionMethod()))) {

            // 3.IRR或者XIRR变更为实收
            if (!AccrualMethodEnum.RECEIPT.getCode().equals(contractDTO.getIncomeProvisionMethod())
                    && AccrualMethodEnum.RECEIPT.getCode().equals(
                    AccrualMethodEnum.getCodeByDesc(incomeImport.getIncomeProvisionMethod()))) {
                repaymentPlanEntities = this.irrTransferToReceipt(incomeImport, repaymentPlanEntities);
            }

            // 4.实收变更为IRR或者XIRR
            if (AccrualMethodEnum.RECEIPT.getCode().equals(contractDTO.getIncomeProvisionMethod())
                    && !AccrualMethodEnum.RECEIPT.getCode().equals(
                    AccrualMethodEnum.getCodeByDesc(incomeImport.getIncomeProvisionMethod()))) {
                repaymentPlanEntities = this.receiptTransferToIrr(contractDTO, incomeImport, repaymentPlanEntities);
            }
            List<Long> ids = repaymentPlanEntities.stream().map(e->e.getId()).collect(Collectors.toList());;
            this.getBaseMapper().deleteBatchIds(ids);
            this.saveBatch(repaymentPlanEntities);
        }
        return "ok";
    }

    /**
     * 实收转IRR或者XIRR
     */
    private List<RepaymentPlanEntity> receiptTransferToIrr(ContractDTO contractDTO, LeaseIncomeImport incomeImport,
                                                           List<RepaymentPlanEntity> repaymentPlanEntityList) {
        // 之前的数据收入总计
        String initDate = DateUtil.format(incomeImport.getBusinessDate(), "yyyy-MM-dd");
        OutstandingAmountCashFlowSumInputDTO dto = new OutstandingAmountCashFlowSumInputDTO();
        dto.setInitDate(initDate);
        dto.setContractCode(contractDTO.getContractCode());
        List<OutstandingAmountCashFlowSumDTO> outstandingAmountCashFlowSumDTOList =
                repaymentPlanMapper.outstandingAmountCashFlowSum(dto);
        if (outstandingAmountCashFlowSumDTOList == null || outstandingAmountCashFlowSumDTOList.isEmpty()) {
            throw new ServiceException(incomeImport.getContractCode() + ",未找未实现收益数据!");
        }

        // 未实现收益
        Map<String, BigDecimal> outstandingAmountMap = outstandingAmountInitService.
                selectEndBalFor(Lists.newArrayList(contractDTO.getContractCode()));

        // 待摊销的金额
        BigDecimal toBeAssessedAmount = serviceNoAmortizationService.getToBeAssessedAmount(contractDTO.getContractCode());

        RepaymentPlanEntity tempCashFlowData = this.createTempCashFlowData(incomeImport,
                outstandingAmountCashFlowSumDTOList.get(0), repaymentPlanEntityList);

        // 租赁收入求和
        BigDecimal cashflowSum = repaymentPlanEntityList.stream().map(e -> e.getCashFlow()).
                reduce(BigDecimal.ZERO, BigDecimal::add);

        List<RepaymentPlanEntity> result = new ArrayList<>();
        List<RepaymentPlanEntity> recalculationList = new ArrayList<>();
        recalculationList.add(tempCashFlowData);

        BigDecimal rentalIncomeSum = BigDecimal.ZERO;
        RepaymentPlanEntity e = null;
        for (int i = 0; i < repaymentPlanEntityList.size(); i++) {
            e = repaymentPlanEntityList.get(i);
            // 偿还日期是否小于2023-11-30
            int compareValue = DateUtils.truncatedCompareTo(e.getPlanDate(),
                    tempCashFlowData.getPlanDate(), Calendar.DATE);

            if (compareValue <= 0) {
                // 租赁收入求和
                rentalIncomeSum = rentalIncomeSum.add(e.getRentalIncome());

                // 计算金蝶的下的未实现收益与偿还计划本身的未实现收益差异； 如果是偿还计划最后一条，则不再计算差异(没有意义)
                if (compareValue == 0 && i != repaymentPlanEntityList.size() - 1) {
                    BigDecimal diffAmount = cashflowSum.subtract(toBeAssessedAmount).subtract(rentalIncomeSum);
                    e.setRentalIncome(e.getRentalIncome().add(diffAmount));
                    e.setRentalIncomeAfterTotal(e.getRentalIncomeAfterTotal().subtract(diffAmount));
                }
                result.add(e);
            } else {
                if (e.getPeriods() != null) {
                    recalculationList.add(e);
                }
            }
        }

        // 再计算
        if (recalculationList.size() > 1) {
            contractDTO.setIncomeProvisionMethod(AccrualMethodEnum.getCodeByDesc(incomeImport.getIncomeProvisionMethod()));
            result.addAll(this.businessProcess(recalculationList, contractDTO,
                    outstandingAmountMap.get(contractDTO.getContractCode())));
        } else if (recalculationList.size() == 1) {
            result.stream().forEach(k -> {
                k.setExceptionType("期初日期即为计划结束日期");
            });
        }

        // 删除临时生成的现金流数据
        result = result.stream().filter(a -> a.getPeriods() == null || a.getPeriods().intValue() != -1).
                collect(Collectors.toList());
        return result;
    }

    /**
     * 创建临时现金流对象
     */
    private RepaymentPlanEntity createTempCashFlowData(LeaseIncomeImport incomeImport,
                                                       OutstandingAmountCashFlowSumDTO outstandingAmountCashFlowSumDTO,
                                                       List<RepaymentPlanEntity> repaymentPlanEntityList) {


        Date startDayForMonth = DateUtil.beginOfDay(DateUtil.beginOfMonth(incomeImport.getBusinessDate()));
        RepaymentPlanEntity maxDateDateByInitDateEntity = repaymentPlanEntityList.stream().filter(
                e -> e.getPlanDate().compareTo(startDayForMonth) > 0).findFirst().get();
        if (maxDateDateByInitDateEntity == null) {
            maxDateDateByInitDateEntity = repaymentPlanEntityList.get(repaymentPlanEntityList.size() - 1);
        }

        RepaymentPlanEntity repaymentPlanEntity = new RepaymentPlanEntity();
        BeanUtils.copyProperties(maxDateDateByInitDateEntity, repaymentPlanEntity);
        repaymentPlanEntity.setId(IdWorker.getId());
        repaymentPlanEntity.setContractCode(outstandingAmountCashFlowSumDTO.getContractCode());
        repaymentPlanEntity.setCashFlow(outstandingAmountCashFlowSumDTO.getCashflowInit());
        repaymentPlanEntity.setPeriods(-1);
        repaymentPlanEntity.setPlanDate(DateUtil.endOfMonth(incomeImport.getBusinessDate()));
        return repaymentPlanEntity;
    }

    /**
     * 生成偿还计划数据
     */
    private List<RepaymentPlanEntity> businessProcess(List<RepaymentPlanEntity> dataList, ContractDTO contractDTO,
                                                      BigDecimal outstandingAmount) {
        // 偿还计划数据
        List<RepaymentPlanEntity> repayments = new ArrayList<>();

        // 补齐合同从起租到结清的每月数据
        // 判断原数据是否为每月都有数据，如否，则需要补齐每月的数据对象，如是，则不用补齐
        List<RepaymentPlanSaveDTO> repaymentsSaveDTOList = new ArrayList<>();
        List<RepaymentPlanSaveDTO> dataSaveDtoList = BeanUtil.copyToList(dataList, RepaymentPlanSaveDTO.class);
        if (this.isContinuousMonth(dataSaveDtoList)) {
            repaymentsSaveDTOList.addAll(dataSaveDtoList);
        } else {
            repaymentsSaveDTOList.addAll(this.createEmptyRepaymentsData(dataSaveDtoList));
        }
        repayments.addAll(BeanUtil.copyToList(repaymentsSaveDTOList, RepaymentPlanEntity.class));

        // 通过IRR计算利率
        Double incomeRateDouble = 0.00;
        if (AccrualMethodEnum.XIRR.getCode().equals(contractDTO.getIncomeProvisionMethod())) {
            List<Double> cashFlowList = dataList.stream().map(e -> e.getCashFlow().doubleValue()).collect(Collectors.toList());
            List<Date> allPlanDate = dataList.stream().map(e -> e.getPlanDate()).collect(Collectors.toList());
            incomeRateDouble = XirrUtils.xirr(cashFlowList, allPlanDate);
        } else {
            incomeRateDouble = IRRUtils.calculateIRRYear(
                    repayments.stream().map(e -> e.getCashFlow().doubleValue()).collect(Collectors.toList()));
        }
        if (Double.isNaN(incomeRateDouble) || incomeRateDouble.doubleValue() == 0) {
            for (int i = 0; i < repayments.size(); i++) {
                RepaymentPlanEntity e = repayments.get(i);
                if (StringUtils.isEmpty(e.getSystemCode())) {
                    e.setSystemCode(repayments.get(0).getSystemCode());
                }
                e.setExceptionType("irr计算错误");
                if (i == 1) {
                    e.setRentalIncome(outstandingAmount == null ? BigDecimal.ZERO : outstandingAmount.multiply(new BigDecimal(-1)));
                } else {
                    e.setRentalIncome(BigDecimal.ZERO);
                }
            }
            return repayments;
        }

        List<RepaymentPlanSaveDTO> newRepayments = new ArrayList<>();
        if (AccrualMethodEnum.XIRR.getCode().equals(contractDTO.getIncomeProvisionMethod())) {
            newRepayments = BeanUtil.copyToList(repayments, RepaymentPlanSaveDTO.class);
            newRepayments = this.generateXirrRepayments(newRepayments, incomeRateDouble);
        } else {
            newRepayments = this.irrApportion(repayments, incomeRateDouble);
            if (newRepayments == null || newRepayments.isEmpty()) {
                return repayments;
            }
        }

        // 数据处理
        BigDecimal totalUnrealizedRevenue = newRepayments.stream().map(e -> e.getRentalIncome()).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal rentalIncomeBeforeTotal = BigDecimal.ZERO;
        BigDecimal rentalIncomeAfterTotal = totalUnrealizedRevenue;

        for (int i = 0; i < newRepayments.size(); i++) {
            RepaymentPlanSaveDTO saveDTO = newRepayments.get(i);
            saveDTO.setXirrRate(NumberUtil.mul(incomeRateDouble, new BigDecimal(100)).
                    setScale(5, RoundingMode.HALF_UP));
            saveDTO.setUnrealizedRevenue(totalUnrealizedRevenue);
            saveDTO.setRentalIncomeBeforeTotal(rentalIncomeBeforeTotal);
            rentalIncomeBeforeTotal = NumberUtil.add(rentalIncomeBeforeTotal, saveDTO.getRentalIncome());
            rentalIncomeAfterTotal = NumberUtil.sub(rentalIncomeAfterTotal, saveDTO.getRentalIncome());
            saveDTO.setRentalIncomeAfterTotal(rentalIncomeAfterTotal);
            saveDTO.setAccrued(YesOrNoEnum.YES.getCode());
            saveDTO.setIncomeProvisionMethod(contractDTO.getIncomeProvisionMethod());
        }

        List<RepaymentPlanEntity> result = BeanUtil.copyToList(newRepayments, RepaymentPlanEntity.class);
        result.parallelStream().forEach(e -> {
            if (e.getRentalIncome() == null) {
                e.setRentalIncome(BigDecimal.ZERO);
            }
            e.setContractCode(repayments.get(0).getContractCode());
            e.setOrgId(repayments.get(0).getOrgId());
            e.setClientCode(repayments.get(0).getClientCode());
            e.setClientName(repayments.get(0).getClientName());
            e.setMessageId(repayments.get(0).getMessageId());
            e.setSystemCode(repayments.get(0).getSystemCode());
            e.setSourceIrrRate(repayments.get(0).getSourceIrrRate());
            e.setPlanDatePeriod(Integer.valueOf(DateUtil.format(e.getPlanDate(), DatePattern.SIMPLE_MONTH_PATTERN)));
        });
        return result;
    }

    /**
     * irr转实收/实收回笼时/期初生成偿还计划时-偿还计划变更
     */
    public List<RepaymentPlanEntity> irrTransferToReceipt(
            LeaseIncomeImport incomeImport, List<RepaymentPlanEntity> repaymentPlanEntities) {

        Date startDayForMonth = DateUtil.beginOfDay(DateUtil.beginOfMonth(incomeImport.getBusinessDate()));
        Date endDayForMonth = DateUtil.endOfDay(DateUtil.endOfMonth(incomeImport.getBusinessDate()));
        List<RepaymentPlanEntity> beforeRepaymentPlanEntities = repaymentPlanEntities.stream().filter(
                e -> e.getPlanDate().compareTo(endDayForMonth) <= 0).collect(Collectors.toList());
        // 之前的实收利息(包含计提月)
        BigDecimal beforeAcutalInteresAmountIncludeCurMonth = beforeRepaymentPlanEntities.stream().
                filter(e -> e.getRentAmount() != null && e.getRentAmount().compareTo(BigDecimal.ZERO) > 0).
                map(RepaymentPlanEntity::getActualRepaymentInteresAmount).filter(Objects::nonNull).
                reduce(BigDecimal.ZERO, BigDecimal::add);

        // 之前的实收利息(不包含计提月)
        beforeRepaymentPlanEntities = repaymentPlanEntities.stream().filter(
                e -> e.getPlanDate().compareTo(startDayForMonth) < 0).collect(Collectors.toList());
        // 之前的租赁收入
        BigDecimal beforeRentalIncome = beforeRepaymentPlanEntities.stream().
                map(RepaymentPlanEntity::getRentalIncome).filter(Objects::nonNull).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        // 差异
        BigDecimal diff = beforeAcutalInteresAmountIncludeCurMonth.subtract(beforeRentalIncome);

        // 之后的偿还计划
        List<RepaymentPlanEntity> afterRepaymentPlanEntities = repaymentPlanEntities.stream().filter(
                e -> e.getPlanDate().compareTo(startDayForMonth) >= 0).collect(Collectors.toList());
        // 如果之后的偿还计划为空，则把最后一期数据作为之后的偿还计划，用于存放差异
        if (afterRepaymentPlanEntities == null || afterRepaymentPlanEntities.isEmpty()) {
            afterRepaymentPlanEntities.add(repaymentPlanEntities.get(repaymentPlanEntities.size() - 1));
            beforeRepaymentPlanEntities.remove(beforeRepaymentPlanEntities.size() - 1);
        }

        for (int i = 0; i < afterRepaymentPlanEntities.size(); i++) {
            RepaymentPlanEntity entity = afterRepaymentPlanEntities.get(i);
            entity.setRentalIncome(BigDecimal.ZERO);

            if (afterRepaymentPlanEntities.size() == 1) {
                entity.setRentalIncome(diff);

                // 本月之前、之后计算
                entity.setRentalIncomeBeforeTotal(beforeRentalIncome);
                entity.setRentalIncomeAfterTotal(entity.getUnrealizedRevenue().
                        subtract(beforeRentalIncome).subtract(entity.getRentalIncome()));
                break;
            }

            if (DateUtil.isSameMonth(entity.getPlanDate(), startDayForMonth)) {
                if (DateUtil.isLastDayOfMonth(entity.getPlanDate())) {
                    entity.setRentalIncome(diff);

                    // 本月之前、之后计算
                    entity.setRentalIncomeBeforeTotal(beforeRentalIncome);
                    entity.setRentalIncomeAfterTotal(entity.getUnrealizedRevenue().
                            subtract(entity.getRentalIncomeBeforeTotal()).subtract(entity.getRentalIncome()));
                } else {
                    // 本月之前、之后计算
                    entity.setRentalIncomeBeforeTotal(beforeRentalIncome);
                    entity.setRentalIncomeAfterTotal(entity.getUnrealizedRevenue().subtract(beforeRentalIncome));
                }
            } else {
                entity.setRentalIncomeBeforeTotal(beforeRentalIncome.add(diff));
                entity.setRentalIncomeAfterTotal(entity.getUnrealizedRevenue().
                        subtract(entity.getRentalIncomeBeforeTotal()).subtract(entity.getRentalIncome()));
            }
        }

        List<RepaymentPlanEntity> newRepaymentPlanEntityList = new ArrayList<>();
        newRepaymentPlanEntityList.addAll(beforeRepaymentPlanEntities);
        newRepaymentPlanEntityList.addAll(afterRepaymentPlanEntities);
        return newRepaymentPlanEntityList;
    }
}
