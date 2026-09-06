package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.nacos.common.utils.CollectionUtils;
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
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailResultEntity;
import com.utfinancing.financehub.engine.finance.entity.ClientEntity;
import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.FileRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReportFinanceInOutQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReportFinanceInOutDTO;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.entity.ReportFinanceInOutEntity;
import com.utfinancing.financehub.engine.finance.mapper.ReportFinanceInOutMapper;
import com.utfinancing.financehub.engine.finance.service.IClientService;
import com.utfinancing.financehub.engine.finance.service.IFileRecordService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.finance.service.IReportFinanceInOutService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.hthx.common.page.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @Author : jnc
 * @Date : Create in 2024-04-22
 * @Description :  ReportFinanceInOut服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ReportFinanceInOutServiceImpl extends ServiceImpl<ReportFinanceInOutMapper, ReportFinanceInOutEntity> implements IReportFinanceInOutService {

    private final ReportFinanceInOutMapper reportFinanceInOutMapper;

    @Resource
    private IClientService iClientService;

    @Resource
    private IOrgCompanyService iOrgCompanyService;

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

    //服务器真实文件路径
    private final String FILE_PATH_LOCAL = "/home/admin/financehub/service/financehub-engine/export/";
    //nginx配置路径
    private final String FILE_PATH = "/report-export/";
//    private final String FILE_PATH_LOCAL = "C:\\Users\\jefjiang\\Desktop\\test\\";

    @Override
    public Long saveReportFinanceInOut(ReportFinanceInOutDTO dto) {
        ReportFinanceInOutEntity entity = BeanUtil.copyProperties(dto, ReportFinanceInOutEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateReportFinanceInOut(Long id, ReportFinanceInOutDTO dto) {
        ReportFinanceInOutEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ReportFinanceInOutDTO getReportFinanceInOutDTOById(Long id) {
        ReportFinanceInOutEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ReportFinanceInOutDTO.class);
    }

    @Override
    public PageResult selectPage(ReportFinanceInOutQueryDTO queryDTO) {
        PageResult batchQueryPageResult = new PageResult();
        Integer periodCode = queryDTO.getPeriodCode();
        if(ObjectUtil.isEmpty(periodCode)){
            throw new ServiceException("会计期间不能为空");
        }
        Integer currentPeriodCode = Integer.valueOf(DateUtil.format(LocalDateTime.now(), "yyyyMM"));
        if(periodCode.compareTo(currentPeriodCode) == 0){
            return queryPageByPeriodCodeByPage(queryDTO);
        }else {
            //查询历史数据，查询固化表
            LambdaQueryWrapper<ReportFinanceInOutEntity> queryWrapper = Wrappers.<ReportFinanceInOutEntity>lambdaQuery();
            //这里注入查询条件
            queryWrapper.eq(ReportFinanceInOutEntity::getPeriodCode, periodCode);
            queryWrapper.eq(ReportFinanceInOutEntity::getDelFlag,YesOrNoEnum.NO.getCode());
            if(StringUtils.isNotEmpty(queryDTO.getContractCode())){
                queryWrapper.eq(ReportFinanceInOutEntity::getContractCode, queryDTO.getContractCode());
            }
            if(StringUtils.isNotEmpty(queryDTO.getClientName())){
                queryWrapper.like(ReportFinanceInOutEntity::getClientName, queryDTO.getClientName());
            }
            if(StringUtils.isNotEmpty(queryDTO.getOrgId())){
                queryWrapper.eq(ReportFinanceInOutEntity::getOrgId, queryDTO.getOrgId());
            }
            if(StringUtils.isNotEmpty(queryDTO.getOutboundType())){
                queryWrapper.eq(ReportFinanceInOutEntity::getOutboundType, queryDTO.getOutboundType());
            }
            if(StringUtils.isNotEmpty(queryDTO.getInboundDateStart())){
                queryWrapper.apply("inbound_date >= {0}", queryDTO.getInboundDateStart());
            }
            if(StringUtils.isNotEmpty(queryDTO.getInboundDateEnd())){
                queryWrapper.apply("inbound_date <= {0}", queryDTO.getInboundDateEnd());
            }
            if(StringUtils.isNotEmpty(queryDTO.getOutboundDateStart())){
                queryWrapper.apply("outbound_date >= {0}", queryDTO.getOutboundDateStart());
            }
            if(StringUtils.isNotEmpty(queryDTO.getOutboundDateEnd())){
                queryWrapper.apply("outbound_date <= {0}", queryDTO.getOutboundDateEnd());
            }
            IPage<ReportFinanceInOutEntity> entityIPage = reportFinanceInOutMapper.selectPage(new Page<ReportFinanceInOutEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
            IPage<ReportFinanceInOutVO> page = ListBeanUtil.copyPage(entityIPage, ReportFinanceInOutVO.class);
            List<ReportFinanceInOutVO> records = page.getRecords();
            if(CollectionUtil.isNotEmpty(records) && records.size()>0) {
                Map<String, String> orgMap = getOrgNameOrgId();
                Map<String, String> clientMap = getClientNameClientCode(records.stream().map(ReportFinanceInOutVO::getClientCode).distinct().collect(Collectors.toList()));
                records.stream().forEach(i->{
                    if(StringUtils.isNotEmpty(i.getOrgId())&&orgMap.containsKey(i.getOrgId())){
                        i.setOrgName(orgMap.get(i.getOrgId()));
                    }
                    if(StringUtils.isNotEmpty(i.getClientCode())&&clientMap.containsKey(i.getClientCode())){
                        i.setClientName(clientMap.get(i.getClientCode()));
                    }
                });
                batchQueryPageResult = pageConvertToPageResult(page,queryDTO);
            }else{
                return queryPageByPeriodCodeByPage(queryDTO);
            }
        }
        return batchQueryPageResult;
    }

    private static <T> IPage<T> getIPage(int pageNum, int pageSize, List<T> list, long totalSize) {
        IPage<T> page = new Page<>(pageNum, pageSize);
        page.setRecords(list);
        page.setTotal(totalSize);
        return page;
    }

    @Transactional
    @Override
    public String syncFinanceInboundOutboundData() {
        String executeDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int periodCode = Integer.valueOf(DateUtil.format(DateUtil.offsetMonth(DateUtil.date(), -1), "yyyyMM"));
//        int periodCode = Integer.valueOf(DateUtil.format(LocalDateTime.now(), "yyyyMM"));
        //From：modify by zhangli.chen for 新增删除逻辑 on 20250224
        this.remove(new LambdaQueryWrapper<ReportFinanceInOutEntity>().eq(ReportFinanceInOutEntity::getPeriodCode,periodCode));
        //End：modify by zhangli.chen for 新增删除逻辑 on 20250224
        List<ReportFinanceInOutDTO> list = reportFinanceInOutMapper.selectInAndOutDataByPeriodCode();
        if(CollectionUtil.isNotEmpty(list)){
            list.stream().forEach(i->{
                //财务闭口
                i.setFinancialExposure(toBigDecimal(i.getReceivableRentBalance())
                        .add(toBigDecimal(i.getReceivableResidualValueBalance())).add(toBigDecimal(i.getReceivableOuttaxBalance()))
                        .subtract(toBigDecimal(i.getUnrealizedRevenueBalance())).subtract(toBigDecimal(i.getLesseeMarginBalance())));
                //净值 回收设备成本-回收设备减值
                i.setNetWorth(toBigDecimal(i.getRecyclingEquipmentCost()).subtract(toBigDecimal(i.getSubstractBalance())));
                i.setPeriodCode(periodCode);
                i.setExecuteDate(executeDate);
            });

            this.saveInOutData(list, periodCode, executeDate);
        }else{
            throw new ServiceException("未发现财务入库 回收设备数据");
        }

        return "success";
    }

    private void saveInOutData(List<ReportFinanceInOutDTO> list, int periodCode, String executeDate) {
        log.info("同步财务入库出库数据 periodCode:{}, 执行时间:{}  转换后数据量:{}", periodCode, executeDate, list.size());
        List<List<ReportFinanceInOutEntity>> entityPage = ListUtil.partition(BeanUtil.copyToList(list, ReportFinanceInOutEntity.class), 5000);
        int pageCount = entityPage.size();
        CountDownLatch countDownLatch = new CountDownLatch(pageCount);
        log.info("同步财务入库出库数据 分组完成  periodCode:{}, 执行时间:{} , pageCount:{}", periodCode, executeDate, pageCount);
        int i = 1;
        for (List<ReportFinanceInOutEntity> entityList: entityPage){
            this.batchSaveInOutData(countDownLatch, entityList, NumberUtil.formatPercent(i/Double.valueOf(Integer.valueOf(pageCount-1).toString()),2), periodCode);
            i++;
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("同步财务入库出库数据，保存完成 入库 periodCode:{}, 执行时间:{}, 转换后数据量:{}", periodCode, executeDate, list.size());

    }

    @Async
    public void batchSaveInOutData(CountDownLatch countDownLatch, List<ReportFinanceInOutEntity> entityList, String percent, int periodCode) {
        this.saveBatch(entityList);
        log.info("同步财务入库出库数据 单页保存完成.periodCode:{}, 完成比例:{}", periodCode, percent);
        countDownLatch.countDown();
    }

    private static BigDecimal toBigDecimal(BigDecimal bd) {
        return StringUtils.nvl(bd, BigDecimal.ZERO);
    }

    private Map<String,String> getOrgNameOrgId(){
        Map<String,String> orgNameAndIdMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  iOrgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(orgCompanyVOList)) {
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

    @Override
    public Map<String, String> generateInboundOutboundReportExcel(ReportFinanceInOutQueryDTO queryDTO) {
        Integer periodCode = queryDTO.getPeriodCode();
        if(ObjectUtil.isEmpty(periodCode)){
            throw new ServiceException("会计期间不能为空");
        }
        String fileName = "report_inbound_outbound_" + LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss")+".xlsx";
        FileRecordEntity record = new FileRecordEntity();
        record.setModuleName(ModuleEnum.REPORT.getCode());
        record.setBusinessScene(BusinessSceneEnum.INBOUND_OUTBOUND.getCode());
        record.setFileLocation(getFilePath()+fileName);
        record.setFileName(fileName);
        record.setExecuteStatus(CheckExecuteStatusEnum.INPROGRESS.getCode());
        record.setFileUploadBy(String.valueOf(SecurityUtils.getUserId()));
        fileRecordService.save(record);

        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            // 异步执行的任务
            queryAndWriteInboundOutbound(queryDTO, fileName);
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
    public IPage<FileRecordEntity> selectInboundOutboundFileList(FileRecordQueryDTO queryDTO) {
        queryDTO.setExecuteStatus(Arrays.asList(CheckExecuteStatusEnum.FINISH.getCode(), CheckExecuteStatusEnum.INPROGRESS.getCode()));
        queryDTO.setModuleName(ModuleEnum.REPORT.getCode());
        queryDTO.setBusinessScene(BusinessSceneEnum.INBOUND_OUTBOUND.getCode());
        return fileRecordService.selectFileListByModuleAndBusiness(queryDTO);
    }

    /**
     * @description: 财务出库入库口报表-异步下载
     **/
    private void queryAndWriteInboundOutbound(ReportFinanceInOutQueryDTO queryDTO, String fileName) {
        log.info("查询要导出的数据 开始");
        long l1 = System.currentTimeMillis();
        Integer periodCode = queryDTO.getPeriodCode();
        Integer currentPeriodCode = Integer.valueOf(DateUtil.format(LocalDateTime.now(), "yyyyMM"));
        List<ReportFinanceInOutVO> voList = Lists.newArrayList();
        if(periodCode.compareTo(currentPeriodCode) == 0){
            voList = exportByPeriodCode(queryDTO);
        }else {
            //查询历史数据，查询固化表
            LambdaQueryWrapper<ReportFinanceInOutEntity> queryWrapper = Wrappers.<ReportFinanceInOutEntity>lambdaQuery();
            //这里注入查询条件
            queryWrapper.eq(ReportFinanceInOutEntity::getPeriodCode, periodCode);
            queryWrapper.eq(ReportFinanceInOutEntity::getDelFlag, YesOrNoEnum.NO.getCode());

            if(StringUtils.isNotEmpty(queryDTO.getContractCode())){
                queryWrapper.eq(ReportFinanceInOutEntity::getContractCode, queryDTO.getContractCode());
            }
            if(StringUtils.isNotEmpty(queryDTO.getClientName())){
                queryWrapper.exists("select 1 from eg_client ec where ec.client_name like {0} and eg_report_finance_in_out.client_code = ec.client_code ", "%"+queryDTO.getClientName()+"%");
            }
            if(StringUtils.isNotEmpty(queryDTO.getOrgId())){
                queryWrapper.eq(ReportFinanceInOutEntity::getOrgId, queryDTO.getOrgId());
            }
            if(StringUtils.isNotEmpty(queryDTO.getOutboundType())){
                queryWrapper.eq(ReportFinanceInOutEntity::getOutboundType, queryDTO.getOutboundType());
            }
            if(StringUtils.isNotEmpty(queryDTO.getInboundDateStart())){
                queryWrapper.apply("inbound_date >= {0}", queryDTO.getInboundDateStart());
            }
            if(StringUtils.isNotEmpty(queryDTO.getInboundDateEnd())){
                queryWrapper.apply("inbound_date <= {0}", queryDTO.getInboundDateEnd());
            }
            if(StringUtils.isNotEmpty(queryDTO.getOutboundDateStart())){
                queryWrapper.apply("outbound_date >= {0}", queryDTO.getOutboundDateStart());
            }
            if(StringUtils.isNotEmpty(queryDTO.getOutboundDateEnd())){
                queryWrapper.apply("outbound_date <= {0}", queryDTO.getOutboundDateEnd());
            }
            List<ReportFinanceInOutEntity> tmpList = this.list(queryWrapper);
            voList = BeanUtil.copyToList(tmpList, ReportFinanceInOutVO.class);
            if(CollectionUtil.isNotEmpty(voList)) {
                Map<String, String> orgMap = getOrgNameOrgId();
                Map<String, String> clientMap = getClientNameClientCode(voList.stream().map(ReportFinanceInOutVO::getClientCode).distinct().collect(Collectors.toList()));
                voList.stream().forEach(i->{
                    if(StringUtils.isNotEmpty(i.getOrgId())&&orgMap.containsKey(i.getOrgId())){
                        i.setOrgName(orgMap.get(i.getOrgId()));
                    }
                    if(StringUtils.isNotEmpty(i.getClientCode())&&clientMap.containsKey(i.getClientCode())){
                        i.setClientName(clientMap.get(i.getClientCode()));
                    }
                });
            }else{
                voList = exportByPeriodCode(queryDTO);
            }
        }

        List<ReportFinanceInOutExcelVO> excelList = BeanUtil.copyToList(voList, ReportFinanceInOutExcelVO.class);
        long l2 = System.currentTimeMillis();
        log.info("查询要导出的数据 结束，用时{} s", (l2-l1)/1000);
        log.info("导出Excel数据 开始");
        ExcelWriter writer = ExcelUtil.getWriter(getFilePath()+fileName);
//        writer.writeHeadRow(LEASE_TABLE_HEAD);
//        writer.autoSizeColumnAll();
//        writer.addHeaderAlias("contractCode", "合同编码");
//        writer.addHeaderAlias("clientCode", "客户编码");
        writer.addHeaderAlias("clientName", "客户名称");
        writer.addHeaderAlias("orgName", "签约主体");
        writer.addHeaderAlias("inboundDate", "入库时间");
        writer.addHeaderAlias("receivableRentBalance", "应收租金");
        writer.addHeaderAlias("receivableResidualValueBalance", "应收期末残值");
        writer.addHeaderAlias("receivableOuttaxBalance", "应收销项税");
        writer.addHeaderAlias("unrealizedRevenueBalance", "未实现收益");
        writer.addHeaderAlias("lesseeMarginBalance", "承租人保证金");
        writer.addHeaderAlias("financialExposure", "财务敞口");
        writer.addHeaderAlias("recyclingEquipmentCost", "回收设备成本");
        writer.addHeaderAlias("recyclingEquipmentAccountBalance", "回收设备成本科目余额");
        writer.addHeaderAlias("provisionForImpairment", "入库时计提减值");
        writer.addHeaderAlias("substractBalance", "回收设备减值");
        writer.addHeaderAlias("netWorth", "净值");
        writer.addHeaderAlias("provisionalReceiptsBalance", "暂收款项");
        writer.addHeaderAlias("outboundDate", "出库日期");
        writer.addHeaderAlias("outboundType", "出库类型");
        writer.addHeaderAlias("dealAmount", "处置金额");
        writer.addHeaderAlias("receivableServiceOuttaxAmount", "应交销项税");
        writer.addHeaderAlias("profitLoss", "融资租赁资产处置损益");

        writer.autoSizeColumnAll();
        writer.write(excelList, true);
        writer.close();
        long l3 = System.currentTimeMillis();
        log.info("导出Excel数据 结束，用时{} s", (l3-l2)/1000);
    }

    private String getFilePath(){
        String osName = System.getProperties().getProperty("os.name");
        if(osName.toLowerCase().contains("windows")){
            return basicPathWindows+"inboundOutbound"+ File.separator;
        }else if(osName.toLowerCase().contains("linux") || osName.toLowerCase().contains("unix")){
            return basicPathLinux+"inboundOutbound"+File.separator;
        }
        return StringUtils.EMPTY;
    }

    /**
     * @description: 按查询条件过滤并分页
     * @author: zhangli.chen
     **/
    private PageResult<List<ReportFinanceInOutDTO>> filterAndPage(List<ReportFinanceInOutDTO> toFilterList, ReportFinanceInOutQueryDTO queryParams){
        PageResult<List<ReportFinanceInOutDTO>> filterPageResult = new PageResult<>();
        if(CollectionUtil.isNotEmpty(toFilterList)){
            // 计算总记录数
            int total = toFilterList.size();
            if(total>0){
                // 计算分页的起始索引和结束索引
                int start = (queryParams.getPageNum() - 1) * queryParams.getPageSize();
                int end = Math.min(start + queryParams.getPageSize(), toFilterList.size());
                int totalPage = (total % queryParams.getPageSize() != 0 ? total / queryParams.getPageSize() + 1 : total/queryParams.getPageSize());
                // 提取当前页的数据
                List<ReportFinanceInOutDTO> pageData = toFilterList.subList(start, end);
                filterPageResult = new PageResult(total,queryParams.getPageNum(),queryParams.getPageSize(),totalPage, pageData);
            }
        }else{
            filterPageResult = new PageResult(FinanceEngineEnum.Numbers.ZERO.getKey(),queryParams.getPageNum(),queryParams.getPageSize(),FinanceEngineEnum.Numbers.ZERO.getKey());
        }
        return filterPageResult;
    }

    /**
     * @description: pageConvertToPageResult
     * @author: zhangli.chen
     **/
    private PageResult<List<ReportFinanceInOutVO>> pageConvertToPageResult(IPage<ReportFinanceInOutVO> page,ReportFinanceInOutQueryDTO queryParams){
        PageResult<List<ReportFinanceInOutVO>> filterPageResult =  new PageResult<>();
        List<ReportFinanceInOutVO> records = page.getRecords();
        if(CollectionUtil.isNotEmpty(records)){
            int total = new Integer(Long.valueOf(page.getTotal()).intValue());
            int pages = new Integer(Long.valueOf(page.getPages()).intValue());
            if(total>0){
                filterPageResult = new PageResult(total,queryParams.getPageNum(),queryParams.getPageSize(),pages,records);
            }
        }else{
            filterPageResult = new PageResult(FinanceEngineEnum.Numbers.ZERO.getKey(),queryParams.getPageNum(),queryParams.getPageSize(),FinanceEngineEnum.Numbers.ZERO.getKey());
        }
        return filterPageResult;
    }


    /**
     * @description: 根据特定期间实时查询分页数据
     * @author: zhangli.chen
     * @date 2025/11/11 10:44
     * @param: queryDTO
     * @return PageResult
     **/
    PageResult queryPageByPeriodCodeByPage(ReportFinanceInOutQueryDTO queryDTO){
        PageResult batchQueryPageResult = new PageResult();
        List<ReportFinanceInOutDTO> currentMonthDataList = reportFinanceInOutMapper.getFinanceInboundAndOutboundDataForCurrentMonth(queryDTO);
        batchQueryPageResult = filterAndPage(currentMonthDataList,queryDTO);
        List<ReportFinanceInOutDTO> filterResultList = batchQueryPageResult.getRecords();
        if(CollectionUtil.isNotEmpty(filterResultList)){
            List<ReportFinanceInOutVO> voList = BeanUtil.copyToList(filterResultList, ReportFinanceInOutVO.class);
            Map<String, String> orgMap = getOrgNameOrgId();
            voList.stream().forEach(i->{
                i.setFinancialExposure(toBigDecimal(i.getReceivableRentBalance())
                        .add(toBigDecimal(i.getReceivableResidualValueBalance())).add(toBigDecimal(i.getReceivableOuttaxBalance()))
                        .subtract(toBigDecimal(i.getUnrealizedRevenueBalance())).subtract(toBigDecimal(i.getLesseeMarginBalance())));
                //净值 回收设备成本-回收设备减值
                i.setNetWorth(toBigDecimal(i.getRecyclingEquipmentCost()).subtract(toBigDecimal(i.getSubstractBalance())));
                i.setPeriodCode(queryDTO.getPeriodCode());
                if(StringUtils.isNotEmpty(i.getOrgId())&&orgMap.containsKey(i.getOrgId())){
                    i.setOrgName(orgMap.get(i.getOrgId()));
                }
            });
            batchQueryPageResult.setRecords(voList);
        }
        return batchQueryPageResult;
    }

    /**
     * @description: 导出查询
     * @author: zhangli.chen
     **/
    List<ReportFinanceInOutVO> exportByPeriodCode(ReportFinanceInOutQueryDTO queryDTO){
        List<ReportFinanceInOutVO> voList = Lists.newArrayList();
        List<ReportFinanceInOutDTO> list = reportFinanceInOutMapper.getFinanceInboundAndOutboundDataForCurrentMonth(queryDTO);
        if(CollectionUtil.isNotEmpty(list)){
            voList = BeanUtil.copyToList(list, ReportFinanceInOutVO.class);
            Map<String, String> orgMap = getOrgNameOrgId();
            voList.stream().forEach(i->{
                i.setFinancialExposure(toBigDecimal(i.getReceivableRentBalance())
                        .add(toBigDecimal(i.getReceivableResidualValueBalance())).add(toBigDecimal(i.getReceivableOuttaxBalance()))
                        .subtract(toBigDecimal(i.getUnrealizedRevenueBalance())).subtract(toBigDecimal(i.getLesseeMarginBalance())));
                //净值 回收设备成本-回收设备减值
                i.setNetWorth(toBigDecimal(i.getRecyclingEquipmentCost()).subtract(toBigDecimal(i.getSubstractBalance())));
                i.setPeriodCode(queryDTO.getPeriodCode());
                if(StringUtils.isNotEmpty(i.getOrgId())&&orgMap.containsKey(i.getOrgId())){
                    i.setOrgName(orgMap.get(i.getOrgId()));
                }
            });
        }
        return voList;
    }


}

