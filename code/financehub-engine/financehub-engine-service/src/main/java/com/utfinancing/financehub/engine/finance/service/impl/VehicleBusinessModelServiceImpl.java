package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.VehicleBusinessModelMapper;
import com.utfinancing.financehub.engine.finance.mapper.ContractMapper;
import com.utfinancing.financehub.engine.finance.mapper.ContractBalanceMapper;
import com.utfinancing.financehub.engine.finance.mapper.LeaseIncomeDetailsMapper;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanMapper;
import com.utfinancing.financehub.engine.finance.mapper.VoucherMapper;
import com.utfinancing.financehub.engine.finance.model.vo.VehicleLifecycleVO;
import com.utfinancing.financehub.engine.finance.model.vo.VehicleContractExcelVO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractQueryDTO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.rule.entity.RawTransactionDataEntity;
import com.utfinancing.financehub.engine.rule.mapper.RawTransactionDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleBusinessModelServiceImpl
        extends ServiceImpl<VehicleBusinessModelMapper, VehicleBusinessModelEntity>
        implements IVehicleBusinessModelService {

    private final ContractMapper contractMapper;
    private final RepaymentPlanMapper repaymentPlanMapper;
    private final ContractBalanceMapper contractBalanceMapper;
    private final VoucherMapper voucherMapper;
    private final RawTransactionDataMapper rawTransactionDataMapper;
    private final LeaseIncomeDetailsMapper leaseIncomeDetailsMapper;

    @Override
    public void saveOrUpdateFromLeaseStart(Map<String, Object> data) {
        String contractCode = text(data, "contractCode", "contract_no", "contractCode");
        if (StrUtil.isBlank(contractCode)) return;

        VehicleBusinessModelEntity entity = lambdaQuery()
                .eq(VehicleBusinessModelEntity::getContractCode, contractCode).one();
        if (entity == null) {
            entity = new VehicleBusinessModelEntity();
            entity.setContractCode(contractCode);
            entity.setCreateTime(LocalDateTime.now());
        }
        entity.setSourceSystem(text(data, "systemCode", "source_system"));
        entity.setSourceEventId(text(data, "sourceEventId", "source_event_id"));
        entity.setEventCode(text(data, "sceneCode", "event_code"));
        entity.setOrgId(text(data, "orgId", "accounting_org_code"));
        entity.setBusinessLine(text(data, "businessPlate", "business_line"));
        entity.setLeaseType(text(data, "leaseType", "lease_category"));
        entity.setLeaseMethod(text(data, "returnType", "lease_method"));
        entity.setProductCode(text(data, "productCode", "product_code"));
        entity.setProductName(text(data, "contractName", "product_name"));
        entity.setGuaranteeFlag(text(data, "guaranteeModel", "guarantee_model"));
        entity.setChannelMode(text(data, "channelMode", "channel_mode"));
        entity.setDealerCode(text(data, "dealerCode", "dealer_code"));
        entity.setDealerName(text(data, "dealerName", "dealer_name"));
        entity.setCustomerCode(text(data, "clientCode", "customer_no"));
        entity.setCustomerName(text(data, "clientName", "customer_name"));
        entity.setContractSignDate(date(data, "contractSignDate", "contract_sign_date"));
        entity.setLeaseStartDate(date(data, "leaseDateStart", "lease_start_date"));
        entity.setMaturityDate(date(data, "leaseDateEnd", "contract_end_date"));
        entity.setCurrency(text(data, "currencyType", "currency"));
        entity.setContractAmount(decimal(data, "contractAmount", "contract_amount"));
        entity.setFinanceAmount(decimal(data, "financeAmount", "finance_amount", "lease_principal"));
        entity.setActualDisbursement(decimal(data, "actualDisbursement", "actual_disbursement"));
        entity.setTotalTerms(integer(data, "totalTerms", "total_terms"));
        entity.setContractRate(decimal(data, "currentInterestRate", "current_interest_rate", "irr"));
        entity.setXirrRate(decimal(data, "xirrRate", "irr"));
        entity.setResidualValue(decimal(data, "retainedPrice", "residual_value"));
        entity.setRepaymentFrequency(text(data, "repaymentFrequency", "repayment_frequency"));
        entity.setRepaymentMethod(text(data, "repaymentMethod", "repayment_method"));
        entity.setContractStatus(text(data, "contractStatus", "contract_status"));
        entity.setFiveClass(text(data, "classificationFive", "five_class"));

        Map<String, Object> asset = firstMap(data.get("assets"));
        if (asset != null) {
            entity.setVin(text(asset, "vin"));
            entity.setAssetNo(text(asset, "assetNo", "asset_no"));
            entity.setAssetName(text(asset, "assetName", "asset_name"));
            entity.setBrand(text(asset, "brand"));
            entity.setModel(text(asset, "model"));
            entity.setCarType(text(asset, "carType", "car_type"));
            entity.setOriginalAssetValue(decimal(asset, "originalAssetValue", "original_asset_value"));
            entity.setRecognizedAssetValue(decimal(asset, "recognizedAssetValue", "recognized_asset_value"));
            entity.setMortgageFlag(text(asset, "mortgageFlag", "mortgage_flag"));
            entity.setMortgageCertificateNo(text(asset, "mortgageCertificateNo", "mortgage_certificate_no"));
        }
        entity.setUpdateTime(LocalDateTime.now());
        saveOrUpdate(entity);
    }

    /**
     * 单合同查询以乘用车合同模型为主数据源。旧的 eg_contract 只在详情中作为
     * 兼容补充，避免 MySQL 环境继续执行旧模型中的 PostgreSQL 方言查询。
     */
    @Override
    @Transactional(readOnly = true)
    public IPage<VehicleBusinessModelEntity> selectPage(ContractQueryDTO queryDTO) {
        return page(new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper(queryDTO));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleContractExcelVO> export(ContractQueryDTO queryDTO) {
        return BeanUtil.copyToList(list(queryWrapper(queryDTO)), VehicleContractExcelVO.class);
    }

    private LambdaQueryWrapper<VehicleBusinessModelEntity> queryWrapper(ContractQueryDTO queryDTO) {
        LambdaQueryWrapper<VehicleBusinessModelEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getContractCode()),
                        VehicleBusinessModelEntity::getContractCode, queryDTO.getContractCode())
                .like(StrUtil.isNotBlank(queryDTO.getClientName()),
                        VehicleBusinessModelEntity::getCustomerName, queryDTO.getClientName())
                .like(StrUtil.isNotBlank(queryDTO.getDealerName()),
                        VehicleBusinessModelEntity::getDealerName, queryDTO.getDealerName())
                .like(StrUtil.isNotBlank(queryDTO.getVin()),
                        VehicleBusinessModelEntity::getVin, queryDTO.getVin())
                .like(StrUtil.isNotBlank(queryDTO.getBrand()),
                        VehicleBusinessModelEntity::getBrand, queryDTO.getBrand())
                .like(StrUtil.isNotBlank(queryDTO.getModel()),
                        VehicleBusinessModelEntity::getModel, queryDTO.getModel())
                .in(!CollectionUtils.isEmpty(queryDTO.getOrgIds()),
                        VehicleBusinessModelEntity::getOrgId, queryDTO.getOrgIds())
                .in(!CollectionUtils.isEmpty(queryDTO.getContractStatuses()),
                        VehicleBusinessModelEntity::getContractStatus, queryDTO.getContractStatuses())
                .in(!CollectionUtils.isEmpty(queryDTO.getSystemCodeList()),
                        VehicleBusinessModelEntity::getSourceSystem, queryDTO.getSystemCodeList())
                .ge(queryDTO.getBusinessDateStart() != null,
                        VehicleBusinessModelEntity::getContractSignDate, queryDTO.getBusinessDateStart())
                .le(queryDTO.getBusinessDateEnd() != null,
                        VehicleBusinessModelEntity::getContractSignDate, queryDTO.getBusinessDateEnd())
                .ge(queryDTO.getLeaseDateStartDate() != null,
                        VehicleBusinessModelEntity::getLeaseStartDate, queryDTO.getLeaseDateStartDate())
                .le(queryDTO.getLeaseDateStartEndDate() != null,
                        VehicleBusinessModelEntity::getLeaseStartDate, queryDTO.getLeaseDateStartEndDate())
                .ge(queryDTO.getLeaseDateEndStartDate() != null,
                        VehicleBusinessModelEntity::getMaturityDate, queryDTO.getLeaseDateEndStartDate())
                .le(queryDTO.getLeaseDateEndEndDate() != null,
                        VehicleBusinessModelEntity::getMaturityDate, queryDTO.getLeaseDateEndEndDate())
                .orderByDesc(VehicleBusinessModelEntity::getLeaseStartDate,
                        VehicleBusinessModelEntity::getUpdateTime,
                        VehicleBusinessModelEntity::getId);
        return wrapper;
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleLifecycleVO getLifecycle(Long contractId) {
        VehicleBusinessModelEntity model = getById(contractId);
        ContractEntity contract = null;
        if (model == null) {
            // 兼容历史页面传入 eg_contract.id 的旧链接。
            contract = contractMapper.selectById(contractId);
            if (contract == null) return null;
            model = lambdaQuery().eq(VehicleBusinessModelEntity::getContractCode,
                    contract.getContractCode()).one();
        }
        String code = model != null ? model.getContractCode() : contract.getContractCode();
        if (contract == null) {
            LambdaQueryWrapper<ContractEntity> contractWrapper = Wrappers.lambdaQuery();
            contractWrapper.eq(ContractEntity::getContractCode, code);
            if (StrUtil.isNotBlank(model.getOrgId())) {
                contractWrapper.eq(ContractEntity::getOrgId, model.getOrgId());
            }
            contractWrapper.orderByDesc(ContractEntity::getUpdateTime, ContractEntity::getId).last("limit 1");
            contract = contractMapper.selectOne(contractWrapper);
        }
        VehicleLifecycleVO vo = new VehicleLifecycleVO();
        vo.setContract(contract);
        vo.setBusinessModel(model);
        List<RepaymentPlanEntity> plans = repaymentPlanMapper.selectList(Wrappers.<RepaymentPlanEntity>lambdaQuery()
                .eq(RepaymentPlanEntity::getContractCode, code)
                .eq(RepaymentPlanEntity::getDelFlag, "0")
                .orderByAsc(RepaymentPlanEntity::getPlanDate, RepaymentPlanEntity::getId));
        populateProfitSharingAllocation(plans, model != null ? model.getOrgId() : contract.getOrgId());
        if (model != null && !plans.isEmpty() && plans.get(0).getXirrRate() != null) {
            model.setXirrRate(plans.get(0).getXirrRate());
        }
        vo.setRepaymentPlans(plans);
        List<ContractBalanceEntity> rawBalances = contractBalanceMapper.selectList(Wrappers.<ContractBalanceEntity>lambdaQuery()
                .eq(ContractBalanceEntity::getContractCode, code)
                .and(w -> w.isNull(ContractBalanceEntity::getDelFlag)
                        .or().eq(ContractBalanceEntity::getDelFlag, "0"))
                .orderByAsc(ContractBalanceEntity::getBusinessDate,
                        ContractBalanceEntity::getVoucherDate, ContractBalanceEntity::getId));
        List<VoucherEntity> vouchers = voucherMapper.selectList(Wrappers.<VoucherEntity>lambdaQuery()
                .eq(VoucherEntity::getContractCode, code)
                .eq(VoucherEntity::getDelFlag, "0")
                .orderByAsc(VoucherEntity::getBusinessDate, VoucherEntity::getVoucherDate, VoucherEntity::getId));
        fillEventNames(vouchers);
        vo.setBalances(buildLifecycleBalances(vouchers, rawBalances));
        vo.setVouchers(vouchers);
        return vo;
    }

    /**
     * 按各收益节点的实际利率收益占比分摊分润费，末个节点承接舍入尾差。
     */
    private void populateProfitSharingAllocation(List<RepaymentPlanEntity> plans, String fallbackOrgId) {
        plans.forEach(plan -> plan.setProfitSharingAllocationAmount(BigDecimal.ZERO));
        Map<String, List<RepaymentPlanEntity>> plansByOrg = plans.stream()
                .filter(plan -> StrUtil.isNotBlank(plan.getOrgId()) || StrUtil.isNotBlank(fallbackOrgId))
                .collect(Collectors.groupingBy(plan -> StrUtil.isNotBlank(plan.getOrgId())
                        ? plan.getOrgId() : fallbackOrgId, LinkedHashMap::new, Collectors.toList()));

        plansByOrg.forEach((orgId, orgPlans) -> {
            BigDecimal totalAmount = Optional.ofNullable(leaseIncomeDetailsMapper.selectProfitSharingTotalAmount(
                    orgPlans.get(0).getContractCode(), orgId)).orElse(BigDecimal.ZERO);
            List<RepaymentPlanEntity> incomePlans = orgPlans.stream()
                    .filter(plan -> Optional.ofNullable(plan.getRentalIncome()).orElse(BigDecimal.ZERO)
                            .compareTo(BigDecimal.ZERO) > 0)
                    .collect(Collectors.toList());
            BigDecimal totalIncome = incomePlans.stream()
                    .map(RepaymentPlanEntity::getRentalIncome)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalAmount.compareTo(BigDecimal.ZERO) <= 0 || totalIncome.compareTo(BigDecimal.ZERO) <= 0) return;

            BigDecimal allocated = BigDecimal.ZERO;
            for (int index = 0; index < incomePlans.size(); index++) {
                RepaymentPlanEntity plan = incomePlans.get(index);
                BigDecimal currentAmount = index == incomePlans.size() - 1
                        ? totalAmount.subtract(allocated)
                        : totalAmount.multiply(plan.getRentalIncome()).divide(totalIncome, 2, RoundingMode.HALF_UP);
                plan.setProfitSharingAllocationAmount(currentAmount);
                allocated = allocated.add(currentAmount);
            }
        });
    }

    /**
     * 单合同流水按凭证形成一个余额快照。余额表底层会按辅助核算组合拆行，
     * 因此先合并同一凭证的发生额，再按真实业务时间逐笔结转合同级余额。
     */
    private List<ContractBalanceEntity> buildLifecycleBalances(List<VoucherEntity> vouchers,
                                                                List<ContractBalanceEntity> rawBalances) {
        Map<Long, List<ContractBalanceEntity>> balancesByVoucher = new LinkedHashMap<>();
        for (ContractBalanceEntity balance : rawBalances) {
            if (balance.getVoucherId() != null) {
                balancesByVoucher.computeIfAbsent(balance.getVoucherId(), key -> new ArrayList<>()).add(balance);
            }
        }

        List<Field> amountFields = new ArrayList<>();
        for (Field field : ContractBalanceEntity.class.getDeclaredFields()) {
            if (field.getType() == BigDecimal.class && field.getName().endsWith("Amount")) {
                field.setAccessible(true);
                amountFields.add(field);
            }
        }

        Map<String, BigDecimal> runningBalances = new HashMap<>();
        List<ContractBalanceEntity> result = new ArrayList<>();
        for (VoucherEntity voucher : vouchers) {
            ContractBalanceEntity snapshot = new ContractBalanceEntity();
            snapshot.setId(voucher.getId());
            snapshot.setVoucherId(voucher.getId());
            snapshot.setInterfaceDataId(voucher.getInterfaceDataId());
            snapshot.setSystemCode(voucher.getSystemCode());
            snapshot.setBusinessCode(voucher.getBusinessCode());
            snapshot.setBusinessDate(voucher.getBusinessDate());
            snapshot.setVoucherDate(voucher.getVoucherDate());
            snapshot.setSceneCode(voucher.getSceneCode());
            snapshot.setContractCode(voucher.getContractCode());
            snapshot.setClientCode(voucher.getClientCode());
            snapshot.setOrgId(voucher.getOrgId());
            snapshot.setPeriodCode(voucher.getPeriodCode());
            snapshot.setDelFlag(voucher.getDelFlag());

            List<ContractBalanceEntity> eventRows = balancesByVoucher.getOrDefault(voucher.getId(), new ArrayList<>());
            for (Field amountField : amountFields) {
                BigDecimal amount = BigDecimal.ZERO;
                try {
                    for (ContractBalanceEntity eventRow : eventRows) {
                        BigDecimal rowAmount = (BigDecimal) amountField.get(eventRow);
                        if (rowAmount != null) amount = amount.add(rowAmount);
                    }
                    amountField.set(snapshot, amount);
                    String subject = amountField.getName().substring(0,
                            amountField.getName().length() - "Amount".length());
                    BigDecimal balance = runningBalances.getOrDefault(subject, BigDecimal.ZERO).add(amount);
                    runningBalances.put(subject, balance);
                    Field balanceField = ContractBalanceEntity.class.getDeclaredField(subject + "Balance");
                    balanceField.setAccessible(true);
                    balanceField.set(snapshot, balance);
                } catch (IllegalAccessException | NoSuchFieldException exception) {
                    throw new IllegalStateException("合同生命周期余额快照构建失败: " + amountField.getName(), exception);
                }
            }
            result.add(snapshot);
        }
        return result;
    }

    private void fillEventNames(List<VoucherEntity> vouchers) {
        List<Long> rawIds = vouchers.stream()
                .map(VoucherEntity::getInterfaceId)
                .filter(id -> id != null)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
        Map<Long, RawTransactionDataEntity> rawById = new HashMap<>();
        if (!rawIds.isEmpty()) {
            for (RawTransactionDataEntity raw : rawTransactionDataMapper.selectBatchIds(rawIds)) {
                rawById.put(raw.getId(), raw);
            }
        }
        for (VoucherEntity voucher : vouchers) {
            RawTransactionDataEntity raw = rawById.get(voucher.getInterfaceId());
            String eventName = raw == null || raw.getMessageContent() == null ? null
                    : text(raw.getMessageContent(), "event_name", "sourceEventName",
                    "sourceEventCode", "eventName", "sceneCodeOriginal");
            voucher.setEventName(StrUtil.blankToDefault(eventName, voucher.getSceneName()));
        }
    }

    private static String text(Map<String, Object> map, String... keys) {
        for (String key : keys) {
            Object value = map.get(key);
            if (value != null && StrUtil.isNotBlank(String.valueOf(value))) return String.valueOf(value);
        }
        return null;
    }

    private static BigDecimal decimal(Map<String, Object> map, String... keys) {
        String value = text(map, keys);
        return StrUtil.isBlank(value) ? null : Convert.toBigDecimal(value);
    }

    private static Integer integer(Map<String, Object> map, String... keys) {
        String value = text(map, keys);
        return StrUtil.isBlank(value) ? null : Convert.toInt(value);
    }

    private static LocalDate date(Map<String, Object> map, String... keys) {
        String value = text(map, keys);
        return StrUtil.isBlank(value) ? null : DateUtil.parse(value).toLocalDateTime().toLocalDate();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> firstMap(Object value) {
        if (!(value instanceof List) || ((List<?>) value).isEmpty()) return null;
        Object first = ((List<?>) value).get(0);
        return first instanceof Map ? (Map<String, Object>) first : null;
    }
}
