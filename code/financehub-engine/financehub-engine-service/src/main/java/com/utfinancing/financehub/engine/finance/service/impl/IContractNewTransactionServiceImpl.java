package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.core.constant.GenConstants;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.constant.DefaultConstant;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractHisEntity;
import com.utfinancing.financehub.engine.finance.mapper.ContractHisMapper;
import com.utfinancing.financehub.engine.finance.mapper.ContractMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ContractChangeSaveDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractHisDTO;
import com.utfinancing.financehub.engine.finance.service.IContractNewTransactionService;
import com.utfinancing.financehub.engine.finance.service.IVehicleBusinessModelService;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class IContractNewTransactionServiceImpl extends ServiceImpl<ContractMapper, ContractEntity> implements IContractNewTransactionService {


    @Resource
    private ContractHisMapper contractHisMapper;

    @Resource
    private IVehicleBusinessModelService vehicleBusinessModelService;

    /**
     * 批量更新合同信息-根据ID
     */
    public void batchUpdateById(List<ContractEntity> contractEntityList) {
        this.updateBatchById(contractEntityList);
    }

    @Override
    public String saveOrUpdateContract(Map<String, Object> interfaceDataMap) {
        String contractCode = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_CONTRACT_CODE);
        String contractCodeM = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_CONTRACT_CODE_M);
        String orgId = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_ORG_ID);
        String isSubmitFlag =  MapUtil.getStr(interfaceDataMap, RuleConstant.IS_SUBMIT);
        String sceneCode = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_SCENE_CODE);
        String financialContractStatus = MapUtil.getStr(interfaceDataMap, RuleConstant.FINANCIAL_CONTRACT_STATUS);
        String clientType = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_CLIENT_TYPE);
        String clientAttribute = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_CLIENT_ATTRIBUTE);
        String contractLeaseType = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_CONTRACT_LEASE_TYPE);
        if (StrUtil.isBlank(contractCode)) {
            return null;
        }
        QueryWrapper<ContractEntity> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(ContractEntity::getContractCode,contractCode);
        if (StringUtils.isNotEmpty(orgId)) {
            queryWrapper.lambda().eq(ContractEntity::getOrgId,orgId);
        }
        queryWrapper.lambda().orderByDesc(ContractEntity::getId);
        ContractEntity entity = null;
        List<ContractEntity> contractEntityList = this.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(contractEntityList)) {
            entity = contractEntityList.get(0);
        }
        if (entity != null) {
            Boolean updateLeaseTypeFlag = StringUtils.isNotEmpty(entity.getLeaseType());
            String systemCode = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_SYSTEM_CODE);
            String oldClientType = entity.getClientType();

            if (YesOrNoEnum.NO.getCode().equals(isSubmitFlag) || FinancialContractStatusEnum.THREE.getDesc().equals(financialContractStatus)) {
                BeanUtil.copyProperties(interfaceDataMap, entity, "id","financialContractStatus", "financialContractStatusUpdateTime","accountDate","estimateGPSExpense","payableInsuranceAmount");
                if (StringUtils.isNotEmpty(oldClientType) && DefaultConstant.CLIENT_TYPE_LESSEE.equals(oldClientType)) {
                    entity.setClientType(oldClientType);
                }
            } else {

                if (StringUtils.isNotEmpty(clientType) && DefaultConstant.CLIENT_TYPE_LESSEE.equals(clientType)) {
                    BeanUtil.copyProperties(interfaceDataMap, entity,"id","accountDate","estimateGPSExpense","payableInsuranceAmount");
                } else {
                    if (StringUtils.isNotEmpty(oldClientType) && DefaultConstant.CLIENT_TYPE_LESSEE.equals(oldClientType)) {
                        BeanUtil.copyProperties(interfaceDataMap, entity,"id",RuleConstant.FIELD_CLIENT_CODE,
                                RuleConstant.FIELD_CLIENT_NAME, RuleConstant.FIELD_CLIENT_TYPE,"accountDate","estimateGPSExpense","payableInsuranceAmount");
                    } else {
                        BeanUtil.copyProperties(interfaceDataMap, entity,"id",RuleConstant.FIELD_CLIENT_CODE,
                                RuleConstant.FIELD_CLIENT_NAME,"accountDate","estimateGPSExpense","payableInsuranceAmount");
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
                    if(StringUtils.isNotEmpty(contractLeaseType)){
                        entity.setTaxRate(new BigDecimal("0.06"));
                    }
                }
            }
            // 非中台 交易结构变更
            if (!SystemEnum.CWZT.getCode().equals(systemCode) && SceneEnum.JYJGBG.getCode().equals(sceneCode)) {
                ContractChangeSaveDTO contractChangeSaveDTO = BeanUtil.copyProperties(interfaceDataMap, ContractChangeSaveDTO.class);
                entity.setPayableDeviceAmount(NumberUtil.add(contractChangeSaveDTO.getPayableDeviceAdjustAmount(),entity.getPayableDeviceAmount()));
                entity.setReceivableFirstAmount(NumberUtil.add(contractChangeSaveDTO.getFirstAdjustAmount(),entity.getReceivableFirstAmount()));
                entity.setReceivableMarginAmount(NumberUtil.add(contractChangeSaveDTO.getImplementMarginAdjustAmount(),entity.getReceivableMarginAmount()));
                entity.setPayableChannelExpense(NumberUtil.add(contractChangeSaveDTO.getChannelAdjustExpense(),entity.getPayableChannelExpense()));
                entity.setPayableInnerExpense(NumberUtil.add(contractChangeSaveDTO.getInnerAdjustExpense(),entity.getPayableInnerExpense()));
                entity.setReceivableProcedureAmount(NumberUtil.add(contractChangeSaveDTO.getProcedureAdjustRevenues(),entity.getReceivableProcedureAmount()));
                entity.setReceivableFirmRebate(NumberUtil.add(contractChangeSaveDTO.getFirmAdjustRebate(),entity.getReceivableFirmRebate()));
                entity.setReceivableInsuranceAmount(NumberUtil.add(contractChangeSaveDTO.getInsuranceAdjustAmount(),entity.getReceivableInsuranceAmount()));
                entity.setRetainedPrice(NumberUtil.add(contractChangeSaveDTO.getResidualAdjustAmount(),entity.getRetainedPrice()));
                entity.setReceivableOther(NumberUtil.add(contractChangeSaveDTO.getOtherAdjustRevenues(),entity.getReceivableOther()));
                entity.setReceivableServiceAmount(NumberUtil.add(contractChangeSaveDTO.getServiceAdjustAmount(),entity.getReceivableServiceAmount()));
                entity.setVendorMarginAmount(NumberUtil.add(contractChangeSaveDTO.getMarginAdjustAmount(),entity.getVendorMarginAmount()));
                entity.setPayableOtherAmount(NumberUtil.add(contractChangeSaveDTO.getOtherCostAdjustAmount(),entity.getPayableOtherAmount()));
                entity.setPayableBraceletCost(NumberUtil.add(contractChangeSaveDTO.getPayableBraceletAdjustAmount(),entity.getPayableBraceletCost()));

                entity.setChannelFees(NumberUtil.add(entity.getPayableChannelExpense(),entity.getPayableInnerExpense()));
                entity.setLessorOtherCosts(NumberUtil.add(entity.getPayableOtherAmount(),entity.getEstimateGPSExpense(),entity.getPayableIntroduce(),entity.getPayableLawAmount(),entity.getPayableBraceletCost()));
            }

            ContractChangeSaveDTO tmpDto = BeanUtil.copyProperties(interfaceDataMap, ContractChangeSaveDTO.class);

            if((SystemEnum.XWXT.getCode().equals(systemCode)||SystemEnum.TYPT.getCode().equals(systemCode)) && SceneEnum.JYJGBG.getCode().equals(sceneCode)){
                entity.setReceivableRent(NumberUtil.add(entity.getReceivableRent(), tmpDto.getReceivablePrincipalAdjustAmount(), tmpDto.getReceivableInterestAdjustAmount(), tmpDto.getDeratePrincipalAmount().multiply(new BigDecimal(-1)), tmpDto.getDerateInterestAmount().multiply(new BigDecimal(-1))));
                entity.setPayableDeviceAmount(NumberUtil.add(entity.getPayableDeviceAmount(), tmpDto.getReceivablePrincipalAdjustAmount()));
            }else if(!SystemEnum.XWXT.getCode().equals(systemCode)&&!SystemEnum.TYPT.getCode().equals(systemCode) && SceneEnum.JYJGBG.getCode().equals(sceneCode)){
                entity.setReceivableRent(NumberUtil.add(entity.getReceivableRent(), tmpDto.getReceivableLeaseAdjustAmount(), tmpDto.getDeratePrincipalAmount().multiply(new BigDecimal(-1)), tmpDto.getDerateInterestAmount().multiply(new BigDecimal(-1))));
                entity.setPayableDeviceAmount(NumberUtil.add(entity.getPayableDeviceAmount(), tmpDto.getPayableDeviceAdjustAmount()));
            }
            //更新时不处理租赁类型字段
            if(updateLeaseTypeFlag){
                entity.setLeaseType(null);
            }


            entity.updateById();
        } else {
            if (YesOrNoEnum.NO.getCode().equals(isSubmitFlag) || FinancialContractStatusEnum.THREE.getDesc().equals(financialContractStatus)) {
                entity = BeanUtil.copyProperties(interfaceDataMap, ContractEntity.class,"id", "financialContractStatus", "financialContractStatusUpdateTime","accountDate","estimateGPSExpense","payableInsuranceAmount");
            } else {
                entity = BeanUtil.copyProperties(interfaceDataMap, ContractEntity.class,"id","accountDate","estimateGPSExpense","payableInsuranceAmount");
            }

            ContractChangeSaveDTO tmpDto = BeanUtil.copyProperties(interfaceDataMap, ContractChangeSaveDTO.class,"id","accountDate");

            if(SceneEnum.HTQZ.getCode().equals(sceneCode)){
                entity.setReceivableRent(NumberUtil.add(entity.getReceivableRent(), tmpDto.getReceivableLeaseAmount()));
                entity.setPayableDeviceAmount(NumberUtil.add(entity.getPayableDeviceAmount(), tmpDto.getPayableDeviceAmount()));
                String payableInsuranceAmountStr = MapUtil.getStr(interfaceDataMap, "payableInsuranceAmount");
                BigDecimal payableInsuranceAmount = NumberUtil.isNumber(payableInsuranceAmountStr)
                        ? new BigDecimal(payableInsuranceAmountStr)
                        : BigDecimal.ZERO;
                if (payableInsuranceAmount.compareTo(BigDecimal.ZERO) != 0) {
                    entity.setPayableInsuranceAmount(payableInsuranceAmount);
                }
                String estimaeGPSExpenseStr = MapUtil.getStr(interfaceDataMap, "estimateGPSExpense");
                if (StringUtils.isNotEmpty(estimaeGPSExpenseStr)){
                    entity.setEstimateGPSExpense(new BigDecimal(estimaeGPSExpenseStr));
                }
            }
            //校验租赁收款场景合同编码是否存在，存在则不新增否则新增
            boolean isExists = Boolean.FALSE;
            if (sceneCode.equals(SceneEnum.ZLSK.getCode())) {
                isExists = this.lambdaQuery().eq(ContractEntity::getContractCode,contractCode).exists();
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
        if (SceneEnum.HTQZ.getCode().equals(sceneCode)) {
            vehicleBusinessModelService.saveOrUpdateFromLeaseStart(interfaceDataMap);
        }
        return contractCode;
    }
}
