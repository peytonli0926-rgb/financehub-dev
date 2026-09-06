package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.enums.DataExecutionTaskStatusEnum;
import com.utfinancing.financehub.engine.enums.MqErrorMessageStatusEnum;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.InvoiceClaimVO;
import com.utfinancing.financehub.engine.finance.entity.InvoiceClaimEntity;
import com.utfinancing.financehub.engine.finance.mapper.InvoiceClaimMapper;
import com.utfinancing.financehub.engine.finance.model.vo.OutTableContractDetailVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.model.dto.CourtCostVerificationDTO;
import com.utfinancing.financehub.engine.rule.model.dto.DataExecutionTaskDTO;
import com.utfinancing.financehub.engine.rule.model.dto.MqErrorMessageDTO;
import com.utfinancing.financehub.engine.rule.model.vo.RawTransactionDataDuplicateVo;
import com.utfinancing.financehub.engine.rule.service.IDataExecutionTaskService;
import com.utfinancing.financehub.engine.rule.service.IMqErrorMessageService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.verification.service.IVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-05
 * @Description :  InvoiceClaim服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class InvoiceClaimServiceImpl extends ServiceImpl<InvoiceClaimMapper, InvoiceClaimEntity> implements IInvoiceClaimService {

    private final InvoiceClaimMapper invoiceClaimMapper;

    @Resource
    private RemoteDictService remoteDictService;

    @Resource
    private IDataExecutionTaskService dataExecutionTaskService;

    @Resource
    private IRuleService iRuleService;

    @Resource
    private IVerificationService iVerificationService;

    @Resource
    private IOutTableAbsService iOutTableAbsService;

    @Resource
    private IContractService iContractService;

    @Resource
    private IVoucherService iVoucherService;

    @Resource
    private IClientService iClientService;

    @Resource
    private final IMqErrorMessageService errorMessageService;
    private static Random random;

    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            log.error("error.", e);
        }
    }

    @Override
    public Long saveInvoiceClaim(InvoiceClaimDTO dto) {
        InvoiceClaimEntity entity = BeanUtil.copyProperties(dto, InvoiceClaimEntity.class);
        entity.setId(IdWorker.getId());
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateInvoiceClaim(Long id, InvoiceClaimDTO dto) {
        InvoiceClaimEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public InvoiceClaimDTO getInvoiceClaimDTOById(Long id) {
        InvoiceClaimEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, InvoiceClaimDTO.class);
    }

    @Override
    public IPage<InvoiceClaimVO> selectPage(InvoiceClaimQueryDTO queryDTO) {
        LambdaQueryWrapper<InvoiceClaimEntity> queryWrapper = Wrappers.<InvoiceClaimEntity>lambdaQuery();
        //这里注入查询条件
        IPage<InvoiceClaimEntity> entityIPage = invoiceClaimMapper.selectPage(new Page<InvoiceClaimEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, InvoiceClaimVO.class);
    }

    @Override
    public Boolean saveBatchInvoiceClaim(List<InvoiceClaimDTO> dto) {
        return null;
    }

    @Override
    public Long saveMqInvoiceClaim(InvoiceClaimDTO dto) {
        dto.setSourceFrom("2");
        dto.setSystemInvoiceId(random.nextLong());
        //处理是否自动生成凭证标识
        //获取映射配置规则
        try {
            R<List<SysDictData>> sysDictDataResult = remoteDictService.listDictData("invoice_claim");
            if (null == sysDictDataResult || CollectionUtils.isEmpty(sysDictDataResult.getData())) {
                throw new RuntimeException("配置文件不存在请检查");
            }
            //获取字典标签配置最大的那一条数据
            Optional<SysDictData> sysDictDataOptional = sysDictDataResult.getData().stream().max(Comparator.comparing(SysDictData::getDictValue));
            SysDictData sysDictData = sysDictDataOptional.get();
            //字典值转换为map
            Map<String, String> dictMap = JSON.parseObject(sysDictData.getDictLabel(), HashMap.class);
            //特殊处理，获取金融/融资租赁大类下的开票内容
            List<String> invoiceCotentList = Lists.newArrayList();
            for (Map.Entry<String, String> entry : dictMap.entrySet()) {
                if (StringUtils.isNotEmpty(entry.getKey()) && entry.getKey().contains("金融服务/融资租赁")) {
                    String[] array = entry.getKey().split("\\_");
                    String invoiceCotent = StringUtils.isNotEmpty(array[1]) ? array[1] : "";
                    invoiceCotentList.add(invoiceCotent);
                }
            }
            String systemMainCategory = StringUtils.isNotEmpty(dto.getMainCategory()) ? dto.getMainCategory() : "null";
            String productName = StringUtils.isNotEmpty(dto.getProductName()) ? dto.getProductName() : "null";
            if (StringUtils.isEmpty(systemMainCategory) && StringUtils.isNotEmpty(dto.getContractCode())) {
                dto.setIsAutoGeneration("1");
                dto.setAccountName("租金");
            } else {
                for (Map.Entry<String, String> entry : dictMap.entrySet()) {
                    //拆分key值 大类”_“开票内容 存在的自动自动生成凭证，否则手动生成
                    String[] array = entry.getKey().split("\\_");
                    String mainCategory = StringUtils.isEmpty(array[0]) || "null".equals(array[0]) ? "" : array[0];
                    String invoiceCotent = StringUtils.isEmpty(array[1]) || "null".equals(array[1]) ? "" : array[1];
                    //按照”/“拆分大类
                    Boolean isBreak = Boolean.FALSE;
                    if (StringUtils.isNotEmpty(mainCategory)) {
                        String[] mainArray = mainCategory.split("\\/");
                        for (String s : mainArray) {
                            if (systemMainCategory.contains(s) && invoiceCotent.contains(productName)) {
                                dto.setAccountName(entry.getValue());
                                dto.setMappingCategory(s);
                                dto.setIsAutoGeneration("1");
                                isBreak = Boolean.TRUE;
                                break;
                            } else if ("金融服务/融资租赁".equals(mainCategory)
                                    && systemMainCategory.contains(s)
                                    && !invoiceCotentList.contains(productName)
                                    && StringUtils.isEmpty(invoiceCotent)) {
                                dto.setAccountName(entry.getValue());
                                dto.setMappingCategory(s);
                                dto.setIsAutoGeneration("1");
                                isBreak = Boolean.TRUE;
                                break;
                            }
                        }
                        if (isBreak) {
                            break;
                        }
                    } else if (StringUtils.isEmpty(systemMainCategory) && invoiceCotent.contains(productName)) {
                        dto.setAccountName(entry.getValue());
                        dto.setIsAutoGeneration("1");
                        break;
                    }
                }

                if (StringUtils.isEmpty(dto.getAccountName()) && StringUtils.isNotEmpty(dto.getContractCode())) {
                    dto.setIsAutoGeneration("1");
                    dto.setAccountName("租金");
                }
            }
            //处理税率还有%的数据
            if (StringUtils.isNotEmpty(dto.getTaxRate()) && dto.getTaxRate().contains("%")) {
                String numericPart = dto.getTaxRate().replace("%", "");
                // 转换为BigDecimal并除以100
                String taxRate = new BigDecimal(numericPart).divide(new BigDecimal("100")).toString();
                dto.setTaxRate(taxRate);
            }
        } catch (Exception e) {
            log.info("获取字典配置失败，失败原因：" + e.getMessage());
        }
        return this.saveInvoiceClaim(dto);
    }

    @Async
    @Override
    public Boolean invoiceGenerateVoucher(Boolean skipRepeatDataCheck) {
        //1.查询是否有正在执行的任务
        String systemCode = "KPRL_VOUCHER";
        DataExecutionTaskDTO taskDTO = dataExecutionTaskService.getRunningTaskBySystem(systemCode);
        if (taskDTO != null) {
            log.info("存在正在执行的任务，本次任务跳过, systemCode:{}", systemCode);
            return Boolean.TRUE;
        }
        List<CourtCostVerificationDTO> verificationDTOList = Lists.newArrayList();




        //读取自动生成凭证的数据，调用凭证接口生成凭证
        List<InvoiceClaimEntity> vourcheClaimList = this.list(Wrappers.<InvoiceClaimEntity>lambdaQuery().eq(InvoiceClaimEntity::getIsAutoGeneration, "1").isNull(InvoiceClaimEntity::getVoucherId)
                .apply("(source_from !='2' or (source_from ='2' and invoice_type not like '%收据%'))").orderByAsc(InvoiceClaimEntity::getDocumentDate)
                .orderByAsc(InvoiceClaimEntity::getCreateTime)
                .last("limit 1000"));
        log.info("读取数据条数：{}", vourcheClaimList.size());
        if (CollectionUtils.isEmpty(vourcheClaimList)) {
            return Boolean.TRUE;
        }
        //从出表Abs状态为已提交、已复核、已传至金蝶获取借款合同编号多条取最新的一条记录
        Map<String, String> billContractMap = getBillContractMap(vourcheClaimList);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failedCount = new AtomicInteger();
        Long taskId = dataExecutionTaskService.createNewTask(systemCode, DataExecutionTaskStatusEnum.RUNNING.getCode(), vourcheClaimList.size(), CollectionUtil.getFirst(vourcheClaimList).getDocumentDate(), CollectionUtil.getLast(vourcheClaimList).getDocumentDate());


        //校验是否存在重复推送的数据
        List<RawTransactionDataDuplicateVo> duplicateDataList = this.getDuplicateData();
        if (CollectionUtil.isNotEmpty(duplicateDataList) && !skipRepeatDataCheck) {
            MqErrorMessageDTO errorMessageDTO = new MqErrorMessageDTO();
            errorMessageDTO.setStatus(MqErrorMessageStatusEnum.NOT_PROCESS.getCode());
            errorMessageDTO.setMessageBody(JSONObject.toJSONString(duplicateDataList));
            errorMessageDTO.setExceptionMessage("数据重复");
            errorMessageService.saveMqErrorMessage(errorMessageDTO);
            log.info("存在重复的数据，本次任务跳过, systemCode:{},duplacate data:{}", systemCode, duplicateDataList);
            return Boolean.TRUE;
        }
        //获取合同,开票对象
        Map<String, String> contractPurchaseMap = vourcheClaimList.stream().filter(v -> StringUtils.isNotEmpty(v.getContractCode())).collect(HashMap::new, (map, item) -> map.put(item.getContractCode(), item.getPurchaserName()), HashMap::putAll);
        final List<ContractEntity>[] contractEntityList = new List[]{Lists.newArrayList()};
        if (CollectionUtils.isNotEmpty(vourcheClaimList)) {
            if (!contractPurchaseMap.keySet().isEmpty()) {
                contractEntityList[0] = iContractService.lambdaQuery().in(ContractEntity::getContractCode, new ArrayList<>(contractPurchaseMap.keySet()))
                        .in(ContractEntity::getClientName, new ArrayList<>(contractPurchaseMap.values())).list();
            }

            vourcheClaimList.forEach(v -> {
                String key = v.getContractCode() + "-" + v.getPurchaserName();
                CourtCostVerificationDTO costVerificationDTO = new CourtCostVerificationDTO();
                costVerificationDTO.setContractCode(v.getContractCode());
                costVerificationDTO.setDocumentDate(DateUtil.date(v.getDocumentDate()));
                Map<String, Object> ruleMap = BeanUtil.beanToMap(v);
                ruleMap.put("systemCode", SystemEnum.KPXT.getCode());
                ruleMap.put("systemName", SystemEnum.KPXT.getCode());
                ruleMap.put("businessCode", "ZLYW");
                ruleMap.put("businessName", "租赁");
                ruleMap.put("sceneCode", "KJFP");
                ruleMap.put("sceneName", "开票");
                ruleMap.put("businessDate", v.getDocumentDate());
                ruleMap.put("voucherDate", v.getDocumentDate());
                ruleMap.put("batchId", v.getId().toString());
                ruleMap.put("batchType", "KJFP");
                ruleMap.put("orgId", v.getOrgId());
                ruleMap.put("interfaceCreateTime", v.getCreateTime());
                ruleMap.put("interfaceId", v.getId());
                ruleMap.put("billContratCode", billContractMap.getOrDefault(v.getContractCode(), Constants.BILL_CONTRACT_CODE_DEFAULT));
                ruleMap.put("clientCode", getClientCode(v.getContractCode(), v.getPurchaserName(), v.getOrgId()));
                log.info("开票查询客户编码结束");
                ruleMap.put("invoiceNumber", v.getInvoiceNumber());
                ruleMap.put("invoiceCode", v.getInvoicePointCode());
                Long voucherId = 0L;
                String error = "";
                try {
                    log.info("调用凭证接口ruleFacade.executeRule请求参数:{}", JSON.toJSONString(ruleMap));
                    List<VoucherDTO> voucherResult = iRuleService.executeRule(ruleMap);
                    log.info("调用凭证接口ruleFacade.executeRule返回值:{}", JSON.toJSONString(voucherResult));
                    if (CollectionUtils.isNotEmpty(voucherResult)) {
                        v.setVoucherId(voucherResult.get(0).getId());
                        voucherId = voucherResult.get(0).getId();
                        successCount.getAndIncrement();
                    } else {
                        v.setVoucherId(0L);
                    }
                    verificationDTOList.add(costVerificationDTO);
                } catch (Exception e) {
                    log.error("生成凭证失败原因：", e);
                    v.setErrorMessage(e.getMessage());
                    error = e.getMessage();
                    failedCount.getAndIncrement();
                } finally {
                    log.info("更新开票表数据开始");
                    this.lambdaUpdate().set(InvoiceClaimEntity::getVoucherId, voucherId).set(InvoiceClaimEntity::getErrorMessage, error).eq(InvoiceClaimEntity::getId, v.getId()).update();
                    log.info("更新开票表数据结束");
                }
            });
        }
        dataExecutionTaskService.finishedTask(taskId, DataExecutionTaskStatusEnum.SUCCESS.getCode(), successCount.get(), failedCount.get());
        if (CollectionUtils.isNotEmpty(verificationDTOList)) {
            updateContractAmount(verificationDTOList);
        }
        CompletableFuture.runAsync(() -> {
            if (!contractPurchaseMap.isEmpty()) {
                //开票合同的合同表开票标识为计提，且开票对象=合同表客户时，生成开票凭证时，将合同表开票标识改为“开票（原计提）
                contractEntityList[0] = contractEntityList[0].stream().filter(v -> Constants.INVOICING_FLAG.equals(v.getInvoicingFlag())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(contractEntityList[0])) {
                    iContractService.lambdaUpdate().set(ContractEntity::getInvoicingFlag, Constants.NEW_INVOICING_FLAG).in(ContractEntity::getId, contractEntityList[0].stream().map(ContractEntity::getId).collect(Collectors.toList())).update();
                }
            }
        });
        return Boolean.TRUE;
    }

    private List<RawTransactionDataDuplicateVo> getDuplicateData() {
        return invoiceClaimMapper.getDuplicateData();
    }

    @Override
    public void checkInvoiceData() {
        log.info("财务中台校验发票系统电子和纸质 校验重复数据开始");
        invoiceClaimMapper.checkInvoiceData();
        log.info("财务中台校验发票系统电子和纸质 校验重复数据结束");
    }

    @Override
    public void updateInvoiceDataContractCode() {
        log.info("财务中台校验发票系统电子和纸质 校验重复数据开始");
        invoiceClaimMapper.updateInvoiceDataContractCode();
        log.info("财务中台校验发票系统电子和纸质 校验重复数据结束");
    }

    public void updateContractAmount(List<CourtCostVerificationDTO> verificationDTOList) {
        CompletableFuture.runAsync(() -> {
            iVerificationService.updateContractAmount(verificationDTOList);
        });

    }

    public Map<String, String> getBillContractMap(List<InvoiceClaimEntity> invoiceClaimEntityList) {
        Map<String, String> billContractMap = Maps.newHashMap();
        List<String> contractCodeList = invoiceClaimEntityList.stream().map(InvoiceClaimEntity::getContractCode).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(contractCodeList)) {
            return billContractMap;
        }
        OutTableContractDetailQueryDTO outTableContractDetailQueryDTO = new OutTableContractDetailQueryDTO();
        outTableContractDetailQueryDTO.setContractCodeList(contractCodeList);
        outTableContractDetailQueryDTO.setProcessStatusList(Lists.newArrayList(ProcessStatusEnum.SUBMITTED.getCode(), ProcessStatusEnum.REVIEWED.getCode(), ProcessStatusEnum.TO_KINGDEE.getCode()));
        List<OutTableContractDetailVO> contractDetailVOList = iOutTableAbsService.selectByCondition(outTableContractDetailQueryDTO);
        if (CollectionUtils.isEmpty(contractDetailVOList)) {
            return billContractMap;
        }
        //按照合同编号分组，id倒叙排序取最大的那条记录
        billContractMap = contractDetailVOList.stream()
                .collect(Collectors.groupingBy(
                        OutTableContractDetailVO::getContractCode,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingLong(OutTableContractDetailVO::getId)),
                                optional -> optional.map(OutTableContractDetailVO::getLoanContractCode).orElse("")
                        )
                ));
        return billContractMap;
    }

    public String getClientCode(String contractCode, String purchaserName, String orgId) {
        log.info("开票查询客户编码开始");
        /**
         * 参数clientCode取值改为：
         * 生成凭证前，根据名称查询的客户编码，查不到时根据合同+org_id查合同表的客户编码
         */
        String clientCode = "";
        if (StringUtils.isEmpty(purchaserName)) {
            return StringUtil.EMPTY;
        }
        clientCode = iClientService.selectClientCodeByName(purchaserName);

        //客户编码没有找到根据名称去找客户编码
        if (StringUtils.isEmpty(clientCode)) {
            ContractDTO contractDTO = iContractService.getContractDTOByCode(contractCode, orgId);
            if (contractDTO != null) {
                clientCode = contractDTO.getClientCode();
            }
        }
        return clientCode;
    }

}

