package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.SpringUtils;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.utfinancing.financehub.etl.commveh.service.ICommercialVehicleDataService;
import com.utfinancing.financehub.etl.commvehat.service.ICommercialVehicleAssetTransferDataService;
import com.utfinancing.financehub.etl.financial.entity.CheckCommonDataEntity;
import com.utfinancing.financehub.etl.financial.entity.CheckCommonDataResultEntity;
import com.utfinancing.financehub.etl.financial.entity.CheckCommonFieldEntity;
import com.utfinancing.financehub.etl.financial.entity.CheckSqlEntity;
import com.utfinancing.financehub.etl.financial.enums.CheckDBCodeEnum;
import com.utfinancing.financehub.etl.financial.mapper.CheckSqlMapper;
import com.utfinancing.financehub.etl.financial.service.ICheckCommonDataResultService;
import com.utfinancing.financehub.etl.financial.service.ICheckCommonDataService;
import com.utfinancing.financehub.etl.financial.service.ICheckCommonFieldService;
import com.utfinancing.financehub.etl.financial.service.ICheckSqlService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.micro.service.IMicroDataService;
import com.utfinancing.financehub.etl.passveh.service.IPassengerVehicleDataService;
import com.utfinancing.financehub.etl.passvehat.service.IPassengerVehicleAssetTransferDataService;
import com.utfinancing.financehub.etl.platform.service.IPlatformDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-29
 * @Description :  CheckSql服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
//@Transactional
public class CheckSqlServiceImpl extends ServiceImpl<CheckSqlMapper, CheckSqlEntity> implements ICheckSqlService {

    private final CheckSqlMapper checkSqlMapper;

    @Resource
    private ICheckCommonDataService checkCommonDataService;

    @Resource
    private ICheckCommonFieldService checkCommonFieldService;

    @Resource
    private ICommercialVehicleDataService commercialVehicleDataService;

    @Resource
    private IPassengerVehicleDataService passengerVehicleDataService;

    @Resource
    private ICommercialVehicleAssetTransferDataService commercialVehicleAssetTransferDataService;

    @Resource
    private IPassengerVehicleAssetTransferDataService passengerVehicleAssetTransferDataService;

    @Resource
    private IMicroDataService microDataService;

    @Resource
    private IPlatformDataService platformDataService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private ICheckCommonDataResultService checkCommonDataResultService;

    @Override
    public String saveCommonToTmp(Integer periodCode, String sqlMark) {
        List<CheckSqlEntity> checkSqlList = this.list(new LambdaQueryWrapper<CheckSqlEntity>()
                .eq(CheckSqlEntity::getSqlMark, sqlMark).orderByAsc(CheckSqlEntity::getSqlOrder));

        if(CollectionUtil.isEmpty(checkSqlList)){
//            throw new ServiceException("没有找到有效的sql信息");
            return "没有找到有效的sql信息";
        }
        String executeDateCode = DateUtil.format(DateUtil.toLocalDateTime(new Date()), "yyyyMMdd");

//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            checkCommonDataService.clearTableData(executeDateCode, sqlMark, periodCode);
            for (CheckSqlEntity checkSql : checkSqlList){
                String querySql = checkSql.getQuerySql();
                String systemCode = checkSql.getDbCode();
                final String finalSql = getFinalSql(periodCode, querySql);
//                final String finalSql = " select t.*, 12.11 AS field_b  from abs050708 t";
                List<Map<String, Object>> list = getTmpDataByDbCodeAndSql(systemCode, finalSql);

                if(CollectionUtil.isEmpty(list)){
                    log.info("periodCode:{} systemCode:{} sqlMark:{} 没有查询到业务系统数据", periodCode, systemCode, sqlMark);
//                    throw new ServiceException("systemCode "+systemCode+" sqlMark "+sqlMark+" periodCode "+periodCode+" 获取到没有获取到数据");
                    continue;
                }
                List<CheckCommonDataEntity> commonDataList = getCheckCommonDataList(systemCode, sqlMark, list, executeDateCode, periodCode);

                saveCommonToTmp(periodCode, systemCode, sqlMark, commonDataList);
            }
//        }, threadPoolTaskExecutor).thenRunAsync(() -> {
            checkCommonDataResultService.clearTableData(executeDateCode, sqlMark, periodCode);

            List<CheckCommonDataEntity> commonDataList = checkCommonDataService.list(new LambdaQueryWrapper<CheckCommonDataEntity>()
                    .eq(CheckCommonDataEntity::getBusinessType, sqlMark)
                    .eq(CheckCommonDataEntity::getPeriodCode, periodCode)
                    .apply("to_char(execute_date, 'yyyyMMdd') = {0} ", executeDateCode));

            if(CollectionUtil.isEmpty(commonDataList)){
//                throw new ServiceException("没有找到 executeDateCode:"+executeDateCode+" periodCode:"+periodCode+" sqlMark:"+sqlMark+" 下的原始表数据");
                log.info("没有找到 executeDateCode:"+executeDateCode+" periodCode:"+periodCode+" sqlMark:"+sqlMark+" 下的原始表数据");
                return "没有找到 executeDateCode:"+executeDateCode+" periodCode:"+periodCode+" sqlMark:"+sqlMark+" 下的原始表数据";
            }
            //先根据systemCode，再根据joinField分组
            Map<String, Map<String, List<CheckCommonDataEntity>>> totalCommonDataMap = commonDataList.stream()
                    .collect(Collectors.groupingBy(CheckCommonDataEntity::getDbCode, Collectors.groupingBy(CheckCommonDataEntity::getJoinField, Collectors.toList())));

            totalCommonDataMap.forEach((systemCodeKey, systemCodeVal)->{
                List<CheckCommonDataResultEntity> resultList = Lists.newArrayList();

//                Map<String, List<CheckCommonDataEntity>> commonDataMap = commonDataList.stream().collect(Collectors.groupingBy(CheckCommonDataEntity::getJoinField));
                String systemCode = systemCodeKey;
                systemCodeVal.forEach((k,v)->{
                    Map<String, Object> queryFieldMap = Maps.newLinkedHashMap();
                    List<CheckCommonDataEntity> sortedList = v.stream().sorted(Comparator.comparing(CheckCommonDataEntity::getId)).collect(Collectors.toList());
                    CheckCommonDataResultEntity resultEntity = new CheckCommonDataResultEntity();
                    for(CheckCommonDataEntity tmp : sortedList){
                        String queryField = tmp.getQueryField();
                        JSONObject jsonObject = JSON.parseObject(queryField);

                        queryFieldMap.putAll(jsonObject.getInnerMap());
                    }

                    resultEntity.setBusinessType(sqlMark);
                    resultEntity.setDbCode(systemCode);
                    resultEntity.setJoinField(k);
                    resultEntity.setQueryField(JSONObject.toJSONString(queryFieldMap));
                    resultEntity.setShowField(v.get(0).getShowField());
                    resultEntity.setExecuteDate(LocalDateTime.now());
                    resultEntity.setPeriodCode(periodCode);
                    resultList.add(resultEntity);
                });
                saveCommonToResultTmp(periodCode, systemCode, sqlMark, resultList);
            });


//        }, threadPoolTaskExecutor);

        return "success";
    }

    private static String getFinalSql(Integer periodCode, String querySql) {
        LocalDateTime periodMonth = LocalDateTimeUtil.parse(periodCode.toString(), "yyyyMM");
        String monthDayStart = LocalDateTimeUtil.format(periodMonth.minusMonths(1).plusDays(1), "yyyy-MM-dd");
        String monthDayEnd = LocalDateTimeUtil.format(periodMonth.plusMonths(1).minusDays(1), "yyyy-MM-dd");

        return querySql.replaceAll("#monthDayStart#", monthDayStart)
                .replaceAll("#monthDayEnd#", monthDayEnd)
                .replaceAll("#periodCode#", periodCode+"");
    }

    private List<Map<String, Object>> getTmpDataByDbCodeAndSql(String systemCode, String finalSql) {
        List<Map<String, Object>> list = Lists.newArrayList();

        if(StringUtils.equals(CheckDBCodeEnum.COMMERCIAL_VEHICLE.getCode(), systemCode)){
            list = commercialVehicleDataService.selectDataCommon(finalSql);
        } else if(StringUtils.equals(CheckDBCodeEnum.PASSENGER_VEHICLE.getCode(), systemCode)){
            list = passengerVehicleDataService.selectDataCommon(finalSql);
        } else if(StringUtils.equals(CheckDBCodeEnum.COMMERCIAL_VEHICLE_ASSET_TRANSFER.getCode(), systemCode)){
            list = commercialVehicleAssetTransferDataService.selectDataCommon(finalSql);
        } else if(StringUtils.equals(CheckDBCodeEnum.PASSENGER_VEHICLE_ASSET_TRANSFER.getCode(), systemCode)){
            list = passengerVehicleAssetTransferDataService.selectDataCommon(finalSql);
        } else if(StringUtils.equals(CheckDBCodeEnum.XW.getCode(), systemCode)){
            list = microDataService.selectDataCommon(finalSql);
        } else if(StringUtils.equals(CheckDBCodeEnum.PLATFORM.getCode(), systemCode)){
            list = platformDataService.selectDataCommon(finalSql);
        }
        return list;
    }

    private List<CheckCommonDataEntity> getCheckCommonDataList(String systemCode, String sqlMark, List<Map<String, Object>> list, String executeDateCode, Integer periodCode) {
        List<CheckCommonFieldEntity> fieldList = checkCommonFieldService.list(
                new LambdaQueryWrapper<CheckCommonFieldEntity>()
                        .eq(CheckCommonFieldEntity::getBusinessType, sqlMark));
        if(CollectionUtil.isEmpty(fieldList)){
            throw new ServiceException("systemCode "+ systemCode +" sqlMark "+ sqlMark +" 获取获取查询字段失败");
        }
        List<String > joinFieldList = fieldList.stream().filter(v -> StringUtils.equals(v.getFieldType(), "1"))
                .sorted(Comparator.comparing(CheckCommonFieldEntity::getFieldOrder)).map(CheckCommonFieldEntity::getCommonField).collect(Collectors.toList());

        List<String> queryFieldList = fieldList.stream().filter(v -> StringUtils.equals(v.getFieldType(), "2"))
                .sorted(Comparator.comparing(CheckCommonFieldEntity::getFieldOrder)).map(CheckCommonFieldEntity::getCommonField).collect(Collectors.toList());

        List<String> showFieldList = fieldList.stream().filter(v -> StringUtils.equals(v.getFieldType(), "3"))
                .sorted(Comparator.comparing(CheckCommonFieldEntity::getFieldOrder)).map(CheckCommonFieldEntity::getCommonField).collect(Collectors.toList());

        return list.stream().map(v-> {
            return getCheckCommonData(systemCode, sqlMark, v, joinFieldList, queryFieldList, showFieldList, executeDateCode, periodCode);
        }).collect(Collectors.toList());
    }

    private void saveCommonToTmp(Integer periodCode, String systemCode, String sqlMark, List<CheckCommonDataEntity> commonDataList) {
        log.info("同步其他业务系统数据开始保存到原始表 入库 dbCode:{} sqlMark:{} periodCode:{}  转换后数据量:{}", systemCode, sqlMark, periodCode, commonDataList.size());
        List<List<CheckCommonDataEntity>> entityPage = ListUtil.partition(commonDataList, 1000);
        ICheckSqlService checkSqlService = SpringUtils.getBean(ICheckSqlService.class);
        int pageCount = entityPage.size();
        CountDownLatch countDownLatch = new CountDownLatch(pageCount-1);
        log.info("同步其他业务系统数据数据保存到原始表 分组完成 dbCode:{} sqlMark:{}  periodCode:{}, pageCount:{}", systemCode, sqlMark, periodCode, pageCount);
        int i = 1;

        //要先保存第一组，并发时创建分区表
        checkCommonDataService.saveBatch(entityPage.get(0));
        if(pageCount>1){
            for (List<CheckCommonDataEntity> entityList: entityPage.stream().skip(1).collect(Collectors.toList())){
                checkSqlService.batchSaveToTmp(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount-1).toString()),2), periodCode, systemCode, sqlMark);
                i++;
            }

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        log.info("同步其他业务系统数据数据完成保存到原始表 入库 dbCode:{} sqlMark:{}  periodCode:{}  转换后数据量:{}", systemCode, sqlMark, periodCode, commonDataList.size());
    }

    private void saveCommonToResultTmp(Integer periodCode, String systemCode, String sqlMark, List<CheckCommonDataResultEntity> commonDataResultList) {
        log.info("同步其他业务系统数据开始保存到结果表 入库 dbCode:{} sqlMark:{} periodCode:{}  转换后数据量:{}", systemCode, sqlMark, periodCode, commonDataResultList.size());
        List<List<CheckCommonDataResultEntity>> entityPage = ListUtil.partition(commonDataResultList, 1000);
        ICheckSqlService checkSqlService = SpringUtils.getBean(ICheckSqlService.class);
        int pageCount = entityPage.size();
        CountDownLatch countDownLatch = new CountDownLatch(pageCount-1);
        log.info("同步其他业务系统数据数据保存到结果表 分组完成 dbCode:{} sqlMark:{}  periodCode:{}, pageCount:{}", systemCode, sqlMark, periodCode, pageCount);
        int i = 1;

        //要先保存第一组，并发时创建分区表
        checkCommonDataResultService.saveBatch(entityPage.get(0));

        if(pageCount>1){
            for (List<CheckCommonDataResultEntity> entityList: entityPage.stream().skip(1).collect(Collectors.toList())){
                checkSqlService.batchSaveToResultTmp(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount-1).toString()),2), periodCode, systemCode, sqlMark);
                i++;
            }

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        log.info("同步其他业务系统数据数据完成保存到结果表 入库 dbCode:{} sqlMark:{}  periodCode:{}  转换后数据量:{}", systemCode, sqlMark, periodCode, commonDataResultList.size());
    }

    private static CheckCommonDataEntity getCheckCommonData(String systemCode, String sqlMark, Map<String, Object> v, List<String> joinFieldList, List<String> queryFieldList, List<String> showFieldList, String executeDateCode, Integer periodCode) {
        Map<String, Object> joinFieldMap = Maps.newLinkedHashMap();
        Map<String, Object> queryFieldMap = Maps.newLinkedHashMap();
        Map<String, Object> showFieldMap = Maps.newLinkedHashMap();

        for (String joinField : joinFieldList){
            joinFieldMap.put(joinField, StringUtils.nvl(v.get(joinField),""));
        }
        for (String queryField : queryFieldList){
            queryFieldMap.put(queryField, v.get(queryField));
        }
        for (String showField : showFieldList){
            showFieldMap.put(showField, StringUtils.nvl(v.get(showField),""));
        }
        CheckCommonDataEntity checkCommonData = new CheckCommonDataEntity();
        checkCommonData.setBusinessType(sqlMark);
        checkCommonData.setDbCode(systemCode);
        checkCommonData.setJoinField(JSONObject.toJSONString(joinFieldMap));
        checkCommonData.setQueryField(JSONObject.toJSONString(queryFieldMap));
        checkCommonData.setShowField(JSONObject.toJSONString(showFieldMap));
        checkCommonData.setExecuteDate(LocalDateTime.now());
        checkCommonData.setPeriodCode(periodCode);
        return checkCommonData;
    }

    @Async
    @Override
    public void batchSaveToTmp(CountDownLatch countDownLatch, List<CheckCommonDataEntity> entityList, String percent, Integer periodCode, String systemCode, String sqlMark) {
        checkCommonDataService.saveBatch(entityList);
        log.info("同步其他业务系统数据数据保存到原始表 单页保存完成.dbCode:{} sqlMark:{} periodCode:{}, 完成比例:{}", systemCode, sqlMark, periodCode, percent);
        countDownLatch.countDown();
    }

    @Async
    @Override
    public void batchSaveToResultTmp(CountDownLatch countDownLatch, List<CheckCommonDataResultEntity> entityList, String percent, Integer periodCode, String systemCode, String sqlMark) {
        checkCommonDataResultService.saveBatch(entityList);
        log.info("同步其他业务系统数据数据保存到结果表 单页保存完成.dbCode:{} sqlMark:{} periodCode:{}, 完成比例:{}", systemCode, sqlMark, periodCode, percent);
        countDownLatch.countDown();
    }
}

