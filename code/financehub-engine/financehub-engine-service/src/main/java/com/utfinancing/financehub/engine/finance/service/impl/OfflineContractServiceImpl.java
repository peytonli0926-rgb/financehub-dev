package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
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
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.config.RabbitmqConfig;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.OfflineContractMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractRepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractStructureVO;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanHisVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
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
 * @Date : Create in 2023-10-18
 * @Description :  OfflineContract服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class OfflineContractServiceImpl extends ServiceImpl<OfflineContractMapper, OfflineContractEntity> implements IOfflineContractService {

    public static final String CONTRACT_STATUS_NORMAL = "1";
    private final OfflineContractMapper offlineContractMapper;
    private final IOfflineContractStructureService offlineContractStructureService;
    private final IOfflineContractRepaymentPlanService offlineContractRepaymentPlanService;
    private final IContractService contractService;
    private final IRuleService iRuleService;
    private final IRepaymentPlanService repaymentPlanService;
    private final RemoteDictService remoteDictService;
    private final IOrgCompanyService orgCompanyService;

    private final IContractHisService contractHisService;
    private final IVoucherService iVoucherService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${approve.url.offlineContract-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;

    private final BigDecimal tax6=new BigDecimal("1.06");
    private final BigDecimal tax13=new BigDecimal("1.13");

    @Override
    public Long saveOfflineContract(OfflineContractDTO dto) {
        OfflineContractEntity entity = BeanUtil.copyProperties(dto, OfflineContractEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateOfflineContract(Long id, OfflineContractDTO dto) {
        OfflineContractEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public OfflineContractDTO getOfflineContractDTOById(Long id) {
        OfflineContractEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, OfflineContractDTO.class);
    }

    @Override
    public IPage<OfflineContractVO> selectPage(OfflineContractQueryDTO queryDTO) {
        LambdaQueryWrapper<OfflineContractEntity> queryWrapper = Wrappers.<OfflineContractEntity>lambdaQuery();
        //这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        IPage<OfflineContractEntity> entityIPage = offlineContractMapper.selectPage(new Page<OfflineContractEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        IPage<OfflineContractVO> page = ListBeanUtil.copyPage(entityIPage, OfflineContractVO.class);
        page.getRecords().forEach(e -> {
            e.setTaxRate(NumberUtil.mul(e.getTaxRate(), 100));
            e.setBatchType(BatchTypeEnum.XXHT.getCode());
        });
        return page;
    }

    private void setQueryCondition(OfflineContractQueryDTO queryDTO, LambdaQueryWrapper<OfflineContractEntity> queryWrapper) {
        queryWrapper.like(StringUtils.isNotBlank(queryDTO.getContractCode()), OfflineContractEntity::getContractCode, queryDTO.getContractCode());
        queryWrapper.like(StringUtils.isNotBlank(queryDTO.getClientCode()), OfflineContractEntity::getClientCode, queryDTO.getClientCode());
        queryWrapper.like(StringUtils.isNotBlank(queryDTO.getClientName()), OfflineContractEntity::getClientName, queryDTO.getClientName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getCurrencyType()), OfflineContractEntity::getCurrencyType, queryDTO.getCurrencyType());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getOrgId()), OfflineContractEntity::getOrgId, queryDTO.getOrgId());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), OfflineContractEntity::getOrgId, queryDTO.getOrgIdList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList()), OfflineContractEntity::getProcessStatus, queryDTO.getProcessStatusList());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getIdList()), OfflineContractEntity::getId, queryDTO.getIdList());
        queryWrapper.eq(ObjectUtil.isNotEmpty(queryDTO.getId()), OfflineContractEntity::getId, queryDTO.getId());
        queryWrapper.orderByDesc(OfflineContractEntity::getId);
    }

    @Override
    public List<OfflineContractVO> selectList(OfflineContractQueryDTO queryDTO) {
        LambdaQueryWrapper<OfflineContractEntity> queryWrapper = Wrappers.<OfflineContractEntity>lambdaQuery();
        //这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        List<OfflineContractEntity> entityIPage = offlineContractMapper.selectList(queryWrapper);
        List<OfflineContractVO> offlineContractVOS = ListBeanUtil.copyList(entityIPage, OfflineContractVO.class);
        offlineContractVOS.forEach(e -> e.setTaxRate(NumberUtil.mul(e.getTaxRate(), 100)));
        translateDict(offlineContractVOS);
        return offlineContractVOS;
    }

    private void translateDict(List<OfflineContractVO> list) {
        //币种
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
        //客户类型
        R<List<SysDictData>> sysClientTypeR = remoteDictService.listDictData(DictTypeEnum.SYS_CLIENT_TYPE.getCode());
        Map<String, String> sysClientTypeMap = sysClientTypeR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        //还款标识
        R<List<SysDictData>> payMethodR = remoteDictService.listDictData(DictTypeEnum.PAY_METHOD.getCode());
        Map<String, String> payMethodMap = payMethodR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        //签约主体
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgId(), e -> e.getOrgName(), (a, b) -> b));
        list.forEach(e -> {
            e.setOrgId(companyMap.get(e.getOrgId()));
            e.setCurrencyType(currencyMap.get(e.getCurrencyType()));
            e.setLeaseType(leaseTypeMap.get(e.getLeaseType()));
            e.setInvoiceType(invoiceTypeMap.get(e.getInvoiceType()));
            e.setInvoicingFlag(invoiceFlagMap.get(e.getInvoicingFlag()));
            e.setIncomeProvisionMethod(accrualMethodMap.get(e.getIncomeProvisionMethod()));
            e.setClientType(sysClientTypeMap.get(e.getClientType()));
            e.setPayMethod(payMethodMap.get(e.getPayMethod()));
        });
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        // 只筛选可以删除的合同
        List<OfflineContractEntity> existOfflineContractEntities = offlineContractMapper.selectList(Wrappers.<OfflineContractEntity>lambdaQuery()
                .in(OfflineContractEntity::getId, ids).in(OfflineContractEntity::getProcessStatus, MarginStatusEnum.canChangeStatus()));
        if (CollectionUtils.isEmpty(existOfflineContractEntities)) {
            return;
        }
        List<String> contractCodeList = existOfflineContractEntities.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
        removeBatchByIds(existOfflineContractEntities.stream().map(e -> e.getId()).collect(Collectors.toList()));
        // 删除交易结构,删除租金计划
        LambdaUpdateWrapper<OfflineContractStructureEntity> structureEntityLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        structureEntityLambdaUpdateWrapper
                .in(OfflineContractStructureEntity::getContractCode, contractCodeList)
                .set(OfflineContractStructureEntity::getUpdateBy, SecurityUtils.getUsername())
                .set(OfflineContractStructureEntity::getUpdateTime, LocalDateTime.now())
                .set(OfflineContractStructureEntity::getDelFlag, YesOrNoEnum.YES.getCode());
        offlineContractStructureService.update(structureEntityLambdaUpdateWrapper);

        LambdaUpdateWrapper<OfflineContractRepaymentPlanEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .in(OfflineContractRepaymentPlanEntity::getContractCode, contractCodeList)
                .set(OfflineContractRepaymentPlanEntity::getUpdateBy, SecurityUtils.getUsername())
                .set(OfflineContractRepaymentPlanEntity::getUpdateTime, LocalDateTime.now())
                .set(OfflineContractRepaymentPlanEntity::getDelFlag, YesOrNoEnum.YES.getCode());
        offlineContractRepaymentPlanService.update(updateChainWrapper);
        //删除凭证
        batchDeleteVoucher(ids);
    }

    @Override
    public void importData(List<OfflineContractExcel> contractExcels, List<OfflineContractStructureExcel> contractStructureExcels,
                           List<OfflineContractRepaymentPlanExcel> repaymentPlanExcels) {
        List<String> contractCodeList = contractExcels.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
        contractCodeList.addAll(contractStructureExcels.stream().map(e -> e.getContractCode()).collect(Collectors.toList()));
        contractCodeList.addAll(repaymentPlanExcels.stream().map(e -> e.getContractCode()).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(contractCodeList)) {
            return;
        }
        List<ContractDTO> contractDTOS = contractService.listContractDTOByCodeList(contractCodeList.stream().distinct().collect(Collectors.toList()));
        List<String> existContractCodeList = contractDTOS.stream().map(ContractDTO::getContractCode).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(contractCodeList)) {
            List<OfflineContractEntity> existOfflineContractEntities = offlineContractMapper.selectList(Wrappers.<OfflineContractEntity>lambdaQuery()
                    .in(OfflineContractEntity::getContractCode, contractCodeList).in(OfflineContractEntity::getProcessStatus, MarginStatusEnum.cantChangeStatus()));
            existContractCodeList.addAll(existOfflineContractEntities.stream().map(e -> e.getContractCode()).collect(Collectors.toList()));
        }
        // 排除已存在合同表的 + 线下表不能修改状态的合同
        List<OfflineContractExcel>  contractExcelList = contractExcels.stream().filter(e -> !existContractCodeList.contains(e.getContractCode())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(contractExcelList)) {
            checkDate(contractExcelList);
            //发票类型
            R<List<SysDictData>> invoiceTypeR = remoteDictService.listDictData(DictTypeEnum.INVOICE_TYPE.getCode());
            Map<String, String> invoiceTypeMap = invoiceTypeR.getData().stream().collect(Collectors.toMap(e -> e.getDictLabel(), e -> e.getDictValue()));
            //开票标识
            R<List<SysDictData>> invoiceFlagR = remoteDictService.listDictData(DictTypeEnum.INVOICE_FLAG.getCode());
            Map<String, String> invoiceFlagMap = invoiceFlagR.getData().stream().collect(Collectors.toMap(e -> e.getDictLabel(), e -> e.getDictValue()));
            //收益计提方式
            R<List<SysDictData>> accrualMethodR = remoteDictService.listDictData(DictTypeEnum.ACCRUAL_METHOD.getCode());
            Map<String, String> accrualMethodMap = accrualMethodR.getData().stream().collect(Collectors.toMap(e -> e.getDictLabel(), e -> e.getDictValue()));
            //租赁类型
            R<List<SysDictData>> leaseTypeR = remoteDictService.listDictData(DictTypeEnum.LEASE_TYPE.getCode());
            Map<String, String> leaseTypeMap = leaseTypeR.getData().stream().collect(Collectors.toMap(e -> e.getDictLabel(), e -> e.getDictValue()));
            //币种
            R<List<SysDictData>> currencyR = remoteDictService.listDictData(DictTypeEnum.SYS_CURRENCY_TYPE.getCode());
            Map<String, String> currencyMap = currencyR.getData().stream().collect(Collectors.toMap(e -> e.getDictLabel(), e -> e.getDictValue()));
            //客户类型
            R<List<SysDictData>> sysClientTypeR = remoteDictService.listDictData(DictTypeEnum.SYS_CLIENT_TYPE.getCode());
            Map<String, String> sysClientTypeMap = sysClientTypeR.getData().stream().collect(Collectors.toMap(e -> e.getDictLabel(), e -> e.getDictValue()));
            //还款标识
            R<List<SysDictData>> payMethodR = remoteDictService.listDictData(DictTypeEnum.PAY_METHOD.getCode());
            Map<String, String> payMethodMap = payMethodR.getData().stream().collect(Collectors.toMap(e -> e.getDictLabel(), e -> e.getDictValue()));
            //签约主体
            Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgName(), e -> e.getOrgId(), (a, b) -> b));
            // 查询合同存在的
            Map<String, OfflineContractEntity> contractEntityMap = offlineContractMapper.selectList(Wrappers.<OfflineContractEntity>lambdaQuery()
                            .in(OfflineContractEntity::getContractCode, contractExcelList.stream().map(e -> e.getContractCode()).collect(Collectors.toList())))
                    .stream().collect(Collectors.toMap(OfflineContractEntity::getContractCode, e -> e));

            List<OfflineContractEntity> offlineContractEntities = new ArrayList<>();
            List<ContractHisEntity> contractHisEntities = new ArrayList<>();
            List<ContractEntity> contractEntities = new ArrayList<>();
            contractExcelList.forEach(e -> {
                OfflineContractEntity entity = contractEntityMap.get(e.getContractCode());
                e.setCurrencyType(currencyMap.get(e.getCurrencyType()));
                e.setLeaseType(leaseTypeMap.get(e.getLeaseType()));
                e.setOrgId(companyMap.get(e.getOrgId()));
                e.setInvoiceType(invoiceTypeMap.get(e.getInvoiceType()));
                e.setIncomeProvisionMethod(accrualMethodMap.get(e.getIncomeProvisionMethod()));
                e.setInvoicingFlag(invoiceFlagMap.get(e.getInvoicingFlag()));
                e.setClientType(sysClientTypeMap.get(e.getClientType()));
                e.setPayMethod(payMethodMap.get(e.getPayMethod()));
                if (null != entity) {
                    BeanUtil.copyProperties(e, entity);
                } else {
                    entity = BeanUtil.copyProperties(e, OfflineContractEntity.class);
                    entity.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
                    entity.setProcessStatus(MarginStatusEnum.ENTERED.getCode());
                }
                if(ObjectUtil.isEmpty(e.getCurrencyType())){
                    // 没有币种，默认为人民币
                    e.setCurrencyType(CurrencyTypeEnum.CNY.getCode());
                }
                offlineContractEntities.add(entity);

                contractHisEntities.add(BeanUtil.copyProperties(entity, ContractHisEntity.class));
                contractEntities.add(BeanUtil.copyProperties(entity, ContractEntity.class));
            });
            this.saveOrUpdateBatch(offlineContractEntities);
            // 线下合同录入时，不写入合同表，审批通过后写入
            // contractHisService.saveOrUpdateBatch(contractHisEntities);
            // contractService.saveOrUpdateBatch(contractEntities);
        }
        if (CollectionUtils.isNotEmpty(contractStructureExcels)) {
            contractStructureExcels = contractStructureExcels.stream().filter(e -> !existContractCodeList.contains(e.getContractCode())).collect(Collectors.toList());
            List<String> contractCodes =contractStructureExcels.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
            Map<String, OfflineContractStructureEntity> contractStructureEntityMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(contractCodes)) {
                contractStructureEntityMap = offlineContractStructureService.getBaseMapper().selectList(Wrappers.<OfflineContractStructureEntity>lambdaQuery()
                        .in(OfflineContractStructureEntity::getContractCode, contractCodes))
                        .stream().collect(Collectors.toMap(OfflineContractStructureEntity::getContractCode, e -> e));
            }
            List<OfflineContractStructureEntity> contractStructureEntities = new ArrayList<>();
            Map<String, OfflineContractStructureEntity> finalContractStructureEntityMap = contractStructureEntityMap;
            contractStructureExcels.forEach(e -> {
                OfflineContractStructureEntity entity = null;
                if (finalContractStructureEntityMap.containsKey(e.getContractCode())) {
                    entity = finalContractStructureEntityMap.get(e.getContractCode());
                }
                if (null != entity) {
                    BeanUtil.copyProperties(e, entity);
                } else {
                    entity = BeanUtil.copyProperties(e, OfflineContractStructureEntity.class);
                }
                contractStructureEntities.add(entity);
            });
            offlineContractStructureService.saveOrUpdateBatch(contractStructureEntities);
        }
        if (CollectionUtils.isNotEmpty(repaymentPlanExcels)) {
            checkPlan(repaymentPlanExcels);
            repaymentPlanExcels = repaymentPlanExcels.stream().filter(e -> !existContractCodeList.contains(e.getContractCode())).collect(Collectors.toList());
            List<String> contractCodeLists = repaymentPlanExcels.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(contractCodeLists)) {
                offlineContractRepaymentPlanService.deleteByContractCodeList(contractCodeLists);
            }

            List<OfflineContractRepaymentPlanEntity> repaymentPlanEntities = BeanUtil.copyToList(repaymentPlanExcels, OfflineContractRepaymentPlanEntity.class);
            // 计算现金流
            Map<String, List<OfflineContractRepaymentPlanEntity>> repaymentMap = repaymentPlanEntities.stream().collect(Collectors.groupingBy(OfflineContractRepaymentPlanEntity::getContractCode));
            for (String contractCode : repaymentMap.keySet()) {
                List<OfflineContractRepaymentPlanEntity> list = repaymentMap.get(contractCode);
                OfflineContractExcel offlineContractExcel = contractExcelList.stream().filter(b -> ObjectUtil.equals(contractCode, b.getContractCode())).findFirst().orElse(null);
                if(ObjectUtil.isEmpty(offlineContractExcel)){
                    throw new ServiceException("合同["+contractCode+"]无基础数据，不能计算现金流");
                }
                OfflineContractStructureExcel structure = contractStructureExcels.stream().filter(b -> ObjectUtil.equals(contractCode, b.getContractCode())).findFirst().orElse(null);
                if(ObjectUtil.isEmpty(structure)){
                    throw new ServiceException("合同["+contractCode+"]无交易结构数据，不能计算现金流");
                }
                if (ObjectUtil.equals(offlineContractExcel.getLeaseType(), LeaseTypeEnum.DIRECT.getCode())) {
                    // 直租
                    for (int i = 0; i < list.size(); i++) {
                        OfflineContractRepaymentPlanEntity plan = list.get(i);
                        BigDecimal cashFlow = BigDecimal.ZERO;
                        if (i == 0) {
                            // 第一行是起租日
                            // 税后资金流出 = 设备价格（行次1）/1.13+ (出租人保险费用（行次2）/1.13+渠道费用（行次3）/1.06+出租人其它成本 （行次4）/1.13+ 承租人保险费用（行次11）/1.06 - 其他收入 (含增值税)（行次12）/1.06)
                            cashFlow = NumberUtil.div(structure.getPayableDevice(), tax13).add(NumberUtil.div(structure.getLessorInsurance(), tax13)
                                    .add(NumberUtil.div(structure.getChannelFee(), tax6)).add(NumberUtil.div(structure.getLessorOtherincome(), tax13))
                                    .add(NumberUtil.div(structure.getReceivableInsurance(), tax6)).subtract(NumberUtil.div(structure.getReceivableOtherincome(), tax6))
                            );
                        } else if (i == list.size() - 1) {
                            // 最后一行
                            // 期末资金流入=应收利息/1.13+应收本金/1.13+名义留购价（行次9）/1.13
                            cashFlow = NumberUtil.div(plan.getInterestAmount(), tax13).add(NumberUtil.div(plan.getPrincipalAmount(), tax13)).add(NumberUtil.div(structure.getReceivableResidualValue(), tax13));
                        } else {
                            // 后续资金流入=应收利息/1.13+应收本金/1.13
                            cashFlow = NumberUtil.div(plan.getInterestAmount(), tax13).add(NumberUtil.div(plan.getPrincipalAmount(), tax13));
                        }
                        plan.setCashFlow(cashFlow);
                    }
                } else {
                    // 回租
                    for (int i = 0; i < list.size(); i++) {
                        OfflineContractRepaymentPlanEntity plan = list.get(i);
                        BigDecimal cashFlow = BigDecimal.ZERO;
                        if (i == 0) {
                            // 第一行是起租日
                            // 期初税后资金流出 = 设备价格（行次1）+ (出租人保险费用（行次2）/1.06+渠道费用（行次3）/1.06+出租人其它成本 （行次4）/1.06+ 承租人保险费用（行次11）/1.06 - 其他收入 (含增值税)（行次12）/1.06)
                            cashFlow = NumberUtil.toBigDecimal(structure.getPayableDevice()).add(NumberUtil.div(structure.getLessorInsurance(), tax6)
                                    .add(NumberUtil.div(structure.getChannelFee(), tax6)).add(NumberUtil.div(structure.getLessorOtherincome(), tax6))
                                    .add(NumberUtil.div(structure.getReceivableInsurance(), tax6)).subtract(NumberUtil.div(structure.getReceivableOtherincome(), tax6))
                            );
                        } else if (i == list.size() - 1) {
                            // 其他为还款日
                            // 期末资金流入=应收利息/1.06+应收本金+名义留购价（行次9）/1.06
                            cashFlow = NumberUtil.div(plan.getInterestAmount(), tax6).add(NumberUtil.toBigDecimal(plan.getPrincipalAmount())).add(NumberUtil.div(structure.getReceivableResidualValue(), tax6));
                        } else {
                            // 后续资金流入=应收利息/1.06+应收本金
                            cashFlow = NumberUtil.div(plan.getInterestAmount(), tax6).add(NumberUtil.toBigDecimal(plan.getPrincipalAmount()));
                        }
                        plan.setCashFlow(cashFlow);
                    }
                }
            }
            offlineContractRepaymentPlanService.saveBatch(repaymentPlanEntities);
        }

    }

    private void checkDate(List<OfflineContractExcel> contractExcels) {
        contractExcels.stream().forEach(a->{
            if(ObjectUtil.isEmpty(a.getLeaseDateStart())){
                throw new ServiceException("起租日不能为空");
            }
            if(ObjectUtil.isEmpty(a.getLeaseType())){
                throw new ServiceException("租赁类型不能为空");
            }
        });
    }

    private void checkPlan(List<OfflineContractRepaymentPlanExcel> repaymentPlanExcels) {
        repaymentPlanExcels.stream().forEach(a->{
            if(ObjectUtil.isEmpty(a.getContractCode())){
                throw new ServiceException("合同编号不能为空");
            }
            if(ObjectUtil.isEmpty(a.getPeriod())){
                throw new ServiceException("期数不能为空");
            }
            if(ObjectUtil.isEmpty(a.getPlanDate())){
                throw new ServiceException("还款日不能为空");
            }
            if(ObjectUtil.isEmpty(a.getRentAmount())){
                throw new ServiceException("应收租金不能为空");
            }
            if(ObjectUtil.isEmpty(a.getPrincipalAmount())){
                throw new ServiceException("本金不能为空");
            }
            if(ObjectUtil.isEmpty(a.getInterestAmount())){
                throw new ServiceException("利息不能为空");
            }
        });
    }

    @Override
    public Void submit(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        List<OfflineContractEntity> offlineContractEntities = listByIds(ids);
        List<OfflineContractStructureEntity> contractStructureEntities = offlineContractStructureService.getBaseMapper().selectList(Wrappers.<OfflineContractStructureEntity>lambdaQuery()
                .in(OfflineContractStructureEntity::getContractCode, offlineContractEntities.stream().map(e -> e.getContractCode()).collect(Collectors.toList())));
        if (offlineContractEntities.size() != contractStructureEntities.size()) {
            throw new ServiceException("缺少交易结构信息");
        }
        List<OfflineContractRepaymentPlanEntity> offlineContractRepaymentPlanEntities = offlineContractRepaymentPlanService.getBaseMapper().selectList(Wrappers.<OfflineContractRepaymentPlanEntity>lambdaQuery()
                .in(OfflineContractRepaymentPlanEntity::getContractCode, offlineContractEntities.stream().map(e -> e.getContractCode()).collect(Collectors.toList())));
        if (offlineContractEntities.size() != offlineContractRepaymentPlanEntities.stream().map(e -> e.getContractCode()).distinct().count()) {
            throw new ServiceException("缺少租金计划信息");
        }
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        offlineContractEntities.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.XXHT.getCode());
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

        List<OfflineContractEntity> newEntityList = this.listByIds(ids);
        newEntityList.stream().forEach(v -> {
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
            v.setUpdateTime(LocalDateTime.now());
            v.setSubmitBy(SecurityUtils.getUserId()+"");
        });
       this.updateBatchById(newEntityList);
        return null;
    }

    @Override
    public Void withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<OfflineContractEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(entityList.stream().map(OfflineContractEntity::getProcessInstanceId).collect(Collectors.toList()));
        this.updateBatchById(entityList);
        //撤回之后需要将凭证状态改为已录入状态
        updateVoucherStatusByBatch(ids,BatchTypeEnum.XXHT.getCode(),ProcessStatusEnum.ENTERED.getCode(),"","");
        return null;
    }

    @Override
    public Void pass(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        List<OfflineContractEntity> entities = this.getBaseMapper().selectList(Wrappers.<OfflineContractEntity>lambdaQuery().in(OfflineContractEntity::getId, ids));
        if (entities.stream().anyMatch(e -> !MarginStatusEnum.SUBMITTED.getCode().equals(e.getProcessStatus()))) {
            throw new ServiceException("存在错误状态数据");
        }
        // entities.forEach(e -> e.setProcessStatus(MarginStatusEnum.PASS.getCode()));
        // updateBatchById(entities);
        // 处理合同表 + 交易结构数据
        List<String> contractCodeList = entities.stream().map(e -> e.getContractCode()).distinct().collect(Collectors.toList());
        OfflineContractStructureQueryDTO structureQueryDTO = new OfflineContractStructureQueryDTO();
        structureQueryDTO.setContractCodeList(contractCodeList);
        List<OfflineContractStructureVO> offlineContractStructureVOS = offlineContractStructureService.selectList(structureQueryDTO);
        Map<String, OfflineContractStructureVO> contractMap = offlineContractStructureVOS.stream().collect(Collectors.toMap(e -> e.getContractCode(), e -> e));
        List<ContractDTO> contractDTOList = new ArrayList<>();
        entities.forEach(e -> {
            ContractDTO contractDTO = BeanUtil.copyProperties(e, ContractDTO.class, GenConstants.BASE_ENTITY);
            OfflineContractStructureVO structureVO = contractMap.get(e.getContractCode());
            contractDTO.setPayableDeviceAmount(structureVO.getPayableDevice());
            contractDTO.setReceivableFirstAmount(structureVO.getReceivableDownpayment());
            contractDTO.setLessorInsuranceAmount(structureVO.getLessorInsurance());
            contractDTO.setReceivableMarginAmount(structureVO.getLesseeMargin());
            contractDTO.setChannelFees(structureVO.getChannelFee());
            contractDTO.setReceivableProcedureAmount(structureVO.getReceivableCommission());
            contractDTO.setLessorOtherCosts(structureVO.getLessorOtherincome());
            contractDTO.setReceivableFirmRebate(structureVO.getReceivableRebate());
            contractDTO.setReceivableInsuranceAmount(structureVO.getReceivableInsurance());
            contractDTO.setRetainedPrice(structureVO.getReceivableResidualValue());
            contractDTO.setReceivableOther(structureVO.getReceivableOtherincome());
            contractDTO.setReceivableServiceAmount(structureVO.getReceivableService());
            contractDTO.setVendorMarginAmount(structureVO.getSupplierMargin());
            contractDTO.setSystemCode(SystemEnum.CWZT.getCode());
            contractDTO.setContractStatus(BusinessContractStatusEnum.CONTRACT_STATUS_1.getCode());
            contractDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
            contractDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
            contractDTO.setManualLeaseFlag(YesOrNoEnum.YES.getCode());
            contractService.saveContractAndHis(contractDTO);
            contractDTOList.add(contractDTO);
        });

        // 推送偿还计划
        OfflineContractRepaymentPlanQueryDTO planQueryDTO = new OfflineContractRepaymentPlanQueryDTO();
        planQueryDTO.setContractCodeList(contractCodeList);
        List<OfflineContractRepaymentPlanVO> offlineContractRepaymentPlanVOS = offlineContractRepaymentPlanService.selectList(planQueryDTO);
        Map<String, List<OfflineContractRepaymentPlanVO>> repaymentPlanGroup = offlineContractRepaymentPlanVOS.stream().collect(Collectors.groupingBy(e -> e.getContractCode()));
        for (String contractCode : repaymentPlanGroup.keySet()) {
            // 按照日期排序
            List<OfflineContractRepaymentPlanVO> list = repaymentPlanGroup.get(contractCode).stream().sorted(Comparator.comparing(OfflineContractRepaymentPlanVO::getPlanDate)).collect(Collectors.toList());
            OfflineContractEntity offlineContractEntity = entities.stream().filter(a -> ObjectUtil.equals(a.getContractCode(), contractCode)).findFirst().orElse(null);
            List<RepaymentPlanSaveDTO> planSaveDTOList = Lists.newArrayList();
            for (int i = 0; i < list.size(); i++) {
                OfflineContractRepaymentPlanVO vo = list.get(i);
                RepaymentPlanSaveDTO planSaveDTO = new RepaymentPlanSaveDTO();
                planSaveDTO.setPeriods(vo.getPeriod());
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
            dataMap.put("contractCode", contractCode);
            dataMap.put("orgId", offlineContractEntity.getOrgId());
            dataMap.put("sceneCode", "线下合同录入");
            dataMap.put("orderId", list.stream().min(Comparator.comparingLong(OfflineContractRepaymentPlanVO::getId)).map(OfflineContractRepaymentPlanVO::getId).orElse(null));
            dataMap.put("repaymentPlanList", planSaveDTOList);
            log.info("线下合同录入推送偿还计划发送mq数据：" + JSONObject.toJSONString(dataMap));
            // 发送mq 推送偿还计划
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_DIRECT_TRANSACTION_DATA, RabbitmqConfig.ROUTINGKEY_REPAYMENT_PLAN_DATA, JSONObject.toJSONString(dataMap));
            // iRepaymentPlanService.saveRawData("1", JSONObject.parseObject(JSONObject.toJSONString(dataMap)));
        }

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

    @Override
    public Boolean voucher(List<Long> ids,String isSubmit) {
        List<OfflineContractEntity> offlineContractEntities = listByIds(ids);
        List<OfflineContractStructureEntity> contractStructureEntities = new ArrayList<>();
        List<OfflineContractRepaymentPlanEntity> repaymentPlanEntities = new ArrayList<>();
        for (OfflineContractEntity offlineContractEntity : offlineContractEntities) {
            if (StringUtils.isNotEmpty(offlineContractEntity.getContractCodeM())) {
                // 有主合同号 服务费合同 不判断租金计划
                OfflineContractStructureEntity contractStructureEntity = offlineContractStructureService.getBaseMapper().selectOne(Wrappers.<OfflineContractStructureEntity>lambdaQuery()
                        .eq(OfflineContractStructureEntity::getContractCode, offlineContractEntity.getContractCode())
                        .eq(OfflineContractStructureEntity::getContractCodeM, offlineContractEntity.getContractCodeM())
                );
                if (null == contractStructureEntity) {
                    throw new ServiceException("缺少交易结构信息");
                }
                contractStructureEntities.add(contractStructureEntity);
            } else {
                // 没有主合同号由 租赁合同
                OfflineContractStructureEntity contractStructureEntitie = offlineContractStructureService.getBaseMapper().selectOne(Wrappers.<OfflineContractStructureEntity>lambdaQuery()
                        .eq(OfflineContractStructureEntity::getContractCode, offlineContractEntity.getContractCode())
                        .eq(OfflineContractStructureEntity::getContractCodeM, "")
                );
                if (null == contractStructureEntitie) {
                    throw new ServiceException("缺少交易结构信息");
                }
                contractStructureEntities.add(contractStructureEntitie);
                List<OfflineContractRepaymentPlanEntity> planEntityList = offlineContractRepaymentPlanService.getBaseMapper().selectList(Wrappers.<OfflineContractRepaymentPlanEntity>lambdaQuery()
                        .eq(OfflineContractRepaymentPlanEntity::getContractCode, offlineContractEntity.getContractCode())
                );
                if (CollectionUtils.isEmpty(planEntityList)) {
                    throw new ServiceException("缺少租金计划信息");
                }
                repaymentPlanEntities.addAll(planEntityList);
            }

        }

        //生成凭证前先删除之前的凭证
        batchDeleteVoucher(ids);

        Map<String, OfflineContractStructureEntity> contractStructureEntityMap = contractStructureEntities.stream().collect(Collectors.toMap(e -> e.getContractCode(), e -> e));
        Map<String, List<OfflineContractRepaymentPlanEntity>> planGroupMap = repaymentPlanEntities.stream().collect(Collectors.groupingBy(e -> e.getContractCode()));

        //租赁类型
        R<List<SysDictData>> leaseTypeR = remoteDictService.listDictData(DictTypeEnum.LEASE_TYPE.getCode());
        Map<String, String> leaseTypeMap = leaseTypeR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));

        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        for (OfflineContractEntity offlineContractEntity : offlineContractEntities) {
            ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
            commonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            commonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            commonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
            commonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
            commonDTO.setOrderId(offlineContractEntity.getId().toString());
            commonDTO.setContractCode(offlineContractEntity.getContractCode());
            commonDTO.setContractName(offlineContractEntity.getContractName());
            commonDTO.setClientCode(offlineContractEntity.getClientCode());
            commonDTO.setClientName(offlineContractEntity.getClientName());
            commonDTO.setSceneCode(SceneEnum.HTQZ.getCode());
            commonDTO.setSceneName(SceneEnum.HTQZ.getDesc());
            commonDTO.setOrgId(offlineContractEntity.getOrgId());
            commonDTO.setBusinessDate(offlineContractEntity.getLeaseDateStart());
            commonDTO.setAccountDate(offlineContractEntity.getLeaseDateStart());
            commonDTO.setCurrencyType(offlineContractEntity.getCurrencyType());
            commonDTO.setBatchId(offlineContractEntity.getId());
            commonDTO.setBatchType(BatchTypeEnum.XXHT.getCode());
            commonDTO.setIsSubmit(isSubmit);
            Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);
            List<OfflineContractRepaymentPlanEntity> offlineContractRepaymentPlanEntities = planGroupMap.get(offlineContractEntity.getContractCode());
            OfflineContractStructureEntity offlineContractStructureEntity = contractStructureEntityMap.get(offlineContractEntity.getContractCode());
            //DR
            //1531.01.01 应收租金
            if (CollectionUtils.isNotEmpty(offlineContractRepaymentPlanEntities)) {
                commonMap.put("receivableLeaseAmount", offlineContractRepaymentPlanEntities.stream().map(e -> e.getRentAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
            }
            //1531.01.02 应收首付款
            commonMap.put("receivableFirstAmount", offlineContractStructureEntity.getReceivableDownpayment());
            //1531.01.04 应收手续费
            commonMap.put("receivableProcedureAmount", offlineContractStructureEntity.getReceivableCommission());
            //1531.01.06 应收保险费
            commonMap.put("receivableInsuranceAmount", offlineContractStructureEntity.getReceivableInsurance());
            //1531.01.03 应收期末残值
            commonMap.put("retainedPrice", offlineContractStructureEntity.getReceivableResidualValue());
            //1531.01.08 应收其他收入
            commonMap.put("receivableOther", offlineContractStructureEntity.getReceivableOtherincome());
            //1122.06 应收账款-应收服务费
            commonMap.put("receivableServiceAmount", offlineContractStructureEntity.getReceivableService());
            //CR
            //1531.01.09 应收销项税 根据其余数据直接生成
            //1122.07 应收服务费-销项税 根据其余数据直接生成
            //2202.01.01 应付租赁设备款-暂估
            commonMap.put("payableDeviceAmount", offlineContractStructureEntity.getPayableDevice());
            // 租赁类型
            commonMap.put("leaseType", leaseTypeMap.get(offlineContractEntity.getLeaseType()));
            //2202.02.01 应付其他租赁成本-暂估
            commonMap.put("estimateGPSExpense", BigDecimal.ZERO);
            //应付经销商服务费-暂估
            commonMap.put("payableService", offlineContractStructureEntity.getReceivableService());
            commonMap.put("receivableServiceTax", offlineContractEntity.getTaxRate());
            //2701.03.01 应付保险费-暂估
            commonMap.put("payableInsuranceAmount", NumberUtil.add(offlineContractStructureEntity.getLessorInsurance(), offlineContractStructureEntity.getReceivableInsurance()));
            //主营业务收入-服务收入?
            //1532.01 未实现融资租赁收益 根据其余数据直接生成
            commonMap.put("payableOtherAmount",offlineContractStructureEntity.getLessorOtherincome());
            commonMap.put("payableChannelExpense",offlineContractStructureEntity.getChannelFee());
            voucherMapList.add(commonMap);
        }
        log.info("生成凭证参数：{}", com.alibaba.fastjson.JSON.toJSONString(voucherMapList));
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
            log.error("线下合同导入 生成凭证失败");
        } else {
            for (VoucherInfoVO entry : voucherInfoVOList) {
                List<VoucherDTO> value = entry.getVoucherDTOList();
                if (CollectionUtils.isNotEmpty(value)) {
                    String vouchIds = value.stream().map(VoucherDTO::getId).map(String::valueOf).collect(
                            Collectors.joining(","));

                    this.lambdaUpdate()
                            .set(OfflineContractEntity::getVoucherId, vouchIds)
                            .set(OfflineContractEntity::getIsGenerateVoucher, YesOrNoEnum.YES.getCode())
                            .set(OfflineContractEntity::getExceptionType, entry.getErrorInfo())
                            .eq(OfflineContractEntity::getId, Long.parseLong(entry.getOrderId()))
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

    private void changeStatus(List<Long> ids, MarginStatusEnum marginStatusEnum) {
        LambdaUpdateWrapper<OfflineContractEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .in(OfflineContractEntity::getId, ids)
                .eq(OfflineContractEntity::getProcessStatus, MarginStatusEnum.SUBMITTED.getCode())
                .set(OfflineContractEntity::getUpdateTime, LocalDateTime.now())
                .set(OfflineContractEntity::getProcessStatus, marginStatusEnum.getCode());
        this.update(updateChainWrapper);
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
        OfflineContractEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("线下合同数据不存在");
        }
        if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
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
     * 删除凭证
     *
     * @param ids
     */
    public void batchDeleteVoucher(List<Long> ids) {
        //根据批次号删除凭证
        iVoucherService.deleteByBatchIdList(ids,BatchTypeEnum.XXHT.getCode());
    }

    /**
     * 更新凭证状态
     *
     * @param ids
     * @param approveDTO
     */
    public void updateVoucherStatus(List<Long> ids, CommonApproveDTO approveDTO){
        // 获取所有的凭证Id
        List<OfflineContractEntity> entityList = this.listByIds(ids);
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

    public void updateVoucherStatusByBatch(List<Long> batchIdList,String batchType,String status,String userNo,String userName) {
        iVoucherService.updateStatusByBatch(batchIdList,batchType,status,userNo,userName);
    }

}

