package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.ServiceFeeDetailsMapper;
import com.utfinancing.financehub.engine.finance.mapper.ServiceFeeMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IDataExecutionTaskService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.FinhubAmountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-07
 * @Description :  ServiceFee服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServiceFeeServiceImpl extends ServiceImpl<ServiceFeeMapper, ServiceFeeEntity> implements IServiceFeeService {

    public static final String CONTRACT_STATUS_END = "2";
    private final ServiceFeeMapper serviceFeeMapper;

    private final IServiceFeeAsyncService serviceFeeAsyncService;
    private final IServiceFeeDetailsService detailsService;
    private final ServiceFeeDetailsMapper detailsMapper;
    private final IContractService contractService;
    private final IContractMonthService contractMonthService;
    private final IRuleService iRuleService;
    private final RemoteDictService remoteDictService;
    private final IOrgCompanyService orgCompanyService;
    private final IVoucherService voucherService;
    private final IServiceFeePlanService serviceFeePlanService;
    private final IDataExecutionTaskService dataExecutionTaskService;
    private final IContractStatusRecordService contractStatusRecordService;

    @Value("${approve.url.serviceFee-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;

    @Override
    public Long saveServiceFee(ServiceFeeDTO dto) {
        ServiceFeeEntity entity = BeanUtil.copyProperties(dto, ServiceFeeEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateServiceFee(Long id, ServiceFeeDTO dto) {
        ServiceFeeEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ServiceFeeDTO getServiceFeeDTOById(Long id) {
        ServiceFeeEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ServiceFeeDTO.class);
    }

    @Override
    public IPage<ServiceFeeVO> selectPage(ServiceFeeQueryDTO queryDTO) {
        LambdaQueryWrapper<ServiceFeeEntity> queryWrapper = Wrappers.<ServiceFeeEntity>lambdaQuery();
        Date queryDate = queryDTO.getBusinessDate();
        if (null != queryDate) {
            queryDate = DateUtil.beginOfDay(DateUtil.endOfMonth(queryDate));
            queryWrapper.eq(ServiceFeeEntity::getBusinessDate, queryDate);
        }
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), ServiceFeeEntity::getOrgId, queryDTO.getOrgIdList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getServiceOrgIdList()), ServiceFeeEntity::getOrgId, queryDTO.getServiceOrgIdList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList()), ServiceFeeEntity::getProcessStatus, queryDTO.getProcessStatusList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getIdList()), ServiceFeeEntity::getId, queryDTO.getIdList());
        queryWrapper.eq(ObjectUtil.isNotEmpty(queryDTO.getId()), ServiceFeeEntity::getId, queryDTO.getId());
        queryWrapper.eq(ServiceFeeEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        queryWrapper.orderByDesc(ServiceFeeEntity::getBusinessDate, ServiceFeeEntity::getOrgId);
        //这里注入查询条件
        IPage<ServiceFeeEntity> entityIPage = serviceFeeMapper.selectPage(new Page<ServiceFeeEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        IPage<ServiceFeeVO> page = ListBeanUtil.copyPage(entityIPage, ServiceFeeVO.class);
        page.getRecords().forEach(v -> {
            v.setBatchType(BatchTypeEnum.FWFJT.getCode());
            v.setServiceOrgId(v.getOrgId());
        });
        return page;
    }

    @Override
    public IPage<ServiceFeeDetailsVO> selectDetailPage(ServiceFeeDetailsQueryDTO queryDTO) {
        Date queryDate = queryDTO.getBusinessDate();
        if (null != queryDate) {
            queryDate = DateUtil.beginOfDay(DateUtil.endOfMonth(queryDate));
            queryDTO.setBusinessDate(queryDate);
        }
        Date businessStartDate = queryDTO.getBusinessStartDate();
        if (null != businessStartDate) {
            businessStartDate = DateUtil.beginOfMonth(businessStartDate);
            queryDTO.setBusinessStartDate(businessStartDate);
        }
        Date businessEndDate = queryDTO.getBusinessEndDate();
        if (null != businessEndDate) {
            businessEndDate = DateUtil.endOfMonth(businessEndDate);
            queryDTO.setBusinessEndDate(businessEndDate);
        }
        Page<ServiceFeeDetailsVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<ServiceFeeDetailsVO> serviceFeeDetailsVOS = detailsMapper.selectServiceFeeInfo(page, queryDTO);
        List<String> contractCodes = serviceFeeDetailsVOS.stream().map(ServiceFeeDetailsVO::getContractCode).collect(Collectors.toList());
        setPeriodAmount(queryDTO.getBusinessStartDate(), queryDTO.getBusinessEndDate(), contractCodes, serviceFeeDetailsVOS);
        checkTransferStatus(serviceFeeDetailsVOS, contractCodes, queryDTO.getBusinessDate());
        page.setRecords(serviceFeeDetailsVOS);
        return page;
    }

    private void checkTransferStatus(List<ServiceFeeDetailsVO> serviceFeeDetailsVOS, List<String> contractCodes, Date businessDate) {
        if (CollectionUtils.isEmpty(contractCodes)) {
            return;
        }
//        List<ContractStatusRecordEntity> finContractStatusByContractCodes = contractStatusRecordService.listFinContractStatusByContractCodes(contractCodes, Lists.newArrayList(FinancialContractStatusEnum.ASSET_DISPOSAL_INNER_TRANSFER.getDesc()));
        List<ContractStatusRecordEntity> finContractStatusByContractCodes = contractStatusRecordService.listFinContractStatusByContractCodes(contractCodes, Lists.newArrayList());
        if (CollectionUtils.isNotEmpty(finContractStatusByContractCodes)) {
            List<ContractStatusRecordEntity> finContractStatusByContractCodesGroupByContractCode = finContractStatusByContractCodes.stream().collect(Collectors.groupingBy(ContractStatusRecordEntity::getFinancialContractStatus)).get(FinancialContractStatusEnum.ASSET_DISPOSAL_INNER_TRANSFER.getDesc());
            Map<String, List<ContractStatusRecordEntity>> collect = finContractStatusByContractCodesGroupByContractCode.stream().collect(Collectors.groupingBy(ContractStatusRecordEntity::getContractCode));
            serviceFeeDetailsVOS.forEach(v -> {
                if (CollectionUtils.isNotEmpty(collect.get(v.getContractCode()))) {
                    Optional<ContractStatusRecordEntity> max = collect.get(v.getContractCode()).stream()
                            .filter(a -> a.getFinancialContractStatusUpdateTime().before(DateUtil.offset(businessDate, DateField.MONTH, 1)))
                            .max(Comparator.comparing(ContractStatusRecordEntity::getFinancialContractStatusUpdateTime));
                    if (max.isPresent() && FinancialContractStatusEnum.ASSET_DISPOSAL_INNER_TRANSFER.getDesc().equals(max.get().getFinancialContractStatus())) {
                        v.setExceptionType(v.getExceptionType() + "合同内部转让");
                    }

                }
            });
        }
    }

    private void setPeriodAmount(Date businessStartDate, Date businessEndDate, List<String> contractCodes, List<ServiceFeeDetailsVO> serviceFeeDetailsVOS) {
        if (CollectionUtils.isNotEmpty(contractCodes)) {
            LambdaQueryWrapper<ServiceFeePlanEntity> serviceFeePlanQueryWrapper = new LambdaQueryWrapper<>();
            serviceFeePlanQueryWrapper.in(ServiceFeePlanEntity::getContractCode, contractCodes);
            serviceFeePlanQueryWrapper.eq(ServiceFeePlanEntity::getDelFlag, YesOrNoEnum.NO.getCode());
            List<ServiceFeePlanEntity> serviceFeePlanEntities = serviceFeePlanService.list(serviceFeePlanQueryWrapper);

            Map<String, List<ServiceFeePlanEntity>> serviceFeePlanGroupByContractCode = serviceFeePlanEntities.stream().collect(Collectors.groupingBy(ServiceFeePlanEntity::getContractCode));
            for (ServiceFeeDetailsVO serviceFeeDetailsVO : serviceFeeDetailsVOS) {
                List<ServiceFeePlanEntity> serviceFeePlanEntities1 = serviceFeePlanGroupByContractCode.get(serviceFeeDetailsVO.getContractCode());
                if (CollectionUtils.isNotEmpty(serviceFeePlanEntities1) && businessStartDate != null && businessEndDate != null) {
                    List<ServiceFeePlanEntity> collect = serviceFeePlanEntities1.stream().filter(v -> v.getContractCode().equals(serviceFeeDetailsVO.getContractCode()) &&
                            v.getServiceFeeNo().equals(serviceFeeDetailsVO.getServiceFeeNo()) && v.getOrgId().equals(serviceFeeDetailsVO.getOrgId()) &&
                            v.getServiceOrgId().equals(serviceFeeDetailsVO.getServiceOrgId())).collect(Collectors.toList());
                    BigDecimal before = collect.stream().filter(v -> v.getPlanDate().compareTo(businessStartDate) < 0).map(ServiceFeePlanEntity::getPlanAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal after = collect.stream().filter(v -> v.getPlanDate().compareTo(businessEndDate) > 0).map(ServiceFeePlanEntity::getPlanAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal current = collect.stream().filter(v -> v.getPlanDate().compareTo(businessStartDate) >= 0 && v.getPlanDate().compareTo(businessEndDate) <= 0).map(ServiceFeePlanEntity::getPlanAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    serviceFeeDetailsVO.setBeforePeriodAmountTaxIncluded(before);
                    serviceFeeDetailsVO.setCurrentPeriodAmountTaxIncluded(current);
                    serviceFeeDetailsVO.setAfterPeriodAmountTaxIncluded(serviceFeeDetailsVO.getServiceFeeAllocationTaxIncluded().subtract(before).subtract(current));
                }
                serviceFeeDetailsVO.setEndSharingServiceFeeFlagStr(YesOrNoEnum.getDescByBool(serviceFeeDetailsVO.getEndSharingServiceFeeFlag()));
                serviceFeeDetailsVO.setSharedFlagStr(YesOrNoEnum.getDescByBool(serviceFeeDetailsVO.getSharedFlag()));
                serviceFeeDetailsVO.setSpecialStatusAdjustmentFlagStr(YesOrNoEnum.getDescByBool(serviceFeeDetailsVO.getSpecialStatusAdjustmentFlag()));
                serviceFeeDetailsVO.setVoucherStatus(YesOrNoEnum.YES.getCode().equals(serviceFeeDetailsVO.getIsGenerateVoucher()) ? "已生成" : "未生成");
                serviceFeeDetailsVO.setAllocationCompletionMark(serviceFeeDetailsVO.getEndSharingServiceFeeFlag() ? "1" : "0");
                //设置上月分摊金额
                setLastMonthAllocationAmount(serviceFeeDetailsVO);
                if (serviceFeeDetailsVO.getLastMonthServiceFeeAllocationTaxIncluded().compareTo(BigDecimal.ZERO) != 0) {
                    serviceFeeDetailsVO.setReclassificationAdjustmentNoTaxAmount(serviceFeeDetailsVO.getLastMonthServiceFeeAllocationNoTax().subtract(serviceFeeDetailsVO.getServiceFeeAllocationNoTax()));
                    serviceFeeDetailsVO.setReclassificationAdjustmentTaxIncluded(serviceFeeDetailsVO.getLastMonthServiceFeeAllocationTaxIncluded().subtract(serviceFeeDetailsVO.getServiceFeeAllocationTaxIncluded()));
                }
                serviceFeeDetailsVO.setBusinessName(BusinessEnum.getDescByCode(serviceFeeDetailsVO.getBusinessCode()));
            }
        }
    }

    private void setLastMonthAllocationAmount(ServiceFeeDetailsVO serviceFeeDetailsVO) {
        List<ServiceFeeDetailsEntity> lastServiceFeeDetail = detailsService.selectLastPeriodData(serviceFeeDetailsVO);
        serviceFeeDetailsVO.setAccrualType("");
        if (CollectionUtils.isNotEmpty(lastServiceFeeDetail)) {
            serviceFeeDetailsVO.setLastMonthServiceFeeAllocationNoTax(lastServiceFeeDetail.get(0).getServiceFeeAllocationNoTax());
            serviceFeeDetailsVO.setLastMonthServiceFeeAllocationTaxIncluded(lastServiceFeeDetail.get(0).getServiceFeeAllocationTaxIncluded());
            if (serviceFeeDetailsVO.getLastMonthServiceFeeAllocationTaxIncluded().compareTo(serviceFeeDetailsVO.getServiceFeeAllocationTaxIncluded()) != 0) {
                serviceFeeDetailsVO.setAccrualType(AccrualTypeEnum.ADJUST.getCode());
            }
        } else {
            serviceFeeDetailsVO.setLastMonthServiceFeeAllocationNoTax(BigDecimal.ZERO);
            serviceFeeDetailsVO.setLastMonthServiceFeeAllocationTaxIncluded(BigDecimal.ZERO);
            if (ObjectUtil.isNull(serviceFeeDetailsVO.getLastMonthServiceFeeAllocationTaxIncluded()) || serviceFeeDetailsVO.getLastMonthServiceFeeAllocationTaxIncluded().compareTo(BigDecimal.ZERO) == 0) {
                serviceFeeDetailsVO.setAccrualType(AccrualTypeEnum.ADDITIONAL.getCode());
            }
        }
    }

    @Override
    public List<ServiceFeeDetailsVO> selectExportDetailList(ServiceFeeDetailsQueryDTO queryDTO) {
        List<ServiceFeeDetailsVO> serviceFeeDetailsVOS = detailsMapper.selectDetailList(queryDTO);

        if (CollectionUtils.isNotEmpty(serviceFeeDetailsVOS)) {
            List<String> contractCodes = serviceFeeDetailsVOS.stream().map(ServiceFeeDetailsVO::getContractCode).collect(Collectors.toList());
            setPeriodAmount(queryDTO.getBusinessStartDate(), queryDTO.getBusinessEndDate(), contractCodes, serviceFeeDetailsVOS);
            checkTransferStatus(serviceFeeDetailsVOS, contractCodes, queryDTO.getBusinessDate());
            //查询上月有分摊，本月没分摊的数据
            List<String> orgIds = serviceFeeDetailsVOS.stream().map(ServiceFeeDetailsVO::getServiceOrgId).collect(Collectors.toList());
            String lastMonth = DateUtil.format(DateUtil.offset(serviceFeeDetailsVOS.get(0).getBusinessDate(), DateField.MONTH, -1), "yyyy-MM");
            List<ServiceFeeDetailsEntity> serviceFeeDetailsEntities = detailsMapper.selectList(new LambdaQueryWrapper<ServiceFeeDetailsEntity>()
                    .in(ServiceFeeDetailsEntity::getServiceOrgId, orgIds)
                    .notIn(ServiceFeeDetailsEntity::getContractCode, contractCodes)
                    .apply("to_char(business_date, 'yyyy-MM') = {0}", lastMonth));
            if (CollectionUtils.isNotEmpty(serviceFeeDetailsEntities)) {
                for (ServiceFeeDetailsEntity serviceFeeDetailsEntity : serviceFeeDetailsEntities) {
                    ServiceFeeDetailsVO serviceFeeDetailsVO = new ServiceFeeDetailsVO();
                    BeanUtil.copyProperties(serviceFeeDetailsEntity, serviceFeeDetailsVO);
                    serviceFeeDetailsVO.setBeforePeriodAmountTaxIncluded(serviceFeeDetailsVO.getServiceFeeAllocationTaxIncluded());
                    serviceFeeDetailsVO.setCurrentPeriodAmountTaxIncluded(BigDecimal.ZERO);
                    serviceFeeDetailsVO.setAfterPeriodAmountTaxIncluded(BigDecimal.ZERO);
                    serviceFeeDetailsVO.setBeforeThisMonthAmountNoTax(serviceFeeDetailsVO.getBeforeThisMonthAmountNoTax().add(serviceFeeDetailsVO.getThisMonthAdjustmentAmountNoTax()));
                    serviceFeeDetailsVO.setThisMonthAdjustmentAmountNoTax(BigDecimal.ZERO);
                    serviceFeeDetailsVO.setAllocationBeforeThisMonthBalanceNoTax(BigDecimal.ZERO);
                    serviceFeeDetailsVO.setEndSharingServiceFeeFlagStr(YesOrNoEnum.YES.getDesc());
                    serviceFeeDetailsVO.setAllocationCompletionMark("1");
                    serviceFeeDetailsVO.setSharedFlagStr(YesOrNoEnum.NO.getDesc());
                    serviceFeeDetailsVO.setVoucherStatus(YesOrNoEnum.YES.getCode().equals(serviceFeeDetailsVO.getIsGenerateVoucher()) ? "已生成" : "未生成");
                    //设置上月分摊金额
                    setLastMonthAllocationAmount(serviceFeeDetailsVO);
                    if (serviceFeeDetailsVO.getLastMonthServiceFeeAllocationNoTax().compareTo(BigDecimal.ZERO) != 0) {
                        serviceFeeDetailsVO.setReclassificationAdjustmentNoTaxAmount(serviceFeeDetailsVO.getLastMonthServiceFeeAllocationNoTax().subtract(serviceFeeDetailsVO.getServiceFeeAllocationNoTax()));
                        serviceFeeDetailsVO.setReclassificationAdjustmentTaxIncluded(serviceFeeDetailsVO.getLastMonthServiceFeeAllocationTaxIncluded().subtract(serviceFeeDetailsVO.getServiceFeeAllocationTaxIncluded()));
                    }
                    serviceFeeDetailsVOS.add(serviceFeeDetailsVO);
                }
            }
        }
        translateDict(serviceFeeDetailsVOS);


        return serviceFeeDetailsVOS;
    }

    private void translateDict(List<ServiceFeeDetailsVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<ServiceFeeEntity> serviceFeeEntities = listByIds(list.stream().map(e -> e.getServiceFeeId()).distinct().collect(Collectors.toList()));
        Map<Long, String> processStatusMap = serviceFeeEntities.stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getProcessStatus()));
        //业务合同状态
        R<List<SysDictData>> contractStatusR = remoteDictService.listDictData(DictTypeEnum.CONTRACT_STATUS.getCode());
        Map<String, String> contractStatusMap = contractStatusR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        //财务合同状态
        R<List<SysDictData>> financialContractStatusR = remoteDictService.listDictData(DictTypeEnum.FINANCIAL_CONTRACT_STATUS.getCode());
        Map<String, String> financialContractStatusMap = financialContractStatusR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        //签约主体
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgId(), e -> e.getOrgName(), (a, b) -> b));

        List<ContractMonthEntity> contractMonths = contractMonthService.list(
                new LambdaQueryWrapper<ContractMonthEntity>().in(ContractMonthEntity::getContractCodeM, list.stream().map(ServiceFeeDetailsVO::getContractCode).distinct().collect(Collectors.toList())));
        Map<String, ContractMonthEntity> collect = contractMonths.stream().collect(Collectors.toMap(ContractMonthEntity::getContractCode, e -> e, (k1, k2) -> k2));
        list.forEach(e -> {
            // 签约主体
            e.setOrgId(companyMap.get(e.getOrgId()));
            // 服务费签约主体
            e.setServiceOrgId(companyMap.get(e.getServiceOrgId()));
            // 处理状态
            e.setProcessStatus(MarginStatusEnum.getDescByCode(processStatusMap.get(e.getServiceFeeId())));
            // 业务类型
            e.setBusinessName(BusinessEnum.getDescByCode(e.getBusinessCode()));
        });
    }

    @Override
    public List<ServiceFeePlanVO> selectDetailPlanList(ServiceFeeDetailsQueryDTO queryDTO) {
        String contractCode = queryDTO.getContractCode();
        if (StringUtils.isBlank(contractCode)) {
            return new ArrayList<>();
        }
        List<ServiceFeePlanVO> repaymentPlanVOS = serviceFeeMapper.selectRepaymentPlanByContractCode(contractCode, queryDTO.getServiceFeeNo());
        if (CollectionUtils.isNotEmpty(repaymentPlanVOS)) {
            for (ServiceFeePlanVO repaymentPlanVO : repaymentPlanVOS) {
                repaymentPlanVO.setActualReceiveServiceFeeNoTax(FinhubAmountUtils.amountNoTax(repaymentPlanVO.getActualReceiveServiceFee()));
                repaymentPlanVO.setAgreedApportionAmountNoTax(FinhubAmountUtils.amountNoTax(repaymentPlanVO.getAgreedApportionAmount()));
                repaymentPlanVO.setActualAccruedAmount(repaymentPlanVO.getThisMonthAdjustmentAmountNoTax());
                repaymentPlanVO.setAdjustAmount(repaymentPlanVO.getThisMonthAdjustmentAmountNoTax());
                repaymentPlanVO.setBusinessName(BusinessEnum.getDescByCode(repaymentPlanVO.getBusinessCode()));
            }
        }
        return repaymentPlanVOS;
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        removeBatchByIds(ids);
        LambdaUpdateWrapper<ServiceFeeDetailsEntity> wrapper = new LambdaUpdateWrapper<>();
        //删除详情表
        wrapper.in(ServiceFeeDetailsEntity::getServiceFeeId, ids);
        detailsService.remove(wrapper);
    }

    @Override
    public void submit(List<Long> ids) {
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<ServiceFeeEntity> entityList = this.listByIds(ids);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())
                    || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus())
                    || ProcessStatusEnum.WRITEOFF.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入、已拒绝、已冲销的才可以提交");
            }
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.FWFJT.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);
        });
        // 生成凭证
        Boolean generateVoucherFlag = voucher(ids, YesOrNoEnum.YES.getCode());
        if (generateVoucherFlag) {
            // 发送审核
            Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);

            List<ServiceFeeEntity> newEntityList = this.listByIds(ids);
            newEntityList.stream().forEach(v -> {
                v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                    v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
                }
            });

            // 凭证生成成功
            this.updateBatchById(newEntityList);
        } else {
            // 凭证生成失败
            throw new ServiceException("凭证生成失败，提交失败");
        }
    }

    @Override
    public void withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<ServiceFeeEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(entityList.stream().map(ServiceFeeEntity::getProcessInstanceId).collect(Collectors.toList()));
        this.updateBatchById(entityList);
        if (entityList.get(0).getAccountDate() != null) {
            detailsService.updateVoucherStatus(ids, ProcessStatusEnum.ENTERED.getCode(), Integer.valueOf(DateUtil.format(entityList.get(0).getAccountDate(), "yyyyMM")));
        }
    }

    @Override
    public Boolean voucher(List<Long> ids, String isSubmit) {
        List<ServiceFeeEntity> serviceFeeEntities = listByIds(ids);
        List<Long> deleteIds = serviceFeeEntities.stream().filter(v -> !ProcessStatusEnum.WRITEOFF.getCode().equals(v.getProcessStatus())).map(ServiceFeeEntity::getId).collect(Collectors.toList());

        List<ServiceFeeDetailsEntity> detailsEntities = detailsService.getBaseMapper().selectList(Wrappers.<ServiceFeeDetailsEntity>lambdaQuery()
                .in(ServiceFeeDetailsEntity::getServiceFeeId, serviceFeeEntities.stream().map(ServiceFeeEntity::getId).collect(Collectors.toList())));
        if (CollectionUtils.isEmpty(detailsEntities)) {
            throw new ServiceException("缺少分摊信息");
        }
        Date accountDate = DateUtil.beginOfDay(detailsEntities.get(0).getBusinessDate());
        // 查询合同信息
        List<ContractDTO> contractDTOS = contractService.listContractDTOByCodeList(detailsEntities.stream().map(ServiceFeeDetailsEntity::getContractCode).distinct().collect(Collectors.toList()));
        Map<String, ContractDTO> contractMap = contractDTOS.stream().collect(Collectors.toMap(ContractDTO::getContractCode, e -> e, (a, b) -> b));

        // 生成凭证前先删除之前的凭证
        if (CollectionUtils.isNotEmpty(deleteIds)) {
            batchDeleteVoucher(ids);
        }

        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        for (ServiceFeeDetailsEntity entity : detailsEntities) {
            ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
            commonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            commonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            commonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
            commonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
            commonDTO.setOrderId(entity.getId().toString());
            commonDTO.setContractCode(entity.getContractCode());
            ContractDTO contractDTO = contractMap.get(entity.getContractCode());
            if (null != contractDTO) {
                commonDTO.setContractName(contractDTO.getContractName());
                commonDTO.setCurrencyType(contractDTO.getCurrencyType());
            }
            commonDTO.setClientCode(entity.getClientCode());
            commonDTO.setClientName(entity.getClientName());
            commonDTO.setSceneCode(SceneEnum.FWFJT.getCode());
            commonDTO.setSceneName(SceneEnum.FWFJT.getDesc());
            commonDTO.setOrgId(entity.getOrgId());
            commonDTO.setBusinessDate(entity.getBusinessDate());
            commonDTO.setBatchId(entity.getServiceFeeId());
            commonDTO.setBatchType(BatchTypeEnum.FWFJT.getCode());
            commonDTO.setIsSubmit(isSubmit);
            Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);
            // 分摊方式
            commonMap.put("splitMethod", entity.getAllocationMethod());
            // 是否计提
            commonMap.put("isAccrual", "1");
            // 服务费分摊金额
            commonMap.put("serviceSplit", entity.getThisMonthAdjustmentAmountNoTax());
            // 服务费调整金额
            commonMap.put("serviceAdjust", entity.getReclassificationAdjustmentNoTaxAmount());
            commonMap.put("accrualMonth", DateUtil.format(entity.getBusinessDate(), "yyyy-MM"));
            voucherMapList.add(commonMap);
        }
        log.info("生成凭证参数：{}", JSON.toJSONString(voucherMapList));
        //多个线程更新客户表锁表优化
        Map<Integer, List<Map<String, Object>>> ruleListMap = new HashMap<>();
        Map<String, Integer> clientCount = new HashMap<>();
        for (Map<String, Object> stringObjectMap : voucherMapList) {
            String clientCode = MapUtil.getStr(stringObjectMap, RuleConstant.FIELD_CLIENT_CODE);
            if (clientCount.get(clientCode) == null) {
                if (ruleListMap.get(0) == null) {
                    List<Map<String, Object>> mateList = new ArrayList<>();
                    mateList.add(stringObjectMap);
                    ruleListMap.put(0, mateList);
                } else {
                    ruleListMap.get(0).add(stringObjectMap);
                }
                clientCount.put(clientCode, 1);
            } else {
                if (ruleListMap.get(clientCount.get(clientCode) + 1) == null) {
                    List<Map<String, Object>> mateList = new ArrayList<>();
                    mateList.add(stringObjectMap);
                    ruleListMap.put(clientCount.get(clientCode) + 1, mateList);
                } else {
                    ruleListMap.get(clientCount.get(clientCode) + 1).add(stringObjectMap);
                }
                clientCount.put(clientCode, clientCount.get(clientCode) + 1);
            }
        }
        List<VoucherInfoVO> resultList = new ArrayList<>();
        ruleListMap.forEach((k, v) -> {
            resultList.addAll(iRuleService.batchExecuteRule(v));
        });

        Boolean isExistVoucherError = resultList.stream().allMatch(v -> com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotEmpty(v.getErrorInfo()));
        if (YesOrNoEnum.YES.getCode().equals(isSubmit) && isExistVoucherError) {
            // 异步删除已生成的凭证
            List<Long> voucherIdList = Lists.newArrayList();
            resultList.stream().forEach(voucherInfoVO -> {
                if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(voucherInfoVO.getVoucherDTOList())) {
                    voucherIdList.addAll(voucherInfoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).collect(Collectors.toList()));
                }
            });
            asnyDeleteVoucher(voucherIdList);
            return Boolean.FALSE;
        }
        if (resultList.isEmpty()) {
            log.error("服务费计提 生成凭证失败");
        } else {
            for (VoucherInfoVO entry : resultList) {
                List<VoucherDTO> value = entry.getVoucherDTOList();
                if (CollectionUtils.isNotEmpty(value)) {
                    serviceFeeEntities.forEach(e -> {
                        e.setIsGenerateVoucher(YesOrNoEnum.YES.getCode());
                        e.setAccountDate(accountDate);
                    });
                    updateBatchById(serviceFeeEntities);

                    String vouchIds = value.stream().map(VoucherDTO::getId).map(String::valueOf).collect(
                            Collectors.joining(","));
                    detailsService.lambdaUpdate()
                            .set(ServiceFeeDetailsEntity::getVoucherId, vouchIds)
                            .set(ServiceFeeDetailsEntity::getAccountDate, accountDate)
                            .set(ServiceFeeDetailsEntity::getIsGenerateVoucher, YesOrNoEnum.YES.getCode())
                            .set(ServiceFeeDetailsEntity::getExceptionType, entry.getErrorInfo())
                            .eq(ServiceFeeDetailsEntity::getId, Long.parseLong(entry.getOrderId()))
                            .update();
                }
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 异步删除凭证
     *
     * @param voucherIdList
     */
    public void asnyDeleteVoucher(List<Long> voucherIdList) {
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(voucherIdList)) {
            return;
        }
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // 异步任务的代码
            voucherService.deleteByIdList(voucherIdList);
        });
    }


    @Override
    public void reversalVoucher(List<Long> ids) {
        // 查询信息
        List<ServiceFeeEntity> serviceFeeEntities = listByIds(ids);
        // 校验是否有不是 已复核的
        if (serviceFeeEntities.stream().anyMatch(e -> !MarginStatusEnum.PASS.getCode().equals(e.getProcessStatus()))) {
            throw new ServiceException("存在非已复核数据");
        }
        // todo 重新生成冲销凭证
        List<ServiceFeeDetailsEntity> detailsEntities = detailsService.getBaseMapper().selectList(Wrappers.<ServiceFeeDetailsEntity>lambdaQuery()
                .in(ServiceFeeDetailsEntity::getServiceFeeId, serviceFeeEntities.stream().map(ServiceFeeEntity::getId).collect(Collectors.toList())));
        List<VoucherCopyDTO> copyDTOList = new ArrayList<>();
        detailsEntities.forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherId())) {
                VoucherCopyDTO voucherCopyDTO = new VoucherCopyDTO();
                voucherCopyDTO.setVoucherId(Long.parseLong(v.getVoucherId()));
                voucherCopyDTO.setSourceFromType("0");
                copyDTOList.add(voucherCopyDTO);
            }
        });
        voucherService.writeOff(copyDTOList);
        // 置为提交
        Long userId = SecurityUtils.getUserId();
        serviceFeeEntities.forEach(e -> {
            e.setSubmitBy(String.valueOf(userId));
            e.setProcessStatus(MarginStatusEnum.WRITE_OFF.getCode());
        });
        updateBatchById(serviceFeeEntities);
    }

    @Override
    public R importData(List<ServiceFeeImport> list) {
        String checkResult = checkDate(list);
        if (StringUtils.isNotEmpty(checkResult)) {
            return R.fail(checkResult);
        }
        // 查询数据是否提交或复核
        List<Date> businessDates = list.stream().map(ServiceFeeImport::getBusinessDate).collect(Collectors.toList());
        LambdaQueryWrapper<ServiceFeeEntity> serviceFeeEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        serviceFeeEntityLambdaQueryWrapper.in(ServiceFeeEntity::getBusinessDate, businessDates.stream()
                .map(DateUtil::endOfMonth)
                .map(DateUtil::beginOfDay)
                .collect(Collectors.toList()));
        List<ServiceFeeEntity> serviceFees = this.list(serviceFeeEntityLambdaQueryWrapper);
        Map<String, List<Date>> serviceFeeMap = serviceFees.stream()
                .filter(e -> MarginStatusEnum.PASS.getCode().equals(e.getProcessStatus()) || MarginStatusEnum.SUBMITTED.getCode().equals(e.getProcessStatus()))
                .collect(Collectors.groupingBy(ServiceFeeEntity::getOrgId, Collectors.mapping(ServiceFeeEntity::getBusinessDate, Collectors.toList())));

        //月末+合同
        List<ServiceFeeDetailsEntity> entityList = new ArrayList<>();
        List<ContractEntity> contractEntities = new ArrayList<>();
        for (ServiceFeeImport payableInsuranceDetailImport : list) {

            ContractEntity contractEntity = new ContractEntity();
            //变更服务费分摊信息
            LambdaQueryWrapper<ServiceFeeDetailsEntity> queryWrapper = Wrappers.<ServiceFeeDetailsEntity>lambdaQuery();
            Date queryDate = payableInsuranceDetailImport.getBusinessDate();
            if (null != queryDate) {
                queryDate = DateUtil.beginOfDay(DateUtil.endOfMonth(queryDate));
                queryWrapper.eq(ServiceFeeDetailsEntity::getBusinessDate, queryDate);
            }
            queryWrapper.eq(ServiceFeeDetailsEntity::getContractCode, payableInsuranceDetailImport.getContractCode());
            queryWrapper.eq(ServiceFeeDetailsEntity::getServiceFeeNo, payableInsuranceDetailImport.getServiceFeeNo());
            queryWrapper.last("limit 1");
            ServiceFeeDetailsEntity entity = detailsService.getBaseMapper().selectOne(queryWrapper);
            if (entity == null) {
                return R.fail(String.format("合同号：%s计提数据不存在!", payableInsuranceDetailImport.getContractCode()));
            }
            if (serviceFeeMap.get(entity.getServiceOrgId()) != null && serviceFeeMap.get(entity.getServiceOrgId()).contains(DateUtil.beginOfDay(DateUtil.endOfMonth(payableInsuranceDetailImport.getBusinessDate())))) {
                return R.fail(String.format("计提月份：%s已提交或复核，不能重复提交", DateUtil.format(payableInsuranceDetailImport.getBusinessDate(), DatePattern.NORM_MONTH_PATTERN)));
            }
            if (YesOrNoEnum.YES.getCode().equals(payableInsuranceDetailImport.getEndSharingServiceFeeFlag())) {
//                entity.setXYearMonthAdjustmentAmount();
                entity.setThisMonthAdjustmentAmountNoTax(entity.getServiceFeeAllocationNoTax().subtract(entity.getBeforeThisMonthAmountNoTax()));
                entity.setThisMonthAdjustmentAmountTaxIncluded(entity.getServiceFeeAllocationTaxIncluded().subtract(entity.getBeforeThisMonthAmountTaxIncluded()));
                entity.setAllocationBeforeThisMonthBalanceNoTax(BigDecimal.ZERO);
                entity.setAllocationBeforeThisMonthBalanceTaxIncluded(BigDecimal.ZERO);

            } else {
                if (payableInsuranceDetailImport.getAccruedAmount() != null) {
                    entity.setXYearMonthAdjustmentAmount(payableInsuranceDetailImport.getAccruedAmount());
                    entity.setThisMonthAdjustmentAmountNoTax(payableInsuranceDetailImport.getAccruedAmount());
                    entity.setThisMonthAdjustmentAmountTaxIncluded(FinhubAmountUtils.amountTaxIncluded(payableInsuranceDetailImport.getAccruedAmount()));
                    entity.setAllocationBeforeThisMonthBalanceNoTax(entity.getServiceFeeAllocationNoTax().subtract(entity.getBeforeThisMonthAmountNoTax()).subtract(entity.getThisMonthAdjustmentAmountNoTax()));
                    entity.setAllocationBeforeThisMonthBalanceTaxIncluded(entity.getServiceFeeAllocationTaxIncluded().subtract(entity.getBeforeThisMonthAmountTaxIncluded()).subtract(entity.getThisMonthAdjustmentAmountTaxIncluded()));

                }
            }
            entity.setAllocationMethod(payableInsuranceDetailImport.getAllocationMethod());
            entityList.add(entity);
            contractEntity.setContractCode(payableInsuranceDetailImport.getContractCode());
            if (StringUtils.isNotEmpty(payableInsuranceDetailImport.getSharingServiceFeeFlag())) {
                contractEntity.setSharingServiceFeeFlag(YesOrNoEnum.YES.getCode().equals(payableInsuranceDetailImport.getSharingServiceFeeFlag()));
            }
            if (StringUtils.isNotEmpty(payableInsuranceDetailImport.getEndSharingServiceFeeFlag())) {
                contractEntity.setEndSharingServiceFeeFlag(YesOrNoEnum.YES.getCode().equals(payableInsuranceDetailImport.getEndSharingServiceFeeFlag()));
            }
            if (contractEntity.getEndSharingServiceFeeFlag() != null && contractEntity.getEndSharingServiceFeeFlag()) {
                int periodCode = Integer.parseInt(Objects.requireNonNull(DateUtils.format(queryDate, "yyyyMM")));
                contractEntity.setSetEndSharingServiceFeePeriod(periodCode);
            }
            contractEntities.add(contractEntity);
        }

        if (CollectionUtils.isNotEmpty(entityList)) {
            detailsService.updateBatchById(entityList);
            updateServiceFeeByIdList(entityList.stream().map(ServiceFeeDetailsEntity::getServiceFeeId).distinct().collect(Collectors.toList()));
        }

        //更新合同字段
        contractService.updateServiceShareFlagByContractCode(contractEntities);
        return R.ok();
    }

    private String checkDate(List<ServiceFeeImport> list) {
        for (int i = 0; i < list.size(); i++) {
            if (ObjectUtil.isEmpty(list.get(i).getBusinessDate())) {
                return String.format("%s行数据:计提月份不能为空", String.valueOf(i + 1));
            }
            if (ObjectUtil.isEmpty(list.get(i).getServiceFeeNo())) {
                return String.format("%s行数据:服务费协议编号不能为空", String.valueOf(i + 1));
            }
            if (ObjectUtil.isEmpty(list.get(i).getContractCode())) {
                return String.format("%s行数据:合同编号不能为空", String.valueOf(i + 1));
            }
//            if (ObjectUtil.isEmpty(list.get(i).getAccruedAmount())) {
//                return String.format("%s行数据:计提金额不能为空", String.valueOf(i + 1));
//            }
        }
        return StringUtil.EMPTY;
    }

    private void updateServiceFeeByIdList(List<Long> ids) {
        Map<Long, List<ServiceFeeDetailsEntity>> groupByInsuranceId = detailsService.getBaseMapper()
                .selectList(Wrappers.<ServiceFeeDetailsEntity>lambdaQuery().in(ServiceFeeDetailsEntity::getServiceFeeId, ids))
                .stream().collect(Collectors.groupingBy(e -> e.getServiceFeeId()));
        groupByInsuranceId.forEach((insuranceId, detailsEntityList) -> {
            LambdaUpdateWrapper<ServiceFeeEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
            updateChainWrapper
                    .eq(ServiceFeeEntity::getId, insuranceId)
                    .set(ServiceFeeEntity::getIsGenerateVoucher, YesOrNoEnum.NO.getCode())
                    .set(ServiceFeeEntity::getServiceFeeAmortizationIncome, detailsEntityList.stream().map(ServiceFeeDetailsEntity::getThisMonthAdjustmentAmountNoTax).reduce(BigDecimal.ZERO, BigDecimal::add));
            this.update(updateChainWrapper);
            // 删除凭证
            List<String> voucherIdList = detailsEntityList.stream().map(e -> e.getVoucherId()).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<Long> newVoucherIdList = new ArrayList<>();
            if (voucherIdList != null && !voucherIdList.isEmpty()) {
                for (String voucherIds : voucherIdList) {
                    if (StringUtils.isNotEmpty(voucherIds)) {
                        newVoucherIdList.addAll(Arrays.asList(voucherIds.split(",")).stream().
                                map(e -> Long.parseLong(e.trim())).collect(Collectors.toList()));
                    }
                }
            }
            voucherService.deleteByIdList(newVoucherIdList);
        });
    }

    @Override
    public R measurement(ServiceFeeQueryDTO queryDTO) {
        Date queryDate = queryDTO.getBusinessDate();
        BigDecimal allocationRatio = queryDTO.getAllocationRatio();
        if (null == queryDate) {
            return R.fail("计提月份不能为空!");
        }
        if (null == allocationRatio) {
            return R.fail("分摊比例不能为空!");
        }
        LambdaQueryWrapper<ServiceFeeEntity> queryWrapper = Wrappers.<ServiceFeeEntity>lambdaQuery();
        queryDate = DateUtil.beginOfDay(DateUtil.endOfMonth(queryDate));
        queryWrapper.eq(ServiceFeeEntity::getBusinessDate, queryDate);
        //这里注入查询条件
        List<ServiceFeeEntity> serviceFeeEntities = serviceFeeMapper.selectList(queryWrapper);
        List<ServiceFeeEntity> cantMeasurement = serviceFeeEntities.stream().filter(v -> MarginStatusEnum.cantChangeStatus().contains(v.getProcessStatus())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(cantMeasurement)) {
            return R.fail("非已录入、已拒绝、已冲销状态，不能重新测算!");
        }
//        //校验任务
        Long taskId = dataExecutionTaskService.checkAndCreateTask(BatchTypeEnum.ZXFWF.getCode());
        CompletableFuture.supplyAsync(() -> {
            serviceFeeAsyncService.measurementAsync(queryDTO);
            return 0;
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            dataExecutionTaskService.finishedTask(taskId, DataExecutionTaskStatusEnum.SUCCESS.getCode(), 0, 0);
        }).exceptionally(e -> {
            log.error("咨询服务费测算失败 异步执行异常：", e);
            dataExecutionTaskService.errorTask(taskId, DataExecutionTaskStatusEnum.FAILED.getCode(), 0, 0, e.getMessage());
            // 执行失败
            return null;
        });
        return R.ok("咨询服务费测算中，请稍后查看结果!");
    }

//    public R measurement1(ServiceFeeQueryDTO queryDTO) {
//        Date queryDate = queryDTO.getBusinessDate();
//        BigDecimal allocationRatio = queryDTO.getAllocationRatio();
//        if (null == queryDate) {
//            return R.fail("计提月份不能为空!");
//        }
//        if (null == allocationRatio) {
//            return R.fail("分摊比例不能为空!");
//        }
//
//        // 获取现有数据 排除已经审批的公司
//        List<String> notGenerateOrgIds = deleteNotSubmitAndFilterSubmitedOrg(queryDate);
//        // 取得待摊销的合同列表(2024年以前的数据，根据摊销表数据进行摊销； 2024年以后的合同按照新规则进行摊销)
//        List<ContractEntity> contractEntityList = contractService.selectContractForReceviceServiceAmount(notGenerateOrgIds);
//        if (contractEntityList == null || contractEntityList.isEmpty()) {
//            return R.fail("没有查找到待摊销的合同!");
//        }
//
//        // 取得2024年以前起租的合同-根据摊销表数据进行摊销
//        Date cutOverDate = DateUtils.parseDate("2024-01-01 00:00:00");
//        List<ContractEntity> contractEntityBefore2024List = contractEntityList.stream().
//                filter(e->cutOverDate.after(e.getLeaseDateStart())).collect(Collectors.toList());
//        if (contractEntityBefore2024List != null && !contractEntityBefore2024List.isEmpty()) {
//
//        }
//
//        // 取得2024年以后
//        List<ContractEntity> contractEntityAfter2024List = contractEntityList.stream().
//                filter(e->cutOverDate.after(e.getLeaseDateStart())).collect(Collectors.toList());
//        if (contractEntityAfter2024List != null && !contractEntityAfter2024List.isEmpty()) {
//
//        }
//        return R.ok();
//    }

    /**
     * 2024年之后的服务费分摊规则
     */
//    private void serviceFeeAllocatedForNewRule(ServiceFeeQueryDTO queryDTO, List<ContractEntity> contractEntityAfter2024List) {
//
//        BigDecimal allocationRatioPer = NumberUtil.div(queryDTO.getAllocationRatio(), 100); // 起点比例
//        DateTime endOfMonth = DateUtil.endOfMonth(queryDTO.getBusinessDate());  // 计提月月末
//        DateTime beginOfMonth = DateUtil.beginOfMonth(queryDTO.getBusinessDate()); // 计提月月初
//        DateTime queryDate = DateUtil.beginOfDay(endOfMonth);  // 计提月月末开始的时间
//
//        // 2024年以后-计提年以前的合同列表
//        List<String> lastYearContractCodeList = contractEntityAfter2024List.stream().filter(e -> DateUtil.beginOfYear(beginOfMonth).
//                after(e.getLeaseDateStart())).map(e -> e.getContractCode()).collect(Collectors.toList());
//
//        // 2024以后-计提年当年的合同列表
//        List<String> curYearContractCodeList = contractEntityAfter2024List.stream().filter(e -> !DateUtil.beginOfYear(beginOfMonth).
//                after(e.getLeaseDateStart())).map(e -> e.getContractCode()).collect(Collectors.toList());
//    }

    /**
     * 当年服务费分摊
     */
//    private void curYearServiceFeeApportion(ServiceFeeQueryDTO queryDTO, List<ContractEntity> contractEntityAfter2024List) {
//
//        BigDecimal allocationRatioPer = NumberUtil.div(queryDTO.getAllocationRatio(), 100); // 起点比例
//        DateTime endOfMonth = DateUtil.endOfMonth(queryDTO.getBusinessDate());  // 计提月月末
//        DateTime beginOfMonth = DateUtil.beginOfMonth(queryDTO.getBusinessDate()); // 计提月月初
//        DateTime queryDate = DateUtil.beginOfDay(endOfMonth);  // 计提月月末开始的时间
//
//        List<ContractEntity> curYearContractEntityList = contractEntityAfter2024List.stream().filter(e -> !DateUtil.beginOfYear(beginOfMonth).
//                after(e.getLeaseDateStart())).collect(Collectors.toList());
//        if (curYearContractEntityList == null || curYearContractEntityList.isEmpty()) {
//            return ;
//        }
//
//        // 合同列表
//        List<String> curYearContractCodeList = curYearContractEntityList.stream().map(ContractEntity::getContractCode).
//                collect(Collectors.toList());
//        // 取得偿还计划
//        Map<String, List<RepaymentPlanEntity>> repaymentListMap = this.selectRepaymentList(curYearContractCodeList);
//
//        for (int i = 0; i < curYearContractEntityList.size(); i++) {
//            ContractEntity contractEntity = curYearContractEntityList.get(i);
//            List<RepaymentPlanEntity> repaymentPlanEntityList = repaymentListMap.get(contractEntity.getContractCode());
//            if (repaymentPlanEntityList == null || repaymentPlanEntityList.isEmpty()) {
//                log.error("合同编码：" + contractEntity.getContractCode() + "未找到偿还计划");
//                continue;
//            }
//
//            // 当服务费比例（服务费金额/设备款）超过起点比例时，超过起点比例的部分进行分摊
//            BigDecimal divRate = NumberUtil.div(contractEntity.getReceivableServiceAmount(), contractEntity.getPayableDeviceAmount());
//            if (divRate.compareTo(allocationRatioPer) <= 0) {
//                continue;
//            }
//
//
//        }
//    }

    /**
     * 取得偿还计划
     */
//    private Map<String, List<RepaymentPlanEntity>> selectRepaymentList(List<String> contractCodeList) {
//        LambdaQueryWrapper<RepaymentPlanEntity> queryWrapper = Wrappers.<RepaymentPlanEntity>lambdaQuery();
//        queryWrapper.eq(RepaymentPlanEntity::getDelFlag, YesOrNoEnum.NO.getCode());
//        queryWrapper.in(RepaymentPlanEntity::getContractCode, contractCodeList);
//        queryWrapper.orderByAsc(RepaymentPlanEntity::getContractCode);
//        queryWrapper.orderByAsc(RepaymentPlanEntity::getPlanDate);
//        List<RepaymentPlanEntity> repaymentPlanEntityList = repaymentPlanService.getBaseMapper().selectList(queryWrapper);
//        return repaymentPlanEntityList.stream().collect(Collectors.groupingBy(e->e.getContractCode()));
//    }


    /**
     * 审批修改单据状态
     *
     * @param approveDTO
     */
    @Override
    public void updateProcessStatus(CommonApproveDTO approveDTO) {
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isEmpty(approveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        ServiceFeeEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("咨询服务费分摊数据不存在");
        }
        // 修改凭证状态，通过和驳回都修改
        updateVoucherStatus(Lists.newArrayList(approveDTO.getDocumentId()), approveDTO);
        // 修改状态
        entity.setProcessStatus(approveDTO.getDocumentStatus());
        this.updateById(entity);
    }

    /**
     * 更新凭证状态
     *
     * @param ids
     * @param approveDTO
     */
    public void updateVoucherStatus(List<Long> ids, CommonApproveDTO approveDTO) {
        // 获取所有的凭证Id
        List<VoucherVO> voucherVOList = voucherService.getByBatchIdList(ids, BatchTypeEnum.FWFJT.getCode());
        List<String> voucherIdList = voucherVOList.stream().map(VoucherVO::getId).map(Objects::toString).collect(Collectors.toList());
        // 更新凭证状态
        voucherService.updateStatusByids(voucherIdList, approveDTO.getDocumentStatus(),
                approveDTO.getApproverNum(), approveDTO.getApproverName());
    }

    /**
     * 删除凭证
     *
     * @param ids
     */
    @Override
    public void batchDeleteVoucher(List<Long> ids) {
        //根据批次号删除凭证
        voucherService.deleteByBatchIdList(ids, BatchTypeEnum.FWFJT.getCode());
    }

}

