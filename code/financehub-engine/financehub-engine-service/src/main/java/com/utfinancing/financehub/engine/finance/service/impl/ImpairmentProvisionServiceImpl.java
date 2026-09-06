package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.Constants;
import com.utfinancing.financehub.engine.finance.constant.DefaultConstant;
import com.utfinancing.financehub.engine.finance.mapper.VoucherMapper;
import com.utfinancing.financehub.engine.hthx.model.vo.HthxBatchVoucherResultVO;
import com.utfinancing.financehub.engine.hthx.service.IHthxCommonService;
import com.utfinancing.financehub.engine.hthx.utils.PromptMessageUtil;
import com.utfinancing.financehub.engine.hthx.utils.StringUtils ;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.constant.GenConstants;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.mapper.ImpairmentProvisionMapper;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.hthx.common.enums.ResultEnum;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.hthx.utils.HthxDateUtils;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IDataExecutionTaskService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import com.utfinancing.financehub.engine.utils.PeriodCodeUtil;
import com.utfinancing.financehub.etl.api.RemoteKingdeeEasService;
import com.utfinancing.financehub.engine.finance.model.dto.HthxPushVoucherParamsDTO;
import com.utfinancing.financehub.engine.finance.model.dto.HthxPushVoucherResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description :  ImpairmentProvision服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ImpairmentProvisionServiceImpl extends ServiceImpl<ImpairmentProvisionMapper, ImpairmentProvisionEntity> implements IImpairmentProvisionService {

    private final ImpairmentProvisionMapper impairmentProvisionMapper;
    private final IImpairmentProvisionDetailService iImpairmentProvisionDetailService;
    private final IRuleService iRuleService;
    private final IVoucherService iVoucherService;
    private final IContractService iContractService;
    private final IOrgCompanyService iOrgCompanyService;
    private final IRepaymentPlanService iRepaymentPlanService;
    private final IEasExchangeRateService iEasExchangeRateService;
    private final IKingdeeCostcenterService iKingdeeCostcenterService;
    private final IKingdeeBankService iKingdeeBankService;
    private final IImpairmentProvisionUploadTaskService iImpairmentProvisionUploadTaskService;
    private final IDataExecutionTaskService iDataExecutionTaskService;

    private final IContractMonthService contractMonthService;

    // 金蝶
    private final RemoteKingdeeEasService remoteKingdeeEasService;

    @Value("${approve.url.impairmentProvision-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;


    @Autowired
    @Qualifier("hthxTaskAsyncExecutor")
    private Executor hthxTaskAsyncExecutor;

//    @Resource
//    private HthxImpairmentProvisionFacade hthxImpairmentProvisionFacade;

    private final VoucherMapper voucherMapper;

    private final IAccountService iAccountService;
    private final IVoucherEntryService voucherEntryService;

    @Resource
    private IHthxCommonService hthxCommonService;

    private final IClientService iClientService;

    @Override
    public Long saveImpairmentProvision(ImpairmentProvisionDTO dto) {
        ImpairmentProvisionEntity entity = BeanUtil.copyProperties(dto, ImpairmentProvisionEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateImpairmentProvision(Long id, ImpairmentProvisionDTO dto) {
        ImpairmentProvisionEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ImpairmentProvisionDTO getImpairmentProvisionDTOById(Long id) {
        ImpairmentProvisionEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ImpairmentProvisionDTO.class);
    }

    @Override
    public IPage<ImpairmentProvisionVO> selectPage(ImpairmentProvisionQueryDTO queryDTO) {
        LambdaQueryWrapper<ImpairmentProvisionEntity> queryWrapper = Wrappers.<ImpairmentProvisionEntity>lambdaQuery();
        // 这里注入查询条件
        if (ObjectUtil.isNotEmpty(queryDTO.getAccountDate())) {
            queryWrapper.apply("to_char(account_date,'YYYY-MM-DD')={0}", DateUtil.format(queryDTO.getAccountDate(), "yyyy-MM-dd"));
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getImpairmentType())) {
            queryWrapper.eq(ImpairmentProvisionEntity::getImpairmentType, queryDTO.getImpairmentType());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList())) {
            queryWrapper.in(ImpairmentProvisionEntity::getProcessStatus, queryDTO.getProcessStatusList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getIdList())) {
            queryWrapper.in(ImpairmentProvisionEntity::getId, queryDTO.getIdList());
        }
        queryWrapper.orderByDesc(ImpairmentProvisionEntity::getId);
        IPage<ImpairmentProvisionEntity> entityIPage = impairmentProvisionMapper.selectPage(new Page<ImpairmentProvisionEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        IPage<ImpairmentProvisionVO> provisionVOIPage = ListBeanUtil.copyPage(entityIPage, ImpairmentProvisionVO.class);
        List<ImpairmentProvisionVO> list = provisionVOIPage.getRecords();
        setData(list);
        return provisionVOIPage;
    }

    private void setData(List<ImpairmentProvisionVO> list) {
        list.stream().forEach(a -> {
            // 设置批次类型
            a.setBatchType(BatchTypeEnum.JZJT.getCode());
            // 会计期间
            Integer periodCode = null;
            if(ObjectUtil.isNotEmpty(a.getAccountDate())){
                periodCode = NumberUtil.parseInt(LocalDateTimeUtil.format(a.getAccountDate(), "yyyyMM"));
            }else if(YesOrNoEnum.YES.getCode().equals(a.getIsGenerateVoucher())){
                LambdaQueryWrapper<ImpairmentProvisionDetailEntity> detailQueryWrapper = Wrappers.lambdaQuery();
                detailQueryWrapper.eq(ImpairmentProvisionDetailEntity::getImpairmentProvisionId,a.getId());
                List<ImpairmentProvisionDetailEntity> detailEntityList = iImpairmentProvisionDetailService.list(detailQueryWrapper);
                log.info("====>>ImpairmentProvisionServiceImpl==>>setData==00==>>id:{},detailEntityList:{}",a.getId(),detailEntityList);
                boolean setPeriodCodeFlag = true;
                if(CollectionUtils.isNotEmpty(detailEntityList)){
                    Set<LocalDateTime> accountDateSet =  detailEntityList.stream()
                            .filter(entity -> entity.getAccountDate() != null)
                            .map(ImpairmentProvisionDetailEntity::getAccountDate)
                            .distinct()
                            .collect(Collectors.toSet());
                    log.info("====>>ImpairmentProvisionServiceImpl==>>setData==01==>>accountDateSet:{}",accountDateSet);
                    if(CollectionUtils.isNotEmpty(accountDateSet)){
                        if(accountDateSet.size()==1){
                            periodCode = NumberUtil.parseInt(LocalDateTimeUtil.format(accountDateSet.iterator().next(), "yyyyMM"));
                        }else{
                            setPeriodCodeFlag = false;
                        }
                    }
                }
                log.info("====>>ImpairmentProvisionServiceImpl==>>setData==02==>>periodCode:{},setPeriodCodeFlag:{}",periodCode,setPeriodCodeFlag);
                if(periodCode==null && setPeriodCodeFlag){
                    periodCode = NumberUtil.parseInt(LocalDateTimeUtil.format(LocalDateTime.now(), "yyyyMM"));
                }
                log.info("====>>ImpairmentProvisionServiceImpl==>>setData==100==>>id:{},periodCode:{}",a.getId(),periodCode);
            }else{
                periodCode = NumberUtil.parseInt(LocalDateTimeUtil.format(LocalDateTime.now(), "yyyyMM"));
            }
            a.setPeriodCode(periodCode);
        });
    }

    @Override
    public List<ImpairmentProvisionVO> selectList(ImpairmentProvisionQueryDTO queryDTO) {
        LambdaQueryWrapper<ImpairmentProvisionEntity> queryWrapper = Wrappers.<ImpairmentProvisionEntity>lambdaQuery();
        // 这里注入查询条件
        if (ObjectUtil.isNotEmpty(queryDTO.getAccountDate())) {
            queryWrapper.apply("to_char(account_date,'YYYY-MM-DD')={0}", DateUtil.format(queryDTO.getAccountDate(), "yyyy-MM-dd"));
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getImpairmentType())) {
            queryWrapper.eq(ImpairmentProvisionEntity::getImpairmentType, queryDTO.getImpairmentType());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList())) {
            queryWrapper.in(ImpairmentProvisionEntity::getProcessStatus, queryDTO.getProcessStatusList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getIdList())) {
            queryWrapper.in(ImpairmentProvisionEntity::getId, queryDTO.getIdList());
        }
        List<ImpairmentProvisionEntity> impairmentProvisionEntityList = impairmentProvisionMapper.selectList(queryWrapper);
        List<ImpairmentProvisionVO> list = ListBeanUtil.copyList(impairmentProvisionEntityList, ImpairmentProvisionVO.class);
        setData(list);
        return list;
    }

    /**
     * 异步生成凭证
     */
    @Override
    public String generateVoucherAsync(List<Long> ids, String code) {
        log.info("====>>ImpairmentProvisionServiceImpl==>>generateVoucherAsync==>>00==>>ids:{},code:{}",ids,code);
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException(ResultEnum.COMMON_NO_DATA_SELECTED.getMessage());
        }
        if (ids.size() > 1) {
            throw new ServiceException(ResultEnum.COMMON_ONLY_SELECTED_ONE.getMessage());
        }
        List<ImpairmentProvisionEntity> provisionEntityList = this.listByIds(ids);
        // 只有1-已录入或者5-已拒绝状态数据才能提交
        provisionEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException(ResultEnum.COMMON_VOUCHER_STATUS_ERROR.getMessage());
            }
        });
        Long headId = ids.get(FinanceEngineEnum.Numbers.ZERO.getKey());
        ImpairmentProvisionUploadTaskEntity vo = iImpairmentProvisionUploadTaskService.getOne(new LambdaUpdateWrapper<ImpairmentProvisionUploadTaskEntity>()
                .eq(ImpairmentProvisionUploadTaskEntity::getTaskType, ImpairmentTaskTypeEnum.TASK_TYPE_2.getCode())
                .eq(ObjectUtil.isNotEmpty(headId), ImpairmentProvisionUploadTaskEntity::getDocId, headId)
                .eq(ImpairmentProvisionUploadTaskEntity::getStatus, ImpairmentTaskTypeEnum.STATUS_1.getCode()));
        // 校验某个类型文件的某个操作，是否重复操作
        if (ObjectUtil.isNotEmpty(vo)) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("用户：" + vo.getUserName());
            stringBuffer.append(" 开始于：" + HthxDateUtils.localDateTimeFormat(vo.getStartTime(), HthxDateUtils.YYYY_MM_DD_HH_MM_SS));
            stringBuffer.append(" 的[" + vo.getTaskType() + "]任务未执行完成，请稍后再试");
            return stringBuffer.toString();
        }
        ImpairmentProvisionEntity impairmentProvisionEntity = this.getById(headId);
        // 查询减值计提明细
        ImpairmentProvisionDetailQueryDTO queryDTO = new ImpairmentProvisionDetailQueryDTO();
        queryDTO.setImpairmentProvisionId(impairmentProvisionEntity.getId());
        List<ImpairmentProvisionDetailVO> impairmentProvisionDetailVOList = iImpairmentProvisionDetailService.selectDetailList(queryDTO);
        if (CollectionUtils.isEmpty(impairmentProvisionDetailVOList)) {
            saveTask(FinanceEngineEnum.Numbers.ZERO.getKey(), ImpairmentTaskTypeEnum.TASK_TYPE_2.getCode(),
                    ImpairmentTaskTypeEnum.STATUS_2.getCode(),impairmentProvisionEntity.getId());
        }else{
            Long taskId = saveTask(null, null, impairmentProvisionDetailVOList.size(), ImpairmentTaskTypeEnum.TASK_TYPE_2.getCode(), impairmentProvisionEntity.getId());
            log.info("====>>ImpairmentProvisionServiceImpl==>>generateVoucherAsync==>>02==>>headId:{},taskId:{}",headId,taskId);
            // 异步生成凭证
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> asyncBatchGenerateVoucher(ids, headId, taskId),
                    hthxTaskAsyncExecutor);
        }
        return PromptMessageUtil.promptMessageFormat(ResultEnum.IP_VOUCHER_PROMPT,(impairmentProvisionDetailVOList.size() / FinanceEngineEnum.Numbers.THOUSAND.getKey() + 1));
    }

    /**
     * @description: 异步+分批次生成凭证
     * @author: zhangli.chen
     **/
    public void asyncBatchGenerateVoucher(List<Long> headIds,Long headId,Long taskId) {
        ImpairmentProvisionEntity impairmentProvisionEntity = this.getById(headId);
        // 查询减值计提明细
        ImpairmentProvisionDetailQueryDTO queryDTO = new ImpairmentProvisionDetailQueryDTO();
        queryDTO.setImpairmentProvisionId(impairmentProvisionEntity.getId());
        List<ImpairmentProvisionDetailVO> impairmentProvisionDetailVOList = iImpairmentProvisionDetailService.selectDetailList(queryDTO);
        int totalSize = impairmentProvisionDetailVOList.size();
        log.info("====>>ImpairmentProvisionServiceImpl.asyncBatchGenerateVoucher==>>00==>>headIds:{},headId:{},taskId:{},impairmentProvisionDetailVOList.size():{}"
                ,headIds,headId,taskId,totalSize);
        // 生成凭证前先删除之前的凭证
        batchDeleteVoucher(headIds);
        log.info("====>>ImpairmentProvisionServiceImpl.asyncBatchGenerateVoucher==>>01==>>headIds:{}",headIds);
        // 1. 分批次处理
        List<List<ImpairmentProvisionDetailVO>> batches = ListUtil.partition(impairmentProvisionDetailVOList, FinanceEngineEnum.Numbers.THOUSAND.getKey());
        // 2. 并行处理每个批次并聚合结果
        CompletableFuture<HthxBatchVoucherResultVO> resultFuture = CompletableFuture.supplyAsync(() -> {
            List<CompletableFuture<HthxBatchVoucherResultVO>> futures = batches.stream()
                    .map(batch ->
                            CompletableFuture.supplyAsync(
                                    () -> batchGenerateVoucher(batch, YesOrNoEnum.NO.getCode(), headId),
                                    hthxTaskAsyncExecutor
                            ).exceptionally(e -> {
                                log.error("减值计提-并行处理凭证生成异常:{}", e.getMessage());
                                return createErrorResult(batch.size(), e.getMessage());
                            })
                    ).collect(Collectors.toList());
            // 合并所有批次结果
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            return allFutures.thenApply(v -> {
                // 初始化汇总结果对象
                HthxBatchVoucherResultVO totalResult = new HthxBatchVoucherResultVO();
                //线程安全集合
                totalResult.setVoucherIdList(new CopyOnWriteArrayList<>());
                StringBuffer errorInfoBuffer = totalResult.getErrorInfo();
                if (errorInfoBuffer == null) {
                    errorInfoBuffer = new StringBuffer();
                    totalResult.setErrorInfo(errorInfoBuffer);
                }
                futures.forEach(future -> {
                    try {
                        HthxBatchVoucherResultVO batchResult = future.get();
                        // 防御性检查：处理batchResult或字段为null的情况
                        if (batchResult == null) {
                            totalResult.setFailSize(totalResult.getFailSize() + 1);
                            totalResult.getErrorInfo().append("减值计提-并行处理凭证生成异常-单个批次生成凭证返回结果为空");
                            return;
                        }
                        log.info("====>>ImpairmentProvisionServiceImpl.asyncBatchGenerateVoucher==>>03==>>batchResult:{}",batchResult);
                        totalResult.setTotalSize(totalResult.getTotalSize() + batchResult.getTotalSize());
                        totalResult.setSuccessSize(totalResult.getSuccessSize() + batchResult.getSuccessSize());
                        totalResult.setFailSize(totalResult.getFailSize() + batchResult.getFailSize() + batchResult.getExceptionRecords());
                        // 例外记录数-归属于凭证失败记录数
                        totalResult.setExceptionRecords(totalResult.getExceptionRecords() + batchResult.getExceptionRecords());
                        // 处理errorInfo：若为null则初始化为空StringBuffer
                        StringBuffer batchError = batchResult.getErrorInfo() != null ? batchResult.getErrorInfo() : new StringBuffer();
                        if (StringUtils.isNotEmpty(batchError)) {
                            totalResult.getErrorInfo().append(batchError);
                        }
                        Optional.ofNullable(batchResult.getVoucherIdList()).ifPresent(list -> totalResult.getVoucherIdList().addAll(list));
                        if (totalResult.getAccountDate() == null) {
                            totalResult.setAccountDate(batchResult.getAccountDate());
                        }
                        if (YesOrNoEnum.NO.getCode().equals(batchResult.getGenerateResult())) {
                            totalResult.setGenerateResult(YesOrNoEnum.NO.getCode());
                        }
                    } catch (Exception e) {
                        log.error("减值计提-并行处理凭证生成异常-聚合结果异常:{}", e.getMessage());
                        totalResult.setFailSize(totalResult.getFailSize() + 1);
                        totalResult.getErrorInfo().append("减值计提-并行处理凭证生成异常-聚合结果异常: ").append(e.getMessage());
                    }
                });
                return totalResult;
            });
        }, hthxTaskAsyncExecutor).thenCompose(Function.identity()).exceptionally(ex -> {
            // 捕获并返回兜底结果
            HthxBatchVoucherResultVO errorResult = new HthxBatchVoucherResultVO();
            errorResult.setErrorInfo(new StringBuffer("减值计提-全局异常: ").append(ex.getMessage()));
            return errorResult;
        });
        // 3. 在结果完成后更新头表
        resultFuture.whenComplete((totalResult, e) -> {
            log.info("====>>ImpairmentProvisionServiceImpl.asyncBatchGenerateVoucher==>>04==>>totalResult:{}",totalResult);
            if (e != null) {
                updateTaskWithTotalNumber(taskId, ImpairmentTaskTypeEnum.STATUS_3.getCode(),
                        totalResult.getSuccessSize(), totalResult.getFailSize(),
                        StringUtils.truncateStringBuffer(totalResult.getErrorInfo()),totalResult.getTotalSize(),
                        totalResult.getExceptionRecords());
                log.info("====>>ImpairmentProvisionServiceImpl.asyncBatchGenerateVoucher==>>05==>>totalResult:{},e.getMessage():{}",totalResult,e.getMessage());
            }else{
                try {
                    // add by zhangli.chen for 新增凭证生成完后检查分录是否生成成功逻辑 on 20251110
                    if(StringUtils.isNotEmpty(totalResult.getVoucherIdList())){
                        List<Long> longVoucherIdList = totalResult.getVoucherIdList().stream()
                                .map(str -> {
                                    try {
                                        return Long.parseLong(str);
                                    } catch (NumberFormatException ex) {
                                        return null;
                                    }
                                }).filter(Objects::nonNull).collect(Collectors.toList());
                        Map<Long, List<String>> returnMessage = verifyVoucherBeforeSubmit(longVoucherIdList);
                        if(StringUtils.isNotEmpty(returnMessage) && returnMessage.size()>0){
                            totalResult.setGenerateResult(YesOrNoEnum.NO.getCode());
                            totalResult.setFailSize(totalResult.getFailSize() + returnMessage.size());
                            if(totalSize>=totalResult.getFailSize()){
                                totalResult.setSuccessSize(totalSize-totalResult.getFailSize());
                            }
                            totalResult.setErrorInfo(new StringBuffer(generateVerifyInfoBeforeSubmit(returnMessage)));
                        }
                    }
                    // 更新头表
                    String headVoucherIdStr = String.join(",", totalResult.getVoucherIdList());
                    // 凭证状态-生成状态返回为空，视为生成成功
                    if(StringUtils.isEmpty(totalResult.getGenerateResult())){
                        totalResult.setGenerateResult(YesOrNoEnum.YES.getCode());
                    }
                    log.info("====>>ImpairmentProvisionServiceImpl.asyncBatchGenerateVoucher==>>06==>>totalResult:{},headId:{}",totalResult,headId);
                    lambdaUpdate()
                            .set(ImpairmentProvisionEntity::getIsGenerateVoucher, totalResult.getGenerateResult())
                            .set(ImpairmentProvisionEntity::getErrorInfo, StringUtils.truncateStringBuffer(totalResult.getErrorInfo()))
                            .set(ImpairmentProvisionEntity::getVoucherId, headVoucherIdStr)
                            .set(ImpairmentProvisionEntity::getAccountDate, totalResult.getAccountDate())
                            .set(ImpairmentProvisionEntity::getUpdateTime, LocalDateTime.now())
                            .eq(ImpairmentProvisionEntity::getId, headId)
                            .update();
                    // 更新任务表
                    String taskStatus = YesOrNoEnum.NO.getCode().equals(totalResult.getGenerateResult())
                            ? ImpairmentTaskTypeEnum.STATUS_3.getCode()
                            : ImpairmentTaskTypeEnum.STATUS_2.getCode();
                    updateTaskWithTotalNumber(
                            taskId,
                            taskStatus,
                            totalResult.getSuccessSize(),
                            totalResult.getFailSize(),
                            StringUtils.truncateStringBuffer(totalResult.getErrorInfo()),
                            totalResult.getTotalSize(),
                            totalResult.getExceptionRecords());
                } catch (Exception ex) {
                    log.error("减值计提-并行处理凭证-更新头表或任务表异常: {}", ex.getMessage());
                }
            }
        });
        // 确保触发异步链并等待完成（测试用）
        //resultFuture.join();
        log.info("====>>ImpairmentProvisionServiceImpl.asyncBatchGenerateVoucher==>>100==>>");
    }

    /**
     * @description: 创建异常批次结果
     * @author: zhangli.chen
     **/
    private HthxBatchVoucherResultVO createErrorResult(int batchSize, String errorMsg) {
        HthxBatchVoucherResultVO result = new HthxBatchVoucherResultVO();
        result.setTotalSize(batchSize);
        result.setFailSize(batchSize);
        result.setErrorInfo(new StringBuffer(errorMsg));
        result.setGenerateResult(YesOrNoEnum.NO.getCode());
        return result;
    }

    /**
     * @description: 单批次生成凭证
     * 1、境外主体不生成凭证-不代表凭证生成报错，可是会将检验信息反显到错误信息上
     * 2、其他校验不通过不生成凭证-不代表凭证生成报错，可是会将检验信息反显到错误信息上
     * @author: zhangli.chen
     **/
    private HthxBatchVoucherResultVO batchGenerateVoucher(List<ImpairmentProvisionDetailVO> detailEntityList, String isSubmit,Long batchId){
        log.info("====>>ImpairmentProvisionServiceImpl.batchGenerateVoucher==>>00==>>detailEntityList.size():{},isSubmit:{},batchId:{}"
                ,detailEntityList.size(),isSubmit,batchId);
        HthxBatchVoucherResultVO hthxBatchVoucherResultVO = new HthxBatchVoucherResultVO();
        hthxBatchVoucherResultVO.setGenerateResult(YesOrNoEnum.YES.getCode());
        hthxBatchVoucherResultVO.setTotalSize(detailEntityList.size());
        hthxBatchVoucherResultVO.setErrorInfo(new StringBuffer());
        hthxBatchVoucherResultVO.setVoucherIdList(new CopyOnWriteArrayList<>());
        hthxBatchVoucherResultVO.setTotalSize(0);
        hthxBatchVoucherResultVO.setSuccessSize(0);
        hthxBatchVoucherResultVO.setFailSize(0);
        hthxBatchVoucherResultVO.setExceptionRecords(0);
        hthxBatchVoucherResultVO.setGenerateResult(YesOrNoEnum.YES.getCode());
        try {
            if(CollectionUtils.isEmpty(detailEntityList)){
                return hthxBatchVoucherResultVO;
            }
            // 获取金蝶所有当前会计期间
//            R<List<Map<String, String>>> periodCodeAll = remoteKingdeeEasService.getCurrentPeriodCodeAll();
//            Map<String, Integer> periodCodeMap = new HashMap<>();
//            for (Map<String, String> map : periodCodeAll.getData()) {
//                periodCodeMap.put(String.valueOf(map.get("ORGID")), Integer.valueOf(map.get("PERIODCODE")));
//            }
            Map<String, Integer> periodCodeMap = getPeriodFromKingdee();
            // 签约主体
            Map<String, String> companyMap = iOrgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgId(), e -> e.getOrgName(), (a, b) -> b));
            // 金蝶-成本中心
            List<KingdeeCostcenterDTO> kingdeeCostcenterDTOS = iKingdeeCostcenterService.selectAll();
            // 记录所有详情的凭证生成信息
            List<VoucherInfoVO> voucherResultList = Lists.newArrayList();
            Date accountDateHead = DateUtils.getNowDate();
            List<String> financialInstitutionList = detailEntityList.stream().filter(a -> ObjectUtil.isNotEmpty(a.getFinancialInstitution()))
                    .map(ImpairmentProvisionDetailVO::getFinancialInstitution).collect(Collectors.toList());
            List<KingdeeBankEntity> kingdeeBankEntityList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(financialInstitutionList)) {
                kingdeeBankEntityList = iKingdeeBankService.list(new LambdaQueryWrapper<KingdeeBankEntity>().in(KingdeeBankEntity::getName, financialInstitutionList));
            }
            // 金融机构
            List<KingdeeBankEntity> finalKingdeeBankEntityList = kingdeeBankEntityList;
            List<Map<String, Object>> voucherMapList = Lists.newArrayList();
            // 头表汇总统计所有的详情凭证报错信息
            StringBuffer headVoucherErrorInfo = new StringBuffer();
            for (ImpairmentProvisionDetailVO v : detailEntityList) {
                // 获取金蝶当前记账的最后一天
                Date accountDate = getAccountDate(v, periodCodeMap);
                accountDateHead = accountDate;
                // 境外主体 不生成凭证
                if (ObjectUtil.equals(v.getOrgId(), OrgCompanyEnum.ORG_00000.getCode()) ||
                        ObjectUtil.equals(v.getOrgId(), OrgCompanyEnum.ORG_50001.getCode()) ||
                        ObjectUtil.equals(v.getOrgId(), OrgCompanyEnum.ORG_60001.getCode())) {
                    iImpairmentProvisionDetailService.lambdaUpdate().set(ImpairmentProvisionDetailEntity::getAccountDate, DateUtil.toLocalDateTime(accountDateHead))
                            .set(ImpairmentProvisionDetailEntity::getUpdateTime,LocalDateTime.now())
                            .set(ImpairmentProvisionDetailEntity::getImportFlag,FinanceEngineEnum.Numbers.THREE.getValue())
                            .eq(ImpairmentProvisionDetailEntity::getId, v.getId()).update();
                    hthxBatchVoucherResultVO.setExceptionRecords(hthxBatchVoucherResultVO.getExceptionRecords()+1);
                    headVoucherErrorInfo.append(PromptMessageUtil.promptMessageFormat(ResultEnum.IP_ABROAD_NEED_TO_GENERATE_VOUCHERS,
                            v.getContractCode(), v.getOrgName()));
                    continue;
                }
                ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
                executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
                executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
                executeCommonDTO.setSceneCode(SceneEnum.JZJT.getCode());
                executeCommonDTO.setSceneName(SceneEnum.JZJT.name());
                executeCommonDTO.setOrderId(v.getId().toString());
                executeCommonDTO.setOrgId(v.getOrgId());
                executeCommonDTO.setContractCode(v.getContractCode());
                executeCommonDTO.setContractStatus(v.getContractStatus());
                //modify by zhangli.chen for 减值计提数据无需回更合同表的客户信息 on 20251020
//                executeCommonDTO.setClientCode(v.getClientCode());
//                executeCommonDTO.setClientName(v.getClientName());
                executeCommonDTO.setAccountDate(accountDate);
                executeCommonDTO.setBusinessDate(accountDate);
                executeCommonDTO.setIsSubmit(isSubmit);
                executeCommonDTO.setBatchId(batchId);
                executeCommonDTO.setInterfaceId(v.getId());
                executeCommonDTO.setBatchType(BatchTypeEnum.JZJT.getCode());
                executeCommonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
                executeCommonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
                // 无合同编号的设置默认业务类型
                if (ObjectUtil.isEmpty(v.getContractCode())) {
                    // 业务类型设为默认
                    executeCommonDTO.setBusinessCode(BusinessEnum.DEFAULT.getCode());
                    executeCommonDTO.setBusinessName(BusinessEnum.DEFAULT.getDesc());
                }
                Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
                // 业务类型
                dataMap.put("businessType", v.getBusinessType());
                // 减值类型
                dataMap.put("impairmentType", v.getImpairmentType());
                dataMap.put(RuleConstant.FIELD_ID,batchId);
                // 原始场景编码
                dataMap.put(RuleConstant.FIELD_SCENE_CODE_ORIGINAL,SceneEnum.JZJT.getCode());
                // 设置本月计提
                BigDecimal thisMonthProvision = v.getThisMonthProvision();
                if (ObjectUtil.isNotEmpty(v.getExchangeRate())) {
                    continue;
                }
                // 计提金额
                dataMap.put("provisionAmount", thisMonthProvision);
                // 前置凭证数据校验
                boolean preVoucherDataCheck = true;
                StringBuffer preVoucherDataCheckInfo = new StringBuffer();
                //业务类型
                if( ObjectUtil.equals(ImpairmentTypeEnum.BUSINESS_TYPE_3.getCode(), v.getBusinessType())){
                    // 投资性房地产凭证需要在传金蝶时传固定的成本中心11001
                    dataMap.put("costCentre", "11001");
                }else if (ObjectUtil.equals(ImpairmentTypeEnum.BUSINESS_TYPE_1.getCode(), v.getBusinessType())
                        || ObjectUtil.equals(ImpairmentTypeEnum.BUSINESS_TYPE_2.getCode(), v.getBusinessType())
                        || ObjectUtil.equals(ImpairmentTypeEnum.BUSINESS_TYPE_4.getCode(), v.getBusinessType())
                        || ObjectUtil.equals(ImpairmentTypeEnum.BUSINESS_TYPE_5.getCode(), v.getBusinessType())
                        || ObjectUtil.equals(ImpairmentTypeEnum.BUSINESS_TYPE_6.getCode(), v.getBusinessType())) {
                    // 签约主体
                    String orgName = companyMap.get(v.getOrgId());
                    if (ObjectUtil.isEmpty(orgName)) {
                        preVoucherDataCheck = false;
                        preVoucherDataCheckInfo.append(PromptMessageUtil.promptMessageFormat(ResultEnum.IP_SIGNING_PARTY_EMPTY,v.getContractCode(), v.getOrgId()));
                    }
                    // 成本中心
                    if (ObjectUtil.equals(orgName, "海通恒信国际融资租赁（天津）有限公司")) {
                        // 默认成本中心
                        dataMap.put("costCentre", "300001");
                    } else {
                        KingdeeCostcenterDTO kingdeeCostcenterDTO = kingdeeCostcenterDTOS.stream().filter(a -> ObjectUtil.equals(a.getName(), orgName)).findFirst().orElse(null);
                        if (ObjectUtil.isEmpty(kingdeeCostcenterDTO)) {
                            preVoucherDataCheck = false;
                            preVoucherDataCheckInfo.append(PromptMessageUtil.promptMessageFormat(ResultEnum.IP_COST_CENTER_EMPTY,v.getContractCode(),orgName));
                        }else{
                            // 成本中心
                            dataMap.put("costCentre", kingdeeCostcenterDTO.getCode());
                        }
                    }
                }
                // 业务类型-资产支持专项计划
                if (ObjectUtil.equals(ImpairmentTypeEnum.BUSINESS_TYPE_4.getCode(), v.getBusinessType())) {
                    KingdeeBankEntity kingdeeBank = finalKingdeeBankEntityList.stream().filter(a -> ObjectUtil.equals(a.getName(), v.getFinancialInstitution())).findFirst().orElse(null);
                    if (ObjectUtil.isEmpty(kingdeeBank)) {
                        preVoucherDataCheck = false;
                        preVoucherDataCheckInfo.append(PromptMessageUtil.promptMessageFormat(ResultEnum.IP_FINANCIAL_EMPTY,v.getContractCode(),v.getFinancialInstitution()));
                    }else{
                        // 金融机构
                        dataMap.put("financialInstitution", kingdeeBank.getCode());
                    }
                }
                // 针对校验未通过的，同步更新详情明细表
                if(!preVoucherDataCheck){
                    iImpairmentProvisionDetailService.lambdaUpdate()
                            .set(ImpairmentProvisionDetailEntity::getErrorInfo, StringUtils.truncateStringBuffer(preVoucherDataCheckInfo))
                            .set(ImpairmentProvisionDetailEntity::getImportFlag,FinanceEngineEnum.Numbers.FOUR.getValue())
                            .set(ImpairmentProvisionDetailEntity::getUpdateTime,LocalDateTime.now())
                            .eq(ImpairmentProvisionDetailEntity::getId, v.getId()).update();
                    hthxBatchVoucherResultVO.setExceptionRecords(hthxBatchVoucherResultVO.getExceptionRecords()+1);
                    continue;
                }
                voucherMapList.add(dataMap);
            }
            // 以上一次批次处理结束
            try {
                List<VoucherInfoVO> voucherResultLists = iRuleService.batchExecuteRule(voucherMapList);
                if (CollectionUtils.isNotEmpty(voucherResultLists)) {
                    voucherResultList.addAll(voucherResultLists);
                }
            } catch (Exception e) {
                hthxBatchVoucherResultVO.setGenerateResult(YesOrNoEnum.NO.getCode());
                log.info("减值计提-生成凭证失败，失败原因：{}", e.getMessage());
            }
            // 以上凭证处理结束
            // 记录头表生成的凭证汇总
            List<String> headVoucherIdsList = Lists.newArrayList();
            String isGenerateVoucherHead = YesOrNoEnum.YES.getCode();
            int voucherSuccessSize = 0;
            int voucherFailSize = 0;
            if(CollectionUtils.isNotEmpty(voucherResultList)){
                for (VoucherInfoVO infoVO : voucherResultList) {
                    String detailVoucherIds = "";
                    String detailVoucherErrorInfo = "";
                    String isGenerateVoucherDetail = FinanceEngineEnum.Numbers.FIVE.getValue();
                    if (StringUtils.isNotEmpty(infoVO.getErrorInfo())) {
                        detailVoucherErrorInfo = StringUtils.truncateString(infoVO.getErrorInfo());
                        isGenerateVoucherHead = YesOrNoEnum.NO.getCode();
                        voucherFailSize++;
                        isGenerateVoucherDetail = FinanceEngineEnum.Numbers.SIX.getValue();
                        headVoucherErrorInfo.append(detailVoucherErrorInfo + ";");
                    } else {
                        voucherSuccessSize++;
                    }
                    LocalDateTime voucherDate = DateUtil.toLocalDateTime(accountDateHead);
                    if (CollectionUtils.isNotEmpty(infoVO.getVoucherDTOList())) {
                        detailVoucherIds = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).map(String::valueOf).collect(Collectors.toList()).stream().collect(Collectors.joining(","));
                        headVoucherIdsList.add(detailVoucherIds);
                        voucherDate = infoVO.getVoucherDTOList().get(0).getVoucherDate();
                    }
                    // 更新详情数据的凭证信息
                    if(FinanceEngineEnum.Numbers.FIVE.getValue().equals(isGenerateVoucherDetail)){
                        if(StringUtils.isNotEmpty(detailVoucherIds)){
                            iImpairmentProvisionDetailService.lambdaUpdate().set(ImpairmentProvisionDetailEntity::getVoucherId, detailVoucherIds)
                                    .set(ImpairmentProvisionDetailEntity::getErrorInfo, detailVoucherErrorInfo)
                                    .set(ImpairmentProvisionDetailEntity::getAccountDate, voucherDate)
                                    .set(ImpairmentProvisionDetailEntity::getImportFlag, isGenerateVoucherDetail)
                                    .set(ImpairmentProvisionDetailEntity::getUpdateTime,LocalDateTime.now())
                                    .eq(ImpairmentProvisionDetailEntity::getId, Long.parseLong(infoVO.getOrderId())).update();
                        }
                    }else{
                        iImpairmentProvisionDetailService.lambdaUpdate().set(ImpairmentProvisionDetailEntity::getVoucherId, detailVoucherIds)
                                .set(ImpairmentProvisionDetailEntity::getErrorInfo, detailVoucherErrorInfo)
                                .set(ImpairmentProvisionDetailEntity::getAccountDate, voucherDate)
                                .set(ImpairmentProvisionDetailEntity::getImportFlag, isGenerateVoucherDetail)
                                .set(ImpairmentProvisionDetailEntity::getUpdateTime,LocalDateTime.now())
                                .eq(ImpairmentProvisionDetailEntity::getId, Long.parseLong(infoVO.getOrderId())).update();
                    }
                }
            }else{
                isGenerateVoucherHead = YesOrNoEnum.NO.getCode();
                voucherFailSize = voucherMapList.size();
            }
            hthxBatchVoucherResultVO.setGenerateResult(isGenerateVoucherHead);
            hthxBatchVoucherResultVO.setErrorInfo(hthxBatchVoucherResultVO.getErrorInfo().append(headVoucherErrorInfo));
            hthxBatchVoucherResultVO.setVoucherIdList(headVoucherIdsList);
            hthxBatchVoucherResultVO.setSuccessSize(voucherSuccessSize);
            hthxBatchVoucherResultVO.setFailSize(voucherFailSize);
            if(accountDateHead!=null){
                hthxBatchVoucherResultVO.setAccountDate(DateUtil.toLocalDateTime(accountDateHead));
            }
        } catch (Exception e) {
            hthxBatchVoucherResultVO.setGenerateResult(YesOrNoEnum.NO.getCode());
            hthxBatchVoucherResultVO.setErrorInfo(hthxBatchVoucherResultVO.getErrorInfo()
                    .append("减值计提-生成凭证报错，报错原因："+e.getMessage()));
        }
        log.info("====>>ImpairmentProvisionServiceImpl.batchGenerateVoucher==>>100==>>detailEntityList.size():{},isSubmit:{},batchId:{},hthxBatchVoucherResultVO:{}"
                ,detailEntityList.size(),isSubmit,batchId,hthxBatchVoucherResultVO);
        return hthxBatchVoucherResultVO;
    }


    /**
     * 获取金蝶当前记账的最后一天
     *
     * @param v
     * @param periodCodeAllData
     * @return
     */
    private Date getAccountDate(ImpairmentProvisionDetailVO v, Map<String, Integer> periodCodeAllData) {
        String orgId = v.getOrgId();
        // 取会计期间时，海通恒信金融集团（香港）换为 海通恒信国际融资租赁股份有限公司
        if(ObjectUtil.equals(orgId,"00000")){
            orgId="01-C0001";
        }
        Integer currentPeriodCode = periodCodeAllData.get(orgId);
        if (ObjectUtil.isEmpty(currentPeriodCode)) {
            throw new ServiceException("查询签约主体[" + orgId + "]的金蝶当前会计期间失败");
        }
        LocalDate accountDate = PeriodCodeUtil.parseLastDayOfMonth(currentPeriodCode);
        Date date = DateUtils.toDate(accountDate);
        return date;
    }

    /**
     * 异步删除凭证
     *
     * @param voucherIdList
     */
    public void asnyDeleteVoucher(List<Long> voucherIdList) {
        if (CollectionUtils.isEmpty(voucherIdList)) {
            return;
        }
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // 异步任务的代码
            iVoucherService.deleteByIdList(voucherIdList);
        });
    }

    /**
     * @description:减值计提-首页列表-提交
     **/
    @Override
    public String submit(List<Long> idList) {
        log.info("====>>ImpairmentProvisionServiceImpl==>>submit==>>00==>>idList:{}",idList);
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException(ResultEnum.COMMON_NO_DATA_SELECTED.getMessage());
        }
        if (idList.size() > 1) {
            throw new ServiceException(ResultEnum.COMMON_ONLY_SELECTED_ONE.getMessage());
        }
        List<ImpairmentProvisionEntity> provisionEntityList = this.listByIds(idList);
        provisionEntityList.stream().forEach(v -> {
            // 已录入+已拒绝 的记录才能进行提交
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException(ResultEnum.COMMON_SUBMIT_STATUS_ERROR.getMessage());
            }
            // 生成凭证后才允许进行提交
            if (ObjectUtil.equal(v.getIsGenerateVoucher(), YesOrNoEnum.NO.getCode()) || StringUtils.isEmpty(v.getVoucherId())) {
                throw new ServiceException(ResultEnum.IP_SUBMIT_BEFORE_VOUCHER_ERROR.getMessage());
            }
        });
        Long headId = idList.get(FinanceEngineEnum.Numbers.ZERO.getKey());
        ImpairmentProvisionEntity provisionEntity = this.getById(headId);
        List<Long> voucherIdList = Lists.newArrayList();
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        ApproveDTO approveDTO = new ApproveDTO();
        approveDTO.setDocumentId(provisionEntity.getId());
        approveDTO.setDocumentType(BatchTypeEnum.JZJT.getCode());
        approveDTO.setUrl(approveUrl + provisionEntity.getId());
        approveDTOList.add(approveDTO);
        if (StringUtils.isNotEmpty(provisionEntity.getVoucherId())) {
            voucherIdList.addAll(Arrays.stream(provisionEntity.getVoucherId().split(","))
                    .map(Long::parseLong).collect(Collectors.toList()));
        }
        if (CollectionUtils.isEmpty(voucherIdList)) {
            saveTask(FinanceEngineEnum.Numbers.ZERO.getKey(), ImpairmentTaskTypeEnum.TASK_TYPE_3.getCode(),
                    ImpairmentTaskTypeEnum.STATUS_2.getCode(),provisionEntity.getId());
        }else{
            Long taskId  = saveTask(null, null, voucherIdList.size(), ImpairmentTaskTypeEnum.TASK_TYPE_3.getCode(), provisionEntity.getId());
            // 异步提交
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> asyncBatchSubmit(headId,taskId,approveDTOList), hthxTaskAsyncExecutor);
        }
        return PromptMessageUtil.promptMessageFormat(ResultEnum.IP_LATER_QUERY_RESULTS,(voucherIdList.size() / FinanceEngineEnum.Numbers.THOUSAND.getKey() + 1));
    }

    /**
     * @description:减值计提-首页列表-提交-异步并行提交
     **/
    private void asyncBatchSubmit(Long headId,Long taskId,List<ApproveDTO> approveDTOList) {
        log.info("====>>ImpairmentProvisionServiceImpl==>>asyncBatchSubmit==>>00==>>headId:{},taskId:{}",headId,taskId);
        ImpairmentProvisionEntity provisionEntity = this.getById(headId);
        List<Long> voucherIdList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(provisionEntity.getVoucherId())) {
            voucherIdList.addAll(Arrays.stream(provisionEntity.getVoucherId().split(","))
                    .map(Long::parseLong).collect(Collectors.toList()));
        }
        // add by zhangli.chen for 前置凭证提交校验 on 20251110
        Map<Long, List<String>> returnMessage = verifyVoucherBeforeSubmit(voucherIdList);
        if(StringUtils.isNotEmpty(returnMessage) && returnMessage.size()>0){
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_3.getCode(),
                    FinanceEngineEnum.Numbers.ZERO.getKey(), FinanceEngineEnum.Numbers.ZERO.getKey(),
                    generateVerifyInfoBeforeSubmit(returnMessage));
            return ;
        }
        // 1. 分片并行提交凭证
        List<List<Long>> voucherBatches = ListUtil.partition(voucherIdList, FinanceEngineEnum.Numbers.FIVE_HUNDRED.getKey());
        // 2. 创建线程安全的错误信息收集器
        ConcurrentLinkedQueue<String> errorQueue = new ConcurrentLinkedQueue<>();
        AtomicBoolean hasError = new AtomicBoolean(false);
        List<CompletableFuture<Void>> futures = voucherBatches.stream()
                .map(batch ->
                        CompletableFuture.runAsync(() -> {
                            try {
                                log.info("====>>ImpairmentProvisionServiceImpl==>>asyncBatchSubmit==>>01==>>batch.size():{}",batch.size());
                                iVoucherService.commitVoucherList(batch);
                            } catch (Exception e) {
                                // 记录原始异常信息
                                String errorMsg = String.format("减值计提-分片并行流程提交失败: %s", e.getMessage());
                                errorQueue.add(errorMsg);
                                hasError.set(true);
                                log.error("减值计提-分片并行流程提交失败:{}", e.getMessage());
                                // 保持异常传播
                                throw new CompletionException(e);
                            }
                        }, hthxTaskAsyncExecutor)
                ).collect(Collectors.toList());
        // 3. 等待所有凭证提交完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        try {
            log.info("====>>ImpairmentProvisionServiceImpl==>>asyncBatchSubmit==>>02==>>");
            allOf.get(FinanceEngineEnum.Numbers.ONE.getKey(), TimeUnit.HOURS);
        } catch (TimeoutException e) {
            errorQueue.add("减值计提-流程提交失败-提交超时:"+ e.getMessage());
            hasError.set(true);
        } catch (ExecutionException e) {
            errorQueue.add("减值计提-流程提交失败-执行报错:"+ e.getMessage());
            hasError.set(true);
        } catch (InterruptedException e) {
            errorQueue.add("减值计提-流程提交失败-任务被中断:" + e.getMessage());
            hasError.set(true);
            // 恢复中断状态
            Thread.currentThread().interrupt();
        }
        log.info("====>>ImpairmentProvisionServiceImpl==>>asyncBatchSubmit==>>03==>>hasError.get():{}",hasError.get());
        // 4. 统一处理任务状态更新
        if (hasError.get()) {
            // 合并错误信息（最多保留5条）
            String errorDetail = StringUtils.truncateString(
                    errorQueue.stream().limit(FinanceEngineEnum.Numbers.FIVE.getKey()).collect(Collectors.joining("; ")));
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_3.getCode(), FinanceEngineEnum.Numbers.ZERO.getKey(), FinanceEngineEnum.Numbers.ZERO.getKey(), errorDetail);
        } else {
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_2.getCode(), voucherIdList.size(), FinanceEngineEnum.Numbers.ZERO.getKey(), null);
        }
        // 5. 仅当无错误时继续后续流程
        if (!hasError.get()) {
            log.info("====>>ImpairmentProvisionServiceImpl==>>asyncBatchSubmit==>>04==>>approveDTOList:{}",approveDTOList);
            Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);
            log.info("====>>ImpairmentProvisionServiceImpl==>>asyncBatchSubmit==>>05==>>provisionEntity.getId():{},processInstantIdMap:{}"
                    ,provisionEntity.getId(),processInstantIdMap);
            ImpairmentProvisionEntity updateProvisionEntity = this.getById(provisionEntity.getId());
            if (updateProvisionEntity != null) {
                updateProvisionEntity.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                Optional.ofNullable(processInstantIdMap).ifPresent(map -> updateProvisionEntity.setProcessInstanceId(map.get(updateProvisionEntity.getId())));
                this.updateById(updateProvisionEntity);
            }
        }
        log.info("====>>ImpairmentProvisionServiceImpl==>>asyncBatchSubmit==>>100==>>provisionEntity.getId():{}",provisionEntity.getId());
    }

    /**
     * 撤回
     *
     * @param idList
     * @return
     */
    @Override
    public Boolean withdraw(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<ImpairmentProvisionEntity> provisionEntityList = this.listByIds(idList);
        provisionEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(provisionEntityList.stream().map(ImpairmentProvisionEntity::getProcessInstanceId).collect(Collectors.toList()));
        return this.updateBatchById(provisionEntityList);
    }

    /**
     * 删除凭证
     *
     * @param idList
     */
    public void batchDeleteVoucher(List<Long> idList) {
        //根据批次号删除凭证
        iVoucherService.deleteByBatchIdList(idList,BatchTypeEnum.JZJT.getCode());
    }


    /**
     * 上传
     *
     * @param file
     * @return
     */
    @Override
    public String importFile(MultipartFile file, String excelType) {
        String msg = "文件上传成功";
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            log.info("减值类型上传 文件名：{},excel模板类型：{}", fileName, excelType);
            if (ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_1.getCode())
                    || ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_2.getCode())) {
                // 导入模板1,2--租赁资产-1
                msg = import1(file, excelType);
            }
            else if (ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_5.getCode())) {
                // 导入模板5--应收经营租赁-2
                msg = import5(file, excelType);
            } else if (ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_6.getCode())) {
                // 导入模板6--其他应收款项-4
                msg = import6(file, excelType);
            } else if (ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_7.getCode())) {
                // 导入模板7--库存减值-7--
                msg = import7(file, excelType);
            } else if (ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_8.getCode())) {
                // 导入模板8--应收投资性房地产-6--
                msg = import8(file, excelType);
            }
//            else if (ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_9.getCode())) {
//                // 导入模板9--应收关联方租赁款-8
//                msg = import9(file, excelType);
//            }
            else if (ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_10.getCode())) {
                // 导入模板10--其他金融资产-9
                msg = import10(file, excelType);
            } else if (ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_11.getCode())
                    || ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_12.getCode())) {
                // 导入模板11,12--库存减值-7
                msg = import11(file, excelType);
            } else if (ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_13.getCode())) {
                // 导入模板13--库存减值-7
                msg = import13(file, excelType);
            } else {
                throw new ServiceException("excel模板类型[" + excelType + "]不匹配");
            }
        } catch (Exception e) {
            throw new ServiceException("上传文件失败，失败原因:" + e.getMessage());
        }
        return msg;
    }

    /**
     * @description:减值计提-首页列表-上传导入模板13
     **/
    private String import13(MultipartFile file, String excelType) throws Exception {
        // 库存减值-7
        String impairmentType = ImpairmentTypeEnum.TYPE_ENUM_7.getCode();
        ExcelUtil<ImpairmentProvisionDetailExcel13> util = new ExcelUtil<ImpairmentProvisionDetailExcel13>(ImpairmentProvisionDetailExcel13.class);
        List<ImpairmentProvisionDetailExcel13> list = util.importExcel(file.getInputStream());
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException(ResultEnum.COMMON_IMPORT_EMPTY_ERROR.getMessage());
        }
        // 校验减值计提的状态
        checkStatus(impairmentType);
        // 记录上传任务
        Long taskId = saveTask(file.getOriginalFilename(), excelType, list.size(), ImpairmentTaskTypeEnum.TASK_TYPE_1.getCode(), null);
        // 1.校验数据
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("合同号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getBusinessType())) {
                throw new ServiceException("入库类型不能为空");
            }
            if (ObjectUtil.isEmpty(a.getProvisionTotal())) {
                a.setProvisionTotal(BigDecimal.ZERO);
            }
//            if (ObjectUtil.isEmpty(a.getOrgId())) {
//                throw new ServiceException("公司所属不能为空");
//            }
            // 签约主体简称转换
            //a.setOrgId(mapOrgIdName(a.getOrgId()));
            // 1.上传入库类型like'%抵债资产%'，业务类型为抵债资产；
            if (StrUtil.contains(a.getBusinessType(), "抵债资产")) {
                a.setBusinessType("抵债资产");
            }else if(StrUtil.contains(a.getBusinessType(), "租赁设备入库")) {
                a.setBusinessType("回收设备");
            }
            else {
                throw new ServiceException("根据资产类别[" + a.getBusinessType() + "]未匹配到对应业务类型");
            }
            if (ObjectUtil.equals(a.getIsVerification(), "是")) {
                a.setDelFlag(YesOrNoEnum.YES.getCode());
            }
            a.setImpairmentType(impairmentType);
            a.setExcelType(excelType);
        });
        // 减值计提-异步处理
        List<ImpairmentProvisionDetailEntity> detailEntityList = BeanUtil.copyToList(list, ImpairmentProvisionDetailEntity.class);
        // 1. 数据分片：将 list 拆分为多个批次
        List<List<ImpairmentProvisionDetailEntity>> batches = StringUtils.splitBatches(detailEntityList, FinanceEngineEnum.Numbers.ONE_HUNDRED_THOUSAND.getKey());
        // 2. 并行处理每个批次
        List<CompletableFuture<Integer>> futures = batches.stream().map(batch -> CompletableFuture.supplyAsync(() ->
                processSingleBatch(batch, excelType, impairmentType,
                        FinanceEngineEnum.TrueOrFalse.FALSE.isValue(),
                        FinanceEngineEnum.TrueOrFalse.FALSE.isValue(),
                        FinanceEngineEnum.TrueOrFalse.TRUE.isValue(),
                        FinanceEngineEnum.TrueOrFalse.FALSE.isValue(),
                        FinanceEngineEnum.TrueOrFalse.FALSE.isValue()), hthxTaskAsyncExecutor)).collect(Collectors.toList());
        // 3. 合并所有批次结果
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        // 4.转换结果并处理异常
        CompletableFuture<Integer> resultFuture = allFutures.handleAsync((result, ex) -> {
            if (ex != null) {
                // 如果有异常，传播异常
                throw new CompletionException(ex);
            }
            // 否则，计算总和 这里调用join不会抛出异常，因为allOf已完成
            return futures.stream().mapToInt(cf -> cf.join()).sum();
        });
        // 5. 统一处理任务状态更新
        resultFuture.whenComplete((totalCount, e) -> {
            if (e == null) {
                updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_2.getCode(), totalCount, 0, null);
            } else {
                // 提取原始异常信息
                Throwable rootCause = e.getCause();
                log.error(impairmentType+"-减值计提异步导入批次处理异常:{}", rootCause.getMessage());
                // 统计成功数（这里需要额外处理，可能需要遍历所有future）
                // 在异常分支中使用：
                int successCount = futures.stream()
                        .filter(cf -> !cf.isCompletedExceptionally() && !cf.isCancelled())
                        .mapToInt(cf -> {
                            try {
                                Integer result = cf.join();
                                return result != null ? result : 0;
                            } catch (Exception exception) {
                                return 0;
                            }
                        }).sum();
                // 准确计算失败数
                int actualTotalCount = detailEntityList.size();
                log.info("====>>ImpairmentProvisionServiceImpl.import13==>>00==>>actualTotalCount:{},totalCount:{},successCount:{}"
                        ,actualTotalCount,totalCount,successCount);
                int failCount = totalCount - successCount;
                updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_3.getCode(), successCount, failCount,StringUtils.truncateString(rootCause.getMessage()));
            }
        });
        return PromptMessageUtil.promptMessageFormat(ResultEnum.IP_FILE_UPLOAD_WAIT_TIME,(list.size() / FinanceEngineEnum.Numbers.THOUSAND.getKey() + 1));
    }

    /**
     * 导入模板11,12
     *
     * @param file
     * @param excelType
     */
    private String import11(MultipartFile file, String excelType) throws Exception {
        // 库存减值-7
        String impairmentType = ImpairmentTypeEnum.TYPE_ENUM_7.getCode();
        ExcelUtil<ImpairmentProvisionDetailExcel11> util = new ExcelUtil<ImpairmentProvisionDetailExcel11>(ImpairmentProvisionDetailExcel11.class);
        List<ImpairmentProvisionDetailExcel11> list = util.importExcel(file.getInputStream());
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException(ResultEnum.COMMON_IMPORT_EMPTY_ERROR.getMessage());
        }
        // 校验减值计提的状态
        checkStatus(impairmentType);
        // 记录上传任务
        Long taskId = saveTask(file.getOriginalFilename(), excelType, list.size(), ImpairmentTaskTypeEnum.TASK_TYPE_1.getCode(), null);
        // 1.校验数据
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("合同号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getBusinessType())) {
                throw new ServiceException("资产类别不能为空");
            }
            // 1.上传资产类别in('商用车','乘用车')，业务类型为回收设备；
            if (StrUtil.contains(a.getBusinessType(), "商用车") ||
                    StrUtil.contains(a.getBusinessType(), "乘用车")) {
                a.setBusinessType("回收设备");
            } else {
                throw new ServiceException("根据资产类别[" + a.getBusinessType() + "]未匹配到对应业务类型");
            }
            if (ObjectUtil.isEmpty(a.getProvisionTotal())) {
                //throw new ServiceException("累计减值准备不能为空！");
                a.setProvisionTotal(BigDecimal.ZERO);
            }
            if (ObjectUtil.equals(a.getIsVerification(), "是")) {
                a.setDelFlag(YesOrNoEnum.YES.getCode());
            }
            a.setImpairmentType(impairmentType);
            a.setExcelType(excelType);
        });
        // 减值计提-异步处理
        List<ImpairmentProvisionDetailEntity> detailEntityList = BeanUtil.copyToList(list, ImpairmentProvisionDetailEntity.class);
        // 1. 数据分片：将 list 拆分为多个批次
        List<List<ImpairmentProvisionDetailEntity>> batches = StringUtils.splitBatches(detailEntityList, FinanceEngineEnum.Numbers.ONE_HUNDRED_THOUSAND.getKey());
        // 2. 并行处理每个批次
        List<CompletableFuture<Integer>> futures = batches.stream().map(batch -> CompletableFuture.supplyAsync(() ->
                processSingleBatch(batch, excelType, impairmentType,
                        FinanceEngineEnum.TrueOrFalse.FALSE.isValue(),
                        FinanceEngineEnum.TrueOrFalse.TRUE.isValue(),
                        FinanceEngineEnum.TrueOrFalse.TRUE.isValue(),
                        FinanceEngineEnum.TrueOrFalse.FALSE.isValue(),
                        FinanceEngineEnum.TrueOrFalse.FALSE.isValue()), hthxTaskAsyncExecutor)).collect(Collectors.toList());
        // 3. 合并所有批次结果
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        // 4.转换结果并处理异常
        CompletableFuture<Integer> resultFuture = allFutures.handleAsync((result, ex) -> {
            if (ex != null) {
                // 如果有异常，传播异常
                throw new CompletionException(ex);
            }
            // 否则，计算总和 这里调用join不会抛出异常，因为allOf已完成
            return futures.stream().mapToInt(cf -> cf.join()).sum();
        });
        // 5. 统一处理任务状态更新
        resultFuture.whenComplete((totalCount, e) -> {
            if (e == null) {
                updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_2.getCode(), totalCount, 0, null);
            } else {
                // 提取原始异常信息
                Throwable rootCause = e.getCause();
                log.error(impairmentType+"-减值计提异步导入批次处理异常:{}", rootCause.getMessage());
                // 统计成功数（这里需要额外处理，可能需要遍历所有future）
                // 在异常分支中使用：
                int successCount = futures.stream()
                        .filter(cf -> !cf.isCompletedExceptionally() && !cf.isCancelled())
                        .mapToInt(cf -> {
                            try {
                                Integer result = cf.join();
                                return result != null ? result : 0;
                            } catch (Exception exception) {
                                return 0;
                            }
                        }).sum();
                // 准确计算失败数
                int actualTotalCount = detailEntityList.size();
                log.info("====>>ImpairmentProvisionServiceImpl.import11==>>00==>>actualTotalCount:{},totalCount:{},successCount:{}"
                        ,actualTotalCount,totalCount,successCount);
                int failCount = totalCount - successCount;
                updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_3.getCode(), successCount, failCount,StringUtils.truncateString(rootCause.getMessage()));
            }
        });
        return PromptMessageUtil.promptMessageFormat(ResultEnum.IP_FILE_UPLOAD_WAIT_TIME,(list.size() / FinanceEngineEnum.Numbers.THOUSAND.getKey() + 1));
    }

    /**
     * 导入模板10
     * 合同为空
     *
     * @param file
     * @param excelType
     */
    private String import10(MultipartFile file, String excelType) throws Exception {
        // 其他金融资产
        String impairmentType = ImpairmentTypeEnum.TYPE_ENUM_9.getCode();
        ExcelUtil<ImpairmentProvisionDetailExcel10> util = new ExcelUtil<ImpairmentProvisionDetailExcel10>(ImpairmentProvisionDetailExcel10.class);
        List<ImpairmentProvisionDetailExcel10> list = util.importExcel(file.getInputStream());
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException(ResultEnum.COMMON_IMPORT_EMPTY_ERROR.getMessage());
        }
        // 校验减值计提的状态
        checkStatus(impairmentType);
        // 记录上传任务
        Long taskId = saveTask(file.getOriginalFilename(), excelType, list.size(), ImpairmentTaskTypeEnum.TASK_TYPE_1.getCode(), null);
        // 1.校验数据
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getOrgId())) {
                throw new ServiceException("公司不能为空");
            }
            if (ObjectUtil.isEmpty(a.getBusinessType())) {
                throw new ServiceException("业务类型不能为空");
            }
            if (ObjectUtil.isEmpty(a.getProvisionTotal())) {
                throw new ServiceException("ECL（人民币）不能为空");
            }
            if (ObjectUtil.isEmpty(a.getFiveClass())) {
                throw new ServiceException("资产分类不能为空");
            }
            if (ObjectUtil.isEmpty(a.getThreeStep())) {
                throw new ServiceException("风险阶段不能为空");
            }
            if (ObjectUtil.isEmpty(a.getFinancialInstitution())) {
                throw new ServiceException("金融机构不能为空");
            }

            // 签约主体简称转换
            //a.setOrgId(mapOrgIdName(a.getOrgId()));

            // 1.上传业务类型like'%资产支持专项计划%'，业务类型为资产支持专项计划；
            // 2.上传业务类型like'%其他金融资产%'，业务类型为其他金融资产；
            // 3.上传业务类型like'%私募债%'，业务类型为私募债；
            if (StrUtil.contains(a.getBusinessType(), "资产支持专项计划")) {
                a.setBusinessType("资产支持专项计划");
            } else if (StrUtil.contains(a.getBusinessType(), "其他金融资产")) {
                a.setBusinessType("其他金融资产");
            } else if (StrUtil.contains(a.getBusinessType(), "私募债")) {
                a.setBusinessType("私募债");
            } else {
                throw new ServiceException("根据业务类型[" + a.getBusinessType() + "]未匹配到对应业务类型");
            }
            a.setImpairmentType(impairmentType);
            a.setExcelType(excelType);

            // 币种不同，转换汇率
            if (ObjectUtil.isNotEmpty(a.getOriginCurrency()) && ObjectUtil.isNotEmpty(a.getTargetCurrency()) && ObjectUtil.notEqual(a.getOriginCurrency(), a.getTargetCurrency())) {
                // 根据来源币种 和 目标币种查汇率
                BigDecimal exchangeRate = iEasExchangeRateService.getRateBySourceNameAndTargetName(a.getOriginCurrency(), a.getTargetCurrency());
                a.setExchangeRate(exchangeRate);
            }

        });
        // 异步处理
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            List<ImpairmentProvisionDetailEntity> detailEntityList = BeanUtil.copyToList(list, ImpairmentProvisionDetailEntity.class);
            // 转换签约主体
            transformOrgId(detailEntityList);
            // 转换五级分类
            transformFiveClass(detailEntityList);
            saveData(excelType, detailEntityList, impairmentType, FinanceEngineEnum.TrueOrFalse.TRUE.isValue());
            return detailEntityList.size();
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_2.getCode(), v, 0, null);
        }).exceptionally(e -> {
            log.info("减值计提上传文件 异步执行异常：", e);
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_3.getCode(), 0, 0, e.getMessage());
            return null;
        });
        int minute = list.size() / 5000 + 1;
        return "文件上传成功，请" + minute + "分钟后查看数据";
    }

    /**
     * 导入模板9
     *
     * @param file
     * @param excelType
     */
    private String import9(MultipartFile file, String excelType) throws Exception {
        // 应收关联方租赁款
        String impairmentType = ImpairmentTypeEnum.TYPE_ENUM_8.getCode();
        ExcelUtil<ImpairmentProvisionDetailExcel9> util = new ExcelUtil<ImpairmentProvisionDetailExcel9>(ImpairmentProvisionDetailExcel9.class);
        List<ImpairmentProvisionDetailExcel9> list = util.importExcel(file.getInputStream());
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException(ResultEnum.COMMON_IMPORT_EMPTY_ERROR.getMessage());
        }
        // 校验减值计提的状态
        checkStatus(impairmentType);
        // 记录上传任务
        Long taskId = saveTask(file.getOriginalFilename(), excelType, list.size(), ImpairmentTaskTypeEnum.TASK_TYPE_1.getCode(), null);
        // 1.校验数据
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("物料编码不能为空");
            }
            if (ObjectUtil.isEmpty(a.getClientName())) {
                throw new ServiceException("公司名称不能为空");
            }
            if (ObjectUtil.isEmpty(a.getProvisionTotal())) {
                throw new ServiceException("减值不能为空");
            }
            // 业务类型默认：恒信应收蓬莱
            a.setBusinessType("恒信应收蓬莱");
            a.setImpairmentType(impairmentType);
            a.setExcelType(excelType);
        });
        // 异步处理
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            List<ImpairmentProvisionDetailEntity> detailEntityList = BeanUtil.copyToList(list, ImpairmentProvisionDetailEntity.class);
            // 校验合同编号
            checkContractCode(detailEntityList);
            // 根据合同编号查询签约主体
            setOrgIdByContractCode(detailEntityList);
            saveData(excelType, detailEntityList, impairmentType, FinanceEngineEnum.TrueOrFalse.TRUE.isValue());
            return detailEntityList.size();
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_2.getCode(), v, 0, null);
        }).exceptionally(e -> {
            log.info("减值计提上传文件 异步执行异常：", e);
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_3.getCode(), 0, 0, e.getMessage());
            return null;
        });
        int minute = list.size() / 5000 + 1;
        return "文件上传成功，请" + minute + "分钟后查看数据";
    }

    /**
     * 导入模板8
     * 合同、签约主体为空
     *
     * @param file
     * @param excelType
     */
    private String import8(MultipartFile file, String excelType) throws Exception {
        // 应收投资性房地产
        String impairmentType = ImpairmentTypeEnum.TYPE_ENUM_6.getCode();
        ExcelUtil<ImpairmentProvisionDetailExcel8> util = new ExcelUtil<ImpairmentProvisionDetailExcel8>(ImpairmentProvisionDetailExcel8.class);
        List<ImpairmentProvisionDetailExcel8> list = util.importExcel(file.getInputStream());
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException(ResultEnum.COMMON_IMPORT_EMPTY_ERROR.getMessage());
        }
        // 校验减值计提的状态
        checkStatus(impairmentType);
        // 记录上传任务
        Long taskId = saveTask(file.getOriginalFilename(), excelType, list.size(), ImpairmentTaskTypeEnum.TASK_TYPE_1.getCode(), null);
        // 1.校验数据
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getClientName())) {
                throw new ServiceException("核算项目名称不能为空");
            }
            if (ObjectUtil.isEmpty(a.getBusinessType())) {
                throw new ServiceException("科目名称不能为空");
            }
            if (ObjectUtil.isEmpty(a.getProvisionTotal())) {
                throw new ServiceException("拨备合计（折合人民币）不能为空");
            }
            if (ObjectUtil.isEmpty(a.getOrgId())) {
                throw new ServiceException("主体不能为空");
            }
            // 签约主体简称转换
            //a.setOrgId(mapOrgIdName(a.getOrgId()));
            // 1.上传科目like'%投资性房地产%'，业务类型为投资性房地产；
            if (StrUtil.contains(a.getBusinessType(), "投资性房地产")) {
                a.setBusinessType("投资性房地产");
            } else {
                throw new ServiceException("根据科目名称[" + a.getBusinessType() + "]未匹配到对应业务类型");
            }
            a.setImpairmentType(impairmentType);
            a.setExcelType(excelType);
        });
        // 异步处理
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            // 没有签约主体、合同，只有客户，按签约主体、合同为空处理，明细页按合同（空）+签约主体（空）+业务类型+减值类型+客户展示
            List<ImpairmentProvisionDetailEntity> detailEntityList = BeanUtil.copyToList(list, ImpairmentProvisionDetailEntity.class);
            // 转换签约主体
            transformOrgId(detailEntityList);
            // 根据合同编号查询签约主体
            //setOrgIdByContractCode(detailEntityList);
            saveData(excelType, detailEntityList, impairmentType, FinanceEngineEnum.TrueOrFalse.TRUE.isValue());
            return detailEntityList.size();
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_2.getCode(), v, 0, null);
        }).exceptionally(e -> {
            log.info("减值计提上传文件 异步执行异常：", e);
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_3.getCode(), 0, 0, e.getMessage());
            return null;
        });
        int minute = list.size() / 5000 + 1;
        return "文件上传成功，请" + minute + "分钟后查看数据";
    }


    /**
     * 导入模板7
     *
     * @param file
     * @param excelType
     */
    private String import7(MultipartFile file, String excelType) throws Exception {
        // 长期应收款
        String impairmentType = ImpairmentTypeEnum.TYPE_ENUM_5.getCode();
        ExcelUtil<ImpairmentProvisionDetailExcel7> util = new ExcelUtil<ImpairmentProvisionDetailExcel7>(ImpairmentProvisionDetailExcel7.class);
        List<ImpairmentProvisionDetailExcel7> list = util.importExcel(file.getInputStream());
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException(ResultEnum.COMMON_IMPORT_EMPTY_ERROR.getMessage());
        }
        // 校验减值计提的状态
        checkStatus(impairmentType);
        // 记录上传任务
        Long taskId = saveTask(file.getOriginalFilename(), excelType, list.size(), ImpairmentTaskTypeEnum.TASK_TYPE_1.getCode(), null);
        // 1.校验数据
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getBusinessType())) {
                throw new ServiceException("科目名称不能为空");
            }
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("核算项目名称不能为空");
            }
            if (ObjectUtil.isEmpty(a.getProvisionTotal())) {
                throw new ServiceException("拨备合计Round(ECL)不能为空");
            }
            // 签约主体简称转换
            //a.setOrgId(mapOrgIdName(a.getOrgId()));
            // 1.上传科目like'%长期应收款%'，业务类型为长期应收款；
            if (StrUtil.contains(a.getBusinessType(), "长期应收款")) {
                a.setBusinessType("长期应收款");
            } else {
                throw new ServiceException("根据科目名称[" + a.getBusinessType() + "]未匹配到对应业务类型");
            }
            a.setImpairmentType(impairmentType);
            a.setExcelType(excelType);
        });
        // 异步处理
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            List<ImpairmentProvisionDetailEntity> detailEntityList = BeanUtil.copyToList(list, ImpairmentProvisionDetailEntity.class);
            // 转换签约主体
            //transformOrgId(detailEntityList);
            // 根据合同编号查询签约主体
            setOrgIdByContractCode(detailEntityList);
            saveData(excelType, detailEntityList, impairmentType, FinanceEngineEnum.TrueOrFalse.TRUE.isValue());
            return detailEntityList.size();
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_2.getCode(), v, 0, null);
        }).exceptionally(e -> {
            log.info("减值计提上传文件 异步执行异常：", e);
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_3.getCode(), 0, 0, e.getMessage());
            return null;
        });
        int minute = list.size() / 5000 + 1;
        return "文件上传成功，请" + minute + "分钟后查看数据";
    }

    /**
     * 导入模板6
     *
     * @param file
     * @param excelType
     */
    private String import6(MultipartFile file, String excelType) throws Exception {
        // 其他应收款项
        String impairmentType = ImpairmentTypeEnum.TYPE_ENUM_4.getCode();
        ExcelUtil<ImpairmentProvisionDetailExcel6> util = new ExcelUtil<ImpairmentProvisionDetailExcel6>(ImpairmentProvisionDetailExcel6.class);
        List<ImpairmentProvisionDetailExcel6> list = util.importExcel(file.getInputStream());
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException(ResultEnum.COMMON_IMPORT_EMPTY_ERROR.getMessage());
        }
        // 校验减值计提的状态
        checkStatus(impairmentType);
        // 记录上传任务
        Long taskId = saveTask(file.getOriginalFilename(), excelType, list.size(), ImpairmentTaskTypeEnum.TASK_TYPE_1.getCode(), null);
        // 1.校验数据
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getOrgId())) {
                throw new ServiceException("主体不能为空");
            }
            if (ObjectUtil.isEmpty(a.getBusinessType())) {
                throw new ServiceException("科目名称不能为空");
            }
            if (ObjectUtil.isEmpty(a.getProvisionTotal())) {
                throw new ServiceException("拨备合计不能为空");
            }
            // 签约主体简称转换
            //a.setOrgId(mapOrgIdName(a.getOrgId()));
            // 1.上传科目like'%其他应收款项%'，业务类型为其他应收款项；
            if (StrUtil.contains(a.getBusinessType(), "其他应收款项")) {
                a.setBusinessType("其他应收款项");
            } else {
                throw new ServiceException("根据科目名称[" + a.getBusinessType() + "]未匹配到对应业务类型");
            }
            a.setImpairmentType(impairmentType);
            a.setExcelType(excelType);
        });
        // 异步处理
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            List<ImpairmentProvisionDetailEntity> detailEntityList = BeanUtil.copyToList(list, ImpairmentProvisionDetailEntity.class);
            // 转换签约主体
            transformOrgId(detailEntityList);
            // 校验合同编号
            checkContractCode(detailEntityList);
            saveData(excelType, detailEntityList, impairmentType, FinanceEngineEnum.TrueOrFalse.TRUE.isValue());
            return detailEntityList.size();
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_2.getCode(), v, 0, null);
        }).exceptionally(e -> {
            log.info("减值计提上传文件 异步执行异常：", e);
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_3.getCode(), 0, 0, e.getMessage());
            return null;
        });
        int minute = list.size() / 5000 + 1;
        return "文件上传成功，请" + minute + "分钟后查看数据";
    }

    /**
     * 导入模板5
     *
     * @param file
     * @param excelType
     */
    private String import5(MultipartFile file, String excelType) throws Exception {
        // 应收经营租赁
        String impairmentType = ImpairmentTypeEnum.TYPE_ENUM_2.getCode();
        ExcelUtil<ImpairmentProvisionDetailExcel5> util = new ExcelUtil<ImpairmentProvisionDetailExcel5>(ImpairmentProvisionDetailExcel5.class);
        List<ImpairmentProvisionDetailExcel5> list = util.importExcel(file.getInputStream());
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException(ResultEnum.COMMON_IMPORT_EMPTY_ERROR.getMessage());
        }
        // 校验减值计提的状态
        checkStatus(impairmentType);
        // 记录上传任务
        Long taskId = saveTask(file.getOriginalFilename(), excelType, list.size(), ImpairmentTaskTypeEnum.TASK_TYPE_1.getCode(), null);
        // 1.校验数据
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getOrgId())) {
                throw new ServiceException("核算项目名称不能为空");
            }
            if (ObjectUtil.isEmpty(a.getBusinessType())) {
                throw new ServiceException("科目名称不能为空");
            }
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("合同号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getProvisionTotal())) {
                throw new ServiceException("拨备合计不能为空");
            }
            // 1.上传科目like'%经营租赁%'，业务类型为经营租赁；
            if (StrUtil.contains(a.getBusinessType(), "经营租赁")) {
                a.setBusinessType("经营租赁");
            } else {
                throw new ServiceException("根据科目名称[" + a.getBusinessType() + "]未匹配到对应业务类型");
            }
            a.setImpairmentType(impairmentType);
            a.setExcelType(excelType);
        });
        // 异步处理
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            List<ImpairmentProvisionDetailEntity> detailEntityList = BeanUtil.copyToList(list, ImpairmentProvisionDetailEntity.class);
            // 转换签约主体
            transformOrgId(detailEntityList);
            // 校验合同编号
            checkContractCode(detailEntityList);
            saveData(excelType, detailEntityList, impairmentType, FinanceEngineEnum.TrueOrFalse.TRUE.isValue());
            return detailEntityList.size();
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_2.getCode(), v, 0, null);
        }).exceptionally(e -> {
            log.info("减值计提上传文件 异步执行异常：", e);
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_3.getCode(), 0, 0, e.getMessage());
            return null;
        });
        int minute = list.size() / 5000 + 1;
        return "文件上传成功，请" + minute + "分钟后查看数据";
    }

    /**
     * 导入模板3,4
     *
     * @param file
     * @param excelType
     */
    private String import3(MultipartFile file, String excelType) throws Exception {
        // 减值类型 为其他应收款
        String impairmentType = ImpairmentTypeEnum.TYPE_ENUM_3.getCode();

        ExcelUtil<ImpairmentProvisionDetailExcel3> util = new ExcelUtil<ImpairmentProvisionDetailExcel3>(ImpairmentProvisionDetailExcel3.class);
        List<ImpairmentProvisionDetailExcel3> list = util.importExcel(file.getInputStream());
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException(ResultEnum.COMMON_IMPORT_EMPTY_ERROR.getMessage());
        }
        // 校验减值计提的状态
        checkStatus(impairmentType);
        // 记录上传任务
        Long taskId = saveTask(file.getOriginalFilename(), excelType, list.size(), ImpairmentTaskTypeEnum.TASK_TYPE_1.getCode(), null);
        // 1.校验数据
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getOrgId())) {
                throw new ServiceException("所属账套不能为空");
            }
            if (ObjectUtil.isEmpty(a.getBusinessType())) {
                throw new ServiceException("科目不能为空");
            }
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("辅助账编码不能为空");
            }
            if (ObjectUtil.isEmpty(a.getProvisionTotal())) {
                throw new ServiceException("ECL不能为空");
            }

            if (StrUtil.contains(a.getBusinessType(), "诉讼保全费")) {
                a.setBusinessType("诉讼保全费");
            } else if (StrUtil.contains(a.getBusinessType(), "诉讼保证金")) {
                a.setBusinessType("诉讼保证金");
            } else if (StrUtil.contains(a.getBusinessType(), "其他保证金")) {
                a.setBusinessType("其他保证金");
            } else {
                throw new ServiceException("根据科目[" + a.getBusinessType() + "]未匹配到对应业务类型");
            }
            a.setImpairmentType(impairmentType);
            a.setExcelType(excelType);

            // 币种不同，转换汇率
            if (ObjectUtil.isNotEmpty(a.getOriginCurrency()) && ObjectUtil.isNotEmpty(a.getTargetCurrency()) && ObjectUtil.notEqual(a.getOriginCurrency(), a.getTargetCurrency())) {
                // 根据来源币种 和 目标币种查汇率
                BigDecimal exchangeRate = iEasExchangeRateService.getRateBySourceNameAndTargetName(a.getOriginCurrency(), a.getTargetCurrency());
                a.setProvisionTotal(NumberUtil.mul(a.getProvisionTotal(), exchangeRate));
            }
        });
        // 异步处理
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            List<ImpairmentProvisionDetailEntity> detailEntityList = BeanUtil.copyToList(list, ImpairmentProvisionDetailEntity.class);
            // 转换签约主体
            transformOrgId(detailEntityList);
            // 校验合同编号
            checkContractCode(detailEntityList);
            saveData(excelType, detailEntityList, impairmentType, FinanceEngineEnum.TrueOrFalse.TRUE.isValue());
            return detailEntityList.size();
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_2.getCode(), v, 0, null);
        }).exceptionally(e -> {
            log.info("减值计提上传文件 异步执行异常：", e);
            updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_3.getCode(), 0, 0, e.getMessage());
            return null;
        });
        int minute = list.size() / 5000 + 1;
        return "文件上传成功，请" + minute + "分钟后查看数据";
    }

    /**
     * 导入模板1，2
     */
    private String import1(MultipartFile file, String excelType) throws Exception {
        // 租赁资产-1
        String impairmentType = ImpairmentTypeEnum.TYPE_ENUM_1.getCode();
        ExcelUtil<ImpairmentProvisionDetailExcel1> util = new ExcelUtil<ImpairmentProvisionDetailExcel1>(ImpairmentProvisionDetailExcel1.class);
        List<ImpairmentProvisionDetailExcel1> list = util.importExcel(file.getInputStream());
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException(ResultEnum.COMMON_IMPORT_EMPTY_ERROR.getMessage());
        }
        // 校验减值计提的状态-校验当前月份对应减值类型的数据，只有（2-已提交）+（已冲销+并且状态为1-已录入/5-已拒绝）的数据处理完后才能允许提交
        checkStatus(impairmentType);
        // 记录本次上传任务-校验某个类型文件的某个操作，是否重复操作
        Long taskId = saveTask(file.getOriginalFilename(), excelType, list.size(), ImpairmentTaskTypeEnum.TASK_TYPE_1.getCode(), null);
        // 1.校验数据
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("合同号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getClientName())) {
                throw new ServiceException("承租人不能为空");
            }
            if (ObjectUtil.isEmpty(a.getRiskExposure())) {
                throw new ServiceException("剩余风险敞口不能为空");
            }
            if (ObjectUtil.isEmpty(a.getRentReceivableBalance())) {
                throw new ServiceException("租金余额不能为空");
            }
            if (ObjectUtil.isEmpty(a.getFiveClass())) {
                throw new ServiceException("五级分类不能为空");
            }
            if (ObjectUtil.isEmpty(a.getThreeStep())) {
                throw new ServiceException("风险阶段划分不能为空");
            }
            if (ObjectUtil.isEmpty(a.getProvisionTotal())) {
                throw new ServiceException("Round(ECL4)不能为空");
            }
            if (ObjectUtil.isEmpty(a.getBusinessType())) {
                // 上传为空时默认：租赁
                a.setBusinessType("租赁");
            }
            // 减值类型 为租赁资产
            a.setImpairmentType(impairmentType);
            a.setExcelType(excelType);
        });
        // 减值计提-异步处理
        List<ImpairmentProvisionDetailEntity> detailEntityList = BeanUtil.copyToList(list, ImpairmentProvisionDetailEntity.class);
        // 1. 数据分片：将 list 拆分为多个批次
        List<List<ImpairmentProvisionDetailEntity>> batches = StringUtils.splitBatches(detailEntityList, FinanceEngineEnum.Numbers.ONE_HUNDRED_THOUSAND.getKey());
        // 2. 并行处理每个批次
        List<CompletableFuture<Integer>> futures = batches.stream().map(batch -> CompletableFuture.supplyAsync(() ->
                processSingleBatch(batch, excelType, impairmentType,
                        FinanceEngineEnum.TrueOrFalse.TRUE.isValue(),
                        FinanceEngineEnum.TrueOrFalse.TRUE.isValue(),
                        FinanceEngineEnum.TrueOrFalse.TRUE.isValue(),
                        FinanceEngineEnum.TrueOrFalse.FALSE.isValue(),
                        FinanceEngineEnum.TrueOrFalse.TRUE.isValue()), hthxTaskAsyncExecutor)).collect(Collectors.toList());
        // 3. 合并所有批次结果
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        // 4.转换结果并处理异常
        CompletableFuture<Integer> resultFuture = allFutures.handleAsync((result, ex) -> {
            if (ex != null) {
                // 如果有异常，传播异常
                throw new CompletionException(ex);
            }
            // 否则，计算总和 这里调用join不会抛出异常，因为allOf已完成
            return futures.stream().mapToInt(cf -> cf.join()).sum();
        });
        // 5. 统一处理任务状态更新
        resultFuture.whenComplete((totalCount, e) -> {
            if (e == null) {
                updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_2.getCode(), totalCount, 0, null);
            } else {
                // 提取原始异常信息
                Throwable rootCause = e.getCause();
                log.error(impairmentType+"-减值计提异步导入批次处理异常:{}", rootCause.getMessage());
                // 统计成功数（这里需要额外处理，可能需要遍历所有future）
                // 在异常分支中使用：
                int successCount = futures.stream()
                        .filter(cf -> !cf.isCompletedExceptionally() && !cf.isCancelled())
                        .mapToInt(cf -> {
                            try {
                                Integer result = cf.join();
                                return result != null ? result : 0;
                            } catch (Exception exception) {
                                return 0;
                            }
                        }).sum();
                int actualTotalCount = detailEntityList.size();
                log.info("====>>ImpairmentProvisionServiceImpl.import1==>>00==>>actualTotalCount:{},totalCount:{},successCount:{}"
                        ,actualTotalCount,totalCount,successCount);
                // 准确计算失败数
                int failCount = actualTotalCount - successCount;
                updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_3.getCode(), successCount, failCount,StringUtils.truncateString(rootCause.getMessage()));
            }
        });
        return PromptMessageUtil.promptMessageFormat(ResultEnum.IP_FILE_UPLOAD_WAIT_TIME,(list.size() / FinanceEngineEnum.Numbers.THOUSAND.getKey() + 1));
    }

    /**
     * @description: 减值计提-异步处理-单个批次处理逻辑
     * @param: detailEntityList
     * @param: excelType
     * @param: impairmentType
     * @param: isTransformFiveClass 是否转换五级分类
     * @param: isCheckContractCode 是否校验合同编号
     * @param: isGetOrgIdByContractCode 是否查询签约主体
     * @param: isTransformOrgId 是否转换签约主体
     * @param: isExtends 是否需要继承上月减值计提数据 true 需要继承，false 不需要继承
     **/
    private Integer processSingleBatch(List<ImpairmentProvisionDetailEntity> detailEntityList, String excelType,
                                       String impairmentType, boolean isTransformFiveClass,
                                       boolean isCheckContractCode, boolean isSetOrgIdByContractCode,
                                       boolean isTransformOrgId,
                                       boolean isExtends) {
        // 校验合同编号
        if(isCheckContractCode){
            checkContractCode(detailEntityList);
        }
        // 设置客户编码
        batchSetClientCode(detailEntityList);
        // 转换五级分类
        if(isTransformFiveClass){
            transformFiveClass(detailEntityList);
        }
        // 设置签约主体
        if(isSetOrgIdByContractCode){
            setOrgIdByContractCode(detailEntityList);
        }
        // 转换签约主体
        if(isTransformOrgId){
            transformOrgId(detailEntityList);
        }
        // 保存数据
        saveData(excelType, detailEntityList, impairmentType,isExtends);
        return detailEntityList.size();
    }

    /**
     * 记录上传任务-校验某个类型文件的某个操作，是否重复操作
     * @param fileName
     * @param excelType
     * @param size
     * @param taskType
     * @param docId
     * @return
     */
    private Long saveTask(String fileName, String excelType, int size, String taskType, Long docId) {
        // 1.先校验任务-第一次上传：
        ImpairmentProvisionUploadTaskEntity vo = iImpairmentProvisionUploadTaskService.getOne(new LambdaUpdateWrapper<ImpairmentProvisionUploadTaskEntity>()
                .eq(ImpairmentProvisionUploadTaskEntity::getTaskType, taskType)
                .eq(ObjectUtil.isNotEmpty(docId), ImpairmentProvisionUploadTaskEntity::getDocId, docId)
                .eq(ObjectUtil.isNotEmpty(excelType), ImpairmentProvisionUploadTaskEntity::getExcelType, excelType)
                .eq(ImpairmentProvisionUploadTaskEntity::getStatus, ImpairmentTaskTypeEnum.STATUS_1.getCode()));
        // 校验某个类型文件的某个操作，是否重复操作
        if (ObjectUtil.isNotEmpty(vo)) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("用户：" + vo.getUserName());
            stringBuffer.append(" 开始于：" + HthxDateUtils.localDateTimeFormat(vo.getStartTime(), HthxDateUtils.YYYY_MM_DD_HH_MM_SS));
            stringBuffer.append(" 的[" + vo.getTaskType() + "]任务未执行完成，请稍后再试");
            throw new ServiceException(stringBuffer.toString());
        }
        // 2.再创建任务
        ImpairmentProvisionUploadTaskEntity taskEntity = new ImpairmentProvisionUploadTaskEntity();
        taskEntity.setExcelType(excelType);
        taskEntity.setStartTime(LocalDateTime.now());
        taskEntity.setFileName(fileName);
        taskEntity.setUserId(SecurityUtils.getUserId());
        taskEntity.setUserName(SecurityUtils.getUsername());
        taskEntity.setDataSize(size);
        taskEntity.setTaskType(taskType);
        taskEntity.setStatus(ImpairmentTaskTypeEnum.STATUS_1.getCode());
        taskEntity.setDocId(docId);
        iImpairmentProvisionUploadTaskService.save(taskEntity);
        return taskEntity.getId();
    }

    /**
     * @description: 重载更新任务状态
     * @author: zhangli.chen
     **/
    private Long saveTask(int size, String taskType,String status, Long docId) {
        // 1.先校验任务-第一次上传：
        ImpairmentProvisionUploadTaskEntity taskEntity = iImpairmentProvisionUploadTaskService.getOne(new LambdaUpdateWrapper<ImpairmentProvisionUploadTaskEntity>()
                .eq(ImpairmentProvisionUploadTaskEntity::getTaskType, taskType)
                .eq(ObjectUtil.isNotEmpty(docId), ImpairmentProvisionUploadTaskEntity::getDocId, docId)
                .eq(ImpairmentProvisionUploadTaskEntity::getStatus, ImpairmentTaskTypeEnum.STATUS_1.getCode()));
        if(taskEntity==null){
            taskEntity = new ImpairmentProvisionUploadTaskEntity();
            taskEntity.setStartTime(LocalDateTime.now());
        }
        taskEntity.setEndTime(LocalDateTime.now());
        taskEntity.setUserId(SecurityUtils.getUserId());
        taskEntity.setUserName(SecurityUtils.getUsername());
        taskEntity.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
        taskEntity.setUpdateTime(LocalDateTime.now());
        taskEntity.setDataSize(size);
        taskEntity.setDataTotalSize(size);
        taskEntity.setTaskType(taskType);
        taskEntity.setStatus(status);
        taskEntity.setDocId(docId);
        iImpairmentProvisionUploadTaskService.saveOrUpdate(taskEntity);
        return taskEntity.getId();
    }

    /**
     * 更新任务状态
     *
     * @param taskId
     * @param taskStatus
     * @param successSize
     * @param failSize
     * @param msg
     */
    private void updateTask(Long taskId, String taskStatus, int successSize, int failSize, String msg) {
        iImpairmentProvisionUploadTaskService.lambdaUpdate()
                .set(ImpairmentProvisionUploadTaskEntity::getStatus, taskStatus)
                .set(ImpairmentProvisionUploadTaskEntity::getErrorInfo, msg)
                .set(ImpairmentProvisionUploadTaskEntity::getEndTime, LocalDateTime.now())
                .set(ImpairmentProvisionUploadTaskEntity::getDataSuccessSize, successSize)
                .set(ImpairmentProvisionUploadTaskEntity::getDataFailedSize, failSize)
                .eq(ImpairmentProvisionUploadTaskEntity::getId, taskId)
                .update();
    }

    private void updateTask(Long taskId, String taskStatus,String msg) {
        iImpairmentProvisionUploadTaskService.lambdaUpdate()
                .set(ImpairmentProvisionUploadTaskEntity::getStatus, taskStatus)
                .set(ImpairmentProvisionUploadTaskEntity::getErrorInfo, msg)
                .set(ImpairmentProvisionUploadTaskEntity::getEndTime, LocalDateTime.now())
                .eq(ImpairmentProvisionUploadTaskEntity::getId, taskId)
                .update();
    }

    /**
     * @description: 更新任务表+新增数据总条数更新
     * @author: zhangli.chen
     * @date 2025/04/23 11:07
     **/
    private void updateTaskWithTotalNumber(Long taskId, String taskStatus, int successSize, int failSize, String message, int dataTotalSize, int dataExceptionSize) {
        iImpairmentProvisionUploadTaskService.lambdaUpdate()
                .set(ImpairmentProvisionUploadTaskEntity::getStatus, taskStatus)
                .set(ImpairmentProvisionUploadTaskEntity::getErrorInfo, message)
                .set(ImpairmentProvisionUploadTaskEntity::getEndTime, LocalDateTime.now())
                .set(ImpairmentProvisionUploadTaskEntity::getDataSuccessSize, successSize)
                .set(ImpairmentProvisionUploadTaskEntity::getDataFailedSize, failSize)
                .set(ImpairmentProvisionUploadTaskEntity::getDataTotalSize, dataTotalSize)
                .set(ImpairmentProvisionUploadTaskEntity::getDataExceptionSize,dataExceptionSize)
                .eq(ImpairmentProvisionUploadTaskEntity::getId, taskId)
                .update();
    }

    /**
     * 转换五级分类
     *
     * @param detailEntityList
     */
    private void transformFiveClass(List<ImpairmentProvisionDetailEntity> detailEntityList) {
        detailEntityList.stream().forEach(a -> {
            if (ObjectUtil.isNotEmpty(a.getFiveClass())) {
                if (StrUtil.contains(a.getFiveClass(), "次级")) {
                    a.setFiveClass(ImpairmentTypeEnum.FIVE_CLASS_3.getCode());
                } else if (StrUtil.contains(a.getFiveClass(), "关注")) {
                    a.setFiveClass(ImpairmentTypeEnum.FIVE_CLASS_2.getCode());
                } else if (StrUtil.contains(a.getFiveClass(), "可疑")) {
                    a.setFiveClass(ImpairmentTypeEnum.FIVE_CLASS_4.getCode());
                } else if (StrUtil.contains(a.getFiveClass(), "损失")) {
                    a.setFiveClass(ImpairmentTypeEnum.FIVE_CLASS_5.getCode());
                } else if (StrUtil.contains(a.getFiveClass(), "正常")) {
                    a.setFiveClass(ImpairmentTypeEnum.FIVE_CLASS_1.getCode());
                }
            }
        });
    }

    /**
     * 根据合同编号查询签约主体
     * 1、只有在签约主体为空的情况下才会根据合同来查询主体信息
     * 2、支持同时传的一批数据无合同编号，即针对有合同编号的数据来查询主体信息
     */
    private void setOrgIdByContractCode(List<ImpairmentProvisionDetailEntity> detailEntityList) {
        // 过滤生成本次导入的合同列表
        List<String> contractCodeList = detailEntityList.stream().map(ImpairmentProvisionDetailEntity::getContractCode).distinct().collect(Collectors.toList());
        // 查询合同表，查询出本次导入合同的所有合同信息
        List<ContractEntity> contractEntityList ;
        if(StringUtils.isNotEmpty(contractCodeList)){
//            contractEntityList =  iContractService.list(new LambdaQueryWrapper<ContractEntity>()
//                    .select(ContractEntity::getContractCode, ContractEntity::getOrgId, ContractEntity::getContractCodeM
//                            ,ContractEntity::getClientCode,ContractEntity::getClientName,ContractEntity::getFinancialContractStatus).in(ContractEntity::getContractCode, contractCodeList));
           List<ContractMonthEntity> contractMonthEntityList =  contractMonthService.list(new LambdaQueryWrapper<ContractMonthEntity>()
                    .select(ContractMonthEntity::getContractCode, ContractMonthEntity::getOrgId, ContractMonthEntity::getContractCodeM
                            ,ContractMonthEntity::getClientCode,ContractMonthEntity::getClientName,ContractMonthEntity::getFinancialContractStatus).in(ContractMonthEntity::getContractCode, contractCodeList));
            contractEntityList = BeanUtil.copyToList(contractMonthEntityList, ContractEntity.class);
        }else{
            contractEntityList = new ArrayList<>();
        }
        //log.info("====>>setOrgIdByContractCode==>>00==>>contractEntityList:{}",contractEntityList);
        // 循环本次导入明细
        detailEntityList.stream().forEach(a -> {
            // 若已存在签约主体，则以导入为准，若没有则关联合同查询
            //log.info("====>>setOrgIdByContractCode==>>01==>>StringUtils.isNotEmpty(a.getOrgId()):{}",StringUtils.isNotEmpty(a.getOrgId()));
            if(StringUtils.isNotEmpty(a.getOrgId())){
                if(CollectionUtils.isNotEmpty(contractEntityList)){
                    ContractEntity contractEntity = contractEntityList.stream().filter(contract -> Objects.equals(contract.getContractCode(), a.getContractCode()) &&
                            Objects.equals(contract.getOrgId(), a.getOrgId())).findAny().orElse(null);
                    //log.info("====>>setOrgIdByContractCode==>>02==>>a:{},contractEntity:{}",a,contractEntity);
                    if(ObjectUtil.isNotEmpty(contractEntity)){
                        // modify by zhangli.chen 针对导入模板中有承租人数据，已导入模板数据为准，只有导入承租人为空的情况下才从月表取，避免编码和名称不一致情况 on 20251020
//                        if(StringUtils.isEmpty(a.getClientCode())){
//                            a.setClientCode(contractEntity.getClientCode());
//                        }
//                        if(StringUtils.isEmpty(a.getClientName())){
//                            a.setClientName(contractEntity.getClientName());
//                        }
                       if(StringUtils.isEmpty(a.getClientName())){
                           a.setClientCode(contractEntity.getClientCode());
                           a.setClientName(contractEntity.getClientName());
                        }
                        // 财务合同状态
                        a.setFinancialContractStatus(contractEntity.getFinancialContractStatus());
                    }
                }
            }else{
                List<ContractEntity> contractEntities = contractEntityList.stream().filter(o -> ObjectUtil.equals(a.getContractCode(), o.getContractCode())).collect(Collectors.toList());
                String orgId = "";
                // 判断财务中台合同表中必须要有本次导入合同数据
                if (CollectionUtils.isEmpty(contractEntities) && !YesOrNoEnum.YES.getCode().equals(a.getDelFlag())) {
                    throw new ServiceException("未查询到合同[" + a.getContractCode() + "]");
                } else if (contractEntities.size() > 1) {
                    // 查合同表若合同表签约主体存在多条，取主合同编号为空的 或者主合同编号=合同编号；
                    ContractEntity contractEntity = contractEntities.stream().filter(o -> ObjectUtil.isEmpty(o.getContractCodeM())).findFirst().orElse(null);
                    if(ObjectUtil.isEmpty(contractEntity)){
                        contractEntity = contractEntities.stream().filter(o -> ObjectUtil.equals(o.getContractCodeM(), o.getContractCode())).findFirst().orElse(null);
                    }
                    if (ObjectUtil.isEmpty(contractEntity)) {
                        throw new ServiceException("合同[" + a.getContractCode() + "]存在多条合同数据，但是没有主合同编号为空的合同");
                    }
                    orgId = contractEntity.getOrgId();
                    //log.info("====>>setOrgIdByContractCode==>>03==>>a.getClientCode():{},contractEntity.getClientCode():{}",a.getClientCode(),contractEntity.getClientCode());
                    // modify by zhangli.chen 针对导入模板中有承租人数据，已导入模板数据为准，避免编码和名称不一致情况 on 20251020
//                    if(StringUtils.isEmpty(a.getClientCode())){
//                        a.setClientCode(contractEntity.getClientCode());
//                    }
//                    if(StringUtils.isEmpty(a.getClientName())){
//                        a.setClientName(contractEntity.getClientName());
//                    }
                    if(StringUtils.isEmpty(a.getClientName())){
                        a.setClientCode(contractEntity.getClientCode());
                        a.setClientName(contractEntity.getClientName());
                    }
                    // 财务合同状态
                    a.setFinancialContractStatus(contractEntity.getFinancialContractStatus());
                } else if(CollectionUtils.isNotEmpty(contractEntities)) {
                    orgId = contractEntities.get(0).getOrgId();
                    //log.info("====>>setOrgIdByContractCode==>>04==>>a.getClientCode():{},contractEntities.get(0).getClientCode():{}",contractEntities.get(0).getClientCode());
                    // modify by zhangli.chen 针对导入模板中有承租人数据，已导入模板数据为准，避免编码和名称不一致情况 on 20251020
//                    if(StringUtils.isEmpty(a.getClientCode())){
//                        a.setClientCode(contractEntities.get(0).getClientCode());
//                    }
//                    if(StringUtils.isEmpty(a.getClientName())){
//                        a.setClientName(contractEntities.get(0).getClientName());
//                    }
                    if(StringUtils.isEmpty(a.getClientName())){
                        a.setClientCode(contractEntities.get(0).getClientCode());
                        a.setClientName(contractEntities.get(0).getClientName());
                    }
                    // 财务合同状态
                    a.setFinancialContractStatus(contractEntities.get(0).getFinancialContractStatus());
                }
                if (ObjectUtil.isEmpty(orgId) && !YesOrNoEnum.YES.getCode().equals(a.getDelFlag())) {
                    throw new ServiceException("根据合同[" + a.getContractCode() + "]未查询到对应的签约主体");
                }
                //log.info("====>>setOrgIdByContractCode==>>05==>>a.getClientCode():{}",a.getClientCode());
                a.setOrgId(orgId);
            }
        });
        log.info("====>>setOrgIdByContractCode==>>100==>>contractEntityList:{}",contractEntityList);
    }


    /**
     * @description: 更新合同最新的签约主体
     * @author: zhangli.chen
     **/
    private ImpairmentProvisionDetailEntity setOrgIdByContractCode(ImpairmentProvisionDetailEntity detailEntity) {
        if(detailEntity==null){
            return null;
        }
        // 查询合同表，查询出本次导入合同的所有合同信息
//        List<ContractEntity> contractEntityList = iContractService.list(new LambdaQueryWrapper<ContractEntity>()
//                .select(ContractEntity::getContractCode, ContractEntity::getOrgId, ContractEntity::getContractCodeM
//                        ,ContractEntity::getClientCode,ContractEntity::getClientName,ContractEntity::getFinancialContractStatus).eq(ContractEntity::getContractCode, detailEntity.getContractCode()));

        List<ContractMonthEntity> contractMonthEntityList =  contractMonthService.list(new LambdaQueryWrapper<ContractMonthEntity>()
                .select(ContractMonthEntity::getContractCode, ContractMonthEntity::getOrgId, ContractMonthEntity::getContractCodeM
                        ,ContractMonthEntity::getClientCode,ContractMonthEntity::getClientName,ContractMonthEntity::getFinancialContractStatus).eq(ContractMonthEntity::getContractCode, detailEntity.getContractCode()));
        List<ContractEntity> contractEntityList = BeanUtil.copyToList(contractMonthEntityList, ContractEntity.class);

        if (contractEntityList!=null && contractEntityList.size()>0){
            // 查合同表若合同表签约主体存在多条，取主合同编号为空的 或者主合同编号=合同编号；
            ContractEntity contractEntity = contractEntityList.stream().filter(o -> ObjectUtil.isEmpty(o.getContractCodeM())).findFirst().orElse(null);
            if(ObjectUtil.isEmpty(contractEntity)){
                contractEntity = contractEntityList.stream().filter(o -> ObjectUtil.equals(o.getContractCodeM(), o.getContractCode())).findFirst().orElse(null);
            }
            if (ObjectUtil.isNotEmpty(contractEntity)) {
                detailEntity.setOrgId(contractEntity.getOrgId());
                detailEntity.setClientCode(contractEntity.getClientCode());
                detailEntity.setClientName(contractEntity.getClientName());
            }
        }
        return detailEntity;
    }

    /**
     * 签约主体简称映射关系转换
     *
     * @param orgIdShort
     * @return
     */
    private String mapOrgIdName(String orgIdShort) {
        String orgIdName = "";
        // 1.恒信=海通恒信国际融资租赁股份有限公司；
        // 2.恒运=海通恒信国际融资租赁（天津）有限公司；
        // 3.自贸区=海通恒运融资租赁（上海）有限公司；
        // 4.金融集团=海通恒信金融集团有限公司本部 ；
        if (ObjectUtil.equals(orgIdShort, "恒信")) {
            orgIdName = "海通恒信国际融资租赁股份有限公司";
        } else if (ObjectUtil.equals(orgIdShort, "恒运")) {
            orgIdName = "海通恒信国际融资租赁（天津）有限公司";
        } else if (ObjectUtil.equals(orgIdShort, "自贸区")) {
            orgIdName = "海通恒运融资租赁（上海）有限公司";
        } else if (ObjectUtil.equals(orgIdShort, "金融集团")) {
            orgIdName = "海通恒信金融集团有限公司本部";
        } else {
            throw new ServiceException("根据签约主体简称[" + orgIdShort + "]未匹配到对应签约主体名称");
        }
        return orgIdName;
    }

    /**
     * 转换签约主体
     * 输入的是名称，需要转为orgId
     *
     * @param detailEntityList
     */
    private void transformOrgId(List<ImpairmentProvisionDetailEntity> detailEntityList) {
        // 签约主体
        Map<String, String> companyMap = iOrgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgName(), e -> e.getOrgId(), (a, b) -> b));
        log.info("==>>transformOrgId==>>00==>>companyMap:{}",companyMap);
        detailEntityList.stream().forEach(a -> {
            if(!YesOrNoEnum.YES.getCode().equals(a.getDelFlag())){
                String orgId = companyMap.get(a.getOrgId());
                if (ObjectUtil.isEmpty(orgId)) {
                    //log.info("==>>transformOrgId==>>01==>>a.getContractCode():{},a.getOrgId():{}",a.getContractCode(),a.getOrgId());
                    throw new ServiceException("根据合同编号["+a.getContractCode()+"]查询出来的签约主体名称[" + a.getOrgId() + "]未查询到对应的签约主体！");
                }
                a.setOrgId(orgId);
            }
        });
    }

    /**
     * 校验合同编号-校验合同编号在财务中台系统中是否存在，支持导入文件中部分有合同部分无合同情况
     * @param detailEntityList
     */
    private void checkContractCode(List<ImpairmentProvisionDetailEntity> detailEntityList) {
        List<String> contractCodeList = detailEntityList.stream()
                .map(ImpairmentProvisionDetailEntity::getContractCode)
                .filter(code -> code != null && !code.isEmpty())
                .collect(Collectors.toList());
        // 若导入文件合同全部为空
        if(CollectionUtil.isNotEmpty(contractCodeList)){
//            List<ContractEntity> contractEntityList = iContractService.list(
//                    new LambdaQueryWrapper<ContractEntity>().in(ContractEntity::getContractCode, contractCodeList));
            List<ContractMonthEntity> contractMonthEntityList = contractMonthService.list(
                    new LambdaQueryWrapper<ContractMonthEntity>().in(ContractMonthEntity::getContractCode, contractCodeList));
            List<ContractEntity> contractEntityList  = BeanUtil.copyToList(contractMonthEntityList, ContractEntity.class);

            List<String> notExistContractCodeList=Lists.newArrayList();
            detailEntityList.stream().forEach(a -> {
                // 支持导入行存在合同号的存在性校验
                if(StringUtils.isNotEmpty(a.getContractCode())){
                    List<ContractEntity> contractEntities = contractEntityList.stream().filter(o -> ObjectUtil.equals(a.getContractCode(), o.getContractCode())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(contractEntities) && !YesOrNoEnum.YES.getCode().equals(a.getDelFlag())) {
                        notExistContractCodeList.add(a.getContractCode());
                    }
                }
            });
            if(ObjectUtil.isNotEmpty(notExistContractCodeList)){
                String s = notExistContractCodeList.stream().collect(Collectors.joining(","));
                throw new ServiceException("未查询到合同编号："+ s);
            }
        }
    }


    /**
     * 保存数据-若已有导入未提交数据
     * @param excelType
     * @param detailEntityList
     * @param impairmentType
     */
    private void saveData(String excelType, List<ImpairmentProvisionDetailEntity> detailEntityList
            , String impairmentType, boolean isExtends) {
        log.info("====>>ImpairmentProvisionServiceImpl.saveData==>>00==>>excelType:{}," +
                "detailEntityList.size():{},impairmentType:{},isExtends:{}",excelType,detailEntityList.size(),impairmentType,isExtends);
        // 校验减值计提的状态-校验当前月份对应减值类型的数据，只有（2-已提交）+（已冲销+并且状态为1-已录入/5-已拒绝）的数据处理完后才能允许提交
        checkStatus(impairmentType);
        // 首先看当前账期的数据有没有-获取金蝶所有当前会计期间
        // 当前账期
        Integer currentPeriodCode = null;
        // 当前账期第一天
        LocalDate currentPeriodCodeFirstDay = null;
        Map<String, Integer> periodCodeMap = new HashMap<>();
        List<Map<String, String>> periodCodeList = new ArrayList<>();
        log.info("====>>ImpairmentProvisionServiceImpl.saveData==>>01==>>periodCodeMap:{}",periodCodeMap);
        try {
            periodCodeMap = getPeriodFromKingdee();
            log.info("====>>ImpairmentProvisionServiceImpl.saveData==>>02==>>periodCodeMap:{}",periodCodeMap);
            for(ImpairmentProvisionDetailEntity detailEntity : detailEntityList){
                //log.info("====>>ImpairmentProvisionServiceImpl.saveData==>>02==>>detailEntity.getOrgId():{}",detailEntity.getOrgId());
                if(CollectionUtil.isNotEmpty(periodCodeMap) && periodCodeMap.containsKey(detailEntity.getOrgId())){
                    currentPeriodCode =  periodCodeMap.get(detailEntity.getOrgId());
                    break;
                }
            }
        }catch (Exception e){
            throw new ServiceException("获取金蝶账期失败：[" + periodCodeList + "]");
        }
        log.info("====>>ImpairmentProvisionServiceImpl.saveData==>>03==>>currentPeriodCode:{}",currentPeriodCode);
        if(currentPeriodCode==null){
            throw new ServiceException("导入文件中的合同其归属主体查询金蝶不存在账期！");
        }else{
            currentPeriodCodeFirstDay = PeriodCodeUtil.parseFirstDayOfMonth(currentPeriodCode);
        }
        log.info("====>>ImpairmentProvisionServiceImpl.saveData==>>04==>>currentPeriodCodeFirstDay:{}",currentPeriodCodeFirstDay);
        // 1.获取最新未提交的汇总数据 状态为 1-已录入或者5-已拒绝 减值类型+日期+状态 合并同一个减值类型多个上传文件至同一个头表数据-合并当前账期的还未提交数据
        ImpairmentProvisionEntity impairmentProvisionEntity = getOne(new LambdaQueryWrapper<ImpairmentProvisionEntity>()
                .eq(ImpairmentProvisionEntity::getImpairmentType, impairmentType)
                .ge(ImpairmentProvisionEntity::getCreateTime, currentPeriodCodeFirstDay.atStartOfDay())
                .in(ImpairmentProvisionEntity::getProcessStatus, Lists.newArrayList(ProcessStatusEnum.ENTERED.getCode(), ProcessStatusEnum.REJECTED.getCode())));
        if (ObjectUtil.isEmpty(impairmentProvisionEntity)) {
            impairmentProvisionEntity = new ImpairmentProvisionEntity();
            impairmentProvisionEntity.setImpairmentType(impairmentType);
            save(impairmentProvisionEntity);
        }
        // 减值计提汇总id
        Long impairmentProvisionId = impairmentProvisionEntity.getId();
        // 2.获取上一个账期的数据-导入明细数据：通过文件类型+上个月账期来筛选明细表的所有历史导入数据
        String lastPeriodCode = String.valueOf(PeriodCodeUtil.getLastMonthPeriodCode(currentPeriodCode));
        log.info("====>>ImpairmentProvisionServiceImpl.saveData==>>05==>>impairmentProvisionId:{},lastPeriodCode:{}",impairmentProvisionId,lastPeriodCode);
        List<ImpairmentProvisionDetailEntity> lastPeriodCodeEntityList = impairmentProvisionMapper.getLastMonthByPeriodCode(excelType,lastPeriodCode);
        // 3.筛选本次导入中已结束的合同
        // 过滤生成本次导入的合同列表
        List<String> contractCodeList = detailEntityList.stream().map(ImpairmentProvisionDetailEntity::getContractCode).distinct().collect(Collectors.toList());
        // 查询合同表，查询出本次导入合同的所有财务合同状态已结束合同信息
        final List<String> finishContractCodeList ;
        // modify by zhangli.chen 针对不计算当月计提金额的合同结束状态切换到参数配置功能 on 20250711
        List<SysDictData> dictDataList =
                hthxCommonService.getDictTypeDataByRealTime(DictTypeEnum.JZJT_CONTRACT_FINISH_STATUS.getCode(),null);
        List<String> jzjtContractFinishStatus = new ArrayList<>();
        if(dictDataList!=null && !dictDataList.isEmpty()){
            jzjtContractFinishStatus =  dictDataList.stream().map(SysDictData::getDictValue).distinct().collect(Collectors.toList());
        }
        log.info("====>>ImpairmentProvisionServiceImpl.saveData==>>06==>>jzjtContractFinishStatus:{}",jzjtContractFinishStatus);
        if(StringUtils.isNotEmpty(contractCodeList)){
//            List<ContractEntity> finishContractEntityList = iContractService.list(new LambdaQueryWrapper<ContractEntity>()
//                    .select(ContractEntity::getContractCode, ContractEntity::getOrgId, ContractEntity::getFinancialContractStatus)
//                    .in(ContractEntity::getContractCode, contractCodeList)
//                    .in(ContractEntity::getFinancialContractStatus, FinanceEngineEnum.FinishFinancialContractStatus.getAllColumnName()));
            List<ContractMonthEntity> contractMonthEntityList =  contractMonthService.list(new LambdaQueryWrapper<ContractMonthEntity>()
                    .select(ContractMonthEntity::getContractCode, ContractMonthEntity::getOrgId, ContractMonthEntity::getFinancialContractStatus)
                    .in(ContractMonthEntity::getContractCode, contractCodeList)
                    .in(ContractMonthEntity::getFinancialContractStatus, jzjtContractFinishStatus));
            List<ContractEntity> finishContractEntityList = BeanUtil.copyToList(contractMonthEntityList, ContractEntity.class);
            if (CollectionUtils.isNotEmpty(finishContractEntityList)) {
                finishContractCodeList = finishContractEntityList.stream().map(ContractEntity::getContractCode).collect(Collectors.toList());
            }else {
                finishContractCodeList = new ArrayList<>();
            }
        }else {
            finishContractCodeList = new ArrayList<>();
        }
        // 记录上个月有，本月导入已有，发生资产处置结束（内部转让）的合同-这部分合同其上月余额=上个月的拨备合计
        //List<String> innerTransferContractList = new ArrayList<>();
        log.info("====>>ImpairmentProvisionServiceImpl.saveData==>>07==>>finishContractCodeList:{}",finishContractCodeList);
        // 4.循环导入本次导入明细，设置 本月计提 等字段值
        detailEntityList.stream().forEach(a -> {
            try {
                // 本次新增插入
                a.setHistoryFlag(FinanceEngineEnum.ValidFlag.NO.getKey());
                // 获取上月余额 根据合同+签约主体+客户+减值类型查询-拨备合计汇总-按时间倒排序获取第一条数据
                Optional<ImpairmentProvisionDetailEntity> firstEntity = lastPeriodCodeEntityList.stream()
                        .filter(v -> ObjectUtil.equals(a.getContractCode(), v.getContractCode())
                                && ObjectUtil.equals(a.getOrgId(), v.getOrgId())
                                && ObjectUtil.equals(a.getImpairmentType(), v.getImpairmentType()))
                        .sorted(Comparator.comparing(ImpairmentProvisionDetailEntity::getCreateTime).reversed())
                        .findFirst();
                ImpairmentProvisionDetailEntity lastPeriodCodeContract = firstEntity.orElse(null);
                if (lastPeriodCodeContract!=null){
                    // 上月余额--上个账期该合同的拨备合计
                    a.setLastMonthBalance(lastPeriodCodeContract.getProvisionTotal());
                }else{
                    // 处理跨月变更主体情况
                    Optional<ImpairmentProvisionDetailEntity> lastContractEntity = lastPeriodCodeEntityList.stream()
                            .filter(v -> ObjectUtil.equals(a.getContractCode(), v.getContractCode())
                                    && ObjectUtil.equals(a.getImpairmentType(), v.getImpairmentType()))
                            .sorted(Comparator.comparing(ImpairmentProvisionDetailEntity::getCreateTime).reversed())
                            .findFirst();
                    ImpairmentProvisionDetailEntity lastContract = lastContractEntity.orElse(null);
                    // 针对已资产处置结束（内部转让），判断本次导入是否已变更主体--主体已确认不同
                    if(lastContract!=null){
                        // 跨月-资产处置结束（内部转让）-其上月余额=上个月的拨备合计
                        if(FinanceEngineEnum.InnerFinishFinancialContractStatus.CZJS_NBZR.getColumnName()
                                .equals(lastContract.getFinancialContractStatus())){
                            // 合同跨月发生主体变更，并且上个月该合同对应主体下的财务合同状态为资产处置结束（内部转让）
                            a.setImportFlag(FinanceEngineEnum.Numbers.SEVEN.getValue());
                        }
                        // 跨月主体发生变更，新主体：拨备合计=导入值 上月余额=0 本月计提 = 拨备合计-上月余额
                        a.setLastMonthBalance(BigDecimal.ZERO);
                    }else{
                        // 新增合同上月余额=0 拨备合计=本月计提
                        a.setLastMonthBalance(BigDecimal.ZERO);
                    }
                }
                // 剔除已结束合同
                if(finishContractCodeList!=null && finishContractCodeList.contains(a.getContractCode())){
                    if (!ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_11.getCode())
                            && !ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_12.getCode())
                            && !ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_13.getCode())){
                        a.setThisMonthProvision(BigDecimal.ZERO);
                        a.setProvisionTotal(BigDecimal.ZERO);
                    }
                    a.setImportFlag(FinanceEngineEnum.Numbers.ONE.getValue());
                }else if (ObjectUtil.equals(a.getIsVerification(), "是")) {
                    // 本月计提
                    a.setThisMonthProvision(BigDecimal.ZERO);
                    a.setImportFlag(FinanceEngineEnum.Numbers.TWO.getValue());
                    // 附件11-13 在上传时就剔除掉已出库的数据（是否出库=是）
                    if (ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_11.getCode())
                            || ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_12.getCode())
                            || ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_13.getCode())){
                        a.setDelFlag(YesOrNoEnum.YES.getCode());
                    }
                }else{
                    // 附件11-13 本月计提全部以导入文件中的本月减值列为准
                    if (!ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_11.getCode())
                            && !ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_12.getCode())
                            && !ObjectUtil.equals(excelType, ImpairmentExcelTypeEnum.EXCEL_TYPE_13.getCode())){
                        // 若导入文件存在本月计提数额，则以导入文件中的为准
                        if(a.getThisMonthProvision() == null || BigDecimal.ZERO.compareTo(a.getThisMonthProvision())==0){
                            // 本月计提 = 拨备合计-上月余额
                            a.setThisMonthProvision(NumberUtil.sub(a.getProvisionTotal(), a.getLastMonthBalance()));
                        }
                    }
                }
                // 汇率
                if(ObjectUtil.isNotEmpty(a.getExchangeRate())){
                    // 目标币种本月计提金额 = 本月计提 * 汇率
                    a.setTargetAmount(NumberUtil.mul(a.getThisMonthProvision(), a.getExchangeRate()));
                }
                // 设置减值计提id
                a.setImpairmentProvisionId(impairmentProvisionId);
            }catch (Exception e){
                throw new ServiceException("合同[" + a.getContractCode() + "]处理异常，请检查导入文件中该笔数据是否正常！");
            }
         });
        // 5.针对上个账期的数据，如果在本月导入清单中没有的话，那么需要新增到本月数据中
        // 本次导入的明细数据分组
        List<String> thisMonthKey = detailEntityList.stream().map(a -> a.getContractCode()
                + FinanceEngineEnum.Symbol.UNDERLINE.getValue() + a.getOrgId() + FinanceEngineEnum.Symbol.UNDERLINE.getValue()
                + a.getImpairmentType()).collect(Collectors.toList());
        final List<String> lastFinishContractCodeList ;
        if(CollectionUtils.isNotEmpty(lastPeriodCodeEntityList)){
            // 查询历史合同工状态
            List<String> lastContractCodeList = lastPeriodCodeEntityList.stream().map(ImpairmentProvisionDetailEntity::getContractCode).distinct().collect(Collectors.toList());
            // 查询合同表，查询出本次导入合同的所有财务合同状态已结束合同信息
            if(CollectionUtils.isNotEmpty(lastContractCodeList)){
//                List<ContractEntity> lastFinishContractEntityList = iContractService.list(new LambdaQueryWrapper<ContractEntity>()
//                        .select(ContractEntity::getContractCode, ContractEntity::getOrgId, ContractEntity::getFinancialContractStatus)
//                        .in(ContractEntity::getContractCode, lastContractCodeList)
//                        .in(ContractEntity::getFinancialContractStatus, FinanceEngineEnum.FinishFinancialContractStatus.getAllColumnName()));
                List<ContractMonthEntity> contractMonthEntityList =  contractMonthService.list(new LambdaQueryWrapper<ContractMonthEntity>()
                        .select(ContractMonthEntity::getContractCode, ContractMonthEntity::getOrgId, ContractMonthEntity::getFinancialContractStatus)
                        .in(ContractMonthEntity::getContractCode, lastContractCodeList)
                        .in(ContractMonthEntity::getFinancialContractStatus, jzjtContractFinishStatus));
                List<ContractEntity> lastFinishContractEntityList = BeanUtil.copyToList(contractMonthEntityList, ContractEntity.class);
                if (CollectionUtils.isNotEmpty(lastFinishContractEntityList)) {
                    lastFinishContractCodeList = lastFinishContractEntityList.stream().map(ContractEntity::getContractCode).collect(Collectors.toList());
                }else{
                    lastFinishContractCodeList = new ArrayList<>();
                }
            }else{
                lastFinishContractCodeList = new ArrayList<>();
            }
        }else{
            lastFinishContractCodeList = new ArrayList<>();
        }
        // 循环上个月数据-本月未上传，也要展示上月的数据，本月金额设为0，上月余额取上月的值
        // modify by zhangli.chen for 根据导入模板确定是否继承上个月数据 on 20250711
        if(isExtends){
            log.info("====>>ImpairmentProvisionServiceImpl.saveData==>>08==>>isExtends:{},lastPeriodCodeEntityList.size():{}",isExtends,(lastPeriodCodeEntityList!=null ? lastPeriodCodeEntityList.size():0));
            lastPeriodCodeEntityList.stream().forEach(a -> {
                try {
                    // 合同编号-签约主体-客户编码-减值类型
                    String key = a.getContractCode() + FinanceEngineEnum.Symbol.UNDERLINE.getValue() + a.getOrgId()
                            + FinanceEngineEnum.Symbol.UNDERLINE.getValue() + a.getImpairmentType();
                    // 上月的数据在本月不存在，则自动添加
                    if (!thisMonthKey.contains(key)) {
//                // 判断本月是否也有该合同导入
//                Optional<ImpairmentProvisionDetailEntity> thisMonthContractEntity = detailEntityList.stream()
//                        .filter(v -> ObjectUtil.equals(a.getContractCode(), v.getContractCode())
//                                && ObjectUtil.equals(a.getImpairmentType(), v.getImpairmentType()))
//                        .sorted(Comparator.comparing(ImpairmentProvisionDetailEntity::getCreateTime).reversed())
//                        .findFirst();
//                ImpairmentProvisionDetailEntity thisMonthContract = thisMonthContractEntity.orElse(null);
                        // 针对上个月有，而这个月没有，跨月资产转让数据--加上该提交主要是是防止按合同号维度本月是有导入的，那么此时就要看是否发生了资产转让
                        // 第一种情况：上月和本月都有导入，只是签约主体不一样了，跨月主体发生变更，旧主体： 拨备合计=0 上月余额=上个月的拨备合计 本月计提=拨备合计-上月余额 为负数
                        ImpairmentProvisionDetailEntity detailEntity = BeanUtil.copyProperties(a, ImpairmentProvisionDetailEntity.class, GenConstants.BASE_ENTITY);
                        detailEntity.setId(null);
                        detailEntity.setVoucherId(null);
                        detailEntity.setAccountDate(null);
                        detailEntity.setErrorInfo(null);
                        detailEntity.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
                        detailEntity.setUpdateTime(LocalDateTime.now());
                        //五级分类-对于上月有本月没有的合同 它的五级分类三阶段在本月置为空
                        detailEntity.setFiveClass(null);
                        //三阶段
                        detailEntity.setThreeStep(null);
                        // 风险敞口
                        detailEntity.setRiskExposure(BigDecimal.ZERO);
                        // 拨备合计：本月导入清单已不存在合同-拨备合计为0
                        detailEntity.setProvisionTotal(BigDecimal.ZERO);
                        // 上月余额：上月余额=上个月的拨备合计本月导入清单已不存在合同-上月余额取上月
                        detailEntity.setLastMonthBalance(a.getProvisionTotal());
                        // 剔除已结束合同
                        if(lastFinishContractCodeList!=null && lastFinishContractCodeList.contains(a.getContractCode())){
                            detailEntity.setThisMonthProvision(BigDecimal.ZERO);
                            detailEntity.setProvisionTotal(BigDecimal.ZERO);
                            detailEntity.setImportFlag(FinanceEngineEnum.Numbers.ONE.getValue());
                        }else{
                            // 本月计提 = 拨备合计-上月余额
                            detailEntity.setThisMonthProvision(NumberUtil.sub(detailEntity.getProvisionTotal(), detailEntity.getLastMonthBalance()));
                        }
                        // 减值计提id
                        detailEntity.setImpairmentProvisionId(impairmentProvisionId);
                        // 作为历史数据插入
                        detailEntity.setHistoryFlag(FinanceEngineEnum.ValidFlag.YES.getKey());
                        // 以最新的主体来进行更新
                        //setOrgIdByContractCode(detailEntity);
                        // 将历史合同新增到本次需要导入的合同中
                        detailEntityList.add(detailEntity);
                    }
                }catch (Exception e){
                    throw new ServiceException("上月合同[" + a.getContractCode() + "]处理异常，请检查！");
                }
            });
        }

        // 6.先删除本月明细数据--举个例子，文件1和文件2都是导入的租赁资产类型，那么在导入文件1成功后，再导入文件2，那么只会重置文件2的导入内容
        iImpairmentProvisionDetailService.remove(new LambdaQueryWrapper<ImpairmentProvisionDetailEntity>()
                .eq(ImpairmentProvisionDetailEntity::getExcelType, excelType)
                .eq(ImpairmentProvisionDetailEntity::getImpairmentProvisionId, impairmentProvisionId)
        );
        // 7.保存明细数据
        iImpairmentProvisionDetailService.saveBatch(detailEntityList);
        // 根据减值类型获取本月数据
        List<ImpairmentProvisionDetailEntity> impairmentList = iImpairmentProvisionDetailService.list(new LambdaQueryWrapper<ImpairmentProvisionDetailEntity>()
                .eq(ImpairmentProvisionDetailEntity::getImpairmentProvisionId, impairmentProvisionId)
                .eq(ImpairmentProvisionDetailEntity::getImpairmentType, impairmentType)
                .eq(ImpairmentProvisionDetailEntity::getDelFlag,YesOrNoEnum.NO.getCode())
                .ge(ImpairmentProvisionDetailEntity::getCreateTime, currentPeriodCodeFirstDay.atStartOfDay()));
        // 拨备合计
        impairmentProvisionEntity.setProvisionTotal(impairmentList.stream().filter(
                o -> ObjectUtil.isNotEmpty(o.getProvisionTotal())).map(ImpairmentProvisionDetailEntity::getProvisionTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 上月余额
        impairmentProvisionEntity.setLastMonthBalance(impairmentList.stream().filter(
                o -> ObjectUtil.isNotEmpty(o.getLastMonthBalance())).map(ImpairmentProvisionDetailEntity::getLastMonthBalance).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 本月计提
        impairmentProvisionEntity.setThisMonthProvision(impairmentList.stream().filter(
                o -> ObjectUtil.isNotEmpty(o.getThisMonthProvision())).map(ImpairmentProvisionDetailEntity::getThisMonthProvision).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 更新时间
        impairmentProvisionEntity.setUpdateTime(LocalDateTime.now());
        //上传后，凭证需要重新生成
        impairmentProvisionEntity.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
        // 凭证id,多个按照逗号分隔
        impairmentProvisionEntity.setVoucherId(null);
        // 5.保存汇总头信息
        saveOrUpdate(impairmentProvisionEntity);
        log.info("====>>ImpairmentProvisionServiceImpl.saveData==>>100==>>");
    }

    /**
     * @description: 校验当前月份对应减值类型的数据，只有（2-已提交）+（已冲销+并且状态为1-已录入/5-已拒绝）的数据处理完后才能允许提交
     **/
    private void checkStatus(String impairmentType) {
        // 校验减值类型是否审批，存在已提交的 则不能上传
        List<ImpairmentProvisionEntity> list = list(new LambdaQueryWrapper<ImpairmentProvisionEntity>()
                .eq(ImpairmentProvisionEntity::getImpairmentType, impairmentType)
                .ge(ImpairmentProvisionEntity::getCreateTime, getFirstDayOfMonth()));
        for (ImpairmentProvisionEntity entity : list) {
            if (ObjectUtil.equals(entity.getProcessStatus(), ProcessStatusEnum.SUBMITTED.getCode())) {
                throw new ServiceException("减值类型[" + impairmentType + "] 本月存在未审批的数据，请审批后再上传");
            }
            // 冲销单 必须提交、审批通过
            if (ObjectUtil.isNotEmpty(entity.getWriteOffOriginalId()) &&
                    (ObjectUtil.equals(entity.getProcessStatus(), ProcessStatusEnum.ENTERED.getCode()) || ObjectUtil.equals(entity.getProcessStatus(), ProcessStatusEnum.REJECTED.getCode()))) {
                throw new ServiceException("减值类型[" + impairmentType + "] 本月存在未提交的冲销数据，请提交审批后再上传");
            }
        }
    }

    /**
     * 获取本月第一天的时间
     *
     * @return
     */
    private LocalDateTime getFirstDayOfMonth() {
        LocalDateTime firstDayOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        return firstDayOfMonth;
    }

    /**
     * 获取上传excel列表
     *
     * @return
     */
    @Override
    public List<ImpairmentProvisionExcelTypeVO> getUploadExcelList() {
        List<ImpairmentProvisionExcelTypeVO> list = Lists.newArrayList();
        for (ImpairmentExcelTypeEnum excelTypeEnum : ImpairmentExcelTypeEnum.values()) {
            ImpairmentProvisionExcelTypeVO vo = new ImpairmentProvisionExcelTypeVO();
            vo.setExcelType(excelTypeEnum.getCode());
            vo.setName(excelTypeEnum.getDesc());
            list.add(vo);
        }
        return list;
    }

    /**
     * @description:减值计提-首页列表-查看本月减值报告按钮-查看本月减值报告
     **/
    @Override
    public List<ImpairmentProvisionDetailReportVO> getImpairmentReport() {
        // 返回列表
        List<ImpairmentProvisionDetailReportVO> reportVOList = Lists.newArrayList();
        // 当前账期
        Integer currentPeriodCode = null;
        // 当前账期第一天
        LocalDate currentPeriodCodeFirstDay = null;
//        R<List<Map<String, String>>> periodCodeAll = remoteKingdeeEasService.getCurrentPeriodCodeAll();
//        Map<String, Integer> periodCodeMap = new HashMap<>();
//        for (Map<String, String> map : periodCodeAll.getData()) {
//            periodCodeMap.put(String.valueOf(map.get("ORGID")), Integer.valueOf(map.get("PERIODCODE")));
//        }
        Map<String, Integer> periodCodeMap = getPeriodFromKingdee();
        if(periodCodeMap!=null && periodCodeMap.size()>0){
            // 倒序排序并获取最大值对应的entry-如果map为空，返回null，否则返回entry对象
            Map.Entry<String, Integer> firstEntry = periodCodeMap.entrySet().stream()
                    .max(Comparator.comparingInt(Map.Entry::getValue))
                    .orElse(null);
            if (firstEntry != null) {
                log.info("====>>getImpairmentReport==>>00==>>Key: " + firstEntry.getKey() + ", Value: " + firstEntry.getValue());
                currentPeriodCode = firstEntry.getValue();
            }
        }
        if(currentPeriodCode==null){
            currentPeriodCode = PeriodCodeUtil.periodCodeByDate(new Date());
        }
        currentPeriodCodeFirstDay = PeriodCodeUtil.parseFirstDayOfMonth(currentPeriodCode);
        String periodCode = String.valueOf(currentPeriodCode);
        String firstDay = HthxDateUtils.dateToShortStr(currentPeriodCodeFirstDay);
        log.info("====>>getImpairmentReport==>>01==>>periodCode:{},firstDay:{}",periodCode,firstDay);
        List<ImpairmentProvisionDetailVO> list = iImpairmentProvisionDetailService.getThisMonthDataList(periodCode,firstDay);
        // 按减值类型分组-减值导入明细
        Map<String, List<ImpairmentProvisionDetailVO>> listMap = list.stream().collect(Collectors.groupingBy(ImpairmentProvisionDetailVO::getImpairmentType));
        // 本月转出-按减值类型分组-拨备金额汇总值--应收租赁款组合拨备 1231.03.01
        Map<String, BigDecimal> transferOutMap = iImpairmentProvisionDetailService.getTransferOut(periodCode,firstDay);
        // 1.租赁资产
        generateRentProperty(transferOutMap, listMap, reportVOList);
        // 2.应收经营租赁
        generateOtherType(ImpairmentTypeEnum.TYPE_ENUM_2.getCode(), list, reportVOList, transferOutMap);
        // 3.其他应收款
        //generateOtherType(ImpairmentTypeEnum.TYPE_ENUM_3.getCode(), list, reportVOList, transferOutMap);
        // 4.其他应收款项
        generateOtherType(ImpairmentTypeEnum.TYPE_ENUM_4.getCode(), list, reportVOList, transferOutMap);
        // 5.长期应收款
        generateOtherType(ImpairmentTypeEnum.TYPE_ENUM_5.getCode(), list, reportVOList, transferOutMap);
        // 6.应收投资性房地产
        generateOtherType(ImpairmentTypeEnum.TYPE_ENUM_6.getCode(), list, reportVOList, transferOutMap);
        // 7.库存减值
        generateOtherType(ImpairmentTypeEnum.TYPE_ENUM_7.getCode(), list, reportVOList, transferOutMap);
        // 8.应收关联方租赁款
        //generateOtherType(ImpairmentTypeEnum.TYPE_ENUM_8.getCode(), list, reportVOList, transferOutMap);
        // 9.其他金融资产 todo 分类
        //generateOtherType(ImpairmentTypeEnum.TYPE_ENUM_9.getCode(), list, reportVOList, transferOutMap);
        return reportVOList;
    }

    /**
     * 封装其他减值类型
     *
     * @param code
     * @param list
     * @param reportVOList
     * @param transferOutMap
     */
    private void generateOtherType(String code, List<ImpairmentProvisionDetailVO> list, List<ImpairmentProvisionDetailReportVO> reportVOList, Map<String, BigDecimal> transferOutMap) {
        ImpairmentProvisionDetailReportVO reportVO = new ImpairmentProvisionDetailReportVO();
        reportVO.setImpairmentType(code);
        reportVO.setProvisionTotal(NumberUtil.toBigDecimal(list.stream().filter(a -> ObjectUtil.equals(a.getImpairmentType(), code))
                .map(ImpairmentProvisionDetailVO::getProvisionTotal).reduce(BigDecimal.ZERO, BigDecimal::add)));
        reportVO.setLastMonthBalance(NumberUtil.toBigDecimal(list.stream().filter(a -> ObjectUtil.equals(a.getImpairmentType(), code))
                .map(ImpairmentProvisionDetailVO::getLastMonthBalance).reduce(BigDecimal.ZERO, BigDecimal::add)));
        if (ObjectUtil.equals(code, ImpairmentTypeEnum.TYPE_ENUM_7.getCode()) && ObjectUtil.isNotEmpty(transferOutMap)) {
            // 本月转出
            reportVO.setThisMonthTransferOut(transferOutMap.get(code));
        }
        // 本月计提=拨备合计-上月余额+本月转出
        reportVO.setThisMonthProvision(NumberUtil.add(NumberUtil.sub(reportVO.getProvisionTotal(), reportVO.getLastMonthBalance()), reportVO.getThisMonthTransferOut()));
        reportVOList.add(reportVO);
    }

    /**
     * 封装租赁资产
     *
     * @param transferOutMap
     * @param listMap
     * @param reportVOList
     */


    /**
     * @description:
     * @author: zhangli.chen
     * @date 2025/04/11 14:53
     * @param: transferOutMap 按减值类型分组-拨备金额汇总值
     * @param: listMap 按减值类型分组-减值导入明细
     * @param: reportVOList 返回列表
     * @return void
     **/
    private void generateRentProperty(Map<String, BigDecimal> transferOutMap, Map<String, List<ImpairmentProvisionDetailVO>> listMap, List<ImpairmentProvisionDetailReportVO> reportVOList) {
        ImpairmentProvisionDetailReportVO reportVO = new ImpairmentProvisionDetailReportVO();
        BigDecimal renProvisionTransferOut = BigDecimal.ZERO;
        if (ObjectUtil.isNotEmpty(transferOutMap)) {
            // 租赁资产-本月转出
            renProvisionTransferOut = NumberUtil.toBigDecimal(transferOutMap.get(ImpairmentTypeEnum.TYPE_ENUM_1.getCode()));
        }
        // 1.租赁资产-按减值类型分组-减值导入明细
        List<ImpairmentProvisionDetailVO> rentPropertyList = listMap.get(ImpairmentTypeEnum.TYPE_ENUM_1.getCode());
        // 五级分类
        List<ImpairmentProvisionDetailReportVO> fiveClassReportVOList = Lists.newArrayList();
        // 租赁资产
        reportVO.setImpairmentType(ImpairmentTypeEnum.TYPE_ENUM_1.getCode());
        // 1.1租赁资产-正常-获取明细中对应五级分类下汇总的风险敞口+拨备合计
        fiveClassReportVOList.add(generateRentPropertyByFiveClass(reportVO, rentPropertyList, ImpairmentTypeEnum.FIVE_CLASS_1.getCode()));
        // 1.2租赁资产-关注类-获取明细中对应五级分类下汇总的风险敞口+拨备合计
        fiveClassReportVOList.add(generateRentPropertyByFiveClass(reportVO, rentPropertyList, ImpairmentTypeEnum.FIVE_CLASS_2.getCode()));
        // 1.3租赁资产-次级类-获取明细中对应五级分类下汇总的风险敞口+拨备合计
        fiveClassReportVOList.add(generateRentPropertyByFiveClass(reportVO, rentPropertyList, ImpairmentTypeEnum.FIVE_CLASS_3.getCode()));
        // 1.4租赁资产-可疑类-获取明细中对应五级分类下汇总的风险敞口+拨备合计
        fiveClassReportVOList.add(generateRentPropertyByFiveClass(reportVO, rentPropertyList, ImpairmentTypeEnum.FIVE_CLASS_4.getCode()));
        // 1.5租赁资产-损失类-获取明细中对应五级分类下汇总的风险敞口+拨备合计
        fiveClassReportVOList.add(generateRentPropertyByFiveClass(reportVO, rentPropertyList, ImpairmentTypeEnum.FIVE_CLASS_5.getCode()));
        // 1.6合计五级分类-租赁资产-合计项
        ImpairmentProvisionDetailReportVO fiveClassTotalVO = getRentPropertyTotal(reportVO, fiveClassReportVOList, rentPropertyList, renProvisionTransferOut);
        reportVOList.addAll(fiveClassReportVOList);
        reportVOList.add(fiveClassTotalVO);
        // 2.租赁资产-三阶段
        List<ImpairmentProvisionDetailReportVO> threeStepReportVOList = Lists.newArrayList();
        // 2.1租赁资产-阶段一
        threeStepReportVOList.add(generateRentPropertyByThreeStep(reportVO, rentPropertyList, ImpairmentTypeEnum.THREE_STEP_1.getCode()));
        // 2.2租赁资产-阶段二
        threeStepReportVOList.add(generateRentPropertyByThreeStep(reportVO, rentPropertyList, ImpairmentTypeEnum.THREE_STEP_2.getCode()));
        // 2.3租赁资产-阶段三
        threeStepReportVOList.add(generateRentPropertyByThreeStep(reportVO, rentPropertyList, ImpairmentTypeEnum.THREE_STEP_3.getCode()));
        // 2.4合计三阶段
        ImpairmentProvisionDetailReportVO threeStepTotalVO = getRentPropertyTotal(reportVO, threeStepReportVOList, rentPropertyList, renProvisionTransferOut);
        reportVOList.addAll(threeStepReportVOList);
        reportVOList.add(threeStepTotalVO);
        // 自动校验两种方式本月计提合计值应相等，如不相等高亮提示
        if (!NumberUtil.equals(fiveClassTotalVO.getThisMonthProvision(), threeStepTotalVO.getThisMonthProvision())) {
            // 本月计提异常(0-否，1-是)-有异常
            threeStepTotalVO.setThisMonthProvisionError(YesOrNoEnum.YES.getCode());
            fiveClassTotalVO.setThisMonthProvisionError(YesOrNoEnum.YES.getCode());
        }
    }


    /**
     * @description: 租赁资产-合计项
     * @author: zhangli.chen
     * @date 2025/04/11 15:03
     * @param: reportVO 租赁资产分类下的实体
     * @param: classifyReportVOList 租赁资产分类下-各五级分类下-对应五级分类下汇总的风险敞口+拨备合计汇总值
     * @param: rentPropertyList 租赁资产分类下-减值导入明细
     * @param: renProvisionTransferOut 租赁资产分类下-本月转出
     * @return ImpairmentProvisionDetailReportVO
     **/
    private ImpairmentProvisionDetailReportVO getRentPropertyTotal(ImpairmentProvisionDetailReportVO reportVO,
                                                                   List<ImpairmentProvisionDetailReportVO> classifyReportVOList,
                                                                   List<ImpairmentProvisionDetailVO> rentPropertyList,
                                                                   BigDecimal renProvisionTransferOut) {
        ImpairmentProvisionDetailReportVO rentPropertyTotalVO = BeanUtil.copyProperties(reportVO, ImpairmentProvisionDetailReportVO.class);
        // 减值类型
        rentPropertyTotalVO.setClassResult(ImpairmentTypeEnum.TYPE_TOTAL.getCode());
        // 风险敞口
        rentPropertyTotalVO.setRiskExposure(classifyReportVOList.stream().map(ImpairmentProvisionDetailReportVO::getRiskExposure).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 拨备合计
        rentPropertyTotalVO.setProvisionTotal(classifyReportVOList.stream().map(ImpairmentProvisionDetailReportVO::getProvisionTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 汇总减值分类下-导入数据的上月余额汇总--与风险敞口和拨备合计的统计差异是：上月余额不是根据分类后再汇总，而是直接根据导入明细汇总，如果五级分类或者三阶段为空那么就存在数据统计差异
        if (CollectionUtils.isNotEmpty(rentPropertyList)) {
//            rentPropertyTotalVO.setLastMonthBalance(rentPropertyList.stream()
//                    .map(ImpairmentProvisionDetailVO::getLastMonthBalance).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 假设 rentPropertyList 是 List<ImpairmentProvisionDetailVO>
            rentPropertyTotalVO.setLastMonthBalance(rentPropertyList.stream()
                    .map(vo -> (vo.getLastMonthBalance() != null) ? vo.getLastMonthBalance() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            rentPropertyTotalVO.setLastMonthBalance(BigDecimal.ZERO);
        }
        if(renProvisionTransferOut==null){
            renProvisionTransferOut = BigDecimal.ZERO;
        }
        // 本月转出
        rentPropertyTotalVO.setThisMonthTransferOut(renProvisionTransferOut);
        // 本月计提 = 拨备合计-上月余额+本月转出
        rentPropertyTotalVO.setThisMonthProvision(NumberUtil.add(NumberUtil.sub(rentPropertyTotalVO.getProvisionTotal(), rentPropertyTotalVO.getLastMonthBalance()),
                rentPropertyTotalVO.getThisMonthTransferOut()));
        return rentPropertyTotalVO;
    }

    /**
     * @description: 获取明细中对应三阶段下汇总的风险敞口+拨备合计
     * @author: zhangli.chen
     * @date 2025/04/11 15:34
     * @param: reportVO 减值计提明细VO对象
     * @param: rentPropertyList 数据明细
     * @param: code 三阶段CODE值
     * @return ImpairmentProvisionDetailReportVO
     **/
    private ImpairmentProvisionDetailReportVO generateRentPropertyByThreeStep(ImpairmentProvisionDetailReportVO reportVO
            , List<ImpairmentProvisionDetailVO> rentPropertyList
            , String code) {
        ImpairmentProvisionDetailReportVO rentProperTyReportVO = BeanUtil.copyProperties(reportVO, ImpairmentProvisionDetailReportVO.class);
        // 分类结果
        rentProperTyReportVO.setClassResult(code);
        if (CollectionUtils.isNotEmpty(rentPropertyList)) {
            // 根据三阶段汇总金额-风险敞口
            rentProperTyReportVO.setRiskExposure(NumberUtil.toBigDecimal(
                    rentPropertyList.stream().filter(a -> ObjectUtil.equals(a.getThreeStep(), code))
                            .map(ImpairmentProvisionDetailVO::getRiskExposure).reduce(BigDecimal.ZERO, BigDecimal::add)));
            // 拨备合计
            rentProperTyReportVO.setProvisionTotal(NumberUtil.toBigDecimal(
                    rentPropertyList.stream().filter(a -> ObjectUtil.equals(a.getThreeStep(), code))
                            .map(ImpairmentProvisionDetailVO::getProvisionTotal).reduce(BigDecimal.ZERO, BigDecimal::add)));
        } else {
            rentProperTyReportVO.setRiskExposure(BigDecimal.ZERO);
            rentProperTyReportVO.setProvisionTotal(BigDecimal.ZERO);
        }
        return rentProperTyReportVO;
    }

    /**
     * @description: 获取明细中对应五级分类下汇总的风险敞口+拨备合计
     * @author: zhangli.chen
     * @date 2025/04/11 14:40
     * @param: reportVO 减值计提明细VO对象
     * @param: rentPropertyList 数据明细
     * @param: code 五级分类CODE值
     * @return ImpairmentProvisionDetailReportVO
     **/
    private ImpairmentProvisionDetailReportVO generateRentPropertyByFiveClass(ImpairmentProvisionDetailReportVO reportVO
            , List<ImpairmentProvisionDetailVO> rentPropertyList
            , String code) {
        ImpairmentProvisionDetailReportVO rentProperTyReportVO = BeanUtil.copyProperties(reportVO, ImpairmentProvisionDetailReportVO.class);
        // 分类结果
        rentProperTyReportVO.setClassResult(code);
        if (CollectionUtils.isNotEmpty(rentPropertyList)) {
            // 根据五级分类汇总金额-风险敞口
            rentProperTyReportVO.setRiskExposure(NumberUtil.toBigDecimal(
                    rentPropertyList.stream().filter(a -> ObjectUtil.equals(a.getFiveClass(), code))
                            .map(ImpairmentProvisionDetailVO::getRiskExposure).reduce(BigDecimal.ZERO, BigDecimal::add)));
            // 拨备合计
            rentProperTyReportVO.setProvisionTotal(NumberUtil.toBigDecimal(
                    rentPropertyList.stream().filter(a -> ObjectUtil.equals(a.getFiveClass(), code))
                            .map(ImpairmentProvisionDetailVO::getProvisionTotal).reduce(BigDecimal.ZERO, BigDecimal::add)));
        } else {
            rentProperTyReportVO.setRiskExposure(BigDecimal.ZERO);
            rentProperTyReportVO.setProvisionTotal(BigDecimal.ZERO);
        }
        return rentProperTyReportVO;
    }

    /**
     * @description:减值计提-首页列表-导出减值清单-查询待下载的待下载的EXCEL文件类型
     **/
    @Override
    public List<ImpairmentProvisionExcelTypeVO> getExportExcelList() {
        List<ImpairmentProvisionExcelTypeVO> list = Lists.newArrayList();
        for (ImpairmentExportExcelTypeEnum excelTypeEnum : ImpairmentExportExcelTypeEnum.values()) {
            ImpairmentProvisionExcelTypeVO vo = new ImpairmentProvisionExcelTypeVO();
            vo.setExcelType(excelTypeEnum.getCode());
            vo.setName(excelTypeEnum.getDesc());
            list.add(vo);
        }
        return list;
    }

    /**
     * @description:减值计提-首页列表-导出减值清单-导出具体减值清单文件
     **/
    @Override
    public void exportImpairmentList(HttpServletResponse response, String excelType) {
        String excelName = "";
        try {
//            if (ObjectUtil.equals(ImpairmentExportExcelTypeEnum.EXCEL_TYPE_1.getCode(), excelType)) {
//                // 附件1：其他应收款_诉讼费保全费
//                excelName = ImpairmentExportExcelTypeEnum.getDescByCode(excelType);
//                List<ImpairmentProvisionExcelVOExport1> list = impairmentProvisionMapper.getExportExcel1();
//                ExcelUtil<ImpairmentProvisionExcelVOExport1> util = new ExcelUtil<ImpairmentProvisionExcelVOExport1>(ImpairmentProvisionExcelVOExport1.class);
//                response.setContentType("application/octet-stream; charset=utf-8");
//                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelName + ".xlsx", "utf8"));
//                util.exportExcel(response, BeanUtil.copyToList(list, ImpairmentProvisionExcelVOExport1.class), excelName.replace("：", ""));
//            } else if (ObjectUtil.equals(ImpairmentExportExcelTypeEnum.EXCEL_TYPE_2.getCode(), excelType)) {
//                // 附件2：恒信应收蓬莱租赁清单
//                excelName = ImpairmentExportExcelTypeEnum.getDescByCode(excelType);
//                List<ImpairmentProvisionExcelVOExport4> list = impairmentProvisionMapper.getExportExcel4();
//                ExcelUtil<ImpairmentProvisionExcelVOExport4> util = new ExcelUtil<ImpairmentProvisionExcelVOExport4>(ImpairmentProvisionExcelVOExport4.class);
//                response.setContentType("application/octet-stream; charset=utf-8");
//                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelName + ".xlsx", "utf8"));
//                util.exportExcel(response, BeanUtil.copyToList(list, ImpairmentProvisionExcelVOExport4.class), excelName.replace("：", ""));
//            }
            if (ObjectUtil.equals(ImpairmentExportExcelTypeEnum.EXCEL_TYPE_3.getCode(), excelType)) {
                // 附件3：债务重组项目长期应收款
                excelName = ImpairmentExportExcelTypeEnum.getDescByCode(excelType);
                List<ImpairmentProvisionExcelVOExport5> list = impairmentProvisionMapper.getExportExcel5();
                // 查询第一笔应收日期
                setData5(list);
                ExcelUtil<ImpairmentProvisionExcelVOExport5> util = new ExcelUtil<ImpairmentProvisionExcelVOExport5>(ImpairmentProvisionExcelVOExport5.class);
                response.setContentType("application/octet-stream; charset=utf-8");
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelName + ".xlsx", "utf8"));
                util.exportExcel(response, BeanUtil.copyToList(list, ImpairmentProvisionExcelVOExport5.class), excelName.replace("：", ""));
            } else {
                throw new ServiceException("根据导出excel模板类型" + excelType + "未匹配到对应的类型");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("减值计提导出:{},失败:", excelName, e);
            throw new ServiceException("减值计提导出" + excelName + "失败，失败原因:" + e.getMessage());
        }
    }

    /**
     * @description:减值计提-首页列表-导出减值清单-导出具体减值清单文件-附件3：债务重组项目长期应收款
     **/
    private void setData5(List<ImpairmentProvisionExcelVOExport5> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<String> contractCodeList = list.stream().map(ImpairmentProvisionExcelVOExport5::getContractCode).distinct().collect(Collectors.toList());
        List<RepaymentPlanVO> repaymentPlanVOList = iRepaymentPlanService.selectMinPlanDate(contractCodeList);
        list.stream().forEach(a -> {
            // 获取最小的计划日期
            RepaymentPlanVO repaymentPlanVO = repaymentPlanVOList.stream().filter(b -> ObjectUtil.equals(a.getContractCode(), b.getContractCode())).findFirst().orElse(null);
            if (ObjectUtil.isNotEmpty(repaymentPlanVO)) {
                a.setPlanDate(repaymentPlanVO.getPlanDate());
            }
        });
    }

    /**
     * 设置数据
     *
     * @param list
     */
    private void setData3(List<ImpairmentProvisionExcelVOExport3> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<String> contractCodeList = list.stream().map(ImpairmentProvisionExcelVOExport3::getContractCode).distinct().collect(Collectors.toList());
        List<RepaymentPlanVO> repaymentPlanVOList = iRepaymentPlanService.selectMinPlanDate(contractCodeList);
        list.stream().forEach(a -> {
            // 获取最小的计划日期
            RepaymentPlanVO repaymentPlanVO = repaymentPlanVOList.stream().filter(b -> ObjectUtil.equals(a.getContractCode(), b.getContractCode())).findFirst().orElse(null);
            if (ObjectUtil.isNotEmpty(repaymentPlanVO)) {
                a.setPlanDate(repaymentPlanVO.getPlanDate());
            }
        });
    }

    /**
     * 设置数据
     *
     * @param list
     */
    private void setData2(List<ImpairmentProvisionExcelVOExport2> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<String> contractCodeList = list.stream().map(ImpairmentProvisionExcelVOExport2::getContractCode).distinct().collect(Collectors.toList());
        List<RepaymentPlanVO> repaymentPlanVOList = iRepaymentPlanService.selectMinPlanDate(contractCodeList);
        list.stream().forEach(a -> {
            // 获取最小的计划日期
            RepaymentPlanVO repaymentPlanVO = repaymentPlanVOList.stream().filter(b -> ObjectUtil.equals(a.getContractCode(), b.getContractCode())).findFirst().orElse(null);
            if (ObjectUtil.isNotEmpty(repaymentPlanVO)) {
                a.setPlanDate(repaymentPlanVO.getPlanDate());
            }
        });
    }

    /**
     * @description:减值计提-流程复核审批通过-审批修改单据状态
     **/
    @Override
    public void updateProcessStatus(CommonApproveDTO approveDTO) {
        if (StringUtils.isEmpty(approveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        ImpairmentProvisionEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("减值计提数据不存在");
        }
        // 修改凭证状态，通过和驳回都修改
        updateVoucherStatus(Lists.newArrayList(approveDTO.getDocumentId()), approveDTO);
        // add by zhangli.chen for 异步推送汇总凭证 on 20250507
        // 异步提交
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> asyncPushSummaryVoucher(approveDTO), hthxTaskAsyncExecutor);
        // 修改状态
        entity.setProcessStatus(approveDTO.getDocumentStatus());
        // 更新时间
        entity.setUpdateTime(LocalDateTime.now());
        // 更新修改人
        entity.setUpdateBy(approveDTO.getApproverNum());
        this.updateById(entity);
    }

    /**
     * 批量冲销
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean writeOff(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据冲销");
        }
        List<ImpairmentProvisionEntity> provisionEntityList = this.listByIds(ids);
        provisionEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.REVIEWED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.TO_KINGDEE.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有审批通过的才可以冲销");
            }
            if (ObjectUtil.equals(v.getIsWriteOff(), YesOrNoEnum.YES.getCode())) {
                throw new ServiceException("已经冲销的不能再次冲销");
            }
            if (ObjectUtil.isNotEmpty(v.getWriteOffOriginalId())) {
                throw new ServiceException("冲销单不能做冲销");
            }
            v.setIsWriteOff(YesOrNoEnum.YES.getCode());
            v.setProcessStatus(ProcessStatusEnum.WRITEOFF.getCode());
            // 1.更新冲销状态
            this.updateById(v);
            ImpairmentProvisionEntity writeOffEntity = BeanUtil.copyProperties(v, ImpairmentProvisionEntity.class, GenConstants.BASE_ENTITY);
            writeOffEntity.setId(null);
            writeOffEntity.setIsWriteOff(YesOrNoEnum.NO.getCode());
            writeOffEntity.setWriteOffOriginalId(v.getId());
            writeOffEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            writeOffEntity.setProcessInstanceId(null);
            writeOffEntity.setVoucherId(null);
            writeOffEntity.setErrorInfo(null);
            writeOffEntity.setAccountDate(null);
            writeOffEntity.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
            writeOffEntity.setProvisionTotal(NumberUtil.toBigDecimal(v.getProvisionTotal()).negate());
            writeOffEntity.setThisMonthProvision(NumberUtil.toBigDecimal(v.getThisMonthProvision()).negate());
            writeOffEntity.setSummaryId(null);
            writeOffEntity.setSummaryVoucherId(null);
            // 2.保存冲销减值计提
            this.save(writeOffEntity);
            // 查询减值计提明细
            List<ImpairmentProvisionDetailEntity> impairmentProvisionDetailVOList = iImpairmentProvisionDetailService.list(new LambdaQueryWrapper<ImpairmentProvisionDetailEntity>()
                    .eq(ImpairmentProvisionDetailEntity::getImpairmentProvisionId, v.getId()));
            List<ImpairmentProvisionDetailEntity> detailEntityList = Lists.newArrayList();
            impairmentProvisionDetailVOList.stream().forEach(a -> {
                ImpairmentProvisionDetailEntity detailEntity = BeanUtil.copyProperties(a, ImpairmentProvisionDetailEntity.class, GenConstants.BASE_ENTITY);
                detailEntity.setProvisionTotal(NumberUtil.toBigDecimal(a.getProvisionTotal()).negate());
                detailEntity.setThisMonthProvision(NumberUtil.toBigDecimal(a.getThisMonthProvision()).negate());
                detailEntity.setVoucherId(null);
                detailEntity.setErrorInfo(null);
                detailEntity.setAccountDate(null);
                detailEntity.setImpairmentProvisionId(writeOffEntity.getId());
                detailEntityList.add(detailEntity);
            });
            // 3.保存冲销减值计提明细
            iImpairmentProvisionDetailService.saveBatch(detailEntityList);
        });
        return Boolean.TRUE;
    }


    /**
     * @description:减值计提-首页列表-查询上传任务
     **/
    @Override
    public IPage<ImpairmentProvisionUploadTaskVO> queryUpdateTask(ImpairmentProvisionUploadTaskQueryDTO queryDTO) {
        IPage<ImpairmentProvisionUploadTaskVO> vo = iImpairmentProvisionUploadTaskService.selectPage(queryDTO);
        return vo;
    }

    /**
     * 更新凭证状态
     *
     * @param ids
     * @param approveDTO
     */
    public void updateVoucherStatus(List<Long> ids, CommonApproveDTO approveDTO){
        // 获取所有的凭证Id
        List<VoucherVO> voucherVOList = iVoucherService.getByBatchIdList(ids, BatchTypeEnum.JZJT.getCode());
        List<String> voucherIdList = voucherVOList.stream().map(VoucherVO::getId).map(Objects::toString).collect(Collectors.toList());
        // 更新凭证状态
        iVoucherService.updateStatusByids(voucherIdList, approveDTO.getDocumentStatus(),
                approveDTO.getApproverNum(), approveDTO.getApproverName());
    }

    /**
     * @description:减值计提-流程复核通过-异步推送汇总凭证
     **/
    @Override
    public void asyncPushSummaryVoucher(CommonApproveDTO approveDTO){
        log.info("====>>ImpairmentProvisionServiceImpl==>>asyncPushSummaryVoucher==00==>>approveDTO:{}",approveDTO);
        // 只有审批通过已复核才需要生成凭证
        if(approveDTO!=null && ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())){
            // 第一步:判断该单据是否已完成了汇总凭证推送
            ImpairmentProvisionUploadTaskEntity vo = iImpairmentProvisionUploadTaskService.getOne(new LambdaUpdateWrapper<ImpairmentProvisionUploadTaskEntity>()
                    .eq(ImpairmentProvisionUploadTaskEntity::getTaskType, ImpairmentTaskTypeEnum.TASK_TYPE_4.getCode())
                    .eq(ObjectUtil.isNotEmpty(approveDTO.getDocumentId()), ImpairmentProvisionUploadTaskEntity::getDocId, approveDTO.getDocumentId())
                    .eq(ImpairmentProvisionUploadTaskEntity::getStatus, ImpairmentTaskTypeEnum.STATUS_1.getCode()));
            log.info("====>>ImpairmentProvisionServiceImpl==>>asyncPushSummaryVoucher==01==>>vo:{}",vo);
            if (ObjectUtil.isNotEmpty(vo)) {
                vo.setDataExceptionSize(vo.getDataExceptionSize()+1);
                vo.setErrorInfo(ResultEnum.IP_THERE_IS_A_SUMMARY_VOUCHER_BEING_GENERATED.getMessage());
                iImpairmentProvisionUploadTaskService.updateById(vo);
            }else{
                // add by zhangli.chen for 只有租赁资产才需要生成汇总凭证 on 20250523
                ImpairmentProvisionEntity impairmentProvisionEntity = this.getById(approveDTO.getDocumentId());
                if(impairmentProvisionEntity!=null && ImpairmentTypeEnum.TYPE_ENUM_1.getCode().equals(impairmentProvisionEntity.getImpairmentType())){
                    // 若存在冲销后再生成凭证，那么则需要将之前的汇总凭证删除掉
                    List<ImpairmentProvisionEntity> provisionEntityList = this.list(new LambdaQueryWrapper<ImpairmentProvisionEntity>()
                            .eq(ImpairmentProvisionEntity::getId, approveDTO.getDocumentId())
                            .isNotNull(ImpairmentProvisionEntity::getSummaryId));
                    if(CollectionUtils.isNotEmpty(provisionEntityList)){
                        List<Long> summaryVouchersIdList =  provisionEntityList.stream().map(ImpairmentProvisionEntity::getSummaryId).map(s -> StringUtils.toLong(s)).distinct()
                                .collect(Collectors.toList());
                        log.info("====>>ImpairmentProvisionServiceImpl==>>asyncPushSummaryVoucher==02==>>summaryVouchersIdList:{}",summaryVouchersIdList);
                        batchDeleteVoucher(summaryVouchersIdList);
                    }
                    // 第二步:新建推送汇总凭证任务
                    ImpairmentProvisionUploadTaskEntity taskEntity = new ImpairmentProvisionUploadTaskEntity();
                    taskEntity.setTaskType(ImpairmentTaskTypeEnum.TASK_TYPE_4.getCode());
                    // 进行中
                    taskEntity.setStatus(ImpairmentTaskTypeEnum.STATUS_1.getCode());
                    taskEntity.setStartTime(LocalDateTime.now());
                    // 任务总条数
                    VoucherQueryDTO queryDTO = new VoucherQueryDTO();
                    queryDTO.setPageNum(FinanceEngineEnum.Numbers.ONE.getKey());
                    queryDTO.setPageSize(FinanceEngineEnum.Numbers.HUNDRED.getKey());
                    queryDTO.setBatchType(approveDTO.getDocumentType());
                    queryDTO.setBatchId(approveDTO.getDocumentId());
                    // 设置会计期间
                    //ImpairmentProvisionEntity impairmentProvisionEntity = this.baseMapper.selectById(approveDTO.getDocumentId());
                    queryDTO.setPeriodCode(getPeriodCode(impairmentProvisionEntity));
                    // 只查询出来了单页的数据
                    IPage<VoucherDetailDTO> ImpairmentProvisionSummary = iVoucherService.summaryByPage(queryDTO);
                    log.info("====>>ImpairmentProvisionServiceImpl==>>asyncPushSummaryVoucher==03==>>queryDTO:{},ImpairmentProvisionSummary:{}",queryDTO,ImpairmentProvisionSummary);
                    // 任务总条数
                    if(ImpairmentProvisionSummary!=null){
                        List<VoucherDetailDTO> voucherDetailDTOList = ImpairmentProvisionSummary.getRecords();
                        if(CollectionUtils.isNotEmpty(voucherDetailDTOList)) {
                            Map<String, List<VoucherDetailDTO>> voucherDetailMapByOrgId = voucherDetailDTOList.stream()
                                    .collect(Collectors.groupingBy(VoucherDetailDTO::getOrgId));
                            taskEntity.setDataSize(voucherDetailMapByOrgId.size());
                        }
                        taskEntity.setDataTotalSize(Math.toIntExact(ImpairmentProvisionSummary.getTotal()));
                    }
                    taskEntity.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
                    taskEntity.setCreateTime(LocalDateTime.now());
                    taskEntity.setUserId(SecurityUtils.getUserId());
                    taskEntity.setUserName(SecurityUtils.getUsername());
                    taskEntity.setDocId(approveDTO.getDocumentId());
                    taskEntity.setDelFlag(YesOrNoEnum.NO.getCode());
                    iImpairmentProvisionUploadTaskService.saveOrUpdate(taskEntity);
                    log.info("====>>ImpairmentProvisionServiceImpl==>>asyncPushSummaryVoucher==04==>>taskEntity:{}",taskEntity);
                    // 第三步:Feign请求ETL服务，推送汇总凭证
                    CompletableFuture<HthxPushVoucherResultDTO> completableFuture = CompletableFuture.supplyAsync(() -> {
                        HthxPushVoucherParamsDTO pushVoucherParams = new HthxPushVoucherParamsDTO();
                        pushVoucherParams.setDocumentId(approveDTO.getDocumentId());
                        pushVoucherParams.setDocumentType(approveDTO.getDocumentType());
                        pushVoucherParams.setSubmitUserId(approveDTO.getSubmitterNum());
                        pushVoucherParams.setSubmitUserName(approveDTO.getSubmitterName());
                        log.info("====>>ImpairmentProvisionServiceImpl==>>asyncPushSummaryVoucher==05==>>pushVoucherParams:{}",pushVoucherParams);
                        // 请求ETL服务上凭证推送接口
                        //R<HthxPushVoucherResultDTO> pushVoucherResult = hthxImpairmentProvisionFacade.pushSummaryVouchers(pushVoucherParams);
                        // modify by zhangli.chen for 方案调整为修改生成虚拟合同号-VL05Z0001的汇总凭证 on 20250509
                        HthxPushVoucherResultDTO hthxPushVoucherResult = generateSummaryVouchers(pushVoucherParams,queryDTO.getPeriodCode(),
                                taskEntity.getDataTotalSize());
                        R<HthxPushVoucherResultDTO> pushVoucherResult = R.ok(hthxPushVoucherResult);
                        log.info("====>>ImpairmentProvisionServiceImpl==>>asyncPushSummaryVoucher==06==>>pushVoucherResult:{}",pushVoucherResult);
                        HthxPushVoucherResultDTO hthxPushVoucherResultDTO;
                        if(Constants.SUCCESS.intValue()==pushVoucherResult.getCode()){
                            hthxPushVoucherResultDTO = pushVoucherResult.getData();
                        }else{
                            hthxPushVoucherResultDTO = new HthxPushVoucherResultDTO();
                            hthxPushVoucherResultDTO.setPushResult(YesOrNoEnum.NO.getCode());
                            StringBuffer resultMessage = pushVoucherResult.getMsg() != null ? new StringBuffer(pushVoucherResult.getMsg()) : new StringBuffer();
                            hthxPushVoucherResultDTO.setErrorInfo(resultMessage);
                        }
                        log.info("====>>ImpairmentProvisionServiceImpl==>>asyncPushSummaryVoucher==07==>>hthxPushVoucherResultDTO:{}",hthxPushVoucherResultDTO);
                        return hthxPushVoucherResultDTO;
                    }, hthxTaskAsyncExecutor).whenComplete((pushVoucherResult, e) -> {
                        log.info("====>>ImpairmentProvisionServiceImpl==>>asyncPushSummaryVoucher==08==>>pushVoucherResult:{}",pushVoucherResult);
                        // 更新明细凭证状态
                        List<Long> voucherIdList = Lists.newArrayList();
                        if (StringUtils.isNotEmpty(impairmentProvisionEntity.getVoucherId())) {
                            voucherIdList.addAll(Arrays.stream(impairmentProvisionEntity.getVoucherId().split(","))
                                    .map(Long::parseLong).collect(Collectors.toList()));
                        }
                        if (voucherIdList != null && !voucherIdList.isEmpty()) {
                            List<List<Long>> partitions = Lists.partition(voucherIdList, FinanceEngineEnum.Numbers.THOUSAND.getKey());
                            for (List<Long> batchIds : partitions) {
                                iVoucherService.lambdaUpdate()
                                        .set(VoucherEntity::getVoucherStatus, FinanceEngineEnum.Numbers.NINE.getValue())
                                        .eq(VoucherEntity::getBatchId, impairmentProvisionEntity.getId())
                                        .eq(VoucherEntity::getPeriodCode, queryDTO.getPeriodCode())
                                        .in(VoucherEntity::getId, batchIds)
                                        .update();
                            }
                        }
                        // 第四步:根据推送结果更新任务状态
                        updateTask(taskEntity.getId(), ImpairmentTaskTypeEnum.STATUS_2.getCode(), pushVoucherResult.getSuccessSize(),
                                pushVoucherResult.getFailSize(), StringUtils.truncateStringBuffer(pushVoucherResult.getErrorInfo()));
                    }).exceptionally(e -> {
                        log.info("====>>ImpairmentProvisionServiceImpl==>>asyncPushSummaryVoucher==09==>>");
                        updateTask(taskEntity.getId(), ImpairmentTaskTypeEnum.STATUS_3.getCode(),
                                FinanceEngineEnum.Numbers.ZERO.getKey(), FinanceEngineEnum.Numbers.ZERO.getKey(), e.getMessage());
                        return null;
                    });
                }else{
                    // 非租赁资产的只需要将明细凭证更新为待传金蝶状态即可
                    log.info("====>>ImpairmentProvisionServiceImpl==>>asyncPushSummaryVoucher==10==>>impairmentProvisionEntity:{}",impairmentProvisionEntity);
                    // 更新明细凭证状态
                    List<Long> voucherIdList = Lists.newArrayList();
                    if (StringUtils.isNotEmpty(impairmentProvisionEntity.getVoucherId())) {
                        voucherIdList.addAll(Arrays.stream(impairmentProvisionEntity.getVoucherId().split(","))
                                .map(Long::parseLong).collect(Collectors.toList()));
                    }
                    if (voucherIdList != null && !voucherIdList.isEmpty()) {
                        Integer periodCode = getPeriodCode(impairmentProvisionEntity);
                        log.info("====>>ImpairmentProvisionServiceImpl==>>asyncPushSummaryVoucher==11==>>voucherIdList:{}",voucherIdList);
                        List<List<Long>> partitions = Lists.partition(voucherIdList, FinanceEngineEnum.Numbers.THOUSAND.getKey());
                        for (List<Long> batchIds : partitions) {
                            iVoucherService.lambdaUpdate()
                                    .set(VoucherEntity::getVoucherStatus, FinanceEngineEnum.Numbers.NINE.getValue())
                                    .eq(VoucherEntity::getBatchId, impairmentProvisionEntity.getId())
                                    .eq(VoucherEntity::getPeriodCode, periodCode)
                                    .in(VoucherEntity::getId, batchIds)
                                    .update();
                        }
                    }
                }
            }
        }
        log.info("====>>ImpairmentProvisionServiceImpl==>>asyncPushSummaryVoucher==100==>>");
    }

    /**
     * @description:减值计提-生成汇总凭证
     **/
    private HthxPushVoucherResultDTO generateSummaryVouchers(HthxPushVoucherParamsDTO pushVoucherParams,Integer periodCode,Integer dataSize){
        log.info("====>>ImpairmentProvisionServiceImpl==>>generateSummaryVouchers==00==>>pushVoucherParams:{},periodCode:{},dataSize:{}"
                ,pushVoucherParams,periodCode,dataSize);
        HthxPushVoucherResultDTO hthxPushVoucherResult = new HthxPushVoucherResultDTO();
        hthxPushVoucherResult = generateSummaryVouchersByCode(pushVoucherParams,periodCode,dataSize);
        log.info("====>>ImpairmentProvisionServiceImpl==>>generateSummaryVouchers==02==>>hthxPushVoucherResult:{}"
                ,hthxPushVoucherResult);
        ImpairmentProvisionEntity entity = this.getById(pushVoucherParams.getDocumentId());
        if(hthxPushVoucherResult!=null && CollectionUtils.isNotEmpty(hthxPushVoucherResult.getBatchIdList())){
            String summaryId = hthxPushVoucherResult.getBatchIdList().stream()
                    .map(String::valueOf).collect(Collectors.joining(","));
            // 更新汇总凭证的单据ID
            entity.setSummaryId(summaryId);
        }
        // 更新汇总凭证ID
        if(hthxPushVoucherResult!=null && CollectionUtils.isNotEmpty(hthxPushVoucherResult.getVoucherIdList())){
            entity.setSummaryVoucherId(String.join(FinanceEngineEnum.Symbol.COMMA.getKey(), hthxPushVoucherResult.getVoucherIdList()));
        }
        entity.setUpdateTime(LocalDateTime.now());
        this.updateById(entity);
        log.info("====>>ImpairmentProvisionServiceImpl==>>generateSummaryVouchers==100==>>");
        return hthxPushVoucherResult;
    }

    /**
     * @description:减值计提-生成汇总凭证-代码编程实现
     **/
    private HthxPushVoucherResultDTO generateSummaryVouchersByCode(HthxPushVoucherParamsDTO pushVoucherParams,Integer periodCode
            ,Integer dataSize){
        log.info("====>>ImpairmentProvisionServiceImpl==>>generateSummaryVouchersByCode==00==>>pushVoucherParams:{},periodCode:{},dataSize:{}"
                ,pushVoucherParams,periodCode,dataSize);
        // 汇总凭证批次ID
        List<Long> batchIdList = new ArrayList<>();
        HthxPushVoucherResultDTO hthxPushVoucherResult = new HthxPushVoucherResultDTO();
        VoucherQueryDTO queryDTO = new VoucherQueryDTO();
        queryDTO.setPageNum(FinanceEngineEnum.Numbers.ONE.getKey());
        queryDTO.setPageSize(dataSize);
        queryDTO.setBatchType(pushVoucherParams.getDocumentType());
        queryDTO.setBatchId(pushVoucherParams.getDocumentId());
        queryDTO.setPeriodCode(periodCode);
        IPage<VoucherDetailDTO> ImpairmentProvisionSummary = iVoucherService.summaryByPage(queryDTO);
        List<VoucherDTO> voucherDTOList = new ArrayList<>();
        if(ImpairmentProvisionSummary!=null){
            List<VoucherDetailDTO> voucherDetailDTOList = ImpairmentProvisionSummary.getRecords();
            if(CollectionUtils.isNotEmpty(voucherDetailDTOList)){
                Map<String, List<VoucherDetailDTO>> voucherDetailMapByOrgId = voucherDetailDTOList.stream()
                        .collect(Collectors.groupingBy(VoucherDetailDTO::getOrgId));
                log.info("====>>ImpairmentProvisionServiceImpl==>>generateSummaryVouchersByCode==01==>>voucherDetailMapByOrgId:{}",voucherDetailMapByOrgId);
                voucherDetailMapByOrgId.forEach((orgId, voucherDetailList) -> {
                    Long summaryId = IdUtil.getSnowflake().nextId();
                    VoucherEntity voucherEntity = new VoucherEntity();
                    voucherEntity.setInterfaceDataId(pushVoucherParams.getDocumentId());
                    voucherEntity.setSource(SystemEnum.CWZT.getCode());
                    voucherEntity.setSystemCode(SystemEnum.CWZT.getCode());
                    voucherEntity.setSystemName(SystemEnum.CWZT.getDesc());
                    voucherEntity.setBusinessCode(BusinessEnum.ZLYW.getCode());
                    voucherEntity.setBusinessName(BusinessEnum.ZLYW.getDesc());
                    voucherEntity.setOrderId(String.valueOf(pushVoucherParams.getDocumentId()));
                    voucherEntity.setSceneCode(SceneEnum.JZJT.getCode());
                    voucherEntity.setSceneName(SceneEnum.JZJT.name());
                    voucherEntity.setContractCode("VL05Z0001");
                    voucherEntity.setOrgId(orgId);
                    voucherEntity.setSignCompany(orgId);
                    voucherEntity.setCreateBy(pushVoucherParams.getSubmitUserId());
                    voucherEntity.setCreateTime(LocalDateTime.now());
                    voucherEntity.setDelFlag(YesOrNoEnum.NO.getCode());
                    voucherEntity.setCreateUserName(pushVoucherParams.getSubmitUserName());
                    voucherEntity.setCreateUserNo(pushVoucherParams.getSubmitUserId());
                    voucherEntity.setVoucherStatus(FinanceEngineEnum.Numbers.THREE.getValue());
                    voucherEntity.setBatchId(summaryId);
                    voucherEntity.setBatchType(BatchTypeEnum.JZJT.getCode());
                    // 复核人工号
                    voucherEntity.setRecheckUserNo(pushVoucherParams.getSubmitUserId());
                    voucherEntity.setRecheckUserName(pushVoucherParams.getSubmitUserName());
                    if(CollectionUtils.isNotEmpty(voucherDetailList)){
                        VoucherDetailDTO voucherDetail = voucherDetailList.stream().filter(b -> ObjectUtil.equals(orgId, b.getOrgId())).findFirst().orElse(null);
                        if (ObjectUtil.isNotEmpty(voucherDetail)) {
                            voucherEntity.setVoucherType(voucherDetail.getVoucherType());
                            voucherEntity.setBusinessDate(HthxDateUtils.localDateToLocalDateTime(voucherDetail.getBusinessDate()));
                            voucherEntity.setVoucherDate(HthxDateUtils.localDateToLocalDateTime(voucherDetail.getVoucherDate()));
                            voucherEntity.setCurrency(voucherDetail.getCurrency());
                            voucherEntity.setVoucherSummary(voucherDetail.getVoucherSummary());
                            voucherEntity.setVoucherNum(iVoucherService.generateVoucherNum(voucherEntity.getVoucherType(), voucherEntity.getVoucherDate()));
                            voucherEntity.setSubSceneType(voucherDetail.getSubSceneType());
                            voucherEntity.setPeriodCode(voucherDetail.getPeriodCode());
                        }
                    }
                    voucherEntity.setValidFlag(FinanceEngineEnum.Numbers.ONE.getValue());
                    voucherEntity.setIsSummary(FinanceEngineEnum.Numbers.ZERO.getValue());
                    voucherEntity.setIsWriteOff(FinanceEngineEnum.Numbers.ZERO.getValue());
                    voucherEntity.setIsSendKingdee(FinanceEngineEnum.Numbers.ZERO.getValue());
                    // 保存凭证头
                    iVoucherService.save(voucherEntity);
                    batchIdList.add(summaryId);
                    VoucherDTO dto = BeanUtil.copyProperties(voucherEntity, VoucherDTO.class);
                    log.info("====>>ImpairmentProvisionServiceImpl==>>generateSummaryVouchersByCode==02==>>voucherEntity:{},dto:{}",voucherEntity,dto);
                    // 生成凭证行
                    if (CollectionUtil.isNotEmpty(voucherDetailList)){
                        voucherDetailList.stream().forEach(e -> {
                            VoucherEntryEntity entryEntity = BeanUtil.copyProperties(e, VoucherEntryEntity.class);
                            entryEntity.setVoucherId(voucherEntity.getId());
                            entryEntity.setPeriodCode(voucherEntity.getPeriodCode());
                            // 金融机构，成本中心放到凭证行上
                            entryEntity.setFinancialInstitution(dto.getFinancialInstitution());
                            entryEntity.setCostCentre(dto.getCostCentre());
                            if (DefaultConstant.EDIT_FLAG_ACCOUNT_CODE.equals(entryEntity.getAccountCode())) {
                                entryEntity.setEditFlag(YesOrNoEnum.YES.getCode());
                            }
                            //获取汇率
                            BigDecimal rate = iEasExchangeRateService.getRateBySourceNameAndTargetName(CurrencyTypeEnum.getDescByCode(voucherEntity.getCurrency()),CurrencyTypeEnum.CNY.getDesc());
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
                            List<AccountEntity> accountEntityList = iAccountService.lambdaQuery().eq(AccountEntity::getAccountCode,
                                    entryEntity.getAccountCode()).eq(AccountEntity::getBusinessCode,voucherEntity.getBusinessCode()).list();
                            if (CollectionUtils.isNotEmpty(accountEntityList)) {
                                AccountEntity accountEntity = accountEntityList.get(0);
                                entryEntity.setAssistFlags(accountEntity.getAssistFlags());
                                entryEntity.setFundType(accountEntity.getFundType());
                            }
                            entryEntity.setRelateBankFlag(FinanceEngineEnum.Numbers.ZERO.getValue());
                            entryEntity.setContractCode(voucherEntity.getContractCode());
                            entryEntity.setCreateBy(voucherEntity.getCreateBy());
                            entryEntity.setCreateTime(voucherEntity.getCreateTime());
                            entryEntity.setDelFlag(YesOrNoEnum.NO.getCode());
                            entryEntity.setEditFlag(YesOrNoEnum.NO.getCode());
                            entryEntity.setIsSendKingdee(FinanceEngineEnum.Numbers.ZERO.getValue());
                            // 保存凭证行
                            voucherEntryService.save(entryEntity);
                        });
                    }
                     voucherDTOList.add(dto);
                });
                log.info("====>>ImpairmentProvisionServiceImpl==>>generateSummaryVouchersByCode==03==>>voucherDTOList:{}",voucherDTOList);
            }
        }
        if(CollectionUtils.isNotEmpty(voucherDTOList)){
            hthxPushVoucherResult.setPushResult(YesOrNoEnum.YES.getCode());
            hthxPushVoucherResult.setSuccessSize(voucherDTOList.size());
            hthxPushVoucherResult.setVoucherIdList(voucherDTOList.stream().map(VoucherDTO::getId)
                    .map(String::valueOf).collect(Collectors.toList()));
            hthxPushVoucherResult.setBatchIdList(batchIdList);
        }else{
            hthxPushVoucherResult.setPushResult(YesOrNoEnum.NO.getCode());
            hthxPushVoucherResult.setErrorInfo(new StringBuffer(ResultEnum.IP_NO_SUMMARY_VOUCHER_NEED_GENERATE.getMessage()));
        }
        log.info("====>>ImpairmentProvisionServiceImpl==>>generateSummaryVouchersByCode==100==>>hthxPushVoucherResult:{}",hthxPushVoucherResult);
        return hthxPushVoucherResult;
    }

    /**
     * @description:减值计提-生成汇总凭证-配置凭证引擎实现
     **/
    private HthxPushVoucherResultDTO generateSummaryVouchersByConfig(HthxPushVoucherParamsDTO pushVoucherParams
            ,Integer periodCode,Integer dataSize,Long summaryId){
        HthxPushVoucherResultDTO hthxPushVoucherResult = new HthxPushVoucherResultDTO();
        VoucherQueryDTO queryDTO = new VoucherQueryDTO();
        queryDTO.setPageNum(FinanceEngineEnum.Numbers.ONE.getKey());
        queryDTO.setPageSize(dataSize);
        queryDTO.setBatchType(pushVoucherParams.getDocumentType());
        queryDTO.setBatchId(pushVoucherParams.getDocumentId());
        queryDTO.setPeriodCode(periodCode);
        IPage<VoucherDetailDTO> ImpairmentProvisionSummary = iVoucherService.summaryByPage(queryDTO);
        if(ImpairmentProvisionSummary!=null) {
            List<Map<String, Object>> voucherMapList = Lists.newArrayList();
            List<VoucherDetailDTO> voucherDetailDTOList = ImpairmentProvisionSummary.getRecords();
            for (VoucherDetailDTO voucherDetail : voucherDetailDTOList) {
                ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
                executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
                executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
                executeCommonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
                executeCommonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
                executeCommonDTO.setSceneCode(SceneEnum.JZJT.getCode());
                executeCommonDTO.setSceneName(SceneEnum.JZJT.name());
                executeCommonDTO.setOrderId(pushVoucherParams.getDocumentId().toString());
                executeCommonDTO.setOrgId(voucherDetail.getOrgId());
                executeCommonDTO.setContractCode("VL05Z0001");
                executeCommonDTO.setClientCode(OrgIdEnum.C0001_01.getClientCode());
                executeCommonDTO.setClientName(null);
                executeCommonDTO.setAccountDate(HthxDateUtils.toDate(voucherDetail.getVoucherDate()));
                executeCommonDTO.setBusinessDate(HthxDateUtils.toDate(voucherDetail.getBusinessDate()));
                executeCommonDTO.setIsSubmit(YesOrNoEnum.YES.getCode());
                executeCommonDTO.setBatchId(summaryId);
                executeCommonDTO.setBatchType(BatchTypeEnum.JZJT.getCode());
                executeCommonDTO.setCurrencyType(voucherDetail.getCurrency());
                executeCommonDTO.setInterfaceId(pushVoucherParams.getDocumentId());
                // 无合同编号的设置默认业务类型
                if (ObjectUtil.isEmpty(executeCommonDTO.getContractCode())) {
                    // 业务类型设为默认
                    executeCommonDTO.setBusinessCode(BusinessEnum.DEFAULT.getCode());
                    executeCommonDTO.setBusinessName(BusinessEnum.DEFAULT.getDesc());
                }
                executeCommonDTO.setCreateUserName(pushVoucherParams.getSubmitUserName());
                executeCommonDTO.setCreateUserNo(pushVoucherParams.getSubmitUserId());
                Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
                // 业务类型
                //dataMap.put("businessType", voucherDetail.getBusinessType());
                // 减值类型
                //dataMap.put("impairmentType", voucherDetail.getImpairmentType());
                dataMap.put(RuleConstant.FIELD_ID, executeCommonDTO.getBatchId());
                // 计提金额
                dataMap.put("provisionAmount",
                        (new BigDecimal(voucherDetail.getCreditAmount()).compareTo(new BigDecimal(FinanceEngineEnum.Numbers.ZERO.getKey())) > 0)
                                ? voucherDetail.getCreditAmount() : voucherDetail.getDebitAmount());
                voucherMapList.add(dataMap);
            }
        }
        return hthxPushVoucherResult;
    }




    /**
     * @description:减值计提-获取该减值类型的会计期间
     **/
    private Integer getPeriodCode(ImpairmentProvisionEntity impairmentProvisionEntity) {
        // 会计期间
        Integer periodCode = null;
        if(ObjectUtil.isEmpty(impairmentProvisionEntity)){
            return periodCode;
        }
        if(ObjectUtil.isNotEmpty(impairmentProvisionEntity.getAccountDate())){
            periodCode = NumberUtil.parseInt(LocalDateTimeUtil.format(impairmentProvisionEntity.getAccountDate(), "yyyyMM"));
        }else if(YesOrNoEnum.YES.getCode().equals(impairmentProvisionEntity.getIsGenerateVoucher())){
            LambdaQueryWrapper<ImpairmentProvisionDetailEntity> detailQueryWrapper = Wrappers.lambdaQuery();
            detailQueryWrapper.eq(ImpairmentProvisionDetailEntity::getImpairmentProvisionId,impairmentProvisionEntity.getId());
            List<ImpairmentProvisionDetailEntity> detailEntityList = iImpairmentProvisionDetailService.list(detailQueryWrapper);
            log.info("====>>ImpairmentProvisionServiceImpl==>>setPeriodCode==00==>>id:{},detailEntityList:{}",impairmentProvisionEntity.getId(),detailEntityList);
            boolean setPeriodCodeFlag = true;
            if(CollectionUtils.isNotEmpty(detailEntityList)){
                Set<LocalDateTime> accountDateSet =  detailEntityList.stream()
                        .filter(entity -> entity.getAccountDate() != null)
                        .map(ImpairmentProvisionDetailEntity::getAccountDate)
                        .distinct()
                        .collect(Collectors.toSet());
                log.info("====>>ImpairmentProvisionServiceImpl==>>setPeriodCode==01==>>accountDateSet:{}",accountDateSet);
                if(CollectionUtils.isNotEmpty(accountDateSet)){
                    if(accountDateSet.size()==1){
                        periodCode = NumberUtil.parseInt(LocalDateTimeUtil.format(accountDateSet.iterator().next(), "yyyyMM"));
                    }else{
                        setPeriodCodeFlag = false;
                    }
                }
            }
            log.info("====>>ImpairmentProvisionServiceImpl==>>setPeriodCode==02==>>periodCode:{},setPeriodCodeFlag:{}",periodCode,setPeriodCodeFlag);
            if(periodCode==null && setPeriodCodeFlag){
                periodCode = NumberUtil.parseInt(LocalDateTimeUtil.format(LocalDateTime.now(), "yyyyMM"));
            }
            log.info("====>>ImpairmentProvisionServiceImpl==>>setPeriodCode==100==>>id:{},periodCode:{}",impairmentProvisionEntity.getId(),periodCode);
        }else{
            periodCode = NumberUtil.parseInt(LocalDateTimeUtil.format(LocalDateTime.now(), "yyyyMM"));
        }
        return periodCode;
    }

    /**
     * @param ids
     * @description:减值计提-首页-传送明细凭证至金蝶
     */
    @Override
    public String pushDetailVouchers(List<Long> ids) {
        log.info("====>>ImpairmentProvisionServiceImpl==>>pushDetailVouchers==00==>>ids:{}",ids);
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException(ResultEnum.COMMON_NO_DATA_SELECTED.getMessage());
        }
        if (ids.size() > 1) {
            throw new ServiceException(ResultEnum.COMMON_ONLY_SELECTED_ONE.getMessage());
        }
        List<ImpairmentProvisionEntity> provisionEntityList = this.listByIds(ids);
        // 只有已复核和完成凭证生成的数据才允许推送至金蝶
        provisionEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.REVIEWED.getCode().equals(v.getProcessStatus()) ) {
                throw new ServiceException(ResultEnum.IP_ONLY_REVIEWED_CAN_PUSH_VOUCHERS.getMessage());
            }
            if (!YesOrNoEnum.YES.getCode().equals(v.getIsGenerateVoucher()) || StringUtils.isEmpty(v.getVoucherId())) {
                throw new ServiceException(ResultEnum.IP_VOUCHER_IS_NULL.getMessage());
            }
        });
        Long headId = ids.get(FinanceEngineEnum.Numbers.ZERO.getKey());
        // 第一步:判断该单据是否已完成了明细凭证推送
        List<String> finishStatus = new ArrayList<>();
        finishStatus.add(ImpairmentTaskTypeEnum.STATUS_2.getCode());
        ImpairmentProvisionUploadTaskEntity vo = iImpairmentProvisionUploadTaskService.getOne(new LambdaUpdateWrapper<ImpairmentProvisionUploadTaskEntity>()
                .eq(ImpairmentProvisionUploadTaskEntity::getTaskType, ImpairmentTaskTypeEnum.TASK_TYPE_5.getCode())
                .eq(ObjectUtil.isNotEmpty(headId), ImpairmentProvisionUploadTaskEntity::getDocId, headId)
                .in(ImpairmentProvisionUploadTaskEntity::getStatus, finishStatus));
        log.info("====>>ImpairmentProvisionServiceImpl==>>pushDetailVouchers==01==>>vo:{}",vo);
        if (ObjectUtil.isNotEmpty(vo)) {
            vo.setDataExceptionSize(vo.getDataExceptionSize()+1);
            iImpairmentProvisionUploadTaskService.updateById(vo);
            return PromptMessageUtil.promptMessageFormat(ResultEnum.IP_CAN_NOT_REPEATEDLY_PUSH);
        }else{
            // 第二步:新建推送明细凭证任务
            ImpairmentProvisionEntity provisionEntity = this.getById(headId);
            List<Long> voucherIdList = Lists.newArrayList();
            if (StringUtils.isNotEmpty(provisionEntity.getVoucherId())) {
                voucherIdList.addAll(Arrays.stream(provisionEntity.getVoucherId().split(","))
                        .map(Long::parseLong).collect(Collectors.toList()));
            }
            if (CollectionUtils.isEmpty(voucherIdList)) {
                saveTask(FinanceEngineEnum.Numbers.ZERO.getKey(), ImpairmentTaskTypeEnum.TASK_TYPE_5.getCode(),
                        ImpairmentTaskTypeEnum.STATUS_2.getCode(),provisionEntity.getId());
            }else{
                Long taskId  = saveTask(null, null, voucherIdList.size(), ImpairmentTaskTypeEnum.TASK_TYPE_5.getCode(), provisionEntity.getId());
                 // 第三步:Feign请求ETL服务，推送汇总凭证
                CompletableFuture<HthxPushVoucherResultDTO> completableFuture = CompletableFuture.supplyAsync(() -> {
                    HthxPushVoucherParamsDTO pushVoucherParams = new HthxPushVoucherParamsDTO();
                    pushVoucherParams.setDocumentId(headId);
                    pushVoucherParams.setDocumentType(BatchTypeEnum.JZJT.getCode());
                    pushVoucherParams.setSubmitUserId(String.valueOf(SecurityUtils.getUserId()));
                    pushVoucherParams.setSubmitUserName(SecurityUtils.getUsername());
                    log.info("====>>ImpairmentProvisionServiceImpl==>>pushDetailVouchers==02==>>pushVoucherParams:{}",pushVoucherParams);
                    // 请求ETL服务上凭证推送接口
                   // R<HthxPushVoucherResultDTO> pushVoucherResult = hthxImpairmentProvisionFacade.pushDetailVouchers(pushVoucherParams);
                    // modify by zhangli.chen for 方案调整为修改凭证表状态为5，手工再传送凭证 on 20250509
                    LambdaQueryWrapper<VoucherEntity> wrapper = new LambdaQueryWrapper();
                    wrapper.eq(VoucherEntity::getDelFlag, YesOrNoEnum.NO.getCode());
                    wrapper.in(VoucherEntity::getId, voucherIdList);
                    List<VoucherEntity> voucherEntities = voucherMapper.selectList(wrapper);
                    HthxPushVoucherResultDTO hthxPushVoucherResult = new HthxPushVoucherResultDTO();
                    hthxPushVoucherResult.setTotalSize(voucherIdList.size());
                    hthxPushVoucherResult.setPushResult(YesOrNoEnum.YES.getCode());
                    if (voucherEntities != null && !voucherEntities.isEmpty()) {
                        List<Long> voucherIds = voucherEntities.stream().map(VoucherEntity::getId).distinct().collect(Collectors.toList());
                        List<List<Long>> partitions = Lists.partition(voucherIds, FinanceEngineEnum.Numbers.THOUSAND.getKey());
                        for (List<Long> batchIds : partitions) {
                                iVoucherService.lambdaUpdate().set(VoucherEntity::getVoucherStatus, FinanceEngineEnum.Numbers.THREE.getValue())
                                .eq(VoucherEntity::getVoucherStatus,FinanceEngineEnum.Numbers.NINE.getValue())
                                .eq(VoucherEntity::getBatchId, provisionEntity.getId())
                                .eq(VoucherEntity::getPeriodCode, getPeriodCode(provisionEntity))
                                .in(VoucherEntity::getId, batchIds).update();
                        }
                        hthxPushVoucherResult.setSuccessSize(voucherIds.size());
                        hthxPushVoucherResult.setFailSize(voucherIdList.size()-voucherIds.size());
                    }
                    R<HthxPushVoucherResultDTO> pushVoucherResult = R.ok(hthxPushVoucherResult);
                    // modify by zhangli.chen for 方案调整为修改凭证表状态为5，手工再传送凭证 on 20250509
                    log.info("====>>ImpairmentProvisionServiceImpl==>>pushDetailVouchers==03==>>pushVoucherResult:{}",pushVoucherResult);
                    HthxPushVoucherResultDTO hthxPushVoucherResultDTO;
                    if(Constants.SUCCESS.intValue()==pushVoucherResult.getCode()){
                        hthxPushVoucherResultDTO = pushVoucherResult.getData();
                    }else{
                        hthxPushVoucherResultDTO = new HthxPushVoucherResultDTO();
                        hthxPushVoucherResultDTO.setPushResult(YesOrNoEnum.NO.getCode());
                        StringBuffer resultMessage = pushVoucherResult.getMsg() != null ? new StringBuffer(pushVoucherResult.getMsg()) : new StringBuffer();
                        hthxPushVoucherResultDTO.setErrorInfo(resultMessage);
                    }
                    log.info("====>>ImpairmentProvisionServiceImpl==>>pushDetailVouchers==04==>>hthxPushVoucherResultDTO:{}",hthxPushVoucherResultDTO);
                    return hthxPushVoucherResultDTO;
                }, hthxTaskAsyncExecutor).whenComplete((pushVoucherResult, e) -> {
                    log.info("====>>ImpairmentProvisionServiceImpl==>>pushDetailVouchers==05==>>pushVoucherResult:{}",pushVoucherResult);
                    // 第四步:根据推送结果更新任务状态
                    updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_2.getCode(), pushVoucherResult.getSuccessSize(),
                            pushVoucherResult.getFailSize(), StringUtils.truncateStringBuffer(pushVoucherResult.getErrorInfo()));
                }).exceptionally(e -> {
                    log.info("====>>ImpairmentProvisionServiceImpl==>>pushDetailVouchers==06==>>");
                    updateTask(taskId, ImpairmentTaskTypeEnum.STATUS_3.getCode(),
                            FinanceEngineEnum.Numbers.ZERO.getKey(), FinanceEngineEnum.Numbers.ZERO.getKey(), e.getMessage());
                    return null;
                });
            }
            return PromptMessageUtil.promptMessageFormat(ResultEnum.IP_VOUCHER_LATER_QUERY_RESULTS,(voucherIdList.size() / FinanceEngineEnum.Numbers.THOUSAND.getKey() + 1));
        }
     }

     /**
      * @description: 批量设置客户编码
      * @author: zhangli.chen
      * @date 2025/10/20 16:14
      * @param: detailEntityList
      * @return void
      **/
    public void batchSetClientCode(List<ImpairmentProvisionDetailEntity> detailEntityList) {
        if (CollectionUtil.isNotEmpty(detailEntityList)) {
            detailEntityList.stream().forEach(a -> {
                if (StringUtils.isNotEmpty(a.getClientName())) {
                    String clientCode = iClientService.selectClientCodeByName(StringUtils.trim(a.getClientName()));
                    a.setClientCode(clientCode);
                }
            });
        }
    }


    /**
     * @description: 从金蝶获取账期
     * @author: zhangli.chen
     **/
    private Map<String, Integer> getPeriodFromKingdee(){
        Map<String, Integer> periodCodeMap = new HashMap<>();
        R<List<Map<String, String>>> periodCodeAll  = remoteKingdeeEasService.getCurrentPeriodCodeAll();
        if(ObjectUtil.isNotNull(periodCodeAll)){
            List<Map<String, String>> periodCodeList  =  periodCodeAll.getData();
            for (Map<String, String> map : periodCodeList) {
                if(StringUtils.isNotEmpty(map.get("ORGID")) && StringUtils.isNotEmpty(map.get("PERIODCODE"))){
                    periodCodeMap.put(String.valueOf(map.get("ORGID")), Integer.valueOf(map.get("PERIODCODE")));
                }
            }
        }
        return periodCodeMap;
    }

    /**
     * @description: 提交前校验凭证，返回有报错的凭证ID及其对应的所有错误信息
     * @author: zhangli.chen
     * @date 2025/11/10 13:46
     * @param voucherIdList 待校验的凭证ID列表
     * @return key：有报错的凭证ID，value：该凭证的所有错误信息（去重且保持顺序）
     **/
    private Map<Long, List<String>> verifyVoucherBeforeSubmit(List<Long> voucherIdList) {
        // 用LinkedHashMap保证凭证ID的遍历顺序与输入一致；value用LinkedHashSet保证错误顺序且自动去重
        Map<Long, Set<String>> errorSetMap = new LinkedHashMap<>();
        if (CollectionUtils.isNotEmpty(voucherIdList)) {
            voucherIdList.forEach(voucherId -> {
                // 1. 查询凭证行信息
                List<VoucherEntryEntity> voucherEntryEntities = voucherEntryService.selectByVoucherId(voucherId);
                // 2. 校验：未查询到凭证行
                if (CollectionUtil.isEmpty(voucherEntryEntities)) {
                    // 无凭证行，无需后续校验
                    errorSetMap.computeIfAbsent(voucherId, k -> new LinkedHashSet<>())
                            .add("未查询到凭证行！");
                    return;
                }
                // 3. 校验：存在禁止提交的科目代码（9999.99）
                boolean hasForbiddenAccount = voucherEntryEntities.stream()
                        .anyMatch(entry -> ObjectUtil.equals(entry.getAccountCode(), DefaultConstant.EDIT_FLAG_ACCOUNT_CODE));
                if (hasForbiddenAccount) {
                    errorSetMap.computeIfAbsent(voucherId, k -> new LinkedHashSet<>())
                            .add(String.format("存在科目代码为【%s】的凭证行，不能提交！", DefaultConstant.EDIT_FLAG_ACCOUNT_CODE));
                }
            });
        }
        // 转换为List返回（Set转List保持顺序），确保Map中仅包含有错误的凭证ID
        return errorSetMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new ArrayList<>(entry.getValue()),
                        (k1, k2) -> k1, // 冲突解决（理论上不会触发，因key唯一）
                        LinkedHashMap::new // 保持输入顺序
                ));
    }


    /**
     * @description:
     * @author: zhangli.chen
     * @date 2025/11/10 15:30
     * @param: voucherIdList
     * @return String
     **/
    private String generateVerifyInfoBeforeSubmit(Map<Long, List<String>> voucherErrorMap) {
        // 格式化错误信息：按凭证ID分组展示
        String errorDetail = voucherErrorMap.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty()) // 过滤掉无错误的凭证
                .map(entry -> String.format("凭证【%d】：%s",
                        entry.getKey(),
                        String.join(FinanceEngineEnum.Symbol.COMMA.getValue(), entry.getValue()) // 同一凭证的多个错误用分号分隔
                )).collect(Collectors.joining(FinanceEngineEnum.Symbol.SEMICOLON.getValue())); // 不同凭证的错误用分号分隔
        // 返回格式化后的提示信息
        return PromptMessageUtil.promptMessageFormat(ResultEnum.IP_VERIFY_VOUCHER_BEFORE_SUBMIT, errorDetail);
    }


}





