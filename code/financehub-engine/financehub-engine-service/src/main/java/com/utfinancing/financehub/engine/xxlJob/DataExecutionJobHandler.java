package com.utfinancing.financehub.engine.xxlJob;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.DicDataConstant;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankMappingEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ContractStatusRecordSaveDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.finance.service.IContractStatusRecordService;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.entity.InterfaceDataEntity;
import com.utfinancing.financehub.engine.rule.entity.RawTransactionDataEntity;
import com.utfinancing.financehub.engine.rule.mapper.RawTransactionDataMapper;
import com.utfinancing.financehub.engine.rule.model.dto.DataExecutionTaskDTO;
import com.utfinancing.financehub.engine.rule.model.dto.MqErrorMessageDTO;
import com.utfinancing.financehub.engine.rule.model.vo.RawTransactionDataDuplicateVo;
import com.utfinancing.financehub.engine.rule.model.vo.RawTransactionDataQueryVO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.*;
import com.utfinancing.financehub.engine.scene.model.dto.MappingDTO;
import com.utfinancing.financehub.engine.scene.service.IFieldMappingService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.MDC;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 25/02/2024
 */
@Slf4j
@Component
@AllArgsConstructor
public class DataExecutionJobHandler {

    private final IDataExecutionTaskService dataExecutionTaskService;
    private final IRawTransactionDataService rawTransactionDataService;
    private final RawTransactionDataMapper rawTransactionDataMapper;
    private final IRuleService ruleService;
    private final RemoteDictService remoteDictService;
    private final RedisService redisService;
    private final IFieldMappingService iFieldMappingService;
    private final static String REDIS_KEY_FIELD_MAPPING = "_FIELD_MAPPING_DATA";
    private final IInterfaceDataService interfaceDataService;
    private final IContractService contractService;
    private final IContractStatusRecordService contractStatusRecordService;
    private final IMqErrorMessageService errorMessageService;
//    @Autowired
//    private final MailService mailService;

    /**
     * 接口单据无需人工审批，落入 raw 表后立即执行。
     * 同一入口也供失败单据重新执行使用。
     */
    @Async("hthxTaskAsyncExecutor")
    public void executeRawData(Long rawDataId) {
        RawTransactionDataEntity entity = rawTransactionDataService.getById(rawDataId);
        if (entity == null) {
            log.warn("接口原始单据不存在, rawDataId:{}", rawDataId);
            return;
        }
        if (RawMessageStatusEnum.RUNNING.getCode().equals(entity.getMessageStatus())) {
            return;
        }
        rawTransactionDataService.lambdaUpdate()
                .set(RawTransactionDataEntity::getMessageStatus, RawMessageStatusEnum.RUNNING.getCode())
                .set(RawTransactionDataEntity::getErrorInfo, null)
                .set(RawTransactionDataEntity::getUpdateTime, LocalDateTime.now())
                .eq(RawTransactionDataEntity::getId, rawDataId)
                .update();
        try {
            batchHandler(entity.getSystemCode(), new ArrayList<>(Arrays.asList(entity)), LocalDateTime.now(), new AtomicInteger(), new AtomicInteger());
        } catch (Exception e) {
            log.error("接口单据自动执行失败, rawDataId:{}", rawDataId, e);
            rawTransactionDataService.updateStatus(rawDataId, RawMessageStatusEnum.FAILED.getCode(), e.getMessage());
        }
    }

    public void validateRetryRawData(Long rawDataId) {
        RawTransactionDataEntity entity = rawTransactionDataService.getById(rawDataId);
        if (entity == null) {
            throw new ServiceException("接口单据不存在");
        }
        if (RawMessageStatusEnum.RUNNING.getCode().equals(entity.getMessageStatus())) {
            throw new ServiceException("单据正在执行，请稍后再试");
        }
        if (RawMessageStatusEnum.SUCCESS.getCode().equals(entity.getMessageStatus())) {
            throw new ServiceException("执行成功的单据不允许重复执行");
        }
    }


    @XxlJob(value = "executeSystemDataJob")
    public void executeSystemDataJobHandler() {
        MDC.put("PID", UUID.fastUUID().toString(true));
        String jobParam = XxlJobHelper.getJobParam();
        String systemCode = JSONObject.parseObject(jobParam).getString(RuleConstant.FIELD_SYSTEM_CODE);
        String skipRepeatDataCheck = JSONObject.parseObject(jobParam).getString(DicDataConstant.SKIP_REPEAT_DATA_CHECK);
        String ignoreRepeatDataSceneCode = JSONObject.parseObject(jobParam).getString(DicDataConstant.IGNORE_REPEAT_DATA_SCENE_CODE);
        XxlJobHelper.log("定时任务-批量执行会计引擎, 开始. systemCode:{}", systemCode);
        this.executeSystemData(systemCode, skipRepeatDataCheck,ignoreRepeatDataSceneCode);
        XxlJobHelper.log("定时任务-批量执行会计引擎, 结束. systemCode:{}", systemCode);
    }

    public void executeSystemData(String systemCode) {
        this.executeSystemData(systemCode, "skip","");
    }

    public void executeSystemData(String systemCode, String skipRepeatDataCheck,String ignoreRepeatDataSceneCode) {
        log.info("批量执行会计引擎, 开始. systemCode:{}", systemCode);
        LocalDateTime startDate = LocalDateTime.now();
        log.info("批量跑数据开始时间：{}", startDate);
        //检查开关是否打开
        if (!checkSystemOpen(systemCode)) {
            log.info("业务系统未注册自动跑批，本次任务跳过, systemCode:{}", systemCode);
            return;
        }


        //1.查询是否有正在执行的任务
        DataExecutionTaskDTO taskDTO = dataExecutionTaskService.getRunningTaskBySystem(systemCode);
        if (taskDTO != null) {
            log.info("存在正在执行的任务，本次任务跳过, systemCode:{}", systemCode);
            return;
        }

        //2.查询是否有未执行的业务数据
        log.info("执行状态类型：test");//RawMessageStatusEnum.NOT_EXECUTE.getCode()
        JSONObject todoRawTransactionDataInfo = rawTransactionDataMapper.getTodoRawTransactionDataInfo(systemCode);

        LocalDateTime minBusinessDate = DateUtil.parse(todoRawTransactionDataInfo.getString("minbusinessdate")).toLocalDateTime();
        LocalDateTime maxBusinessDate = DateUtil.parse(todoRawTransactionDataInfo.getString("maxbusinessdate")).toLocalDateTime();
        Long taskId = dataExecutionTaskService.createNewTask(systemCode, DataExecutionTaskStatusEnum.RUNNING.getCode(), todoRawTransactionDataInfo.getInteger("total"), minBusinessDate, maxBusinessDate);

        //2.1 检查是否有重复的数据
        List<RawTransactionDataDuplicateVo> duplicateDataList = rawTransactionDataService.getDuplicateData(systemCode, Arrays.asList(ignoreRepeatDataSceneCode.split(",")));
        if (CollectionUtil.isNotEmpty(duplicateDataList) && !skipRepeatDataCheck.equals("skip")) {
//            mailService.normalNotify(Arrays.asList("<EMAIL>"), "数据重复", "数据重复", JSONObject.toJSONString(duplicateDataList));
            return;
        }

        List<RawTransactionDataEntity> rawDataEntityList = new ArrayList<>();
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failedCount = new AtomicInteger();
        rawTransactionDataMapper.fetchTodoRawTransactionData(resultContext -> {
            RawTransactionDataQueryVO resultObject = resultContext.getResultObject();
            RawTransactionDataEntity rawTransactionDataEntity = new RawTransactionDataEntity();
            BeanUtil.copyProperties(resultObject, rawTransactionDataEntity, "messageContent");
            rawTransactionDataEntity.setMessageContent(JSONObject.parseObject(resultObject.getMessageContent()));
            rawDataEntityList.add(rawTransactionDataEntity);
            if (rawDataEntityList.size() == 1000) {
                batchHandler(systemCode, rawDataEntityList, startDate, failedCount, successCount);
                rawDataEntityList.clear();
            }
        }, systemCode);
        //收尾残数数据
        if (CollectionUtils.isNotEmpty(rawDataEntityList)) {
            batchHandler(systemCode, rawDataEntityList, startDate, failedCount, successCount);
            rawDataEntityList.clear();
        }
        dataExecutionTaskService.finishedTask(taskId, DataExecutionTaskStatusEnum.SUCCESS.getCode(), successCount.get(), failedCount.get());
        LocalDateTime endDate = LocalDateTime.now();
        log.info("批量跑数据开始时间：{}，结束时间:{},共用时：{}秒", startDate, endDate, ChronoUnit.SECONDS.between(startDate, endDate));
    }

    private void batchHandler(String systemCode, List<RawTransactionDataEntity> rawDataEntityList, LocalDateTime startDate, AtomicInteger failedCount, AtomicInteger successCount) {
        log.info("查询到未执行的业务数据, systemCode:{}, dataSize:{}", systemCode, rawDataEntityList.size());
        int i = 1;
        List<Map<String, Object>> ruleMapList = Lists.newArrayList();
        try {
            Map<String, Map<String, List<MappingDTO>>> allFieldMapping = queryAllFieldMapping();
            Map<String, List<MappingDTO>> systemMapping = allFieldMapping.get(systemCode);
            for (RawTransactionDataEntity entity : rawDataEntityList) {
                JSONObject jsonObject = entity.getMessageContent();
                convertDataFromMapping(jsonObject, systemMapping);
                normalizeStandardInterfaceFields(jsonObject);
                jsonObject.put("businessCode", "ZLYW");
                jsonObject.put("orderId", entity.getId().toString());
                jsonObject.put("interfaceId", entity.getId());
                jsonObject.put("interfaceCreateTime", entity.getCreateTime());
                jsonObject.put("id", entity.getId());//用户后续数据排序
                if (StrUtil.isBlank(jsonObject.getString("businessDate"))) {
                    LocalDateTime rawBusinessDate = entity.getBusinessDate() == null ? entity.getCreateTime() : entity.getBusinessDate();
                    jsonObject.put("businessDate", LocalDateTimeUtil.format(rawBusinessDate, "yyyy-MM-dd HH:mm:ss"));
                }

                ruleMapList.add(jsonObject.to(new TypeReference<Map<String, Object>>() {
                }));
            }
            LocalDateTime endLocalDate = LocalDateTime.now();
            log.info("组装数据开始时间：{}，结束时间:{},共用时：{}秒", startDate, endLocalDate, ChronoUnit.SECONDS.between(startDate, endLocalDate));
            //批量跑数据
            List<VoucherInfoVO> voucherList = ruleService.batchExecuteRule(ruleMapList);
            Map<Long, String> idMessageStatusMap = Maps.newHashMap();
            for (VoucherInfoVO vo : voucherList) {
                String valid;
                List<VoucherDTO> voucherDTOList = vo.getVoucherDTOList();
                String errorInfo = vo.getErrorInfo();
                String messageStatus = "";
                if (StringUtils.isNotEmpty(errorInfo)) {
                    if (errorInfo.length() > 1000) {
                        errorInfo = errorInfo.substring(0, 1000);
                    }
                    messageStatus = "FAILED";
                    valid = VoucherValidFlagEnum.NO_VALID.getCode();
                } else if (CollectionUtils.isEmpty(voucherDTOList) || CollectionUtils.isEmpty(voucherDTOList.get(0).getEntryList())) {
                    failedCount.getAndIncrement();
                    log.info("执行失败, num:{}, rawMessageId:{}", (i + "/" + voucherList.size()), vo.getOrderId());
                    errorInfo = "凭证行为空";
                    messageStatus = "FAILED";
                    valid = VoucherValidFlagEnum.ENTRY_EMPTY.getCode();
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
                        failedCount.getAndIncrement();
                        log.info("执行失败, num:{}, rawMessageId:{}", (i + "/" + voucherList.size()), vo.getOrderId());
                        errorInfo = "签约主体为空";
                        messageStatus = "FAILED";
                        valid = VoucherValidFlagEnum.NO_VALID.getCode();
                    } else if (hasFailed) {
                        failedCount.getAndIncrement();
                        log.info("执行失败, num:{}, rawMessageId:{}", (i + "/" + voucherList.size()), vo.getOrderId());
                        errorInfo = "借贷金额不平";
                        messageStatus = "FAILED";
                        valid = VoucherValidFlagEnum.NOT_EQUALS.getCode();
                    } else if (hasNoValid) {
                        failedCount.getAndIncrement();
                        log.info("执行失败, num:{}, rawMessageId:{}", (i + "/" + voucherList.size()), vo.getOrderId());
                        errorInfo = "存在无效的凭证";
                        messageStatus = "FAILED";
                        valid = VoucherValidFlagEnum.NOT_EQUALS.getCode();
                    } else {
                        successCount.getAndIncrement();
                        log.info("执行成功, num:{}, rawMessageId:{}", (i + "/" + voucherList.size()), vo.getOrderId());
                        messageStatus = "SUCCESS";
                        valid = VoucherValidFlagEnum.VALID.getCode();

                        // 更新合同信息
                        this.contractUpdate(vo, voucherDTOList);
                    }
                }
                i++;
                idMessageStatusMap.put(Long.parseLong(vo.getOrderId()), valid);
                rawTransactionDataService.lambdaUpdate().set(RawTransactionDataEntity::getErrorInfo, errorInfo).set(RawTransactionDataEntity::getMessageStatus, messageStatus).set(RawTransactionDataEntity::getUpdateTime, LocalDateTime.now()).eq(RawTransactionDataEntity::getId, Long.parseLong(vo.getOrderId())).update();
            }
            log.info("批量执行会计引擎, 结束. systemCode:{}", systemCode);
        } catch (Exception e) {
            log.error("批量执行会计引擎失败，失败原因：", e);
            String errorInfo = StrUtil.subWithLength(e.getMessage() == null ? "会计引擎执行异常" : e.getMessage(), 0, 1000);
            rawDataEntityList.forEach(entity -> rawTransactionDataService.lambdaUpdate()
                    .set(RawTransactionDataEntity::getMessageStatus, RawMessageStatusEnum.FAILED.getCode())
                    .set(RawTransactionDataEntity::getErrorInfo, errorInfo)
                    .set(RawTransactionDataEntity::getUpdateTime, LocalDateTime.now())
                    .eq(RawTransactionDataEntity::getId, entity.getId())
                    .update());
        }
    }

    /**
     * 华夏金租标准接口采用 snake_case 字段名，原会计引擎内部仍使用 camelCase。
     * 在进入规则引擎前补齐内部字段，避免客户、合同等辅助核算维度被误判为空。
     */
    private void normalizeStandardInterfaceFields(JSONObject jsonObject) {
        copyAliasIfAbsent(jsonObject, "customer_no", RuleConstant.FIELD_CLIENT_CODE);
        copyAliasIfAbsent(jsonObject, "customer_name", RuleConstant.FIELD_CLIENT_NAME);
        copyAliasIfAbsent(jsonObject, "contract_no", RuleConstant.FIELD_CONTRACT_CODE);
        copyAliasIfAbsent(jsonObject, "accounting_org_code", RuleConstant.FIELD_ORG_ID);
        copyAliasIfAbsent(jsonObject, "business_date", RuleConstant.FIELD_BUSINESS_DATE);
        copyAliasIfAbsent(jsonObject, "source_system", RuleConstant.FIELD_SYSTEM_CODE);
        copyAliasIfAbsent(jsonObject, "event_code", RuleConstant.FIELD_SCENE_CODE);
    }

    private void copyAliasIfAbsent(JSONObject jsonObject, String sourceField, String targetField) {
        if (StrUtil.isBlank(jsonObject.getString(targetField)) && StrUtil.isNotBlank(jsonObject.getString(sourceField))) {
            jsonObject.put(targetField, jsonObject.get(sourceField));
        }
    }

    private void contractUpdate(VoucherInfoVO vo, List<VoucherDTO> voucherDTOList) {

        // 1.若scene_code='RKCZ'，同时按主体+合同更新合同表的financial_contract_status为'入库后处置'，
        // financial_contract_status_update_time更新为接口的业务日期/凭证日期；
        if (SceneEnum.RKCZ.getCode().equals(vo.getSceneCode())) {
            contractService.lambdaUpdate().
                    set(ContractEntity::getFinancialContractStatus,
                            FinancialContractStatusForExceptionEnum.F_ENUM_6.getCode()).
                    set(ContractEntity::getFinancialContractStatusUpdateTime,
                            voucherDTOList.get(0).getVoucherDate()).
                    eq(ContractEntity::getOrgId, vo.getOrgId()).
                    eq(ContractEntity::getContractCode, vo.getContractCode()).update();

            ContractStatusRecordSaveDTO contractStatusRecordSaveDTO =
                    BeanUtil.copyProperties(vo, ContractStatusRecordSaveDTO.class);
            contractStatusRecordSaveDTO.setFinancialContractStatus(FinancialContractStatusForExceptionEnum.F_ENUM_6.getCode());
            contractStatusRecordSaveDTO.setFinancialContractStatusUpdateTime(DateUtils.toDate(voucherDTOList.get(0).getVoucherDate()));
            contractStatusRecordService.saveContractStatusRecord(contractStatusRecordSaveDTO);
        }

        //2.若scene_code='RKSH'，同时按主体+合同更新合同表的financial_contract_status为'入库后赎回'，
        // financial_contract_status_update_time更新为接口的业务日期/凭证日期；
        if (SceneEnum.RKSH.getCode().equals(vo.getSceneCode())) {
            contractService.lambdaUpdate().
                    set(ContractEntity::getFinancialContractStatus,
                            FinancialContractStatusForExceptionEnum.F_ENUM_20.getCode()).
                    set(ContractEntity::getFinancialContractStatusUpdateTime,
                            voucherDTOList.get(0).getVoucherDate()).
                    eq(ContractEntity::getOrgId, vo.getOrgId()).
                    eq(ContractEntity::getContractCode, vo.getContractCode()).update();

            ContractStatusRecordSaveDTO contractStatusRecordSaveDTO =
                    BeanUtil.copyProperties(vo, ContractStatusRecordSaveDTO.class);
            contractStatusRecordSaveDTO.setFinancialContractStatus(FinancialContractStatusForExceptionEnum.F_ENUM_20.getCode());
            contractStatusRecordSaveDTO.setFinancialContractStatusUpdateTime(DateUtils.toDate(voucherDTOList.get(0).getVoucherDate()));
            contractStatusRecordService.saveContractStatusRecord(contractStatusRecordSaveDTO);
        }
    }

    private boolean checkSystemOpen(String systemCode) {
        R<List<SysDictData>> currencyR = remoteDictService.listDictData(DictTypeEnum.SYS_AUTO_EXECUTE_SYSTEM.getCode());
        Map<String, String> currencyMap = currencyR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
        return currencyMap.containsKey(systemCode);
    }

    public Map<String, Map<String, List<MappingDTO>>> queryAllFieldMapping() {
        Map<String, Map<String, List<MappingDTO>>> cacheMap = redisService.getCacheObject(REDIS_KEY_FIELD_MAPPING);
        if (cacheMap == null) {
            cacheMap = iFieldMappingService.queryAllMappingFromDB();
            redisService.setCacheObject(REDIS_KEY_FIELD_MAPPING, cacheMap, 60L, TimeUnit.MINUTES); //过期时间：60分钟
        }
        return cacheMap;
    }

    public void convertDataFromMapping(JSONObject jsonObject, Map<String, List<MappingDTO>> systemMapping) {
        String sceneCode = jsonObject.getString("sceneCode");
        //原始场景code
        jsonObject.put(RuleConstant.FIELD_SCENE_CODE_ORIGINAL, sceneCode);
        if (systemMapping == null) {
            return;
        }
        for (Map.Entry<String, List<MappingDTO>> systemEntry : systemMapping.entrySet()) {
            String fieldCode = systemEntry.getKey();
            if (jsonObject.containsKey(fieldCode)) {
                List<MappingDTO> fieldList = systemEntry.getValue();
                for (MappingDTO fieldMap : fieldList) {
                    if (StrUtil.split(fieldMap.getSourceValue(), ",").contains(jsonObject.getString(fieldCode))) {
                        if (StrUtil.isNotBlank(fieldMap.getTargetFieldCode())) {
                            jsonObject.put(fieldMap.getTargetFieldCode(), fieldMap.getTargetValue());
                        } else {
                            jsonObject.put(fieldCode, fieldMap.getTargetValue());
                        }
                    }
                }
            }
        }
    }

    @XxlJob(value = "executeInterfaceDataJob")
    public void executeInterfaceDataJob() {
        XxlJobHelper.log("定时任务-接口执行会计引擎, 开始.");
        this.executeIntefaceData();
        XxlJobHelper.log("定时任务-接口执行会计引擎, 结束.");
    }

    public void executeIntefaceData() {
        //1.查询是否有正在执行的任务
        try {
            String systemCode = "INTERFACEDATA_VOUCHER";
            DataExecutionTaskDTO taskDTO = dataExecutionTaskService.getRunningTaskBySystem(systemCode);
            if (taskDTO != null) {
                log.info("存在正在执行的任务，本次任务跳过, systemCode:{}", systemCode);
                return;
            }
            //读取状态需要执行：1的数据
            List<InterfaceDataEntity> interfaceDataEntityList = interfaceDataService.lambdaQuery().eq(InterfaceDataEntity::getStatus, "1").orderByAsc(InterfaceDataEntity::getId).list();
            if (CollectionUtils.isEmpty(interfaceDataEntityList)) {
                return;
            }
            int i = 1;
            int successCount = 0;
            int failedCount = 0;
            int dataSize = interfaceDataEntityList.size();
            Long taskId = dataExecutionTaskService.createNewTask(systemCode, DataExecutionTaskStatusEnum.RUNNING.getCode(), dataSize, CollectionUtil.getFirst(interfaceDataEntityList).getBusinessDate(), CollectionUtil.getLast(interfaceDataEntityList).getBusinessDate());
            for (InterfaceDataEntity entity : interfaceDataEntityList) {
                JSONObject jsonObject = entity.getInterfaceData();
                if (null == jsonObject) {
                    continue;
                }
                Map<String, Object> dataMap = jsonObject.to(new TypeReference<Map<String, Object>>() {
                });
                dataMap.put("status", "0");
                //判断是否可以执行
                String ebankSerialNumber = MapUtil.getStr(dataMap, RuleConstant.FIELD_EBANK_SECIAL_NUMBER);
                FundBusinessSystemEbankMappingEntity mappingEntity = ruleService.bankExistFlag(ebankSerialNumber);
                log.info("映射银行账号Mapping：{}", mappingEntity);
                if (StringUtils.isNotEmpty(ebankSerialNumber) && null != mappingEntity) {
                    try {
                        dataMap.put(RuleConstant.INTERFACE_CREATE_TIME, mappingEntity.getCreateTime());
                        dataMap.put(RuleConstant.IS_INTERFACE_DATA, "1");
                        List<VoucherDTO> voucherDTOList = ruleService.executeRule(dataMap);
                        if (CollectionUtils.isEmpty(voucherDTOList) || CollectionUtils.isEmpty(voucherDTOList.get(0).getEntryList())) {
                            failedCount++;
                            log.info("执行失败, num:{}, interfaceDataId:{}", (i + "/" + dataSize), entity.getId());
                            entity.setErrorInfo("凭证行为空");
                            entity.setStatus("3");
                        } else {
                            boolean hasFailed = false;
                            boolean hasOrgFailed = false;
                            for (VoucherDTO voucherDTO : voucherDTOList) {
                                if (StrUtil.equals(VoucherValidFlagEnum.NOT_EQUALS.getCode(), voucherDTO.getValidFlag())) {
                                    hasFailed = true;
                                    break;
                                } else if (StrUtil.equals(VoucherValidFlagEnum.NO_VALID.getCode(), voucherDTO.getValidFlag())) {
                                    hasOrgFailed = true;
                                    break;
                                }
                            }
                            if (hasFailed) {
                                failedCount++;
                                log.info("执行失败, num:{}, interfaceDataId:{}", (i + "/" + dataSize), entity.getId());
                                entity.setErrorInfo("借贷金额不平");
                                entity.setStatus("3");
                            } else if (hasOrgFailed) {
                                failedCount++;
                                log.info("执行失败, num:{}, interfaceDataId:{}", (i + "/" + dataSize), entity.getId());
                                entity.setErrorInfo("签约主体为空");
                                entity.setStatus("3");
                            } else {
                                log.info("执行成功, num:{}, interfaceDataId:{}", (i + "/" + dataSize), entity.getId());
                                entity.setStatus("2");

                                // 1.若scene_code='RKCZ'，同时按主体+合同更新合同表的financial_contract_status为'入库后处置'，
                                // financial_contract_status_update_time更新为接口的业务日期/凭证日期；
                                if (SceneEnum.RKCZ.getCode().equals(entity.getSceneCode())) {
                                    contractService.lambdaUpdate().
                                            set(ContractEntity::getFinancialContractStatus,
                                                    FinancialContractStatusForExceptionEnum.F_ENUM_6.getCode()).
                                            set(ContractEntity::getFinancialContractStatusUpdateTime,
                                                    voucherDTOList.get(0).getVoucherDate()).
                                            eq(ContractEntity::getOrgId, entity.getOrgId()).
                                            eq(ContractEntity::getContractCode, entity.getContractCode()).update();

                                    ContractStatusRecordSaveDTO contractStatusRecordSaveDTO =
                                            BeanUtil.copyProperties(entity, ContractStatusRecordSaveDTO.class);
                                    contractStatusRecordService.saveContractStatusRecord(contractStatusRecordSaveDTO);
                                }

                                //2.若scene_code='RKSH'，同时按主体+合同更新合同表的financial_contract_status为'入库后赎回'，
                                // financial_contract_status_update_time更新为接口的业务日期/凭证日期；
                                if (SceneEnum.RKSH.getCode().equals(entity.getSceneCode())) {
                                    contractService.lambdaUpdate().
                                            set(ContractEntity::getFinancialContractStatus,
                                                    FinancialContractStatusForExceptionEnum.F_ENUM_20.getCode()).
                                            set(ContractEntity::getFinancialContractStatusUpdateTime,
                                                    voucherDTOList.get(0).getVoucherDate()).
                                            eq(ContractEntity::getOrgId, entity.getOrgId()).
                                            eq(ContractEntity::getContractCode, entity.getContractCode()).update();

                                    ContractStatusRecordSaveDTO contractStatusRecordSaveDTO =
                                            BeanUtil.copyProperties(entity, ContractStatusRecordSaveDTO.class);
                                    contractStatusRecordService.saveContractStatusRecord(contractStatusRecordSaveDTO);
                                }
                            }
                        }
                    } catch (Exception e) {
                        entity.setErrorInfo(e.toString() + ":" + e.getMessage());
                        entity.setStatus("3");
                    } finally {
                        interfaceDataService.lambdaUpdate().set(InterfaceDataEntity::getErrorInfo, entity.getErrorInfo()).set(InterfaceDataEntity::getStatus, entity.getStatus()).set(InterfaceDataEntity::getUpdateTime, LocalDateTime.now()).eq(InterfaceDataEntity::getId, entity.getId()).update();
                        i++;
                    }
                }
            }
            dataExecutionTaskService.finishedTask(taskId, DataExecutionTaskStatusEnum.SUCCESS.getCode(), successCount, failedCount);
            log.info("接口执行会计引擎, 结束. systemCode:{}", systemCode);
        } catch (Exception e) {
            log.error("接口执行会计引擎失败，失败原因：", e);
        }
    }

}
