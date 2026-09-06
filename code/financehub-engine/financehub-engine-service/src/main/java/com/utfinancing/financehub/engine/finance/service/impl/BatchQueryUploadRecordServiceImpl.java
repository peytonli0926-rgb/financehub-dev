package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.enums.BusinessSceneEnum;
import com.utfinancing.financehub.engine.enums.CheckExecuteStatusEnum;
import com.utfinancing.financehub.engine.enums.ModuleEnum;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.BatchQueryUploadRecordMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.AccountAssistBalanceVO;
import com.utfinancing.financehub.engine.finance.model.vo.BatchQueryDataVO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author : jnc
 * @Date : Create in 2024-05-14
 * @Description :  BatchQueryUploadRecord服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class BatchQueryUploadRecordServiceImpl extends ServiceImpl<BatchQueryUploadRecordMapper, BatchQueryUploadRecordEntity> implements IBatchQueryUploadRecordService {

    private final BatchQueryUploadRecordMapper batchQueryUploadRecordMapper;

    @Resource
    private IContractService contractService;

    @Resource
    private IAccountService accountService;

    @Resource
    private IOrgCompanyService orgCompanyService;

    @Resource
    private IAccountAssistBalanceService accountAssistBalanceService;

    @Resource
    private IFileRecordService fileRecordService;

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Value("${service.parth:null}")
    private String servicePath;

    @Value("${file.storage.basicpath.windows:null}")
    private String basicPathWindows;

    @Value("${file.storage.basicpath.linux:null}")
    private String basicPathLinux;

    //服务器真实文件路径
//    private final String FILE_PATH_LOCAL = "/home/admin/financehub/service/financehub-engine/export/";
    private final String FILE_PATH_LOCAL = "C:\\Users\\jefjiang\\Desktop\\test\\";

    //nginx配置路径
    private final String FILE_PATH = "/report-export/";

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
        } catch (ServiceException serviceException) {
            throw new ServiceException("导入批量查询模板数据失败，失败原因："+serviceException.getMessage());
        } catch (Exception exception) {
            throw new ServiceException("导入批量查询模板数据失败，失败原因："+exception.getMessage());
        }


        return Boolean.TRUE;
    }

    @Override
    public IPage<Map<String, String>> selectPage(BatchQueryUploadQueryDTO queryDTO) {
        queryDTO.setQueryType("query");

        BatchQueryUploadRecordEntity lastRecord = setParam(queryDTO);

        Map<String, Long> sizeMap = batchQueryUploadRecordMapper.selectBatchQueryContractBalanceInfoSize(queryDTO);
        List<Map<String, String>> contractBalanceInfoList = batchQueryUploadRecordMapper.selectBatchQueryContractBalanceInfo(queryDTO);

//        List<AccountAssistBalanceEntity> assistBalanceList = accountAssistBalanceService.list(new LambdaQueryWrapper<AccountAssistBalanceEntity>()
//                .in(AccountAssistBalanceEntity::getOrgId, queryDTO.getOrgIdList())
//                .in(AccountAssistBalanceEntity::getContractCode, queryDTO.getContractCodeList())
//                .in(AccountAssistBalanceEntity::getAccountCode, queryDTO.getAccountCodeList())
//                .eq(AccountAssistBalanceEntity::getPeriodCode, periodCode));

        if(CollectionUtil.isNotEmpty(contractBalanceInfoList)){
            Map<String,String> orgMap = getOrgNameOrgId();
            contractBalanceInfoList.stream().forEach(i->{
                if(ObjectUtil.isNotEmpty(i.get("org_id"))&&orgMap.containsKey(i.get("org_id"))){
                    i.put("org_name", orgMap.get(i.get("org_id")));
                }
            });
        }
        return getIPage(queryDTO.getPageNum(), queryDTO.getPageSize(), contractBalanceInfoList, sizeMap.get("total_size"));
    }

    private Map<String,String> getOrgNameOrgId(){
        Map<String,String> orgNameAndIdMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  orgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgNameAndIdMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId,OrgCompanyVO::getOrgName, (k1, k2)->k2));
        }
        return orgNameAndIdMap;
    }

    private BatchQueryUploadRecordEntity setParam(BatchQueryUploadQueryDTO queryDTO) {
        String queryDate = queryDTO.getQueryDate();
        if(StringUtils.isEmpty(queryDate)){
            throw new ServiceException("查询日期不能为空");
        }

        List<BatchQueryUploadRecordEntity> records = this.list(new LambdaQueryWrapper<BatchQueryUploadRecordEntity>().eq(BatchQueryUploadRecordEntity::getUploadBy, SecurityUtils.getUserId().toString()).orderByDesc(BatchQueryUploadRecordEntity::getUploadTime));
        if(CollectionUtil.isEmpty(records)){
            throw new ServiceException("未找到需要查询的合同编号和科目编号");
        }
        BatchQueryUploadRecordEntity lastRecord = records.get(0);

        String accountCodes = lastRecord.getAccountCodeList();
        String contractCodes = lastRecord.getContractCodeList();

        List<String> accountCodeList = Arrays.asList(accountCodes.split(","));
        List<String> contractCodeList = Arrays.asList(contractCodes.split(","));

        queryDTO.setAccountCodeList(accountCodeList);
        queryDTO.setContractCodeList(contractCodeList);

        Integer periodCode = Integer.valueOf(queryDate.replaceAll("-","").substring(0,6));
        queryDTO.setPeriodCode(periodCode);

//        List<AccountEntity> accountEntityList = accountService.list(new LambdaQueryWrapper<AccountEntity>().in(AccountEntity::getAccountCode, accountCodeList));

        List<AccountEntity> accountEntityList = accountService.list(
                new QueryWrapper<AccountEntity>().select("distinct account_code, account_name, fund_type").lambda().in(AccountEntity::getAccountCode, accountCodeList));

        if(CollectionUtil.isEmpty(accountEntityList)){
            throw new ServiceException("未找到需要查询科目编号");
        }

        //1532.01特殊处理
        List<String> fundTypeList = accountEntityList.stream().filter(i->StringUtils.isNotEmpty(i.getFundType())&&!StringUtils.equals(i.getAccountCode(), "1532.01")).map(i->i.getFundType()+"_balance").distinct().collect(Collectors.toList());
        queryDTO.setFundTypeList(fundTypeList);

        Integer limit = queryDTO.getPageSize()* queryDTO.getPageNum();
        Integer offset = queryDTO.getPageSize()*(queryDTO.getPageNum()-1);
        queryDTO.setLimit(limit);
        queryDTO.setOffset(offset);

        return lastRecord;
    }

    @Override
    public Map<String, String> generateBatchQueryReportExcel(BatchQueryUploadQueryDTO queryDTO) {
        queryDTO.setQueryType("export");

        BatchQueryUploadRecordEntity lastRecord = setParam(queryDTO);

        String fileName = "批量查询生成文件_"+queryDTO.getExportType()+"_" + LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss")+".xlsx";
        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(getFilePath()+fileName);
//        getFilePath()

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

    @Override
    public IPage<FileRecordEntity> selectBatchQueryFileList(FileRecordQueryDTO queryDTO) {
        queryDTO.setExecuteStatus(Arrays.asList(CheckExecuteStatusEnum.FINISH.getCode(), CheckExecuteStatusEnum.INPROGRESS.getCode()));
        if (StringUtils.isEmpty(queryDTO.getModuleName())) {
            queryDTO.setModuleName(ModuleEnum.QUERY_MODULE.getCode());
        }
        if (StringUtils.isEmpty(queryDTO.getBusinessScene())) {
            queryDTO.setBusinessScene(BusinessSceneEnum.BATCH_QUERY.getCode());
        }
        return fileRecordService.selectFileListByModuleAndBusiness(queryDTO);
    }

    @Override
    public List<Map<String, String>> selectHeaderList() {
        List<Map<String, String>> resList = Lists.newArrayList();
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

        List<BatchQueryUploadRecordEntity> records = this.list(new LambdaQueryWrapper<BatchQueryUploadRecordEntity>().eq(BatchQueryUploadRecordEntity::getUploadBy, SecurityUtils.getUserId().toString()).orderByDesc(BatchQueryUploadRecordEntity::getUploadTime));
        if(CollectionUtil.isEmpty(records)){
            return resList;
        }
        BatchQueryUploadRecordEntity lastRecord = records.get(0);

        String accountCodes = lastRecord.getAccountCodeList();

        List<String> accountCodeList = Arrays.asList(accountCodes.split(","));
        //
        List<AccountEntity> accountEntityList = accountService.list(
                new QueryWrapper<AccountEntity>().select("distinct account_code, account_name, fund_type").lambda().in(AccountEntity::getAccountCode, accountCodeList));

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

        return resList;
    }


    private String getFilePath(){
        String osName = System.getProperties().getProperty("os.name");
        if(osName.toLowerCase().contains("windows")){
            return basicPathWindows+"batchQuery"+File.separator;
        }else if(osName.toLowerCase().contains("linux") || osName.toLowerCase().contains("unix")){
            return basicPathLinux+"batchQuery"+File.separator;
        }
        return StringUtils.EMPTY;
    }


    private void queryAndWriteBatchQueryData(BatchQueryUploadQueryDTO queryDTO, String fileName, ExcelWriter writer, BatchQueryUploadRecordEntity lastRecord) {

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

        List<AccountEntity> accountEntityList = accountService.list(new LambdaQueryWrapper<AccountEntity>().in(AccountEntity::getAccountCode, accountCodeList));
        List<Map<String, String>> contractBalanceInfoList = batchQueryUploadRecordMapper.selectBatchQueryContractBalanceInfo(queryDTO);

        List<Map<String, String>> tmpList = Lists.newArrayList();

        if(StringUtils.equals("common", queryDTO.getExportType())){

            if(CollectionUtil.isNotEmpty(contractBalanceInfoList)){
                Map<String,String> orgMap = getOrgNameOrgId();

                for(Map<String, String> map : contractBalanceInfoList){
                    if(ObjectUtil.isNotEmpty(map.get("org_id"))&&orgMap.containsKey(map.get("org_id"))){
                        map.put("org_name", orgMap.get(map.get("org_id")));
                    }

//                    Map<String, String> copiedMap = map.entrySet().stream()
//                            .filter(m->!StringUtils.equals(m.getKey(), "org_id")&&!StringUtils.equals(m.getKey(), "client_code"))
//                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    Map<String, String> copiedMap = Maps.newHashMap(map);
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

//            writer.autoSizeColumnAll();
//            writer.write(tmpList, true);
        }else{

            List<Map<String, String>> assistBalanceInfoList = batchQueryUploadRecordMapper.selectBatchQueryAssistBalanceInfo(queryDTO);

            Map<String, Map<String, String>> contractGroupedMap = contractBalanceInfoList.stream().collect(Collectors.toMap(m->m.get("org_id")+"&"+m.get("contract_code"), Function.identity(), ((key1 , key2) -> key1)));

            Map<String, List<Map<String, String>>> assistGroupedMap = assistBalanceInfoList.stream().collect(Collectors.groupingBy(m->m.get("org_id")+"&"+m.get("contract_code")));

            Map<String,String> orgMap = getOrgNameOrgId();

            for(Map.Entry<String, Map<String, String>> entry: contractGroupedMap.entrySet()){
                String key = entry.getKey();
                Map<String, String> map = entry.getValue();
                List<Map<String, String>> assistMaps = assistGroupedMap.get(key);
                for(AccountEntity account : accountEntityList){
                    if(CollectionUtil.isEmpty(assistMaps)){
                        //1532.01特殊处理
                        if(StringUtils.equals(account.getAccountCode(), "1532.01")){
                            map.put("unrealized_revenue_no_service_fee_month_debit_amount", "0");
                            map.put("unrealized_revenue_no_service_fee_month_credit_amount", "0");
                            map.put("unrealized_revenue_no_service_fee_month_end_debit_balance", "0");
                            map.put("unrealized_revenue_no_service_fee_month_end_credit_balance", "0");

                            map.put("unrealized_revenue_service_fee_month_debit_amount", "0");
                            map.put("unrealized_revenue_service_fee_month_credit_amount", "0");
                            map.put("unrealized_revenue_service_fee_month_end_debit_balance", "0");
                            map.put("unrealized_revenue_service_fee_month_end_credit_balance", "0");
                        }else{
                            map.put(account.getFundType()+"_month_debit_amount", "0");
                            map.put(account.getFundType()+"_month_credit_amount", "0");
                            map.put(account.getFundType()+"_month_end_debit_balance", "0");
                            map.put(account.getFundType()+"_month_end_credit_balance", "0");
                        }
                    }else {
                        Optional<Map<String, String>> op = assistMaps.stream().filter(i->StringUtils.equals(i.get("account_code"), account.getAccountCode())).findFirst();
                        if(op.isPresent()){
                            Map<String, String> tmp = op.get();
                            //1532.01特殊处理
                            if(StringUtils.equals(tmp.get("account_code"), "1532.01")){
                                map.put("unrealized_revenue_no_service_fee_month_debit_amount", tmp.get("month_debit_amount"));
                                map.put("unrealized_revenue_no_service_fee_month_credit_amount", tmp.get("month_credit_amount"));
                                map.put("unrealized_revenue_no_service_fee_month_end_debit_balance", tmp.get("month_end_debit_balance"));
                                map.put("unrealized_revenue_no_service_fee_month_end_credit_balance", tmp.get("month_end_credit_balance"));

                                map.put("unrealized_revenue_service_fee_month_debit_amount", tmp.get("month_debit_amount"));
                                map.put("unrealized_revenue_service_fee_month_credit_amount", tmp.get("month_credit_amount"));
                                map.put("unrealized_revenue_service_fee_month_end_debit_balance", tmp.get("month_end_debit_balance"));
                                map.put("unrealized_revenue_service_fee_month_end_credit_balance", tmp.get("month_end_credit_balance"));
                            }else{
                                map.put(account.getFundType()+"_month_debit_amount", tmp.get("month_debit_amount"));
                                map.put(account.getFundType()+"_month_credit_amount", tmp.get("month_credit_amount"));
                                map.put(account.getFundType()+"_month_end_debit_balance", tmp.get("month_end_debit_balance"));
                                map.put(account.getFundType()+"_month_end_credit_balance", tmp.get("month_end_credit_balance"));
                            }
                        }else{
                            map.put(account.getFundType()+"_month_debit_amount", "0");
                            map.put(account.getFundType()+"_month_credit_amount", "0");
                            map.put(account.getFundType()+"_month_end_debit_balance", "0");
                            map.put(account.getFundType()+"_month_end_credit_balance", "0");
                        }
                    }

                }

                if(ObjectUtil.isNotEmpty(map.get("org_id"))&&orgMap.containsKey(map.get("org_id"))){
                    map.put("org_name", orgMap.get(map.get("org_id")));
                }

//                Map<String, String> copiedMap = map.entrySet().stream()
//                        .filter(m->!StringUtils.equals(m.getKey(), "org_id")&&!StringUtils.equals(m.getKey(), "client_code"))
//                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                Map<String, String> copiedMap = Maps.newHashMap(map);
                copiedMap.remove("org_id");
                copiedMap.remove("client_code");
                //1532.01特殊处理
                if(!accountCodeList.contains("1532.01")){
                    copiedMap.remove("unrealized_revenue_balance_no_service_fee");
                    copiedMap.remove("unrealized_revenue_balance_service_fee");
                }
                tmpList.add(copiedMap);

            }

            for(AccountEntity account : accountEntityList){
                //1532.01特殊处理
                if(StringUtils.equals(account.getAccountCode(), "1532.01")){
                    writer.addHeaderAlias("unrealized_revenue_balance_no_service_fee", "未实现收益（不含咨询服务费分摊）");
                    writer.addHeaderAlias("unrealized_revenue_no_service_fee_month_debit_amount", "未实现收益（不含咨询服务费分摊）_当月借方发生额");
                    writer.addHeaderAlias("unrealized_revenue_no_service_fee_month_credit_amount", "未实现收益（不含咨询服务费分摊）_当月贷方发生额");
                    writer.addHeaderAlias("unrealized_revenue_no_service_fee_month_end_debit_balance", "未实现收益（不含咨询服务费分摊）_月末借方余额");
                    writer.addHeaderAlias("unrealized_revenue_no_service_fee_month_end_credit_balance", "未实现收益（不含咨询服务费分摊）_月末贷方余额");

                    writer.addHeaderAlias("unrealized_revenue_balance_service_fee", "未实现收益（咨询服务费分摊）");
                    writer.addHeaderAlias("unrealized_revenue_service_fee_month_debit_amount", "未实现收益（咨询服务费分摊）_当月借方发生额");
                    writer.addHeaderAlias("unrealized_revenue_service_fee_month_credit_amount", "未实现收益（咨询服务费分摊）_当月贷方发生额");
                    writer.addHeaderAlias("unrealized_revenue_service_fee_month_end_debit_balance", "未实现收益（咨询服务费分摊）_月末借方余额");
                    writer.addHeaderAlias("unrealized_revenue_service_fee_month_end_credit_balance", "未实现收益（咨询服务费分摊）_月末贷方余额");

                }else{
                    writer.addHeaderAlias(account.getFundType()+"_balance", account.getAccountName());
                    writer.addHeaderAlias(account.getFundType()+"_month_debit_amount", account.getAccountName()+"_当月借方发生额");
                    writer.addHeaderAlias(account.getFundType()+"_month_credit_amount", account.getAccountName()+"_当月贷方发生额");
                    writer.addHeaderAlias(account.getFundType()+"_month_end_debit_balance", account.getAccountName()+"_月末借方余额");
                    writer.addHeaderAlias(account.getFundType()+"_month_end_credit_balance", account.getAccountName()+"_月末贷方余额");
                }

            }


        }
        writer.autoSizeColumnAll();
        writer.write(tmpList, true);
        writer.close();
    }

    private static <T> IPage<T> getIPage(int pageNum, int pageSize, List<T> list, long totalSize) {
        IPage<T> page = new Page<>(pageNum, pageSize);
        page.setRecords(list);
        page.setTotal(totalSize);
        return page;
    }

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
            throw new ServiceException("存在合同编号有误的数据");
        }
        for(String contractCode : contractCodeList){
            Optional<ContractEntity> op = contractEntityList.stream().filter(i->StringUtils.equals(i.getContractCode(), contractCode)).findAny();
            if(!op.isPresent()){
                throw new ServiceException("存在合同编号有误的数据");
            }
        }

        accountCodeList.addAll(excelList.stream().map(BatchQueryUploadExcelDTO::getAccountCode).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList()));
        if(CollectionUtil.isEmpty(accountCodeList)){
            throw new ServiceException("科目编号不能为空");
        }

        List<AccountEntity> accountEntityList = accountService.list(new LambdaQueryWrapper<AccountEntity>().in(AccountEntity::getAccountCode, accountCodeList));
        if(CollectionUtil.isEmpty(accountEntityList)){
            throw new ServiceException("存在科目编号有误的数据");
        }

        for(String accountCode : accountCodeList){
            Optional<AccountEntity> op = accountEntityList.stream().filter(i->StringUtils.equals(i.getAccountCode(), accountCode)).findAny();
            if(!op.isPresent()){
                throw new ServiceException("存在科目编号有误的数据");
            }
        }
    }
}

