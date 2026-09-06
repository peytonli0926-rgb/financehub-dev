package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankPcAmountQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankPcAmountDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundBusinessSystemEbankPcAmountVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IFundBusinessSystemEbankPcAmountService;


/**
 * @Author : bruyang
 * @Date : Create in 2024-07-16
 * @Description :   FundBusinessSystemEbankPcAmount控制器实现类
 * @Modified :
 */
@Api(tags = "资金系统、业务系统批次、金额")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/fund-business-system-ebank-pc-amount")
public class FundBusinessSystemEbankPcAmountController {

    private final IFundBusinessSystemEbankPcAmountService  fundBusinessSystemEbankPcAmountService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody FundBusinessSystemEbankPcAmountDTO dto) {
        return R.ok(fundBusinessSystemEbankPcAmountService.saveFundBusinessSystemEbankPcAmount(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody FundBusinessSystemEbankPcAmountDTO dto) {
        return R.ok(fundBusinessSystemEbankPcAmountService.updateFundBusinessSystemEbankPcAmount(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(fundBusinessSystemEbankPcAmountService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<FundBusinessSystemEbankPcAmountDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(fundBusinessSystemEbankPcAmountService.getFundBusinessSystemEbankPcAmountDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<FundBusinessSystemEbankPcAmountVO>> page(@RequestBody @Valid FundBusinessSystemEbankPcAmountQueryDTO queryDTO) {
        return R.ok(fundBusinessSystemEbankPcAmountService.selectPage(queryDTO));
    }

}



