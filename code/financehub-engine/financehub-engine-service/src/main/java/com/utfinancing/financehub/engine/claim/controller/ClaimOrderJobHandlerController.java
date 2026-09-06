package com.utfinancing.financehub.engine.claim.controller;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.claim.controller.ClaimOrderJobHandlerController</li>
 * <li>CreateTime : 2023/11/27 09:34</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderQueryDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSpecialQueryDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderCostVo;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderJobHandlerService;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderSpecialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "报销系统订单定时任务")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/claim")
public class ClaimOrderJobHandlerController {

    @Resource
    private IClaimOrderJobHandlerService iClaimOrderJobHandlerService;

    @Resource
    private IClaimOrderSpecialService iClaimOrderSpecialService;

    @ApiOperation(value = "成本类费用生成凭证")
    @PostMapping("/costGpsGenerateVoucher")
    public R<Boolean> costGpsGenerateVoucher(@RequestBody ClaimOrderSpecialQueryDTO queryDTO) {
        return R.ok(iClaimOrderJobHandlerService.costGenerateVoucher(queryDTO));
    }

    @ApiOperation(value = "诉讼费用生成凭证")
    @PostMapping("/courtCostGenerateVoucher")
    public R<Boolean> courtCostGenerateVoucher(@RequestBody ClaimOrderQueryDTO queryDTO){
        return R.ok(iClaimOrderJobHandlerService.courtCostGenerateVoucher(queryDTO));
    }

    @ApiOperation(value = "测试成本类费用统计接口")
    @PostMapping("/statisticalCost")
    public R<List<ClaimOrderCostVo>> statisticalCost(@RequestBody ClaimOrderSpecialQueryDTO queryDTO){
        return R.ok(iClaimOrderSpecialService.statisticalCost(queryDTO));
    }
}
