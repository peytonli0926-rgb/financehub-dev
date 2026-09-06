package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.OfflineContractRepaymentPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OfflineContractRepaymentPlanDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractRepaymentPlanVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IOfflineContractRepaymentPlanService;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description :   OfflineContractRepaymentPlan控制器实现类
 * @Modified :
 */
@ApiIgnore
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/offline-contract-repayment-plan")
public class OfflineContractRepaymentPlanController {

    private final IOfflineContractRepaymentPlanService  offlineContractRepaymentPlanService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody OfflineContractRepaymentPlanDTO dto) {
        return R.ok(offlineContractRepaymentPlanService.saveOfflineContractRepaymentPlan(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody OfflineContractRepaymentPlanDTO dto) {
        return R.ok(offlineContractRepaymentPlanService.updateOfflineContractRepaymentPlan(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(offlineContractRepaymentPlanService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<OfflineContractRepaymentPlanDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(offlineContractRepaymentPlanService.getOfflineContractRepaymentPlanDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<OfflineContractRepaymentPlanVO>> page(@RequestBody @Valid OfflineContractRepaymentPlanQueryDTO queryDTO) {
        return R.ok(offlineContractRepaymentPlanService.selectPage(queryDTO));
    }

}



