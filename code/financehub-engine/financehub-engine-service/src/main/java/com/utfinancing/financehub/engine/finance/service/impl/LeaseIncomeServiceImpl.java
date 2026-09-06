package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.engine.approve.mapper.ApproveMapper;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.dw.service.IDwsBzHetjyjgxxDService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.ContractBalanceLatestMapper;
import com.utfinancing.financehub.engine.finance.mapper.LeaseIncomeDetailsMapper;
import com.utfinancing.financehub.engine.finance.mapper.LeaseIncomeMapper;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanTempMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeDetailsVO;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import com.utfinancing.financehub.engine.scene.service.ISceneFieldsService;
import com.utfinancing.financehub.engine.scene.service.ISceneVoucherEntryService;
import com.utfinancing.financehub.engine.utils.UserUtils;
import com.utfinancing.financehub.etl.api.KingdeeDataSyncFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-10
 * @Description :  LeaseIncome服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class LeaseIncomeServiceImpl extends ServiceImpl<LeaseIncomeMapper, LeaseIncomeEntity> implements ILeaseIncomeService {

    public static final int MAX_NUM_PAMAXRTITIONS = 20;
    public static final int EACH_THREAD_PROCESS_NUM = 200;

    public static final int EACH_GEN_VOUCHER_NUM = 200;
    private final LeaseIncomeMapper leaseIncomeMapper;
    private final ILeaseIncomeDetailsService detailsService;
    private final LeaseIncomeDetailsMapper detailsMapper;
    private final IContractService contractService;
    private final IContractMonthService contractMonthService;
    private final IRuleService iRuleService;
    private final IRepaymentPlanService repaymentPlanService;
    private final RemoteDictService remoteDictService;
    private final RepaymentPlanTempMapper repaymentPlanTempMapper;
    private final IRepaymentPlanTempService repaymentPlanTempService;
    private final ContractBalanceLatestMapper contractBalanceLatestMapper;

    private final IContractTaAmountService contractTaAmountService;

    private final IRepaymentPlanProvisionService repaymentPlanProvisionService;

    private final IVoucherService voucherService;

    private final IDwsBzHetjyjgxxDService dwsBzHetjyjgxxDService;

    private final IApproveService approveService;

    private final ApproveMapper approveMapper;

    private final RedisService redisService;

    private final ISceneVoucherEntryService sceneVoucherEntryService;

    private final IContractBalanceLatestService contractBalanceLatestService;

    private final IClientService clientService;

    private final ISceneFieldsService sceneFieldsService;

    private final IAccountService accountService;

    private final ICurrencyService currencyService;

    private final IOutstandingAmountInitService outstandingAmountInitService;

    private final KingdeeDataSyncFacade kingdeeDataSyncFacade;

    private final IBusinessClaimRepaymentRecordService businessClaimRepaymentRecordService;

    private final ILeaseIncomeUploadRecordService leaseIncomeUploadRecordService;

    @Value("${approve.url.leaseIncome-url:/measurementEngine/incomeProvisionDetail?leaseIncomeId=}")
    private String approveUrl;

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    private final ILeaseIncomeNewTransactionService leaseIncomeNewTransactionService;

    @Override
    public Long saveLeaseIncome(LeaseIncomeDTO dto) {
        LeaseIncomeEntity entity = BeanUtil.copyProperties(dto, LeaseIncomeEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateLeaseIncome(Long id, LeaseIncomeDTO dto) {
        LeaseIncomeEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public LeaseIncomeDTO getLeaseIncomeDTOById(Long id) {
        LeaseIncomeEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, LeaseIncomeDTO.class);
    }

    @Override
    public IPage<LeaseIncomeVO> selectPage(LeaseIncomeQueryDTO queryDTO) {
        LambdaQueryWrapper<LeaseIncomeEntity> queryWrapper = Wrappers.<LeaseIncomeEntity>lambdaQuery();
        Date queryDate = queryDTO.getBusinessDate();
        if (null != queryDate) {
            queryDate = DateUtil.endOfDay(DateUtil.endOfMonth(queryDate));
            queryWrapper.eq(LeaseIncomeEntity::getBusinessDate, queryDate);
        }
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), LeaseIncomeEntity::getOrgId, queryDTO.getOrgIdList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList()), LeaseIncomeEntity::getProcessStatus, queryDTO.getProcessStatusList());
        queryWrapper.eq(LeaseIncomeEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        if (queryDTO.getSystemCodeList() != null && !queryDTO.getSystemCodeList().isEmpty()) {
            queryWrapper.in(LeaseIncomeEntity::getSystemCode, queryDTO.getSystemCodeList());
        }
        queryWrapper.orderByDesc(LeaseIncomeEntity::getUpdateTime);
        IPage<LeaseIncomeEntity> entityIPage = leaseIncomeMapper.selectPage(new Page<LeaseIncomeEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        IPage<LeaseIncomeVO> page = ListBeanUtil.copyPage(entityIPage, LeaseIncomeVO.class);
        page.getRecords().forEach(v -> {
            v.setBatchType(BatchTypeEnum.SYJT.getCode());
            v.setSystemName(SystemEnum.getDescByCode(v.getSystemCode()));
        });
        return page;
    }

    @Override
    public IPage<LeaseIncomeDetailsVO> selectDetailPage(LeaseIncomeDetailsQueryDTO queryDTO) {
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
        // 特殊合同状态
        if (YesOrNoEnum.YES.getCode().equals(queryDTO.getSpecialContractFlag())) {
            R<List<SysDictData>> list = listDictTypeData(DictTypeEnum.ACCRUAL_FINANCIAL_CONTRACT_STATUS_LIST.getCode());
            if (queryDTO.getFinancialContractStatusList() == null || queryDTO.getFinancialContractStatusList().isEmpty()) {
                queryDTO.setFinancialContractStatusList(new ArrayList<>());
            }
            List<String> spcialContractStatusList = list.getData().stream().map(SysDictData::getDictValue).collect(Collectors.toList());
            queryDTO.getFinancialContractStatusList().addAll(spcialContractStatusList);
        } else if (YesOrNoEnum.NO.getCode().equals(queryDTO.getSpecialContractFlag())) {
            if (queryDTO.getFinancialContractStatusList() == null || queryDTO.getFinancialContractStatusList().isEmpty()) {
                queryDTO.setFinancialContractStatusList(new ArrayList<>());
            }
            queryDTO.getFinancialContractStatusList().addAll(this.getNoSpcialContractStatusList());
        }

        // abs赎回
        if (YesOrNoEnum.YES.getCode().equals(queryDTO.getAbsMark())) {
            R<List<SysDictData>> list = listDictTypeData(DictTypeEnum.ACCRUAL_ABS_LIST.getCode());
            if (queryDTO.getFinancialContractStatusList() == null || queryDTO.getFinancialContractStatusList().isEmpty()) {
                queryDTO.setFinancialContractStatusList(new ArrayList<>());
            }
            List<String> absMarkList = list.getData().stream().map(SysDictData::getDictValue).collect(Collectors.toList());
            queryDTO.getFinancialContractStatusList().addAll(absMarkList);
        } else if (YesOrNoEnum.NO.getCode().equals(queryDTO.getAbsMark())) {
            if (queryDTO.getFinancialContractStatusList() == null || queryDTO.getFinancialContractStatusList().isEmpty()) {
                queryDTO.setFinancialContractStatusList(new ArrayList<>());
            }
            queryDTO.getFinancialContractStatusList().addAll(this.getNoABSMarkContractStatusList());
        }

        // 还款情况
        if (StringUtils.isNotEmpty(queryDTO.getRepaymentFlag())) {
            Date lastThirtyDay = DateUtil.offsetMonth(new Date(), -30);
            queryDTO.setLastRepaymentDate(lastThirtyDay);
        }

        Page<LeaseIncomeDetailsVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<LeaseIncomeDetailsVO> records1 = detailsMapper.selectLeaseIncomeInfo(page, queryDTO);

        List<CurrencyEntity> currencyR = currencyService.getBaseMapper().selectList(new LambdaQueryWrapper<>());
        Map<String, String> currencyMap = currencyR.stream().collect(Collectors.toMap(e -> e.getCurrencyCode(), e -> e.getCurrenctName()));

        R<List<SysDictData>> businessCategoryR = remoteDictService.listDictData(DictTypeEnum.BUSINESS_CATEGORY.getCode());
        Map<String, String> businessCategoryMap = businessCategoryR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));

        R<List<SysDictData>> companyR = remoteDictService.listDictData(DictTypeEnum.COMPANY.getCode());
        Map<String, String> companyMap = companyR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));

        records1.forEach(e -> {
            e.setCurrencyType(currencyMap.get(e.getCurrencyType()));
            e.setBusinessType(businessCategoryMap.get(e.getBusinessType()));
            e.setOrgName(companyMap.get(e.getOrgId()));
            e.setSystemName(SystemEnum.getDescByCode(e.getSystemCode()));
            if (StringUtils.isNotEmpty(e.getLeaseDateStart())) {
                e.setLeaseDateStart(DateUtils.format(DateUtils.parseDate(e.getLeaseDateStart()), "yyyy-MM-dd"));
            }
            if (StringUtils.isNotEmpty(e.getLeaseDateEnd())) {
                e.setLeaseDateEnd(DateUtils.format(DateUtils.parseDate(e.getLeaseDateEnd()), "yyyy-MM-dd"));
            }
            if (StringUtils.isNotEmpty(e.getPreviousPaidPeriod())) {
                e.setPreviousPaidPeriod(DateUtils.format(DateUtils.parseDate(e.getPreviousPaidPeriod()), "yyyy-MM-dd"));
            }
            if (StringUtils.isNotEmpty(e.getObservedExpirationDate())) {
                e.setObservedExpirationDate(DateUtils.format(DateUtils.parseDate(e.getObservedExpirationDate()), "yyyy-MM-dd"));
            }
            if (StringUtils.isNotEmpty(e.getLastRepaymentDate())) {
                e.setLastRepaymentDate(DateUtils.format(DateUtils.parseDate(e.getLastRepaymentDate()), "yyyy-MM-dd"));
            }
            if (StringUtils.isNotEmpty(e.getNextRepaymentDate())) {
                e.setNextRepaymentDate(DateUtils.format(DateUtils.parseDate(e.getNextRepaymentDate()), "yyyy-MM-dd"));
            }
            if (StringUtils.isNotEmpty(e.getObserved())) {
                e.setObserved(YesOrNoEnum.getDescByCode(e.getObserved()));
            }
            if (StringUtils.isNotEmpty(e.getIncomeProvisionMethod())) {
                e.setIncomeProvisionMethod(AccrualMethodEnum.getDescByCode(e.getIncomeProvisionMethod()));
            }
            e.setAccrued(YesOrNoEnum.YES.getDesc());
            if (StringUtils.isNotEmpty(e.getHistoryOverdue())) {
                String[] historyOverdueArray = e.getHistoryOverdue().split("\\|");
                Map<String, String> historyOverdueMap = Arrays.stream(historyOverdueArray).
                        collect(Collectors.toMap(k -> k.split(":")[0], k -> k.split(":")[1]));
                Date businessDate = DateUtils.parseDate(e.getBusinessDate());
                String oneMonthBefore = DateUtils.format(DateUtil.offsetMonth(businessDate, -1), "yyyyMM");
                String twoMonthBefore = DateUtils.format(DateUtil.offsetMonth(businessDate, -2), "yyyyMM");
                String threeMonthBefore = DateUtils.format(DateUtil.offsetMonth(businessDate, -3), "yyyyMM");

                if (historyOverdueMap.containsKey(oneMonthBefore)) {
                    e.setPreviousOneMonthOverdueDays(Integer.parseInt(historyOverdueMap.get(oneMonthBefore)));
                } else {
                    e.setPreviousOneMonthOverdueDays(0);
                }
                if (historyOverdueMap.containsKey(twoMonthBefore)) {
                    e.setPreviousTwoMonthOverdueDays(Integer.parseInt(historyOverdueMap.get(twoMonthBefore)));
                } else {
                    e.setPreviousTwoMonthOverdueDays(0);
                }
                if (historyOverdueMap.containsKey(threeMonthBefore)) {
                    e.setPreviousThreeMonthOverdueDays(Integer.parseInt(historyOverdueMap.get(threeMonthBefore)));
                } else {
                    e.setPreviousThreeMonthOverdueDays(0);
                }
            }
            e.setBusinessDate(DateUtils.format(DateUtils.parseDate(e.getBusinessDate()), "yyyy-MM"));
            e.setPaidHandlingFeeAndOtherIncome(Optional.ofNullable(e.getPaidHandlingFees()).orElse(BigDecimal.ZERO).add(Optional.ofNullable(e.getOtherIncome()).orElse(BigDecimal.ZERO)));
        });
        page.setRecords(records1);
        return page;
    }

    /**
     * 取得特殊合同状态除外的其它状态
     */
    private List<String> getNoSpcialContractStatusList() {
        R<List<SysDictData>> absMarkContractStatusList = listDictTypeData(DictTypeEnum.ACCRUAL_ABS_LIST.getCode());
        R<List<SysDictData>> allContractStatusList = listDictTypeData(DictTypeEnum.FINANCIAL_CONTRACT_STATUS.getCode());

        Map<String, String> absMarkContractStatusMap = absMarkContractStatusList.getData().stream().collect(Collectors.toMap(SysDictData::getDictLabel,
                SysDictData::getDictValue));
        List<String> paramList = new ArrayList<>();
        for (SysDictData sysDictData : allContractStatusList.getData()) {
            String dictValue = absMarkContractStatusMap.get(sysDictData.getDictLabel());
            if (StringUtils.isEmpty(dictValue)) {
                paramList.add(sysDictData.getDictValue());
            }
        }
        return paramList;
    }

    /**
     * 取得非ABS赎回财务合同状态列表
     */
    private List<String> getNoABSMarkContractStatusList() {
        R<List<SysDictData>> spcialContractStatusList = listDictTypeData(DictTypeEnum.ACCRUAL_FINANCIAL_CONTRACT_STATUS_LIST.getCode());
        R<List<SysDictData>> allContractStatusList = listDictTypeData(DictTypeEnum.FINANCIAL_CONTRACT_STATUS.getCode());

        Map<String, String> spcialContractStatusMap = spcialContractStatusList.getData().stream().collect(Collectors.toMap(SysDictData::getDictLabel,
                SysDictData::getDictValue));
        List<String> paramList = new ArrayList<>();
        for (SysDictData sysDictData : allContractStatusList.getData()) {
            String dictValue = spcialContractStatusMap.get(sysDictData.getDictLabel());
            if (StringUtils.isEmpty(dictValue)) {
                paramList.add(sysDictData.getDictValue());
            }
        }
        return paramList;
    }

    /**
     * 字典数据
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
    public List<LeaseIncomeDetailsVO> selectDetailList(LeaseIncomeDetailsQueryDTO queryDTO) {

        Page<LeaseIncomeDetailsVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<LeaseIncomeDetailsVO> records1 = detailsMapper.selectLeaseIncomeInfo(page, queryDTO);

        // 签约主体
        R<List<SysDictData>> companyList = listDictTypeData(DictTypeEnum.COMPANY.getCode());
        Map<String, String> companyMap = companyList.getData().stream().collect(Collectors.toMap(
                SysDictData::getDictValue, (e) -> e.getDictLabel()));

        List<CurrencyEntity> currencyR = currencyService.getBaseMapper().selectList(new LambdaQueryWrapper<>());
        Map<String, String> currencyMap = currencyR.stream().collect(
                Collectors.toMap(e -> e.getCurrencyCode(), e -> e.getCurrenctName()));

        R<List<SysDictData>> businessCategoryR = remoteDictService.listDictData(DictTypeEnum.BUSINESS_CATEGORY.getCode());
        Map<String, String> businessCategoryMap = businessCategoryR.getData().stream().collect(
                Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));

        records1.forEach(e -> {
            e.setCurrencyType(currencyMap.get(e.getCurrencyType()));
            e.setBusinessType(businessCategoryMap.get(e.getBusinessType()));
            e.setOrgName(companyMap.get(e.getOrgId()));
            e.setSystemName(SystemEnum.getDescByCode(e.getSystemCode()));
            if (StringUtils.isNotEmpty(e.getLeaseDateStart())) {
                e.setLeaseDateStart(DateUtils.format(DateUtils.parseDate(e.getLeaseDateStart()), "yyyy-MM-dd"));
            }
            if (StringUtils.isNotEmpty(e.getLeaseDateEnd())) {
                e.setLeaseDateEnd(DateUtils.format(DateUtils.parseDate(e.getLeaseDateEnd()), "yyyy-MM-dd"));
            }
            if (StringUtils.isNotEmpty(e.getPreviousPaidPeriod())) {
                e.setPreviousPaidPeriod(DateUtils.format(DateUtils.parseDate(e.getPreviousPaidPeriod()), "yyyy-MM-dd"));
            }
            if (StringUtils.isNotEmpty(e.getObservedExpirationDate())) {
                e.setObservedExpirationDate(DateUtils.format(DateUtils.parseDate(e.getObservedExpirationDate()), "yyyy-MM-dd"));
            }
            if (StringUtils.isNotEmpty(e.getLastRepaymentDate())) {
                e.setLastRepaymentDate(DateUtils.format(DateUtils.parseDate(e.getLastRepaymentDate()), "yyyy-MM-dd"));
            }
            if (StringUtils.isNotEmpty(e.getNextRepaymentDate())) {
                e.setNextRepaymentDate(DateUtils.format(DateUtils.parseDate(e.getNextRepaymentDate()), "yyyy-MM-dd"));
            }
            if (StringUtils.isNotEmpty(e.getObserved())) {
                e.setObserved(YesOrNoEnum.getDescByCode(e.getObserved()));
            }
            if (StringUtils.isNotEmpty(e.getIncomeProvisionMethod())) {
                e.setIncomeProvisionMethod(AccrualMethodEnum.getDescByCode(e.getIncomeProvisionMethod()));
            }
            e.setAccrued(YesOrNoEnum.YES.getDesc());
            if (StringUtils.isNotEmpty(e.getHistoryOverdue())) {
                String[] historyOverdueArray = e.getHistoryOverdue().split("\\|");
                Map<String, String> historyOverdueMap = Arrays.stream(historyOverdueArray).
                        collect(Collectors.toMap(k -> k.split(":")[0], k -> k.split(":")[1]));
                Date businessDate = DateUtils.parseDate(e.getBusinessDate());
                String oneMonthBefore = DateUtils.format(DateUtil.offsetMonth(businessDate, -1), "yyyyMM");
                String twoMonthBefore = DateUtils.format(DateUtil.offsetMonth(businessDate, -2), "yyyyMM");
                String threeMonthBefore = DateUtils.format(DateUtil.offsetMonth(businessDate, -3), "yyyyMM");

                if (historyOverdueMap.containsKey(oneMonthBefore)) {
                    e.setPreviousOneMonthOverdueDays(Integer.parseInt(historyOverdueMap.get(oneMonthBefore)));
                } else {
                    e.setPreviousOneMonthOverdueDays(0);
                }
                if (historyOverdueMap.containsKey(twoMonthBefore)) {
                    e.setPreviousTwoMonthOverdueDays(Integer.parseInt(historyOverdueMap.get(twoMonthBefore)));
                } else {
                    e.setPreviousTwoMonthOverdueDays(0);
                }
                if (historyOverdueMap.containsKey(threeMonthBefore)) {
                    e.setPreviousThreeMonthOverdueDays(Integer.parseInt(historyOverdueMap.get(threeMonthBefore)));
                } else {
                    e.setPreviousThreeMonthOverdueDays(0);
                }
            }
            e.setBusinessDate(DateUtils.format(DateUtils.parseDate(e.getBusinessDate()), "yyyy-MM"));
        });

        return records1;
    }

    @Override
    public List<RepaymentPlanVO> selectDetailPlanList(LeaseIncomeDetailsQueryDTO queryDTO) {
        String contractCode = queryDTO.getContractCode();
        if (StringUtils.isBlank(contractCode)) {
            return new ArrayList<>();
        }

        // 优先查询临时计提产生的偿还计划，如果不存在的情况下，查询原偿还计划
        List<RepaymentPlanProvisionEntity> repaymentPlanProvisionEntityList = repaymentPlanProvisionService.
                selectByContractCode(contractCode);
        if (repaymentPlanProvisionEntityList != null && !repaymentPlanProvisionEntityList.isEmpty()) {
            List<RepaymentPlanVO> result = ListBeanUtil.copyList(repaymentPlanProvisionEntityList, RepaymentPlanVO.class);
            result.stream().forEach(e -> {
                e.setRecaptureStatus(RecaptureStatusEnum.getDescByCode(e.getRecaptureStatus()));
            });
            return result;
        }

        List<RepaymentPlanVO> repaymentPlanVOS = repaymentPlanService.selectByContractCode(contractCode);
        if (repaymentPlanVOS != null && repaymentPlanVOS.isEmpty()) {
            repaymentPlanVOS.stream().forEach(e -> {
                e.setRecaptureStatus(RecaptureStatusEnum.getDescByCode(e.getRecaptureStatus()));
            });
        }
        return repaymentPlanVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条收益计提数据删除");
        }
        List<LeaseIncomeEntity> entities = getBaseMapper().selectBatchIds(ids);
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        List<LeaseIncomeEntity> cannotDelete = entities.stream()
                .filter(entity -> !MarginStatusEnum.canChangeStatus().contains(entity.getProcessStatus()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(cannotDelete)) {
            String statuses = cannotDelete.stream()
                    .map(LeaseIncomeEntity::getProcessStatus)
                    .map(MarginStatusEnum::getDescByCode)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.joining("、"));
            throw new ServiceException("仅未录入、已录入或复核失败的收益计提可以删除，当前状态：" + statuses);
        }

        List<Long> deleteIds = entities.stream().map(LeaseIncomeEntity::getId).collect(Collectors.toList());
        // 必须先按明细中记录的凭证主键删除凭证，再删除明细，避免留下孤儿凭证。
        delVoucherByIds(deleteIds);
        detailsService.getBaseMapper().delete(Wrappers.<LeaseIncomeDetailsEntity>lambdaQuery()
                .in(LeaseIncomeDetailsEntity::getLeaseIncomeId, deleteIds));
        approveMapper.physicalDeleteByDocuments(deleteIds, BatchTypeEnum.SYJT.getCode());
        getBaseMapper().deleteBatchIds(deleteIds);
    }

    /**
     *
     * @param ids
     */
    @Override
    public void submit(List<Long> ids) {
        // 提交至审核页面
        List<ApproveDTO> approveDTOList = new ArrayList<>();
        List<LeaseIncomeEntity> LeaseIncomeEntityList = getLeaseIncomeEntityByIds(ids);
        if (LeaseIncomeEntityList == null || LeaseIncomeEntityList.isEmpty()) {
            return;
        }
        boolean voucherNotGenerated = LeaseIncomeEntityList.stream()
                .anyMatch(entity -> !YesOrNoEnum.YES.getCode().equals(entity.getIsGenerateVoucher()));
        if (voucherNotGenerated) {
            throw new ServiceException("请先生成凭证后再提交");
        }

        List<LeaseIncomeDetailsEntity> detailsEntityList = detailsService.list(
                Wrappers.<LeaseIncomeDetailsEntity>lambdaQuery()
                        .in(LeaseIncomeDetailsEntity::getLeaseIncomeId, ids)
                        .eq(LeaseIncomeDetailsEntity::getDelFlag, YesOrNoEnum.NO.getCode()));
        List<String> voucherIds = detailsEntityList.stream()
                .map(LeaseIncomeDetailsEntity::getVoucherId)
                .filter(StringUtils::isNotBlank)
                .flatMap(voucherId -> Arrays.stream(voucherId.split(",")))
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(voucherIds)) {
            throw new ServiceException("请先生成凭证后再提交");
        }
        voucherService.updateStatusByids(voucherIds, ProcessStatusEnum.SUBMITTED.getCode(),
                StringUtils.EMPTY, StringUtils.EMPTY);

        for (LeaseIncomeEntity entity : LeaseIncomeEntityList) {
            entity.setProcessStatus(MarginStatusEnum.SUBMITTED.getCode());
            entity.setSubmitBy(UserUtils.getStaffName());
            entity.setUpdateTime(LocalDateTime.now());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setUrl(approveUrl + entity.getId());
            approveDTO.setSubmitDate(LocalDateTime.now());
            approveDTO.setSubmitterName(UserUtils.getStaffName());
            approveDTO.setSubmitterNum(UserUtils.getStaffCode());
            approveDTO.setDocumentId(entity.getId());
            approveDTO.setDocumentType(BatchTypeEnum.SYJT.getCode());
            approveDTOList.add(approveDTO);
        }
        Map<Long, Long> approveSubmitResultMap = approveService.submit(approveDTOList);

        for (LeaseIncomeEntity entity : LeaseIncomeEntityList) {
            entity.setApproveId(approveSubmitResultMap.get(entity.getId()));
        }
        this.updateBatchById(LeaseIncomeEntityList);
    }

    private List<LeaseIncomeEntity> getLeaseIncomeEntityByIds(List<Long> ids) {
        LambdaQueryWrapper<LeaseIncomeEntity> wrapper = new LambdaQueryWrapper();
        wrapper.in(LeaseIncomeEntity::getId, ids);
        wrapper.eq(LeaseIncomeEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        return this.getBaseMapper().selectList(wrapper);
    }

    @Override
    public void withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        changeStatus(ids, MarginStatusEnum.ENTERED);

        // 审核记录处进行撤回操作
        LambdaQueryWrapper<LeaseIncomeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(LeaseIncomeEntity::getId, ids);
        wrapper.eq(LeaseIncomeEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        List<LeaseIncomeEntity> leaseIncomeEntityList = this.baseMapper.selectList(wrapper);
        if (leaseIncomeEntityList == null || leaseIncomeEntityList.isEmpty()) {
            return;
        }

        List<Long> approveIds = leaseIncomeEntityList.stream().map(LeaseIncomeEntity::getApproveId).
                collect(Collectors.toList());
        boolean result = approveService.withdraw(approveIds);
        if (!result) {
            throw new ServiceException("审核记录撤回失败!");
        }
    }

    /**
     * 审核通过后处理
     */
    @Override
    public void pass(CommonApproveDTO approveDTO) {
        List<Long> ids = new ArrayList<>();
        ids.add(approveDTO.getDocumentId());
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        changeStatus(ids, MarginStatusEnum.PASS);

        // 将计提产生的临时偿还计划数据更新到原偿还计划中
        LambdaQueryWrapper<LeaseIncomeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(LeaseIncomeEntity::getId, ids);
        wrapper.eq(LeaseIncomeEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        List<LeaseIncomeEntity> leaseIncomeEntityList = this.baseMapper.selectList(wrapper);
        if (leaseIncomeEntityList == null || leaseIncomeEntityList.isEmpty()) {
            return;
        }

        List<String> orgIds = leaseIncomeEntityList.stream().map(LeaseIncomeEntity::getOrgId).distinct().
                collect(Collectors.toList());
        repaymentPlanProvisionService.updateProvisionData(orgIds);

        // 凭证状态更新
        LambdaQueryWrapper<LeaseIncomeDetailsEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(LeaseIncomeDetailsEntity::getLeaseIncomeId, ids);
        List<LeaseIncomeDetailsEntity> detailsEntityList = detailsService.list(lambdaQueryWrapper);
        List<String> voucherList = detailsEntityList.stream()
                .map(LeaseIncomeDetailsEntity::getVoucherId)
                .filter(StringUtils::isNotBlank)
                .flatMap(voucherId -> Arrays.stream(voucherId.split(",")))
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(voucherList)) {
            return;
        }
        List<String> voucherUpdateList = new ArrayList<>();
        for (int i = 0; i < voucherList.size(); i++) {
            voucherUpdateList.add(voucherList.get(i));
            if (voucherUpdateList.size() > 2000) {
                voucherService.updateStatusByids(voucherUpdateList, ProcessStatusEnum.REVIEWED.getCode(),
                        approveDTO.getApproverNum(), approveDTO.getApproverName());
                voucherUpdateList = new ArrayList<>();
            }
        }

        if (!voucherUpdateList.isEmpty()) {
            voucherService.updateStatusByids(voucherUpdateList, ProcessStatusEnum.REVIEWED.getCode(),
                    approveDTO.getApproverNum(), approveDTO.getApproverName());
        }
    }

    @Override
    public void fail(CommonApproveDTO approveDTO) {
        List<Long> ids = new ArrayList<>();
        ids.add(approveDTO.getDocumentId());

        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        changeStatus(ids, MarginStatusEnum.FAILED);

        // 凭证状态更新
        LambdaQueryWrapper<LeaseIncomeDetailsEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(LeaseIncomeDetailsEntity::getLeaseIncomeId, ids);
        List<LeaseIncomeDetailsEntity> detailsEntityList = detailsService.list(lambdaQueryWrapper);
        List<String> voucherList = detailsEntityList.stream()
                .map(LeaseIncomeDetailsEntity::getVoucherId)
                .filter(StringUtils::isNotBlank)
                .flatMap(voucherId -> Arrays.stream(voucherId.split(",")))
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(voucherList)) {
            return;
        }
        List<String> voucherUpdateList = new ArrayList<>();
        for (int i = 0; i < voucherList.size(); i++) {
            voucherUpdateList.add(voucherList.get(i));
            if (voucherUpdateList.size() > 2000) {
                voucherService.updateStatusByids(voucherUpdateList, ProcessStatusEnum.REJECTED.getCode(),
                        approveDTO.getApproverNum(), approveDTO.getApproverName());
                voucherUpdateList = new ArrayList<>();
            }
        }

        if (!voucherUpdateList.isEmpty()) {
            voucherService.updateStatusByids(voucherUpdateList, ProcessStatusEnum.REJECTED.getCode(),
                    approveDTO.getApproverNum(), approveDTO.getApproverName());
        }
    }

    private void changeStatus(List<Long> ids, MarginStatusEnum marginStatusEnum) {
        LambdaUpdateWrapper<LeaseIncomeEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .in(LeaseIncomeEntity::getId, ids)
                .eq(LeaseIncomeEntity::getProcessStatus, MarginStatusEnum.SUBMITTED.getCode())
                .set(LeaseIncomeEntity::getUpdateTime, LocalDateTime.now())
                .set(LeaseIncomeEntity::getProcessStatus, marginStatusEnum.getCode());
        this.update(updateChainWrapper);
    }


    /**
     * 同步未实现收益
     */
    public Boolean outstandingAmountSync(LeaseIncomeQueryDTO queryDTO) {
        Date queryDate = queryDTO.getBusinessDate();
        R<Boolean> result = kingdeeDataSyncFacade.outstandingAmountSync(DateUtils.format(queryDate, "yyyyMM"));
        return result.getData();
    }

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void generateAsync(LeaseIncomeQueryDTO queryDTO) {
        log.info("收益计提 start");
        long startTime = System.currentTimeMillis();
        Date queryDate = queryDTO.getBusinessDate();
        if (null == queryDate) {
            return;
        }
        queryDate = DateUtil.endOfDay(DateUtil.endOfMonth(queryDate));
        queryDTO.setBusinessDate(queryDate);

        // 删除未提交数据 排除已审批数据
        log.info("------删除未提交的收益计提数据 start");
        List<String> notGenerateOrgIds = deleteNotSubmitAndFilterSubmitedOrg(queryDTO);
        log.info("------删除未提交的收益计提数据 end");
        queryDTO.setNotGenerateOrgIds(notGenerateOrgIds);

        // 1. 删除临时表数据 把需要处理的数据转到临时表
        log.info("------将待计提的偿还计划迁移到临时表 start");
        repaymentPlanTempService.deleteAllCompleteData();
        repaymentPlanTempService.copyDataFromRepaymentPlan(queryDTO);
        log.info("------将待计提的偿还计划迁移到临时表 end");

        repaymentPlanProvisionService.deleteAllDate();

        // 取得是否计提收益的状态列表
        R<List<SysDictData>> list = listDictTypeData(DictTypeEnum.IS_CALCULATE_REVENUE.getCode());
        List<String> noLeaseStatusList = Optional.ofNullable(list.getData()).get().stream().
                filter(e -> StringUtils.isNotEmpty(e.getRemark()) && YesOrNoEnum.NO.getDesc().equals(e.getRemark())).
                map(SysDictData::getDictLabel).collect(Collectors.toList());

//        LambdaQueryWrapper<LeaseIncomeDetailsEntity> wrapper = new LambdaQueryWrapper();
//        wrapper.eq(LeaseIncomeDetailsEntity :: getBusinessDate, queryDate);
//        detailsService.getBaseMapper().delete(wrapper);
//        LambdaQueryWrapper<LeaseIncomeEntity> wrapper1 = new LambdaQueryWrapper();
//        wrapper1.eq(LeaseIncomeEntity :: getBusinessDate, queryDate);
//        leaseIncomeMapper.delete(wrapper1);

        // 2. 查询临时表信息
        int numData = repaymentPlanTempMapper.countContractCode(); // 数据条数
        int numPartitions = MAX_NUM_PAMAXRTITIONS; // 分区数 数据小于5000,则使用1个线程,最多10个线程
        if (numData < 1000) {
            numPartitions = 1;
        } else {
            numPartitions = Math.min(numData / 1000, MAX_NUM_PAMAXRTITIONS);
        }
        // 计算每个分区的整数量
        int numPerPartition = numData / numPartitions;
        int remainingNum = numData % numPartitions;

        // 3. 线程分析 根据合同
        List<CompletableFuture<List<LeaseIncomeDetailsEntity>>> completableFutures = new ArrayList<>();
        int currentNum = 0;
        for (int i = 0; i < numPartitions; i++) {
            int partitionSize = numPerPartition + (remainingNum > 0 ? 1 : 0);
            log.info("当前分区开始offset :{}, 数据量:{}", currentNum, partitionSize);
            int finalCurrentNum = currentNum;
            Date finalQueryDate = queryDate;
            CompletableFuture<List<LeaseIncomeDetailsEntity>> future = CompletableFuture.supplyAsync(() -> {
                // 4. 执行线程 根据 offset limit限制查询的数据
                return processPartition(finalCurrentNum, partitionSize, finalQueryDate, noLeaseStatusList);
            }, asyncTaskExecutor);
            completableFutures.add(future);

            currentNum += partitionSize;
            remainingNum--;
        }

        // 5. 等待每个线程结束
        // 将所有CompletableFuture组合成一个新的CompletableFuture，并等待所有线程任务完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                completableFutures.toArray(new CompletableFuture[0]));

        try {
            // 等待所有线程任务完成
            allFutures.join();
            long endJoinTime = System.currentTimeMillis();
            log.info("收益计提处理时间 所有线程处理完成 " + (endJoinTime - startTime));

            List<LeaseIncomeDetailsEntity> leaseIncomeDetailsEntities = new ArrayList<>();
            // 获取每个线程任务的结果
            for (CompletableFuture<List<LeaseIncomeDetailsEntity>> completableFuture : completableFutures) {
                List<LeaseIncomeDetailsEntity> result = completableFuture.get();
                leaseIncomeDetailsEntities.addAll(result);
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("generateAsync error", e);
            throw new ServiceException("批量生成凭证失败，失败原因:" + e.getMessage());
        }

        // 6. 收益计提数据汇总
        generateLeaseIncome(queryDTO);
        log.info("收益计提 End");
    }

    public List<LeaseIncomeDetailsEntity> processPartition(int offset, int partitionSize, Date queryDate,
                                                           List<String> noLeaseStatusList) {

        // 循环处理数据，每次循环处理的数据为{ACH_THREAD_PROCESS_NUM}个合同号
        int loopCount = getLoopCount(partitionSize);
        for (int i = 0; i < loopCount; i++) {
            log.info("loopCount：" + i);
            int offsetCurrent = offset + i * EACH_THREAD_PROCESS_NUM;
            int eachThreadProcessNum = EACH_THREAD_PROCESS_NUM;
            if (i == loopCount - 1) {
                eachThreadProcessNum = partitionSize % EACH_THREAD_PROCESS_NUM;
            }

            // 2. 查询相关数据
            log.info("查询合同的偿还计划 start offsetCurrent：" + offsetCurrent);
            List<RepaymentPlanEntity> allRepaymentPlanEntities = repaymentPlanTempMapper.
                    listByOffsetAndLimit(offsetCurrent, eachThreadProcessNum);
            log.info("查询合同的偿还计划 end");

            // 合同编码列表
            List<String> contractCodeList = allRepaymentPlanEntities.stream().map(
                    e -> e.getContractCode()).distinct().collect(Collectors.toList());

            // 查询合同的基础信息
            List<ContractMonthEntity> contractMonthEntities = contractMonthService.getTranStatusList(contractCodeList);

            List<ContractEntity> contractEntities = BeanUtil.copyToList(contractMonthEntities, ContractEntity.class);
            if (CollectionUtils.isEmpty(contractEntities)) {
                log.info("无符合条件的合同:{}", JSON.toJSONString(contractCodeList));
                continue;
            }
            // 设置合同的期初期末状态（paymethod）
//            contractEntities = dwsBzHetjyjgxxDService.setContractPayMethod(contractEntities);
            // 将合同映射为Map, key:合同编码-签约主体
            Map<String, ContractEntity> contractEntityMap = contractEntities.stream().collect(
                    Collectors.toMap(e -> this.getMapKey(e.getContractCode(), e.getOrgId()),
                            e -> e, (a, b) -> b));
            List<String> queryContractCodeList = contractEntities.stream().map(e -> e.getContractCode()).
                    collect(Collectors.toList());

            // 3.查询合同余额表 的应收租金余额
            List<ContractBalanceLatestEntity> contractBalanceLatestEntities = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(queryContractCodeList)) {
                contractBalanceLatestEntities = contractBalanceLatestMapper.
                        listReceivableRentBalanceByContractCodes(queryContractCodeList);
            }

            // 4. 获取合同上月计提数据
//            DateTime lastMonthLastDay = DateUtil.beginOfDay(DateUtil.endOfMonth(DateUtil.offsetMonth(queryDate, -1)));
//            List<LeaseIncomeDetailsEntity> lastDetails = detailsService.getBaseMapper().selectList(Wrappers.<LeaseIncomeDetailsEntity>lambdaQuery()
//                    .in(LeaseIncomeDetailsEntity::getContractCode, contractCodeList)
//                    .eq(LeaseIncomeDetailsEntity::getBusinessDate, lastMonthLastDay));

            // 取得TA金额
            Map<String, BigDecimal> contractTAAmountMap = contractTaAmountService.getTaAmountMap(contractEntityMap);

            // 取得未实现收益
            Map<String, BigDecimal> outstandingAmount = outstandingAmountInitService.selectEndBalFor(contractCodeList);

            // 5.处理 偿还计划生成收益计提详情 保存
            try {
                List<LeaseIncomeDetailsEntity> leaseIncomeDetailsEntities = generateLeaseIncomeDetails(
                        allRepaymentPlanEntities, queryDate, contractEntityMap, contractBalanceLatestEntities,
                        noLeaseStatusList, contractTAAmountMap, outstandingAmount);
                detailsService.saveBatch(leaseIncomeDetailsEntities);
                leaseIncomeDetailsEntities.clear();
            } catch (Exception e) {
                log.error("收益计提处理错误", e);
            }

            allRepaymentPlanEntities.clear();
            contractCodeList.clear();
            contractEntities.clear();
            contractEntityMap.clear();
            queryContractCodeList.clear();
            contractBalanceLatestEntities.clear();
            contractTAAmountMap.clear();
            // 6.更新temp表为已处理 暂时全部删除
//        List<String> updateContractCodeList = leaseIncomeDetailsEntities.stream().map(e -> e.getContractCode()).distinct().collect(Collectors.toList());
//        repaymentPlanTempMapper.updateProcessStatusByContractCode(updateContractCodeList);
        }
        log.info("线程任务 end");
        return new ArrayList<>();
    }

    private String getMapKey(String contractCode, String orgId) {
        StringBuffer result = new StringBuffer(contractCode).append("-");
        if (StringUtils.isEmpty(orgId)) {
            result.append("");
        } else {
            result.append(orgId);
        }

        return result.toString();
    }

    private int getLoopCount(int partitionSize) {
        int loopCount = partitionSize / EACH_THREAD_PROCESS_NUM;
        if (partitionSize % EACH_THREAD_PROCESS_NUM != 0) {
            return loopCount + 1;
        } else {
            return loopCount;
        }
    }

    @Override
    public void generateLeaseIncome(LeaseIncomeQueryDTO queryDTO) {
        Date queryDate = queryDTO.getBusinessDate();
        if (null == queryDate) {
            return;
        }
        queryDate = DateUtil.endOfDay(DateUtil.endOfMonth(queryDate));
        queryDTO.setBusinessDate(queryDate);
        // 汇总该日期所有详情生成数据 并把汇总id回写到详情表
        // 1.生成详情数据
        List<LeaseIncomeEntity> leaseIncomeEntities = leaseIncomeMapper.countFromDetailData(queryDTO);
        leaseIncomeEntities.forEach(e -> {
            e.setBusinessDate(queryDTO.getBusinessDate());
            e.setAccountDate(queryDTO.getBusinessDate());
            e.setProcessStatus(MarginStatusEnum.ENTERED.getCode());
            e.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
            e.setDelFlag(YesOrNoEnum.NO.getCode());
        });
        saveBatch(leaseIncomeEntities);
        // 2.回写详情
        leaseIncomeEntities.forEach(e -> {
            LambdaUpdateWrapper<LeaseIncomeDetailsEntity> detailUpdateChainWrapper = new LambdaUpdateWrapper<>();
            detailUpdateChainWrapper
                    .eq(LeaseIncomeDetailsEntity::getOrgId, e.getOrgId())
                    .eq(LeaseIncomeDetailsEntity::getSystemCode, e.getSystemCode())
                    .eq(LeaseIncomeDetailsEntity::getBusinessDate, queryDTO.getBusinessDate())
                    .set(LeaseIncomeDetailsEntity::getLeaseIncomeId, e.getId());
            detailsService.update(detailUpdateChainWrapper);
        });
    }

    /**
     *  当合同已结束/已结清、应收租金余额为0时，摊销表的数据未摊销的收益及转入表外的收益，全额进行计提；不受计提方式、逾期状态、观察期状态的影响
     */
    private void closeOffContract(List<RepaymentPlanEntity> planEntitityList, ContractEntity contractEntity,
                                  Map<String, List<ContractBalanceLatestEntity>> contractRentMap,
                                  List<LeaseIncomeDetailsEntity> leaseIncomeDetailsEntities,
                                  Integer planDatePeriod) {
        // 已结束,已结清的合同 查看应收租金余额是否为0,为0,则把所有未计提及表外金额计提,否则不处理
        List<ContractBalanceLatestEntity> contractBalanceLatestEntities = contractRentMap.
                get(this.getMapKey(contractEntity.getContractCode(), planEntitityList.get(0).getOrgId()));

        if (CollectionUtils.isEmpty(contractBalanceLatestEntities)) {
            return;
        }

        BigDecimal totalRentBalance = contractBalanceLatestEntities.stream().
                map(ContractBalanceLatestEntity::getReceivableRentBalance).filter(Objects::nonNull).
                reduce(BigDecimal.ZERO, BigDecimal::add);

        // 表外转表内计提金额
        BigDecimal outTableTransferInTableAmount = new BigDecimal(0);
        // 表内计提金额
        BigDecimal inTableAmount = new BigDecimal(0);

        if (totalRentBalance.compareTo(BigDecimal.ZERO) == 0) {
            for (RepaymentPlanEntity entity : planEntitityList) {
                if (!RecaptureStatusEnum.RETURNED.getCode().equals(entity.getRecaptureStatus())) {
                    entity.setRecaptureStatus(RecaptureStatusEnum.RETURNED.getCode());
                    if (OnOrOffBalanceSheetEnum.OFF.getCode().equals(entity.getOnAndOffBalanceSheet())) {
                        entity.setOnAndOffBalanceSheet(OnOrOffBalanceSheetEnum.ON.getCode());
                        outTableTransferInTableAmount = outTableTransferInTableAmount.add(entity.getRentalIncome()).
                                add(entity.getAdjustmentAmount() == null ? BigDecimal.ZERO : entity.getAdjustmentAmount());
                    } else {
                        inTableAmount = inTableAmount.add(entity.getRentalIncome()).
                                add(entity.getAdjustmentAmount() == null ? BigDecimal.ZERO : entity.getAdjustmentAmount());
                    }
                    entity.setAmortized(YesOrNoEnum.YES.getCode());
                    entity.setUpdateTime(LocalDateTime.now());
                }
            }

            // 业务日期
            Date businessDateFirstDay = DateUtils.parseDate(planDatePeriod.toString().concat("01"), "yyyyMMdd");
            Date planDateLastDay = DateUtil.beginOfDay(DateUtil.endOfMonth(businessDateFirstDay));
            Map<String, BigDecimal> createLeaseIncomeDetailsMap = new HashMap<>();
            createLeaseIncomeDetailsMap.put("inTableAmount", inTableAmount);
            createLeaseIncomeDetailsMap.put("outTableAmount", new BigDecimal(0));
            createLeaseIncomeDetailsMap.put("intableTransferOuttableAmount", new BigDecimal(0));
            createLeaseIncomeDetailsMap.put("outtableTransferIntableAmount", outTableTransferInTableAmount);

            Map<String, Date> createLeaseIncomeDetailsDateMap = new HashMap<>();
            createLeaseIncomeDetailsDateMap.put("planDateLastDay", planDateLastDay);
            createLeaseIncomeDetailsDateMap.put("lastReturnValidDate", null);
            createLeaseIncomeDetailsDateMap.put("lastRepaymentDate", null);
            createLeaseIncomeDetailsDateMap.put("nextPaymentDate", null);

            Map<String, String> otherParams = new HashMap<>();
            leaseIncomeDetailsEntities.add(this.createLeaseIncomeDetailsEntity(null,
                    planEntitityList.get(planEntitityList.size() - 1), contractEntity, createLeaseIncomeDetailsDateMap,
                    contractBalanceLatestEntities.get(0), createLeaseIncomeDetailsMap, otherParams));
        } else {
            log.info("收益计提 应收租金余额不为0 :{}", contractEntity.getContractCode());
        }
    }

    /**
     * 计提处理  测试逾期合同：L21B118584  未逾期合同：L23B104076
     */
    private void provisionProcess(List<RepaymentPlanEntity> repaymentPlanEntityList, Integer planDatePeriod,
                                  Map<String, BigDecimal> contractTAAmountMap, ContractEntity contractEntity,
                                  Map<String, List<ContractBalanceLatestEntity>> contractRentMap,
                                  List<LeaseIncomeDetailsEntity> leaseIncomeDetailsEntities,
                                  BigDecimal outstandingAmount) {

        String contractCode = repaymentPlanEntityList.get(0).getContractCode();
        log.info("合同：" + contractCode + "计提开始---------------");
        // 业务日期-计提月份第一天
        Date businessDateFirstDay = DateUtils.parseDate(planDatePeriod.toString().concat("01"), "yyyyMMdd");
        // 计提月份最后一天
        Date planDateLastDay = DateUtil.endOfDay(DateUtil.endOfMonth(businessDateFirstDay));
        // 计提月份上一期
        int lastPlanDateLastDay = Integer.parseInt(DateUtil.format(DateUtil.offsetMonth(businessDateFirstDay, -1), "yyyyMM"));
        // 上一期数据
        List<RepaymentPlanEntity> lastRepaymentPlanEntityList = repaymentPlanEntityList.stream().
                filter(e -> e.getPlanDatePeriod() == lastPlanDateLastDay).collect(Collectors.toList());
        RepaymentPlanEntity lastRepaymentPlanEntity = null;
        if (lastRepaymentPlanEntityList != null && !lastRepaymentPlanEntityList.isEmpty()) {
            lastRepaymentPlanEntity = lastRepaymentPlanEntityList.get(lastRepaymentPlanEntityList.size() - 1);
        }
        if (lastRepaymentPlanEntity != null && lastRepaymentPlanEntity.getOverdueDays() == null) {
            lastRepaymentPlanEntity.setOverdueDays(0);
        }

        // 当前计提月数据
        List<RepaymentPlanEntity> curMonthRepaymentPlanEntityList = repaymentPlanEntityList.stream().filter(
                e -> planDatePeriod.intValue() == e.getPlanDatePeriod().intValue()).collect(Collectors.toList());
        RepaymentPlanEntity curRepaymentPlanEntity = null;
        if (curMonthRepaymentPlanEntityList != null && !curMonthRepaymentPlanEntityList.isEmpty()) {
            curRepaymentPlanEntity = curMonthRepaymentPlanEntityList.get(curMonthRepaymentPlanEntityList.size() - 1);
        } else {
            curRepaymentPlanEntity = repaymentPlanEntityList.get(repaymentPlanEntityList.size() - 1);
        }

        // 观察期到期日
        Date observeExpireDate = null;
        // 最后一次回笼的日期
//        Date lastReturnAmountDate = null;
        // 第一次逾期日期
//        Date firstNoReturnDate = null;
        // 合同为期初时， 最后一次回笼金额覆盖的回款最后日期-上期实收期间
        Date lastReturnValidDate = repaymentPlanEntityList.get(0).getPlanDate();
        // 上次还款日
        Date lastRepaymentDate = null;
        Integer enterObservePeriod = null;
        // 下次回款日
        Date nextPaymentDate = null;
        // 偿还计划最后一条数据
        RepaymentPlanEntity lastPlanEntity = repaymentPlanEntityList.get(repaymentPlanEntityList.size() - 1);
        // 完全回笼标识（偿还计划最后一条数据如果是回笼，则表示该合同已经完全回笼）
//        boolean isReceivedAllAmount = false;
//        if (lastPlanEntity.getRecaptureStatus() != null &&
//                RecaptureStatusEnum.RETURNED.getCode().equals(lastPlanEntity.getRecaptureStatus()) &&
//                lastPlanEntity.getPlanDate().before(planDateLastDay)) {
//            isReceivedAllAmount = true;
//        }

        // 剩余TA金额
        BigDecimal surplusTAAmount = contractTAAmountMap.get(contractCode);
        if (surplusTAAmount == null) {
            surplusTAAmount = new BigDecimal(0);
        }

        // 异常信息
        StringBuffer exceptionType = new StringBuffer(StringUtil.EMPTY);
        // 表内计提金额
        BigDecimal inTableAmount = BigDecimal.ZERO;
        // 表内转表外计提金额
        BigDecimal inTableTransferOutTableAmount = BigDecimal.ZERO;
        // 表外计提金额
        BigDecimal outTableAmount = BigDecimal.ZERO;
        // 表外转表内计提金额
        BigDecimal outTableTransferInTableAmount = BigDecimal.ZERO;
        // 逾期收益
        BigDecimal overdueEarning = BigDecimal.ZERO;
        // 实收利息
        BigDecimal actualReceivedInterest = BigDecimal.ZERO;
        // 当月之前计提表内的金额
        BigDecimal beforeInTableAmount = BigDecimal.ZERO;
        // 未回笼的表内金额
        Map<String, BigDecimal> noReturnInTableAmountMap = new HashMap<>();
        // 未回笼的表外金额
        Map<String, BigDecimal> noReturnOutTableAmountMap = new HashMap<>();
        // 未回笼的当月数据-还款日以前
        Map<String, BigDecimal> noReturnCurMonthBeforeAmountMap = new HashMap<>();
        // 未回笼的当月数据-还款日以后
        Map<String, BigDecimal> noReturnCurMonthAfterAmountMap = new HashMap<>();

        // 取得合同的余额数据
        List<ContractBalanceLatestEntity> contractBalanceLatestList = contractRentMap.
                get(this.getMapKey(contractCode, repaymentPlanEntityList.get(0).getOrgId()));
        if (contractBalanceLatestList == null || contractBalanceLatestList.isEmpty()) {
            contractBalanceLatestList = new ArrayList<>();
        }
        if (contractBalanceLatestList.isEmpty()) {
            ContractBalanceLatestEntity contractBalanceLatestEntity = new ContractBalanceLatestEntity();
            contractBalanceLatestEntity.setReceivableRentBalance(new BigDecimal(0));
            contractBalanceLatestList.add(contractBalanceLatestEntity);
        }


        // 计提需要的基础数据准备
        for (int i = 0; i < repaymentPlanEntityList.size(); i++) {
            RepaymentPlanEntity entity = repaymentPlanEntityList.get(i);
            if (entity.getRentalIncome() == null) {
                entity.setRentalIncome(BigDecimal.ZERO);
            }
            if (entity.getAdjustmentAmount() == null) {
                entity.setAdjustmentAmount(BigDecimal.ZERO);
            }
            if (entity.getActualRepaymentRentAmount() == null) {
                entity.setActualRepaymentRentAmount(new BigDecimal(0));
            }

            // 当月之前表内计提金额汇总
            if (YesOrNoEnum.YES.getCode().equals(entity.getAccrued())
                    && YesOrNoEnum.NO.getCode().equals(entity.getOnAndOffBalanceSheet())
                    && entity.getPlanDatePeriod() <= planDatePeriod.intValue()) {
                beforeInTableAmount = beforeInTableAmount.add(entity.getRentalIncome());
            }

            // 每一期数据处理(非月底数据)
            if (entity.getRentAmount() != null && entity.getRentAmount().compareTo(BigDecimal.ZERO) > 0) {
                // 回笼的情况下处理
                if (RecaptureStatusEnum.RETURNED.getCode().equals(entity.getRecaptureStatus())) {
                    if (entity.getInterestAmount() != null) {
                        actualReceivedInterest = actualReceivedInterest.add(entity.getInterestAmount());
                    }
                }

                // 查找第一次未完全回笼的日期,如果是手工上传的上期实收期间，则直接取手工上传的逾期日期
                if (entity.getPreviousPaidPeriod() != null) {
                    nextPaymentDate = entity.getPlanDate();
                } else {

                    // 每一期剩余未回笼金额
                    BigDecimal surplusAmount = entity.getRentAmount().subtract(entity.getActualRepaymentRentAmount());
                    // 剩余未回笼金额大于剩余TA金额-表示未完全回笼
                    if (surplusAmount.compareTo(surplusTAAmount) > 0) {
                        nextPaymentDate = entity.getPlanDate();
                        break;
                    } else {
                        surplusTAAmount = surplusTAAmount.subtract(surplusAmount);

                        // 刷新最后一次回笼的日期
                        lastRepaymentDate = entity.getPlanDate();
                        // 刷新最后一次回笼覆盖到的日期
                        if (Constants.PAY_METHOD_PERIOD_LAST.equals(contractEntity.getPayMethod())) {
                            lastReturnValidDate = entity.getPlanDate();
                        } else {
                            // 刷新上期实收期间
                            // 上次还款日+付息频率
                            if (StringUtils.isNotEmpty(contractEntity.getReturnType()) && "半年付".equals(contractEntity.getReturnType())) {
                                lastReturnValidDate = DateUtil.offset(entity.getPlanDate(), DateField.MONTH, 6);
                                if (DateUtil.isLastDayOfMonth(entity.getPlanDate())) {
                                    lastReturnValidDate = DateUtil.endOfMonth(lastReturnValidDate);
                                }
                            } else if (StringUtils.isNotEmpty(contractEntity.getReturnType()) && "季付".equals(contractEntity.getReturnType())) {
                                lastReturnValidDate = DateUtil.offset(entity.getPlanDate(), DateField.MONTH, 3);
                                if (DateUtil.isLastDayOfMonth(entity.getPlanDate())) {
                                    lastReturnValidDate = DateUtil.endOfMonth(lastReturnValidDate);
                                }
                            } else if (StringUtils.isNotEmpty(contractEntity.getReturnType()) && "年付".equals(contractEntity.getReturnType())) {
                                lastReturnValidDate = DateUtil.offset(entity.getPlanDate(), DateField.MONTH, 12);
                                if (DateUtil.isLastDayOfMonth(entity.getPlanDate())) {
                                    lastReturnValidDate = DateUtil.endOfMonth(lastReturnValidDate);
                                }
                            } else if (StringUtils.isNotEmpty(contractEntity.getReturnType()) && "月付".equals(contractEntity.getReturnType())) {
                                lastReturnValidDate = DateUtil.offset(entity.getPlanDate(), DateField.MONTH, 1);
                                if (DateUtil.isLastDayOfMonth(entity.getPlanDate())) {
                                    lastReturnValidDate = DateUtil.endOfMonth(lastReturnValidDate);
                                }
                            } else {
                                RepaymentPlanEntity nextEntity = repaymentPlanEntityList.stream().filter(e ->
                                        e.getPlanDate().compareTo(entity.getPlanDate()) > 0
                                                && e.getRentAmount().compareTo(BigDecimal.ZERO) > 0).findFirst().orElse(null);
                                if (nextEntity != null) {
                                    lastReturnValidDate = nextEntity.getPlanDate();
                                } else {
                                    lastReturnValidDate = null;
                                }
                            }
                        }

                        // 加上TA金额未逾期，则计提到表外转表内
                        if (BigDecimal.ZERO.compareTo(surplusAmount) < 0 && entity.getPlanDatePeriod() <= planDatePeriod.intValue()) {
                            outTableTransferInTableAmount = outTableTransferInTableAmount.add(entity.getRentalIncome());
                            entity.setAmortized(YesOrNoEnum.YES.getCode());
                            entity.setOnAndOffBalanceSheet(OnOrOffBalanceSheetEnum.ON.getCode());
                        }
                    }
                }
            }
        }

        // 当合同已结束/已结清、应收租金余额为0时(或者合同已经完全回笼)，
        // 摊销表的数据未摊销的收益及转入表外的收益，全额进行计提；不受计提方式、逾期状态、观察期状态的影响
        List<RepaymentPlanEntity> planEntities = repaymentPlanEntityList.stream()
                .filter(item -> RecaptureStatusEnum.PARTIALRECOVERY.getCode().equals(item.getRecaptureStatus()))
                .collect(Collectors.toList());
        if (Constants.CONTRACT_STATUS_CLOSE.contains(contractEntity.getContractStatus())
                || (nextPaymentDate == null && CollectionUtils.isEmpty(planEntities))) {

            // 如果合同已经完结，则不进行计提
            boolean isFinished = true;
            actualReceivedInterest = BigDecimal.ZERO;
            for (RepaymentPlanEntity entity : repaymentPlanEntityList) {
                if (entity.getRentalIncome() == null) {
                    entity.setRentalIncome(BigDecimal.ZERO);
                }
                if (entity.getAdjustmentAmount() == null) {
                    entity.setAdjustmentAmount(BigDecimal.ZERO);
                }

                if (entity.getAmortized() == null || YesOrNoEnum.NO.getCode().equals(entity.getAmortized())
                        || OnOrOffBalanceSheetEnum.OFF.getCode().equals(entity.getOnAndOffBalanceSheet())) {
                    entity.setAmortized(YesOrNoEnum.YES.getCode());
                    entity.setOnAndOffBalanceSheet(OnOrOffBalanceSheetEnum.ON.getCode());
                    if (AccrualMethodEnum.RECEIPT.getCode().equals(entity.getIncomeProvisionMethod())) {
                        inTableAmount = inTableAmount.add(entity.getActualRepaymentRentAmount());
                    } else {
                        inTableAmount = inTableAmount.add(entity.getRentalIncome()).add(entity.getAdjustmentAmount());
                    }

                    // 未完结，需要进行计提操作
                    isFinished = false;
                }
                if (RecaptureStatusEnum.PARTIALRECOVERY.getCode().equals(entity.getRecaptureStatus())) {
                    isFinished = false;
                }

                if (entity.getRentAmount() != null && entity.getRentAmount().compareTo(BigDecimal.ZERO) > 0) {
                    actualReceivedInterest = NumberUtil.add(actualReceivedInterest, entity.getInterestAmount());
                }
            }

            BigDecimal totalRentBalance = contractBalanceLatestList.get(0).getReceivableRentBalance();
            if (isFinished && totalRentBalance.compareTo(BigDecimal.ZERO) != 0) {
                exceptionType.append("合同已经结束,应收租金余额不为0! | ");
            }

            // 合同已经完结，则不再进行计提操作
            if (isFinished) {
                log.info("合同：" + contractCode + "计提结束");
                return;
            }

            // 单月收益计提明细
            Map<String, BigDecimal> createLeaseIncomeDetailsMap = new HashMap<>();
            createLeaseIncomeDetailsMap.put("inTableAmount", inTableAmount);
            createLeaseIncomeDetailsMap.put("outTableAmount", outTableAmount);
            createLeaseIncomeDetailsMap.put("intableTransferOuttableAmount", inTableTransferOutTableAmount);
            createLeaseIncomeDetailsMap.put("outtableTransferIntableAmount", outTableTransferInTableAmount);
            createLeaseIncomeDetailsMap.put("overdueEarning", overdueEarning);
            createLeaseIncomeDetailsMap.put("actualReceivedInterest", actualReceivedInterest);
            createLeaseIncomeDetailsMap.put("beforeInTableAmount", beforeInTableAmount);
            createLeaseIncomeDetailsMap.put("outstandingAmount", BigDecimal.ZERO);

            lastRepaymentPlanEntity = repaymentPlanEntityList.get(repaymentPlanEntityList.size() - 1);
            Map<String, Date> createLeaseIncomeDetailsDateMap = new HashMap<>();
            createLeaseIncomeDetailsDateMap.put("planDateLastDay", planDateLastDay);
            createLeaseIncomeDetailsDateMap.put("lastReturnValidDate", lastRepaymentPlanEntity.getPlanDate());
            createLeaseIncomeDetailsDateMap.put("lastRepaymentDate", lastRepaymentPlanEntity.getPlanDate());
            createLeaseIncomeDetailsDateMap.put("nextPaymentDate", null);
            createLeaseIncomeDetailsDateMap.put("repaymentLastPlanDate", lastPlanEntity.getPlanDate());

            Map<String, String> otherParams = new HashMap<>();
            otherParams.put("exceptionType", exceptionType.toString());
            otherParams.put("allProvisionFlag", YesOrNoEnum.NO.getCode());
            otherParams.put("lastRepaymentPlanEntityIsNull", YesOrNoEnum.NO.getCode());
            // 特殊合同后面需要设置本月租赁收入为当月租赁收入求和加上本月之后
            otherParams.put("specialFlag", YesOrNoEnum.YES.getCode());

            leaseIncomeDetailsEntities.add(this.createLeaseIncomeDetailsEntity(curMonthRepaymentPlanEntityList,
                    lastRepaymentPlanEntity, contractEntity, createLeaseIncomeDetailsDateMap,
                    contractBalanceLatestList.get(0), createLeaseIncomeDetailsMap, otherParams));
            log.info("合同：" + contractCode + "计提结束");
            return;
        }


        // 逾期天数计算
        int overdueDays = 0;
        if (nextPaymentDate != null && nextPaymentDate.compareTo(planDateLastDay) < 0) {
            overdueDays = (int) DateUtil.betweenDay(nextPaymentDate, planDateLastDay, true);
        }

        for (int i = 0; i < repaymentPlanEntityList.size(); i++) {
            RepaymentPlanEntity entity = repaymentPlanEntityList.get(i);

            // 如果当前对象的plandate大于当前业务日期,则退出
            if (repaymentPlanEntityList.get(i).getPlanDatePeriod() > planDatePeriod.intValue()) {
                break;
            }

            if (curMonthRepaymentPlanEntityList != null && !curMonthRepaymentPlanEntityList.isEmpty() &&
                    curRepaymentPlanEntity != null &&
                    curRepaymentPlanEntity.getPlanDatePeriod().intValue() == entity.getPlanDatePeriod().intValue()) {
                this.curMonthDataProcess(entity, noReturnCurMonthBeforeAmountMap, noReturnCurMonthAfterAmountMap,
                        nextPaymentDate, lastReturnValidDate);
            } else {
                // 正常计划计提或者逾期
                // 未回笼金额求和：如果是期初，为最后一期归还租金的下一个归还日期，如果是期末，为当前计划归还日期（默认是期末）
                if (Constants.PAY_METHOD_PERIOD_INIT.equals(contractEntity.getPayMethod())) {
                    // 未回笼数据
                    if (nextPaymentDate != null && entity.getPlanDate().compareTo(nextPaymentDate) > 0) {
                        if (YesOrNoEnum.YES.getCode().equals(curRepaymentPlanEntity.getImpairmentThirdStage())) {
                            outTableAmount = outTableAmount.add(this.supplementaryRevenueAccrual(entity, OnOrOffBalanceSheetEnum.OFF.getCode()));
                        } else {
                            inTableAmount = inTableAmount.add(this.supplementaryRevenueAccrual(entity, OnOrOffBalanceSheetEnum.ON.getCode()));
                        }
                        // 统计未回笼的表外表内金额
                        this.noReturnDataProcess(entity, noReturnOutTableAmountMap, noReturnInTableAmountMap);

                    } else {
                        inTableAmount = inTableAmount.add(this.supplementaryRevenueAccrual(entity, OnOrOffBalanceSheetEnum.ON.getCode()));
                    }
                } else {
                    // 未回笼数据
                    if (lastRepaymentDate == null || (lastRepaymentDate != null && entity.getPlanDate().compareTo(lastRepaymentDate) > 0)) {
                        if (YesOrNoEnum.YES.getCode().equals(curRepaymentPlanEntity.getImpairmentThirdStage())) {
                            outTableAmount = outTableAmount.add(this.supplementaryRevenueAccrual(entity, OnOrOffBalanceSheetEnum.OFF.getCode()));
                        } else {
                            inTableAmount = inTableAmount.add(this.supplementaryRevenueAccrual(entity, OnOrOffBalanceSheetEnum.ON.getCode()));
                        }
                        // 统计未回笼的表外表内金额
                        this.noReturnDataProcess(entity, noReturnOutTableAmountMap, noReturnInTableAmountMap);

                    } else {
                        inTableAmount = inTableAmount.add(this.supplementaryRevenueAccrual(entity, OnOrOffBalanceSheetEnum.ON.getCode()));
                    }
                }
            }

            BusinessClaimRepaymentRecordEntity claimAmountEntity = new BusinessClaimRepaymentRecordEntity();
            if (curRepaymentPlanEntity.getId().longValue() == entity.getId().longValue()) {
                // 处理状态判定
                // 2.当期在三阶段
                if (YesOrNoEnum.YES.getCode().equals(entity.getImpairmentThirdStage())) {
                    // 2.1 合同逾期
                    if (overdueDays >= 90 || YesOrNoEnum.YES.getCode().equals(entity.getManualChangeMark())) {
                        // 当月计提表外
                        Map<String, BigDecimal> noReturnCurMonthAmountMap = new HashMap<>();
                        noReturnCurMonthAmountMap.putAll(noReturnCurMonthBeforeAmountMap);
                        noReturnCurMonthAmountMap.putAll(noReturnCurMonthAfterAmountMap);
                        BigDecimal noReturnCurMonthAmountSum = this.noReturnAmountSum(noReturnCurMonthAmountMap);
                        outTableAmount = outTableAmount.add(noReturnCurMonthAmountSum);
                        this.provisionStatusModify(noReturnCurMonthAmountMap, repaymentPlanEntityList,
                                OnOrOffBalanceSheetEnum.OFF.getCode());

                        // 未回笼的表内金额转到表外
                        BigDecimal noReturnIntableAmountSum = this.noReturnAmountSum(noReturnInTableAmountMap);
                        inTableTransferOutTableAmount = inTableTransferOutTableAmount.add(noReturnIntableAmountSum);
                        this.provisionStatusModify(noReturnInTableAmountMap, repaymentPlanEntityList,
                                OnOrOffBalanceSheetEnum.OFF.getCode());
                    }

                    // 2.2 手动上传了观察期
                    if (lastRepaymentPlanEntity != null) {
                        if ((lastRepaymentPlanEntity.getOverdueDays() >= 90 && overdueDays < 90)
                                || ((StringUtils.isEmpty(lastRepaymentPlanEntity.getObserved())
                                || YesOrNoEnum.NO.getCode().equals(lastRepaymentPlanEntity.getObserved()))
                                && YesOrNoEnum.YES.getCode().equals(entity.getObserved()))) {
                            // 当月计提表外
                            Map<String, BigDecimal> noReturnCurMonthAmountMap = new HashMap<>();
                            noReturnCurMonthAmountMap.putAll(noReturnCurMonthBeforeAmountMap);
                            noReturnCurMonthAmountMap.putAll(noReturnCurMonthAfterAmountMap);
                            BigDecimal noReturnCurMonthAmountSum = this.noReturnAmountSum(noReturnCurMonthAmountMap);
                            outTableAmount = outTableAmount.add(noReturnCurMonthAmountSum);
                            this.provisionStatusModify(noReturnCurMonthAmountMap, repaymentPlanEntityList,
                                    OnOrOffBalanceSheetEnum.OFF.getCode());

                            // 未回笼的表内金额转到表外
                            BigDecimal noReturnIntableAmountSum = this.noReturnAmountSum(noReturnInTableAmountMap);
                            inTableTransferOutTableAmount = inTableTransferOutTableAmount.add(noReturnIntableAmountSum);
                            this.provisionStatusModify(noReturnInTableAmountMap, repaymentPlanEntityList,
                                    OnOrOffBalanceSheetEnum.OFF.getCode());
                            enterObservePeriod = entity.getPlanDatePeriod();
                            entity.setObserved(YesOrNoEnum.YES.getCode());
                            entity.setObservedExpirationDate(planDateLastDay);
                        }

                        // 2.3 合同在观察期
                        if (lastRepaymentPlanEntity.getObserved() != null
                                && YesOrNoEnum.YES.getCode().equals(lastRepaymentPlanEntity.getObserved())) {
                            // 当月计提表外
                            Map<String, BigDecimal> noReturnCurMonthAmountMap = new HashMap<>();
                            noReturnCurMonthAmountMap.putAll(noReturnCurMonthBeforeAmountMap);
                            noReturnCurMonthAmountMap.putAll(noReturnCurMonthAfterAmountMap);
                            BigDecimal noReturnCurMonthAmountSum = this.noReturnAmountSum(noReturnCurMonthAmountMap);
                            outTableAmount = outTableAmount.add(noReturnCurMonthAmountSum);
                            this.provisionStatusModify(noReturnCurMonthAmountMap, repaymentPlanEntityList,
                                    OnOrOffBalanceSheetEnum.OFF.getCode());

                            // 未回笼的表内金额转到表外
                            BigDecimal noReturnIntableAmountSum = this.noReturnAmountSum(noReturnInTableAmountMap);
                            inTableTransferOutTableAmount = inTableTransferOutTableAmount.add(noReturnIntableAmountSum);
                            this.provisionStatusModify(noReturnInTableAmountMap, repaymentPlanEntityList,
                                    OnOrOffBalanceSheetEnum.OFF.getCode());

                            entity.setObserved(YesOrNoEnum.YES.getCode());
                        }
                    }

                    // 2.4 合同退出观察期-三阶段的合同不做退出观察期判定
                    // 2.5 合同非逾期状态
                    if (overdueDays < 90) {
                        // 当月计提表外
                        Map<String, BigDecimal> noReturnCurMonthAmountMap = new HashMap<>();
                        noReturnCurMonthAmountMap.putAll(noReturnCurMonthBeforeAmountMap);
                        noReturnCurMonthAmountMap.putAll(noReturnCurMonthAfterAmountMap);
                        BigDecimal noReturnCurMonthAmountSum = this.noReturnAmountSum(noReturnCurMonthAmountMap);
                        outTableAmount = outTableAmount.add(noReturnCurMonthAmountSum);
                        this.provisionStatusModify(noReturnCurMonthAmountMap, repaymentPlanEntityList,
                                OnOrOffBalanceSheetEnum.OFF.getCode());

                        // 未回笼的表内金额转到表外
                        BigDecimal noReturnIntableAmountSum = this.noReturnAmountSum(noReturnInTableAmountMap);
                        inTableTransferOutTableAmount = inTableTransferOutTableAmount.add(noReturnIntableAmountSum);
                        this.provisionStatusModify(noReturnInTableAmountMap, repaymentPlanEntityList,
                                OnOrOffBalanceSheetEnum.OFF.getCode());
                    }
                    overdueEarning = overdueEarning.add(outTableAmount).add(inTableTransferOutTableAmount);
                } else {
                    // 1 当期在一二阶段
                    // 计提金额
                    Map<String, BigDecimal> noReturnCurMonthAmountMap = new HashMap<>();
                    noReturnCurMonthAmountMap.putAll(noReturnCurMonthBeforeAmountMap);
                    noReturnCurMonthAmountMap.putAll(noReturnCurMonthAfterAmountMap);
                    BigDecimal noReturnCurMonthAmountSum = this.noReturnAmountSum(noReturnCurMonthAmountMap);
                    inTableAmount = inTableAmount.add(noReturnCurMonthAmountSum);
                    this.provisionStatusModify(noReturnCurMonthAmountMap, repaymentPlanEntityList,
                            OnOrOffBalanceSheetEnum.ON.getCode());

                    // 未回笼的表外金额转到表内
                    BigDecimal noReturnOuttableAmountSum = this.noReturnAmountSum(noReturnOutTableAmountMap);
                    outTableTransferInTableAmount = outTableTransferInTableAmount.add(noReturnOuttableAmountSum);
                    this.provisionStatusModify(noReturnOutTableAmountMap, repaymentPlanEntityList,
                            OnOrOffBalanceSheetEnum.ON.getCode());

                    // 进入观察期修改状态
                    if (lastRepaymentPlanEntity != null && lastRepaymentPlanEntity.getObservedExpirationDate() != null) {
                        claimAmountEntity = businessClaimRepaymentRecordService.
                                queryClaimAmountByContract(entity.getContractCode(),
                                        lastRepaymentPlanEntity.getObservedExpirationDate());
                        if (((overdueDays <= 90 && lastRepaymentPlanEntity.getOverdueDays() != null
                                && lastRepaymentPlanEntity.getOverdueDays() > 90)
                                || ((StringUtils.isEmpty(lastRepaymentPlanEntity.getObserved())
                                || YesOrNoEnum.NO.getCode().equals(lastRepaymentPlanEntity.getObserved()))
                                && YesOrNoEnum.YES.getCode().equals(entity.getObserved())))
                                && YesOrNoEnum.NO.getCode().equals(entity.getImpairmentThirdStage())) {
                            entity.setObservedExpirationDate(planDateLastDay);
                            entity.setObserved(YesOrNoEnum.YES.getCode());
                            enterObservePeriod = entity.getPlanDatePeriod();
                            // 观察期转出判定（1.进入观察期次月起完全回笼三期（无论是否含本金,但不包含还款计划金额为0的期次）；
                            // 2. “进入观察期后实际现金流变化”大于0； 3.逾期天数为0 ）
                        } else if (((int) DateUtil.betweenDay(lastRepaymentPlanEntity.getObservedExpirationDate(),
                                entity.getPlanDate(), true) > 90
                                && claimAmountEntity.getClaimAmount().compareTo(BigDecimal.ZERO) > 0 && overdueDays <= 0)
                                || (StringUtils.isNotEmpty(entity.getObserved())
                                || YesOrNoEnum.NO.getCode().equals(entity.getObserved()))) {
                            entity.setObservedExpirationDate(planDateLastDay);
                            entity.setObserved(YesOrNoEnum.NO.getCode());
                        }
                    }
                }

                entity.setOverdueDays(overdueDays);
                // 单月收益计提明细
                Map<String, BigDecimal> createLeaseIncomeDetailsMap = new HashMap<>();
                createLeaseIncomeDetailsMap.put("inTableAmount", inTableAmount);
                createLeaseIncomeDetailsMap.put("outTableAmount", outTableAmount);
                createLeaseIncomeDetailsMap.put("intableTransferOuttableAmount", inTableTransferOutTableAmount);
                createLeaseIncomeDetailsMap.put("outtableTransferIntableAmount", outTableTransferInTableAmount);
                createLeaseIncomeDetailsMap.put("overdueEarning", overdueEarning);
                createLeaseIncomeDetailsMap.put("taAmount", contractTAAmountMap.get(contractCode));
                createLeaseIncomeDetailsMap.put("actualReceivedInterest", actualReceivedInterest);
                createLeaseIncomeDetailsMap.put("beforeInTableAmount", beforeInTableAmount);
                createLeaseIncomeDetailsMap.put("outstandingAmount", outstandingAmount);
                createLeaseIncomeDetailsMap.put("cashChange", claimAmountEntity.getClaimAmount());

                Map<String, Date> createLeaseIncomeDetailsDateMap = new HashMap<>();
                createLeaseIncomeDetailsDateMap.put("planDateLastDay", planDateLastDay);
                createLeaseIncomeDetailsDateMap.put("lastReturnValidDate", lastReturnValidDate);
                createLeaseIncomeDetailsDateMap.put("lastRepaymentDate", lastRepaymentDate);
                createLeaseIncomeDetailsDateMap.put("finalRepaymentDate", lastRepaymentDate);
                createLeaseIncomeDetailsDateMap.put("nextPaymentDate", nextPaymentDate);
                createLeaseIncomeDetailsDateMap.put("repaymentLastPlanDate", lastPlanEntity.getPlanDate());

                Map<String, String> otherParams = new HashMap<>();
                otherParams.put("exceptionType", exceptionType.toString());
                otherParams.put("allProvisionFlag", YesOrNoEnum.NO.getCode());
                if (ObjectUtil.isNotNull(enterObservePeriod)) {
                    otherParams.put("enterObservePeriod", enterObservePeriod.toString());
                }
                if (lastRepaymentPlanEntity == null) {
                    otherParams.put("lastRepaymentPlanEntityIsNull", YesOrNoEnum.YES.getCode());
                } else {
                    otherParams.put("lastRepaymentPlanEntityIsNull", YesOrNoEnum.NO.getCode());
                }

                leaseIncomeDetailsEntities.add(this.createLeaseIncomeDetailsEntity(curMonthRepaymentPlanEntityList,
                        entity, contractEntity, createLeaseIncomeDetailsDateMap, contractBalanceLatestList.get(0),
                        createLeaseIncomeDetailsMap, otherParams));
            }
        }
        log.info("合同：" + contractCode + "计提结束");
    }

    /**
     * 当月收益进行补充计提
     */
    private BigDecimal supplementaryRevenueAccrual(RepaymentPlanEntity entity, String inTableOrOutTable) {
        if (entity.getAmortized() == null || entity.getOnAndOffBalanceSheet() == null) {
            entity.setOnAndOffBalanceSheet(inTableOrOutTable);
            entity.setAmortized(YesOrNoEnum.YES.getCode());
            // 补提金额
            BigDecimal revenueAccrualAmount = BigDecimal.ZERO;
            if (AccrualMethodEnum.RECEIPT.getCode().equals(entity.getIncomeProvisionMethod())) {
                revenueAccrualAmount = revenueAccrualAmount.add(entity.getActualRepaymentRentAmount());
            } else {
                revenueAccrualAmount = revenueAccrualAmount.add(entity.getRentalIncome()).
                        add(entity.getAdjustmentAmount() == null ? BigDecimal.ZERO : entity.getAdjustmentAmount());
            }
            return revenueAccrualAmount;
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 手动逾期处理
     */
    private Map<String, BigDecimal> manualBeOverduProcess(List<RepaymentPlanEntity> repaymentPlanEntityList, RepaymentPlanEntity curEntity) {
        // 表内计提金额
        BigDecimal inTableAmount = BigDecimal.ZERO;
        // 表内转表外计提金额
        BigDecimal inTableTransferOutTableAmount = BigDecimal.ZERO;
        // 表外计提金额
        BigDecimal outTableAmount = BigDecimal.ZERO;
        // 表外转表内计提金额
        BigDecimal outTableTransferInTableAmount = BigDecimal.ZERO;
        // 逾期收益
        BigDecimal overdueEarning = BigDecimal.ZERO;

        for (int i = 0; i < repaymentPlanEntityList.size(); i++) {
            RepaymentPlanEntity entity = repaymentPlanEntityList.get(i);
            if (entity.getPlanDate().compareTo(curEntity.getPreviousPaidPeriod()) >= 0
                    && entity.getPlanDate().compareTo(curEntity.getPlanDate()) <= 0) {
                // 原本表内的计提到表外
                if (OnOrOffBalanceSheetEnum.ON.getCode().equals(entity.getOnAndOffBalanceSheet())) {
                    inTableTransferOutTableAmount = inTableTransferOutTableAmount.add(entity.getRentalIncome());
                    entity.setAmortized(YesOrNoEnum.YES.getCode());
                    entity.setOnAndOffBalanceSheet(OnOrOffBalanceSheetEnum.OFF.getCode());
                } else if (StringUtils.isEmpty(entity.getAmortized())) {
                    // 原本未计提的补计提到表外
                    outTableAmount = outTableAmount.add(entity.getRentalIncome());
                    entity.setAmortized(YesOrNoEnum.YES.getCode());
                    entity.setOnAndOffBalanceSheet(OnOrOffBalanceSheetEnum.OFF.getCode());
                }

                // 计算逾期收益
                overdueEarning = overdueEarning.add(entity.getRentalIncome());
            } else if (entity.getPlanDate().compareTo(curEntity.getPreviousPaidPeriod()) < 0) {
                // 已经回笼在表外则计提到表内
                if (OnOrOffBalanceSheetEnum.OFF.getCode().equals(entity.getOnAndOffBalanceSheet())
                        && RecaptureStatusEnum.RETURNED.getCode().equals(entity.getRecaptureStatus())) {
                    outTableTransferInTableAmount = outTableTransferInTableAmount.add(entity.getRentalIncome());
                    entity.setAmortized(YesOrNoEnum.YES.getCode());
                    entity.setOnAndOffBalanceSheet(OnOrOffBalanceSheetEnum.ON.getCode());
                } else if (StringUtils.isEmpty(entity.getAmortized())
                        && RecaptureStatusEnum.RETURNED.getCode().equals(entity.getRecaptureStatus())) {
                    // 未计提的情况，且回笼计提到表内
                    inTableAmount = inTableAmount.add(entity.getRentalIncome());
                    entity.setAmortized(YesOrNoEnum.YES.getCode());
                    entity.setOnAndOffBalanceSheet(OnOrOffBalanceSheetEnum.ON.getCode());
                } else if (StringUtils.isEmpty(entity.getAmortized())
                        && !RecaptureStatusEnum.RETURNED.getCode().equals(entity.getRecaptureStatus())) {
                    // 未计提的情况，未回笼计提到表外
                    outTableAmount = outTableAmount.add(entity.getRentalIncome());
                    entity.setAmortized(YesOrNoEnum.YES.getCode());
                    entity.setOnAndOffBalanceSheet(OnOrOffBalanceSheetEnum.OFF.getCode());
                }
            }
        }

        Map<String, BigDecimal> amountMap = new HashMap<>();
        amountMap.put("inTableAmount", inTableAmount);
        amountMap.put("inTableTransferOutTableAmount", inTableTransferOutTableAmount);
        amountMap.put("outTableAmount", outTableAmount);
        amountMap.put("outTableTransferInTableAmount", outTableTransferInTableAmount);
        amountMap.put("overdueEarning", overdueEarning);
        return amountMap;
    }

    /**
     * 未回笼数据的表内表外区分
     */
    private void noReturnDataProcess(RepaymentPlanEntity entity, Map<String, BigDecimal> noReturnOutTableAmountMap,
                                     Map<String, BigDecimal> noReturnInTableAmountMap) {
        if (OnOrOffBalanceSheetEnum.OFF.getCode().equals(entity.getOnAndOffBalanceSheet())) {
            noReturnOutTableAmountMap.put(String.valueOf(entity.getId()), entity.getRentalIncome());
//            entity.setAmortized(YesOrNoEnum.YES.getCode());
        } else {
            noReturnInTableAmountMap.put(String.valueOf(entity.getId()), entity.getRentalIncome());
//            entity.setOnAndOffBalanceSheet(OnOrOffBalanceSheetEnum.ON.getCode());
//            entity.setAmortized(YesOrNoEnum.YES.getCode());
        }
    }

    /**
     * 当月数据金额处理
     */
    private void curMonthDataProcess(RepaymentPlanEntity entity, Map<String, BigDecimal> curMonthAmountBeforeMap,
                                     Map<String, BigDecimal> curMonthAmountAfterMap,
                                     Date firstNoReturnDate, Date lastReturnValidDate) {
        // firstNoReturnDate为空表示,正常回笼，且是期初的场景
        if (!YesOrNoEnum.YES.getCode().equals(entity.getAmortized())) {
            if (firstNoReturnDate == null) {
                curMonthAmountBeforeMap.put(String.valueOf(entity.getId()), entity.getRentalIncome().
                        add(entity.getAdjustmentAmount() == null ? BigDecimal.ZERO : entity.getAdjustmentAmount()));
            } else {
                if (lastReturnValidDate != null && entity.getPlanDate().compareTo(lastReturnValidDate) <= 0) {
                    curMonthAmountBeforeMap.put(String.valueOf(entity.getId()), NumberUtil.add(entity.getRentalIncome(),
                            entity.getAdjustmentAmount() == null ? BigDecimal.ZERO : entity.getAdjustmentAmount()));
                } else {
                    curMonthAmountAfterMap.put(String.valueOf(entity.getId()), NumberUtil.add(entity.getRentalIncome(),
                            entity.getAdjustmentAmount() == null ? BigDecimal.ZERO : entity.getAdjustmentAmount()));
                }
            }
        }
    }

    /**
     * 未回笼的表外/表内金额求和
     */
    private BigDecimal noReturnAmountSum(Map<String, BigDecimal> noReturnAmountMap) {
        BigDecimal result = new BigDecimal(0);

        if (noReturnAmountMap != null && !noReturnAmountMap.isEmpty()) {
            Iterator<Map.Entry<String, BigDecimal>> it = noReturnAmountMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, BigDecimal> entry = it.next();
                if (entry.getValue() != null) {
                    result = result.add(entry.getValue());
                }
            }
        }
        return result;
    }

    /**
     * 计提状态的变更
     */
    private void provisionStatusModify(Map<String, BigDecimal> modifyMap,
                                       List<RepaymentPlanEntity> repaymentPlanEntityList, String isOnOrOff) {
        if (modifyMap.size() == 0) {
            return;
        }
        for (RepaymentPlanEntity entity : repaymentPlanEntityList) {
            if (modifyMap.get(String.valueOf(entity.getId())) != null) {
                entity.setOnAndOffBalanceSheet(isOnOrOff);
                entity.setAmortized(YesOrNoEnum.YES.getCode());
            }
        }
    }

    /**
     * 创建租赁收益表详情
     */
    private LeaseIncomeDetailsEntity createLeaseIncomeDetailsEntity(List<RepaymentPlanEntity> curMonthRepaymentPlanEntityList,
                                                                    RepaymentPlanEntity entity, ContractEntity contractEntity,
                                                                    Map<String, Date> createLeaseIncomeDetailsDateMap,
                                                                    ContractBalanceLatestEntity contractBalanceLatestEntity,
                                                                    Map<String, BigDecimal> createLeaseIncomeDetailsParams,
                                                                    Map<String, String> otherParams) {
        BigDecimal inTableAmount = createLeaseIncomeDetailsParams.get("inTableAmount");
        BigDecimal outTableAmount = createLeaseIncomeDetailsParams.get("outTableAmount");
        BigDecimal intableTransferOuttableAmount = createLeaseIncomeDetailsParams.get("intableTransferOuttableAmount");
        BigDecimal outtableTransferIntableAmount = createLeaseIncomeDetailsParams.get("outtableTransferIntableAmount");
        BigDecimal overdueEarning = createLeaseIncomeDetailsParams.get("overdueEarning");
        BigDecimal actualReceivedInterest = createLeaseIncomeDetailsParams.get("actualReceivedInterest");
        BigDecimal beforeInTableAmount = createLeaseIncomeDetailsParams.get("beforeInTableAmount");
        BigDecimal cashChange = createLeaseIncomeDetailsParams.get("cashChange");
        BigDecimal outstandingAmount = createLeaseIncomeDetailsParams.get("outstandingAmount");
        BigDecimal taAmount = createLeaseIncomeDetailsParams.get("taAmount");
        if (taAmount == null) {
            taAmount = BigDecimal.ZERO;
        }

        Date planDateLastDay = createLeaseIncomeDetailsDateMap.get("planDateLastDay");
        String planLastMonth = DateUtils.format(planDateLastDay, "yyyyMM");
        Date previousPaidPeriod = createLeaseIncomeDetailsDateMap.get("lastReturnValidDate");
        Date lastRepaymentDate = createLeaseIncomeDetailsDateMap.get("lastRepaymentDate");
        Date nextPaymentDate = createLeaseIncomeDetailsDateMap.get("nextPaymentDate");
        Date lastRepaymentPlanDate = createLeaseIncomeDetailsDateMap.get("repaymentLastPlanDate");
        Date finalRepaymentDate = createLeaseIncomeDetailsDateMap.get("finalRepaymentDate");

        String exceptionType = otherParams.get("exceptionType");
        String allProvisionFlag = otherParams.get("allProvisionFlag");
        String lastRepaymentPlanEntityIsNull = otherParams.get("lastRepaymentPlanEntityIsNull");
        String specialFlag = otherParams.get("specialFlag");
        String enterObservePeriodStr = Optional.ofNullable(otherParams.get("enterObservePeriod")).orElse("");

        // 异常类型判断
        Date curDate = DateUtil.beginOfDay(DateUtil.date());
        // 当月最后一天之前（不含）起租但当月没有收益摊销的
        if (contractEntity.getLeaseDateStart() != null && contractEntity.getLeaseDateStart().compareTo(curDate) < 0 &&
                BigDecimal.ZERO.compareTo(inTableAmount) == 0 && BigDecimal.ZERO.compareTo(outTableAmount) == 0 &&
                BigDecimal.ZERO.compareTo(intableTransferOuttableAmount) == 0 &&
                BigDecimal.ZERO.compareTo(outtableTransferIntableAmount) == 0) {
            exceptionType.concat("在计提时间之前起租，当月没有计提! | ");
        }
        // 合同签约或撤销状态但仍有当月分摊金额或未分摊金额的
        if (BusinessContractStatusEnum.CONTRACT_STATUS_4.getCode().equals(contractEntity.getContractCode()) &&
                BigDecimal.ZERO.compareTo(inTableAmount) == 0 && BigDecimal.ZERO.compareTo(outTableAmount) == 0 &&
                BigDecimal.ZERO.compareTo(intableTransferOuttableAmount) == 0 &&
                BigDecimal.ZERO.compareTo(outtableTransferIntableAmount) == 0) {
            exceptionType.concat("合同签约，当月未分摊金额! | ");
        }
        if (BusinessContractStatusEnum.CONTRACT_STATUS_5.getCode().equals(contractEntity.getContractCode()) &&
                (BigDecimal.ZERO.compareTo(inTableAmount) != 0 || BigDecimal.ZERO.compareTo(outTableAmount) != 0 ||
                        BigDecimal.ZERO.compareTo(intableTransferOuttableAmount) != 0 ||
                        BigDecimal.ZERO.compareTo(outtableTransferIntableAmount) != 0)) {
            exceptionType.concat("合同撤销，当月出现分摊金额! | ");
        }
        // 逾期计算结果是负数
        if (entity.getOverdueDays() != null && entity.getOverdueDays() > 0 && inTableAmount.compareTo(BigDecimal.ZERO) < 0) {
            exceptionType.concat("逾期计算结果是负数! | ");
        }

        // 期初数据处理-如果合同是期初计提月份结束，则表内表外金额要加上未实现收益
        if (curMonthRepaymentPlanEntityList != null && !curMonthRepaymentPlanEntityList.isEmpty()
                && StringUtils.isNotEmpty(entity.getExceptionType())
                && entity.getExceptionType().contains("期初日期即为计划结束日期")) {
            inTableAmount = inTableAmount.add(outstandingAmount);

            // 上一期不为空，当期为空，则为期初的下月计提数据
        } else if ((curMonthRepaymentPlanEntityList == null || curMonthRepaymentPlanEntityList.isEmpty()) &&
                YesOrNoEnum.NO.getCode().equals(lastRepaymentPlanEntityIsNull)) {
            inTableAmount = inTableAmount.add(outstandingAmount.multiply(new BigDecimal(-1)));
        }


        if (entity.getOverdueDays() == null) {
            entity.setOverdueDays(0);
        }

        // 查询上月是否存在计提数,如果不存在则为期初计提或者起租
        Date lastMonth = DateUtil.offsetMonth(planDateLastDay, -1);
        LeaseIncomeDetailsEntity detailsEntity = detailsService.
                getLeaseIncomeDetailsByMonth(contractEntity.getContractCode(), lastMonth);

        // 应收手续费余额和应收其他收入余额-实收计提时，需要扣减该金额
        if (AccrualMethodEnum.RECEIPT.getCode().equals(entity.getIncomeProvisionMethod())) {
            BigDecimal costTypeExpense = new BigDecimal(0);
            if (contractBalanceLatestEntity != null) {
                BigDecimal receivableCommissionBalance = new BigDecimal(0);
                if (ObjectUtil.isNotNull(contractBalanceLatestEntity.getReceivableCommissionBalance())) {
                    receivableCommissionBalance = contractBalanceLatestEntity.getReceivableCommissionBalance();
                }
                BigDecimal receivableOtherincomeBalance = new BigDecimal(0);
                if (ObjectUtil.isNotNull(contractBalanceLatestEntity.getReceivableOtherincomeBalance())) {
                    receivableOtherincomeBalance = contractBalanceLatestEntity.getReceivableOtherincomeBalance();
                }
                costTypeExpense = costTypeExpense.add(receivableCommissionBalance).add(receivableOtherincomeBalance);
            }
            inTableAmount = inTableAmount.subtract(costTypeExpense);
        }

        // 单月收益计提明细
        // 调整额 = 表内转到表外的金额+表外金额
        BigDecimal overdueAdjustmentAmount = NumberUtil.add(intableTransferOuttableAmount, outTableAmount);
        LeaseIncomeDetailsEntity leaseIncomeDetailsEntity = new LeaseIncomeDetailsEntity();
        leaseIncomeDetailsEntity.setAccountDate(planDateLastDay);
        leaseIncomeDetailsEntity.setBusinessDate(planDateLastDay);
        leaseIncomeDetailsEntity.setSystemCode(entity.getSystemCode());
        leaseIncomeDetailsEntity.setContractCode(contractEntity.getContractCode());
        leaseIncomeDetailsEntity.setClientCode(contractEntity.getClientCode());
        leaseIncomeDetailsEntity.setClientName(contractEntity.getClientName());
        leaseIncomeDetailsEntity.setOrgId(entity.getOrgId());
        leaseIncomeDetailsEntity.setAccrued(YesOrNoEnum.YES.getCode());
        leaseIncomeDetailsEntity.setBusinessType(contractEntity.getBusinessCategory());
        leaseIncomeDetailsEntity.setBusinessCode(contractEntity.getBusinessCode());
        leaseIncomeDetailsEntity.setManualChangeMark(entity.getManualChangeMark());
        leaseIncomeDetailsEntity.setBusinessName(contractEntity.getLeaseType());
        leaseIncomeDetailsEntity.setContractStatus(contractEntity.getContractStatus());
        leaseIncomeDetailsEntity.setFinancialContractStatus(contractEntity.getFinancialContractStatus());
        leaseIncomeDetailsEntity.setLessorOtherCosts(contractEntity.getLessorOtherCosts());
        if (StringUtils.isNotEmpty(enterObservePeriodStr)) {
            leaseIncomeDetailsEntity.setEnterObservePeriod(Integer.valueOf(enterObservePeriodStr));
        }
        leaseIncomeDetailsEntity.setEnterObserveFinalRepaymentDate(finalRepaymentDate);
        if (StringUtils.isNotEmpty(contractEntity.getCurrencyType())
                && "CNY".equals(contractEntity.getCurrencyType())) {
            leaseIncomeDetailsEntity.setCurrencyType("RMB");
        } else {
            leaseIncomeDetailsEntity.setCurrencyType(contractEntity.getCurrencyType());
        }

        if (LeaseTypeEnum.DIRECT.getCode().equals(contractEntity.getLeaseType())) {
            leaseIncomeDetailsEntity.setTaxRate(new BigDecimal(13));
        } else {
            leaseIncomeDetailsEntity.setTaxRate(new BigDecimal(6));
        }
        leaseIncomeDetailsEntity.setXirrRate(AccrualMethodEnum.RECEIPT.getCode().equals(
                entity.getIncomeProvisionMethod()) ? null : entity.getXirrRate());
        leaseIncomeDetailsEntity.setLeaseDateStart(DateUtil.date(contractEntity.getLeaseDateStart()));
        leaseIncomeDetailsEntity.setLeaseDateEnd(DateUtil.date(contractEntity.getLeaseDateEnd()));
        leaseIncomeDetailsEntity.setIncomeProvisionMethod(entity.getIncomeProvisionMethod());
        // 当月租赁收入求和
        BigDecimal curMonthRetenalIncome = this.rentalIncomeSum(curMonthRepaymentPlanEntityList);
        if (curMonthRepaymentPlanEntityList != null && !curMonthRepaymentPlanEntityList.isEmpty()) {
            RepaymentPlanEntity curMonthRepaymentPlanEntity = curMonthRepaymentPlanEntityList.get(
                    curMonthRepaymentPlanEntityList.size() - 1);
            leaseIncomeDetailsEntity.setRentalIncomeBeforeTotal(curMonthRepaymentPlanEntityList.get(0).
                    getRentalIncomeBeforeTotal());
            leaseIncomeDetailsEntity.setUnrealizedRevenue(curMonthRepaymentPlanEntityList.get(0).getUnrealizedRevenue());

            if (detailsEntity == null && curMonthRepaymentPlanEntity.getPlanDate().compareTo(lastRepaymentPlanDate) == 0) {
                if (!AccrualMethodEnum.RECEIPT.getCode().equals(entity.getIncomeProvisionMethod())) {
                    leaseIncomeDetailsEntity.setRentalIncome(curMonthRetenalIncome.add(outstandingAmount));
                    leaseIncomeDetailsEntity.setRentalIncomeAfterTotal(outstandingAmount.multiply(new BigDecimal(-1).add(
                            curMonthRepaymentPlanEntity.getRentalIncomeAfterTotal())));
                } else {
                    leaseIncomeDetailsEntity.setRentalIncome(BigDecimal.ZERO);
                    leaseIncomeDetailsEntity.setRentalIncomeAfterTotal(BigDecimal.ZERO);
                }
            } else {
                if (YesOrNoEnum.YES.getCode().equals(specialFlag)) {
                    leaseIncomeDetailsEntity.setRentalIncomeAfterTotal(BigDecimal.ZERO);
                    leaseIncomeDetailsEntity.setRentalIncome(NumberUtil.add(curMonthRetenalIncome, curMonthRepaymentPlanEntity.getRentalIncomeAfterTotal()));
                } else {
                    leaseIncomeDetailsEntity.setRentalIncomeAfterTotal(curMonthRepaymentPlanEntity.getRentalIncomeAfterTotal());
                    leaseIncomeDetailsEntity.setRentalIncome(curMonthRetenalIncome);
                }
            }
        } else {

            leaseIncomeDetailsEntity.setUnrealizedRevenue(entity.getUnrealizedRevenue());
            if (detailsEntity == null) {
                leaseIncomeDetailsEntity.setRentalIncomeBeforeTotal(entity.getUnrealizedRevenue());
                if (!AccrualMethodEnum.RECEIPT.getCode().equals(entity.getIncomeProvisionMethod())) {
                    leaseIncomeDetailsEntity.setRentalIncomeAfterTotal(outstandingAmount.multiply(new BigDecimal(-1)));
                    leaseIncomeDetailsEntity.setRentalIncome(outstandingAmount);
                } else {
                    leaseIncomeDetailsEntity.setRentalIncome(BigDecimal.ZERO);
                    leaseIncomeDetailsEntity.setRentalIncomeAfterTotal(BigDecimal.ZERO);
                }
            } else {
                leaseIncomeDetailsEntity.setRentalIncomeBeforeTotal(
                        detailsEntity.getRentalIncomeBeforeTotal().add(detailsEntity.getRentalIncome()));
                if (!AccrualMethodEnum.RECEIPT.getCode().equals(entity.getIncomeProvisionMethod())) {
                    leaseIncomeDetailsEntity.setRentalIncomeAfterTotal(BigDecimal.ZERO);
                    leaseIncomeDetailsEntity.setRentalIncome(detailsEntity.getRentalIncomeAfterTotal());
                } else {
                    leaseIncomeDetailsEntity.setRentalIncome(BigDecimal.ZERO);
                    leaseIncomeDetailsEntity.setRentalIncomeAfterTotal(BigDecimal.ZERO);
                }
            }
        }

//        BigDecimal confirmedActualReceipt = actualReceivedInterest.subtract(beforeInTableAmount.add(inTableAmount).
//                subtract(intableTransferOuttableAmount));
//        leaseIncomeDetailsEntity.setConfirmedActualReceipt(confirmedActualReceipt);

        leaseIncomeDetailsEntity.setOverdueDays(entity.getOverdueDays());
        leaseIncomeDetailsEntity.setOverdueEarnings(overdueEarning);
        leaseIncomeDetailsEntity.setObserved(entity.getObserved());
        leaseIncomeDetailsEntity.setObservedExpirationDate(entity.getObservedExpirationDate());
        if (YesOrNoEnum.YES.getCode().equals(entity.getLaborOverdueMark())) {
            leaseIncomeDetailsEntity.setLaborOverdueMark(entity.getLaborOverdueMark());
            leaseIncomeDetailsEntity.setLaborOverdueDays(entity.getLaborOverdueDays());
            leaseIncomeDetailsEntity.setPreviousPaidPeriod(entity.getPreviousPaidPeriod());
        } else {
            leaseIncomeDetailsEntity.setPreviousPaidPeriod(previousPaidPeriod);
        }
        leaseIncomeDetailsEntity.setRentalIncomeOnBalance(inTableAmount);
        leaseIncomeDetailsEntity.setRentalIncomeOffBalance(outTableAmount);
        // detailsEntity为空表示期初数据初始化
        String historyOverdue = planLastMonth.concat(":").concat(String.valueOf(leaseIncomeDetailsEntity.getOverdueDays()));
        if (detailsEntity == null) {
            if (!AccrualMethodEnum.RECEIPT.getCode().equals(entity.getIncomeProvisionMethod())) {
                leaseIncomeDetailsEntity.setOverdueAdjustmentAmount((
                        outTableAmount.add(intableTransferOuttableAmount)).multiply(new BigDecimal(-1)));
                leaseIncomeDetailsEntity.setLastMonthOverdueEarnings(BigDecimal.ZERO);
            } else {
                leaseIncomeDetailsEntity.setOverdueAdjustmentAmount(BigDecimal.ZERO);
                leaseIncomeDetailsEntity.setLastMonthOverdueEarnings(BigDecimal.ZERO);
                leaseIncomeDetailsEntity.setOverdueEarnings(BigDecimal.ZERO);
            }
            leaseIncomeDetailsEntity.setHistoryOverdue(historyOverdue);
        } else {
            if (!AccrualMethodEnum.RECEIPT.getCode().equals(entity.getIncomeProvisionMethod())) {
                leaseIncomeDetailsEntity.setLastMonthOverdueEarnings(detailsEntity.getOverdueEarnings());
                leaseIncomeDetailsEntity.setOverdueAdjustmentAmount(leaseIncomeDetailsEntity.getLastMonthOverdueEarnings().
                        subtract(leaseIncomeDetailsEntity.getOverdueEarnings()));
            } else {
                // 计提方式如果是实收,当月逾期调整额等于0减去逾期收益
                leaseIncomeDetailsEntity.setOverdueAdjustmentAmount(BigDecimal.ZERO.subtract(leaseIncomeDetailsEntity.getOverdueEarnings()));
                leaseIncomeDetailsEntity.setLastMonthOverdueEarnings(BigDecimal.ZERO);
                leaseIncomeDetailsEntity.setOverdueEarnings(BigDecimal.ZERO);
            }
            leaseIncomeDetailsEntity.setHistoryOverdue(detailsEntity.getHistoryOverdue().concat("|").
                    concat(historyOverdue));
        }

        if (YesOrNoEnum.YES.getCode().equals(allProvisionFlag) ||
                AccrualMethodEnum.RECEIPT.getCode().equals(entity.getIncomeProvisionMethod())) {
            leaseIncomeDetailsEntity.setTotalRecordedAmount(inTableAmount);
        } else {
            leaseIncomeDetailsEntity.setTotalRecordedAmount(NumberUtil.add(leaseIncomeDetailsEntity.getRentalIncome(),
                    leaseIncomeDetailsEntity.getOverdueAdjustmentAmount()));
        }

        leaseIncomeDetailsEntity.setInvoicingFlag(contractEntity.getInvoicingFlag());
        leaseIncomeDetailsEntity.setIntableTransferOuttableAmount(intableTransferOuttableAmount);
        leaseIncomeDetailsEntity.setOuttableTransferIntableAmount(outtableTransferIntableAmount);
        leaseIncomeDetailsEntity.setTaAmount(taAmount);
        leaseIncomeDetailsEntity.setLastRepaymentDate(lastRepaymentDate);
        leaseIncomeDetailsEntity.setNextRepaymentDate(nextPaymentDate);
        if (actualReceivedInterest != null) {
            if (LeaseTypeEnum.DIRECT.getCode().equals(contractEntity.getLeaseType())) {
                leaseIncomeDetailsEntity.setPaidInterest(actualReceivedInterest.
                        divide(Constants.DIRECT_RATE, 2, RoundingMode.HALF_UP));
            } else {
                leaseIncomeDetailsEntity.setPaidInterest(actualReceivedInterest.
                        divide(Constants.LEASEBACK_RATE, 2, RoundingMode.HALF_UP));
            }
        } else {
            leaseIncomeDetailsEntity.setPaidInterest(BigDecimal.ZERO);
        }
        leaseIncomeDetailsEntity.setConfirmedIncome(leaseIncomeDetailsEntity.getRentalIncomeBeforeTotal().
                add(leaseIncomeDetailsEntity.getRentalIncome()).subtract(leaseIncomeDetailsEntity.getOverdueEarnings()));

        // 实收手续费
        if (contractEntity.getPaidHandlingFees() == null) {
            contractEntity.setPaidHandlingFees(BigDecimal.ZERO);
        }
        if (contractEntity.getReceivableVendorProcedureAmount() == null) {
            contractEntity.setReceivableVendorProcedureAmount(BigDecimal.ZERO);
        }
        BigDecimal paidHandingFeesNotIncTax = BigDecimal.ZERO;
        if (LeaseTypeEnum.DIRECT.getCode().equals(contractEntity.getLeaseType())) {
            paidHandingFeesNotIncTax = (contractEntity.getPaidHandlingFees().
                    add(contractEntity.getReceivableVendorProcedureAmount())).
                    divide(Constants.DIRECT_RATE, 2, RoundingMode.HALF_UP);
        } else {
            paidHandingFeesNotIncTax = (contractEntity.getPaidHandlingFees().
                    add(contractEntity.getReceivableVendorProcedureAmount())).
                    divide(Constants.LEASEBACK_RATE, 2, RoundingMode.HALF_UP);
        }
        leaseIncomeDetailsEntity.setPaidHandlingFees(paidHandingFeesNotIncTax);

        if (contractEntity.getOtherIncome() != null) {
            leaseIncomeDetailsEntity.setOtherIncome(contractEntity.getOtherIncome().
                    divide(Constants.LEASEBACK_RATE, 2, RoundingMode.HALF_UP));
        } else {
            leaseIncomeDetailsEntity.setOtherIncome(BigDecimal.ZERO);
        }
        leaseIncomeDetailsEntity.setConfirmedActualReceipt(
                leaseIncomeDetailsEntity.getPaidInterest().
                        subtract(leaseIncomeDetailsEntity.getConfirmedIncome()).
                        add(leaseIncomeDetailsEntity.getPaidHandlingFees().
                                add(leaseIncomeDetailsEntity.getOtherIncome())));
        leaseIncomeDetailsEntity.setExceptionType(exceptionType);
        leaseIncomeDetailsEntity.setCashChange(cashChange == null ? BigDecimal.ZERO : cashChange);
        leaseIncomeDetailsEntity.setDelFlag(YesOrNoEnum.NO.getCode());
        leaseIncomeDetailsEntity.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());

        return leaseIncomeDetailsEntity;
    }

    /**
     * 本月rentail income求和
     */
    private BigDecimal rentalIncomeSum(List<RepaymentPlanEntity> curMonthRepaymentPlanEntityList) {
        BigDecimal result = new BigDecimal(0);
        if (curMonthRepaymentPlanEntityList != null && !curMonthRepaymentPlanEntityList.isEmpty()) {
            for (RepaymentPlanEntity entity : curMonthRepaymentPlanEntityList) {
                if (entity.getRentalIncome() != null) {
                    result = result.add(entity.getRentalIncome());
                }
            }
        }
        return result;
    }

    private List<LeaseIncomeDetailsEntity> generateLeaseIncomeDetails(
            List<RepaymentPlanEntity> allRepaymentPlanEntities, Date queryDate,
            Map<String, ContractEntity> contractEntityMap,
            List<ContractBalanceLatestEntity> allContractBalanceLatestEntities,
            List<String> noLeaseStatusList,
            Map<String, BigDecimal> contractTAAmountMap,
            Map<String, BigDecimal> outstandingAmountMap) {

        Integer planDatePeriod = Integer.valueOf(DateUtil.format(queryDate, DatePattern.SIMPLE_MONTH_PATTERN));
        List<LeaseIncomeDetailsEntity> leaseIncomeDetailsEntities = new ArrayList<>();
        Map<String, List<RepaymentPlanEntity>> repaymentMap = allRepaymentPlanEntities.stream().
                sorted(Comparator.comparing(RepaymentPlanEntity::getPlanDate)).
                collect(Collectors.groupingBy(e -> e.getContractCode()));
        Map<String, List<ContractBalanceLatestEntity>> contractRentMap = allContractBalanceLatestEntities.stream().
                collect(Collectors.groupingBy(e -> this.getMapKey(e.getContractCode(), e.getOrgId())));
//        Map<String, LeaseIncomeDetailsEntity> lastDetailsMap = lastDetails.stream().
//                collect(Collectors.toMap(e -> e.getContractCode(), e -> e, (a, b) -> b));

        List<RepaymentPlanProvisionEntity> repaymentPlanProvisionEntityList = new ArrayList<>();
        repaymentMap.forEach((contractCode, planEntities) -> {
            try {
                ContractEntity contractEntity = contractEntityMap.get(this.getMapKey(contractCode, planEntities.get(0).getOrgId()));
                if (null == contractEntity) {
                    log.info("收益计提 无合同数据:{}", contractCode);
                    List<RepaymentPlanProvisionEntity> RepaymentPlanProvisionEntityList = BeanUtil.copyToList(
                            planEntities, RepaymentPlanProvisionEntity.class);

                    RepaymentPlanProvisionEntityList.stream().forEach(e -> {
                        e.setExceptionType("无合同数据");
                    });
//                    repaymentPlanProvisionService.repaymentPlanDataByIds(RepaymentPlanProvisionEntityList);
                    repaymentPlanProvisionService.saveBatch(RepaymentPlanProvisionEntityList);
                    return;
                }

                // 当合同已结束/已结清、应收租金余额为0时，摊销表的数据未摊销的收益及转入表外的收益，全额进行计提；不受计提方式、逾期状态、观察期状态的影响
//                if (FinancialContractStatusLeaseIncomeEnum.getStatus().contains(contractEntity.getContractStatus())) {
//                    this.closeOffContract(planEntities, contractEntity, contractRentMap, leaseIncomeDetailsEntities, planDatePeriod);
//                    return;
//                }

                // 合同状态为资产出表后赎回、在执行合同抵债资产处置、部分处置、拍卖部分处置（未入库）、入库后赎回、
                // 资产处置结束（转让至恒信）、资产出表（到期）等时，不做计提；
                if (noLeaseStatusList.contains(contractEntity.getFinancialContractStatus())) {
                    return;
                }

                // 取得未实现收益总额
                BigDecimal outstandingAmount = outstandingAmountMap.get(contractEntity.getContractCode());
                if (outstandingAmount == null) {
                    outstandingAmount = BigDecimal.ZERO;
                }

                this.provisionProcess(planEntities, planDatePeriod, contractTAAmountMap, contractEntity,
                        contractRentMap, leaseIncomeDetailsEntities, outstandingAmount);

                repaymentPlanProvisionEntityList.addAll(BeanUtil.copyToList(
                        planEntities, RepaymentPlanProvisionEntity.class));

//                log.info("收益计提处理成功" + contractCode);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.error("收益计提处理错误 合同号 :{} 错误信息" + e, contractCode);
            }
        });
//        repaymentPlanProvisionService.repaymentPlanDataByIds(repaymentPlanProvisionEntityList);
        repaymentPlanProvisionService.saveBatch(repaymentPlanProvisionEntityList);

        repaymentPlanProvisionEntityList.clear();
        repaymentMap.clear();
        return leaseIncomeDetailsEntities;
    }

    /**
     * 删除已经生成计提的数据（计提未提交的数据）
     *
     * @param queryDTO
     * @return 不能重新生成计提的签约主体
     */
    private List<String> deleteNotSubmitAndFilterSubmitedOrg(LeaseIncomeQueryDTO queryDTO) {
        List<String> notGenerateOrgIds = new ArrayList<>();
        List<LeaseIncomeEntity> leaseIncomeEntities = getBaseMapper().selectList(Wrappers.<LeaseIncomeEntity>lambdaQuery()
                .eq(LeaseIncomeEntity::getBusinessDate, queryDTO.getBusinessDate())
                .in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), LeaseIncomeEntity::getOrgId, queryDTO.getOrgIdList()));
        if (CollectionUtils.isNotEmpty(leaseIncomeEntities)) {
            // 删除未提交数据
            List<Long> deleteIds = leaseIncomeEntities.stream().filter(e -> MarginStatusEnum.canChangeStatus().
                    contains(e.getProcessStatus())).map(e -> e.getId()).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deleteIds)) {
                // 删除汇总数据
//                LambdaUpdateWrapper<LeaseIncomeEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
//                updateChainWrapper
//                        .in(LeaseIncomeEntity::getId, deleteIds)
//                        .set(LeaseIncomeEntity::getUpdateTime, LocalDateTime.now())
//                        .set(LeaseIncomeEntity::getDelFlag, YesOrNoEnum.YES.getCode());
                this.getBaseMapper().deleteBatchIds(deleteIds);

                // 删除凭证
                this.delVoucherByIds(deleteIds);

                // 删除明细数据
                LambdaQueryWrapper<LeaseIncomeDetailsEntity> detailUpdateChainWrapper = new LambdaQueryWrapper<>();
                detailUpdateChainWrapper
                        .in(LeaseIncomeDetailsEntity::getLeaseIncomeId, deleteIds);
//                        .set(LeaseIncomeDetailsEntity::getUpdateTime, LocalDateTime.now())
//                        .set(LeaseIncomeDetailsEntity::getDelFlag, YesOrNoEnum.YES.getCode());
                detailsService.remove(detailUpdateChainWrapper);

                // 复核失败后重新生成时，不保留旧审批版本。
                approveMapper.physicalDeleteByDocuments(deleteIds, BatchTypeEnum.SYJT.getCode());
            }
            // 获取不生成数据的公司
            notGenerateOrgIds = leaseIncomeEntities.stream().filter(e -> MarginStatusEnum.cantChangeStatus().
                    contains(e.getProcessStatus())).map(e -> e.getOrgId()).distinct().collect(Collectors.toList());
        }
        return notGenerateOrgIds;
    }

    /**
     * 凭证删除
     */
    private void delVoucherByIds(List<Long> deleteIds) {
        LambdaQueryWrapper<LeaseIncomeDetailsEntity> detailQueryWrapper = new LambdaQueryWrapper();
        detailQueryWrapper.in(LeaseIncomeDetailsEntity::getLeaseIncomeId, deleteIds);
        List<LeaseIncomeDetailsEntity> detailsEntityList = detailsService.getBaseMapper().selectList(detailQueryWrapper);
        if (detailsEntityList != null && !detailsEntityList.isEmpty()) {
            List<String> voucherIds = detailsEntityList.stream().filter(e -> StringUtils.isNotEmpty(e.getVoucherId())).
                    map(e -> e.getVoucherId()).collect(Collectors.toList());
            if (voucherIds != null && !voucherIds.isEmpty()) {
                voucherIds.stream().forEach(e -> {
                    List<Long> tempVoucherIds = new ArrayList<>();
                    tempVoucherIds.addAll(Arrays.asList(e.split(",")).stream().map(Long::parseLong).collect(Collectors.toList()));
                    voucherService.deleteByIdList(tempVoucherIds);
                });
            }
        }
    }

//    private void handleChange(List<RepaymentPlanHisEntity> repaymentPlanHisEntities, Map<String, ContractEntity> contractEntityMap) {
//        if (!repaymentPlanHisEntities.isEmpty()) {
//            Map<String, List<RepaymentPlanHisEntity>> planHisMap = repaymentPlanHisEntities.stream().collect(Collectors.groupingBy(e -> e.getContractCode()));
//            // 处理变更
//            planHisMap.forEach((contractCode, planHisEntities) -> {
//                ContractEntity contractEntity = contractEntityMap.get(contractCode);
//                List<RepaymentPlanSaveDTO> repaymentPlanSaveDTOS = repaymentPlanService.handleChange(contractEntity, planHisEntities);
//
//                //更新数据 删除变更日后没有期数的数据
//                repaymentPlanService.deleteGEChangeDataByCode(planHisEntities.get(0).getChangeDate(), contractCode);
//                repaymentPlanSaveDTOS.forEach(e -> {
//                    //在变更日期后的 含期数的 覆盖原始数据，不含期数的删除再新增
//                    if (null != e.getPeriods()) {
//                        RepaymentPlanSaveDTO saveDTO = repaymentPlanService.selectByContractCodeAndPeriods(contractCode, e.getPeriods());
//                        repaymentPlanService.updateRepaymentPlan(saveDTO.getId(), saveDTO);
//                    } else {
//                        repaymentPlanService.saveRepaymentPlan(e);
//                    }
//                });
//            });
//        }
//    }

    @Override
    public R<String> importData(List<LeaseIncomeImport> list) {
        List<LeaseIncomeUploadRecordEntity> entities = new ArrayList<>();
        for (LeaseIncomeImport incomeImport : list) {
            LeaseIncomeUploadRecordEntity entity = BeanUtil.copyProperties(incomeImport, LeaseIncomeUploadRecordEntity.class);
            entity.setId(IdWorker.getId());
            entity.setCreateBy(SecurityUtils.getUsername());
            entity.setCreateTime(DateUtil.date());
            entity.setUpdateBy(SecurityUtils.getUsername());
            entity.setUpdateTime(DateUtil.date());
            entities.add(entity);

            Date queryDate = incomeImport.getBusinessDate();
            String contractCode = incomeImport.getContractCode();
            if (null == queryDate || StringUtils.isBlank(contractCode)) {
                continue;
            }

            // 是否做了计提方式改变
            String isChangeProvisionMethodResult = repaymentPlanService.incomeProvisionMethodChange(incomeImport);
            if (!"ok".equals(isChangeProvisionMethodResult)) {
                log.error(isChangeProvisionMethodResult);
                throw new ServiceException(isChangeProvisionMethodResult);
            }

            // 处理 偿还计划
            List<RepaymentPlanEntity> repaymentPlanEntities = repaymentPlanService.getBaseMapper().selectList(Wrappers.<RepaymentPlanEntity>lambdaQuery()
                    .eq(RepaymentPlanEntity::getContractCode, contractCode));
            handelImportRepaymentPlan(incomeImport, repaymentPlanEntities);
        }

        // 保存上传记录
        leaseIncomeUploadRecordService.saveBatch(entities);

        log.info("上传完成!");
        return R.ok("上传完成!");
    }

    /**
     * 更新偿还计划的数据
     */
    private void handelImportRepaymentPlan(LeaseIncomeImport incomeImport,
                                           List<RepaymentPlanEntity> repaymentPlanEntities) {
        // 该计提月份之后 偿还计划
        String businessDate = DateUtils.format(incomeImport.getBusinessDate(), "yyyy-MM");
        if (CollectionUtils.isNotEmpty(repaymentPlanEntities)) {
            for (RepaymentPlanEntity entity : repaymentPlanEntities) {
//                String planDate = DateUtils.format(entity.getPlanDate(), "yyyy-MM");
                if (entity.getPlanDate().compareTo(incomeImport.getBusinessDate()) == 0) {
                    if (StringUtils.isNotEmpty(incomeImport.getAccrued())) {
                        entity.setAccrued(YesOrNoEnum.getCodeByDesc(incomeImport.getAccrued()));
                    }
                    if (StringUtils.isNotEmpty(incomeImport.getLaborOverdueMark())) {
                        entity.setLaborOverdueMark(incomeImport.getLaborOverdueMark());
                    }
                    if (StringUtils.isNotEmpty(incomeImport.getObserved())) {
                        entity.setObserved(incomeImport.getObserved());
//                        entity.setObservedExpirationDate(incomeImport.getObservedExpirationDate());
                    }
                    if (incomeImport.getPreviousPaidPeriod() != null) {
                        entity.setPreviousPaidPeriod(incomeImport.getPreviousPaidPeriod());
                    }
//                    if (StringUtils.isNotEmpty(incomeImport.getObserved())
//                            && entity.getPlanDate().compareTo(incomeImport.getObservedExpirationDate()) <= 0) {
//                        entity.setObserved(incomeImport.getObserved());
//                        entity.setObservedExpirationDate(incomeImport.getObservedExpirationDate());
//                    }
                    if (StringUtils.isNotEmpty(incomeImport.getIncomeProvisionMethod())) {
                        entity.setIncomeProvisionMethod(AccrualMethodEnum.getCodeByDesc(incomeImport.getIncomeProvisionMethod()));
                    }
                }
            }
            repaymentPlanService.saveOrUpdateBatch(repaymentPlanEntities);
        }
        // 偿还计划的日期是否大于导入的计提月份,修改计提方式
        List<RepaymentPlanEntity> updateRepaymentPlanEntities = repaymentPlanEntities.stream()
                .filter(e -> DateUtil.beginOfMonth(e.getPlanDate()).compareTo(incomeImport.getBusinessDate()) >= 0)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(updateRepaymentPlanEntities)) {
            updateRepaymentPlanEntities.forEach(e -> {
                e.setIncomeProvisionMethod(AccrualMethodEnum.getCodeByDesc(incomeImport.getIncomeProvisionMethod()));
            });
            repaymentPlanService.saveOrUpdateBatch(updateRepaymentPlanEntities);
        }
        // 如果偿还计划为空则用合同编号直接更新合同信息
        if (CollectionUtils.isEmpty(repaymentPlanEntities)) {
            List<ContractEntity> contractEntities = contractService.getContractDTOByCode(incomeImport.getContractCode());
            if (CollectionUtils.isNotEmpty(contractEntities)) {
                for (ContractEntity contractEntity : contractEntities) {
                    contractEntity.setIncomeProvisionMethod(AccrualMethodEnum.getCodeByDesc(incomeImport.getIncomeProvisionMethod()));
                    contractEntity.setUpdateTime(LocalDateTime.now());
                }
                contractService.updateBatchById(contractEntities);
            }
            List<ContractMonthEntity> contractDTOByCode = contractMonthService.getContractDTOByCode(incomeImport.getContractCode());
            if (CollectionUtils.isNotEmpty(contractDTOByCode)) {
                for (ContractMonthEntity contractMonthEntity : contractDTOByCode) {
                    contractMonthEntity.setIncomeProvisionMethod(AccrualMethodEnum.getCodeByDesc(incomeImport.getIncomeProvisionMethod()));
                    contractMonthEntity.setUpdateTime(LocalDateTime.now());
                }
                contractMonthService.updateBatchById(contractDTOByCode);
            }
            return;
        }
        // 合同表更新计提方式
        String orgId = repaymentPlanEntities.get(0).getOrgId();
        ContractDTO contractDTO = contractService.getContractDTOByCode(incomeImport.getContractCode(), orgId);
        ContractEntity contractEntity = BeanUtil.copyProperties(contractDTO, ContractEntity.class);
        contractEntity.setIncomeProvisionMethod(AccrualMethodEnum.getCodeByDesc(incomeImport.getIncomeProvisionMethod()));
        contractEntity.setUpdateTime(LocalDateTime.now());
        contractService.getBaseMapper().updateById(contractEntity);
        ContractMonthDTO contractDTOByCode = contractMonthService.getContractDTOByCode(incomeImport.getContractCode(), orgId);
        ContractMonthEntity contractMonthEntity = BeanUtil.copyProperties(contractDTOByCode, ContractMonthEntity.class);
        contractMonthEntity.setIncomeProvisionMethod(AccrualMethodEnum.getCodeByDesc(incomeImport.getIncomeProvisionMethod()));
        contractMonthEntity.setUpdateTime(LocalDateTime.now());
        contractMonthService.getBaseMapper().updateById(contractMonthEntity);

    }

    private void handleImportLeaseIncomeDetail(LeaseIncomeImport incomeImport, LeaseIncomeDetailsEntity entity) {
        if (null == entity) {
            return;
        }
        // 当月变为计提，则需要将前期进行补提
        if (StringUtils.isNotBlank(incomeImport.getAccrued())) {
            entity.setAccrued(incomeImport.getAccrued());
        }
        if (null != incomeImport.getPreviousPaidPeriod()) {
            entity.setPreviousPaidPeriod(incomeImport.getPreviousPaidPeriod());
            // 把该实收期间前未回笼数据置为已回笼
            RepaymentPlanQueryDTO repaymentPlanQueryDTO = new RepaymentPlanQueryDTO();
            repaymentPlanQueryDTO.setContractCode(entity.getContractCode());
            repaymentPlanQueryDTO.setPlanDateBeforeEq(incomeImport.getPreviousPaidPeriod());
            repaymentPlanQueryDTO.setHasPeriods(true);
            repaymentPlanQueryDTO.setRecaptureStatusList(RecaptureStatusEnum.overDueStatus());
            List<RepaymentPlanVO> beforeDateRepaymentPlanVOS = repaymentPlanService.selectList(repaymentPlanQueryDTO);
            beforeDateRepaymentPlanVOS.forEach(repaymentPlanVO -> {
                repaymentPlanVO.setRecaptureStatus(RecaptureStatusEnum.RETURNED.getCode());
                repaymentPlanService.updateRepaymentPlan(repaymentPlanVO.getId(), repaymentPlanVO);
            });
        }
        if (StringUtils.isNotBlank(incomeImport.getLaborOverdueMark())) {
            entity.setLaborOverdueMark(incomeImport.getLaborOverdueMark());
        }
//        if (StringUtils.isNotBlank(incomeImport.getObserved())) {
//            entity.setObserved(incomeImport.getObserved());
//        }
//        if (null != incomeImport.getObservedExpirationDate()) {
//            entity.setObservedExpirationDate(incomeImport.getObservedExpirationDate());
//        }
        if (StringUtils.isNotBlank(incomeImport.getIncomeProvisionMethod())) {
            entity.setIncomeProvisionMethod(AccrualMethodEnum.getDescByCode(incomeImport.getIncomeProvisionMethod()));
        }
        if (StringUtils.isNotBlank(incomeImport.getComment())) {
            entity.setComment(incomeImport.getComment());
        }
//        if (StringUtils.isNotBlank(incomeImport.getProcessMethod())) {
//            entity.setProcessMethod(incomeImport.getProcessMethod());
//        }
    }

    @Override
    public void voucher(List<Long> ids, String isSubmit) {
        // 删除凭证
        this.delVoucherByIds(ids);

        List<LeaseIncomeEntity> serviceFeeEntities = listByIds(ids);
        for (LeaseIncomeEntity leaseIncomeEntity : serviceFeeEntities) {

            List<LeaseIncomeDetailsEntity> detailsEntities = detailsService.getBaseMapper().selectList(Wrappers.<LeaseIncomeDetailsEntity>lambdaQuery()
                    .eq(LeaseIncomeDetailsEntity::getLeaseIncomeId, leaseIncomeEntity.getId())
                    .eq(LeaseIncomeDetailsEntity::getDelFlag, YesOrNoEnum.NO.getCode()));
            if (CollectionUtils.isEmpty(detailsEntities)) {
                throw new ServiceException("缺少计提信息");
            }
            // 查询合同信息
            List<ContractDTO> contractDTOS = contractService.listContractDTOByCodeList(detailsEntities.stream().
                    map(e -> e.getContractCode()).distinct().collect(Collectors.toList()));
            Map<String, ContractDTO> contractMap = contractDTOS.stream().collect(Collectors.toMap(
                    e -> e.getContractCode().concat("|").concat(
                            StringUtils.isNotEmpty(e.getOrgId()) ? e.getOrgId() : ""), e -> e));

            // 分批次生成凭证-每次凭证生成后,就提交DB
            // 循环次数
            int loopCount = getGenVoucherLoopCount(detailsEntities.size());
            log.info("收益计提凭证生成总循环次数:" + loopCount);

            boolean isSuccess = true;
            for (int i = 0; i < loopCount; i++) {
                log.info("收益计提凭证生成当前批次:" + i);

                int fromIndex = i * EACH_GEN_VOUCHER_NUM;
                int toIndex = 0;
                if (i == loopCount - 1) {
                    toIndex = detailsEntities.size();
                } else {
                    toIndex = (i + 1) * EACH_GEN_VOUCHER_NUM;
                }
                // 每次取500的数据量进行凭证生成
                List<LeaseIncomeDetailsEntity> newDetailEntites = detailsEntities.subList(fromIndex, toIndex);
                boolean genVoucherResult = leaseIncomeNewTransactionService.genVoucher(
                        isSubmit, newDetailEntites, contractMap);
                if (!genVoucherResult) {
                    isSuccess = false;
                }
            }

            // 更新主表状态
            this.updateLeaseIncomeStatus(isSubmit, leaseIncomeEntity, isSuccess);
        }
    }

    @Async
    public void multiThreadGenVoucher(List<Long> ids, String isSubmit) {

        // 将科目数据存入redis中
        accountService.selectAllAccountToRedis();

        List<LeaseIncomeEntity> serviceFeeEntities = listByIds(ids);
        for (LeaseIncomeEntity leaseIncomeEntity : serviceFeeEntities) {

            int detailsEntitiesSize = Integer.parseInt(detailsService.getBaseMapper().selectCount(Wrappers.<LeaseIncomeDetailsEntity>lambdaQuery()
                    .eq(LeaseIncomeDetailsEntity::getLeaseIncomeId, leaseIncomeEntity.getId())
                    .eq(LeaseIncomeDetailsEntity::getDelFlag, YesOrNoEnum.NO.getCode())).toString());
            log.info("Detail Entity 数据量：" + detailsEntitiesSize);
            if (detailsEntitiesSize == 0) {
                throw new ServiceException("缺少计提信息");
            }

            // 线程分析
            int threadNum = MAX_NUM_PAMAXRTITIONS; // 线程数 数据小于1000,则使用1个线程,最多20个线程
            if (detailsEntitiesSize < 200) {
                threadNum = 1;
            } else {
                int isHaveRemain = 0;
                if (detailsEntitiesSize % 200 != 0) {
                    isHaveRemain = 1;
                }
                threadNum = Math.min((detailsEntitiesSize / 200) + isHaveRemain, MAX_NUM_PAMAXRTITIONS);
            }
            // 计算每个分区的数据量
            int numPerPartition = detailsEntitiesSize / threadNum;

            boolean isSuccess = true;
            List<CompletableFuture<Boolean>> completableFutures = new ArrayList<>();
            for (int i = 0; i < threadNum; i++) {

                int fromIndex = i * numPerPartition;
                int toIndex = 0;
                if (i == threadNum - 1) {
                    toIndex = detailsEntitiesSize;
                } else {
                    toIndex = (i + 1) * numPerPartition;
                }
                log.info("收益计提凭证生成当前线程数据分配，线程：" + i + ";fromIndex=" + fromIndex + ", toIndex=" + toIndex);

                // 每次取相应的数据量进行凭证生成(创建线程)
//                List<LeaseIncomeDetailsEntity> newDetailEntites = detailsEntities.subList(fromIndex, toIndex);

                int finalToIndex = toIndex;
                CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
                    return createThreadForGenVoucher(fromIndex, finalToIndex, isSubmit, leaseIncomeEntity.getId());
                }, asyncTaskExecutor);
                completableFutures.add(future);
            }

            // 将所有CompletableFuture组合成一个新的CompletableFuture，并等待所有线程任务完成
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));

            // 等待所有线程任务完成
            allFutures.join();

            // 更新主表状态-最终执行结果更新
            for (Future<Boolean> eachResult : completableFutures) {
                try {
                    if (!eachResult.get()) {
                        isSuccess = false;
                    }
                } catch (Exception e) {
                    isSuccess = false;
                }
            }
            this.updateLeaseIncomeStatus(isSubmit, leaseIncomeEntity, isSuccess);
        }
    }

    private List<LeaseIncomeDetailsEntity> selectLeaseIncomeDetailsData(int fromIndex, int toIndex, Long leaseIncomeId) {
        SelectDetailsByPageDTO params = new SelectDetailsByPageDTO();
        params.setLeaseIncomeId(leaseIncomeId);
        params.setSize(toIndex - fromIndex);
        params.setStartIndex(fromIndex);
        List<LeaseIncomeDetailsEntity> detailsEntityPage = detailsMapper.selectDetailsByPage(params);
        if (detailsEntityPage == null || detailsEntityPage.isEmpty()) {
            return new ArrayList<>();
        } else {
            return detailsEntityPage;
        }
    }


    public Boolean createThreadForGenVoucher(int startIndex, int endIndex, String isSubmit, Long leaseIncomeId) {
        Boolean result = true;
        Map<String, Object> params = new HashMap<>();

        List<LeaseIncomeDetailsEntity> newDetailEntites = this.selectLeaseIncomeDetailsData(startIndex, endIndex, leaseIncomeId);

        // 查询场景信息
        List<String> assistFlagsList = sceneVoucherEntryService.
                selectDistinctAssistFlagsBySceneCode(SceneEnum.SYJT.getCode());
        params.put("assistFlagsList", assistFlagsList);

        Map<String, Object> sceneFieldsMap = sceneFieldsService.selectSceneFieldsMapByCode(SceneEnum.SYJT.getCode());
        params.put("sceneFieldsMap", sceneFieldsMap);

        // 每次取EACH_GEN_VOUCHER_NUM个合同进行凭证生成
        int loopCount = getGenVoucherLoopCount(newDetailEntites.size());
        for (int i = 0; i < loopCount; i++) {
            log.info("收益计提凭证生成当前批次:" + i);

            int fromIndex = i * EACH_GEN_VOUCHER_NUM;
            int toIndex = 0;
            if (i == loopCount - 1) {
                toIndex = newDetailEntites.size();
            } else {
                toIndex = (i + 1) * EACH_GEN_VOUCHER_NUM;
            }
            // 每次取500的数据量进行凭证生成
            List<LeaseIncomeDetailsEntity> subNewDetailEntites = newDetailEntites.subList(fromIndex, toIndex);

            // 合同编码列表
            List<String> contractCodeList = subNewDetailEntites.stream().map(e -> e.getContractCode()).distinct().
                    collect(Collectors.toList());

            // 查询合同信息
            List<ContractDTO> contractDTOS = contractService.listContractDTOByCodeList(contractCodeList);
            Map<String, ContractDTO> contractMap = contractDTOS.stream().collect(Collectors.toMap(
                    e -> e.getContractCode().concat("|").concat(
                            StringUtils.isNotEmpty(e.getOrgId()) ? e.getOrgId() : ""), e -> e));
            params.put("contractMap", contractMap);

            // 查询合同余额
            Map<String, ContractBalanceLatestEntity> contractBalanceLatestEntityMap = contractBalanceLatestService.
                    selectContractBalanceLatestMap(contractCodeList);
            params.put("contractBalanceLatestEntityMap", contractBalanceLatestEntityMap);

            // 客户编码列表
            List<String> clientCodeList = subNewDetailEntites.stream().map(e -> e.getClientCode()).distinct().
                    collect(Collectors.toList());

            // 查询客户信息
            Map<String, ClientEntity> clientEntitylist = clientService.selectClientMap(clientCodeList);
            params.put("clientEntitylist", clientEntitylist);

            boolean genVoucherResult = leaseIncomeNewTransactionService.genVoucher1(
                    isSubmit, subNewDetailEntites, params);
            if (!genVoucherResult) {
                result = false;
            }
        }
        return result;
    }

    private int getGenVoucherLoopCount(int totalCount) {
        int loopCount = totalCount / EACH_GEN_VOUCHER_NUM;
        if (totalCount % EACH_GEN_VOUCHER_NUM != 0) {
            return loopCount + 1;
        } else {
            return loopCount;
        }
    }

    private void updateLeaseIncomeStatus(String isSubmit, LeaseIncomeEntity leaseIncomeEntity, boolean isSuccess) {
        if (isSuccess) {
            leaseIncomeEntity.setIsGenerateVoucher(YesOrNoEnum.YES.getCode());
        } else {
            leaseIncomeEntity.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
        }
        leaseIncomeEntity.setAccountDate(DateUtil.beginOfDay(new Date()));
        leaseIncomeEntity.setUpdateTime(LocalDateTime.now());
        if (YesOrNoEnum.YES.getCode().equals(isSubmit)) {
            leaseIncomeEntity.setProcessStatus(MarginStatusEnum.SUBMITTED.getCode());
        }
        updateById(leaseIncomeEntity);
    }
}

