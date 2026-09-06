package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
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
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.ServiceFeeDetailsNewMapper;
import com.utfinancing.financehub.engine.finance.mapper.ServiceFeeNewMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.FinhubAmountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author : le
 * @Date : Create in 2025-11-10
 * @Description :  ServiceFeeNew服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServiceFeeNewServiceImpl extends ServiceImpl<ServiceFeeNewMapper, ServiceFeeNewEntity> implements IServiceFeeNewService {

    private final ServiceFeeNewMapper serviceFeeNewMapper;
    private final ServiceFeeDetailsNewMapper serviceFeeDetailsNewMapper;
    private final IServiceFeeDetailsNewService serviceFeeDetailsNewService;
    private final IOrgCompanyService orgCompanyService;
    private final IContractService contractService;
    private final IContractStatusRecordService contractStatusRecordService;
    private final IServiceFeePlanNewService serviceFeePlanNewService;
    private final IVoucherService voucherService;
    private final IRuleService iRuleService;
    @Resource
    private IApproveService iApproveService;
    @Value("${approve.url.serviceFee-url:null}")
    private String approveUrl;

    @Override
    public IPage<ServiceFeeNewVO> selectPage(ServiceFeeQueryDTO queryDTO) {
        LambdaQueryWrapper<ServiceFeeNewEntity> queryWrapper = Wrappers.<ServiceFeeNewEntity>lambdaQuery();
        Date queryDate = queryDTO.getBusinessDate();
        if (null != queryDate) {
            queryDate = DateUtil.beginOfDay(DateUtil.endOfMonth(queryDate));
            queryWrapper.eq(ServiceFeeNewEntity::getBusinessDate, queryDate);
        }
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), ServiceFeeNewEntity::getOrgId, queryDTO.getOrgIdList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getServiceOrgIdList()), ServiceFeeNewEntity::getOrgId, queryDTO.getServiceOrgIdList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList()), ServiceFeeNewEntity::getProcessStatus, queryDTO.getProcessStatusList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getIdList()), ServiceFeeNewEntity::getId, queryDTO.getIdList());
        queryWrapper.eq(ObjectUtil.isNotEmpty(queryDTO.getId()), ServiceFeeNewEntity::getId, queryDTO.getId());
        queryWrapper.eq(ServiceFeeNewEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        queryWrapper.orderByDesc(ServiceFeeNewEntity::getBusinessDate, ServiceFeeNewEntity::getOrgId);
        //这里注入查询条件
        IPage<ServiceFeeNewEntity> entityIPage = serviceFeeNewMapper.selectPage(new Page<ServiceFeeNewEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        IPage<ServiceFeeNewVO> page = ListBeanUtil.copyPage(entityIPage, ServiceFeeNewVO.class);
        return page;
    }

    @Override
    public List<ServiceFeeDetailsNewVO> selectExportDetailList(ServiceFeeDetailsQueryDTO queryDTO) {

        List<ServiceFeeDetailsNewVO> serviceFeeDetailsVOS = serviceFeeDetailsNewMapper.selectDetailList(queryDTO);

        if (CollectionUtils.isNotEmpty(serviceFeeDetailsVOS)) {
            List<String> contractCodes = serviceFeeDetailsVOS.stream().map(ServiceFeeDetailsNewVO::getContractCode).collect(Collectors.toList());
            setPeriodAmount(queryDTO.getBusinessStartDate(), queryDTO.getBusinessEndDate(), contractCodes, serviceFeeDetailsVOS);
            checkTransferStatus(serviceFeeDetailsVOS, contractCodes, queryDTO.getBusinessDate());
            //查询上月有分摊，本月没分摊的数据
            List<String> orgIds = serviceFeeDetailsVOS.stream().map(ServiceFeeDetailsNewVO::getServiceOrgId).collect(Collectors.toList());
            String lastMonth = DateUtil.format(DateUtil.offset(serviceFeeDetailsVOS.get(0).getBusinessDate(), DateField.MONTH, -1), "yyyy-MM");
            List<ServiceFeeDetailsNewEntity> serviceFeeDetailsEntities = serviceFeeDetailsNewMapper.selectList(new LambdaQueryWrapper<ServiceFeeDetailsNewEntity>()
                    .in(ServiceFeeDetailsNewEntity::getServiceOrgId, orgIds)
                    .notIn(ServiceFeeDetailsNewEntity::getContractCode, contractCodes)
                    .apply("to_char(business_date, 'yyyy-MM') = {0}", lastMonth));
            if (CollectionUtils.isNotEmpty(serviceFeeDetailsEntities)) {
                for (ServiceFeeDetailsNewEntity ServiceFeeDetailsNewEntity : serviceFeeDetailsEntities) {
                    ServiceFeeDetailsNewVO serviceFeeDetailsVO = new ServiceFeeDetailsNewVO();
                    BeanUtil.copyProperties(ServiceFeeDetailsNewEntity, serviceFeeDetailsVO);

                    serviceFeeDetailsVO.setBeforeCurrentPeriodPlanAmountTaxInclude(serviceFeeDetailsVO.getShouldApportionmentAmountTaxInclude());
                    serviceFeeDetailsVO.setCurrentPeriodPlanAmountTaxInclude(BigDecimal.ZERO);
                    serviceFeeDetailsVO.setAfterCurrentPeriodPlanAmountTaxInclude(BigDecimal.ZERO);
                    serviceFeeDetailsVO.setBeforeAccruedAmount(serviceFeeDetailsVO.getShouldApportionmentAmountNoTax());
                    serviceFeeDetailsVO.setAccruedAmount(BigDecimal.ZERO);
                    serviceFeeDetailsVO.setAfterAccruedAmount(BigDecimal.ZERO);
                    serviceFeeDetailsVO.setVoucherStatus(YesOrNoEnum.YES.getCode().equals(serviceFeeDetailsVO.getIsGenerateVoucher()) ? "已生成" : "未生成");
                    //设置上月分摊金额
                    setLastMonthAllocationAmount(serviceFeeDetailsVO);
                    if (serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountNoTax().compareTo(BigDecimal.ZERO) != 0) {
                        serviceFeeDetailsVO.setThisMonthReclassificationAdjustmentAmountTaxInclude(serviceFeeDetailsVO.getShouldApportionmentAmountNoTax().subtract(serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountNoTax()));
                        serviceFeeDetailsVO.setThisMonthReclassificationAdjustmentAmountNoTax(serviceFeeDetailsVO.getShouldApportionmentAmountTaxInclude().subtract(serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountTaxInclude()));
                    }
                    serviceFeeDetailsVOS.add(serviceFeeDetailsVO);
                }
            }
        }
        translateDict(serviceFeeDetailsVOS);


        return serviceFeeDetailsVOS;
    }

    private void translateDict(List<ServiceFeeDetailsNewVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<ServiceFeeNewEntity> serviceFeeEntities = listByIds(list.stream().map(ServiceFeeDetailsNewVO::getServiceFeeId).distinct().collect(Collectors.toList()));
        Map<Long, String> processStatusMap = serviceFeeEntities.stream().collect(Collectors.toMap(ServiceFeeNewEntity::getId, ServiceFeeNewEntity::getProcessStatus));
        //签约主体
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId, OrgCompanyVO::getOrgName, (a, b) -> b));
        List<ContractEntity> contractMonths = contractService.list(
                new LambdaQueryWrapper<ContractEntity>().in(ContractEntity::getContractCodeM, list.stream().map(ServiceFeeDetailsNewVO::getContractCode).distinct().collect(Collectors.toList())));
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

    private void checkTransferStatus(List<ServiceFeeDetailsNewVO> serviceFeeDetailsVOS, List<String> contractCodes, Date businessDate) {
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

    private void setPeriodAmount(Date businessStartDate, Date businessEndDate, List<String> contractCodes, List<ServiceFeeDetailsNewVO> serviceFeeDetailsVOS) {
        if (CollectionUtils.isNotEmpty(contractCodes)) {
            LambdaQueryWrapper<ServiceFeePlanNewEntity> serviceFeePlanQueryWrapper = new LambdaQueryWrapper<>();
            serviceFeePlanQueryWrapper.in(ServiceFeePlanNewEntity::getContractCode, contractCodes);
            serviceFeePlanQueryWrapper.eq(ServiceFeePlanNewEntity::getDelFlag, YesOrNoEnum.NO.getCode());
            List<ServiceFeePlanNewEntity> serviceFeePlanEntities = serviceFeePlanNewService.list(serviceFeePlanQueryWrapper);

            Map<String, List<ServiceFeePlanNewEntity>> serviceFeePlanGroupByContractCode = serviceFeePlanEntities.stream().collect(Collectors.groupingBy(ServiceFeePlanNewEntity::getContractCode));
            for (ServiceFeeDetailsNewVO serviceFeeDetailsVO : serviceFeeDetailsVOS) {
                List<ServiceFeePlanNewEntity> serviceFeePlanEntities1 = serviceFeePlanGroupByContractCode.get(serviceFeeDetailsVO.getContractCode());
                if (CollectionUtils.isNotEmpty(serviceFeePlanEntities1) && businessStartDate != null && businessEndDate != null) {
                    List<ServiceFeePlanNewEntity> collect = serviceFeePlanEntities1.stream().filter(v -> v.getContractCode().equals(serviceFeeDetailsVO.getContractCode()) &&
                            v.getServiceFeeNo().equals(serviceFeeDetailsVO.getServiceFeeNo()) && v.getOrgId().equals(serviceFeeDetailsVO.getOrgId()) &&
                            v.getServiceOrgId().equals(serviceFeeDetailsVO.getServiceOrgId())).collect(Collectors.toList());
                    BigDecimal before = collect.stream().filter(v -> v.getPlanDate().compareTo(businessStartDate) < 0).map(ServiceFeePlanNewEntity::getPlanAmountTaxInclude).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal after = collect.stream().filter(v -> v.getPlanDate().compareTo(businessEndDate) > 0).map(ServiceFeePlanNewEntity::getPlanAmountTaxInclude).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal current = collect.stream().filter(v -> v.getPlanDate().compareTo(businessStartDate) >= 0 && v.getPlanDate().compareTo(businessEndDate) <= 0).map(ServiceFeePlanNewEntity::getPlanAmountTaxInclude).reduce(BigDecimal.ZERO, BigDecimal::add);
                    serviceFeeDetailsVO.setBeforeCurrentPeriodPlanAmountTaxInclude(before);
                    serviceFeeDetailsVO.setCurrentPeriodPlanAmountTaxInclude(current);
                    serviceFeeDetailsVO.setAfterCurrentPeriodPlanAmountTaxInclude(serviceFeeDetailsVO.getShouldApportionmentAmountTaxInclude().subtract(before).subtract(current));
                }
                serviceFeeDetailsVO.setVoucherStatus(YesOrNoEnum.YES.getCode().equals(serviceFeeDetailsVO.getIsGenerateVoucher()) ? "已生成" : "未生成");
                //设置上月分摊金额
                setLastMonthAllocationAmount(serviceFeeDetailsVO);
                if (serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountTaxInclude().compareTo(BigDecimal.ZERO) != 0) {
                    serviceFeeDetailsVO.setThisMonthReclassificationAdjustmentAmountNoTax(serviceFeeDetailsVO.getShouldApportionmentAmountNoTax().subtract(serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountNoTax()));
                    serviceFeeDetailsVO.setThisMonthReclassificationAdjustmentAmountTaxInclude(serviceFeeDetailsVO.getShouldApportionmentAmountTaxInclude().subtract(serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountTaxInclude()));
                }
                serviceFeeDetailsVO.setBusinessName(BusinessEnum.getDescByCode(serviceFeeDetailsVO.getBusinessCode()));
            }
        }
    }

    private void setLastMonthAllocationAmount(ServiceFeeDetailsNewVO serviceFeeDetailsVO) {
        List<ServiceFeeDetailsNewEntity> lastServiceFeeDetail = serviceFeeDetailsNewService.selectLastPeriodData(serviceFeeDetailsVO);
        serviceFeeDetailsVO.setAccrualType("");
        if (CollectionUtils.isNotEmpty(lastServiceFeeDetail)) {
            serviceFeeDetailsVO.setLastMonthShouldApportionmentAmountNoTax(lastServiceFeeDetail.get(0).getShouldApportionmentAmountNoTax());
            serviceFeeDetailsVO.setLastMonthShouldApportionmentAmountTaxInclude(lastServiceFeeDetail.get(0).getShouldApportionmentAmountTaxInclude());
            if (serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountTaxInclude().compareTo(serviceFeeDetailsVO.getShouldApportionmentAmountTaxInclude()) != 0) {
                serviceFeeDetailsVO.setAccrualType(AccrualTypeEnum.ADJUST.getCode());
            }
        } else {
            serviceFeeDetailsVO.setLastMonthShouldApportionmentAmountNoTax(BigDecimal.ZERO);
            serviceFeeDetailsVO.setLastMonthShouldApportionmentAmountTaxInclude(BigDecimal.ZERO);
            if (ObjectUtil.isNull(serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountTaxInclude()) || serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountTaxInclude().compareTo(BigDecimal.ZERO) == 0) {
                serviceFeeDetailsVO.setAccrualType(AccrualTypeEnum.ADDITIONAL.getCode());
            }
        }
    }

    @Override
    public void reversalVoucher(List<Long> ids) {
        // 查询信息
        List<ServiceFeeNewEntity> serviceFeeEntities = listByIds(ids);
        // 校验是否有不是 已复核的
        if (serviceFeeEntities.stream().anyMatch(e -> !MarginStatusEnum.PASS.getCode().equals(e.getProcessStatus()))) {
            throw new ServiceException("存在非已复核数据");
        }
        List<ServiceFeeDetailsNewEntity> detailsEntities = serviceFeeDetailsNewService.getBaseMapper().selectList(Wrappers.<ServiceFeeDetailsNewEntity>lambdaQuery()
                .in(ServiceFeeDetailsNewEntity::getServiceFeeId, serviceFeeEntities.stream().map(ServiceFeeNewEntity::getId).collect(Collectors.toList())));
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
    public void deleteByIds(List<Long> ids) {
        removeBatchByIds(ids);
        LambdaUpdateWrapper<ServiceFeeDetailsNewEntity> wrapper = new LambdaUpdateWrapper<>();
        //删除详情表
        wrapper.in(ServiceFeeDetailsNewEntity::getServiceFeeId, ids);
        serviceFeeDetailsNewService.remove(wrapper);
    }

    @Override
    public R importData(List<ServiceFeeImport> list) {
        String checkResult = checkDate(list);
        if (StringUtils.isNotEmpty(checkResult)) {
            return R.fail(checkResult);
        }
        // 查询数据是否提交或复核
        List<Date> businessDates = list.stream().map(ServiceFeeImport::getBusinessDate).collect(Collectors.toList());
        LambdaQueryWrapper<ServiceFeeNewEntity> serviceFeeEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        serviceFeeEntityLambdaQueryWrapper.in(ServiceFeeNewEntity::getBusinessDate, businessDates.stream()
                .map(DateUtil::endOfMonth)
                .map(DateUtil::beginOfDay)
                .collect(Collectors.toList()));
        List<ServiceFeeNewEntity> serviceFees = this.list(serviceFeeEntityLambdaQueryWrapper);
        Map<String, List<Date>> serviceFeeMap = serviceFees.stream()
                .filter(e -> MarginStatusEnum.PASS.getCode().equals(e.getProcessStatus()) || MarginStatusEnum.SUBMITTED.getCode().equals(e.getProcessStatus()))
                .collect(Collectors.groupingBy(ServiceFeeNewEntity::getOrgId, Collectors.mapping(ServiceFeeNewEntity::getBusinessDate, Collectors.toList())));

        //月末+合同
        List<ServiceFeeDetailsNewEntity> entityList = new ArrayList<>();
        List<ContractEntity> contractEntities = new ArrayList<>();
        for (ServiceFeeImport payableInsuranceDetailImport : list) {

            ContractEntity contractEntity = new ContractEntity();
            //变更服务费分摊信息
            LambdaQueryWrapper<ServiceFeeDetailsNewEntity> queryWrapper = Wrappers.<ServiceFeeDetailsNewEntity>lambdaQuery();
            Date queryDate = payableInsuranceDetailImport.getBusinessDate();
            if (null != queryDate) {
                queryDate = DateUtil.beginOfDay(DateUtil.endOfMonth(queryDate));
                queryWrapper.eq(ServiceFeeDetailsNewEntity::getBusinessDate, queryDate);
            }
            queryWrapper.eq(ServiceFeeDetailsNewEntity::getContractCode, payableInsuranceDetailImport.getContractCode());
            queryWrapper.eq(ServiceFeeDetailsNewEntity::getServiceFeeNo, payableInsuranceDetailImport.getServiceFeeNo());
            queryWrapper.last("limit 1");
            ServiceFeeDetailsNewEntity entity = serviceFeeDetailsNewService.getBaseMapper().selectOne(queryWrapper);
            if (entity == null) {
                return R.fail(String.format("合同号：%s计提数据不存在!", payableInsuranceDetailImport.getContractCode()));
            }
            if (serviceFeeMap.get(entity.getServiceOrgId()) != null && serviceFeeMap.get(entity.getServiceOrgId()).contains(DateUtil.beginOfDay(DateUtil.endOfMonth(payableInsuranceDetailImport.getBusinessDate())))) {
                return R.fail(String.format("计提月份：%s已提交或复核，不能重复提交", DateUtil.format(payableInsuranceDetailImport.getBusinessDate(), DatePattern.NORM_MONTH_PATTERN)));
            }
            if (YesOrNoEnum.YES.getCode().equals(payableInsuranceDetailImport.getEndSharingServiceFeeFlag())) {
//                entity.setXYearMonthAdjustmentAmount();
                entity.setAccruedAmount(entity.getShouldApportionmentAmountNoTax().subtract(entity.getBeforeAccruedAmount()));
                entity.setAfterAccruedAmount(BigDecimal.ZERO);

            } else {
                if (payableInsuranceDetailImport.getAccruedAmount() != null) {
                    entity.setAccruedAmount(payableInsuranceDetailImport.getAccruedAmount());
                    entity.setAfterAccruedAmount(entity.getShouldApportionmentAmountNoTax().subtract(entity.getBeforeAccruedAmount()).subtract(entity.getAccruedAmount()));

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
            serviceFeeDetailsNewService.updateBatchById(entityList);
            updateServiceFeeByIdList(entityList.stream().map(ServiceFeeDetailsNewEntity::getServiceFeeId).distinct().collect(Collectors.toList()));
        }

        //更新合同字段
        contractService.updateServiceShareFlagByContractCode(contractEntities);
        return R.ok();
    }

    private void updateServiceFeeByIdList(List<Long> ids) {
        Map<Long, List<ServiceFeeDetailsNewEntity>> groupByInsuranceId = serviceFeeDetailsNewService.getBaseMapper()
                .selectList(Wrappers.<ServiceFeeDetailsNewEntity>lambdaQuery().in(ServiceFeeDetailsNewEntity::getServiceFeeId, ids))
                .stream().collect(Collectors.groupingBy(ServiceFeeDetailsNewEntity::getServiceFeeId));
        groupByInsuranceId.forEach((insuranceId, detailsEntityList) -> {
            LambdaUpdateWrapper<ServiceFeeNewEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
            updateChainWrapper
                    .eq(ServiceFeeNewEntity::getId, insuranceId)
                    .set(ServiceFeeNewEntity::getIsGenerateVoucher, YesOrNoEnum.NO.getCode())
                    .set(ServiceFeeNewEntity::getAccruedAmount, detailsEntityList.stream().map(ServiceFeeDetailsNewEntity::getAccruedAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            this.update(updateChainWrapper);
            // 删除凭证
            List<String> voucherIdList = detailsEntityList.stream().map(ServiceFeeDetailsNewEntity::getVoucherId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<Long> newVoucherIdList = new ArrayList<>();
            if (!voucherIdList.isEmpty()) {
                for (String voucherIds : voucherIdList) {
                    if (StringUtils.isNotEmpty(voucherIds)) {
                        newVoucherIdList.addAll(Arrays.stream(voucherIds.split(",")).
                                map(e -> Long.parseLong(e.trim())).collect(Collectors.toList()));
                    }
                }
            }
            voucherService.deleteByIdList(newVoucherIdList);
        });
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
        }
        return StringUtil.EMPTY;
    }

    @Override
    public void submit(List<Long> ids) {
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<ServiceFeeNewEntity> entityList = this.listByIds(ids);
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

            List<ServiceFeeNewEntity> newEntityList = this.listByIds(ids);
            newEntityList.forEach(v -> {
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
        List<ServiceFeeNewEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(entityList.stream().map(ServiceFeeNewEntity::getProcessInstanceId).collect(Collectors.toList()));
        this.updateBatchById(entityList);
        if (entityList.get(0).getAccountDate() != null) {
            serviceFeeDetailsNewService.updateVoucherStatus(ids, ProcessStatusEnum.ENTERED.getCode(), Integer.valueOf(DateUtil.format(entityList.get(0).getAccountDate(), "yyyyMM")));
        }
    }

    @Override
    public Boolean voucher(List<Long> ids, String isSubmit) {
        List<ServiceFeeNewEntity> serviceFeeEntities = listByIds(ids);
        List<Long> deleteIds = serviceFeeEntities.stream().filter(v -> !ProcessStatusEnum.WRITEOFF.getCode().equals(v.getProcessStatus())).map(ServiceFeeNewEntity::getId).collect(Collectors.toList());

        List<ServiceFeeDetailsNewEntity> detailsEntities = serviceFeeDetailsNewService.getBaseMapper().selectList(Wrappers.<ServiceFeeDetailsNewEntity>lambdaQuery()
                .in(ServiceFeeDetailsNewEntity::getServiceFeeId, serviceFeeEntities.stream().map(ServiceFeeNewEntity::getId).collect(Collectors.toList())));
        if (CollectionUtils.isEmpty(detailsEntities)) {
            throw new ServiceException("缺少分摊信息");
        }
        Date accountDate = DateUtil.beginOfDay(detailsEntities.get(0).getBusinessDate());
        // 查询合同信息
        List<ContractDTO> contractDTOS = contractService.listContractDTOByCodeList(detailsEntities.stream().map(ServiceFeeDetailsNewEntity::getContractCode).distinct().collect(Collectors.toList()));
        Map<String, ContractDTO> contractMap = contractDTOS.stream().collect(Collectors.toMap(ContractDTO::getContractCode, e -> e, (a, b) -> b));

        // 生成凭证前先删除之前的凭证
        if (CollectionUtils.isNotEmpty(deleteIds)) {
            batchDeleteVoucher(ids);
        }

        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        for (ServiceFeeDetailsNewEntity entity : detailsEntities) {
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
            commonMap.put("isAccrual", entity.getSharingServiceFeeFlag());
            // 服务费分摊金额
            commonMap.put("serviceSplit", entity.getAccruedAmount());
            // 服务费调整金额
            commonMap.put("serviceAdjust", entity.getThisMonthReclassificationAdjustmentAmountNoTax());
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
                    serviceFeeDetailsNewService.lambdaUpdate()
                            .set(ServiceFeeDetailsNewEntity::getVoucherId, vouchIds)
                            .set(ServiceFeeDetailsNewEntity::getAccountDate, accountDate)
                            .set(ServiceFeeDetailsNewEntity::getIsGenerateVoucher, YesOrNoEnum.YES.getCode())
                            .set(ServiceFeeDetailsNewEntity::getExceptionType, entry.getErrorInfo())
                            .eq(ServiceFeeDetailsNewEntity::getId, Long.parseLong(entry.getOrderId()))
                            .update();
                }
            }
        }
        return Boolean.TRUE;
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
        ServiceFeeNewEntity entity = this.getById(approveDTO.getDocumentId());
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

}

