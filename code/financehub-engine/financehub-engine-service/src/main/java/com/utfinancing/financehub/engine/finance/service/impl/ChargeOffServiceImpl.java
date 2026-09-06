package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.dw.entity.DwChargeOffSummaryEntity;
import com.utfinancing.financehub.engine.dw.mapper.DwChargeOffSummaryMapper;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.ChargeOffSummaryReportEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceLatestEntity;
import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ChargeOffSummaryReportVO;
import com.utfinancing.financehub.engine.finance.model.vo.ChargeOffVO;
import com.utfinancing.financehub.engine.finance.entity.ChargeOffEntity;
import com.utfinancing.financehub.engine.finance.mapper.ChargeOffMapper;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.hthx.common.page.PageResult;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.utils.PeriodCodeUtil;
import com.utfinancing.financehub.engine.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-02-27
 * @Description :  ChargeOff服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ChargeOffServiceImpl extends ServiceImpl<ChargeOffMapper, ChargeOffEntity> implements IChargeOffService {

    private final ChargeOffMapper chargeOffMapper;
    private final DwChargeOffSummaryMapper dwChargeOffSummaryMapper;
    private final IOrgCompanyService iOrgCompanyService;

    @Value("${approve.url.chargeOff-url:null}")
    private String approveUrl;

    private final IApproveService iApproveService;

    private final IContractBalanceLatestService iContractBalanceLatestService;

    @Value("${file.storage.basicpath.windows:null}")
    private String basicPathWindows;

    @Value("${file.storage.basicpath.linux:null}")
    private String basicPathLinux;

    private final IFileRecordService fileRecordService;

    @Value("${service.parth:null}")
    private String servicePath;

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    private final IChargeOffSummaryReportService iChargeOffSummaryReportService;

    @Override
    public Long saveChargeOff(ChargeOffDTO dto) {
        ChargeOffEntity entity = BeanUtil.copyProperties(dto, ChargeOffEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateChargeOff(Long id, ChargeOffDTO dto) {
        ChargeOffEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ChargeOffDTO getChargeOffDTOById(Long id) {
        ChargeOffEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ChargeOffDTO.class);
    }

    @Override
    public IPage<ChargeOffVO> selectPage(ChargeOffQueryDTO queryDTO) {
        LambdaQueryWrapper<ChargeOffEntity> queryWrapper = getQueryWrapper(queryDTO);
        //这里注入查询条件
        IPage<ChargeOffEntity> entityIPage = chargeOffMapper.selectPage(new Page<ChargeOffEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ChargeOffVO.class);
    }

    @Override
    public Boolean importTemplate(MultipartFile file, String type) {
        //0:新增，1：修改
        try {
            List<ChargeOffDTO> chargeOffDTOList = Lists.newArrayList();
            if ("0".equals(type)) {
                ExcelUtil<ChargeOffAddExcelDTO> util = new ExcelUtil<ChargeOffAddExcelDTO>(ChargeOffAddExcelDTO.class);
                List<ChargeOffAddExcelDTO> addChargeOffExcelList = util.importExcel(file.getInputStream());
                chargeOffDTOList = BeanUtil.copyToList(addChargeOffExcelList, ChargeOffDTO.class);
            } else if ("1".equals(type)) {
                ExcelUtil<ChargeOffModifyExcelDTO> util = new ExcelUtil<ChargeOffModifyExcelDTO>(ChargeOffModifyExcelDTO.class);
                List<ChargeOffModifyExcelDTO> modifyChargeOffExcelList = util.importExcel(file.getInputStream());
                chargeOffDTOList = BeanUtil.copyToList(modifyChargeOffExcelList, ChargeOffDTO.class);
            }
            checkData(chargeOffDTOList, type);
            if ("0".equals(type)) {
                List<ChargeOffEntity> saveChargeOffList = BeanUtil.copyToList(chargeOffDTOList, ChargeOffEntity.class);
                saveChargeOffList.stream().forEach(v -> {
                    v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
                });
                this.saveBatch(saveChargeOffList);
            } else if ("1".equals(type)) {
                chargeOffDTOList.stream().forEach(v -> {
                    // 拨备转回金额按合同编号+签约主体+核销状态+核销时间+拨备转回年份 更新数据，财务核销敞口、税务核销日期、税务核销金额按合同编号+签约主体+核销状态+核销时间
                    List<ChargeOffEntity> chargeOffEntityList = this.list(Wrappers.<ChargeOffEntity>lambdaQuery()
                            .eq(ChargeOffEntity::getContractCode, v.getContractCode())
                            .eq(ChargeOffEntity::getOrgId, v.getOrgId()).eq(ChargeOffEntity::getProvisionReversalYear, v.getProvisionReversalYear())
                            .eq(ChargeOffEntity::getVerificationDate, v.getVerificationDate())
                            .eq(ChargeOffEntity::getVerificationStatus, v.getVerificationStatus()));
                    if (CollectionUtils.isNotEmpty(chargeOffEntityList)) {
                        chargeOffEntityList.stream().forEach(s -> {
                            s.setProvisionReversalAmount(v.getProvisionReversalAmount());
                            if (v.getProvisionReversalYear().equals(s.getProvisionReversalYear())) {
                                s.setFinancialExpenseAmount(v.getFinancialExpenseAmount());
                                s.setTaxVerificationAmount(v.getTaxVerificationAmount());
                                s.setTaxVerificationDate(v.getTaxVerificationDate());
                            }
                        });
                    }
                    this.updateBatchById(chargeOffEntityList);
                });
            }
        } catch (Exception exception) {
            throw new ServiceException("导入文件失败，失败原因：" + exception.getMessage());
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteByIds(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据删除");
        }
        List<ChargeOffEntity> chargeOffEntityList = this.listByIds(idList);
        chargeOffEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已录入的才可以删除");
            }
        });
        return this.removeBatchByIds(idList);
    }

    @Override
    public Boolean submit(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据提交");
        }
        List<ChargeOffEntity> chargeOffEntityList = this.listByIds(idList);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        chargeOffEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已录入的才可以提交");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.CHARGEOFF.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);
        });
        //发送审核
        Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        chargeOffEntityList.stream().forEach(v -> {
            if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
        });
        return this.updateBatchById(chargeOffEntityList);
    }

    @Override
    public Boolean withdraw(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据撤回");
        }
        List<ChargeOffEntity> chargeOffEntityList = this.listByIds(idList);
        chargeOffEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(chargeOffEntityList.stream().map(ChargeOffEntity::getProcessInstanceId).collect(Collectors.toList()));
        return this.updateBatchById(chargeOffEntityList);
    }

    @Override
    public Boolean updateProcessStatus(CommonApproveDTO commonApproveDTO) {
        if (StringUtils.isEmpty(commonApproveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        ChargeOffEntity chargeOffEntity = this.getById(commonApproveDTO.getDocumentId());
        if (null == chargeOffEntity) {
            throw new ServiceException("ChargeOff数据不存在");
        }
        String processStatus = chargeOffEntity.getProcessStatus();
        if (ProcessStatusEnum.REVIEWED.getCode().equals(commonApproveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REVIEWED.getCode();
        } else if (ProcessStatusEnum.REJECTED.getCode().equals(commonApproveDTO.getDocumentStatus())) {
            processStatus = ProcessStatusEnum.REJECTED.getCode();
        }
        chargeOffEntity.setProcessStatus(processStatus);
        return this.updateById(chargeOffEntity);
    }

    /**
     * @description:ChargeOff汇总-列表查询
     **/
    @Override
    public IPage<ChargeOffVO> summaryPage(ChargeOffQueryDTO queryDTO) {
        IPage<ChargeOffVO> querySummaryPageData = new Page<>();
        // 核销开始时间
        if (ObjectUtil.isNotNull(queryDTO.getStartVerificationDate()) && ObjectUtil.isNotNull(queryDTO.getPeriodCode())) {
            // 获取查询期间月份的最后一天
            LocalDate lastDayOfMonth = PeriodCodeUtil.parseLastDayOfMonth(queryDTO.getPeriodCode());
            Date lastDayOfPeriodDate  = PeriodCodeUtil.LocalDateToDate(lastDayOfMonth);
            if (lastDayOfPeriodDate.compareTo(queryDTO.getStartVerificationDate()) < 0) {
                throw new ServiceException("会计期间应该大于等于核销开始日期！");
            }
        }
        if (ObjectUtil.isNotNull(queryDTO.getPeriodCode())) {
            // 获取查询期间月份的最后一天
            LocalDate periodLocalDate = PeriodCodeUtil.parseLastDayOfMonth(queryDTO.getPeriodCode());
            // 核销结束时间
            queryDTO.setEndVerificationDate(PeriodCodeUtil.LocalDateToDate(periodLocalDate));
        }
        // 上个月会计期间
        queryDTO.setLastPeriodCode(PeriodCodeUtil.getLastMonthPeriodCode(queryDTO.getPeriodCode()));
        //  根据期间是属于历史期间还是当前期间查询数据，历史期间查询历史汇总表，否则查询当前数据
        int currentPeriodCode = PeriodCodeUtil.periodCodeByDate(new Date());
        // 历史期间查询固化数据
        if (queryDTO.getPeriodCode().compareTo(currentPeriodCode) < 0) {
            ChargeOffSummaryReportQueryDTO reportQueryDTO = BeanUtil.copyProperties(queryDTO, ChargeOffSummaryReportQueryDTO.class);
            // 查询固化数据
            IPage<ChargeOffSummaryReportVO> summaryReportVOIPage = iChargeOffSummaryReportService.selectPage(reportQueryDTO);
            BeanUtil.copyProperties(summaryReportVOIPage, querySummaryPageData);
            if (summaryReportVOIPage!=null && CollectionUtils.isNotEmpty(summaryReportVOIPage.getRecords())) {
                querySummaryPageData.setRecords(BeanUtil.copyToList(summaryReportVOIPage.getRecords(),ChargeOffVO.class));
            }else{
                // 实时查询
                querySummaryPageData = queryPageByPeriodCode(queryDTO);
            }
        } else {
            // 实时查询
            querySummaryPageData = queryPageByPeriodCode(queryDTO);
        }
        return querySummaryPageData;
    }

    /**
     * @description:ChargeOff汇总-列表-查询详情
     **/
    @Override
    public IPage<ChargeOffVO> summaryDetailPage(ChargeOffDetailQueryDTO queryDTO) {
        log.info("====>>summaryDetailPage==00==>>queryDTO:{}",queryDTO);
        IPage<ChargeOffVO> resultPage = new Page<>();
        Page page = new Page(queryDTO.getPageNum(), queryDTO.getPageSize());
        if (ObjectUtil.isNull(queryDTO.getPeriodCode())) {
            queryDTO.setPeriodCode(PeriodCodeUtil.periodCodeByDate(new Date()));
        }
        queryDTO.setLastPeriodCode(PeriodCodeUtil.getLastMonthPeriodCode(queryDTO.getPeriodCode()));
        int currentPeriodCode = PeriodCodeUtil.periodCodeByDate(new Date());
        log.info("====>>summaryDetailPage==01==>>queryDTO:{},currentPeriodCode:{}",queryDTO,currentPeriodCode);
        if (queryDTO.getPeriodCode().compareTo(currentPeriodCode) < 0) {
            ChargeOffSummaryReportQueryDTO reportQueryDTO = BeanUtil.copyProperties(queryDTO, ChargeOffSummaryReportQueryDTO.class);
            IPage<ChargeOffSummaryReportVO> summaryReportVOIPage = iChargeOffSummaryReportService.summaryDetailPage(reportQueryDTO);
            BeanUtil.copyProperties(summaryReportVOIPage, resultPage);
            log.info("====>>summaryDetailPage==03==>>summaryReportVOIPage:{}", summaryReportVOIPage);
            log.info("====>>summaryDetailPage==04==>>getRecords:{}", summaryReportVOIPage.getRecords());
            if (summaryReportVOIPage!=null && CollectionUtils.isNotEmpty(summaryReportVOIPage.getRecords())) {
                resultPage.setRecords(BeanUtil.copyToList(summaryReportVOIPage.getRecords(),ChargeOffVO.class));
                return resultPage;
            }else{
                log.info("====>>summaryDetailPage==05==>>queryDTO:{}", queryDTO);
                resultPage = chargeOffMapper.summaryDetailPage(page, queryDTO);
                log.info("====>>summaryDetailPage==06==>>resultPage:{}", resultPage);
                resultPage.getRecords().stream().forEach(v -> {
                    v.setVerificationDate(queryDTO.getVerificationDate());
                    v.setVerificationStatus(queryDTO.getVerificationStatus());
                    v.setPeriodCode(queryDTO.getPeriodCode());
                });
            }
        } else {
            log.info("====>>summaryDetailPage==07==>>queryDTO:{}", queryDTO);
            resultPage = chargeOffMapper.summaryDetailPage(page, queryDTO);
            log.info("====>>summaryDetailPage==08==>>resultPage:{}", resultPage);
            resultPage.getRecords().stream().forEach(v -> {
                v.setVerificationDate(queryDTO.getVerificationDate());
                v.setVerificationStatus(queryDTO.getVerificationStatus());
                v.setPeriodCode(queryDTO.getPeriodCode());
            });
        }
        log.info("====>>summaryDetailPage==100==>>resultPage:{}", resultPage);
        return resultPage;
    }

    /**
     * @description:ChargeOff汇总-导出
     **/
    @Override
    public Map<String, String> summaryList(ChargeOffQueryDTO queryDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fileName = "charge off汇总_" + LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss") + ".xlsx";
        String filePath = getFilePath();
        FileRecordEntity record = new FileRecordEntity();
        record.setModuleName(ModuleEnum.CHARGE_OFF_SUMMARY.getCode());
        record.setBusinessScene(BusinessSceneEnum.CHARGE_OFF_SUMMARY.getCode());
        record.setFileLocation(filePath + fileName);
        record.setFileName(fileName);
        record.setExecuteStatus(CheckExecuteStatusEnum.INPROGRESS.getCode());
        record.setFileUploadBy(String.valueOf(SecurityUtils.getUserId()));
        fileRecordService.save(record);
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            // 异步执行的任务
            queryAndWriteSpecialTable(queryDTO, fileName);
        }, asyncTaskExecutor).thenRun(() -> {
            FileRecordEntity tmp = new FileRecordEntity();
            tmp.setId(record.getId());
            tmp.setExecuteStatus(CheckExecuteStatusEnum.FINISH.getCode());
            tmp.setFileUploadTime(LocalDateTime.now());
            fileRecordService.updateById(tmp);
        });
        Map<String, String> map = Maps.newLinkedHashMap();
        map.put("fileName", fileName);
        map.put("location", filePath + fileName);
        map.put("servicePath", servicePath);
        return map;
    }

    @Override
    public void DwChargeoffSolidified(ChargeOffQueryDTO queryDTO) {

        List<DwChargeOffSummaryEntity> result = Lists.newArrayList();
        if (ObjectUtil.isNotNull(queryDTO.getPeriodCode())) {
            //获取查询期间月份的最后一天
            LocalDate periodLocalDate = PeriodCodeUtil.parseLastDayOfMonth(queryDTO.getPeriodCode());
            Date periodDate = PeriodCodeUtil.LocalDateToDate(periodLocalDate);
            queryDTO.setEndVerificationDate(periodDate);
        }
        queryDTO.setLastPeriodCode(PeriodCodeUtil.getLastMonthPeriodCode(queryDTO.getPeriodCode()));
        log.info("charge off 开始查询时间：" + LocalDateTime.now());

        //根据期间是属于历史期间还是当前期间查询数据，历史期间查询历史汇总表，否则查询当前数据
        int currentPeriodCode = PeriodCodeUtil.periodCodeByDate(new Date());
        List<ChargeOffVO> chargeOffVOList = new ArrayList<>();
        if (queryDTO.getPeriodCode().compareTo(currentPeriodCode) < 0) {
            chargeOffVOList = chargeOffMapper.selectChargeOffSummaryReport(queryDTO);

        } else {
            chargeOffVOList = chargeOffMapper.exportForCurMonth(queryDTO);
        }
        log.info("charge off 查询结束时间：" + LocalDateTime.now());
//        List<ChargeOffExcelDTO> chargeOffExcelDTOList = BeanUtil.copyToList(chargeOffVOList, ChargeOffExcelDTO.class);
        //按照年份分组
        Map<String, Map<String, List<ChargeOffVO>>> yearMap = chargeOffVOList.stream().collect(
                Collectors.groupingBy(v -> v.getContractCode() + "-" + v.getOrgId() + "-" + v.getClientCode() + "-"
                                + v.getVerificationStatus() + "-" + DateUtil.format(v.getVerificationDate(), "yyyy-MM-dd"),
                        Collectors.groupingBy(ChargeOffVO::getProvisionReversalYear)));
        for (Map.Entry<String, Map<String, List<ChargeOffVO>>> entry : yearMap.entrySet()) {
            Map<String, List<ChargeOffVO>> yearGroup = entry.getValue();
            DwChargeOffSummaryEntity v = null;
            //获取当前年份之前十年的数据
            int currentYear = LocalDateTime.now().getYear();
            BigDecimal totalAmount = BigDecimal.ZERO;
            int tenYear = currentYear - 10;
            BigDecimal tenTotalAmount = BigDecimal.ZERO;
            for (Map.Entry<String, List<ChargeOffVO>> yearEntry : yearGroup.entrySet()) {
                if (null == v) {
                    ChargeOffVO chargeOffVO = yearGroup.get(yearEntry.getKey()).get(0);
                    v = BeanUtil.copyProperties(chargeOffVO, DwChargeOffSummaryEntity.class);
                    v.setVerificationDate(DateUtil.format(chargeOffVO.getVerificationDate(), "yyyy-MM-dd"));
                    v.setTaxVerificationDate(DateUtil.format(chargeOffVO.getTaxVerificationDate(), "yyyy-MM-dd"));
                }
                int year = Integer.parseInt(yearEntry.getKey());
                BigDecimal currentAmount = getProvisionReversalAmount(yearEntry.getValue());
                if (year <= tenYear) {
                    tenTotalAmount = tenTotalAmount.add(currentAmount);
                }
                totalAmount = totalAmount.add(currentAmount);
            }
            //全部金额之和
            v.setId(IdWorker.getId());
            v.setPeriodCode(queryDTO.getPeriodCode());
            v.setProvisionReversalAmountTotal(totalAmount);
            for (int i = 0; i <= 10; i++) {
                String year = (currentYear - i) + "";
                if (!yearGroup.containsKey(year)) {
                    continue;
                }
                BigDecimal amount = getProvisionReversalAmount(yearGroup.get(year + ""));
                if (i == 0) {
                    v.setProvisionReversalAmount0(amount);
                } else if (i == 1) {
                    v.setProvisionReversalAmount1(amount);
                } else if (i == 2) {
                    v.setProvisionReversalAmount2(amount);
                } else if (i == 3) {
                    v.setProvisionReversalAmount3(amount);
                } else if (i == 4) {
                    v.setProvisionReversalAmount4(amount);
                } else if (i == 5) {
                    v.setProvisionReversalAmount5(amount);
                } else if (i == 6) {
                    v.setProvisionReversalAmount6(amount);
                } else if (i == 7) {
                    v.setProvisionReversalAmount7(amount);
                } else if (i == 8) {
                    v.setProvisionReversalAmount8(amount);
                } else if (i == 9) {
                    v.setProvisionReversalAmount9(amount);
                } else {
                    //十年之前的金额
                    v.setProvisionReversalAmount10(tenTotalAmount);
                }
            }
            result.add(v);
        }
        LambdaQueryWrapper<DwChargeOffSummaryEntity> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(DwChargeOffSummaryEntity::getPeriodCode, queryDTO.getPeriodCode());
        dwChargeOffSummaryMapper.delete(deleteWrapper);
        batchInsertWithChunking(result);
    }

    private void batchInsertWithChunking(List<DwChargeOffSummaryEntity> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        int batchSize = 1000; // 每批处理1000条记录
        int totalSize = dataList.size();
        for (int i = 0; i < totalSize; i += batchSize) {
            int endIndex = Math.min(i + batchSize, totalSize);
            List<DwChargeOffSummaryEntity> batch = dataList.subList(i, endIndex);
            // 批量插入
            dwChargeOffSummaryMapper.insertBatch(batch);
            log.info("已插入 {} / {} 条记录", endIndex, totalSize);
        }
    }

    private String getFilePath() {
        String osName = System.getProperties().getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            return basicPathWindows + "chargeOff" + File.separator;
        } else if (osName.toLowerCase().contains("linux") || osName.toLowerCase().contains("unix")) {
            return basicPathLinux + "chargeOff" + File.separator;
        }
        return com.utfinancing.financehub.common.core.utils.StringUtils.EMPTY;
    }

    /**
     * @description:ChargeOff汇总-导出
     **/
    public void queryAndWriteSpecialTable(ChargeOffQueryDTO queryDTO, String fileName) {
        List<ChargeOffExcelDTO> resultExcelDTO = Lists.newArrayList();
        if (ObjectUtil.isNotNull(queryDTO.getPeriodCode())) {
            // 获取查询期间月份的最后一天
            LocalDate periodLocalDate = PeriodCodeUtil.parseLastDayOfMonth(queryDTO.getPeriodCode());
            // 核销结束时间
            queryDTO.setEndVerificationDate(PeriodCodeUtil.LocalDateToDate(periodLocalDate));
        }
        // 上个月会计期间
        queryDTO.setLastPeriodCode(PeriodCodeUtil.getLastMonthPeriodCode(queryDTO.getPeriodCode()));
        //  根据期间是属于历史期间还是当前期间查询数据，历史期间查询历史汇总表，否则查询当前数据
        int currentPeriodCode = PeriodCodeUtil.periodCodeByDate(new Date());
        // 查询结果
        List<ChargeOffVO> chargeOffVOList = new ArrayList<>();
        if (queryDTO.getPeriodCode().compareTo(currentPeriodCode) < 0) {
            // 查询固化数据
            chargeOffVOList = chargeOffMapper.selectChargeOffSummaryReport(queryDTO);
        }
        if(CollectionUtils.isEmpty(chargeOffVOList)){
            // 实时查询
            chargeOffVOList = chargeOffMapper.exportForCurMonth(queryDTO);
        }

        log.info("charge off 查询结束时间：" + LocalDate.now());
//        List<ChargeOffExcelDTO> chargeOffExcelDTOList = BeanUtil.copyToList(chargeOffVOList, ChargeOffExcelDTO.class);
        //按照年份分组
        Map<String, Map<String, List<ChargeOffVO>>> yearMap = chargeOffVOList.stream().collect(
                Collectors.groupingBy(v -> v.getContractCode() + "-" + v.getOrgId() + "-" + v.getClientCode() + "-"
                                + v.getVerificationStatus() + "-" + DateUtil.format(v.getVerificationDate(), "yyyy-MM-dd"),
                        Collectors.groupingBy(v -> v.getProvisionReversalYear())));
        for (Map.Entry<String, Map<String, List<ChargeOffVO>>> entry : yearMap.entrySet()) {
            Map<String, List<ChargeOffVO>> yearGroup = entry.getValue();
            ChargeOffExcelDTO v = null;
            //获取当前年份之前十年的数据
            int currentYear = LocalDateTime.now().getYear();
            BigDecimal totalAmount = BigDecimal.ZERO;
            int tenYear = currentYear - 10;
            BigDecimal tenTotalAmount = BigDecimal.ZERO;
            for (Map.Entry<String, List<ChargeOffVO>> yearEntry : yearGroup.entrySet()) {
                if (null == v) {
                    ChargeOffVO chargeOffVO = yearGroup.get(yearEntry.getKey()).get(0);
                    v = BeanUtil.copyProperties(chargeOffVO, ChargeOffExcelDTO.class);
                }
                int year = Integer.parseInt(yearEntry.getKey());
                BigDecimal currentAmount = getProvisionReversalAmount(yearEntry.getValue());
                if (year <= tenYear) {
                    tenTotalAmount = tenTotalAmount.add(currentAmount);
                }
                totalAmount = totalAmount.add(currentAmount);
            }
            //全部金额之和
            v.setProvisionReversalAmountTotal(totalAmount);
            if (null != v.getVerificationDate()) {
                v.setVerificationDateString(DateUtil.format(v.getVerificationDate(), "yyyy-MM-dd"));
            }
            if (null != v.getTaxVerificationDate()) {
                v.setTaxVerificationDateString(DateUtil.format(v.getTaxVerificationDate(), "yyyy-MM-dd"));
            }
            for (int i = 0; i <= 10; i++) {
                String year = (currentYear - i) + "";
                if (!yearGroup.containsKey(year)) {
                    continue;
                }
                BigDecimal amount = getProvisionReversalAmount(yearGroup.get(year + ""));
                if (i == 0) {
                    v.setProvisionReversalAmount0(amount);
                } else if (i == 1) {
                    v.setProvisionReversalAmount1(amount);
                } else if (i == 2) {
                    v.setProvisionReversalAmount2(amount);
                } else if (i == 3) {
                    v.setProvisionReversalAmount3(amount);
                } else if (i == 4) {
                    v.setProvisionReversalAmount4(amount);
                } else if (i == 5) {
                    v.setProvisionReversalAmount5(amount);
                } else if (i == 6) {
                    v.setProvisionReversalAmount6(amount);
                } else if (i == 7) {
                    v.setProvisionReversalAmount7(amount);
                } else if (i == 8) {
                    v.setProvisionReversalAmount8(amount);
                } else if (i == 9) {
                    v.setProvisionReversalAmount9(amount);
                } else {
                    //十年之前的金额
                    v.setProvisionReversalAmount10(tenTotalAmount);
                }
            }
            resultExcelDTO.add(v);
        }
        long l2 = System.currentTimeMillis();
        log.info("导出Excel数据 开始");
        Integer year = DateUtil.thisYear();
        if (queryDTO.getPeriodCode() != null) {
            year = queryDTO.getPeriodCode() / 100;
        }
        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(getFilePath() + fileName);
        writer.addHeaderAlias("contractCode", "合同编号");
        writer.addHeaderAlias("orgName", "签约主体");
        writer.addHeaderAlias("verificationStatus", "核销状态");
        writer.addHeaderAlias("clientName", "客户名称");
        writer.addHeaderAlias("verificationDateString", "核销时间");
        writer.addHeaderAlias("financialExpenseAmount", "财务核销敞口");
        writer.addHeaderAlias("provisionReversalAmount10", (year - 10) + "年以前拨备转回金额");
        writer.addHeaderAlias("provisionReversalAmount9", (year - 9) + "年拨备转回金额");
        writer.addHeaderAlias("provisionReversalAmount8", (year - 8) + "年拨备转回金额");
        writer.addHeaderAlias("provisionReversalAmount7", (year - 7) + "年拨备转回金额");
        writer.addHeaderAlias("provisionReversalAmount6", (year - 6) + "年拨备转回金额");
        writer.addHeaderAlias("provisionReversalAmount5", (year - 5) + "年拨备转回金额");
        writer.addHeaderAlias("provisionReversalAmount4", (year - 4) + "年拨备转回金额");
        writer.addHeaderAlias("provisionReversalAmount3", (year - 3) + "年拨备转回金额");
        writer.addHeaderAlias("provisionReversalAmount2", (year - 2) + "年拨备转回金额");
        writer.addHeaderAlias("provisionReversalAmount1", (year - 1) + "年拨备转回金额");
        writer.addHeaderAlias("provisionReversalAmount0", year + "年拨备转回金额");
        writer.addHeaderAlias("provisionReversalAmountTotal", "拨备转回金额");
        writer.addHeaderAlias("badDebtWriteOffBalance", "坏账核销余额");
        writer.addHeaderAlias("taxVerificationDateString", "税务核销日期");
        writer.addHeaderAlias("taxVerificationAmount", "税务核销金额");
        writer.autoSizeColumnAll();
        writer.setOnlyAlias(true);
        writer.setColumnWidth(-1, 20);
        writer.write(resultExcelDTO, true);
//        setCellStyle(writer,2,3,IndexedColors.YELLOW.getIndex());
//        int rowSize = writer.getColumnCount();
//        for (int i=1; i< resultExcelDTO.size(); i++) {
//            Row row = writer.getOrCreateRow(i);
//            for (int j=0;j<rowSize; j++) {
//                setCellStyle(writer,j,i,IndexedColors.YELLOW.getIndex());
//                break;
//            }
//        }
        writer.close();
        long l3 = System.currentTimeMillis();
        log.info("导出Excel数据 结束，用时{} s", (l3 - l2) / 1000);
    }

    private static void setCellStyle(ExcelWriter writer, int x, int y, short index) {
        CellStyle cellStyle = writer.createCellStyle(x, y);
        cellStyle.setFillForegroundColor(index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    public BigDecimal getProvisionReversalAmount(List<ChargeOffVO> chargeOffEntityList) {
        return chargeOffEntityList.stream().map(ChargeOffVO::getProvisionReversalAmount).reduce(BigDecimal.ZERO, (a, b) -> NumberUtil.add(null == a ? BigDecimal.ZERO : a, null == b ? BigDecimal.ZERO : b)).setScale(2, RoundingMode.HALF_UP);

    }

    public LambdaQueryWrapper<ChargeOffEntity> getQueryWrapper(ChargeOffQueryDTO queryDTO) {
        //合同编号	签约主体	核销状态	客户名称	核销时间	税务核销日期
        LambdaQueryWrapper<ChargeOffEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryDTO.getContractCode())) {
            queryWrapper.like(ChargeOffEntity::getContractCode, queryDTO.getContractCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getOrgId())) {
            queryWrapper.eq(ChargeOffEntity::getOrgId, queryDTO.getOrgId());
        }
        if (StringUtils.isNotEmpty(queryDTO.getOrgId())) {
            queryWrapper.eq(ChargeOffEntity::getOrgId, queryDTO.getOrgId());
        }
        if (ObjectUtil.isNotNull(queryDTO.getStartVerificationDate())) {
            queryWrapper.ge(ChargeOffEntity::getVerificationDate, queryDTO.getStartVerificationDate());
        }
        if (ObjectUtil.isNotNull(queryDTO.getEndVerificationDate())) {
            queryWrapper.le(ChargeOffEntity::getVerificationDate, queryDTO.getEndVerificationDate());
        }
        if (ObjectUtil.isNotNull(queryDTO.getStartTaxVerificationDate())) {
            queryWrapper.ge(ChargeOffEntity::getTaxVerificationDate, queryDTO.getStartTaxVerificationDate());
        }
        if (ObjectUtil.isNotNull(queryDTO.getEndTaxVerificationDate())) {
            queryWrapper.le(ChargeOffEntity::getTaxVerificationDate, queryDTO.getEndTaxVerificationDate());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getOrgIdList())) {
            queryWrapper.in(ChargeOffEntity::getOrgId, queryDTO.getOrgIdList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList())) {
            queryWrapper.in(ChargeOffEntity::getProcessStatus, queryDTO.getProcessStatusList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getVerificationStatusList())) {
            queryWrapper.in(ChargeOffEntity::getVerificationStatus, queryDTO.getVerificationStatusList());
        }
        if (StringUtils.isNotEmpty(queryDTO.getClientName())) {
            queryWrapper.like(ChargeOffEntity::getClientName, queryDTO.getClientName());
        }
        if (ObjectUtil.isNotNull(queryDTO.getId())) {
            queryWrapper.eq(ChargeOffEntity::getId, queryDTO.getId());
        }
        queryWrapper.orderByDesc(ChargeOffEntity::getCreateTime);
        return queryWrapper;
    }

    public void checkData(List<ChargeOffDTO> chargeOffDTOList, String type) {
        Map<String, String> orgIdMap = iOrgCompanyService.list().stream().collect(HashMap::new, (h, v) -> h.put(v.getOrgName(), v.getOrgId()), HashMap::putAll);
        if (CollectionUtils.isEmpty(chargeOffDTOList)) {
            throw new ServiceException("导入的数据是空的");
        }
        //获取当前年份
        String currentYear = String.valueOf(LocalDateTime.now().getYear());
        chargeOffDTOList.stream().forEach(v -> {
            if (StringUtils.isEmpty(v.getContractCode())) {
                throw new ServiceException("合同编码不可以为空");
            }
            if (StringUtils.isEmpty(v.getOrgIdName())) {
                throw new ServiceException("签约主体不可以为空");
            }
            if (StringUtils.isEmpty(v.getProvisionReversalYear())) {
                throw new ServiceException("拨备转回年份不可以为空");
            }
            if (orgIdMap.containsKey(v.getOrgIdName())) {
                v.setOrgId(orgIdMap.get(v.getOrgIdName()));
            } else {
                throw new ServiceException("签约主体在系统中不存在");
            }
            if (StringUtils.isEmpty(v.getVerificationStatus())) {
                throw new ServiceException("核销状态不可以为空");
            }
            if (ObjectUtil.isNull(v.getVerificationDate())) {
                throw new ServiceException("核销时间不可以为空");
            }
            if ("1".equals(type)) {
                if (!v.getProvisionReversalYear().equals(currentYear)) {
                    throw new ServiceException("拨备转回年份只能是当前年");
                }
            }
            if ("0".equals(type)) {
                if (StringUtils.isEmpty(v.getClientName())) {
                    throw new ServiceException("客户名称不可以为空");
                }
                //合同编码+签约主体唯一，存在则报错
                if (isExist(v.getContractCode(), v.getOrgId(), v.getProvisionReversalYear())) {
                    throw new ServiceException(String.format("合同编码：%s + 签约主体:%s + 拨备转回年份:%s,已经存在，不可导入", v.getContractCode(), v.getOrgIdName(), v.getProvisionReversalYear()));
                }
            }
        });
    }

    public Boolean isExist(String contractCode, String orgId, String provisionReversalYear) {
        return this.lambdaQuery().eq(ChargeOffEntity::getContractCode, contractCode).eq(ChargeOffEntity::getOrgId, orgId).eq(ChargeOffEntity::getProvisionReversalYear, provisionReversalYear).exists();
    }

    public void setProvisionReversalAmount(ChargeOffVO v) {
        //先查询合同编码+签约主体+客户编码+核销状态+核销开始结束时间查最新的那条拨备转回金额，没有的话再查charge off
        QueryWrapper<ContractBalanceLatestEntity> latestEntityQueryWrapper = new QueryWrapper<>();
        latestEntityQueryWrapper.lambda().eq(ContractBalanceLatestEntity::getContractCode, v.getContractCode());
        latestEntityQueryWrapper.lambda().eq(ContractBalanceLatestEntity::getOrgId, v.getOrgId());
        latestEntityQueryWrapper.lambda().ge(ContractBalanceLatestEntity::getVoucherDate, v.getVerificationDate());
        Date endVerificationDate = null;
        if (ObjectUtil.isNotNull(v.getPeriodCode())) {
            //获取查询期间月份的最后一天
            LocalDate periodLocalDate = PeriodCodeUtil.parseLastDayOfMonth(v.getPeriodCode());
            endVerificationDate = PeriodCodeUtil.LocalDateToDate(periodLocalDate);
        }
        if (ObjectUtil.isNotNull(endVerificationDate)) {
            latestEntityQueryWrapper.lambda().le(ContractBalanceLatestEntity::getVoucherDate, endVerificationDate);
        }
        if (StringUtils.isNotEmpty(v.getClientCode())) {
            latestEntityQueryWrapper.lambda().eq(ContractBalanceLatestEntity::getClientCode, v.getClientCode());
        } else {
            latestEntityQueryWrapper.lambda().isNull(ContractBalanceLatestEntity::getClientCode);
        }
        List<ContractBalanceLatestEntity> balanceLatestEntityList = iContractBalanceLatestService.list(latestEntityQueryWrapper);
        if (CollectionUtils.isNotEmpty(balanceLatestEntityList)) {
            BigDecimal provisionReversalAmount = balanceLatestEntityList.get(0).getDepreciationLossAmount();
            v.setProvisionReversalAmount(provisionReversalAmount);
        } else {
            QueryWrapper<ChargeOffEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(ChargeOffEntity::getContractCode, v.getContractCode());
            queryWrapper.lambda().eq(ChargeOffEntity::getOrgId, v.getOrgId());
            queryWrapper.lambda().eq(ChargeOffEntity::getVerificationStatus, v.getVerificationStatus());
            queryWrapper.lambda().ge(ChargeOffEntity::getVerificationDate, v.getVerificationDate());
            if (ObjectUtil.isNotNull(endVerificationDate)) {
                queryWrapper.lambda().le(ChargeOffEntity::getVerificationDate, endVerificationDate);
            }
            if (StringUtils.isNotEmpty(v.getProvisionReversalYear())) {
                queryWrapper.lambda().eq(ChargeOffEntity::getProvisionReversalYear, v.getProvisionReversalYear());
            }
            if (StringUtils.isNotEmpty(v.getClientCode())) {
                queryWrapper.lambda().eq(ChargeOffEntity::getClientCode, v.getClientCode());
            } else {
                queryWrapper.lambda().isNull(ChargeOffEntity::getClientCode);
            }
            List<ChargeOffEntity> chargeOffEntityList = this.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(chargeOffEntityList)) {
                BigDecimal provisionReversalAmount = chargeOffEntityList.stream().map(ChargeOffEntity::getProvisionReversalAmount).reduce(BigDecimal.ZERO, (a, b) -> NumberUtil.add(a, b)).setScale(2, RoundingMode.HALF_UP);
                v.setProvisionReversalAmount(provisionReversalAmount);
            }
        }
    }

    @Transactional
    @Override
    public void summaryReportByPeriodCode(int periodCode) {
        //组装数据固化charge off 汇总报表
        ChargeOffQueryDTO queryDTO = new ChargeOffQueryDTO();
        queryDTO.setPeriodCode(periodCode);
        queryDTO.setLastPeriodCode(PeriodCodeUtil.getLastMonthPeriodCode(periodCode));
        //先删除固化的数据，再新增数据
        iChargeOffSummaryReportService.remove(new LambdaQueryWrapper<ChargeOffSummaryReportEntity>().eq(ChargeOffSummaryReportEntity::getPeriodCode, periodCode));
        chargeOffMapper.summaryList(queryDTO);
    }

    @Override
    public boolean isGenerateBalanceMonth(int periodCode) {
        return chargeOffMapper.isGenerateBalanceMonth(periodCode);
    }

    /**
     * @description: 根据特定期间实时查询分页数据
     * @author: zhangli.chen
     * @date 2025/11/11 10:54
     * @param: queryDTO
     * @return IPage<ChargeOffVO>
     **/
    IPage<ChargeOffVO> queryPageByPeriodCode(ChargeOffQueryDTO queryDTO) {
        IPage<ChargeOffVO> resultPage = new Page<>();
        Page page = new Page(queryDTO.getPageNum(), queryDTO.getPageSize());
        resultPage = chargeOffMapper.summaryPage(page, queryDTO);
        // 拨备转回金额：如果charge off 中合同编码+签约主体+核销状态+核销时间一致则取该表的拨备转回金额否则取汇总的金额
        resultPage.getRecords().forEach(v -> {
            v.setPeriodCode(queryDTO.getPeriodCode());
        });
        return resultPage;
    }


}

