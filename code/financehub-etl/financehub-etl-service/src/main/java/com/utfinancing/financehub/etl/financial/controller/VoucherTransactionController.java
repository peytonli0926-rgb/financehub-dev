package com.utfinancing.financehub.etl.financial.controller;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.easold.model.dto.QueryEas1VoucherInputDTO;
import com.utfinancing.financehub.etl.financial.service.VoucherTransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Author : robjiang
 * @Date : Create in 2024-02-29
 * @Description :   VoucherToEasRecord控制器实现类
 * @Modified :
 */
@Api(tags = "")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/etl/voucher-transaction")
public class VoucherTransactionController {

    private final VoucherTransactionService voucherTransactionService;

    @PostMapping("/toEas")
    @ApiOperation(value = "金蝶数据中心1的凭证传送至数据中心2")
    public R<Boolean> eas1VoucherToEas2(@RequestBody QueryEas1VoucherInputDTO params) throws Exception {
        voucherTransactionService.transferVoucherToEAS(params);
        return R.ok();
    }

}



