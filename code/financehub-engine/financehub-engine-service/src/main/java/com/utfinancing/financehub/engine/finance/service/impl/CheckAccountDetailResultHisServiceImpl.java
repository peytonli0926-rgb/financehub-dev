package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.enums.CheckDiffEnum;
import com.utfinancing.financehub.engine.enums.CheckTypeEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailResultHisQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailResultHisDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountDetailResultQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountDetailResultHisVO;
import com.utfinancing.financehub.engine.finance.mapper.CheckAccountDetailResultHisMapper;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountDetailResultVO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.IAccountAssistBalanceService;
import com.utfinancing.financehub.engine.finance.service.ICheckAccountDetailResultHisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IClientService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.utfinancing.financehub.engine.finance.service.impl.CheckAccountDetailResultServiceImpl.*;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-20
 * @Description :  CheckAccountDetailResultHis服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CheckAccountDetailResultHisServiceImpl extends ServiceImpl<CheckAccountDetailResultHisMapper, CheckAccountDetailResultHisEntity> implements ICheckAccountDetailResultHisService {

    private final CheckAccountDetailResultHisMapper checkAccountDetailResultHisMapper;

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Resource
    IAccountAssistBalanceService accountAssistBalanceService;

    @Resource
    IAccountService iAccountService;

    @Resource
    IClientService iClientService;

    @Resource
    IOrgCompanyService iOrgCompanyService;

    @Override
    public Long saveCheckAccountDetailResultHis(CheckAccountDetailResultHisDTO dto) {
        CheckAccountDetailResultHisEntity entity = BeanUtil.copyProperties(dto, CheckAccountDetailResultHisEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateCheckAccountDetailResultHis(Long id, CheckAccountDetailResultHisDTO dto) {
        CheckAccountDetailResultHisEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public CheckAccountDetailResultHisDTO getCheckAccountDetailResultHisDTOById(Long id) {
        CheckAccountDetailResultHisEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, CheckAccountDetailResultHisDTO.class);
    }

    @Override
    public IPage<CheckAccountDetailResultHisVO> selectDetailHisPage(CheckAccountDetailResultHisQueryDTO queryDTO) {
//        LambdaQueryWrapper<CheckAccountDetailResultHisEntity> queryWrapper = Wrappers.<CheckAccountDetailResultHisEntity>lambdaQuery();
        LambdaQueryWrapper<CheckAccountDetailResultHisEntity> queryWrapper = getQueryWrapper(queryDTO);

        //这里注入查询条件
        IPage<CheckAccountDetailResultHisEntity> entityIPage = checkAccountDetailResultHisMapper.selectPage(new Page<CheckAccountDetailResultHisEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<CheckAccountDetailResultHisVO> page = ListBeanUtil.copyPage(entityIPage, CheckAccountDetailResultHisVO.class);

        List<CheckAccountDetailResultHisVO> list = page.getRecords();
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

    private static LambdaQueryWrapper<CheckAccountDetailResultHisEntity> getQueryWrapper(CheckAccountDetailResultHisQueryDTO queryDTO) {
        LambdaQueryWrapper<CheckAccountDetailResultHisEntity> queryWrapper = Wrappers.<CheckAccountDetailResultHisEntity>lambdaQuery();
        //这里注入查询条件
        if (ObjectUtil.isNotEmpty(queryDTO.getPeriodCode())) {
            queryWrapper.eq(CheckAccountDetailResultHisEntity::getPeriodCode, queryDTO.getPeriodCode());
        }else{
            throw new ServiceException("期间不能为空");
        }

        if(StringUtils.isNotEmpty(queryDTO.getOrgId())){
            queryWrapper.eq(CheckAccountDetailResultHisEntity::getOrgId, queryDTO.getOrgId());
        }

        if(StringUtils.isNotEmpty(queryDTO.getClientCode())){
            queryWrapper.eq(CheckAccountDetailResultHisEntity::getClientCode, queryDTO.getClientCode());
        }

        if(StringUtils.isNotEmpty(queryDTO.getAccountCode())){
            queryWrapper.eq(CheckAccountDetailResultHisEntity::getAccountCode, queryDTO.getAccountCode());
        }

        if(StringUtils.isNotEmpty(queryDTO.getCurrencyType())){
            queryWrapper.eq(CheckAccountDetailResultHisEntity::getCurrencyType, queryDTO.getCurrencyType());
        }

        if(StringUtils.isNotEmpty(queryDTO.getClientCode())){
            queryWrapper.eq(CheckAccountDetailResultHisEntity::getClientCode, queryDTO.getClientCode());
        }

        if(StringUtils.isNotEmpty(queryDTO.getDiffFlag())){
            queryWrapper.eq(CheckAccountDetailResultHisEntity::getDiffFlag, queryDTO.getDiffFlag());
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

    private Map<String, String> getClientNameClientCode(List<CheckAccountDetailResultHisVO> list) {
        List<String> clientCodeList = list.stream().filter(v -> StringUtils.isNotEmpty(v.getClientCode())).map(CheckAccountDetailResultHisVO::getClientCode).distinct().collect(Collectors.toList());
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
    public void saveResultToHis() {
        checkAccountDetailResultHisMapper.saveResultToHis();
    }


    @Override
    public void clearHisTableData(Integer periodCode) {
        checkAccountDetailResultHisMapper.clearHisTableData(periodCode);
    }


//    @Async
    @Override
    public void queryAndSaveCheckResultDtoByParamAsync(CheckAccountDetailResultHisQueryDTO param,
                                                       List<Map<String, Object>> paramList) {
        checkAccountDetailResultHisMapper.queryAndSaveCheckResultDtoByParam(param, paramList);
    }

    @Override
    public void queryAndSaveCheckResultDtoAsync(Integer periodCode, String checkType, List<String> accountCodeList, Map<String, String> accountMap, CheckAccountDetailRecordEntity latestRecord) {
        List<Map<String, Object>> paramList = getParamList(accountCodeList, accountMap);
        CheckAccountDetailResultHisQueryDTO param = getQueryDto(periodCode, checkType, latestRecord);
//        this.queryAndSaveCheckResultDtoByParamAsync(param ,paramList);
        checkAccountDetailResultHisMapper.queryAndSaveCheckResultDtoByParam(param, paramList);

    }

    private CheckAccountDetailResultHisQueryDTO getQueryDto(Integer periodCode, String checkType, CheckAccountDetailRecordEntity latestRecord) {
        CheckAccountDetailResultHisQueryDTO param = new CheckAccountDetailResultHisQueryDTO();
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
//            log.info("accountCode "+accountCode+" 获取的result list开始");
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

    @Override
    public void clearContractTmpTableData() {
        checkAccountDetailResultHisMapper.clearContractTmpTableData();
    }

//    @Async
    @Override
    public void insertContractTmpTableData(Integer periodCode) {
        checkAccountDetailResultHisMapper.insertContractTmpTableData(periodCode);
    }

    @Override
    public List<CheckAccountDetailResultHisEntity> queryCheckResultDto(Integer periodCode, String checkType, List<String> accountCodeList, Map<String, String> accountMap, CheckAccountDetailRecordEntity latestRecord) {
        List<Map<String, Object>> paramList = getParamList(accountCodeList, accountMap);
        CheckAccountDetailResultHisQueryDTO param = getQueryDto(periodCode, checkType, latestRecord);
        return checkAccountDetailResultHisMapper.queryCheckResultDto(param, paramList);
    }

    @Async
    @Override
    public void batchSaveToResultHis(CountDownLatch countDownLatch, List<CheckAccountDetailResultHisEntity> entityList, String percent, String periodCode) {
        this.saveBatch(entityList);
        log.info("同步科目余额与明细余额数据保存到历史表 单页保存完成.periodCode:{}, 完成比例:{}", periodCode, percent);
        countDownLatch.countDown();
    }
}

