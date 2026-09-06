package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.ContractQueryInfoDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RepaymentPlanHisQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RepaymentPlanHisDTO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanHisVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanHisService;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @Author : hzhao
 * @Date : Create in 2023-10-31
 * @Description :   RepaymentPlanHis控制器实现类
 * @Modified :
 */
@Api(tags = "回笼计划Api")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/repayment-plan-his")
public class RepaymentPlanHisController {

    private final IRepaymentPlanHisService  repaymentPlanHisService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    @ApiIgnore
    public R<Long> save(@Valid @RequestBody RepaymentPlanHisDTO dto) {
        return R.ok(repaymentPlanHisService.saveRepaymentPlanHis(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiIgnore
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody RepaymentPlanHisDTO dto) {
        return R.ok(repaymentPlanHisService.updateRepaymentPlanHis(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiIgnore
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(repaymentPlanHisService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiIgnore
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<RepaymentPlanHisDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(repaymentPlanHisService.getRepaymentPlanHisDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    @ApiIgnore
    public R<IPage<RepaymentPlanHisVO>> page(@RequestBody @Valid RepaymentPlanHisQueryDTO queryDTO) {
        return R.ok(repaymentPlanHisService.selectPage(queryDTO));
    }
}



