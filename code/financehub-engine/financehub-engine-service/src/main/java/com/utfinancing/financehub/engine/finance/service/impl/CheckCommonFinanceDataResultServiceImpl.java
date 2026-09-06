package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.CheckDBCodeEnum;
import com.utfinancing.financehub.engine.finance.constant.CheckSuffixConstant;
import com.utfinancing.financehub.engine.finance.entity.CheckCommonFieldEntity;
import com.utfinancing.financehub.engine.finance.entity.CheckSqlEntity;
import com.utfinancing.financehub.engine.finance.entity.ClientEntity;
import com.utfinancing.financehub.engine.finance.model.dto.CheckCommonFinanceDataResultQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckCommonFinanceDataResultDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountDetailResultHisVO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckCommonFinanceDataResultVO;
import com.utfinancing.financehub.engine.finance.entity.CheckCommonFinanceDataResultEntity;
import com.utfinancing.financehub.engine.finance.mapper.CheckCommonFinanceDataResultMapper;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @Author : jnc
 * @Date : Create in 2024-04-02
 * @Description :  CheckCommonFinanceDataResult服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CheckCommonFinanceDataResultServiceImpl extends ServiceImpl<CheckCommonFinanceDataResultMapper, CheckCommonFinanceDataResultEntity> implements ICheckCommonFinanceDataResultService {

    private final CheckCommonFinanceDataResultMapper checkCommonFinanceDataResultMapper;

    @Resource
    private ICheckCommonFieldService checkCommonFieldService;

    @Resource
    IAccountService iAccountService;

    @Resource
    IClientService iClientService;

    @Resource
    IOrgCompanyService iOrgCompanyService;

    @Resource
    ICheckSqlService checkSqlService;

//    @Async
    @Override
    public void batchSaveToResultTmp(CountDownLatch countDownLatch, List<CheckCommonFinanceDataResultEntity> entityList, String percent, String periodCode, String systemCode, String sqlMark) {
        this.saveBatch(entityList);
        log.info("同步其他业务系统数据数据保存到结果表 单页保存完成.dbCode:{} sqlMark:{} periodCode:{}, 完成比例:{}", systemCode, sqlMark, periodCode, percent);
//        countDownLatch.countDown();
    }


    @Override
    public IPage<Map<String, Object>> selectCommonPage(CheckCommonFinanceDataResultQueryDTO queryDTO) {
        Long recordId = queryDTO.getRecordId();
        String systemCode = queryDTO.getDbCode();
        String businessType = queryDTO.getBusinessType();
        if(recordId==null||StringUtils.isEmpty(businessType)){
            throw new ServiceException("记录ID,业务场景均不能为空");
        }
        Map<String, Object> param = queryDTO.getParam();
        List<CheckCommonFieldEntity> fieldList = checkCommonFieldService.list(new LambdaQueryWrapper<CheckCommonFieldEntity>()
//                .eq(CheckCommonFieldEntity::getDbCode, systemCode)
                .eq(CheckCommonFieldEntity::getBusinessType, businessType)
                .orderByAsc(CheckCommonFieldEntity::getFieldOrder));

        List<CheckSqlEntity> sqlList = checkSqlService.list(new LambdaQueryWrapper<CheckSqlEntity>().eq(CheckSqlEntity::getSqlMark, businessType).orderByAsc(CheckSqlEntity::getSqlOrder));
        List<String> systemList = sqlList.stream().map(CheckSqlEntity::getDbCode).distinct().collect(Collectors.toList());


        LambdaQueryWrapper<CheckCommonFinanceDataResultEntity> queryWrapper = generateQueryWrapper(recordId, systemCode, businessType, param, fieldList);
        IPage<CheckCommonFinanceDataResultEntity> entityIPage = checkCommonFinanceDataResultMapper.selectPage(new Page<CheckCommonFinanceDataResultEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        List<CheckCommonFinanceDataResultEntity> records = entityIPage.getRecords();
        List<Map<String, Object>> mapRecords = Lists.newArrayList();
        List<CheckCommonFieldEntity> queryFieldList = fieldList.stream().filter(x->StringUtils.equals("2", x.getFieldType())).collect(Collectors.toList());
        //转换成Map
        if(CollectionUtil.isNotEmpty(records)){
            records.stream().forEach(v->{
                Map<String, Object> map = Maps.newLinkedHashMap();
                for(CheckCommonFieldEntity field : fieldList){
                    putCommonField(v, field, map, systemList);
                }
                if(CollectionUtil.isNotEmpty(queryFieldList)){
                    putCompareField(v, queryFieldList, map);
                }
                map.put("systemCode", v.getDbCode());
                mapRecords.add(map);
            });
        }
        /**
         * TODO 自动组装特殊字段
         * "ORG_ID","CLIENT_CODE","ACCOUNT_CODE"
         * "ORG_NAME","CLIENT_NAME","ACCOUNT_NAME"
         */
        fillSpecialName(fieldList, mapRecords);


        IPage<Map<String, Object>> page = transIntoMapPage(entityIPage, mapRecords);
        return page;
    }

    private void fillSpecialName(List<CheckCommonFieldEntity> fieldList, List<Map<String, Object>> mapRecords) {
        List<String> descFieldList = Arrays.asList("ORG_ID","CLIENT_CODE","ACCOUNT_CODE");

        List<String> fieldColumn = fieldList.stream().map(CheckCommonFieldEntity::getCommonField).collect(Collectors.toList());
        List<String> remainDescFieldList = descFieldList.stream().filter(fieldColumn::contains).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(remainDescFieldList)){
            Map<String,String> orgNameByOrgIdMap = null;
            Map<String, String> clientMap = null;
            Map<String, String> accountMap = null;
            for(String colum : remainDescFieldList){
                switch (colum){
                    case "ORG_ID":
                        orgNameByOrgIdMap = getOrgNameOrgId();
                        break;
                    case "CLIENT_CODE":
                        List<String> clientCodeList = mapRecords.stream().map(v->(String)v.get("ORG_ID")).distinct().collect(Collectors.toList());;
                        clientMap = getClientNameClientCode(clientCodeList);
                        break;
                    case "ACCOUNT_CODE":
                        accountMap = getAccountNameAccountCode();
                        break;
                    default: break;
                }
            }

            for (Map<String, Object> map : mapRecords) {
                if (CollectionUtil.isNotEmpty(orgNameByOrgIdMap)) {
                    map.put("ORG_NAME", orgNameByOrgIdMap.get((String) map.get("ORG_ID")));
                }
                if (CollectionUtil.isNotEmpty(clientMap)) {
                    map.put("CLIENT_NAME", clientMap.get((String) map.get("CLIENT_CODE")));
                }
                if (CollectionUtil.isNotEmpty(accountMap)) {
                    map.put("ACCOUNT_NAME", accountMap.get((String) map.get("ACCOUNT_CODE")));
                }
            }
        }
    }


    private Map<String, String> getAccountNameAccountCode() {
        Map<String,String> accountMap = new HashMap<>();
        List<AccountEntity> accountEntityList = iAccountService.list();
        if (CollectionUtils.isNotEmpty(accountEntityList)) {
            accountMap = accountEntityList.stream().collect(HashMap::new,(map,item)->map.put(item.getAccountCode(),item.getAccountName()),HashMap::putAll);
        }
        return accountMap;
    }

    private Map<String, String> getClientNameClientCode(List<String> clientCodeList) {
        Map<String,String> clientMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(clientCodeList)) {
            List<ClientEntity> clientEntityList = iClientService.lambdaQuery().in(ClientEntity::getClientCode,clientCodeList).list();
            if (CollectionUtils.isNotEmpty(clientEntityList)) {
                clientMap = clientEntityList.stream().collect(HashMap::new,(map,item)->map.put(item.getClientCode(),item.getClientName()),HashMap::putAll);
            }
        }
        return clientMap;
    }

    private Map<String,String> getOrgNameOrgId(){
        Map<String,String> orgNameAndIdMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  iOrgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgNameAndIdMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId,OrgCompanyVO::getOrgName, (k1,k2)->k2));
        }
        return orgNameAndIdMap;
    }

    @Override
    public void deleteCheckCommonData(String businessType) {
        checkCommonFinanceDataResultMapper.deleteCheckCommonData(businessType);
    }

    @Override
    public void deleteCheckCommonDataByExecuteDate(Integer executeDateCode, String businessType, Integer periodCode) {
        checkCommonFinanceDataResultMapper.deleteCheckCommonDataByExecuteDate(executeDateCode, businessType, periodCode);
    }

    private static LambdaQueryWrapper<CheckCommonFinanceDataResultEntity> generateQueryWrapper(Long recordId, String systemCode, String businessType, Map<String, Object> param, List<CheckCommonFieldEntity> fieldList) {
        LambdaQueryWrapper<CheckCommonFinanceDataResultEntity> queryWrapper = new LambdaQueryWrapper<CheckCommonFinanceDataResultEntity>();
        queryWrapper.eq(CheckCommonFinanceDataResultEntity::getRecordId, recordId)
//                .eq(CheckCommonFinanceDataResultEntity::getDbCode, systemCode)
                .eq(CheckCommonFinanceDataResultEntity::getBusinessType, businessType);

        if (CollectionUtil.isNotEmpty(param)){
            param.forEach((k, v)->{
                Optional<CheckCommonFieldEntity> op =
                        fieldList.stream().filter(x->StringUtils.equals(x.getCommonField(), k)).findFirst();
                Optional<CheckCommonFieldEntity> suffixOp =
                        fieldList.stream().filter(x->StringUtils.equals(x.getCommonField()+CheckSuffixConstant.DIFF_FLAG, k)).findFirst();
                //处理参数是传list情况_LIST
                Optional<CheckCommonFieldEntity> listOp =
                        fieldList.stream().filter(x->StringUtils.equals(x.getCommonField()+CheckSuffixConstant.LIST, k)).findFirst();

                if (op.isPresent()){
                    queryWrapper.apply("(" +
                            "join_field ::json->>'"+StringUtils.upperCase(op.get().getCommonField())+"' = '"+v +"' " +
                            "or show_field ::json->>'"+StringUtils.upperCase(op.get().getCommonField())+"' = '"+v +"' " +
//                            "or compare_flag_field ::json->>'"+StringUtils.upperCase(op.get().getCommonField())+"' = '"+v+"'" +
                            ")");
                }else if(suffixOp.isPresent()){
                    queryWrapper.apply("(" +
                            "compare_flag_field ::json->>'"+StringUtils.upperCase(suffixOp.get().getCommonField())+CheckSuffixConstant.DIFF_FLAG+"' = '"+v+"'" +
                            ")");
                }else if(listOp.isPresent()){
                    List<String> tmpList = (List<String>) v;
                    StringBuffer value = new StringBuffer();
                    if(CollectionUtil.isNotEmpty(tmpList)){
                        for(String tmp : tmpList){
                            value.append("'").append(tmp).append("',");
                        }
                        String finalValue = value.substring(0, value.lastIndexOf(","));
                        queryWrapper.apply("(" +
                                "join_field ::json->>'"+StringUtils.upperCase(listOp.get().getCommonField())+"' in ("+finalValue+") " +
                                "or show_field ::json->>'"+StringUtils.upperCase(listOp.get().getCommonField())+"' in ("+finalValue+") " +
                                ")");
                    }

                }
            });
        }
        return queryWrapper;
    }

    private static IPage<Map<String, Object>> transIntoMapPage(IPage<CheckCommonFinanceDataResultEntity> entityIPage, List<Map<String, Object>> mapRecords) {
        IPage<Map<String, Object>> page = new Page<Map<String, Object>>();
        page.setPages(entityIPage.getPages());
        page.setCurrent(entityIPage.getCurrent());
        page.setSize(entityIPage.getSize());
        page.setTotal(entityIPage.getTotal());
        page.setRecords(mapRecords);
        return page;
    }

    private static void putCommonField(CheckCommonFinanceDataResultEntity v, CheckCommonFieldEntity field, Map<String, Object> map, List<String> systemList) {
        String dbCode = v.getDbCode();
        String joinField = v.getJoinField();
        String showField = v.getShowField();
        String queryField = v.getQueryField();

        // 使用fastjson将字符串转换为JSONObject
        JSONObject joinJsonObject = JSON.parseObject(joinField);
        JSONObject showJsonObject = JSON.parseObject(showField);
        JSONObject queryJsonObject = JSON.parseObject(queryField);


        // 查询某个字段，例如"name"
        String joinFieldValue = joinJsonObject.getString(field.getCommonField());
        if(joinFieldValue == null){
            String showFieldValue = showJsonObject.getString(field.getCommonField());
            if(showFieldValue == null){
                String queryFieldValue = queryJsonObject.getString(field.getCommonField());
                if(queryFieldValue == null){
//                    throw new ServiceException("组装返回对象出错");
                }else{
                    for(String systemCode : systemList){
                        if(StringUtils.equals(systemCode, dbCode)){
                            map.put(field.getCommonField()+"_"+CheckDBCodeEnum.getInstanceByCode(systemCode).getCode(), queryJsonObject.getString(field.getCommonField()));
                        }else{
                            map.put(field.getCommonField()+"_"+CheckDBCodeEnum.getInstanceByCode(systemCode).getCode(), "");
                        }
                    }
                }

            }else{
                map.put(field.getCommonField(), showJsonObject.getString(field.getCommonField()));
            }
        }else{
            map.put(field.getCommonField(), joinJsonObject.getString(field.getCommonField()));
        }

    }

    private static void putCompareField(CheckCommonFinanceDataResultEntity v, List<CheckCommonFieldEntity> queryFieldList, Map<String, Object> map) {
        String queryField = v.getQueryField();
        String compareFlagField = v.getCompareFlagField();
        String compareResultField = v.getCompareResultField();

        // 使用fastjson将字符串转换为JSONObject
        JSONObject queryJsonObject = JSON.parseObject(queryField);
        JSONObject compareFlagJsonObject = JSON.parseObject(compareFlagField);
        JSONObject compareResultJsonObject = JSON.parseObject(compareResultField);

        for(CheckCommonFieldEntity queryEntity : queryFieldList){
            map.put(queryEntity.getCommonField()+CheckSuffixConstant.FINANCE, queryJsonObject.getString(queryEntity.getCommonField()+CheckSuffixConstant.FINANCE));
        }
        for(CheckCommonFieldEntity queryEntity : queryFieldList){
            map.put(queryEntity.getCommonField()+ CheckSuffixConstant.DIFF_FLAG, compareFlagJsonObject.getString(queryEntity.getCommonField()+CheckSuffixConstant.DIFF_FLAG));
        }
        for(CheckCommonFieldEntity queryEntity : queryFieldList){
            map.put(queryEntity.getCommonField()+CheckSuffixConstant.DIFF, compareResultJsonObject.getString(queryEntity.getCommonField()+CheckSuffixConstant.DIFF));
        }
    }
}

