package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.enums.BusinessSceneEnum;
import com.utfinancing.financehub.engine.enums.CheckExecuteStatusEnum;
import com.utfinancing.financehub.engine.enums.ModuleEnum;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.mapper.AccountAssistBalanceMapper;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import com.utfinancing.financehub.engine.scene.service.impl.AccountServiceImpl;
import com.utfinancing.financehub.engine.utils.PeriodCodeUtil;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-05
 * @Description :  AccountAssistBalance服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class AccountAssistBalanceServiceImpl extends ServiceImpl<AccountAssistBalanceMapper, AccountAssistBalanceEntity> implements IAccountAssistBalanceService {

    private final AccountAssistBalanceMapper accountAssistBalanceMapper;
    private final IVoucherEntryService voucherEntryService;

    @Resource
    IClientService iClientService;

    @Resource
    IOrgCompanyService iOrgCompanyService;

    @Resource
    private IAccountService accountService;

    @Resource
    private IContractService contractService;

    @Resource
    IReportPeriodSyncRecordService reportPeriodSyncRecordService;

    @Resource
    private IFileRecordService fileRecordService;

    @Value("${service.parth:null}")
    private String servicePath;

    @Value("${file.storage.basicpath.windows:null}")
    private String basicPathWindows;

    @Value("${file.storage.basicpath.linux:null}")
    private String basicPathLinux;

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;

//    @Override
//    public IPage<AccountAssistBalanceVO> assistBalancePage(AccountAssistBalanceQueryDTO queryDTO) {
//
//
//        String queryType = getQueryType(queryDTO.getProcessStatusList());
//        queryDTO.setQueryType(queryType);
//
//        Integer limit = queryDTO.getPageSize()*queryDTO.getPageNum();
//        Integer offset = queryDTO.getPageSize()*(queryDTO.getPageNum()-1);
//        queryDTO.setLimit(limit);
//        queryDTO.setOffset(offset);
//
//
//        List<AccountAssistBalanceVO> list = accountAssistBalanceMapper.selectAssistBalance(queryDTO);
//        Map<String, Long> sizeMap = accountAssistBalanceMapper.selectAssistBalanceSize(queryDTO);
//
//        if(CollectionUtil.isNotEmpty(list)){
//
//            fillVos(list);
//
//        }
//        return getIPage(queryDTO.getPageNum(), queryDTO.getPageSize(), list, sizeMap.get("total_size"));
//    }

    @Override
    public IPage<AccountAssistBalanceVO> assistBalancePage(AccountAssistBalanceQueryDTO queryDTO) {
        String queryType = getQueryType(queryDTO.getProcessStatusList());

        queryDTO.setQueryType(queryType);

        Integer limit = queryDTO.getPageSize();
        Integer offset = queryDTO.getPageSize()*(queryDTO.getPageNum()-1);
        queryDTO.setLimit(limit);
        queryDTO.setOffset(offset);

        Integer periodCodeStart = queryDTO.getPeriodCodeStart();
        Integer periodCodeEnd = queryDTO.getPeriodCodeEnd();

        setAssistPeriodCode(queryDTO, periodCodeEnd, periodCodeStart);

        queryDTO.setPeriodYearStart(Integer.valueOf(periodCodeEnd.toString().substring(0, 4)+"01"));

        List<AccountAssistBalanceVO> finalList = Lists.newArrayList();
        Map<String, Long> finalMap = Maps.newHashMap();

        fillAssistContent(queryDTO, queryType, finalMap, finalList, true);

        return getIPage(queryDTO.getPageNum(), queryDTO.getPageSize(), finalList, finalMap.get("total_size"));

    }

    private void setAssistPeriodCode(AccountAssistBalanceQueryDTO queryDTO, Integer periodCodeEnd, Integer periodCodeStart) {
        ReportPeriodSyncRecordEntity periodSyncRecord = reportPeriodSyncRecordService.getOne(new LambdaQueryWrapper<ReportPeriodSyncRecordEntity>().orderByDesc(ReportPeriodSyncRecordEntity::getPeriodCode));
        Integer lastClosedPeriodCode = periodSyncRecord.getPeriodCode();
        if(periodCodeEnd.compareTo(lastClosedPeriodCode)<=0){
//            queryTypeMap.put("assist", periodCodeStart+"-"+periodCodeEnd);
//            queryTypeMap.put("other", "");
            queryDTO.setAssistPeriodCodeStart(periodCodeStart);
            queryDTO.setAssistPeriodCodeEnd(periodCodeEnd);
            queryDTO.setOtherPeriodCodeStart(null);
            queryDTO.setOtherPeriodCodeEnd(null);
        }else if(periodCodeStart.compareTo(lastClosedPeriodCode)>0){
//            queryTypeMap.put("assist", "");
//            queryTypeMap.put("other", periodCodeStart+"-"+periodCodeEnd);
            queryDTO.setAssistPeriodCodeStart(null);
            queryDTO.setAssistPeriodCodeEnd(null);
            queryDTO.setOtherPeriodCodeStart(periodCodeStart);
            queryDTO.setOtherPeriodCodeEnd(periodCodeEnd);
        }else{
            LocalDateTime lastClosedPeriodNextMonth = LocalDateTimeUtil.parse(lastClosedPeriodCode.toString(), DateTimeFormatter.ofPattern("yyyyMM")).plusMonths(1);
            String lastClosedPeriodNextMonthStr = lastClosedPeriodNextMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
//            queryTypeMap.put("assist", periodCodeStart+"-"+lastClosedPeriodCode);
//            queryTypeMap.put("other", Integer.valueOf(lastClosedPeriodNextMonthStr)+"-"+periodCodeEnd);
            queryDTO.setAssistPeriodCodeStart(periodCodeStart);
            queryDTO.setAssistPeriodCodeEnd(lastClosedPeriodCode);
            queryDTO.setOtherPeriodCodeStart(Integer.valueOf(lastClosedPeriodNextMonthStr));
            queryDTO.setOtherPeriodCodeEnd(periodCodeEnd);
        }
    }


    private void fillAssistContent(AccountAssistBalanceQueryDTO queryDTO, String queryType, Map<String, Long> finalMap, List<AccountAssistBalanceVO> finalList, boolean isQuery) {
        finalMap.put("total_size", 0L);
        Map<String, Long> sizeMap = Maps.newHashMap();
        List<AccountAssistBalanceVO> resultList = Lists.newArrayList();
//        List<String> queryOrgIdList = Lists.newArrayList();
//        List<String> queryAccountCodeList = Lists.newArrayList();
        AccountAssistBalanceQueryDTO queryDTOBak = BeanUtil.copyProperties(queryDTO, AccountAssistBalanceQueryDTO.class);

        Integer assistPeriodCodeStart = queryDTO.getAssistPeriodCodeStart();
        Integer assistPeriodCodeEnd = queryDTO.getAssistPeriodCodeEnd();
        Integer otherPeriodCodeStart = queryDTO.getOtherPeriodCodeStart();
        Integer otherPeriodCodeEnd = queryDTO.getOtherPeriodCodeEnd();
        resultList = accountAssistBalanceMapper.selectAssistAll(queryDTO);
        if(isQuery) {
            sizeMap = accountAssistBalanceMapper.selectAssistAllSize(queryDTO);
        }

//        if(ObjectUtil.isNotEmpty(assistPeriodCodeStart)&&ObjectUtil.isEmpty(otherPeriodCodeStart)){
//            resultList = accountAssistBalanceMapper.selectAccountAssistPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectAccountAssistPartSize(queryDTO);
//            }
//        }else if(ObjectUtil.isEmpty(assistPeriodCodeStart)&&ObjectUtil.isNotEmpty(otherPeriodCodeStart)){
//            //如果查询的范围超过了关账的最后一条
//            queryDTO.setAssistPeriodCodeStart(otherPeriodCodeStart);
//            queryDTO.setAssistPeriodCodeEnd(otherPeriodCodeEnd);
//            List<AccountAssistBalanceVO> list1 = accountAssistBalanceMapper.selectAccountAssistPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectAccountAssistPartSize(queryDTO);
//            }
//            List<AccountAssistBalanceVO> list2 = accountAssistBalanceMapper.selectAccountVoucherPart(queryDTO);
////            if(isQuery) {
////                sizeMap = accountAssistBalanceMapper.selectAccountVoucherPartSize(queryDTO);
////            }
//            List<AccountAssistBalanceVO> list3 = accountAssistBalanceMapper.selectAccountManualPart(queryDTOBak);
//            Map<String, AccountAssistBalanceVO> tmpMap = list1.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode()+"&"+m.getContractCode()+"&"+m.getBillContractCode(), Function.identity(), ((key1 , key2) -> key1)));
//
//            AddAccountBalanceList(list2, tmpMap);
//            AddAccountBalanceList(list3, tmpMap);
//            for(Map.Entry<String, AccountAssistBalanceVO> entry: tmpMap.entrySet()){
//                resultList.add(entry.getValue());
//            }
//        }else if(ObjectUtil.isNotEmpty(assistPeriodCodeStart)&&ObjectUtil.isNotEmpty(otherPeriodCodeStart)){
//            List<AccountAssistBalanceVO> list1 = accountAssistBalanceMapper.selectAccountAssistPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectAccountAssistPartSize(queryDTO);
//            }
////            if(CollectionUtil.isNotEmpty(list1)){
////                queryOrgIdList = list1.stream().map(AccountBalanceSheetVO::getOrgId).distinct().collect(Collectors.toList());
////                queryAccountCodeList = list1.stream().map(AccountBalanceSheetVO::getAccountCode).distinct().collect(Collectors.toList());
////                queryDTOBak.setOrgIdList(queryOrgIdList);
////                queryDTOBak.setAccountCodeList(queryAccountCodeList);
////            }
//            List<AccountAssistBalanceVO> list2 = accountAssistBalanceMapper.selectAccountVoucherPart(queryDTOBak);
//            List<AccountAssistBalanceVO> list3 = accountAssistBalanceMapper.selectAccountManualPart(queryDTOBak);
//            Map<String, AccountAssistBalanceVO> tmpMap = list1.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode()+"&"+m.getContractCode()+"&"+m.getBillContractCode(), Function.identity(), ((key1 , key2) -> key1)));
//
//            AddAccountBalanceList(list2, tmpMap);
//            AddAccountBalanceList(list3, tmpMap);
//            for(Map.Entry<String, AccountAssistBalanceVO> entry: tmpMap.entrySet()){
//                resultList.add(entry.getValue());
//            }
//        }

//        if(StringUtils.equals("assist", queryType)){
//            resultList = accountAssistBalanceMapper.selectAccountAssistPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectAccountAssistPartSize(queryDTO);
//            }
//        }else if(StringUtils.equals("voucher", queryType)){
//            resultList = accountAssistBalanceMapper.selectAccountVoucherPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectAccountVoucherPartSize(queryDTO);
//            }
//        }else if(StringUtils.equals("manual", queryType)){
//            resultList = accountAssistBalanceMapper.selectAccountManualPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectAccountManualPartSize(queryDTO);
//            }
//        }else if(StringUtils.equals("assist+voucher", queryType)){
//            List<AccountAssistBalanceVO> list1 = accountAssistBalanceMapper.selectAccountAssistPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectAccountAssistPartSize(queryDTO);
//            }
//            List<AccountAssistBalanceVO> list2 = accountAssistBalanceMapper.selectAccountVoucherPart(queryDTOBak);
//            Map<String, AccountAssistBalanceVO> tmpMap = list1.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode()+"&"+m.getContractCode()+"&"+m.getBillContractCode(), Function.identity(), ((key1 , key2) -> key1)));
//
//            AddAccountBalanceList(list2, tmpMap);
//            for(Map.Entry<String, AccountAssistBalanceVO> entry: tmpMap.entrySet()){
//                resultList.add(entry.getValue());
//            }
//        }else if(StringUtils.equals("assist+manual", queryType)){
//            List<AccountAssistBalanceVO> list1 = accountAssistBalanceMapper.selectAccountAssistPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectAccountAssistPartSize(queryDTO);
//            }
//            List<AccountAssistBalanceVO> list2 = accountAssistBalanceMapper.selectAccountManualPart(queryDTOBak);
//            Map<String, AccountAssistBalanceVO> tmpMap = list1.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode()+"&"+m.getContractCode()+"&"+m.getBillContractCode(), Function.identity(), ((key1 , key2) -> key1)));
//
//            AddAccountBalanceList(list2, tmpMap);
//            for(Map.Entry<String, AccountAssistBalanceVO> entry: tmpMap.entrySet()){
//                resultList.add(entry.getValue());
//            }
//        }else if(StringUtils.equals("voucher+manual", queryType)){
//            List<AccountAssistBalanceVO> list1 = accountAssistBalanceMapper.selectAccountVoucherPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectAccountVoucherPartSize(queryDTO);
//            }
//            List<AccountAssistBalanceVO> list2 = accountAssistBalanceMapper.selectAccountManualPart(queryDTOBak);
//            Map<String, AccountAssistBalanceVO> tmpMap = list1.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode()+"&"+m.getContractCode()+"&"+m.getBillContractCode(), Function.identity(), ((key1 , key2) -> key1)));
//
//            AddAccountBalanceList(list2, tmpMap);
//            for(Map.Entry<String, AccountAssistBalanceVO> entry: tmpMap.entrySet()){
//                resultList.add(entry.getValue());
//            }
//        }else if(StringUtils.equals("assist+voucher+manual", queryType)){
//            List<AccountAssistBalanceVO> list1 = accountAssistBalanceMapper.selectAccountAssistPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectAccountAssistPartSize(queryDTO);
//            }
////            if(CollectionUtil.isNotEmpty(list1)){
////                queryOrgIdList = list1.stream().map(AccountBalanceSheetVO::getOrgId).distinct().collect(Collectors.toList());
////                queryAccountCodeList = list1.stream().map(AccountBalanceSheetVO::getAccountCode).distinct().collect(Collectors.toList());
////                queryDTOBak.setOrgIdList(queryOrgIdList);
////                queryDTOBak.setAccountCodeList(queryAccountCodeList);
////            }
//            List<AccountAssistBalanceVO> list2 = accountAssistBalanceMapper.selectAccountVoucherPart(queryDTOBak);
//            List<AccountAssistBalanceVO> list3 = accountAssistBalanceMapper.selectAccountManualPart(queryDTOBak);
//            Map<String, AccountAssistBalanceVO> tmpMap = list1.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode()+"&"+m.getContractCode()+"&"+m.getBillContractCode(), Function.identity(), ((key1 , key2) -> key1)));
//
//            AddAccountBalanceList(list2, tmpMap);
//            AddAccountBalanceList(list3, tmpMap);
//            for(Map.Entry<String, AccountAssistBalanceVO> entry: tmpMap.entrySet()){
//                resultList.add(entry.getValue());
//            }
//        }

        if(CollectionUtil.isNotEmpty(resultList)){
            AccountAssistBalanceQueryDTO tmpDto = BeanUtil.copyProperties(queryDTO, AccountAssistBalanceQueryDTO.class);
            tmpDto.setOtherPeriodCodeStart(queryDTO.getPeriodCodeStart());
            tmpDto.setOtherPeriodCodeEnd(queryDTO.getPeriodCodeEnd());
//            List<AccountAssistBalanceVO> voucherIdList = accountAssistBalanceMapper.selectAccountVoucherIdPart(tmpDto);
            List<AccountEntity> accountList = accountService.list(new QueryWrapper<AccountEntity>().select("DISTINCT account_code", "account_name").lambda());
            Map<String, ContractEntity> contractMap = getContractMap(resultList.stream().map(AccountAssistBalanceVO::getContractCode).distinct().collect(Collectors.toList()), resultList.stream().map(AccountAssistBalanceVO::getOrgId).distinct().collect(Collectors.toList()));
            Map<String, String> clientMap = getClientNameClientCode(resultList.stream().map(AccountAssistBalanceVO::getClientCode).distinct().collect(Collectors.toList()));

            Map<String, AccountAssistBalanceVO> tmpMap = resultList.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode()+"&"+m.getContractCode()+"&"+m.getBillContractCode(), Function.identity(), ((key1 , key2) -> key1)));
//            Map<String, AccountAssistBalanceVO> tmpVoucherIdMap = voucherIdList.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode()+"&"+m.getContractCode()+"&"+m.getBillContractCode(), Function.identity(), ((key1 , key2) -> key1)));
            Map<String, String> accountMap = accountList.stream().collect(Collectors.toMap(AccountEntity::getAccountCode, AccountEntity::getAccountName,(k, v) -> k));
            Map<String, String> orgMap = getOrgNameOrgId();
            for(Map.Entry<String, AccountAssistBalanceVO> entry: tmpMap.entrySet()){
                AccountAssistBalanceVO vo = entry.getValue();
//                if(tmpVoucherIdMap.containsKey(entry.getKey())){
//                    vo.setVoucherId(tmpVoucherIdMap.get(entry.getKey()).getVoucherId());
//                }
                if(accountMap.containsKey(vo.getAccountCode())){
                    vo.setAccountName(accountMap.get(vo.getAccountCode()));
                }

                String contractMapKey = vo.getContractCode()+"&"+vo.getOrgId();
                if(contractMap.containsKey(contractMapKey)){
                    ContractEntity contract = contractMap.get(contractMapKey);
                    vo.setContractId(contract.getId());
                    vo.setContractName(contract.getContractName());
                    vo.setLeaseType(contract.getLeaseType());
                    vo.setClientCode(contract.getClientCode());
                    vo.setClientName(contract.getClientName());
                }
//                if(clientMap.containsKey(vo.getClientCode())){
//                    vo.setContractName(clientMap.get(vo.getClientCode()));
//                }
                if(orgMap.containsKey(vo.getOrgId())){
                    vo.setOrgName(orgMap.get(vo.getOrgId()));
                }
//                entry.setValue(vo);
                vo.setPeriodCodeStart(queryDTO.getPeriodCodeStart());
                vo.setPeriodCodeEnd(queryDTO.getPeriodCodeEnd());
                vo.setPeriodCode(queryDTO.getPeriodCodeStart()+"-"+queryDTO.getPeriodCodeEnd());
                finalList.add(vo);
            }
            finalMap.put("total_size", sizeMap.get("total_size"));
        }
        return;
    }

    private static String getQueryType(List<String> processStatusList) {
        /**
         * 1 只查assist                       assist left 1=2 voucher left 1=2 manual
         * 2 只查voucher                      assist right 1=2 voucher  left 1=2 manual
         * 3 只查manual                       assist left 1=2 voucher  right 1=2 manual
         * 4 查assist+voucher                 assist left voucher left 1=2 manual
         * 5 查assist+manual                  assist left 1=2 voucher left manual
         * 6.查voucher+manual                 assist right 1=2 voucher  right 1=2 manual 特殊处理voucher left join manual
         * 7 查assist+voucher+manual          assist left voucher left manual
         */
        String queryType = null;

        if(CollectionUtil.isNotEmpty(processStatusList)){
            List<String> assistList = Arrays.asList(ProcessStatusEnum.REVIEWED.getCode(), ProcessStatusEnum.TO_KINGDEE.getCode(), ProcessStatusEnum.REJECTED.getCode());
            long cnt = processStatusList.stream().filter(assistList::contains).count();
            if(!processStatusList.contains(ProcessStatusEnum.SUBMITTED.getCode())&&!processStatusList.contains(ProcessStatusEnum.ENTERED.getCode())){
                queryType = "assist";
            }else if(cnt==0&&processStatusList.size()==1&&processStatusList.contains(ProcessStatusEnum.SUBMITTED.getCode())){
                queryType = "voucher";
            }else if(cnt==0&&processStatusList.size()==1&&processStatusList.contains(ProcessStatusEnum.ENTERED.getCode())){
                queryType = "manual";
            }else if(cnt>0&&!processStatusList.contains(ProcessStatusEnum.ENTERED.getCode())&&processStatusList.contains(ProcessStatusEnum.SUBMITTED.getCode())){
                queryType = "assist+voucher";
            }else if(cnt>0&&!processStatusList.contains(ProcessStatusEnum.SUBMITTED.getCode())&&processStatusList.contains(ProcessStatusEnum.ENTERED.getCode())){
                queryType = "assist+manual";
            }else if(cnt==0&&processStatusList.contains(ProcessStatusEnum.SUBMITTED.getCode())&&processStatusList.contains(ProcessStatusEnum.ENTERED.getCode())){
                queryType = "voucher+manual";
            }else if(cnt>0&&processStatusList.contains(ProcessStatusEnum.SUBMITTED.getCode())&&processStatusList.contains(ProcessStatusEnum.ENTERED.getCode())){
                queryType = "assist+voucher+manual";
            }
        }else{
            queryType = "assist+voucher+manual";
        }
        return queryType;
    }


    private void fillVos(List<AccountAssistBalanceVO> records) {
        List<String> clientCodes = records.stream().map(AccountAssistBalanceVO::getClientCode).distinct().collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(clientCodes)){
            Map<String, String> clientMap = getClientNameClientCode(clientCodes);
            for(AccountAssistBalanceVO record : records){
                if (StringUtils.isNotEmpty(record.getClientCode())&&clientMap.containsKey(record.getClientCode())) {
                    record.setClientName(clientMap.get(record.getClientCode()));
                }
            }
        }

        Map<String, String> orgMap = getOrgNameOrgId();
        for(AccountAssistBalanceVO record : records){
            if (StringUtils.isNotEmpty(record.getOrgId())&&orgMap.containsKey(record.getOrgId())) {
                record.setOrgName(orgMap.get(record.getOrgId()));
            }
        }
//        List<String> accountCodes = records.stream().map(AccountAssistBalanceVO::getAccountCode).distinct().collect(Collectors.toList());
//        if(CollectionUtil.isNotEmpty(accountCodes)){
//            Map<String, String> accountMap = getAccountNameAccountCode(accountCodes);
//            for(AccountAssistBalanceVO record : records){
//                if (StringUtils.isNotEmpty(record.getAccountCode())&&accountMap.containsKey(record.getAccountCode())) {
//                    record.setAccountName(accountMap.get(record.getAccountCode()));
//                }
//            }
//        }

//        List<String> contractCodes = records.stream().map(AccountAssistBalanceVO::getContractCode).distinct().collect(Collectors.toList());
//        if(CollectionUtil.isNotEmpty(contractCodes)){
//            Map<String, Long> contractIdMap = getContractIdMap(contractCodes);
//            for(AccountAssistBalanceVO record : records){
//                if (StringUtils.isNotEmpty(record.getContractCode())&&contractIdMap.containsKey(record.getContractCode())) {
//                    record.setContractId(contractIdMap.get(record.getContractCode()));
//                }
//            }
//        }


//        page.setRecords(records);
    }

    private Map<String, ContractEntity> getContractMap(List<String> contractCodes, List<String> orgIds) {
        Map<String, ContractEntity> contractMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(contractCodes)){
            List<ContractEntity> contractEntityList = contractService.list(new LambdaQueryWrapper<ContractEntity>().in(ContractEntity::getContractCode, contractCodes).in(ContractEntity::getOrgId, orgIds));
            if(CollectionUtil.isNotEmpty(contractEntityList)){
                contractMap = contractEntityList.stream().collect(HashMap::new,(map,item)->map.put(item.getContractCode()+"&"+item.getOrgId(),item),HashMap::putAll);
            }
        }
        return contractMap;
    }
/**
    @Override
    public IPage<AccountBalanceSheetVO> accountBalancePage(AccountBalanceSheetQueryDTO queryDTO) {
//        QueryWrapper<AccountAssistBalanceEntity> queryWrapper = getAccountQueryWrapper(queryDTO);
//        LambdaQueryWrapper<AccountAssistBalanceEntity> queryWrapper = Wrappers.<AccountAssistBalanceEntity>lambdaQuery();
//        //这里注入查询条件
//        if (ObjectUtil.isNotNull(queryDTO.getPeriodCode())) {
//            queryWrapper.eq(AccountAssistBalanceEntity::getPeriodCode,queryDTO.getPeriodCode());
//        }
//        queryWrapper.groupBy(AccountBalanceSheetVO::getPeriodCode,AccountBalanceSheetVO::getAccountCode);

//        IPage<AccountAssistBalanceEntity> entityIPage = accountAssistBalanceMapper.selectPage(new Page<AccountAssistBalanceEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
//        IPage<AccountBalanceSheetVO> page = ListBeanUtil.copyPage(entityIPage, AccountBalanceSheetVO.class);
//        List<AccountBalanceSheetVO> records = page.getRecords();
//        fillRecords(records);
//        page.setRecords(records);
//        return page;

        String queryType = getQueryType(queryDTO.getProcessStatusList());
        queryDTO.setQueryType(queryType);

        Integer limit = queryDTO.getPageSize();
        Integer offset = queryDTO.getPageSize()*(queryDTO.getPageNum()-1);
        queryDTO.setLimit(limit);
        queryDTO.setOffset(offset);


        List<AccountBalanceSheetVO> list = accountAssistBalanceMapper.selectAccountBalance(queryDTO);
        Map<String, Long> sizeMap = accountAssistBalanceMapper.selectAccountBalanceSize(queryDTO);

        if(CollectionUtil.isNotEmpty(list)){
            Map<String, String> orgMap = getOrgNameOrgId();
            for(AccountBalanceSheetVO record : list){
                if (StringUtils.isNotEmpty(record.getOrgId())&&orgMap.containsKey(record.getOrgId())) {
                    record.setOrgName(orgMap.get(record.getOrgId()));
                }
            }

        }
        return getIPage(queryDTO.getPageNum(), queryDTO.getPageSize(), list, sizeMap.get("total_size"));
    }
**/
    @Override
    public IPage<AccountBalanceSheetVO> accountBalancePage(AccountBalanceSheetQueryDTO queryDTO) {
        String queryType = getQueryType(queryDTO.getProcessStatusList());
        queryDTO.setQueryType(queryType);

        Integer limit = queryDTO.getPageSize();
        Integer offset = queryDTO.getPageSize()*(queryDTO.getPageNum()-1);
        queryDTO.setLimit(limit);
        queryDTO.setOffset(offset);

        Integer periodCodeStart = queryDTO.getPeriodCodeStart();
        Integer periodCodeEnd = queryDTO.getPeriodCodeEnd();

        setAccountPeriodCode(queryDTO, periodCodeEnd, periodCodeStart);

        queryDTO.setPeriodYearStart(Integer.valueOf(periodCodeEnd.toString().substring(0, 4)+"01"));

        List<AccountBalanceSheetVO> finalList = Lists.newArrayList();
        Map<String, Long> finalMap = Maps.newHashMap();
        fillAccountContent(queryDTO, queryType, finalMap, finalList, true);

        return getIPage(queryDTO.getPageNum(), queryDTO.getPageSize(), finalList, finalMap.get("total_size"));
    }

    private void setAccountPeriodCode(AccountBalanceSheetQueryDTO queryDTO, Integer periodCodeEnd, Integer periodCodeStart) {
        ReportPeriodSyncRecordEntity periodSyncRecord = reportPeriodSyncRecordService.getOne(new LambdaQueryWrapper<ReportPeriodSyncRecordEntity>().orderByDesc(ReportPeriodSyncRecordEntity::getPeriodCode));
        Integer lastClosedPeriodCode = periodSyncRecord.getPeriodCode();
        if(periodCodeEnd.compareTo(lastClosedPeriodCode)<=0){
//            queryTypeMap.put("assist", periodCodeStart+"-"+periodCodeEnd);
//            queryTypeMap.put("other", "");
            queryDTO.setAssistPeriodCodeStart(periodCodeStart);
            queryDTO.setAssistPeriodCodeEnd(periodCodeEnd);
            queryDTO.setOtherPeriodCodeStart(null);
            queryDTO.setOtherPeriodCodeEnd(null);
        }else if(periodCodeStart.compareTo(lastClosedPeriodCode)>0){
//            queryTypeMap.put("assist", "");
//            queryTypeMap.put("other", periodCodeStart+"-"+periodCodeEnd);
            queryDTO.setAssistPeriodCodeStart(null);
            queryDTO.setAssistPeriodCodeEnd(null);
            queryDTO.setOtherPeriodCodeStart(periodCodeStart);
            queryDTO.setOtherPeriodCodeEnd(periodCodeEnd);
        }else{
            LocalDateTime lastClosedPeriodNextMonth = LocalDateTimeUtil.parse(lastClosedPeriodCode.toString(), DateTimeFormatter.ofPattern("yyyyMM")).plusMonths(1);
            String lastClosedPeriodNextMonthStr = lastClosedPeriodNextMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
//            queryTypeMap.put("assist", periodCodeStart+"-"+lastClosedPeriodCode);
//            queryTypeMap.put("other", Integer.valueOf(lastClosedPeriodNextMonthStr)+"-"+periodCodeEnd);
            queryDTO.setAssistPeriodCodeStart(periodCodeStart);
            queryDTO.setAssistPeriodCodeEnd(lastClosedPeriodCode);
            queryDTO.setOtherPeriodCodeStart(Integer.valueOf(lastClosedPeriodNextMonthStr));
            queryDTO.setOtherPeriodCodeEnd(periodCodeEnd);
        }
    }

    private void fillAccountContent(AccountBalanceSheetQueryDTO queryDTO, String queryType, Map<String, Long> finalMap, List<AccountBalanceSheetVO> finalList, boolean isQuery) {
        finalMap.put("total_size", 0L);

        Map<String, Long> sizeMap = Maps.newHashMap();
        List<AccountBalanceSheetVO> resultList = Lists.newArrayList();
//        List<String> queryOrgIdList = Lists.newArrayList();
//        List<String> queryAccountCodeList = Lists.newArrayList();
        AccountBalanceSheetQueryDTO queryDTOBak = BeanUtil.copyProperties(queryDTO, AccountBalanceSheetQueryDTO.class);

        Integer assistPeriodCodeStart = queryDTO.getAssistPeriodCodeStart();
        Integer assistPeriodCodeEnd = queryDTO.getAssistPeriodCodeEnd();
        Integer otherPeriodCodeStart = queryDTO.getOtherPeriodCodeStart();
        Integer otherPeriodCodeEnd = queryDTO.getOtherPeriodCodeEnd();

        resultList = accountAssistBalanceMapper.selectAccountAll(queryDTO);
        if(isQuery){
            sizeMap = accountAssistBalanceMapper.selectAccountAllSize(queryDTO);
        }
//        if(ObjectUtil.isNotEmpty(assistPeriodCodeStart)&&ObjectUtil.isEmpty(otherPeriodCodeStart)){
//            resultList = accountAssistBalanceMapper.selectAssistPart(queryDTO);
//            if(isQuery){
//                sizeMap = accountAssistBalanceMapper.selectAssistPartSize(queryDTO);
//            }
//        }else if(ObjectUtil.isEmpty(assistPeriodCodeStart)&&ObjectUtil.isNotEmpty(otherPeriodCodeStart)){
//            //如果查询的范围超过了关账的最后一条
//            queryDTO.setAssistPeriodCodeStart(otherPeriodCodeStart);
//            queryDTO.setAssistPeriodCodeEnd(otherPeriodCodeEnd);
//
//            List<AccountBalanceSheetVO> list1 = accountAssistBalanceMapper.selectAssistPart(queryDTO);
//            if(isQuery){
//                sizeMap = accountAssistBalanceMapper.selectAssistPartSize(queryDTO);
//            }
//            List<AccountBalanceSheetVO> list2 = accountAssistBalanceMapper.selectVoucherPart(queryDTO);
////            if(isQuery) {
////                sizeMap = accountAssistBalanceMapper.selectVoucherPartSize(queryDTO);
////            }
//            List<AccountBalanceSheetVO> list3 = accountAssistBalanceMapper.selectManualPart(queryDTOBak);
//            Map<String, AccountBalanceSheetVO> tmpMap = list1.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode(), Function.identity(), ((key1 , key2) -> key1)));
//
//            AddBalanceList(list2, tmpMap);
//            AddBalanceList(list3, tmpMap);
//            for(Map.Entry<String, AccountBalanceSheetVO> entry: tmpMap.entrySet()){
//                resultList.add(entry.getValue());
//            }
//        }else if(ObjectUtil.isNotEmpty(assistPeriodCodeStart)&&ObjectUtil.isNotEmpty(otherPeriodCodeStart)){
//            List<AccountBalanceSheetVO> list1 = accountAssistBalanceMapper.selectAssistPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectAssistPartSize(queryDTO);
//            }
////            if(CollectionUtil.isNotEmpty(list1)){
////                queryOrgIdList = list1.stream().map(AccountBalanceSheetVO::getOrgId).distinct().collect(Collectors.toList());
////                queryAccountCodeList = list1.stream().map(AccountBalanceSheetVO::getAccountCode).distinct().collect(Collectors.toList());
////                queryDTOBak.setOrgIdList(queryOrgIdList);
////                queryDTOBak.setAccountCodeList(queryAccountCodeList);
////            }
//            List<AccountBalanceSheetVO> list2 = accountAssistBalanceMapper.selectVoucherPart(queryDTOBak);
//            List<AccountBalanceSheetVO> list3 = accountAssistBalanceMapper.selectManualPart(queryDTOBak);
//            Map<String, AccountBalanceSheetVO> tmpMap = list1.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode(), Function.identity(), ((key1 , key2) -> key1)));
//
//            AddBalanceList(list2, tmpMap);
//            AddBalanceList(list3, tmpMap);
//            for(Map.Entry<String, AccountBalanceSheetVO> entry: tmpMap.entrySet()){
//                resultList.add(entry.getValue());
//            }
//        }


//        if(StringUtils.equals("assist", queryType)){
//            resultList = accountAssistBalanceMapper.selectAssistPart(queryDTO);
//            if(isQuery){
//                sizeMap = accountAssistBalanceMapper.selectAssistPartSize(queryDTO);
//            }
//        }else if(StringUtils.equals("voucher", queryType)){
//            resultList = accountAssistBalanceMapper.selectVoucherPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectVoucherPartSize(queryDTO);
//            }
//        }else if(StringUtils.equals("manual", queryType)){
//            resultList = accountAssistBalanceMapper.selectManualPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectManualPartSize(queryDTO);
//            }
//        }else if(StringUtils.equals("assist+voucher", queryType)){
//            List<AccountBalanceSheetVO> list1 = accountAssistBalanceMapper.selectAssistPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectAssistPartSize(queryDTO);
//            }
//            List<AccountBalanceSheetVO> list2 = accountAssistBalanceMapper.selectVoucherPart(queryDTOBak);
//            Map<String, AccountBalanceSheetVO> tmpMap = list1.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode(), Function.identity(), ((key1 , key2) -> key1)));
//
//            AddBalanceList(list2, tmpMap);
//            for(Map.Entry<String, AccountBalanceSheetVO> entry: tmpMap.entrySet()){
//                resultList.add(entry.getValue());
//            }
//        }else if(StringUtils.equals("assist+manual", queryType)){
//            List<AccountBalanceSheetVO> list1 = accountAssistBalanceMapper.selectAssistPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectAssistPartSize(queryDTO);
//            }
//            List<AccountBalanceSheetVO> list2 = accountAssistBalanceMapper.selectManualPart(queryDTOBak);
//            Map<String, AccountBalanceSheetVO> tmpMap = list1.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode(), Function.identity(), ((key1 , key2) -> key1)));
//
//            AddBalanceList(list2, tmpMap);
//            for(Map.Entry<String, AccountBalanceSheetVO> entry: tmpMap.entrySet()){
//                resultList.add(entry.getValue());
//            }
//        }else if(StringUtils.equals("voucher+manual", queryType)){
//            List<AccountBalanceSheetVO> list1 = accountAssistBalanceMapper.selectVoucherPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectVoucherPartSize(queryDTO);
//            }
//            List<AccountBalanceSheetVO> list2 = accountAssistBalanceMapper.selectManualPart(queryDTOBak);
//            Map<String, AccountBalanceSheetVO> tmpMap = list1.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode(), Function.identity(), ((key1 , key2) -> key1)));
//
//            AddBalanceList(list2, tmpMap);
//            for(Map.Entry<String, AccountBalanceSheetVO> entry: tmpMap.entrySet()){
//                resultList.add(entry.getValue());
//            }
//        }else if(StringUtils.equals("assist+voucher+manual", queryType)){
//            List<AccountBalanceSheetVO> list1 = accountAssistBalanceMapper.selectAssistPart(queryDTO);
//            if(isQuery) {
//                sizeMap = accountAssistBalanceMapper.selectAssistPartSize(queryDTO);
//            }
////            if(CollectionUtil.isNotEmpty(list1)){
////                queryOrgIdList = list1.stream().map(AccountBalanceSheetVO::getOrgId).distinct().collect(Collectors.toList());
////                queryAccountCodeList = list1.stream().map(AccountBalanceSheetVO::getAccountCode).distinct().collect(Collectors.toList());
////                queryDTOBak.setOrgIdList(queryOrgIdList);
////                queryDTOBak.setAccountCodeList(queryAccountCodeList);
////            }
//            List<AccountBalanceSheetVO> list2 = accountAssistBalanceMapper.selectVoucherPart(queryDTOBak);
//            List<AccountBalanceSheetVO> list3 = accountAssistBalanceMapper.selectManualPart(queryDTOBak);
//            Map<String, AccountBalanceSheetVO> tmpMap = list1.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode(), Function.identity(), ((key1 , key2) -> key1)));
//
//            AddBalanceList(list2, tmpMap);
//            AddBalanceList(list3, tmpMap);
//            for(Map.Entry<String, AccountBalanceSheetVO> entry: tmpMap.entrySet()){
//                resultList.add(entry.getValue());
//            }
//        }

        if(CollectionUtil.isNotEmpty(resultList)){
            AccountBalanceSheetQueryDTO tmpDto = BeanUtil.copyProperties(queryDTO, AccountBalanceSheetQueryDTO.class);
            tmpDto.setOtherPeriodCodeStart(queryDTO.getPeriodCodeStart());
            tmpDto.setOtherPeriodCodeEnd(queryDTO.getPeriodCodeEnd());
//            List<AccountBalanceSheetVO> voucherIdList = accountAssistBalanceMapper.selectVoucherIdPart(tmpDto);
            List<AccountEntity> accountList = accountService.list(new QueryWrapper<AccountEntity>().select("DISTINCT account_code", "account_name").lambda());

            Map<String, AccountBalanceSheetVO> tmpMap = resultList.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode(), Function.identity(), ((key1 , key2) -> key1)));
//            Map<String, AccountBalanceSheetVO> tmpVoucherIdMap = voucherIdList.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode(), Function.identity(), ((key1 , key2) -> key1)));
            Map<String, String> accountMap = accountList.stream().collect(Collectors.toMap(AccountEntity::getAccountCode, AccountEntity::getAccountName,(k, v) -> k));
            Map<String, String> orgMap = getOrgNameOrgId();
            for(Map.Entry<String, AccountBalanceSheetVO> entry: tmpMap.entrySet()){
                AccountBalanceSheetVO vo = entry.getValue();
//                if(tmpVoucherIdMap.containsKey(entry.getKey())){
//                    vo.setVoucherId(tmpVoucherIdMap.get(entry.getKey()).getVoucherId());
//                }
                if(accountMap.containsKey(vo.getAccountCode())){
                    vo.setAccountName(accountMap.get(vo.getAccountCode()));
                }
                if(orgMap.containsKey(vo.getOrgId())){
                    vo.setOrgName(orgMap.get(vo.getOrgId()));
                }
//                entry.setValue(vo);
                vo.setPeriodCodeStart(queryDTO.getPeriodCodeStart());
                vo.setPeriodCodeEnd(queryDTO.getPeriodCodeEnd());
                vo.setPeriodCode(queryDTO.getPeriodCodeStart()+"-"+queryDTO.getPeriodCodeEnd());
                finalList.add(vo);
            }
            finalMap.put("total_size", sizeMap.get("total_size"));
        }
        return;
    }

    private static void AddBalanceList(List<AccountBalanceSheetVO> list, Map<String, AccountBalanceSheetVO> tmpMap) {
        if(CollectionUtil.isNotEmpty(list)){
            //把tmpMap2的数据加到tmpMap上
            Map<String, AccountBalanceSheetVO> tmpMap2 = list.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode(), Function.identity(), ((key1 , key2) -> key1)));
            for(Map.Entry<String, AccountBalanceSheetVO> entry: tmpMap2.entrySet()){
                if(tmpMap.containsKey(entry.getKey())){
                    AccountBalanceSheetVO tmpVo = tmpMap.get(entry.getKey());
                    AccountBalanceSheetVO tmpVo2 = entry.getValue();
//                        tmpVo.setMonthBeginCreditBalance();
//                        tmpVo.setMonthBeginDebitBalance();
                    tmpVo.setMonthCreditAmount(NumberUtil.add(tmpVo.getMonthCreditAmount(), tmpVo2.getMonthCreditAmount()));
                    tmpVo.setMonthDebitAmount(NumberUtil.add(tmpVo.getMonthDebitAmount(), tmpVo2.getMonthDebitAmount()));
                    tmpVo.setMonthEndCreditBalance(NumberUtil.add(tmpVo.getMonthEndCreditBalance(), tmpVo2.getMonthCreditAmount()));
                    tmpVo.setMonthEndDebitBalance(NumberUtil.add(tmpVo.getMonthEndDebitBalance(), tmpVo2.getMonthDebitAmount()));
//                        tmpVo.setYearBeginCreditBalance();
//                        tmpVo.setYearBeginDebitBalance();
                    tmpVo.setYearCreditAmount(NumberUtil.add(tmpVo.getYearCreditAmount(), tmpVo2.getMonthCreditAmount()));
                    tmpVo.setYearDebitAmount(NumberUtil.add(tmpVo.getYearDebitAmount(), tmpVo2.getMonthDebitAmount()));

                    tmpMap.put(entry.getKey(), tmpVo);
                }else {
                    AccountBalanceSheetVO tmpVo3 = entry.getValue();
                    tmpVo3.setMonthBeginCreditBalance(BigDecimal.ZERO);
                    tmpVo3.setMonthBeginDebitBalance(BigDecimal.ZERO);
                    tmpVo3.setMonthEndCreditBalance(tmpVo3.getMonthCreditAmount());
                    tmpVo3.setMonthEndDebitBalance(tmpVo3.getMonthDebitAmount());
                    tmpVo3.setYearBeginCreditBalance(BigDecimal.ZERO);
                    tmpVo3.setYearBeginDebitBalance(BigDecimal.ZERO);
                    tmpVo3.setYearCreditAmount(tmpVo3.getMonthCreditAmount());
                    tmpVo3.setYearDebitAmount(tmpVo3.getMonthDebitAmount());
                    tmpMap.put(entry.getKey(), tmpVo3);
                }
            }
        }
    }

    private static void AddAccountBalanceList(List<AccountAssistBalanceVO> list, Map<String, AccountAssistBalanceVO> tmpMap) {
        if(CollectionUtil.isNotEmpty(list)){
            //tmpMap2是要被加到tmpMap上的
            Map<String, AccountAssistBalanceVO> tmpMap2 = list.stream().collect(Collectors.toMap(m->m.getOrgId()+"&"+m.getAccountCode()+"&"+m.getCurrencyCode()+"&"+m.getContractCode()+"&"+m.getBillContractCode(), Function.identity(), ((key1 , key2) -> key1)));
            for(Map.Entry<String, AccountAssistBalanceVO> entry: tmpMap2.entrySet()){
                if(tmpMap.containsKey(entry.getKey())){
                    AccountAssistBalanceVO tmpVo = tmpMap.get(entry.getKey());
                    AccountAssistBalanceVO tmpVo2 = entry.getValue();
//                        tmpVo.setMonthBeginCreditBalance();
//                        tmpVo.setMonthBeginDebitBalance();
                    tmpVo.setMonthCreditAmount(NumberUtil.add(tmpVo.getMonthCreditAmount(), tmpVo2.getMonthCreditAmount()));
                    tmpVo.setMonthDebitAmount(NumberUtil.add(tmpVo.getMonthDebitAmount(), tmpVo2.getMonthDebitAmount()));
                    tmpVo.setMonthEndCreditBalance(NumberUtil.add(tmpVo.getMonthEndCreditBalance(), tmpVo2.getMonthCreditAmount()));
                    tmpVo.setMonthEndDebitBalance(NumberUtil.add(tmpVo.getMonthEndDebitBalance(), tmpVo2.getMonthDebitAmount()));
//                        tmpVo.setYearBeginCreditBalance();
//                        tmpVo.setYearBeginDebitBalance();
                    tmpVo.setYearCreditAmount(NumberUtil.add(tmpVo.getYearCreditAmount(), tmpVo2.getMonthCreditAmount()));
                    tmpVo.setYearDebitAmount(NumberUtil.add(tmpVo.getYearDebitAmount(), tmpVo2.getMonthDebitAmount()));
                    tmpMap.put(entry.getKey(), tmpVo);
                }else {
                    AccountAssistBalanceVO tmpVo3 = entry.getValue();
//                    tmpVo3.setClientCode();
//                    tmpVo3.setBillContractCode();
//                    tmpVo3.setBusinessCode();
//                    tmpVo3.setCurrencyCode();

                    tmpVo3.setMonthBeginCreditBalance(BigDecimal.ZERO);
                    tmpVo3.setMonthBeginDebitBalance(BigDecimal.ZERO);
                    tmpVo3.setMonthEndCreditBalance(tmpVo3.getMonthCreditAmount());
                    tmpVo3.setMonthEndDebitBalance(tmpVo3.getMonthDebitAmount());
                    tmpVo3.setYearBeginCreditBalance(BigDecimal.ZERO);
                    tmpVo3.setYearBeginDebitBalance(BigDecimal.ZERO);
                    tmpVo3.setYearCreditAmount(tmpVo3.getMonthCreditAmount());
                    tmpVo3.setYearDebitAmount(tmpVo3.getMonthDebitAmount());
                    tmpMap.put(entry.getKey(), tmpVo3);
                }
            }
        }
    }

    public static void main(String[] args) {
        BigDecimal a = NumberUtil.add(null, BigDecimal.ONE);
        System.out.println(a);

        Integer b = 202405;
        LocalDateTime dateTime = LocalDateTimeUtil.parse(b.toString(), DateTimeFormatter.ofPattern("yyyyMM")).plusMonths(1);
        String c = dateTime.format(DateTimeFormatter.ofPattern("yyyyMM"));
        System.out.println(dateTime);
        System.out.println(c);

    }
    private void fillRecords(List<AccountBalanceSheetVO> records) {
        if(CollectionUtil.isNotEmpty(records)){
            Map<String, String> orgMap = getOrgNameOrgId();
            List<String> accountCodes = records.stream().map(AccountBalanceSheetVO::getAccountCode).distinct().collect(Collectors.toList());
            Map<String, String> accountMap = getAccountNameAccountCode(accountCodes);

            records.forEach(v->{
                if(StringUtils.isNotEmpty(v.getOrgId())&&orgMap.containsKey(v.getOrgId())){
                    v.setOrgName(orgMap.get(v.getOrgId()));
                }
                if(StringUtils.isNotEmpty(v.getAccountCode())&&accountMap.containsKey(v.getAccountCode())){
                    v.setAccountName(accountMap.get(v.getAccountCode()));
                }

            });
        }
    }

    private static QueryWrapper<AccountAssistBalanceEntity> getAccountQueryWrapper(AccountBalanceSheetQueryDTO queryDTO) {
        QueryWrapper<AccountAssistBalanceEntity> queryWrapper = Wrappers.query(AccountAssistBalanceEntity.class);
        queryWrapper.select("period_code","org_id","account_code",
                        "sum(year_begin_debit_balance) year_begin_debit_balance","sum(year_begin_credit_balance) year_begin_credit_balance",
                        "sum(month_begin_debit_balance) month_begin_debit_balance", "sum(month_begin_credit_balance) month_begin_credit_balance",
                        "sum(month_debit_amount) month_debit_amount","sum(month_credit_amount) month_credit_amount",
                        "sum(year_debit_amount) year_debit_amount","sum(year_credit_amount) year_credit_amount",
                        "sum(month_end_debit_balance) month_end_debit_balance","sum(month_end_credit_balance) month_end_credit_balance")
                .groupBy("org_id","account_code","period_code")
                .orderByAsc("account_code");
        if (ObjectUtil.isNotNull(queryDTO.getPeriodCode())) {
            queryWrapper.eq("period_code", queryDTO.getPeriodCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getAccountName())) {
            queryWrapper.like("account_name", queryDTO.getAccountName());
        }
//        if (StringUtils.isNotEmpty(queryDTO.getContractCode())) {
//            queryWrapper.like("contract_code",queryDTO.getContractCode());
//        }
        if (StringUtils.isNotEmpty(queryDTO.getOrgId())) {
            queryWrapper.eq("org_id", queryDTO.getOrgId());
        }
        if (StringUtils.isNotEmpty(queryDTO.getAccountCodeStart())) {
            queryWrapper.ge("account_code", queryDTO.getAccountCodeStart());
        }
        if (StringUtils.isNotEmpty(queryDTO.getAccountCodeEnd())) {
            queryWrapper.le("account_code", queryDTO.getAccountCodeEnd());
        }
        return queryWrapper;
    }

    private Map<String, String> getAccountNameAccountCode(List<String> accountCodes) {
        Map<String, String> accountMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(accountCodes)){
            List<AccountEntity> accountEntityList = accountService.list(new LambdaQueryWrapper<AccountEntity>().in(AccountEntity::getAccountCode, accountCodes));
            if(CollectionUtil.isNotEmpty(accountEntityList)){
                accountMap = accountEntityList.stream().collect(HashMap::new,(map,item)->map.put(item.getAccountCode(),item.getAccountName()),HashMap::putAll);
            }
        }
        return accountMap;
    }

    /**
     * 更新辅助帐科目余额表
     * 年初余额=上年年末余额
     * 期初余额=上月期末余额
     * 本期发生额=本期凭证汇总额
     * 本年累计=年初发生额到现在的汇总
     * 期末余额=期初余额+本期发生额
     */
    @Override
    public void saveAccountBalanceByVoucherDTO(VoucherDTO voucherDTO) {
        //1.查询本期的余额
        Integer periodCode = voucherDTO.getPeriodCode();
        List<VoucherEntryDTO> voucherEntryList = voucherDTO.getEntryList();
        if (CollectionUtil.isEmpty(voucherEntryList)){
            return;
        }
        for (VoucherEntryDTO entryDTO : voucherEntryList) {
            AccountAssistBalanceGetOneQueryDTO queryDTO = BeanUtil.copyProperties(voucherDTO, AccountAssistBalanceGetOneQueryDTO.class);
            BeanUtil.copyProperties(entryDTO, queryDTO);
            AccountAssistBalanceEntity balanceEntity = this.getOneAccountAssistBalance(queryDTO);
            if (balanceEntity != null){
                //存在，则累加本期余额、年累计发生额、期末余额
                //本期发生额
                balanceEntity.setMonthDebitAmount(NumberUtil.add(balanceEntity.getMonthDebitAmount(), entryDTO.getDebitAmount()));
                balanceEntity.setMonthCreditAmount(NumberUtil.add(balanceEntity.getMonthCreditAmount(), entryDTO.getCreditAmount()));
                //年累计发生额
                balanceEntity.setYearDebitAmount(NumberUtil.add(balanceEntity.getYearDebitAmount(), entryDTO.getDebitAmount()));
                balanceEntity.setYearCreditAmount(NumberUtil.add(balanceEntity.getYearCreditAmount(), entryDTO.getCreditAmount()));
                //期末余额
                balanceEntity.setMonthEndDebitBalance(NumberUtil.add(balanceEntity.getMonthBeginDebitBalance(), balanceEntity.getMonthDebitAmount()));
                balanceEntity.setMonthEndCreditBalance(NumberUtil.add(balanceEntity.getMonthBeginCreditBalance(), balanceEntity.getMonthCreditAmount()));
                balanceEntity.updateById();
                //科目余额表ID关联到凭证分录
                entryDTO.setAccountAssistBalanceId(balanceEntity.getId());
            } else {
                //不存在，则插入一条新记录，其中:
                AccountAssistBalanceEntity currentEntity = new AccountAssistBalanceEntity();
                currentEntity.setPeriodCode(periodCode);
                currentEntity.setContractCode(entryDTO.getContractCode());
                currentEntity.setClientCode(entryDTO.getClientCode());
                currentEntity.setBusinessCode(voucherDTO.getBusinessCode());
                currentEntity.setOrgId(voucherDTO.getOrgId());
                currentEntity.setBillContractCode(entryDTO.getBillContractCode());
                currentEntity.setCurrencyCode(voucherDTO.getCurrency());
                currentEntity.setAccountCode(entryDTO.getAccountCode());
                currentEntity.setAccountName(entryDTO.getAccountName());
                //年初余额 =上一年12月期末余额
                Integer lastYearPeriodCode = PeriodCodeUtil.getLastYearMonthPeriodCode(periodCode);
                AccountAssistBalanceGetOneQueryDTO lastYearQueryDTO = BeanUtil.copyProperties(queryDTO, AccountAssistBalanceGetOneQueryDTO.class);
                lastYearQueryDTO.setPeriodCode(lastYearPeriodCode);
                AccountAssistBalanceEntity lastYearBalanceEntity = this.getOneAccountAssistBalance(lastYearQueryDTO);
                if (lastYearBalanceEntity != null){
                    currentEntity.setYearBeginDebitBalance(lastYearBalanceEntity.getMonthEndDebitBalance());
                    currentEntity.setYearBeginCreditBalance(lastYearBalanceEntity.getMonthEndCreditBalance());
                }
                //期初余额 =上月期末余额
                Integer lastMonthPeriodCode = PeriodCodeUtil.getLastMonthPeriodCode(periodCode);
                AccountAssistBalanceGetOneQueryDTO lastMonthQueryDTO = BeanUtil.copyProperties(queryDTO, AccountAssistBalanceGetOneQueryDTO.class);
                lastMonthQueryDTO.setPeriodCode(lastMonthPeriodCode);
                AccountAssistBalanceEntity lastMonthBalanceEntity = this.getOneAccountAssistBalance(lastYearQueryDTO);
                if (lastMonthBalanceEntity != null){
                    currentEntity.setMonthBeginDebitBalance(lastMonthBalanceEntity.getMonthEndDebitBalance());
                    currentEntity.setMonthBeginCreditBalance(lastMonthBalanceEntity.getMonthEndCreditBalance());
                }
                //本期发生额 = 当前凭证金额
                currentEntity.setMonthDebitAmount(entryDTO.getDebitAmount());
                currentEntity.setMonthCreditAmount(entryDTO.getCreditAmount());
                //年累计发生额 = 当前凭证金额
                currentEntity.setYearDebitAmount(entryDTO.getDebitAmount());
                currentEntity.setYearCreditAmount(entryDTO.getCreditAmount());
                //期末余额 =上月期末余额+当前凭证金额
                currentEntity.setMonthEndDebitBalance(NumberUtil.add(currentEntity.getMonthBeginDebitBalance(), currentEntity.getMonthDebitAmount()));
                currentEntity.setMonthEndCreditBalance(NumberUtil.add(currentEntity.getMonthBeginCreditBalance(), currentEntity.getMonthCreditAmount()));
                //保存
                this.save(currentEntity);
                //科目余额表ID关联到凭证分录
                entryDTO.setAccountAssistBalanceId(currentEntity.getId());
            }
            //更新凭证分录
            voucherEntryService.updateVoucherEntry(entryDTO.getId(), entryDTO);
        }
    }

    @Override
    public AccountAssistBalanceEntity getOneAccountAssistBalance(AccountAssistBalanceGetOneQueryDTO queryDTO) {
        LambdaQueryWrapper<AccountAssistBalanceEntity> queryWrapper = Wrappers.<AccountAssistBalanceEntity>lambdaQuery();
        queryWrapper.eq(AccountAssistBalanceEntity::getPeriodCode, queryDTO.getPeriodCode());
        queryWrapper.eq(AccountAssistBalanceEntity::getOrgId, queryDTO.getOrgId());
        queryWrapper.eq(AccountAssistBalanceEntity::getAccountCode, queryDTO.getAccountCode());
        if (StrUtil.isNotBlank(queryDTO.getCurrencyCode())) {
            queryWrapper.eq(AccountAssistBalanceEntity::getCurrencyCode, queryDTO.getCurrencyCode());
        } else {
            queryWrapper.isNull(AccountAssistBalanceEntity::getCurrencyCode);
        }
        if (StrUtil.isNotBlank(queryDTO.getContractCode())){
            queryWrapper.eq(AccountAssistBalanceEntity::getContractCode, queryDTO.getContractCode());
        } else {
            queryWrapper.isNull(AccountAssistBalanceEntity::getContractCode);
        }
        if (StrUtil.isNotBlank(queryDTO.getClientCode())){
            queryWrapper.eq(AccountAssistBalanceEntity::getClientCode, queryDTO.getClientCode());
        } else {
            queryWrapper.isNull(AccountAssistBalanceEntity::getClientCode);
        }
        if (StrUtil.isNotBlank(queryDTO.getBusinessCode())){
            queryWrapper.eq(AccountAssistBalanceEntity::getBusinessCode, queryDTO.getBusinessCode());
        } else {
            queryWrapper.isNull(AccountAssistBalanceEntity::getBusinessCode);
        }
        if (StrUtil.isNotBlank(queryDTO.getBillContractCode())){
            queryWrapper.eq(AccountAssistBalanceEntity::getBillContractCode, queryDTO.getBillContractCode());
        } else {
            queryWrapper.isNull(AccountAssistBalanceEntity::getBillContractCode);
        }
        return this.getOne(queryWrapper, false);
    }

    @Override
    public List<AccountBalanceSheetExcelExportDTO> listByCondition(AccountBalanceSheetQueryDTO queryDTO) {
        String queryType = getQueryType(queryDTO.getProcessStatusList());
        queryDTO.setQueryType(queryType);

        Integer periodCodeStart = queryDTO.getPeriodCodeStart();
        Integer periodCodeEnd = queryDTO.getPeriodCodeEnd();

        setAccountPeriodCode(queryDTO, periodCodeEnd, periodCodeStart);

        if(!StringUtils.equals(periodCodeStart.toString().substring(0, 4) ,periodCodeEnd.toString().substring(0, 4))){
            queryDTO.setPeriodYearStart(Integer.valueOf(periodCodeEnd.toString().substring(0, 4)+"01"));
        }
//
//        List<AccountBalanceSheetVO> list = accountAssistBalanceMapper.selectAccountBalance(queryDTO);

        List<AccountBalanceSheetVO> finalList = Lists.newArrayList();
        Map<String, Long> finalMap = Maps.newHashMap();
        fillAccountContent(queryDTO, queryType, finalMap, finalList, false);
        
        List<AccountBalanceSheetExcelExportDTO> exportList = BeanUtil.copyToList(finalList, AccountBalanceSheetExcelExportDTO.class);

//        fillExportRecords(exportList);
        return exportList;
    }

    @Override
    public IPage<AccountAssistCurrentBalanceSheetVO> currentBalancePage(AccountAssistCurrentBalanceSheetQueryDTO queryDTO) {

        Integer periodCodeStart = queryDTO.getPeriodCodeStart();
        Integer periodCodeEnd = queryDTO.getPeriodCodeEnd();
        if(ObjectUtil.isEmpty(periodCodeStart)||ObjectUtil.isEmpty(periodCodeEnd)){
            throw new ServiceException("会计期间开始和会计期间结束都不能为空");
        }
        setParamPeriodCodeList(queryDTO, periodCodeStart, periodCodeEnd);

        Integer limit = queryDTO.getPageSize()*queryDTO.getPageNum();
        Integer offset = queryDTO.getPageSize()*(queryDTO.getPageNum()-1);
        queryDTO.setLimit(limit);
        queryDTO.setOffset(offset);
        Map<String, Long> sizeMap = accountAssistBalanceMapper.selectCurrentBalanceSize(queryDTO);
        List<AccountAssistCurrentBalanceSheetVO> list = this.getCurrentBalanceList(queryDTO);
//        fillCurrentBalance(list);
        return getIPage(queryDTO.getPageNum(), queryDTO.getPageSize(), list, sizeMap.get("total_size"));
    }

    private static void setParamPeriodCodeList(AccountAssistCurrentBalanceSheetQueryDTO queryDTO, Integer periodCodeStart, Integer periodCodeEnd) {
        List<Integer> periodCodeList = IntStream.range(periodCodeStart, periodCodeEnd +1)
                .map(i -> i).boxed().collect(Collectors.toList());
        queryDTO.setPeriodCodeList(periodCodeList);
    }

    @Override
    public List<AccountAssistCurrentBalanceSheetVO> getCurrentBalanceList(AccountAssistCurrentBalanceSheetQueryDTO queryDTO) {
        Integer periodCodeStart = queryDTO.getPeriodCodeStart();
        Integer periodCodeEnd = queryDTO.getPeriodCodeEnd();
        if(ObjectUtil.isEmpty(periodCodeStart)||ObjectUtil.isEmpty(periodCodeEnd)){
            throw new ServiceException("会计期间开始和会计期间结束都不能为空");
        }
        setParamPeriodCodeList(queryDTO, periodCodeStart, periodCodeEnd);
        List<AccountAssistCurrentBalanceSheetVO> list = accountAssistBalanceMapper.selectCurrentBalance(queryDTO);

        fillCurrentBalance(list);
        return list;
    }

    @Override
    public List<AccountAssistBalanceVO> getAssistBalanceData(AccountAssistBalanceQueryDTO queryDTO) {
        String queryType = getQueryType(queryDTO.getProcessStatusList());
        queryDTO.setQueryType(queryType);

        Integer periodCodeStart = queryDTO.getPeriodCodeStart();
        Integer periodCodeEnd = queryDTO.getPeriodCodeEnd();

        setAssistPeriodCode(queryDTO, periodCodeEnd, periodCodeStart);

        if(!StringUtils.equals(periodCodeStart.toString().substring(0, 4) ,periodCodeEnd.toString().substring(0, 4))){
            queryDTO.setPeriodYearStart(Integer.valueOf(periodCodeEnd.toString().substring(0, 4)+"01"));
        }

//        Integer limit = queryDTO.getPageSize()*queryDTO.getPageNum();
//        Integer offset = queryDTO.getPageSize()*(queryDTO.getPageNum()-1);
//        queryDTO.setLimit(limit);
//        queryDTO.setOffset(offset);


//        List<AccountAssistBalanceVO> list = accountAssistBalanceMapper.selectAssistBalance(queryDTO);
//        Map<String, Long> sizeMap = accountAssistBalanceMapper.selectAssistBalanceSize(queryDTO);
        List<AccountAssistBalanceVO> finalList = Lists.newArrayList();
        Map<String, Long> finalMap = Maps.newHashMap();

        fillAssistContent(queryDTO, queryType, finalMap, finalList, false);

        return finalList;
    }

    @Override
    public Map<String, String> generateAssistBalanceExcel(AccountAssistBalanceQueryDTO queryDTO) {
        String fileName = "assist_balance_" + LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss")+".xlsx";

        FileRecordEntity record = new FileRecordEntity();
        record.setModuleName(ModuleEnum.QUERY_MODULE.getCode());
        record.setBusinessScene(BusinessSceneEnum.ASSIST_BALANCE.getCode());
        record.setFileLocation(getFilePath()+fileName);
        record.setFileName(fileName);
        record.setExecuteStatus(CheckExecuteStatusEnum.INPROGRESS.getCode());
        record.setFileUploadBy(String.valueOf(SecurityUtils.getUserId()));
        fileRecordService.save(record);

        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            // 异步执行的任务
            queryAndWriteAssistBalance(queryDTO, fileName);
        }, asyncTaskExecutor).thenRun(() -> {
            FileRecordEntity tmp = new FileRecordEntity();
            tmp.setId(record.getId());
            tmp.setExecuteStatus(CheckExecuteStatusEnum.FINISH.getCode());
            tmp.setFileUploadTime(LocalDateTime.now());

            fileRecordService.updateById(tmp);
        });

        Map<String, String> map = Maps.newLinkedHashMap();
        map.put("fileName", fileName);
        map.put("location", getFilePath()+fileName);
        map.put("servicePath", servicePath);
        return map;
    }

    @Override
    public IPage<FileRecordEntity> selectAssistBalanceFileList(FileRecordQueryDTO queryDTO) {
        queryDTO.setExecuteStatus(Arrays.asList(CheckExecuteStatusEnum.FINISH.getCode(), CheckExecuteStatusEnum.INPROGRESS.getCode()));
        queryDTO.setModuleName(ModuleEnum.QUERY_MODULE.getCode());
        queryDTO.setBusinessScene(BusinessSceneEnum.ASSIST_BALANCE.getCode());
        return fileRecordService.selectFileListByModuleAndBusiness(queryDTO);
    }

    private void queryAndWriteAssistBalance(AccountAssistBalanceQueryDTO queryDTO, String fileName) {
        log.info("查询要导出的数据 开始");
        long l1 = System.currentTimeMillis();

        List<AccountAssistBalanceVO> list = this.getAssistBalanceData(queryDTO);

        List<AccountAssistBalanceExcelVO> excelList = BeanUtil.copyToList(list, AccountAssistBalanceExcelVO.class);
        long l2 = System.currentTimeMillis();
        log.info("查询要导出的数据 结束，用时{} s", (l2-l1)/1000);
        log.info("导出Excel数据 开始");
        ExcelWriter writer = ExcelUtil.getWriter(getFilePath()+fileName);

        writer.addHeaderAlias("periodCode", "会计期间");
        writer.addHeaderAlias("orgName", "签约主体");
        writer.addHeaderAlias("contractCode", "合同编号");
        writer.addHeaderAlias("clientName", "客户名称");
        writer.addHeaderAlias("billContractCode", "借款合同编号");
        writer.addHeaderAlias("leaseType", "租赁类型");
        writer.addHeaderAlias("currencyCode", "币种");
        writer.addHeaderAlias("accountCode", "科目编码");
        writer.addHeaderAlias("accountName", "科目名称");
        writer.addHeaderAlias("yearBeginDebitBalance", "年初借方余额");
        writer.addHeaderAlias("yearBeginCreditBalance", "年初贷方余额");
        writer.addHeaderAlias("monthBeginDebitBalance", "期初借方余额");
        writer.addHeaderAlias("monthBeginCreditBalance", "期初贷方余额");
        writer.addHeaderAlias("monthDebitAmount", "本期借方发生额");
        writer.addHeaderAlias("monthCreditAmount", "本期贷方发生额");
        writer.addHeaderAlias("yearDebitAmount", "年累计借方发生额");
        writer.addHeaderAlias("yearCreditAmount", "年累计贷方发生额");
        writer.addHeaderAlias("monthEndDebitBalance", "期末借方余额");
        writer.addHeaderAlias("monthEndCreditBalance", "期末贷方余额");

        writer.autoSizeColumnAll();
        writer.write(excelList, true);
        writer.close();
        long l3 = System.currentTimeMillis();
        log.info("导出Excel数据 结束，用时{} s", (l3-l2)/1000);
    }

    private String getFilePath(){
        String osName = System.getProperties().getProperty("os.name");
        if(osName.toLowerCase().contains("windows")){
            return basicPathWindows+"assistBalance"+ File.separator;
        }else if(osName.toLowerCase().contains("linux") || osName.toLowerCase().contains("unix")){
            return basicPathLinux+"assistBalance"+File.separator;
        }
        return StringUtils.EMPTY;
    }

    private void fillCurrentBalance(List<AccountAssistCurrentBalanceSheetVO> list) {
        if(CollectionUtil.isNotEmpty(list)){
            Map<String, String> orgMap = getOrgNameOrgId();

            List<String> accountCodes = list.stream().map(AccountAssistCurrentBalanceSheetVO::getAccountCode).distinct().collect(Collectors.toList());
            Map<String, String> accountMap = getAccountNameAccountCode(accountCodes);

            List<String> clientCodes = list.stream().map(AccountAssistCurrentBalanceSheetVO::getClientCode).distinct().collect(Collectors.toList());
            Map<String, String> clientMap = getClientNameClientCode(clientCodes);

            list.forEach(v->{
                if(StringUtils.isNotEmpty(v.getOrgId())&&orgMap.containsKey(v.getOrgId())){
                    v.setOrgName(orgMap.get(v.getOrgId()));
                }
                if(StringUtils.isNotEmpty(v.getAccountCode())&&accountMap.containsKey(v.getAccountCode())){
                    v.setAccountName(accountMap.get(v.getAccountCode()));
                }
                if(StringUtils.isNotEmpty(v.getClientCode())&&clientMap.containsKey(v.getClientCode())){
                    v.setClientName(clientMap.get(v.getClientCode()));
                }
                if(StringUtils.isNotEmpty(v.getBusinessCode())){
                    v.setBusinessCodeDesc(v.getBusinessCode());
                }
            });
        }
    }

    private static <T> IPage<T> getIPage(int pageNum, int pageSize, List<T> list, long totalSize) {
        IPage<T> page = new Page<>(pageNum, pageSize);
        page.setRecords(list);
        page.setTotal(totalSize);
        return page;
    }

    private void fillExportRecords(List<AccountBalanceSheetExcelExportDTO> records) {
        if(CollectionUtil.isNotEmpty(records)){
            Map<String, String> orgMap = getOrgNameOrgId();
//            List<String> accountCodes = records.stream().map(AccountBalanceSheetExcelExportDTO::getAccountCode).distinct().collect(Collectors.toList());
//            Map<String, String> accountMap = getAccountNameAccountCode(accountCodes);

            records.forEach(v->{
                if(StringUtils.isNotEmpty(v.getOrgId())&&orgMap.containsKey(v.getOrgId())){
                    v.setOrgName(orgMap.get(v.getOrgId()));
                }
//                if(StringUtils.isNotEmpty(v.getAccountCode())&&accountMap.containsKey(v.getAccountCode())){
//                    v.setAccountName(accountMap.get(v.getAccountCode()));
//                }

            });
        }
    }

    private Map<String,String> getOrgNameOrgId(){
        Map<String,String> orgNameAndIdMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  iOrgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgNameAndIdMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId,OrgCompanyVO::getOrgName, (k1, k2)->k2));
        }
        return orgNameAndIdMap;
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
}

