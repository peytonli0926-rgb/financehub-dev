package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.ChargeOffSummaryReportQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ChargeOffSummaryReportDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ChargeOffSummaryReportVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IChargeOffSummaryReportService;


/**
 * @Author : bruyang
 * @Date : Create in 2024-07-26
 * @Description :   ChargeOffSummaryReport控制器实现类
 * @Modified :
 */
@Api(tags = "Charge Off汇总报表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/charge-off-summary-report")
public class ChargeOffSummaryReportController {

    private final IChargeOffSummaryReportService  chargeOffSummaryReportService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ChargeOffSummaryReportDTO dto) {
        return R.ok(chargeOffSummaryReportService.saveChargeOffSummaryReport(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ChargeOffSummaryReportDTO dto) {
        return R.ok(chargeOffSummaryReportService.updateChargeOffSummaryReport(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(chargeOffSummaryReportService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ChargeOffSummaryReportDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(chargeOffSummaryReportService.getChargeOffSummaryReportDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ChargeOffSummaryReportVO>> page(@RequestBody @Valid ChargeOffSummaryReportQueryDTO queryDTO) {
        return R.ok(chargeOffSummaryReportService.selectPage(queryDTO));
    }

}



