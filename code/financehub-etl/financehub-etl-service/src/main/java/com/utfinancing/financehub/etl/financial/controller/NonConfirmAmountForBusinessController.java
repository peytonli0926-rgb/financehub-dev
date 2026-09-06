package com.utfinancing.financehub.etl.financial.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.financial.model.dto.NonConfirmAmountForBusinessQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.NonConfirmAmountForBusinessDTO;
import com.utfinancing.financehub.etl.financial.model.vo.NonConfirmAmountForBusinessVO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.etl.financial.service.INonConfirmAmountForBusinessService;


/**
 * @Author : robjiang
 * @Date : Create in 2024-07-02
 * @Description :   NonConfirmAmountForBusiness控制器实现类
 * @Modified :
 */
@Api(tags = "")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/non-confirm-amount-for-business")
public class NonConfirmAmountForBusinessController {

    private final INonConfirmAmountForBusinessService  nonConfirmAmountForBusinessService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody NonConfirmAmountForBusinessDTO dto) {
        return R.ok(nonConfirmAmountForBusinessService.saveNonConfirmAmountForBusiness(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody NonConfirmAmountForBusinessDTO dto) {
        return R.ok(nonConfirmAmountForBusinessService.updateNonConfirmAmountForBusiness(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(nonConfirmAmountForBusinessService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<NonConfirmAmountForBusinessDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(nonConfirmAmountForBusinessService.getNonConfirmAmountForBusinessDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<NonConfirmAmountForBusinessVO>> page(@RequestBody @Valid NonConfirmAmountForBusinessQueryDTO queryDTO) {
        return R.ok(nonConfirmAmountForBusinessService.selectPage(queryDTO));
    }

    /**
     * 业务系统同步未确认收款
     */
    @ApiOperation(value = "未确认收款数据同步")
    @PostMapping("/nonConfirmAmountSync")
    public void nonConfirmAmountSync() {
        nonConfirmAmountForBusinessService.nonConfirmAmountSyncJob();
    }
}



