package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.GenConstants;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.config.RabbitmqConfig;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.ContractHisMapper;
import com.utfinancing.financehub.engine.finance.mapper.ContractMapper;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanHisMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ContractHisVO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanHisVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.service.ITaxRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-01
 * @Description :  ContractHis服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ContractHisServiceImpl extends ServiceImpl<ContractHisMapper, ContractHisEntity> implements IContractHisService {

    private final ContractHisMapper contractHisMapper;
    private final IRepaymentPlanHisService repaymentPlanHisService;
    private final IRepaymentPlanService repaymentPlanService;
    private final RepaymentPlanHisMapper repaymentPlanHisMapper;
    private final ContractMapper contractMapper;
    private final RemoteDictService remoteDictService;
    private final IOrgCompanyService orgCompanyService;
    private final ITaxRateService taxRateService;
    private final IRuleService iRuleService;
    private final IVoucherService iVoucherService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${approve.url.contractHis-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;
    @Resource
    private IContractService iContractService;

    private final BigDecimal tax6=new BigDecimal("1.06");
    private final BigDecimal tax13=new BigDecimal("1.13");

    @Override
    public Long saveContractHis(ContractHisDTO dto) {
        ContractHisEntity entity = BeanUtil.copyProperties(dto, ContractHisEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateContractHis(Long id, ContractHisDTO dto) {
        ContractHisEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ContractHisDTO getContractHisDTOById(Long id) {
        ContractHisEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ContractHisDTO.class);
    }

    @Override
    public ContractHisDTO getContractHisDTOByCode(String code) {
        ContractHisEntity entity = this.getOne(Wrappers.<ContractHisEntity>lambdaQuery()
                .eq(ContractHisEntity::getContractCode, code)
                .orderByDesc(ContractHisEntity::getCreateTime)
                .last("limit 1")
        );
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ContractHisDTO.class);
    }

    @Override
    public IPage<ContractHisVO> selectPage(ContractHisQueryDTO queryDTO) {
        if (CollectionUtils.isEmpty(queryDTO.getProcessStatusList())){
            // 默认查询 不包含未录入的 只查询修改了的合同
            queryDTO.setProcessStatusList(Lists.newArrayList(ProcessStatusEnum.ENTERED.getCode()
            ,ProcessStatusEnum.SUBMITTED.getCode(),ProcessStatusEnum.REVIEWED.getCode(),ProcessStatusEnum.TO_KINGDEE.getCode()
                    ,ProcessStatusEnum.REJECTED.getCode(),ProcessStatusEnum.WRITEOFF.getCode()));
        }
        Page<ContractHisVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<ContractHisEntity> contractHisEntities = contractHisMapper.selectContractHisInfo(page, queryDTO);
        List<ContractHisVO> records = BeanUtil.copyToList(contractHisEntities, ContractHisVO.class);
        setTaxRate(records);
        records.forEach(e->{
            List<Long> voucherIdList = new ArrayList<>();
            if (null != e.getVoucherId()) {
                voucherIdList.add(e.getVoucherId());
            }
            if (null != e.getPlanVoucherId()) {
                voucherIdList.add(e.getPlanVoucherId());
            }
            e.setVoucherIdList(voucherIdList);
        });
        page.setRecords(records);
        return page;
    }

    private void setTaxRate(List<ContractHisVO> records) {
        //租赁类型
        R<List<SysDictData>> leaseTypeR = remoteDictService.listDictData(DictTypeEnum.LEASE_TYPE.getCode());
        Map<String, String> leaseTypeMap = leaseTypeR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        Map<String, BigDecimal> taxRateMap = new HashMap<>();
        records.forEach(e -> {
            String leaseType = e.getLeaseType();
            // 50001,60001签约主体下的使用原值
            if (!"50001".equals(e.getOrgId()) && !"60001".equals(e.getOrgId())) {
                if (StringUtils.isNotBlank(leaseType)) {
                    leaseType = leaseTypeMap.get(leaseType);
                    BigDecimal taxRate = taxRateMap.get(leaseType);
                    if (null == taxRate) {
                        taxRate = taxRateService.getGeneralValidTaxRateByLeaseType(e.getBusinessCode(), leaseTypeMap.get(leaseType));
                        if (null == taxRate) {
                            taxRate = BigDecimal.ZERO;
                        }
                        taxRate = NumberUtil.mul(taxRate, 100);
                        taxRateMap.put(leaseType, taxRate);
                    }
                    e.setTaxRate(taxRate);
                }
            }
        });
    }

    @Override
    public List<ContractHisVO> selectList(ContractHisQueryDTO queryDTO) {
        List<ContractHisEntity> contractHisEntities = contractHisMapper.selectContractHisInfo(queryDTO);
        List<ContractHisVO> contractHisVOS = BeanUtil.copyToList(contractHisEntities, ContractHisVO.class);
        translateDict(contractHisVOS);
        setTaxRate(contractHisVOS);
        return contractHisVOS;
    }

    private void translateDict(List<ContractHisVO> list) {
        //业务合同状态
        R<List<SysDictData>> currencyR = remoteDictService.listDictData(DictTypeEnum.SYS_CURRENCY_TYPE.getCode());
        Map<String, String> currencyMap = currencyR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        //发票类型
        R<List<SysDictData>> invoiceTypeR = remoteDictService.listDictData(DictTypeEnum.INVOICE_TYPE.getCode());
        Map<String, String> invoiceTypeMap = invoiceTypeR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        //开票标识
        R<List<SysDictData>> invoiceFlagR = remoteDictService.listDictData(DictTypeEnum.INVOICE_FLAG.getCode());
        Map<String, String> invoiceFlagMap = invoiceFlagR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        //收益计提方式
        R<List<SysDictData>> accrualMethodR = remoteDictService.listDictData(DictTypeEnum.ACCRUAL_METHOD.getCode());
        Map<String, String> accrualMethodMap = accrualMethodR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        //租赁类型
        R<List<SysDictData>> leaseTypeR = remoteDictService.listDictData(DictTypeEnum.LEASE_TYPE.getCode());
        Map<String, String> leaseTypeMap = leaseTypeR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        //签约主体
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgId(), e -> e.getOrgName(), (a, b) -> b));
        //业务合同状态
        R<List<SysDictData>> contractStatusR = remoteDictService.listDictData(DictTypeEnum.CONTRACT_STATUS.getCode());
        Map<String, String> contractStatusMap = contractStatusR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        //财务合同状态
        R<List<SysDictData>> financialContractStatusR = remoteDictService.listDictData(DictTypeEnum.FINANCIAL_CONTRACT_STATUS.getCode());
        Map<String, String> financialContractStatusMap = financialContractStatusR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        //客户类型
        R<List<SysDictData>> sysClientTypeR = remoteDictService.listDictData(DictTypeEnum.SYS_CLIENT_TYPE.getCode());
        Map<String, String> sysClientTypeMap = sysClientTypeR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        list.forEach(e -> {
            e.setOrgId(companyMap.get(e.getOrgId()));
            e.setCurrencyType(currencyMap.get(e.getCurrencyType()));
            e.setLeaseType(leaseTypeMap.get(e.getLeaseType()));
            e.setInvoiceType(invoiceTypeMap.get(e.getInvoiceType()));
            e.setInvoicingFlag(invoiceFlagMap.get(e.getInvoicingFlag()));
            e.setIncomeProvisionMethod(accrualMethodMap.get(e.getIncomeProvisionMethod()));
            e.setContractStatus(contractStatusMap.get(e.getContractStatus()));
            e.setFinancialContractStatus(financialContractStatusMap.get(e.getFinancialContractStatus()));
            e.setClientType(sysClientTypeMap.get(e.getClientType()));
        });
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<ContractHisEntity> contractHisEntityList = this.listByIds(ids);
        if(CollectionUtils.isEmpty(contractHisEntityList)){
            throw new ServiceException("未查询到合同数据");
        }
        contractHisEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以删除");
            }
        });

        List<String> contractCodeList = contractHisEntityList.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
        removeBatchByIds(ids);
        // 删除可删除的租金计划
        ContractHisQueryDTO queryDTO = new ContractHisQueryDTO();
        queryDTO.setContractCodeList(contractCodeList);
        List<RepaymentPlanHisEntity> repaymentPlanHisEntities = repaymentPlanHisMapper.selectGroupByContractCode(queryDTO);
        repaymentPlanHisEntities = repaymentPlanHisEntities.stream().filter(e -> MarginStatusEnum.canChangeStatus().contains(e.getProcessStatus())).collect(Collectors.toList());
        List<Long> repaymentPlanHisIdList = repaymentPlanHisEntities.stream().map(e -> e.getId()).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(repaymentPlanHisIdList)) {
            repaymentPlanHisMapper.deleteBatchIds(repaymentPlanHisIdList);
        }
    }

    @Override
    public void submit(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        // 基本信息 交易结构
        List<ContractHisEntity> contractHisEntities = listByIds(ids);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        contractHisEntities.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.HTXXXG.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);
        });
        // 生成凭证
        Boolean generateVoucherFlag = voucher(ids, YesOrNoEnum.YES.getCode());
        if(!generateVoucherFlag){
            throw new ServiceException("生成凭证失败，不能提交");
        }

        // 发送审核
        Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);

        List<ContractHisEntity> newEntityList = this.listByIds(ids);
        newEntityList.stream().forEach(v -> {
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
            v.setUpdateTime(LocalDateTime.now());
            v.setSubmitBy(SecurityUtils.getUserId()+"");
        });

        this.updateBatchById(newEntityList);
    }

    /**
     * 推送偿还计划
     * @param ids
     */
    private void pushRepaymentPlan(List<Long> ids) {
        List<ContractHisEntity> contractHisEntities = listByIds(ids);
        ContractHisQueryDTO contractHisQueryDTO = new ContractHisQueryDTO();
        contractHisQueryDTO.setContractCodeList(contractHisEntities.stream().map(e->e.getContractCode()).collect(Collectors.toList()));
        //查询租金计划
        List<RepaymentPlanHisVO> contractRepaymentPlanVOS = repaymentPlanHisService.selectLastGroupList(contractHisQueryDTO);
        Map<String, List<RepaymentPlanHisVO>> listMap = contractRepaymentPlanVOS.stream().collect(Collectors.groupingBy(RepaymentPlanHisVO::getContractCode));
        for (String contractCode : listMap.keySet()) {
            // 按照日期排序
            List<RepaymentPlanHisVO> list = listMap.get(contractCode).stream().sorted(Comparator.comparing(RepaymentPlanHisVO::getPlanDate)).collect(Collectors.toList());
            ContractHisEntity contractHisEntity = contractHisEntities.stream().filter(a -> ObjectUtil.equals(a.getContractCode(), contractCode)).findFirst().orElse(null);
            List<RepaymentPlanSaveDTO> planSaveDTOList = Lists.newArrayList();
            for (int i = 0; i < list.size(); i++) {
                RepaymentPlanHisVO vo = list.get(i);
                RepaymentPlanSaveDTO planSaveDTO = new RepaymentPlanSaveDTO();
                planSaveDTO.setPeriods(i + 1);
                planSaveDTO.setPlanDate(vo.getPlanDate());
                planSaveDTO.setInterestAmount(vo.getInterestAmount());
                planSaveDTO.setPrincipalAmount(vo.getPrincipalAmount());
                planSaveDTO.setRentAmount(vo.getRentAmount());
                planSaveDTO.setCashFlow(vo.getCashFlow());
                planSaveDTOList.add(planSaveDTO);
            }

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("businessDate", LocalDate.now());
            dataMap.put("systemCode", SystemEnum.CWZT.getCode());
            dataMap.put("orgId", contractHisEntity.getOrgId());
            dataMap.put("contractCode", contractCode);
            dataMap.put("sceneCode", "合同信息修改");
            dataMap.put("orderId", list.stream().min(Comparator.comparingLong(RepaymentPlanHisVO::getId)).map(RepaymentPlanHisVO::getId).orElse(null));
            dataMap.put("repaymentPlanList", planSaveDTOList);
            log.info("合同信息修改推送偿还计划发送mq数据：" + JSONObject.toJSONString(dataMap));
            // 发送mq 推送偿还计划
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_DIRECT_TRANSACTION_DATA, RabbitmqConfig.ROUTINGKEY_REPAYMENT_PLAN_DATA, JSONObject.toJSONString(dataMap));
            // iRepaymentPlanService.saveRawData("1", JSONObject.parseObject(JSONObject.toJSONString(dataMap)));
        }

    }

    @Override
    public void withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<ContractHisEntity> entityList = this.listByIds(ids);
        entityList.forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            v.setVoucherId("");
            v.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
        });
        iApproveService.withdraw(entityList.stream().map(ContractHisEntity::getProcessInstanceId).collect(Collectors.toList()));
        //撤回之后需要将凭证表状态改为已录入，恢复余额表数据
        iVoucherService.updateStatusByBatch(ids,BatchTypeEnum.HTXXXG.getCode(),ProcessStatusEnum.ENTERED.getCode(),"","");
        this.updateBatchById(entityList);
    }

    /**
     * 删除凭证
     *
     * @param ids
     */
    public void batchDeleteVoucher(List<Long> ids) {
        // 获取所有的凭证Id
        List<ContractHisEntity> entityList = this.listByIds(ids);
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

    @Override
    public void pass(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        // 处理合同历史表
        List<ContractHisEntity> contractHisEntities = this.getBaseMapper().selectList(Wrappers.<ContractHisEntity>lambdaQuery().in(ContractHisEntity::getId, ids));
        List<ContractEntity> contractEntityList=Lists.newArrayList();
        //修改合同信息
        for (ContractHisEntity contractHisEntity : contractHisEntities) {
            String orgId = contractHisEntity.getOrgId();
            if(ObjectUtil.isNotEmpty(contractHisEntity.getOldOrgId())){
                // 修改了签约主体
                // 如果有原签约主体，则用原签约主体查询合同，
                orgId=contractHisEntity.getOldOrgId();
                //查询合同表数据
                ContractEntity contractEntity = contractMapper.selectOne(new LambdaUpdateWrapper<ContractEntity>().eq(ContractEntity::getContractCode, contractHisEntity.getContractCode())
                        .eq(ContractEntity::getOrgId, orgId));
                if(ObjectUtil.isEmpty(contractEntity)){
                    throw new ServiceException("根据合同编号["+contractEntity.getContractCode()+"]+签约主体["+ orgId +"] 未在合同表查询到数据");
                }
                BeanUtil.copyProperties(contractHisEntity,contractEntity,GenConstants.BASE_ENTITY);
                // 用新签约主体查询是否存在合同
                ContractEntity newContractEntity = contractMapper.selectOne(new LambdaUpdateWrapper<ContractEntity>().eq(ContractEntity::getContractCode, contractHisEntity.getContractCode())
                        .eq(ContractEntity::getOrgId, contractHisEntity.getOrgId()));
                if(ObjectUtil.isNotEmpty(newContractEntity)){
                    // 存在新签约主体的合同，直接更新合同
                    BeanUtil.copyProperties(contractEntity,newContractEntity,GenConstants.BASE_ENTITY);
                    contractMapper.updateById(newContractEntity);
                }else {
                    // 新签约主体的合同不存在，则新增
                    contractEntity.setId(null);
                    contractMapper.insert(contractEntity);
                }
                // 写特殊合同状态
                contractEntity.setTransferContractCode(contractEntity.getContractCode());
                contractEntity.setTransferOrgId(orgId);
                contractEntity.setTransferContractStatus(contractEntity.getContractStatus());
                contractEntityList.add(contractEntity);
            }else {
                // 没有修改签约主体
                //查询合同表数据
                ContractEntity contractEntity = contractMapper.selectOne(new LambdaUpdateWrapper<ContractEntity>().eq(ContractEntity::getContractCode, contractHisEntity.getContractCode())
                        .eq(ContractEntity::getOrgId, orgId));
                if(ObjectUtil.isEmpty(contractEntity)){
                    throw new ServiceException("根据合同编号["+contractEntity.getContractCode()+"]+签约主体["+ orgId +"] 未在合同表查询到数据");
                }
                BeanUtil.copyProperties(contractHisEntity,contractEntity,GenConstants.BASE_ENTITY);
                //保存合同数据
                contractMapper.updateById(contractEntity);
            }
        }
        // 写入特殊合同状态
        iContractService.saveRecordList(BeanUtil.copyToList(contractEntityList, ContractVO.class));

        // 推送偿还计划
        pushRepaymentPlan(ids);
    }

    @Override
    public void fail(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        // 基本信息 交易结构
        List<ContractHisEntity> contractHisEntities = listByIds(ids);
        List<String> contractCodeList = contractHisEntities.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
        LambdaUpdateWrapper<ContractHisEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .in(ContractHisEntity::getId, ids)
                .eq(ContractHisEntity::getProcessStatus, MarginStatusEnum.SUBMITTED.getCode())
                .set(ContractHisEntity::getUpdateTime, LocalDateTime.now())
                .set(ContractHisEntity::getProcessStatus, MarginStatusEnum.FAILED.getCode());
        this.update(updateChainWrapper);
        // 租金计划
        ContractHisQueryDTO queryDTO = new ContractHisQueryDTO();
        queryDTO.setContractCodeList(contractCodeList);
        List<RepaymentPlanHisEntity> repaymentPlanHisEntities = repaymentPlanHisMapper.selectGroupByContractCode(queryDTO);
        LambdaUpdateWrapper<RepaymentPlanHisEntity> planUpdateChainWrapper = new LambdaUpdateWrapper<>();
        planUpdateChainWrapper
                .in(RepaymentPlanHisEntity::getId, repaymentPlanHisEntities.stream().map(e->e.getId()).collect(Collectors.toList()))
                .eq(RepaymentPlanHisEntity::getProcessStatus, MarginStatusEnum.SUBMITTED.getCode())
                .set(RepaymentPlanHisEntity::getUpdateTime, LocalDateTime.now())
                .set(RepaymentPlanHisEntity::getProcessStatus, MarginStatusEnum.FAILED.getCode());
        repaymentPlanHisMapper.update(null, planUpdateChainWrapper);
    }

    @Override
    public Boolean voucher(List<Long> ids, String isSubmit) {
        if (CollectionUtils.isEmpty(ids)) {
            return Boolean.FALSE;
        }
        List<ContractHisEntity> contractHisEntities = listByIds(ids);
        ContractHisQueryDTO queryDTO = new ContractHisQueryDTO();
        queryDTO.setContractCodeList(contractHisEntities.stream().map(e -> e.getContractCode()).collect(Collectors.toList()));
        List<RepaymentPlanHisEntity> repaymentPlanHisEntities = repaymentPlanHisMapper.selectGroupByContractCode(queryDTO);
        if (repaymentPlanHisEntities.stream().map(e -> e.getContractCode()).distinct().count() != contractHisEntities.size()) {
            throw new ServiceException("缺少租金计划信息");
        }
        Map<String, List<RepaymentPlanHisEntity>> planGroupMap = repaymentPlanHisEntities.stream().collect(Collectors.groupingBy(e -> e.getContractCode()));
        //生成凭证前先删除之前的凭证
        batchDeleteVoucher(ids);
        // 生成凭证
        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        for (ContractHisEntity contractHisEntity : contractHisEntities) {
            ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
            commonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            commonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            commonDTO.setBusinessCode(contractHisEntity.getBusinessCode());
            commonDTO.setBusinessName(contractHisEntity.getBusinessName());
            commonDTO.setOrderId(contractHisEntity.getId().toString());
            commonDTO.setContractCode(contractHisEntity.getContractCode());
            commonDTO.setContractName(contractHisEntity.getContractName());
            commonDTO.setClientCode(contractHisEntity.getClientCode());
            commonDTO.setClientName(contractHisEntity.getClientName());
            commonDTO.setSceneCode(SceneEnum.JYJGBG.getCode());
            commonDTO.setSceneName(SceneEnum.JYJGBG.getDesc());
            commonDTO.setOrgId(contractHisEntity.getOrgId());
            commonDTO.setBusinessDate(DateUtil.beginOfDay(new Date()));
            commonDTO.setCurrencyType(contractHisEntity.getCurrencyType());
            commonDTO.setBatchId(contractHisEntity.getId());
            commonDTO.setBatchType(BatchTypeEnum.HTXXXG.getCode());
            commonDTO.setIsSubmit(isSubmit);
            Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);
            List<RepaymentPlanHisEntity> offlineContractRepaymentPlanEntities = planGroupMap.get(contractHisEntity.getContractCode());
            List<RepaymentPlanVO> repaymentPlanVOS = repaymentPlanService.selectByContractCode(contractHisEntity.getContractCode());
            // 应收租金调整额
            BigDecimal newAmount = offlineContractRepaymentPlanEntities.stream().map(e -> e.getRentAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal oldAmount = repaymentPlanVOS.stream().map(e -> e.getRentAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);

            commonMap.put("receivableLeaseAdjustAmount", NumberUtil.sub(newAmount, oldAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
            //主营业务收入-服务收入?
            //1532.01 未实现融资租赁收益 根据其余数据直接生成
            voucherMapList.add(commonMap);
        }
        log.info("生成凭证参数：{}", JSON.toJSONString(voucherMapList));
        List<VoucherInfoVO> voucherInfoVOList = iRuleService.batchExecuteRule(voucherMapList);
        Boolean isExistVoucherError = voucherInfoVOList.stream().allMatch(v -> com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotEmpty(v.getErrorInfo()));
        if (YesOrNoEnum.YES.getCode().equals(isSubmit) && isExistVoucherError) {
            // 异步删除已生成的凭证
            List<Long> voucherIdList = Lists.newArrayList();
            voucherInfoVOList.stream().forEach(voucherInfoVO -> {
                if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(voucherInfoVO.getVoucherDTOList())) {
                    voucherIdList.addAll(voucherInfoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).collect(Collectors.toList()));
                }
            });
            asnyDeleteVoucher(voucherIdList);
            return Boolean.FALSE;
        }
        if (voucherInfoVOList == null || voucherInfoVOList.isEmpty()) {
            log.error("合同信息修改 生成凭证失败");
        } else {
            for (VoucherInfoVO entry : voucherInfoVOList) {
                List<VoucherDTO> value = entry.getVoucherDTOList();
                if (CollectionUtils.isNotEmpty(value)) {
                    String vouchIds = value.stream().map(VoucherDTO::getId).map(String::valueOf).collect(
                            Collectors.joining(","));

                    this.lambdaUpdate()
                            .set(ContractHisEntity::getVoucherId, vouchIds)
                            .set(ContractHisEntity::getIsPlanVoucher, YesOrNoEnum.YES.getCode())
                            .set(ContractHisEntity::getExceptionType, entry.getErrorInfo())
                            .eq(ContractHisEntity::getId, Long.parseLong(entry.getOrderId()))
                            .update();
                }
            }
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

    @Override
    public void importData(List<ContractHisExcel> contractExcels, List<ContractHisStructureExcel> contractStructureExcels, List<ContractHisRepaymentPlanExcel> repaymentPlanExcels) {

        // 判断contractExcels contractStructureExcels的合同是否存在 不存在报错 存在着获取主表数据后覆盖 保存到历史表
        List<String> contractCodeList = contractExcels.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
        contractCodeList.addAll(contractStructureExcels.stream().map(e -> e.getContractCode()).collect(Collectors.toList()));
        contractCodeList = contractCodeList.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(contractCodeList)) {
            List<ContractEntity> contractEntities = contractMapper.selectList(Wrappers.<ContractEntity>lambdaQuery().in(ContractEntity::getContractCode, contractCodeList));
            List<String> exsitContractCodeList = contractEntities.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
            List<String> notExsitList = contractCodeList.stream().filter(e -> !exsitContractCodeList.contains(e)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(notExsitList)) {
                throw new ServiceException(StringUtils.join(notExsitList, ",") + "合同不存在!");
            }
            //开票标识
            R<List<SysDictData>> invoiceFlagR = remoteDictService.listDictData(DictTypeEnum.INVOICE_FLAG.getCode());
            Map<String, String> invoiceFlagMap = invoiceFlagR.getData().stream().collect(Collectors.toMap(e -> e.getDictLabel(), e -> e.getDictValue()));
            //收益计提方式
            R<List<SysDictData>> accrualMethodR = remoteDictService.listDictData(DictTypeEnum.ACCRUAL_METHOD.getCode());
            Map<String, String> accrualMethodMap = accrualMethodR.getData().stream().collect(Collectors.toMap(e -> e.getDictLabel(), e -> e.getDictValue()));
            //租赁类型
            R<List<SysDictData>> leaseTypeR = remoteDictService.listDictData(DictTypeEnum.LEASE_TYPE.getCode());
            Map<String, String> leaseTypeMap = leaseTypeR.getData().stream().collect(Collectors.toMap(e -> e.getDictLabel(), e -> e.getDictValue()));
            //签约主体
            Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgName(), e -> e.getOrgId(), (a, b) -> b));
            Map<String, ContractHisExcel> stringContractHisExcelMap = contractExcels.stream().collect(Collectors.toMap(e -> e.getContractCode(), e -> e));
            Map<String, ContractHisStructureExcel> stringContractHisStructureExcelMap = contractStructureExcels.stream().collect(Collectors.toMap(e -> e.getContractCode(), e -> e));

            //转换数据
            contractExcels.stream().forEach(a->{
                a.setOrgId(companyMap.get(a.getOrgId()));
                if(ObjectUtil.isNotEmpty(a.getNewOrgId())){
                    a.setNewOrgId(companyMap.get(a.getNewOrgId()));
                }
                a.setLeaseType(leaseTypeMap.get(a.getLeaseType()));
                a.setInvoicingFlag(invoiceFlagMap.get(a.getInvoicingFlag()));
                a.setIncomeProvisionMethod(accrualMethodMap.get(a.getIncomeProvisionMethod()));
            });

            List<ContractHisEntity> contractHisEntities = new ArrayList<>();
            for (ContractHisExcel contractExcel : contractExcels) {
                // 获取合同数据
                ContractEntity contractEntity = contractEntities.stream().filter(a -> ObjectUtil.equals(a.getContractCode(), contractExcel.getContractCode())
                                && ObjectUtil.equals(a.getOrgId(), contractExcel.getOrgId())).findFirst().orElse(null);
                if (ObjectUtil.isEmpty(contractEntity)) {
                    throw new ServiceException("合同编号[" + contractExcel.getContractCode() + "]+签约主体[" + contractExcel.getOrgId() + "]在合同表中不存在!");
                }
                // 获取交易结构数据
                ContractHisStructureExcel contractHisStructureExcel = contractStructureExcels.stream().filter(a -> ObjectUtil.equals(a.getContractCode(), contractExcel.getContractCode())
                        && ObjectUtil.equals(a.getContractCodeM(), contractExcel.getContractCodeM())).findFirst().get();
                if(ObjectUtil.isEmpty(contractHisStructureExcel)){
                    throw new ServiceException("合同编号[" + contractExcel.getContractCode() + "]+主合同编码[" + contractExcel.getContractCodeM() + "]在交易结构中不存在!");
                }
                ContractHisEntity contractHisEntity = BeanUtil.copyProperties(contractEntity, ContractHisEntity.class, GenConstants.BASE_ENTITY);
                BeanUtil.copyProperties(contractHisStructureExcel, contractHisEntity);
                contractHisEntity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                if(ObjectUtil.isNotEmpty(contractExcel.getNewOrgId())){
                    // 将上传的新主体放入orgId，将原主体放入oldOrgId
                    contractHisEntity.setOldOrgId(contractExcel.getOrgId());
                    contractHisEntity.setOrgId(contractExcel.getNewOrgId());
                }
                contractHisEntities.add(contractHisEntity);
            }
            saveBatch(contractHisEntities);
        }
        // 新增
        if (CollectionUtils.isNotEmpty(repaymentPlanExcels)) {
            List<RepaymentPlanHisEntity> repaymentPlanHisEntities = BeanUtil.copyToList(repaymentPlanExcels, RepaymentPlanHisEntity.class);
            repaymentPlanHisEntities.forEach(e -> {
                e.setProcessStatus(MarginStatusEnum.ENTERED.getCode());
                e.setChangeDate(DateUtil.beginOfDay(new Date()));
                e.setManualChangeMark(YesOrNoEnum.YES.getCode());
            });
            // 计算现金流
            Map<String, List<RepaymentPlanHisEntity>> repaymentMap = repaymentPlanHisEntities.stream().collect(Collectors.groupingBy(RepaymentPlanHisEntity::getContractCode));
            for (String contractCode : repaymentMap.keySet()) {
                List<RepaymentPlanHisEntity> list = repaymentMap.get(contractCode);
                ContractHisExcel contractHisExcel = contractExcels.stream().filter(b -> ObjectUtil.equals(contractCode, b.getContractCode())).findFirst().orElse(null);
                if(ObjectUtil.isEmpty(contractHisExcel)){
                    throw new ServiceException("合同["+contractCode+"]无基础数据，不能计算现金流");
                }
                ContractHisStructureExcel structure = contractStructureExcels.stream().filter(b -> ObjectUtil.equals(contractCode, b.getContractCode())).findFirst().orElse(null);
                if(ObjectUtil.isEmpty(structure)){
                    throw new ServiceException("合同["+contractCode+"]无交易结构数据，不能计算现金流");
                }
                if (ObjectUtil.equals(contractHisExcel.getLeaseType(), LeaseTypeEnum.DIRECT.getCode())) {
                    // 直租
                    for (int i = 0; i < list.size(); i++) {
                        RepaymentPlanHisEntity plan = list.get(i);
                        BigDecimal cashFlow = BigDecimal.ZERO;
                        if (i == 0) {
                            // 第一行是起租日
                            // 税后资金流出 = 设备价格（行次1）/1.13+ (出租人保险费用（行次2）/1.13+渠道费用（行次3）/1.06+出租人其它成本 （行次4）/1.13+ 承租人保险费用（行次11）/1.06 - 其他收入 (含增值税)（行次12）/1.06)
                            cashFlow = NumberUtil.div(structure.getPayableDeviceAmount(), tax13).add(NumberUtil.div(structure.getLessorInsuranceAmount(), tax13)
                                    .add(NumberUtil.div(structure.getChannelFees(), tax6)).add(NumberUtil.div(structure.getLessorOtherCosts(), tax13))
                                    .add(NumberUtil.div(structure.getReceivableInsuranceAmount(), tax6)).subtract(NumberUtil.div(structure.getReceivableOther(), tax6))
                            );
                        } else if (i == list.size() - 1) {
                            // 最后一行
                            // 期末资金流入=应收利息/1.13+应收本金/1.13+名义留购价（行次9）/1.13
                            cashFlow = NumberUtil.div(plan.getInterestAmount(), tax13).add(NumberUtil.div(plan.getPrincipalAmount(), tax13)).add(NumberUtil.div(structure.getRetainedPrice(), tax13));
                        } else {
                            // 后续资金流入=应收利息/1.13+应收本金/1.13
                            cashFlow = NumberUtil.div(plan.getInterestAmount(), tax13).add(NumberUtil.div(plan.getPrincipalAmount(), tax13));
                        }
                        plan.setCashFlow(cashFlow);
                    }
                } else {
                    // 回租
                    for (int i = 0; i < list.size(); i++) {
                        RepaymentPlanHisEntity plan = list.get(i);
                        BigDecimal cashFlow = BigDecimal.ZERO;
                        if (i == 0) {
                            // 第一行是起租日
                            // 期初税后资金流出 = 设备价格（行次1）+ (出租人保险费用（行次2）/1.06+渠道费用（行次3）/1.06+出租人其它成本 （行次4）/1.06+ 承租人保险费用（行次11）/1.06 - 其他收入 (含增值税)（行次12）/1.06)
                            cashFlow = NumberUtil.toBigDecimal(structure.getPayableDeviceAmount()).add(NumberUtil.div(structure.getLessorInsuranceAmount(), tax6)
                                    .add(NumberUtil.div(structure.getChannelFees(), tax6)).add(NumberUtil.div(structure.getLessorOtherCosts(), tax6))
                                    .add(NumberUtil.div(structure.getReceivableInsuranceAmount(), tax6)).subtract(NumberUtil.div(structure.getReceivableOther(), tax6))
                            );
                        } else if (i == list.size() - 1) {
                            // 其他为还款日
                            // 期末资金流入=应收利息/1.06+应收本金+名义留购价（行次9）/1.06
                            cashFlow = NumberUtil.div(plan.getInterestAmount(), tax6).add(NumberUtil.toBigDecimal(plan.getPrincipalAmount())).add(NumberUtil.div(structure.getRetainedPrice(), tax6));
                        } else {
                            // 后续资金流入=应收利息/1.06+应收本金
                            cashFlow = NumberUtil.div(plan.getInterestAmount(), tax6).add(NumberUtil.toBigDecimal(plan.getPrincipalAmount()));
                        }
                        plan.setCashFlow(cashFlow);
                    }
                }
            }
            repaymentPlanHisService.saveRepaymentPlanHisBatchByEntity(repaymentPlanHisEntities);
        }
    }


    /**
     * 审批后修改状态
     * @param approveDTO
     */
    @Override
    public void updateProcessStatus(CommonApproveDTO approveDTO) {
        if (StringUtils.isEmpty(approveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        ContractHisEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("合同修改数据不存在");
        }
        if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
            // 驳回 删除凭证
            // batchDeleteVoucher(Lists.newArrayList(entity.getId()));
            // entity.setVoucherId("");
            // entity.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
        }else {
            //通过，后续逻辑
            pass(Lists.newArrayList(approveDTO.getDocumentId()));
        }
        // 修改凭证状态，通过和驳回都修改
        updateVoucherStatus(Lists.newArrayList(approveDTO.getDocumentId()), approveDTO);
        // 通过，直接修改状态
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
        List<ContractHisEntity> entityList = this.listByIds(ids);
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

