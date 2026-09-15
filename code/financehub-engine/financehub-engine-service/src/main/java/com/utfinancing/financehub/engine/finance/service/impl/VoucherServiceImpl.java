package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.common.utils.MapUtil;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.CacheConstants;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.constant.DefaultConstant;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.ManualMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherDetailExportDTO;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherVO;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.mapper.VoucherMapper;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataDTO;
import com.utfinancing.financehub.engine.rule.service.IPeriodCodeService;
import com.utfinancing.financehub.engine.rule.service.impl.LocalSegmentVoucherGenerator;
import com.utfinancing.financehub.engine.scene.model.dto.*;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import com.utfinancing.financehub.engine.utils.AssistFlagUtil;
import com.utfinancing.financehub.engine.utils.PeriodCodeUtil;
import com.utfinancing.financehub.engine.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-01
 * @Description :  Voucher服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class VoucherServiceImpl extends MPJBaseServiceImpl<VoucherMapper, VoucherEntity> implements IVoucherService {

    private final VoucherMapper voucherMapper;
    private final RemoteDictService remoteDictService;
    private final IVoucherEntryService voucherEntryService;
    @Autowired
    private final IOrgCompanyService orgCompanyService;
    private final IAccountService accountService;
    private final RedisService redisService;
    private final IContractBalanceService iContractBalanceService;
    private final IBankAccountService iBankAccountService;
    private final IContractBalanceLatestService iContractBalanceLatestService;

    private final IPeriodCodeService periodCodeService;

    private final IContractBalanceTempService contractBalanceTempService;

    //过期时间：1个月
    private static final long REDIS_VOUCHER_NUM_EXPIRE = 3600L * 24 * 30;

    private final IClientService iClientService;

    private final ManualMapper manualMapper;

    private final ICurrencyService iCurrencyService;

    private final IEasExchangeRateService iEasExchangeRateService;
    @Value("${eas.service.easLogin.url:null}")
    private String easLoginUrl;

    @Value("${file.storage.easLoginePath.windows:null}")
    private String easFilePathForWindows;

    @Value("${file.storage.easLoginePath.linux:null}")
    private String easFilePathForLinux;

    @Resource
    private ResourceLoader resourceLoader;

    @Resource
    private IAccrualSituationDataService accrualSituationDataService;

    @Lazy
    @Resource
    private IManualService iManualService;

    @Lazy
    @Resource
    private IManualVoucherService iManualVoucherService;

    // 注入新的本地号段生成器
    @Autowired
    private LocalSegmentVoucherGenerator localSegmentVoucherGenerator;


    @Override
    public Long saveVoucher(VoucherDTO dto) {
        VoucherEntity entity = BeanUtil.copyProperties(dto, VoucherEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateVoucher(Long id, VoucherDTO dto) {
        VoucherEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public VoucherDTO getVoucherDTOById(Long id) {
        VoucherEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, VoucherDTO.class);
    }

    @Override
    public IPage<VoucherVO> selectPage(VoucherQueryDTO queryDTO) {
        LambdaQueryWrapper<VoucherEntity> queryWrapper = Wrappers.<VoucherEntity>lambdaQuery();
        queryWrapper.orderByDesc(VoucherEntity::getCreateTime);
        //这里注入查询条件
        IPage<VoucherEntity> entityIPage = voucherMapper.selectPage(new Page<VoucherEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, VoucherVO.class);
    }

    @Override
    public VoucherDTO saveVoucherAndEntries(VoucherSaveDTO voucherDTO) {
        VoucherEntity voucherEntity = BeanUtil.copyProperties(voucherDTO, VoucherEntity.class);
        //中台系统根据是否提交字段更新状态为提交或已录入状态其他系统更新为已复核
        String voucherStatus = ProcessStatusEnum.REVIEWED.getCode();
        if (SystemEnum.CWZT.getCode().equals(voucherEntity.getSystemCode())
//            && !SceneEnum.SGPZ.getCode().equals(voucherEntity.getBatchType())
        ) {
            if (YesOrNoEnum.YES.getCode().equals(voucherDTO.getIsSubmit())) {
                voucherStatus = ProcessStatusEnum.SUBMITTED.getCode();
            } else {
                voucherStatus = ProcessStatusEnum.ENTERED.getCode();
            }
        }
        //转换币种
        voucherEntity.setCurrency(getCurrency(voucherEntity.getCurrency()));
        if (StringUtils.isEmpty(voucherEntity.getCreateUserNo())) {
            voucherEntity.setCreateUserNo(UserUtils.getStaffCode());
            voucherEntity.setCreateUserName(UserUtils.getStaffName());
        }
        voucherEntity.setVoucherStatus(voucherStatus);
        //处理凭证是否汇总字段
        setIsSummary(voucherEntity, voucherDTO);
        //获取汇率
        BigDecimal rate = iEasExchangeRateService.getRateBySourceNameAndTargetName(CurrencyTypeEnum.getDescByCode(voucherEntity.getCurrency()), CurrencyTypeEnum.CNY.getDesc());
        this.save(voucherEntity);
        VoucherDTO dto = BeanUtil.copyProperties(voucherEntity, VoucherDTO.class);

        if (CollectionUtil.isNotEmpty(voucherDTO.getEntryList())) {
            List<String> accountCodeList = Lists.newArrayList();

            List<VoucherEntryDTO> entryList = voucherDTO.getEntryList().stream().map(e -> {
                VoucherEntryEntity entryEntity = BeanUtil.copyProperties(e, VoucherEntryEntity.class);
                entryEntity.setVoucherId(voucherEntity.getId());
                entryEntity.setPeriodCode(voucherEntity.getPeriodCode());
                entryEntity.setAssistFlags(e.getAssistFlags());
                // 金融机构，成本中心放到凭证行上
                entryEntity.setFinancialInstitution(dto.getFinancialInstitution());
                entryEntity.setCostCentre(dto.getCostCentre());
                if (DefaultConstant.EDIT_FLAG_ACCOUNT_CODE.equals(entryEntity.getAccountCode())) {
                    entryEntity.setEditFlag(YesOrNoEnum.YES.getCode());
                }
                if (DRCREnum.CR.getCode().equals(entryEntity.getDebitCreditType())) {
                    if (entryEntity.getCreditAmount() == null) {
                        entryEntity.setConvertCreditAmount(BigDecimal.ZERO);
                        entryEntity.setVoucherAmount(BigDecimal.ZERO);
                    } else {
                        entryEntity.setConvertCreditAmount(entryEntity.getCreditAmount().multiply(rate).setScale(2, RoundingMode.HALF_UP));
                        entryEntity.setVoucherAmount(entryEntity.getCreditAmount());
                    }
                } else {
                    if (entryEntity.getDebitAmount() == null) {
                        entryEntity.setConvertDebitAmount(BigDecimal.ZERO);
                        entryEntity.setVoucherAmount(BigDecimal.ZERO);
                    } else {
                        entryEntity.setConvertDebitAmount(entryEntity.getDebitAmount().multiply(rate).setScale(2, RoundingMode.HALF_UP));
                        entryEntity.setVoucherAmount(entryEntity.getDebitAmount());
                    }
                }
                voucherEntryService.save(entryEntity); //保存
                VoucherEntryDTO entryDTO = BeanUtil.copyProperties(entryEntity, VoucherEntryDTO.class);
                entryDTO.setAssistFlags(e.getAssistFlags());

                accountCodeList.add(entryDTO.getAccountCode());

                return entryDTO;
            }).collect(Collectors.toList());
            dto.setEntryList(entryList);

            saveAccrualData(voucherEntity, accountCodeList);
        } else {
            dto.setEntryList(new ArrayList<>());
        }
        //voucherEntryService.saveBatch(entryEntityList);
        return dto;
    }

    //将符合要求的凭证数据存入计提情况表
    private void saveAccrualData(VoucherEntity voucherEntity, List<String> accountCodeList) {
        String sceneCode = voucherEntity.getSceneCode();
        String validFlag = voucherEntity.getValidFlag();
        String processStatus = voucherEntity.getVoucherStatus();
        String voucherType = voucherEntity.getVoucherType();

        String fillType = "";
        if (StringUtils.equals(SceneEnum.ZLSK.getCode(), sceneCode) && StringUtils.equals(YesOrNoEnum.YES.getCode(), validFlag) &&
                (StringUtils.equals(ProcessStatusEnum.SUBMITTED.getCode(), processStatus) || StringUtils.equals(ProcessStatusEnum.REVIEWED.getCode(), processStatus) || StringUtils.equals(ProcessStatusEnum.TO_KINGDEE.getCode(), processStatus)) &&
                StringUtils.equals("5", voucherType) && accountCodeList.containsAll(Lists.newArrayList("1531.01.01", "1531.01.09", "2221.01.05"))) {
            //租金
            fillType = "1";
        } else if (StringUtils.equals(SceneEnum.ZLSK.getCode(), sceneCode) && StringUtils.equals(YesOrNoEnum.YES.getCode(), validFlag) &&
                (StringUtils.equals(ProcessStatusEnum.SUBMITTED.getCode(), processStatus) || StringUtils.equals(ProcessStatusEnum.REVIEWED.getCode(), processStatus) || StringUtils.equals(ProcessStatusEnum.TO_KINGDEE.getCode(), processStatus)) &&
                StringUtils.equals("5", voucherType) && accountCodeList.containsAll(Lists.newArrayList("1531.01.03", "1531.01.09", "2221.01.05"))) {
            //留购价
            fillType = "2";
        } else if (StringUtils.equals(SceneEnum.ZLSK.getCode(), sceneCode) && StringUtils.equals(YesOrNoEnum.YES.getCode(), validFlag) &&
                (StringUtils.equals(ProcessStatusEnum.SUBMITTED.getCode(), processStatus) || StringUtils.equals(ProcessStatusEnum.REVIEWED.getCode(), processStatus) || StringUtils.equals(ProcessStatusEnum.TO_KINGDEE.getCode(), processStatus)) &&
                StringUtils.equals("5", voucherType) && accountCodeList.containsAll(Lists.newArrayList("1531.01.04", "1531.01.09", "2221.01.05"))) {
            //罚息
            fillType = "3";
        } else if (StringUtils.equals(SceneEnum.ZLSK.getCode(), sceneCode) && StringUtils.equals(YesOrNoEnum.YES.getCode(), validFlag) &&
                (StringUtils.equals(ProcessStatusEnum.SUBMITTED.getCode(), processStatus) || StringUtils.equals(ProcessStatusEnum.REVIEWED.getCode(), processStatus) || StringUtils.equals(ProcessStatusEnum.TO_KINGDEE.getCode(), processStatus)) &&
                StringUtils.equals("5", voucherType) && accountCodeList.containsAll(Lists.newArrayList("1221.16", "1531.01.09", "2221.01.05"))) {
            //手续费
            fillType = "4";
        } else if (StringUtils.equals(SceneEnum.ZLSK.getCode(), sceneCode) && StringUtils.equals(YesOrNoEnum.YES.getCode(), validFlag) &&
                (StringUtils.equals(ProcessStatusEnum.SUBMITTED.getCode(), processStatus) || StringUtils.equals(ProcessStatusEnum.REVIEWED.getCode(), processStatus) || StringUtils.equals(ProcessStatusEnum.TO_KINGDEE.getCode(), processStatus)) &&
                StringUtils.equals("5", voucherType) && accountCodeList.containsAll(Lists.newArrayList("1221.17", "1531.01.09", "2221.01.05"))) {
            //变更手续费
            fillType = "5";
        } else if (StringUtils.equals(SceneEnum.ZLSK.getCode(), sceneCode) && StringUtils.equals(YesOrNoEnum.YES.getCode(), validFlag) &&
                (StringUtils.equals(ProcessStatusEnum.SUBMITTED.getCode(), processStatus) || StringUtils.equals(ProcessStatusEnum.REVIEWED.getCode(), processStatus) || StringUtils.equals(ProcessStatusEnum.TO_KINGDEE.getCode(), processStatus)) &&
                StringUtils.equals("3", voucherType) && accountCodeList.containsAll(Lists.newArrayList("2221.01.05"))) {
            //租金
            fillType = "6";
        }
        if (StringUtils.isNotEmpty(fillType)) {
            AccrualSituationDataEntity accrualSituationDataEntity = accrualSituationDataService.getDataByVoucherId(voucherEntity.getId(), fillType);
            if (ObjectUtil.isNotEmpty(accrualSituationDataEntity)) {
                accrualSituationDataService.save(accrualSituationDataEntity);
            }
        }
    }

    @Override
    public VoucherDTO saveVoucherDetailByRuleResult(SceneRuleDTO ruleDTO, InterfaceDataDTO interfaceDataDTO) {
        VoucherSaveDTO voucherSaveDTO = new VoucherSaveDTO();
        BeanUtil.copyProperties(interfaceDataDTO, voucherSaveDTO);
        voucherSaveDTO.setInterfaceDataId(interfaceDataDTO.getId());
        voucherSaveDTO.setSource(ruleDTO.getSource());//来源
        voucherSaveDTO.setVoucherType(ruleDTO.getVoucherType());//凭证类型
        voucherSaveDTO.setBusinessDate(DateUtil.toLocalDateTime(DateUtil.parse(ruleDTO.getBusinessDate())));//业务日期
        voucherSaveDTO.setCurrency(ruleDTO.getCurrency());//币种
        voucherSaveDTO.setDeptName(ruleDTO.getDeptName());//部门
        voucherSaveDTO.setVoucherSummary(ruleDTO.getVoucherSummary());//凭证摘要
        voucherSaveDTO.setSignCompany(ruleDTO.getCompany()); //公司
        voucherSaveDTO.setVoucherWay(VoucherWayEnum.AUTO.getCode());//自动生成
        voucherSaveDTO.setSubSceneType(ruleDTO.getSubSceneType());
        voucherSaveDTO.setOrgId(ruleDTO.getCompany()); //凭证的签约主体为规则配置的签约主体

        LocalDate voucherDate = periodCodeService.generateVoucherDate(interfaceDataDTO);
        voucherSaveDTO.setVoucherDate(voucherDate.atStartOfDay());//凭证日期
        voucherSaveDTO.setPeriodCode(NumberUtil.parseInt(LocalDateTimeUtil.format(voucherDate, "yyyyMM")));
        voucherSaveDTO.setVoucherNum(generateVoucherNum(ruleDTO.getVoucherType(), voucherDate.atStartOfDay()));

        List<VoucherEntrySaveDTO> entryList = new ArrayList<>();

        //凭证行
        List<SceneVoucherEntryDTO> sceneVoucherEntryDTOList = ruleDTO.getEntryList();
        for (SceneVoucherEntryDTO sceneVoucherEntryDTO : sceneVoucherEntryDTOList) {
            for (SceneVoucherConditionDTO sceneVoucherConditionDTO : sceneVoucherEntryDTO.getConditionList()) {
                boolean condition = BooleanUtil.toBoolean(sceneVoucherConditionDTO.getScriptCondition());
                if (condition) {
                    //条件成立，则生成凭证行
                    VoucherEntrySaveDTO entrySaveDTO = mappingVoucherEntry(sceneVoucherEntryDTO,
                            sceneVoucherConditionDTO, interfaceDataDTO, voucherSaveDTO);
                    entryList.add(entrySaveDTO);
                }
            }
        }
        voucherSaveDTO.setEntryList(entryList);
        return this.saveVoucherAndEntries(voucherSaveDTO);
    }

    @Override
    public VoucherSaveDTO generateVoucherFromRule(SceneRuleDTO ruleDTO, InterfaceDataDTO interfaceDataDTO) {
        VoucherSaveDTO voucherSaveDTO = new VoucherSaveDTO();
        try {
            BeanUtil.copyProperties(interfaceDataDTO, voucherSaveDTO);
            voucherSaveDTO.setValidFlag(VoucherValidFlagEnum.VALID.getCode());
            voucherSaveDTO.setInterfaceDataId(interfaceDataDTO.getId());
            voucherSaveDTO.setSource(ruleDTO.getSource());//来源
            voucherSaveDTO.setVoucherType(ruleDTO.getVoucherType());//凭证类型
            voucherSaveDTO.setBusinessDate(DateUtil.toLocalDateTime(DateUtil.parse(ruleDTO.getBusinessDate())));//业务日期
            voucherSaveDTO.setCurrency(ruleDTO.getCurrency());//币种
            voucherSaveDTO.setDeptName(ruleDTO.getDeptName());//部门
            voucherSaveDTO.setVoucherSummary(ruleDTO.getVoucherSummary());//凭证摘要
            voucherSaveDTO.setSignCompany(ruleDTO.getCompany()); //公司
            voucherSaveDTO.setVoucherWay(VoucherWayEnum.AUTO.getCode());//自动生成
            voucherSaveDTO.setSubSceneType(ruleDTO.getSubSceneType());
            voucherSaveDTO.setOrgId(ruleDTO.getCompany()); //凭证的签约主体为规则配置的签约主体
            voucherSaveDTO.setEasVoucherId(interfaceDataDTO.getEasVoucherId());
            if (StringUtils.isEmpty(voucherSaveDTO.getOrgId())) {
                voucherSaveDTO.setOrgId(interfaceDataDTO.getOrgId());
            }
            if (null != interfaceDataDTO.getInterfaceId()) {
                voucherSaveDTO.setInterfaceId(interfaceDataDTO.getInterfaceId());
            }
            //核销场景
            LocalDate voucherDate = null;
            if (SceneEnum.HZHX.getCode().equals(voucherSaveDTO.getSceneCode())) {
                voucherDate = interfaceDataDTO.getFinanceDate().toLocalDate();
            } else {
                voucherDate = periodCodeService.generateVoucherDate(interfaceDataDTO);
            }
            voucherSaveDTO.setVoucherDate(voucherDate.atStartOfDay());//凭证日期
            voucherSaveDTO.setPeriodCode(NumberUtil.parseInt(LocalDateTimeUtil.format(voucherDate, "yyyyMM")));
            voucherSaveDTO.setVoucherNum(generateVoucherNum(ruleDTO.getVoucherType(), voucherDate.atStartOfDay()));

            List<VoucherEntrySaveDTO> entryList = new ArrayList<>();
            //场景为ZLSK，细分场景为1/2/3/4/33，系统来源不等于FINHUB时修改客户编码
            boolean isEditClientCode;
            List<String> sceneList = Lists.newArrayList(SceneEnum.ZLSK.getCode()
                    , SceneEnum.RKCZ.getCode()
                    , SceneEnum.HTCX.getCode(), SceneEnum.TACFL.getCode());
            List<String> subSceneTypeList = Lists.newArrayList("1", "2", "3", "4", "8", "18", "33");
            if (!SystemEnum.CWZT.getCode().equals(voucherSaveDTO.getSystemCode())
                    && sceneList.contains(voucherSaveDTO.getSceneCode())
                    && subSceneTypeList.contains(voucherSaveDTO.getSubSceneType())) {
                isEditClientCode = Boolean.TRUE;
            } else {
                isEditClientCode = Boolean.FALSE;
            }

            boolean isCWCK = false;
            if (SceneEnum.CWCK.getCode().equals(voucherSaveDTO.getSceneCode())) {
                isCWCK = true;
            }

            String sceneCode = voucherSaveDTO.getSceneCode();
            String subSceneCode = voucherSaveDTO.getSubSceneType();
            //凭证摘要存在空值替换为空字符
            if (StringUtils.isNotEmpty(voucherSaveDTO.getVoucherSummary())) {
                voucherSaveDTO.setVoucherSummary(voucherSaveDTO.getVoucherSummary().replace("null-", ""));
            }
            //凭证行
            List<SceneVoucherEntryDTO> sceneVoucherEntryDTOList = ruleDTO.getEntryList();
            for (SceneVoucherEntryDTO sceneVoucherEntryDTO : sceneVoucherEntryDTOList) {
                for (SceneVoucherConditionDTO sceneVoucherConditionDTO : sceneVoucherEntryDTO.getConditionList()) {
                    boolean condition = BooleanUtil.toBoolean(sceneVoucherConditionDTO.getScriptCondition());
                    if (condition) {
                        //条件成立，则生成凭证行
                        VoucherEntrySaveDTO entrySaveDTO = mappingVoucherEntry(sceneVoucherEntryDTO,
                                sceneVoucherConditionDTO, interfaceDataDTO, voucherSaveDTO);
                        if (isEditClientCode) {
                            if (StringUtils.isNotEmpty(voucherSaveDTO.getCrossContractFlag()) && "1".equals(voucherSaveDTO.getCrossContractFlag())) {
                                if (DefaultConstant.BANK_COLLECTION_TRANSFER.equals(entrySaveDTO.getAccountCode())
                                        || DefaultConstant.BANK_PAYABLE_RELATED_PARTY.equals(entrySaveDTO.getAccountCode())) {
                                    entrySaveDTO.setClientCode(interfaceDataDTO.getBankClientCode());
                                } else if (DefaultConstant.ORG_RECEIVABLE_RELATED_PARTY.equals(entrySaveDTO.getAccountCode())
                                        || DefaultConstant.ORG_RECEIVABLE_COLLECTION_TRANSGER.equals(entrySaveDTO.getAccountCode())) {
                                    entrySaveDTO.setClientCode(interfaceDataDTO.getOrgClientCode());
                                }
                            } else {
                                if (DefaultConstant.BANK_COLLECTION_TRANSFER.equals(entrySaveDTO.getAccountCode())
                                        || DefaultConstant.BANK_PAYABLE_RELATED_PARTY.equals(entrySaveDTO.getAccountCode())) {
                                    entrySaveDTO.setClientCode(interfaceDataDTO.getOrgClientCode());
                                } else if (DefaultConstant.ORG_RECEIVABLE_RELATED_PARTY.equals(entrySaveDTO.getAccountCode())
                                        || DefaultConstant.ORG_RECEIVABLE_COLLECTION_TRANSGER.equals(entrySaveDTO.getAccountCode())) {
                                    entrySaveDTO.setClientCode(interfaceDataDTO.getBankClientCode());
                                }
                            }
                        }

                        //替换客户编码
                        setClientCode(entrySaveDTO, interfaceDataDTO, sceneCode, subSceneCode);

                        //回收设备（财务出库）生成的凭证科目编码为1221.99的客户修改为01-02-001758
                        if (isCWCK) {
                            if (DefaultConstant.OTHER_RECEIVABLE_COLLECTION_TRANSGER.equals(entrySaveDTO.getAccountCode())) {
                                entrySaveDTO.setClientCode("01-02-001758");
                            }
                        }
                        //凭证摘要存在空值替换为空字符
                        if (StringUtils.isNotEmpty(entrySaveDTO.getVoucherSummary())) {
                            entrySaveDTO.setVoucherSummary(entrySaveDTO.getVoucherSummary().replace("null-", ""));
                        }
                        entryList.add(entrySaveDTO);
                    }
                }
            }
            voucherSaveDTO.setEntryList(entryList);
        } catch (Exception e) {
            log.error("错误信息", e);
        }
        return voucherSaveDTO;
    }

    @Override
    public VoucherDTO getVoucherDTO(Long id) {
        VoucherEntity voucherEntity = this.getById(id);
        VoucherDTO voucherDTO = BeanUtil.copyProperties(voucherEntity, VoucherDTO.class);
        List<VoucherEntryEntity> voucherEntryEntity = voucherEntryService.list(Wrappers.<VoucherEntryEntity>lambdaQuery()
                .eq(VoucherEntryEntity::getVoucherId, id));
        List<VoucherEntryDTO> voucherEntryDTOList = ListBeanUtil.copyList(voucherEntryEntity, VoucherEntryDTO.class);
        voucherDTO.setEntryList(voucherEntryDTOList);
        return voucherDTO;
    }

    @Override
    public VoucherDTO getLastVoucherDTO(String businessCode, String orgId, String clientCode, String contractCode, Date lastDay) {
        LambdaQueryWrapper<VoucherEntity> lambdaQueryWrapper = Wrappers.<VoucherEntity>lambdaQuery();
        lambdaQueryWrapper.eq(VoucherEntity::getBusinessCode, businessCode);
        lambdaQueryWrapper.eq(VoucherEntity::getOrgId, orgId);
        if (StrUtil.isNotBlank(clientCode)) {
            lambdaQueryWrapper.eq(VoucherEntity::getClientCode, clientCode);
        } else {
            lambdaQueryWrapper.isNull(VoucherEntity::getClientCode);
        }
        if (StrUtil.isNotBlank(contractCode)) {
            lambdaQueryWrapper.eq(VoucherEntity::getContractCode, contractCode);
        } else {
            lambdaQueryWrapper.isNull(VoucherEntity::getContractCode);
        }
        if (null != lastDay) {
            lambdaQueryWrapper.lt(VoucherEntity::getBusinessDate, lastDay);
        }
        lambdaQueryWrapper.orderByDesc(VoucherEntity::getVoucherDate);
        lambdaQueryWrapper.last("limit 1");
        VoucherEntity voucherEntity = this.getOne(lambdaQueryWrapper);
        if (null == voucherEntity) {
            return null;
        }
        VoucherDTO voucherDTO = BeanUtil.copyProperties(voucherEntity, VoucherDTO.class);
        List<VoucherEntryEntity> voucherEntryEntity = voucherEntryService.list(Wrappers.<VoucherEntryEntity>lambdaQuery()
                .eq(VoucherEntryEntity::getVoucherId, voucherEntity.getId()));
        List<VoucherEntryDTO> voucherEntryDTOList = ListBeanUtil.copyList(voucherEntryEntity, VoucherEntryDTO.class);
        voucherDTO.setEntryList(voucherEntryDTOList);
        return voucherDTO;
    }

    @Override
    public IPage<VoucherDetailDTO> queryVoucherPage(VoucherQueryDTO queryDTO) {
        //如果勾选了科目代码或者科目名称需要查出其下该会计期间的所有凭证号,凭证头摘要、科目代码、凭证行摘要、合同编号、客户名称、借款合同编号、银行账号
        if (CollectionUtil.isNotEmpty(queryDTO.getAccountCodeList()) || CollectionUtil.isNotEmpty(queryDTO.getAccountNameList())
                || StringUtils.isNotEmpty(queryDTO.getVoucherEntrySummary()) || StringUtils.isNotEmpty(queryDTO.getContractCode())
                || CollectionUtil.isNotEmpty(queryDTO.getBillContractCodeList()) || StringUtils.isNotEmpty(queryDTO.getBankAccount())
                || CollectionUtil.isNotEmpty(queryDTO.getContractCodeList())
        ) {
            queryDTO.setIsAccountCodeFlag("1");
        }
        if (CollectionUtil.isNotEmpty(queryDTO.getClientNameList())) {
            //根据名称查询客户编码
            List<String> clientCodeList = iClientService.lambdaQuery().in(ClientEntity::getClientName, queryDTO.getClientNameList()).list().stream().map(ClientEntity::getClientCode).distinct().collect(Collectors.toList());
            queryDTO.setClientCodeList(clientCodeList);
        }
        //构建关联查询
//        MPJLambdaWrapper<VoucherEntity> lambdaWrapper = MPJWrappers.<VoucherEntity>lambdaJoin()
//                .select(VoucherEntity::getVoucherNum, VoucherEntity::getVoucherType, VoucherEntity::getOrgId,
//                        VoucherEntity::getBusinessDate, VoucherEntity::getVoucherDate,
//                        VoucherEntity::getSceneCode, VoucherEntity::getSceneName, VoucherEntity::getCurrency,
//                        VoucherEntity::getVoucherSummary,
//                        VoucherEntity::getCreateUserNo, VoucherEntity::getCreateUserName, VoucherEntity::getRecheckUserNo,
//                        VoucherEntity::getRecheckUserName,VoucherEntity::getVoucherStatus, VoucherEntity::getBatchId, VoucherEntity::getBatchType)
//                .select(VoucherEntryEntity::getId, VoucherEntryEntity::getAccountCode, VoucherEntryEntity::getAccountName,
//                        VoucherEntryEntity::getContractCode, VoucherEntryEntity::getClientCode, VoucherEntryEntity::getClientName,
//                        VoucherEntryEntity::getDebitAmount, VoucherEntryEntity::getCreditAmount,
//                        VoucherEntryEntity::getBankAccount,VoucherEntryEntity::getEditFlag,VoucherEntryEntity::getBillContractCode,VoucherEntryEntity::getCostCentre)
//                .selectAs(VoucherEntryEntity::getVoucherSummary, VoucherDetailDTO::getVoucherEntrySummary)
//                .leftJoin(VoucherEntryEntity.class, VoucherEntryEntity::getVoucherId, VoucherEntity::getId);
//        MPJLambdaWrapper<ManualEntity> manualEntityMPJLambdaWrapperr = MPJWrappers.<ManualEntity>lambdaJoin().select(ManualEntity::getVoucherNum,ManualEntity::getVoucherType,ManualEntity::getOrgId,
//                        ManualEntity::getBusinessDate,ManualEntity::getVoucherDate,
//                        ManualEntity::getSceneCode, ManualEntity::getSceneName, ManualEntity::getCurrencyCode,
//                        ManualEntity::getVoucherSummary,
//                        ManualEntity::getCreateBy).selectAs("", VoucherDetailDTO::getCreateUserName)
//                .selectAs("", VoucherDetailDTO::getRecheckUserNo)
//                .selectAs("", VoucherDetailDTO::getRecheckUserName)
//                .selectAs(ManualEntity::getProcessStatus,VoucherDetailDTO::getVoucherStatus)
//                .selectAs("", VoucherDetailDTO::getBatchId)
//                .selectAs("", VoucherDetailDTO::getBatchType)
//                .select(ManualVoucherEntity::getId, ManualVoucherEntity::getAccountCode, ManualVoucherEntity::getAccountName,
//                        ManualVoucherEntity::getContractCode, ManualVoucherEntity::getClientCode, ManualVoucherEntity::getClientName,
//                        ManualVoucherEntity::getDebitAmount, ManualVoucherEntity::getCreditAmount)
//                .selectAs(ManualVoucherEntity::getBankNo,VoucherDetailDTO::getBankAccount)
//                .selectAs("",VoucherDetailDTO::getEditFlag).selectAs(ManualVoucherEntity::getLoansContractCode,VoucherDetailDTO::getBillContractCode)
//                .select(ManualVoucherEntity::getCostCentre)
//                .selectAs(ManualVoucherEntity::getVoucherSummary,VoucherDetailDTO::getVoucherEntrySummary)
//                .leftJoin(ManualVoucherEntity.class,ManualVoucherEntity::getManualId,ManualEntity::getId);
//
//        //查询条件
//        if (ObjectUtil.isNotNull(queryDTO.getPeriodCode())){
//            lambdaWrapper.eq(VoucherEntity::getPeriodCode, queryDTO.getPeriodCode());
//        }
//        if (ObjectUtil.isNotNull(queryDTO.getBusinessDateStart())){
//            lambdaWrapper.ge(VoucherEntity::getBusinessDate, queryDTO.getBusinessDateStart());
//        }
//        if (ObjectUtil.isNotNull(queryDTO.getBusinessDateEnd())){
//            lambdaWrapper.lt(VoucherEntity::getBusinessDate, LocalDateTimeUtil.offset(queryDTO.getBusinessDateEnd().atStartOfDay(),1, ChronoUnit.DAYS));
//        }
//        if (ObjectUtil.isNotNull(queryDTO.getVoucherDateStart())){
//            lambdaWrapper.ge(VoucherEntity::getVoucherDate, queryDTO.getVoucherDateStart());
//        }
//        if (ObjectUtil.isNotNull(queryDTO.getVoucherDateEnd())){
//            lambdaWrapper.lt(VoucherEntity::getVoucherDate, LocalDateTimeUtil.offset(queryDTO.getVoucherDateEnd().atStartOfDay(),1, ChronoUnit.DAYS));
//        }
//        if (CollectionUtil.isNotEmpty(queryDTO.getProcessStatusList())){
//            lambdaWrapper.in(VoucherEntity::getVoucherStatus, queryDTO.getProcessStatusList());
//        }
//        if (CollectionUtil.isNotEmpty(queryDTO.getVoucherNumList())){
//            lambdaWrapper.in(VoucherEntity::getVoucherNum, queryDTO.getVoucherNumList());
//        }
//        if (CollectionUtil.isNotEmpty(queryDTO.getVoucherTypeList())) {
//            lambdaWrapper.in(VoucherEntity::getVoucherType, queryDTO.getVoucherTypeList());
//        }
//        if (CollectionUtil.isNotEmpty(queryDTO.getOrgIdList())) {
//            lambdaWrapper.in(VoucherEntity::getOrgId, queryDTO.getOrgIdList());
//        }
//        if (CollectionUtil.isNotEmpty(queryDTO.getSubSceneTypeList())) {
//            lambdaWrapper.in(VoucherEntity::getSubSceneType, queryDTO.getSubSceneTypeList());
//        }
//        if (CollectionUtil.isNotEmpty(queryDTO.getSceneCodeList())) {
//            lambdaWrapper.in(VoucherEntity::getSceneCode, queryDTO.getSceneCodeList());
//        }
//        if (CollectionUtil.isNotEmpty(queryDTO.getCurrencyList())) {
//            lambdaWrapper.in(VoucherEntity::getCurrency, queryDTO.getCurrencyList());
//        }
//        if (CollectionUtil.isNotEmpty(queryDTO.getCreateUserNameList())) {
//            lambdaWrapper.in(VoucherEntity::getCreateUserName, queryDTO.getCreateUserNameList());
//        }
//        if (CollectionUtil.isNotEmpty(queryDTO.getRecheckUserNameList())) {
//            lambdaWrapper.in(VoucherEntity::getRecheckUserName, queryDTO.getRecheckUserNameList());
//        }
//        if (CollectionUtil.isNotEmpty(queryDTO.getRecheckUserNameList())) {
//            lambdaWrapper.in(VoucherEntryEntity::getClientCode, queryDTO.getClientCodeList());
//        }
//        if (CollectionUtil.isNotEmpty(queryDTO.getContractCodeList())) {
//            lambdaWrapper.in(VoucherEntity::getContractCode, queryDTO.getContractCodeList());
//        }
//        if (CollectionUtil.isNotEmpty(queryDTO.getBillContractCodeList())) {
//            lambdaWrapper.in(VoucherEntryEntity::getBillContractCode, queryDTO.getBillContractCodeList());
//        }
//        if (CollectionUtil.isNotEmpty(queryDTO.getCostCentreList())) {
//            lambdaWrapper.in(VoucherEntryEntity::getCostCentre, queryDTO.getCostCentreList());
//        }
//        lambdaWrapper.eq(ObjectUtil.isNotNull(queryDTO.getVoucherNum()), VoucherEntity::getVoucherNum, queryDTO.getVoucherNum())
//                .eq(ObjectUtil.isNotNull(queryDTO.getBatchId()), VoucherEntity::getBatchId, queryDTO.getBatchId())
//                .eq(StrUtil.isNotBlank(queryDTO.getBatchType()), VoucherEntity::getBatchType, queryDTO.getBatchType())
//                .eq(StrUtil.isNotBlank(queryDTO.getVoucherType()), VoucherEntity::getVoucherType, queryDTO.getVoucherType())
//                .eq(StrUtil.isNotBlank(queryDTO.getOrgId()), VoucherEntity::getOrgId, queryDTO.getOrgId())
//                .eq(StrUtil.isNotBlank(queryDTO.getSceneCode()), VoucherEntity::getSceneCode, queryDTO.getSceneCode())
//                .eq(StrUtil.isNotBlank(queryDTO.getAccountCode()), VoucherEntryEntity::getAccountCode, queryDTO.getAccountCode())
//                .like(StrUtil.isNotBlank(queryDTO.getAccountName()), VoucherEntryEntity::getAccountName, queryDTO.getAccountName())
//                .like(StrUtil.isNotBlank(queryDTO.getVoucherSummary()), VoucherEntity::getVoucherSummary, queryDTO.getVoucherSummary())
//                .like(StrUtil.isNotBlank(queryDTO.getVoucherEntrySummary()), VoucherEntryEntity::getVoucherSummary, queryDTO.getVoucherEntrySummary())
//                .eq(StrUtil.isNotBlank(queryDTO.getCreateUserName()), VoucherEntity::getCreateUserName, queryDTO.getCreateUserName())
//                .eq(StrUtil.isNotBlank(queryDTO.getRecheckUserName()), VoucherEntity::getRecheckUserName, queryDTO.getRecheckUserName())
//                .like(StrUtil.isNotBlank(queryDTO.getClientName()), VoucherEntity::getClientName, queryDTO.getClientName())
//                .eq(StrUtil.isNotBlank(queryDTO.getContractCode()), VoucherEntity::getContractCode, queryDTO.getContractCode())
//                .eq(StrUtil.isNotBlank(queryDTO.getBankAccount()), VoucherEntryEntity::getBankAccount, queryDTO.getBankAccount())
//                .eq(StrUtil.isNotBlank(queryDTO.getCurrency()), VoucherEntity::getCurrency, queryDTO.getCurrency())
//                .eq(StrUtil.isNotBlank(queryDTO.getSubSceneType()), VoucherEntity::getSubSceneType, queryDTO.getSubSceneType())
//                .eq(ObjectUtil.isNotNull(queryDTO.getPeriodCode()), VoucherEntryEntity::getPeriodCode, queryDTO.getPeriodCode())
//                .in(CollectionUtil.isNotEmpty(queryDTO.getVoucherIdList()), VoucherEntity::getId, queryDTO.getVoucherIdList())
//                .eq(ObjectUtil.isNotNull(queryDTO.getAccountAssistBalanceId()), VoucherEntryEntity::getAccountAssistBalanceId, queryDTO.getAccountAssistBalanceId())
//                .orderByDesc(VoucherEntryEntity::getCreateTime)
//        ;
//        IPage<VoucherDetailDTO> voucherDetailDTOPage =  voucherMapper.selectJoinPage(new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize()), VoucherDetailDTO.class, lambdaWrapper);
        IPage<VoucherDetailDTO> voucherDetailDTOPage = voucherMapper.voucherManualPage(new Page(queryDTO.getPageNum(), queryDTO.getPageSize()), queryDTO);
        //设置客户名称
        Map<String, String> clientCodeMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(voucherDetailDTOPage.getRecords())) {
            List<String> clientCodeLists = voucherDetailDTOPage.getRecords().stream().map(VoucherDetailDTO::getClientCode).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(clientCodeLists)) {
                clientCodeMap = iClientService.lambdaQuery().in(ClientEntity::getClientCode, clientCodeLists).list().stream().collect(HashMap::new, (map, item) -> map.put(item.getClientCode(), item.getClientName()), HashMap::putAll);
            }
        }
        Map<String, String> finalClientCodeMap = clientCodeMap;
        String staffCode = UserUtils.getStaffCode();
        String parth = getFileStoragePath();
        String password = UserUtils.getKindeeLoginPassword(parth);
        log.info("password:" + password);
        voucherDetailDTOPage.getRecords().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getClientCode()) && finalClientCodeMap.containsKey(v.getClientCode())) {
                v.setClientName(finalClientCodeMap.get(v.getClientCode()));
            }
            if (StringUtils.isNotEmpty(v.getEasVoucherId())) {
                v.setEasLoginUrl(String.format(easLoginUrl, staffCode, v.getEasVoucherId(), password));
            }
        });
        return voucherDetailDTOPage;
    }

    @Override
    public Boolean deleteByIdList(List<Long> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            return Boolean.TRUE;
        }
        this.removeBatchByIds(idList);
        //删除voucher_entry表
        List<Long> entryIdList = voucherEntryService.lambdaQuery().in(VoucherEntryEntity::getVoucherId, idList).list().stream().map(VoucherEntryEntity::getId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(entryIdList)) {
            voucherEntryService.removeBatchByIds(entryIdList);
        }

        // 删除balance临时表数据
        int delDataCount = contractBalanceTempService.delBalanceTempByVoucherId(idList);
        if (delDataCount > 0) {
            return Boolean.TRUE;
        }

        // 更新balance和latest
        updateBalanceByVoucher(idList);
        return Boolean.TRUE;
    }

    private void updateBalanceByVoucher(List<Long> idList) {
        // 取得contract_balance表信息
        LambdaQueryWrapper<ContractBalanceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ContractBalanceEntity::getVoucherId, idList).
                eq(ContractBalanceEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        List<Map<String, Object>> contractBalanceEntityList = iContractBalanceService.listMaps(wrapper);

        // 删除contract_balance表
        iContractBalanceService.lambdaUpdate().set(ContractBalanceEntity::getDelFlag, "1").
                in(ContractBalanceEntity::getVoucherId, idList).
                eq(ContractBalanceEntity::getDelFlag, YesOrNoEnum.NO.getCode()).update();

        // 删除余额表信息
        Map<String, Map<String, Object>> contractBalanceEntityListMap = new HashMap<>();
        if (contractBalanceEntityList != null && !contractBalanceEntityList.isEmpty()) {
            // 汇总发生额
            for (Map<String, Object> rowMap : contractBalanceEntityList) {
                String businesskey = this.createContractLatestKey(rowMap);
                Map<String, Object> contractBalanceTotalMap = contractBalanceEntityListMap.get(businesskey);
                if (contractBalanceTotalMap == null) {
                    contractBalanceTotalMap = new HashMap<>();
                    contractBalanceTotalMap.putAll(rowMap);
                    // 将不需要的信息删除
                    contractBalanceTotalMap.put("voucher_id", null);
                    contractBalanceTotalMap.put("interface_data_id", null);
                    contractBalanceTotalMap.put("business_date", LocalDateTime.now());
                    contractBalanceTotalMap.put("voucher_date", null);
                    contractBalanceTotalMap.put("update_time", LocalDateTime.now());
                    contractBalanceTotalMap.put("update_by", UserUtils.getStaffCode());
                    contractBalanceEntityListMap.put(businesskey, contractBalanceTotalMap);
                } else {
                    // 发生额求和
                    Iterator<Map.Entry<String, Object>> it = contractBalanceTotalMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, Object> entry = it.next();
                        if (entry.getKey().endsWith("_amount")) {
                            BigDecimal balanceAmountTotal = new BigDecimal(entry.getValue().toString());
                            BigDecimal balanceAmount = new BigDecimal(rowMap.get(entry.getKey()).toString());
                            contractBalanceTotalMap.put(entry.getKey(), NumberUtil.add(balanceAmountTotal, balanceAmount));
                        }
                    }
                }
            }

            // 更新latest余额信息
            if (!contractBalanceEntityListMap.isEmpty()) {
                for (String key : contractBalanceEntityListMap.keySet()) {
                    // 取得lastest信息
                    String[] keyArray = key.split("\\|");
                    Map<String, Object> contractBalanceLatestMap = null;
//                    if (keyArray.length == 2) {
//                        contractBalanceLatestMap = iContractBalanceLatestService.getLastBalanceMap(keyArray[1],
//                                null, keyArray[0],null,null);
//                    } else {
//                        contractBalanceLatestMap = iContractBalanceLatestService.getLastBalanceMap(keyArray[1],
//                                keyArray[2], keyArray[0],null,null);
//                    }
                    contractBalanceLatestMap = iContractBalanceLatestService.getLastBalanceMap(keyArray[1],
                            keyArray[2], keyArray[0], keyArray[3], keyArray[4]);

                    // 取得发生额信息
                    Map<String, Object> contractBalanceTotalMap = contractBalanceEntityListMap.get(key);

                    // 更新latest表的余额
                    for (String lastestKey : contractBalanceLatestMap.keySet()) {
                        if (key.endsWith("_balance")) {
                            BigDecimal balanceAmountTotal = new BigDecimal(contractBalanceTotalMap.get(lastestKey).toString());
                            BigDecimal balanceAmount = new BigDecimal(contractBalanceLatestMap.get(key).toString());
                            balanceAmount = NumberUtil.add(balanceAmountTotal.multiply(new BigDecimal(-1)), balanceAmount);
                            contractBalanceLatestMap.put(key, balanceAmount);
                            contractBalanceLatestMap.put(key.replace("_balance", "_amount"), balanceAmountTotal);

                            contractBalanceTotalMap.put(key, balanceAmount);
                        }
                    }
                    // 更新余额表
                    iContractBalanceLatestService.updateLastBalanceMap(contractBalanceLatestMap);
                    // 保存balance发生额
                    iContractBalanceService.insertMap(contractBalanceTotalMap);
                    // 删除balance临时表的数据
                    contractBalanceTempService.delBalanceTeamData(contractBalanceTotalMap);
                }
            }
        }
    }

    private String createContractLatestKey(Map<String, Object> rowMap) {
        StringBuffer key = new StringBuffer();
        if (null != rowMap.get(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode())) {
            key = key.append(rowMap.get(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode()));
        } else {
            key = key.append("null");
        }
        key = key.append("|").append(rowMap.get(ContractBalanceColumnsEnum.BUSINESS_CODE.getCode()).toString());

        if (rowMap.get(ContractBalanceColumnsEnum.CLIENT_CODE.getCode()) != null) {
            key = key.append("|").append(rowMap.get(ContractBalanceColumnsEnum.CLIENT_CODE.getCode()));
        } else {
            key = key.append("|").append("null");
        }
        if (rowMap.get(ContractBalanceColumnsEnum.ORG_ID.getCode()) != null) {
            key = key.append("|").append(rowMap.get(ContractBalanceColumnsEnum.ORG_ID.getCode()));
        } else {
            key = key.append("|").append("null");
        }
        if (rowMap.get(ContractBalanceColumnsEnum.BILL_CONTRACT_CODE.getCode()) != null) {
            key = key.append("|").append(rowMap.get(ContractBalanceColumnsEnum.BILL_CONTRACT_CODE.getCode()));
        } else {
            key = key.append("|").append("null");
        }
        return key.toString();
    }

    public void saveLastBalance(List<Long> idList) {
        //删除最新余额表，如果删除的最新余额表数据存在，则需要重新从余额表中拿到最新的重新保存
        List<ContractBalanceLatestEntity> balanceLatestEntityList = iContractBalanceLatestService.lambdaQuery().in(ContractBalanceLatestEntity::getVoucherId, idList).list();
        Map<String, List<ContractBalanceLatestEntity>> keyMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(balanceLatestEntityList)) {
            //按照业务编码，合同编码，客户编码 分组
            keyMap = balanceLatestEntityList.stream().collect(Collectors.groupingBy(v -> v.getBusinessCode() + "-" + v.getContractCode() + "-" + v.getClientCode()));
            iContractBalanceLatestService.removeBatchByIds(balanceLatestEntityList.stream().map(ContractBalanceLatestEntity::getId).collect(Collectors.toList()));
        }
        if (MapUtil.isNotEmpty(keyMap)) {
            for (Map.Entry<String, List<ContractBalanceLatestEntity>> entry : keyMap.entrySet()) {
                //获取最新的余额表数据
                ContractBalanceLatestEntity contractBalanceLatestEntity = entry.getValue().get(0);
                VoucherDTO voucherDTO = BeanUtil.copyProperties(contractBalanceLatestEntity, VoucherDTO.class);
                Map<String, Object> newMap = iContractBalanceService.getOriginalLastBalanceMap(contractBalanceLatestEntity.getBusinessCode(), contractBalanceLatestEntity.getClientCode(), contractBalanceLatestEntity.getContractCode());
                //保存最新的余额
                iContractBalanceLatestService.insertMap(voucherDTO, newMap);
            }
        }
    }


    private VoucherEntrySaveDTO mappingVoucherEntry(SceneVoucherEntryDTO sceneVoucherEntryDTO,
                                                    SceneVoucherConditionDTO sceneVoucherConditionDTO,
                                                    InterfaceDataDTO interfaceDataDTO,
                                                    VoucherSaveDTO voucherSaveDTO) {
        VoucherEntrySaveDTO entrySaveDTO = new VoucherEntrySaveDTO();
        entrySaveDTO.setFundType(sceneVoucherEntryDTO.getFundType());
        entrySaveDTO.setRelateBankFlag(sceneVoucherEntryDTO.getRelateBankFlag());
        String entryBankAccount = sceneVoucherEntryDTO.getBankAccount();
        entrySaveDTO.setBankAccount(StringUtils.isNotEmpty(entryBankAccount) ? entryBankAccount : interfaceDataDTO.getBankNo());
        entrySaveDTO.setCashAttribute(sceneVoucherEntryDTO.getCashAttribute());
        entrySaveDTO.setVoucherSummary(sceneVoucherEntryDTO.getVoucherSummary());
        entrySaveDTO.setAssistFlags(sceneVoucherEntryDTO.getAssistFlags());
        BigDecimal voucherAmount = StringUtils.isNotEmpty(sceneVoucherConditionDTO.getScriptAmount()) ? new BigDecimal(sceneVoucherConditionDTO.getScriptAmount()) : BigDecimal.ZERO;
        if (DRCREnum.DR.getCode().equals(sceneVoucherConditionDTO.getDebitCreditType())) {
            entrySaveDTO.setDebitAmount(voucherAmount);
        } else if (DRCREnum.CR.getCode().equals(sceneVoucherConditionDTO.getDebitCreditType())) {
            entrySaveDTO.setCreditAmount(voucherAmount);
        }
        if (AssistFlagUtil.hasActualClientFlag(sceneVoucherEntryDTO.getAssistFlags())) {
            if (StringUtils.isEmpty(interfaceDataDTO.getActualClientCode())) {
                voucherSaveDTO.setValidFlag(VoucherValidFlagEnum.NO_VALID.getCode());
            }
            entrySaveDTO.setClientCode(interfaceDataDTO.getActualClientCode());
        }
        if (AssistFlagUtil.hasClientFlag(sceneVoucherEntryDTO.getAssistFlags())) {
            if (StringUtils.isEmpty(interfaceDataDTO.getClientCode())) {
                voucherSaveDTO.setValidFlag(VoucherValidFlagEnum.NO_VALID.getCode());
            }
            entrySaveDTO.setClientCode(interfaceDataDTO.getClientCode());
            entrySaveDTO.setClientName(interfaceDataDTO.getClientName());
            entrySaveDTO.setClientFlag("1");
        } else {
            entrySaveDTO.setClientFlag("0");
        }
        if (AssistFlagUtil.hasContractFlag(sceneVoucherEntryDTO.getAssistFlags())) {
            if (StringUtils.isEmpty(interfaceDataDTO.getContractCode())) {
                voucherSaveDTO.setValidFlag(VoucherValidFlagEnum.NO_VALID.getCode());
            }
            entrySaveDTO.setContractCode(interfaceDataDTO.getContractCode());
            entrySaveDTO.setContractName(interfaceDataDTO.getContractName());
            entrySaveDTO.setContractFlag("1");
        } else {
            entrySaveDTO.setContractFlag("0");
        }
        if (AssistFlagUtil.hasBillContractFlag(sceneVoucherEntryDTO.getAssistFlags())) {
            if (StringUtils.isEmpty(interfaceDataDTO.getBillContractCode())) {
                voucherSaveDTO.setValidFlag(VoucherValidFlagEnum.NO_VALID.getCode());
            }
            entrySaveDTO.setBillContractCode(interfaceDataDTO.getBillContractCode());
        }
        entrySaveDTO.setDebitCreditType(sceneVoucherConditionDTO.getDebitCreditType());
        // bank_deposits is the historical cash-type code; bank_deposit is the
        // current Kingdee-synchronised dictionary code. Both must resolve the
        // detail subject from the actual bank account instead of eg_account.
        if ("bank_deposits".equals(entrySaveDTO.getFundType())
                || "bank_deposit".equals(entrySaveDTO.getFundType())) {
            setAccountCodeAndName(entrySaveDTO, entrySaveDTO.getBankAccount(), sceneVoucherEntryDTO.getFundType());
        } else {
            //查询科目编码及科目名称
            String fundType = sceneVoucherEntryDTO.getFundType();
            AccountDTO accountDTO;
            if (SceneEnum.HTQZ.getCode().equals(interfaceDataDTO.getSceneCode())) {
                // HTQZ closes the configuration loop in this order:
                // amount type -> derived accounting business -> exact subject.
                accountDTO = accountService.getAccountByFundTypeStrict(
                        fundType, interfaceDataDTO.getAccountingBusinessCode());
            } else {
                String accountBusinessCode = StringUtils.isNotEmpty(interfaceDataDTO.getAccountingBusinessCode())
                        ? interfaceDataDTO.getAccountingBusinessCode() : interfaceDataDTO.getBusinessCode();
                accountDTO = accountService.getAccountByFundType(accountBusinessCode, fundType);
            }
            entrySaveDTO.setAccountCode(accountDTO.getAccountCode());
            entrySaveDTO.setAccountName(accountDTO.getAccountName());
        }
        return entrySaveDTO;
    }

    @Override
    public long generateVoucherNum(String voucherType, LocalDateTime dateTime) {
        try {
            // ========== 使用新的本地号段预分配方案 ==========
            return localSegmentVoucherGenerator.generateVoucherNum(voucherType, dateTime);
        }catch (Exception e){
            log.error("==>>VoucherServiceImpl.generateVoucherNum==>>error:{}",e.getMessage());
            //redis key : finhub-年月-凭证类型编码
            String key = CacheConstants.COMMON_PREFIX+"_voucher_num_"+dateTime.getYear()+dateTime.getMonthValue()+"_"+voucherType;
            return redisService.generate(key, REDIS_VOUCHER_NUM_EXPIRE);
        }
    }

    public void setAccountCodeAndName(VoucherEntrySaveDTO entrySaveDTO, String bankNo, String fundType) {
        if (StringUtils.isEmpty(bankNo)) {
            throw new ServiceException("银行账号[bankNo]不能为空");
        }
        List<BankAccountEntity> bankAccountEntityList = iBankAccountService.lambdaQuery()
                .eq(BankAccountEntity::getBankAccountNumber, bankNo)
                .eq(BankAccountEntity::getDelFlag, YesOrNoEnum.NO.getCode())
                .list();
        if (CollectionUtil.isNotEmpty(bankAccountEntityList)) {
            entrySaveDTO.setAccountCode(bankAccountEntityList.get(0).getAccountCode());
            entrySaveDTO.setAccountName(bankAccountEntityList.get(0).getAccountName());
            return;
        }
        throw new ServiceException("银行账号未同步会计科目: " + bankNo);
    }

    /**
     * 批量更新凭证的审核状态
     */
    public void updateStatusByids(List<String> ids, String status, String recheckUserNo, String recheckUserName) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        List<Long> idsLong = ids.stream().map(e -> Long.valueOf(e)).collect(Collectors.toList());
        LambdaUpdateWrapper<VoucherEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(VoucherEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        updateWrapper.in(VoucherEntity::getId, idsLong);
        updateWrapper.set(VoucherEntity::getVoucherStatus, status);
        updateWrapper.set(VoucherEntity::getRecheckUserNo, recheckUserNo);
        updateWrapper.set(VoucherEntity::getRecheckUserName, recheckUserName);
        this.update(updateWrapper);

        // 更新余额信息
        List<Long> voucherIds = new ArrayList<>();
        if (ProcessStatusEnum.REJECTED.getCode().equals(status)) {
            for (String id : ids) {
                voucherIds.add(Long.valueOf(id));
            }
            this.updateBalanceByVoucher(voucherIds);
        }
    }

    @Override
    public IPage<VoucherVO> voucherNumberPage(VoucherQueryDTO queryDTO) {
        //这里注入查询条件
        return voucherMapper.voucherNumberPage(new Page<VoucherVO>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryDTO);
    }

    @Override
    public List<VoucherDetailExportDTO> selectAllVoucerDetails(VoucherQueryDTO queryDTO) {
        //如果勾选了科目代码或者科目名称需要查出其下该会计期间的所有凭证号
        //如果勾选了科目代码或者科目名称需要查出其下该会计期间的所有凭证号,凭证头摘要、科目代码、凭证行摘要、合同编号、客户名称、借款合同编号、银行账号
        if (CollectionUtil.isNotEmpty(queryDTO.getAccountCodeList()) || CollectionUtil.isNotEmpty(queryDTO.getAccountNameList())
                || StringUtils.isNotEmpty(queryDTO.getVoucherEntrySummary()) || StringUtils.isNotEmpty(queryDTO.getContractCode())
                || CollectionUtil.isNotEmpty(queryDTO.getBillContractCodeList()) || StringUtils.isNotEmpty(queryDTO.getBankAccount())
                || CollectionUtil.isNotEmpty(queryDTO.getContractCodeList())
        ) {
            queryDTO.setIsAccountCodeFlag("1");
        }
        if (CollectionUtil.isNotEmpty(queryDTO.getClientNameList())) {
            //根据名称查询客户编码
            List<String> clientCodeList = iClientService.lambdaQuery().in(ClientEntity::getClientName, queryDTO.getClientNameList()).list().stream().map(ClientEntity::getClientCode).distinct().collect(Collectors.toList());
            queryDTO.setClientCodeList(clientCodeList);
        }
        return voucherMapper.selectAllVoucerDetails(queryDTO);
    }

    @Override
    public Boolean deleteByBatchIdList(List<Long> batchIdList, String batchType) {
        List<VoucherEntity> voucherEntityList = this.lambdaQuery().in(VoucherEntity::getBatchId, batchIdList).eq(VoucherEntity::getBatchType, batchType).list();
        if (CollectionUtil.isNotEmpty(voucherEntityList)) {
            deleteByIdList(voucherEntityList.stream().map(VoucherEntity::getId).distinct().collect(Collectors.toList()));
        }
        return Boolean.TRUE;
    }

    /**
     * 提交凭证
     * @param voucherIdList
     */
    @Override
    public void commitVoucherList(List<Long> voucherIdList) {
        List<VoucherEntity> voucherEntityList = listByIds(voucherIdList);
        if (CollectionUtil.isEmpty(voucherEntityList)) {
            throw new ServiceException("未查询到凭证");
        }
        List<VoucherDTO> voucherDTOS = BeanUtil.copyToList(voucherEntityList, VoucherDTO.class);
        //更新凭证状态
        LambdaUpdateWrapper<VoucherEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(VoucherEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        updateWrapper.in(VoucherEntity::getId, voucherIdList);
        updateWrapper.set(VoucherEntity::getVoucherStatus, ProcessStatusEnum.SUBMITTED.getCode());
        this.update(updateWrapper);

        for (VoucherDTO voucherDTO : voucherDTOS) {
            voucherDTO.setIsSubmit(YesOrNoEnum.YES.getCode());
            // 查询凭证行信息
            List<VoucherEntryEntity> voucherEntryEntities = voucherEntryService.selectByVoucherId(voucherDTO.getId());
            if (CollectionUtil.isEmpty(voucherEntryEntities)) {
                log.info("==>>VoucherServiceImpl.commitVoucherList==>>voucherDTO.getId():{}", voucherDTO.getId());
                throw new ServiceException("未查询到凭证行:" + voucherDTO.getId());
            }
            // 存在科目为9999.99的则不能提交
            voucherEntryEntities.stream().forEach(a -> {
                if (ObjectUtil.equals(a.getAccountCode(), DefaultConstant.EDIT_FLAG_ACCOUNT_CODE)) {
                    throw new ServiceException("凭证号[" + voucherDTO.getVoucherNum() + "]存在科目代码为" + DefaultConstant.EDIT_FLAG_ACCOUNT_CODE + "的凭证行，不能提交");
                }
            });
            voucherDTO.setEntryList(BeanUtil.copyToList(voucherEntryEntities, VoucherEntryDTO.class));
            // 更新合同余额
            iContractBalanceService.saveContractBalanceFromVoucher(voucherDTO);
        }
    }

    /**
     * 查询应付保险费相关凭证金额
     *
     * @param voucherQueryDTO
     * @return
     */
    @Override
    public List<VoucherDetailDTO> getInsuranceAmount(VoucherQueryDTO voucherQueryDTO) {
        List<VoucherDetailDTO> list = baseMapper.getInsuranceAmount(voucherQueryDTO);
        return list;
    }

    public String getCurrency(String currency) {
        List<CurrencyEntity> currencyEntityList = queryRedisAllForCurrency();
        Map<String, String> currencyMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(currencyEntityList)) {
            currencyMap = currencyEntityList.stream().collect(HashMap::new, (map, item) -> map.put(item.getCurrenctName(), item.getCurrencyCode()), HashMap::putAll);
        }
        currencyMap.put(Constants.CNY, Constants.RMB);
        if (currencyMap.containsKey(currency)) {
            currency = currencyMap.get(currency);
        }
        if (StringUtils.isEmpty(currency)) {
            currency = Constants.RMB;
        }
        return currency;
    }

    public List<CurrencyEntity> queryRedisAllForCurrency() {
        List<CurrencyEntity> currencyList = redisService.getCacheObject(RedisConstant.V_ALL_CURRENCY);
        if (CollectionUtil.isEmpty(currencyList)) {
            currencyList = iCurrencyService.list();
            if (CollectionUtil.isNotEmpty(currencyList)) {
                redisService.setCacheObject(RedisConstant.V_ALL_CURRENCY, currencyList, RedisConstant.TIME_OUT, TimeUnit.MINUTES);
            }
        }
        return currencyList;
    }


    /**
     * 根据批次id和批次类型获取凭证
     * @param batchIdList
     * @param batchType
     * @return
     */
    @Override
    public List<VoucherVO> getByBatchIdList(List<Long> batchIdList, String batchType) {
        List<VoucherEntity> voucherEntityList = this.lambdaQuery().in(VoucherEntity::getBatchId, batchIdList).eq(VoucherEntity::getBatchType, batchType).list();
        List<VoucherVO> voucherVOList = BeanUtil.copyToList(voucherEntityList, VoucherVO.class);
        return voucherVOList;
    }

    @Override
    public IPage<VoucherDetailDTO> summaryByPage(VoucherQueryDTO queryDTO) {
        return voucherMapper.summaryByPage(new Page(queryDTO.getPageNum(), queryDTO.getPageSize()), queryDTO);
    }

    @Override
    public String getClientCodeByClientName(String clientName) {
        return voucherMapper.getClientCodeByClientName(clientName);
    }

    @Override
    public List<VoucherEntity> selectVoucherEntity(VoucherQueryDTO queryDTO) {
        return voucherMapper.selectVoucherEntity(queryDTO);
    }

    @Override
    public void updateStatusByBatch(List<Long> batchIds, String batchType, String status, String userNo, String userName) {
        if (CollectionUtil.isEmpty(batchIds)) {
            return;
        }
        this.lambdaUpdate().set(VoucherEntity::getVoucherStatus, status)
                .set((ProcessStatusEnum.REVIEWED.getCode().equals(status) || ProcessStatusEnum.REJECTED.getCode().equals(status)), VoucherEntity::getRecheckUserNo, userNo)
                .set((ProcessStatusEnum.REVIEWED.getCode().equals(status) || ProcessStatusEnum.REJECTED.getCode().equals(status)), VoucherEntity::getRecheckUserName, userName)
                .in(VoucherEntity::getBatchId, batchIds)
                .eq(StringUtils.isNotBlank(batchType), VoucherEntity::getBatchType, batchType).update();
        writeOffVoucher(batchIds, batchType, status, userNo, userName);
    }

    @Override
    public void writeOffVoucher(List<Long> batchIdList, String batchType, String status, String userNo, String userName) {
        if (ProcessStatusEnum.REVIEWED.getCode().equals(status)) {
            return;
        }
        //拿到所有的凭证分录信息
        List<VoucherEntity> voucherEntityList = this.lambdaQuery().in(VoucherEntity::getBatchId, batchIdList).eq(VoucherEntity::getBatchType, batchType).list();
        if (CollectionUtil.isEmpty(voucherEntityList)) {
            return;
        }
        List<Long> idList = voucherEntityList.stream().map(VoucherEntity::getId).distinct().collect(Collectors.toList());
        ;
        List<VoucherEntryEntity> entryEntityList = voucherEntryService.lambdaQuery().in(VoucherEntryEntity::getVoucherId, idList).list();
        Map<Long, List<VoucherEntryEntity>> entryMap = entryEntityList.stream().collect(Collectors.groupingBy(VoucherEntryEntity::getVoucherId));
        for (VoucherEntity entity : voucherEntityList) {
            VoucherDTO voucherDTO = BeanUtil.copyProperties(entity, VoucherDTO.class);
            voucherDTO.setVoucherStatus(status);
            if (ProcessStatusEnum.REVIEWED.getCode().equals(status) || ProcessStatusEnum.REJECTED.getCode().equals(status)) {
                voucherDTO.setRecheckUserName(userName);
                voucherDTO.setRecheckUserNo(userNo);
            }
            entryMap.get(entity.getId()).forEach(e -> {
                e.setDebitAmount((null == e.getDebitAmount() ? BigDecimal.ZERO : e.getDebitAmount()).multiply(new BigDecimal(-1)));
                e.setCreditAmount((null == e.getCreditAmount() ? BigDecimal.ZERO : e.getCreditAmount()).multiply(new BigDecimal(-1)));
            });
            List<VoucherEntryDTO> entryDTOList = BeanUtil.copyToList(entryMap.get(entity.getId()), VoucherEntryDTO.class);
            voucherDTO.setEntryList(entryDTOList);
            iContractBalanceService.saveMonualContractBalanceFromVoucher(voucherDTO);
        }
    }

    public void setIsSummary(VoucherEntity voucherEntity, VoucherSaveDTO voucherDTO) {
      /*  中台生成的凭证符合任意一项时，该凭证不合并传至金蝶
        1.凭证头币种不为人民币时；
        2.凭证分录含2221.01.01进项税科目时；
        3.凭证分录含2221.01.05销项税科目时；
        4.凭证分录含【是否涉及其他客户及辅助帐】为是时
       */
        if (CollectionUtil.isEmpty(voucherDTO.getEntryList())) {
            return;
        }
        long count = voucherDTO.getEntryList().stream().filter(v -> Constants.INPUT_TAX_ACCOUNT_CODE.equals(v.getAccountCode())
                || Constants.OUT_TAX_ACCOUNT_CODE.equals(v.getAccountCode())
                || YesOrNoEnum.YES.getCode().equals(v.getIsRelatedOtherCustomer())).count();
        String isSummary = YesOrNoEnum.NO.getCode();//汇总
        if (!CurrencyTypeEnum.CNY.getCode().equals(voucherEntity.getCurrency())) {
            isSummary = YesOrNoEnum.YES.getCode();
        } else if (0 != count) {
            isSummary = YesOrNoEnum.YES.getCode();
            ;
        }
        voucherEntity.setIsSummary(isSummary);

    }

    public void setClientCode(VoucherEntrySaveDTO entrySaveDTO, InterfaceDataDTO interfaceDataDTO, String sceneCode, String subSceneCodeType) {
        /**
         * 1.场景为JYJGBG，且合同表租赁类型为直租，科目为2202.01.01或2202.01.02时，分录上的client_code改为按合同+签约主体查最新余额表客户类别为供应商的client_code；
         * 2.场景为ZLSK/ZLHL，细分场景为3，且合同表租赁类型为直租，科目为2202.01.01或2202.01.02时，分录上的client_code改为按合同+签约主体查最新余额表客户类别为供应商的client_code；
         * 3.场景为SDFP，科目为2202.02.01时，分录上的client_code改为按合同+签约主体查合同表的client_code，且分录上的摘要 {收到发票接口表.合同编号}+'-'+{收到发票接口表.客户编号}+'-'+'收到应付其他租赁成本发票' 里客户编号也改为按合同+签约主体查合同表的client_code；
         * 4.场景为SQBXFFP，科目为2701.03.01时，分录上的client_code改为按合同+签约主体查合同表的client_code，且分录上的摘要 {收到发票接口表.合同编号}+'-'+{收到发票接口表.客户编号}+'-'+'收到应付其他租赁成本发票' 里客户编号也改为按合同+签约主体查合同表的client_code；
         * 5.场景为SCF，科目为2202.20.01/2202.13.01/2202.21.01时，分录上的client_code改为按合同+签约主体查合同表的client_code；
         * 6.场景为SHSBK，科目为2202.18.01时，分录上的client_code改为按合同+签约主体查合同表的client_code；
         * 7.场景为ZLSK，科目为2701.04.01时，分录上的client_code改为按合同+签约主体查最新余额表客户类别为供应商的client_code；
         * 8.场景为WYLSSK，科目为1121时，分录上的client_code根据clientName查eg_client的clientCode
         * 8.场景为TACFL，科目为1121.01和1531.02时，分录上的client_code取网银到账主体的clientCode
         */
        List<String> scfAccoundCodeList = Lists.newArrayList("2202.20.01", "2202.13.01", "2202.21.01");
        List<String> jyjgbgAccoundCodeList = Lists.newArrayList("2202.01.01", "2202.01.02");
        String clientCode = "";
        log.info("interfaceDataJson:{}", interfaceDataDTO);
        if ((SceneEnum.SDFP.getCode().equals(sceneCode) && "2202.02.01".equals(entrySaveDTO.getAccountCode()))
                || (SceneEnum.SQBXFFP.getCode().equals(sceneCode) && "2701.03.01".equals(entrySaveDTO.getAccountCode()))
                || (SceneEnum.SCF.getCode().equals(sceneCode) && scfAccoundCodeList.contains(entrySaveDTO.getAccountCode()))
                || (SceneEnum.SHSBK.getCode().equals(sceneCode) && "2202.18.01".equals(entrySaveDTO.getAccountCode()))
        ) {
            clientCode = interfaceDataDTO.getContractClientCode();
//            String voucherSummary = entrySaveDTO.getVoucherSummary();
//            if (SceneEnum.SQBXFFP.getCode().equals(sceneCode) || SceneEnum.SCF.getCode().equals(sceneCode)) {
//                voucherSummary = interfaceDataDTO.getContractCode()+"-"+clientCode+"-收到应付其他租赁成本发票";
//            }
//            entrySaveDTO.setVoucherSummary(voucherSummary);
            entrySaveDTO.setClientCode(clientCode);
        } else if (SceneEnum.JYJGBG.getCode().equals(sceneCode)
                && jyjgbgAccoundCodeList.contains(entrySaveDTO.getAccountCode())
                && LeaseTypeEnum.DIRECT.getCode().equals(interfaceDataDTO.getContractLeaseType())
        ) {
            clientCode = getLatstClientCode(interfaceDataDTO.getOrgId(), interfaceDataDTO.getContractCode(), null);
            log.info("满足第一个条件：{}", clientCode);
            if (StringUtils.isEmpty(clientCode)) {
                clientCode = interfaceDataDTO.getClientCode();
            }
            entrySaveDTO.setClientCode(clientCode);
        } else if ((SceneEnum.ZLSK.getCode().equals(sceneCode)
                || SceneEnum.ZLHL.getCode().equals(sceneCode))
                && "3".equals(subSceneCodeType)
                && LeaseTypeEnum.DIRECT.getCode().equals(interfaceDataDTO.getContractLeaseType())
                && jyjgbgAccoundCodeList.contains(entrySaveDTO.getAccountCode())) {
            clientCode = getLatstClientCode(interfaceDataDTO.getOrgId(), interfaceDataDTO.getContractCode(), null);
            log.info("满足第二个条件：{}", clientCode);
            if (StringUtils.isEmpty(clientCode)) {
                clientCode = interfaceDataDTO.getClientCode();
            }
            entrySaveDTO.setClientCode(clientCode);
        } else if (SceneEnum.ZLSK.getCode().equals(sceneCode) && "2701.04.01".equals(entrySaveDTO.getAccountCode())) {
            String deductionMarginClient = interfaceDataDTO.getInterfaceData().getString("deductionMarginClient");
            if (StringUtils.isNotEmpty(deductionMarginClient)) {
                entrySaveDTO.setClientCode(deductionMarginClient);
            } else {
                clientCode = getLatstClientCode(interfaceDataDTO.getOrgId(), interfaceDataDTO.getContractCode(), interfaceDataDTO.getVendorPoolType());
                if (StringUtils.isEmpty(clientCode)) {
                    entrySaveDTO.setClientCode(interfaceDataDTO.getClientCode());
                } else {
                    entrySaveDTO.setClientCode(clientCode);
                }
            }
        } else if (SceneEnum.WYLSSK.getCode().equals(sceneCode) && "1121".equals(entrySaveDTO.getAccountCode())) {
            clientCode = iClientService.selectClientCodeByName(interfaceDataDTO.getClientName());
            entrySaveDTO.setClientCode(clientCode);
        } else if (SceneEnum.TACFL.getCode().equals(sceneCode) && "1221.01".equals(entrySaveDTO.getAccountCode())) {
            String bankOrgIdClientCode = interfaceDataDTO.getInterfaceData().getString("bankOrgIdClientCode");
            entrySaveDTO.setClientCode(bankOrgIdClientCode);
        }
    }

    public String getLatstClientCode(String orgId, String contractCode, String clientType) {
        if (StringUtils.isEmpty(clientType)) {
            clientType = FinanceEngineEnum.XwClientType.SU.getValue();
        }
        ContractBalanceLatestEntity latestEntity = iContractBalanceLatestService.lambdaQuery()
                .eq(ContractBalanceLatestEntity::getContractCode, contractCode)
                .eq(ContractBalanceLatestEntity::getOrgId, orgId)
                .eq(ContractBalanceLatestEntity::getClientType, clientType)
                .orderByDesc(ContractBalanceLatestEntity::getId).last("limit 1").one();
        if (null != latestEntity) {
            return latestEntity.getClientCode();
        }
        return "";
    }

    private String getFileStoragePath() {
        String operationSystemName = System.getProperties().getProperty("os.name");
        if (operationSystemName.toLowerCase().indexOf(Constants.OPERATION_SYSTEM_NAME_WINDOWS) > -1) {
            return easFilePathForWindows;
        } else if (operationSystemName.toLowerCase().indexOf(Constants.OPERATION_SYSTEM_NAME_LINUX) > -1
                || operationSystemName.toLowerCase().indexOf(Constants.OPERATION_SYSTEM_NAME_UNIX) > -1) {
            return easFilePathForLinux;
        }
        return StringUtil.EMPTY;
    }

    @Override
    public Boolean writeOff(List<VoucherCopyDTO> copyDTOList) {
        //按照类型非组
        if (CollectionUtil.isEmpty(copyDTOList)) {
            throw new ServiceException("请至少勾选一条数据冲销");
        }
        List<Long> idList = copyDTOList.stream().filter(v -> YesOrNoEnum.NO.getCode().equals(v.getSourceFromType())).map(VoucherCopyDTO::getVoucherId).distinct().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条已复核的数据冲销");
        }
        generateManualInfo(copyDTOList, true);
        //冲销之后修改记录为已冲销
        this.lambdaUpdate().set(VoucherEntity::getIsWriteOff, YesOrNoEnum.YES.getCode()).in(VoucherEntity::getId, idList).update();
        return Boolean.TRUE;
    }

    @Override
    public Boolean copy(List<VoucherCopyDTO> copyDTOList) {
        if (CollectionUtil.isEmpty(copyDTOList)) {
            throw new ServiceException("请至少勾选一条数据复制");
        }
        generateManualInfo(copyDTOList, false);
        return Boolean.TRUE;
    }

    @Override
    public void updateStatusBatchByIds(List<String> voucherIds, String voucherStatus, Integer periodCode) {
        LambdaUpdateWrapper<VoucherEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(VoucherEntity::getId,voucherIds);
        updateWrapper.in(VoucherEntity::getPeriodCode,periodCode);
        this.update(VoucherEntity.builder().voucherStatus(voucherStatus).build(),updateWrapper);
    }

    @Override
    public List<VoucherExportVo> summaryExport(VoucherQueryDTO queryDTO) {
        List<VoucherExportVo> voucherExportVos = voucherMapper.summaryExport(queryDTO);
        List<SysDictData> sysDictDataList = remoteDictService.listDictData(DictTypeEnum.SYS_VOUCHER_TYPE.getCode()).getData();
        Map<String, String> voucherTypeMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(sysDictDataList)) {
            voucherTypeMap = sysDictDataList.stream().collect(HashMap::new, (map, item) -> map.put(item.getDictValue(), item.getDictLabel()), HashMap::putAll);
        }
        // 细分场景编码
        List<SysDictData> subSceneDictDataList = remoteDictService.listDictData(DictTypeEnum.SYS_SUB_SCENE_TYPE.getCode()).getData();
        Map<String, String> subSceneTypeMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(subSceneDictDataList)) {
            subSceneTypeMap = subSceneDictDataList.stream().collect(HashMap::new, (map, item) -> map.put(item.getDictValue(), item.getDictLabel()), HashMap::putAll);
        }
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId, OrgCompanyVO::getOrgName, (a, b) -> b));
        for (VoucherExportVo voucherExportVo : voucherExportVos) {
            voucherExportVo.setVoucherType(voucherTypeMap.get(voucherExportVo.getVoucherType()));
            if (StringUtils.isNotEmpty(SceneEnum.getDescByCode(voucherExportVo.getSceneName()))){
                voucherExportVo.setSceneName(SceneEnum.getDescByCode(voucherExportVo.getSceneName()));
            }
            if (subSceneTypeMap.get(voucherExportVo.getSubSceneType()) != null) {
                voucherExportVo.setSubSceneType(subSceneTypeMap.get(voucherExportVo.getSubSceneType()));
            }
            if (FundCurrencyTypeEnum.getEnumByType(voucherExportVo.getCurrency()) != null) {
                voucherExportVo.setCurrency(FundCurrencyTypeEnum.getEnumByType(voucherExportVo.getCurrency()).getDesc());
            }
            if (FundCurrencyTypeEnum.getEnumByCode(voucherExportVo.getCurrency()) != null) {
                voucherExportVo.setCurrency(FundCurrencyTypeEnum.getEnumByCode(voucherExportVo.getCurrency()).getDesc());
            }
            voucherExportVo.setVoucherStatus(ProcessStatusEnum.getDescByCode(voucherExportVo.getVoucherStatus()));
            voucherExportVo.setOrgName(companyMap.get(voucherExportVo.getOrgId()));
        }
        return voucherExportVos;
    }

    public void generateManualInfo(List<VoucherCopyDTO> copyDTOList, boolean isWriteOff) {
        Map<String, List<Long>> copyMap = copyDTOList.stream().collect(Collectors.groupingBy(VoucherCopyDTO::getSourceFromType, Collectors.mapping(VoucherCopyDTO::getVoucherId, Collectors.toList())));
        for (Map.Entry<String, List<Long>> entry : copyMap.entrySet()) {
            if (YesOrNoEnum.NO.getCode().equals(entry.getKey())) {
                handleVoucherData(entry.getValue().stream().distinct().collect(Collectors.toList()), isWriteOff);
            } else {
                handleManual(entry.getValue().stream().distinct().collect(Collectors.toList()), isWriteOff);
            }
        }
    }

    //手工凭证只有复制没有冲销
    public void handleManual(List<Long> idList, boolean isWriteOff) {
        iManualService.copy(idList);
    }

    public void handleVoucherData(List<Long> idList, boolean isWriteOff) {
        List<VoucherEntity> voucherEntityList = this.lambdaQuery().in(VoucherEntity::getId, idList).eq(VoucherEntity::getDelFlag, "0").list();
        List<VoucherEntryEntity> voucherEntryEntityList = voucherEntryService.lambdaQuery().in(VoucherEntryEntity::getVoucherId, idList).eq(VoucherEntryEntity::getDelFlag, "0").list();
        Map<Long, List<VoucherEntryEntity>> entryMap = voucherEntryEntityList.stream().collect(Collectors.groupingBy(VoucherEntryEntity::getVoucherId));
        String userNo = UserUtils.getStaffCode();
        String userName = UserUtils.getStaffName();
        for (VoucherEntity entity : voucherEntityList) {
            if (isWriteOff) {
                if (!ProcessStatusEnum.REVIEWED.getCode().equals(entity.getVoucherStatus())) {
                    throw new ServiceException("只有凭证状态为已复核的才可以冲销");
                }
                if (YesOrNoEnum.YES.getCode().equals(entity.getIsWriteOff())) {
                    throw new ServiceException("存在已冲销的数据不可以进行冲销");
                }
            }
            List<ManualVoucherEntity> manualVoucherEntityList = Lists.newArrayList();
            ManualEntity manualEntity = BeanUtil.copyProperties(entity, ManualEntity.class);
            manualEntity.setId(IdWorker.getId());
            manualEntity.setPeriodCode(PeriodCodeUtil.periodCodeByLocalDateTime(LocalDateTime.now()));
            manualEntity.setBusinessDate(LocalDate.now().atStartOfDay());
            manualEntity.setVoucherDate(LocalDate.now().atStartOfDay());
            manualEntity.setCurrencyCode(entity.getCurrency());
            manualEntity.setVoucherNum(generateVoucherNum(entity.getVoucherType(), manualEntity.getVoucherDate()));
            manualEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            manualEntity.setUpdateTime(LocalDateTime.now());
            manualEntity.setCreateTime(LocalDateTime.now());
            manualEntity.setCreateBy(userNo);
            manualEntity.setUpdateBy(userNo);
            manualEntity.setCreateUserName(userName);
            manualEntity.setIsWriteOff(YesOrNoEnum.NO.getCode());
            manualEntity.setSourceFrom(ManualSourceFrom.PZ.getCode());
            manualEntity.setSourceId(entity.getId());
            manualEntity.setVoucherSummary(entity.getVoucherSummary());
            //冲销原凭证记账日期-原凭证号-原凭证凭证头摘要
            String voucherSummary = "冲销" + DateUtil.format(entity.getVoucherDate(), "yyyy-MM-dd") + "-" + entity.getVoucherNum() + "-";
            if (isWriteOff) {
                manualEntity.setVoucherSummary(voucherSummary + entity.getVoucherSummary());
            }
            manualEntity.setRecheckUserName("");
            manualEntity.setRecheckUserNo("");
            //复制凭证分录
            if (!entryMap.containsKey(entity.getId())) {
                continue;
            }
            iManualService.save(manualEntity);
            //获取客户名称
            List<VoucherEntryEntity> entryEntityList = entryMap.get(entity.getId());
            Map<String, String> clientCodeMap = Maps.newHashMap();
            if (CollectionUtil.isNotEmpty(entryEntityList)) {
                List<VoucherEntryEntity> clientCodeListEntity = entryEntityList.stream().
                        filter(v -> StringUtils.isEmpty(v.getClientName()) && StringUtils.isNotEmpty(v.getClientCode())).collect(Collectors.toList());
                if (clientCodeListEntity != null && !clientCodeListEntity.isEmpty()) {
                    clientCodeMap = iClientService.lambdaQuery().in(ClientEntity::getClientCode, clientCodeListEntity.stream().
                                    map(VoucherEntryEntity::getClientCode).distinct().collect(Collectors.toList())).list().stream().
                            collect(HashMap::new, (map, item) -> map.put(item.getClientCode(), item.getClientName()), HashMap::putAll);
                }
            }
            for (VoucherEntryEntity entryEntity : entryEntityList) {
                ManualVoucherEntity voucherEntity = BeanUtil.copyProperties(entryEntity, ManualVoucherEntity.class);
                voucherEntity.setManualId(manualEntity.getId());
                voucherEntity.setId(IdWorker.getId());
                voucherEntity.setVoucherType(manualEntity.getVoucherType());
                voucherEntity.setBusinessDate(manualEntity.getBusinessDate());
                voucherEntity.setPeriodCode(manualEntity.getPeriodCode());
                voucherEntity.setCurrencyCode(manualEntity.getCurrencyCode());
                voucherEntity.setOrgId(manualEntity.getOrgId());
                voucherEntity.setVoucherDate(manualEntity.getVoucherDate());
                voucherEntity.setSceneCode(manualEntity.getSceneCode());
                voucherEntity.setCreateBy(entity.getCreateBy());
                voucherEntity.setCreateTime(LocalDateTime.now());
                voucherEntity.setUpdateBy(entity.getUpdateBy());
                voucherEntity.setUpdateTime(LocalDateTime.now());
                voucherEntity.setClientCode(entryEntity.getClientCode());
                voucherEntity.setVoucherSummary(entryEntity.getVoucherSummary());
                voucherEntity.setSubsidiaryAccount(entryEntity.getVoucherSummary());
                if (isWriteOff) {
                    //冲销原凭证记账日期-原凭证号-原凭证凭证行摘要
                    voucherEntity.setVoucherSummary(voucherSummary + entryEntity.getVoucherSummary());
                    voucherEntity.setSubsidiaryAccount(voucherSummary + entryEntity.getVoucherSummary());
                    if (ObjectUtil.isNotNull(entryEntity.getCreditAmount())) {
                        voucherEntity.setCreditAmount(entryEntity.getCreditAmount().multiply(new BigDecimal(-1)));
                    }
                    if (ObjectUtil.isNotNull(entryEntity.getDebitAmount())) {
                        voucherEntity.setDebitAmount(entryEntity.getDebitAmount().multiply(new BigDecimal(-1)));
                    }
                }
                if (StringUtils.isEmpty(voucherEntity.getClientName())
                        && StringUtils.isNotEmpty(voucherEntity.getClientCode())
                        && clientCodeMap.containsKey(voucherEntity.getClientCode())) {
                    voucherEntity.setClientName(clientCodeMap.get(voucherEntity.getClientCode()));
                }
                manualVoucherEntityList.add(voucherEntity);
            }
            iManualVoucherService.saveBatch(manualVoucherEntityList);
        }
    }

    /**

     * @description: 手工凭证撤回-仅供手工凭证撤回使用
     * @author: zhangli.chen
     * @date 2025/11/11 15:55
     * @param batchIds
     * @param batchType
     * @param status
     * @param userNo
     * @param userName
     * @return void
     */
    @Override
    public void withdrawOnlyForManualVoucher(List<Long> batchIds, String batchType, String status, String userNo, String userName) {
        if (CollectionUtil.isEmpty(batchIds) || ProcessStatusEnum.REVIEWED.getCode().equals(status)) {
            return;
        }
        // 查询所有的凭证信息
        List<VoucherEntity> voucherEntityList = this.lambdaQuery().in(VoucherEntity::getBatchId,batchIds)
                .eq(VoucherEntity::getBatchType,batchType)
                .eq(VoucherEntity::getDelFlag,YesOrNoEnum.NO.getCode()).list();
        if (CollectionUtil.isEmpty(voucherEntityList)) {
            return;
        }
        // 获取凭证头ID
        List<Long> idList = voucherEntityList.stream().map(VoucherEntity::getId).distinct().collect(Collectors.toList());
        // 获取凭证期间
        List<Integer> periodList = voucherEntityList.stream().map(VoucherEntity::getPeriodCode).distinct().collect(Collectors.toList());
        // 查询所有的分录信息
        List<VoucherEntryEntity> entryEntityList = voucherEntryService.lambdaQuery()
                .in(VoucherEntryEntity::getVoucherId,idList)
                .eq(VoucherEntryEntity::getDelFlag,YesOrNoEnum.NO.getCode())
                .in(VoucherEntryEntity::getPeriodCode,periodList).list();
        Map<Long,List<VoucherEntryEntity>> entryMap = entryEntityList.stream().collect(Collectors.groupingBy(VoucherEntryEntity::getVoucherId));
        for (VoucherEntity entity : voucherEntityList) {
            VoucherDTO voucherDTO = BeanUtil.copyProperties(entity,VoucherDTO.class);
            voucherDTO.setVoucherStatus(status);
            // add by zhangli.chen for 针对手工凭证撤销需要更新余额信息 on 20251111
            voucherDTO.setIsSubmit(YesOrNoEnum.YES.getCode());
            entryMap.get(entity.getId()).forEach(e -> {
                e.setDebitAmount((null==e.getDebitAmount()?BigDecimal.ZERO:e.getDebitAmount()).multiply(new BigDecimal(-1)));
                e.setCreditAmount((null==e.getCreditAmount()?BigDecimal.ZERO:e.getCreditAmount()).multiply(new BigDecimal(-1)));
            });
            List<VoucherEntryDTO> entryDTOList = BeanUtil.copyToList(entryMap.get(entity.getId()),VoucherEntryDTO.class);
            voucherDTO.setEntryList(entryDTOList);
            iContractBalanceService.saveMonualContractBalanceFromVoucher(voucherDTO);
        }
        // 删除凭证以及余额数据
        if (CollectionUtil.isNotEmpty(idList)) {
            // 1、删除提交时生成的凭证头表数据
            //this.removeBatchByIds(idList);
            if (CollectionUtil.isNotEmpty(periodList)){
                if(periodList.size() >= FinanceEngineEnum.Numbers.TWO.getKey()){
                    this.lambdaUpdate().set(VoucherEntity::getDelFlag, YesOrNoEnum.YES.getCode()).
                            in(VoucherEntity::getId,idList).eq(VoucherEntity::getDelFlag,YesOrNoEnum.NO.getCode()).
                            in(VoucherEntity::getPeriodCode,periodList).update();
                    //  2、删除分录表数据
//                    voucherEntryService.removeBatchByIds(entryIdList);
//                    List<Long> entryIdList = voucherEntryService.lambdaQuery().in(VoucherEntryEntity::getVoucherId, idList)
//                                    .in(VoucherEntryEntity::getPeriodCode,periodList).list().stream().map(VoucherEntryEntity::getId).collect(Collectors.toList());
//                    if (CollectionUtil.isNotEmpty(entryIdList)) {
//                        voucherEntryService.lambdaUpdate().set(VoucherEntryEntity::getDelFlag,YesOrNoEnum.YES.getCode()).
//                                in(VoucherEntryEntity::getId, entryIdList).eq(VoucherEntryEntity::getDelFlag,YesOrNoEnum.NO.getCode())
//                                .in(VoucherEntryEntity::getPeriodCode,periodList).update();
//                    }
                    voucherEntryService.lambdaUpdate().set(VoucherEntryEntity::getDelFlag,YesOrNoEnum.YES.getCode()).
                            in(VoucherEntryEntity::getVoucherId, idList).eq(VoucherEntryEntity::getDelFlag,YesOrNoEnum.NO.getCode())
                            .in(VoucherEntryEntity::getPeriodCode,periodList).update();
                }else{
                    this.lambdaUpdate().set(VoucherEntity::getDelFlag, YesOrNoEnum.YES.getCode()).
                            in(VoucherEntity::getId,idList).eq(VoucherEntity::getDelFlag,YesOrNoEnum.NO.getCode()).
                            eq(VoucherEntity::getPeriodCode,periodList.get(FinanceEngineEnum.Numbers.ZERO.getKey())).update();
                    //  2、删除分录表数据
                    voucherEntryService.lambdaUpdate().set(VoucherEntryEntity::getDelFlag,YesOrNoEnum.YES.getCode()).
                            in(VoucherEntryEntity::getVoucherId, idList).eq(VoucherEntryEntity::getDelFlag,YesOrNoEnum.NO.getCode())
                            .eq(VoucherEntryEntity::getPeriodCode,periodList.get(FinanceEngineEnum.Numbers.ZERO.getKey())).update();

                }
            }
            // 3、删除balance临时表数据
            contractBalanceTempService.delBalanceTempByVoucherId(idList);
            // 4、更新余额表状态
//            iContractBalanceService.lambdaUpdate().set(ContractBalanceEntity::getDelFlag, FinanceEngineEnum.Numbers.ONE.getValue()).
//                    in(ContractBalanceEntity::getVoucherId,idList).
//                    eq(ContractBalanceEntity::getDelFlag, YesOrNoEnum.NO.getCode()).update();
        }
    }
}
