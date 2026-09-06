package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ContractAccountBalanceVO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractBalanceVO;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceTempEntity;
import com.utfinancing.financehub.engine.finance.mapper.ContractBalanceMapper;
import com.utfinancing.financehub.engine.finance.service.IContractBalanceLatestService;
import com.utfinancing.financehub.engine.finance.service.IContractBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IContractBalanceTempService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.model.dto.AccountDTO;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-23
 * @Description :  ContractBalance服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ContractBalanceServiceImpl extends ServiceImpl<ContractBalanceMapper, ContractBalanceEntity> implements IContractBalanceService {

    private final ContractBalanceMapper contractBalanceMapper;
    private final IOrgCompanyService orgCompanyService;
    private final IContractBalanceLatestService iContractBalanceLatestService;
    private final IAccountService accountService;

    private final IContractBalanceTempService contractBalanceTempService;

    @Override
    public Long saveContractBalance(ContractBalanceDTO dto) {
        ContractBalanceEntity entity = BeanUtil.copyProperties(dto, ContractBalanceEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateContractBalance(Long id, ContractBalanceDTO dto) {
        ContractBalanceEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ContractBalanceDTO getContractBalanceDTOById(Long id) {
        ContractBalanceEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ContractBalanceDTO.class);
    }

    @Override
    public IPage<ContractBalanceVO> selectPage(ContractBalanceQueryDTO queryDTO) {
        LambdaQueryWrapper<ContractBalanceEntity> queryWrapper = Wrappers.<ContractBalanceEntity>lambdaQuery();
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getBusinessCode()), ContractBalanceEntity::getBusinessCode, queryDTO.getBusinessCode());
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getSceneCode()), ContractBalanceEntity::getSceneCode, queryDTO.getSceneCode());
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getClientCode()), ContractBalanceEntity::getClientCode, queryDTO.getClientCode());
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getContractCode()), ContractBalanceEntity::getContractCode, queryDTO.getContractCode());
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getOrgId()), ContractBalanceEntity::getOrgId, queryDTO.getOrgId());
        if (ObjectUtil.isNotNull(queryDTO.getVoucherDateStart())){
            queryWrapper.ge(ContractBalanceEntity::getVoucherDate, queryDTO.getVoucherDateStart());
        }
        if (ObjectUtil.isNotNull(queryDTO.getVoucherDateEnd())){
            queryWrapper.lt(ContractBalanceEntity::getVoucherDate, LocalDateTimeUtil.offset(queryDTO.getVoucherDateEnd().atStartOfDay(),1, ChronoUnit.DAYS));
        }
        IPage<ContractBalanceEntity> entityIPage = contractBalanceMapper.selectPage(new Page<ContractBalanceEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ContractBalanceVO.class);
    }

    @Override
    public Map<String, Object> getMap(Long id) {
        return this.getMap(Wrappers.<ContractBalanceEntity>lambdaQuery().eq(ContractBalanceEntity::getId, id));
    }

    /**
     * 查询最新的合同科目余额, 只返回余额字段
     *
     * @param businessCode 业务编码， 必须要传
     * @param contractCode 合同编码， 如果不传，查询整体
     * @param clientCode   客户编码， 如果不传，查询整体
     * @return
     */
    @Override
    public Map<String, Object> getLastBalanceMap(String businessCode, String clientCode, String contractCode,String orgId,String billContractCode) {
//        long start = System.currentTimeMillis();
//        LambdaQueryWrapper<ContractBalanceEntity> lambdaQueryWrapper = Wrappers.<ContractBalanceEntity>lambdaQuery();
//        lambdaQueryWrapper.eq(ContractBalanceEntity::getBusinessCode, businessCode);
//        if (StrUtil.isNotBlank(clientCode)) {
//            lambdaQueryWrapper.eq(ContractBalanceEntity::getClientCode, clientCode);
//        } else {
//            lambdaQueryWrapper.isNull(ContractBalanceEntity::getClientCode);
//        }
//        if (StrUtil.isNotBlank(contractCode)) {
//            lambdaQueryWrapper.eq(ContractBalanceEntity::getContractCode, contractCode);
//        } else {
//            lambdaQueryWrapper.isNull(ContractBalanceEntity::getContractCode);
//        }
//        lambdaQueryWrapper.orderByDesc(ContractBalanceEntity::getVoucherDate);
//        lambdaQueryWrapper.last("limit 1");
//        Map<String, Object> rowMap = this.getMap(lambdaQueryWrapper);
//        if (MapUtil.isEmpty(rowMap)) {
//            Map<String, Object> emptyMap = new HashMap<>();
//            emptyMap.put(ContractBalanceColumnsEnum.BUSINESS_CODE.getCode(), businessCode);
//            emptyMap.put(ContractBalanceColumnsEnum.CLIENT_CODE.getCode(), clientCode);
//            emptyMap.put(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode(), contractCode);
//            return emptyMap;
//        }
//        //过滤发生额和其他字段，只保留余额字段
//        for (Iterator<Map.Entry<String, Object>> it = rowMap.entrySet().iterator(); it.hasNext(); ) {
//            Map.Entry<String, Object> item = it.next();
//            if (!StrUtil.containsAny(item.getKey(), "_balance") && !StrUtil.equals(item.getKey(), "id")) {
//                it.remove();
//            }
//        }
//        rowMap.put(ContractBalanceColumnsEnum.BUSINESS_CODE.getCode(), businessCode);
//        rowMap.put(ContractBalanceColumnsEnum.CLIENT_CODE.getCode(), clientCode);
//        rowMap.put(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode(), contractCode);
//        long end = System.currentTimeMillis();
//        log.info("getLastBalanceMap 查询耗时：{}ms",(end-start));
        return iContractBalanceLatestService.getLastBalanceMap(businessCode,clientCode,contractCode,orgId,billContractCode);
    }

    @Override
    public Map<String, Object> getOriginalLastBalanceMap(String businessCode, String clientCode, String contractCode) {
        long start = System.currentTimeMillis();
        LambdaQueryWrapper<ContractBalanceEntity> lambdaQueryWrapper = Wrappers.<ContractBalanceEntity>lambdaQuery();
        lambdaQueryWrapper.eq(ContractBalanceEntity::getDelFlag,"0");
        lambdaQueryWrapper.eq(ContractBalanceEntity::getBusinessCode, businessCode);
        if (StrUtil.isNotBlank(clientCode)) {
            lambdaQueryWrapper.eq(ContractBalanceEntity::getClientCode, clientCode);
        } else {
            lambdaQueryWrapper.isNull(ContractBalanceEntity::getClientCode);
        }
        if (StrUtil.isNotBlank(contractCode)) {
            lambdaQueryWrapper.eq(ContractBalanceEntity::getContractCode, contractCode);
        } else {
            lambdaQueryWrapper.isNull(ContractBalanceEntity::getContractCode);
        }
        lambdaQueryWrapper.orderByDesc(ContractBalanceEntity::getId);
        lambdaQueryWrapper.last("limit 1");
        Map<String, Object> rowMap = this.getMap(lambdaQueryWrapper);
        if (MapUtil.isEmpty(rowMap)) {
            Map<String, Object> emptyMap = new HashMap<>();
            emptyMap.put(ContractBalanceColumnsEnum.BUSINESS_CODE.getCode(), businessCode);
            emptyMap.put(ContractBalanceColumnsEnum.CLIENT_CODE.getCode(), clientCode);
            emptyMap.put(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode(), contractCode);
            return emptyMap;
        }
        rowMap.put(ContractBalanceColumnsEnum.BUSINESS_CODE.getCode(), businessCode);
        rowMap.put(ContractBalanceColumnsEnum.CLIENT_CODE.getCode(), clientCode);
        rowMap.put(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode(), contractCode);
        long end = System.currentTimeMillis();
        log.info("getLastBalanceMap 查询耗时：{}ms",(end-start));
        return rowMap;
    }


    /**
     * 构建新的记录map
     *
     * @param fundTypeMap
     * @return
     */
    private Map<String, Object> buildNewContractBalanceMap(Map<String, BigDecimal> fundTypeMap, Map<String, Object> lastBalanceMap, VoucherDTO voucherDTO) {
        Map<String, Object> rowMap = new HashMap<>();
        Iterator<Map.Entry<String, BigDecimal>> it = fundTypeMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, BigDecimal> entry = it.next();
            //发生额字段
            rowMap.put(entry.getKey() + "_amount", entry.getValue());
            //余额字段
            rowMap.put(entry.getKey() + "_balance", entry.getValue());
        }
        rowMap.put(ContractBalanceColumnsEnum.BUSINESS_CODE.getCode(), lastBalanceMap.get(ContractBalanceColumnsEnum.BUSINESS_CODE.getCode()));
        rowMap.put(ContractBalanceColumnsEnum.CLIENT_CODE.getCode(), lastBalanceMap.get(ContractBalanceColumnsEnum.CLIENT_CODE.getCode()));
        rowMap.put(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode(), lastBalanceMap.get(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode()));
        rowMap.put(ContractBalanceColumnsEnum.VOUCHER_ID.getCode(), voucherDTO.getId());
        rowMap.put(ContractBalanceColumnsEnum.BUSINESS_DATE.getCode(), voucherDTO.getBusinessDate());
        rowMap.put(ContractBalanceColumnsEnum.VOUCHER_DATE.getCode(), voucherDTO.getVoucherDate());
        rowMap.put(ContractBalanceColumnsEnum.SCENE_CODE.getCode(), voucherDTO.getSceneCode());
        rowMap.put(ContractBalanceColumnsEnum.ORG_ID.getCode(), voucherDTO.getOrgId());
        rowMap.put(ContractBalanceColumnsEnum.PERIOD_CODE.getCode(), voucherDTO.getPeriodCode());
        rowMap.put(ContractBalanceColumnsEnum.SYSTEM_CODE.getCode(), voucherDTO.getSystemCode());
        if (StringUtils.isNotEmpty(voucherDTO.getClientType())) {
            rowMap.put(ContractBalanceColumnsEnum.CLIENT_TYPE.getCode(), voucherDTO.getClientType());
        }
        return rowMap;
    }

    public void insertMap(Map<String, Object> rowMap) {
        rowMap.put("id", IdWorker.getId());
        rowMap.put("update_time", LocalDateTime.now());
        rowMap.put("create_time", LocalDateTime.now());
        List<String> columns = ListUtil.toList();
        List<Object> values = ListUtil.toList();
        for (Map.Entry<String, Object> mapEntry : rowMap.entrySet()) {
            columns.add(mapEntry.getKey());
            values.add(mapEntry.getValue());
        }
        //入库
        contractBalanceMapper.insertContractBalance(columns, values);
    }

    @Override
    public IPage<ContractBalanceVO> selectLatestBalance(ContractBalanceQueryDTO contractBalanceQueryDTO) {
        Page page = new Page(contractBalanceQueryDTO.getPageNum(),contractBalanceQueryDTO.getPageSize());
        return contractBalanceMapper.selectLatestBalance(page,contractBalanceQueryDTO);
    }

    private void insertContractBalance(Map<String, BigDecimal> fundTypeMap, Map<String, Object> lastBalanceMap, VoucherDTO voucherDTO,List<AccountEntity> accountEntityList) {
        Map<String, List<AccountEntity>>  fundTypeAccountListMap = accountEntityList.stream().collect(Collectors.groupingBy(AccountEntity::getFundType));
        if (lastBalanceMap.get("id") == null) {
            //如果没有以前的合同余额数据，则直接insert新的
            Map<String, Object> rowMap = this.buildNewContractBalanceMap(fundTypeMap, lastBalanceMap, voucherDTO);
            // 财务中台的数据且非提交请求时，只创建balance的数据，不更新余额表数据
            if (SystemEnum.CWZT.getCode().equals(voucherDTO.getSystemCode())
                    && !YesOrNoEnum.YES.getCode().equals(voucherDTO.getIsSubmit())) {
                contractBalanceTempService.insertMap(rowMap);
            } else {
                // 删除临时生成的balance数据
                contractBalanceTempService.delBalanceTeamData(rowMap);
                this.insertMap(rowMap);
                //保存最新的数据到最新余额表中
                iContractBalanceLatestService.insertMap(rowMap);
            }
            return;
        }
        Map<String, Object> newRowMap = new HashMap<>();
        Iterator<Map.Entry<String, Object>> it = lastBalanceMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            newRowMap.put(entry.getKey(), entry.getValue());
        }
        newRowMap.put(ContractBalanceColumnsEnum.VOUCHER_ID.getCode(), voucherDTO.getId());
        newRowMap.put(ContractBalanceColumnsEnum.BUSINESS_DATE.getCode(), voucherDTO.getBusinessDate());
        newRowMap.put(ContractBalanceColumnsEnum.VOUCHER_DATE.getCode(), voucherDTO.getVoucherDate());
        newRowMap.put(ContractBalanceColumnsEnum.SCENE_CODE.getCode(), voucherDTO.getSceneCode());
        newRowMap.put(ContractBalanceColumnsEnum.ORG_ID.getCode(), voucherDTO.getOrgId());
        newRowMap.put(ContractBalanceColumnsEnum.INTERFACE_DATA_ID.getCode(), voucherDTO.getInterfaceDataId());
        newRowMap.put(ContractBalanceColumnsEnum.PERIOD_CODE.getCode(),voucherDTO.getPeriodCode());
        newRowMap.put(ContractBalanceColumnsEnum.SYSTEM_CODE.getCode(),voucherDTO.getSystemCode());
        if (StringUtils.isNotEmpty(voucherDTO.getClientType())) {
            newRowMap.put(ContractBalanceColumnsEnum.CLIENT_TYPE.getCode(), voucherDTO.getClientType());
        }
        for (String fundType : fundTypeMap.keySet()) {
            String balanceKey = fundType + "_balance";
            String amountKey = fundType + "_amount";
            newRowMap.put(amountKey, fundTypeMap.get(fundType));
            if (lastBalanceMap.containsKey(balanceKey)) {
                BigDecimal balance = (BigDecimal) lastBalanceMap.get(balanceKey);
                BigDecimal amount = fundTypeMap.get(fundType);
                //余额 = 余额 + 发生额
//                String debitCreditType = fundTypeAccountListMap.get(fundType).get(0).getDebitCreditType();
//                int directe = 1;
//                if ("CR".equals(debitCreditType)){
//                    directe = -1;
//                }
                //计算新的余额，然后覆盖
                BigDecimal newBalance = NumberUtil.add(balance, amount);
                newRowMap.put(balanceKey, newBalance);
            }
        }
        //插入合同余额表
        // 财务中台的数据且非提交请求时，只创建balance的数据，不更新余额表数据
        if (SystemEnum.CWZT.getCode().equals(voucherDTO.getSystemCode())
                && !YesOrNoEnum.YES.getCode().equals(voucherDTO.getIsSubmit())) {
            contractBalanceTempService.insertMap(newRowMap);
        } else {
            //更新最新的合同余额表
            iContractBalanceLatestService.updateLastBalanceMap(newRowMap);
            // 删除临时生成的balance数据
            contractBalanceTempService.delBalanceTeamData(newRowMap);
            this.insertMap(newRowMap);
        }
    }


    @Override
    public void saveContractBalanceFromVoucher(VoucherDTO voucherDTO) {
        if (voucherDTO == null || voucherDTO.getId() == null) {
            return;
        }
        // 接口重试/重新执行时同一张凭证不能重复累计余额。
        if (this.lambdaQuery().eq(ContractBalanceEntity::getVoucherId, voucherDTO.getId()).exists()
                || contractBalanceTempService.lambdaQuery()
                .eq(ContractBalanceTempEntity::getVoucherId, voucherDTO.getId()).exists()) {
            log.info("凭证{}已更新合同余额，本次跳过", voucherDTO.getId());
            return;
        }
        List<AccountEntity> accountEntityList = accountService.list(Wrappers.<AccountEntity>lambdaQuery());
        List<VoucherEntryDTO> voucherEntryList = voucherDTO.getEntryList();
        if (CollectionUtil.isNotEmpty(voucherEntryList)){
            //根据维度配置分组
            Map<List<String>, List<VoucherEntryDTO>> assistGroupMap = voucherEntryList.stream().collect(Collectors.groupingBy(e->e.getAssistFlags()==null?new ArrayList<>():e.getAssistFlags()));
            for (Map.Entry<List<String>, List<VoucherEntryDTO>> assistGroupEntry: assistGroupMap.entrySet()){
                Map<String, BigDecimal> fundTypeMap = new HashMap<>();
                VoucherEntryDTO voucherEntryDTO = assistGroupEntry.getValue().get(0);
                for (VoucherEntryDTO entryDTO : assistGroupEntry.getValue()) {
                    //根据科目余额方向计算发生额
                    AccountDTO accountDTO = accountService.getOneAccountByCode(entryDTO.getAccountCode());
                    BigDecimal currentAmount;
                    if (DRCREnum.DR.getCode().equals(accountDTO.getDebitCreditType())){
                        //余额方向为借方 发生额 = 借方金额 - 贷方金额
                        currentAmount = NumberUtil.sub(entryDTO.getDebitAmount(), entryDTO.getCreditAmount());
                    } else{
                        currentAmount = NumberUtil.sub(entryDTO.getCreditAmount(), entryDTO.getDebitAmount());
                    }
                    //累计金额
                    if (entryDTO.getFundType().equals("margin_transition")) {
                        continue;
                    }
                    fundTypeMap.put(entryDTO.getFundType(), NumberUtil.add(fundTypeMap.get(entryDTO.getFundType()), currentAmount));
                }
                String businessCode = voucherDTO.getBusinessCode();
                String clientCode = null;
                String contractCode = null;
                String orgId = voucherDTO.getOrgId();
                String billContractCode = voucherDTO.getBillContractCode();
                if (CollectionUtil.isNotEmpty(assistGroupEntry.getKey()) && assistGroupEntry.getKey().contains(AssistFlagEnum.CONTRACT.getCode())){
                    if (voucherDTO.getIsAutoVoucherFlag()) {
                        contractCode = voucherDTO.getContractCode();
                    } else {
                        contractCode = voucherEntryDTO.getContractCode();
                    }
                }
                if (CollectionUtil.isNotEmpty(assistGroupEntry.getKey()) && assistGroupEntry.getKey().contains(AssistFlagEnum.CLIENT.getCode())){
                    if (voucherDTO.getIsAutoVoucherFlag()) {
                        clientCode = voucherDTO.getClientCode();
                    } else {
                        clientCode = voucherEntryDTO.getClientCode();
                    }
                }
                log.info("业务编码：{}，合同号：{},客户编码：{}",businessCode,contractCode,clientCode);
                insertContractBalance(fundTypeMap, iContractBalanceLatestService.getLastBalanceMap(businessCode, clientCode, contractCode,orgId,billContractCode), voucherDTO,accountEntityList);
            }
        }
    }

    @Override
    public void saveMonualContractBalanceFromVoucher(VoucherDTO voucherDTO) {
        List<AccountEntity> accountEntityList = accountService.list(Wrappers.<AccountEntity>lambdaQuery());
        List<VoucherEntryDTO> voucherEntryList = voucherDTO.getEntryList();
        if (CollectionUtil.isNotEmpty(voucherEntryList)){
            //按照签约主体,合同，客户，借款合同编码分组
            Map<String, List<VoucherEntryDTO>> assistGroupMap = voucherEntryList.stream().collect(Collectors.groupingBy(e->e.getContractCode()+"-"+e.getClientCode()+"-"+e.getBillContractCode()));
            for (Map.Entry<String, List<VoucherEntryDTO>> entryDTO : assistGroupMap.entrySet()) {
                Map<String, BigDecimal> fundTypeMap = new HashMap<>();
                VoucherEntryDTO dto = entryDTO.getValue().get(0);
                for (VoucherEntryDTO voucherEntryDTO: entryDTO.getValue()) {
                    //根据科目余额方向计算发生额
                    AccountDTO accountDTO = accountService.getOneAccountByCode(voucherEntryDTO.getAccountCode());
                    BigDecimal currentAmount;
                    if (DRCREnum.DR.getCode().equals(accountDTO.getDebitCreditType())) {
                        //余额方向为借方 发生额 = 借方金额 - 贷方金额
                        currentAmount = NumberUtil.sub(voucherEntryDTO.getDebitAmount(), voucherEntryDTO.getCreditAmount());
                    } else {
                        currentAmount = NumberUtil.sub(voucherEntryDTO.getCreditAmount(), voucherEntryDTO.getDebitAmount());
                    }
                    //累计金额
                    if (voucherEntryDTO.getFundType().equals("margin_transition")) {
                        continue;
                    }
                    fundTypeMap.put(voucherEntryDTO.getFundType(), NumberUtil.add(fundTypeMap.get(voucherEntryDTO.getFundType()), currentAmount));
                }
                String businessCode = voucherDTO.getBusinessCode();
                String clientCode = dto.getClientCode();;
                String contractCode = dto.getContractCode();;
                String orgId = voucherDTO.getOrgId();
                String billContractCode = dto.getBillContractCode();
                log.info("业务编码：{}，合同号：{},客户编码：{}", businessCode, contractCode, clientCode);
                insertContractBalance(fundTypeMap, iContractBalanceLatestService.getLastBalanceMap(businessCode, clientCode, contractCode, orgId, billContractCode), voucherDTO, accountEntityList);
            }
        }
    }



    @Override
    public String importDataMargin(List<ImportContractBalanceExcel> list) {
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgId(), e -> e.getOrgId(), (a, b) -> b));
        List<ContractBalanceEntity> insertList = new ArrayList<>();
        DateTime dateTime = DateUtil.parseDate("2023-6-30");
        list.forEach(e -> {
            ContractBalanceEntity balanceEntity = BeanUtil.copyProperties(e, ContractBalanceEntity.class);
            balanceEntity.setClientCode("test");
            balanceEntity.setBusinessDate(dateTime.toLocalDateTime());
            balanceEntity.setVoucherDate(dateTime.toLocalDateTime());
            balanceEntity.setPeriodCode(NumberUtil.parseInt(LocalDateTimeUtil.format(balanceEntity.getVoucherDate(), "yyyyMM")));
            balanceEntity.setOrgId(companyMap.get(balanceEntity.getOrgId()));
            if ("2701.01".equals(e.getAccountCode())) {
                balanceEntity.setLesseeMarginBalance(e.getBalance());
                balanceEntity.setBusinessCode("ZLYW");
            }
            else if ("2701.02".equals(e.getAccountCode()))   {
                balanceEntity.setSupplierMarginBalance(e.getBalance());
                balanceEntity.setBusinessCode("ZLYW");
            }
            else if ("2701.09".equals(e.getAccountCode()))   {
                balanceEntity.setLesseeMarginBalance(e.getBalance());
                balanceEntity.setBusinessCode("JYZL");
            }
            insertList.add(balanceEntity);

        });
        this.saveBatch(insertList);
        return null;
    }

    @Override
    public void importData(List<ImportContractBalanceExcel> list) {
        Map<String, String> companyMap = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgName(), e -> e.getOrgId(), (a, b) -> b));
        List<ContractBalanceEntity> insertList = new ArrayList<>();
        list.forEach(e -> {
            DateTime dateTime = DateUtil.beginOfDay(e.getBusinessDate());
            ContractBalanceEntity balanceEntity = BeanUtil.copyProperties(e, ContractBalanceEntity.class);
            balanceEntity.setBusinessCode("ZLYW");
            balanceEntity.setBusinessDate(dateTime.toLocalDateTime());
            balanceEntity.setVoucherDate(dateTime.toLocalDateTime());
            balanceEntity.setPeriodCode(NumberUtil.parseInt(LocalDateTimeUtil.format(balanceEntity.getVoucherDate(), "yyyyMM")));
            balanceEntity.setOrgId(companyMap.get(balanceEntity.getOrgId()));
            insertList.add(balanceEntity);

        });
        this.saveBatch(insertList);
    }

    @Override
    public void importData1(List<ImportContractExcel1> list) {
        List<String> codeList = list.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
        DateTime busDate = DateUtil.parse("2022-12-31");
        List<ContractBalanceEntity> contractBalanceEntities = this.getBaseMapper().selectList(Wrappers.<ContractBalanceEntity>lambdaQuery()
                .in(ContractBalanceEntity::getContractCode, codeList).eq(ContractBalanceEntity::getBusinessDate, busDate));
        List<String> existCodeList = contractBalanceEntities.stream().map(e -> e.getContractCode()).collect(Collectors.toList());
        list.forEach(e -> {
            if (existCodeList.contains(e.getContractCode())) {
                UpdateWrapper<ContractBalanceEntity> updateChainWrapper = new UpdateWrapper<>();
                updateChainWrapper
                        .eq("contract_code", e.getContractCode())
                        .eq("business_date", busDate)
                        .set("receive_cost_balance", e.getReceiveCostBalance())
                        .set("equipment_depreciation_reserves_balance", e.getEquipmentDepreciationReservesBalance());
                this.update(updateChainWrapper);
            } else {
                Map<String, Object> newRowMap = new HashMap<>();
                newRowMap.put(ContractBalanceColumnsEnum.BUSINESS_DATE.getCode(), busDate);
                newRowMap.put(ContractBalanceColumnsEnum.ORG_ID.getCode(), e.getOrgId());
                newRowMap.put(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode(), e.getContractCode());
                newRowMap.put("receive_cost_balance", e.getReceiveCostBalance());
                newRowMap.put("equipment_depreciation_reserves_balance", e.getEquipmentDepreciationReservesBalance());
                insertMap(newRowMap);
            }
        });
    }

    @Override
    public ContractBalanceDTO getLastBySceneCode(String businessCode, String contractCode, String sceneCode) {
        LambdaQueryWrapper<ContractBalanceEntity> lambdaQueryWrapper = Wrappers.<ContractBalanceEntity>lambdaQuery();
        lambdaQueryWrapper.eq(ContractBalanceEntity::getBusinessCode, businessCode);
        lambdaQueryWrapper.eq(ContractBalanceEntity::getContractCode, contractCode);
        if (StrUtil.isNotBlank(sceneCode)){
            lambdaQueryWrapper.eq(ContractBalanceEntity::getSceneCode, sceneCode);
        }
        lambdaQueryWrapper.orderByDesc(ContractBalanceEntity::getCreateTime);
        lambdaQueryWrapper.last("limit 1");
        ContractBalanceEntity entity = this.getOne(lambdaQueryWrapper);
        if (entity != null) return BeanUtil.copyProperties(entity, ContractBalanceDTO.class);
        return null;
    }

    @Override
    public IPage<ContractBalanceVO> selectCheckPage(ContractBalanceCheckQueryDTO queryDTO) {
        Page page = new Page(queryDTO.getPageNum(),queryDTO.getPageSize());
        return contractBalanceMapper.selectCheckPage(page,queryDTO);
    }

    @Override
    public List<ContractBalanceVO> selectLatestBalanceByCondition(ContractBalanceQueryDTO contractBalanceQueryDTO) {
        return contractBalanceMapper.selectLatestBalanceByCondition(contractBalanceQueryDTO);
    }

    @Override
    public IPage<ContractAccountBalanceVO> sumAccountBalancePage(ContractBalanceQueryDTO params) {
        Page page = new Page(params.getPageNum(),params.getPageSize());
        return contractBalanceMapper.sumAccountBalancePage(page, params);
    }

    @Override
    public List<ContractBalanceEntity> getLastContract(ContractBalanceLastQueryDTO param) {
        return contractBalanceMapper.getLastContract(param);
    }

    @Override
    public List<ContractBalanceVO> selectLatestBalanceByOrgIdContractCodeList(TransferContractBalanceQueryDTO dto){
        String orgId = dto.getOrgId();
        if (StringUtils.isEmpty(orgId)){
            throw new ServiceException("orgId is null");
        }
        List<String> contractCodeList = dto.getContractCodeList();
        if (CollectionUtil.isEmpty(contractCodeList)){
            throw new ServiceException("contractCodeList is null");
        }
        LocalDate voucherDate = dto.getVoucherDateEnd();
        LocalDate voucherDateStart = dto.getVoucherDateStart();
        if (Objects.isNull(voucherDateStart) && Objects.isNull(voucherDate)){
            throw new ServiceException("voucherDate, voucherDateStart both null");
        }

        return contractBalanceMapper.selectLatestBalanceByOrgIdContractCodeList(dto);
    }

    @Override
    public List<ContractBalanceEntity> selectContractBalanceAfterJZRGroupByOrgIdContractScene(
            Map<String, List<String>> orgContractMap,
            List<String> sceneCode,
            LocalDate localDate,
            int periodCode
    ) {
        return contractBalanceMapper.selectContractBalanceAfterJZRGroupByOrgIdContractScene(orgContractMap, sceneCode, localDate, periodCode);
    }
}

