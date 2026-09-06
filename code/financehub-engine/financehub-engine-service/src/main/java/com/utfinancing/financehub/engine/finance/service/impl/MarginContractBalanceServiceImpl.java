package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.GenConstants;
import com.utfinancing.financehub.common.core.constant.HttpStatus;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.common.security.utils.DictUtils;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.constant.DefaultConstant;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.ContractBalanceMapper;
import com.utfinancing.financehub.engine.finance.mapper.MarginContractBalanceMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.LprDataVO;
import com.utfinancing.financehub.engine.finance.model.vo.MarginContractBalanceVO;
import com.utfinancing.financehub.engine.finance.model.vo.PayableInsuranceDetailsVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.model.dto.AccountQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneFieldsDTO;
import com.utfinancing.financehub.engine.scene.model.vo.AccountVO;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import com.utfinancing.financehub.engine.scene.service.ISceneFieldsService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.utils.PeriodCodeUtil;
import com.utfinancing.financehub.engine.utils.UserUtils;
import com.utfinancing.financehub.engine.verification.entity.VerificationDetailsEntity;
import com.utfinancing.financehub.engine.verification.entity.VerificationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-24
 * @Description :  MarginContractBalance服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class MarginContractBalanceServiceImpl extends ServiceImpl<MarginContractBalanceMapper, MarginContractBalanceEntity> implements IMarginContractBalanceService {

    private final MarginContractBalanceMapper marginContractBalanceMapper;
    private final ILprDataService lprDataService;
    private final ContractBalanceMapper contractBalanceMapper;
    private final IAccountService accountService;
    private final IContractService contractService;
    private final RemoteDictService remoteDictService;
    private final IRuleService iRuleService;
    private final ISceneFieldsService sceneFieldsService;
    private final IVoucherService voucherService;
    private final IOrgCompanyService orgCompanyService;
    private final IApproveService iApproveService;



    @Value("${approve.url.marginContract-url:null}")
    private String approveUrl;


    @Value("${file.storage.basicpath.windows:null}")
    private String basicPathWindows;

    @Value("${file.storage.basicpath.linux:null}")
    private String basicPathLinux;

    @Resource
    private IFileRecordService fileRecordService;

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Value("${service.parth:null}")
    private String servicePath;

    @Resource
    private IBatchTaskService iBatchTaskService;

    @Resource
    private IVoucherService iVoucherService;

    private static Map<String, CellStyle> cellStyleMap = Maps.newHashMap();

    @Override
    public Long saveMarginContractBalance(MarginContractBalanceDTO dto) {
        MarginContractBalanceEntity entity = BeanUtil.copyProperties(dto, MarginContractBalanceEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Void saveMarginContractBalanceBatch(List<MarginContractBalanceSaveDTO> dto) {
        // 删除
        this.remove(new LambdaQueryWrapper<MarginContractBalanceEntity>()
                .isNotNull(MarginContractBalanceEntity::getEntryDate)
                .isNull(MarginContractBalanceEntity::getMarginType));
        // 新增
        List<MarginContractBalanceEntity> marginContractBalanceEntities = BeanUtil.copyToList(dto, MarginContractBalanceEntity.class);
        marginContractBalanceEntities.forEach(v -> {
            v.setEntryDate(new Date());
        });
        saveBatch(marginContractBalanceEntities);
        return null;
    }


    @Override
    public List<MarginContractBalanceVO> enterList() {
        LambdaQueryWrapper<MarginContractBalanceEntity> queryWrapper = Wrappers.<MarginContractBalanceEntity>lambdaQuery();
        //这里注入查询条件
        queryWrapper.isNotNull(MarginContractBalanceEntity::getEntryDate);
        queryWrapper.isNull(MarginContractBalanceEntity::getMarginType);
        List<MarginContractBalanceEntity> list = marginContractBalanceMapper.selectList(queryWrapper);
        return ListBeanUtil.copyList(list, MarginContractBalanceVO.class);
    }

    @Override
    public Long updateMarginContractBalance(Long id, MarginContractBalanceDTO dto) {
        MarginContractBalanceEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public MarginContractBalanceDTO getMarginContractBalanceDTOById(Long id) {
        MarginContractBalanceEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, MarginContractBalanceDTO.class);
    }

    @Override
    public IPage<MarginContractBalanceVO> selectPage(MarginContractBalanceQueryDTO queryDTO) {
        LambdaQueryWrapper<MarginContractBalanceEntity> queryWrapper = Wrappers.<MarginContractBalanceEntity>lambdaQuery();
        //这里注入查询条件
        Date queryDate = queryDTO.getBalanceDate();
        if (null != queryDate) {
            queryDate = DateUtil.beginOfDay(DateUtil.endOfMonth(queryDate));
            queryWrapper.eq(MarginContractBalanceEntity::getBalanceDate, queryDate);
        }
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), MarginContractBalanceEntity::getOrgId, queryDTO.getOrgIdList());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getMarginType()), MarginContractBalanceEntity::getMarginType, queryDTO.getMarginType());
        queryWrapper.eq(null != queryDTO.getBatchId(), MarginContractBalanceEntity::getBatchId, queryDTO.getBatchId());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getBatchIdList()), MarginContractBalanceEntity::getBatchId, queryDTO.getBatchIdList());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getAccountCode()), MarginContractBalanceEntity::getAccountCode, queryDTO.getAccountCode());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getAccountCodeList()), MarginContractBalanceEntity::getAccountCode, queryDTO.getAccountCodeList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getAccountNameList()), MarginContractBalanceEntity::getAccountName, queryDTO.getAccountNameList());
        IPage<MarginContractBalanceEntity> entityIPage = marginContractBalanceMapper.selectPage(new Page<MarginContractBalanceEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, MarginContractBalanceVO.class);
    }

    @Override
    public List<MarginContractBalanceVO> selectList(MarginContractBalanceQueryDTO queryDTO) {
        Date queryDate = queryDTO.getBalanceDate();
        if (ObjectUtil.isNotNull(queryDate)) {
            queryDate = DateUtil.beginOfDay(DateUtil.endOfMonth(queryDate));
        }
        LambdaQueryWrapper<MarginContractBalanceEntity> queryWrapper = Wrappers.<MarginContractBalanceEntity>lambdaQuery();
        //这里注入查询条件
        if (ObjectUtil.isNotNull(queryDate)) {
            queryWrapper.eq(MarginContractBalanceEntity::getBalanceDate, queryDate);
        }
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), MarginContractBalanceEntity::getOrgId, queryDTO.getOrgIdList());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getMarginType()), MarginContractBalanceEntity::getMarginType, queryDTO.getMarginType());
        queryWrapper.eq(null != queryDTO.getBatchId(), MarginContractBalanceEntity::getBatchId, queryDTO.getBatchId());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getBatchIdList()), MarginContractBalanceEntity::getBatchId, queryDTO.getBatchIdList());
        queryWrapper.like(StringUtils.isNotBlank(queryDTO.getAccountCode()), MarginContractBalanceEntity::getAccountCode, queryDTO.getAccountCode());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getAccountCodeList()), MarginContractBalanceEntity::getAccountCode, queryDTO.getAccountCodeList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getAccountNameList()), MarginContractBalanceEntity::getAccountName, queryDTO.getAccountNameList());
        queryWrapper.orderByAsc(MarginContractBalanceEntity::getContractBalance);
        List<MarginContractBalanceEntity> list = marginContractBalanceMapper.selectList(queryWrapper);
        List<MarginContractBalanceVO> marginContractBalanceVOS = ListBeanUtil.copyList(list, MarginContractBalanceVO.class);
        translateDict(marginContractBalanceVOS);
        return marginContractBalanceVOS;
    }

    private void translateDict(List<MarginContractBalanceVO> list) {
        //签约主体
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgId(), e -> e.getOrgName(), (a, b) -> b));
        //签约主体
        R<List<SysDictData>> currencyR = remoteDictService.listDictData(DictTypeEnum.SYS_CURRENCY_TYPE.getCode());
        Map<String, String> currencyMap = currencyR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        list.forEach(e -> {
            e.setOrgId(companyMap.get(e.getOrgId()));
            e.setCurrencyType(currencyMap.get(e.getCurrencyType()));
        });
    }

    @Override
    public IPage<MarginContractBalanceVO> selectSummaryPage(MarginContractBalanceQueryDTO queryDTO) {
        if (ObjectUtil.isNotNull(queryDTO.getBalanceDate())) {
            Date queryDate = queryDTO.getBalanceDate();
            log.info("获取查询日期：{}", queryDTO);
            queryDate = DateUtil.beginOfDay(DateUtil.endOfMonth(queryDate));
            log.info("转换后查询日期：{}", queryDTO);
            queryDTO.setBalanceDate(queryDate);
        }
        Page<MarginContractBalanceVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<MarginContractBalanceEntity> contractBalanceEntities = this.getBaseMapper().selectSummaryInfo(page, queryDTO);
        page.setRecords(BeanUtil.copyToList(contractBalanceEntities, MarginContractBalanceVO.class));
        page.getRecords().forEach(v -> {
            v.setBatchType(BatchTypeEnum.BZJ.getCode());
            v.setId(v.getBatchId());
        });
        return page;
    }

    @Override
    public Void generate(MarginContractBalanceQueryDTO queryDTO) {
        long startTime = System.currentTimeMillis();
        // 本,上期末
        String marginType = queryDTO.getMarginType();
        Date queryDate = DateUtil.beginOfDay(DateUtil.endOfMonth(queryDTO.getBalanceDate()));
        queryDTO.setBalanceDate(queryDate);
        queryDTO.setStartBalanceDate(DateUtil.beginOfMonth(queryDate));
        DateTime endDate = DateUtil.beginOfDay(DateUtil.endOfMonth(queryDate));
        DateTime lastEndDate = DateUtil.beginOfDay(DateUtil.endOfMonth(DateUtil.offsetMonth(endDate, -1)));

        this.remove(new LambdaQueryWrapper<MarginContractBalanceEntity>()
                .in(MarginContractBalanceEntity::getMarginStatus, MarginStatusEnum.canChangeStatus())
                .in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), MarginContractBalanceEntity::getOrgId, queryDTO.getOrgIdList())
                .in(CollectionUtils.isNotEmpty(queryDTO.getAccountCodeList()), MarginContractBalanceEntity::getAccountCode, queryDTO.getAccountCodeList())
                .eq(MarginContractBalanceEntity::getMarginType, marginType)
                .eq(MarginContractBalanceEntity::getBalanceDate, queryDate)
        );
        //过滤保证金科目filterAccountCodeList
        List<String> filterAccountCodeList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(queryDTO.getAccountCodeList())) {
            filterAccountCodeList = queryDTO.getAccountCodeList();
        }
        //1.获取保证金科目
        List<String> marginAccountList = queryDTO.getAccountCodeList();
        if (CollectionUtils.isEmpty(marginAccountList)) {
            //为空补充所有的保证金科目
            R<List<SysDictData>> dictListR = remoteDictService.listDictData(DictTypeEnum.MARGIN_SUBJECT.getCode());
            if (dictListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(dictListR.getData())) {
                marginAccountList = dictListR.getData().stream().map(SysDictData::getDictValue).collect(Collectors.toList());
            }
        }
        // 过滤凭证特有的保证金类型
        R<List<SysDictData>> dictListR = remoteDictService.listDictData(MarginTypeEnum.RECLASSIFICATION.getCode().equals(marginType) ?
                DictTypeEnum.MARGIN_RECLASSIFIED_SCOPE.getCode() : DictTypeEnum.MARGIN_INTEREST_SCOPE.getCode());
        if (dictListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(dictListR.getData())) {
            List<String> marginAccountScopesList = dictListR.getData().stream().map(SysDictData::getDictValue).collect(Collectors.toList());
            marginAccountList = marginAccountList.stream().filter(marginAccountScopesList::contains).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(marginAccountList)) {
            return null;
        }
        AccountQueryDTO query = new AccountQueryDTO();
        query.setAccountCodeList(marginAccountList);
        //2.本次处理的所有保证金科目
        List<AccountVO> accounts = accountService.selectList(query);

        // 科目包含的业务code
        queryDTO.setBusinessCodeList(accounts.stream().map(AccountVO::getBusinessCode).distinct().collect(Collectors.toList()));
        queryDTO.setPeriodCode(Integer.parseInt(DateUtils.format(queryDTO.getBalanceDate(), "yyyyMM")));
        queryDTO.setLastPeriodCode(Integer.parseInt(DateUtils.format(lastEndDate, "yyyyMM")));
//        List<Map<String, Object>> contractBalanceEntities = contractBalanceMapper.selectContractMap(queryDTO);
        //固化一张临时表去上个月balanceMonth的不为空过的数据+balance最新的这个月的数据，并按照合同+客户+签约主体+借款合同编号金额不为O的数据进行更新操作
        log.info("保证金固化临时数据开始");
        List<Map<String, Object>> contractBalanceEntities = getMarginBalance(queryDTO);
        log.info("保证金固化临时数据结束,size:{}", contractBalanceEntities.size());
        long time1 = System.currentTimeMillis();
        log.info("保证金 合同map查询时间 代码执行时间" + (time1 - startTime));
        // 3.获取不能修改的保证金重分类的合同数据
        List<MarginContractBalanceEntity> marginContractBalanceEntities = this.getBaseMapper().selectList(Wrappers.<MarginContractBalanceEntity>lambdaQuery()
                .in(MarginContractBalanceEntity::getMarginStatus, MarginStatusEnum.cantChangeStatus())
                .in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), MarginContractBalanceEntity::getOrgId, queryDTO.getOrgIdList())
                .in(MarginContractBalanceEntity::getAccountCode, marginAccountList)
                .eq(MarginContractBalanceEntity::getMarginType, marginType)
                .eq(MarginContractBalanceEntity::getBalanceDate, queryDate));
        // 排除不能处理的科目
        List<String> existAccountCodeList = marginContractBalanceEntities.stream()
                .map(MarginContractBalanceEntity::getAccountCode).collect(Collectors.toList());
        marginAccountList = marginAccountList.stream().filter(e -> !existAccountCodeList.contains(e)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(marginAccountList)) {
            return null;
        }
        List<AccountVO> existAccounts = accounts.stream().filter(e -> !existAccountCodeList.contains(e.getAccountCode())).collect(Collectors.toList());
        // 合同余额convert保证金
        List<MarginContractBalanceEntity> contractBalanceEntityList = contractBalanceConvertToMargin(contractBalanceEntities, queryDate, existAccounts);
        long time2 = System.currentTimeMillis();
        log.info("保证金 合同余额convert保证金 代码执行时间" + (time2 - time1));
        //手动录入的数据处理
        handleEntryData(queryDTO, queryDate, marginAccountList, contractBalanceEntityList);
        // 保证金数据处理
        List<MarginContractBalanceEntity> result = marginConvert(contractBalanceEntityList, queryDate, endDate, lastEndDate, marginType, filterAccountCodeList);
        Map<String, Long> idMap = new HashMap<>();
        // 增加批次id
        result.forEach(e -> {
            String key = e.getOrgId() + e.getAccountCode();
            Long id = idMap.get(key);
            if (null == id) {
                id = IdWorker.getId();
                e.setBatchId(id);
                idMap.put(key, id);
            } else {
                e.setBatchId(id);
            }
        });
        long time3 = System.currentTimeMillis();
        log.info("保证金 保存前 代码执行时间" + (time3 - time2));
        // 保存
        saveBatch(result);
        long endTime = System.currentTimeMillis();
        log.info("保证金 保存 代码执行时间" + (endTime - time3));

        return null;
    }

    private void handleEntryData(MarginContractBalanceQueryDTO queryDTO, Date queryDate, List<String> marginAccountList, List<MarginContractBalanceEntity> contractBalanceEntityList) {
        LambdaQueryWrapper<MarginContractBalanceEntity> queryWrapper = Wrappers.<MarginContractBalanceEntity>lambdaQuery();
//        queryWrapper.eq(MarginContractBalanceEntity::getEntryDate, queryDate);
        queryWrapper.isNull(MarginContractBalanceEntity::getMarginType);
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), MarginContractBalanceEntity::getOrgId, queryDTO.getOrgIdList());
        queryWrapper.in(MarginContractBalanceEntity::getAccountCode, marginAccountList);
        queryWrapper.notIn(CollectionUtils.isNotEmpty(queryDTO.getOrgIdListNotIn()), MarginContractBalanceEntity::getOrgId, queryDTO.getOrgIdListNotIn());
        List<MarginContractBalanceEntity> list = marginContractBalanceMapper.selectList(queryWrapper);
        contractBalanceEntityList.addAll(list.stream().map(e -> {
            MarginContractBalanceEntity result = BeanUtil.copyProperties(e, MarginContractBalanceEntity.class, GenConstants.BASE_ENTITY);
            Date queryDates = e.getEntryDate();
            result.setBalanceDate(queryDate);
            result.setBusinessDate(queryDate);
            result.setAccountPeriod(Integer.valueOf(DateUtil.format(queryDate, DatePattern.SIMPLE_MONTH_PATTERN)));
            return result;
        }).collect(Collectors.toList()));
    }

    @Override
    public void voucher(MarginContractBalanceCheckDTO checkDTO, MarginTypeEnum marginTypeEnum, String isSubmit) {
        List<Long> batchIdList = checkDTO.getBatchIdList();
        if (CollectionUtils.isEmpty(batchIdList)) {
            return;
        }
        for (Long id : batchIdList) {
            List<MarginContractBalanceEntity> list = marginContractBalanceMapper.selectList(Wrappers.<MarginContractBalanceEntity>lambdaQuery()
                    .eq(MarginContractBalanceEntity::getBatchId, id));
            DateTime today = DateUtil.beginOfDay(new Date());
            if (YesOrNoEnum.NO.getCode().equals(isSubmit)) {
                //校验任务
                Boolean isExistFlag = iBatchTaskService.isExistTask(id, BatchTypeEnum.BZJ.getCode());
                if (isExistFlag) {
                    throw new ServiceException("存在任务正在执行，请稍后重试");
                }
                //保存任务
                List<Long> taskIdList = Lists.newArrayList();
                taskIdList.add(iBatchTaskService.saveBatchTask(BatchTaskDTO.builder().businessId(id).businessType(BatchTypeEnum.BZJ.getCode()).status("1").build()));
                CompletableFuture.runAsync(() -> {
                    //生成凭证之前需要将原凭证删除
                    batchDeleteVoucher(list);
                    saveVoucher(list, marginTypeEnum.getCode(), today, isSubmit);
                }).whenComplete((v, e) -> {
                    // 执行成功，更新任务状态
                    iBatchTaskService.updateBatchTask(taskIdList, "2");
                }).exceptionally(e -> {
                    log.info("批量处理数据失败", e);
                    iBatchTaskService.updateBatchTask(taskIdList, "3");
                    return null;
                });
                ;
            } else {
                //生成凭证之前需要将原凭证删除
                batchDeleteVoucher(list);
                saveVoucher(list, marginTypeEnum.getCode(), today, isSubmit);
            }
        }

    }

    private void saveVoucher(List<MarginContractBalanceEntity> result, String marginType, Date queryDate, String isSubmit) {
        if (CollectionUtils.isEmpty(result)) {
            return;
        }
        R<List<SysDictData>> dictListR = remoteDictService.listDictData(MarginTypeEnum.RECLASSIFICATION.getCode().equals(marginType) ?
                DictTypeEnum.MARGIN_RECLASSIFIED_SCOPE.getCode() : DictTypeEnum.MARGIN_INTEREST_SCOPE.getCode());
        List<String> marginAccountScopesList = dictListR.getData().stream().map(SysDictData::getDictValue).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(marginAccountScopesList)) {
            return;
        }
        AccountQueryDTO query = new AccountQueryDTO();
        query.setAccountCodeList(marginAccountScopesList);
        List<AccountVO> accounts = accountService.selectList(query);
        Map<String, AccountVO> accountMap = accounts.stream().collect(Collectors.toMap(e -> e.getAccountCode(), e -> e));
        String createUserNo = UserUtils.getStaffCode();
        String createUserName = UserUtils.getStaffName();
        if (MarginTypeEnum.RECLASSIFICATION.getCode().equals(marginType)) {
            Map<String, List<MarginContractBalanceEntity>> accountCodeGroup = result.stream().collect(Collectors.groupingBy(MarginContractBalanceEntity::getAccountCode));
            List<Map<String, Object>> voucherMapList = Lists.newArrayList();
            accountCodeGroup.forEach((accountCode, entityList) -> {
                MarginContractBalanceEntity marginContractBalanceEntity = entityList.get(0);
                String orgId = marginContractBalanceEntity.getOrgId();
                ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
                commonDTO.setSystemCode(SystemEnum.CWZT.getCode());
                commonDTO.setSystemName(SystemEnum.CWZT.getDesc());
                commonDTO.setBusinessCode(accountMap.get(accountCode).getBusinessCode());
                commonDTO.setBusinessName(accountMap.get(accountCode).getBusinessName());
                commonDTO.setOrderId(marginContractBalanceEntity.getBatchId().toString());
                commonDTO.setSceneCode(SceneEnum.BZJCFL.getCode());
                commonDTO.setSceneName(SceneEnum.BZJCFL.getDesc());
                commonDTO.setOrgId(orgId);
                commonDTO.setBusinessDate(marginContractBalanceEntity.getBusinessDate());
                commonDTO.setCurrencyType(marginContractBalanceEntity.getCurrencyType());
                commonDTO.setBatchId(marginContractBalanceEntity.getBatchId());
                commonDTO.setBatchType(BatchTypeEnum.BZJ.getCode());
                commonDTO.setBillContractCode(Constants.BILL_CONTRACT_CODE_DEFAULT);
                commonDTO.setIsSubmit(isSubmit);
                commonDTO.setAccountName(marginContractBalanceEntity.getAccountName());
                commonDTO.setInterfaceId(marginContractBalanceEntity.getBatchId());
                OrgIdEnum orgIdEnum = OrgIdEnum.getDescByCode(commonDTO.getOrgId());
                if (null != orgIdEnum) {
                    commonDTO.setContractCode(orgIdEnum.getContractCode());
                    commonDTO.setClientCode(orgIdEnum.getClientCode());
                }
                commonDTO.setCreateUserName(createUserName);
                commonDTO.setCreateUserNo(createUserNo);
                log.info("保证金重分类凭证参数：{}", JSON.toJSON(commonDTO));
                Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);
                // 获取重分类所有字段初始化
                List<SceneFieldsDTO> sceneFieldsDTOS = sceneFieldsService.listSceneFieldsByCode(SceneEnum.BZJCFL.getCode())
                        .stream().filter(e -> DataTypeEnum.NUMBER.getCode().equals(e.getDataType())).collect(Collectors.toList());
                sceneFieldsDTOS.forEach(e -> commonMap.put(e.getFieldCode(), 0));
                // 查询上月凭证 没有则为0 查询本表上个余额金额签约主体+业务编码+业务日期-1个月最新的那条
                MarginContractBalanceEntity lastBalanceEntity = getLastMonth(marginContractBalanceEntity);
                String fundType = accountMap.get(accountCode).getFundType();
                String fundYearType = "payable_" + fundType + "_year";
                String lastMonth = "last_month_" + fundType;
                String lastMonthYear = "last_month_" + fundYearType;
                commonMap.put(lastMonth, null == lastBalanceEntity.getContractBalance() ? BigDecimal.ZERO : lastBalanceEntity.getContractBalance());
                commonMap.put(lastMonthYear, null == lastBalanceEntity.getWithinOneYearDeposit() ? BigDecimal.ZERO : lastBalanceEntity.getWithinOneYearDeposit());
                String thisMonth = "this_month_payable_" + fundType;
                String thisMonthYear = "this_month_" + fundYearType;
                commonMap.put(thisMonth, entityList.stream().map(MarginContractBalanceEntity::getContractBalance).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                commonMap.put(thisMonthYear, entityList.stream().map(MarginContractBalanceEntity::getWithinOneYearDeposit).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                voucherMapList.add(commonMap);
            });
            log.info("生成凭证参数：{}", JSON.toJSONString(voucherMapList));
            List<VoucherInfoVO> voucherInfoVOList = iRuleService.batchExecuteRule(voucherMapList);
            log.info("保证金生成凭证返回值：{}", JSON.toJSON(voucherInfoVOList));
            if (voucherInfoVOList == null || voucherInfoVOList.isEmpty()) {
                log.error("保证金重分类生成凭证失败");
            } else {
                for (VoucherInfoVO entry : voucherInfoVOList) {
                    List<VoucherDTO> value = entry.getVoucherDTOList();
                    if (CollectionUtils.isNotEmpty(value)) {
                        String vouchIds = value.stream().map(VoucherDTO::getId).map(String::valueOf).collect(
                                Collectors.joining(","));

                        this.lambdaUpdate()
                                .set(MarginContractBalanceEntity::getVoucherId, vouchIds)
                                .set(MarginContractBalanceEntity::getFinanceDate, value.get(0).getVoucherDate())
                                .set(MarginContractBalanceEntity::getIsGenerateVoucher, YesOrNoEnum.YES.getCode())
                                .set(MarginContractBalanceEntity::getExceptionType, entry.getErrorInfo())
                                .eq(MarginContractBalanceEntity::getBatchId, Long.parseLong(entry.getOrderId()))
                                .update();
                    }
                }
            }
        } else {
            List<Map<String, Object>> voucherMapList = Lists.newArrayList();
            for (MarginContractBalanceEntity marginContractBalanceEntity : result) {
                String accountCode = marginContractBalanceEntity.getAccountCode();
                ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
                commonDTO.setSystemCode(SystemEnum.CWZT.getCode());
                commonDTO.setSystemName(SystemEnum.CWZT.getDesc());
                commonDTO.setBusinessCode(accountMap.get(accountCode).getBusinessCode());
                commonDTO.setBusinessName(accountMap.get(accountCode).getBusinessName());
                commonDTO.setOrderId(marginContractBalanceEntity.getId().toString());
                commonDTO.setContractCode(marginContractBalanceEntity.getContractCode());
                commonDTO.setContractName(marginContractBalanceEntity.getContractName());
                commonDTO.setClientCode(marginContractBalanceEntity.getClientCode());
                commonDTO.setClientName(marginContractBalanceEntity.getClientName());
                commonDTO.setSceneCode(SceneEnum.BZJJT.getCode());
                commonDTO.setSceneName(SceneEnum.BZJJT.getDesc());
                commonDTO.setOrgId(marginContractBalanceEntity.getOrgId());
                commonDTO.setBusinessDate(marginContractBalanceEntity.getBusinessDate());
                commonDTO.setCurrencyType(marginContractBalanceEntity.getCurrencyType());
                commonDTO.setBatchId(marginContractBalanceEntity.getBatchId());
                commonDTO.setBatchType(BatchTypeEnum.BZJ.getCode());
                commonDTO.setIsSubmit(isSubmit);
                commonDTO.setAccountName(marginContractBalanceEntity.getAccountName());
                commonDTO.setInterfaceId(marginContractBalanceEntity.getId());
                commonDTO.setCreateUserName(createUserName);
                commonDTO.setCreateUserNo(createUserNo);
                Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);
                commonMap.put("marginInterest", marginContractBalanceEntity.getCurrentEnterPl());
                voucherMapList.add(commonMap);
            }
            log.info("生成凭证参数：{}", JSON.toJSONString(voucherMapList));
            List<VoucherInfoVO> voucherInfoVOList = iRuleService.batchExecuteRule(voucherMapList);
            if (voucherInfoVOList == null || voucherInfoVOList.isEmpty()) {
                log.error("保证金计提生成凭证失败");
            } else {
                for (VoucherInfoVO entry : voucherInfoVOList) {
                    List<VoucherDTO> value = entry.getVoucherDTOList();
                    if (CollectionUtils.isNotEmpty(value)) {
                        String vouchIds = value.stream().map(VoucherDTO::getId).map(String::valueOf).collect(
                                Collectors.joining(","));

                        this.lambdaUpdate()
                                .set(MarginContractBalanceEntity::getVoucherId, vouchIds)
                                .set(MarginContractBalanceEntity::getFinanceDate, value.get(0).getVoucherDate())
                                .set(MarginContractBalanceEntity::getIsGenerateVoucher, YesOrNoEnum.YES.getCode())
                                .set(MarginContractBalanceEntity::getExceptionType, entry.getErrorInfo())
                                .eq(MarginContractBalanceEntity::getId, Long.parseLong(entry.getOrderId()))
                                .update();
                    }
                }
            }
        }
    }

    public MarginContractBalanceEntity getLastMonth(MarginContractBalanceEntity entity) {
        MarginContractBalanceQueryDTO queryDTO = new MarginContractBalanceQueryDTO();
        queryDTO.setOrgId(entity.getOrgId());
        queryDTO.setBusinessDateMonth(DateUtil.format(CommonDateUtils.lastMonth(entity.getBusinessDate()), "yyyy-MM"));
        queryDTO.setMarginType("1");
        queryDTO.setAccountCode(entity.getAccountCode());
        MarginContractBalanceEntity entity1 = marginContractBalanceMapper.getLastMonth(queryDTO);
        return null == entity1 ? new MarginContractBalanceEntity() : entity1;
    }

    @Override
    public String submmit(MarginContractBalanceCheckDTO checkDTO) {
        // 负数的过滤报错
        LambdaQueryWrapper<MarginContractBalanceEntity> queryWrapper = Wrappers.<MarginContractBalanceEntity>lambdaQuery();
        queryWrapper
                .in(MarginContractBalanceEntity::getMarginStatus, MarginStatusEnum.canChangeStatus())
                .in(CollectionUtils.isNotEmpty(checkDTO.getBatchIdList()), MarginContractBalanceEntity::getBatchId, checkDTO.getBatchIdList())
                .lt(MarginContractBalanceEntity::getContractBalance, BigDecimal.ZERO);
        List<MarginContractBalanceEntity> negativeList = this.getBaseMapper().selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(negativeList)) {
            throw new ServiceException(StringUtils.join(negativeList.stream().map(e -> e.getAccountName() + "科目").distinct().collect(Collectors.toList()), ",") + "存在负数金额");
        }
        List<MarginContractBalanceEntity> balanceEntityList = this.lambdaQuery().in(MarginContractBalanceEntity::getMarginStatus, MarginStatusEnum.canChangeStatus())
                .in(CollectionUtils.isNotEmpty(checkDTO.getBatchIdList()), MarginContractBalanceEntity::getBatchId, checkDTO.getBatchIdList()).list();
//
//        LambdaUpdateWrapper<MarginContractBalanceEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
//        updateChainWrapper
//                .in(MarginContractBalanceEntity::getMarginStatus, MarginStatusEnum.canChangeStatus())
//                .in(CollectionUtils.isNotEmpty(checkDTO.getBatchIdList()), MarginContractBalanceEntity::getBatchId, checkDTO.getBatchIdList())
//                .set(MarginContractBalanceEntity::getUpdateTime, LocalDateTime.now())
//                .set(MarginContractBalanceEntity::getSubmitBy, SecurityUtils.getUserId())
////                .set(MarginContractBalanceEntity::getMarginStatus, MarginStatusEnum.SUBMITTED.getCode());
//        this.update(updateChainWrapper);
        //提交需要重新调用凭证接口
        Map<String, List<MarginContractBalanceEntity>> balanceEntityMap = balanceEntityList.stream().collect(Collectors.groupingBy(MarginContractBalanceEntity::getMarginType));
        balanceEntityList.forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getMarginStatus())) {
                throw new ServiceException("只有处理状态为已录入的才可以提交");
            }
        });
        List<Long> batchIdsList = balanceEntityList.stream().map(MarginContractBalanceEntity::getBatchId).distinct().collect(Collectors.toList());
        //记录任务
        //校验任务
        batchIdsList.forEach(v -> {
            Boolean isExistFlag = iBatchTaskService.isExistTask(v, BatchTypeEnum.BZJ.getCode());
            if (isExistFlag) {
                throw new ServiceException("存在任务正在执行，请稍后重试");
            }
        });
        //保存任务
        List<Long> taskIdList = Lists.newArrayList();
        batchIdsList.forEach(v -> {
            taskIdList.add(iBatchTaskService.saveBatchTask(BatchTaskDTO.builder().businessId(v).businessType(BatchTypeEnum.BZJ.getCode()).status("1").build()));
        });
        CompletableFuture.runAsync(() -> {
            for (Map.Entry<String, List<MarginContractBalanceEntity>> entry : balanceEntityMap.entrySet()) {
                List<Long> batchIdList = entry.getValue().stream().map(MarginContractBalanceEntity::getBatchId).distinct().collect(Collectors.toList());
                MarginContractBalanceCheckDTO newCheckDTO = new MarginContractBalanceCheckDTO();
                newCheckDTO.setBatchIdList(batchIdList);
                if (MarginTypeEnum.RECLASSIFICATION.getCode().equals(entry.getKey())) {
                    voucher(newCheckDTO, MarginTypeEnum.RECLASSIFICATION, YesOrNoEnum.YES.getCode());
                } else if (MarginTypeEnum.INTEREST_PROVISION.getCode().equals(entry.getKey())) {
                    voucher(newCheckDTO, MarginTypeEnum.INTEREST_PROVISION, YesOrNoEnum.YES.getCode());
                }
            }
            List<ApproveDTO> approveDTOList = Lists.newArrayList();
            //batchId分组取任意一条
            Map<Long, MarginContractBalanceEntity> entityMap = balanceEntityList.stream().collect(Collectors.toMap(MarginContractBalanceEntity::getBatchId, Function.identity(), BinaryOperator.maxBy(Comparator.comparingLong(MarginContractBalanceEntity::getId))));
            List<Long> batchIdList = Lists.newArrayList();
            for (Map.Entry<Long, MarginContractBalanceEntity> v : entityMap.entrySet()) {
                if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getValue().getMarginStatus())) {
                    throw new ServiceException("只有处理状态为已录入的才可以提交");
                }
                ApproveDTO approveDTO = new ApproveDTO();
                approveDTO.setDocumentId(v.getValue().getBatchId());
                approveDTO.setDocumentType(BatchTypeEnum.BZJ.getCode());
                approveDTO.setUrl(approveUrl + v.getValue().getBatchId());
                approveDTOList.add(approveDTO);
                batchIdList.add(v.getValue().getBatchId());
            }
            ;
            //发送审核
            Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);
            batchIdList.forEach(v -> {
                if (null != processInstantIdMap && processInstantIdMap.containsKey(v)) {
                    this.lambdaUpdate().set(MarginContractBalanceEntity::getProcessInstanceId, processInstantIdMap.get(v))
                            .set(MarginContractBalanceEntity::getMarginStatus, ProcessStatusEnum.SUBMITTED.getCode())
                            .set(MarginContractBalanceEntity::getSubmitBy, UserUtils.getStaffCode())
                            .eq(MarginContractBalanceEntity::getBatchId, v).update();
                }
            });
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            iBatchTaskService.updateBatchTask(taskIdList, "2");
        }).exceptionally(e -> {
            log.info("保证金批量处理数据失败", e);
            iBatchTaskService.updateBatchTask(taskIdList, "3");
            return null;
        });
        ;
        return null;
    }

    @Override
    public Void pass(MarginContractBalanceCheckDTO checkDTO) {
        changeStatus(checkDTO, MarginStatusEnum.PASS);
        return null;
    }

    @Override
    public Void fail(MarginContractBalanceCheckDTO checkDTO) {
        changeStatus(checkDTO, MarginStatusEnum.FAILED);
        return null;
    }

    @Override
    public Void withdraw(MarginContractBalanceCheckDTO checkDTO) {
        //撤回需要判断是否已经提交数据了
        List<MarginContractBalanceEntity> balanceEntityList = this.lambdaQuery().in(MarginContractBalanceEntity::getBatchId, checkDTO.getBatchIdList()).list();
        if (CollectionUtils.isEmpty(balanceEntityList)) {
            throw new ServiceException("没有数据需要撤回");
        }
        List<Long> processInstanceIdList = balanceEntityList.stream().map(MarginContractBalanceEntity::getProcessInstanceId).filter(ObjectUtil::isNotNull).distinct().collect(Collectors.toList());
        balanceEntityList.forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getMarginStatus())) {
                throw new ServiceException("只有状态为已提交的才可以撤回");
            }
            v.setMarginStatus(ProcessStatusEnum.ENTERED.getCode());
            v.setProcessInstanceId(null);
        });
        iApproveService.withdraw(processInstanceIdList);
        this.updateBatchById(balanceEntityList);
        return null;
    }

    private void changeStatus(MarginContractBalanceCheckDTO checkDTO, MarginStatusEnum marginStatusEnum) {
        LambdaUpdateWrapper<MarginContractBalanceEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .eq(MarginContractBalanceEntity::getMarginStatus, MarginStatusEnum.SUBMITTED.getCode())
                .in(CollectionUtils.isNotEmpty(checkDTO.getBatchIdList()), MarginContractBalanceEntity::getBatchId, checkDTO.getBatchIdList())
                .set(MarginContractBalanceEntity::getUpdateTime, LocalDateTime.now())
                .set(MarginContractBalanceEntity::getMarginStatus, marginStatusEnum.getCode());
        this.update(updateChainWrapper);
    }

    private List<MarginContractBalanceEntity> marginConvert(List<MarginContractBalanceEntity> records, Date queryDate, Date thisEndDate, Date lastEndDate, String marginType, List<String> filterAccountCodeList) {
        List<LprDataVO> lprConfigs = lprDataService.listOneYear();
        LocalDate queryDateLocalDate = queryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        for (MarginContractBalanceEntity record : records) {
            if (CollectionUtils.isNotEmpty(filterAccountCodeList) && !filterAccountCodeList.contains(record.getAccountCode())) {
                continue;
            }
            BigDecimal fv = record.getContractBalance();
            Date a = record.getLeaseDateStart();
            Date b = record.getLeaseDateEnd();
            LocalDate endLocalDate = b.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int endYearMonth = PeriodCodeUtil.periodCodeByDate(b);
            log.info("结束日：{}", endYearMonth);
            log.info("结束日：{}", queryDate);
            int queryYearMonth = PeriodCodeUtil.periodCodeByDate(CommonDateUtils.nextYear(queryDate));
            log.info("查询日加一年：{}", queryYearMonth);
            // 是否一年内到期 修改：1.到期日小于 -筛选日期小于等于365 改为 到期日小于等于筛选日期年+1；//2.到期日小于筛选日期 满足一个即为true
            boolean withinOneYear = Boolean.FALSE;
            if (endLocalDate.isBefore(queryDateLocalDate) || endYearMonth <= queryYearMonth) {
                withinOneYear = Boolean.TRUE;
            }
//            DateUtil.isIn(b, queryDate, DateUtil.offset(queryDate, DateField.YEAR, 1));
            record.setWithinOneYear(withinOneYear ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode());
            record.setMarginStatus(MarginStatusEnum.ENTERED.getCode());
            record.setMarginType(marginType);
            if (MarginTypeEnum.RECLASSIFICATION.getCode().equals(marginType)) {
                record.setWithinOneYearDeposit(withinOneYear ? fv : BigDecimal.ZERO);
            } else {
                BigDecimal lpr = null;
                BigDecimal PV = null;
                BigDecimal currentEnterPL = null;
                //固定日期：2019-12-31
                if (a.compareTo(CommonDateUtils.bzjDefaultDate()) <= 0) {
                    lpr = DefaultConstant.DEFAULT_LPR;
                } else {
                    // 查起始日对应的LPR
                    lpr = getLprByMonth(lprConfigs, a);
                }
                double lprAndOne = NumberUtil.div(lpr, 100).doubleValue() + 1;//1.042
                BigDecimal principalAmount = NumberUtil.div(fv, Math.pow(lprAndOne, (double) DateUtil.betweenDay(a, b, true) / 365)).setScale(2, RoundingMode.HALF_UP);
                if (b.before(thisEndDate)) {
                    PV = fv;
                } else {
                    PV = NumberUtil.div(fv, Math.pow(lprAndOne, (double) DateUtil.betweenDay(b, thisEndDate, true) / 365)).setScale(2, RoundingMode.HALF_UP);
                }
                if (!b.before(lastEndDate)) {
                    if (a.before(lastEndDate)) {
                        currentEnterPL = NumberUtil.sub(PV, NumberUtil.div(fv, Math.pow(lprAndOne, (double) DateUtil.betweenDay(b, lastEndDate, true) / 365))).setScale(2, RoundingMode.HALF_UP);
                    } else {
                        currentEnterPL = NumberUtil.sub(PV, NumberUtil.div(fv, Math.pow(lprAndOne, (double) DateUtil.betweenDay(b, a, true) / 365))).setScale(2, RoundingMode.HALF_UP);
                    }
                }
                record.setLpr(lpr);
                record.setPrincipalAmount(principalAmount);
                record.setPv(PV);
                record.setCurrentEnterPl(currentEnterPL);
                record.setDepositInterestExpense(currentEnterPL);
                record.setDepositInterestIncome(currentEnterPL);
                record.setWithinOneYearDeposit(withinOneYear ? fv : BigDecimal.ZERO);
            }
            //如果是否一年内到期，如果是“否”，则future value改为0
            if (!withinOneYear) {
                record.setWithinOneYearDeposit(BigDecimal.ZERO);
            }
        }
//        if (MarginTypeEnum.RECLASSIFICATION.getCode().equals(marginType)) {
//            records = records.stream().filter(e -> e.getWithinOneYearDeposit().compareTo(BigDecimal.ZERO) != 0).collect(Collectors.toList());
//        }
        log.info("marginConvert:{}", JSON.toJSONString(records));
        return records;
    }

    private List<MarginContractBalanceVO> marginConvertToVO(List<MarginContractBalanceEntity> records, Date queryDate, Date thisEndDate, Date lastEndDate) {
        List<MarginContractBalanceVO> result = new ArrayList<>();
        List<LprDataVO> lprConfigs = lprDataService.listOneYear();
        for (MarginContractBalanceEntity record : records) {
            MarginContractBalanceVO marginContractBalanceVO = new MarginContractBalanceVO();
            BigDecimal fv = record.getContractBalance();
            BigDecimal lpr = null;
            BigDecimal PV = null;
            BigDecimal currentEnterPL = null;
            Date a = record.getLeaseDateStart();
            Date b = record.getLeaseDateEnd();
            boolean withinOneYear = b.after(queryDate) && DateUtil.isIn(queryDate, b, DateUtil.offset(b, DateField.YEAR, 1));
            // 是否一年内到期

            if (a.compareTo(CommonDateUtils.bzjDefaultDate()) <= 0) {
                lpr = DefaultConstant.DEFAULT_LPR;
            } else {
                // 查起始日对应的LPR
                lpr = getLprByMonth(lprConfigs, a);
            }
            double lprAndOne = NumberUtil.div(lpr, 100).doubleValue() + 1;//1.042
            BigDecimal principalAmount = NumberUtil.div(fv, Math.pow(lprAndOne, (double) DateUtil.betweenDay(a, b, true) / 365)).setScale(2, RoundingMode.HALF_UP);
            if (b.before(thisEndDate)) {
                PV = fv;
            } else {
                PV = NumberUtil.div(fv, Math.pow(lprAndOne, (double) DateUtil.betweenDay(b, thisEndDate, true) / 365)).setScale(2, RoundingMode.HALF_UP);
            }
            if (!b.before(lastEndDate)) {
                if (lastEndDate.before(a)) {
                    currentEnterPL = NumberUtil.sub(PV, NumberUtil.div(fv, Math.pow(lprAndOne, (double) DateUtil.betweenDay(b, lastEndDate, true) / 365))).setScale(2, RoundingMode.HALF_UP);
                } else {
                    currentEnterPL = NumberUtil.div(fv, Math.pow(lprAndOne, (double) DateUtil.betweenDay(b, a, true) / 365)).setScale(2, RoundingMode.HALF_UP);
                }
            }

            record.setLpr(lpr);
            record.setPrincipalAmount(principalAmount);
            record.setPv(PV);
            record.setCurrentEnterPl(currentEnterPL);
            record.setDepositInterestExpense(currentEnterPL);
            record.setDepositInterestIncome(currentEnterPL);
            record.setWithinOneYear(withinOneYear ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode());
            record.setWithinOneYearDeposit(withinOneYear ? fv : BigDecimal.ZERO);
            BeanUtil.copyProperties(record, marginContractBalanceVO);
            result.add(marginContractBalanceVO);
        }
        log.info("marginConvert:{}", JSON.toJSONString(result));
        return result;
    }

    private List<MarginContractBalanceEntity> contractBalanceConvertToMargin(List<Map<String, Object>> contractBalanceEntities, Date balanceDate, List<AccountVO> accountInfo) {
        List<MarginContractBalanceEntity> records = new ArrayList<>();
        if (CollectionUtils.isEmpty(contractBalanceEntities)) {
            return records;
        }
        Map<String, List<AccountVO>> busCodeMap = accountInfo.stream().collect(Collectors.groupingBy(e -> e.getBusinessCode()));
//        Map<String, ContractEntity> contractEntityMap = contractService.getBaseMapper().selectList(
//                        Wrappers.<ContractEntity>lambdaQuery().in(ContractEntity::getContractCode,
//                                contractBalanceEntities.stream().map(e -> e.get(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode())).collect(Collectors.toList())))
//                .stream().collect(Collectors.toMap(e -> e.getContractCode(), e -> e, (a, b) -> b));
        List<AccountEntity> accountEntities = accountService.getBaseMapper().selectList(Wrappers.<AccountEntity>lambdaQuery());
        Map<String, String> accountNameMap = accountEntities.stream().collect(Collectors.toMap(e -> e.getAccountCode(), e -> e.getAccountName(), (a, b) -> b));
        for (Map<String, Object> entity : contractBalanceEntities) {
            // 通过 accountCode + busCode确定一个fundType类型
            String businessCode = MapUtil.getStr(entity, ContractBalanceColumnsEnum.BUSINESS_CODE.getCode());
            List<AccountVO> accountVOS = busCodeMap.get(businessCode);
            if (CollectionUtils.isNotEmpty(accountVOS)) {
                accountInfo.stream().filter(e -> StringUtils.equals(e.getBusinessCode(), businessCode))
                        .forEach(accountVO -> {
                            String accountCode = accountVO.getAccountCode();
                            String fundType = accountVO.getFundType();
                            String fundAmountStr = MapUtil.getStr(entity, fundType + "_balance");
                            if (StringUtils.isNotBlank(fundAmountStr)) {
                                BigDecimal fundAmount = new BigDecimal(fundAmountStr);
                                if (BigDecimal.ZERO.compareTo(fundAmount) != 0) {
                                    MarginContractBalanceEntity marginContractBalanceEntity = new MarginContractBalanceEntity();
                                    marginContractBalanceEntity.setBalanceDate(DateUtil.beginOfDay(balanceDate));
                                    marginContractBalanceEntity.setBusinessDate(DateUtil.beginOfDay(balanceDate));
                                    marginContractBalanceEntity.setContractCode(MapUtil.getStr(entity, ContractBalanceColumnsEnum.CONTRACT_CODE.getCode()));
                                    marginContractBalanceEntity.setAccountPeriod(Integer.valueOf(DateUtil.format(balanceDate, DatePattern.SIMPLE_MONTH_PATTERN)));
                                    marginContractBalanceEntity.setClientCode(MapUtil.getStr(entity, ContractBalanceColumnsEnum.CLIENT_CODE.getCode()));
                                    marginContractBalanceEntity.setOrgId(MapUtil.getStr(entity, ContractBalanceColumnsEnum.ORG_ID.getCode()));
                                    marginContractBalanceEntity.setBusinessCode(businessCode);

                                    marginContractBalanceEntity.setContractName(MapUtil.getStr(entity, ContractBalanceColumnsEnum.CONTRACT_NAME.getCode()));
                                    marginContractBalanceEntity.setLeaseDateStart(
                                            MapUtil.getDate(entity, ContractBalanceColumnsEnum.LEASE_DATE_START.getCode()));
                                    marginContractBalanceEntity.setLeaseDateEnd(
                                            MapUtil.getDate(entity, ContractBalanceColumnsEnum.LEASE_DATE_END.getCode()));
                                    marginContractBalanceEntity.setBusinessName(
                                            MapUtil.getStr(entity, ContractBalanceColumnsEnum.BUSINESS_NAME.getCode()));
                                    marginContractBalanceEntity.setCurrencyType(
                                            MapUtil.getStr(entity, ContractBalanceColumnsEnum.CURRENCY_TYPE.getCode()));
                                    marginContractBalanceEntity.setClientName(
                                            MapUtil.getStr(entity, ContractBalanceColumnsEnum.CLIENT_NAME.getCode()));

                                    marginContractBalanceEntity.setAccountCode(accountCode);
                                    marginContractBalanceEntity.setAccountName(accountNameMap.get(accountCode));
                                    marginContractBalanceEntity.setContractBalance(fundAmount);
                                    if (null == marginContractBalanceEntity.getLeaseDateStart() || null == marginContractBalanceEntity.getLeaseDateEnd()) {
                                        log.info(marginContractBalanceEntity.getContractCode() + "该合同无合同起止日期");
                                    } else {
                                        records.add(marginContractBalanceEntity);
                                    }
                                }
                            }
                        });
            }
        }
        log.info("contractBalanceConvertToMarginContractBalanceVO:{}", JSON.toJSONString(records));
        return records;
    }

    /**
     * 根据日期获取lpr
     *
     * @param lprConfigs
     * @param beginDate
     * @return
     */
    private BigDecimal getLprByMonth(List<LprDataVO> lprConfigs, Date beginDate) {
        //取开始时间减一个月的对应月份的lpr
        Date lastMonth = CommonDateUtils.lastMonth(beginDate);
        String lastMonthYear = DateUtil.format(lastMonth, "yyyyMM");
        log.info("上个月的日期：{}", lastMonthYear);
        Optional<LprDataVO> configVO = lprConfigs.stream().filter(e -> DateUtil.format(e.getStartDate(), "yyyyMM").equals(lastMonthYear)).findFirst();
        log.info("上个月的LPR：{}", configVO);
        BigDecimal lpr = DefaultConstant.DEFAULT_LPR;
        if (configVO.isPresent()) {
            lpr = configVO.get().getLprRate();
        }
        return lpr;
    }

    private void batchDeleteVoucher(List<MarginContractBalanceEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            voucherService.deleteByBatchIdList(list.stream().map(MarginContractBalanceEntity::getBatchId).distinct().collect(Collectors.toList()), BatchTypeEnum.BZJ.getCode());
        });
    }

    @Override
    public Boolean updateProcessStatus(CommonApproveDTO commonApproveDTO) {
        if (StringUtils.isEmpty(commonApproveDTO.getDocumentStatus())) {
            throw new ServiceException("复核状态不可以为空");
        }
        List<MarginContractBalanceEntity> balanceEntityList = this.lambdaQuery().eq(MarginContractBalanceEntity::getBatchId, commonApproveDTO.getDocumentId()).list();
        if (CollectionUtils.isEmpty(balanceEntityList)) {
            throw new ServiceException("复核数据不存在");
        }
        String processStatus;
        if (ProcessStatusEnum.REVIEWED.getCode().equals(commonApproveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REVIEWED.getCode();
        } else if (ProcessStatusEnum.REJECTED.getCode().equals(commonApproveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REJECTED.getCode();
        } else {
            processStatus = balanceEntityList.get(0).getMarginStatus();
        }
        balanceEntityList.forEach(v -> {
            v.setMarginStatus(processStatus);
        });
        //更新凭证信息
        iVoucherService.updateStatusByBatch(Lists.newArrayList(commonApproveDTO.getDocumentId()), BatchTypeEnum.BZJ.getCode(), processStatus, commonApproveDTO.getApproverNum(), commonApproveDTO.getApproverName());
        return this.updateBatchById(balanceEntityList);
    }

    @Override
    public Map<String, String> export(MarginContractBalanceQueryDTO queryDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fileName = "保证金_" + LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss") + ".xlsx";

        String filePath = getFilePath();
        FileRecordEntity record = new FileRecordEntity();
        record.setModuleName(ModuleEnum.MARGIN_CONTRACT.getCode());
        record.setBusinessScene(BusinessSceneEnum.MARGIN_CONTRACT.getCode());
        record.setFileLocation(filePath + fileName);
        record.setFileName(fileName);
        record.setExecuteStatus(CheckExecuteStatusEnum.INPROGRESS.getCode());
        record.setFileUploadBy(String.valueOf(SecurityUtils.getUserId()));
        fileRecordService.save(record);

        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            // 异步执行的任务
            queryAndWriteTable(queryDTO, fileName);
        }, asyncTaskExecutor).thenRun(() -> {
            FileRecordEntity tmp = new FileRecordEntity();
            tmp.setId(record.getId());
            tmp.setExecuteStatus(CheckExecuteStatusEnum.FINISH.getCode());
            tmp.setFileUploadTime(LocalDateTime.now());
            fileRecordService.updateById(tmp);
        });

        Map<String, String> map = Maps.newLinkedHashMap();
        map.put("fileName", fileName);
        map.put("location", filePath + fileName);
        map.put("servicePath", servicePath);
        return map;
    }

    public void queryAndWriteTable(MarginContractBalanceQueryDTO queryDTO, String fileName) {
        log.info("查询要导出的数据 开始");
        long l1 = System.currentTimeMillis();
        List<MarginContractBalanceVO> list = this.selectList(queryDTO);
        List<MarginContractBalanceMonthExcel> monthExcelList = Lists.newArrayList();
        list.forEach(v -> {
            MarginContractBalanceMonthExcel monthExcel = BeanUtil.copyProperties(v, MarginContractBalanceMonthExcel.class);
            monthExcel.setBusinessDate(DateUtil.format(v.getBalanceDate(), "yyyy-MM-dd"));
            monthExcel.setFinanceDate(DateUtil.format(v.getFinanceDate(), "yyyy-MM-dd"));
            monthExcel.setLeaseDateEnd(DateUtil.format(v.getLeaseDateEnd(), "yyyy-MM-dd"));
            monthExcel.setLeaseDateStart(DateUtil.format(v.getLeaseDateStart(), "yyyy-MM-dd"));
            if (YesOrNoEnum.YES.getCode().equals(v.getWithinOneYear())) {
                monthExcel.setWithinOneYear(YesOrNoEnum.YES.getDesc());
            } else {
                monthExcel.setWithinOneYear(YesOrNoEnum.NO.getDesc());
            }
            monthExcel.setMarginStatus(ProcessStatusEnum.getDescByCode(v.getMarginStatus()));
            monthExcelList.add(monthExcel);
        });
        long l2 = System.currentTimeMillis();
        log.info("查询要导出的数据 结束，用时{} s", (l2 - l1) / 1000);
        log.info("导出Excel数据 开始");
        ExcelWriter writer = ExcelUtil.getWriter(getFilePath() + fileName);
        writer.addHeaderAlias("businessDate", "业务日期");
        writer.addHeaderAlias("financeDate", "财务日期");
        writer.addHeaderAlias("orgId", "签约主体");
        writer.addHeaderAlias("contractCode", "合同编码");
        writer.addHeaderAlias("contractName", "合同名称");
        writer.addHeaderAlias("accountCode", "科目编码");
        writer.addHeaderAlias("accountName", "科目名称");
        writer.addHeaderAlias("currencyType", "币种");
        writer.addHeaderAlias("contractBalance", "保证金余额");
        writer.addHeaderAlias("leaseDateStart", "财务起租日");
        writer.addHeaderAlias("leaseDateEnd", "约定到期日");
        writer.addHeaderAlias("withinOneYearDeposit", "应付一年内到期保证金");
        writer.addHeaderAlias("depositInterestExpense", "保证金利息支出");
        writer.addHeaderAlias("depositInterestIncome", "保证金利息收入");
        writer.addHeaderAlias("withinOneYear", "是否一年内到期");
        writer.addHeaderAlias("lpr", "贷款利率");
        writer.addHeaderAlias("pv", "PV");
        writer.addHeaderAlias("principalAmount", "本金");
        writer.addHeaderAlias("currentEnterPl", "本期进入PL");
        writer.addHeaderAlias("marginStatus", "状态");
        writer.autoSizeColumnAll();
        writer.setColumnWidth(-1, 20);
        writer.write(monthExcelList, true);
        int rowSize = writer.getColumnCount();
        for (int i = 1; i < monthExcelList.size(); i++) {
            Row row = writer.getOrCreateRow(i);
            Double value = row.getCell(8).getNumericCellValue();
            if (value.compareTo(0.0d) < 0) {
                setCellStyle(writer, 8, i, IndexedColors.YELLOW.getIndex());
            }
        }
        writer.close();
        long l3 = System.currentTimeMillis();
        log.info("导出Excel数据 结束，用时{} s", (l3 - l2) / 1000);
    }

    private static void setCellStyle(ExcelWriter writer, int x, int y, short index) {
        CellStyle cellStyle = writer.createCellStyle(x, y);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setFillForegroundColor(index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }


    private String getFilePath() {
        String osName = System.getProperties().getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            return basicPathWindows + "marginContract" + File.separator;
        } else if (osName.toLowerCase().contains("linux") || osName.toLowerCase().contains("unix")) {
            return basicPathLinux + "marginContract" + File.separator;
        }
        return com.utfinancing.financehub.common.core.utils.StringUtils.EMPTY;
    }

    public List<Map<String, Object>> getMarginBalance(MarginContractBalanceQueryDTO queryDTO) {
        //清空临时表
        marginContractBalanceMapper.clearMarginTemp();
        //固化上个月的余额进入临时表
        marginContractBalanceMapper.initLastMonthBalance(queryDTO);
        //固化本月的最新余额进入临时表，有则更新无则新增
        marginContractBalanceMapper.initContractBalance(queryDTO);
        //更新特殊经营租赁合同
        R<List<SysDictData>> sysDictData = remoteDictService.listDictData(DictTypeEnum.SPECIAL_ZYZL_CONTRACT.getCode());
        if (CollectionUtil.isNotEmpty(sysDictData.getData())) {
            for (SysDictData dictData : sysDictData.getData()) {
                List<String> contracts = Arrays.asList(dictData.getDictLabel().split(","));
                marginContractBalanceMapper.updateSpecialContractBalance(contracts,dictData.getDictValue());
            }
        }
        return marginContractBalanceMapper.selectMarginContractTemp(queryDTO);
    }


}

