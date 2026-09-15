package com.utfinancing.financehub.engine.rule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.exception.ExpressionSyntaxErrorException;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.CacheConstants;
import com.utfinancing.financehub.common.core.constant.HttpStatus;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.constant.DefaultConstant;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.FundBusinessSystemEbankMappingMapper;
import com.utfinancing.financehub.engine.finance.mapper.FundEbankTransactionDataMapper;
import com.utfinancing.financehub.engine.finance.mapper.FundPaymentDataMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.payment.service.RetailLeasebackStampDutyService;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.entity.InterfaceDataEntity;
import com.utfinancing.financehub.engine.rule.entity.RawTransactionDataEntity;
import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ListAmountDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IInterfaceDataService;
import com.utfinancing.financehub.engine.rule.service.IPeriodCodeService;
import com.utfinancing.financehub.engine.rule.service.IRawTransactionDataService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.rule.util.RuleUtil;
import com.utfinancing.financehub.engine.scene.model.dto.*;
import com.utfinancing.financehub.engine.scene.model.vo.BusinessVO;
import com.utfinancing.financehub.engine.scene.service.*;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.verification.service.IVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements IRuleService {

    private final ISceneFieldsService sceneFieldsService;
    private final ISceneService sceneService;
    private final IVoucherService voucherService;

    // 注入新的本地号段生成器
    @Autowired
    private LocalSegmentVoucherGenerator localSegmentVoucherGenerator;

    @Resource
    @Lazy
    private IContractService contractService;
    @Resource
    @Lazy
    private IContractMonthService contractMonthService;

    @Resource
    private IContractNewTransactionService contractNewTransactionService;

    private final IClientService clientService;
    private final IInterfaceDataService interfaceDataService;
    private final RemoteDictService remoteDictService;
    private final IBusinessService businessService;
    private final IContractBalanceService contractBalanceService;
    private final IContractBalanceLatestService contractBalanceLatestService;
    private final IRawTransactionDataService rawTransactionDataService;
    private final IFieldMappingService fieldMappingService;
    private final ITaxRateService taxRateService;
    private final IContractInterfaceTotalService contractInterfaceTotalService;
    private final IRepaymentPlanService repaymentPlanService;
    private final IFundSystemBalanceService iFundSystemBalanceService;
    private final ISceneVoucherEntryService sceneVoucherEntryService;
    private final RedisService redisService;
    private final FundEbankTransactionDataMapper fundEbankTransactionDataMapper;

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;
    private final IBankAccountService iBankAccountService;

    @Lazy
    @Resource
    private IOutTableAbsService outTableAbsService;
    @Lazy
    @Resource
    private IVerificationService iVerificationService;
    @Resource
    private IOrgCompanyService iOrgCompanyService;

    @Lazy
    @Resource
    private IRecyclingEquipmentInDetailService iRecyclingEquipmentInDetailService;
    @Lazy
    @Resource
    private IRecyclingEquipmentOutDetailService iRecyclingEquipmentOutDetailService;
    private final IVoucherEntryService iVoucherEntryService;
    @Lazy
    @Resource
    private IPeriodCodeService periodCodeService;
    private static final long REDIS_VOUCHER_NUM_EXPIRE = 3600L * 24 * 30;

    @Resource
    private FundBusinessSystemEbankMappingMapper fundBusinessSystemEbankMappingMapper;

    @Resource
    private FundPaymentDataMapper fundPaymentDataMapper;

    @Lazy
    @Resource
    private RetailLeasebackStampDutyService retailLeasebackStampDutyService;

    @Override
    public List<SceneRuleDTO> getTranslateRule(String sceneCode, InterfaceDataDTO interfaceDataDTO, Map<String, Object> dataMap) {
        //获取原始规则
        List<SceneRuleDTO> ruleDTOList = getSceneRuleDTOByCode(sceneCode);

        Map<String, Object> ruleKeyMap = new HashMap<>();
        //接口字段
        Map<String, Object> sceneFieldsMap = sceneFieldsService.selectSceneFieldsMapByCode(sceneCode);
        ruleKeyMap.putAll(sceneFieldsMap);
        if (SceneEnum.HTQZ.getCode().equals(sceneCode)) {
            // These values are produced by the accounting engine from raw lease-start
            // data. They remain available to the rule editor without being exposed as
            // fields that an upstream business system must provide.
            ruleKeyMap.putAll(leaseStartCalculatedRuleFields());
        }

        //金额类型参数
        R<List<SysDictData>> dictListR = listDictTypeData(DictTypeEnum.CASH_TYPE.getCode());
        if (dictListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(dictListR.getData())) {
            for (SysDictData dictData : dictListR.getData()) {
                ruleKeyMap.put("金额类型参数表." + dictData.getDictLabel(), dictData.getDictValue());
                ruleKeyMap.put(dictData.getDictValue(), dictData.getDictValue());
            }
        }

        //合同余额表
        List<String> assistFlags = sceneVoucherEntryService.selectDistinctAssistFlagsBySceneCode(sceneCode);
        R<List<SysDictData>> contractBalanceListR = listDictTypeData(DictTypeEnum.CASH_TYPE.getCode());
        if (contractBalanceListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(contractBalanceListR.getData())) {
            for (String assistFlag : assistFlags) {
                //维度合同余额
                if (StrUtil.isBlank(assistFlag)) {
                    assistFlag = "none";
                }
                for (SysDictData dictData : contractBalanceListR.getData()) {
                    ruleKeyMap.put("科目余额表" + assistFlag + "." + dictData.getDictLabel() + "余额", dictData.getDictValue() + "_balance_" + assistFlag);
                }
            }
            //默认合同余额
            for (SysDictData dictData : contractBalanceListR.getData()) {
                ruleKeyMap.put("科目余额表." + dictData.getDictLabel() + "余额", dictData.getDictValue() + "_balance");
            }
        }

        //合同表
        R<List<SysDictData>> contractListR = listDictTypeData(DictTypeEnum.CONTRACT_FIELDS.getCode());
        if (contractListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(contractListR.getData())) {
            for (SysDictData dictData : contractListR.getData()) {
                ruleKeyMap.put("合同表." + dictData.getDictLabel(), "contract_" + dictData.getDictValue());
            }
        }
        //合同月表
        R<List<SysDictData>> contractMonthListR = listDictTypeData(DictTypeEnum.CONTRACT_MONTH_FIELDS.getCode());
        if (contractMonthListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(contractMonthListR.getData())) {
            for (SysDictData dictData : contractMonthListR.getData()) {
                ruleKeyMap.put("合同月表." + dictData.getDictLabel(), "contract_month_" + dictData.getDictValue());
            }
        }

        //客户表
        R<List<SysDictData>> clientListR = listDictTypeData(DictTypeEnum.CLIENT_FIELDS.getCode());
        if (clientListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(clientListR.getData())) {
            for (SysDictData dictData : clientListR.getData()) {
                ruleKeyMap.put("客户表." + dictData.getDictLabel(), "client_" + dictData.getDictValue());
            }
        }

        //税率
        List<TaxRateDTO> taxRateDTOList = queryRedisAllForEditor();
        for (TaxRateDTO taxRateDTO : taxRateDTOList) {
            String name = "税率." + taxRateDTO.getBusinessCode() + "." + taxRateDTO.getFundType() + "." + taxRateDTO.getLeaseType();
            if (StrUtil.isNotBlank(taxRateDTO.getLeaseSubType())) {
                name = name + "." + taxRateDTO.getLeaseSubType();
            }
            ruleKeyMap.put(name, name);
        }

        for (SceneRuleDTO ruleDTO : ruleDTOList) {
            //翻译规则
            RuleUtil.translateRule(ruleDTO, ruleKeyMap, dataMap);
        }
        return ruleDTOList;
    }

    @Override
    public List<VoucherDTO> executeRule(Map<String, Object> dataMap) {

        //预处理特殊场景下的业务编码
        preBusinessCode(dataMap);
        Boolean isGenerateVoucher = isGenerateVoucher(dataMap);
        if (!isGenerateVoucher) {
            return Lists.newArrayList();
        }
        //必填参数校验
        validateRequiredField(dataMap);

        //获取场景编码
        String sceneCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE);
        //缓存
        SceneDTO sceneDTO = getRedisSceneDTOByCode(sceneCode);

        if (sceneDTO == null) {
            throw new ServiceException(StrUtil.format("场景[{}]不存在", sceneCode));
        }
        dataMap.put(RuleConstant.FIELD_SCENE_NAME, sceneDTO.getSceneName());
        //ZLSK KJFP ZLFK场景需要预设值借款合同编号值
        fillValues(dataMap);
        //预处理，主要用于数据字段的拆分和自动填充字段的填充
        List<Map<String, Object>> listDataMap = preExecuteInterfaceMap(sceneCode, dataMap);
        String interFaceId = MapUtil.getStr(dataMap, RuleConstant.FIELD_INTERFACE_ID);
        String orderId = MapUtil.getStr(dataMap, RuleConstant.FIELD_ORDER_ID);
        //先统一删除interFaceData数据
        //再新增之前需要删除已存在的数据根据interfaceId
        deleteInterfaceData(interFaceId);
        List<VoucherDTO> voucherAllList = new ArrayList<>();
        for (Map<String, Object> subDataMap : listDataMap) {
            if (StringUtils.isNotEmpty(interFaceId)) {
                subDataMap.put(RuleConstant.FIELD_INTERFACE_ID, interFaceId);
            }
            if (StringUtils.isNotEmpty(orderId)) {
                subDataMap.put(RuleConstant.FIELD_ORDER_ID, orderId);
            }
            //合同撤销拆分的场景编码需要另外取值
            if (SceneEnum.HTCX.getCode().equals(sceneCode)) {
                sceneCode = MapUtil.getStr(subDataMap, RuleConstant.FIELD_SCENE_CODE);
                if (SceneEnum.HTCXSK.getCode().equals(sceneCode)) {
                    subDataMap.put(RuleConstant.FIELD_SCENE_NAME, SceneEnum.HTCXSK.getDesc());
                }
            }
            List<VoucherDTO> voucherDTOList = executeSingleInterface(sceneCode, subDataMap);
            log.info("返回值：{}", JSON.toJSONString(voucherDTOList));
            voucherAllList.addAll(voucherDTOList);
        }
        //场景为HTCX的需要单独处理，按合同+签约主体查凭证表场景为HTQZ的数据，全部复制后debit_amount，credit_amount分别*-1，新生成数据场景名改为'HTCX'，凭证id、会计期间、凭证号等自动生成
        if (SceneEnum.HTCX.getCode().equals(sceneCode)) {
            copyHtcxVoucher(voucherAllList, dataMap);
        }
        //单条数据全部生成凭证且全部有效才会更新余额表信息 按照是否是资金系统余额分组，按照凭证id升序排序
        log.info("返回参数：{}", JSON.toJSON(voucherAllList));
        if (CollectionUtil.isNotEmpty(voucherAllList)) {
            boolean isValidFlag = voucherAllList.stream().allMatch(v -> VoucherValidFlagEnum.VALID.getCode().equals(v.getValidFlag()));
            List<Long> voucherIdList = voucherAllList.stream().map(VoucherDTO::getId).distinct().collect(Collectors.toList());
            if (isValidFlag) {
                Map<String, List<VoucherDTO>> voucherMap = voucherAllList.stream().collect(Collectors.groupingBy(VoucherDTO::getIsFundSystemBalance, Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            list.sort(Comparator.comparingLong(VoucherDTO::getId));
                            return list;
                        }
                )));
                for (Map.Entry<String, List<VoucherDTO>> entry : voucherMap.entrySet()) {
                    if (YesOrNoEnum.YES.getCode().equals(entry.getKey())) {
                        //付款方式为票据时银行账号可能为空，为空不保存资金系统余额
                        entry.getValue().stream().filter(v -> StringUtils.isNotEmpty(v.getBankNo())).forEach(iFundSystemBalanceService::saveFundSystemBalanceFromVoucher);
                    } else {
                        entry.getValue().forEach(contractBalanceService::saveContractBalanceFromVoucher);
                        //更新合同核销相关字段值
                        updateContractVerification(entry.getValue(), dataMap);
                    }
                }
            } else {
                //更新这条数据生成的凭证全部无效
                voucherService.lambdaUpdate().set(VoucherEntity::getValidFlag, VoucherValidFlagEnum.NO_VALID.getCode()).in(VoucherEntity::getId, voucherIdList).update();
            }
        }
        if (SceneEnum.HTQZ.getCode().equals(sceneCode)) {
            retailLeasebackStampDutyService.triggerAfterLeaseStart(dataMap);
        }
        return voucherAllList;
    }

    public void updateContractVerification(List<VoucherDTO> voucherDTOList, Map<String, Object> dataMap) {
        //获取凭证分录的信息
        //合同编码
        String contractCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE);
        String orgId = MapUtil.getStr(dataMap, RuleConstant.FIELD_ORG_ID);
        String oriSceneCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE_ORIGINAL);
        LocalDateTime voucherDate = voucherDTOList.get(0).getVoucherDate();
        if (StringUtils.isEmpty(contractCode) || StringUtils.isEmpty(orgId)) {
            return;
        }

        QueryWrapper<ContractEntity> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(ContractEntity::getContractCode, contractCode);
        if (StringUtils.isNotEmpty(orgId)) {
            queryWrapper.lambda().eq(ContractEntity::getOrgId, orgId);
        }
        queryWrapper.lambda().orderByDesc(ContractEntity::getId);
        ContractEntity entity = null;
        List<ContractEntity> contractEntityList = contractService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(contractEntityList)) {
            entity = contractEntityList.get(0);
        }
        if (null == entity) {
            return;
        }

        updateContractInfo(voucherDTOList, dataMap,entity);

        LocalDateTime maxAccountDate = iVerificationService.getMaxAccountDate(contractCode, orgId);
        if (ObjectUtil.isNull(maxAccountDate)) {
            return;
        }

        LocalDateTime contractAccountDate = entity.getAccountDate();
        boolean isSetZero = Boolean.FALSE;
        if (ObjectUtil.isNull(contractAccountDate) || contractAccountDate.compareTo(maxAccountDate) != 0) {
            isSetZero = Boolean.TRUE;
        }
        final BigDecimal[] depreciationReservesAmountKp = {isSetZero ? BigDecimal.ZERO : Optional.ofNullable(entity.getDepreciationReservesAmountKp()).orElse(BigDecimal.ZERO)};
        final BigDecimal[] depreciationReservesAmountSk = {isSetZero ? BigDecimal.ZERO : Optional.ofNullable(entity.getDepreciationReservesAmountSk()).orElse(BigDecimal.ZERO)};
        final BigDecimal[] outtaxAmount = {isSetZero ? BigDecimal.ZERO : Optional.ofNullable(entity.getOuttaxAmount()).orElse(BigDecimal.ZERO)};
        final BigDecimal[] taxAmount = {isSetZero ? BigDecimal.ZERO : Optional.ofNullable(entity.getTaxAmount()).orElse(BigDecimal.ZERO)};
        final BigDecimal[] leaseRevenueAmount = {isSetZero ? BigDecimal.ZERO : Optional.ofNullable(entity.getLeaseRevenueAmount()).orElse(BigDecimal.ZERO)};
        final BigDecimal[] receivableVendorProcedureAmount = {entity.getReceivableVendorProcedureAmount() == null ? BigDecimal.ZERO : entity.getReceivableVendorProcedureAmount()};

        //方式一：每次余额表新增数据时，符合scene_code='KJFP'，凭证日期>=eg_verification_details中该合同+签约主体最大的记账日期的depreciation_loss_reverse_amount累加到此字段
        //每次余额表新增数据时，符合scene_code='ZLSK'，凭证日期>=eg_verification_details中该合同+签约主体最大的记账日期的depreciation_loss_reverse_amount累加到此字段；

        voucherDTOList.forEach(v -> {
            // 每次余额表新增数据时，符合scene_code='KJFP'，凭证日期>=eg_verification_details中该合同+签约主体最大的记账日期的depreciation_reserves_amount累加到此字段；
            if (!voucherDate.isBefore(maxAccountDate)) {
                if (SceneEnum.KJFP.getCode().equals(v.getSceneCode())) {
                    List<String> accountCodeList = Arrays.asList(AccountFunTpeEnum.DEPRECIATION_RESERVES.getCode().split(","));
                    List<String> taxList = Arrays.asList(AccountFunTpeEnum.TAX_AMOUNT.getCode().split(","));
                    v.getEntryList().forEach(e -> {
                        if (accountCodeList.contains(e.getAccountCode())) {
                            BigDecimal amount = DRCREnum.CR.getCode().equals(e.getDebitCreditType()) ? e.getCreditAmount() : e.getDebitAmount();
                            depreciationReservesAmountKp[0] = depreciationReservesAmountKp[0].add((null == amount ? BigDecimal.ZERO : amount).multiply(new BigDecimal(-1)));
                        }
                        if (StringUtils.isNotEmpty(v.getSubSceneType()) &&
                                DefaultConstant.INVOICE.equals(v.getSubSceneType()) &&
                                taxList.contains(e.getAccountCode())) {
                            if (DRCREnum.CR.getCode().equals(e.getDebitCreditType())) {
                                taxAmount[0] = taxAmount[0].add(null == e.getCreditAmount() ? BigDecimal.ZERO : e.getCreditAmount());
                            } else {
                                taxAmount[0] = taxAmount[0].subtract(null == e.getDebitAmount() ? BigDecimal.ZERO : e.getDebitAmount());
                            }
                        }
                    });

                }
                if (SceneEnum.ZLSK.getCode().equals(v.getSceneCode()) || SceneEnum.KJFP.getCode().equals(v.getSceneCode())) {
                    List<String> accountCodeList = Arrays.asList(AccountFunTpeEnum.DEPRECIATION_RESERVES.getCode().split(","));
                    List<String> outTaxList = Arrays.asList(AccountFunTpeEnum.OUTTAX_AMOUNT.getCode().split(","));
                    v.getEntryList().forEach(e -> {
                        // modify by zhangli.chen for 核销合同收款时转回的拨备发生额 不应该统计开票场景的数据 on 20250610
                        if (accountCodeList.contains(e.getAccountCode()) && SceneEnum.ZLSK.getCode().equals(v.getSceneCode())) {
                            BigDecimal amount = DRCREnum.CR.getCode().equals(e.getDebitCreditType()) ? e.getCreditAmount() : e.getDebitAmount();
                            depreciationReservesAmountSk[0] = depreciationReservesAmountSk[0].add((null == amount ? BigDecimal.ZERO : amount).multiply(new BigDecimal(-1)));
                        }
                        if (StringUtils.isNotEmpty(v.getSubSceneType()) &&
                                DefaultConstant.SUB_SCENE_CODE_List.contains(v.getSubSceneType()) &&
                                outTaxList.contains(e.getAccountCode())) {
                            if (DRCREnum.CR.getCode().equals(e.getDebitCreditType())) {
                                outtaxAmount[0] = outtaxAmount[0].add(null == e.getCreditAmount() ? BigDecimal.ZERO : e.getCreditAmount());
                            } else {
                                outtaxAmount[0] = outtaxAmount[0].subtract(null == e.getDebitAmount() ? BigDecimal.ZERO : e.getDebitAmount());
                            }
                        }
                    });
                }
                if (SceneEnum.SYJT.getCode().equals(v.getSceneCode())
                        || SceneEnum.KJFP.getCode().equals(v.getSceneCode())
                        || SceneEnum.ZLSK.getCode().equals(v.getSceneCode())) {
                    List<String> accountCodeList = Arrays.asList(AccountFunTpeEnum.LEASE_REVENUE_AMOUNT.getCode().split(","));
                    v.getEntryList().forEach(e -> {
                        if (accountCodeList.contains(e.getAccountCode())) {
                            if (DRCREnum.CR.getCode().equals(e.getDebitCreditType())) {
                                leaseRevenueAmount[0] = leaseRevenueAmount[0].add(null == e.getCreditAmount() ? BigDecimal.ZERO : e.getCreditAmount());
                            } else {
                                leaseRevenueAmount[0] = leaseRevenueAmount[0].subtract(null == e.getDebitAmount() ? BigDecimal.ZERO : e.getDebitAmount());
                            }
                        }
                    });
                }
                if (SceneEnum.HTQZ.getCode().equals(v.getSceneCode())) {
                    String receivableVendorProcedureAmountStr = MapUtil.getStr(dataMap, "receivableVendorProcedureAmount");
                    receivableVendorProcedureAmount[0] = receivableVendorProcedureAmount[0].add(new BigDecimal(receivableVendorProcedureAmountStr));
                } else if (SceneEnum.ZLSK.getCode().equals(v.getSceneCode())) {
                    String receiveVendorProcedureAmountStr = MapUtil.getStr(dataMap, "receiveVendorProcedureAmount");
                    receivableVendorProcedureAmount[0] = receivableVendorProcedureAmount[0].add(new BigDecimal(receiveVendorProcedureAmountStr));
                } else if (SceneEnum.JYJGBG.getCode().equals(v.getSceneCode())) {
                    String vendorProcedureAdjustRevenuesStr = MapUtil.getStr(dataMap, "vendorProcedureAdjustRevenues");
                    receivableVendorProcedureAmount[0] = receivableVendorProcedureAmount[0].add(new BigDecimal(vendorProcedureAdjustRevenuesStr));
                }
            }

        });
        updateContractInfo(dataMap, maxAccountDate, depreciationReservesAmountKp, depreciationReservesAmountSk,
                outtaxAmount, taxAmount, leaseRevenueAmount, oriSceneCode, entity, receivableVendorProcedureAmount[0]);
    }

    private void updateContractInfo(List<VoucherDTO> voucherDTOList, Map<String, Object> dataMap, ContractEntity entity) {
        AtomicReference<BigDecimal> firstRent = new AtomicReference<>(entity.getFirstRent());
        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setId(entity.getId());
        ArrayList<String> orderIds = new ArrayList<>();
        voucherDTOList.forEach(v -> {
            if (SceneEnum.ZLSK.getCode().equals(v.getSceneCode())) {
                String withdrawalPeriodsStr = MapUtil.getStr(dataMap, "withdrawalPeriods");
                String recyclePrincipalAmountStr = MapUtil.getStr(dataMap, "recyclePrincipalAmount");
                String recycleInterestAmountStr = MapUtil.getStr(dataMap, "recycleInterestAmount");
                if (!orderIds.contains(v.getOrderId())&&StringUtils.isNotEmpty(withdrawalPeriodsStr) && "1".equals(withdrawalPeriodsStr)) {
                    orderIds.add(v.getOrderId()) ;
                    BigDecimal firstRentOptional = Optional.ofNullable(firstRent.get()).orElse(BigDecimal.ZERO);
                    firstRent.set(firstRentOptional.add(new BigDecimal(recyclePrincipalAmountStr)).add(new BigDecimal(recycleInterestAmountStr)));
                    contractEntity.setFirstRent(firstRent.get());
                }
            }
            if (SceneEnum.HTQZ.getCode().equals(v.getSceneCode())) {
                String payableInsuranceAmountStr = MapUtil.getStr(dataMap, "payableInsuranceAmount");
                BigDecimal payableInsuranceAmount = NumberUtil.isNumber(payableInsuranceAmountStr)
                        ? new BigDecimal(payableInsuranceAmountStr) : BigDecimal.ZERO;
                if (payableInsuranceAmount.compareTo(BigDecimal.ZERO)!= 0 ){
                    contractEntity.setPayableInsuranceAmount(payableInsuranceAmount);
                }
                String estimaeGPSExpenseStr = MapUtil.getStr(dataMap, "estimateGPSExpense");
                if (StringUtils.isNotEmpty(estimaeGPSExpenseStr)){
                    contractEntity.setEstimateGPSExpense(new BigDecimal(estimaeGPSExpenseStr));
                }
            }
        });
        contractService.updateById(contractEntity);
    }

    private void updateContractInfo(Map<String, Object> dataMap, LocalDateTime maxAccountDate, BigDecimal[] depreciationReservesAmountKp, BigDecimal[] depreciationReservesAmountSk,
                                    BigDecimal[] outtaxAmount, BigDecimal[] taxAmount, BigDecimal[] leaseRevenueAmount, String oriSceneCode, ContractEntity entity,
                                    BigDecimal receivableVendorProcedureAmount) {
        String payableDeviceAmount = MapUtil.getStr(dataMap, RuleConstant.FIELD_PAYABLE_DEVICE_AMOUNT);
//        String receivableServiceAmount =  MapUtil.getStr(dataMap, RuleConstant.FIELD_RECEIVABLE_SERVICE_AMOUNT);
//        String receiveServiceAmount =  MapUtil.getStr(dataMap, RuleConstant.FIELD_RECEIVE_SERVICE_AMOUNT);
        ContractEntityUpdateVo contractEntityUpdateVo = new ContractEntityUpdateVo();
        contractEntityUpdateVo.setAccountDate(maxAccountDate);
        contractEntityUpdateVo.setDepreciationReservesAmountKp(depreciationReservesAmountKp[0]);
        contractEntityUpdateVo.setDepreciationReservesAmountSk(depreciationReservesAmountSk[0]);
        contractEntityUpdateVo.setOuttaxAmount(outtaxAmount[0]);
        contractEntityUpdateVo.setTaxAmount(taxAmount[0]);
        contractEntityUpdateVo.setLeaseRevenueAmount(leaseRevenueAmount[0]);
        contractEntityUpdateVo.setReceivableVendorProcedureAmount(receivableVendorProcedureAmount);
        if (StringUtils.equals(oriSceneCode, "起租") && StringUtils.isNotBlank(payableDeviceAmount)) {
            contractEntityUpdateVo.setPayableDeviceAmount(new BigDecimal(payableDeviceAmount));
        }
//        if (StringUtils.equals(oriSceneCode, "咨询服务签约")  && StringUtils.isNotBlank(receivableServiceAmount)){
//            contractEntityUpdateVo.setReceivableServiceAmount(new BigDecimal(receivableServiceAmount));
//        }
//        if ((StringUtils.equals(oriSceneCode, "咨询服务费") || StringUtils.equals(oriSceneCode, "咨询服务费收款")) && StringUtils.isNotBlank(receiveServiceAmount)){
//            contractEntityUpdateVo.setActualServiceAmount(new BigDecimal(receiveServiceAmount));
//        }
        contractEntityUpdateVo.setId(entity.getId());
        contractService.updateContractById(contractEntityUpdateVo);
    }

    @Override
    public InterfaceDataDTO saveInterfaceData(Map<String, Object> dataMap) {
        //保存接口数据
        InterfaceDataDTO interfaceDataDTO = interfaceDataService.saveInterfaceDataFromMap(dataMap);
        String systemCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SYSTEM_CODE);
        String sceneCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE);
        if ((ObjectUtil.equals(systemCode, SystemEnum.CWZT.getCode()) && ObjectUtil.equals(sceneCode, SceneEnum.HTQZ.getCode()))
                || sceneCode.equals(SceneEnum.KJFP.getCode())) {
            // 中台的合同起租 不创建合同数据

        } else {
            //保存合同
            contractNewTransactionService.saveOrUpdateContract(dataMap);
        }
        //保存客户
        //如果合同为非虚拟合同才会新增客户信息
        if (!(StringUtils.isNotEmpty(interfaceDataDTO.getContractCode()) && interfaceDataDTO.getContractCode().startsWith(Constants.VIRTUAL_CONTRACT_START))) {
            clientService.saveOrUpdateClient(dataMap);
        }

        //保存累计金额
        contractInterfaceTotalService.saveFromInterfaceData(dataMap);
        if (SceneEnum.HTQZ.getCode().equals(sceneCode)) {
            // The Huaxia lease-start event includes its full repayment schedule.
            // Persist the schedule synchronously so accrual preparation is complete
            // before the interface document is marked successful.
            repaymentPlanService.saveLeaseStartPlanFromInterfaceData(dataMap);
        }
        repaymentPlanService.saveFromInterfaceData(dataMap);
        return interfaceDataDTO;
    }

    @Override
    public void execute(String messageId, JSONObject jsonData) {
        //保存
        RawTransactionDataEntity entity = rawTransactionDataService.saveRawData(null, jsonData);
        try {
            //映射
            log.info("映射前数据:{}", jsonData);
            fieldMappingService.convertDataFromMapping(jsonData);
            log.info("映射后数据:{}", jsonData);
            //执行
            executeRule(jsonData.to(new TypeReference<Map<String, Object>>() {
            }));
            throw new ServiceException("手动异常");
        } catch (ServiceException e) {
            log.error("execute error.", e);
            rawTransactionDataService.updateStatus(entity.getId(), "执行失败", e.getMessage());
        } catch (Exception e) {
            log.error("execute error.", e);
            rawTransactionDataService.updateStatus(entity.getId(), "执行失败", e.getMessage());
        }
    }

    @Override
    public List<VoucherDetailDTO> generateVoucher(JSONObject jsonObject) {
        //映射
        fieldMappingService.convertDataFromMapping(jsonObject);
        //执行
        List<VoucherDTO> voucherDTOList = executeRule(jsonObject.to(new TypeReference<Map<String, Object>>() {
        }));
        if (CollectionUtil.isNotEmpty(voucherDTOList)) {
            VoucherQueryDTO queryDTO = new VoucherQueryDTO();
            queryDTO.setPageSize(100);
            queryDTO.setPeriodCode(voucherDTOList.get(0).getPeriodCode());
            queryDTO.setVoucherIdList(voucherDTOList.stream().map(e -> e.getId()).collect(Collectors.toList()));
            IPage<VoucherDetailDTO> detailDTOIPage = voucherService.queryVoucherPage(queryDTO);
            return detailDTOIPage.getRecords();
        }
        return null;
    }

    @Override
    public Boolean validateSyntax(String script) {
        if (StrUtil.isBlank(script)) {
            return Boolean.TRUE;
        }
        script = script.replaceAll("\\{|\\}", "");
        script = StrUtil.cleanBlank(script);
        try {
            AviatorEvaluator.validate(StrUtil.cleanBlank(script));
        } catch (ExpressionSyntaxErrorException e) {
            throw new RuntimeException(e.getMessage());
        }
        return Boolean.TRUE;
    }


    private void injectBusinessCode(Map<String, Object> dataMap) {
        //如果业务编码存在则跳出
        if (StringUtils.isNotEmpty(MapUtil.getStr(dataMap, RuleConstant.FIELD_BUSINESS_CODE))) {
            return;
        }
        //通过合同编号查询业务编码
        if (dataMap.containsKey(RuleConstant.FIELD_CONTRACT_CODE)) {
            ContractDTO contractDTO = contractService.getContractDTOByCode(MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE), MapUtil.getStr(dataMap, RuleConstant.FIELD_ORG_ID));
            if (contractDTO != null) {
                dataMap.put(RuleConstant.FIELD_BUSINESS_CODE, contractDTO.getBusinessCode());
                return;
            }
        }
        //如果根据合同编号查不到，则根据主合同编号查询业务编码
        if (dataMap.containsKey(RuleConstant.FIELD_CONTRACT_CODE_M)) {
            ContractDTO contractDTO = contractService.getContractDTOByCode(MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE_M), MapUtil.getStr(dataMap, RuleConstant.FIELD_ORG_ID));
            if (contractDTO != null) {
                dataMap.put(RuleConstant.FIELD_BUSINESS_CODE, contractDTO.getBusinessCode());
            }
        }
    }

    public void injectDefaultNumberFields(String sceneCode, Map<String, Object> dataMap) {
        List<SceneFieldsDTO> numberFieldList = selectRedisNumberSceneFields(sceneCode);
        for (SceneFieldsDTO fieldsDTO : numberFieldList) {
            if (!dataMap.containsKey(fieldsDTO.getFieldCode())) {
                dataMap.put(fieldsDTO.getFieldCode(), 0);
            } else if (dataMap.containsKey(fieldsDTO.getFieldCode()) && ObjectUtil.isNull(dataMap.get(fieldsDTO.getFieldCode()))) {
                dataMap.put(fieldsDTO.getFieldCode(), 0);
            }
        }
    }

    /**
     * 替换数组字段，取数组金额之和
     */
    private void replaceListFields(String sceneCode, Map<String, Object> dataMap) {
        List<SceneFieldsDTO> listFieldList = sceneFieldsService.selectListSceneFields(sceneCode);
        for (SceneFieldsDTO fieldsDTO : listFieldList) {
            if (dataMap.containsKey(fieldsDTO.getFieldCode())) {
                List<ListAmountDTO> listAmountDTOList = JSONArray.parseArray(JSONArray.toJSONString(dataMap.get(fieldsDTO.getFieldCode())), ListAmountDTO.class);
                if (CollectionUtil.isNotEmpty(listAmountDTOList)) {
                    BigDecimal totalAmount = listAmountDTOList.stream().map(ListAmountDTO::getEbankAmount).reduce(BigDecimal.ZERO, (x, y) -> NumberUtil.add(x, y));
                    dataMap.put(fieldsDTO.getFieldCode(), totalAmount);
                }
            }
        }
    }


    /**
     * 接口数据预处理，处理接口数据中的数组类型数据，处理逻辑如下：
     * 入库处置：
     * 1.拆除数组“长期应收款_未确认收款列表”：
     * 1.1 非数组字段一个接口数据，数组字段每一行一个接口数据
     * 1.2 数组字段合计金额单独传一个接口数据，字段名为长期应收款_未确认收款列表汇总
     * 入库赎回：
     * 1. 拆除数据“应付租赁设备款列表”和“合同解约及更改手续费列表”：
     * 1.1 非数组字段一个接口数据，数组字段每一行一个接口数据
     * 1.2 数组字段合计金额单独传一个接口数据，字段名为XXX汇总
     *
     * @param dataMap
     * @return
     */
    private List<Map<String, Object>> preExecuteInterfaceMap(String sceneCode, Map<String, Object> dataMap) {
        String contractCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE);
        String clientCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CLIENT_CODE);
        String businessDate = MapUtil.getStr(dataMap, RuleConstant.FIELD_BUSINESS_DATE);
        String systemCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SYSTEM_CODE);
        String orgId = MapUtil.getStr(dataMap, RuleConstant.FIELD_ORG_ID);
        String interFaceCreateTime = MapUtil.getStr(dataMap, RuleConstant.INTERFACE_CREATE_TIME);
        //场景编码为租赁收款时需要将签约主体置为空，取合同表的签约主体
//        if (sceneCode.equals(SceneEnum.ZLSK.getCode())) {
//            dataMap.put(RuleConstant.FIELD_ORG_ID,"");
//        }
        //注入业务编码
        injectBusinessCode(dataMap);
        String businessCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_BUSINESS_CODE);
        Map<String, Object> copyMap = Maps.newHashMap();
        List<Map<String, Object>> copyDataMap = new ArrayList<>();
        List<Map<String, Object>> finalDataMap = new ArrayList<>();
        finalDataMap.add(dataMap); //非数组字段一个接口数据
        //入库处置
        if (SceneEnum.RKCZ.getCode().equals(sceneCode)) {
//            ContractDTO contractDTO = contractService.getContractDTOByCode(contractCode,orgId);
//            if(ObjectUtil.isNotEmpty(contractDTO)){
//                dataMap.put("clientName",contractDTO.getClientName());
//            }
            //注入凭证金额 开始
            // 1.入库时计提减值：按合同+签约主体查eg_recycling_equipment_in_detail的（最新入库的）provision_for_impairment；
            RecyclingEquipmentInDetailVO recyclingEquipmentInDetailVO = iRecyclingEquipmentInDetailService.getProvision(contractCode, orgId);
            if (ObjectUtil.isNotEmpty(recyclingEquipmentInDetailVO)) {
                dataMap.put(InterfaceFieldEnum.PROVISION_BALANCE.getCode(), recyclingEquipmentInDetailVO.getProvisionForImpairment());//入库时计提减值
            }
            RecyclingEquipmentOutDetailEntity recyclingEquipmentOutDetailEntity = iRecyclingEquipmentOutDetailService.getOne(new LambdaQueryWrapper<RecyclingEquipmentOutDetailEntity>()
                    .eq(RecyclingEquipmentOutDetailEntity::getContractCode, contractCode)
                    .eq(RecyclingEquipmentOutDetailEntity::getOrgId, orgId)
                    .in(RecyclingEquipmentOutDetailEntity::getProcessStatus, ProcessStatusEnum.getCannotModifyCode())
                    .orderByDesc(RecyclingEquipmentOutDetailEntity::getId)
                    .last("limit 1")
            );
            // 2.出库时回收融资租赁设备减值准备余额：按合同+签约主体查eg_recycling_equipment_out_detail的（最新入库的）provision_for_impairment；
            // 3.出库时回收融资租赁设备成本余额：按合同+签约主体查eg_recycling_equipment_out_detail的（最新入库的）recycling_equipment_cost；
            if (ObjectUtil.isNotEmpty(recyclingEquipmentOutDetailEntity)) {
                dataMap.put(InterfaceFieldEnum.OUTBOUND_EQUIPMENT_PROVISION_BALANCE.getCode(), recyclingEquipmentOutDetailEntity.getProvisionForImpairment());
                dataMap.put(InterfaceFieldEnum.OUTBOUND_RECEIVE_COST_BALANCE.getCode(), recyclingEquipmentOutDetailEntity.getRecyclingEquipmentCost());
            }
            // 1.回收设备减值准备余额equipment_depreciation_reserves_balance：按合同+签约主体查最新余额表，sum(equipment_depreciation_reserves_balance)；
            // 2.暂收款项余额provisional_receipts_balance：按合同+签约主体查最新余额表，sum(provisional_receipts_balance)；
            // 3.回收融资租赁设备成本余额receive_cost_balance：按合同+签约主体查最新余额表，sum(receive_cost_balance)；
            List<ContractBalanceLatestEntity> latestEntityList = contractBalanceLatestService.list(new LambdaQueryWrapper<ContractBalanceLatestEntity>()
                    .eq(ContractBalanceLatestEntity::getContractCode, contractCode).eq(ContractBalanceLatestEntity::getOrgId, orgId));
            dataMap.put("equipmentDepreciationReservesBalance", latestEntityList.stream().
                    map(ContractBalanceLatestEntity::getEquipmentDepreciationReservesBalance).
                    reduce(BigDecimal.ZERO, BigDecimal::add));
            dataMap.put("provisionalReceiptsBalance", latestEntityList.stream().
                    map(ContractBalanceLatestEntity::getProvisionalReceiptsBalance).reduce(BigDecimal.ZERO, BigDecimal::add));
            dataMap.put("receiveCostBalance", latestEntityList.stream().
                    map(ContractBalanceLatestEntity::getReceiveCostBalance).reduce(BigDecimal.ZERO, BigDecimal::add));

            List<ContractBalanceEntity> balanceEntities = contractBalanceService.list(new LambdaQueryWrapper<ContractBalanceEntity>()
                    .eq(ContractBalanceEntity::getContractCode, contractCode).eq(ContractBalanceEntity::getOrgId, orgId));
            List<ContractBalanceEntity> balanceEntityList = balanceEntities.stream().
                    filter(e -> StringUtils.isNotEmpty(e.getClientCode())
                            && SceneEnum.RKCZ.getCode().equals(e.getSceneCode())).collect(Collectors.toList());
            if (balanceEntityList != null && balanceEntityList.size() > 0) {
                dataMap.put("provisionalReceiptsAmount", balanceEntityList.stream().
                        max(Comparator.comparingLong(e -> e.getId())).get().getProvisionalReceiptsAmount());
                dataMap.put("receiveCostAmount", balanceEntityList.stream().
                        max(Comparator.comparingLong(e -> e.getId())).get().getReceiveCostAmount());
            } else {
                dataMap.put("provisionalReceiptsAmount", BigDecimal.ZERO);
                dataMap.put("receiveCostAmount", BigDecimal.ZERO);
            }

            balanceEntityList = balanceEntities.stream().
                    filter(e -> StringUtils.isEmpty(e.getClientCode())
                            && SceneEnum.RKCZ.getCode().equals(e.getSceneCode())).collect(Collectors.toList());
            if (balanceEntityList != null && balanceEntityList.size() > 0) {
                dataMap.put("equipmentDepreciationReservesAmount", balanceEntityList.stream().
                        max(Comparator.comparingLong(e -> e.getId())).get().getEquipmentDepreciationReservesAmount());
            } else {
                dataMap.put("equipmentDepreciationReservesAmount", BigDecimal.ZERO);
            }

            //注入凭证金额 结束

            //如果长期应收款款_未确认收款数组不为空, 则拆分数组
            if (dataMap.containsKey(InterfaceFieldEnum.RECEIVABLE_UNCONFIRM_LIST.getCode())) {
                //长期应收款_未确认收款列表
                List<Map<String, Object>> receivableUnconfirmList = parseListData(dataMap, InterfaceFieldEnum.RECEIVABLE_UNCONFIRM_LIST.getCode()
                        , InterfaceFieldEnum.RECEIVABLE_UNCONFIRM_RECEIPT.getCode()
                        , InterfaceFieldEnum.RECEIVABLE_UNCONFIRM_RECEIPT_SUM.getCode());
                // 数组第一行放所有信息
//                receivableUnconfirmList.get(0).putAll(dataMap);
                //数组汇总行放所有信息
                receivableUnconfirmList.get(receivableUnconfirmList.size() - 1).putAll(dataMap);

                //清空
                finalDataMap.clear();
                // 再添加数据
                finalDataMap.addAll(receivableUnconfirmList);
                copyDataMap.addAll(receivableUnconfirmList);
            }
        }
        //财务赎回
        if (SceneEnum.RKSH.getCode().equals(sceneCode)) {
            //注入财务入库累计金额
            ContractInterfaceTotalDTO contractInterfaceTotalDTO = contractInterfaceTotalService.getByContractCode(contractCode);
            if (contractInterfaceTotalDTO != null) {
                dataMap.putAll(JSONObject.parseObject(JSONObject.toJSONString(contractInterfaceTotalDTO), Map.class));
            }
            // 注入凭证金额 开始
            RecyclingEquipmentInDetailVO recyclingEquipmentInDetailVO = iRecyclingEquipmentInDetailService.getProvision(contractCode, orgId);
            if (ObjectUtil.isNotEmpty(recyclingEquipmentInDetailVO)) {
                // 入库时回收融资租赁设备成本 receiveCost:按合同+签约主体查eg_recycling_equipment_in_detail的recycling_equipment_cost（已提交、已复核、已传至金蝶的最新一条）
                dataMap.put(InterfaceFieldEnum.RECEIVE_COST.getCode(), recyclingEquipmentInDetailVO.getRecyclingEquipmentCost());
                // 入库时应收租金余额	receivableLeaseBalance:按合同+签约主体查eg_recycling_equipment_in_detail的receivable_rent_balance
                dataMap.put(InterfaceFieldEnum.RECEIVABLE_LEASE_BALANCE.getCode(), recyclingEquipmentInDetailVO.getReceivableRentBalance());
                // 入库时应收期末残值余额	residualBalance：按合同+签约主体查eg_recycling_equipment_in_detail的receivable_residual_value_balance
                dataMap.put(InterfaceFieldEnum.RESIDUAL_BALANCE.getCode(), recyclingEquipmentInDetailVO.getReceivableResidualValueBalance());
                // 入库时应收销项税余额	receivableOuttaxBalance：按合同+签约主体查eg_recycling_equipment_in_detail的receivable_outtax_balance
                dataMap.put(InterfaceFieldEnum.RECEIVABLE_OUTTAX_BALANCE.getCode(), recyclingEquipmentInDetailVO.getReceivableOuttaxBalance());
                // 入库时未实现融资收益余额	unrealizedRevenueBalance：按合同+签约主体查eg_recycling_equipment_in_detail的unrealized_revenue_balance
                dataMap.put(InterfaceFieldEnum.UNREALIZED_REVENUE_BALANCE.getCode(), recyclingEquipmentInDetailVO.getUnrealizedRevenueBalance());
                // 入库时承租人保证金余额	receivableMarginBalance：按合同+签约主体查eg_recycling_equipment_in_detail的lessee_margin_balance
                dataMap.put(InterfaceFieldEnum.RECEIVABLE_MARGIN_BALANCE.getCode(), recyclingEquipmentInDetailVO.getLesseeMarginBalance());
                // 入库时计提减值	provisionBalance：按合同+签约主体查eg_recycling_equipment_in_detail的provision_for_impairment
                dataMap.put(InterfaceFieldEnum.PROVISION_BALANCE.getCode(), recyclingEquipmentInDetailVO.getProvisionForImpairment());
            }
            RecyclingEquipmentOutDetailEntity recyclingEquipmentOutDetailEntity = iRecyclingEquipmentOutDetailService.getOne(new LambdaQueryWrapper<RecyclingEquipmentOutDetailEntity>()
                    .eq(RecyclingEquipmentOutDetailEntity::getContractCode, contractCode)
                    .eq(RecyclingEquipmentOutDetailEntity::getOrgId, orgId)
                    .in(RecyclingEquipmentOutDetailEntity::getProcessStatus, ProcessStatusEnum.getCannotModifyCode())
                    .orderByDesc(RecyclingEquipmentOutDetailEntity::getId)
                    .last("limit 1")
            );
            if (ObjectUtil.isNotEmpty(recyclingEquipmentOutDetailEntity)) {
                // 出库时回收融资租赁设备减值准备余额	outboundEquipmentProvisionBalance：按合同+签约主体查eg_recycling_equipment_out_detail的provision_for_impairment
                dataMap.put(InterfaceFieldEnum.OUTBOUND_EQUIPMENT_PROVISION_BALANCE.getCode(), recyclingEquipmentOutDetailEntity.getProvisionForImpairment());
                // 出库时回收融资租赁设备成本余额	outboundReceiveCostBalance：按合同+签约主体查eg_recycling_equipment_out_detail的recycling_equipment_cost
                dataMap.put(InterfaceFieldEnum.OUTBOUND_RECEIVE_COST_BALANCE.getCode(), recyclingEquipmentOutDetailEntity.getRecyclingEquipmentCost());
            }
            // 回收融资租赁设备减值准备余额	equipmentProvisionBalance：按合同+签约主体查最新余额表sum(equipment_depreciation_reserves_balance)
            List<ContractBalanceLatestEntity> latestEntityList = contractBalanceLatestService.list(new LambdaQueryWrapper<ContractBalanceLatestEntity>()
                    .eq(ContractBalanceLatestEntity::getContractCode, contractCode).eq(ContractBalanceLatestEntity::getOrgId, orgId));
            dataMap.put("equipmentProvisionBalance", latestEntityList.stream().map(ContractBalanceLatestEntity::getEquipmentDepreciationReservesBalance).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 注入凭证金额 结束
        }
        //合同撤销
        if (SceneEnum.HTCX.getCode().equals(sceneCode)) {
            //应付租赁设备款列表
            List<Map<String, Object>> payableDeviceList = parseListData(dataMap, InterfaceFieldEnum.PAYABLE_DEVICE_LIST.getCode()
                    , InterfaceFieldEnum.PAYABLE_DEVICE.getCode()
                    , InterfaceFieldEnum.PAYABLE_DEVICE_SUM.getCode());
            finalDataMap.addAll(payableDeviceList);
            copyDataMap.addAll(payableDeviceList);

            //合同解约及更改手续费列表
            List<Map<String, Object>> receiveTerminateProcedureList = parseListData(dataMap, InterfaceFieldEnum.RECEIVE_TERMINATE_PROCEDURE_LIST.getCode()
                    , InterfaceFieldEnum.RECEIVE_TERMINATE_PROCEDURE.getCode()
                    , InterfaceFieldEnum.RECEIVE_TERMINATE_PROCEDURE_SUM.getCode());
            finalDataMap.addAll(receiveTerminateProcedureList);
            copyDataMap.addAll(receiveTerminateProcedureList);
            // Map<String, Object> balanceMap = contractBalanceService.getLastBalanceMap(businessCode, clientCode, contractCode);
            // //将所有余额字段放入参数中
            // if (null != balanceMap && !balanceMap.isEmpty()) {
            //     for (Map.Entry<String, Object> entry : balanceMap.entrySet()) {
            //         if (entry.getKey().contains("_balance")) {
            //             dataMap.put(entry.getKey(), entry.getValue());
            //         }
            //     }
            // }
            // 新增字段
            List<ContractBalanceEntity> contractBalanceEntityList = contractBalanceService.list(new LambdaQueryWrapper<ContractBalanceEntity>().eq(ContractBalanceEntity::getContractCode, contractCode)
                    .eq(ContractBalanceEntity::getOrgId, orgId).eq(ContractBalanceEntity::getSceneCode, SceneEnum.HTQZ.getCode())
            );
            if (CollUtil.isNotEmpty(contractBalanceEntityList)) {
                dataMap.put("receivableRentAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getReceivableRentAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receivableDownpaymentAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getReceivableDownpaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receivableCommissionAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getReceivableCommissionAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receivableInsuranceAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getReceivableInsuranceAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receivableResidualValueAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getReceivableResidualValueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receivableOtherincomeAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getReceivableOtherincomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receivableOuttaxAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getReceivableOuttaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("payableDeviceEstimateAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getPayableDeviceEstimateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("payableOtherCostEstimateAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getPayableOtherCostEstimateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("payableAgencyEstimateAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getPayableAgencyEstimateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("payableInsuranceEstimateAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getPayableInsuranceEstimateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("payableBandCostAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getPayableBandCostAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("unrealizedRevenueAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getUnrealizedRevenueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveSumAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getReceiveSumAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveSumOuttaxAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getReceiveSumOuttaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("payableOtherEstimateAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getPayableOtherEstimateAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dataMap.put("receiveUnrealizedRevenueAmount", contractBalanceEntityList.stream().map(ContractBalanceEntity::getReceiveUnrealizedRevenueAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            }
        }
        //租赁收款-需要根据deductionMarginContract 合同号 找到签约主体注入deductionMarginOrgId
        String deductionMarginContract = MapUtil.getStr(dataMap, InterfaceFieldEnum.DEDUCTION_MARGIN_CONTRACT.getCode());

        if (SceneEnum.ZLSK.getCode().equals(sceneCode)) {
            String receiveProcedureAmount = MapUtil.getStr(dataMap, InterfaceFieldEnum.RECEIVE_PROCEDURE_AMOUNT.getCode());
            if (StringUtils.isNotEmpty(deductionMarginContract)) {
                List<ContractEntity> contractEntityList = contractService.lambdaQuery().eq(ContractEntity::getContractCode, deductionMarginContract).list();
                if (CollectionUtil.isNotEmpty(contractEntityList)) {
                    contractEntityList = contractEntityList.stream().filter(v -> StringUtils.isEmpty(v.getContractCodeM())).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(contractEntityList)) {
                        dataMap.put(InterfaceFieldEnum.DEDUCTION_MARGIN_ORG_ID.getCode(), contractEntityList.get(0).getOrgId());
                    }
                }
            }
            if (StringUtils.isNotEmpty(receiveProcedureAmount)) {
                setRentSettlementWay(dataMap);
            }
        }
        //如果contractCode!=deductionMarginContract 复制一份数据
        if (StringUtils.isNotEmpty(deductionMarginContract) && !contractCode.equals(deductionMarginContract)) {
            dataMap.put(RuleConstant.CROSS_CONTRACT_FLAG, "0");
            log.info("原始map参数：{}", dataMap);
            String newOrgId = MapUtil.getStr(dataMap, InterfaceFieldEnum.DEDUCTION_MARGIN_ORG_ID.getCode());
            copyMap.putAll(dataMap);
            copyMap.put(RuleConstant.CROSS_CONTRACT_FLAG, "1");
            copyMap.put(RuleConstant.FIELD_CONTRACT_CODE, deductionMarginContract);
            copyMap.put(InterfaceFieldEnum.DEDUCTION_MARGIN_CONTRACT.getCode(), contractCode);
            copyMap.put(InterfaceFieldEnum.DEDUCTION_MARGIN_ORG_ID.getCode(), orgId);
            copyMap.put(InterfaceFieldEnum.ORG_ID.getCode(), newOrgId);
            log.info("copy参数：{}", copyMap);
            copyDataMap.add(copyMap);
            finalDataMap.addAll(copyDataMap);
        }
        //统一赋值businessDate
        for (Map<String, Object> map : finalDataMap) {
            String newBusinessDate = MapUtil.getStr(map, RuleConstant.FIELD_BUSINESS_DATE);
            String newSystemCode = MapUtil.getStr(map, RuleConstant.FIELD_SYSTEM_CODE);
            String newInterfaceCreateTime = MapUtil.getStr(map, RuleConstant.INTERFACE_CREATE_TIME);
            if (StringUtils.isEmpty(newBusinessDate)) {
                map.put(RuleConstant.FIELD_BUSINESS_DATE, businessDate);
            }
            if (StringUtils.isEmpty(newSystemCode)) {
                map.put(RuleConstant.FIELD_SYSTEM_CODE, systemCode);
            }
            if (StringUtils.isEmpty(newInterfaceCreateTime)) {
                map.put(RuleConstant.INTERFACE_CREATE_TIME, interFaceCreateTime);
            }
        }
        return finalDataMap;
    }

    public void setRentSettlementWay(Map<String, Object> dataMap) {
        String businessDate = MapUtil.getStr(dataMap, RuleConstant.FIELD_BUSINESS_DATE);
        String contractCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE);
        if (StringUtils.isEmpty(businessDate) || StringUtils.isEmpty(contractCode)) {
            return;
        }
        Date businessDateTime = CommonDateUtils.parseDateStringToDate(businessDate);
        dataMap.put(InterfaceFieldEnum.RENT_SETTLEMENT_WAY.getCode(), getSettlementWay(businessDateTime, contractCode));
    }

    public String getSettlementWay(Date businessDateTime, String contractCode) {
        RepaymentPlanVO repaymentPlanVO = repaymentPlanService.getPlanByMaxActualRepaymentDate(businessDateTime, contractCode);
        if (null != repaymentPlanVO) {
            return repaymentPlanVO.getSettlementWay();
        }
        return "";
    }

    private List<Map<String, Object>> parseListData(Map<String, Object> dataMap, String listDataKey, String singleDataKey, String sumDataKey) {
        List<Map<String, Object>> finalDataMap = new ArrayList<>();
        String sceneCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE);
        if (dataMap.containsKey(listDataKey)) {
            List<ListAmountDTO> listAmountDTOList = JSONArray.parseArray(JSONArray.toJSONString(dataMap.get(listDataKey)), ListAmountDTO.class);
            if (CollectionUtil.isNotEmpty(listAmountDTOList)) {
                dataMap.remove(listDataKey);//删除数组key
                BigDecimal totalAmount = BigDecimal.ZERO;
                for (ListAmountDTO listAmountDTO : listAmountDTOList) {
                    Map<String, Object> lineDataMap = cloneRequiredInterfaceData(dataMap);
                    lineDataMap.put(singleDataKey, listAmountDTO.getEbankAmount());//添加单个金额
//                    lineDataMap.put(InterfaceFieldEnum.ORG_ID.getCode(), listAmountDTO.getOrgId()); //覆盖签约主体
                    lineDataMap.put(InterfaceFieldEnum.EBANK_NUM.getCode(), listAmountDTO.getEbankNum()); //覆盖网银编号
                    lineDataMap.put(InterfaceFieldEnum.AMOUNT_TYPE.getCode(), listAmountDTO.getAmountType()); //覆盖金额类型
                    lineDataMap.put("ebankSerialNumber", listAmountDTO.getEbankSerialNumber()); //网银编号
                    lineDataMap.put("ebankBatchNo", listAmountDTO.getEbankBatchNo()); //批扣流水号
                    lineDataMap.put("bankOrgId", listAmountDTO.getBankOrgId()); //批扣流水号
                    totalAmount = NumberUtil.add(totalAmount, listAmountDTO.getEbankAmount());
                    //合同撤销场景拆分的数据场景编码改为租赁收款 不用转换了
                    // if (SceneEnum.HTCX.getCode().equals(sceneCode)) {
                    //     lineDataMap.put(RuleConstant.FIELD_SCENE_CODE, SceneEnum.HTCXSK.getCode());
                    // }
                    finalDataMap.add(lineDataMap);
                }
                //数组字段合计金额单独传一个接口数据
                Map<String, Object> totalMap = cloneRequiredInterfaceData(dataMap);
                totalMap.put(sumDataKey, totalAmount);
                // if (SceneEnum.HTCX.getCode().equals(sceneCode)) {
                //     totalMap.put(RuleConstant.FIELD_SCENE_CODE, SceneEnum.HTCXSK.getCode());
                // }
                finalDataMap.add(totalMap);
            }
        }
        return finalDataMap;
    }


    /**
     * 克隆必填的接口数据
     *
     * @param dataMap
     * @return
     */
    private Map<String, Object> cloneRequiredInterfaceData(Map<String, Object> dataMap) {
        Map<String, Object> newDataMap = new HashMap<>();
        if (dataMap.containsKey(RuleConstant.FIELD_BUSINESS_CODE)) {
            newDataMap.put(RuleConstant.FIELD_BUSINESS_CODE, dataMap.get(RuleConstant.FIELD_BUSINESS_CODE));
        }
        newDataMap.put(RuleConstant.FIELD_SCENE_CODE, dataMap.get(RuleConstant.FIELD_SCENE_CODE));
        newDataMap.put(RuleConstant.FIELD_CONTRACT_CODE, dataMap.get(RuleConstant.FIELD_CONTRACT_CODE));
        newDataMap.put(RuleConstant.FIELD_CLIENT_CODE, dataMap.get(RuleConstant.FIELD_CLIENT_CODE));
        newDataMap.put(RuleConstant.FIELD_ORG_ID, dataMap.get(RuleConstant.FIELD_ORG_ID));
        newDataMap.put(RuleConstant.FIELD_SCENE_NAME, dataMap.get(RuleConstant.FIELD_SCENE_NAME));
        newDataMap.put(RuleConstant.INTERFACE_CREATE_TIME, dataMap.get(RuleConstant.INTERFACE_CREATE_TIME));
        return newDataMap;
    }

    private List<VoucherDTO> executeSingleInterface(String sceneCode, Map<String, Object> dataMap) {

        List<VoucherDTO> voucherDTOList = new ArrayList<>();

        //注入业务编码
        injectBusinessCode(dataMap);

        String businessCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_BUSINESS_CODE);
        String systemCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SYSTEM_CODE);
        if (businessCode == null) {
            throw new ServiceException("业务编码[businessCode]不能为空");
        }
        BusinessDTO businessDTO = getRedisBusinessByCode(businessCode);
        if (businessDTO == null) {
            throw new ServiceException(StrUtil.format("业务编码[{}]不存在", businessCode));
        }
        // 通用场景直接使用接口中的 bankNo；物业收付款场景仍按其历史字段规则覆盖。
        String bankNo = MapUtil.getStr(dataMap, "bankNo");
        String transactionType = "";
        String paymentMethod = "";
        if (sceneCode.equals(SceneEnum.WYLSFK.getCode()) || sceneCode.equals(SceneEnum.WYLSSK.getCode())) {
            transactionType = MapUtil.getStr(dataMap, RuleConstant.TRANSACTION_TYPE);
            paymentMethod = MapUtil.getStr(dataMap, RuleConstant.FIELD_PAYMENT_METHOD);
            if ("collection".equals(transactionType)) {
                bankNo = MapUtil.getStr(dataMap, RuleConstant.BANK_NO);
            } else if ("payment".equals(transactionType)) {
                bankNo = MapUtil.getStr(dataMap, RuleConstant.PAY_BANK_NO);
            }
            if (StringUtils.isEmpty(bankNo)) {
                if (StringUtils.isNotEmpty(paymentMethod) && !"票据".equals(paymentMethod)) {
                    throw new ServiceException("银行账号[bankNo]不能为空");
                }
            }
        }
        String ebankNum = MapUtil.getStr(dataMap, RuleConstant.EBANK_NUM);
        if (StringUtils.isNotEmpty(bankNo)) {
            ebankNum = bankNo;
        }
        if ((sceneCode.equals(SceneEnum.ZLSK.getCode()) ||
                sceneCode.equals(SceneEnum.HTCX.getCode())
                || sceneCode.equals(SceneEnum.RKCZ.getCode())
        ) && (SystemEnum.SYCXT.getCode().equals(systemCode) || SystemEnum.CYCXT.getCode().equals(systemCode))) {
            String bankOrgId = MapUtil.getStr(dataMap, RuleConstant.BANK_ORG_ID);
            if (StringUtils.isNotEmpty(bankOrgId)) {
                dataMap.put(RuleConstant.BANK_ORG_ID, bankOrgId);
            } else {
                dataMap.put(RuleConstant.BANK_ORG_ID, MapUtil.getStr(dataMap, RuleConstant.FIELD_ORG_ID));
            }
        } else if (StringUtils.isNotEmpty(ebankNum) && !"null".equals(ebankNum)) {
            //银行账号签约主体
            Map<String, Object> bankOrgIdMap = getBankOrgIdMap(ebankNum);
            if (MapUtil.isNotEmpty(bankOrgIdMap)) {
                dataMap.putAll(bankOrgIdMap);
                if (StringUtils.isNotEmpty(bankNo)) {
                    dataMap.put(RuleConstant.FIELD_ORG_ID, bankOrgIdMap.get(RuleConstant.BANK_ORG_ID));
                }

            } else {
                String ebankSerialNumber = MapUtil.getStr(dataMap, RuleConstant.EBANK_SERIAL_NUMBER);
                List<FundEbankTransactionDataEntity> list =
                        fundEbankTransactionDataMapper.selectList(new LambdaQueryWrapper<FundEbankTransactionDataEntity>().eq(FundEbankTransactionDataEntity::getEbankNumber, ebankSerialNumber).orderByDesc(FundEbankTransactionDataEntity::getId));
                if (CollectionUtil.isNotEmpty(list)) {
                    dataMap.put(RuleConstant.BANK_ORG_ID, list.get(0).getBankOrgId());
                } else {
                    String orgId = MapUtil.getStr(dataMap, RuleConstant.FIELD_ORG_ID);
                    dataMap.put(RuleConstant.BANK_ORG_ID, orgId);
                }
            }

        }
        //注入默认金额0
        injectDefaultNumberFields(sceneCode, dataMap);

        //去掉空字符串的字段
        cleanStringEmptyFields(dataMap);

        //替换数组字段
//        replaceListFields(sceneCode, dataMap);

        //业务编码
        List<BusinessVO> allBusinessList = selectRedisBusinessAll();
        allBusinessList.forEach(e -> dataMap.put(e.getBusinessCode(), e.getBusinessCode()));
        //获取bankOrgId对应的客户编码
        setBankOrgIdClientCode(dataMap);
        //保存关联数据
        InterfaceDataDTO interfaceDataDTO = saveInterfaceData(dataMap);
        if (StringUtils.isNotEmpty(bankNo)) {
            interfaceDataDTO.setBankNo(bankNo);
        }

        //合同余额表
        List<String> assistFlags = sceneVoucherEntryService.selectDistinctAssistFlagsBySceneCode(sceneCode);
//        Map<String, Object> contractBalanceMap = contractBalanceService.getLastBalanceMap(interfaceDataDTO.getBusinessCode(), interfaceDataDTO.getClientCode(), interfaceDataDTO.getContractCode());
        Map<String, Object> contractBalanceMap = contractBalanceLatestService.queryFullLastBalanceMap(interfaceDataDTO.getBusinessCode(), interfaceDataDTO.getOrgId(), interfaceDataDTO.getContractCode(), interfaceDataDTO.getClientCode(), interfaceDataDTO.getBillContractCode(), assistFlags);
        if (MapUtil.isNotEmpty(contractBalanceMap)) {
            dataMap.putAll(contractBalanceMap);
        }

        //合同表
        Map<String, Object> contractMap = contractService.getContractMap(interfaceDataDTO.getContractCode(), interfaceDataDTO.getOrgId());
        if (MapUtil.isNotEmpty(contractMap)) {
            dataMap.putAll(contractMap);
            interfaceDataDTO.setContractClientCode(MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CLIENT_CODE));
            interfaceDataDTO.setContractLeaseType(MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_LEASE_TYPE));
        }
        //合同月表
        Map<String, Object> contractMonthMap = contractMonthService.getContractMap(interfaceDataDTO.getContractCode(), interfaceDataDTO.getOrgId());
        if (MapUtil.isNotEmpty(contractMonthMap)) {
            dataMap.putAll(contractMonthMap);
        }

        //客户表
        Map<String, Object> clientMap = clientService.getClientMap(interfaceDataDTO.getClientCode());
        if (MapUtil.isNotEmpty(clientMap)) {
            dataMap.putAll(clientMap);
            if (SceneEnum.ZLHL.getCode().equals(sceneCode)) {
                dataMap.put(RuleConstant.FIELD_CLIENT_NAME, clientMap.get(RuleConstant.FIELD_CLIENT_TABLE_NAME));
            }
        }

        //税率
        List<TaxRateDTO> taxRateDTOList = queryRedisAllForEditor();
        for (TaxRateDTO taxRateDTO : taxRateDTOList) {
            String name = "税率." + taxRateDTO.getBusinessCode() + "." + taxRateDTO.getFundType() + "." + taxRateDTO.getLeaseType();
            if (StrUtil.isNotBlank(taxRateDTO.getLeaseSubType())) {
                name = name + "." + taxRateDTO.getLeaseSubType();
            }
            dataMap.put(name, taxRateDTO.getTaxRate());
        }

        if (SceneEnum.HTQZ.getCode().equals(sceneCode)) {
            repaymentPlanService.prepareLeaseStartCalculatedFields(dataMap);
        }

        //获取翻译后的规则
        List<SceneRuleDTO> ruleDTOList = getTranslateRule(sceneCode, interfaceDataDTO, dataMap);


        for (SceneRuleDTO ruleDTO : ruleDTOList) {
            //执行规则，返回执行后的规则对象
            SceneRuleDTO resultRule = RuleUtil.executeRule(ruleDTO, dataMap);
//            log.info("规则执行结果对象: {}", JSONUtil.toJsonStr(resultRule));
            boolean ruleCondition = BooleanUtil.toBoolean(resultRule.getScriptCondition());
            if (ruleCondition) {
                //根据规则执行结果生成凭证
                VoucherSaveDTO voucherSaveDTO = voucherService.generateVoucherFromRule(resultRule, interfaceDataDTO);
                //校验凭证是否配平
                validateVoucher(voucherSaveDTO);
                voucherSaveDTO.setIsSubmit(MapUtil.getStr(dataMap, RuleConstant.IS_SUBMIT));
                //入库
                VoucherDTO voucherDTO = voucherService.saveVoucherAndEntries(voucherSaveDTO);
                voucherDTOList.add(voucherDTO);
                //设置客户类型
                String clentType = MapUtil.getStr(dataMap, RuleConstant.FIELD_CLIENT_TYPE);
                if (StringUtils.isNotEmpty(clentType)) {
                    voucherDTO.setClientType(clentType);
                }
                if (!StrUtil.equals(voucherDTO.getValidFlag(), VoucherValidFlagEnum.VALID.getCode())) {
                    //如果凭证无效，则直接跳过，不扣减余额
                    continue;
                }
                voucherDTO.setIsFundSystemBalance(YesOrNoEnum.NO.getCode());
                if (sceneCode.equals(SceneEnum.WYLSFK.getCode()) || sceneCode.equals(SceneEnum.WYLSSK.getCode())) {
                    //资金系统的数据需要保存资金余额表
                    voucherDTO.setBankNo(bankNo);
                    voucherDTO.setTransactionType(transactionType);
                    voucherDTO.setIsFundSystemBalance(YesOrNoEnum.YES.getCode());
                    //iFundSystemBalanceService.saveFundSystemBalanceFromVoucher(voucherDTO);
                } else {
                    //保存/更新合同余额表
                    voucherDTO.setIsSubmit(MapUtil.getStr(dataMap, RuleConstant.IS_SUBMIT));
                    // 凭证生成成功即登记合同余额。余额服务按凭证ID保证幂等，
                    // 同步维护发生额、最新余额及未提交临时余额三套数据。
                    contractBalanceService.saveContractBalanceFromVoucher(voucherDTO);
                    //更新科目余额表
                    //accountAssistBalanceService.saveAccountBalanceByVoucherDTO(voucherDTO);
                }
            }
        }

        if (voucherDTOList != null && !voucherDTOList.isEmpty()) {
            // modify by zhangli.chen for 针对有效凭证才更新接口表凭证日期 on 20250929
            VoucherDTO voucherDTO = voucherDTOList.get(FinanceEngineEnum.Numbers.ZERO.getKey());
            if (voucherDTO != null && StrUtil.equals(voucherDTO.getValidFlag(), VoucherValidFlagEnum.VALID.getCode())) {
                interfaceDataDTO.setVoucherDate(voucherDTO.getVoucherDate());
            }
            interfaceDataService.updateInterfaceData(interfaceDataDTO.getId(), interfaceDataDTO);
        }

        return voucherDTOList;
    }

    /**
     * 场景缓存
     *
     * @param sceneCode
     * @return
     */
    public SceneDTO getRedisSceneDTOByCode(String sceneCode) {
        SceneDTO sceneDTO = redisService.getCacheObject(String.format(RedisConstant.V_SCENE_CODE_KEY, sceneCode));
        if (sceneDTO == null) {
            sceneDTO = sceneService.getSceneDTOByCode(sceneCode);
            if (sceneDTO != null) {
                redisService.setCacheObject(String.format(RedisConstant.V_SCENE_CODE_KEY, sceneCode), sceneDTO, RedisConstant.TIME_OUT, TimeUnit.MINUTES);
            }
        }
        return sceneDTO;
    }

    /**
     * 业务缓存
     *
     * @param businessCode
     * @return
     */
    public BusinessDTO getRedisBusinessByCode(String businessCode) {
        BusinessDTO businessDTO = redisService.getCacheObject(String.format(RedisConstant.V_BUSINESS_CODE_KEY, businessCode));
        if (businessDTO == null) {
            businessDTO = businessService.getBusinessByCode(businessCode);
            if (businessDTO != null) {
                redisService.setCacheObject(String.format(RedisConstant.V_BUSINESS_CODE_KEY, businessCode), businessDTO, RedisConstant.TIME_OUT, TimeUnit.MINUTES);
            }
        }
        return businessDTO;
    }

    /**
     * 场景配置缓存
     *
     * @param sceneCode
     * @return
     */
    public List<SceneFieldsDTO> selectRedisNumberSceneFields(String sceneCode) {
        List<SceneFieldsDTO> numberFieldList = redisService.getCacheObject(String.format(RedisConstant.V_FIELD_SCENE_CODE_KEY, sceneCode));
        if (CollectionUtil.isEmpty(numberFieldList)) {
            numberFieldList = sceneFieldsService.selectNumberSceneFields(sceneCode);
            if (CollectionUtil.isNotEmpty(numberFieldList)) {
                redisService.setCacheObject(String.format(RedisConstant.V_FIELD_SCENE_CODE_KEY, sceneCode), numberFieldList, RedisConstant.TIME_OUT, TimeUnit.MINUTES);
            }
        }
        return numberFieldList;
    }

    /**
     * 所有的业务编码
     *
     * @return
     */
    public List<BusinessVO> selectRedisBusinessAll() {
        List<BusinessVO> allBusinessList = redisService.getCacheObject(RedisConstant.V_ALL_BUSINESS);
        if (CollectionUtil.isEmpty(allBusinessList)) {
            allBusinessList = businessService.selectAll(new BusinessQueryDTO());
            if (CollectionUtil.isNotEmpty(allBusinessList)) {
                redisService.setCacheObject(RedisConstant.V_ALL_BUSINESS, allBusinessList, RedisConstant.TIME_OUT, TimeUnit.MINUTES);
            }
        }
        return allBusinessList;
    }

    public List<TaxRateDTO> queryRedisAllForEditor() {
        List<TaxRateDTO> taxRateDTOList = redisService.getCacheObject(RedisConstant.V_ALL_TAX_RATE);
        if (CollectionUtil.isEmpty(taxRateDTOList)) {
            taxRateDTOList = taxRateService.queryAllForEditor();
            if (CollectionUtil.isNotEmpty(taxRateDTOList)) {
                redisService.setCacheObject(RedisConstant.V_ALL_TAX_RATE, taxRateDTOList, RedisConstant.TIME_OUT, TimeUnit.MINUTES);
            }
        }
        return taxRateDTOList;
    }

    public List<SceneRuleDTO> getSceneRuleDTOByCode(String sceneCode) {
        List<SceneRuleDTO> sceneRuleDTOList = redisService.getCacheObject(String.format(RedisConstant.V_SCENE_CODE_RULE, sceneCode));
//        log.info("获取规则：{}",sceneRuleDTOList);
        if (CollectionUtil.isEmpty(sceneRuleDTOList)) {
            sceneRuleDTOList = sceneService.getSceneRuleDTOByCode(sceneCode);
            if (CollectionUtil.isNotEmpty(sceneRuleDTOList)) {
                redisService.setCacheObject(String.format(RedisConstant.V_SCENE_CODE_RULE, sceneCode), sceneRuleDTOList, RedisConstant.TIME_OUT, TimeUnit.MINUTES);
            }
        }
        return sceneRuleDTOList;
    }

    /**
     * 字典金额
     *
     * @return
     */
    public R<List<SysDictData>> listDictTypeData(String dictType) {
        R<List<SysDictData>> sysDictR = redisService.getCacheObject(String.format(RedisConstant.V_DICT_SYS_CASH_TYPE, dictType));
        if (ObjectUtil.isNull(sysDictR)) {
            sysDictR = remoteDictService.listDictData(dictType);
            if (ObjectUtil.isNotNull(sysDictR)) {
                redisService.setCacheObject(String.format(RedisConstant.V_DICT_SYS_CASH_TYPE, dictType), sysDictR, RedisConstant.TIME_OUT, TimeUnit.MINUTES);
            }
        }
        return sysDictR;
    }


    @Override
    public List<VoucherInfoVO> batchExecuteRule(List<Map<String, Object>> dataMapList) {
        List<VoucherInfoVO> resultVo = new CopyOnWriteArrayList<>();
        try {
            log.info("批量生成凭证开始，参数{}", JSONObject.toJSONString(dataMapList));
            long startDate = System.currentTimeMillis();
            log.info("生成凭证开始时间：{}", startDate);
            if (CollectionUtil.isEmpty(dataMapList)) {
                throw new ServiceException("凭证参数不可以为空");
            }
            //按照合同编码+客户编码+签约主体+借款合同编号分组
            Map<String, List<Map<String, Object>>> groupListMap = dataMapList.stream()
                    .collect(Collectors.groupingBy(v -> MapUtil.getStr(v, RuleConstant.FIELD_ORG_ID)
                                    + "-" + MapUtil.getStr(v, RuleConstant.FIELD_CONTRACT_CODE),
//                                    + "-" + MapUtil.getStr(v, RuleConstant.FIELD_CLIENT_CODE)
//                                    + "-" + MapUtil.getStr(v, RuleConstant.FIELD_BILL_CONTRACT_CODE)
                            Collectors.collectingAndThen(
                                    Collectors.toList(),
                                    //根据ID排序，遵循先进先出执行
                                    list -> list.stream()
                                            .sorted((map1, map2) -> {
                                                Long id1 = map1.get(RuleConstant.FIELD_ID) == null ? 0L : (Long) map1.get(RuleConstant.FIELD_ID);
                                                Long id2 = map2.get(RuleConstant.FIELD_ID) == null ? 0L : (Long) map2.get(RuleConstant.FIELD_ID);
                                                return Long.compare(id1, id2); // 升序排序
                                            })
                                            .collect(Collectors.toList())
                            )));
            CountDownLatch latch = new CountDownLatch(dataMapList.size());
            for (Map.Entry<String, List<Map<String, Object>>> entry : groupListMap.entrySet()) {
                log.info("分组之后的顺序：{}", JSON.toJSONString(entry.getValue()));
                asyncTaskExecutor.submit(() -> {
                    log.info("线程：" + Thread.currentThread().getId());
                    // 使用 Stream API 根据 id 属性值升序排序
//                    List<Map<String, Object>> sortedList = entry.getValue().stream()
//                            .sorted((map1, map2) -> {
//                                int id1 = (int) map1.get(RuleConstant.FIELD_ID);
//                                int id2 = (int) map2.get(RuleConstant.FIELD_ID);
//                                return Integer.compare(id1, id2); // 升序排序
//                            })
//                            .collect(Collectors.toList());
                    for (Map<String, Object> dataMap : entry.getValue()) {
                        VoucherInfoVO voucherInfoVO = new VoucherInfoVO();
                        try {
                            getExecuteRuleResult(dataMap, voucherInfoVO);
                        } catch (Exception | Error e) {
                            log.error("执行凭证失败，失败原因:", e);
                        } finally {
                            latch.countDown();
                            resultVo.add(voucherInfoVO);
                        }
                    }
                });
            }
            latch.await();
            long endDate = System.currentTimeMillis();
            log.info("生成凭证结束时间：{},共花费时间：{}", endDate, endDate - startDate);
            return resultVo;
        } catch (Exception e) {
            log.error("batchExecuteRule fail!", e);
            throw new ServiceException("批量生成凭证失败，失败原因:" + e.getMessage());
        }
    }

    @Override
    public void getExecuteRuleResult(Map<String, Object> dataMap, VoucherInfoVO voucherInfoVO) {
//        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition());
        Map<String, List<VoucherDTO>> resultMap = Maps.newHashMap();
        String orderId = MapUtil.getStr(dataMap, RuleConstant.FIELD_ORDER_ID);
        voucherInfoVO.setOrderId(orderId);

        String sceneCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE);
        voucherInfoVO.setSceneCode(sceneCode);
        String contractCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE);
        voucherInfoVO.setContractCode(contractCode);
        String orgId = MapUtil.getStr(dataMap, RuleConstant.FIELD_ORG_ID);
        voucherInfoVO.setOrgId(orgId);

        try {
            if (StringUtils.isEmpty(orderId)) {
                throw new ServiceException("订单ID不可以为空");
            }
            List<VoucherDTO> voucherDTOList = executeRule(dataMap);
            resultMap.put(orderId, voucherDTOList);
            voucherInfoVO.setVoucherDTOList(voucherDTOList);
//            dataSourceTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            log.error("execute rule exception", e);
            voucherInfoVO.setErrorInfo(e.getMessage());
//            dataSourceTransactionManager.rollback(transactionStatus);
        }
    }

    public void cleanStringEmptyFields(Map<String, Object> dataMap) {
        for (Iterator<Map.Entry<String, Object>> it = dataMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> item = it.next();
            Object value = item.getValue();
            if (value instanceof String && StrUtil.isBlank((CharSequence) value)) {
                it.remove();
            }
        }
    }

    public Map<String, Object> getBankOrgIdMap(String ebankNum) {
        Map<String, Object> orgIdMap = Maps.newHashMap();
        if (StringUtils.isEmpty(ebankNum)) {
            return orgIdMap;
        }
        List<BankAccountEntity> bankAccountEntityList = iBankAccountService.lambdaQuery().eq(BankAccountEntity::getBankAccountNumber, ebankNum).list();
        if (CollectionUtil.isNotEmpty(bankAccountEntityList)) {
            orgIdMap.put(RuleConstant.BANK_ORG_ID, bankAccountEntityList.get(0).getOrgId());
        }
        return orgIdMap;
    }

    private void validateVoucher(VoucherSaveDTO voucherSaveDTO) {
        List<VoucherEntrySaveDTO> allEntryList = voucherSaveDTO.getEntryList();
        if (CollectionUtil.isEmpty(allEntryList)) {
            voucherSaveDTO.setValidFlag(VoucherValidFlagEnum.ENTRY_EMPTY.getCode());
            return;
        }
        //签约主体为空，数据无效
        if (StringUtils.isEmpty(voucherSaveDTO.getOrgId())) {
            voucherSaveDTO.setValidFlag(VoucherValidFlagEnum.NO_VALID.getCode());
            return;
        }
//        List<String> accountCodeList = allEntryList.stream().map(VoucherEntrySaveDTO::getAccountCode).collect(Collectors.toList());
//        //按照科目编码查到核算类型
//        Map<String,String> accountCodeMap = new HashMap<>();
//        if (CollectionUtil.isNotEmpty(accountCodeList)) {
//            accountCodeMap = iAccountService.lambdaQuery().in(AccountEntity::getAccountCode, accountCodeList).eq(AccountEntity::getBusinessCode,voucherSaveDTO.getBusinessCode()).list().stream().collect(HashMap::new, (h, v) -> h.put(v.getAccountCode(), v.getSettlementType()), HashMap::putAll);
//        }
//        //修改：按照核算类型筛选出表内的数据 2024-02-26
//        Map<String, String> finalAccountCodeMap = accountCodeMap;
//        log.info("核算类型{}",JSON.toJSON(finalAccountCodeMap));
//        allEntryList.stream().forEach(v -> {
//            if (finalAccountCodeMap.containsKey(v.getAccountCode())) {
//                v.setSettlementType(finalAccountCodeMap.get(v.getAccountCode()));
//            }
//        });
//        log.info("分录数据{}",JSON.toJSON(allEntryList));
//        List<VoucherEntrySaveDTO> innerEntryList = allEntryList.stream().filter(v -> "intra".equals(v.getSettlementType())).collect(Collectors.toList());
        //提取表内科目的凭证行 1-6开头的科目为表内科目
        List<VoucherEntrySaveDTO> innerEntryList = allEntryList.stream().filter(e -> StrUtil.startWithAny(e.getAccountCode(), "1", "2", "3", "4", "5", "6")).collect(Collectors.toList());
        //校验是否平
        //借方总金额
        BigDecimal debitTotalAmount = innerEntryList.stream().filter(e -> StrUtil.equals(DRCREnum.DR.getCode(), e.getDebitCreditType())).map(VoucherEntrySaveDTO::getDebitAmount).collect(Collectors.toList()).stream().map(v -> v.setScale(2, RoundingMode.HALF_UP)).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal creditTotalAmount = innerEntryList.stream().filter(e -> StrUtil.equals(DRCREnum.CR.getCode(), e.getDebitCreditType())).map(VoucherEntrySaveDTO::getCreditAmount).collect(Collectors.toList()).stream().map(v -> v.setScale(2, RoundingMode.HALF_UP)).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (!NumberUtil.equals(debitTotalAmount, creditTotalAmount)) {
            voucherSaveDTO.setValidFlag(VoucherValidFlagEnum.NOT_EQUALS.getCode());
        }
    }


    public void validateRequiredField(Map<String, Object> dataMap) {
        if (StrUtil.isBlank(MapUtil.getStr(dataMap, RuleConstant.FIELD_SYSTEM_CODE))) {
            throw new ServiceException("系统编码[systemCode]不能为空");
        }
        if (StrUtil.isBlank(MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE))) {
            throw new ServiceException("场景编码[sceneCode]不能为空");
        }
        if (StrUtil.isBlank(MapUtil.getStr(dataMap, RuleConstant.FIELD_BUSINESS_DATE))) {
            throw new ServiceException("业务日期[businessDate]不能为空");
        }
    }

    public void preBusinessCode(Map<String, Object> dataMap) {
        //场景类型为咨询服务费 按照主合同"contractCodeM"填充业务类型，多条取任意一条，为空默认中台
        String sceneCodeOriginal = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE_ORIGINAL);
        String contractCodeM = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE_M);
        String contractCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE);
        String systemCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SYSTEM_CODE);
        String mappedSceneCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE);
        if (SceneEnum.HTQZ.getCode().equals(mappedSceneCode)) {
            prepareLeaseStartAccountingVariant(dataMap, systemCode, sceneCodeOriginal);
        }
        //开票系统需要替换掉合同编码包含"-A","-1"的合同
        if (SystemEnum.KPXT.getCode().equals(systemCode)
                && StringUtils.isNotEmpty(contractCode)
                && contractCode.contains(Constants.CONTRACT_CODE_1)) {
            dataMap.put(RuleConstant.FIELD_CONTRACT_CODE, contractCode.replace(Constants.CONTRACT_CODE_1, ""));
        }
        if (SceneEnum.ZXFUFQY.getDesc().equals(sceneCodeOriginal) || SceneEnum.ZXFWFQY.getDesc().equals(sceneCodeOriginal)) {
            if (StringUtils.isNotEmpty(contractCodeM)) {
                List<ContractEntity> contractEntityList = contractService.lambdaQuery().eq(ContractEntity::getContractCodeM, contractCodeM).list();
                if (CollectionUtil.isNotEmpty(contractEntityList)) {
                    dataMap.put(RuleConstant.FIELD_BUSINESS_CODE, contractEntityList.get(0).getBusinessCode());
                }
            }
            String businessCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_BUSINESS_CODE);
            if (StringUtils.isEmpty(businessCode)) {
                dataMap.put(RuleConstant.FIELD_BUSINESS_CODE, BusinessEnum.ZLYW.getCode());
            }
        }
    }

    /**
     * Selects an account mapping set without changing the real business code.
     * The extra fields also make mutually-exclusive start-event rules explicit,
     * so one canonical amount type is configured only once in each template.
     */
    private void prepareLeaseStartAccountingVariant(Map<String, Object> dataMap,
                                                     String systemCode,
                                                     String sceneCodeOriginal) {
        // Only raw business attributes are accepted from upstream. Event and
        // accounting variants are always derived internally.
        copyLeaseStartAlias(dataMap, "asset_category", "assetCategory");
        copyLeaseStartAlias(dataMap, "lease_category", "leaseCategory");
        copyLeaseStartAlias(dataMap, "lease_method", "leaseMethod");
        copyLeaseStartAlias(dataMap, "received_service_fee", "receivedServiceFee");
        copyLeaseStartAlias(dataMap, "amortized_service_fee", "amortizedServiceFee");

        String sourceSystem = firstNonBlank(systemCode,
                MapUtil.getStr(dataMap, "source_system"),
                MapUtil.getStr(dataMap, "sourceSystem"));
        String rawEvent = firstNonBlank(MapUtil.getStr(dataMap, "event_code"),
                MapUtil.getStr(dataMap, "eventCode"), sceneCodeOriginal);
        String leaseMethod = firstNonBlank(MapUtil.getStr(dataMap, "lease_method"),
                MapUtil.getStr(dataMap, "leaseMethod"), MapUtil.getStr(dataMap, "returnType"));
        String leaseCategory = firstNonBlank(MapUtil.getStr(dataMap, "lease_category"),
                MapUtil.getStr(dataMap, "leaseCategory"), MapUtil.getStr(dataMap, "leaseType"));
        String assetCategory = firstNonBlank(MapUtil.getStr(dataMap, "asset_category"),
                MapUtil.getStr(dataMap, "assetCategory"), MapUtil.getStr(dataMap, "business_line"),
                MapUtil.getStr(dataMap, "businessPlate"));

        if (StringUtils.isNotEmpty(sourceSystem)) {
            dataMap.put("source_system", sourceSystem);
        }
        if (StringUtils.isNotEmpty(rawEvent)) {
            dataMap.put("event_code", rawEvent);
        }

        String eventVariant = normalizeLeaseStartEvent(rawEvent);
        if (StringUtils.isNotEmpty(eventVariant)) {
            dataMap.put("start_event_variant", eventVariant);
        }

        sourceSystem = normalizeLeaseStartSourceSystem(sourceSystem);
        leaseMethod = normalizeLeaseStartLeaseMethod(leaseMethod);
        leaseCategory = normalizeLeaseStartLeaseCategory(leaseCategory);
        assetCategory = normalizeLeaseStartAssetCategory(assetCategory);
        dataMap.put("source_system", sourceSystem);
        if (leaseMethod != null) dataMap.put("lease_method", leaseMethod);
        if (leaseCategory != null) dataMap.put("lease_category", leaseCategory);
        if (assetCategory != null) dataMap.put("asset_category", assetCategory);

        String accountingVariant;
        if ("FINANCE_LEASE".equals(sourceSystem)) {
            if (assetCategory == null) {
                throw new ServiceException("融资租赁合同起租事件缺少有效资产类别[asset_category]，可选值：REAL_ESTATE、MOVABLE");
            }
            if (leaseMethod == null) {
                throw new ServiceException("融资租赁合同起租事件缺少有效租赁方式[lease_method]，可选值：DIRECT_LEASE、SALE_AND_LEASEBACK");
            }
            accountingVariant = "SALE_AND_LEASEBACK".equals(leaseMethod)
                    ? "ZLYW_LEASEBACK_" + assetCategory : "ZLYW_DIRECT_" + assetCategory;
            if (StringUtils.isEmpty(MapUtil.getStr(dataMap, "principal_offset_type"))) {
                dataMap.put("principal_offset_type", "LEASE_ASSET");
            }
        } else if ("HOUSEHOLD_PV".equals(sourceSystem)) {
            accountingVariant = leaseCategory != null && (leaseCategory.contains("经营")
                    || "OPERATING_LEASE".equalsIgnoreCase(leaseCategory))
                    || "JY003".equals(eventVariant)
                    ? "JYZL_HOUSEHOLD_PV" : "ZLYW_HOUSEHOLD_PV";
            if (StringUtils.isEmpty(MapUtil.getStr(dataMap, "principal_offset_type"))) {
                if ("HY072".equals(eventVariant)) {
                    dataMap.put("principal_offset_type", "PROJECT_COMPANY_PREPAID");
                } else if ("HY063".equals(eventVariant)) {
                    dataMap.put("principal_offset_type", "DEALER_PREPAID");
                } else {
                    dataMap.put("principal_offset_type", "LEASE_ASSET");
                }
            }
        } else if ("OPERATING_LEASE".equals(sourceSystem)) {
            // The only OPERATING_LEASE start event currently defined by the
            // accounting template is JY003 (household-PV asset transfer).
            accountingVariant = "JYZL_HOUSEHOLD_PV";
        } else if ("RETAIL_FINANCE_LEASE".equals(sourceSystem)) {
            accountingVariant = "CYC_RETAIL_LEASEBACK";
        } else {
            throw new ServiceException("合同起租事件缺少有效系统来源[systemCode]，可选值：FINANCE_LEASE、HOUSEHOLD_PV、OPERATING_LEASE、RETAIL_FINANCE_LEASE");
        }

        if (StringUtils.isNotEmpty(accountingVariant)) {
            dataMap.put("accounting_variant", accountingVariant);
            dataMap.put(RuleConstant.FIELD_ACCOUNTING_BUSINESS_CODE, accountingVariant);
        }
    }

    private String normalizeLeaseStartSourceSystem(String value) {
        if (StringUtils.isEmpty(value)) return null;
        String normalized = value.trim().toUpperCase(Locale.ROOT);
        if ("融资租赁业务系统".equals(value)) return "FINANCE_LEASE";
        if ("户用光伏业务系统".equals(value)) return "HOUSEHOLD_PV";
        if ("经营租赁业务系统".equals(value)) return "OPERATING_LEASE";
        if ("零售融资租赁业务系统".equals(value)) return "RETAIL_FINANCE_LEASE";
        return normalized;
    }

    private String normalizeLeaseStartLeaseMethod(String value) {
        if (StringUtils.isEmpty(value)) return null;
        String normalized = value.trim().toUpperCase(Locale.ROOT);
        if ("直租".equals(value) || "DIRECT".equals(normalized) || "DIRECT_LEASE".equals(normalized)) {
            return "DIRECT_LEASE";
        }
        if ("回租".equals(value) || "LEASEBACK".equals(normalized)
                || "SALE_AND_LEASEBACK".equals(normalized)) {
            return "SALE_AND_LEASEBACK";
        }
        return null;
    }

    private String normalizeLeaseStartLeaseCategory(String value) {
        if (StringUtils.isEmpty(value)) return null;
        String normalized = value.trim().toUpperCase(Locale.ROOT);
        if ("融资性租赁".equals(value) || "FINANCE_LEASE".equals(normalized)) return "FINANCE_LEASE";
        if ("经营性租赁".equals(value) || "OPERATING_LEASE".equals(normalized)) return "OPERATING_LEASE";
        return normalized;
    }

    private String normalizeLeaseStartAssetCategory(String value) {
        if (StringUtils.isEmpty(value)) return null;
        String normalized = value.trim().toUpperCase(Locale.ROOT);
        if ("动产".equals(value) || "MOVABLE".equals(normalized)) return "MOVABLE";
        if ("不动产".equals(value) || "REAL_ESTATE".equals(normalized)) return "REAL_ESTATE";
        return null;
    }

    private void copyLeaseStartAlias(Map<String, Object> dataMap, String target, String alias) {
        if (ObjectUtil.isNull(dataMap.get(target)) && ObjectUtil.isNotNull(dataMap.get(alias))) {
            dataMap.put(target, dataMap.get(alias));
        }
    }

    private String normalizeLeaseStartEvent(String rawEvent) {
        if (StringUtils.isEmpty(rawEvent)) {
            return null;
        }
        if (rawEvent.contains("HY008") || rawEvent.contains("H008")) return "HY008";
        if (rawEvent.contains("HY072") || rawEvent.contains("H072")) return "HY072";
        if (rawEvent.contains("HY063") || rawEvent.contains("H063")) return "HY063";
        if (rawEvent.contains("JY003") || rawEvent.contains("J003")) return "JY003";
        if (rawEvent.contains("ZZ005") || rawEvent.contains("Z005")) return "ZZ005";
        if (rawEvent.contains("ZZ008") || rawEvent.contains("Z008") || rawEvent.contains("Z009")) return "ZZ008";
        if (rawEvent.contains("HZ002") || rawEvent.contains("H002")) return "HZ002";
        if (rawEvent.contains("HZ006") || rawEvent.contains("H005") || rawEvent.contains("H006")) return "HZ006";
        return rawEvent;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (StringUtils.isNotEmpty(value)) {
                return value;
            }
        }
        return null;
    }

    private Map<String, Object> leaseStartCalculatedRuleFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("起租计算结果表.核算业务类型", RuleConstant.FIELD_ACCOUNTING_BUSINESS_CODE);
        fields.put("起租计算结果表.本金结转方式", "principal_offset_type");
        fields.put("起租计算结果表.起租不含税本金", "lease_principal_net");
        fields.put("起租计算结果表.起租不含税利息", "lease_interest_net");
        fields.put("起租计算结果表.起租不含税留购价", "residual_value_net");
        fields.put("起租计算结果表.起租利息税额", "lease_interest_vat");
        fields.put("起租计算结果表.起租留购价税额", "residual_value_vat");
        fields.put("起租计算结果表.已收手续费未摊销不含税金额", "received_fee_unamortized_net");
        fields.put("起租计算结果表.未收取手续费不含税金额", "unreceived_fee_net");
        fields.put("起租计算结果表.未收手续费未摊销不含税金额", "unreceived_fee_unamortized_net");
        fields.put("起租计算结果表.未收取手续费税额", "unreceived_fee_vat");
        fields.put("起租计算结果表.未收取手续费未摊销税额", "unreceived_fee_unamortized_vat");
        fields.put("起租计算结果表.未收取手续费含税金额", "unreceived_fee_gross");
        fields.put("起租计算结果表.未摊销手续费不含税合计", "fee_unamortized_net_total");
        fields.put("起租计算结果表.应收手续费不含税金额", "service_fee_net");
        fields.put("起租计算结果表.应收手续费税额", "service_fee_vat");
        fields.put("起租计算结果表.经营租赁资产成本不含税金额", "operating_asset_cost_net");
        fields.put("起租计算结果表.客户融资不含税总额", "customer_finance_net");
        return fields;
    }

    public void fillValues(Map<String, Object> dataMap) {
        //ZLSK KJFP ZLFK场景需要预设值借款合同编号值
        String contractCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE);
        String sceneCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE);
        String billContractCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_BILL_CONTRACT_CODE);
        String clientCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CLIENT_CODE);
        if (StringUtils.isEmpty(billContractCode) && (SceneEnum.ZLSK.getCode().equals(sceneCode)
                || SceneEnum.KJFP.getCode().equals(sceneCode)
                || SceneEnum.ZLFK.getCode().equals(sceneCode))) {
            dataMap.put(RuleConstant.FIELD_BILL_CONTRACT_CODE, Constants.BILL_CONTRACT_CODE_DEFAULT);
            //ZLFK场景，若jsonb中clientCode为空或为空字符串，按照推送的clientName查客户表的clientCode，若查询不到，默认赋值999
            if ((SceneEnum.ZLFK.getCode().equals(sceneCode) || SceneEnum.BXTB.getCode().equals(sceneCode))
                    && StringUtils.isEmpty(clientCode)) {
                getZlfkClientCode(dataMap);
            }
            if (StringUtils.isEmpty(contractCode)) {
                return;
            }
            OutTableContractDetailQueryDTO outTableContractDetailQueryDTO = new OutTableContractDetailQueryDTO();
            outTableContractDetailQueryDTO.setContractCode(contractCode);
            outTableContractDetailQueryDTO.setProcessStatusList(Lists.newArrayList(ProcessStatusEnum.SUBMITTED.getCode(), ProcessStatusEnum.REVIEWED.getCode(), ProcessStatusEnum.TO_KINGDEE.getCode()));
            List<OutTableContractDetailVO> contractDetailVOList = outTableAbsService.selectByCondition(outTableContractDetailQueryDTO);
            if (CollectionUtil.isEmpty(contractDetailVOList)) {
                return;
            }
            //按照合同编号分组，id倒叙排序取最大的那条记录
            Map<String, String> billContractMap = contractDetailVOList.stream()
                    .collect(Collectors.groupingBy(
                            OutTableContractDetailVO::getContractCode,
                            Collectors.collectingAndThen(
                                    Collectors.maxBy(Comparator.comparingLong(OutTableContractDetailVO::getId)),
                                    optional -> optional.map(OutTableContractDetailVO::getLoanContractCode).orElse(Constants.BILL_CONTRACT_CODE_DEFAULT)
                            )
                    ));
            dataMap.put(RuleConstant.FIELD_BILL_CONTRACT_CODE, billContractMap.get(contractCode));
            ;
        }
    }

    public void deleteInterfaceData(String interFaceId) {
        if (StringUtils.isNotEmpty(interFaceId)) {
            interfaceDataService.remove(Wrappers.<InterfaceDataEntity>lambdaQuery()
                    .eq(InterfaceDataEntity::getInterfaceId, Long.parseLong(interFaceId)));
        }
    }

    public void setBankOrgIdClientCode(Map<String, Object> dataMap) {
        String bankOrgId = MapUtil.getStr(dataMap, RuleConstant.BANK_ORG_ID);
        String orgId = MapUtil.getStr(dataMap, RuleConstant.FIELD_ORG_ID);
        String deductionMarginOrgId = MapUtil.getStr(dataMap, RuleConstant.DEDUCTION_MARGIN_ORG_ID);
        if (StringUtils.isNotEmpty(deductionMarginOrgId)) {
            dataMap.put(RuleConstant.BANK_CLIENT_CODE, getClientCode(deductionMarginOrgId));
        } else if (StringUtils.isNotEmpty(bankOrgId)) {
            dataMap.put(RuleConstant.BANK_CLIENT_CODE, getClientCode(bankOrgId));
        }
        if (StringUtils.isNotEmpty(orgId)) {
            dataMap.put(RuleConstant.ORG_CLIENT_CODE, getClientCode(orgId));
        }

    }

    public String getClientCode(String orgId) {
        List<OrgCompanyEntity> entityList = iOrgCompanyService.lambdaQuery().in(OrgCompanyEntity::getOrgId, orgId).list();
        String clientCode = "";
        String clientName;
        if (CollectionUtil.isEmpty(entityList)) {
            return clientCode;
        }
        clientName = entityList.get(0).getOrgName();
        List<ClientEntity> clientEntityList = clientService.lambdaQuery().eq(ClientEntity::getClientName, clientName).list();
        if (CollectionUtil.isEmpty(clientEntityList)) {
            return clientCode;
        }
        return clientEntityList.get(0).getClientCode();
    }

    public void copyHtcxVoucher(List<VoucherDTO> voucherDTOList, Map<String, Object> dataMap) {
        //按合同+签约主体查凭证表场景为HTQZ的数据，全部复制后debit_amount，credit_amount分别*-1，
        String orgId = MapUtil.getStr(dataMap, RuleConstant.FIELD_ORG_ID);
        String contractCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE);
        LocalDateTime businessDate = MapUtil.get(dataMap, RuleConstant.FIELD_BUSINESS_DATE, LocalDateTime.class);
        String systemCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SYSTEM_CODE);
        String sceneCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE);
        String settlementWay = MapUtil.getStr(dataMap, RuleConstant.FIELD_SETTLEMENT_WAY);
        String ebankSerialNumber = MapUtil.getStr(dataMap, RuleConstant.FIELD_EBANK_SECIAL_NUMBER);
        LocalDateTime interFaceDateTime = MapUtil.get(dataMap, RuleConstant.INTERFACE_CREATE_TIME, LocalDateTime.class);
        if (StringUtils.isEmpty(contractCode)) {
            log.info("合同撤销合同编码为空");
            return;
        }
        //凭证日期
        InterfaceDataDTO interfaceDataDTO = new InterfaceDataDTO();
        interfaceDataDTO.setSystemCode(systemCode);
        interfaceDataDTO.setOrgId(orgId);
        interfaceDataDTO.setInterfaceCreateTime(interFaceDateTime);
        interfaceDataDTO.setSceneCode(sceneCode);
        interfaceDataDTO.setSettlementWay(settlementWay);
        interfaceDataDTO.setEbankSerialNumber(ebankSerialNumber);
        LocalDate voucherDate = periodCodeService.generateVoucherDate(interfaceDataDTO);
        interfaceDataDTO.setBusinessDate(voucherDate.atStartOfDay());
        // 如果记账日期小于业务日期，则修改业务日期=记账日期
        if (voucherDate.compareTo(businessDate.toLocalDate()) < 0) {
            businessDate = voucherDate.atStartOfDay();
        }

        VoucherQueryDTO queryDTO = new VoucherQueryDTO();
        queryDTO.setOrgId(orgId);
        queryDTO.setContractCode(contractCode);
        queryDTO.setSceneCode(SceneEnum.HTQZ.getCode());
        List<VoucherEntity> voucherEntityList = voucherService.selectVoucherEntity(queryDTO);
        if (CollectionUtils.isEmpty(voucherEntityList)) {
            return;
        }
        List<VoucherEntryEntity> voucherEntryEntityList = iVoucherEntryService.lambdaQuery().in(VoucherEntryEntity::getVoucherId, voucherEntityList.stream().map(VoucherEntity::getId).collect(Collectors.toList())).list();
        Map<Long, List<VoucherEntryEntity>> voucherEntryMap = voucherEntryEntityList.stream().collect(Collectors.groupingBy(VoucherEntryEntity::getVoucherId));
        for (VoucherEntity entity : voucherEntityList) {
            List<VoucherEntryEntity> newVoucherEntryList = voucherEntryMap.get(entity.getId());
            entity.setId(IdWorker.getId());
            entity.setVoucherDate(voucherDate.atStartOfDay());
            entity.setPeriodCode(NumberUtil.parseInt(LocalDateTimeUtil.format(voucherDate, "yyyyMM")));
            entity.setVoucherNum(generateVoucherNum(entity.getVoucherType(), voucherDate.atStartOfDay()));
            entity.setSceneCode(SceneEnum.HTCX.getCode());
            entity.setSceneName(SceneEnum.HTCX.getDesc());
            entity.setVoucherSummary(entity.getVoucherSummary());
            entity.setBusinessDate(voucherDate.atStartOfDay());
            entity.setValidFlag(VoucherValidFlagEnum.VALID.getCode());
            entity.setVoucherStatus(ProcessStatusEnum.REVIEWED.getCode());
            entity.setCreateBy("system");
            entity.setCreateTime(LocalDateTime.now());
            entity.setEasbzcode(null);
            entity.setEasVoucherId(null);
            entity.setEasVoucherNumber(null);
            log.info("合同撤销Copy的头数据：{}", JSON.toJSON(entity));
            voucherService.save(entity);
            VoucherDTO newVoucherDto = BeanUtil.copyProperties(entity, VoucherDTO.class);
            newVoucherDto.setIsFundSystemBalance(YesOrNoEnum.NO.getCode());
            newVoucherDto.setIsSubmit(YesOrNoEnum.YES.getCode());
            for (VoucherEntryEntity entry : newVoucherEntryList) {
                entry.setId(IdWorker.getId());
                entry.setVoucherId(entity.getId());
                entry.setPeriodCode(entity.getPeriodCode());
                entry.setVoucherSummary(entity.getVoucherSummary());
                if (null != entry.getDebitAmount()) {
                    entry.setDebitAmount(entry.getDebitAmount().multiply(new BigDecimal(-1)));
                }
                if (null != entry.getCreditAmount()) {
                    entry.setCreditAmount(entry.getCreditAmount().multiply(new BigDecimal(-1)));
                }
                entry.setIsSendKingdee("0");
                entry.setEasVoucherNumber(null);
                entry.setCreateBy("system");
                entry.setCreateTime(LocalDateTime.now());
            }
            log.info("合同撤销Copy的分录数据：{}", JSON.toJSON(newVoucherEntryList));
            iVoucherEntryService.saveBatch(newVoucherEntryList);
            newVoucherDto.setEntryList(BeanUtil.copyToList(newVoucherEntryList, VoucherEntryDTO.class));
            voucherDTOList.add(newVoucherDto);
        }
    }

    private long generateVoucherNum(String voucherType, LocalDateTime dateTime) {
        try {
            // ========== 使用新的本地号段预分配方案 ==========
            return localSegmentVoucherGenerator.generateVoucherNum(voucherType, dateTime);
        } catch (Exception e) {
            log.error("==>>RuleServiceImpl.generateVoucherNum==>>error:{}", e.getMessage());
            //redis key : finhub-年月-凭证类型编码
            String key = CacheConstants.COMMON_PREFIX + "_voucher_num_" + dateTime.getYear() + dateTime.getMonthValue() + "_" + voucherType;
            return redisService.generate(key, REDIS_VOUCHER_NUM_EXPIRE);
        }
    }

    public Boolean isGenerateVoucher(Map<String, Object> dataMap) {
        /**
         * 业务系统推送ZLSK场景时，若接口推送的ebankSerialNumber不存在于eg_fund_business_system_ebank_mapping中的ebank_serial_number字段中，
         * 则该条接口数据暂时不出凭证，待eg_fund_business_system_ebank_mapping中新增了数据时，
         * 触发interfacedata里ebankSerialNumber存在于eg_fund_business_system_ebank_mapping的ebank_serial_number字段中，
         * 则跑出该条接口数据的凭证；
         * 新增规则：20240613。加限制条件：系统为CYCXT/SYCXT，且jsonb里ebankBatchNo字段不为空，
         * 且jsonb里ebankBatchNo字段值不等于ebankSerialNumber值，
         * 且businessDate在每个月末最后五个工作日期间时，才走这个规则
         */
        //获取场景编码
        String sceneCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SCENE_CODE);
        String systemCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_SYSTEM_CODE);
        String ebankSerialNumber = MapUtil.getStr(dataMap, RuleConstant.FIELD_EBANK_SECIAL_NUMBER);
        String ebankBatchNo = MapUtil.getStr(dataMap, RuleConstant.FIELD_EBANK_BATCH_NO);
        LocalDateTime businessDate = MapUtil.get(dataMap, RuleConstant.FIELD_BUSINESS_DATE, LocalDateTime.class);
        Boolean flag = Boolean.TRUE;

        String payableNumber = MapUtil.getStr(dataMap, RuleConstant.FIELD_PAYABLE_NUMBER);
        String paymentOrder = MapUtil.getStr(dataMap, RuleConstant.FIELD_PAYMENT_ORDER);
        String contractCode = MapUtil.getStr(dataMap, RuleConstant.FIELD_CONTRACT_CODE);
        if ((SystemEnum.CYCXT.getCode().equals(systemCode)
                || SystemEnum.SYCXT.getCode().equals(systemCode))
                && StringUtils.isNotEmpty(ebankBatchNo)
                && StringUtils.isNotEmpty(ebankSerialNumber)
                && !ebankBatchNo.equals(ebankSerialNumber)
                && ObjectUtil.isNotNull(businessDate)
                && CommonDateUtils.isWithinLastFiveDays(CommonDateUtils.parseLocalDateTimeToDate(businessDate))
                && (SceneEnum.ZLSK.getCode().equals(sceneCode) || SceneEnum.BXTB.getCode().equals(sceneCode))
        ) {
            if (null == bankExistFlag(ebankSerialNumber)) {
                //不存在保存数据到interFaceDate表
                //删除已经存在的数据
                String interFaceId = MapUtil.getStr(dataMap, RuleConstant.FIELD_INTERFACE_ID);
                //先统一删除interFaceData数据
                deleteInterfaceData(interFaceId);
                dataMap.put("status", "1"); //需要重新执行的数据
                interfaceDataService.saveInterfaceDataFromMap(dataMap);
                flag = Boolean.FALSE;
            }
        } else if (
                StringUtils.isNotEmpty(payableNumber)
                        && ObjectUtil.isNotNull(businessDate)
                        && (SceneEnum.ZLFK.getCode().equals(sceneCode) || SceneEnum.BXTB.getCode().equals(sceneCode))
        ) {
            /**
             * 2024-08-23 业务系统推送ZLFK场景时，若接口推送的payable_number不存在于eg_fund_payment_data中的payment_order字段中，
             * 则该条接口数据暂时不出凭证，待eg_fund_payment_data中新增了数据时，
             * 触发interfacedata里payable_number存在于eg_fund_payment_data中的payment_order字段中，
             * 则跑出该条接口数据的凭证；
             */
            FundPaymentDataEntity paymentDataEntity = findPaymentByPayableNumber(payableNumber, contractCode);
            if (null == paymentDataEntity) {
                //不存在保存数据到interFaceDate表
                //删除已经存在的数据
                String interFaceId = MapUtil.getStr(dataMap, RuleConstant.FIELD_INTERFACE_ID);
                //先统一删除interFaceData数据
                deleteInterfaceData(interFaceId);
                dataMap.put("status", "1"); //需要重新执行的数据
                interfaceDataService.saveInterfaceDataFromMap(dataMap);
                flag = Boolean.FALSE;
            } else {
                dataMap.put(RuleConstant.FIELD_CLIENT_CODE, paymentDataEntity.getClientCode());
            }
        }

        return flag;
    }

    private FundPaymentDataEntity findPaymentByPayableNumber(String payableNumber, String contractCode) {
        List<FundPaymentDataEntity> list = fundPaymentDataMapper.selectList(new LambdaQueryWrapper<FundPaymentDataEntity>().
                eq(FundPaymentDataEntity::getPaymentOrder, payableNumber).
                eq(StringUtils.isNotEmpty(contractCode) && !"VL05Z0001".equals(contractCode),
                        FundPaymentDataEntity::getContractCode, contractCode).
                orderByDesc(FundPaymentDataEntity::getId));
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public FundBusinessSystemEbankMappingEntity bankExistFlag(String ebankSerialNumber) {
        String number = "'" + ebankSerialNumber + "'";
        List<FundBusinessSystemEbankMappingEntity> mappingEntityList = fundBusinessSystemEbankMappingMapper.selectList(Wrappers.<FundBusinessSystemEbankMappingEntity>lambdaQuery().apply("STRING_TO_ARRAY(ebank_serial_number, ',') @> ARRAY[" + number + "]"));
        if (CollectionUtil.isEmpty(mappingEntityList)) {
            return null;
        }
        return mappingEntityList.get(0);
    }

    public void getZlfkClientCode(Map<String, Object> dataMap) {
        String clientName = MapUtil.getStr(dataMap, RuleConstant.FIELD_CLIENT_NAME);
        String defaultClientCode = DefaultConstant.DEFAULT_CLIENT_CODE;
        if (StringUtils.isNotEmpty(clientName)) {
            List<ClientEntity> clientEntityList = clientService.lambdaQuery().eq(ClientEntity::getClientName, clientName).list();
            if (CollectionUtil.isNotEmpty(clientEntityList)) {
                defaultClientCode = clientEntityList.get(0).getClientCode();
            }
        }
        dataMap.put(RuleConstant.FIELD_CLIENT_CODE, defaultClientCode);
    }

}
