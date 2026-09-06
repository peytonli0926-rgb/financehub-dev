package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.SpringUtils;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.enums.CheckExecuteStatusEnum;
import com.utfinancing.financehub.engine.enums.CheckTargetEnum;
import com.utfinancing.financehub.engine.enums.CheckTypeEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.finance.constant.CheckSuffixConstant;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountDetailRecordVO;
import com.utfinancing.financehub.engine.finance.mapper.CheckAccountDetailRecordMapper;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountDetailResultHisVO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountDetailResultVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.service.impl.AccountServiceImpl;
import com.utfinancing.financehub.etl.api.RemoteCheckDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-20
 * @Description :  CheckAccountDetailRecord服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CheckAccountDetailRecordServiceImpl extends ServiceImpl<CheckAccountDetailRecordMapper, CheckAccountDetailRecordEntity> implements ICheckAccountDetailRecordService {

    private final CheckAccountDetailRecordMapper checkAccountDetailRecordMapper;

    @Resource
    private CheckAccountDetailResultServiceImpl checkAccountDetailResultService;

    @Resource
    private CheckAccountDetailResultHisServiceImpl checkAccountDetailResultHisService;

    @Resource
    private AccountServiceImpl accountService;

    @Resource
    private AccountAssistBalanceServiceImpl accountAssistBalanceService;

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Resource
    private ICheckAccountKingdeeResultService checkAccountKingdeeResultService;

    @Resource
    private ICheckAccountKingdeeResultHisService checkAccountKingdeeResultHisService;

    @Resource
    private RemoteCheckDataService remoteCheckDataService;

    @Resource
    private ICheckAccountMiddleResultService checkAccountMiddleResultService;

    @Resource
    private ICheckAccountMiddleResultHisService checkAccountMiddleResultHisService;

    @Resource
    private ICheckCommonFinanceDataService checkCommonFinanceDataService;

    @Resource
    private ICheckCommonFinanceDataResultService checkCommonFinanceDataResultService;

    @Resource
    private ICheckSqlService checkSqlService;

    @Resource
    private ICheckCommonFieldService checkCommonFieldService;

    @Resource
    private ICheckCommonDataResultService checkCommonDataResultService;

    @Resource
    private ICheckAccountDetailContractBalanceService checkAccountDetailContractBalanceService;

    @Override
    public Long saveCheckAccountDetailRecord(CheckAccountDetailRecordDTO dto) {
        CheckAccountDetailRecordEntity entity = BeanUtil.copyProperties(dto, CheckAccountDetailRecordEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateCheckAccountDetailRecord(Long id, CheckAccountDetailRecordDTO dto) {
        CheckAccountDetailRecordEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public CheckAccountDetailRecordDTO getCheckAccountDetailRecordDTOById(Long id) {
        CheckAccountDetailRecordEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, CheckAccountDetailRecordDTO.class);
    }

    @Override
    public IPage<CheckAccountDetailRecordVO> selectPage(CheckAccountDetailRecordQueryDTO queryDTO) {
        LambdaQueryWrapper<CheckAccountDetailRecordEntity> queryWrapper = Wrappers.<CheckAccountDetailRecordEntity>lambdaQuery();
        if(StringUtils.isNotEmpty(queryDTO.getCheckTarget())){
            queryWrapper.eq(CheckAccountDetailRecordEntity::getCheckTarget, queryDTO.getCheckTarget());
        }else {
            throw new ServiceException("业务场景不能为空");
        }
        queryWrapper.eq(CheckAccountDetailRecordEntity::getExecuteStatus, CheckExecuteStatusEnum.FINISH.getCode());
        queryWrapper.orderByDesc(CheckAccountDetailRecordEntity::getId);

        //这里注入查询条件
        IPage<CheckAccountDetailRecordEntity> entityIPage = checkAccountDetailRecordMapper.selectPage(new Page<CheckAccountDetailRecordEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<CheckAccountDetailRecordVO> page = ListBeanUtil.copyPage(entityIPage, CheckAccountDetailRecordVO.class);
        List<CheckAccountDetailRecordVO> records = page.getRecords();
        if(CollectionUtil.isNotEmpty(records)){
            records.forEach(i->i.setVersion(LocalDateTimeUtil.format(i.getCreateTime(), "yyyy-MM-dd")+"_"+i.getId()));
        }
        page.setRecords(records);
        return page;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean isLastDayOfMonth() {
        LocalDateTime lastDayOfMonth = LocalDateTime.of(LocalDate.from(LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth())), LocalTime.MAX);
        return LocalDateTimeUtil.isSameDay(LocalDateTime.now(), lastDayOfMonth);
    }

    @Override
    public CheckAccountDetailRecordVO findRecord(Integer periodCode, String target) {
        CheckAccountDetailRecordEntity record = this.getOne(new LambdaQueryWrapper<CheckAccountDetailRecordEntity>()
                .eq(CheckAccountDetailRecordEntity::getExecuteStatus, CheckExecuteStatusEnum.FINISH.getCode())
                .eq(CheckAccountDetailRecordEntity::getCheckTarget, target)
                .eq(CheckAccountDetailRecordEntity::getPeriodCode, periodCode));
        return getVo(record);
    }

    @Override
    public void checkKingdee(Integer periodCode, String checkType) {
        if(periodCode==null){
            throw  new ServiceException("期间不能为空");
        }
//        List<CheckAccountDetailRecordEntity> inProgressRecords = getInProgressRecords(CheckTargetEnum.KINGDEE.getCode());
//        log.info("查询in progress核对记录结束");
//        if (CollectionUtils.isNotEmpty(inProgressRecords)) {
//            throw new ServiceException("还有正在执行的任务，请等待执行结束");
//        }

        CheckAccountDetailRecordEntity record = saveCheckRecords(periodCode, checkType, CheckTargetEnum.KINGDEE.getCode());

        if(StringUtils.equals(CheckTypeEnum.JOB.getCode(), checkType)
                && isLastDayOfMonth()){
            checkAccountKingdeeResultHisService.saveResultToHis();
            log.info("保存实时表数据到历史表结束");
        }

        boolean currentMonth = isCurrentMonth(periodCode);
        //如果是实时对账则插入对账结果实时表，如果是历史期间对账则插入对账结果历史表
        if(currentMonth){
            checkAccountKingdeeResultService.clearTableData();
        }else{
            checkAccountKingdeeResultHisService.clearHisTableData(periodCode);
        }
        log.info("实时表清理结束");

        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            // 异步执行的任务
            log.info("调用etl将金蝶数据存放到临时表 开始 period:"+ periodCode);
            remoteCheckDataService.saveToKingdeeTmp(String.valueOf(periodCode));
            log.info("调用etl将金蝶数据存放到临时表 结束 period:"+ periodCode);


        }, asyncTaskExecutor).thenRun(() -> {
            log.info("将金蝶与科目余额对账数据保存 开始 period:"+ periodCode);
//            CheckAccountDetailRecordEntity latestRecord = getLastRecord(CheckTargetEnum.KINGDEE.getCode());
            // 下一个异步任务
            if(currentMonth){
                checkAccountKingdeeResultService.queryAndSaveCheckKingdeeDto(periodCode, checkType, record);
            }else{
                checkAccountKingdeeResultHisService.queryAndSaveCheckKingdeeDto(periodCode, checkType, record);
            }
            log.info("将金蝶与科目余额对账数据保存 结束 period:"+ periodCode);
            //修改record状态
            record.setExecuteStatus(CheckExecuteStatusEnum.FINISH.getCode());
            record.setEndTime(LocalDateTime.now());
            this.updateById(record);
            log.info("更新记录状态 recordId:"+ record.getId());
        });

    }

    @Override
    public void checkKingdeeMiddle(Integer periodCode, String checkType) {
        if(periodCode==null){
            throw  new ServiceException("期间不能为空");
        }
//        List<CheckAccountDetailRecordEntity> inProgressRecords = getInProgressRecords(CheckTargetEnum.MIDDLE.getCode());
//        log.info("查询in progress核对记录结束");
//        if (CollectionUtils.isNotEmpty(inProgressRecords)) {
//            throw new ServiceException("还有正在执行的任务，请等待执行结束");
//        }

        CheckAccountDetailRecordEntity record = saveCheckRecords(periodCode, checkType, CheckTargetEnum.MIDDLE.getCode());

        if(StringUtils.equals(CheckTypeEnum.JOB.getCode(), checkType)
                && isLastDayOfMonth()){
            checkAccountMiddleResultHisService.saveResultToHis();
            log.info("保存实时表数据到历史表结束");
        }

        boolean currentMonth = isCurrentMonth(periodCode);
        //如果是实时对账则插入对账结果实时表，如果是历史期间对账则插入对账结果历史表
        if(currentMonth){
            checkAccountMiddleResultService.clearTableData();
        }else{
            checkAccountMiddleResultHisService.clearHisTableData(periodCode);
        }
        log.info("实时表清理结束");

        ICheckAccountDetailRecordService checkAccountDetailRecordService = SpringUtils.getBean(ICheckAccountDetailRecordService.class);
        checkAccountDetailRecordService.syncMiddleCheck(periodCode, checkType, record, currentMonth);


//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
//            // 异步执行的任务
//            log.info("调用etl将金蝶中间表数据存放到临时表 开始 period:"+ periodCode);
//            try{
//                remoteCheckDataService.saveToMiddleTmp(String.valueOf(periodCode));
//            }catch (Exception e){
//                log.info("调用etl生成临时表数据报错:"+e.getMessage());
//            }finally {
//                log.info("调用etl将金蝶中间表数据存放到临时表 调用结束");
//            }
//
//            log.info("调用etl将金蝶中间表数据存放到临时表 结束 period:"+ periodCode);
//
//
//        }, asyncTaskExecutor).thenRun(() -> {
////            CheckAccountDetailRecordEntity latestRecord = getLastRecord(CheckTargetEnum.MIDDLE.getCode());
//
//            // 下一个异步任务
//            log.info("将金蝶中间表与科目余额对账数据保存 开始 period:"+ periodCode);
//            if(currentMonth){
//                checkAccountMiddleResultService.queryAndSaveCheckMiddleDto(periodCode, checkType, record);
//            }else{
//                checkAccountMiddleResultHisService.queryAndSaveCheckMiddleDto(periodCode, checkType, record);
//            }
//            log.info("将金蝶中间表与科目余额对账数据保存 结束 period:"+ periodCode);
//
//            //修改record状态
//            record.setExecuteStatus(CheckExecuteStatusEnum.FINISH.getCode());
//            record.setEndTime(LocalDateTime.now());
//            this.updateById(record);
//            log.info("更新记录状态 recordId:"+ record.getId());
//
//        });

    }

    @Override
    public void checkCommon(Integer periodCode, String businessType, String checkType) {
        if(periodCode==null||StringUtils.isEmpty(businessType)){
            throw new ServiceException("期间，业务场景不能为空");
        }
        CheckTargetEnum commonTarget = CheckTargetEnum.getInstanceByCode(businessType);
        if(commonTarget==null){
            throw new ServiceException("业务系统代码，业务场景不合法");
        }
//        List<CheckAccountDetailRecordEntity> inProgressRecords = getInProgressRecords(commonTarget.getCode());
//        log.info("查询in progress核对记录结束");
//        if (CollectionUtils.isNotEmpty(inProgressRecords)) {
//            throw new ServiceException("还有正在执行的任务，请等待执行结束");
//        }

        CheckAccountDetailRecordEntity record = saveCheckRecords(periodCode, checkType, commonTarget.getCode());

        //月末清理对账原始表和对账结果表
        if(StringUtils.equals(CheckTypeEnum.JOB.getCode(), checkType)
                && isLastDayOfMonth()){
            checkCommonFinanceDataService.deleteCheckCommonData(businessType);
            checkCommonFinanceDataResultService.deleteCheckCommonData(businessType);
            log.info("月末清理对账原始表和对账结果表结束");
        }

        Integer executeDateCode = Integer.valueOf(DateUtil.format(DateUtil.toLocalDateTime(new Date()), "yyyyMMdd"));
        checkCommonFinanceDataService.deleteCheckCommonDataByExecuteDate(executeDateCode, businessType, periodCode);
        checkCommonFinanceDataResultService.deleteCheckCommonDataByExecuteDate(executeDateCode, businessType, periodCode);

        LocalDateTime executeDate = LocalDateTime.now();
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            // 异步执行的任务
            log.info("调用etl将对账数据存放到临时表 开始 period:{} businessType:{}", periodCode, businessType);
            try {
                remoteCheckDataService.saveToCommonTmp(periodCode, businessType);
            }catch (Exception e){
                log.error(e.getMessage());
            }
            log.info("调用etl将对账数据存放到临时表 结束 period:{} businessType:{}", periodCode, businessType);


        }, asyncTaskExecutor);

        completableFuture.thenRunAsync(() -> {

            List<CheckSqlEntity> checkSqlList = checkSqlService.list(new LambdaQueryWrapper<CheckSqlEntity>()
                    .eq(CheckSqlEntity::getSqlMark, businessType).orderByAsc(CheckSqlEntity::getSqlOrder));

//            CheckSqlEntity tmpSql = new CheckSqlEntity();
//            tmpSql.setSqlMark("shifbxf");
//            tmpSql.setSqlOrder(1);
//            tmpSql.setFinanceQuerySql("select '11111' org_id,'222222' contract_code, 0.01 SFBXF, 0.02 DZFBXF, 0.03 QZHTFY ");
//
//            checkSqlList = Arrays.asList(tmpSql);
            if(CollectionUtil.isEmpty(checkSqlList)){
                throw new ServiceException("没有找到有效的sql信息");
            }
            for (CheckSqlEntity checkSql : checkSqlList){
                String querySql = checkSql.getFinanceQuerySql();
                if(StringUtils.isEmpty(querySql)){
                    continue;
                }
                final String finalSql = getFinalSql(periodCode, querySql);
//                final String finalSql = " select t.*, 12.11 AS field_b  from abs050708 t";
                List<Map<String, Object>> list = checkCommonFinanceDataService.selectDataCommon(finalSql);

                if(CollectionUtil.isEmpty(list)){
//                    throw new ServiceException("systemCode "+systemCode+" sqlMark "+sqlMark+" periodCode "+periodCode+" 获取到没有获取到数据");
                    continue;
                }
                List<CheckCommonFinanceDataEntity> commonDataList = getCheckCommonDataList(businessType, list, executeDate, periodCode);

                saveCommonToTmp(String.valueOf(periodCode), businessType, commonDataList);
            }
        }, asyncTaskExecutor).thenRun(() -> {
//            CheckAccountDetailRecordEntity latestRecord = getLastRecord(commonTarget.getCode());

            List<CheckCommonFinanceDataEntity> financeCommonDataList = checkCommonFinanceDataService.list(new LambdaQueryWrapper<CheckCommonFinanceDataEntity>()
//                    .eq(CheckCommonFinanceDataEntity::getDbCode, SystemEnum.CWZT.getCode())
                    .eq(CheckCommonFinanceDataEntity::getBusinessType, businessType)
                    .eq(CheckCommonFinanceDataEntity::getPeriodCode, periodCode)
                    .apply("to_char(execute_date, 'yyyyMMdd') = {0} ", String.valueOf(executeDateCode)));

            if(CollectionUtil.isEmpty(financeCommonDataList)){
                throw new ServiceException("没有找到 executeDateCode:"+executeDateCode+" sqlMark:"+businessType+" 下的原始表数据");
            }

            List<CheckCommonDataResultEntity> commonDataList = checkCommonDataResultService.list(new LambdaQueryWrapper<CheckCommonDataResultEntity>()
                    .eq(CheckCommonDataResultEntity::getBusinessType, businessType)
                    .eq(CheckCommonDataResultEntity::getPeriodCode, periodCode)
                    .apply("to_char(execute_date, 'yyyyMMdd') = {0} ", String.valueOf(executeDateCode)));


            Map<String, List<CheckCommonFinanceDataEntity>> financeCommonDataMap = financeCommonDataList.stream().collect(Collectors.groupingBy(m->StringUtils.upperCase(m.getJoinField())));
            //先根据systemCode分组，再根据joinField提取
//            Map<String, Map<String, CheckCommonDataResultEntity>> commonDataMap = commonDataList.stream().collect(
//                    Collectors.groupingBy(CheckCommonDataResultEntity::getDbCode,
//                    Collectors.toMap(CheckCommonDataResultEntity::getJoinField, Function.identity(), ((key1 , key2) -> key1))));
            //与业务确认不会出现不同业务系统取出joinField相同的情况
            Map<String, CheckCommonDataResultEntity> commonDataMap = commonDataList.stream().collect(Collectors.toMap(CheckCommonDataResultEntity::getJoinField, Function.identity(), ((key1 , key2) -> key1)));

            List<CheckCommonFinanceDataResultEntity> financeResultList = Lists.newArrayList();

            //循环中台对账数据
            financeCommonDataMap.forEach((k,v)->{
//                List<CheckCommonDataEntity> tmpList = v;
                Map<String, Object> queryFieldMap = Maps.newLinkedHashMap();
                Map<String, Object> compareResultMap = Maps.newLinkedHashMap();
                Map<String, Object> compareFlagMap = Maps.newLinkedHashMap();

                Map<String, Object> map = Maps.newLinkedHashMap();
                List<CheckCommonFinanceDataEntity> sortedList = v.stream().sorted(Comparator.comparing(CheckCommonFinanceDataEntity::getId)).collect(Collectors.toList());
                CheckCommonFinanceDataResultEntity resultEntity = new CheckCommonFinanceDataResultEntity();
                putQueryField(sortedList, map, "tmp");

                resultEntity.setBusinessType(businessType);
                resultEntity.setJoinField(k);
                resultEntity.setExecuteDate(executeDate);
                resultEntity.setRecordId(record.getId());
                if (commonDataMap.containsKey(k)){
                    CheckCommonDataResultEntity commonDataResult = commonDataMap.get(k);
                    String queryField = commonDataResult.getQueryField();
                    JSONObject jsonObject = JSON.parseObject(queryField);
                    Map<String, Object> commonDataQueryMap = jsonObject.getInnerMap();
//                    queryFieldMap.putAll(map);
                    commonDataQueryMap.forEach((k2,v2)->{
                        if(map.containsKey(k2)){
                            queryFieldMap.put(k2, v2);
                            compareResultMap.put(k2+CheckSuffixConstant.DIFF, (NumberUtil.toBigDecimal(v2.toString())).subtract(NumberUtil.toBigDecimal(map.get(k2).toString())));
                            compareFlagMap.put(k2+CheckSuffixConstant.DIFF_FLAG, NumberUtil.toBigDecimal(v2.toString()).compareTo(NumberUtil.toBigDecimal(map.get(k2).toString()))==0?"0":"1");
                        }
                    });
                    resultEntity.setShowField(commonDataResult.getShowField());
                    resultEntity.setDbCode(commonDataResult.getDbCode());
                }else{
                    map.forEach((k2,v2)->{
                        queryFieldMap.put(StringUtils.upperCase(k2), BigDecimal.ZERO);
                        compareResultMap.put(StringUtils.upperCase(k2)+CheckSuffixConstant.DIFF, BigDecimal.ZERO.subtract(NumberUtil.toBigDecimal(v2.toString())));
                        compareFlagMap.put(StringUtils.upperCase(k2)+CheckSuffixConstant.DIFF_FLAG, "1");
                    });
                    resultEntity.setShowField(sortedList.get(0).getShowField());
                    resultEntity.setDbCode(SystemEnum.CWZT.getCode());
                }

                putQueryField(sortedList, queryFieldMap, "query");
//                for(CheckCommonFinanceDataEntity tmp : sortedList){
//                    String queryField = tmp.getQueryField();
//                    JSONObject jsonObject = JSON.parseObject(queryField);
//                    Map<String, Object> tmpMap = jsonObject.getInnerMap();
//                    tmpMap.forEach((k1,v1)->{
//                        queryFieldMap.put(StringUtils.upperCase(k1)+ CheckSuffixConstant.FINANCE, v1);
////                        map.put(StringUtils.upperCase(k1),v1);
//                    });
////                    queryFieldMap.putAll(jsonObject.getInnerMap());
//                }
                resultEntity.setQueryField(JSONObject.toJSONString(queryFieldMap));
                resultEntity.setCompareResultField(JSONObject.toJSONString(compareResultMap));
                resultEntity.setCompareFlagField(JSONObject.toJSONString(compareFlagMap));
                resultEntity.setPeriodCode(periodCode);
                financeResultList.add(resultEntity);
            });

            //循环业务系统对账数据
            commonDataMap.entrySet().removeIf(entry -> financeCommonDataMap.containsKey(entry.getKey()));
            commonDataMap.forEach((k3, v3) -> {
                Map<String, Object> queryFieldMap = Maps.newLinkedHashMap();
                Map<String, Object> compareResultMap = Maps.newLinkedHashMap();
                Map<String, Object> compareFlagMap = Maps.newLinkedHashMap();

                CheckCommonFinanceDataResultEntity resultEntity = new CheckCommonFinanceDataResultEntity();
                resultEntity.setBusinessType(businessType);
                resultEntity.setDbCode(v3.getDbCode());
                resultEntity.setJoinField(k3);
                resultEntity.setExecuteDate(executeDate);
                resultEntity.setRecordId(record.getId());

                String queryField = v3.getQueryField();
                JSONObject jsonObject = JSON.parseObject(queryField);
                Map<String, Object> commonDataQueryMap = jsonObject.getInnerMap();
                commonDataQueryMap.forEach((k4,v4)->{
                    queryFieldMap.put(k4, v4);
                    compareResultMap.put(k4+CheckSuffixConstant.DIFF, v4);
                    compareFlagMap.put(k4+CheckSuffixConstant.DIFF_FLAG, "1");
                });

                resultEntity.setQueryField(JSONObject.toJSONString(queryFieldMap));
                resultEntity.setCompareResultField(JSONObject.toJSONString(compareResultMap));
                resultEntity.setCompareFlagField(JSONObject.toJSONString(compareFlagMap));
                financeResultList.add(resultEntity);
            });

            saveCommonToResultTmp(String.valueOf(periodCode), SystemEnum.CWZT.getCode(), businessType, financeResultList);

            //修改record状态
            record.setExecuteStatus(CheckExecuteStatusEnum.FINISH.getCode());
            record.setEndTime(LocalDateTime.now());
            this.updateById(record);
            log.info("更新记录状态 recordId:"+ record.getId());
        });
    }

    @Override
    public Boolean checkPeriodExist(CheckCommonQueryDTO queryDTO) {
        Integer periodCode = queryDTO.getPeriodCode();
        String businessType = queryDTO.getBusinessType();
        if(ObjectUtil.isEmpty(periodCode)||StringUtils.isEmpty(businessType)){
            throw new ServiceException("期间和业务类型都不能为空");
        }
        if(ObjectUtil.isEmpty(CheckTargetEnum.getInstanceByCode(businessType))){
            throw new ServiceException("业务类型有误");
        }
        List<CheckAccountDetailRecordEntity> inProgressRecords = getInProgressRecords(businessType);
        log.info("查询in progress核对记录结束");
        if (CollectionUtils.isNotEmpty(inProgressRecords)) {
            throw new ServiceException("还有正在执行的任务，请等待执行结束");
        }

        List<CheckAccountDetailRecordEntity> recordList =  this.list(new LambdaQueryWrapper<CheckAccountDetailRecordEntity>()
                .eq(CheckAccountDetailRecordEntity::getPeriodCode, periodCode)
                .eq(CheckAccountDetailRecordEntity::getCheckTarget, businessType)
                .eq(CheckAccountDetailRecordEntity::getExecuteStatus, CheckExecuteStatusEnum.FINISH.getCode()));
        if(CollectionUtil.isNotEmpty(recordList)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Async
    @Override
    public void syncMiddleCheck(Integer periodCode, String checkType, CheckAccountDetailRecordEntity record, boolean currentMonth) {
        log.info("调用etl将金蝶中间表数据存放到临时表 开始 period:"+ periodCode);
        remoteCheckDataService.saveToMiddleTmp(String.valueOf(periodCode));
        log.info("调用etl将金蝶中间表数据存放到临时表 结束 period:"+ periodCode);
        log.info("将金蝶中间表与科目余额对账数据保存 开始 period:"+ periodCode);
        if(currentMonth){
            checkAccountMiddleResultService.queryAndSaveCheckMiddleDto(periodCode, checkType, record);
        }else{
            checkAccountMiddleResultHisService.queryAndSaveCheckMiddleDto(periodCode, checkType, record);
        }
        log.info("将金蝶中间表与科目余额对账数据保存 结束 period:"+ periodCode);

        //修改record状态
        record.setExecuteStatus(CheckExecuteStatusEnum.FINISH.getCode());
        record.setEndTime(LocalDateTime.now());
        this.updateById(record);
        log.info("更新记录状态 recordId:"+ record.getId());
    }

    private static void putQueryField(List<CheckCommonFinanceDataEntity> sortedList, Map<String, Object> map, String type) {
        for(CheckCommonFinanceDataEntity tmp : sortedList){
            String queryField = tmp.getQueryField();
            JSONObject jsonObject = JSON.parseObject(queryField);
            Map<String, Object> tmpMap = jsonObject.getInnerMap();
            tmpMap.forEach((k1,v1)->{
//                        queryFieldMap.put(StringUtils.upperCase(k1)+ CheckSuffixConstant.FINANCE, v1);
                if(StringUtils.equals(type, "tmp")){
                    map.put(StringUtils.upperCase(k1),v1);
                }else if(StringUtils.equals(type, "query")){
                    map.put(StringUtils.upperCase(k1)+ CheckSuffixConstant.FINANCE, v1);
                }
            });
//                    queryFieldMap.putAll(jsonObject.getInnerMap());
        }
    }

    private static String getFinalSql(Integer periodCode, String querySql) {
        final String finalSql = querySql.replaceAll("#periodCode#", String.valueOf(periodCode));
        return finalSql;
    }


    private void saveCommonToResultTmp(String periodCode, String systemCode, String sqlMark, List<CheckCommonFinanceDataResultEntity> commonDataResultList) {
        log.info("同步其他业务系统数据开始保存到结果表 入库 dbCode:{} sqlMark:{} periodCode:{}  转换后数据量:{}", systemCode, sqlMark, periodCode, commonDataResultList.size());
        List<List<CheckCommonFinanceDataResultEntity>> entityPage = ListUtil.partition(commonDataResultList, 1000);
//        ICheckSqlService checkSqlService = SpringUtils.getBean(ICheckSqlService.class);
        int pageCount = entityPage.size();
//        CountDownLatch countDownLatch = new CountDownLatch(pageCount-1);
        log.info("同步其他业务系统数据数据保存到结果表 分组完成 dbCode:{} sqlMark:{}  periodCode:{}, pageCount:{}", systemCode, sqlMark, periodCode, pageCount);
        int i = 1;

        checkCommonFinanceDataResultService.saveBatch(entityPage.get(0));
        if(pageCount>1){
            for (List<CheckCommonFinanceDataResultEntity> entityList: entityPage.stream().skip(1).collect(Collectors.toList())){
//                checkCommonFinanceDataResultService.batchSaveToResultTmp(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount-1).toString()),2), periodCode, systemCode, sqlMark);
                checkCommonFinanceDataResultService.batchSaveToResultTmp(null, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount-1).toString()),2), periodCode, systemCode, sqlMark);

                i++;
            }

//            try {
//                countDownLatch.await();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }

        log.info("同步其他业务系统数据数据完成保存到结果表 入库 dbCode:{} sqlMark:{}  periodCode:{}  转换后数据量:{}", systemCode, sqlMark, periodCode, commonDataResultList.size());
    }

    private void saveCommonToTmp(String periodCode, String sqlMark, List<CheckCommonFinanceDataEntity> commonDataList) {
        log.info("同步中台对账数据开始保存到原始表 入库 dbCode:{} sqlMark:{} periodCode:{}  转换后数据量:{}", SystemEnum.CWZT.getCode(), sqlMark, periodCode, commonDataList.size());
        List<List<CheckCommonFinanceDataEntity>> entityPage = ListUtil.partition(commonDataList, 1000);
        int pageCount = entityPage.size();
//        CountDownLatch countDownLatch = new CountDownLatch(pageCount-1);
        log.info("同步中台对账数据开始保存到原始表 分组完成 dbCode:{} sqlMark:{}  periodCode:{}, pageCount:{}", SystemEnum.CWZT.getCode(), sqlMark, periodCode, pageCount);
        int i = 1;
        checkCommonFinanceDataService.saveBatch(entityPage.get(0));
        if(pageCount>1){
            for (List<CheckCommonFinanceDataEntity> entityList: entityPage.stream().skip(1).collect(Collectors.toList())){
                checkCommonFinanceDataService.batchSaveToTmp(null, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount-1).toString()),2), periodCode, SystemEnum.CWZT.getCode(), sqlMark);
//                checkCommonFinanceDataService.batchSaveToTmp(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount-1).toString()),2), periodCode, SystemEnum.CWZT.getCode(), sqlMark);
                i++;
            }

//            try {
//                countDownLatch.await();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }

        log.info("同步中台对账数据开始保存到原始表，保存原始表完成 入库 dbCode:{} sqlMark:{}  periodCode:{}  转换后数据量:{}", SystemEnum.CWZT.getCode(), sqlMark, periodCode, commonDataList.size());
    }

    private List<CheckCommonFinanceDataEntity> getCheckCommonDataList(String sqlMark, List<Map<String, Object>> list, LocalDateTime executeDate, Integer periodCode) {
        List<CheckCommonFieldEntity> fieldList = checkCommonFieldService.list(
                new LambdaQueryWrapper<CheckCommonFieldEntity>()
                        .eq(CheckCommonFieldEntity::getBusinessType, sqlMark).orderByAsc(CheckCommonFieldEntity::getFieldOrder));
        if(CollectionUtil.isEmpty(fieldList)){
            throw new ServiceException(" sqlMark "+ sqlMark +" 获取获取查询字段失败");
        }
        List<String > joinFieldList = fieldList.stream().filter(v -> StringUtils.equals(v.getFieldType(), "1"))
                .sorted(Comparator.comparing(CheckCommonFieldEntity::getFieldOrder)).map(m -> StringUtils.lowerCase(m.getCommonField())).collect(Collectors.toList());

        List<String> queryFieldList = fieldList.stream().filter(v -> StringUtils.equals(v.getFieldType(), "2"))
                .sorted(Comparator.comparing(CheckCommonFieldEntity::getFieldOrder)).map(m -> StringUtils.lowerCase(m.getCommonField())).collect(Collectors.toList());

        List<String> showFieldList = fieldList.stream().filter(v -> StringUtils.equals(v.getFieldType(), "3"))
                .sorted(Comparator.comparing(CheckCommonFieldEntity::getFieldOrder)).map(m -> StringUtils.lowerCase(m.getCommonField())).collect(Collectors.toList());

        return list.stream().map(v-> {
            return getCheckCommonData(sqlMark, v, joinFieldList, queryFieldList, showFieldList, executeDate, periodCode);
        }).collect(Collectors.toList());
    }

    private static CheckCommonFinanceDataEntity getCheckCommonData(String sqlMark, Map<String, Object> v, List<String> joinFieldList, List<String> queryFieldList, List<String> showFieldList, LocalDateTime executeDate, Integer periodCode) {
        Map<String, Object> joinFieldMap = Maps.newLinkedHashMap();
        Map<String, Object> queryFieldMap = Maps.newLinkedHashMap();
        Map<String, Object> showFieldMap = Maps.newLinkedHashMap();

        for (String joinField : joinFieldList){
            joinFieldMap.put(joinField, StringUtils.nvl(v.get(joinField), ""));
        }
        for (String queryField : queryFieldList){
            queryFieldMap.put(queryField, v.get(queryField));
        }
        for (String showField : showFieldList){
            showFieldMap.put(showField, StringUtils.nvl(v.get(showField), ""));
        }
        CheckCommonFinanceDataEntity checkCommonData = new CheckCommonFinanceDataEntity();
        checkCommonData.setBusinessType(sqlMark);
        checkCommonData.setDbCode(SystemEnum.CWZT.getCode());
        checkCommonData.setJoinField(JSONObject.toJSONString(joinFieldMap));
        checkCommonData.setQueryField(JSONObject.toJSONString(queryFieldMap));
        checkCommonData.setShowField(JSONObject.toJSONString(showFieldMap));
        checkCommonData.setExecuteDate(executeDate);
        checkCommonData.setPeriodCode(periodCode);
        return checkCommonData;
    }

    private CheckAccountDetailRecordEntity saveCheckRecords(Integer periodCode, String checkType, String target) {
        String today = DateUtil.format(DateUtil.toLocalDateTime(new Date()), "yyyy-MM-dd");
        LambdaUpdateWrapper<CheckAccountDetailRecordEntity> wrapper = new LambdaUpdateWrapper<CheckAccountDetailRecordEntity>()
                .eq(CheckAccountDetailRecordEntity::getPeriodCode, periodCode)
                .eq(CheckAccountDetailRecordEntity::getCheckTarget, target)
                .eq(CheckAccountDetailRecordEntity::getExecuteStatus, CheckExecuteStatusEnum.FINISH.getCode())
                .apply("to_char(end_time, 'YYYY-MM-DD') = {0}", today)
                .set(CheckAccountDetailRecordEntity::getDelFlag, "1");
        this.update(wrapper);

        CheckAccountDetailRecordEntity checkAccountDetailRecordEntity = new CheckAccountDetailRecordEntity();
        checkAccountDetailRecordEntity.setCheckType(checkType);
        checkAccountDetailRecordEntity.setExecuteStatus(CheckExecuteStatusEnum.INPROGRESS.getCode());
        checkAccountDetailRecordEntity.setPeriodCode(periodCode);
        checkAccountDetailRecordEntity.setSubmitBy(StringUtils.equals(CheckTypeEnum.JOB.getCode(), checkType)?"System":SecurityUtils.getUserId().toString());
        checkAccountDetailRecordEntity.setStartTime(DateUtil.toLocalDateTime(new Date()));
        checkAccountDetailRecordEntity.setCheckTarget(target);
        this.save(checkAccountDetailRecordEntity);
        return checkAccountDetailRecordEntity;
    }

    private List<CheckAccountDetailRecordEntity> getInProgressRecords(String target) {
        List<CheckAccountDetailRecordEntity> inProgressRecords =
                this.list(new LambdaQueryWrapper<CheckAccountDetailRecordEntity>()
                        .eq(CheckAccountDetailRecordEntity::getExecuteStatus, CheckExecuteStatusEnum.INPROGRESS.getCode())
                        .eq(CheckAccountDetailRecordEntity::getCheckTarget, target));
        return inProgressRecords;
    }

    private CheckAccountDetailRecordVO getVo(CheckAccountDetailRecordEntity record) {
        CheckAccountDetailRecordVO vo = BeanUtil.copyProperties(record, CheckAccountDetailRecordVO.class);
        vo.setVersion(LocalDateTimeUtil.format(vo.getCreateTime(), "yyyy-MM-dd")+"_"+vo.getId());
        return vo;
    }

    private List<List<CheckAccountDetailResultHisVO>> groupByList(List<CheckAccountDetailResultHisVO> sourceList, int pageSize){
        List<List<CheckAccountDetailResultHisVO>> groupedList = Lists.newArrayList();
        List<CheckAccountDetailResultHisVO> tmpList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(sourceList)&&pageSize>0){
            for(int i=0; i<sourceList.size();i++){
                if(i%pageSize==0){
                    if(!tmpList.isEmpty()){
                        groupedList.add(tmpList);
                    }
                    tmpList = Lists.newArrayList();
                }
                tmpList.add(sourceList.get(i));
            }
            if(!tmpList.isEmpty()){
                groupedList.add(tmpList);
            }
        }
        return groupedList;
    }

    public void checkDetail(Integer periodCode, String checkType) {
        if(periodCode==null){
            throw  new ServiceException("期间不能为空");
        }
        /**
         * 1.查看还有没有正在跑的核对记录(根据核对记录的状态和结束时间判断)
         * 2.新建对账记录
         * 3.如果是job触发且是每月最后一天，则需要将实时表的数据拷贝到历史表
         * 4.清空实时表
         * 5.将eg_account表 tomap key:account code value:科目查询字段
         * 6.将eg_account_assist_balance表用期间过滤后，提取distinct account_code
         * 7.循环account_code根据periodCode(如果periodCode==当月则关联eg_contract_balance_latest，
         *   否则管理eg_contract_balance) 组装出list<CheckAccountDetailResultEntity>
         * 8.保存list<CheckAccountDetailResultEntity>
         * 9.修改对账记录状态
         */

        QueryWrapper<AccountAssistBalanceEntity> queryWrapper = new QueryWrapper<AccountAssistBalanceEntity>();
        queryWrapper.select("distinct account_code").eq("period_code", String.valueOf(periodCode));
        List<String> accountCodeList = accountAssistBalanceService.list(queryWrapper).stream().map(AccountAssistBalanceEntity::getAccountCode).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(accountCodeList)){
            throw new ServiceException("period: "+periodCode+" 没有找到对应的科目余额数据");
        }

        CheckAccountDetailRecordEntity record = saveCheckRecords(periodCode, checkType, CheckTargetEnum.DETAIL.getCode());
        log.info("保存核对记录结束");

        if(StringUtils.equals(CheckTypeEnum.JOB.getCode(), checkType)
                && isLastDayOfMonth()){
            checkAccountDetailResultHisService.saveResultToHis();
            log.info("保存实时表数据到历史表结束");
        }

        boolean currentMonth = isCurrentMonth(periodCode);
        //如果是实时对账则插入对账结果实时表，如果是历史期间对账则插入对账结果历史表
        if(currentMonth){
            checkAccountDetailResultService.clearTableData();
        }else{
            checkAccountDetailResultHisService.clearHisTableData(periodCode);
            checkAccountDetailResultHisService.clearContractTmpTableData();
        }
        log.info("实时表清理结束");

        List<AccountEntity> accountEntityList = accountService.list();
        Map<String, String> accountMap =
                accountEntityList.stream().filter(v->StringUtils.isNotEmpty(v.getFundType())).collect(Collectors.toMap(AccountEntity::getAccountCode, AccountEntity::getFundType,(k, v) -> k));
        log.info("accountMap组装结束"+accountMap.keySet().size());

        log.info("accountAssistBalance accountCodeList组装结束 size:"+accountCodeList.size());

        // 创建一个CompletableFuture
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            // 异步执行的任务
            if(!currentMonth){
                log.info("将账期下合同表的最新数据保存到临时表 开始 periodCode:"+ periodCode);
//                List<CheckAccountDetailContractBalanceEntity> list = checkAccountDetailContractBalanceService.selectContractTmpTableData(periodCode);
                checkAccountDetailResultHisService.insertContractTmpTableData(periodCode);
//                checkAccountDetailContractBalanceService.saveTmpBatch(list, periodCode);
                log.info("将账期下合同表的最新数据保存到临时表 结束 periodCode:"+ periodCode);
            }

        }, asyncTaskExecutor).thenRun(() -> {
//            CheckAccountDetailRecordEntity latestRecord = getLastRecord(CheckTargetEnum.DETAIL.getCode());

            // 下一个异步任务
            log.info("将科目余额与明细余额对账数据保存 开始 periodCode:"+ periodCode);
            if(currentMonth){
//                checkAccountDetailResultService.queryAndSaveCheckResultDtoAsync(periodCode, checkType, accountCodeList, accountMap, latestRecord);
                List<CheckAccountDetailResultEntity> list = checkAccountDetailResultService.queryCheckResultDto(periodCode, checkType, accountCodeList, accountMap, record);
                this.saveResultBatch(periodCode.toString(), list);
            }else{
//                checkAccountDetailResultHisService.queryAndSaveCheckResultDtoAsync(periodCode, checkType, accountCodeList, accountMap, latestRecord);
                List<CheckAccountDetailResultHisEntity> hisList = checkAccountDetailResultHisService.queryCheckResultDto(periodCode, checkType, accountCodeList, accountMap, record);
                this.saveResultHisBatch(periodCode.toString(), hisList);
            }
            log.info("将科目余额与明细余额对账数据保存 结束 periodCode:"+ periodCode);

            //修改record状态
            record.setExecuteStatus(CheckExecuteStatusEnum.FINISH.getCode());
            record.setEndTime(LocalDateTime.now());
            this.updateById(record);
            log.info("更新记录状态 recordId:"+ record.getId());
        });

    }

    private void saveResultBatch(String periodCode, List<CheckAccountDetailResultEntity> list) {
        log.info("同步科目余额与明细余额数据开始 保存到实时表 periodCode:{}  转换后数据量:{}", periodCode, list.size());
        List<List<CheckAccountDetailResultEntity>> entityPage = ListUtil.partition(list, 5000);
        int pageCount = entityPage.size();
        CountDownLatch countDownLatch = new CountDownLatch(pageCount);
        log.info("同步科目余额与明细余额数据 保存到实时表 分组完成  periodCode:{}, pageCount:{}", periodCode, pageCount);
        int i = 1;
        for (List<CheckAccountDetailResultEntity> entityList: entityPage){
            checkAccountDetailResultService.batchSaveToResult(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount-1).toString()),2), periodCode);
            i++;
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("同步科目余额与明细余额数据，保存实时表完成 入库 periodCode:{}  转换后数据量:{}", periodCode, list.size());
    }

    private void saveResultHisBatch(String periodCode, List<CheckAccountDetailResultHisEntity> list) {
        log.info("同步科目余额与明细余额数据开始 保存到历史表 periodCode:{}  转换后数据量:{}", periodCode, list.size());
        List<List<CheckAccountDetailResultHisEntity>> entityPage = ListUtil.partition(list, 5000);
        int pageCount = entityPage.size();
        CountDownLatch countDownLatch = new CountDownLatch(pageCount);
        log.info("同步科目余额与明细余额数据 保存到历史表 分组完成  periodCode:{}, pageCount:{}", periodCode, pageCount);
        int i = 1;
        for (List<CheckAccountDetailResultHisEntity> entityList: entityPage){
            checkAccountDetailResultHisService.batchSaveToResultHis(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount-1).toString()),2), periodCode);
            i++;
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("同步科目余额与明细余额数据，保存历史表完成 入库 periodCode:{}  转换后数据量:{}", periodCode, list.size());
    }

    private CheckAccountDetailRecordEntity getLastRecord(String checkTarget) {
        CheckAccountDetailRecordEntity latestRecord = this.getOne(new LambdaQueryWrapper<CheckAccountDetailRecordEntity>()
                .eq(CheckAccountDetailRecordEntity::getExecuteStatus, CheckExecuteStatusEnum.INPROGRESS.getCode())
                .eq(CheckAccountDetailRecordEntity::getCheckTarget, checkTarget)
                .orderByDesc(CheckAccountDetailRecordEntity::getStartTime));
        return latestRecord;
    }

    private static boolean isCurrentMonth(Integer periodCode) {
        boolean currentMonth = Integer.valueOf(DateUtil.format(DateUtil.toLocalDateTime(new Date()), "yyyyMM")).compareTo(periodCode) ==0;
        return currentMonth;
    }

}

