package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.FundEbankTransactionDataQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundPaymentDataQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundSystemBalanceQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundSystemBalanceDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundSystemBalanceVO;
import com.utfinancing.financehub.engine.finance.service.IFundEbankTransactionDataService;
import com.utfinancing.financehub.engine.finance.service.IFundPaymentDataService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IFundSystemBalanceService;


/**
 * @Author : bruyang
 * @Date : Create in 2023-12-05
 * @Description :   FundSystemBalance控制器实现类
 * @Modified :
 */
@Api(tags = "资金系统余额表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/fund-system-balance")
public class FundSystemBalanceController {

    private final IFundSystemBalanceService  fundSystemBalanceService;

    private final IFundEbankTransactionDataService fundEbankTransactionDataService;

    private final IFundPaymentDataService fundPaymentDataService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody FundSystemBalanceDTO dto) {
        return R.ok(fundSystemBalanceService.saveFundSystemBalance(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody FundSystemBalanceDTO dto) {
        return R.ok(fundSystemBalanceService.updateFundSystemBalance(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(fundSystemBalanceService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<FundSystemBalanceDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(fundSystemBalanceService.getFundSystemBalanceDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<FundSystemBalanceVO>> page(@RequestBody @Valid FundSystemBalanceQueryDTO queryDTO) {
        return R.ok(fundSystemBalanceService.selectPage(queryDTO));
    }

    @ApiOperation(value = "资金系统收付款生成凭证")
    @PostMapping("/testGenerateVoucher1")
    public R<Boolean> testGenerateVoucher1(@RequestBody @Valid FundEbankTransactionDataQueryDTO queryDTO){
        return R.ok(fundEbankTransactionDataService.transactionGenerateVoucher(queryDTO));
    }

    @ApiOperation(value = "资金系统收付款生成凭证按照开始结束时间跑同步")
    @PostMapping("/testGenerateVoucherByDay")
    public R<Boolean> testGenerateVoucherByDay(@RequestBody @Valid FundEbankTransactionDataQueryDTO queryDTO){
        return R.ok(fundEbankTransactionDataService.testGenerateVoucherByDay(queryDTO));
    }

    @ApiOperation(value = "资金系统付款生成凭证")
    @PostMapping("/testGenerateVoucher2")
    public R<Boolean> testGenerateVoucher2(@RequestBody @Valid FundPaymentDataQueryDTO queryDTO){
        return R.ok(fundPaymentDataService.payMentGenerateVoucher(queryDTO));
    }

    @ApiOperation(value = "资金系统付款生成凭证按照开始结束时间跑同步")
    @PostMapping("/testGenerateVoucher2ByDay")
    public R<Boolean> testGenerateVoucher2ByDay(@RequestBody @Valid FundPaymentDataQueryDTO queryDTO){
        return R.ok(fundPaymentDataService.testGenerateVoucher2ByDay(queryDTO));
    }


    @ApiOperation(value = "资金系统金额映射表生成凭证接口")
    @PostMapping("/generateMappingVoucher")
    public R generateMappingVoucher(){
        fundEbankTransactionDataService.generateMappingVoucher();
        return R.ok();
    }


}



