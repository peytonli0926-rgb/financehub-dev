package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.constant.CacheConstants;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.constants.RegexExpConst;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.KingdeeHybVoucherVO;
import com.utfinancing.financehub.engine.finance.mapper.KingdeeHybVoucherMapper;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.rule.util.RuleUtil;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.utils.PeriodCodeUtil;
import com.utfinancing.financehub.engine.utils.UserUtils;
import io.swagger.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.codehaus.groovy.ast.GenericsType;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-02
 * @Description :  KingdeeHybVoucher服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class KingdeeHybVoucherServiceImpl extends ServiceImpl<KingdeeHybVoucherMapper, KingdeeHybVoucherEntity> implements IKingdeeHybVoucherService {

    private final KingdeeHybVoucherMapper kingdeeHybVoucherMapper;

    @Lazy
    private final IFundEbankTransactionDataService iFundEbankTransactionDataService;

    @Lazy
    private final IRuleService ruleService;

    private final IBusinessClaimRepaymentRecordService businessClaimRepaymentRecordService;

    private final IBankAccountService bankAccountService;

    @Lazy
    private final IVoucherService iVoucherService;

    @Lazy
    private final IVoucherEntryService iVoucherEntryService;

    private final IKingdeeMiddleVoucherService kingdeeMiddleVoucherService;

    private final IContractBalanceService iContractBalanceService;

    private static final String accountCode = "1531.02";

    private final IAccountService iAccountService;

    private final ICurrencyService iCurrencyService;

    private final IOrgCompanyService iOrgCompanyService;

    private final IFundBusinessSystemEbankMappingService fundBusinessSystemEbankMappingService;

    private final IFundBusinessSystemEbankWyAmountService fundBusinessSystemEbankWyAmountService;


    @Override
    public Long saveKingdeeHybVoucher(KingdeeHybVoucherDTO dto) {
        KingdeeHybVoucherEntity entity = BeanUtil.copyProperties(dto, KingdeeHybVoucherEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateKingdeeHybVoucher(Long id, KingdeeHybVoucherDTO dto) {
        KingdeeHybVoucherEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public KingdeeHybVoucherDTO getKingdeeHybVoucherDTOById(Long id) {
        KingdeeHybVoucherEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, KingdeeHybVoucherDTO.class);
    }

    @Override
    public IPage<KingdeeHybVoucherVO> selectPage(KingdeeHybVoucherQueryDTO queryDTO) {
        LambdaQueryWrapper<KingdeeHybVoucherEntity> queryWrapper = Wrappers.<KingdeeHybVoucherEntity>lambdaQuery();
        //这里注入查询条件
        IPage<KingdeeHybVoucherEntity> entityIPage = kingdeeHybVoucherMapper.selectPage(new Page<KingdeeHybVoucherEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, KingdeeHybVoucherVO.class);
    }


    @Async
    @Override
    public void generateKingdeeHybVoucher() {
        //获取未生成凭证的数据
        List<KingdeeHybVoucherEntity> voucherEntityList = this.lambdaQuery().eq(KingdeeHybVoucherEntity::getMessageStatus, RawMessageStatusEnum.NOT_EXECUTE.getCode()).orderByAsc(KingdeeHybVoucherEntity::getId).list();
        if (CollectionUtils.isEmpty(voucherEntityList)) {
            return;
        }
        Map<String, String> currencyMap = getCurrencyMap();
        Map<String, String> orgIdMap = getOrgNameOrgId();
        //按照easbzCode分组
        voucherEntityList.forEach(v -> {
            if (StringUtils.isNotEmpty(v.getCurrencyName()) && currencyMap.containsKey(v.getCurrencyName())) {
                v.setCurrencyName(currencyMap.get(v.getCurrencyName()));
            } else {
                v.setCurrencyName(CurrencyTypeEnum.CNY.getCode());
            }
            if (StringUtils.isEmpty(v.getOrgName()) && StringUtils.isNotEmpty(v.getOrgId())) {
                v.setOrgName(orgIdMap.get(v.getOrgId()));
            }
        });
        //按照fid分组
        //Map<String, List<KingdeeHybVoucherEntity>> voucherMap = voucherEntityList.stream().collect(Collectors.groupingBy(KingdeeHybVoucherEntity::getFid));
        // add by zhangli.chen 新增interfaceid传值，支持凭证多次生成时删除interface表数据 on 20251127
        Map<String, List<KingdeeHybVoucherEntity>> voucherMap = voucherEntityList.stream()
                .collect(Collectors.groupingBy(KingdeeHybVoucherEntity::getFid,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparingLong(KingdeeHybVoucherEntity::getId))
                                        .collect(Collectors.toList()))));
        double totalSize = voucherMap.size();
        int i = 1;
        for (Map.Entry<String, List<KingdeeHybVoucherEntity>> entity : voucherMap.entrySet()) {
            //过滤不包含1531.02的数据
            boolean isExist = entity.getValue().stream().anyMatch(v -> "1531.02".equals(v.getAccountCode()));
            if (!isExist) {
                log.info("fid:{}不存在1531.02的数据", entity.getKey());
                continue;
            }
            KingdeeHybVoucherEntity firstEntity = entity.getValue().get(0);
            String fid = firstEntity.getFid();
            //组转数据
            Optional<KingdeeHybVoucherEntity> contractCodeVoucherEntity = entity.getValue().stream().filter(v -> StringUtils.isNotEmpty(v.getContractCode())).findFirst();
            Optional<KingdeeHybVoucherEntity> clientCodeVoucherEntity = entity.getValue().stream().filter(v -> StringUtils.isNotEmpty(v.getClientCode())).findFirst();
            BigDecimal recyclePrincipalAmount = BigDecimal.ZERO;
            BigDecimal recycleDefaultInterestAmount = BigDecimal.ZERO;
            BigDecimal receiveRetainedPrice = BigDecimal.ZERO;
            BigDecimal receiveTerminateProcedureAmount = BigDecimal.ZERO;
            BigDecimal receiveUnconfirmed = BigDecimal.ZERO;
            BigDecimal outtax = BigDecimal.ZERO;
            String ebankNum = "";
            String ebankNo = "";
            String bankOrgId = StringUtil.EMPTY;
            for (KingdeeHybVoucherEntity entry : entity.getValue()) {
                if (entry.getAccountCode().equals(accountCode)) {
                    String abstractContent = entry.getAbstractContent();
                    ebankNum = getBankNum(abstractContent);
                }
                BigDecimal amount = entry.getDebitAmount().compareTo(BigDecimal.ZERO) != 0
                        ? entry.getDebitAmount() : entry.getCreditAmount();
                if (entry.getAccountCode().equals("1531.01.01") || entry.getAccountCode().equals("2241.09")) {
                    recyclePrincipalAmount = amount;
                } else if (entry.getAccountCode().equals("6051.02")) {
                    recycleDefaultInterestAmount = amount;
                } else if (entry.getAccountCode().equals("1531.01.03")) {
                    receiveRetainedPrice = amount;
                } else if (entry.getAccountCode().equals("6051.03")) {
                    receiveTerminateProcedureAmount = amount;
                } else if (entry.getAccountCode().equals("1531.02")) {
                    receiveUnconfirmed = amount;
                } else if (entry.getAccountCode().equals("2221.01.05")) {
                    outtax = outtax.add(amount);
                }
            }
            ;
            if (recycleDefaultInterestAmount.compareTo(BigDecimal.ZERO) != 0) {
                recycleDefaultInterestAmount = recycleDefaultInterestAmount.add(outtax);
            } else if (receiveTerminateProcedureAmount.compareTo(BigDecimal.ZERO) != 0) {
                receiveTerminateProcedureAmount = receiveTerminateProcedureAmount.add(outtax);
            } else if (receiveRetainedPrice.compareTo(BigDecimal.ZERO) != 0) {
                receiveRetainedPrice = receiveRetainedPrice.add(outtax);
            } else {
                recyclePrincipalAmount = recyclePrincipalAmount.add(outtax);
            }
            log.info("eanknum:{}", ebankNum);
            if (StringUtils.isNotEmpty(ebankNum)) {
                Optional<FundEbankTransactionDataEntity> dataEntity = iFundEbankTransactionDataService.lambdaQuery().eq(FundEbankTransactionDataEntity::getEbankNumber, ebankNum).list().stream().findFirst();
                if (dataEntity.isPresent()) {
                    String collectionAccountsBankNo = dataEntity.get().getCollectionAccountsBankNo();
                    ebankNo = collectionAccountsBankNo;
                    if (StringUtils.isNotEmpty(collectionAccountsBankNo)) {
                        List<BankAccountEntity> bankAccountEntityList = bankAccountService.
                                selectByBankAccountCode(collectionAccountsBankNo);
                        if (CollectionUtils.isNotEmpty(bankAccountEntityList)) {
                            bankOrgId = bankAccountEntityList.get(0).getOrgId();
                        }
                    }
                }
            }

            // 考虑批扣网银的情况
            if (StringUtils.isEmpty(bankOrgId)) {
                FundBusinessSystemEbankMappingEntity fundBusinessSystemEbankMappingEntity =
                        fundBusinessSystemEbankMappingService.selectFundBusinessSystemEbankMappingBySerialNumber(ebankNum);
                if (fundBusinessSystemEbankMappingEntity != null) {
                    FundBusinessSystemEbankWyAmountEntity fundBusinessSystemEbankWyAmountEntity = fundBusinessSystemEbankWyAmountService.
                            selectFundEbankTransactionDataByCon(fundBusinessSystemEbankMappingEntity.getMatchNumber());
                    if (fundBusinessSystemEbankWyAmountEntity != null) {
                        List<BankAccountEntity> bankAccountEntityList = bankAccountService.
                                selectBankAccountEntity(fundBusinessSystemEbankWyAmountEntity.getCollectionAccountsBankNo());
                        if (bankAccountEntityList != null && !bankAccountEntityList.isEmpty()) {
                            bankOrgId = bankAccountEntityList.get(0).getOrgId();
                        }
                    }
                }
            }

            ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
            commonDTO.setSystemCode(SystemEnum.HYB.getCode());
            commonDTO.setSystemName(SystemEnum.HYB.getDesc());
            commonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
            commonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
            commonDTO.setCurrencyType(entity.getValue().get(0).getCurrencyName());
            commonDTO.setOrderId(entity.getKey()); //fid
            commonDTO.setOrgId(firstEntity.getOrgId());
            commonDTO.setOrgName(firstEntity.getOrgName());
            contractCodeVoucherEntity.ifPresent(kingdeeHybVoucherEntity -> commonDTO.setContractCode(kingdeeHybVoucherEntity.getContractCode()));
            contractCodeVoucherEntity.ifPresent(kingdeeHybVoucherEntity -> commonDTO.setContractName(kingdeeHybVoucherEntity.getContractName()));
            contractCodeVoucherEntity.ifPresent(kingdeeHybVoucherEntity -> commonDTO.setCurrencyType(kingdeeHybVoucherEntity.getCurrencyName()));
            clientCodeVoucherEntity.ifPresent(kingdeeHybVoucherEntity -> commonDTO.setClientCode(kingdeeHybVoucherEntity.getClientCode()));
            clientCodeVoucherEntity.ifPresent(kingdeeHybVoucherEntity -> commonDTO.setClientName(kingdeeHybVoucherEntity.getClientName()));
            commonDTO.setSceneCode(SceneEnum.ZLSK.getCode());
            commonDTO.setSceneName(SceneEnum.ZLSK.getDesc());
            contractCodeVoucherEntity.ifPresent(kingdeeHybVoucherEntity -> commonDTO.setBusinessDate(CommonDateUtils.parseLocalDateTimeToDate(kingdeeHybVoucherEntity.getVoucherDate())));
            commonDTO.setBatchId(firstEntity.getId());
            commonDTO.setBatchType(BatchTypeEnum.HYB.getCode());
            // add by zhangli.chen 新增interfaceid传值，支持凭证多次生成时删除interface表数据 on 20251127
            commonDTO.setInterfaceId(firstEntity.getId());
            Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);
            // 是否批量
            commonMap.put("isBulkOperation", YesOrNoEnum.NO.getDesc());
            commonMap.put("easVoucherId", entity.getKey());
            // 银行到账主体
            commonMap.put("ebankNum", ebankNo);
            commonMap.put("ebankSerialNumber", ebankNum);
            // 借款合同编号
            commonMap.put("billContractCode", "");
            commonMap.put("createDate", DateUtils.dateTimeNow());
            commonMap.put("updateDate", DateUtils.dateTimeNow());
            commonMap.put("recyclePrincipalAmount", recyclePrincipalAmount);
            commonMap.put("recycleDefaultInterestAmount", recycleDefaultInterestAmount);
            commonMap.put("receiveRetainedPrice", receiveRetainedPrice);
            commonMap.put("receiveTerminateProcedureAmount", receiveTerminateProcedureAmount);
            commonMap.put("receiveUnconfirmed", receiveUnconfirmed);
            commonMap.put("bankOrgId", bankOrgId);
            commonMap.put(RuleConstant.IS_SUBMIT, YesOrNoEnum.YES.getCode());
            log.info("金蝶恒运宝生成凭证参数：{}", JSON.toJSON(commonMap));
            String errorInfo = "";
            String messageStatus = RawMessageStatusEnum.NOT_EXECUTE.getCode();
            try {
                List<VoucherDTO> voucherDTOList = ruleService.executeRule(commonMap);
                log.info("金蝶恒运宝生成凭证返回值:{}", JSON.toJSON(voucherDTOList));
                if (CollectionUtils.isEmpty(voucherDTOList) || CollectionUtils.isEmpty(voucherDTOList.get(0).getEntryList())) {
                    errorInfo = "凭证行为空";
                    messageStatus = RawMessageStatusEnum.FAILED.getCode();
                } else {
                    boolean hasFailed = false;
                    boolean hasOrgFailed = false;
                    for (VoucherDTO voucherDTO : voucherDTOList) {
                        if (StrUtil.equals(VoucherValidFlagEnum.NOT_EQUALS.getCode(), voucherDTO.getValidFlag())) {
                            hasFailed = true;
                            break;
                        } else if (StrUtil.equals(VoucherValidFlagEnum.NO_VALID.getCode(), voucherDTO.getValidFlag())) {
                            hasOrgFailed = true;
                            break;
                        }
                    }
                    if (hasOrgFailed) {
                        errorInfo = "签约主体为空";
                        messageStatus = "FAILED";
                    } else if (hasFailed) {
                        errorInfo = "借贷金额不平";
                        messageStatus = RawMessageStatusEnum.FAILED.getCode();
                    } else {
                        messageStatus = RawMessageStatusEnum.SUCCESS.getCode();
                    }
                }
            } catch (Exception e) {
                errorInfo = e.getMessage();
                messageStatus = RawMessageStatusEnum.FAILED.getCode();
            }
            this.lambdaUpdate().set(KingdeeHybVoucherEntity::getMessageStatus, messageStatus).set(KingdeeHybVoucherEntity::getMessageError, errorInfo).eq(KingdeeHybVoucherEntity::getFid, entity.getKey()).update();
            //生成凭证之后生成认领记录
            if (messageStatus.equals(RawMessageStatusEnum.SUCCESS.getCode())) {
                /******modify by zhangli.chen for 去除抽取恒运宝数据生成凭证时同步插入认领表的逻辑，转而通过另外一条路径job抽数路径：
                 * 抽取业务系统未确认收款金额(nonConfirmAmountSync)来生成认领数据 ******/
                //saveRecord(commonMap);
                //将数据保存一份到凭证表，金额*-1，validflag=4 无效
                saveVoucher(entity.getValue());
            }
            log.info("处理恒运宝生成凭证fid：{},完成，当前进度：{}", fid, NumberUtil.formatPercent(i / totalSize, 2));
            i++;
        }
    }

    public void saveRecord(Map<String, Object> commonMap) {
        log.info("保存认领记录信息：{}", JSON.toJSON(commonMap));
        // 生成认领记录
        String clientCode = MapUtil.getStr(commonMap, RuleConstant.FIELD_CLIENT_CODE);
        String contractCode = MapUtil.getStr(commonMap, RuleConstant.FIELD_CONTRACT_CODE);
        String currencyType = MapUtil.getStr(commonMap, "currencyType");
        String ebankSerialNumber = MapUtil.getStr(commonMap, "ebankSerialNumber");
        String ebankNum = MapUtil.getStr(commonMap, "ebankNum");
        String orgId = MapUtil.getStr(commonMap, RuleConstant.FIELD_ORG_ID);
        String orgIdName = MapUtil.getStr(commonMap, "orgName");
        String systemCode = MapUtil.getStr(commonMap, RuleConstant.FIELD_SYSTEM_CODE);
        LocalDateTime businessDate = MapUtil.get(commonMap, RuleConstant.FIELD_BUSINESS_DATE, LocalDateTime.class);
        BigDecimal amount = MapUtil.get(commonMap, "receiveUnconfirmed", BigDecimal.class);
        BusinessClaimRepaymentRecordEntity entity = new BusinessClaimRepaymentRecordEntity();
        entity.setId(IdWorker.getId());
        entity.setSystemCode(systemCode);
        entity.setClaimAmount(amount);
        entity.setClientCode(clientCode);
        entity.setContractCode(contractCode);
        entity.setCurrencyType(currencyType);
        entity.setBusinessDate(businessDate);
        entity.setEbankSerialNumber(ebankSerialNumber);
        entity.setOrgId(orgId);
        entity.setOrgName(orgIdName);
        if (StringUtils.isNotEmpty(ebankSerialNumber)) {
            int maxBatchNo = businessClaimRepaymentRecordService.getMaxBatchNo(ebankSerialNumber);
            maxBatchNo = maxBatchNo + 1;
            entity.setBatchNo(new BigDecimal(maxBatchNo));
        } else {
            entity.setBatchNo(new BigDecimal(RandomUtil.randomInt()));
        }
//        entity.setNonConfirmSecondDetailId("");
        entity.setSceneCode(SceneEnum.ZLSK.getCode());
        entity.setSceneName(SceneEnum.ZLSK.getDesc());
        entity.setOperationType(ClaimOperationTypeEnum.BUSINESS_AUTO_CLAIM.getCode());
        entity.setProcessStatus(ProcessStatusEnum.REVIEWED.getCode());
//        if (!StringUtils.equals(ebankNum, entity.getOrgName())) {
//            entity.setIsCrossOrg(YesOrNoEnum.YES.getCode());
//        } else {
        entity.setIsCrossOrg(YesOrNoEnum.NO.getCode());
//        }
        businessClaimRepaymentRecordService.save(entity);
    }

    public void saveVoucher(List<KingdeeHybVoucherEntity> hybVoucherEntityList) {
        KingdeeHybVoucherEntity entity = hybVoucherEntityList.get(0);
        //组装凭证头
        VoucherEntity voucherEntity = new VoucherEntity();
        voucherEntity.setVoucherDate(entity.getVoucherDate());
        voucherEntity.setVoucherSummary(entity.getAbstractContent());
        voucherEntity.setCreateUserName(UserUtils.getStaffCode());
        voucherEntity.setVoucherStatus(ProcessStatusEnum.REVIEWED.getCode());
        voucherEntity.setCurrency(entity.getCurrencyName());
        voucherEntity.setBusinessDate(entity.getVoucherDate());
        voucherEntity.setSceneName(SceneEnum.ZLSK.getDesc());
        voucherEntity.setSceneCode(SceneEnum.ZLSK.getCode());
        voucherEntity.setPeriodCode(PeriodCodeUtil.periodCodeByLocalDateTime(voucherEntity.getVoucherDate()));
        voucherEntity.setIsSummary(YesOrNoEnum.NO.getCode());
        voucherEntity.setCreateUserNo(UserUtils.getStaffCode());
        voucherEntity.setEasVoucherId(entity.getFid());
        voucherEntity.setValidFlag(VoucherValidFlagEnum.VALID.getCode());
        voucherEntity.setSystemCode(SystemEnum.HYB.getCode());
        voucherEntity.setSystemName(SystemEnum.HYB.getDesc());
        voucherEntity.setBusinessCode(BusinessEnum.ZLYW.getCode());
        voucherEntity.setBusinessName(BusinessEnum.ZLYW.getDesc());
        voucherEntity.setVoucherType("05");//默认自动转账
        voucherEntity.setVoucherNum(iVoucherService.generateVoucherNum(voucherEntity.getVoucherType(), voucherEntity.getVoucherDate()));
        voucherEntity.setId(IdWorker.getId());
        voucherEntity.setOrgId(entity.getOrgId());
        voucherEntity.setOrgName(entity.getOrgName());
        iVoucherService.save(voucherEntity);
        List<VoucherEntryEntity> entryEntityList = Lists.newArrayList();
        hybVoucherEntityList.forEach(v -> {
            VoucherEntryEntity entity1 = new VoucherEntryEntity();
            entity1.setId(IdWorker.getId());
            String debitCreditType = v.getDebitAmount().compareTo(BigDecimal.ZERO) == 0 ? DRCREnum.CR.getCode() : DRCREnum.DR.getCode();
            entity1.setVoucherId(voucherEntity.getId());
            entity1.setVoucherAmount(DRCREnum.DR.getCode().equals(debitCreditType) ? v.getDebitAmount() : v.getCreditAmount());
            entity1.setVoucherSummary(v.getAbstractContent());
            entity1.setCreditAmount(v.getCreditAmount().multiply(new BigDecimal(-1)));
            entity1.setDebitAmount(v.getDebitAmount().multiply(new BigDecimal(-1)));
            entity1.setPeriodCode(voucherEntity.getPeriodCode());
            entity1.setConvertDebitAmount(v.getDebitAmount());
            entity1.setConvertCreditAmount(v.getCreditAmount());
            entity1.setClientCode(v.getClientCode());
            entity1.setAccountName(v.getAccountName());
            entity1.setAccountCode(v.getAccountCode());
            entity1.setClientName(v.getClientName());
            entity1.setContractCode(v.getContractCode());
            entity1.setContractName(v.getContractName());
            entity1.setDebitCreditType(debitCreditType);
            entryEntityList.add(entity1);
        });
        iVoucherEntryService.saveBatch(entryEntityList);
    }

    @Async
    @Override
    public void generateKingdeeMiddleVoucher() {
        List<KingdeeMiddleVoucherEntity> voucherEntityList = kingdeeMiddleVoucherService.lambdaQuery().
                eq(KingdeeMiddleVoucherEntity::getMessageStatus, RawMessageStatusEnum.NOT_EXECUTE.getCode()).
                orderByAsc(KingdeeMiddleVoucherEntity::getId).list();
        if (CollectionUtils.isEmpty(voucherEntityList)) {
            log.info("金蝶中间表贵安，现代物流生成凭证数据为空，不执行");
            return;
        }
        Map<String, String> currencyMap = getCurrencyMap();
        Map<String, String> orgMap = getOrgNameOrgId();
        //按照easbzCode分组
        voucherEntityList.forEach(v -> {
            if (StringUtils.isNotEmpty(v.getCurrencyNumber()) && currencyMap.containsKey(v.getCurrencyNumber())) {
                v.setCurrencyNumber(currencyMap.get(v.getCurrencyNumber()));
            } else {
                v.setCurrencyNumber(CurrencyTypeEnum.CNY.getCode());
            }
        });
        Map<String, List<KingdeeMiddleVoucherEntity>> voucherMap = voucherEntityList.stream().collect(Collectors.
                groupingBy(KingdeeMiddleVoucherEntity::getEasbzCode));
        double totalSize = voucherMap.size();
        int i = 1;
        for (Map.Entry<String, List<KingdeeMiddleVoucherEntity>> entity : voucherMap.entrySet()) {
            if ("CSH_CONSOLIDATION".equals(entity.getValue().get(0).getSceneName())
                    || "租金回笼".equals(entity.getValue().get(0).getSceneName())
                    || "收款".equals(entity.getValue().get(0).getSceneName())
                    || "红冲".equals(entity.getValue().get(0).getSceneName())
                    || "保证金抵扣租金".equals(entity.getValue().get(0).getSceneName())) {
                this.processForCshConsolidation(entity, orgMap);
            } else if ("FIN_INCOME_RECOGNITION".equals(entity.getValue().get(0).getSceneName())) {
                this.processForFinIncomeRecognition(entity);
            } else if ("费用减免-留购价".equals(entity.getValue().get(0).getSceneName())
                    || "偿还计划修改".equals(entity.getValue().get(0).getSceneName())) {
                this.processForRepaymentPlanModify(entity);
            }
            log.info("处理贵安，现代物流生成凭证easbzCode：{},完成，当前进度：{}", entity.getKey(), NumberUtil.formatPercent(i / totalSize, 2));
            i++;
        }
    }

    private void processForRepaymentPlanModify(Map.Entry<String, List<KingdeeMiddleVoucherEntity>> entity) {
        KingdeeMiddleVoucherEntity firstEntity = entity.getValue().get(0);

        Map<String, BigDecimal> contractAccountAmountMap = new HashMap<>();
        for (KingdeeMiddleVoucherEntity entry : entity.getValue()) {
            BigDecimal amount = entry.getDebitAmount().compareTo(BigDecimal.ZERO) != 0
                    ? entry.getDebitAmount() : entry.getCreditAmount();
            if (entry.getAccountCode().equals("1531.01.01")) {
                BigDecimal receivablePrincipalAdjustAmount = contractAccountAmountMap.get(entry.getContractCode().
                        concat("receivablePrincipalAdjustAmount"));
                if (receivablePrincipalAdjustAmount == null) {
                    receivablePrincipalAdjustAmount = amount;
                } else {
                    receivablePrincipalAdjustAmount = receivablePrincipalAdjustAmount.add(amount);
                }
                contractAccountAmountMap.put(entry.getContractCode().concat("receivablePrincipalAdjustAmount"), receivablePrincipalAdjustAmount);
            }

            if (entry.getAccountCode().equals("1531.01.03")) {
                BigDecimal residualAdjustAmount = contractAccountAmountMap.get(entry.getContractCode().
                        concat("residualAdjustAmount"));
                if (residualAdjustAmount == null) {
                    residualAdjustAmount = amount;
                } else {
                    residualAdjustAmount = residualAdjustAmount.add(amount);
                }
                contractAccountAmountMap.put(entry.getContractCode().concat("residualAdjustAmount"), residualAdjustAmount);
            }
        }


        List<String> contractCodeList = entity.getValue().stream().
                filter(e -> StringUtils.isNotEmpty(e.getContractCode())).
                map(e -> e.getContractCode()).distinct().collect(Collectors.toList());
        for (String contractCode : contractCodeList) {
            BigDecimal receivablePrincipalAdjustAmount = contractAccountAmountMap.get(contractCode.concat("receivablePrincipalAdjustAmount"));
            if (receivablePrincipalAdjustAmount == null) {
                receivablePrincipalAdjustAmount = BigDecimal.ZERO;
            }

            BigDecimal residualAdjustAmount = contractAccountAmountMap.get(contractCode.concat("residualAdjustAmount"));
            if (residualAdjustAmount == null) {
                residualAdjustAmount = BigDecimal.ZERO;
            }

            ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
            commonDTO.setSystemCode(firstEntity.getSystemCode());
            commonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
            commonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
            commonDTO.setOrderId(firstEntity.getBatchId().toString());
            commonDTO.setContractCode(contractCode);
            commonDTO.setCurrencyType(firstEntity.getCurrencyNumber());
            commonDTO.setClientCode(firstEntity.getClientCode());
            commonDTO.setSceneCode(SceneEnum.JYJGBG.getCode());
            commonDTO.setSceneName(SceneEnum.JYJGBG.getDesc());
            commonDTO.setOrgId(firstEntity.getOrgId());
            commonDTO.setBusinessDate(CommonDateUtils.parseLocalDateTimeToDate(firstEntity.getVoucherDate()));
            commonDTO.setBatchId(firstEntity.getBatchId());
            commonDTO.setBatchType(firstEntity.getSystemCode());
            // add by zhangli.chen 新增interfaceid传值，支持凭证多次生成时删除interface表数据 on 20251127
            commonDTO.setInterfaceId(firstEntity.getBatchId());
            Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);
            commonMap.put("receivablePrincipalAdjustAmount", receivablePrincipalAdjustAmount);
            commonMap.put("residualAdjustAmount", residualAdjustAmount);
            commonMap.put(RuleConstant.IS_SUBMIT, YesOrNoEnum.YES.getCode());

            //生成凭证信息
            String messageStatus = RawMessageStatusEnum.NOT_EXECUTE.getCode();
            String errorInfo = "";

            try {
                List<VoucherDTO> voucherDTOList = ruleService.executeRule(commonMap);
                log.info("金蝶现代物流生成凭证返回值:{}", JSON.toJSON(voucherDTOList));
                if (CollectionUtils.isEmpty(voucherDTOList) || CollectionUtils.isEmpty(voucherDTOList.get(0).getEntryList())) {
                    errorInfo = "凭证行为空";
                    messageStatus = RawMessageStatusEnum.FAILED.getCode();
                } else {
                    boolean hasFailed = false;
                    boolean hasOrgFailed = false;
                    for (VoucherDTO voucherDTO : voucherDTOList) {
                        if (StrUtil.equals(VoucherValidFlagEnum.NOT_EQUALS.getCode(), voucherDTO.getValidFlag())) {
                            hasFailed = true;
                            break;
                        } else if (StrUtil.equals(VoucherValidFlagEnum.NO_VALID.getCode(), voucherDTO.getValidFlag())) {
                            hasOrgFailed = true;
                            break;
                        }
                    }
                    if (hasOrgFailed) {
                        errorInfo = "签约主体为空";
                        messageStatus = RawMessageStatusEnum.FAILED.getCode();
                    } else if (hasFailed) {
                        errorInfo = "借贷金额不平";
                        messageStatus = RawMessageStatusEnum.FAILED.getCode();
                    } else {
                        messageStatus = RawMessageStatusEnum.SUCCESS.getCode();
                    }
                }
            } catch (Exception e) {
                errorInfo = e.getMessage();
                messageStatus = RawMessageStatusEnum.FAILED.getCode();
            }
            kingdeeMiddleVoucherService.lambdaUpdate().set(KingdeeMiddleVoucherEntity::getMessageStatus, messageStatus).
                    set(KingdeeMiddleVoucherEntity::getMessageError, errorInfo).
                    set(KingdeeMiddleVoucherEntity::getUpdateTime, LocalDateTime.now()).
                    eq(KingdeeMiddleVoucherEntity::getEasbzCode, entity.getKey()).
                    eq(KingdeeMiddleVoucherEntity::getContractCode, contractCode).update();
        }
    }

    /**
     * FIN_INCOME_RECOGNITION
     */
    private void processForFinIncomeRecognition(Map.Entry<String, List<KingdeeMiddleVoucherEntity>> entity) {
        KingdeeMiddleVoucherEntity firstEntity = entity.getValue().get(0);

        Map<String, BigDecimal> contractAccountAmountMap = new HashMap<>();
        for (KingdeeMiddleVoucherEntity entry : entity.getValue()) {
            BigDecimal amount = entry.getDebitAmount().compareTo(BigDecimal.ZERO) != 0
                    ? entry.getDebitAmount() : entry.getCreditAmount();
            if (entry.getAccountCode().equals("1532.01")) {
                BigDecimal incomeAccural = contractAccountAmountMap.get(entry.getContractCode().
                        concat("incomeAccural"));
                if (incomeAccural == null) {
                    incomeAccural = amount;
                } else {
                    incomeAccural = incomeAccural.add(amount);
                }
                contractAccountAmountMap.put(entry.getContractCode().concat("incomeAccural"), incomeAccural);
            }
        }

        List<String> contractCodeList = entity.getValue().stream().
                filter(e -> StringUtils.isNotEmpty(e.getContractCode())).
                map(e -> e.getContractCode()).distinct().collect(Collectors.toList());
        for (String contractCode : contractCodeList) {
            BigDecimal incomeAccural = contractAccountAmountMap.get(contractCode.concat("incomeAccural"));
            if (incomeAccural == null) {
                incomeAccural = BigDecimal.ZERO;
            }

            ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
            commonDTO.setSystemCode(firstEntity.getSystemCode());
            commonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
            commonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
            commonDTO.setOrderId(firstEntity.getBatchId().toString());
            commonDTO.setContractCode(contractCode);
            commonDTO.setCurrencyType(firstEntity.getCurrencyNumber());
            commonDTO.setClientCode(firstEntity.getClientCode());
            commonDTO.setSceneCode(SceneEnum.SYJT.getCode());
            commonDTO.setSceneName(SceneEnum.SYJT.getDesc());
            commonDTO.setOrgId(firstEntity.getOrgId());
            commonDTO.setBusinessDate(CommonDateUtils.parseLocalDateTimeToDate(firstEntity.getVoucherDate()));
            commonDTO.setBatchId(firstEntity.getBatchId());
            commonDTO.setBatchType(firstEntity.getSystemCode());
            // add by zhangli.chen 新增interfaceid传值，支持凭证多次生成时删除interface表数据 on 20251127
            commonDTO.setInterfaceId(firstEntity.getBatchId());
            Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);
            // 是否计提
            commonMap.put("isAccrual", YesOrNoEnum.YES.getDesc());
            // 收益计提金额
            commonMap.put("incomeAccural", incomeAccural);
            // 收益计提调整额 凭证中未使用,接口表中有该字段
            commonMap.put("incomeAdjust", BigDecimal.ZERO);
            // 表外计提金额
            commonMap.put("incomeOther", BigDecimal.ZERO);
            commonMap.put(RuleConstant.IS_SUBMIT, YesOrNoEnum.YES.getCode());
            commonMap.put("intableTransferOuttableAmount", BigDecimal.ZERO);
            commonMap.put("outtableTransferIntableAmount", BigDecimal.ZERO);
            commonMap.put("accuralMonth", DateUtils.parseDateToStr(DateUtils.YYYY_MM,
                    CommonDateUtils.parseLocalDateTimeToDate(firstEntity.getVoucherDate())));

            //生成凭证信息
            String messageStatus = RawMessageStatusEnum.NOT_EXECUTE.getCode();
            String errorInfo = "";

            try {
                List<VoucherDTO> voucherDTOList = ruleService.executeRule(commonMap);
                log.info("金蝶现代物流生成凭证返回值:{}", JSON.toJSON(voucherDTOList));
                if (CollectionUtils.isEmpty(voucherDTOList) || CollectionUtils.isEmpty(voucherDTOList.get(0).getEntryList())) {
                    errorInfo = "凭证行为空";
                    messageStatus = RawMessageStatusEnum.FAILED.getCode();
                } else {
                    boolean hasFailed = false;
                    boolean hasOrgFailed = false;
                    for (VoucherDTO voucherDTO : voucherDTOList) {
                        if (StrUtil.equals(VoucherValidFlagEnum.NOT_EQUALS.getCode(), voucherDTO.getValidFlag())) {
                            hasFailed = true;
                            break;
                        } else if (StrUtil.equals(VoucherValidFlagEnum.NO_VALID.getCode(), voucherDTO.getValidFlag())) {
                            hasOrgFailed = true;
                            break;
                        }
                    }
                    if (hasOrgFailed) {
                        errorInfo = "签约主体为空";
                        messageStatus = RawMessageStatusEnum.FAILED.getCode();
                    } else if (hasFailed) {
                        errorInfo = "借贷金额不平";
                        messageStatus = RawMessageStatusEnum.FAILED.getCode();
                    } else {
                        messageStatus = RawMessageStatusEnum.SUCCESS.getCode();
                    }
                }
            } catch (Exception e) {
                errorInfo = e.getMessage();
                messageStatus = RawMessageStatusEnum.FAILED.getCode();
            }
            kingdeeMiddleVoucherService.lambdaUpdate().set(KingdeeMiddleVoucherEntity::getMessageStatus, messageStatus).
                    set(KingdeeMiddleVoucherEntity::getMessageError, errorInfo).
                    set(KingdeeMiddleVoucherEntity::getUpdateTime, LocalDateTime.now()).
                    eq(KingdeeMiddleVoucherEntity::getEasbzCode, entity.getKey()).
                    eq(KingdeeMiddleVoucherEntity::getContractCode, contractCode).update();
        }
    }

    /**
     * CSH_CONSOLIDATION、租金回笼、收款、红冲
     */
    private void processForCshConsolidation(Map.Entry<String, List<KingdeeMiddleVoucherEntity>> entity,
                                            Map<String, String> orgMap) {
//        boolean isOtherThing = "ACR_INVC_CONFIRM".equals(entity.getValue().get(0).getSceneName())
//                || "已开票增值税发票回导".equals(entity.getValue().get(0).getSceneName());

        //过滤不包含1531.02的数据
        boolean isExist = entity.getValue().stream().anyMatch(v -> "1531.02".equals(v.getAccountCode()));
        boolean isMarginExist = entity.getValue().stream().anyMatch(v -> "2701.01".equals(v.getAccountCode()));
        if (!isExist && !isMarginExist) {
            log.info("EasbzCode:{}不存在1531.02或者2701.01的数据", entity.getKey());
            return;
        }

        KingdeeMiddleVoucherEntity firstEntity = entity.getValue().get(0);

        String ebankNum = "";
        String ebankNo = "";
        String bankOrgId = StringUtil.EMPTY;
        boolean isAddParamReceiveUnconfirmed = false;
        boolean isAddParamDeductionMarginAmount = false;
        Map<String, BigDecimal> contractAccountAmountMap = new HashMap<>();
        for (KingdeeMiddleVoucherEntity entry : entity.getValue()) {
            if (entry.getAccountCode().equals(accountCode)) {
                String abstractContent = entry.getVoucherAbstract();
                ebankNum = getBankNum(abstractContent);

                // 数据包含该科目，则需要加未确认收款参数
                isAddParamReceiveUnconfirmed = true;
                continue;
            }

            BigDecimal amount = entry.getDebitAmount().compareTo(BigDecimal.ZERO) != 0
                    ? entry.getDebitAmount() : entry.getCreditAmount();
            if (entry.getAccountCode().equals("1531.01.01") || entry.getAccountCode().equals("2241.09")) {
                BigDecimal recyclePrincipalAmount = contractAccountAmountMap.get(entry.getContractCode().
                        concat("recyclePrincipalAmount"));
                if (recyclePrincipalAmount == null) {
                    recyclePrincipalAmount = amount;
                } else {
                    recyclePrincipalAmount = recyclePrincipalAmount.add(amount);
                }
                contractAccountAmountMap.put(entry.getContractCode().
                        concat("recyclePrincipalAmount"), recyclePrincipalAmount);
            } else if (entry.getAccountCode().equals("6051.02")) {
                BigDecimal recycleDefaultInterestAmount = contractAccountAmountMap.get(entry.getContractCode().
                        concat("recycleDefaultInterestAmount"));
                if (recycleDefaultInterestAmount == null) {
                    recycleDefaultInterestAmount = amount;
                } else {
                    recycleDefaultInterestAmount = recycleDefaultInterestAmount.add(amount);
                }
                contractAccountAmountMap.put(entry.getContractCode().
                        concat("recycleDefaultInterestAmount"), recycleDefaultInterestAmount);
            } else if (entry.getAccountCode().equals("1531.01.03")) {
                BigDecimal receiveRetainedPrice = contractAccountAmountMap.get(entry.getContractCode().
                        concat("receiveRetainedPrice"));
                if (receiveRetainedPrice == null) {
                    receiveRetainedPrice = amount;
                } else {
                    receiveRetainedPrice = receiveRetainedPrice.add(amount);
                }
                contractAccountAmountMap.put(entry.getContractCode().
                        concat("receiveRetainedPrice"), receiveRetainedPrice);
            } else if (entry.getAccountCode().equals("6051.03")) {
                BigDecimal receiveTerminateProcedureAmount = contractAccountAmountMap.get(entry.getContractCode().
                        concat("receiveTerminateProcedureAmount"));
                if (receiveTerminateProcedureAmount == null) {
                    receiveTerminateProcedureAmount = amount;
                } else {
                    receiveTerminateProcedureAmount = receiveTerminateProcedureAmount.add(amount);
                }
                contractAccountAmountMap.put(entry.getContractCode().
                        concat("receiveTerminateProcedureAmount"), receiveTerminateProcedureAmount);
            } else if (entry.getAccountCode().equals("1531.02")) {
                // 数据包含该科目，则需要加未确认收款参数
                isAddParamReceiveUnconfirmed = true;

            } else if (entry.getAccountCode().equals("2221.01.05")) {
                BigDecimal outtax = contractAccountAmountMap.get(entry.getContractCode().concat("outtax"));
                if (outtax == null) {
                    outtax = amount;
                } else {
                    outtax = outtax.add(amount);
                }
                contractAccountAmountMap.put(entry.getContractCode().concat("outtax"), outtax);
            } else if (entry.getAccountCode().equals("2701.01")) {
                isAddParamDeductionMarginAmount = true;
                BigDecimal deductionMarginAmount = contractAccountAmountMap.get(entry.getContractCode().concat("deductionMarginAmount"));
                if (deductionMarginAmount == null) {
                    deductionMarginAmount = amount;
                } else {
                    deductionMarginAmount = deductionMarginAmount.add(amount);
                }
                contractAccountAmountMap.put(entry.getContractCode().concat("deductionMarginAmount"), deductionMarginAmount);
            }

            BigDecimal receiveUnconfirmed = contractAccountAmountMap.get(entry.getContractCode().
                    concat("receiveUnconfirmed"));
            if (receiveUnconfirmed == null) {
                receiveUnconfirmed = amount;
            } else {
                receiveUnconfirmed = receiveUnconfirmed.add(amount);
            }
            contractAccountAmountMap.put(entry.getContractCode().
                    concat("receiveUnconfirmed"), receiveUnconfirmed);
        }

        List<String> contractCodeList = entity.getValue().stream().
                filter(e -> StringUtils.isNotEmpty(e.getContractCode())).
                map(e -> e.getContractCode()).distinct().collect(Collectors.toList());
        String allMessageStatus = RawMessageStatusEnum.SUCCESS.getCode();
        for (String contractCode : contractCodeList) {
            BigDecimal recycleDefaultInterestAmount = contractAccountAmountMap.get(contractCode.
                    concat("recycleDefaultInterestAmount"));
            if (recycleDefaultInterestAmount == null) {
                recycleDefaultInterestAmount = BigDecimal.ZERO;
            }

            BigDecimal outtax = contractAccountAmountMap.get(contractCode.concat("outtax"));
            if (outtax == null) {
                outtax = BigDecimal.ZERO;
            }

            BigDecimal receiveTerminateProcedureAmount = contractAccountAmountMap.get(contractCode.
                    concat("receiveTerminateProcedureAmount"));
            if (receiveTerminateProcedureAmount == null) {
                receiveTerminateProcedureAmount = BigDecimal.ZERO;
            }

            BigDecimal receiveRetainedPrice = contractAccountAmountMap.get(contractCode.
                    concat("receiveRetainedPrice"));
            if (receiveRetainedPrice == null) {
                receiveRetainedPrice = BigDecimal.ZERO;
            }

            BigDecimal recyclePrincipalAmount = contractAccountAmountMap.get(contractCode.
                    concat("recyclePrincipalAmount"));
            if (recyclePrincipalAmount == null) {
                recyclePrincipalAmount = BigDecimal.ZERO;
            }

            BigDecimal receiveUnconfirmed = contractAccountAmountMap.get(contractCode.
                    concat("receiveUnconfirmed"));
            if (receiveUnconfirmed == null) {
                receiveUnconfirmed = BigDecimal.ZERO;
            }

            BigDecimal deductionMarginAmount = contractAccountAmountMap.get(contractCode.
                    concat("deductionMarginAmount"));
            if (deductionMarginAmount == null) {
                deductionMarginAmount = BigDecimal.ZERO;
            }

            if (recycleDefaultInterestAmount.compareTo(BigDecimal.ZERO) != 0) {
                recycleDefaultInterestAmount = recycleDefaultInterestAmount.add(outtax);
            } else if (receiveTerminateProcedureAmount.compareTo(BigDecimal.ZERO) != 0) {
                receiveTerminateProcedureAmount = receiveTerminateProcedureAmount.add(outtax);
            } else if (receiveRetainedPrice.compareTo(BigDecimal.ZERO) != 0) {
                receiveRetainedPrice = receiveRetainedPrice.add(outtax);
            } else {
                recyclePrincipalAmount = recyclePrincipalAmount.add(outtax);
            }

            log.info("eanknum:{}", ebankNum);
            if (StringUtils.isNotEmpty(ebankNum)) {
                Optional<FundEbankTransactionDataEntity> dataEntity = iFundEbankTransactionDataService.lambdaQuery().
                        eq(FundEbankTransactionDataEntity::getEbankNumber, ebankNum).list().stream().findFirst();
                if (dataEntity.isPresent()) {
                    String collectionAccountsBankNo = dataEntity.get().getCollectionAccountsBankNo();
                    ebankNo = collectionAccountsBankNo;
                    if (StringUtils.isNotEmpty(collectionAccountsBankNo)) {
                        List<BankAccountEntity> bankAccountEntityList = bankAccountService.
                                selectByBankAccountCode(collectionAccountsBankNo);
                        if (CollectionUtils.isNotEmpty(bankAccountEntityList)) {
                            bankOrgId = bankAccountEntityList.get(0).getOrgId();
                        }
                    }
                }
            }
            ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
            commonDTO.setSystemCode(firstEntity.getSystemCode());
            commonDTO.setContractCode(contractCode);
//            commonDTO.setSystemName(SystemEnum.GAXD.getDesc());
            commonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
            commonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
            commonDTO.setCurrencyType(firstEntity.getCurrencyNumber());
            commonDTO.setOrderId(firstEntity.getBatchId().toString()); //fid
            commonDTO.setOrgId(firstEntity.getOrgId());
            commonDTO.setSceneCode(SceneEnum.ZLSK.getCode());
            commonDTO.setSceneName(SceneEnum.ZLSK.getDesc());
            commonDTO.setBusinessDate(CommonDateUtils.parseLocalDateTimeToDate(firstEntity.getVoucherDate()));
            commonDTO.setBatchId(firstEntity.getBatchId());
            commonDTO.setBatchType(firstEntity.getSystemCode());
            String clientCode = entity.getValue().stream().filter(e ->
                    StringUtils.equals(contractCode, e.getContractCode())).findFirst().get().getClientCode();
            commonDTO.setClientCode(clientCode);
            // add by zhangli.chen 新增interfaceid传值，支持凭证多次生成时删除interface表数据 on 20251127
            commonDTO.setInterfaceId(firstEntity.getBatchId());
            Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);
            // 是否批量
            commonMap.put("isBulkOperation", YesOrNoEnum.NO.getDesc());
            commonMap.put("easVoucherId", entity.getKey());
            // 银行到账主体
            commonMap.put("ebankNum", ebankNo);
            commonMap.put("ebankSerialNumber", ebankNum);
            // 借款合同编号
            commonMap.put("billContractCode", "");
            commonMap.put("createDate", DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));
            commonMap.put("updateDate", DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));
            commonMap.put("recyclePrincipalAmount", recyclePrincipalAmount);
            commonMap.put("recycleDefaultInterestAmount", recycleDefaultInterestAmount);
            commonMap.put("receiveRetainedPrice", receiveRetainedPrice);
            commonMap.put("receiveTerminateProcedureAmount", receiveTerminateProcedureAmount);
            if (isAddParamReceiveUnconfirmed) {
                commonMap.put("receiveUnconfirmed", receiveUnconfirmed);
            }
            if (isAddParamDeductionMarginAmount) {
                commonMap.put("deductionMarginAmount", deductionMarginAmount);
                commonMap.put("deductionMarginContract", contractCode);
            }
            commonMap.put("bankOrgId", bankOrgId);
            commonMap.put(RuleConstant.IS_SUBMIT, YesOrNoEnum.YES.getCode());
            if (StringUtils.isNotEmpty(firstEntity.getOrgId())) {
                commonMap.put("orgName", orgMap.get(firstEntity.getOrgId()));
            }

            //生成凭证信息
            String messageStatus = RawMessageStatusEnum.NOT_EXECUTE.getCode();
            String errorInfo = "";

            try {
                List<VoucherDTO> voucherDTOList = ruleService.executeRule(commonMap);
                log.info("金蝶现代物流生成凭证返回值:{}", JSON.toJSON(voucherDTOList));
                if (CollectionUtils.isEmpty(voucherDTOList) || CollectionUtils.isEmpty(voucherDTOList.get(0).getEntryList())) {
                    errorInfo = "凭证行为空";
                    messageStatus = RawMessageStatusEnum.FAILED.getCode();
                    allMessageStatus = RawMessageStatusEnum.FAILED.getCode();
                } else {
                    boolean hasFailed = false;
                    boolean hasOrgFailed = false;
                    for (VoucherDTO voucherDTO : voucherDTOList) {
                        if (StrUtil.equals(VoucherValidFlagEnum.NOT_EQUALS.getCode(), voucherDTO.getValidFlag())) {
                            hasFailed = true;
                            break;
                        } else if (StrUtil.equals(VoucherValidFlagEnum.NO_VALID.getCode(), voucherDTO.getValidFlag())) {
                            hasOrgFailed = true;
                            break;
                        }
                    }
                    if (hasOrgFailed) {
                        errorInfo = "签约主体为空";
                        messageStatus = RawMessageStatusEnum.FAILED.getCode();
                        allMessageStatus = RawMessageStatusEnum.FAILED.getCode();
                    } else if (hasFailed) {
                        errorInfo = "借贷金额不平";
                        messageStatus = RawMessageStatusEnum.FAILED.getCode();
                        allMessageStatus = RawMessageStatusEnum.FAILED.getCode();
                    } else {
                        messageStatus = RawMessageStatusEnum.SUCCESS.getCode();
                    }
                }
            } catch (Exception e) {
                errorInfo = e.getMessage();
                messageStatus = RawMessageStatusEnum.FAILED.getCode();
                allMessageStatus = RawMessageStatusEnum.FAILED.getCode();
            }

            kingdeeMiddleVoucherService.lambdaUpdate().set(KingdeeMiddleVoucherEntity::getMessageStatus, messageStatus).
                    set(KingdeeMiddleVoucherEntity::getMessageError, errorInfo).
                    set(KingdeeMiddleVoucherEntity::getUpdateTime, LocalDateTime.now()).
                    eq(KingdeeMiddleVoucherEntity::getEasbzCode, entity.getKey()).
                    eq(KingdeeMiddleVoucherEntity::getContractCode, contractCode).update();
            //生成凭证之后生成认领记录
            if (messageStatus.equals(RawMessageStatusEnum.SUCCESS.getCode())) {
                /******modify by zhangli.chen for 去除抽取贵安，现代物流数据生成凭证时同步插入认领表的逻辑，转而通过另外一条路径job抽数路径：
                 * 抽取业务系统未确认收款金额(nonConfirmAmountSync)来生成认领数据 ******/
                //saveRecord(commonMap);
            }
        }

        kingdeeMiddleVoucherService.lambdaUpdate().set(KingdeeMiddleVoucherEntity::getMessageStatus, allMessageStatus).
                set(KingdeeMiddleVoucherEntity::getUpdateTime, LocalDateTime.now()).
                eq(KingdeeMiddleVoucherEntity::getEasbzCode, entity.getKey()).
                and(e -> e.isNull(KingdeeMiddleVoucherEntity::getContractCode).
                        or(a -> a.eq(KingdeeMiddleVoucherEntity::getContractCode, ""))).update();
    }

    public String getBankNum(String abstractContent) {
          /*根据网银编号获取eg_fund_ebank_transaction_data表中查出网银编号对应的银行账号、签约主体
          取科目，若科目为1531.02，读取摘要，
          1）判断摘要中是否存在“，”,如果存在“，”，则从第1位取到“，”
          第一次出现的位置，做为网银编号，同时从eg_fund_ebank_transaction_data表中查出网银编号对应的银行账号、签约主体，做为该笔回收的数据；
          2）如果摘要中没有“，”，如果是2开头的，则截取第1位到‘-’第一次出现的位置的值，做为该笔的网银编号，同时从eg_fund_ebank_transaction_data表中查出网银编号对应的银行账号、签约主体，做为该笔回收的数据；
          3）如果前面两个条件都不满足，则截取第1位至“-”第二次出现位置的值，做为网银编号，处理同上
          若科目为1531.01.01、2241.09，对应回收接口表：recyclePrincipalAmount；应收租金，代收的中台可根据到账主体与客户的主体不一致自动生成；
          若科目为6051.02，对应回收接口表：recycleDefaultInterestAmount，应收罚息
          若科目为1531.01.03，对应回收接口表：receiveRetainedPrice     ，应收期末残值
          若科目为6051.03，对应回收接口表：receiveTerminateProcedureAmount，合同解约手续费
          若科目为1531.02 对应receiveUnconfirmed
           */
        String ebankNum = "";
        if (StringUtils.isEmpty(abstractContent)) {
            return ebankNum;
        }
        abstractContent = abstractContent.replace('，', ',');
        if (!abstractContent.startsWith("C")){
            Pattern pattern = Pattern.compile(RegexExpConst.REGEX_ONLINE_BANKING_NUMBER);
            Matcher matcher = pattern.matcher(abstractContent);
            if (matcher.find()) {
                ebankNum = matcher.group(); // 获取匹配的字符串
            }
        }else {
            if (StringUtils.isBlank(ebankNum)) {
                Pattern oldPattern = Pattern.compile(RegexExpConst.REGEX_OLD_ONLINE_BANKING_NUMBER);
                Matcher oldMatcher = oldPattern.matcher(abstractContent);
                if (oldMatcher.find()) {
                    ebankNum = oldMatcher.group(); // 获取匹配的字符串
                }
            }
        }
        return ebankNum;
    }

    public Boolean saveMiddleVoucher(List<KingdeeMiddleVoucherEntity> entityList) {
        KingdeeMiddleVoucherEntity entity = entityList.get(0);
        try {
            List<AccountEntity> accountEntityList = iAccountService.lambdaQuery().list();
            Map<String, List<AccountEntity>> accountMap = accountEntityList.stream().collect(Collectors.groupingBy(v -> v.getBusinessCode() + "-" + v.getAccountCode()));
            //组装凭证头
            VoucherSaveDTO voucherSaveDTO = new VoucherSaveDTO();
            voucherSaveDTO.setVoucherDate(entity.getVoucherDate());
            voucherSaveDTO.setVoucherSummary(entity.getVoucherAbstract());
            voucherSaveDTO.setCreateUserName(UserUtils.getStaffCode());
            voucherSaveDTO.setVoucherStatus(ProcessStatusEnum.REVIEWED.getCode());
            voucherSaveDTO.setCurrency(entity.getCurrencyNumber());
            voucherSaveDTO.setBusinessDate(entity.getVoucherDate());
            voucherSaveDTO.setSceneName(SceneEnum.ZLSK.getDesc());
            voucherSaveDTO.setSceneCode(SceneEnum.ZLSK.getCode());
            voucherSaveDTO.setPeriodCode(PeriodCodeUtil.periodCodeByLocalDateTime(entity.getVoucherDate()));
            voucherSaveDTO.setIsSummary(YesOrNoEnum.NO.getCode());
            voucherSaveDTO.setCreateUserNo(UserUtils.getStaffCode());
            voucherSaveDTO.setEasVoucherId(entity.getEasbzCode());
            voucherSaveDTO.setValidFlag(VoucherValidFlagEnum.VALID.getCode());
            voucherSaveDTO.setSystemCode(entity.getSystemCode());
//            voucherSaveDTO.setSystemName(SystemEnum.GAXD.getDesc());
            voucherSaveDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
            voucherSaveDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
            voucherSaveDTO.setVoucherType("05");//默认自动转账
            voucherSaveDTO.setVoucherNum(iVoucherService.generateVoucherNum(voucherSaveDTO.getVoucherType(), voucherSaveDTO.getVoucherDate()));
            voucherSaveDTO.setOrgId(entity.getOrgId());
            voucherSaveDTO.setSource(entity.getSystemCode());
            voucherSaveDTO.setBatchId(entity.getBatchId());
            voucherSaveDTO.setBatchType(entity.getSystemCode());
            voucherSaveDTO.setOrderId(entity.getBatchId().toString());
            List<VoucherEntrySaveDTO> entryEntityList = Lists.newArrayList();
            entityList.forEach(v -> {
                VoucherEntrySaveDTO entity1 = new VoucherEntrySaveDTO();
                String key = voucherSaveDTO.getBusinessCode() + "-" + v.getAccountCode();
                if (accountMap.containsKey(key)) {
                    entity1.setFundType(accountMap.get(key).get(0).getFundType());
                }
                String debitCreditType = v.getDebitAmount().compareTo(BigDecimal.ZERO) == 0 ? DRCREnum.CR.getCode() : DRCREnum.DR.getCode();
                entity1.setVoucherSummary(v.getVoucherAbstract());
                entity1.setCreditAmount(v.getCreditAmount());
                entity1.setDebitAmount(v.getDebitAmount());
                entity1.setConvertDebitAmount(v.getDebitAmount());
                entity1.setConvertCreditAmount(v.getCreditAmount());
                entity1.setClientCode(v.getClientCode());
                entity1.setAccountName("");
                entity1.setAccountCode(v.getAccountCode());
                entity1.setClientName("");
                entity1.setContractCode(v.getContractCode());
                entity1.setContractName("");
                entity1.setDebitCreditType(debitCreditType);
                entryEntityList.add(entity1);
            });
            voucherSaveDTO.setEntryList(entryEntityList);
            voucherSaveDTO.setIsSubmit(YesOrNoEnum.YES.getCode());
            VoucherDTO voucherDTO = iVoucherService.saveVoucherAndEntries(voucherSaveDTO);
            iContractBalanceService.saveMonualContractBalanceFromVoucher(voucherDTO);
        } catch (Exception e) {
            throw new ServiceException(String.format("保存凭证出错，easbzCode:%s,错误原因：%s", entity.getEasbzCode(), e.getMessage()));
        }
        return Boolean.TRUE;
    }

    public Map<String, String> getCurrencyMap() {
        Map<String, String> currencyMap = Maps.newHashMap();
        List<CurrencyEntity> currencyEntityList = iCurrencyService.lambdaQuery().list();
        if (CollectionUtils.isNotEmpty(currencyEntityList)) {
            currencyMap = currencyEntityList.stream().collect(HashMap::new, (map, item) -> map.put(item.getEasCode(), item.getCurrencyCode()), HashMap::putAll);
        }
        return currencyMap;
    }

    private Map<String, String> getOrgNameOrgId() {
        Map<String, String> orgNameAndIdMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList = iOrgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgNameAndIdMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId, OrgCompanyVO::getOrgName, (k1, k2) -> k2));
        }
        return orgNameAndIdMap;
    }
}

