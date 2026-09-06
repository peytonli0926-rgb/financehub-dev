package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import com.utfinancing.financehub.engine.finance.mapper.AccountAssistBalanceMapper;
import com.utfinancing.financehub.engine.finance.mapper.AccountBalanceMapper;
import com.utfinancing.financehub.engine.finance.mapper.PayableInsuranceMapper;
import com.utfinancing.financehub.engine.finance.mapper.PayableInsuranceReportDataMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-24
 * @Description :  PayableInsurance服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class PayableInsuranceServiceImpl extends ServiceImpl<PayableInsuranceMapper, PayableInsuranceEntity> implements IPayableInsuranceService {

    private final PayableInsuranceMapper payableInsuranceMapper;
    private final IPayableInsuranceDetailsService payableInsuranceDetailsService;
    private final AccountBalanceMapper accountBalanceMapper;
    private final IContractService contractService;
    private final IRuleService iRuleService;
    private final RemoteDictService remoteDictService;
    private final IOrgCompanyService orgCompanyService;
    private final PayableInsuranceReportDataMapper payableInsuranceReportDataMapper;
    private final IVoucherService iVoucherService;
    private final AccountAssistBalanceMapper accountAssistBalanceMapper;

    @Value("${approve.url.payableInsurance-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;

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

    @Override
    public Long savePayableInsurance(PayableInsuranceDTO dto) {
        PayableInsuranceEntity entity = BeanUtil.copyProperties(dto, PayableInsuranceEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updatePayableInsurance(Long id, PayableInsuranceDTO dto) {
        PayableInsuranceEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public PayableInsuranceDTO getPayableInsuranceDTOById(Long id) {
        PayableInsuranceEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, PayableInsuranceDTO.class);
    }

    @Override
    public Void generate(PayableInsuranceQueryDTO queryDTO) {
        if (ObjectUtil.isEmpty(queryDTO.getBusinessDate())) {
            throw new ServiceException("业务日期必填");
        }
        Date queryDate = DateUtil.beginOfDay(queryDTO.getBusinessDate());
        DateTime endDate = DateUtil.beginOfDay(DateUtil.endOfMonth(queryDate));// 期末
        // 转为会计期间
        Integer periodCode = NumberUtil.parseInt(DateUtil.format(queryDate, "yyyyMM"));
        // 获取上一期的会计期间
        Integer lastPeriodCode = NumberUtil.parseInt(DateUtil.format(DateUtil.offsetMonth(queryDate, -1), "yyyyMM"));

        // 获取现有数据
        List<PayableInsuranceEntity> payableInsuranceEntities = payableInsuranceMapper.selectList(Wrappers.<PayableInsuranceEntity>lambdaQuery().eq(PayableInsuranceEntity::getBusinessDate, endDate)
                .in(ObjectUtil.isNotEmpty(queryDTO.getOrgIdList()), PayableInsuranceEntity::getOrgId, queryDTO.getOrgIdList()));
        if (CollectionUtils.isNotEmpty(payableInsuranceEntities)) {
            // 校验状态，如果存在已提交，不能生成
            payableInsuranceEntities.forEach(a -> {
                if (!(ProcessStatusEnum.ENTERED.getCode().equals(a.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(a.getProcessStatus()))) {
                    throw new ServiceException("本月存在状态为[" + ProcessStatusEnum.getDescByCode(a.getProcessStatus()) + "]，不能再次生成信息");
                }
            });

            // 删除未提交数据
            List<Long> deleteIds = payableInsuranceEntities.stream().filter(e -> MarginStatusEnum.canChangeStatus().contains(e.getProcessStatus())).map(e -> e.getId()).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deleteIds)) {
                this.removeBatchByIds(deleteIds);
                payableInsuranceDetailsService.remove(new LambdaQueryWrapper<PayableInsuranceDetailsEntity>()
                        .in(PayableInsuranceDetailsEntity::getPayableInsuranceId, deleteIds));
                //删除凭证
                batchDeleteVoucher(deleteIds);
            }
        }
        // 从科目辅助帐余额表获取数据
        String accountCode = AccountEnum.YFBXFZG.getCode();
        //优化
        AccountAssistBalanceQueryDTO balanceQueryDTO = new AccountAssistBalanceQueryDTO();
        balanceQueryDTO.setAccountCode(accountCode);
        balanceQueryDTO.setLastPeriodCode(lastPeriodCode);
        balanceQueryDTO.setPeriodCode(periodCode);
        balanceQueryDTO.setOrgIdList(queryDTO.getOrgIdList());
        List<PayableInsuranceDetailsEntity> payableInsuranceDetailsEntities = payableInsuranceMapper.getPayableInsuranceDetails(balanceQueryDTO);
        //按照合同+签约主体分组
        List<String> contractCodeList = payableInsuranceDetailsEntities.stream().map(PayableInsuranceDetailsEntity::getContractCode).collect(Collectors.toList());
        List<PayableInsuranceReportDataEntity> interfaceTotalDTOS = payableInsuranceReportDataMapper.selectList(Wrappers.<PayableInsuranceReportDataEntity>lambdaQuery().in(PayableInsuranceReportDataEntity::getContractCode, contractCodeList));
        List<PayableInsuranceDetailsEntity> detailsEntityList = Lists.newArrayList();
        //未处理处理的凭证数据
        for (PayableInsuranceDetailsEntity detailsEntity : payableInsuranceDetailsEntities) {
            // 只生成 应付保险费-暂估 科目余额不为0 的数据
            detailsEntity.setBusinessDate(endDate);
            detailsEntity.setPayableInsuranceBalance(detailsEntity.getPayableInsuranceEstimateBalanceEnding());
            if (detailsEntity.getPayableInsuranceBalance().compareTo(BigDecimal.ZERO) == 0
                && detailsEntity.getPayableInsuranceEstimateBalanceOpening().compareTo(BigDecimal.ZERO) == 0
                    && detailsEntity.getPayableInsuranceEstimateAmountDebit().compareTo(BigDecimal.ZERO) == 0
                    && detailsEntity.getPayableInsuranceEstimateAmountCredit().compareTo(BigDecimal.ZERO) == 0) {
                log.info("合同号：{},金额等于0，期初：{}，贷方：{}，借方：{}", detailsEntity.getContractCode(), detailsEntity.getPayableInsuranceEstimateBalanceOpening(), detailsEntity.getPayableInsuranceEstimateAmountCredit(), detailsEntity.getPayableInsuranceEstimateAmountDebit());
                continue;
            }
            // 投保事件接口表actualPayableInsuaranceAmount-payableInsuranceAmount发生额汇总
            detailsEntity.setPayableInsuranceBalanceReport(interfaceTotalDTOS.stream().filter(a -> ObjectUtil.equals(a.getContractCode(), detailsEntity.getContractCode())
                    && ObjectUtil.equals(a.getOrgId(), detailsEntity.getOrgId())).map(PayableInsuranceReportDataEntity::getPayableInsuranceBalance).reduce(BigDecimal.ZERO, BigDecimal::add));
            if ((ContractStatusEnum.HTJS.getCode().equals(detailsEntity.getContractStatus()) && detailsEntity.getPayableInsuranceBalanceReport().compareTo(BigDecimal.ZERO) == 0)
                    || detailsEntity.getPayableInsuranceEstimateBalanceEnding().compareTo(BigDecimal.ZERO) < 0) {
                detailsEntity.setCarryoverAmount(detailsEntity.getPayableInsuranceEstimateBalanceEnding());
            }else {
                detailsEntity.setCarryoverAmount(BigDecimal.ZERO);
            }
            detailsEntityList.add(detailsEntity);
        }
        // 汇总
        Map<String, List<PayableInsuranceDetailsEntity>> collectMap = detailsEntityList.stream().collect(Collectors.groupingBy(PayableInsuranceDetailsEntity::getOrgId));
        collectMap.forEach((orgId, detailsEntities) -> {
            PayableInsuranceEntity payableInsuranceEntity = new PayableInsuranceEntity();
            payableInsuranceEntity.setBusinessDate(endDate);
            payableInsuranceEntity.setOrgId(orgId);
            payableInsuranceEntity.setProcessStatus(MarginStatusEnum.ENTERED.getCode());
            payableInsuranceEntity.setCarryoverAmount(detailsEntities.stream().map(PayableInsuranceDetailsEntity::getCarryoverAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            save(payableInsuranceEntity);
            detailsEntities.forEach(e -> e.setPayableInsuranceId(payableInsuranceEntity.getId()));
            payableInsuranceDetailsService.saveBatch(detailsEntities);
        });
        return null;
    }


    @Override
    public Boolean voucher(List<Long> ids, String isSubmit) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少选择一条数据生成凭证");
        }
        List<PayableInsuranceEntity> serviceFeeEntities = listByIds(ids);
        serviceFeeEntities.forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("处理状态为已录入或者已拒绝的才可以生成凭证");
            }
        });
        List<PayableInsuranceDetailsEntity> detailsEntities = payableInsuranceDetailsService.getBaseMapper().selectList(Wrappers.<PayableInsuranceDetailsEntity>lambdaQuery()
                .in(PayableInsuranceDetailsEntity::getPayableInsuranceId, serviceFeeEntities.stream().map(e -> e.getId()).collect(Collectors.toList())));
        if (CollectionUtils.isEmpty(detailsEntities)) {
            throw new ServiceException("缺少保险费信息");
        }
        if (YesOrNoEnum.NO.getCode().equals(isSubmit)) {
            //校验任务
            ids.forEach(v -> {
                Boolean isExistFlag = iBatchTaskService.isExistTask(v, BatchTypeEnum.YFBXF.getCode());
                if (isExistFlag) {
                    throw new ServiceException("存在任务正在执行，请稍后重试");
                }
            });
            //保存任务
            List<Long> taskIdList = Lists.newArrayList();
            ids.forEach(v -> {
                taskIdList.add(iBatchTaskService.saveBatchTask(BatchTaskDTO.builder().businessId(v).businessType(BatchTypeEnum.YFBXF.getCode()).status("1").build()));
            });
            CompletableFuture.runAsync(() -> {
                generateVoucher(detailsEntities, serviceFeeEntities, ids, isSubmit);
            }).whenComplete((v, e) -> {
                // 执行成功，更新任务状态
                iBatchTaskService.updateBatchTask(taskIdList, "2");
            }).exceptionally(e -> {
                log.info("批量处理数据失败", e);
                iBatchTaskService.updateBatchTask(taskIdList, "3");
                return null;
            });
        } else {
            generateVoucher(detailsEntities, serviceFeeEntities, ids, isSubmit);
        }
        return Boolean.TRUE;
    }

    public Boolean generateVoucher(List<PayableInsuranceDetailsEntity> detailsEntities, List<PayableInsuranceEntity> serviceFeeEntities, List<Long> ids, String isSubmit) {
        //生成凭证前先删除之前的凭证
        batchDeleteVoucher(ids);
        DateTime accountDate = DateUtil.beginOfDay(new Date());
        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        final BigDecimal[] totalAmount = {BigDecimal.ZERO};
        String createUserNo = UserUtils.getStaffCode();
        String createUserName = UserUtils.getStaffName();
        detailsEntities.forEach(e -> {
            ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
            commonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            commonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            commonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
            commonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
            commonDTO.setBusinessDate(e.getBusinessDate());
            commonDTO.setOrderId(e.getId().toString());
            commonDTO.setContractCode(e.getContractCode());
            commonDTO.setClientCode(e.getClientCode());
            commonDTO.setClientName(e.getClientName());
            commonDTO.setSceneCode(SceneEnum.YFBXF.getCode());
            commonDTO.setSceneName(SceneEnum.YFBXF.getDesc());
            commonDTO.setOrgId(e.getOrgId());
            //设置accountDate导致合同对应金额字段值置为0BUG
            commonDTO.setAccountDate(e.getBusinessDate());
            commonDTO.setContractStatus(e.getContractStatus());
            commonDTO.setFinancialContractStatus(e.getFinancialContractStatus());
            commonDTO.setBatchId(e.getPayableInsuranceId());
            commonDTO.setBatchType(BatchTypeEnum.YFBXF.getCode());
            commonDTO.setFinanceDate(DateUtil.toLocalDateTime(e.getBusinessDate()));
            commonDTO.setIsSubmit(isSubmit);
            commonDTO.setCreateUserNo(createUserNo);
            commonDTO.setCreateUserName(createUserName);
            Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);
            commonMap.put("carryOverAmount", e.getCarryoverAmount());
            commonMap.put("statementBalance", e.getPayableInsuranceBalanceReport());
            commonMap.put("estimatePayableInsurance", e.getPayableInsuranceBalance());
            commonMap.put("period", DateUtil.format(e.getBusinessDate(), "yyyy.MM"));
            voucherMapList.add(commonMap);
            totalAmount[0] = totalAmount[0].add(null == e.getCarryoverAmount() ? BigDecimal.ZERO : e.getCarryoverAmount());
        });
        log.info("结转总金额：{}", totalAmount[0]);
        log.info("生成凭证参数：{}", JSON.toJSONString(voucherMapList));
        List<VoucherInfoVO> voucherInfoVOList = iRuleService.batchExecuteRule(voucherMapList);
        Boolean isExistVoucherError = voucherInfoVOList.stream().allMatch(v -> StringUtils.isNotEmpty(v.getErrorInfo()));
        if (YesOrNoEnum.YES.getCode().equals(isSubmit) && isExistVoucherError) {
            // 异步删除已生成的凭证
            List<Long> voucherIdList = Lists.newArrayList();
            voucherInfoVOList.stream().forEach(voucherInfoVO -> {
                if (CollectionUtils.isNotEmpty(voucherInfoVO.getVoucherDTOList())) {
                    voucherIdList.addAll(voucherInfoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).collect(Collectors.toList()));
                }
            });
            asnyDeleteVoucher(voucherIdList);
            return Boolean.FALSE;
        }
        if (voucherInfoVOList.isEmpty()) {
            throw new ServiceException("应收保险费 生成凭证失败");
        } else {
            LocalDateTime voucherDate = null;
            for (VoucherInfoVO entry : voucherInfoVOList) {
                List<VoucherDTO> value = entry.getVoucherDTOList();
                if (CollectionUtils.isNotEmpty(value)) {
                    String vouchIds = value.stream().map(VoucherDTO::getId).map(String::valueOf).collect(
                            Collectors.joining(","));
                    voucherDate = value.get(0).getVoucherDate();
                    payableInsuranceDetailsService.lambdaUpdate()
                            .set(PayableInsuranceDetailsEntity::getVoucherId, vouchIds)
                            .set(PayableInsuranceDetailsEntity::getAccountDate, voucherDate)
                            .set(PayableInsuranceDetailsEntity::getExceptionType, entry.getErrorInfo())
                            .eq(PayableInsuranceDetailsEntity::getId, Long.parseLong(entry.getOrderId()))
                            .update();
                }
            }
            LocalDateTime finalVoucherDate = voucherDate;
            serviceFeeEntities.forEach(e -> {
                e.setIsGenerateVoucher(YesOrNoEnum.YES.getCode());
                e.setAccountDate(CommonDateUtils.parseLocalDateTimeToDate(finalVoucherDate));
            });
            updateBatchById(serviceFeeEntities);
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
            iVoucherService.deleteByIdList(voucherIdList);
        });
    }

    /**
     * 删除凭证
     *
     * @param ids
     */
    public void batchDeleteVoucher(List<Long> ids) {
        //根据批次号删除凭证
        iVoucherService.deleteByBatchIdList(ids, BatchTypeEnum.YFBXF.getCode());
    }

    @Override
    public Map<String, String> export(PayableInsuranceQueryDTO queryDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fileName = "应付保险费_" + LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss") + ".xlsx";

        String filePath = getFilePath();
        FileRecordEntity record = new FileRecordEntity();
        record.setModuleName(ModuleEnum.PAYABLE_INSURANCE.getCode());
        record.setBusinessScene(BusinessSceneEnum.PAYABLE_INSURANCE.getCode());
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

    public void queryAndWriteTable(PayableInsuranceQueryDTO queryDTO, String fileName) {
        log.info("查询要导出的数据 开始");
        long l1 = System.currentTimeMillis();
        List<PayableInsuranceDetailsVO> list = this.selectDetailList(queryDTO);
        List<PayableInsuranceDetailExcel> detailExcelList = Lists.newArrayList();
        list.forEach(v -> {
            PayableInsuranceDetailExcel excel = BeanUtil.copyProperties(v, PayableInsuranceDetailExcel.class);
            excel.setAccountDate(DateUtil.format(v.getAccountDate(), "yyyy-MM-dd"));
            excel.setBusinessDate(DateUtil.format(v.getBusinessDate(), "yyyy-MM-dd"));
            detailExcelList.add(excel);
        });
        long l2 = System.currentTimeMillis();
        log.info("查询要导出的数据 结束，用时{} s", (l2 - l1) / 1000);
        log.info("导出Excel数据 开始");
        ExcelWriter writer = ExcelUtil.getWriter(getFilePath() + fileName);
        writer.addHeaderAlias("businessDate", "业务日期");
        writer.addHeaderAlias("accountDate", "记账日期");
        writer.addHeaderAlias("orgId", "签约主体");
        writer.addHeaderAlias("contractCode", "合同编号");
        writer.addHeaderAlias("clientCode", "客户编码");
        writer.addHeaderAlias("clientName", "客户名称");
        writer.addHeaderAlias("contractStatus", "业务合同状态");
        writer.addHeaderAlias("financialContractStatus", "财务合同状态");
        writer.addHeaderAlias("payableInsuranceEstimateBalanceOpening", "应付保险费-暂估期初余额");
        writer.addHeaderAlias("payableInsuranceEstimateAmountDebit", "应付保险费-暂估借方");
        writer.addHeaderAlias("payableInsuranceEstimateAmountDebit", "应付保险费-暂估借方");
        writer.addHeaderAlias("payableInsuranceEstimateAmountCredit", "应付保险费-暂估贷方");
        writer.addHeaderAlias("payableInsuranceEstimateBalanceEnding", "应付保险费-暂估期末余额");
//        writer.addHeaderAlias("payableInsuranceBalance", "应付保险费余额");
        writer.addHeaderAlias("carryoverAmount", "结转金额");
        writer.addHeaderAlias("payableInsuranceBalanceReport", "保险费支付报表余额");
        writer.addHeaderAlias("payableInsuranceBalanceActual", "保险费实际支付（不含税）");
        writer.addHeaderAlias("payableInsuranceBalanceLease", "起租时点保险费金额（不含税）");
        writer.addHeaderAlias("payableInsuranceBalanceStructure", "保险费交易结构调整（不含税）");
        writer.addHeaderAlias("payableInsuranceBalanceWithdrawal", "合同撤销（不含税）");
        writer.autoSizeColumnAll();
        writer.setColumnWidth(-1, 20);
        writer.write(detailExcelList, true);
        writer.close();
        long l3 = System.currentTimeMillis();
        log.info("导出Excel数据 结束，用时{} s", (l3 - l2) / 1000);
    }

    private String getFilePath() {
        String osName = System.getProperties().getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            return basicPathWindows + "payableInsurance" + File.separator;
        } else if (osName.toLowerCase().contains("linux") || osName.toLowerCase().contains("unix")) {
            return basicPathLinux + "payableInsurance" + File.separator;
        }
        return com.utfinancing.financehub.common.core.utils.StringUtils.EMPTY;
    }

    @Override
    public IPage<PayableInsuranceVO> selectPage(PayableInsuranceQueryDTO queryDTO) {
        LambdaQueryWrapper<PayableInsuranceEntity> queryWrapper = Wrappers.<PayableInsuranceEntity>lambdaQuery();
        Date queryDate = queryDTO.getAccountDate();
        if (null != queryDate) {
            queryDate = DateUtil.beginOfDay(queryDate);
            queryWrapper.eq(PayableInsuranceEntity::getAccountDate, queryDate);
        }
        Date businessDate = queryDTO.getBusinessDate();
        if (null != businessDate) {
            businessDate = DateUtil.beginOfDay(businessDate);
            queryWrapper.eq(PayableInsuranceEntity::getBusinessDate, businessDate);
        }
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), PayableInsuranceEntity::getOrgId, queryDTO.getOrgIdList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList()), PayableInsuranceEntity::getProcessStatus, queryDTO.getProcessStatusList());
        queryWrapper.eq(ObjectUtil.isNotEmpty(queryDTO.getId()), PayableInsuranceEntity::getId, queryDTO.getId());
        queryWrapper.orderByDesc(PayableInsuranceEntity::getId);
        IPage<PayableInsuranceEntity> entityIPage = payableInsuranceMapper.selectPage(new Page<PayableInsuranceEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        IPage<PayableInsuranceVO> page = ListBeanUtil.copyPage(entityIPage, PayableInsuranceVO.class);
        page.getRecords().forEach(v -> {
            v.setBatchType(BatchTypeEnum.YFBXF.getCode());
        });
        return page;
    }

    @Override
    public IPage<PayableInsuranceDetailsVO> selectDetailPage(PayableInsuranceDetailsQueryDTO queryDTO) {
        LambdaQueryWrapper<PayableInsuranceDetailsEntity> queryWrapper = Wrappers.<PayableInsuranceDetailsEntity>lambdaQuery();
        queryWrapper.eq(null != queryDTO.getPayableInsuranceId(), PayableInsuranceDetailsEntity::getPayableInsuranceId, queryDTO.getPayableInsuranceId());
        queryWrapper.like(StringUtils.isNotBlank(queryDTO.getContractCode()), PayableInsuranceDetailsEntity::getContractCode, queryDTO.getContractCode());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getContractStatus()), PayableInsuranceDetailsEntity::getContractStatus, queryDTO.getContractStatus());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getFinancialContractStatus()), PayableInsuranceDetailsEntity::getFinancialContractStatus, queryDTO.getFinancialContractStatus());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getPayableInsuranceIdList()), PayableInsuranceDetailsEntity::getPayableInsuranceId, queryDTO.getPayableInsuranceIdList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getContractStatusList()), PayableInsuranceDetailsEntity::getContractStatus, queryDTO.getContractStatusList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getFinancialContractStatusList()), PayableInsuranceDetailsEntity::getFinancialContractStatus, queryDTO.getFinancialContractStatusList());
        IPage<PayableInsuranceDetailsEntity> entityIPage = payableInsuranceDetailsService.getBaseMapper().selectPage(new Page<PayableInsuranceDetailsEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, PayableInsuranceDetailsVO.class);
    }

    @Override
    public List<PayableInsuranceDetailsVO> selectDetailList(PayableInsuranceQueryDTO queryDTO) {
        LambdaQueryWrapper<PayableInsuranceDetailsEntity> queryWrapper = Wrappers.lambdaQuery();
        Date queryDate = queryDTO.getAccountDate();
        if (null != queryDate) {
            queryDate = DateUtil.beginOfDay(queryDate);
            queryWrapper.eq(PayableInsuranceDetailsEntity::getAccountDate, queryDate);
        }
        Date businessDate = queryDTO.getBusinessDate();
        if (null != businessDate) {
            businessDate = DateUtil.beginOfDay(businessDate);
            queryWrapper.eq(PayableInsuranceDetailsEntity::getBusinessDate, businessDate);
        }
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), PayableInsuranceDetailsEntity::getOrgId, queryDTO.getOrgIdList());
        List<PayableInsuranceDetailsEntity> entityIPage = payableInsuranceDetailsService.getBaseMapper().selectList(queryWrapper);
        List<PayableInsuranceDetailsVO> payableInsuranceDetailsVOS = ListBeanUtil.copyList(entityIPage, PayableInsuranceDetailsVO.class);
        translateDict(payableInsuranceDetailsVOS);
        return payableInsuranceDetailsVOS;
    }

    private void translateDict(List<PayableInsuranceDetailsVO> list) {
        //业务合同状态
        R<List<SysDictData>> contractStatusR = remoteDictService.listDictData(DictTypeEnum.CONTRACT_STATUS.getCode());
        Map<String, String> contractStatusMap = contractStatusR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        //财务合同状态
        R<List<SysDictData>> financialContractStatusR = remoteDictService.listDictData(DictTypeEnum.FINANCIAL_CONTRACT_STATUS.getCode());
        Map<String, String> financialContractStatusMap = financialContractStatusR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        //签约主体
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgId(), e -> e.getOrgName(), (a, b) -> b));
        list.forEach(e -> {
            // 签约主体
            e.setOrgId(companyMap.get(e.getOrgId()));
        });
    }

    @Override
    public void importData(List<PayableInsuranceDetailImport> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 校验字段
        checkDate(list);
        //签约主体
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgName(), e -> e.getOrgId(), (a, b) -> b));
        //根据日期+主体+合同+客户确定一条数据覆盖 数据是否存在 上级数据状态 获取下级所有金额 更新上级汇总金额
        List<PayableInsuranceDetailsEntity> payableInsuranceDetailsEntities = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        AtomicInteger i = new AtomicInteger(1);
        PayableInsuranceQueryDTO queryDTO = new PayableInsuranceQueryDTO();
        queryDTO.setBusinessDate(list.get(0).getBusinessDate());
        List<PayableInsuranceDetailsVO> detailList = payableInsuranceMapper.selectPayableInsuranceList(queryDTO);
        Map<String, List<PayableInsuranceDetailsVO>> detailMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(detailList)) {
            detailMap = detailList.stream().collect(Collectors.groupingBy(v -> v.getOrgId() + "-" + v.getContractCode()));
        }
        double totalSize = list.size();
        Map<String, List<PayableInsuranceDetailsVO>> finalDetailMap = detailMap;
        list.forEach(detail -> {
            detail.setOrgId(companyMap.get(detail.getOrgId()));
            detail.setBusinessDate(detail.getBusinessDate());
            List<PayableInsuranceDetailsVO> detailsVOS = finalDetailMap.get(detail.getOrgId() + "-" + detail.getContractCode());
            if (CollectionUtils.isNotEmpty(detailsVOS)) {
                PayableInsuranceDetailsEntity one = BeanUtil.copyProperties(detailsVOS.get(0), PayableInsuranceDetailsEntity.class);
                if (MarginStatusEnum.cantChangeStatus().contains(detailsVOS.get(0).getProcessStatus())) {
                    throw new ServiceException(detail.getBusinessDate() + detail.getOrgId() + "状态错误");
                }
                one.setCarryoverAmount(detail.getCarryoverAmount());
                payableInsuranceDetailsEntities.add(one);
            } else {
                String error = "根据业务日期[" + detail.getBusinessDate() + "],签约主体[" + detail.getOrgId() + "],合同编号["
                        + detail.getContractCode() + "],未查询到相关数据。";
                stringBuffer.append(error);
            }
            log.info("上传校验进度：{}", NumberUtil.formatPercent(i.get() / totalSize, 2));
            i.getAndIncrement();
        });
        if (stringBuffer.length() != 0) {
            throw new ServiceException(stringBuffer.toString());
        }
        payableInsuranceDetailsService.updateBatchById(payableInsuranceDetailsEntities);
        // 更新上级汇总 结转金额
        List<Long> insuranceIds = payableInsuranceDetailsEntities.stream().map(e -> e.getPayableInsuranceId()).distinct().collect(Collectors.toList());
        Map<Long, List<PayableInsuranceDetailsEntity>> groupByInsuranceId = payableInsuranceDetailsService.getBaseMapper()
                .selectList(Wrappers.<PayableInsuranceDetailsEntity>lambdaQuery().in(PayableInsuranceDetailsEntity::getPayableInsuranceId, insuranceIds))
                .stream().collect(Collectors.groupingBy(e -> e.getPayableInsuranceId()));
        groupByInsuranceId.forEach((insuranceId, detailsEntityList) -> {
            Optional<Date> first = detailsEntityList.stream().map(PayableInsuranceDetailsEntity::getAccountDate).filter(Objects::nonNull).distinct().findFirst();
            LambdaUpdateWrapper<PayableInsuranceEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
            updateChainWrapper
                    .eq(PayableInsuranceEntity::getId, insuranceId)
                    // .eq(first.isPresent(), PayableInsuranceEntity::getAccountDate, first.get())
                    .set(PayableInsuranceEntity::getCarryoverAmount, detailsEntityList.stream().map(PayableInsuranceDetailsEntity::getCarryoverAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            this.update(updateChainWrapper);
        });
    }

    /**
     * 校验字段
     *
     * @param list
     */
    private void checkDate(List<PayableInsuranceDetailImport> list) {
        Date businessDate = list.get(0).getBusinessDate();
        list.forEach(a -> {
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("合同编号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getOrgId())) {
                throw new ServiceException("签约主体不能为空");
            }
            if (ObjectUtil.isEmpty(a.getBusinessDate())) {
                throw new ServiceException("业务日期不能为空");
            }
            if (ObjectUtil.isEmpty(a.getCarryoverAmount())) {
                throw new ServiceException("结转金额不能为空");
            }
            if (businessDate.compareTo(a.getBusinessDate()) != 0) {
                throw new ServiceException("上传的文件业务时间存在不一致");
            }
        });
    }

    @Override
    public Void submit(List<Long> ids) {
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<PayableInsuranceEntity> entityList = this.listByIds(ids);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
            if (ObjectUtil.notEqual(v.getIsGenerateVoucher(), YesOrNoEnum.YES.getCode())) {
                throw new ServiceException("只有生成凭证后才可以提交");
            }
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.YFBXF.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);
        });
        CompletableFuture.runAsync(() -> {
            // 生成凭证
            Boolean generateVoucherFlag = voucher(ids, YesOrNoEnum.YES.getCode());
            if (generateVoucherFlag) {
                // 发送审核
                Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);

                List<PayableInsuranceEntity> newEntityList = this.listByIds(ids);
                newEntityList.stream().forEach(v -> {
                    v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                    v.setSubmitBy(SecurityUtils.getUserId() + "");
                    if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                        v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
                    }
                });
                // 凭证生成成功
                this.updateBatchById(newEntityList);
            } else {
                // 凭证生成失败
                throw new ServiceException("生成凭证失败，提交失败");
            }
        });
        return null;
    }

    @Override
    public Void withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<PayableInsuranceEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            // v.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
        });
        iApproveService.withdraw(entityList.stream().map(PayableInsuranceEntity::getProcessInstanceId).collect(Collectors.toList()));
        this.updateBatchById(entityList);
        return null;
    }

    @Override
    public Void pass(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        changeStatus(ids, MarginStatusEnum.PASS);
        return null;
    }

    @Override
    public Void fail(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        changeStatus(ids, MarginStatusEnum.FAILED);
        return null;
    }

    private void changeStatus(List<Long> ids, MarginStatusEnum marginStatusEnum) {
        LambdaUpdateWrapper<PayableInsuranceEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .in(PayableInsuranceEntity::getId, ids)
                .eq(PayableInsuranceEntity::getProcessStatus, MarginStatusEnum.SUBMITTED.getCode())
                .set(PayableInsuranceEntity::getUpdateTime, LocalDateTime.now())
                .set(PayableInsuranceEntity::getProcessStatus, marginStatusEnum.getCode());
        this.update(updateChainWrapper);
    }

    /**
     * 审批修改单据状态
     *
     * @param approveDTO
     */
    @Override
    public void updateProcessStatus(CommonApproveDTO approveDTO) {
        if (StringUtils.isEmpty(approveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        PayableInsuranceEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("应付保险费数据不存在");
        }
        if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
            // // 驳回 删除凭证
        }
        // 修改凭证状态，通过和驳回都修改
//        updateVoucherStatus(Lists.newArrayList(approveDTO.getDocumentId()), approveDTO);
        // 通过，直接修改状态
        entity.setProcessStatus(approveDTO.getDocumentStatus());
        entity.setUpdateTime(LocalDateTime.now());
        iVoucherService.updateStatusByBatch(Lists.newArrayList(approveDTO.getDocumentId()), BatchTypeEnum.YFBXF.getCode(), approveDTO.getDocumentStatus(), approveDTO.getApproverNum(), approveDTO.getApproverName());
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
        List<VoucherVO> voucherVOList = iVoucherService.getByBatchIdList(ids, BatchTypeEnum.YFBXF.getCode());
        List<String> voucherIdList = voucherVOList.stream().map(VoucherVO::getId).map(Objects::toString).collect(Collectors.toList());
        // 更新凭证状态
        iVoucherService.updateStatusByids(voucherIdList, approveDTO.getDocumentStatus(),
                approveDTO.getApproverNum(), approveDTO.getApproverName());
    }

}

