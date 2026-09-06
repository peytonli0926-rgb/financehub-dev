package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.LongIncomeConfirmQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LongIncomeConfirmDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LongIncomeConfirmVO;
import com.utfinancing.financehub.engine.finance.mapper.LongIncomeConfirmMapper;
import com.utfinancing.financehub.engine.finance.service.ILongIncomeConfirmService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-15
 * @Description :  LongIncomeConfirm服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class LongIncomeConfirmServiceImpl extends ServiceImpl<LongIncomeConfirmMapper, LongIncomeConfirmEntity> implements ILongIncomeConfirmService {

    private final LongIncomeConfirmMapper longIncomeConfirmMapper;
    private final IRuleService iRuleService;
    private final IVoucherService iVoucherService;

    private final RabbitTemplate rabbitTemplate;

    @Value("${approve.url.longIncomeConfirm-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;

    @Override
    public Long saveLongIncomeConfirm(LongIncomeConfirmDTO dto) {
        LongIncomeConfirmEntity entity = BeanUtil.copyProperties(dto, LongIncomeConfirmEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateLongIncomeConfirm(Long id, LongIncomeConfirmDTO dto) {
        LongIncomeConfirmEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public LongIncomeConfirmDTO getLongIncomeConfirmDTOById(Long id) {
        LongIncomeConfirmEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, LongIncomeConfirmDTO.class);
    }

    @Override
    public IPage<LongIncomeConfirmVO> selectPage(LongIncomeConfirmQueryDTO queryDTO) {
        LambdaQueryWrapper<LongIncomeConfirmEntity> queryWrapper = Wrappers.<LongIncomeConfirmEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        IPage<LongIncomeConfirmEntity> entityIPage = longIncomeConfirmMapper.selectPage(new Page<LongIncomeConfirmEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, LongIncomeConfirmVO.class);
    }

    /**
     * 查询条件
     *
     * @param queryDTO
     * @param queryWrapper
     */
    private void setQueryCondition(LongIncomeConfirmQueryDTO queryDTO, LambdaQueryWrapper<LongIncomeConfirmEntity> queryWrapper) {
        if (ObjectUtil.isNotEmpty(queryDTO.getLongReceivableNumber())) {
            queryWrapper.like(LongIncomeConfirmEntity::getLongReceivableNumber, queryDTO.getLongReceivableNumber());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getContractCode())) {
            queryWrapper.like(LongIncomeConfirmEntity::getContractCode, queryDTO.getContractCode());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getIdList())) {
            queryWrapper.in(LongIncomeConfirmEntity::getId, queryDTO.getIdList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList())) {
            queryWrapper.in(LongIncomeConfirmEntity::getProcessStatus, queryDTO.getProcessStatusList());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getAccountMonth())) {
            queryWrapper.apply("to_char(account_month,'YYYY-MM')={0}", DateUtil.format(queryDTO.getAccountMonth(), "yyyy-MM"));
        }
        queryWrapper.orderByDesc(LongIncomeConfirmEntity::getAccountMonth);
    }

    @Override
    public List<LongIncomeConfirmVO> selectList(LongIncomeConfirmQueryDTO queryDTO) {
        LambdaQueryWrapper<LongIncomeConfirmEntity> queryWrapper = Wrappers.<LongIncomeConfirmEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        List<LongIncomeConfirmEntity> list = longIncomeConfirmMapper.selectList(queryWrapper);
        return ListBeanUtil.copyList(list, LongIncomeConfirmVO.class);
    }

    /**
     * 批量生成凭证
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
        List<LongIncomeConfirmEntity> entityList = this.listByIds(ids);
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
            executeCommonDTO.setSceneCode(SceneEnum.CQYSKSR.getCode());
            executeCommonDTO.setSceneName(SceneEnum.CQYSKSR.name());
            executeCommonDTO.setOrderId(v.getId().toString());
            executeCommonDTO.setOrgId(v.getOrgId());
            executeCommonDTO.setBusinessDate(new Date());
            executeCommonDTO.setContractCode(v.getContractCode());
            executeCommonDTO.setClientCode(v.getClientCode());
            executeCommonDTO.setAccountDate(new Date());
            executeCommonDTO.setBatchId(v.getId());
            executeCommonDTO.setBatchType(BatchTypeEnum.CQYSKSRQR.getCode());
            executeCommonDTO.setIsSubmit(isSubmit);

            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
            dataMap.put("receivablelongTermCode", v.getLongReceivableNumber());
            dataMap.put("accountingMonth", DateUtil.format(v.getAccountMonth(), "yyyy-MM"));
            dataMap.put("revenueAmount", v.getConfirmIncomeAmount());
            dataMap.put("projectName", v.getProjectName());
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
            if (CollectionUtils.isNotEmpty(infoVO.getVoucherDTOList())) {
                voucherIds = infoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).map(String::valueOf).collect(Collectors.toList()).stream().collect(Collectors.joining(","));
            }

            LongIncomeConfirmEntity receivableRegisterEntity = this.getById(Long.parseLong(infoVO.getOrderId()));
            receivableRegisterEntity.setAccountDate(LocalDateTime.now());
            receivableRegisterEntity.setIsGenerateVoucher(isGenerateVoucher);
            receivableRegisterEntity.setErrorInfo(errorInfo);
            receivableRegisterEntity.setVoucherId(voucherIds);
            receivableRegisterEntity.setUpdateTime(null);
            this.updateById(receivableRegisterEntity);
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
        List<LongIncomeConfirmEntity> entityList = this.listByIds(ids);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.CQYSKSRQR.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);
        });

        // 生成凭证
        Boolean generateVoucherFlag = generateVoucher(ids, YesOrNoEnum.YES.getCode());
        if (generateVoucherFlag) {
            // 发送审核
            Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);

            List<LongIncomeConfirmEntity> newEntityList = this.listByIds(ids);
            newEntityList.stream().forEach(v -> {
                v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                    v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
                }
            });
            // 凭证生成成功
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
        List<LongIncomeConfirmEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(entityList.stream().map(LongIncomeConfirmEntity::getProcessInstanceId).collect(Collectors.toList()));
        return this.updateBatchById(entityList);
    }

    /**
     * 删除凭证
     *
     * @param ids
     */
    @Override
    public void batchDeleteVoucher(List<Long> ids) {
        // 获取所有的凭证Id
        List<LongIncomeConfirmEntity> entityList = this.listByIds(ids);
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
     * 根据长期应收款编号+合同编号删除数据
     *
     * @param longReceivableNumber
     * @param contractCode
     */
    @Override
    public void deleteByLongNumberAndContractCode(String longReceivableNumber, String contractCode) {
        List<LongIncomeConfirmEntity> list = list(new LambdaQueryWrapper<LongIncomeConfirmEntity>().eq(LongIncomeConfirmEntity::getLongReceivableNumber, longReceivableNumber)
                .eq(LongIncomeConfirmEntity::getContractCode, contractCode));
        if (CollectionUtils.isNotEmpty(list)){
            List<Long> ids = list.stream().map(LongIncomeConfirmEntity::getId).collect(Collectors.toList());

            // 删除凭证
            batchDeleteVoucher(ids);
            //删除收入确认数据
            removeBatchByIds(ids);
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
        LongIncomeConfirmEntity entity = this.getById(approveDTO.getDocumentId());
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
        List<LongIncomeConfirmEntity> entityList = this.listByIds(ids);
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

