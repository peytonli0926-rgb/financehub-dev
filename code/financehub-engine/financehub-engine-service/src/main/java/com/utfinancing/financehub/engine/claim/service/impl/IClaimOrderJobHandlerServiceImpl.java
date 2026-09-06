package com.utfinancing.financehub.engine.claim.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.MapUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderDetailEntity;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderEntity;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderInvoiceEntity;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderSpecialEntity;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderDetailQueryDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderQueryDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSpecialQueryDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderDetailVO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderSpecialVO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderVO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderVoucherVO;
import com.utfinancing.financehub.engine.claim.service.*;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.CostChannelFeeEntity;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.finance.service.ICostChannelFeeService;
import com.utfinancing.financehub.engine.finance.service.IFundBusinessSystemEbankMappingService;
import com.utfinancing.financehub.engine.rule.model.dto.DataExecutionTaskDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IDataExecutionTaskService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.claim.service.impl.IClaimOrderJobHandlerServiceImpl</li>
 * <li>CreateTime : 2023/11/24 14:57</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Slf4j
@Service
public class IClaimOrderJobHandlerServiceImpl implements IClaimOrderJobHandlerService {
    @Resource
    private IClaimOrderSpecialService iClaimOrderSpecialService;
    @Resource
    private IRuleService iRuleService;
    @Resource
    private IContractService iContractService;
    @Resource
    private IClaimOrderInvoiceService iClaimOrderInvoiceService;
    @Resource
    private IClaimOrderService iClaimOrderService;
    @Resource
    private IClaimOrderDetailService iClaimOrderDetailService;
    @Resource
    private ICostChannelFeeService iCostChannelFeeService;

    @Resource
    private IDataExecutionTaskService iDataExecutionTaskService;
    @Override
    public Boolean costGenerateVoucher(ClaimOrderSpecialQueryDTO queryDTO) {
        //查询费用类型为：GPS费用并且是未生成凭证的数据 状态为付款完成的生成付款凭证，状态为审批完成的且发票表里有数据的生成收票凭证
        //1.查询是否有正在执行的任务
        String systemCode = "COST_GPS_VOUCHER";
        DataExecutionTaskDTO taskDTO = iDataExecutionTaskService.getRunningTaskBySystem(systemCode);
        if (taskDTO != null) {
            log.info("存在正在执行的任务，本次任务跳过, systemCode:{}", systemCode);
            return Boolean.TRUE;
        }
            queryDTO.setOrderStatus("付款成功");
            queryDTO.setExpenseTypeList(ClaimOrderExpenseTypeEnum.getCodeList());
            queryDTO.setIsGenerateVoucher("0");
            List<ClaimOrderSpecialVO> orderSpecialVOList = iClaimOrderSpecialService.selectByCondition(queryDTO);
            if (CollectionUtils.isEmpty(orderSpecialVOList)) {
                return Boolean.FALSE;
            }
            Long taskId = iDataExecutionTaskService.createNewTask(systemCode, DataExecutionTaskStatusEnum.RUNNING.getCode(), orderSpecialVOList.size(), LocalDateTime.now(), LocalDateTime.now());
        try {
            //按照费用类型分组
            Map<String, List<ClaimOrderSpecialVO>> expenseTypeMap = orderSpecialVOList.stream().collect(Collectors.groupingBy(ClaimOrderSpecialVO::getExpenseType));
            for (Map.Entry<String, List<ClaimOrderSpecialVO>> entry : expenseTypeMap.entrySet()) {
                if (ClaimOrderExpenseTypeEnum.GPS.getCode().equals(entry.getKey())) {
                    costGenerateVoucher(orderSpecialVOList, SceneEnum.GPS);
                } else if (ClaimOrderExpenseTypeEnum.SHOUHUAN.getCode().equals(entry.getKey())) {
                    costGenerateVoucher(orderSpecialVOList, SceneEnum.SHSBK);
                } else if (ClaimOrderExpenseTypeEnum.SHOUCHE.getCode().equals(entry.getKey())
                        || ClaimOrderExpenseTypeEnum.DIYA.getCode().equals(entry.getKey())
                        || ClaimOrderExpenseTypeEnum.JIEDIYA.getCode().equals(entry.getKey())
                ) {
                    costGenerateScfVoucher(orderSpecialVOList, SceneEnum.SCF);
                }
            }
        } catch (Exception e) {
            log.error("任务Id：{}成本类任务执行失败",taskId);
        } finally {
            iDataExecutionTaskService.finishedTask(taskId, DataExecutionTaskStatusEnum.SUCCESS.getCode(),0, 0);
        }
        return Boolean.TRUE;
    }

    public void getAmount(ClaimOrderVoucherVO claimOrderVoucherVO,
                          List<ClaimOrderSpecialVO> specialVOList,
                          ClaimOrderSpecialExpenseTypeEnum enums,
                          List<ContractEntity> contractEntityList){
        //结转金额等于合同起租不含税单价*台数-支付含税单价*台数
        final BigDecimal[] actuallyPaymentAmount = {BigDecimal.ZERO};
        final BigDecimal[] noTaxAmount = {BigDecimal.ZERO};
        final BigDecimal[] taxAmount = {BigDecimal.ZERO};
        final BigDecimal[] settlementAmount = {BigDecimal.ZERO};
        //合同GPS单价
        BigDecimal gpsUnitPrice = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(contractEntityList)) {
            gpsUnitPrice = contractEntityList.stream().filter(v -> ObjectUtil.isNotNull(v.getGpsUnitPrice())).map(ContractEntity::getGpsUnitPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
        }
        BigDecimal finalGpsUnitPrice = gpsUnitPrice;
//        if (enums.getCode().equals(ClaimOrderSpecialExpenseTypeEnum.EQUIPMENT_COST.getCode())) {
//            //设备费
//            specialVOList.stream().forEach(v -> {
//                actuallyPaymentAmount[0] = actuallyPaymentAmount[0].add(null!=v.getUnitPrice() ? v.getUnitPrice() : BigDecimal.ZERO);
//                noTaxAmount[0] = noTaxAmount[0].add(null!=v.getEquipNotaxAmount() ? v.getEquipNotaxAmount() : BigDecimal.ZERO);
//                taxAmount[0] = taxAmount[0].add(null!=v.getEquipInputTax() ? v.getEquipInputTax() : BigDecimal.ZERO);
//                settlementAmount[0] = settlementAmount[0].add(finalGpsUnitPrice.subtract(null!=v.getEquipNotaxAmount() ? v.getEquipNotaxAmount() : BigDecimal.ZERO));
//            });
//        } else
        if (enums.getCode().equals(ClaimOrderSpecialExpenseTypeEnum.INSTALLATION_COST.getCode())) {
            //安装费
            specialVOList.stream().forEach(v -> {
                actuallyPaymentAmount[0] = actuallyPaymentAmount[0].add(null!=v.getInstallAmount() ? v.getInstallAmount() : BigDecimal.ZERO);
                noTaxAmount[0] = noTaxAmount[0].add(null!=v.getInstallNotaxAmount() ? v.getInstallNotaxAmount() : BigDecimal.ZERO);
                taxAmount[0] = taxAmount[0].add(null!=v.getInstallInputTax() ? v.getInstallInputTax() : BigDecimal.ZERO);
                settlementAmount[0] = settlementAmount[0].add(finalGpsUnitPrice.subtract(null!=v.getInstallNotaxAmount() ? v.getInstallNotaxAmount() : BigDecimal.ZERO));
            });
        } else if (enums.getCode().equals(ClaimOrderSpecialExpenseTypeEnum.SERVICE_COST.getCode())) {
            //服务费
            specialVOList.stream().forEach(v -> {
                actuallyPaymentAmount[0] = actuallyPaymentAmount[0].add(null!=v.getServiceAmount() ? v.getServiceAmount() : BigDecimal.ZERO);
                noTaxAmount[0] = noTaxAmount[0].add(null!=v.getServiceNotaxAmount() ? v.getServiceNotaxAmount() : BigDecimal.ZERO);
                taxAmount[0] = taxAmount[0].add(null!=v.getServiceInputTax() ? v.getServiceInputTax() : BigDecimal.ZERO);
                settlementAmount[0] = settlementAmount[0].add(finalGpsUnitPrice.subtract(null!=v.getServiceNotaxAmount() ? v.getServiceNotaxAmount() : BigDecimal.ZERO));
            });
        }
        claimOrderVoucherVO.setActuallyPaymentAmount(actuallyPaymentAmount[0]);
        claimOrderVoucherVO.setNoTaxAmount(noTaxAmount[0]);
        claimOrderVoucherVO.setTaxAmount(taxAmount[0]);
        claimOrderVoucherVO.setSettlementAmount(settlementAmount[0]);
    }

    @Override
    public Boolean courtCostGenerateVoucher(ClaimOrderQueryDTO queryDTO) {
        //1.查询是否有正在执行的任务
        String systemCode = "COURT_COST";
        DataExecutionTaskDTO taskDTO = iDataExecutionTaskService.getRunningTaskBySystem(systemCode);
        if (taskDTO != null) {
            log.info("存在正在执行的任务，本次任务跳过, systemCode:{}", systemCode);
            return Boolean.TRUE;
        }
        queryDTO.setExpenseTypeList(Arrays.asList("诉讼保全费","公告费"));
        queryDTO.setIsGenerateVoucher("0");
        queryDTO.setOrderStatus("付款成功");
        //一次查询500条记录
        List<ClaimOrderVO> claimOrderVOList = iClaimOrderService.selectByCondition(queryDTO);
        if (CollectionUtils.isEmpty(claimOrderVOList)) {
            return Boolean.TRUE;
        }
        Long taskId = iDataExecutionTaskService.createNewTask(systemCode, DataExecutionTaskStatusEnum.RUNNING.getCode(), claimOrderVOList.size(), LocalDateTime.now(),  LocalDateTime.now());
        try {
            claimOrderVOList.stream().forEach(v -> {
                List<ClaimOrderDetailEntity> detailEntityList = iClaimOrderDetailService.lambdaQuery().eq(ClaimOrderDetailEntity::getIsGenerateVoucher, "0").eq(ClaimOrderDetailEntity::getClaimOrderId, v.getId()).list();
                ClaimOrderDetailEntity claimOrderDetailEntity = detailEntityList.get(0);
                ClaimOrderVoucherVO claimOrderVoucherVO = new ClaimOrderVoucherVO();
                claimOrderVoucherVO.setSystemCode(SystemEnum.MFXT.getCode());
                claimOrderVoucherVO.setSystemName(SystemEnum.MFXT.getDesc());
                claimOrderVoucherVO.setBusinessCode("ZLYW");
                claimOrderVoucherVO.setBusinessName("租赁");
                claimOrderVoucherVO.setSceneCode(SceneEnum.SSF.getCode());
                claimOrderVoucherVO.setSceneName(SceneEnum.SSF.name());
                claimOrderVoucherVO.setBatchId(v.getId());
                claimOrderVoucherVO.setBatchType(BatchTypeEnum.SSFZF.getCode());
                claimOrderVoucherVO.setExpenseType(claimOrderDetailEntity.getExpenseType());
                claimOrderVoucherVO.setLitigationExpenseType(claimOrderDetailEntity.getExpenseType());
                String contractCode = claimOrderDetailEntity.getLeaseContractNo();
                claimOrderVoucherVO.setInterfaceCreateTime(v.getCreateTime());
                if ("诉讼保全费".equals(claimOrderDetailEntity.getExpenseType())) {
                    contractCode = claimOrderDetailEntity.getLeaseContractNo();
                } else if ("公告费".equals(claimOrderDetailEntity.getExpenseType())) {
                    String content = claimOrderDetailEntity.getContent();
                    if (StringUtils.isNotEmpty(content) && content.contains("-")) {
                        contractCode = content.split("-")[0];
                    }
                }
                claimOrderVoucherVO.setOrderId(v.getId().toString() + "_" + contractCode);
                claimOrderVoucherVO.setContractCode(contractCode);
                claimOrderVoucherVO.setOrderNo(v.getOrderNo());
                claimOrderVoucherVO.setBusinessDate(null == v.getSubmitDate() ? new Date() : DateUtil.date(v.getSubmitDate()));
                claimOrderVoucherVO.setAccountDate(null == v.getSubmitDate() ? new Date() : DateUtil.date(v.getSubmitDate()));
                claimOrderVoucherVO.setCostBearDepartment(v.getCostBearDepartment());
                claimOrderVoucherVO.setContent("");
                claimOrderVoucherVO.setOrgId(v.getOrgId());
                claimOrderVoucherVO.setCurrencyType(v.getCurrencyType());
                claimOrderVoucherVO.setLitigationExpensePayable(StringUtils.isNotEmpty(v.getClaimAmount()) ? new BigDecimal(v.getClaimAmount()) : BigDecimal.ZERO);
                claimOrderVoucherVO.setClientCode(v.getClientCode());
                claimOrderVoucherVO.setClientName(v.getClientName());
                //生成汇总凭证
                Map<String, Object> dataMap = BeanUtil.beanToMap(claimOrderVoucherVO);
                log.info("魔方诉讼支付费生成汇总凭证参数：{}", JSON.toJSONString(claimOrderVoucherVO));
                String isGenerateVoucher = "0";
                try {
                    List<VoucherDTO> voucherDTOList = iRuleService.executeRule(dataMap);
                    log.info("魔方诉讼支付费生成汇总凭证返回值：{}", JSON.toJSONString(voucherDTOList));
                    if (CollectionUtils.isNotEmpty(voucherDTOList)) {
                        //更新报销单主表生成凭证
                        isGenerateVoucher = "1";
                    } else {
                        isGenerateVoucher = "2";
                    }
                } catch (Exception e) {
                    isGenerateVoucher = "2";
                } finally {
                    iClaimOrderService.lambdaUpdate().set(ClaimOrderEntity::getIsGenerateVoucher, isGenerateVoucher).eq(ClaimOrderEntity::getId, v.getId()).update();
                }
                detailEntityList.stream().forEach(d -> {
                    //生成分凭证
                    String clientName = "";
                    if ("诉讼保全费".equals(d.getExpenseType())) {
                        clientName = d.getPassenger();
                    } else if ("公告费".equals(d.getExpenseType())) {
                        String content = d.getContent();
                        if (StringUtils.isNotEmpty(content) && content.contains("-")) {
                            clientName = content.split("-")[1];
                        }
                    }
                    claimOrderVoucherVO.setExpenseType(d.getExpenseType());
                    claimOrderVoucherVO.setLitigationExpenseType(d.getExpenseType());
//                claimOrderVoucherVO.setClientCode(clientName);
                    claimOrderVoucherVO.setClientName(clientName);
                    claimOrderVoucherVO.setContent(d.getContent());
                    claimOrderVoucherVO.setOrderId(d.getId().toString());
                    claimOrderVoucherVO.setLitigationExpensePayment(StringUtils.isNotEmpty(d.getPrePaymentAmount()) ? new BigDecimal(d.getPrePaymentAmount()) : BigDecimal.ZERO);
                    Map<String, Object> detailDataMap = BeanUtil.beanToMap(claimOrderVoucherVO);
                    log.info("魔方诉讼支付费生成分凭证参数：{}", JSON.toJSONString(claimOrderVoucherVO));
                    String isDetailGenerateVoucher = "0";
                    try {
                        List<VoucherDTO> voucherDetailList = iRuleService.executeRule(detailDataMap);
                        log.info("魔方诉讼支付费生成分凭证返回值：{}", JSON.toJSONString(voucherDetailList));
                        if (CollectionUtils.isNotEmpty(voucherDetailList)) {
                            isDetailGenerateVoucher = "1";
                        } else {
                            isDetailGenerateVoucher = "2";
                        }
                    } catch (Exception e) {
                        isDetailGenerateVoucher = "2";
                    } finally {
                        iClaimOrderDetailService.lambdaUpdate().set(ClaimOrderDetailEntity::getIsGenerateVoucher, isDetailGenerateVoucher).eq(ClaimOrderDetailEntity::getId, d.getId()).update();
                    }
                });
            });
            log.info("任务id:{},诉讼费生成凭证完成", taskId);
        } catch (Exception e) {
            log.error("任务执行失败失败原因：",e);
        } finally {
            iDataExecutionTaskService.finishedTask(taskId, DataExecutionTaskStatusEnum.SUCCESS.getCode(),0, 0);
        }
        return Boolean.TRUE;
    }

    public void getVoucherAmount(ClaimOrderVoucherVO claimOrderVoucherVO,
                                 List<ClaimOrderSpecialVO> specialVOList) {
        final BigDecimal[] actuallyPaymentAmount = {BigDecimal.ZERO};
        final BigDecimal[] noTaxAmount = {BigDecimal.ZERO};
        final BigDecimal[] taxAmount = {BigDecimal.ZERO};
        specialVOList.stream().forEach(v -> {
            actuallyPaymentAmount[0] = actuallyPaymentAmount[0].add(ObjectUtil.isNotEmpty(v.getPaymentAmount()) ? v.getPaymentAmount() : BigDecimal.ZERO);
            noTaxAmount[0] = noTaxAmount[0].add(ObjectUtil.isNotEmpty(v.getPaymentNotaxAmount()) ? v.getPaymentNotaxAmount() : BigDecimal.ZERO);
            taxAmount[0] = taxAmount[0].add(ObjectUtil.isNotEmpty(v.getPaymentInputTax()) ? v.getPaymentInputTax() : BigDecimal.ZERO);
        });
        claimOrderVoucherVO.setActuallyPaymentAmount(actuallyPaymentAmount[0]);
        claimOrderVoucherVO.setNoTaxAmount(noTaxAmount[0]);
        claimOrderVoucherVO.setTaxAmount(taxAmount[0]);
    }

    public void costGenerateVoucher(List<ClaimOrderSpecialVO> orderSpecialVOList,SceneEnum sceneEnum){
        //按照订单编号+合同编号分组
        Map<String, List<ClaimOrderSpecialVO>> orderSpecialMap = orderSpecialVOList.stream().collect(Collectors.groupingBy(v -> v.getOrderNo() + "_" + v.getContractNum()));
        for(Map.Entry<String, List<ClaimOrderSpecialVO>> entry : orderSpecialMap.entrySet()) {
            List<Long> specialIdList = entry.getValue().stream().map(ClaimOrderSpecialVO::getId).collect(Collectors.toList());
            //数据合并之后，一条数据分为安装费，服务费生成相应的凭证
            ClaimOrderSpecialVO specialVO = entry.getValue().get(0);
            ClaimOrderVoucherVO claimOrderVoucherVO = new ClaimOrderVoucherVO();
            claimOrderVoucherVO.setSystemCode(SystemEnum.MFXT.getCode());
            claimOrderVoucherVO.setSystemName(SystemEnum.MFXT.getDesc());
            claimOrderVoucherVO.setBusinessCode("ZLYW");
            claimOrderVoucherVO.setBusinessName("租赁");
            claimOrderVoucherVO.setSceneCode(sceneEnum.getCode());
            claimOrderVoucherVO.setSceneName(sceneEnum.getDesc());
            claimOrderVoucherVO.setOrderId(specialVO.getClaimOrderId().toString() + "_" + specialVO.getContractNum());
            claimOrderVoucherVO.setContractCode(specialVO.getContractNum());
            claimOrderVoucherVO.setBusinessDate(null == specialVO.getExpenseDate() ? new Date() : DateUtil.date(specialVO.getExpenseDate()));
            claimOrderVoucherVO.setInterfaceCreateTime(LocalDateTime.now());
            //获取合同表对应的合同编码+签约主体+场景编码（租赁）
            if ("0".equals(specialVO.getIsGenerateVoucher())) {
                List<ContractEntity> contractEntityList = iContractService.lambdaQuery().eq(ContractEntity::getContractCode, specialVO.getContractNum()).eq(ContractEntity::getOrgId, specialVO.getOrgId()).eq(ContractEntity::getBusinessCode, "ZLYW").list();
                for (ClaimOrderSpecialExpenseTypeEnum enums : ClaimOrderSpecialExpenseTypeEnum.values()) {
                    claimOrderVoucherVO.setExpenseType(enums.getDesc());
                    getAmount(claimOrderVoucherVO, entry.getValue(), enums, contractEntityList);
                    //服务费报销费不含税金额加到一起放到一个临时表中字段：客户编码,签约主体，日期，系统来源，不含税金额汇总
                    saveCostChannelFee(claimOrderVoucherVO);
                    claimOrderVoucherVO.setInterfaceCreateTime(specialVO.getCreateTime());
                    Map<String, Object> dataMap = BeanUtil.beanToMap(claimOrderVoucherVO);
                    log.info("魔方GPS费生成凭证参数：{}", JSON.toJSONString(claimOrderVoucherVO));
                    try {
                        List<VoucherDTO> voucherDTOList = iRuleService.executeRule(dataMap);
                        log.info("魔方GPS费生成凭证返回值：{}", JSON.toJSONString(voucherDTOList));
                    } catch (Exception e) {
                        log.error("生成报销费用失败，失败原因:",e);
                    }
                }
            }
            // 更新special表
            iClaimOrderSpecialService.lambdaUpdate().set(ClaimOrderSpecialEntity::getIsGenerateVoucher, "1").in(ClaimOrderSpecialEntity::getId, specialIdList).update();
            //生成收票数据
            //判断报销单是否存在发票数据，存在则需要生成收票凭证
            List<ClaimOrderInvoiceEntity> invoiceEntityList = iClaimOrderInvoiceService.lambdaQuery().eq(ClaimOrderInvoiceEntity::getClaimOrderId, specialVO.getClaimOrderId()).eq(ClaimOrderInvoiceEntity::getIsGenerateVoucher,"0").list();
            if (CollectionUtils.isNotEmpty(invoiceEntityList)) {
                invoiceEntityList.stream().forEach(e ->{
                    claimOrderVoucherVO.setOrderId(e.getId().toString());
                    claimOrderVoucherVO.setExpenseType("");
                    claimOrderVoucherVO.setIsReceiveInvoiceVoucher(1);
                    getVoucherAmount(claimOrderVoucherVO, entry.getValue());
                    claimOrderVoucherVO.setInterfaceCreateTime(e.getCreateTime());
                    Map<String, Object> dataMap = BeanUtil.beanToMap(claimOrderVoucherVO);
                    log.info("魔方GPS费生成收票凭证参数：{}", JSON.toJSONString(claimOrderVoucherVO));
                    List<VoucherDTO> voucherDTOList = iRuleService.executeRule(dataMap);
                    log.info("魔方GPS费生成收票凭证返回值：{}", JSON.toJSONString(voucherDTOList));
                });
            }
        }
    }

    public void costGenerateScfVoucher(List<ClaimOrderSpecialVO> orderSpecialVOList,SceneEnum sceneEnum){
        Map<String, List<ClaimOrderSpecialVO>> orderSpecialMap = orderSpecialVOList.stream().collect(Collectors.groupingBy(v -> v.getOrderNo() + "_" + v.getContractNum()));
        List<Map<String,Object>> voucherMapList = Lists.newArrayList();
        List<Map<String,Object>> receiptMapList = Lists.newArrayList();
        List<Long> orderVoucherIdList = Lists.newArrayList();
        List<Long> orderIdList = Lists.newArrayList();
        List<Long> specialIdList = orderSpecialVOList.stream().map(ClaimOrderSpecialVO::getId).collect(Collectors.toList());
        for (Map.Entry<String, List<ClaimOrderSpecialVO>> entry:orderSpecialMap.entrySet()) {
            List<ClaimOrderSpecialVO> claimOrderSpecialVOList =  entry.getValue();
            ClaimOrderSpecialVO specialVO = entry.getValue().get(0);
            ClaimOrderVoucherVO claimOrderVoucherVO = new ClaimOrderVoucherVO();
            if ("0".equals(specialVO.getIsGenerateVoucher())) {
                orderIdList.add(specialVO.getClaimOrderId());
                List<ContractEntity> contractEntityList = iContractService.lambdaQuery().eq(ContractEntity::getContractCode, specialVO.getContractNum()).eq(ContractEntity::getOrgId, specialVO.getOrgId()).eq(ContractEntity::getBusinessCode, "ZLYW").list();
                claimOrderVoucherVO.setSystemCode(SystemEnum.MFXT.getCode());
                claimOrderVoucherVO.setSystemName(SystemEnum.MFXT.getDesc());
                claimOrderVoucherVO.setBusinessCode(BusinessEnum.ZLYW.getCode());
                claimOrderVoucherVO.setBusinessName(BusinessEnum.ZLYW.getDesc());
                claimOrderVoucherVO.setOrderId(specialVO.getId() + "_" + specialVO.getContractNum());
                claimOrderVoucherVO.setSceneCode(SceneEnum.SCF.getCode());
                claimOrderVoucherVO.setSceneName(SceneEnum.SCF.getDesc());
                claimOrderVoucherVO.setBusinessDate(null == specialVO.getExpenseDate() ? new Date() : DateUtil.date(specialVO.getExpenseDate()));
                claimOrderVoucherVO.setContractCode(specialVO.getContractNum());
                claimOrderVoucherVO.setExpenseType(sceneEnum.getCode());
                claimOrderVoucherVO.setBatchId(specialVO.getClaimOrderId());
                claimOrderVoucherVO.setBatchType(BatchTypeEnum.SCF.getCode());
                claimOrderVoucherVO.setTransactionStructureAdjustType("调整，无需支付");
                setAmount(claimOrderSpecialVOList, contractEntityList, claimOrderVoucherVO);
                claimOrderVoucherVO.setInterfaceCreateTime(specialVO.getCreateTime());
                saveCostChannelFee(claimOrderVoucherVO);
                Map<String, Object> dataMap = BeanUtil.beanToMap(claimOrderVoucherVO);
                voucherMapList.add(dataMap);
            }
            //生成收票数据
            //判断报销单是否存在发票数据，存在则需要生成收票凭证
            List<ClaimOrderInvoiceEntity> invoiceEntityList = iClaimOrderInvoiceService.lambdaQuery().eq(ClaimOrderInvoiceEntity::getClaimOrderId, specialVO.getClaimOrderId()).eq(ClaimOrderInvoiceEntity::getIsGenerateVoucher,"0").list();
            if (CollectionUtils.isNotEmpty(invoiceEntityList)) {
                ClaimOrderVoucherVO receiptVoucerVO = BeanUtil.copyProperties(claimOrderVoucherVO, ClaimOrderVoucherVO.class);
                invoiceEntityList.stream().forEach(e ->{
                    receiptVoucerVO.setOrderId(e.getId().toString());
                    orderVoucherIdList.add(e.getId());
                    receiptVoucerVO.setExpenseType("");
                    receiptVoucerVO.setIsReceiveInvoiceVoucher(1);
                    receiptVoucerVO.setInterfaceCreateTime(e.getCreateTime());
                    Map<String, Object> receipDataMap = BeanUtil.beanToMap(receiptVoucerVO);
                    receiptMapList.add(receipDataMap);
                });
            }
        }
        //生成凭证
        if (CollectionUtils.isNotEmpty(voucherMapList)) {
            List<VoucherInfoVO> voucherResultList = iRuleService.batchExecuteRule(voucherMapList);
        }
        //生成收票凭证
        if (CollectionUtils.isNotEmpty(receiptMapList)) {
            List<VoucherInfoVO> voucherResultList = iRuleService.batchExecuteRule(voucherMapList);
        }
        //服务费报销费不含税金额加到一起放到一个临时表中字段：客户编码,签约主体，日期，系统来源，不含税金额汇总
        //更新报销单主表生成凭证
//        if (CollectionUtils.isNotEmpty(orderIdList)) {
//            iClaimOrderService.lambdaUpdate().set(ClaimOrderEntity::getIsGenerateVoucher, "1").in(ClaimOrderEntity::getId, orderIdList).update();
//        }
//        if (CollectionUtils.isNotEmpty(orderVoucherIdList)) {
//            iClaimOrderInvoiceService.lambdaUpdate().set(ClaimOrderInvoiceEntity::getIsGenerateVoucher, "1").in(ClaimOrderInvoiceEntity::getId, orderVoucherIdList).update();
//        }
        //更新special表
        iClaimOrderSpecialService.lambdaUpdate().set(ClaimOrderSpecialEntity::getIsGenerateVoucher, "1").in(ClaimOrderSpecialEntity::getId, specialIdList).update();

    }

    public void setAmount(List<ClaimOrderSpecialVO> claimOrderSpecialVOList,List<ContractEntity> contractEntityList,ClaimOrderVoucherVO claimOrderVoucherVO){
        //合同GPS单价
        BigDecimal payableRecycleCarAmount = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(contractEntityList)) {
            payableRecycleCarAmount = contractEntityList.stream().filter(v -> ObjectUtil.isNotNull(v.getPayableRecycleCarAmount())).map(ContractEntity::getPayableRecycleCarAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        }
        //实际付款金额
        final BigDecimal[] actuallyPaymentAmount = {BigDecimal.ZERO};
        //支付不含税金额
        final BigDecimal[] noTaxAmount = {BigDecimal.ZERO};
        //税额
        final BigDecimal[] taxAmount = {BigDecimal.ZERO};
        //结转金额
        final BigDecimal[] settlementAmount = {BigDecimal.ZERO};
        BigDecimal finalPayableRecycleCarAmount = payableRecycleCarAmount;
        claimOrderSpecialVOList.stream().forEach(v -> {
            actuallyPaymentAmount[0] = actuallyPaymentAmount[0].add(ObjectUtil.isNotNull(v.getPaymentAmount()) ? v.getPaymentAmount() : BigDecimal.ZERO);
            taxAmount[0] = taxAmount[0].add(ObjectUtil.isNotNull(v.getPaymentInputTax()) ? v.getPaymentInputTax() : BigDecimal.ZERO);
            noTaxAmount[0] = noTaxAmount[0].add(ObjectUtil.isNotNull(v.getPaymentNotaxAmount()) ? v.getPaymentNotaxAmount() : BigDecimal.ZERO);
            settlementAmount[0] = settlementAmount[0].add(finalPayableRecycleCarAmount.subtract(ObjectUtil.isNotNull(v.getPaymentNotaxAmount()) ? v.getPaymentNotaxAmount() : BigDecimal.ZERO));
        });
        claimOrderVoucherVO.setActuallyPaymentAmount(actuallyPaymentAmount[0]);
        claimOrderVoucherVO.setNoTaxAmount(noTaxAmount[0]);
        claimOrderVoucherVO.setTaxAmount(taxAmount[0]);
        claimOrderVoucherVO.setSettlementAmount(settlementAmount[0]);
    }

    public void saveCostChannelFee(ClaimOrderVoucherVO claimOrderVoucherVO){
        CostChannelFeeEntity costChannelFeeEntity = new CostChannelFeeEntity();
        String expenseMainCategoryType = "";
        if (SceneEnum.GPS.getCode().equals(claimOrderVoucherVO.getSceneCode())) {
            expenseMainCategoryType = CostMainCategoryExpenseTypeEnum.GPS.getCode();
        } else if (SceneEnum.SCF.getCode().equals(claimOrderVoucherVO.getSceneCode())) {
            expenseMainCategoryType = CostMainCategoryExpenseTypeEnum.COLLECT_FEE.getCode();
        } else if (SceneEnum.SHSBK.getCode().equals(claimOrderVoucherVO.getSceneCode())) {
            expenseMainCategoryType = CostMainCategoryExpenseTypeEnum.GRACELETE.getCode();
        }
        costChannelFeeEntity.setIsAutoGenerate("1");
        costChannelFeeEntity.setActualAmount(claimOrderVoucherVO.getActuallyPaymentAmount());
        costChannelFeeEntity.setBusinessDate(CommonDateUtils.parseDateToLocalDateTime(claimOrderVoucherVO.getBusinessDate()));
        costChannelFeeEntity.setChannelType(CostChannelTypeEnum.getCodeByDesc(claimOrderVoucherVO.getExpenseType()));
        costChannelFeeEntity.setContractCode(claimOrderVoucherVO.getContractCode());
        costChannelFeeEntity.setExpenseMainCategoryType(expenseMainCategoryType);
        costChannelFeeEntity.setNoTaxAmount(claimOrderVoucherVO.getNoTaxAmount());
        costChannelFeeEntity.setNoTaxTransactionAmount(claimOrderVoucherVO.getSettlementAmount());
        costChannelFeeEntity.setStructureType(claimOrderVoucherVO.getTransactionStructureAdjustType());
        costChannelFeeEntity.setTaxAmount(claimOrderVoucherVO.getTaxAmount());
        iCostChannelFeeService.save(costChannelFeeEntity);
    }
}
