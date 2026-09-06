package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
import com.utfinancing.financehub.engine.dw.model.dto.DwBzHetjbxxDDTO;
import com.utfinancing.financehub.engine.dw.model.dto.TInfoPerformanceAttributionSyncQueryDTO;
import com.utfinancing.financehub.engine.dw.service.IDwBzHetjbxxDService;
import com.utfinancing.financehub.engine.dw.service.IDwDictDataService;
import com.utfinancing.financehub.engine.dw.service.ITInfoPerformanceAttributionSyncService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.constant.DefaultConstant;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.ContractHisMapper;
import com.utfinancing.financehub.engine.finance.mapper.ContractMapper;
import com.utfinancing.financehub.engine.finance.mapper.ContractStatusRecordMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.entity.InterfaceDataEntity;
import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataQueryDTO;
import com.utfinancing.financehub.engine.rule.service.IInterfaceDataService;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessDTO;
import com.utfinancing.financehub.engine.scene.service.IBusinessService;
import com.utfinancing.financehub.engine.scene.service.IFieldMappingService;
import com.utfinancing.financehub.engine.verification.service.IVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-13
 * @Description :  Contract服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ContractServiceImpl extends ServiceImpl<ContractMapper, ContractEntity> implements IContractService {

    private final ContractMapper contractMapper;
    private final IContractMonthService contractMonthService;
    private final RemoteDictService remoteDictService;
    private final IFieldMappingService fieldMappingService;
    private final ContractHisMapper contractHisMapper;
    private final IOrgCompanyService orgCompanyService;
    private final IDwBzHetjbxxDService dwBzHetjbxxDService;
    private final IContractAsyncService contractAsyncService;
    private final IDwDictDataService dwDictDataService;
    private final IRepaymentPlanHisService repaymentPlanHisService;
    private final IContractBalanceService iContractBalanceService;
    private final IClientService iClientService;
    private final ITInfoPerformanceAttributionSyncService itInfoPerformanceAttributionSyncService;
    private final IBusinessService businessService;
    private final IOrgCompanyService iOrgCompanyService;
    private final ContractStatusRecordMapper contractStatusRecordMapper;
    private final IInterfaceDataService iInterfaceDataService;
    @Resource
    private IContractNewTransactionService contractNewTransactionService;
    @Lazy
    @Resource
    private IVerificationService iVerificationService;


    @Override
    public Long saveContract(ContractDTO dto) {
        ContractEntity entity = BeanUtil.copyProperties(dto, ContractEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long saveContractAndHis(ContractDTO dto) {
        ContractEntity entity = BeanUtil.copyProperties(dto, ContractEntity.class);
        this.save(entity);
        ContractHisDTO contractHisDTO = BeanUtil.copyProperties(dto, ContractHisDTO.class);
        contractHisDTO.setProcessStatus(MarginStatusEnum.NOT_ENTERED.getCode());
        ContractHisEntity hisEntity = BeanUtil.copyProperties(contractHisDTO, ContractHisEntity.class);
        contractHisMapper.insert(hisEntity);
        return entity.getId();
    }

    @Override
    public Long updateContract(Long id, ContractDTO dto) {
        ContractEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.setContractStatus(null);
        entity.updateById();
        return id;
    }

    @Override
    public ContractDTO getContractDTOById(Long id) {
        ContractEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ContractDTO.class);
    }

    @Override
    public IPage<ContractVO> selectPage(ContractQueryDTO queryDTO) {
        LambdaQueryWrapper<ContractEntity> queryWrapper = getWrapper(queryDTO);
        IPage<ContractEntity> entityIPage = contractMapper.selectPage(new Page<ContractEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        List<SysDictData> sysDictDataList = remoteDictService.listDictData(DictTypeEnum.SYS_FORM_SOURCE.getCode()).getData();
        Map<String, String> systemCodeMap = sysDictDataList.stream().collect(HashMap::new, (map, item) -> map.put(item.getDictValue(), item.getDictLabel()), HashMap::putAll);
        IPage<ContractVO> contractVOIPage = ListBeanUtil.copyPage(entityIPage, ContractVO.class);
        contractVOIPage.getRecords().forEach(v -> {
            if (systemCodeMap.containsKey(v.getSystemCode())) {
                v.setSystemCodeName(systemCodeMap.get(v.getSystemCode()));
            }
        });
        return contractVOIPage;
    }

    public List<ContractVO> selectAllContractCode(ContractQueryDTO queryDTO) {
        return contractMapper.selectContract(queryDTO);
    }

    public LambdaQueryWrapper<ContractEntity> getWrapper(ContractQueryDTO queryDTO) {
        LambdaQueryWrapper<ContractEntity> queryWrapper = Wrappers.<ContractEntity>lambdaQuery();
        if (StringUtils.isNotEmpty(queryDTO.getContractCode())) {
            queryWrapper.like(ContractEntity::getContractCode, queryDTO.getContractCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getClientName())) {
            queryWrapper.like(ContractEntity::getClientName, queryDTO.getClientName());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getOrgIds())) {
            queryWrapper.in(ContractEntity::getOrgId, queryDTO.getOrgIds());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getContractStatuses())) {
            queryWrapper.in(ContractEntity::getContractStatus, queryDTO.getContractStatuses());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getFinancialContractStatuses())) {
            queryWrapper.in(ContractEntity::getFinancialContractStatus, queryDTO.getFinancialContractStatuses());
        }

        if (ObjectUtils.isNotNull(queryDTO.getBusinessDateStart())) {
            queryWrapper.ge(ContractEntity::getBusinessDate, queryDTO.getBusinessDateStart());
        }
        if (ObjectUtils.isNotNull(queryDTO.getBusinessDateEnd())) {
            queryWrapper.le(ContractEntity::getBusinessDate, queryDTO.getBusinessDateEnd());
        }
        if (ObjectUtils.isNotNull(queryDTO.getLeaseDateStart())) {
            queryWrapper.ge(ContractEntity::getLeaseDateStart, queryDTO.getLeaseDateStart());
        }
        if (ObjectUtils.isNotNull(queryDTO.getLeaseDateEnd())) {
            queryWrapper.le(ContractEntity::getLeaseDateEnd, queryDTO.getLeaseDateEnd());
        }
        if (ObjectUtils.isNotNull(queryDTO.getLeaseDateStartDate())) {
            queryWrapper.ge(ContractEntity::getLeaseDateStart, queryDTO.getLeaseDateStartDate());
        }
        if (ObjectUtils.isNotNull(queryDTO.getLeaseDateStartEndDate())) {
            queryWrapper.le(ContractEntity::getLeaseDateStart, queryDTO.getLeaseDateStartEndDate());
        }
        if (ObjectUtils.isNotNull(queryDTO.getLeaseDateEndStartDate())) {
            queryWrapper.ge(ContractEntity::getLeaseDateEnd, queryDTO.getLeaseDateEndStartDate());
        }
        if (ObjectUtils.isNotNull(queryDTO.getLeaseDateEndEndDate())) {
            queryWrapper.le(ContractEntity::getLeaseDateEnd, queryDTO.getLeaseDateEndEndDate());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getSystemCodeList())) {
            queryWrapper.in(ContractEntity::getSystemCode, queryDTO.getSystemCodeList());
        }
        //只展示不是抵债资产的数据
        queryWrapper.eq(ContractEntity::getIsDzzc, YesOrNoEnum.NO.getCode());
        //过滤掉主合同不为空的数据
//        queryWrapper.isNull(ContractEntity::getContractCodeM);
        queryWrapper.last("and (contract_code_m is null or contract_code_m ='' or contract_code = contract_code_m) " +
                "order by coalesce(business_date, lease_date_start, create_time) desc, id desc");
        return queryWrapper;
    }

    @Override
    public String saveOrUpdateContract(Map<String, Object> interfaceDataMap) {
        String contractCode = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_CONTRACT_CODE);
        String contractCodeM = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_CONTRACT_CODE_M);
        String orgId = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_ORG_ID);
        String isSubmitFlag = MapUtil.getStr(interfaceDataMap, RuleConstant.IS_SUBMIT);
        String sceneCode = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_SCENE_CODE);
        String financialContractStatus = MapUtil.getStr(interfaceDataMap, RuleConstant.FINANCIAL_CONTRACT_STATUS);
        String clientType = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_CLIENT_TYPE);
        String clientAttribute = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_CLIENT_ATTRIBUTE);
        String contractLeaseType = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_CONTRACT_LEASE_TYPE);
        if (StrUtil.isBlank(contractCode)) {
            return null;
        }
        QueryWrapper<ContractEntity> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(ContractEntity::getContractCode, contractCode);
        if (StringUtils.isNotEmpty(orgId)) {
            queryWrapper.lambda().eq(ContractEntity::getOrgId, orgId);
        }
        queryWrapper.lambda().orderByDesc(ContractEntity::getId);
        ContractEntity entity = null;
        List<ContractEntity> contractEntityList = this.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(contractEntityList)) {
            entity = contractEntityList.get(0);
        }
        if (entity != null) {

            String systemCode = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_SYSTEM_CODE);
            String oldClientType = entity.getClientType();

            if (YesOrNoEnum.NO.getCode().equals(isSubmitFlag) || FinancialContractStatusEnum.THREE.getDesc().equals(financialContractStatus)) {
                BeanUtil.copyProperties(interfaceDataMap, entity, "financialContractStatus", "financialContractStatusUpdateTime");
                if (StringUtils.isNotEmpty(oldClientType) && DefaultConstant.CLIENT_TYPE_LESSEE.equals(oldClientType)) {
                    entity.setClientType(oldClientType);
                }
            } else {

                if (StringUtils.isNotEmpty(clientType) && DefaultConstant.CLIENT_TYPE_LESSEE.equals(clientType)) {
                    BeanUtil.copyProperties(interfaceDataMap, entity);
                } else {
                    if (StringUtils.isNotEmpty(oldClientType) && DefaultConstant.CLIENT_TYPE_LESSEE.equals(oldClientType)) {
                        BeanUtil.copyProperties(interfaceDataMap, entity, RuleConstant.FIELD_CLIENT_CODE,
                                RuleConstant.FIELD_CLIENT_NAME, RuleConstant.FIELD_CLIENT_TYPE);
                    } else {
                        BeanUtil.copyProperties(interfaceDataMap, entity, RuleConstant.FIELD_CLIENT_CODE,
                                RuleConstant.FIELD_CLIENT_NAME);
                    }
                }

                if (SystemEnum.TYPT.getCode().equals(systemCode) || SystemEnum.XWXT.getCode().equals(systemCode)) {
                    entity.setInvoicingFlag(DefaultConstant.INVOICE);
                }

                // modify by zhangli.chen for 商用车和乘用车默认开票标识为计提 on 20250611
                if (SystemEnum.SYCXT.getCode().equals(systemCode) || SystemEnum.CYCXT.getCode().equals(systemCode)) {
                    if (StringUtils.isNotEmpty(clientType) && DefaultConstant.CLIENT_TYPE_LESSEE.equals(clientType)) {
                        if (StringUtils.isNotEmpty(clientAttribute)) {
                            entity.setInvoicingFlag(DefaultConstant.ACCRUE);
                        }
                    } else if (StringUtils.isNotEmpty(contractCodeM)) {
                        entity.setInvoicingFlag(DefaultConstant.ACCRUE);
                    }
                }
//                if (SystemEnum.SYCXT.getCode().equals(systemCode) || SystemEnum.CYCXT.getCode().equals(systemCode)) {
//                    if (StringUtils.isNotEmpty(clientType) && DefaultConstant.CLIENT_TYPE_LESSEE.equals(clientType)) {
//                        if (StringUtils.isNotEmpty(clientAttribute)) {
//                            if (clientAttribute.equals(DefaultConstant.PERSON)) {
//                                entity.setInvoicingFlag(DefaultConstant.ACCRUE);
//                            } else if (clientAttribute.equals(DefaultConstant.LEGAL_PERSON)) {
//                                entity.setInvoicingFlag(DefaultConstant.INVOICE);
//                            }
//                        }
//                    } else if (StringUtils.isNotEmpty(contractCodeM)) {
//                        entity.setInvoicingFlag(DefaultConstant.ACCRUE);
//                    }
//                }
//                if (StringUtils.isNotEmpty(contractLeaseType)
//                        && LeaseTypeEnum.DIRECT.getCode().equals(contractLeaseType) && StringUtils.isNotEmpty(contractCodeM)) {
//                    entity.setTaxRate(new BigDecimal("0.13"));
//                } else {
//                    entity.setTaxRate(new BigDecimal("0.06"));
//                }
                // modify by zhangli.chen for 修复直租且非咨询服务费合同税率问题 on 20250208
                if (LeaseTypeEnum.DIRECT.getCode().equals(contractLeaseType) && !StringUtils.isEmpty(contractCode) && !contractCode.toUpperCase().startsWith("CS")) {
                    entity.setTaxRate(new BigDecimal("0.13"));
                } else {
                    if (StringUtils.isNotEmpty(contractLeaseType)) {
                        entity.setTaxRate(new BigDecimal("0.06"));
                    }
                }
            }
            // 非中台 交易结构变更
            if (!SystemEnum.CWZT.getCode().equals(systemCode) && SceneEnum.JYJGBG.getCode().equals(sceneCode)) {
                ContractChangeSaveDTO contractChangeSaveDTO = BeanUtil.copyProperties(interfaceDataMap, ContractChangeSaveDTO.class);
                entity.setPayableDeviceAmount(NumberUtil.add(contractChangeSaveDTO.getPayableDeviceAdjustAmount(), entity.getPayableDeviceAmount()));
                entity.setReceivableFirstAmount(NumberUtil.add(contractChangeSaveDTO.getFirstAdjustAmount(), entity.getReceivableFirstAmount()));
                entity.setReceivableMarginAmount(NumberUtil.add(contractChangeSaveDTO.getImplementMarginAdjustAmount(), entity.getReceivableMarginAmount()));
                entity.setPayableChannelExpense(NumberUtil.add(contractChangeSaveDTO.getChannelAdjustExpense(), entity.getPayableChannelExpense()));
                entity.setPayableInnerExpense(NumberUtil.add(contractChangeSaveDTO.getInnerAdjustExpense(), entity.getPayableInnerExpense()));
                entity.setReceivableProcedureAmount(NumberUtil.add(contractChangeSaveDTO.getProcedureAdjustRevenues(), entity.getReceivableProcedureAmount()));
                entity.setReceivableFirmRebate(NumberUtil.add(contractChangeSaveDTO.getFirmAdjustRebate(), entity.getReceivableFirmRebate()));
                entity.setReceivableInsuranceAmount(NumberUtil.add(contractChangeSaveDTO.getInsuranceAdjustAmount(), entity.getReceivableInsuranceAmount()));
                entity.setRetainedPrice(NumberUtil.add(contractChangeSaveDTO.getResidualAdjustAmount(), entity.getRetainedPrice()));
                entity.setReceivableOther(NumberUtil.add(contractChangeSaveDTO.getOtherAdjustRevenues(), entity.getReceivableOther()));
                entity.setReceivableServiceAmount(NumberUtil.add(contractChangeSaveDTO.getServiceAdjustAmount(), entity.getReceivableServiceAmount()));
                entity.setVendorMarginAmount(NumberUtil.add(contractChangeSaveDTO.getMarginAdjustAmount(), entity.getVendorMarginAmount()));
                entity.setPayableOtherAmount(NumberUtil.add(contractChangeSaveDTO.getOtherCostAdjustAmount(), entity.getPayableOtherAmount()));
                entity.setPayableBraceletCost(NumberUtil.add(contractChangeSaveDTO.getPayableBraceletAdjustAmount(), entity.getPayableBraceletCost()));

                entity.setChannelFees(NumberUtil.add(entity.getPayableChannelExpense(), entity.getPayableInnerExpense()));
                entity.setLessorOtherCosts(NumberUtil.add(entity.getPayableOtherAmount(), entity.getEstimateGPSExpense(), entity.getPayableIntroduce(), entity.getPayableLawAmount(), entity.getPayableBraceletCost()));
            }

            ContractChangeSaveDTO tmpDto = BeanUtil.copyProperties(interfaceDataMap, ContractChangeSaveDTO.class);

            if ((SystemEnum.XWXT.getCode().equals(systemCode) || SystemEnum.TYPT.getCode().equals(systemCode)) && SceneEnum.JYJGBG.getCode().equals(sceneCode)) {
                entity.setReceivableRent(NumberUtil.add(entity.getReceivableRent(), tmpDto.getReceivablePrincipalAdjustAmount(), tmpDto.getReceivableInterestAdjustAmount(), tmpDto.getDeratePrincipalAmount().multiply(new BigDecimal(-1)), tmpDto.getDerateInterestAmount().multiply(new BigDecimal(-1))));
                entity.setPayableDeviceAmount(NumberUtil.add(entity.getPayableDeviceAmount(), tmpDto.getReceivablePrincipalAdjustAmount()));
            } else if (!SystemEnum.XWXT.getCode().equals(systemCode) && !SystemEnum.TYPT.getCode().equals(systemCode) && SceneEnum.JYJGBG.getCode().equals(sceneCode)) {
                entity.setReceivableRent(NumberUtil.add(entity.getReceivableRent(), tmpDto.getReceivableLeaseAdjustAmount(), tmpDto.getDeratePrincipalAmount().multiply(new BigDecimal(-1)), tmpDto.getDerateInterestAmount().multiply(new BigDecimal(-1))));
                entity.setPayableDeviceAmount(NumberUtil.add(entity.getPayableDeviceAmount(), tmpDto.getPayableDeviceAdjustAmount()));
            }
            entity.updateById();
        } else {
            if (YesOrNoEnum.NO.getCode().equals(isSubmitFlag) || FinancialContractStatusEnum.THREE.getDesc().equals(financialContractStatus)) {
                entity = BeanUtil.copyProperties(interfaceDataMap, ContractEntity.class, "financialContractStatus", "financialContractStatusUpdateTime");
            } else {
                entity = BeanUtil.copyProperties(interfaceDataMap, ContractEntity.class);
            }

            ContractChangeSaveDTO tmpDto = BeanUtil.copyProperties(interfaceDataMap, ContractChangeSaveDTO.class);

            if (SceneEnum.HTQZ.getCode().equals(sceneCode)) {
                entity.setReceivableRent(NumberUtil.add(entity.getReceivableRent(), tmpDto.getReceivableLeaseAmount()));
                entity.setPayableDeviceAmount(NumberUtil.add(entity.getPayableDeviceAmount(), tmpDto.getPayableDeviceAmount()));
            }
            //校验租赁收款场景合同编码是否存在，存在则不新增否则新增
            boolean isExists = Boolean.FALSE;
            if (sceneCode.equals(SceneEnum.ZLSK.getCode())) {
                isExists = this.lambdaQuery().eq(ContractEntity::getContractCode, contractCode).exists();
            }
            if (!isExists) {
                this.save(entity);
                // 新增合同才往合同历史表里插入数据
                ContractHisDTO contractHisDTO = BeanUtil.copyProperties(entity, ContractHisDTO.class);
                contractHisDTO.setProcessStatus(MarginStatusEnum.NOT_ENTERED.getCode());//默认状态为 未录入
                ContractHisEntity hisEntity = BeanUtil.copyProperties(contractHisDTO, ContractHisEntity.class, GenConstants.BASE_ENTITY);
                contractHisMapper.insert(hisEntity);
            }
        }
        return contractCode;
    }

    @Override
    public ContractDTO getContractDTOByCode(String contractCode, String orgId) {
        QueryWrapper<ContractEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContractEntity::getContractCode, contractCode);
        if (StringUtils.isNotEmpty(orgId)) {
            queryWrapper.lambda().eq(ContractEntity::getOrgId, orgId);
        }
        queryWrapper.lambda().orderByDesc(ContractEntity::getId);
        List<ContractEntity> entityList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        return BeanUtil.copyProperties(entityList.get(0), ContractDTO.class);
    }

    @Override
    public ContractDTO getTranStatusDTOByCode(String contractCode, String orgId) {
        QueryWrapper<ContractEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContractEntity::getContractCode, contractCode);
        if (StringUtils.isNotEmpty(orgId)) {
            queryWrapper.lambda().eq(ContractEntity::getOrgId, orgId);
        }
        queryWrapper.lambda().orderByDesc(ContractEntity::getId);
        List<ContractEntity> entityList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        ContractDTO contractDTO = BeanUtil.copyProperties(entityList.get(0), ContractDTO.class);
        contractDTO.setContractStatus(ContractStatusTransferEnum.transferContractStatus(contractDTO.getContractStatus()));
        return contractDTO;
    }

    @Override
    public List<ContractEntity> getContractDTOByCode(String contractCode) {
        QueryWrapper<ContractEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(ContractEntity::getContractCode, contractCode);
        queryWrapper.lambda().orderByDesc(ContractEntity::getContractCode);
        List<ContractEntity> entityList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        return entityList;
    }


    @Override
    public List<ContractDTO> listContractDTOByCodeList(List<String> contractCodeList) {
        List<ContractEntity> contractEntities = contractMapper.selectList(
                Wrappers.<ContractEntity>lambdaQuery()
                        .in(ContractEntity::getContractCode, contractCodeList));
        if (CollectionUtils.isEmpty(contractEntities)) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(contractEntities, ContractDTO.class);
    }

    @Override
    public Map<String, Object> getContractMap(String contractCode, String orgId) {
        ContractDTO contractEntity = this.getContractDTOByCode(contractCode, orgId);
        if (contractEntity == null) {
            return null;
        }
        Map<String, Object> oldMap = JSONObject.parseObject(JSONObject.toJSONString(contractEntity), new TypeReference<Map<String, Object>>() {
        });
        //修改拨备转回金额depreciation_reserves_balance
        oldMap.put("depreciationReservesBalance", getDepreciationReservesBalance(contractCode, orgId));
        Map<String, Object> newMap = new HashMap<>();
        Iterator<Map.Entry<String, Object>> it = oldMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            newMap.put("contract_" + entry.getKey(), entry.getValue());
        }
        return newMap;
    }

    public BigDecimal getDepreciationReservesBalance(String contractCode, String orgId) {
        BigDecimal maxReserves = BigDecimal.ZERO;
        if (StringUtils.isEmpty(contractCode) || StringUtils.isEmpty(orgId)) {
            return maxReserves;
        }
        maxReserves = iVerificationService.getMaxDepreciationReserves(contractCode, orgId);
        return null == maxReserves ? BigDecimal.ZERO : maxReserves;
    }

    @Override
    public String importData(List<ImportContractExcel> list) {
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgId(), e -> e.getOrgId(), (a, b) -> b));
        List<String> codeList = list.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
        List<ContractEntity> contractEntities = this.getBaseMapper().selectList(Wrappers.<ContractEntity>lambdaQuery()
                .in(ContractEntity::getContractCode, codeList));
        Map<String, ContractEntity> codeMap = contractEntities.stream().collect(Collectors.toMap(e -> e.getContractCode(), e -> e, (a, b) -> b));
        List<ContractEntity> insertList = new ArrayList<>();
        List<ContractEntity> updateList = new ArrayList<>();
        list.forEach(e -> {
            ContractEntity contractEntity = codeMap.get(e.getContractCode());
            if (null != contractEntity) {
                BeanUtil.copyProperties(e, contractEntity, CopyOptions.create().setIgnoreNullValue(true));
                if (e.getAccountCode().equals("2701.09")) {
                    contractEntity.setBusinessCode("JYZL");
                    contractEntity.setBusinessName("经营租赁");
                } else {
                    contractEntity.setBusinessCode("ZLYW");
                    contractEntity.setBusinessName("租赁业务");
                }
                contractEntity.setOrgId(companyMap.get(contractEntity.getOrgId()));
                updateList.add(contractEntity);
            } else {
                contractEntity = BeanUtil.copyProperties(e, ContractEntity.class);
                if (e.getAccountCode().equals("2701.09")) {
                    contractEntity.setBusinessCode("JYZL");
                    contractEntity.setBusinessName("经营租赁");
                } else {
                    contractEntity.setBusinessCode("ZLYW");
                    contractEntity.setBusinessName("租赁业务");
                }
                contractEntity.setOrgId(companyMap.get(contractEntity.getOrgId()));
                insertList.add(contractEntity);
            }
        });
        this.saveBatch(insertList);
        this.updateBatchById(updateList);

        return null;
    }

    @Override
    public void importData1(List<ImportContractExcel1> list) {
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgName(), e -> e.getOrgId(), (a, b) -> b));
        List<String> codeList = list.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
        List<ContractEntity> contractEntities = this.getBaseMapper().selectList(Wrappers.<ContractEntity>lambdaQuery()
                .in(ContractEntity::getContractCode, codeList));
        Map<String, ContractEntity> contractEntityMap = contractEntities.stream().collect(Collectors.toMap(e -> e.getContractCode(), e -> e, (a, b) -> b));
        List<String> existCodeList = contractEntities.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
        List<ContractEntity> insertList = new ArrayList<>();
        list.forEach(e -> {
            ContractEntity contractEntity;
            if (existCodeList.contains(e.getContractCode())) {
                contractEntity = contractEntityMap.get(e.getContractCode());
                BeanUtil.copyProperties(e, contractEntity);
            } else {
                contractEntity = BeanUtil.copyProperties(e, ContractEntity.class);
            }
            contractEntity.setBusinessCode("ZLYW");
            contractEntity.setBusinessName("租赁业务");
            contractEntity.setOrgId(companyMap.get(contractEntity.getOrgId()));
            insertList.add(contractEntity);
        });
        this.saveOrUpdateBatch(insertList);
    }

    @Override
    public void importData2(List<ImportContractExcel> list) {
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgName(), e -> e.getOrgId(), (a, b) -> b));
        List<String> codeList = list.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
        List<ContractEntity> contractEntities = this.getBaseMapper().selectList(Wrappers.<ContractEntity>lambdaQuery()
                .in(ContractEntity::getContractCode, codeList));
        Map<String, ContractEntity> contractEntityMap = contractEntities.stream().collect(Collectors.toMap(e -> e.getContractCode(), e -> e, (a, b) -> b));
        List<String> existCodeList = contractEntities.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
        List<ContractEntity> insertList = new ArrayList<>();
        //财务合同状态
        R<List<SysDictData>> financialContractStatusR = remoteDictService.listDictData(DictTypeEnum.FINANCIAL_CONTRACT_STATUS.getCode());
        Map<String, String> financialContractStatusMap = financialContractStatusR.getData().stream().collect(Collectors.toMap(e -> e.getDictLabel(), e -> e.getDictValue()));
        list.forEach(e -> {
            ContractEntity contractEntity;
            if (existCodeList.contains(e.getContractCode())) {
                contractEntity = contractEntityMap.get(e.getContractCode());
                BeanUtil.copyProperties(e, contractEntity);
            } else {
                contractEntity = BeanUtil.copyProperties(e, ContractEntity.class);
            }
            contractEntity.setBusinessCode("ZLYW");
            contractEntity.setBusinessName("租赁业务");
            contractEntity.setFinancialContractStatus(financialContractStatusMap.get(contractEntity.getFinancialContractStatus()));
            contractEntity.setOrgId(companyMap.get(contractEntity.getOrgId()));
            insertList.add(contractEntity);
        });
        this.saveOrUpdateBatch(insertList);
    }

    @Override
    public ContractDetailDTO getContractDetail(Long id) {
        ContractEntity entity = this.getById(id);
        if (entity == null) {
            return null;
        }
        ContractDetailDTO detailDTO = BeanUtil.copyProperties(entity, ContractDetailDTO.class);
        List<BusinessDTO> businessDTOList = businessService.queryAll();
        Map<String, String> businessMap = businessDTOList.stream().collect(Collectors.toMap(e -> e.getBusinessCode(), e -> e.getBusinessName()));
        //业务类型名称
        detailDTO.setBusinessName(businessMap.get(detailDTO.getBusinessCode()));
        if (StringUtils.isNotEmpty(detailDTO.getIncomeProvisionMethod())) {
            detailDTO.setIncomeProvisionMethod(AccrualMethodEnum.getDescByCode(detailDTO.getIncomeProvisionMethod()));
        }
        //填充数仓字段
        DwBzHetjbxxDDTO dwBzHetjbxxDDTO = dwBzHetjbxxDService.getHtjbxxByCode(entity.getContractCode());
        if (dwBzHetjbxxDDTO != null) {
            Map<String, Map<String, String>> allDataMap = dwDictDataService.selectAllDictMap();
//            detailDTO.setDwCreateDept(dwBzHetjbxxDDTO.getVcHetcdbm());
            detailDTO.setDwContractSignDate(dwBzHetjbxxDDTO.getDtHetqyrq());
            detailDTO.setDwLeaseDeviceType(dwBzHetjbxxDDTO.getVcSheblx());
            detailDTO.setDwBusinessType(dwDictDataService.getDictValueByKey(allDataMap, DwDictTypeEnum.YEWZL.getCode(), dwBzHetjbxxDDTO.getVcYewzl()));
            detailDTO.setDwIndustryTag(dwDictDataService.getDictValueByKey(allDataMap, DwDictTypeEnum.HAITHYFLBQ.getCode(), dwBzHetjbxxDDTO.getVcHangyflbq()));
            detailDTO.setDwLargeSmallTag(dwDictDataService.getDictValueByKey(allDataMap, DwDictTypeEnum.HAITKHDXBQ.getCode(), dwBzHetjbxxDDTO.getVcKehdxbq()));
            detailDTO.setDwClientNature(dwDictDataService.getDictValueByKey(allDataMap, DwDictTypeEnum.HAITNBKHXZBQ.getCode(), dwBzHetjbxxDDTO.getVcNeibkhxz()));
            detailDTO.setDwDistrict(dwBzHetjbxxDDTO.getVcQuy());
//            detailDTO.setDwInterestRateAdjustType(dwDictDataService.getDictValueByKey(allDataMap, DwDictTypeEnum.TIAOXFS.getCode(), dwBzHetjbxxDDTO.getVcTiaoxfs()));
            detailDTO.setDwPlanTable(dwBzHetjbxxDDTO.getChShifncb());
            detailDTO.setDwHasTable(dwBzHetjbxxDDTO.getChShifcb());
            detailDTO.setProjectNumber(dwBzHetjbxxDDTO.getVcXiangmbh());
        }
        //计算数据结构数据
        calculateAmount(detailDTO);
        //服务费合同、服务费合同主体（当主合同编号=此页面合同编号时，分别取值合同表的合同编号、签约主体）
        boolean contractCodeMExist = this.lambdaQuery().eq(ContractEntity::getContractCodeM, entity.getContractCode()).exists();
        if (contractCodeMExist) {
            detailDTO.setContractCodeM(entity.getContractCode());
            if (StringUtils.isNotEmpty(entity.getOrgId())) {
                List<OrgCompanyEntity> companyEntityList = iOrgCompanyService.lambdaQuery().eq(OrgCompanyEntity::getOrgId, entity.getOrgId()).list();
                detailDTO.setMainOrgIdName(CollectionUtils.isEmpty(companyEntityList) ? "" : companyEntityList.get(0).getOrgName());
            }
        }
        return detailDTO;
    }

    @Override
    public IPage<ContractRepaymentPlanVO> selectPageByContractCode(ContractQueryInfoDTO queryDTO) {
        //根据Id查询合同编码
        ContractEntity contractEntity = this.getById(queryDTO.getId());
        if (ObjectUtils.isNull(contractEntity)) {
            throw new ServiceException("合同信息不存在");
        }
        queryDTO.setContractCode(contractEntity.getContractCode());
        return repaymentPlanHisService.selectPageByContractCode(queryDTO);
    }

    @Override
    public List<String> getLatestVersionDate(Long id) {
        //根据Id查询合同编码
        ContractEntity contractEntity = this.getById(id);
        if (ObjectUtils.isNull(contractEntity)) {
            throw new ServiceException("合同信息不存在");
        }
        return repaymentPlanHisService.getLatestVersionDate(contractEntity.getContractCode());
    }

    @Override
    public IPage<ContractTransactionVO> transactionByPage(ContractQueryInfoDTO queryDTO) {
        InterfaceDataQueryDTO params = BeanUtil.copyProperties(queryDTO, InterfaceDataQueryDTO.class);
        //根据Id查询合同编码
        ContractEntity contractEntity = this.getById(queryDTO.getId());
        if (ObjectUtils.isNull(contractEntity)) {
            throw new ServiceException("合同信息不存在");
        }
        params.setContractCode(contractEntity.getContractCode());
        params.setOrgId(contractEntity.getOrgId());
        return iInterfaceDataService.contractTransactionByPage(params);
    }

    @Override
    public IPage<ContractAccountBalanceVO> accountBalanceByPage(ContractQueryInfoDTO queryDTO) {
        ContractBalanceQueryDTO params = BeanUtil.copyProperties(queryDTO, ContractBalanceQueryDTO.class);
        //根据Id查询合同编码
        ContractEntity contractEntity = this.getById(queryDTO.getId());
        if (ObjectUtils.isNull(contractEntity)) {
            throw new ServiceException("合同信息不存在");
        }
        params.setContractCode(contractEntity.getContractCode());
        params.setOrgId(contractEntity.getOrgId());
        return iContractBalanceService.sumAccountBalancePage(params);
    }

    @Override
    public void verificationSaveRecordList(List<ContractVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<String> contractCodeList = list.stream().map(ContractVO::getContractCode).distinct().collect(Collectors.toList());
        List<String> orgIdList = list.stream().map(ContractVO::getOrgId).distinct().collect(Collectors.toList());
        List<ContractEntity> contractEntityList = this.lambdaQuery().in(ContractEntity::getContractCode, contractCodeList).in(ContractEntity::getOrgId, orgIdList).list();
        if (CollectionUtils.isEmpty(contractCodeList)) {
            return;
        }
        Map<String, ContractEntity> contractEntityMap = contractEntityList.stream().collect(Collectors.groupingBy(v -> v.getContractCode() + "-" + v.getOrgId(), Collectors.collectingAndThen(
                Collectors.toList(), s -> s.get(0))));
        list.forEach(v -> {
            if (contractEntityMap.containsKey(v.getContractCode() + "-" + v.getOrgId())) {
                ContractEntity entity = contractEntityMap.get(v.getContractCode() + "-" + v.getOrgId());
                v.setTransferOrgId(entity.getTransferOrgId());
                v.setTransferContractCode(entity.getTransferContractCode());
                v.setTransferContractStatus(entity.getTransferContractStatus());
                v.setContractStatus(entity.getContractStatus());
            }
            if (null == v.getFinancialContractStatusUpdateTime()) {
                v.setFinancialContractStatusUpdateTime(new Date());
            }
        });
        saveRecordList(list);
    }

    @Override
    public IPage<ContractTInfoPerformanceAttributionVO> salesBonusPage(ContractQueryInfoDTO queryDTO) {
        IPage<ContractTInfoPerformanceAttributionVO> page = new Page<>();
        ContractEntity contractEntity = this.getById(queryDTO.getId());
        if (ObjectUtils.isNull(contractEntity)) {
            throw new ServiceException("合同信息不存在");
        }
        TInfoPerformanceAttributionSyncQueryDTO params = new TInfoPerformanceAttributionSyncQueryDTO();
        params.setContractNo(contractEntity.getContractCode());
        params.setPageNum(queryDTO.getPageNum());
        params.setPageSize(queryDTO.getPageSize());
        IPage<TInfoPerformanceAttributionSyncVO> pageResult = itInfoPerformanceAttributionSyncService.selectPage(params);
        BeanUtil.copyProperties(pageResult, page);
        List<ContractTInfoPerformanceAttributionVO> attributionVOList = Lists.newArrayList();
        pageResult.getRecords().stream().forEach(v -> {
            ContractTInfoPerformanceAttributionVO vo = new ContractTInfoPerformanceAttributionVO();
            vo.setOrgId(contractEntity.getOrgId());
            vo.setDeptName(v.getSaleVolumeBelongDepartName());
            vo.setStaffName(v.getProjectManagerName());
            vo.setSalesPercentage(v.getSaleProportion());
            //金额 = 合同金额*销售占比
            BigDecimal salesPercentage = null == vo.getSalesPercentage() ? BigDecimal.ZERO : vo.getSalesPercentage();
            BigDecimal amount = (null == contractEntity.getContractAmount() ? BigDecimal.ZERO : contractEntity.getContractAmount()).multiply(salesPercentage.divide(new BigDecimal(100)));
            vo.setAmount(amount);
            attributionVOList.add(vo);
        });
        page.setRecords(attributionVOList);
        return page;
    }

    @Override
    public List<ContractRepaymentPlanVO> selectByContractCode(ContractQueryInfoDTO queryDTO) {
        //根据Id查询合同编码
        ContractEntity contractEntity = this.getById(queryDTO.getId());
        if (ObjectUtils.isNull(contractEntity)) {
            throw new ServiceException("合同信息不存在");
        }
        queryDTO.setContractCode(contractEntity.getContractCode());
        return repaymentPlanHisService.selectByContractCode(queryDTO);
    }

    @Override
    public IPage<ContractVO> allContractPage(ContractQueryDTO queryDTO) {
        Page<ContractVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        return contractMapper.selectAllContractPage(page, queryDTO);
    }

    @Override
    public List<ContractVO> allContractList(ContractQueryDTO queryDTO) {
        return contractMapper.selectAllContract(queryDTO);
    }

    public void calculateAmount(ContractDetailDTO detailDTO) {
        //租赁合同总计 = 租金首付款+应收手续费收入+应收租金总额？
        BigDecimal rentReceivedAmount = BigDecimal.ZERO;// TODO: 29/12/2023  待确定
        BigDecimal rentContractTotal = detailDTO.getRentContractTotal() == null ? BigDecimal.ZERO : detailDTO.getRentContractTotal();
        // 渠道费用 (=应付渠道费用+应付海通渠道费用)
        BigDecimal payableChannelExpense = null == detailDTO.getPayableChannelExpense() ? BigDecimal.ZERO : detailDTO.getPayableChannelExpense();
        BigDecimal payableInnerExpense = null == detailDTO.getPayableInnerExpense() ? BigDecimal.ZERO : detailDTO.getPayableInnerExpense();
        detailDTO.setChannelFees(payableChannelExpense.add(payableInnerExpense));
        // 出租人其它成本=(应付其他+GPS预估费用+应付介绍费+应付法律费)
        // 手环成本 private BigDecimal payableBraceletCost;
        BigDecimal lessorOtherCost = (null == detailDTO.getPayableOtherAmount() ? BigDecimal.ZERO : detailDTO.getPayableOtherAmount())
                .add(null == detailDTO.getEstimateGPSExpense() ? BigDecimal.ZERO : detailDTO.getEstimateGPSExpense())
                .add(null == detailDTO.getPayableIntroduce() ? BigDecimal.ZERO : detailDTO.getPayableIntroduce())
                .add(null == detailDTO.getPayableLawAmount() ? BigDecimal.ZERO : detailDTO.getPayableLawAmount());
        detailDTO.setLessorOtherCosts(lessorOtherCost);
        //租金概算本金 = 设备价格-租金首付款
        BigDecimal rentPrincipal = (null == detailDTO.getPayableDeviceAmount() ? BigDecimal.ZERO : detailDTO.getPayableDeviceAmount())
                .subtract(null == detailDTO.getReceivableFirstAmount() ? BigDecimal.ZERO : detailDTO.getReceivableFirstAmount());
        //租赁合同收入总计 = 租赁合同总计+承租人履约保证金+厂商返利（含增值税）+名义留购价+期末残值+供应商履约保证金+承租人保险费+其他收入（含增值税）
        BigDecimal rentContractIncomesTotal = rentContractTotal.add(null == detailDTO.getReceivableMarginAmount() ? BigDecimal.ZERO : detailDTO.getReceivableMarginAmount())
                .add(null == detailDTO.getReceivableFirmRebate() ? BigDecimal.ZERO : detailDTO.getReceivableFirmRebate())
                .add(null == detailDTO.getRetainedPrice() ? BigDecimal.ZERO : detailDTO.getRetainedPrice())
                .add(null == detailDTO.getLastCost() ? BigDecimal.ZERO : detailDTO.getLastCost())
                .add(null == detailDTO.getVendorMarginAmount() ? BigDecimal.ZERO : detailDTO.getVendorMarginAmount())
                .add(null == detailDTO.getReceivableInsuranceAmount() ? BigDecimal.ZERO : detailDTO.getReceivableInsuranceAmount())
                .add(null == detailDTO.getReceivableOther() ? BigDecimal.ZERO : detailDTO.getReceivableOther());
        // 净融资额=设备价格+出租人保险费+渠道费用+出租人其他成本-（租金首付款+承租人履约保证金+手续费收入（含增值税）+厂商返利（含增值税）+供应商履约保证金+其他收入+咨询服务收入+起租前已收租金？）
        BigDecimal financingAmount = ((null == detailDTO.getPayableDeviceAmount() ? BigDecimal.ZERO : detailDTO.getPayableDeviceAmount())
                .add(null == detailDTO.getLessorInsuranceAmount() ? BigDecimal.ZERO : detailDTO.getLessorInsuranceAmount())
                .add(null == detailDTO.getChannelFees() ? BigDecimal.ZERO : detailDTO.getChannelFees())
                .add(null == detailDTO.getLessorOtherCosts() ? BigDecimal.ZERO : detailDTO.getLessorOtherCosts()))
                .subtract((null == detailDTO.getReceivableFirstAmount() ? BigDecimal.ZERO : detailDTO.getReceivableFirstAmount()
                        .add(null == detailDTO.getReceivableFirmRebate() ? BigDecimal.ZERO : detailDTO.getReceivableFirmRebate())
                        .add(null == detailDTO.getReceivableProcedureAmount() ? BigDecimal.ZERO : detailDTO.getReceivableProcedureAmount())
                        .add(null == detailDTO.getReceivableMarginAmount() ? BigDecimal.ZERO : detailDTO.getReceivableMarginAmount())
                        .add(null == detailDTO.getVendorMarginAmount() ? BigDecimal.ZERO : detailDTO.getVendorMarginAmount())
                        .add(null == detailDTO.getReceivableOther() ? BigDecimal.ZERO : detailDTO.getReceivableOther())
                        .add(null == detailDTO.getReceivableServiceAmount() ? BigDecimal.ZERO : detailDTO.getReceivableServiceAmount())
                        .add(rentReceivedAmount)));
        // 租赁销售额=设备价格+出租人保险费+渠道费用+出租人其他成本-（租金首付款+承租人履约保证金+手续费收入（含增值税）+厂商返利（含增值税）+其他收入+咨询服务收入）
        //租赁销售额【租赁销售额】=【设备价格payable_device_amount】+【出租人保险费lessor_insurance_amount】+【渠道费用channel_fees】+【出租人其他成本lessor_other_costs】
        // -【租金首付款receivable_first_amount】-【手续费收入（含增值税receivable_procedure_amount）】-【名义留购价retained_price】
        // -【其他收入（含增值税）receivable_others】-【咨询服务收入（含增值税receivable_service_amount）】
        BigDecimal rentSalesAmount = (null == detailDTO.getPayableDeviceAmount() ? BigDecimal.ZERO : detailDTO.getPayableDeviceAmount())
                .add(null == detailDTO.getLessorInsuranceAmount() ? BigDecimal.ZERO : detailDTO.getLessorInsuranceAmount())
                .add(detailDTO.getChannelFees())
                .add(null == detailDTO.getLessorOtherCosts() ? BigDecimal.ZERO : detailDTO.getLessorOtherCosts())
                .subtract(null == detailDTO.getReceivableFirstAmount() ? BigDecimal.ZERO : detailDTO.getReceivableFirstAmount())
                .subtract(null == detailDTO.getReceivableProcedureAmount() ? BigDecimal.ZERO : detailDTO.getReceivableProcedureAmount())
                .subtract(null == detailDTO.getRetainedPrice() ? BigDecimal.ZERO : detailDTO.getRetainedPrice())
                .subtract(null == detailDTO.getReceivableOther() ? BigDecimal.ZERO : detailDTO.getReceivableOther())
                .subtract(null == detailDTO.getReceivableServiceAmount() ? BigDecimal.ZERO : detailDTO.getReceivableServiceAmount());
        detailDTO.setRentPrincipal(rentPrincipal);
        detailDTO.setRentContractIncomesTotal(rentContractIncomesTotal);
        detailDTO.setRentSalesAmount(rentSalesAmount);
        detailDTO.setFinancingAmount(financingAmount);
        //租金概算本金【设备价格payable_device_amount】-【租金首付款receivable_first_amount】
        BigDecimal rentEstimatePrincipal = (null == detailDTO.getPayableDeviceAmount() ? BigDecimal.ZERO : detailDTO.getPayableDeviceAmount())
                .subtract((null == detailDTO.getReceivableFirstAmount() ? BigDecimal.ZERO : detailDTO.getReceivableFirstAmount()));
        detailDTO.setRentEstimatePrincipal(rentEstimatePrincipal);
    }

    @Override
    public List<ContractExcelVO> export(ContractQueryDTO queryDTO) {
        List<ContractVO> contractVOList = this.selectAllContractCode(queryDTO);
        return BeanUtil.copyToList(contractVOList, ContractExcelVO.class);
    }

    /**
     * 更新财务合同状态
     *
     * @param contractCode
     * @param orgId
     * @param financialContractStatus
     */
    @Override
    public void updateContractFinancialStatus(String contractCode, String orgId, String financialContractStatus) {
        ContractDTO contractDTO = getContractDTOByCode(contractCode, orgId);
        if (ObjectUtils.isEmpty(contractDTO)) {
            throw new ServiceException("根据合同编号[" + contractCode + "]+签约主体[" + orgId + "]未查询到合同信息");
        }
        this.update(new LambdaUpdateWrapper<ContractEntity>()
                .set(ContractEntity::getFinancialContractStatus, financialContractStatus)
                .set(ContractEntity::getFinancialContractStatusUpdateTime, LocalDateTime.now())
                .eq(ContractEntity::getId, contractDTO.getId())
        );
    }

    /**
     * 生成新的合同
     *
     * @param contractCode    原合同号
     * @param orgId           原合同签约主体
     * @param newContractCode 新合同号
     * @param newOrgId        新合同签约主体
     */
    @Override
    public void copyContract(String contractCode, String orgId, String newContractCode, String newOrgId) {
        ContractDTO contractDTO = getContractDTOByCode(contractCode, orgId);
        if (ObjectUtils.isEmpty(contractDTO)) {
            throw new ServiceException("根据合同编号[" + contractCode + "]+签约主体[" + orgId + "]未查询到合同信息");
        }
        ContractDTO newContractDTO = getContractDTOByCode(newContractCode, newOrgId);
        if (ObjectUtils.isEmpty(newContractDTO)) {
            // 新合同不存在，
            newContractDTO = contractDTO;
            newContractDTO.setId(null);
            newContractDTO.setCreateBy(null);
            newContractDTO.setCreateTime(null);
            // 新合同状态 为正常起租
            newContractDTO.setContractStatus(BusinessContractStatusEnum.CONTRACT_STATUS_1.getCode());
            newContractDTO.setFinancialContractStatus(null);
            newContractDTO.setFinancialContractStatusUpdateTime(null);
            newContractDTO.setContractCode(newContractCode);
            newContractDTO.setOrgId(newOrgId);
        } else {
            // 存在则更新
        }
        newContractDTO.setUpdateBy(null);
        newContractDTO.setUpdateTime(null);

        ContractEntity newContractEntity = BeanUtil.copyProperties(newContractDTO, ContractEntity.class);
        // 保存或更新新合同
        saveOrUpdate(newContractEntity);

        // 更新原表转入公司
        ContractEntity olDContractEntity = BeanUtil.copyProperties(contractDTO, ContractEntity.class);
        olDContractEntity.setTransferOrgId(contractDTO.getOrgId());
        olDContractEntity.setTransferContractStatus(contractDTO.getContractStatus());
        olDContractEntity.setTransferContractCode(contractDTO.getContractCode());
        olDContractEntity.setUpdateTime(LocalDateTime.now());
        updateById(olDContractEntity);

        //写入合同状态记录
        List<ContractEntity> contractEntityList = Lists.newArrayList();
        contractEntityList.add(olDContractEntity);
        contractEntityList.add(newContractEntity);
        saveRecordList(BeanUtil.copyToList(contractEntityList, ContractVO.class));
    }

    /**
     * 保存特殊合同状态记录
     *
     * @param contractEntityList
     */
    @Override
    public void saveRecordList(List<ContractVO> contractEntityList) {
        List<String> contractCodeList = contractEntityList.stream().map(ContractVO::getContractCode).distinct().collect(Collectors.toList());
        List<String> orgIdList = contractEntityList.stream().map(ContractVO::getOrgId).distinct().collect(Collectors.toList());
        List<ContractStatusRecordEntity> recordEntityList = contractStatusRecordMapper.selectList(new LambdaQueryWrapper<ContractStatusRecordEntity>()
                .in(ContractStatusRecordEntity::getContractCode, contractCodeList)
                .in(ContractStatusRecordEntity::getOrgId, orgIdList));
        for (ContractVO contractEntity : contractEntityList) {
            // 判断是否已经写入过此状态
            List<ContractStatusRecordEntity> collect = recordEntityList.stream().filter(a -> ObjectUtil.equals(a.getContractCode(), contractEntity.getContractCode())
                    && ObjectUtil.equals(a.getOrgId(), contractEntity.getOrgId())
                    && ObjectUtil.equals(a.getFinancialContractStatus(), contractEntity.getFinancialContractStatus())
                    && ObjectUtil.equals(a.getFinancialContractStatusUpdateTime(), contractEntity.getFinancialContractStatusUpdateTime())
            ).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                //存在相同的数据，不写入
                continue;
            }
            ContractStatusRecordEntity entity = new ContractStatusRecordEntity();
            entity.setContractCode(contractEntity.getContractCode());
            entity.setContractName(contractEntity.getContractName());
            entity.setClientCode(contractEntity.getClientCode());
            entity.setClientName(contractEntity.getClientName());
            entity.setOrgId(contractEntity.getOrgId());
            entity.setContractStatus(contractEntity.getContractStatus());
            entity.setFinancialContractStatusUpdateTime(contractEntity.getFinancialContractStatusUpdateTime());
            entity.setFinancialContractStatus(contractEntity.getFinancialContractStatus());
            entity.setTransferOrgId(contractEntity.getTransferOrgId());
            entity.setTransferContractCode(contractEntity.getTransferContractCode());
            entity.setTransferContractStatus(contractEntity.getTransferContractStatus());
            entity.setOperator(SecurityUtils.getUserId() + "");
            // 状态直接改为 已复核
            entity.setRecordStatus(ProcessStatusEnum.REVIEWED.getCode());
            entity.setSourceFromId(contractEntity.getSourceFromId());
            entity.setSourceFromType(contractEntity.getSourceFromType());
            // 保存合同状态记录数据
            contractStatusRecordMapper.insert(entity);
        }
    }

    /**
     * 取得待分摊服务费的合同信息
     */
    public List<ContractEntity> selectContractForReceviceServiceAmount(List<String> notGenerateOrgIds) {
        LambdaQueryWrapper<ContractEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContractEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        wrapper.ne(ContractEntity::getReceivableServiceAmount, 0);
        wrapper.isNotNull(ContractEntity::getReceivableServiceAmount);
        if (notGenerateOrgIds != null && !notGenerateOrgIds.isEmpty()) {
            wrapper.notIn(ContractEntity::getOrgId, notGenerateOrgIds);
        }
        return contractMapper.selectList(wrapper);
    }

    /**
     * 合同基本信息同步
     */
    public void basicDataSync(String leaseDateStart) {
        log.info("-------------合同基本信息同步任务 Start-------------");
        LambdaQueryWrapper<ContractEntity> contractWrapper = new LambdaQueryWrapper<>();
        contractWrapper.eq(ContractEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        contractWrapper.ge(ContractEntity::getLeaseDateStart, DateUtil.beginOfDay(DateUtil.parse(leaseDateStart)).toJdkDate());
        Long contractCount = contractMapper.selectCount(contractWrapper);
        if (contractCount == null || contractCount == 0L) {
            return;
        }
        log.info("待同步合同数量：".concat(String.valueOf(contractCount)));

        // 取得线程的数量
        int threadNumber = this.getThreadNumber(contractCount);

        // 计算每个线程平均处理多少数据量
        int eachThreadNumber = ((int) contractCount.longValue()) / threadNumber;
        int mod = ((int) contractCount.longValue()) % threadNumber;

        for (int i = 0; i < threadNumber; i++) {
            int startIndex = i * eachThreadNumber;
            int endIndex = i * eachThreadNumber + eachThreadNumber;
            if (i == eachThreadNumber - 1) {
                endIndex = endIndex + mod;
            }
            contractAsyncService.contractInfoAsync(startIndex, endIndex, leaseDateStart);
        }
    }

    @Override
    public String getClientCode(String contractCode, String clientName) {
        return contractMapper.getClientCode(contractCode, clientName);
    }

    @Override
    public void updateContractABasicDataSync() {
        //获取带-A的合同数据
        List<ContractEntity> entityList = this.lambdaQuery().like(ContractEntity::getContractCode, "-A").list();
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }
        int i = 1;
        double totalSize = entityList.size();
        for (ContractEntity contractEntity : entityList) {
            String contractCode = contractEntity.getContractCode();
            //原始合同
            String orginalContractCode = contractCode.substring(0, contractCode.indexOf("-"));
            List<ContractEntity> orginalContractList = this.lambdaQuery().eq(ContractEntity::getContractCode, orginalContractCode).list();
            if (CollectionUtils.isEmpty(orginalContractList)) {
                log.info("合同：{}，不存在原始合同，跳过", contractCode);
                log.info("处理合同：{},完成，当前进度：{}", contractCode, NumberUtil.formatPercent(i / totalSize, 2));
                i++;
                continue;
            }
            ContractEntity orginalContract = orginalContractList.get(0);
            //id,contractCode,contractStatus,createBy,createTime,updateBy,updateTime,financialContractStatusUpdateTime,financialContractStatus
            Long id = contractEntity.getId();
            String contract = contractEntity.getContractCode();
            String contractStatus = contractEntity.getContractStatus();
            String createBy = contractEntity.getCreateBy();
            LocalDateTime createTime = contractEntity.getCreateTime();
            String updateBy = contractEntity.getUpdateBy();
            LocalDateTime updateTime = contractEntity.getUpdateTime();
            Date financialTime = contractEntity.getFinancialContractStatusUpdateTime();
            String financialContractStatus = contractEntity.getFinancialContractStatus();
            String clientType = contractEntity.getClientType();
            //id、合同、签约主体、合同状态、创建人、创建时间、更新人、更新时间、财务合同状态、财务合同状态
            BeanUtils.copyProperties(orginalContract, contractEntity);
            contractEntity.setId(id);
            contractEntity.setContractCode(contract);
            contractEntity.setContractStatus(contractStatus);
            contractEntity.setCreateBy(createBy);
            contractEntity.setCreateTime(createTime);
            contractEntity.setUpdateBy(updateBy);
            contractEntity.setUpdateTime(updateTime);
            contractEntity.setFinancialContractStatusUpdateTime(financialTime);
            contractEntity.setFinancialContractStatus(financialContractStatus);
            // 如果客户类型是承租人，则不更新客户类型
            if (DefaultConstant.CLIENT_TYPE_LESSEE.equals(clientType)) {
                contractEntity.setClientType(clientType);
            }
            log.info("合同信息：{}", JSONObject.toJSONString(contractEntity));
            log.info("处理合同：{},完成，当前进度：{}", contractCode, NumberUtil.formatPercent(i / totalSize, 2));
            i++;
        }
        this.updateBatchById(entityList);
    }

    @Override
    public void syncLossData(String startData) {
        List<InterfaceDataEntity> list = iInterfaceDataService.lambdaQuery().gt(InterfaceDataEntity::getCreateTime, startData).orderByAsc(InterfaceDataEntity::getCreateTime).list();
        for (InterfaceDataEntity interfaceDataEntity : list) {
            contractNewTransactionService.saveOrUpdateContract(interfaceDataEntity.getInterfaceData());
        }
        log.info("syncLossData last id:{}", list.get(list.size() - 1).getId());
    }

    @Override
    public void updateServiceShareFlagByContractCode(List<ContractEntity> contractEntities) {
        //根据合同号批量更新
        if (CollectionUtils.isEmpty(contractEntities)) {
            return;
        }
        for (ContractEntity contractEntity : contractEntities) {
            this.lambdaUpdate().set(ContractEntity::getSharingServiceFeeFlag, contractEntity.getSharingServiceFeeFlag())
                    .set(ContractEntity::getEndSharingServiceFeeFlag, contractEntity.getEndSharingServiceFeeFlag())
                    //变更为合同结束时才更新
                    .set(contractEntity.getEndSharingServiceFeeFlag() != null && contractEntity.getEndSharingServiceFeeFlag(), ContractEntity::getSetEndSharingServiceFeePeriod, contractEntity.getSetEndSharingServiceFeePeriod())
                    .set(!(contractEntity.getEndSharingServiceFeeFlag() != null && contractEntity.getEndSharingServiceFeeFlag()), ContractEntity::getSetEndSharingServiceFeePeriod, 0)
                    .eq(ContractEntity::getContractCode, contractEntity.getContractCode()).update();
            contractMonthService.lambdaUpdate().set(ContractMonthEntity::getSharingServiceFeeFlag, contractEntity.getSharingServiceFeeFlag())
                    .set(ContractMonthEntity::getEndSharingServiceFeeFlag, contractEntity.getEndSharingServiceFeeFlag())
                    //变更为合同结束时才更新
                    .set(contractEntity.getEndSharingServiceFeeFlag() != null && contractEntity.getEndSharingServiceFeeFlag(), ContractMonthEntity::getSetEndSharingServiceFeePeriod, contractEntity.getSetEndSharingServiceFeePeriod())
                    .set(!(contractEntity.getEndSharingServiceFeeFlag() != null && contractEntity.getEndSharingServiceFeeFlag()), ContractMonthEntity::getSetEndSharingServiceFeePeriod, 0)
                    .in(ContractMonthEntity::getContractCode, contractEntity.getContractCode()).update();
        }
    }

    @Override
    public void updateContractById(ContractEntityUpdateVo contractEntityUpdateVo) {
        contractMapper.updateContractById(contractEntityUpdateVo);
    }

    @Override
    public void updateByContractCode(ContractEntity contractEntity) {
        this.update(contractEntity, new LambdaUpdateWrapper<ContractEntity>()
                .eq(ContractEntity::getContractCode, contractEntity.getContractCode()));
    }

    @Override
    public void updateByContractCodeM(ContractEntity contractEntity) {
        if (StringUtils.isNotEmpty(contractEntity.getContractCodeM()))
            this.update(contractEntity, new LambdaUpdateWrapper<ContractEntity>()
                    .eq(ContractEntity::getContractCodeM, contractEntity.getContractCodeM()));
    }

    @Override
    public void updateBatchByContractCode(List<ContractEntity> updateContractEntities) {
        // 使用 BATCH 执行器类型
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ContractMapper contractMapper = sqlSession.getMapper(ContractMapper.class);

            for (ContractEntity contract : updateContractEntities) {
                contractMapper.updateBatchByContractCode(contract);
            }
            // 提交事务
            sqlSession.commit();
        }
    }

    @Override
    public List<ContractEntity> listClientInfoByCodeList(List<String> contractCodes) {
        return getBaseMapper().listClientInfoByCodeList(contractCodes);
    }

    /**
     * 根据待处理的数据量计算线程的数据量
     */
    public int getThreadNumber(Long number) {
        if (number < 5000) {
            return 1;
        } else if (number >= 5000 && number < 80000) {
            return 5;
        } else if (number >= 80000 && number < 250000) {
            return 10;
        } else {
            return 20;
        }
    }
}

