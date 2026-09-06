package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.FundEbankTransactionDataQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundPaymentDataQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundPaymentDataDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundPaymentDataVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IFundPaymentDataService;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @Author : hzhao
 * @Date : Create in 2023-10-17
 * @Description :   FundPaymentData控制器实现类
 * @Modified :
 */
@ApiIgnore
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/fund-payment-data")
public class FundPaymentDataController {

    private final IFundPaymentDataService  fundPaymentDataService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody FundPaymentDataDTO dto) {
        return R.ok(fundPaymentDataService.saveFundPaymentData(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody FundPaymentDataDTO dto) {
        return R.ok(fundPaymentDataService.updateFundPaymentData(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(fundPaymentDataService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<FundPaymentDataDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(fundPaymentDataService.getFundPaymentDataDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<FundPaymentDataVO>> page(@RequestBody @Valid FundPaymentDataQueryDTO queryDTO) {
        return R.ok(fundPaymentDataService.selectPage(queryDTO));
    }


}



