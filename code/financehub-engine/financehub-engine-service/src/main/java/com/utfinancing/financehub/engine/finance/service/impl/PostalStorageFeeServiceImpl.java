package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.LeaseIncomeDetailsEntity;
import com.utfinancing.financehub.engine.finance.entity.PostalStorageFeeDetailsEntity;
import com.utfinancing.financehub.engine.finance.entity.PostalStorageFeeEntity;
import com.utfinancing.financehub.engine.finance.mapper.PostalStorageFeeDetailsMapper;
import com.utfinancing.financehub.engine.finance.mapper.PostalStorageFeeMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.PostalStorageFeeDetailsVO;
import com.utfinancing.financehub.engine.finance.model.vo.PostalStorageFeeVO;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.finance.service.IPostalStorageFeeDetailsService;
import com.utfinancing.financehub.engine.finance.service.IPostalStorageFeeService;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-17
 * @Description :  PostalStorageFee服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class PostalStorageFeeServiceImpl extends ServiceImpl<PostalStorageFeeMapper, PostalStorageFeeEntity> implements IPostalStorageFeeService {

    private final PostalStorageFeeMapper postalStorageFeeMapper;
    private final IPostalStorageFeeDetailsService detailsService;
    private final PostalStorageFeeDetailsMapper detailsMapper;
    private final IContractService contractService;
    private final IRuleService iRuleService;

    @Override
    public Long savePostalStorageFee(PostalStorageFeeDTO dto) {
        PostalStorageFeeEntity entity = BeanUtil.copyProperties(dto, PostalStorageFeeEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updatePostalStorageFee(Long id, PostalStorageFeeDTO dto) {
        PostalStorageFeeEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public PostalStorageFeeDTO getPostalStorageFeeDTOById(Long id) {
        PostalStorageFeeEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, PostalStorageFeeDTO.class);
    }

    @Override
    public IPage<PostalStorageFeeVO> selectPage(PostalStorageFeeQueryDTO queryDTO) {
        LambdaQueryWrapper<PostalStorageFeeEntity> queryWrapper = Wrappers.<PostalStorageFeeEntity>lambdaQuery();
        Date queryDate = queryDTO.getAccountDate();
        if (null != queryDate) {
            queryDate = DateUtil.beginOfDay(queryDate);
            queryWrapper.eq(PostalStorageFeeEntity::getAccountDate, queryDate);
        }
        Date businessDate = queryDTO.getBusinessDate();
        if (null != businessDate) {
            businessDate = DateUtil.beginOfDay(businessDate);
            queryWrapper.eq(PostalStorageFeeEntity::getBusinessDate, businessDate);
        }
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), PostalStorageFeeEntity::getOrgId, queryDTO.getOrgIdList());
        IPage<PostalStorageFeeEntity> entityIPage = postalStorageFeeMapper.selectPage(new Page<PostalStorageFeeEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        IPage<PostalStorageFeeVO> page = ListBeanUtil.copyPage(entityIPage, PostalStorageFeeVO.class);
        page.getRecords().forEach(v -> {
            v.setBatchType(BatchTypeEnum.YCSXF.getCode());
        });
        return page;
    }

    @Override
    public IPage<PostalStorageFeeDetailsVO> selectDetailPage(PostalStorageFeeDetailsQueryDTO queryDTO) {
        LambdaQueryWrapper<PostalStorageFeeDetailsEntity> queryWrapper = Wrappers.<PostalStorageFeeDetailsEntity>lambdaQuery();
        queryWrapper.eq(null != queryDTO.getPostalStorageFeeId(), PostalStorageFeeDetailsEntity::getPostalStorageFeeId, queryDTO.getPostalStorageFeeId());
        IPage<PostalStorageFeeDetailsEntity> entityIPage = detailsService.getBaseMapper().selectPage(new Page<PostalStorageFeeDetailsEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, PostalStorageFeeDetailsVO.class);
    }

    @Override
    public void detailUpdate(PostalStorageFeeDetailsUpdateDTO saveDTO) {
        String postalStorageProjectType = saveDTO.getPostalStorageProjectType();
        BigDecimal allocationAmount = saveDTO.getAllocationAmount();
        // 根据id获取数据
        PostalStorageFeeDetailsEntity detailsEntity = detailsService.getById(saveDTO.getId());
        PostalStorageFeeEntity storageFeeEntity = getById(detailsEntity.getPostalStorageFeeId());
        // 获取差额
        BigDecimal subAmount = NumberUtil.sub(allocationAmount, detailsEntity.getAllocationAmount());
        // 更新 详情表-余额 主表-汇总
        detailsService.lambdaUpdate()
                .set(PostalStorageFeeDetailsEntity::getPostalStorageProjectType, postalStorageProjectType)
                .set(PostalStorageFeeDetailsEntity::getAllocationAmount, allocationAmount)
                .set(PostalStorageFeeDetailsEntity::getAllocationBalance, NumberUtil.sub(detailsEntity.getAllocationBalance(), subAmount))
                .eq(PostalStorageFeeDetailsEntity::getId, detailsEntity.getId())
                .update();
        this.lambdaUpdate()
                .set(PostalStorageFeeEntity::getAllocationAmount, NumberUtil.add(storageFeeEntity.getAllocationAmount(), subAmount))
                .set(PostalStorageFeeEntity::getAllocationBalance, NumberUtil.sub(storageFeeEntity.getAllocationBalance(), subAmount))
                .eq(PostalStorageFeeEntity::getId, detailsEntity.getPostalStorageFeeId())
                .update();
        // 邮储项目类型变更,更新到合同表
        if (!StringUtils.equals(postalStorageProjectType, detailsEntity.getPostalStorageProjectType())) {
            ContractDTO contractDTOByCode = contractService.getContractDTOByCode(detailsEntity.getContractCode(),detailsEntity.getOrgId());
            contractDTOByCode.setPostalSavingsProjectType(postalStorageProjectType);
            contractService.updateContract(contractDTOByCode.getId(), contractDTOByCode);
        }
    }

    @Override
    public void submit(List<Long> ids) {
        LambdaUpdateWrapper<PostalStorageFeeEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .in(PostalStorageFeeEntity::getId, ids)
                .in(PostalStorageFeeEntity::getProcessStatus, MarginStatusEnum.canChangeStatus())
                .set(PostalStorageFeeEntity::getUpdateTime, LocalDateTime.now())
                .set(PostalStorageFeeEntity::getSubmitBy, SecurityUtils.getUserId())
                .set(PostalStorageFeeEntity::getProcessStatus, MarginStatusEnum.SUBMITTED.getCode());
        this.update(updateChainWrapper);
    }

    @Override
    public void withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        changeStatus(ids, MarginStatusEnum.ENTERED);
    }

    @Override
    public void pass(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        changeStatus(ids, MarginStatusEnum.PASS);
    }

    @Override
    public void fail(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        changeStatus(ids, MarginStatusEnum.FAILED);
    }

    private void changeStatus(List<Long> ids, MarginStatusEnum marginStatusEnum) {
        LambdaUpdateWrapper<PostalStorageFeeEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .in(PostalStorageFeeEntity::getId, ids)
                .eq(PostalStorageFeeEntity::getProcessStatus, MarginStatusEnum.SUBMITTED.getCode())
                .set(PostalStorageFeeEntity::getUpdateTime, LocalDateTime.now())
                .set(PostalStorageFeeEntity::getProcessStatus, marginStatusEnum.getCode());
        this.update(updateChainWrapper);
    }

    @Override
    public void generate(PostalStorageFeeQueryDTO queryDTO) {
        Date queryDate = queryDTO.getBusinessDate();
        if (null == queryDate) {
            return;
        }
        DateTime beginOfMonth = DateUtil.beginOfMonth(queryDate);
        DateTime endOfMonth = DateUtil.endOfMonth(queryDate);
        queryDate = DateUtil.beginOfDay(endOfMonth);
        // 获取现有数据 排除已经审批的公司
        List<String> notGenerateOrgIds = new ArrayList<>();
        List<PostalStorageFeeEntity> serviceFeeEntities = getBaseMapper().selectList(Wrappers.<PostalStorageFeeEntity>lambdaQuery()
                .eq(PostalStorageFeeEntity::getBusinessDate, queryDate));
        if (CollectionUtils.isNotEmpty(serviceFeeEntities)) {
            // 删除未提交数据
            List<Long> deleteIds = serviceFeeEntities.stream().filter(e -> MarginStatusEnum.canChangeStatus().contains(e.getProcessStatus())).map(e -> e.getId()).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deleteIds)) {
                LambdaUpdateWrapper<PostalStorageFeeEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
                updateChainWrapper
                        .in(PostalStorageFeeEntity::getId, deleteIds)
                        .set(PostalStorageFeeEntity::getUpdateTime, LocalDateTime.now())
                        .set(PostalStorageFeeEntity::getDelFlag, YesOrNoEnum.YES.getCode());
                this.update(updateChainWrapper);
                LambdaUpdateWrapper<PostalStorageFeeDetailsEntity> detailUpdateChainWrapper = new LambdaUpdateWrapper<>();
                detailUpdateChainWrapper
                        .in(PostalStorageFeeDetailsEntity::getPostalStorageFeeId, deleteIds)
                        .set(PostalStorageFeeDetailsEntity::getUpdateTime, LocalDateTime.now())
                        .set(PostalStorageFeeDetailsEntity::getDelFlag, YesOrNoEnum.YES.getCode());
                detailsService.update(detailUpdateChainWrapper);
            }
            // 获取不生成数据的公司
            notGenerateOrgIds = serviceFeeEntities.stream().filter(e -> MarginStatusEnum.cantChangeStatus().contains(e.getProcessStatus())).map(e -> e.getOrgId()).distinct().collect(Collectors.toList());
        }
        // 查询合同 开始时间在今年的 邮储手续费大于0
        LambdaQueryWrapper<ContractEntity> queryWrapper = Wrappers.<ContractEntity>lambdaQuery();
        queryWrapper.le(ContractEntity::getLeaseDateStart, endOfMonth);
        queryWrapper.ge(ContractEntity::getLeaseDateEnd, beginOfMonth);
        queryWrapper.gt(ContractEntity::getPayableProcedureCost, 0);
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getOrgIdList()), ContractEntity::getOrgId, queryDTO.getOrgIdList());
        queryWrapper.notIn(CollectionUtils.isNotEmpty(notGenerateOrgIds), ContractEntity::getOrgId, notGenerateOrgIds);
        List<ContractEntity> contractEntities = contractService.getBaseMapper().selectList(queryWrapper);
        if (CollectionUtils.isEmpty(contractEntities)) {
            return;
        }

        List<String> contractCodeList = contractEntities.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
        // 查询已有合同最新邮储手续费分摊数据 group bycode max per
        PostalStorageFeeDetailsQueryDTO postalStorageFeeDetailsQueryDTO = new PostalStorageFeeDetailsQueryDTO();
        postalStorageFeeDetailsQueryDTO.setContractCodeList(contractCodeList);
        List<PostalStorageFeeDetailsEntity> postalStorageFeeDetailsEntities = detailsMapper.listByContractCodes(postalStorageFeeDetailsQueryDTO);
        Map<String, PostalStorageFeeDetailsEntity> feeDetailsEntityMap = postalStorageFeeDetailsEntities.stream().collect(Collectors.toMap(e -> e.getContractCode(), e -> e, (a, b) -> b));

        List<PostalStorageFeeDetailsEntity> saveList = new ArrayList<>();
        Date finalQueryDate = queryDate;
        contractEntities.forEach(contract -> {
            PostalStorageFeeDetailsEntity detailsEntity = feeDetailsEntityMap.get(contract.getContractCode());
            String postalStorageProjectType = contract.getPostalSavingsProjectType();
            BigDecimal payableProcedureCost = contract.getPayableProcedureCost();
            PostalStorageFeeDetailsEntity saveEntity = new PostalStorageFeeDetailsEntity();
            Date leaseDateStart = contract.getLeaseDateStart();
            Date leaseDateEnd = contract.getLeaseDateEnd();
            if (null == leaseDateStart || null == leaseDateEnd) {
                log.info("邮储手续费合同" + contract.getContractCode() + "无起止日期，无法生成");
                return;
            }
            saveEntity.setBusinessDate(finalQueryDate);
            saveEntity.setOrgId(contract.getOrgId());
            saveEntity.setContractCode(contract.getContractCode());
            saveEntity.setClientCode(contract.getClientCode());
            saveEntity.setClientName(contract.getClientName());
            saveEntity.setContractStatus(contract.getContractStatus());
            saveEntity.setPostalStorageProjectType(postalStorageProjectType);
            saveEntity.setLeaseDateStart(leaseDateStart);
            saveEntity.setLeaseTerm((int) DateUtil.betweenMonth(leaseDateStart, leaseDateEnd, true) + 1);
            saveEntity.setPayableProcedureCost(payableProcedureCost);
            saveEntity.setAllocationAmount(NumberUtil.div(payableProcedureCost, saveEntity.getLeaseTerm()));
            if (null == detailsEntity) {
                // 新增的 从1开始记录
                saveEntity.setAllocationBalance(NumberUtil.sub(payableProcedureCost, saveEntity.getAllocationAmount()));
                saveEntity.setAllocatedPeriods(1);
            } else {
                // 以前就有的 查询该合同上次的信息
                BigDecimal allocationBalance = NumberUtil.sub(detailsEntity.getAllocationBalance(), saveEntity.getAllocationAmount());
                // 补支付手续费新增额到余额
                BigDecimal allocationAmountDifference = NumberUtil.sub(payableProcedureCost, detailsEntity.getAllocationAmount());
                allocationBalance = NumberUtil.add(allocationBalance, allocationAmountDifference);
                saveEntity.setAllocationBalance(allocationBalance);
                saveEntity.setAllocatedPeriods(detailsEntity.getAllocatedPeriods() + 1);
                // 调整尾差
                if (BigDecimal.ZERO.compareTo(allocationBalance) > 0) {
                    saveEntity.setAllocationBalance(BigDecimal.ZERO);
                    saveEntity.setAllocationAmount(NumberUtil.add(saveEntity.getAllocationAmount(), allocationBalance));
                }
            }
            // 如果是最后一期，且还有余额，则全部调整入分摊金额
            BigDecimal allocationBalance = saveEntity.getAllocationBalance();
            if (endOfMonth.after(leaseDateEnd) && BigDecimal.ZERO.compareTo(allocationBalance) < 0) {
                saveEntity.setAllocationAmount(NumberUtil.add(saveEntity.getAllocationAmount(), allocationBalance));
                saveEntity.setAllocationBalance(BigDecimal.ZERO);
            }
            // 分摊金额大于0 则生成
            if (saveEntity.getAllocationAmount().compareTo(BigDecimal.ZERO) > 0) {
                saveList.add(saveEntity);
            }

        });
        Map<String, List<PostalStorageFeeDetailsEntity>> groupByOrgId = saveList.stream().filter(e -> StringUtils.isNotBlank(e.getOrgId()))
                .collect(Collectors.groupingBy(e -> e.getOrgId()));
        groupByOrgId.forEach((orgId, detailsEntityList) -> {
            PostalStorageFeeEntity entity = new PostalStorageFeeEntity();
            entity.setBusinessDate(finalQueryDate);
            entity.setOrgId(orgId);
            entity.setProcessStatus(MarginStatusEnum.ENTERED.getCode());
            entity.setPayableProcedureCost(detailsEntityList.stream().map(e -> e.getPayableProcedureCost()).reduce(BigDecimal.ZERO, BigDecimal::add));
            entity.setAllocationAmount(detailsEntityList.stream().map(e -> e.getAllocationAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
            entity.setAllocationBalance(detailsEntityList.stream().map(e -> e.getAllocationBalance()).reduce(BigDecimal.ZERO, BigDecimal::add));
            this.save(entity);
            detailsEntityList.forEach(e -> e.setPostalStorageFeeId(entity.getId()));
            detailsService.saveBatch(detailsEntityList);
        });

    }

    @Override
    public void voucher(List<Long> ids) {
        List<PostalStorageFeeEntity> postalStorageFeeEntities = listByIds(ids);
        List<PostalStorageFeeDetailsEntity> detailsEntities = detailsService.getBaseMapper().selectList(Wrappers.<PostalStorageFeeDetailsEntity>lambdaQuery()
                .in(PostalStorageFeeDetailsEntity::getPostalStorageFeeId, postalStorageFeeEntities.stream().map(e -> e.getId()).collect(Collectors.toList())));
        if (CollectionUtils.isEmpty(detailsEntities)) {
            throw new ServiceException("缺少分摊信息");
        }
        DateTime accountDate = DateUtil.beginOfDay(new Date());
        // 查询合同信息
        List<ContractDTO> contractDTOS = contractService.listContractDTOByCodeList(detailsEntities.stream().map(e -> e.getContractCode()).distinct().collect(Collectors.toList()));
        Map<String, ContractDTO> contractMap = contractDTOS.stream().collect(Collectors.toMap(e -> e.getContractCode(), e -> e));

        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        for (PostalStorageFeeDetailsEntity entity : detailsEntities) {
            ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
            commonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            commonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            commonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
            commonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
            commonDTO.setOrderId(entity.getId().toString());
            commonDTO.setContractCode(entity.getContractCode());
            ContractDTO contractDTO = contractMap.get(entity.getContractCode());
            if (null != contractDTO) {
                commonDTO.setContractName(contractDTO.getContractName());
                commonDTO.setCurrencyType(contractDTO.getCurrencyType());
            }
            commonDTO.setClientCode(entity.getClientCode());
            commonDTO.setClientName(entity.getClientName());
            commonDTO.setSceneCode(SceneEnum.YCSXFFT.getCode());
            commonDTO.setSceneName(SceneEnum.YCSXFFT.getDesc());
            commonDTO.setOrgId(entity.getOrgId());
            commonDTO.setBusinessDate(entity.getBusinessDate());
            commonDTO.setBatchId(entity.getPostalStorageFeeId());
            commonDTO.setBatchType(BatchTypeEnum.YCSXF.getCode());
            Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);
            // 应付手续费成本
            commonMap.put("payableProcedureCost", entity.getAllocationAmount());
            voucherMapList.add(commonMap);
        }
        log.info("生成凭证参数：{}", JSON.toJSONString(voucherMapList));
        List<VoucherInfoVO> voucherInfoVOList = iRuleService.batchExecuteRule(voucherMapList);
        if (voucherInfoVOList == null || voucherInfoVOList.isEmpty()) {
            log.error("邮储手续费分摊 生成凭证失败");
        } else {
            for (VoucherInfoVO entry : voucherInfoVOList) {
                List<VoucherDTO> value = entry.getVoucherDTOList();
                if (CollectionUtils.isNotEmpty(value)) {
                    postalStorageFeeEntities.forEach(e -> {
                        e.setIsGenerateVoucher(YesOrNoEnum.YES.getCode());
                        e.setAccountDate(accountDate);
                    });
                    updateBatchById(postalStorageFeeEntities);

                    String vouchIds = value.stream().map(VoucherDTO::getId).map(String::valueOf).collect(
                            Collectors.joining(","));
                    detailsService.lambdaUpdate()
                            .set(PostalStorageFeeDetailsEntity::getVoucherId, vouchIds)
                            .set(PostalStorageFeeDetailsEntity::getAccountDate, accountDate)
                            .set(PostalStorageFeeDetailsEntity::getExceptionType, entry.getErrorInfo())
                            .eq(PostalStorageFeeDetailsEntity::getId, Long.parseLong(entry.getOrderId()))
                            .update();
                }
            }
        }
    }

}

