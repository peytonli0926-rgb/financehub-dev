package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.mapper.TailDifferenceAdjustmentMapper;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.DataExecutionTaskDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IDataExecutionTaskService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.utils.PeriodCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;



/**
 * @Author : bruyang
 * @Date : Create in 2024-03-05
 * @Description :  TailDifferenceAdjustment服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class TailDifferenceAdjustmentServiceImpl extends ServiceImpl<TailDifferenceAdjustmentMapper, TailDifferenceAdjustmentEntity> implements ITailDifferenceAdjustmentService {

    private final TailDifferenceAdjustmentMapper tailDifferenceAdjustmentMapper;

    private final IAccountService iAccountService;

    private final ITailDifferenceAdjustmentDetailService iTailDifferenceAdjustmentDetailService;

    private final IVoucherService iVoucherService;

    private final IApproveService iApproveService;

    @Value("${approve.url.tailAdjust-url:null}")
    private String approveUrl;

    private final IRuleService iRuleService;

    private final ILeaseIncomeDetailsService iLeaseIncomeDetailsService;

    private final IContractService iContractService;

    private final RemoteDictService remoteDictService;

    private final IDataExecutionTaskService iDataExecutionTaskService;

    private final IContractBalanceLatestService iContractBalanceLatestService;

    private final IBatchTaskService iBatchTaskService;
    private final KindeePeriodService kindeePeriodService;

    @Override
    public Long saveTailDifferenceAdjustment(TailDifferenceAdjustmentDTO dto) {
        TailDifferenceAdjustmentEntity entity = BeanUtil.copyProperties(dto, TailDifferenceAdjustmentEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateTailDifferenceAdjustment(Long id, TailDifferenceAdjustmentDTO dto) {
        TailDifferenceAdjustmentEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public TailDifferenceAdjustmentDTO getTailDifferenceAdjustmentDTOById(Long id) {
        TailDifferenceAdjustmentEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, TailDifferenceAdjustmentDTO.class);
    }

    @Override
    public IPage<TailDifferenceAdjustmentVO> selectPage(TailDifferenceAdjustmentQueryDTO queryDTO) {
//        IPage<TailDifferenceAdjustmentVO> resultPage = new Page<>();
//        if (ObjectUtil.isNotNull(queryDTO.getAccountBalance())) {
//            List<TailDifferenceAdjustmentDetailVO> detailVOS = iTailDifferenceAdjustmentDetailService.selectByCondition(BeanUtil.copyProperties(queryDTO, TailDifferenceAdjustmentDetailQueryDTO.class));
//            if (CollectionUtils.isEmpty(detailVOS)) {
//              return resultPage;
//            }
//            List<Long> idList = detailVOS.stream().map(TailDifferenceAdjustmentDetailVO::getTailDifferenceAdjustmentId).distinct().collect(Collectors.toList());
//            queryDTO.setIdList(idList);
//        }
//        LambdaQueryWrapper<TailDifferenceAdjustmentEntity> queryWrapper = getQueryWraper(queryDTO);
//        //这里注入查询条件
//        IPage<TailDifferenceAdjustmentEntity> entityIPage = tailDifferenceAdjustmentMapper.selectPage(new Page<TailDifferenceAdjustmentEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
//        resultPage = ListBeanUtil.copyPage(entityIPage, TailDifferenceAdjustmentVO.class);
        IPage<TailDifferenceAdjustmentVO> resultPage = tailDifferenceAdjustmentMapper.selectDifferenceAdjustPage(new Page<TailDifferenceAdjustmentVO>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryDTO);
        resultPage.getRecords().forEach(v -> {
            setTailDifferenceBalance(v);
            v.setBatchType(BatchTypeEnum.WCTZ.getCode());
        });
        return resultPage;
    }

    public LambdaQueryWrapper<TailDifferenceAdjustmentEntity> getQueryWraper(TailDifferenceAdjustmentQueryDTO queryDTO) {
        LambdaQueryWrapper<TailDifferenceAdjustmentEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryDTO.getAccountCode())) {
            queryWrapper.eq(TailDifferenceAdjustmentEntity::getAccountCode, queryDTO.getAccountCode());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getAccountCodeList())) {
            queryWrapper.in(TailDifferenceAdjustmentEntity::getAccountCode, queryDTO.getAccountCodeList());
        }
        if (StringUtils.isNotEmpty(queryDTO.getAccountName())) {
            queryWrapper.eq(TailDifferenceAdjustmentEntity::getAccountName, queryDTO.getAccountName());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getOrgIdList())) {
            queryWrapper.in(TailDifferenceAdjustmentEntity::getOrgId, queryDTO.getOrgIdList());
        }
        if (ObjectUtil.isNotNull(queryDTO.getAccountDate())) {
            queryWrapper.apply("to_char(account_date,'YYYY-MM-DD')={0}", DateUtil.format(queryDTO.getAccountDate(), "yyyy-MM-dd"));
        }
        if (ObjectUtil.isNotNull(queryDTO.getBusinessDate())) {
            queryWrapper.apply("to_char(business_date,'YYYY-MM-DD')={0}", DateUtil.format(queryDTO.getBusinessDate(), "yyyy-MM-dd"));
        }
        if (ObjectUtil.isNotNull(queryDTO.getId())) {
            queryWrapper.in(TailDifferenceAdjustmentEntity::getId, queryDTO.getId());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getIdList())) {
            queryWrapper.in(TailDifferenceAdjustmentEntity::getId, queryDTO.getIdList());
        }
        queryWrapper.orderByDesc(TailDifferenceAdjustmentEntity::getCreateTime);
        return queryWrapper;
    }

    @Override
    public Boolean initData(TailDifferenceAdjustmentQueryDTO queryDTO) {
        String taskType = BatchTypeEnum.WCTZ.getCode() + "-initData";
        DataExecutionTaskDTO taskDTO = iDataExecutionTaskService.getRunningTaskBySystem(taskType);
        if (taskDTO != null) {
            throw new ServiceException("存在正在初始化的任务，请稍后再试");
        }
        //按照查询条件
        if (ObjectUtil.isNull(queryDTO.getBusinessDate())) {
            throw new ServiceException("同步数据业务时间需要必选");
        }
        LambdaQueryWrapper<TailDifferenceAdjustmentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(queryDTO.getBusinessDate() != null, TailDifferenceAdjustmentEntity::getBusinessDate, queryDTO.getBusinessDate());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getAccountCodeList()), TailDifferenceAdjustmentEntity::getAccountCode, queryDTO.getAccountCodeList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), TailDifferenceAdjustmentEntity::getOrgId, queryDTO.getOrgIdList());
        List<TailDifferenceAdjustmentEntity> entityIPage = tailDifferenceAdjustmentMapper.selectList(queryWrapper);
        List<TailDifferenceAdjustmentEntity> collect = entityIPage.stream().filter(v -> ProcessStatusEnum.getCannotModifyCode().contains(v.getProcessStatus())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            throw new ServiceException("存在已提交或已生成凭证的尾差调整数据，不可以再次生成数据");
        }
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            asyncInitData(queryDTO);
        });
        return Boolean.TRUE;
    }

    public Boolean asyncInitData(TailDifferenceAdjustmentQueryDTO queryDTO) {
        log.info("异步执行开始");
        Long taskId = null;
        try {
            //删除历史数据
            LambdaQueryWrapper<TailDifferenceAdjustmentEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(queryDTO.getBusinessDate() != null, TailDifferenceAdjustmentEntity::getBusinessDate, queryDTO.getBusinessDate());
            queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getAccountCodeList()), TailDifferenceAdjustmentEntity::getAccountCode, queryDTO.getAccountCodeList());
            queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), TailDifferenceAdjustmentEntity::getOrgId, queryDTO.getOrgIdList());
            this.remove(queryWrapper);

            Date startDateTime = new Date();
            log.info("尾差跑数据开始时间：{}", startDateTime);
            String taskType = BatchTypeEnum.WCTZ.getCode() + "-initData";
            taskId = iDataExecutionTaskService.createNewTask(taskType, DataExecutionTaskStatusEnum.RUNNING.getCode(), 0, null, null);
            //获取科目字典
            //获取字典是否区分合同状态
            R<List<SysDictData>> sysDictR = remoteDictService.listDictData(DictTypeEnum.MANTISSA_ADJUST_ACCOUNT.getCode());
            if (ObjectUtil.isNull(sysDictR)) {
                throw new ServiceException("调用字典服务失败");
            }
            if (CollectionUtils.isEmpty(sysDictR.getData())) {
                throw new ServiceException("科目未在字典维护不可以初始化数据");
            }
            queryDTO.setBusinessDateString(DateUtil.format(queryDTO.getBusinessDate(), "yyyy-MM-dd"));
            queryDTO.setPeriodCode(PeriodCodeUtil.periodCodeByDate(queryDTO.getBusinessDate()));
            queryDTO.setLastPeriodCode(PeriodCodeUtil.getLastMonthPeriodCode(queryDTO.getPeriodCode()));
            List<String> accountCodeList = queryDTO.getAccountCodeList();
            if (CollectionUtils.isEmpty(accountCodeList)) {
                accountCodeList = sysDictR.getData().stream().map(SysDictData::getDictValue).collect(Collectors.toList());
            }
            queryDTO.setAccountCodeList(accountCodeList);
            List<AccountEntity> accountEntityList = iAccountService.lambdaQuery().in(AccountEntity::getAccountCode, accountCodeList).list();
            //生成合同余额表临时数据
            iTailDifferenceAdjustmentDetailService.truncateContractBalanceTempData();
            iTailDifferenceAdjustmentDetailService.generateContractBalanceTempData(queryDTO);
            accountEntityList.forEach(a -> {
                queryDTO.setBusinessCode(a.getBusinessCode());
                queryDTO.setAccountCode(a.getAccountCode());
                queryDTO.setAccountName(a.getAccountName());
                queryDTO.setFundTypeBalance(a.getFundType() + "_balance");
                queryDTO.setFundTypeAmount(a.getFundType() + "_amount");
                //先生成数据
                iTailDifferenceAdjustmentDetailService.initInsertData(queryDTO);
                //更新日期
//                iTailDifferenceAdjustmentDetailService.lambdaUpdate().set(TailDifferenceAdjustmentDetailEntity::getBusinessDate,queryDTO.getBusinessDate()).set(TailDifferenceAdjustmentDetailEntity::getCreateTime,LocalDateTime.now()).isNull(TailDifferenceAdjustmentDetailEntity::getTailDifferenceAdjustmentId).update();
                //获取刚生成的数据，如果是空的，需要按照科目+名称+业务时间将已录入的都删除
                removeExistData(a.getAccountCode(), a.getAccountName(), queryDTO.getBusinessDate());
            });
            //按照签约主体+科目分组
            List<TailDifferenceAdjustmentEntity> adjustmentEntityList = iTailDifferenceAdjustmentDetailService.selectAllOrgIdAccountCode();
            if (CollectionUtils.isNotEmpty(adjustmentEntityList)) {
                adjustmentEntityList.forEach(v -> {
                    v.setBusinessDate(CommonDateUtils.parseDateToLocalDateTime(queryDTO.getBusinessDate()));
                    //判断签约主体+科目编码+科目名称+业务日期状态为已录入的是否存在存在则更新否则新增
                    TailDifferenceAdjustmentEntity entity = this.lambdaQuery().eq(TailDifferenceAdjustmentEntity::getOrgId, v.getOrgId())
                            .eq(TailDifferenceAdjustmentEntity::getAccountCode, v.getAccountCode())
                            .eq(TailDifferenceAdjustmentEntity::getAccountName, v.getAccountName())
                            .eq(TailDifferenceAdjustmentEntity::getBusinessDate, v.getBusinessDate()).one();
                    Long tailId = null;
                    Boolean isDelete = Boolean.FALSE;
                    if (null == entity) {
                        v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                        this.save(v);
                        tailId = v.getId();
                    } else if (ProcessStatusEnum.ENTERED.getCode().equals(entity.getProcessStatus())) {
                        tailId = entity.getId();
                        log.info("已经录入了需要先删除现有的数据，进行新增操作");
                        iTailDifferenceAdjustmentDetailService.remove(Wrappers.<TailDifferenceAdjustmentDetailEntity>lambdaQuery()
                                .in(TailDifferenceAdjustmentDetailEntity::getTailDifferenceAdjustmentId, tailId));
                    } else {
                        log.info("数据已经存在且状态不符合不更新,需要删除已经入库的数据");
                        //删除同步的数据
                        isDelete = Boolean.TRUE;
                    }
                    iTailDifferenceAdjustmentDetailService.lambdaUpdate()
                            .set(TailDifferenceAdjustmentDetailEntity::getTailDifferenceAdjustmentId, tailId)
                            .set(TailDifferenceAdjustmentDetailEntity::getBusinessDate, v.getBusinessDate())
                            .set(TailDifferenceAdjustmentDetailEntity::getCreateBy, v.getCreateBy())
                            .set(TailDifferenceAdjustmentDetailEntity::getUpdateBy, v.getCreateBy())
                            .set(isDelete, TailDifferenceAdjustmentDetailEntity::getDelFlag, "1")
                            .isNull(TailDifferenceAdjustmentDetailEntity::getTailDifferenceAdjustmentId)
                            .eq(TailDifferenceAdjustmentDetailEntity::getOrgId, v.getOrgId())
                            .eq(TailDifferenceAdjustmentDetailEntity::getAccountCode, v.getAccountCode())
                            .eq(TailDifferenceAdjustmentDetailEntity::getAccountName, v.getAccountName()).update();
                });
            }
            Date endDateTime = new Date();
            log.info("尾差跑数据结束时间：{}", endDateTime);
            log.info("尾差跑数据共用时：{}", DateUtil.between(startDateTime, endDateTime, DateUnit.SECOND));
        } catch (Exception e) {
            log.error("TailDifferenceAdjustmentService asyncInitData fail!", e);
            throw new ServiceException(e.getMessage());
        } finally {
            iDataExecutionTaskService.finishedTask(taskId, DataExecutionTaskStatusEnum.SUCCESS.getCode(), 0, 0);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteByIds(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<TailDifferenceAdjustmentEntity> adjustmentEntityList = this.listByIds(idList);
        adjustmentEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("处理状态为已录入的才可以删除");
            }
        });
        this.removeBatchByIds(idList);
        batchDeleteVoucher(idList);
        return iTailDifferenceAdjustmentDetailService.removeBatcheByDetailId(idList);
    }

    @Override
    public Boolean submit(List<Long> idList) {
        //校验任务
        idList.forEach(v -> {
            Boolean isExistFlag = iBatchTaskService.isExistTask(v, BatchTypeEnum.WCTZ.getCode());
            if (isExistFlag) {
                throw new ServiceException("存在任务正在执行，请稍后重试");
            }
        });
        //保存任务
        List<Long> taskIdList = Lists.newArrayList();
        idList.forEach(v -> {
            taskIdList.add(iBatchTaskService.saveBatchTask(BatchTaskDTO.builder().businessId(v).businessType(BatchTypeEnum.WCTZ.getCode()).status("1").build()));
        });
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        CompletableFuture.runAsync(() -> {
            iVoucherService.deleteByBatchIdList(idList, BatchTypeEnum.WCTZ.getCode());
            List<TailDifferenceAdjustmentEntity> adjustmentEntityList = this.listByIds(idList);
            List<ApproveDTO> approveDTOList = Lists.newArrayList();
            adjustmentEntityList.stream().forEach(v -> {
                if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                    throw new ServiceException("只有处理状态为已录入的才可以提交");
                }
                v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                ApproveDTO approveDTO = new ApproveDTO();
                approveDTO.setDocumentId(v.getId());
                approveDTO.setDocumentType(BatchTypeEnum.WCTZ.getCode());
                approveDTO.setUrl(approveUrl + v.getId());
                approveDTOList.add(approveDTO);
            });
            //发送审核
            Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);
            adjustmentEntityList.stream().forEach(v -> {
                if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                    v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
                    v.setAccountDate(LocalDateTime.now());
                }
            });
            Boolean isGenerateVoucher = generateVoucher(idList, YesOrNoEnum.YES.getCode());
            if (!isGenerateVoucher) {
                throw new ServiceException("凭证存在未生成，不可以提交");
            }
            this.updateBatchById(adjustmentEntityList);
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            iBatchTaskService.updateBatchTask(taskIdList, "2");
        }).exceptionally(e -> {
            log.info("尾差调整提交处理数据失败", e);
            iBatchTaskService.updateBatchTask(taskIdList, "3");
            return null;
        });
        return Boolean.TRUE;
    }

    @Override
    public Boolean withdraw(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<TailDifferenceAdjustmentEntity> adjustmentEntityList = this.listByIds(idList);
        adjustmentEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(adjustmentEntityList.stream().map(TailDifferenceAdjustmentEntity::getProcessInstanceId).collect(Collectors.toList()));
        iVoucherService.updateStatusByBatch(idList, BatchTypeEnum.WCTZ.getCode(), ProcessStatusEnum.ENTERED.getCode(), "", "");
        return this.updateBatchById(adjustmentEntityList);
    }

    @Override
    public Boolean insertVoucher(List<Long> idList, String isSubmit) {
        //校验任务
        idList.forEach(v -> {
            Boolean isExistFlag = iBatchTaskService.isExistTask(v, BatchTypeEnum.WCTZ.getCode());
            if (isExistFlag) {
                throw new ServiceException("存在任务正在执行，请稍后重试");
            }
        });
        //保存任务
        List<Long> taskIdList = Lists.newArrayList();
        idList.forEach(v -> {
            taskIdList.add(iBatchTaskService.saveBatchTask(BatchTaskDTO.builder().businessId(v).businessType(BatchTypeEnum.WCTZ.getCode()).status("1").build()));
        });
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据生成凭证");
        }
        List<TailDifferenceAdjustmentEntity> adjustmentEntityList = this.listByIds(idList);
        adjustmentEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("处理状态为已录入的才可以生成凭证");
            }
        });
        CompletableFuture.runAsync(() -> {
            //先删除未提交数据
            LambdaQueryWrapper<TailDifferenceAdjustmentDetailEntity> adjustmentEntityQueryWrapper = new LambdaQueryWrapper<>();
            adjustmentEntityQueryWrapper.in(TailDifferenceAdjustmentDetailEntity::getTailDifferenceAdjustmentId,idList);
            adjustmentEntityQueryWrapper.eq(TailDifferenceAdjustmentDetailEntity::getDelFlag,YesOrNoEnum.NO.getCode());
            List<TailDifferenceAdjustmentDetailEntity> list = iTailDifferenceAdjustmentDetailService.list(adjustmentEntityQueryWrapper);
            asnyDeleteVoucher(list.stream().map(TailDifferenceAdjustmentDetailEntity::getVoucherIds).filter(StringUtils::isNotEmpty).map(Long::parseLong).collect(Collectors.toList()));
            generateVoucher(idList, isSubmit);
        }).whenComplete((v, e) -> {
            // 执行成功，更新任务状态
            iBatchTaskService.updateBatchTask(taskIdList, "2");
        }).exceptionally(e -> {
            log.info("核销批量处理数据失败", e);
            iBatchTaskService.updateBatchTask(taskIdList, "3");
            return null;
        });
        return Boolean.TRUE;
    }


    @Override
    public Boolean generateVoucher(List<Long> idList, String isSubmit) {
        List<TailDifferenceAdjustmentDetailEntity> detailEntityList = iTailDifferenceAdjustmentDetailService.lambdaQuery().in(TailDifferenceAdjustmentDetailEntity::getTailDifferenceAdjustmentId, idList).eq(TailDifferenceAdjustmentDetailEntity::getDelFlag, YesOrNoEnum.NO.getCode()).list();
        if (CollectionUtils.isEmpty(detailEntityList)) {
            return Boolean.TRUE;
        }
        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        Map<String, AccountEntity> accountEntityMap = getAccountCodeMap();
        //是否到期是否到期isOverdued：按合同+签约主体查合同表到期日，1.判断到期日>当前日期，则赋值否；2.判断到期日<=当前日期，则赋值是
        List<String> contractCodeList = detailEntityList.stream().map(TailDifferenceAdjustmentDetailEntity::getContractCode).distinct().collect(Collectors.toList());
        List<String> orgIdList = detailEntityList.stream().map(TailDifferenceAdjustmentDetailEntity::getOrgId).distinct().collect(Collectors.toList());
        List<ContractEntity> contractEntityList = iContractService.lambdaQuery().in(ContractEntity::getContractCode, contractCodeList).in(ContractEntity::getOrgId, orgIdList).list();
        Map<String, ContractEntity> contractEntityMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(contractEntityList)) {
            contractEntityMap = contractEntityList.stream().collect(Collectors.toMap(v -> v.getOrgId() + "-" + v.getContractCode(), Function.identity(), BinaryOperator.maxBy(Comparator.comparingLong(ContractEntity::getId))));
        }
        Map<String, ContractEntity> finalContractEntityMap = contractEntityMap;
        detailEntityList.forEach(v -> {
            String key = v.getOrgId() + "-" + v.getContractCode();
            ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
            executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            executeCommonDTO.setBusinessCode(v.getBusinessCode());
            executeCommonDTO.setSceneCode(SceneEnum.WCTZ.getCode());
            executeCommonDTO.setSceneName(SceneEnum.WCTZ.name());
            executeCommonDTO.setOrderId(v.getId().toString());
            executeCommonDTO.setBusinessDate(DateUtil.date(v.getBusinessDate()));
            executeCommonDTO.setContractCode(v.getContractCode());
            executeCommonDTO.setOrgId(v.getOrgId());
            executeCommonDTO.setAccountDate(new Date());
            executeCommonDTO.setBatchId(v.getTailDifferenceAdjustmentId());
            executeCommonDTO.setBatchType(BatchTypeEnum.WCTZ.getCode());
            executeCommonDTO.setFinanceDate(v.getBusinessDate());
            executeCommonDTO.setClientCode(getClientCode(v));
            executeCommonDTO.setAccountCode(v.getAccountCode());
            executeCommonDTO.setAccountName(v.getAccountName());
            executeCommonDTO.setAccountBalance(v.getAccountBalance());
            String isOverDued = YesOrNoEnum.NO.getDesc();
            if (finalContractEntityMap.containsKey(key) && null != finalContractEntityMap.get(key).getLeaseDateEnd()) {
                if (finalContractEntityMap.get(key).getLeaseDateEnd().compareTo(new Date()) <= 0) {
                    isOverDued = YesOrNoEnum.YES.getDesc();
                }
            }
            executeCommonDTO.setIsOverdued(isOverDued);
            executeCommonDTO.setIsSubmit(isSubmit);
            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
            String fundType = accountEntityMap.get(v.getAccountCode()).getFundType() + "_balance";
            dataMap.put(fundType, v.getAccountBalance());
            if (null != v.getRentalIncomeAfterTotal() && v.getRentalIncomeAfterTotal().compareTo(BigDecimal.ZERO) > 0) {
                dataMap.put("remainingAmortizationAmount", v.getRentalIncomeAfterTotal());
            } else {
                dataMap.put("remainingAmortizationAmount", v.getRentalIncomeAfterLeaseTotal());
            }
            dataMap.put("receivableRent", v.getReceivableRent());
            dataMap.put("receivableResidualValue", v.getReceivableResidualValue());
            dataMap.put("payableDevice", v.getPayableDevice());
            dataMap.put("payableOther", v.getPayableOther());
            voucherMapList.add(dataMap);
        });
        log.info("尾差生成凭证参数：{}", JSON.toJSON(voucherMapList));
        List<VoucherInfoVO> voucherResultList = iRuleService.batchExecuteRule(voucherMapList);
        Boolean isExistVoucherError = voucherResultList.stream().allMatch(v -> StringUtils.isNotEmpty(v.getErrorInfo()));
        if (YesOrNoEnum.YES.getCode().equals(isSubmit) && isExistVoucherError) {
            //异步删除已生成的凭证
            List<Long> voucherIdList = Lists.newArrayList();
            voucherResultList.stream().forEach(voucherInfoVO -> {
                if (CollectionUtils.isNotEmpty(voucherInfoVO.getVoucherDTOList())) {
                    voucherIdList.addAll(voucherInfoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).collect(Collectors.toList()));
                }
            });
            asnyDeleteVoucher(voucherIdList);
        }
        for (VoucherInfoVO infoVO : voucherResultList) {
            String voucherIds = "";
            Long batchId = 0L;
            String errorInfo = "";
            LocalDateTime voucherDate = null;
            if (StringUtils.isNotEmpty(infoVO.getErrorInfo())) {
                errorInfo = infoVO.getErrorInfo();
                if (infoVO.getErrorInfo().length() > 2000) {
                    errorInfo = infoVO.getErrorInfo().substring(0, 2000);
                }
            }
            if (CollectionUtils.isNotEmpty(infoVO.getVoucherDTOList())) {
                voucherIds = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).map(String::valueOf).collect(Collectors.toList()).stream().collect(Collectors.joining(","));
                batchId = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getBatchId).findFirst().orElse(0L);
                voucherDate = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getVoucherDate).filter(Objects::nonNull).findFirst().orElse(null);
            }
            iTailDifferenceAdjustmentDetailService.lambdaUpdate()
                    .set(StringUtils.isEmpty(voucherIds) && StringUtils.isEmpty(errorInfo), TailDifferenceAdjustmentDetailEntity::getDelFlag, "1")
                    .set(TailDifferenceAdjustmentDetailEntity::getVoucherIds, voucherIds)
                    .set(TailDifferenceAdjustmentDetailEntity::getErrorInfo, errorInfo)
                    .set(voucherDate != null, TailDifferenceAdjustmentDetailEntity::getAccountDate, voucherDate)
                    .set(TailDifferenceAdjustmentDetailEntity::getUpdateTime, LocalDateTime.now())
                    .eq(TailDifferenceAdjustmentDetailEntity::getId, Long.parseLong(infoVO.getOrderId())).update();
            this.lambdaUpdate().set(voucherDate != null, TailDifferenceAdjustmentEntity::getAccountDate, voucherDate)
                    .set(TailDifferenceAdjustmentEntity::getUpdateTime, LocalDateTime.now())
                    .eq(TailDifferenceAdjustmentEntity::getId, batchId).update();
        }
        if (YesOrNoEnum.NO.getCode().equals(isSubmit) && !isExistVoucherError) {
            updateIsGenerateVoucher(idList, isSubmit);
        }
        return Boolean.TRUE;
    }

    public void batchDeleteVoucher(List<Long> ids) {
        //获取所有的凭证Id
        List<TailDifferenceAdjustmentDetailEntity> detailsEntityList = iTailDifferenceAdjustmentDetailService.lambdaQuery().in(TailDifferenceAdjustmentDetailEntity::getTailDifferenceAdjustmentId, ids).list();
        //逗号拆分
        List<Long> voucherIdList = Lists.newArrayList();
        detailsEntityList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherIds())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherIds().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
            v.setVoucherIds("");
        });
        if (CollectionUtils.isNotEmpty(voucherIdList)) {
            iVoucherService.deleteByIdList(voucherIdList);
        }
        iTailDifferenceAdjustmentDetailService.updateBatchById(detailsEntityList);
    }

    public Map<String, List<AccountEntity>> getFundTypeMap() {
        //业务编码+金额类型分组
        List<AccountEntity> accountEntityList = iAccountService.list().stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(v -> v.getFundType() + "-" + v.getAccountCode() + v.getBusinessCode()))), ArrayList::new));
        Map<String, List<AccountEntity>> accountEntityMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(accountEntityList)) {
            accountEntityMap = accountEntityList.stream().collect(Collectors.groupingBy(v -> v.getBusinessCode() + "-" + v.getFundType()));
        }
        return accountEntityMap;
    }

    public Map<String, AccountEntity> getAccountCodeMap() {
        List<AccountEntity> accountEntityList = iAccountService.list().stream().collect(Collectors.toList());
        Map<String, AccountEntity> accountEntityMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(accountEntityList)) {
            accountEntityMap = accountEntityList.stream().collect(Collectors.groupingBy(v -> v.getAccountCode(), Collectors.collectingAndThen(
                    Collectors.maxBy(Comparator.comparingLong(AccountEntity::getId)),
                    Optional::get)));
        }
        return accountEntityMap;
    }


    @Override
    public Boolean updateProcessStatus(CommonApproveDTO commonApproveDTO) {
        if (StringUtils.isEmpty(commonApproveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        TailDifferenceAdjustmentEntity adjustmentEntity = this.getById(commonApproveDTO.getDocumentId());
        if (null == adjustmentEntity) {
            throw new ServiceException("尾差调整信息数据不存在");
        }
        String processStatus = adjustmentEntity.getProcessStatus();
        try {
            if (ProcessStatusEnum.REVIEWED.getCode().equals(commonApproveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REVIEWED.getCode();
            } else if (ProcessStatusEnum.REJECTED.getCode().equals(commonApproveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REJECTED.getCode();
            }
            //更新凭证状态
            iVoucherService.updateStatusByBatch(Lists.newArrayList(adjustmentEntity.getId()), BatchTypeEnum.WCTZ.getCode(), processStatus, commonApproveDTO.getApproverNum(), commonApproveDTO.getApproverName());
            adjustmentEntity.setProcessStatus(processStatus);
            adjustmentEntity.setApproveErrorInfo("");
        } catch (Exception e) {
            adjustmentEntity.setApproveErrorInfo(e.getMessage());
        }
        return this.updateById(adjustmentEntity);
    }

    @Override
    public List<TailDifferenceAdjustmentDetailVO> listByConditionByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据导出");
        }
        TailDifferenceAdjustmentDetailQueryDTO queryDTO = new TailDifferenceAdjustmentDetailQueryDTO();
        queryDTO.setTailDifferenceIdList(idList);
        return iTailDifferenceAdjustmentDetailService.selectByParams(queryDTO);
    }


    public void updateIsGenerateVoucher(List<Long> idList, String isSubmit) {
        if (YesOrNoEnum.YES.getCode().equals(isSubmit)) {
            return;
        }
        //获取详情信息
        Map<Long, List<TailDifferenceAdjustmentDetailEntity>> detailEntityMap = iTailDifferenceAdjustmentDetailService.lambdaQuery()
                .in(TailDifferenceAdjustmentDetailEntity::getTailDifferenceAdjustmentId, idList).eq(TailDifferenceAdjustmentDetailEntity::getDelFlag,YesOrNoEnum.NO.getCode()).list().stream().collect(Collectors.groupingBy(TailDifferenceAdjustmentDetailEntity::getTailDifferenceAdjustmentId));
        for (Map.Entry<Long, List<TailDifferenceAdjustmentDetailEntity>> entry : detailEntityMap.entrySet()) {
            Boolean isExistEmpty = entry.getValue().stream().anyMatch(v -> StringUtils.isEmpty(v.getVoucherIds()));
            String isGenerateVoucher = YesOrNoEnum.NO.getCode();
            LocalDateTime accountDate = null;
            if (!isExistEmpty) {
                isGenerateVoucher = YesOrNoEnum.YES.getCode();
                accountDate = entry.getValue().get(0).getAccountDate();
            }
            this.lambdaUpdate().set(TailDifferenceAdjustmentEntity::getIsGenerateVoucher, isGenerateVoucher).set(TailDifferenceAdjustmentEntity::getAccountDate, accountDate).eq(TailDifferenceAdjustmentEntity::getId, entry.getKey()).update();
        }
    }

    public void asnyDeleteVoucher(List<Long> voucherIdList) {
        if (CollectionUtils.isEmpty(voucherIdList)) {
            return;
        }
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // 异步任务的代码
            iVoucherService.deleteByIdList(voucherIdList);
        });
    }

    public void setTailDifferenceBalance(TailDifferenceAdjustmentVO v) {
        if (ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
            return;
        }
        //非已录入条件需要过滤未生成凭证的数据
        List<TailDifferenceAdjustmentDetailEntity> detailEntityList = iTailDifferenceAdjustmentDetailService.lambdaQuery()
                .eq(TailDifferenceAdjustmentDetailEntity::getTailDifferenceAdjustmentId, v.getId())
                .list();
        BigDecimal tailDifferenceBalance = detailEntityList.stream().filter(s -> StringUtils.isNotEmpty(s.getVoucherIds())).map(TailDifferenceAdjustmentDetailEntity::getAccountBalance).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        v.setTailDifferenceBalance(tailDifferenceBalance);
    }

    ;

    public void removeExistData(String accountCode, String accountName, Date businessDate) {
        boolean existsFlag = iTailDifferenceAdjustmentDetailService.lambdaQuery()
                .eq(TailDifferenceAdjustmentDetailEntity::getAccountCode, accountCode)
                .eq(TailDifferenceAdjustmentDetailEntity::getAccountName, accountName)
                .eq(TailDifferenceAdjustmentDetailEntity::getBusinessDate, businessDate)
                .isNull(TailDifferenceAdjustmentDetailEntity::getTailDifferenceAdjustmentId).exists();
        if (existsFlag) {
            //清空已录入状态下科目，科目名称，日期对应的数据
            List<TailDifferenceAdjustmentEntity> entityList = this.lambdaQuery().eq(TailDifferenceAdjustmentEntity::getAccountCode, accountCode)
                    .eq(TailDifferenceAdjustmentEntity::getAccountName, accountName)
                    .eq(TailDifferenceAdjustmentEntity::getBusinessDate, businessDate)
                    .eq(TailDifferenceAdjustmentEntity::getProcessStatus, ProcessStatusEnum.ENTERED.getCode()).list();
            if (CollectionUtils.isNotEmpty(entityList)) {
                List<Long> idList = entityList.stream().map(TailDifferenceAdjustmentEntity::getId).collect(Collectors.toList());
                this.removeBatchByIds(idList);
                //清空详情表数据
                iTailDifferenceAdjustmentDetailService.remove(Wrappers.<TailDifferenceAdjustmentDetailEntity>lambdaQuery().in(TailDifferenceAdjustmentDetailEntity::getTailDifferenceAdjustmentId, idList));
            }
        }
    }

    public String getClientCode(TailDifferenceAdjustmentDetailEntity v) {
        /**
         * 客户编码取值逻辑：
         * 1.当科目名称in('应付租赁设备款_暂估','应付其他款项_暂估')，且合同表的租赁类型为’直租'时，按合同+签约主体取最新余额表客户类别为供应商时的客户编码；
         * 2.当科目名称in('应付经销商服务费_暂估')，按合同+签约主体取最新余额表客户类别为经销商时的客户编码；
         * 3.非以上任一条件的，按合同+签约主体取合同表客户；
         */
        List<ContractEntity> contractEntityList = iContractService.lambdaQuery().eq(ContractEntity::getContractCode, v.getContractCode()).eq(ContractEntity::getOrgId, v.getOrgId()).list();
        String clientCode = v.getClientCode();
        ContractEntity contractEntity = null;
        if (CollectionUtils.isEmpty(contractEntityList)) {
            return clientCode;
        }
        contractEntity = contractEntityList.get(0);
        if (("应付租赁设备款_暂估").equals(v.getAccountName()) || ("应付其他款项_暂估").equals(v.getAccountName()) && LeaseTypeEnum.DIRECT.getCode().equals(contractEntity.getLeaseType())) {
            ContractBalanceLatestEntity entity = iContractBalanceLatestService.lambdaQuery().eq(ContractBalanceLatestEntity::getContractCode, v.getContractCode()).eq(ContractBalanceLatestEntity::getOrgId, v.getOrgId()).eq(ContractBalanceLatestEntity::getClientType, "供应商").orderByDesc(ContractBalanceLatestEntity::getId).last("limit 1").one();
            if (null != entity) {
                clientCode = entity.getClientCode();
            }
        } else if (("应付经销商服务费_暂估").equals(v.getAccountName())) {
            ContractBalanceLatestEntity entity = iContractBalanceLatestService.lambdaQuery().eq(ContractBalanceLatestEntity::getContractCode, v.getContractCode()).eq(ContractBalanceLatestEntity::getOrgId, v.getOrgId()).eq(ContractBalanceLatestEntity::getClientType, "经销商").orderByDesc(ContractBalanceLatestEntity::getId).last("limit 1").one();
            if (null != entity) {
                clientCode = entity.getClientCode();
            }
        } else {
            clientCode = contractEntity.getClientCode();
        }
        return clientCode;
    }
}

