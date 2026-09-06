package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.api.FieldMappingFacade;
import com.utfinancing.financehub.engine.api.RuleFacade;
import com.utfinancing.financehub.engine.api.VerificationFacade;
import com.utfinancing.financehub.engine.model.dto.CourtCostVerificationDTO;
import com.utfinancing.financehub.engine.model.dto.FieldMappingApiDTO;
import com.utfinancing.financehub.engine.model.vo.FieldMappingApiVO;
import com.utfinancing.financehub.engine.model.vo.VoucherVO;
import com.utfinancing.financehub.etl.constant.CommonConstant;
import com.utfinancing.financehub.etl.enums.DataExecutionTaskStatusEnum;
import com.utfinancing.financehub.etl.enums.ExecutionTaskSystemEnum;
import com.utfinancing.financehub.etl.financial.entity.InvoiceClaimEntity;
import com.utfinancing.financehub.etl.financial.model.dto.DataExecutionTaskDTO;
import com.utfinancing.financehub.etl.financial.model.dto.InvoiceClaimQueryDTO;
import com.utfinancing.financehub.etl.financial.model.vo.InvoiceClaimVO;
import com.utfinancing.financehub.etl.financial.service.IDataExecutionTaskService;
import com.utfinancing.financehub.etl.financial.service.IInvoiceClaimService;
import com.utfinancing.financehub.etl.financial.service.InvoiceService;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicing.service.ITaxicOiInvoiceMiddleService;
import com.utfinancing.financehub.etl.invoicingelec.service.ITaxicIiInvoiceElecService;
import com.utfinancing.financehub.etl.invoicingpaper.service.ITaxicIiInvoicePaperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.financial.service.impl.InvoiceServiceImpl</li>
 * <li>CreateTime : 2023/11/10 19:14</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {
    @Resource
    IInvoiceClaimService invoiceClaimService;
    @Resource
    private RemoteDictService remoteDictService;
    @Resource
    private ITaxicOiInvoiceMiddleService iTaxicOiInvoiceMiddleService;
    @Resource
    private RuleFacade ruleFacade;
    @Resource
    private VerificationFacade verificationFacade;
    @Resource
    private FieldMappingFacade fieldMappingFacade;

    @Resource
    private IDataExecutionTaskService dataExecutionTaskService;

    @Resource
    private ITaxicIiInvoicePaperService taxicIiInvoicePaperService;

    @Resource
    private ITaxicIiInvoiceElecService taxicIiInvoiceElecService;

    @Override
    public Boolean syncInvoicingSystem(InvoiceClaimQueryDTO queryDTO) {
        //1.查询是否有正在执行的任务
        String systemCode = ExecutionTaskSystemEnum.KPRL_DATA.getCode();
        DataExecutionTaskDTO taskDTO = dataExecutionTaskService.getRunningTaskBySystem(systemCode);
        if (taskDTO != null) {
            log.info("存在正在执行的任务，本次任务跳过, systemCode:{}", systemCode);
            return Boolean.TRUE;
        }
        Date queryDate = new Date();
        if (ObjectUtil.isNotNull(queryDTO.getDocumentDate())) {
            queryDate = queryDTO.getDocumentDate();
        }
        List<InvoiceClaimEntity> invoiceClaimEntityList = Lists.newArrayList();
        //获取映射配置规则
        R<List<SysDictData>> sysDictDataResult = remoteDictService.listDictData(CommonConstant.INVOICE_CLAIM_DICT_TYPE);
        if (null == sysDictDataResult || CollectionUtils.isEmpty(sysDictDataResult.getData())) {
            throw new RuntimeException("配置文件不存在请检查");
        }
        //获取签约实体字典配置
        R<List<SysDictData>> orgDictDataResult = remoteDictService.listDictData(CommonConstant.INVOICE_CLAIM_ORG_ID_DICT_TYPE);
        if (null == orgDictDataResult || CollectionUtils.isEmpty(orgDictDataResult.getData())) {
            throw new RuntimeException("签约实体配置文件不存在请检查");
        }
        Map<String,SysDictData> orgIdMap = orgDictDataResult.getData().stream().collect(Collectors.toMap(SysDictData::getDictLabel,v->v, (k1,k2) -> k1));
        //获取字典标签配置最大的那一条数据
        Optional<SysDictData> sysDictDataOptional = sysDictDataResult.getData().stream().max(Comparator.comparing(SysDictData::getDictValue));
        SysDictData sysDictData = sysDictDataOptional.get();
        //字典值转换为map
        Map<String,String> dictMap = JSON.parseObject(sysDictData.getDictLabel(),LinkedHashMap.class);
        //特殊处理，获取金融/融资租赁大类下的开票内容
        List<String> invoiceCotentList = Lists.newArrayList();
        List<String> mainCategoryList = Lists.newArrayList();
        for (Map.Entry<String, String> entry : dictMap.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getKey()) && entry.getKey().contains(CommonConstant.INCOICE_CLAIM_DICT_FINANCIAL_LEASE)) {
                String[] array= entry.getKey().split("_");
                String mainCategory = StringUtils.isNotEmpty(array[0]) ? array[0] : "";
                String invoiceCotent = StringUtils.isNotEmpty(array[1]) ? array[1] : "";
                invoiceCotentList.add(invoiceCotent);
                mainCategoryList.add(mainCategory);
            }
        }
//         dictMap.get(CommonConstant.INCOICE_CLAIM_DICT_FINANCIAL_LEASE);
        //获取映射表
        FieldMappingApiDTO fieldMappingApiDTO = new FieldMappingApiDTO();
        fieldMappingApiDTO.setSystemCode("SYCXT");
        fieldMappingApiDTO.setFieldCode("contractStatus");
        R<List<FieldMappingApiVO>> fieldMapping = fieldMappingFacade.selectFieldMappingByCondition(fieldMappingApiDTO);
        if (null == fieldMapping) {
            throw new ServiceException("调用映射表接口fieldMappingFacade.selectFieldMappingByCondition失败");
        }
        Map<String,String> fieldMap = fieldMapping.getData().stream().collect(Collectors.toMap(FieldMappingApiVO::getSourceValue,FieldMappingApiVO::getTargetValue, (k1,k2)->k2));
        //读取纸质，电子发票数据
        TaxicOiInvoiceMiddleQueryDTO taxicOiInvoiceMiddleQueryDTO = new TaxicOiInvoiceMiddleQueryDTO();
        taxicOiInvoiceMiddleQueryDTO.setDanjrq(DateUtil.toLocalDateTime(queryDate));

        taxicOiInvoiceMiddleQueryDTO.setQueryDate(LocalDateTimeUtil.format(LocalDateTimeUtil.of(queryDate), "yyyy-MM-dd"));
//        taxicOiInvoiceMiddleQueryDTO.setChuangjrq(DateUtil.toLocalDateTime(queryDate));
        log.info("查询发票中间系统纸质，电子发票参数：{}",JSON.toJSONString(taxicOiInvoiceMiddleQueryDTO));
        List<InvoiceClaimVO> taxicOiInvoiceMiddleVOList = iTaxicOiInvoiceMiddleService.selectByCondition(taxicOiInvoiceMiddleQueryDTO);
        log.info("查询发票中间系统纸质，电子发票返回条数：{}",taxicOiInvoiceMiddleVOList.size());

        log.info("查询发票系统纸质发票参数：{}",JSON.toJSONString(taxicOiInvoiceMiddleQueryDTO));
        List<InvoiceClaimVO> taxicOiInvoicePaperVOList = taxicIiInvoicePaperService.selectByCondition(taxicOiInvoiceMiddleQueryDTO);
        log.info("查询发票系统纸质发票返回条数：{}",taxicOiInvoicePaperVOList.size());

        log.info("查询发票系统电子发票参数：{}",JSON.toJSONString(taxicOiInvoiceMiddleQueryDTO));
        List<InvoiceClaimVO> taxicOiInvoiceElecVOList = taxicIiInvoiceElecService.selectByCondition(taxicOiInvoiceMiddleQueryDTO);
        log.info("查询发票系统电子发票返回条数：{}",taxicOiInvoiceElecVOList.size());

        if(CollectionUtil.isNotEmpty(taxicOiInvoicePaperVOList)){
            taxicOiInvoiceMiddleVOList.addAll(taxicOiInvoicePaperVOList);
        }
        if(CollectionUtil.isNotEmpty(taxicOiInvoiceElecVOList)){
            taxicOiInvoiceMiddleVOList.addAll(taxicOiInvoiceElecVOList);
        }

        List<InvoiceClaimEntity>  invoiceClaimEntities = invoiceClaimService.list();
        List<String> documentNoControctNoList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(taxicOiInvoiceMiddleVOList)) {
            return Boolean.TRUE;
        }
        log.info("查询到未执行的业务数据, systemCode:{}, dataSize:{}", systemCode, taxicOiInvoiceMiddleVOList.size());
        Long taskId = dataExecutionTaskService.createNewTask(systemCode, DataExecutionTaskStatusEnum.RUNNING.getCode(), taxicOiInvoiceMiddleVOList.size(), CollectionUtil.getFirst(taxicOiInvoiceMiddleVOList).getDocumentDate(), CollectionUtil.getLast(taxicOiInvoiceMiddleVOList).getDocumentDate());
        //单据号+合同号+期数
        if (CollectionUtils.isNotEmpty(invoiceClaimEntities)) {
            documentNoControctNoList = invoiceClaimEntities.stream().map(v->v.getSystemInvoiceId() + "_" + v.getSourceFrom()).collect(Collectors.toList());
        }
        for (InvoiceClaimVO v : taxicOiInvoiceMiddleVOList) {
            String key =  v.getSystemInvoiceId() + "_" + v.getSourceFrom();
            //过滤掉已经入库的数据
            if (documentNoControctNoList.contains(key)) {
                continue;
            }
            InvoiceClaimEntity invoiceClaimEntity = new InvoiceClaimEntity();
            String systemMainCategory = StringUtils.isNotEmpty(v.getMainCategory()) ? v.getMainCategory() : "" ;
            String productName = StringUtils.isNotEmpty(v.getProductName()) ? v.getProductName() : "" ;

            if (StringUtils.isEmpty(systemMainCategory) && StringUtils.isNotEmpty(v.getContractCode())) {
                v.setIsAutoGeneration("1");
                v.setAccountName("租金");
            } else {
                for (Map.Entry<String, String> entry : dictMap.entrySet()) {
                    //拆分key值 大类”_“开票内容 存在的自动自动生成凭证，否则手动生成
                    String[] array = entry.getKey().split("_");
                    String mainCategory = StringUtils.isEmpty(array[0]) || "null".equals(array[0]) ? "" : array[0];
                    String invoiceCotent = StringUtils.isEmpty(array[1]) || "null".equals(array[1]) ? "" : array[1];
                    //按照”/“拆分大类
                    Boolean isBreak = Boolean.FALSE;
                    if (StringUtils.isNotEmpty(mainCategory)) {
                        String[] mainArray = mainCategory.split("/");
                        for (String s : mainArray) {
                            if (systemMainCategory.contains(s) && invoiceCotent.contains(productName)) {
                                v.setAccountName(entry.getValue());
                                v.setMappingCategory(s);
                                v.setIsAutoGeneration("1");
                                isBreak = Boolean.TRUE;
                                break;
                            } else if ("金融服务/融资租赁".equals(mainCategory)
                                    && systemMainCategory.contains(s)
                                    && !invoiceCotentList.contains(productName)
                                    && StringUtils.isEmpty(invoiceCotent)) {
                                v.setAccountName(entry.getValue());
                                v.setMappingCategory(s);
                                v.setIsAutoGeneration("1");
                                isBreak = Boolean.TRUE;
                                break;
                            }
                        }
                        if (isBreak) {
                            break;
                        }
                    } else if (StringUtils.isEmpty(systemMainCategory) && invoiceCotent.contains(productName)) {
                        v.setAccountName(entry.getValue());
                        v.setIsAutoGeneration("1");
                        break;
                    }
                }

                if (StringUtils.isEmpty(v.getAccountName()) && StringUtils.isNotEmpty(v.getContractCode())) {
                    v.setIsAutoGeneration("1");
                    v.setAccountName("租金");
                }
            }

//            if (StringUtils.isNotEmpty(systemMainCategory) && mainCategoryList.contains(systemMainCategory)) {
//                boolean findFlag = false;
//                for (Map.Entry<String, String> entry : dictMap.entrySet()) {
//                    if(findFlag){
//                        break;
//                    }
//                    //拆分key值 大类”_“开票内容 存在的自动自动生成凭证，否则手动生成
//                    String[] array = entry.getKey().split("_");
//                    String mainCategory = StringUtils.isEmpty(array[0]) || "null".equals(array[0]) ? "" : array[0];
//                    String invoiceCotent = StringUtils.isEmpty(array[1]) || "null".equals(array[1]) ? "" : array[1];
//                    List<String> invoiceContentList = Arrays.asList(invoiceCotent.split("/"));
//
//                    //按照”/“拆分大类
//                    if (StringUtils.isNotEmpty(mainCategory)) {
//                        String[] mainArray = mainCategory.split("/");
//                        for (String s : mainArray) {
//                            if (systemMainCategory.contains(s) && invoiceContentList.contains(productName)) {
//                                v.setAccountName(entry.getValue());
//                                v.setMappingCategory(s);
//                                v.setIsAutoGeneration("1");
//                                findFlag = true;
//                                break;
//                            } else if (CommonConstant.INCOICE_CLAIM_DICT_FINANCIAL_LEASE.equals(mainCategory)
//                                    && systemMainCategory.contains(s)
//                                    && !invoiceCotentList.contains(productName)
//                                    && invoiceContentList.size()==1
//                                    && StringUtils.isEmpty(invoiceContentList.get(0))) {
//                                v.setAccountName(entry.getValue());
//                                v.setMappingCategory(s);
//                                v.setIsAutoGeneration("1");
//                                findFlag = true;
//                                break;
//                            }
//                        }
//                    } else if (StringUtils.isEmpty(systemMainCategory) && invoiceContentList.contains(productName)) {
//                        v.setAccountName(entry.getValue());
//                        v.setIsAutoGeneration("1");
//                        findFlag = true;
//                        break;
//                    }
//                }
//            } else if (StringUtils.isNotEmpty(v.getContractCode())) {
//                v.setIsAutoGeneration("1");
//                v.setAccountName("租金");
//            }

            //发票来源为纸质和发票系统纸质发票的地址和电话，银行地址和账户需要拆分
            if ("0".equals(v.getSourceFrom())) {
                String address = v.getPurchaserDress();
                String tel = "";
                if (StringUtils.isNotEmpty(v.getPurchaserDress()) && v.getPurchaserDress().length()>13) {
                    address = v.getPurchaserDress().substring(0,v.getPurchaserDress().length()-13);
                    tel = v.getPurchaserDress().substring(v.getPurchaserDress().length()-13);
                }
                v.setPurchaserDress(address);
                v.setPurchaserTel(tel);
            }
            for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
                if (StringUtils.isNotEmpty(v.getContractStatus()) && entry.getKey().contains(v.getContractStatus())) {
                    v.setContractStatus(entry.getValue());
                    break;
                }
            }
            BeanUtil.copyProperties(v, invoiceClaimEntity);
            if (orgIdMap.containsKey(v.getEnterpriseNum())) {
                invoiceClaimEntity.setOrgId(orgIdMap.get(v.getEnterpriseNum()).getDictValue());
                invoiceClaimEntity.setSellerName(orgIdMap.get(v.getEnterpriseNum()).getRemark());
            }
            //读取自动生成凭证的数据，调用凭证接口生成凭证
            invoiceClaimEntityList.add(invoiceClaimEntity);
        };
        log.info("保存开票认领数据数量："+invoiceClaimEntityList.size());
        if (CollectionUtils.isNotEmpty(invoiceClaimEntityList)) {
            invoiceClaimService.saveBatch(invoiceClaimEntityList);
        }
        dataExecutionTaskService.finishedTask(taskId, DataExecutionTaskStatusEnum.SUCCESS.getCode(), taxicOiInvoiceMiddleVOList.size(), 0);
        log.info("批量执行保存开票数据, 结束. systemCode:{}", systemCode);
        return Boolean.TRUE;
    }

    public static void main(String[] args) {
        String[] array = "金融服务/融资租赁_null".split("_");
        String mainCategory = StringUtils.isEmpty(array[0]) || "null".equals(array[0]) ? "" : array[0];
        String invoiceCotent = StringUtils.isEmpty(array[1]) || "null".equals(array[1]) ? "" : array[1];
        List<String> invoiceContentList = Arrays.asList(invoiceCotent.split("/"));
        System.out.println(invoiceContentList.size()+"______"+invoiceContentList.get(0));
    }

    @Override
    public Boolean invoiceGenerateVoucher() {
        //1.查询是否有正在执行的任务
        String systemCode = ExecutionTaskSystemEnum.KPRL_VOUCHER.getCode();
        DataExecutionTaskDTO taskDTO = dataExecutionTaskService.getRunningTaskBySystem(systemCode);
        if (taskDTO != null) {
            log.info("存在正在执行的任务，本次任务跳过, systemCode:{}", systemCode);
            return Boolean.TRUE;
        }
         List<CourtCostVerificationDTO> verificationDTOList = Lists.newArrayList();
         //读取自动生成凭证的数据，调用凭证接口生成凭证
         List<InvoiceClaimEntity> vourcheClaimList = invoiceClaimService.list(Wrappers.<InvoiceClaimEntity>lambdaQuery().eq(InvoiceClaimEntity::getIsAutoGeneration, "1").isNull(InvoiceClaimEntity::getVoucherId).orderByAsc(InvoiceClaimEntity::getDocumentDate)
                .orderByAsc(InvoiceClaimEntity::getCreateTime)
                .last("limit 1000"));
         if (CollectionUtils.isEmpty(vourcheClaimList)) {
             return Boolean.TRUE;
         }
         AtomicInteger successCount = new AtomicInteger();
         AtomicInteger failedCount = new AtomicInteger();
        Long taskId = dataExecutionTaskService.createNewTask(systemCode, DataExecutionTaskStatusEnum.RUNNING.getCode(), vourcheClaimList.size(), CollectionUtil.getFirst(vourcheClaimList).getDocumentDate(), CollectionUtil.getLast(vourcheClaimList).getDocumentDate());
        if (CollectionUtils.isNotEmpty(vourcheClaimList)) {
             vourcheClaimList.stream().forEach(v -> {
                 CourtCostVerificationDTO costVerificationDTO = new CourtCostVerificationDTO();
                 costVerificationDTO.setContractCode(v.getContractCode());
                 costVerificationDTO.setDocumentDate(DateUtil.date(v.getDocumentDate()));
                 Map<String, Object> ruleMap = BeanUtil.beanToMap(v);
                 ruleMap.put("systemCode", "FINHUB");
                 ruleMap.put("systemName", "财务中台");
                 ruleMap.put("businessCode", "ZLYW");
                 ruleMap.put("businessName", "租赁");
                 ruleMap.put("sceneCode", "KJFP");
                 ruleMap.put("sceneName", "开票");
                 ruleMap.put("businessDate", v.getDocumentDate());
                 ruleMap.put("voucherDate", v.getDocumentDate());
                 ruleMap.put("batchId", v.getId().toString());
                 ruleMap.put("batchType", "KJFP");
                 ruleMap.put("orgId", v.getOrgId());
                 try {
                     log.info("调用凭证接口ruleFacade.executeRule请求参数:{}", JSON.toJSONString(ruleMap));
                     R<List<VoucherVO>> voucherResult = ruleFacade.executeRule(ruleMap);
                     if (null == voucherResult || 200!=voucherResult.getCode()) {
                         throw new ServiceException("调用生成凭证接口失败，失败原因：" + (null == voucherResult ? "" : voucherResult.getMsg()));
                     }
                     log.info("调用凭证接口ruleFacade.executeRule返回值:{}", JSON.toJSONString(voucherResult));
                     if (CollectionUtils.isNotEmpty(voucherResult.getData())) {
                         v.setVoucherId(voucherResult.getData().get(0).getId());
                         v.setErrorMessage("");
                         successCount.getAndIncrement();
                     } else {
                         v.setVoucherId(0L);
                     }
                     verificationDTOList.add(costVerificationDTO);
                 } catch (Exception e) {
                     log.info("生成凭证失败原因：" + e.getMessage());
                     v.setErrorMessage(e.getMessage());
                     failedCount.getAndIncrement();
                 }
                 invoiceClaimService.updateById(v);
             });
         }
        dataExecutionTaskService.finishedTask(taskId, DataExecutionTaskStatusEnum.SUCCESS.getCode(), successCount.get(), failedCount.get());
        if (CollectionUtils.isNotEmpty(verificationDTOList)) {
            updateContractAmount(verificationDTOList);
        }
        return Boolean.TRUE;
     }

     @Async
    @Override
    public Boolean syncInvoicingSystemByDay(InvoiceClaimQueryDTO queryDTO) {
        Date startDate = queryDTO.getStartDocumentDate();
        Date endDate = queryDTO.getEndDocumentDate();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        long diffInMillies = Math.abs(endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
        int daysBetween = (int)(diffInMillies / (24 * 60 * 60 * 1000));
        log.info("相差天数：{}",daysBetween);
        while (startCalendar.getTime().compareTo(endCalendar.getTime())<0){
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            queryDTO.setDocumentDate(startCalendar.getTime());
            log.info("同步开票数据开始时间>>>>>>：{}",queryDTO.getDocumentDate());
            syncInvoicingSystem(queryDTO);
            log.info("同步开票数据结束时间>>>>>>：{}",queryDTO.getDocumentDate());
        }
        log.info("同步数据开始时间>>>>>>>>：{}，结束时间>>>>>>：{} 数据同步完成",startDate,endDate);
        return Boolean.TRUE;
    }

    public void updateContractAmount(List<CourtCostVerificationDTO> verificationDTOList){
        CompletableFuture.runAsync(() -> {
            verificationFacade.updateContractAmount(verificationDTOList);
        });

    }

}
