package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.BatchTypeEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.ContractMonthEntity;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeeDetailsNewEntity;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeeNewEntity;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeePlanNewEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsNewVO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeNewVO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.rule.service.IDataExecutionTaskService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.utils.FinhubAmountUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author : le
 * @Date : Create in 2025-11-10
 * @Description :   ServiceFeeNew控制器实现类
 * @Modified :
 */
@Api(tags = "服务费分摊表-新")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/service-fee-new")
public class ServiceFeeNewController {

    private final IServiceFeeNewService serviceFeeNewService;
    private final IServiceFeeDetailsNewService serviceFeeDetailsNewService;
    private final IServiceFeePlanNewService serviceFeePlanNewService;
    private final IServiceFeeMeasurementService serviceFeeMeasurementService;
    private final IContractMonthService contractMonthService;
    private final IDataExecutionTaskService dataExecutionTaskService;


    @ApiOperation(value = "提交")
    @PostMapping("/submit")
    public R submit(@RequestBody List<Long> ids) {
        // 1.先删除凭证
        serviceFeeNewService.batchDeleteVoucher(ids);
        serviceFeeNewService.submit(ids);
        return R.ok();
    }

    @ApiOperation(value = "撤回")
    @PostMapping("/withdraw")
    public R withdraw(@RequestBody List<Long> ids) {
        serviceFeeNewService.withdraw(ids);
        return R.ok();
    }

    @ApiOperation(value = "生成凭证")
    @PostMapping("/voucher")
    public R voucher(@RequestBody List<Long> ids) {
        serviceFeeNewService.voucher(ids, YesOrNoEnum.NO.getCode());
        return R.ok();
    }

    @ApiOperation(value = "冲销凭证")
    @PostMapping("/reversal/voucher")
    public R reversalVoucher(@RequestBody List<Long> ids) {
        serviceFeeNewService.reversalVoucher(ids);
        return R.ok();
    }


    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public R delete(@RequestBody List<Long> ids) {
        serviceFeeNewService.deleteByIds(ids);
        return R.ok();
    }

    @ApiOperation(value = "汇总分页查询")
    @PostMapping("/page")
    public R<IPage<ServiceFeeNewVO>> page(@RequestBody @Valid ServiceFeeQueryDTO queryDTO) {
        return R.ok(serviceFeeNewService.selectPage(queryDTO));
    }

    @ApiOperation(value = "测算")
    @PostMapping("/measurement")
    public R measurement(@RequestBody @Valid ServiceFeeQueryDTO queryDTO) {
        try {
            if (queryDTO.getAllocationRatio() == null || queryDTO.getAllocationRatio().compareTo(BigDecimal.ZERO) < 0) {
                return R.fail("分摊比例不能空或负数");
            }
            Long taskId = dataExecutionTaskService.checkAndCreateTask(BatchTypeEnum.ZXFWF.getCode());
            return serviceFeeMeasurementService.measurement(queryDTO, taskId);
        } catch (Exception e) {
            log.error("ServiceFeeController measurement error", e);
            return R.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "汇总分页查询-导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid ServiceFeeQueryDTO queryDTO) {
        try {
            if (CollectionUtils.isEmpty(queryDTO.getIdList())) {
                return;
            }
            if (queryDTO.getBusinessStartDate() == null) {
                queryDTO.setBusinessStartDate(queryDTO.getBusinessDate());
                queryDTO.setBusinessEndDate(queryDTO.getBusinessDate());
            }
            ServiceFeeDetailsQueryDTO serviceFeeDetailsQueryDTO = new ServiceFeeDetailsQueryDTO();
            serviceFeeDetailsQueryDTO.setServiceFeeIdList(queryDTO.getIdList());
            serviceFeeDetailsQueryDTO.setBusinessDate(queryDTO.getBusinessDate());
            serviceFeeDetailsQueryDTO.setBusinessStartDate(queryDTO.getBusinessStartDate());
            serviceFeeDetailsQueryDTO.setBusinessEndDate(DateUtil.endOfDay(DateUtil.endOfMonth(queryDTO.getBusinessEndDate())));
            List<ServiceFeeDetailsNewVO> list = serviceFeeNewService.selectExportDetailList(serviceFeeDetailsQueryDTO);
            ExcelUtil<ServiceFeeDetailsNewExcel> util = new ExcelUtil<>(ServiceFeeDetailsNewExcel.class);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("服务费分摊.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, ServiceFeeDetailsNewExcel.class), "服务费分摊");
        } catch (Exception e) {
            log.error("咨询服务费导出失败", e);
            throw new ServiceException("咨询服务费导出失败，失败原因:" + e.getMessage());
        }
    }


    @ApiOperation(value = "初始化数据")
    @PostMapping("/initData")
    public R initData(@RequestParam("initDate") String initDate) {
        try {
            LambdaQueryWrapper<ServiceFeePlanNewEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.le(ServiceFeePlanNewEntity::getPlanDate, DateUtil.parse(initDate));
            List<ServiceFeePlanNewEntity> list = serviceFeePlanNewService.list(queryWrapper);
            List<String> contractNos = list.stream().map(ServiceFeePlanNewEntity::getContractCode).distinct().collect(Collectors.toList());
            LambdaQueryWrapper<ContractMonthEntity> getMainContractCodeWrapper = new LambdaQueryWrapper<>();
//            getMainContractCodeWrapper.in(ContractMonthEntity::getContractCategory, Lists.newArrayList("1", "3"));
            getMainContractCodeWrapper.in(ContractMonthEntity::getContractCode, contractNos);
            getMainContractCodeWrapper.eq(ContractMonthEntity::getDelFlag, YesOrNoEnum.NO.getCode());
            List<ContractMonthEntity> contractMonthEntities = contractMonthService.list(getMainContractCodeWrapper);
            Map<String, ContractMonthEntity> contractMap = contractMonthEntities.stream().collect(Collectors.toMap(ContractMonthEntity::getContractCode, v -> v, (existing, replacement) -> existing));
            Map<String, List<ServiceFeePlanNewEntity>> collect = list.stream().collect(Collectors.groupingBy(v -> v.getServiceFeeNo().concat("@").concat(v.getServiceOrgId())));
            List<ServiceFeeNewEntity> serviceFeeNewEntities = new ArrayList<>();
            List<ServiceFeeDetailsNewEntity> serviceFeeDetailsNewEntities = new ArrayList<>();
            Map<String, Long> serviceFeeIdMap = new HashMap<>();

            collect.forEach((key, serviceFeePlanNewEntities) -> {
                serviceFeePlanNewEntities.sort(Comparator.comparing(ServiceFeePlanNewEntity::getPeriods));
                ServiceFeeDetailsNewEntity detailsNewEntity = null;
                BigDecimal accruedAmountTotal = BigDecimal.ZERO;
                for (ServiceFeePlanNewEntity serviceFeePlanNewEntity : serviceFeePlanNewEntities) {
                    ContractMonthEntity contractMonthEntity = contractMap.get(serviceFeePlanNewEntity.getContractCode());
                    try {
                        Long serviceFeeId = getServiceFeeId(serviceFeePlanNewEntity, serviceFeeIdMap);
                        ServiceFeeDetailsNewEntity serviceFeeDetailsNewEntity = new ServiceFeeDetailsNewEntity();
                        serviceFeeDetailsNewEntity.setBusinessDate(serviceFeePlanNewEntity.getPlanDate());
                        serviceFeeDetailsNewEntity.setServiceFeeId(serviceFeeId);
                        serviceFeeDetailsNewEntity.setServiceFeePlanId(serviceFeePlanNewEntity.getId());
                        serviceFeeDetailsNewEntity.setContractCode(serviceFeePlanNewEntity.getContractCode());
                        serviceFeeDetailsNewEntity.setOrgId(serviceFeePlanNewEntity.getOrgId());
                        serviceFeeDetailsNewEntity.setServiceOrgId(serviceFeePlanNewEntity.getServiceOrgId());
                        serviceFeeDetailsNewEntity.setServiceFeeNo(serviceFeePlanNewEntity.getServiceFeeNo());
                        serviceFeeDetailsNewEntity.setClientCode(contractMonthEntity.getClientCode());
                        serviceFeeDetailsNewEntity.setClientName(contractMonthEntity.getClientName());
                        serviceFeeDetailsNewEntity.setContractStatus(contractMonthEntity.getContractStatus());
                        serviceFeeDetailsNewEntity.setBusinessCode(contractMonthEntity.getBusinessCode());
                        serviceFeeDetailsNewEntity.setBusinessName(contractMonthEntity.getBusinessName());
                        serviceFeeDetailsNewEntity.setLeaseDateStart(contractMonthEntity.getLeaseDateStart());
                        serviceFeeDetailsNewEntity.setLeaseDateEnd(contractMonthEntity.getLeaseDateEnd());
                        serviceFeeDetailsNewEntity.setReceivedServiceFeeTaxInclude(contractMonthEntity.getReceivableServiceAmount());
                        serviceFeeDetailsNewEntity.setReceivedServiceFeeNoTax(FinhubAmountUtils.amountNoTax(contractMonthEntity.getReceivableServiceAmount()));
                        serviceFeeDetailsNewEntity.setShouldApportionmentAmountTaxInclude(serviceFeePlanNewEntity.getShouldApportionmentAmountTaxInclude());
                        serviceFeeDetailsNewEntity.setShouldApportionmentAmountNoTax(serviceFeePlanNewEntity.getShouldApportionmentAmountNoTax());
                        serviceFeeDetailsNewEntity.setPlanAmountTaxInclude(serviceFeePlanNewEntity.getPlanAmountTaxInclude());
                        serviceFeeDetailsNewEntity.setPlanAmountNoTax(serviceFeePlanNewEntity.getPlanAmountNoTax());
                        if (detailsNewEntity != null) {
                            serviceFeeDetailsNewEntity.setLastMonthShouldApportionmentAmountTaxInclude(detailsNewEntity.getShouldApportionmentAmountTaxInclude());
                            serviceFeeDetailsNewEntity.setLastMonthShouldApportionmentAmountNoTax(detailsNewEntity.getShouldApportionmentAmountNoTax());
                        }
                        serviceFeeDetailsNewEntity.setThisMonthReclassificationAdjustmentAmountTaxInclude(BigDecimal.ZERO);
                        serviceFeeDetailsNewEntity.setThisMonthReclassificationAdjustmentAmountNoTax(BigDecimal.ZERO);
                        serviceFeeDetailsNewEntity.setAccruedAmount(serviceFeePlanNewEntity.getAccruedAmount());
                        serviceFeeDetailsNewEntity.setBeforeAccruedAmount(accruedAmountTotal);
                        serviceFeeDetailsNewEntity.setAfterAccruedAmount(serviceFeePlanNewEntity.getShouldApportionmentAmountNoTax().subtract(accruedAmountTotal).subtract(serviceFeePlanNewEntity.getAccruedAmount()));
                        serviceFeeDetailsNewEntity.setCurrentPeriodPlanAmountTaxInclude(serviceFeePlanNewEntity.getPlanAmountTaxInclude());
                        serviceFeeDetailsNewEntity.setBeforeCurrentPeriodPlanAmountTaxInclude(serviceFeePlanNewEntity.getPlanAmountTotalTaxInclude().subtract(serviceFeePlanNewEntity.getPlanAmountTotalTaxInclude()));
                        serviceFeeDetailsNewEntity.setAfterCurrentPeriodPlanAmountTaxInclude(serviceFeePlanNewEntity.getShouldApportionmentAmountTaxInclude().subtract(serviceFeePlanNewEntity.getPlanAmountTotalTaxInclude()));
                        serviceFeeDetailsNewEntity.setCreateBy("system");
                        serviceFeeDetailsNewEntity.setCreateTime(LocalDateTime.now());
                        serviceFeeDetailsNewEntity.setPeriods(serviceFeePlanNewEntity.getPeriods());
                        serviceFeeDetailsNewEntity.setFinancialContractStatus(contractMonthEntity.getFinancialContractStatus());
                        serviceFeeDetailsNewEntity.setAccrualYear(CommonDateUtils.getYearValue(serviceFeePlanNewEntity.getPlanDate()));
                        serviceFeeDetailsNewEntity.setAccrualMonth(CommonDateUtils.getMonthValue(serviceFeePlanNewEntity.getPlanDate()));
                        serviceFeeDetailsNewEntity.setAccrualAmountTotalNoTax(accruedAmountTotal);
                        serviceFeeDetailsNewEntity.setAllocateAcrossPrincipals("1");
                        serviceFeeDetailsNewEntities.add(serviceFeeDetailsNewEntity);
                        accruedAmountTotal = accruedAmountTotal.add(serviceFeeDetailsNewEntity.getAccruedAmount());
                        detailsNewEntity = serviceFeeDetailsNewEntity;
                    } catch (Exception e) {
                        log.error("saveServiceFeeDetailsNew error.", e);
                        throw e;
                    }
                }
            });

            log.info("save data");
            serviceFeeDetailsNewService.saveBatch(serviceFeeDetailsNewEntities);
            List<ServiceFeeNewEntity> serviceFeeEntities = new ArrayList<>();
            serviceFeeDetailsNewEntities.stream().collect(Collectors.groupingBy(ServiceFeeDetailsNewEntity::getServiceFeeId))
                    .forEach((serviceFeeId, detailsNewEntities) -> {
                        ServiceFeeDetailsNewEntity serviceFeeDetailsNewEntity = detailsNewEntities.get(0);
                        BigDecimal total = detailsNewEntities.stream().map(ServiceFeeDetailsNewEntity::getAccruedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                        ServiceFeeNewEntity serviceFeeNewEntity = new ServiceFeeNewEntity();
                        serviceFeeNewEntity.setId(serviceFeeId);
                        serviceFeeNewEntity.setBusinessDate(serviceFeeDetailsNewEntity.getBusinessDate());
                        serviceFeeNewEntity.setOrgId(serviceFeeDetailsNewEntity.getServiceOrgId());
                        serviceFeeNewEntity.setAccruedAmount(total);
                        serviceFeeNewEntity.setProcessStatus("1");
                        serviceFeeNewEntity.setCreateBy("system");
                        serviceFeeNewEntity.setCreateTime(LocalDateTime.now());
                        serviceFeeEntities.add(serviceFeeNewEntity);
                    });

            serviceFeeNewService.saveBatch(serviceFeeEntities);
            log.info("service fee details save done");
            return R.ok();
        } catch (Exception e) {
            log.error("ServiceFeeController measurement error", e);
            return R.fail(e.getMessage());
        }
    }

    private Long getServiceFeeId(ServiceFeePlanNewEntity serviceFeePlanNewEntity, Map<String, Long> serviceFeeIdMap) {
        String key = serviceFeePlanNewEntity.getServiceOrgId().concat(DateUtil.format(serviceFeePlanNewEntity.getPlanDate(), "yyyy-MM-dd"));
        if (serviceFeeIdMap.get(key) != null) {
            return serviceFeeIdMap.get(key);
        } else {
            long id = IdWorker.getId();
            serviceFeeIdMap.put(key, id);
            return id;
        }

    }

    @ApiOperation(value = "模板下载")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<ServiceFeeImport> util = new ExcelUtil<ServiceFeeImport>(ServiceFeeImport.class);
        util.importTemplateExcel(response, "sheet1");
    }

    @ApiOperation(value = "上传")
    @PostMapping("/importData")
    public R importData(MultipartFile file) throws Exception {
        ExcelUtil<ServiceFeeImport> util = new ExcelUtil<ServiceFeeImport>(ServiceFeeImport.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<ServiceFeeImport> list = util.importExcel(inputStream);
            return serviceFeeNewService.importData(list);
        } catch (Exception e) {
            log.error("服务费分摊上传报错", e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

}



