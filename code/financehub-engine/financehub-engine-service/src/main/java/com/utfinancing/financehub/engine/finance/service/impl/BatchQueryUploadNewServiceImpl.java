package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.BatchQueryUploadRecordEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.utfinancing.financehub.engine.finance.mapper.BatchQueryUploadRecordMapper;
import com.utfinancing.financehub.engine.finance.model.dto.BatchQueryUploadExcelDTO;
import com.utfinancing.financehub.engine.finance.model.dto.BatchQueryUploadQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.IBatchQueryUploadNewService;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.finance.service.IFileRecordService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.hthx.base.MyBaseServiceImpl;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.hthx.page.PageResult;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.mapper.AccountMapper;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/1/20 15:50
 */

@Slf4j
@Service
public class BatchQueryUploadNewServiceImpl extends MyBaseServiceImpl<BatchQueryUploadRecordMapper, BatchQueryUploadRecordEntity> implements IBatchQueryUploadNewService {

    @Autowired
    private  BatchQueryUploadRecordMapper batchQueryUploadRecordMapper;

    @Resource
    private IAccountService accountService;

    @Resource
    private IOrgCompanyService orgCompanyService;

    @Resource
    private IContractService contractService;

    @Value("${service.parth:null}")
    private String servicePath;

    @Value("${file.storage.basicpath.windows:null}")
    private String basicPathWindows;

    @Value("${file.storage.basicpath.linux:null}")
    private String basicPathLinux;

    @Resource
    private IFileRecordService fileRecordService;

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;


    /**
     * @description: 批量查询功能列表-支持分页查询
     * @author: zhangli.chen
     **/
    @Override
    public PageResult selectPage(BatchQueryUploadQueryDTO queryParams) throws Exception {
        PageResult batchQueryUploadPageResult = new PageResult();
        // 设置默认查询参数
        setDefaultParam(queryParams);
        // 查询出所有数据
        List<Map<String, String>> batchQueryUploadList = batchQueryUploadRecordMapper.selectBatchQueryUploadList(queryParams);
        // 按查询条件过滤并分页
        batchQueryUploadPageResult = filterAndPage(batchQueryUploadList,queryParams);
        List<Map<String, String>> filterResultList = batchQueryUploadPageResult.getRecords();
        // 设置组织机构名称
        if(CollectionUtil.isNotEmpty(filterResultList)){
            // 查询主体信息
            Map<String,String> orgMap = getOrgRelation();
            filterResultList.stream().forEach(i->{
                // 设置主体名称
                if(ObjectUtil.isNotEmpty(i.get(FinanceEngineEnum.KeyValueTextMapping.ORG_ID.getKey())) && orgMap.containsKey(i.get(FinanceEngineEnum.KeyValueTextMapping.ORG_ID.getKey()))){
                    i.put(FinanceEngineEnum.KeyValueTextMapping.ORG_ID.getValue(), orgMap.get(i.get(FinanceEngineEnum.KeyValueTextMapping.ORG_ID.getKey())));
                }
            });
            batchQueryUploadPageResult.setRecords(filterResultList);
        }
        return batchQueryUploadPageResult;
    }

    /**
     * @description: 按查询条件过滤并分页
     * @author: zhangli.chen
     **/
    private  PageResult<List<Map<String, String>>> filterAndPage(List<Map<String, String>> toFilterList,BatchQueryUploadQueryDTO queryParams){
        PageResult<List<Map<String, String>>> filterPageResult = new PageResult<>();
        if(CollectionUtil.isNotEmpty(toFilterList)){
            // 筛选条件01-排除范围 1-当前期间无发生不展示2-余额为零且当前期间无发生不显示3-余额为零不显示4-本年无发生额不显示5-余额为零且本年无发生不显示
            if(CollectionUtil.isNotEmpty(queryParams.getExceptList())){
                toFilterList = exclusionScopeFilter(toFilterList,queryParams.getExceptList());
            }
            // 筛选条件02-处理状态
            if(CollectionUtil.isNotEmpty(queryParams.getProcessStatusList())){
                toFilterList = processStatusFilter(toFilterList,queryParams.getProcessStatusList());
            }
            // 计算总记录数
            int total = toFilterList.size();
            if(total>0){
                // 计算分页的起始索引和结束索引
                int start = (queryParams.getPageNum() - 1) * queryParams.getPageSize();
                int end = Math.min(start + queryParams.getPageSize(), toFilterList.size());
                int totalPage = (total % queryParams.getPageSize() != 0 ? total / queryParams.getPageSize() + 1 : total/queryParams.getPageSize());
                // 提取当前页的数据
                List<Map<String, String>> pageData = toFilterList.subList(start, end);
                filterPageResult = new PageResult(total,queryParams.getPageNum(),queryParams.getPageSize(),totalPage, pageData);
            }
        }else{
            filterPageResult = new PageResult(FinanceEngineEnum.Numbers.ZERO.getKey(),queryParams.getPageNum(),queryParams.getPageSize(),FinanceEngineEnum.Numbers.ZERO.getKey());
        }
        return filterPageResult;
    }


    /**
     * @description: 设置默认参数
     * @author: zhangli.chen
     **/
    private BatchQueryUploadRecordEntity setDefaultParam(BatchQueryUploadQueryDTO queryDTO){
        boolean fullFlag = false;
        if(queryDTO!=null && StringUtils.isEmpty(queryDTO.getQueryDate())){
            throw new ServiceException("查询日期不能为空!");
        }
        LambdaQueryWrapper<BatchQueryUploadRecordEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchQueryUploadRecordEntity::getUploadBy,SecurityUtils.getUserId().toString());
        queryWrapper.orderByDesc(BatchQueryUploadRecordEntity::getUploadTime);
        //log.info("====>>BatchQueryUploadNewServiceImpl.setDefaultParam==>>00==>>UserId:{}",SecurityUtils.getUserId().toString());
        List<BatchQueryUploadRecordEntity> uploadHistory =  batchQueryUploadRecordMapper.selectList(queryWrapper);
        if(CollectionUtil.isEmpty(uploadHistory)){
            throw new ServiceException("未找到需要查询的合同信息!");
        }
        BatchQueryUploadRecordEntity lastRecord = uploadHistory.get(0);
        String contractCodes = lastRecord.getContractCodeList();
        List<String> contractCodeList = Arrays.asList(contractCodes.split(","));
        List<String> accountCodeList = new ArrayList<>();
        if(!StringUtils.isEmpty(lastRecord.getAccountCodeList())){
            String accountCodes = lastRecord.getAccountCodeList();
            accountCodeList = Arrays.asList(accountCodes.split(","));
            queryDTO.setAccountCodeList(accountCodeList);
        }
        log.info("====>>BatchQueryUploadNewServiceImpl.setDefaultParam==>>01==>>lastRecord:{},queryDTO:{}",lastRecord,queryDTO);
        queryDTO.setContractCodeList(contractCodeList);
        // 设置期间
        Integer periodCode = Integer.valueOf(queryDTO.getQueryDate().replaceAll("-","").substring(0,6));
        queryDTO.setPeriodCode(periodCode);
        log.info("====>>BatchQueryUploadNewServiceImpl.setDefaultParam==>>02==>>CollectionUtil.isEmpty(accountCodeList):{},accountCodeList:{}",CollectionUtil.isEmpty(accountCodeList),accountCodeList);
        // 科目编码-科目名称-金额类型 获取科目列表--未上传科目支持查询所有科目
        LambdaQueryWrapper<AccountEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(CollectionUtil.isEmpty(accountCodeList)){
            fullFlag = true;
            lambdaQueryWrapper.select(AccountEntity::getAccountCode, AccountEntity::getAccountName, AccountEntity::getFundType)
                    .eq(AccountEntity::getDelFlag, YesOrNoEnum.NO.getCode());
            log.info("====>>BatchQueryUploadNewServiceImpl.setDefaultParam==>>03==>>");
        }else{
            lambdaQueryWrapper.select(AccountEntity::getAccountCode, AccountEntity::getAccountName, AccountEntity::getFundType)
                    .in(AccountEntity::getAccountCode, accountCodeList);
            log.info("====>>BatchQueryUploadNewServiceImpl.setDefaultParam==>>04==>>");
        }
        List<AccountEntity> accountEntityList = accountService.list(lambdaQueryWrapper)
                .stream()
                .distinct()
                .collect(Collectors.toList());
        log.info("====>>BatchQueryUploadNewServiceImpl.setDefaultParam==>>05==>>fullFlag:{},accountEntityList:{}",fullFlag,accountEntityList);
        if(CollectionUtil.isNotEmpty(accountEntityList)){
            // 设置科目查询信息
            if(fullFlag){
                List<String> uniqueAccountCodes = accountEntityList.stream()
                        .map(AccountEntity::getAccountCode)
                        .distinct()
                        .collect(Collectors.toList());
                queryDTO.setAccountCodeList(uniqueAccountCodes);
                // 使用StringBuilder手动拼接字符串
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < uniqueAccountCodes.size(); i++) {
                    sb.append(uniqueAccountCodes.get(i));
                    if (i < uniqueAccountCodes.size() - 1) {
                        sb.append(",");
                    }
                }
                lastRecord.setAccountCodeList(sb.toString());
                log.info("====>>BatchQueryUploadNewServiceImpl.setDefaultParam==>>06==>>queryDTO:{},lastRecord:{}",queryDTO,lastRecord);
            }
            // 1532.01特殊处理
            List<String> fundTypeList = accountEntityList.stream().filter(i->StringUtils.isNotEmpty(i.getFundType())&&!StringUtils.equals(i.getAccountCode(), "1532.01")).map(i->i.getFundType()+"_balance").distinct().collect(Collectors.toList());
            queryDTO.setFundTypeList(fundTypeList);
        }
        log.info("====>>BatchQueryUploadNewServiceImpl.setDefaultParam==>>100==>>queryDTO:{}",queryDTO);
        return lastRecord;
    }

    /**
     * @description: 获取组织关系
     * @author: zhangli.chen
     **/
    private Map<String,String> getOrgRelation(){
        Map<String,String> orgNameAndIdMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  orgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgNameAndIdMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId,OrgCompanyVO::getOrgName, (k1, k2)->k2));
        }
        return orgNameAndIdMap;
    }

    /**
     * @param file
     * @description:文件上传导入
     */
    @Override
    public Boolean importTemplate(MultipartFile file) {
        try {
            ExcelUtil<BatchQueryUploadExcelDTO> util = new ExcelUtil<BatchQueryUploadExcelDTO>(BatchQueryUploadExcelDTO.class);
            List<BatchQueryUploadExcelDTO> excelList = util.importExcel(file.getInputStream());
            List<String> contractCodeList = Lists.newArrayList();
            List<String> accountCodeList = Lists.newArrayList();
            //校验文件
            checkImportData(excelList, contractCodeList, accountCodeList);
            //记录数据
            BatchQueryUploadRecordEntity record = new BatchQueryUploadRecordEntity();
            record.setUploadBy(SecurityUtils.getUserId().toString());
            record.setUploadTime(LocalDateTime.now());
            record.setAccountCodeList(StringUtils.join(accountCodeList, ","));
            record.setContractCodeList(StringUtils.join(contractCodeList, ","));
            this.save(record);
        } catch (Exception exception) {
            throw new ServiceException("导入批量查询模板数据失败，失败原因："+exception.getMessage());
        }
        return Boolean.TRUE;
    }

    /**
     * @description: 校验上传导入的数据-支持科目编号未填入也可进行文件导入
     * @author: zhangli.chen
     **/
    private void checkImportData(List<BatchQueryUploadExcelDTO> excelList, List<String> contractCodeList, List<String> accountCodeList) {
        if (CollectionUtils.isEmpty(excelList)) {
            throw new ServiceException("没有数据需要上传");
        }
        contractCodeList.addAll(excelList.stream().map(BatchQueryUploadExcelDTO::getContractCode).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList()));
        if(CollectionUtil.isEmpty(contractCodeList)){
            throw new ServiceException("合同编号不能为空");
        }
        List<ContractEntity> contractEntityList = contractService.list(new LambdaQueryWrapper<ContractEntity>().in(ContractEntity::getContractCode, contractCodeList));
        if(CollectionUtil.isEmpty(contractEntityList)){
            throw new ServiceException("导入文件中合同编号全部在系统中未找到！");
        }
        for(String contractCode : contractCodeList){
            Optional<ContractEntity> op = contractEntityList.stream().filter(i->StringUtils.equals(i.getContractCode(), contractCode)).findAny();
            if(!op.isPresent()){
                throw new ServiceException("该合同编号在系统中未找到：{"+contractCode+"},烦请确认！");
            }
        }
        accountCodeList.addAll(excelList.stream().map(BatchQueryUploadExcelDTO::getAccountCode).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList()));
        if(CollectionUtil.isNotEmpty(accountCodeList)){
            List<AccountEntity> accountEntityList = accountService.list(new LambdaQueryWrapper<AccountEntity>().in(AccountEntity::getAccountCode, accountCodeList));
            if(CollectionUtil.isEmpty(accountEntityList)){
                throw new ServiceException("导入文件中科目编号全部在系统中未找到！");
            }
            for(String accountCode : accountCodeList){
                Optional<AccountEntity> op = accountEntityList.stream().filter(i->StringUtils.equals(i.getAccountCode(), accountCode)).findAny();
                if(!op.isPresent()){
                    throw new ServiceException("该科目编号在系统中未找到：{"+accountCode+"},烦请确认！");
                }
            }
        }
    }

    /**
     * @description:批量查询表单头
     **/
    @Override
    public List<Map<String, String>> selectHeaderList() {
        List<Map<String, String>> resList = Lists.newArrayList();
        // 1-固定行
        Map<String, String> map1 = Maps.newHashMap();
        map1.put("label", "合同编号");
        map1.put("prop", "contract_code");
        resList.add(map1);
        Map<String, String> map2 = Maps.newHashMap();
        map2.put("label", "客户名称");
        map2.put("prop", "client_name");
        resList.add(map2);
        Map<String, String> map3 = Maps.newHashMap();
        map3.put("label", "签约主体");
        map3.put("prop", "org_name");
        resList.add(map3);
        Map<String, String> map4 = Maps.newHashMap();
        map4.put("label", "税率");
        map4.put("prop", "tax_rate");
        resList.add(map4);
        Map<String, String> map5 = Maps.newHashMap();
        map5.put("label", "财务合同状态");
        map5.put("prop", "financial_contract_status");
        resList.add(map5);
        Map<String, String> map6 = Maps.newHashMap();
        map6.put("label", "合同状态");
        map6.put("prop", "contract_status");
        resList.add(map6);
        Map<String, String> map7 = Maps.newHashMap();
        map7.put("label", "TA余额");
        map7.put("prop", "ta_amount");
        resList.add(map7);
        Map<String, String> map8 = Maps.newHashMap();
        map8.put("label", "已收款未开票利息");
        map8.put("prop", "non_invoiced_profit");
        resList.add(map8);
        Map<String, String> map9 = Maps.newHashMap();
        map9.put("label", "系统未收本金");
        map9.put("prop", "non_received_principal");
        resList.add(map9);
        Map<String, String> map10 = Maps.newHashMap();
        map10.put("label", "系统未收利息");
        map10.put("prop", "non_received_profit");
        resList.add(map10);
        // 2-动态行
        List<BatchQueryUploadRecordEntity> records = this.list(new LambdaQueryWrapper<BatchQueryUploadRecordEntity>()
                .eq(BatchQueryUploadRecordEntity::getUploadBy, SecurityUtils.getUserId().toString()).orderByDesc(BatchQueryUploadRecordEntity::getUploadTime));
        log.info("====>>BatchQueryUploadNewServiceImpl.selectHeaderList==>>00==>>SecurityUtils.getUserId().toString():{},records:{}",SecurityUtils.getUserId().toString(),records);
        if(CollectionUtil.isEmpty(records)){
            return resList;
        }
        BatchQueryUploadRecordEntity lastRecord = records.get(0);
        String accountCodes = lastRecord.getAccountCodeList();
        log.info("====>>BatchQueryUploadNewServiceImpl.selectHeaderList==>>01==>>lastRecord:{},accountCodes:{}",lastRecord,accountCodes);
        // 科目编码-科目名称-金额类型 获取科目列表--未上传科目支持查询所有科目
        LambdaQueryWrapper<AccountEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        log.info("====>>BatchQueryUploadNewServiceImpl.selectHeaderList==>>02==>>accountCodes:{},StringUtils.isEmpty(accountCodes):{}",accountCodes,StringUtils.isEmpty(accountCodes));
        if(StringUtils.isEmpty(accountCodes)){
            log.info("====>>BatchQueryUploadNewServiceImpl.selectHeaderList==>>03==>>");
            lambdaQueryWrapper.select(AccountEntity::getAccountCode, AccountEntity::getAccountName, AccountEntity::getFundType)
                    .eq(AccountEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        }else{
            List<String> accountCodeList = Arrays.asList(accountCodes.split(","));
            log.info("====>>BatchQueryUploadNewServiceImpl.selectHeaderList==>>04==>>accountCodeList:{}",accountCodeList);
            lambdaQueryWrapper.select(AccountEntity::getAccountCode, AccountEntity::getAccountName, AccountEntity::getFundType)
                    .in(AccountEntity::getAccountCode, accountCodeList);
        }
        List<AccountEntity> accountEntityList = accountService.list(lambdaQueryWrapper)
                .stream()
                .distinct()
                .collect(Collectors.toList());
        log.info("====>>BatchQueryUploadNewServiceImpl.selectHeaderList==>>05==>>accountEntityList:{}",accountEntityList);
        if(CollectionUtil.isNotEmpty(accountEntityList)){
            for(AccountEntity account : accountEntityList){
                //1532.01特殊处理
                if(StringUtils.equals(account.getAccountCode(), "1532.01")){
                    Map<String, String> tmpMap1 = Maps.newHashMap();
                    tmpMap1.put("label", "未实现收益（不含咨询服务费分摊）");
                    tmpMap1.put("prop", "unrealized_revenue_balance_no_service_fee");
                    resList.add(tmpMap1);
                    Map<String, String> tmpMap2 = Maps.newHashMap();
                    tmpMap2.put("label", "未实现收益（咨询服务费分摊）");
                    tmpMap2.put("prop", "unrealized_revenue_balance_service_fee");
                    resList.add(tmpMap2);
                }else{
                    Map<String, String> map = Maps.newHashMap();
                    map.put("label", account.getAccountName());
                    map.put("prop", account.getFundType()+"_balance");
                    resList.add(map);
                }
            }
        }
        log.info("====>>BatchQueryUploadNewServiceImpl.selectHeaderList==>>100==>>resList.size():{}",resList.size());
        return resList;
    }

    /**
     * @description:下载原表+详情
     **/
    @Override
    public Map<String, String> generateBatchQueryReportExcel(BatchQueryUploadQueryDTO queryDTO) {
        queryDTO.setQueryType("export");
        BatchQueryUploadRecordEntity lastRecord = setDefaultParam(queryDTO);
        String fileName = "批量查询生成文件_"+queryDTO.getExportType()+"_" + LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss")+".xlsx";
        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(getFilePath()+fileName);
        FileRecordEntity record = new FileRecordEntity();
        record.setModuleName(ModuleEnum.QUERY_MODULE.getCode());
        record.setBusinessScene(BusinessSceneEnum.BATCH_QUERY.getCode());
        record.setFileLocation(getFilePath()+fileName);
        record.setFileName(fileName);
        record.setExecuteStatus(CheckExecuteStatusEnum.INPROGRESS.getCode());
        record.setFileUploadBy(String.valueOf(SecurityUtils.getUserId()));
        fileRecordService.save(record);
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            // 异步执行的任务
            queryAndWriteBatchQueryData(queryDTO, fileName, writer, lastRecord);
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

    private String getFilePath(){
        String osName = System.getProperties().getProperty("os.name");
        if(osName.toLowerCase().contains("windows")){
            return basicPathWindows+"batchQuery"+ File.separator;
        }else if(osName.toLowerCase().contains("linux") || osName.toLowerCase().contains("unix")){
            return basicPathLinux+"batchQuery"+File.separator;
        }
        return StringUtils.EMPTY;
    }

    /**
     * @description:查询功能-批量查询-下载-下载原表+下载详情
     * @author: zhangli.chen
     **/
    private void queryAndWriteBatchQueryData(BatchQueryUploadQueryDTO queryDTO,
                                             String fileName, ExcelWriter writer,
                                             BatchQueryUploadRecordEntity lastRecord) {
        // 关键：强制导出所有别名列
        writer.setOnlyAlias(true);
        writer.addHeaderAlias("contract_code", "合同编号");
        writer.addHeaderAlias("client_name", "客户名称");
        writer.addHeaderAlias("org_name", "签约主体");
        writer.addHeaderAlias("tax_rate", "税率");
        writer.addHeaderAlias("financial_contract_status", "财务合同状态");
        writer.addHeaderAlias("contract_status", "合同状态");
        writer.addHeaderAlias("ta_amount", "TA余额");
        writer.addHeaderAlias("non_invoiced_profit", "已收款未开票利息");
        writer.addHeaderAlias("non_received_principal", "系统未收本金");
        writer.addHeaderAlias("non_received_profit", "系统未收利息");
        String accountCodes = lastRecord.getAccountCodeList();
        List<String> accountCodeList = Arrays.asList(accountCodes.split(","));
        //List<AccountEntity> accountEntityList = accountService.list(new LambdaQueryWrapper<AccountEntity>().in(AccountEntity::getAccountCode, accountCodeList));
        // 查询客户信息-去除重复
        LambdaQueryWrapper<AccountEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AccountEntity::getAccountCode, AccountEntity::getAccountName
                , AccountEntity::getFundType,AccountEntity::getDebitCreditType)
                .eq(AccountEntity::getDelFlag, YesOrNoEnum.NO.getCode())
                .in(AccountEntity::getAccountCode, accountCodeList);
        List<AccountEntity> accountEntityList = accountService.list(lambdaQueryWrapper).stream().distinct().collect(Collectors.toList());
        //List<Map<String, String>> contractBalanceInfoList = batchQueryUploadRecordMapper.selectBatchQueryContractBalanceInfo(queryDTO);
        List<Map<String, String>> contractBalanceInfoList  =  batchQueryUploadRecordMapper.selectBatchQueryUploadList(queryDTO);
        // excel中写入的每行数据
        List<Map<String, String>> tmpList = Lists.newArrayList();
        List<Map<String, String>> filteredList  = exclusionScopeFilter(contractBalanceInfoList,null);
        // 下载原表
        if(StringUtils.equals("common", queryDTO.getExportType())){
            if(CollectionUtil.isNotEmpty(filteredList)){
                Map<String,String> orgMap = getOrgRelation();
                for(Map<String, String> map : filteredList){
                    if(ObjectUtil.isNotEmpty(map.get("org_id"))&&orgMap.containsKey(map.get("org_id"))){
                        map.put("org_name", orgMap.get(map.get("org_id")));
                    }
                    //Map<String, String> copiedMap = Maps.newHashMap(map);
                    Map<String, String> copiedMap = new LinkedHashMap(map);
                    copiedMap.remove("org_id");
                    copiedMap.remove("client_code");
                    if(!accountCodeList.contains("1532.01")){
                        copiedMap.remove("unrealized_revenue_balance_no_service_fee");
                        copiedMap.remove("unrealized_revenue_balance_service_fee");
                    }
                    tmpList.add(copiedMap);
                }
            }
            for(AccountEntity account : accountEntityList){
                //1532.01特殊处理
                if(StringUtils.equals(account.getAccountCode(), "1532.01")){
                    writer.addHeaderAlias("unrealized_revenue_balance_no_service_fee", "未实现收益（不含咨询服务费分摊）");
                    writer.addHeaderAlias("unrealized_revenue_balance_service_fee", "未实现收益（咨询服务费分摊）");
                }else{
                    writer.addHeaderAlias(account.getFundType()+"_balance", account.getAccountName());
                }
            }
        }else{
            // 下载详情
            // 将余额表中每个Map元素的"org_id"和"contract_code"字段值的拼接字符串作为健进行分组，而值是对应行的个各字段的Map元素，若健重复则只取第一个
            Map<String, Map<String, String>> contractGroupedMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(filteredList)){
                contractGroupedMap = filteredList.stream().collect(Collectors.toMap(m->m.get("org_id")+"&"+m.get("contract_code"), Function.identity(), ((key1 , key2) -> key1)));
            }
            // 查询科目辅助帐余额表-根据签约主体+合同编号+科目分组汇总-分别最终每个科目的-本期借方发生额-本期贷方发生额-期末借方余额-期末贷方余额
            List<Map<String, String>> assistBalanceInfoList = batchQueryUploadRecordMapper.selectBatchQueryAssistBalanceInfo(queryDTO);
            // 将科目辅助帐余额表-签约主体+合同编号分组，其结果是签约主体+合同编号作为key来分组，将多行数据放入到list中，而每个list的元素为map，其map的key为每一个的字段名，value为字段值
            Map<String, List<Map<String, String>>> assistGroupedMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(assistBalanceInfoList)){
                assistGroupedMap = assistBalanceInfoList.stream().collect(Collectors.groupingBy(m->m.get("org_id")+"&"+m.get("contract_code")));
            }
            // 获取签约主体的公司信息
             Map<String,String> orgMap = getOrgRelation();
            // 按科目余额表的签约主体和合同编号分组-进行循环，key为签约主体&合同编号
            for(Map.Entry<String, Map<String, String>> entry: contractGroupedMap.entrySet()){
                // key为签约主体&合同编号
                String key = entry.getKey();
                // value值为每一行的各个字段名作key，值作为value的map
                Map<String, String> map = entry.getValue();
                // 科目余额表也按签约主体和合同编号作为key，而value值为多行的包含科目的列表数据
                List<Map<String, String>> assistMaps = assistGroupedMap.get(key);
                // 循环待查询的科目列表-主要是为了每个科目添加4个本期+期末信息
                for(AccountEntity account : accountEntityList){
                    // 如果本账期查询出来的科目余额表为空
                    if(CollectionUtil.isEmpty(assistMaps)){
                        //1532.01特殊处理
                        if(StringUtils.equals(account.getAccountCode(), "1532.01")){
                            map.put("unrealized_revenue_no_service_fee_month_debit_amount", "0");
                            map.put("unrealized_revenue_no_service_fee_month_credit_amount", "0");
                            // 借方
                            if(DRCREnum.DR.getCode().equals(account.getDebitCreditType())){
                                map.put("unrealized_revenue_no_service_fee_month_end_debit_balance", "0");
                            }
                            // 贷方
                            if(DRCREnum.CR.getCode().equals(account.getDebitCreditType())){
                                map.put("unrealized_revenue_no_service_fee_month_end_credit_balance", "0");
                            }
                            map.put("unrealized_revenue_service_fee_month_debit_amount", "0");
                            map.put("unrealized_revenue_service_fee_month_credit_amount", "0");
                            // 借方
                            if(DRCREnum.DR.getCode().equals(account.getDebitCreditType())){
                                map.put("unrealized_revenue_service_fee_month_end_debit_balance", "0");
                            }
                            // 贷方
                            if(DRCREnum.CR.getCode().equals(account.getDebitCreditType())){
                                map.put("unrealized_revenue_service_fee_month_end_credit_balance", "0");
                            }
                        }else{
                            // 每个科目加上4个本期+期末信息
                            // 本期借方发生额
                            map.put(account.getFundType()+"_month_debit_amount", "0");
                            // 本期贷方发生额
                            map.put(account.getFundType()+"_month_credit_amount", "0");
                            // 借方
                            if(DRCREnum.DR.getCode().equals(account.getDebitCreditType())){
                                // 期末借方余额
                                map.put(account.getFundType()+"_month_end_debit_balance", "0");
                            }
                            // 贷方
                            if(DRCREnum.CR.getCode().equals(account.getDebitCreditType())){
                                // 期末贷方余额
                                map.put(account.getFundType()+"_month_end_credit_balance", "0");
                            }
                        }
                    }else {
                        // 从科目辅助帐余额表中，筛选出来对应科目的4个本期+期末信息
                        Optional<Map<String, String>> op = assistMaps.stream().filter(i->StringUtils.equals(i.get("account_code"), account.getAccountCode())).findFirst();
                        if(op.isPresent()){
                            Map<String, String> tmp = op.get();
                            //1532.01特殊处理
                            if(StringUtils.equals(tmp.get("account_code"), "1532.01")){
                                map.put("unrealized_revenue_no_service_fee_month_debit_amount", tmp.get("month_debit_amount"));
                                map.put("unrealized_revenue_no_service_fee_month_credit_amount", tmp.get("month_credit_amount"));
                                // 借方
                                if(DRCREnum.DR.getCode().equals(account.getDebitCreditType())){
                                    map.put("unrealized_revenue_no_service_fee_month_end_debit_balance", tmp.get("month_end_debit_balance"));
                                }
                                // 贷方
                                if(DRCREnum.CR.getCode().equals(account.getDebitCreditType())){
                                    map.put("unrealized_revenue_no_service_fee_month_end_credit_balance", tmp.get("month_end_credit_balance"));
                                }
                                map.put("unrealized_revenue_service_fee_month_debit_amount", tmp.get("month_debit_amount"));
                                map.put("unrealized_revenue_service_fee_month_credit_amount", tmp.get("month_credit_amount"));
                                // 借方
                                if(DRCREnum.DR.getCode().equals(account.getDebitCreditType())){
                                    map.put("unrealized_revenue_service_fee_month_end_debit_balance", tmp.get("month_end_debit_balance"));
                                }
                                // 贷方
                                if(DRCREnum.CR.getCode().equals(account.getDebitCreditType())){
                                    map.put("unrealized_revenue_service_fee_month_end_credit_balance", tmp.get("month_end_credit_balance"));
                                }
                            }else{
                                // 本期借方发生额
                                map.put(account.getFundType()+"_month_debit_amount", tmp.get("month_debit_amount"));
                                // 本期贷方发生额
                                map.put(account.getFundType()+"_month_credit_amount", tmp.get("month_credit_amount"));
                                // 借方
                                if(DRCREnum.DR.getCode().equals(account.getDebitCreditType())){
                                    // 期末借方余额
                                    map.put(account.getFundType()+"_month_end_debit_balance", tmp.get("month_end_debit_balance"));
                                }
                                // 贷方
                                if(DRCREnum.CR.getCode().equals(account.getDebitCreditType())){
                                    // 期末贷方余额
                                    map.put(account.getFundType()+"_month_end_credit_balance", tmp.get("month_end_credit_balance"));
                                }
                            }
                        }else{
                            // 每个科目加上4个本期+期末信息
                            // 本期借方发生额
                            map.put(account.getFundType()+"_month_debit_amount", "0");
                            // 本期贷方发生额
                            map.put(account.getFundType()+"_month_credit_amount", "0");
                            // 借方
                            if(DRCREnum.DR.getCode().equals(account.getDebitCreditType())){
                                // 期末借方余额
                                map.put(account.getFundType()+"_month_end_debit_balance", "0");
                            }
                            // 贷方
                            if(DRCREnum.CR.getCode().equals(account.getDebitCreditType())){
                                // 期末贷方余额
                                map.put(account.getFundType()+"_month_end_credit_balance", "0");
                            }
                        }
                    }
                }
                // 以上的作用就是将从余额表查询出来的数据，再为每个科目添加上4个本期+期末信息
                if(ObjectUtil.isNotEmpty(map.get("org_id"))&&orgMap.containsKey(map.get("org_id"))){
                    // 设置主体名称
                    map.put("org_name", orgMap.get(map.get("org_id")));
                }
                //Map<String, String> copiedMap = Maps.newHashMap(map);
                // 使用LinkedHashMap 则可保证顺序
                Map<String, String> copiedMap = new LinkedHashMap(map);
                copiedMap.remove("org_id");
                copiedMap.remove("client_code");
                //1532.01特殊处理 1532.01 未实现融资租赁收益
                if(!accountCodeList.contains("1532.01")){
                    copiedMap.remove("unrealized_revenue_balance_no_service_fee");
                    copiedMap.remove("unrealized_revenue_balance_service_fee");
                }
                tmpList.add(copiedMap);
            }
            // 循环每个科目，动态添加表头
            for(AccountEntity account : accountEntityList){
                //1532.01特殊处理
                if(StringUtils.equals(account.getAccountCode(), "1532.01")){
                    writer.addHeaderAlias("unrealized_revenue_balance_no_service_fee", "未实现收益（不含咨询服务费分摊）");
                    writer.addHeaderAlias("unrealized_revenue_no_service_fee_month_debit_amount", "未实现收益（不含咨询服务费分摊）_当月借方发生额");
                    writer.addHeaderAlias("unrealized_revenue_no_service_fee_month_credit_amount", "未实现收益（不含咨询服务费分摊）_当月贷方发生额");
                    // 借方
                    if(DRCREnum.DR.getCode().equals(account.getDebitCreditType())){
                        writer.addHeaderAlias("unrealized_revenue_no_service_fee_month_end_debit_balance", "未实现收益（不含咨询服务费分摊）_月末借方余额");
                    }
                    // 贷方
                    if(DRCREnum.CR.getCode().equals(account.getDebitCreditType())){
                        writer.addHeaderAlias("unrealized_revenue_no_service_fee_month_end_credit_balance", "未实现收益（不含咨询服务费分摊）_月末贷方余额");
                    }
                    writer.addHeaderAlias("unrealized_revenue_balance_service_fee", "未实现收益（咨询服务费分摊）");
                    writer.addHeaderAlias("unrealized_revenue_service_fee_month_debit_amount", "未实现收益（咨询服务费分摊）_当月借方发生额");
                    writer.addHeaderAlias("unrealized_revenue_service_fee_month_credit_amount", "未实现收益（咨询服务费分摊）_当月贷方发生额");
                    // 借方
                    if(DRCREnum.DR.getCode().equals(account.getDebitCreditType())){
                        writer.addHeaderAlias("unrealized_revenue_service_fee_month_end_debit_balance", "未实现收益（咨询服务费分摊）_月末借方余额");
                    }
                    // 贷方
                    if(DRCREnum.CR.getCode().equals(account.getDebitCreditType())){
                        writer.addHeaderAlias("unrealized_revenue_service_fee_month_end_credit_balance", "未实现收益（咨询服务费分摊）_月末贷方余额");
                    }
                }else{
                    writer.addHeaderAlias(account.getFundType()+"_balance", account.getAccountName());
                    // 当月借方发生额
                    writer.addHeaderAlias(account.getFundType()+"_month_debit_amount", account.getAccountName()+"_当月借方发生额");
                    // 当月贷方发生额
                    writer.addHeaderAlias(account.getFundType()+"_month_credit_amount", account.getAccountName()+"_当月贷方发生额");
                    // 借方
                    if(DRCREnum.DR.getCode().equals(account.getDebitCreditType())){
                        // 期末借方余额
                        writer.addHeaderAlias(account.getFundType()+"_month_end_debit_balance", account.getAccountName()+"_月末借方余额");
                    }
                    // 贷方
                    if(DRCREnum.CR.getCode().equals(account.getDebitCreditType())){
                        // 期末贷方余额
                        writer.addHeaderAlias(account.getFundType()+"_month_end_credit_balance", account.getAccountName()+"_月末贷方余额");
                    }
                  }
            }
        }
        writer.autoSizeColumnAll();
        // 强制刷新标题头
        writer.reset();
        // 排序逻辑
        if(CollectionUtils.isNotEmpty(tmpList)){
            tmpList.sort(Comparator.comparing(map -> map.get("contract_code")));
        }
        writer.write(tmpList, true);
        writer.close();
    }

    /**
     * @description:排除范围过滤
     **/
    private List<Map<String, String>> exclusionScopeFilter(List<Map<String, String>> batchQueryUploadList,List<String> exclusionList){
        // 过滤掉符合条件的Map
        if(CollectionUtil.isNotEmpty(batchQueryUploadList)){
//            List<Map<String, String>> filteredList = batchQueryUploadList.stream()
//                    .filter(map -> {
//                        // 获取所有包含"_balance"的键
//                        List<String> balanceKeys = map.keySet().stream()
//                                .filter(key -> key.contains("_balance"))
//                                .collect(Collectors.toList());
//                        // 如果没有包含"_balance"的键，直接保留该Map
//                        if (balanceKeys.isEmpty()) {
//                            return true;
//                        }
//                        // 检查这些键的值是否都为空或为"0"
//                        boolean allEmptyOrZero = balanceKeys.stream()
//                                .allMatch(key -> {
//                                    Object value = map.get(key);
//                                    // 检查值是否为空、空字符串或BigDecimal为0
//                                    if (value == null) return true;
//                                    if (value instanceof String) {
//                                        return ((String) value).isEmpty() || "0".equals(value);
//                                    } else if (value instanceof BigDecimal) {
//                                        return BigDecimal.ZERO.equals(value);
//                                    }
//                                    return false;
//                                });
//                        // 如果所有"_balance"键的值都为空或为"0"，则过滤掉该Map
//                        return !allEmptyOrZero;
//                    }).collect(Collectors.toList());
//            return filteredList;
        }
        return batchQueryUploadList;
    }

    /**
     * @description:处理状态过滤
     **/
    private List<Map<String, String>> processStatusFilter(List<Map<String, String>> batchQueryUploadList,List<String> statusList){
        // 过滤掉符合条件的Map
        if(CollectionUtil.isNotEmpty(batchQueryUploadList)){

        }
        return batchQueryUploadList;
    }
}
