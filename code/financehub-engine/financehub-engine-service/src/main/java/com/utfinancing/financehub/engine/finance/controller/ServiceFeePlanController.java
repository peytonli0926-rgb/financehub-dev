package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanEntity;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeePlanEntity;
import com.utfinancing.financehub.engine.finance.model.dto.AccountBalanceSheetExcelExportDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeePlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeePlanDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanService;
import com.utfinancing.financehub.engine.utils.FinhubAmountUtils;
import com.utfinancing.financehub.engine.utils.excel.ServiceFeeDynamicColumnsExcelUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Var;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.utfinancing.financehub.engine.finance.service.IServiceFeePlanService;
import springfox.documentation.spring.web.json.JsonSerializer;


/**
 * @Author : wenbin
 * @Date : Create in 2024-05-29
 * @Description :   ServiceFeePlan控制器实现类
 * @Modified :
 */
@Api(tags = "服务费计划表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/service-fee-plan")
public class ServiceFeePlanController {

    private final IServiceFeePlanService serviceFeePlanService;
    private final JsonSerializer jsonSerializer;
    private final IRepaymentPlanService repaymentPlanService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ServiceFeePlanDTO dto) {
        return R.ok(serviceFeePlanService.saveServiceFeePlan(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ServiceFeePlanDTO dto) {
        return R.ok(serviceFeePlanService.updateServiceFeePlan(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(serviceFeePlanService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ServiceFeePlanDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(serviceFeePlanService.getServiceFeePlanDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ServiceFeePlanVO>> page(@RequestBody @Valid ServiceFeePlanQueryDTO queryDTO) {
        return R.ok(serviceFeePlanService.selectPage(queryDTO));
    }

    //导出
    @ApiOperation(value = "导出")
    @PostMapping("/export")
    public void export(@RequestBody @Valid ServiceFeePlanQueryDTO queryDTO, HttpServletResponse response) {
        try {
            List<ServiceFeePlanVO> list = serviceFeePlanService.export(queryDTO);
            ExcelUtil<ServiceFeePlanExcelVo> util = new ExcelUtil<>(ServiceFeePlanExcelVo.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("应收服务费合同分摊.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, ServiceFeePlanExcelVo.class), "应收服务费合同分摊");
        } catch (UnsupportedEncodingException e) {
            log.error("减值计提导出失败", e);
            throw new ServiceException("减值计提导出失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "初始化数据")
    @PostMapping("/initServiceFeePlan")
    public void initServiceFeePlan(@RequestParam("year") Integer initYear,@RequestParam("month") Integer initMonth) {
        List<Integer> years = Lists.newArrayList(2020, 2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030, 2031, 2032);
        List<Integer> months = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        File file = new File("D:\\资料\\财务中台\\咨询服务费_期初数据模板_2025.10-小数调整版_Wenyan.xlsx");
        try (ExcelReader reader = cn.hutool.poi.excel.ExcelUtil.getReader(file)) {
            List<Map<String, Object>> read = reader.readAll();
            List<ServiceFeePlanEntity> plans = new ArrayList<>();
            for (Map<String, Object> objectMap : read) {

                String contractCode = objectMap.get("合同号").toString();
                String orgCode = objectMap.get("合同主体").toString();
                String serviceContractCode = objectMap.get("服务费协议编号").toString();
                String serviceOrgCode = objectMap.get("服务费主体").toString();
                BigDecimal serviceAmountTax = new BigDecimal(objectMap.get("应分摊的服务费收入（税前）").toString());
                BigDecimal serviceAmountNoTax = new BigDecimal(objectMap.get("应分摊的服务费收入（税后）").toString());
                BigDecimal actualServiceAmountTax = new BigDecimal(objectMap.get("服务费实收（税前）").toString());
                BigDecimal actualServiceAmountNoTax = new BigDecimal(objectMap.get("服务费实收（税后）").toString());
                List<RepaymentPlanEntity> repayments = repaymentPlanService.selectListPrioritySnapshot(Lists.newArrayList(contractCode));
                Map<Date, BigDecimal> repaymentMap = repayments.stream().collect(Collectors.toMap(
                        a -> DateUtil.beginOfDay(DateUtil.endOfMonth(a.getPlanDate())),
                        RepaymentPlanEntity::getServiceFeeAmortizationRate, BigDecimal::add, LinkedHashMap::new));
                int period = 1;
                BigDecimal totalPlanAmount = BigDecimal.ZERO;
                BigDecimal totalPlanAmountNoTax = BigDecimal.ZERO;
                BigDecimal totalActualAmount = BigDecimal.ZERO;
                for (Integer year : years) {
                    for (Integer month : months) {

                        String planAccruedAmountStr = objectMap.get(year + "年" + month + "月") != null ? objectMap.get(year + "年" + month + "月").toString() : "";
                        String adjustAmountNoTaxStr = objectMap.get(year + "年" + month + "月调整") != null ? objectMap.get(year + "年" + month + "月调整").toString() : "";
                        if (!containsNumber(planAccruedAmountStr) && !containsNumber(adjustAmountNoTaxStr)) continue;
                        BigDecimal planAccruedAmount = containsNumber(planAccruedAmountStr) ? new BigDecimal(planAccruedAmountStr) : BigDecimal.ZERO;
                        BigDecimal adjustAmountNoTax = containsNumber(adjustAmountNoTaxStr) ? new BigDecimal(adjustAmountNoTaxStr) : BigDecimal.ZERO;
                        totalPlanAmount = totalPlanAmount.add(planAccruedAmount);
                        ServiceFeePlanEntity serviceFeePlanEntity = new ServiceFeePlanEntity();
                        serviceFeePlanEntity.setId(IdWorker.getId());
                        serviceFeePlanEntity.setContractCode(contractCode);
                        serviceFeePlanEntity.setOrgId(orgCode);
                        serviceFeePlanEntity.setServiceFeeNo(serviceContractCode);
                        serviceFeePlanEntity.setServiceOrgId(serviceOrgCode);

                        BigDecimal serviceFeeAmortizationRate = repaymentMap.get(DateUtil.beginOfDay(DateUtil.endOfMonth(DateUtil.parse(year + "-" + month, "yyyy-MM"))));
                        serviceFeePlanEntity.setServiceFeeAmortizationRate(Optional.ofNullable(serviceFeeAmortizationRate).orElse(BigDecimal.ZERO));
//                        if (year>=initYear && month>initMonth){
//                            serviceFeePlanEntity.setActualAccruedAmount(FinhubAmountUtils.amountNoTax(totalPlanAmount).subtract(totalActualAmount));
//                        }else {
                            serviceFeePlanEntity.setActualAccruedAmount(adjustAmountNoTax);
//                        }
                        serviceFeePlanEntity.setAdjustAmount(serviceFeePlanEntity.getActualAccruedAmount());
                        serviceFeePlanEntity.setCreateBy("system");
                        serviceFeePlanEntity.setCreateTime(LocalDateTime.now());
                        serviceFeePlanEntity.setPlanDate(DateUtil.endOfMonth(DateUtil.parse(year + "-" + month, "yyyy-MM")));
                        serviceFeePlanEntity.setServiceFeeTotal(serviceAmountTax);
                        serviceFeePlanEntity.setServiceFeeTotalNoTax(serviceAmountNoTax);
                        serviceFeePlanEntity.setActualReceiveServiceFee(actualServiceAmountTax);
                        serviceFeePlanEntity.setActualReceiveNoTax(actualServiceAmountNoTax);
                        serviceFeePlanEntity.setServiceFeeAgreedAmount(serviceAmountTax);
                        serviceFeePlanEntity.setContractConfirmedAmount(BigDecimal.ZERO);
                        serviceFeePlanEntity.setAgreedConfirmedAmount(BigDecimal.ZERO);
                        serviceFeePlanEntity.setAgreedApportionAmount(serviceAmountTax);
                        serviceFeePlanEntity.setContractDeviceAmount(BigDecimal.ZERO);
                        serviceFeePlanEntity.setPlanApportionAmount(planAccruedAmount);
                        serviceFeePlanEntity.setPeriods(period++);
                        serviceFeePlanEntity.setAccrualType(orgCode.equals(serviceOrgCode) ? "0" : "1");
                        serviceFeePlanEntity.setPlanAmount(planAccruedAmount);
                        serviceFeePlanEntity.setPlanApportionNoTax(FinhubAmountUtils.amountNoTax(totalPlanAmount).subtract(totalPlanAmountNoTax));
                        totalPlanAmountNoTax = totalPlanAmountNoTax.add(serviceFeePlanEntity.getPlanApportionNoTax());
                        serviceFeePlanEntity.setPlanAmountNoTax(FinhubAmountUtils.amountNoTax(planAccruedAmount));

                        serviceFeePlanEntity.setDelFlag(YesOrNoEnum.NO.getCode());
                        plans.add(serviceFeePlanEntity);
                        totalActualAmount = totalActualAmount.add(serviceFeePlanEntity.getActualAccruedAmount());
                    }
                }
                log.info("init contract code:{}", contractCode);
            }
            serviceFeePlanService.saveBatch(plans);
            log.info("init contract code done!");
        } catch (Exception e) {
            log.error("initServiceFeePlan failed", e);
        }
    }

    public static boolean containsNumber(String input) {
        // 定义正则表达式，\d 表示匹配任意数字
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    @ApiOperation(value = "财务管理部-导出所有分摊")
    @PostMapping("/exportAllAllocationsPlan")
    public void exportAllAllocationsPlan(HttpServletResponse response, @RequestBody ServiceFeeQueryDTO queryDTO) {
        try {
            List<ServiceFeeAllocationExcelVo> list = serviceFeePlanService.getAllAllocationsPlanList(queryDTO);
            ServiceFeeDynamicColumnsExcelUtils.exportToExcel(list, response);
        } catch (IOException e) {
            throw new ServiceException("下载失败，失败原因:" + e.getMessage());
        }
    }

}



