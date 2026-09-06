package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.InternalTransferVO;
import com.utfinancing.financehub.engine.finance.mapper.InternalTransferMapper;
import com.utfinancing.financehub.engine.finance.model.vo.ParityTransferDetailVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-17
 * @Description :  InternalTransfer服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class InternalTransferServiceImpl extends ServiceImpl<InternalTransferMapper, InternalTransferEntity> implements IInternalTransferService {

    private final InternalTransferMapper internalTransferMapper;
    private final IParityTransferDetailService iParityTransferDetailService;
    private final IParityTransferService iParityTransferService;
    private final IContractBalanceService iContractBalanceService;
    private final IRuleService iRuleService;
    private final IVoucherService iVoucherService;
    private final IBankAccountService iBankAccountService;

    @Value("${approve.url.internalTransfer-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;

    @Override
    public Long saveInternalTransfer(InternalTransferDTO dto) {
        InternalTransferEntity entity = BeanUtil.copyProperties(dto, InternalTransferEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateInternalTransfer(Long id, InternalTransferDTO dto) {
        InternalTransferEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public InternalTransferDTO getInternalTransferDTOById(Long id) {
        InternalTransferEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, InternalTransferDTO.class);
    }

    @Override
    public IPage<InternalTransferVO> selectPage(InternalTransferQueryDTO queryDTO) {
        LambdaQueryWrapper<InternalTransferEntity> queryWrapper = Wrappers.<InternalTransferEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        IPage<InternalTransferEntity> entityIPage = internalTransferMapper.selectPage(new Page<InternalTransferEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, InternalTransferVO.class);
    }

    /**
     * 设置查询条件
     *
     * @param queryDTO
     * @param queryWrapper
     */
    private void setQueryCondition(InternalTransferQueryDTO queryDTO, LambdaQueryWrapper<InternalTransferEntity> queryWrapper) {
        if (ObjectUtil.isNotEmpty(queryDTO.getBatch())) {
            queryWrapper.eq(InternalTransferEntity::getBatch, queryDTO.getBatch());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getPaymentDate())) {
            queryWrapper.apply("to_char(payment_date, 'yyyy-MM-dd')=to_char({0}, 'yyyy-MM-dd')", queryDTO.getPaymentDate());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getIdList())) {
            queryWrapper.in(InternalTransferEntity::getId, queryDTO.getIdList());
        }
        queryWrapper.orderByDesc(InternalTransferEntity::getId);
    }

    @Override
    public List<InternalTransferVO> selectList(InternalTransferQueryDTO queryDTO) {
        LambdaQueryWrapper<InternalTransferEntity> queryWrapper = Wrappers.<InternalTransferEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        List<InternalTransferEntity> list = internalTransferMapper.selectList(queryWrapper);
        return ListBeanUtil.copyList(list, InternalTransferVO.class);
    }

    @Override
    public Boolean importFile(MultipartFile file) {
        try {
            ExcelUtil<InternalTransferExcelDTO> util = new ExcelUtil<InternalTransferExcelDTO>(InternalTransferExcelDTO.class);
            List<InternalTransferExcelDTO> list = util.importExcel(file.getInputStream());
            if (CollectionUtils.isEmpty(list)) {
                throw new ServiceException("没有数据需要上传");
            }
            // 校验数据
            checkDate(list);
            list.stream().forEach(a -> {
                List<InternalTransferEntity> entityList = list(new LambdaQueryWrapper<InternalTransferEntity>().eq(InternalTransferEntity::getBatch, a.getBatch()));
                if (CollectionUtils.isEmpty(entityList)) {
                    throw new ServiceException("根据批次号[" + a.getBatch() + "]未查询到内部调拨数据");
                }
                entityList.stream().forEach(b -> {
                    b.setPaymentDate(DateUtil.toLocalDateTime(a.getPaymentDate()));
                    b.setBankAccountCode(a.getBankAccountCode());
                });
                // 更新数据
                updateBatchById(entityList);
            });

        } catch (Exception e) {
            throw new ServiceException("上传文件失败，失败原因:" + e.getMessage());
        }
        return Boolean.TRUE;
    }

    /**
     * 校验数据
     *
     * @param list
     */
    private void checkDate(List<InternalTransferExcelDTO> list) {
        Set<String> unionSet = new HashSet<>();
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getBatch())) {
                throw new ServiceException("转让批次不能为空");
            }
            if (ObjectUtil.isEmpty(a.getPaymentDate())) {
                throw new ServiceException("支付日期不能为空");
            }
            if (ObjectUtil.isEmpty(a.getBankAccountCode())) {
                throw new ServiceException("银行账号编码不能为空");
            }
            // 校验批次号重复
            if (!unionSet.add(a.getBatch())) {
                throw new ServiceException("文件存在重复的批次号[" + a.getBatch() + "],请检查");
            }
            // 校验 银行账号
            List<BankAccountEntity> bankAccountEntityList = iBankAccountService.selectByBankAccountCode(a.getBankAccountCode());
            if (CollectionUtils.isEmpty(bankAccountEntityList)) {
                throw new ServiceException("根据银行账号编码[" + a.getBankAccountCode() + "]未查询到银行账号数据");
            }
        });
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
        List<InternalTransferEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("处理状态为已录入或者已拒绝的才可以生成凭证");
            }
            if (ObjectUtil.isEmpty(v.getBankAccountCode()) || ObjectUtil.isEmpty(v.getPaymentDate())) {
                throw new ServiceException("转让批次[" + v.getBatch() + "]的银行账号编码或者支付日期为空，请先上传");
            }
        });

        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
            executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            executeCommonDTO.setSceneCode(SceneEnum.PJZRDB.getCode());
            executeCommonDTO.setSceneName(SceneEnum.PJZRDB.getDesc());
            executeCommonDTO.setOrderId(v.getId().toString());
            executeCommonDTO.setBusinessDate(DateUtils.toDate(v.getPaymentDate()));
            executeCommonDTO.setContractCode(v.getContractCode());
            executeCommonDTO.setClientCode(v.getClientCode());
            executeCommonDTO.setAccountDate(new Date());
            executeCommonDTO.setIsSubmit(isSubmit);

            ParityTransferEntity parityTransferEntity = iParityTransferService.getOne(new LambdaQueryWrapper<ParityTransferEntity>().eq(ParityTransferEntity::getBatch, v.getBatch()));
            if (ObjectUtil.isEmpty(parityTransferEntity)) {
                throw new ServiceException("根据批次号[" + v.getBatch() + "]未查询到内部调拨数据");
            }

            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
            dataMap.put("transferType", "平价转让");
            dataMap.put("transferor", parityTransferEntity.getTransferParty());
            dataMap.put("Transferee", parityTransferEntity.getTransfereeParty());
            dataMap.put("transferBatch", parityTransferEntity.getBatch());
            dataMap.put("paymentAmount", v.getAmount());

            //查银行账号的组织
            List<BankAccountEntity> bankAccountEntityList = iBankAccountService.selectByBankAccountCode(v.getBankAccountCode());
            if (CollectionUtils.isEmpty(bankAccountEntityList)) {
                throw new ServiceException("根据银行账号编码[" + v.getBankAccountCode() + "]未查询到银行账号数据");
            }
            BankAccountEntity bankAccountEntity = bankAccountEntityList.get(0);
            // 银行到账主体
            dataMap.put("bankOrgId", bankAccountEntity.getOrgId());
            dataMap.put("ebankNum", bankAccountEntity.getBankAccountNumber());
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

            InternalTransferEntity internalTransferEntity = this.getById(Long.parseLong(infoVO.getOrderId()));
            internalTransferEntity.setAccountDate(LocalDateTime.now());
            internalTransferEntity.setIsGenerateVoucher(isGenerateVoucher);
            internalTransferEntity.setErrorInfo(errorInfo);
            internalTransferEntity.setVoucherId(voucherIds);
            internalTransferEntity.setUpdateTime(null);
            this.updateById(internalTransferEntity);
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

    @Override
    public Boolean submit(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<InternalTransferEntity> entityList = this.listByIds(ids);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
            if (ObjectUtil.equal(v.getIsGenerateVoucher(), YesOrNoEnum.NO.getCode())) {
                throw new ServiceException("生成凭证后才可以提交");
            }
            if (ObjectUtil.isEmpty(v.getBankAccountCode()) || ObjectUtil.isEmpty(v.getPaymentDate())) {
                throw new ServiceException("转让批次[" + v.getBatch() + "]的银行账号编码或者支付日期为空，不能提交，请先上传");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.NBDB.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);
        });

        Boolean generateVoucherFlag = generateVoucher(ids, YesOrNoEnum.YES.getCode());
        if (generateVoucherFlag) {
            // 发送审核
            Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);

            List<InternalTransferEntity> newEntityList = this.listByIds(ids);
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
            throw new ServiceException("凭证生成失败，不能提交");
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
        List<InternalTransferEntity> provisionEntityList = this.listByIds(ids);
        provisionEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            v.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
            v.setVoucherId("");
        });
        iApproveService.withdraw(provisionEntityList.stream().map(InternalTransferEntity::getProcessInstanceId).collect(Collectors.toList()));
        // 删除凭证
        batchDeleteVoucher(ids);
        return this.updateBatchById(provisionEntityList);
    }

    /**
     * 删除凭证
     *
     * @param ids
     */
    public void batchDeleteVoucher(List<Long> ids) {
        // 获取所有的凭证Id
        List<ParityTransferDetailEntity> detailsEntityList = iParityTransferDetailService.lambdaQuery().in(ParityTransferDetailEntity::getParityTransferId, ids).list();
        // 逗号拆分
        List<Long> voucherIdList = Lists.newArrayList();
        detailsEntityList.forEach(v -> {
            if (StringUtils.isNotEmpty(v.getVoucherIds())) {
                voucherIdList.addAll(Arrays.stream(v.getVoucherIds().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
        });
        if (CollectionUtil.isNotEmpty(voucherIdList)) {
            iVoucherService.deleteByIdList(voucherIdList);
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<InternalTransferEntity> transferRegisterEntityList = this.listByIds(ids);
        transferRegisterEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以删除");
            }
        });
        // 删除凭证
        batchDeleteVoucher(ids);
        return removeBatchByIds(ids);
    }

    /**
     * 生成支付信息
     *
     * @param queryDTO
     * @return
     */
    @Override
    public Boolean generatePayment(InternalTransferGenerateDTO queryDTO) {
        List<String> batchList = queryDTO.getBatchList();
        if (ObjectUtil.isEmpty(queryDTO.getFinanceDate())) {
            throw new ServiceException("财务日期不能为空");
        }
        if (CollectionUtils.isEmpty(batchList)) {
            throw new ServiceException("转让批次不能为空");
        }
        List<InternalTransferEntity> list = Lists.newArrayList();
        batchList.stream().forEach(batch -> {
            List<InternalTransferEntity> entityList = list(new LambdaQueryWrapper<InternalTransferEntity>().eq(InternalTransferEntity::getBatch, batch));
            if (CollectionUtils.isNotEmpty(entityList)) {
                throw new ServiceException("转让批次[" + batch + "]已生成支付信息");
            }
            ParityTransferEntity parityTransferEntity = iParityTransferService.getOne(new LambdaQueryWrapper<ParityTransferEntity>().eq(ParityTransferEntity::getBatch, batch));
            if (ObjectUtil.isEmpty(parityTransferEntity)) {
                throw new ServiceException("根据转让批次[" + batch + "]没有查询到转让汇总数据");
            }
            List<ParityTransferDetailVO> parityTransferDetailVOList = iParityTransferDetailService.getByBatch(batch);
            if (CollectionUtils.isEmpty(parityTransferDetailVOList)) {
                throw new ServiceException("根据转让批次[" + batch + "]没有查询到转让明细数据");
            }
            List<String> contractCodeList = parityTransferDetailVOList.stream().map(ParityTransferDetailVO::getContractCode).collect(Collectors.toList());
            List<String> orgIdList = parityTransferDetailVOList.stream().map(ParityTransferDetailVO::getOrgId).collect(Collectors.toList());
            // 合同+签约主体 查余额表：凭证日期<=筛选日期，创建日期最新时collection_transfer_balance余额
            List<ContractBalanceEntity> contractBalanceEntityList = iContractBalanceService.list(new LambdaQueryWrapper<ContractBalanceEntity>()
                    .in(ContractBalanceEntity::getContractCode, contractCodeList)
                    .in(ContractBalanceEntity::getOrgId, orgIdList)
                    .apply("to_char(voucher_date,'YYYY-MM-DD')<={0}", DateUtil.format(queryDTO.getFinanceDate(), "yyyy-MM-dd")));

            parityTransferDetailVOList.stream().forEach(a -> {
                InternalTransferEntity entity = new InternalTransferEntity();
                entity.setBatch(batch);
                entity.setTransferParty(parityTransferEntity.getTransferParty());
                entity.setContractCode(a.getContractCode());
                entity.setClientCode(a.getClientCode());
                BigDecimal amount = contractBalanceEntityList.stream().filter(o -> ObjectUtil.equals(o.getContractCode(), a.getContractCode())
                                && ObjectUtil.equals(o.getOrgId(), a.getOrgId()))
                        .max(Comparator.comparing(ContractBalanceEntity::getId)).map(ContractBalanceEntity::getCollectionTransferBalance).orElse(BigDecimal.ZERO);
                entity.setAmount(amount);
                entity.setFinanceDate(DateUtil.toLocalDateTime(queryDTO.getFinanceDate()));
                list.add(entity);
            });
        });
        // 保存
        this.saveBatch(list);
        return Boolean.TRUE;
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
        InternalTransferEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("数据不存在");
        }
        if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
            // 驳回 删除凭证
            batchDeleteVoucher(Lists.newArrayList(entity.getId()));
            entity.setVoucherId("");
            entity.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
        }
        // 通过，直接修改状态
        entity.setProcessStatus(approveDTO.getDocumentStatus());
        this.updateById(entity);
    }

}

