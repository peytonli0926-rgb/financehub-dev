package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.enums.BusinessSceneEnum;
import com.utfinancing.financehub.engine.enums.CheckExecuteStatusEnum;
import com.utfinancing.financehub.engine.enums.ModuleEnum;
import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.utfinancing.financehub.engine.finance.entity.ReportPeriodSyncRecordEntity;
import com.utfinancing.financehub.engine.finance.mapper.ReportCommonMapper;
import com.utfinancing.financehub.engine.finance.model.dto.FileRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReportFinanceInOutDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReportFinanceInOutQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReportLeaseTableQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ReportFinanceInOutVO;
import com.utfinancing.financehub.engine.finance.model.vo.ReportLeaseTableExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ReportLeaseTableVO;
import com.utfinancing.financehub.engine.finance.service.IFileRecordService;
import com.utfinancing.financehub.engine.finance.service.IReportCommonService;
import com.utfinancing.financehub.engine.finance.service.IReportPeriodSyncRecordService;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-02-27
 * @Description :  ChargeOff服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ReportCommonServiceImpl implements IReportCommonService {

    private final ReportCommonMapper reportCommonMapper;

    @Resource
    private IReportPeriodSyncRecordService reportPeriodSyncRecordService;

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

//    private final String FINANCE_RELEASE_DATE = "2024-04-31";
    private final String FINANCE_RELEASE_DATE = "2016-05-31";

    private final List<String> LEASE_TABLE_HEAD = Arrays.asList("公司","合同编号","业务系统","合同状态","财务合同状态","会计起租日",
            "合同约定到期日","租赁类型","应收租金","应收首付款","应收期末残值","应收手续费","应收保险费","应收其他收入","应收返利","应收销项税",
            "应收销项税-本金","未实现收益-总","未实现收益-不含服务费","融资租赁收益余额","减值准备余额","保证金余额","TA重分类","逾期收益");

    //服务器真实文件路径
    private final String FILE_PATH_LOCAL = "/home/admin/financehub/service/financehub-engine/export/";
    //nginx配置路径
    private final String FILE_PATH = "/report-export/";
//    private final String FILE_PATH_LOCAL = "C:\\Users\\jefjiang\\Desktop\\test\\";
//    private final String FILE_PATH = "/data/finhub/service/financehub-engine/export/";


    @Override
    public IPage<ReportLeaseTableVO> selectLeaseTable(ReportLeaseTableQueryDTO queryDTO) {
//        queryDTO.setExportType("query");
        if(StringUtils.isEmpty(queryDTO.getQueryDate())){
            throw new ServiceException("查询日期不能为空");
        }
        Integer limit = queryDTO.getPageSize()*queryDTO.getPageNum();
        Integer offset = queryDTO.getPageSize()*(queryDTO.getPageNum()-1);
        queryDTO.setLimit(limit);
        queryDTO.setOffset(offset);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String nextDayStr = getQueryNextDate(queryDTO, formatter);
        queryDTO.setQueryNextDate(nextDayStr);
        Integer periodCode = getPeriodCode(queryDTO, formatter);
        queryDTO.setPeriodCode(periodCode);

        Long l1 = System.currentTimeMillis();
        List<ReportLeaseTableVO> leaseTableVOS = reportCommonMapper.selectLeaseTable(queryDTO);
        Long l2 = System.currentTimeMillis();
        log.info("分页查数据用了 {} s" ,(l2-l1)/1000);
        Map<String, Long> sizeMap = reportCommonMapper.selectLeaseTableSize(queryDTO);
        Long l3 = System.currentTimeMillis();
        log.info("总条数查询用了 {} s" ,(l3-l2)/1000);
        IPage<ReportLeaseTableVO> page = getIPage(queryDTO.getPageNum(), queryDTO.getPageSize(), leaseTableVOS, sizeMap.get("total_size"));
        return page;
    }

    private static Integer getPeriodCode(ReportLeaseTableQueryDTO queryDTO, DateTimeFormatter formatter) {
        LocalDate date = LocalDate.parse(queryDTO.getQueryDate(), formatter);
        Integer periodCode = Integer.valueOf(date.format(DateTimeFormatter.ofPattern("yyyyMM")));
        return periodCode;
    }

    private static String getQueryNextDate(ReportLeaseTableQueryDTO queryDTO, DateTimeFormatter formatter) {
        LocalDate date = LocalDate.parse(queryDTO.getQueryDate(), formatter);
        LocalDate nextDay = date.plusDays(1);
        String nextDayStr = nextDay.format(formatter);
        return nextDayStr;
    }

    /**
     * TODO 还需要业务解释
     * @param queryDTO
     * @return
     */
    @Override
    public Map<String, String> checkLeaseTableDate(ReportLeaseTableQueryDTO queryDTO) {
        Map<String, String> resultMap = Maps.newHashMap();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime releaseDate = DateTime.of(FINANCE_RELEASE_DATE, "yyyy-MM-dd").toLocalDateTime();
        LocalDateTime queryDate = DateTime.of(queryDTO.getQueryDate(), "yyyy-MM-dd").toLocalDateTime();
        LocalDateTime today = DateTime.of(LocalDateTime.now().format(formatter), "yyyy-MM-dd").toLocalDateTime();
//        LocalDateTime beginOfCurrentMonth = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth());
//        LocalDateTime endOfCurrentMonth = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth());;

        if(queryDate.isBefore(releaseDate)){
            throw new ServiceException("请联系it帮忙取数");
        }
        //查询日期大于当前日期
        if(queryDate.isAfter(today)){
            throw new ServiceException("不能查询未来时间数据");
        }

        String currentPeriod = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        ReportPeriodSyncRecordEntity record = reportPeriodSyncRecordService.getOne(new LambdaQueryWrapper<ReportPeriodSyncRecordEntity>()
                .eq(ReportPeriodSyncRecordEntity::getPeriodCode, currentPeriod)
                .eq(ReportPeriodSyncRecordEntity::getExecuteStatus, CheckExecuteStatusEnum.FINISH.getCode()));

        //查询日期等于当前日期
        if(queryDate.isEqual(today)){
            resultMap.put("type","latest");
        }else {
            //查询日期<当前日期

            boolean endOfMonthFlag = isEndOfMonth(queryDate);
            //查询时间不是某个月的月末
            if(!endOfMonthFlag){
                resultMap.put("type","contract");
            }else{
                //查询时间是某个月的月末

                if(monthBetween(today, queryDate)==1){
                    //如果查询日期和当前日期差一个月，判断当前月份month表数据是否同步
                    if(ObjectUtil.isNotEmpty(record)){
                        //如果当前月份已同步month表数据
                        resultMap.put("type","month");
                    }else{
                        //当前月份未同步month表数据
                        resultMap.put("type","contract");
                    }
                }else {
                    //如果查询日期和当前日期差大于1个月
                    resultMap.put("type","month");
                }
            }
        }

//
//        if(queryDate.isBefore(beginOfCurrentMonth)){
//            resultMap.put("type","month");
//        }else if(!queryDate.isBefore(beginOfCurrentMonth)&&ObjectUtil.isEmpty(record)){
//            resultMap.put("type","latest");
//        }else if(!queryDate.isBefore(beginOfCurrentMonth)&&!ObjectUtil.isEmpty(record)&&!queryDate.isBefore(syncDataDate)&&queryDate.isBefore(endOfCurrentMonth)){
//            resultMap.put("type","contract");
//        }else if(!queryDate.isBefore(beginOfCurrentMonth)&&!ObjectUtil.isEmpty(record)&&queryDate.isBefore(syncDataDate)&&queryDate.isBefore(endOfCurrentMonth)){
//            resultMap.put("type","month");
//        }else if(queryDate.isAfter(beginOfCurrentMonth)&&!ObjectUtil.isEmpty(record)&&!today.isBefore(endOfCurrentMonth)){
//            resultMap.put("type","month");
//        }

        log.info("today:{} queryDate:{} 是否同步:{} type:{}",today.format(formatter), queryDate.format(formatter), ObjectUtil.isNotEmpty(record), resultMap.get("type"));
        return resultMap;
    }

    @Override
    public Map<String, String> generateLeaseTableReportExcel(ReportLeaseTableQueryDTO queryDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Integer periodCode = getPeriodCode(queryDTO, formatter);
        queryDTO.setPeriodCode(periodCode);
        String fileName = "report_lease_table_" + LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss")+".xlsx";

        FileRecordEntity record = new FileRecordEntity();
        record.setModuleName(ModuleEnum.REPORT.getCode());
        record.setBusinessScene(BusinessSceneEnum.LEASE_TABLE.getCode());
        record.setFileLocation(getFilePath()+fileName);
        record.setFileName(fileName);
        record.setExecuteStatus(CheckExecuteStatusEnum.INPROGRESS.getCode());
        record.setFileUploadBy(String.valueOf(SecurityUtils.getUserId()));
        fileRecordService.save(record);

        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            // 异步执行的任务
            queryAndWriteLeaseTable(queryDTO, fileName);
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
            return basicPathWindows+"leaseTable"+ File.separator;
        }else if(osName.toLowerCase().contains("linux") || osName.toLowerCase().contains("unix")){
            return basicPathLinux+"leaseTable"+File.separator;
        }
        return StringUtils.EMPTY;
    }

    @Override
    public IPage<FileRecordEntity> selectLeaseTableFileList(FileRecordQueryDTO queryDTO) {
        queryDTO.setExecuteStatus(Arrays.asList(CheckExecuteStatusEnum.FINISH.getCode(), CheckExecuteStatusEnum.INPROGRESS.getCode()));
        queryDTO.setModuleName(ModuleEnum.REPORT.getCode());
        queryDTO.setBusinessScene(BusinessSceneEnum.LEASE_TABLE.getCode());
        return fileRecordService.selectFileListByModuleAndBusiness(queryDTO);
    }



    @Async
    public void queryAndWriteLeaseTable(ReportLeaseTableQueryDTO queryDTO, String fileName) {
        log.info("查询要导出的数据 开始");
        long l1 = System.currentTimeMillis();
//        List<ReportLeaseTableVO> leaseTableVOS = reportCommonMapper.selectLeaseTable(queryDTO);
        log.info("生成租赁大表excel 创建临时表 开始 filename:{}", fileName);
        reportCommonMapper.createTempTableLeaseTable();
        log.info("生成租赁大表excel 创建临时表 完成 filename:{}", fileName);
        log.info("生成租赁大表excel 写入id临时表 开始 filename:{}", fileName);
        reportCommonMapper.fillTempTableLeaseTable(queryDTO);
        log.info("生成租赁大表excel 写入id临时表 完成 filename:{}", fileName);
        log.info("生成租赁大表excel 查询报表数据 开始 filename:{}", fileName);
        List<ReportLeaseTableVO> leaseTableVOS = reportCommonMapper.selectLeaseTableFromTempTable(queryDTO);
        log.info("生成租赁大表excel 查询报表数据 完成 filename:{}", fileName);


//        List<ReportLeaseTableVO> leaseTableVOS = Lists.newArrayList();
//        ReportLeaseTableVO tmp = new ReportLeaseTableVO();
//        tmp.setContractCode("1");
//        tmp.setClientCode("2");
//        tmp.setClientName("3");
//        tmp.setContractStatus("4");
//        tmp.setLeaseType("5");
//        tmp.setDepreciationReservesBalance(new BigDecimal(6));
//        tmp.setContractId(7L);
//        tmp.setFinancialContractStatus("8");
//        tmp.setLeaseDateStart("9");
//        tmp.setLeaseDateEnd("10");
//        tmp.setLeaseRevenueBalance(new BigDecimal(11));
//        tmp.setLesseeMarginBalance(new BigDecimal(12));
//        tmp.setOrgId("13");
//        tmp.setOrgName("14");
//        tmp.setOverdueEarnings(new BigDecimal(15));
//        tmp.setReceivableCommissionBalance(new BigDecimal(15));
//        tmp.setReceivableDownpaymentBalance(new BigDecimal(15));
//        tmp.setReceivableInsuranceBalance(new BigDecimal(15));
//        tmp.setReceivableOtherincomeBalance(new BigDecimal(15));
//        tmp.setReceivableOutputtaxBaseBalance(new BigDecimal(15));
//        tmp.setReceivableOuttaxBalance(new BigDecimal(15));
//        tmp.setReceivableRebateBalance(new BigDecimal(15));
//        tmp.setReceivableRentBalance(new BigDecimal(15));
//        tmp.setReceivableResidualValueBalance(new BigDecimal(15));
//        tmp.setRentalIncomeAfterTotal(new BigDecimal(15));
//        tmp.setSystemCode("16");
//        tmp.setTaAmount(new BigDecimal(15));
//        tmp.setUnrealizedRevenueBalance(new BigDecimal(15));
//        tmp.setVoucherId("17");
//        leaseTableVOS.add(tmp);

        List<ReportLeaseTableExcelVO> excelList = BeanUtil.copyToList(leaseTableVOS, ReportLeaseTableExcelVO.class);
        long l2 = System.currentTimeMillis();
        log.info("查询要导出的数据 结束，用时{} s", (l2-l1)/1000);
        log.info("导出Excel数据 开始");
        ExcelWriter writer = ExcelUtil.getWriter(getFilePath()+fileName);
//        writer.writeHeadRow(LEASE_TABLE_HEAD);
//        writer.autoSizeColumnAll();
        writer.addHeaderAlias("queryDate", "日期");
        writer.addHeaderAlias("orgName", "签约主体");
        writer.addHeaderAlias("contractCode", "合同编号");
        writer.addHeaderAlias("clientName", "客户名称");
        writer.addHeaderAlias("systemCode", "业务系统");
        writer.addHeaderAlias("contractStatus", "合同状态");
        writer.addHeaderAlias("financialContractStatus", "财务合同状态");
        writer.addHeaderAlias("leaseDateStart", "会计起租日");
        writer.addHeaderAlias("leaseDateEnd", "合同约定到期日");
        writer.addHeaderAlias("leaseType", "租赁类型");
        writer.addHeaderAlias("receivableRentBalance", "应收租金");
        writer.addHeaderAlias("receivableDownpaymentBalance", "应收首付款");
        writer.addHeaderAlias("receivableResidualValueBalance", "应收期末残值");
        writer.addHeaderAlias("receivableCommissionBalance", "应收手续费");
        writer.addHeaderAlias("receivableInsuranceBalance", "应收保险费");
        writer.addHeaderAlias("receivableOtherincomeBalance", "应收其他收入");
        writer.addHeaderAlias("receivableRebateBalance", "应收返利");
        writer.addHeaderAlias("receivableOuttaxBalance", "应收销项税");
        writer.addHeaderAlias("receivableOutputtaxBaseBalance", "应收销项税-本金");
        writer.addHeaderAlias("receivableTotalBalance", "应收融资租赁款总额");

        writer.addHeaderAlias("receivableServiceFeeBalance", "未实现收益-咨询服务费分摊");
        writer.addHeaderAlias("rentalIncomeAfterTotal", "未实现收益-不含服务费");
        writer.addHeaderAlias("unrealizedRevenueBalance", "未实现收益-总");

        writer.addHeaderAlias("leaseRevenueBalance", "应收融资租赁款余额");
        writer.addHeaderAlias("depreciationReservesBalance", "减值准备余额");
        writer.addHeaderAlias("receivableNetBalance", "应收融资租赁款净值");
        writer.addHeaderAlias("lesseeMarginBalance", "承租人保证金");
        writer.addHeaderAlias("taAmount", "TA重分类");
        writer.addHeaderAlias("overdueEarnings", "逾期收益");

        writer.autoSizeColumnAll();
        writer.write(excelList, true);
        writer.close();
        long l3 = System.currentTimeMillis();
        log.info("导出Excel数据 结束，用时{} s", (l3-l2)/1000);
    }


    public static int monthBetween(LocalDateTime dateTime1, LocalDateTime dateTime2){
        //年差
        int years = dateTime1.getYear() - dateTime2.getYear();

        //月差
        int months = years * 12 + (dateTime1.getMonthValue() - dateTime2.getMonthValue());

        return months;
    }

    public static boolean isSameMonth(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        LocalDate date1 = dateTime1.toLocalDate();
        LocalDate date2 = dateTime2.toLocalDate();
        return date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth();
    }

    public static boolean isEndOfMonth(LocalDateTime dateTime) {
        return dateTime.getDayOfMonth() == dateTime.toLocalDate().lengthOfMonth();
    }

    private static <T> IPage<T> generateIPage(int pageNum, int pageSize, List<T> list) {
        IPage<T> page = new Page<>(pageNum, pageSize);
        // 当前页第一条数据在List中的位置
        int start = (int)((page.getCurrent() - 1) * page.getSize());
        // 当前页最后一条数据在List中的位置
        int end = (int)((start + page.getSize()) > list.size() ? list.size() : (page.getSize() * page.getCurrent()));
        page.setRecords(new ArrayList<>());
        page.setTotal(list.size());
        if (page.getSize()*(page.getCurrent()-1) <= page.getTotal()) {
            // 分隔列表 当前页存在数据时 设置
            page.setRecords(list.subList(start, end));
        }
        return page;
    }

    private static <T> IPage<T> getIPage(int pageNum, int pageSize, List<T> list, long totalSize) {
        IPage<T> page = new Page<>(pageNum, pageSize);
        page.setRecords(list);
        page.setTotal(totalSize);
        return page;
    }
}

