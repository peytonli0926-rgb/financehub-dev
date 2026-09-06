package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.model.dto.RentIncomeConfirmQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RentIncomeConfirmDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RentIncomeConfirmVO;
import com.utfinancing.financehub.engine.finance.entity.RentIncomeConfirmEntity;
import com.utfinancing.financehub.engine.finance.mapper.RentIncomeConfirmMapper;
import com.utfinancing.financehub.engine.finance.service.IRentIncomeConfirmService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.PeriodCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-10
 * @Description :  RentIncomeConfirm服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class RentIncomeConfirmServiceImpl extends ServiceImpl<RentIncomeConfirmMapper, RentIncomeConfirmEntity> implements IRentIncomeConfirmService {

    private final RentIncomeConfirmMapper rentIncomeConfirmMapper;
    private final IRuleService iRuleService;
    private final IVoucherService iVoucherService;

    @Value("${approve.url.rentIncomeConfirm-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;

    @Override
    public Long saveRentIncomeConfirm(RentIncomeConfirmDTO dto) {
        RentIncomeConfirmEntity entity = BeanUtil.copyProperties(dto, RentIncomeConfirmEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateRentIncomeConfirm(Long id, RentIncomeConfirmDTO dto) {
        RentIncomeConfirmEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public RentIncomeConfirmDTO getRentIncomeConfirmDTOById(Long id) {
        RentIncomeConfirmEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, RentIncomeConfirmDTO.class);
    }

    @Override
    public IPage<RentIncomeConfirmVO> selectPage(RentIncomeConfirmQueryDTO queryDTO) {
        LambdaQueryWrapper<RentIncomeConfirmEntity> queryWrapper = Wrappers.<RentIncomeConfirmEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        IPage<RentIncomeConfirmEntity> entityIPage = rentIncomeConfirmMapper.selectPage(new Page<RentIncomeConfirmEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, RentIncomeConfirmVO.class);
    }

    private void setQueryCondition(RentIncomeConfirmQueryDTO queryDTO, LambdaQueryWrapper<RentIncomeConfirmEntity> queryWrapper) {
        if (ObjectUtil.isNotEmpty(queryDTO.getId())) {
            queryWrapper.eq(RentIncomeConfirmEntity::getId, queryDTO.getId());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getContractCode())) {
            queryWrapper.like(RentIncomeConfirmEntity::getContractCode, queryDTO.getContractCode());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getIdList())) {
            queryWrapper.in(RentIncomeConfirmEntity::getId, queryDTO.getIdList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList())) {
            queryWrapper.in(RentIncomeConfirmEntity::getProcessStatus, queryDTO.getProcessStatusList());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getAccountMonth())) {
            queryWrapper.apply("to_char(account_month,'YYYY-MM')={0}", DateUtil.format(queryDTO.getAccountMonth(), "yyyy-MM"));
        }
        queryWrapper.orderByDesc(RentIncomeConfirmEntity::getAccountMonth);
    }

    /**
     * 根据合同编号查询
     *
     * @param contractCode
     * @return
     */
    @Override
    public List<RentIncomeConfirmEntity> getByContractCode(String contractCode) {
        List<RentIncomeConfirmEntity> list = list(new LambdaQueryWrapper<RentIncomeConfirmEntity>().eq(RentIncomeConfirmEntity::getContractCode, contractCode));
        return list;
    }

    /**
     * 根据合同编号删除数据
     *
     * @param contractCode
     */
    @Override
    public void deleteByContractCode(String contractCode) {
        remove(new LambdaQueryWrapper<RentIncomeConfirmEntity>().eq(RentIncomeConfirmEntity::getContractCode, contractCode));
    }

    @Override
    public List<RentIncomeConfirmVO> selectList(RentIncomeConfirmQueryDTO queryDTO) {
        LambdaQueryWrapper<RentIncomeConfirmEntity> queryWrapper = Wrappers.<RentIncomeConfirmEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        List<RentIncomeConfirmEntity> entityIPage = rentIncomeConfirmMapper.selectList(queryWrapper);
        return ListBeanUtil.copyList(entityIPage, RentIncomeConfirmVO.class);
    }

    /**
     * 批量生成收入确认凭证
     *
     * @param ids
     * @param isSubmit
     * @return
     */
    @Override
    public Boolean generateVoucher(List<Long> ids, String isSubmit) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少选择一条数据生成凭证");
        }
        List<RentIncomeConfirmEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("处理状态为已录入或者已拒绝的才可以生成凭证");
            }
        });

        //生成凭证前先删除之前的凭证
        batchDeleteVoucher(ids);

        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
            executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            executeCommonDTO.setSceneCode(SceneEnum.DZZCSR.getCode());
            executeCommonDTO.setSceneName(SceneEnum.DZZCSR.name());
            executeCommonDTO.setOrderId(v.getId().toString());
            executeCommonDTO.setOrgId(v.getOrgId());

            LocalDateTime localDateTime = DateUtil.toLocalDateTime(v.getAccountMonth());
            Date accountDate = DateUtils.toDate(localDateTime.with(TemporalAdjusters.lastDayOfMonth()));

            executeCommonDTO.setBusinessDate(accountDate);
            executeCommonDTO.setContractCode(v.getContractCode());
            executeCommonDTO.setClientCode(v.getClientCode());
            executeCommonDTO.setAccountDate(accountDate);
            executeCommonDTO.setBatchId(v.getId());
            executeCommonDTO.setBatchType(BatchTypeEnum.DZZCCZDJSRQR.getCode());
            executeCommonDTO.setIsSubmit(isSubmit);

            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
            dataMap.put("accountingMonth", DateUtil.format(v.getAccountMonth(), "yyyy-MM"));
            dataMap.put("receivableRentAmount", v.getThisMonthReceivableRent());
            dataMap.put("taxAccrual", v.getThisMonthTax());
            dataMap.put("revenueAmount", v.getThisMonthRentIncome());
            voucherMapList.add(dataMap);
        });

        List<VoucherInfoVO> voucherResultList = iRuleService.batchExecuteRule(voucherMapList);
        Boolean isExistVoucherError = voucherResultList.stream().allMatch(v -> StringUtils.isNotEmpty(v.getErrorInfo()));
        if (YesOrNoEnum.YES.getCode().equals(isSubmit) && isExistVoucherError) {
            // 异步删除已生成的凭证
            List<Long> voucherIdList = Lists.newArrayList();
            voucherResultList.stream().forEach(voucherInfoVO -> {
                if (CollectionUtils.isNotEmpty(voucherInfoVO.getVoucherDTOList())) {
                    voucherIdList.addAll(voucherInfoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).collect(Collectors.toList()));
                }
            });
            asnyDeleteVoucher(voucherIdList);
            return Boolean.FALSE;
        }
        for (VoucherInfoVO infoVO : voucherResultList) {
            String voucherIds = "";
            String errorInfo = "";
            String isGenerateVoucher = YesOrNoEnum.YES.getCode();
            if (StringUtils.isNotEmpty(infoVO.getErrorInfo())) {
                errorInfo = infoVO.getErrorInfo();
                if (infoVO.getErrorInfo().length() > 2000) {
                    errorInfo = infoVO.getErrorInfo().substring(0, 2000);
                }
                isGenerateVoucher = YesOrNoEnum.NO.getCode();
            }
            RentIncomeConfirmEntity confirmEntity = this.getById(Long.parseLong(infoVO.getOrderId()));
            confirmEntity.setAccountDate(LocalDateTime.now());
            if (CollectionUtils.isNotEmpty(infoVO.getVoucherDTOList())) {
                voucherIds = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).map(String::valueOf).collect(Collectors.toList()).stream().collect(Collectors.joining(","));
                confirmEntity.setAccountDate(infoVO.getVoucherDTOList().get(0).getVoucherDate());
            }
            if (ObjectUtil.isEmpty(confirmEntity.getSceneCode())) {
                confirmEntity.setSceneCode(SceneEnum.DZZCSR.getCode());
            } else if (!StrUtil.contains(confirmEntity.getSceneCode(), SceneEnum.DZZCSR.getCode())) {
                // 场景里没有此场景编码，则添加
                confirmEntity.setSceneCode(confirmEntity.getSceneCode() + "," + SceneEnum.DZZCSR.getCode());
            }
            confirmEntity.setVoucherIdDzzcsr(voucherIds);
            confirmEntity.setIsGenerateVoucher(isGenerateVoucher);
            confirmEntity.setErrorInfo(errorInfo);
            String voucherId = getVoucherIdStr(confirmEntity);
            confirmEntity.setVoucherId(voucherId);
            confirmEntity.setUpdateTime(null);
            this.updateById(confirmEntity);
        }
        return Boolean.TRUE;
    }

    /**
     * 获取凭证id
     *
     * @param confirmEntity
     * @return
     */
    private String getVoucherIdStr(RentIncomeConfirmEntity confirmEntity) {
        String voucherId = "";
        if (StringUtils.isNotEmpty(confirmEntity.getVoucherIdDzzcsr()) && StringUtils.isNotEmpty(confirmEntity.getVoucherIdDzzcjz())) {
            voucherId = confirmEntity.getVoucherIdDzzcsr() + "," + confirmEntity.getVoucherIdDzzcjz();
        } else if (StringUtils.isNotEmpty(confirmEntity.getVoucherIdDzzcsr())) {
            voucherId = confirmEntity.getVoucherIdDzzcsr();
        } else if (StringUtils.isNotEmpty(confirmEntity.getVoucherIdDzzcjz())) {
            voucherId = confirmEntity.getVoucherIdDzzcjz();
        }
        return voucherId;
    }

    /**
     * 批量生成结转凭证
     *
     * @param ids
     * @param isSubmit
     * @return
     */
    @Override
    public Boolean generateCarryForwardVoucher(List<Long> ids, String isSubmit) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少选择一条数据生成凭证");
        }
        List<RentIncomeConfirmEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("处理状态为已录入或者已拒绝的才可以生成凭证");
            }
        });

        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
            executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            executeCommonDTO.setSceneCode(SceneEnum.DZZCJZ.getCode());
            executeCommonDTO.setSceneName(SceneEnum.DZZCJZ.name());
            executeCommonDTO.setOrderId(v.getId().toString());
            executeCommonDTO.setOrgId(v.getOrgId());

            LocalDateTime localDateTime = DateUtil.toLocalDateTime(v.getAccountMonth());
            Date accountDate = DateUtils.toDate(localDateTime.with(TemporalAdjusters.lastDayOfMonth()));
            executeCommonDTO.setBusinessDate(accountDate);
            executeCommonDTO.setContractCode(v.getContractCode());
            executeCommonDTO.setClientCode(v.getClientCode());
            executeCommonDTO.setAccountDate(accountDate);
            executeCommonDTO.setBatchId(v.getId());
            executeCommonDTO.setBatchType(BatchTypeEnum.DZZCCZDJSRJZ.getCode());
            executeCommonDTO.setIsSubmit(isSubmit);

            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
            // 按合同+签约主体+客户查余额表，场景为DZZCJZ，创建时间为最新的receivable_rent_investment_property_amount
            BigDecimal previousCarryOverAmount = rentIncomeConfirmMapper.selectPreviousCarryOverAmount(v.getContractCode(), v.getClientCode(), v.getOrgId());
            dataMap.put("previousCarryOverAmount", NumberUtil.toBigDecimal(previousCarryOverAmount));// 上月结转金额
            // 按合同+签约主体+客户查最新余额表receivable_rent_investment_property_balance
            BigDecimal carryOverAmount = rentIncomeConfirmMapper.selectCarryOverAmount(v.getContractCode(), v.getClientCode(), v.getOrgId());
            dataMap.put("CarryOverAmount", NumberUtil.toBigDecimal(carryOverAmount));
            voucherMapList.add(dataMap);
        });

        List<VoucherInfoVO> voucherResultList = iRuleService.batchExecuteRule(voucherMapList);
        Boolean isExistVoucherError = voucherResultList.stream().allMatch(v -> StringUtils.isNotEmpty(v.getErrorInfo()));
        if (YesOrNoEnum.YES.getCode().equals(isSubmit) && isExistVoucherError) {
            // 异步删除已生成的凭证
            List<Long> voucherIdList = Lists.newArrayList();
            voucherResultList.stream().forEach(voucherInfoVO -> {
                if (CollectionUtils.isNotEmpty(voucherInfoVO.getVoucherDTOList())) {
                    voucherIdList.addAll(voucherInfoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).collect(Collectors.toList()));
                }
            });
            asnyDeleteVoucher(voucherIdList);
            return Boolean.FALSE;
        }
        for (VoucherInfoVO infoVO : voucherResultList) {
            String voucherIds = "";
            String errorInfo = "";
            String isGenerateVoucher = YesOrNoEnum.YES.getCode();
            if (StringUtils.isNotEmpty(infoVO.getErrorInfo())) {
                errorInfo = infoVO.getErrorInfo();
                if (infoVO.getErrorInfo().length() > 2000) {
                    errorInfo = infoVO.getErrorInfo().substring(0, 2000);
                }
                isGenerateVoucher = YesOrNoEnum.NO.getCode();
            }
            RentIncomeConfirmEntity confirmEntity = this.getById(Long.parseLong(infoVO.getOrderId()));
            confirmEntity.setAccountDate(LocalDateTime.now());
            if (CollectionUtils.isNotEmpty(infoVO.getVoucherDTOList())) {
                voucherIds = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).map(String::valueOf).collect(Collectors.toList()).stream().collect(Collectors.joining(","));
                confirmEntity.setAccountDate(infoVO.getVoucherDTOList().get(0).getVoucherDate());
            }

            if (ObjectUtil.isEmpty(confirmEntity.getSceneCode())) {
                confirmEntity.setSceneCode(SceneEnum.DZZCJZ.getCode());
            } else if (!StrUtil.contains(confirmEntity.getSceneCode(), SceneEnum.DZZCJZ.getCode())) {
                // 场景里没有此场景编码，则添加
                confirmEntity.setSceneCode(confirmEntity.getSceneCode() + "," + SceneEnum.DZZCJZ.getCode());
            }
            confirmEntity.setVoucherIdDzzcjz(voucherIds);
            confirmEntity.setIsGenerateVoucher(isGenerateVoucher);
            confirmEntity.setErrorInfo(errorInfo);
            String voucherId = getVoucherIdStr(confirmEntity);
            confirmEntity.setVoucherId(voucherId);
            confirmEntity.setUpdateTime(null);
            this.updateById(confirmEntity);
        }
        return Boolean.TRUE;
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
     * 批量提交
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean submit(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<RentIncomeConfirmEntity> entityList = this.listByIds(ids);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
            if (ObjectUtil.equal(v.getIsGenerateVoucher(), YesOrNoEnum.NO.getCode())) {
                throw new ServiceException("生成凭证后才可以提交");
            }

            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.DZZCCZDJSRQR.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);
        });

        Boolean generateVoucherFlag = true;
        for (RentIncomeConfirmEntity entity : entityList) {
            if (StrUtil.contains(entity.getSceneCode(), SceneEnum.DZZCSR.getCode())) {
                // 包含抵债资产收入 凭证
                Boolean generateVoucherFlagSingle = generateVoucher(Lists.newArrayList(entity.getId()), YesOrNoEnum.YES.getCode());
                if (!generateVoucherFlagSingle) {
                    generateVoucherFlag = false;
                }
            }
            if (StrUtil.contains(entity.getSceneCode(), SceneEnum.DZZCJZ.getCode())) {
                // 包含抵债资产结转 凭证
                Boolean generateVoucherFlagSingle = generateCarryForwardVoucher(Lists.newArrayList(entity.getId()), YesOrNoEnum.YES.getCode());
                if (!generateVoucherFlagSingle) {
                    generateVoucherFlag = false;
                }
            }
        }

        if (generateVoucherFlag) {
            // 凭证生成成功
            // 发送审核
            Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);
            // 查询最新的数据(凭证号已经更新了)
            List<RentIncomeConfirmEntity> newEntityList = this.listByIds(ids);
            newEntityList.stream().forEach(v -> {
                v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                    v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
                }
            });
            return this.updateBatchById(newEntityList);
        } else {
            // 凭证生成失败
            return Boolean.FALSE;
        }
    }

    /**
     * 批量撤回
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<RentIncomeConfirmEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            v.setVoucherId("");
            v.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
        });
        iApproveService.withdraw(entityList.stream().map(RentIncomeConfirmEntity::getProcessInstanceId).collect(Collectors.toList()));
        // 删除凭证
        batchDeleteVoucher(ids);
        return this.updateBatchById(entityList);
    }

    /**
     * 删除凭证
     *
     * @param ids
     */
    public void batchDeleteVoucher(List<Long> ids) {
        // 获取所有的凭证Id
        List<RentIncomeConfirmEntity> entityList = this.listByIds(ids);
        // 逗号拆分
        List<Long> voucherIdList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherId())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
        });
        if (CollectionUtils.isNotEmpty(voucherIdList)) {
            iVoucherService.deleteByIdList(voucherIdList);
        }
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
        RentIncomeConfirmEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("转入登记数据不存在");
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
    public void updateVoucherStatus(List<Long> ids, CommonApproveDTO approveDTO){
        // 获取所有的凭证Id
        List<RentIncomeConfirmEntity> entityList = this.listByIds(ids);
        // 逗号拆分
        List<String> voucherIdList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherId())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherId().split(",")).collect(Collectors.toList()));
            }
        });
        iVoucherService.updateStatusByids(voucherIdList, approveDTO.getDocumentStatus(),
                approveDTO.getApproverNum(), approveDTO.getApproverName());
    }

}

