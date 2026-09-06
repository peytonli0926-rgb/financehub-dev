package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.approve.entity.ApproveEntity;
import com.utfinancing.financehub.engine.enums.CheckDiffEnum;
import com.utfinancing.financehub.engine.enums.CheckExecuteStatusEnum;
import com.utfinancing.financehub.engine.enums.CheckTypeEnum;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountDetailResultVO;
import com.utfinancing.financehub.engine.finance.mapper.CheckAccountDetailResultMapper;
import com.utfinancing.financehub.engine.finance.model.vo.ContractBalanceVO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-20
 * @Description :  CheckAccountDetailResult服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CheckAccountDetailResultServiceImpl extends ServiceImpl<CheckAccountDetailResultMapper, CheckAccountDetailResultEntity> implements ICheckAccountDetailResultService {

    private final CheckAccountDetailResultMapper checkAccountDetailResultMapper;

    @Resource
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Resource
    IAccountService iAccountService;

    @Resource
    IClientService iClientService;

    @Resource
    IOrgCompanyService iOrgCompanyService;


    @Override
    public Long saveCheckAccountDetailResult(CheckAccountDetailResultDTO dto) {
        CheckAccountDetailResultEntity entity = BeanUtil.copyProperties(dto, CheckAccountDetailResultEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateCheckAccountDetailResult(Long id, CheckAccountDetailResultDTO dto) {
        CheckAccountDetailResultEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public CheckAccountDetailResultDTO getCheckAccountDetailResultDTOById(Long id) {
        CheckAccountDetailResultEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, CheckAccountDetailResultDTO.class);
    }

    @Override
    public IPage<CheckAccountDetailResultVO> selectDetailPage(CheckAccountDetailResultQueryDTO queryDTO) {
        LambdaQueryWrapper<CheckAccountDetailResultEntity> queryWrapper = getQueryWrapper(queryDTO);
        IPage<CheckAccountDetailResultEntity> entityIPage = checkAccountDetailResultMapper.selectPage(new Page<CheckAccountDetailResultEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<CheckAccountDetailResultVO> page = ListBeanUtil.copyPage(entityIPage, CheckAccountDetailResultVO.class);
        List<CheckAccountDetailResultVO> list = page.getRecords();
        if(CollectionUtils.isNotEmpty(list)){
            Map<String,String> orgNameByOrgIdMap = getOrgNameOrgId();
            Map<String, String> clientMap = getClientNameClientCode(list);
            Map<String, String> accountMap = getAccountNameAccountCode();

            list.stream().forEach(v -> {
                if (orgNameByOrgIdMap.containsKey(v.getOrgId())) {
                    v.setOrgName(orgNameByOrgIdMap.get(v.getOrgId()));
                }
                if(clientMap.containsKey(v.getClientCode())){
                    v.setClientName(clientMap.get(v.getClientCode()));
                }
                if (accountMap.containsKey(v.getAccountCode())){
                    v.setAccountName(accountMap.get(v.getAccountCode()));
                }
                v.setDiffFlagDesc(StringUtils.equals(v.getDiffFlag(), CheckDiffEnum.NO_DIFF.getCode())?CheckDiffEnum.NO_DIFF.getDesc():CheckDiffEnum.DIFF.getDesc());
            });

        }
        page.setRecords(list);
        return page;
    }

    private static boolean isCurrentMonth(Integer periodCode) {
        boolean currentMonth = Integer.valueOf(DateUtil.format(DateUtil.toLocalDateTime(new Date()), "yyyyMM")).compareTo(periodCode) ==0;
        return currentMonth;
    }

    private static LambdaQueryWrapper<CheckAccountDetailResultEntity> getQueryWrapper(CheckAccountDetailResultQueryDTO queryDTO) {
        LambdaQueryWrapper<CheckAccountDetailResultEntity> queryWrapper = Wrappers.<CheckAccountDetailResultEntity>lambdaQuery();
        //这里注入查询条件
        if (ObjectUtil.isNotEmpty(queryDTO.getPeriodCode())) {
            queryWrapper.eq(CheckAccountDetailResultEntity::getPeriodCode, queryDTO.getPeriodCode());
        }else{
            throw new ServiceException("期间不能为空");
        }

        if(StringUtils.isNotEmpty(queryDTO.getOrgId())){
            queryWrapper.eq(CheckAccountDetailResultEntity::getOrgId, queryDTO.getOrgId());
        }

        if(StringUtils.isNotEmpty(queryDTO.getClientCode())){
            queryWrapper.eq(CheckAccountDetailResultEntity::getClientCode, queryDTO.getClientCode());
        }

        if(StringUtils.isNotEmpty(queryDTO.getAccountCode())){
            queryWrapper.eq(CheckAccountDetailResultEntity::getAccountCode, queryDTO.getAccountCode());
        }

        if(StringUtils.isNotEmpty(queryDTO.getCurrencyType())){
            queryWrapper.eq(CheckAccountDetailResultEntity::getCurrencyType, queryDTO.getCurrencyType());
        }

        if(StringUtils.isNotEmpty(queryDTO.getClientCode())){
            queryWrapper.eq(CheckAccountDetailResultEntity::getClientCode, queryDTO.getClientCode());
        }

        if(StringUtils.isNotEmpty(queryDTO.getDiffFlag())){
            queryWrapper.eq(CheckAccountDetailResultEntity::getDiffFlag, queryDTO.getDiffFlag());
        }
        return queryWrapper;
    }

    private Map<String, String> getAccountNameAccountCode() {
        Map<String,String> accountMap = new HashMap<>();
        List<AccountEntity> accountEntityList = iAccountService.list();
        if (CollectionUtils.isNotEmpty(accountEntityList)) {
            accountMap = accountEntityList.stream().collect(HashMap::new,(map,item)->map.put(item.getAccountCode(),item.getAccountName()),HashMap::putAll);
        }
        return accountMap;
    }

    private Map<String, String> getClientNameClientCode(List<CheckAccountDetailResultVO> list) {
        List<String> clientCodeList = list.stream().filter(v -> StringUtils.isNotEmpty(v.getClientCode())).map(CheckAccountDetailResultVO::getClientCode).distinct().collect(Collectors.toList());
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
    public void clearTableData() {
        checkAccountDetailResultMapper.clearTableData();
    }

    @Override
    public void queryAndSaveCheckResultDtoByParamAsync(CheckAccountDetailResultQueryDTO param, List<Map<String, Object>> paramList) {
        checkAccountDetailResultMapper.queryAndSaveCheckResultLatestDtoByParam(param, paramList);
    }

    @Override
    public void queryAndSaveCheckResultDtoAsync(Integer periodCode, String checkType, List<String> accountCodeList, Map<String, String> accountMap, CheckAccountDetailRecordEntity latestRecord) {
        List<Map<String, Object>> paramList = getParamList(accountCodeList, accountMap);
        CheckAccountDetailResultQueryDTO param = getQueryDto(periodCode, checkType, latestRecord);
//        this.queryAndSaveCheckResultDtoByParamAsync(param ,paramList);
        checkAccountDetailResultMapper.queryAndSaveCheckResultLatestDtoByParam(param, paramList);
    }

    @Async
    @Override
    public void batchSaveToResult(CountDownLatch countDownLatch, List<CheckAccountDetailResultEntity> entityList, String percent, String periodCode) {
        this.saveBatch(entityList);
        log.info("同步科目余额与明细余额数据保存到实时表 单页保存完成.periodCode:{}, 完成比例:{}", periodCode, percent);
        countDownLatch.countDown();
    }

    @Override
    public List<CheckAccountDetailResultEntity> queryCheckResultDto(Integer periodCode, String checkType, List<String> accountCodeList, Map<String, String> accountMap, CheckAccountDetailRecordEntity latestRecord) {
        List<Map<String, Object>> paramList = getParamList(accountCodeList, accountMap);
        CheckAccountDetailResultQueryDTO param = getQueryDto(periodCode, checkType, latestRecord);
        return checkAccountDetailResultMapper.queryCheckResultDto(param, paramList);
    }

    private CheckAccountDetailResultQueryDTO getQueryDto(Integer periodCode, String checkType, CheckAccountDetailRecordEntity latestRecord) {
        CheckAccountDetailResultQueryDTO param = new CheckAccountDetailResultQueryDTO();
        param.setRecordId(latestRecord.getId());
        param.setPeriodCode(periodCode);
        param.setDelFlag("0");
        param.setCreateBy(StringUtils.equals(CheckTypeEnum.JOB.getCode(), checkType)?"System": SecurityUtils.getUserId().toString());
        param.setCreateTime(LocalDateTime.now());
        param.setUpdateBy(StringUtils.equals(CheckTypeEnum.JOB.getCode(), checkType)?"System":SecurityUtils.getUserId().toString());
        param.setUpdateTime(LocalDateTime.now());
        return param;
    }


    private List<Map<String, Object>> getParamList(List<String> partAccountCodeList, Map<String, String> accountMap) {
        List<Map<String, Object>> paramList = Lists.newArrayList();
        for(String accountCode : partAccountCodeList){
            log.info("accountCode "+accountCode+" 获取的result list开始");
            if(!accountMap.containsKey(accountCode)){
                log.info("accountCode "+accountCode+" 没有找到对应fund_type");
                continue;
            }
            String field = accountMap.get(accountCode)+"_balance";

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("accountCode", accountCode);
            paramMap.put("queryField", field);
            paramList.add(paramMap);
        }
        return paramList;
    }

}

