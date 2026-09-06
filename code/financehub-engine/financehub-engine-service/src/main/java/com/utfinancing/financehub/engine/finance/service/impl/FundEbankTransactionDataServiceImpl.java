package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.FundEbankTransactionDataVO;
import com.utfinancing.financehub.engine.finance.mapper.FundEbankTransactionDataMapper;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.rule.model.dto.DataExecutionTaskDTO;
import com.utfinancing.financehub.engine.rule.service.IDataExecutionTaskService;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-17
 * @Description :  FundEbankTransactionData服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class FundEbankTransactionDataServiceImpl extends ServiceImpl<FundEbankTransactionDataMapper,
        FundEbankTransactionDataEntity> implements IFundEbankTransactionDataService {

    private final FundEbankTransactionDataMapper fundEbankTransactionDataMapper;

    @Resource
    @Lazy
    private IRuleService iRuleService;

    private final IFundBusinessSystemEbankMappingService iFundBusinessSystemEbankMappingService;

    private final IFundBusinessSystemEbankWyAmountService iFundBusinessSystemEbankWyAmountService;

    private final IHyFullOnlineBankBatchNoMappingService iHyFullOnlineBankBatchNoMappingService;

    private final IDataExecutionTaskService dataExecutionTaskService;

    @Override
    public Long saveFundEbankTransactionData(FundEbankTransactionDataDTO dto) {
        FundEbankTransactionDataEntity entity = BeanUtil.copyProperties(dto, FundEbankTransactionDataEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public void saveRawData(JSONArray jsonArray) {
        List<FundEbankTransactionDataEntity> entities = new ArrayList<>();
        jsonArray.forEach(jsonObject -> {
            FundEbankTransactionDataEntity entity = BeanUtil.copyProperties(jsonObject, FundEbankTransactionDataEntity.class);
            entity.setCollectionType(CollectionTypeEnum.getDescByCode(entity.getCollectionType()));
            if (StringUtils.isNotEmpty(entity.getCurrencyType())) {
                entity.setCurrencyType(FundCurrencyTypeEnum.getEnumByType(entity.getCurrencyType()).getCode());
            }
            entities.add(entity);
        });
        this.saveBatch(entities);
        //生成凭证
        //this.ebankTransactionGenerateVoucher(entities);
    }

    @Override
    public Long updateFundEbankTransactionData(Long id, FundEbankTransactionDataDTO dto) {
        FundEbankTransactionDataEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public FundEbankTransactionDataDTO getFundEbankTransactionDataDTOById(Long id) {
        FundEbankTransactionDataEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, FundEbankTransactionDataDTO.class);
    }

    @Override
    public IPage<FundEbankTransactionDataVO> selectPage(FundEbankTransactionDataQueryDTO queryDTO) {
        LambdaQueryWrapper<FundEbankTransactionDataEntity> queryWrapper = Wrappers.<FundEbankTransactionDataEntity>lambdaQuery();
        //这里注入查询条件
        IPage<FundEbankTransactionDataEntity> entityIPage = fundEbankTransactionDataMapper.selectPage(new Page<FundEbankTransactionDataEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, FundEbankTransactionDataVO.class);
    }

    @Override
    public void ebankTransactionGenerateVoucher(List<FundEbankTransactionDataEntity> ebankTransactionDataEntityList) {
        Long startTime = System.currentTimeMillis();
        log.info("资金系统收付款生成凭证-FundBusinessSystemEbankMappingEntity查询Start：" + startTime);
        //查询网银映射表
        List<FundBusinessSystemEbankMappingEntity> ebankMappingEntityList = iFundBusinessSystemEbankMappingService.lambdaQuery().list();
        Map<String,List<FundBusinessSystemEbankMappingEntity>> ebankMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(ebankMappingEntityList)) {
            ebankMap = ebankMappingEntityList.stream().collect(Collectors.groupingBy(FundBusinessSystemEbankMappingEntity::getEbankNumber));
        }
        log.info("资金系统收付款生成凭证-FundBusinessSystemEbankMappingEntity查询End：" + (System.currentTimeMillis() - startTime));

        for (FundEbankTransactionDataEntity v : ebankTransactionDataEntityList) {
            if (("collection".equals(v.getTransactionType()) && StringUtils.isEmpty(v.getCollectionAccountsBankNo()))
                    || ("payment".equals(v.getTransactionType()) && StringUtils.isEmpty(v.getPaymentAccountsBankNo()))) {
                continue;
            }
            try {
                FundSystemVoucherDTO fundSystemVoucherDTO = BeanUtil.copyProperties(v, FundSystemVoucherDTO.class);
                fundSystemVoucherDTO.setBusinessDate(StringUtils.isNotEmpty(v.getBusinessDate()) ? CommonDateUtils.parseDateStringToDate(v.getBusinessDate()) : new Date());
                SceneEnum sceneEnum = null;
                //收款
                if ("collection".equals(v.getTransactionType())) {
                    sceneEnum = SceneEnum.WYLSSK;
                } else {
                    //付款
                    sceneEnum = SceneEnum.WYLSFK;
                    fundSystemVoucherDTO.setPaymentMethod("银企");
                    fundSystemVoucherDTO.setPaymentAccountsBankNo(v.getPaymentAccountsBankNo());
                }
                fundSystemVoucherDTO.setSceneCode(sceneEnum.getCode());
                fundSystemVoucherDTO.setSceneName(sceneEnum.getDesc());
                fundSystemVoucherDTO.setSystemCode(SystemEnum.ZJXT.getCode());
                fundSystemVoucherDTO.setSystemName(SystemEnum.ZJXT.getDesc());
                fundSystemVoucherDTO.setBusinessCode("ZLYW");
                fundSystemVoucherDTO.setBusinessName("租赁");
                fundSystemVoucherDTO.setBatchId(v.getId());
                fundSystemVoucherDTO.setBatchType(BatchTypeEnum.WYLSSK.getCode());
                fundSystemVoucherDTO.setInterfaceCreateTime(v.getCreateTime());
                fundSystemVoucherDTO.setCollectionType(v.getCollectionType());
                //获取网银编号
                fundSystemVoucherDTO.setEbankSerialNumber(getEbankSerialNumber(ebankMap,fundSystemVoucherDTO.getEbankNumber()));
                Map<String, Object> dataMap = BeanUtil.beanToMap(fundSystemVoucherDTO);
                dataMap.put("interfaceId", v.getId());
                log.info("生成凭证参数：{}", JSON.toJSONString(dataMap));

                startTime = System.currentTimeMillis();
                log.info("资金系统收付款生成凭证-会计引擎调用Start：" + startTime);
                iRuleService.executeRule(dataMap);
                log.info("资金系统收付款生成凭证-会计引擎调用End：" + (System.currentTimeMillis() - startTime));
                v.setIsGenerateVoucher("1");
            } catch (Exception e) {
                log.error("付款生成凭证id:{},异常，异常原因：{}", v.getId(),e.getMessage());
                v.setIsGenerateVoucher("2");
            }

            startTime = System.currentTimeMillis();
            log.info("资金系统收付款生成凭证-状态更新Start：" + startTime);
            this.updateById(v);
            log.info("资金系统收付款生成凭证-状态更新End：" + (System.currentTimeMillis() - startTime));
        }
    }

    @Override
    public Boolean transactionGenerateVoucher(FundEbankTransactionDataQueryDTO queryDTO) {
        Long startTime = System.currentTimeMillis();
        log.info("资金系统收付款生成凭证-FundEbankTransactionDataEntity查询Start：" + startTime);
        QueryWrapper<FundEbankTransactionDataEntity> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(queryDTO.getId())) {
            queryWrapper.lambda().eq(FundEbankTransactionDataEntity::getId,queryDTO.getId());
        }
        if (ObjectUtil.isNotNull(queryDTO.getBusinessDate())) {
            queryWrapper.apply("to_date(operation_date,'YYYY-MM-DD')={0}",queryDTO.getBusinessDate());
        }
//        queryWrapper.lambda().isNotNull(FundEbankTransactionDataEntity::getCollectionAccountsBankNo);
        queryWrapper.lambda().eq(FundEbankTransactionDataEntity::getIsGenerateVoucher,"0");
        List<FundEbankTransactionDataEntity> dataEntityList = this.list(queryWrapper);
        log.info("资金系统收付款生成凭证-FundEbankTransactionDataEntity查询End：" + (System.currentTimeMillis() - startTime));
        ebankTransactionGenerateVoucher(dataEntityList);
        return Boolean.TRUE;
    }

    @Override
    public Boolean testGenerateVoucherByDay(FundEbankTransactionDataQueryDTO queryDTO) {
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
            String date = DateUtil.format(startCalendar.getTime(),"yyyy-MM-dd");
            queryDTO.setBusinessDate(date);
            log.info("同步收付款金额数据开始时间>>>>>>：{}",queryDTO.getBusinessDate());
            transactionGenerateVoucher(queryDTO);
            log.info("同步收付款金额数据结束时间>>>>>>：{}",queryDTO.getBusinessDate());
        }
        log.info("同步收付款金额数据开始时间>>>>>>>>：{}，结束时间>>>>>>：{} 数据同步完成",startDate,endDate);
        return Boolean.TRUE;
    }

    /**
     * 根据条件查询资金系统收款数据
     */
    public List<FundEbankTransactionDataEntity> selectFundEbankTransactionDataByCon(
            SelectFundEbankTransactionDataByConDTO params) {
        LambdaQueryWrapper<FundEbankTransactionDataEntity> wrapper = new LambdaQueryWrapper();
        if (params.getEbankNumberList() != null && !params.getEbankNumberList().isEmpty()) {
            wrapper.in(FundEbankTransactionDataEntity::getEbankNumber, params.getEbankNumberList());
        }
        wrapper.eq(FundEbankTransactionDataEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        return fundEbankTransactionDataMapper.selectList(wrapper);
    }

    public String getEbankSerialNumber(Map<String,List<FundBusinessSystemEbankMappingEntity>> ebankMap,String ebankNumber) {
       String ebankSerialNumber = "";
        if (StringUtils.isEmpty(ebankNumber) || ebankMap.isEmpty()) {
            return ebankSerialNumber;
        }
        for (Map.Entry<String, List<FundBusinessSystemEbankMappingEntity>> entry : ebankMap.entrySet()) {
            String[] keyList = entry.getKey().split(",");
            boolean isExistFlag = Boolean.FALSE;
            for (String key : keyList) {
                if (key.equals(ebankNumber)) {
                    isExistFlag = Boolean.TRUE;
                    break;
                }
            }
            if (isExistFlag) {
                ebankSerialNumber = entry.getValue().get(0).getEbankSerialNumber();
                break;
            }
        }
        return ebankSerialNumber;
    }


    @Async
    @Override
    public void generateMappingVoucher() {
        //1.查询是否有正在执行的任务
        String systemCode = "EBANK_HY_MAPPING";
        DataExecutionTaskDTO taskDTO = dataExecutionTaskService.getRunningTaskBySystem(systemCode);
        if (taskDTO != null) {
            log.info("存在正在执行的任务，本次任务跳过, systemCode:{}", systemCode);
            return;
        }
        //查询资金金额映射表数据 一次查询一千
        List<FundBusinessSystemEbankMappingEntity> mappingEntityList = iFundBusinessSystemEbankMappingService.lambdaQuery()
                .in(FundBusinessSystemEbankMappingEntity::getMessageStatus,RawMessageStatusEnum.NOT_EXECUTE.getCode(),
                        RawMessageStatusEnum.FAILED.getCode())
                .orderByAsc(FundBusinessSystemEbankMappingEntity::getId)
                .last("limit 1000").list();
        if (CollectionUtils.isEmpty(mappingEntityList)) {
            log.info("资金金额映射表没有数据需要生成凭证");
            return;
        }
        Long taskId = dataExecutionTaskService.createNewTask(systemCode, DataExecutionTaskStatusEnum.RUNNING.getCode(),
                mappingEntityList.size(), CollectionUtil.getFirst(mappingEntityList).getCreateTime(), CollectionUtil.getLast(mappingEntityList).getCreateTime());
        int successCount = 0;
        int failedCount = 0;
        double totalSize = mappingEntityList.size();
        int i=0;
        try {
        //批量查询
        for (FundBusinessSystemEbankMappingEntity entity : mappingEntityList) {
            if (org.apache.commons.lang3.StringUtils.isEmpty(entity.getEbankNumber())) {
                log.info("id:{},网银编号是空的，过滤掉",entity.getId());
                i++;
                log.info("eg_fund_business_system_ebank_mapping表生成凭证进度：{}", NumberUtil.formatPercent(i/totalSize, 2));
                continue;
            }
            //查询网银信息
            List<String> ebankNumberList = Arrays.asList(entity.getEbankNumber().split(","));
            List<FundBusinessSystemEbankWyAmountEntity> wyAmountList = iFundBusinessSystemEbankWyAmountService.
                    lambdaQuery().in(FundBusinessSystemEbankWyAmountEntity::getEbankNumber,ebankNumberList).
                    orderByDesc(FundBusinessSystemEbankWyAmountEntity::getMatchNumber, FundBusinessSystemEbankWyAmountEntity::getBusinessDate).list();
            //查询批扣信息
            List<String> batchNoList = Arrays.asList(entity.getEbankSerialNumber().split(","));
            List<HyFullOnlineBankBatchNoMappingEntity> batchMappingList = iHyFullOnlineBankBatchNoMappingService.lambdaQuery().in(HyFullOnlineBankBatchNoMappingEntity::getOnlineBankNo,batchNoList).list();
            if (CollectionUtils.isEmpty(wyAmountList) || CollectionUtils.isEmpty(batchMappingList)) {
                log.info("网银信息Size：{},批扣信息Size:{},存在空值，过滤掉",wyAmountList.size(),batchMappingList.size());
                i++;
                log.info("eg_fund_business_system_ebank_mapping表生成凭证进度：{}", NumberUtil.formatPercent(i/totalSize, 2));
                continue;
            }
            FundSystemVoucherDTO voucherEntity = BeanUtil.copyProperties(entity, FundSystemVoucherDTO.class);
            voucherEntity.setTransactionType("collection");
            SceneEnum sceneEnum = SceneEnum.WYLSSK;
            voucherEntity.setSceneCode(sceneEnum.getCode());
            voucherEntity.setSceneName(sceneEnum.getDesc());
            voucherEntity.setSystemCode(SystemEnum.ZJXT.getCode());
            voucherEntity.setSystemName(SystemEnum.ZJXT.getDesc());
            voucherEntity.setBusinessCode(BusinessEnum.ZLYW.getCode());
            voucherEntity.setBusinessName(BusinessEnum.ZLYW.getDesc());
            voucherEntity.setBatchId(entity.getId());
            voucherEntity.setBatchType(BatchTypeEnum.WYHYJE.getCode());
            voucherEntity.setInterfaceCreateTime(entity.getCreateTime());
            voucherEntity.setBusinessDate(CommonDateUtils.parseLocalDateTimeToDate(entity.getCreateTime()));
            voucherEntity.setOrderId(entity.getId().toString());
            voucherEntity.setInterfaceId(entity.getId());
            voucherEntity.setCurrencyType(CurrencyTypeEnum.CNY.getDesc());
            //生成网银凭证
            boolean isExistError = Boolean.FALSE;
            StringBuffer errorStringBuffer = new StringBuffer();
            String matchNumber = wyAmountList.get(0).getMatchNumber();
            String collectionAccountsBank = wyAmountList.get(0).getCollectionAccountsBank();
            String collectionAccountsBankNo = wyAmountList.get(0).getCollectionAccountsBankNo();

            Map<String, Date> businessDateMap = new HashMap<>();
            for (FundBusinessSystemEbankWyAmountEntity wyAmountEntity : wyAmountList) {
                if (isExistError) {
                    log.info("批次MatchNumber:{},银行编号：{},存在错误不需要再次执行",wyAmountEntity.getMatchNumber(),wyAmountEntity.getEbankNumber());
                    break;
                }
                log.info("批次MatchNumber:{},银行编号：{}",wyAmountEntity.getMatchNumber(),wyAmountEntity.getEbankNumber());
                voucherEntity.setCollectionAccountsBank(wyAmountEntity.getCollectionAccountsBank());
                voucherEntity.setCollectionAccountsBankNo(wyAmountEntity.getCollectionAccountsBankNo());
                voucherEntity.setEbankNumber(wyAmountEntity.getEbankNumber());
                if (BigDecimal.ZERO.compareTo(entity.getMatchAmount()) > 0) {
                    voucherEntity.setBankAmount(new BigDecimal(-1).multiply(wyAmountEntity.getWyAmount()));
                } else {
                    voucherEntity.setBankAmount(wyAmountEntity.getWyAmount());
                }

                // 若同一个match_number存在多个business_date，取最大的business_date
                if (businessDateMap.get(wyAmountEntity.getMatchNumber()) == null) {
                    businessDateMap.put(wyAmountEntity.getMatchNumber(), wyAmountEntity.getBusinessDate());
                }
                voucherEntity.setBusinessDate(businessDateMap.get(wyAmountEntity.getMatchNumber()));
                Map<String, Object> dataMap = BeanUtil.beanToMap(voucherEntity);
                log.info("网银生成凭证参数：{}", com.alibaba.fastjson.JSON.toJSONString(dataMap));
                isExistError = generateVoucher(dataMap,errorStringBuffer);
            }
            //生成hy凭证
            for (HyFullOnlineBankBatchNoMappingEntity hyFullOnlineBankBatchNoMappingEntity : batchMappingList) {
                if (isExistError) {
                    log.info("批次MatchNumber:{},onlineBankNo编号：{},存在错误不需要再次执行",matchNumber,hyFullOnlineBankBatchNoMappingEntity.getOnlineBankNo());
                    break;
                }
                log.info("批次MatchNumber:{},onlineBankNo编号：{}",matchNumber,hyFullOnlineBankBatchNoMappingEntity.getOnlineBankNo());
                voucherEntity.setClientCode(hyFullOnlineBankBatchNoMappingEntity.getClientCode());
                voucherEntity.setClientName(hyFullOnlineBankBatchNoMappingEntity.getClientName());
                if (BigDecimal.ZERO.compareTo(entity.getMatchAmount()) > 0) {
                    voucherEntity.setClaimAmount(new BigDecimal(-1).multiply(hyFullOnlineBankBatchNoMappingEntity.getCollectAmount()));
                } else {
                    voucherEntity.setClaimAmount(hyFullOnlineBankBatchNoMappingEntity.getCollectAmount());
                }
                voucherEntity.setEbankBatchNo(hyFullOnlineBankBatchNoMappingEntity.getDeductBatchNo());
                voucherEntity.setEbankSerialNumber(hyFullOnlineBankBatchNoMappingEntity.getOnlineBankNo());
                voucherEntity.setCollectionAccountsBank(collectionAccountsBank);
                voucherEntity.setCollectionAccountsBankNo(collectionAccountsBankNo);
                voucherEntity.setEbankNumber("");
                voucherEntity.setBankAmount(BigDecimal.ZERO);
                Map<String, Object> dataMap = BeanUtil.beanToMap(voucherEntity);
                log.info("恒运生成凭证参数：{}", com.alibaba.fastjson.JSON.toJSONString(dataMap));
                isExistError = generateVoucher(dataMap,errorStringBuffer);
            }
            log.info("matchNumber:{}，是否存在错误",isExistError);
            String messageStatus = RawMessageStatusEnum.SUCCESS.getCode();
            if (isExistError) {
                messageStatus = RawMessageStatusEnum.FAILED.getCode();
                failedCount++;
            } else {
                successCount++;
            }
            iFundBusinessSystemEbankMappingService.lambdaUpdate().set(FundBusinessSystemEbankMappingEntity::getMessageStatus,messageStatus)
                    .set(errorStringBuffer.length()>0,FundBusinessSystemEbankMappingEntity::getErrorInfo,errorStringBuffer.toString())
                    .eq(FundBusinessSystemEbankMappingEntity::getId,entity.getId()).update();
            i++;
            log.info("eg_fund_business_system_ebank_mapping表生成凭证进度：{}", NumberUtil.formatPercent(i/totalSize, 2));
        }
        }catch(Exception e) {
            log.error("eg_fund_business_system_ebank_mapping生成凭证失败，失败原因:{}",e);
        } finally {
            dataExecutionTaskService.finishedTask(taskId, DataExecutionTaskStatusEnum.SUCCESS.getCode(), successCount, failedCount);
        }
    }

    public boolean generateVoucher(Map<String,Object> dataMap,StringBuffer errorStringBuffer) {
        boolean isExistError = Boolean.FALSE;
        String errorInfo = "";
        String messageStatus = "";
        try {
            List<VoucherDTO> voucherDTOList = iRuleService.executeRule(dataMap);
            if (CollectionUtils.isEmpty(voucherDTOList) || CollectionUtils.isEmpty(voucherDTOList.get(0).getEntryList())) {
                errorInfo = "凭证行为空";
                messageStatus = "FAILED";
            } else {
                boolean hasFailed = false;
                boolean hasOrgFailed = false;
                boolean hasNoValid = false;
                for (VoucherDTO voucherDTO : voucherDTOList) {
                    if (StrUtil.equals(VoucherValidFlagEnum.NOT_EQUALS.getCode(), voucherDTO.getValidFlag())) {
                        hasFailed = true;
                        break;
                    } else if (StrUtil.equals(VoucherValidFlagEnum.NO_VALID.getCode(), voucherDTO.getValidFlag())) {
                        hasOrgFailed = true;
                        break;
                    } else if (!StrUtil.equals(VoucherValidFlagEnum.VALID.getCode(), voucherDTO.getValidFlag())) {
                        hasNoValid = true;
                        break;
                    }
                }
                if (hasOrgFailed) {
                    errorInfo = "签约主体为空";
                    messageStatus = "FAILED";
                } else if (hasFailed) {
                    errorInfo = "借贷金额不平";
                    messageStatus = "FAILED";
                } else if (hasNoValid) {
                    errorInfo = "存在无效的凭证";
                    messageStatus = "FAILED";
                } else {
                    messageStatus = "SUCCESS";
                }
            }

        } catch (Exception e) {
            errorInfo = e.getMessage();
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(errorInfo)) {
            isExistError = Boolean.TRUE;
            errorStringBuffer.append(errorInfo);
        }
        return isExistError;
    }
}

