package com.utfinancing.financehub.engine.finance.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : hzhao
 * @Date : Create in 2023-09-12
 * @Description :   RepaymentPlan控制器实现类
 * @Modified :
 */
@Api(tags = "偿还计划测算表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/repayment-plan")
public class RepaymentPlanController {

    private final IRepaymentPlanService repaymentPlanService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody RepaymentPlanSaveDTO dto) {
        return R.ok(repaymentPlanService.saveRepaymentPlan(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody RepaymentPlanDTO dto) {
        return R.ok(repaymentPlanService.updateRepaymentPlan(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(repaymentPlanService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<RepaymentPlanDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(repaymentPlanService.getRepaymentPlanDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<RepaymentPlanVO>> page(@RequestBody @Valid RepaymentPlanQueryDTO queryDTO) {
        return R.ok(repaymentPlanService.selectPage(queryDTO));
    }

    @PostMapping("/handleIrr")
        @ApiOperation(value = "handleIrr")
    public R<List<RepaymentPlanSaveDTO>> handleIrr(@RequestBody List<RepaymentPlanSaveDTO> repaymentPlanVOS) {

        return R.ok(repaymentPlanService.handleIrr(repaymentPlanVOS));
    }

    @PostMapping("/changeRepayment")
    @ApiOperation(value = "偿还计划起租或变更")
    public void changeRepayment(@RequestBody ChangeRepaymentDTO params) {
        // 测试数据
        if (YesOrNoEnum.YES.getCode().equals(params.getIsLease())) {
            List<RepaymentPlanSaveDTO> repaymentPlanList = this.createTestData(params);
            params.setRepaymentPlanList(repaymentPlanList);
        } else {
            List<RepaymentPlanSaveDTO> repaymentPlanList = this.createTestDataForChange(params);
            params.setRepaymentPlanList(repaymentPlanList);
        }
        repaymentPlanService.saveRawData(null, JSONObject.parseObject(JSON.toJSONString(params)));
    }


    private List<RepaymentPlanSaveDTO> createTestData(ChangeRepaymentDTO params) {
        List<RepaymentPlanSaveDTO> result = new ArrayList<>();
        String cashflow = "-22000000.00,516167.14,616438.20,610051.56,603662.63,602271.40,602381.83,602439.51,600864.43,591366.95,574868.97,568365.73,561860.11,555353.11,553646.71,542338.79,535827.58,592824.95,613655.80,605040.10,597422.36,589803.94,582183.85,574560.07,483924.59,477403.83,470878.57,464354.79,457827.49,451299.65,444769.26,438237.32,431704.81,425169.72,418634.04,412096.75,405557.86,399015.34,392474.19,385929.39,379382.94,342835.82,336275.24,319712.90,309388.84,298887.49,279383.75,269768.06,0,0,259914.26,249059.24,239198.58,229234.66,219298.39,219734.79,209467.88,209615.49,202787.67,202247.68,202740.99,200744.00,200896.07";
        String principalAmount = "0,516167.1358,616438.2013,610051.5591,603662.6302,602271.4045,602381.8349,602439.5145,600864.4309,591366.9513,574868.9721,568365.7304,561860.1121,555353.1087,553646.7094,542338.7891,535827.5834,592824.9536,613655.8047,605040.1019,597422.3551,589803.9425,582183.8526,574560.0732,483924.5934,477403.8338,470878.5708,464354.7919,457827.4881,451299.6487,444769.2623,438237.3187,431704.8066,425169.7158,418634.0351,412096.7536,405557.86,399015.343,392474.1913,385929.3942,379382.9402,342835.817,336275.2442,319712.8979,309388.8432,298887.4902,279383.7466,269768.06,0,0,259914.263,249059.2425,239198.5845,229234.6555,219298.393,219734.7864,209467.8834,209615.4896,202787.6675,202247.6843,202740.9926,200744.0006,200896.0725";
        String planDate = "2020-05-08,2020-06-08,2020-07-08,2020-08-08,2020-09-08,2020-10-08,2020-11-08,2020-12-08,2021-01-08,2021-02-08,2021-03-08,2021-04-08,2021-05-08,2021-06-08,2021-07-08,2021-08-08,2021-09-08,2021-10-08,2021-11-08,2021-12-08,2022-01-08,2022-02-08,2022-03-08,2022-04-08,2022-05-08,2022-06-08,2022-07-08,2022-08-08,2022-09-08,2022-10-08,2022-11-08,2022-12-08,2023-01-08,2023-02-08,2023-03-08,2023-04-08,2023-05-08,2023-06-08,2023-07-08,2023-08-08,2023-09-08,2023-10-08,2023-11-08,2023-12-08,2024-01-08,2024-02-08,2024-03-08,2024-04-08,2024-04-29,2024-04-30,2024-05-08,2024-06-08,2024-07-08,2024-08-08,2024-09-08,2024-10-08,2024-11-08,2024-12-08,2025-01-08,2025-02-08,2025-03-08,2025-04-08,2025-05-08";

        String[] cashflowArray = cashflow.split(",");
        String[] principalAmountArray = principalAmount.split(",");
        String[] planDateArray = planDate.split(",");
        for (int i = 0; i < principalAmountArray.length; i++) {
            RepaymentPlanSaveDTO dto = new RepaymentPlanSaveDTO();
            dto.setContractCode(params.getContractCode());
            dto.setSystemCode(params.getSystemCode());
            dto.setOrgId(params.getOrgId());
            dto.setRentAmount(new BigDecimal(principalAmountArray[i]));
            dto.setPrincipalAmount(new BigDecimal(principalAmountArray[i]));
            dto.setInterestAmount(BigDecimal.ZERO);
            dto.setCashFlow(new BigDecimal(cashflowArray[i]));
            dto.setPlanDate(DateUtils.parseDate(planDateArray[i]));
            if (new BigDecimal(cashflowArray[i]).compareTo(BigDecimal.ZERO) > 0) {
                dto.setPeriods(i);
            }
            result.add(dto);
        }

        return result;
    }

    private List<RepaymentPlanSaveDTO> createTestDataForChange(ChangeRepaymentDTO params) {
        List<RepaymentPlanSaveDTO> result = new ArrayList<>();
        String cashflow = "-22000000.00,516167.14,616438.20,610051.56,603662.63,602271.40,602381.83,602439.51,600864.43,591366.95,574868.97,568365.73,561860.11,555353.11,553646.71,542338.79,535827.58,592824.95,613655.80,605040.10,597422.36,589803.94,582183.85,574560.07,486310.95,481618.00,474933.00,468251.00,461567.00,454884.00,448200.00,441516.00,434833.00,428149.00,421466.00,414783.00,408100.00,401415.00,0,0,0,0,0,0,0,0,0,0,100000.00,0,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,100000.00,120000.00,120000.00,2320094.34";
        String principalAmount = "0,516167.1358,616438.2013,610051.5591,603662.6302,602271.4045,602381.8349,602439.5145,600864.4309,591366.9513,574868.9721,568365.7304,561860.1121,555353.1087,553646.7094,542338.7891,535827.5834,592824.9536,613655.8047,605040.1019,597422.3551,589803.9425,582183.8526,574560.0732,486310.9468,481618,474933,468251,461567,454884,448200,441516,434833,428149,421466,414783,408100,401415,0,0,0,0,0,0,0,0,0,0,100000,0,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,120000,120000,2320094.34";
        String planDate = "2020-05-08,2020-06-08,2020-07-08,2020-08-08,2020-09-08,2020-10-08,2020-11-08,2020-12-08,2021-01-08,2021-02-08,2021-03-08,2021-04-08,2021-05-08,2021-06-08,2021-07-08,2021-08-08,2021-09-08,2021-10-08,2021-11-08,2021-12-08,2022-01-08,2022-02-08,2022-03-08,2022-04-08,2022-05-08,2022-06-08,2022-07-08,2022-08-08,2022-09-08,2022-10-08,2022-11-08,2022-12-08,2023-01-08,2023-02-08,2023-03-08,2023-04-08,2023-05-08,2023-06-08,2023-07-08,2023-08-08,2023-09-08,2023-10-08,2023-11-08,2023-12-08,2024-01-08,2024-02-08,2024-03-08,2024-04-08,2024-04-29,2024-04-30,2024-05-29,2024-06-29,2024-07-29,2024-08-29,2024-09-29,2024-10-29,2024-11-29,2024-12-29,2025-01-29,2025-02-28,2025-03-29,2025-04-29,2025-05-29,2025-06-29,2025-07-29,2025-08-29,2025-09-29,2025-10-29,2025-11-29,2025-12-29,2026-01-29,2026-02-28,2026-03-29,2026-04-29,2026-05-29,2026-06-29";

        String[] cashflowArray = cashflow.split(",");
        String[] principalAmountArray = principalAmount.split(",");
        String[] planDateArray = planDate.split(",");
        for (int i = 0; i < principalAmountArray.length; i++) {
            RepaymentPlanSaveDTO dto = new RepaymentPlanSaveDTO();
            dto.setContractCode(params.getContractCode());
            dto.setSystemCode(params.getSystemCode());
            dto.setOrgId(params.getOrgId());
            dto.setRentAmount(new BigDecimal(principalAmountArray[i]));
            dto.setPrincipalAmount(new BigDecimal(principalAmountArray[i]));
            dto.setInterestAmount(BigDecimal.ZERO);
            dto.setCashFlow(new BigDecimal(cashflowArray[i]));
            dto.setPlanDate(DateUtils.parseDate(planDateArray[i]));
            if (new BigDecimal(cashflowArray[i]).compareTo(BigDecimal.ZERO) > 0) {
                dto.setPeriods(i);
            }
            result.add(dto);
        }

        return result;
    }

    @ApiOperation(value = "小微系统偿还计划期初数据导入")
    @PostMapping("/importDataXWXT")
    public R importDataXWXT(MultipartFile file, String sheetName, String opt) throws Exception {
        ExcelUtil<ImportRepaymentPlanXWXTExcel> util = new ExcelUtil<ImportRepaymentPlanXWXTExcel>(ImportRepaymentPlanXWXTExcel.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<ImportRepaymentPlanXWXTExcel> list = util.importExcel(sheetName, inputStream, 0);
            if ("1".equals(opt)) {
                repaymentPlanService.importDataXWXTAsync(list, opt);
            } else {
                repaymentPlanService.importDataXWXT(list, opt);
            }
            return R.ok();
        } catch (Exception e) {
            log.error("小微系统偿还计划期初数据导入异常--",e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @ApiOperation(value = "恒运系统偿还计划期初数据导入")
    @PostMapping("/importDataHY")
    public R importDataHY(MultipartFile file, String sheetName, String systemCode) throws Exception {
        ExcelUtil<ImportRepaymentPlanHYExcel> util = new ExcelUtil<ImportRepaymentPlanHYExcel>(ImportRepaymentPlanHYExcel.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<ImportRepaymentPlanHYExcel> list = util.importExcel(sheetName, inputStream, 0);
            repaymentPlanService.importDataHY(list, systemCode);
            return R.ok();
        } catch (Exception e) {
            log.error("恒运系统偿还计划期初数据导入异常--",e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @ApiOperation(value = "每月变更任务")
    @PostMapping("/monthlyChangeTask")
    public R monthlyChangeTask(String dateString) {
        try {
            repaymentPlanService.monthlyChangeTask(dateString);
            return R.ok();
        } catch (Exception e) {
            log.error("每月变更任务报错", e);
            return R.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "合同起租任务")
    @PostMapping("/contractOnHire")
    public R contractOnHire() {
        try {
            repaymentPlanService.contractOnHireTask();
            return R.ok();
        } catch (Exception e) {
            log.error("合同起租任务报错", e);
            return R.fail(e.getMessage());
        }
    }
}



