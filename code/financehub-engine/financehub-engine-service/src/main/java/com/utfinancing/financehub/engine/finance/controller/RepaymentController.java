package com.utfinancing.financehub.engine.finance.controller;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.finance.model.dto.DataInitDTO;
import com.utfinancing.financehub.engine.finance.model.dto.GeneratePaymentDataProcessDTO;
import com.utfinancing.financehub.engine.finance.model.dto.GenerateXirrPaymentDataProcessDTO;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanProvisionService;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanService;
import com.utfinancing.financehub.engine.finance.service.IRepaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

@Api(tags = "偿还计划测算")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/repayment")
public class RepaymentController {
    @Resource
    private Map<String, IRepaymentService> repaymentSerivice;

    @Resource
    private IRepaymentPlanService repaymentPlanService;

    @Resource
    private IRepaymentPlanProvisionService repaymentPlanProvisionService;


    @ApiOperation(value = "源数据初始化")
    @PostMapping("/dataInit")
    public R dataInit(DataInitDTO params) {
        try {
            String transferSystemCode = StringUtils.EMPTY;
            if (SystemEnum.XWXT.getCode().equals(params.getSystemCode())) {
                transferSystemCode = "xw";
            } else if (SystemEnum.SYCXT.getCode().equals(params.getSystemCode())
                    || SystemEnum.CYCXT.getCode().equals(params.getSystemCode())) {
                transferSystemCode = "hy";
            } else if (SystemEnum.TYPT.getCode().equals(params.getSystemCode())) {
                transferSystemCode = "pl";
            } else if (SystemEnum.YYPT.getCode().equals(params.getSystemCode())) {
                transferSystemCode = "yy";
            }
            params.setTransferSystemCode(transferSystemCode);
            return repaymentSerivice.get(transferSystemCode).dataInit(params);
        } catch (Exception e) {
            log.error("源数据初始化异常!",e);
            return R.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "生成IRR偿还计划")
    @GetMapping("/generateRepaymentPlan")
    public R generateRepaymentPlan(@RequestParam(name="systemCode") String systemCode,
                                   @RequestParam(name="initDate") String initDate) {
        try {
            String transferSystemCode = StringUtils.EMPTY;
            if (SystemEnum.XWXT.getCode().equals(systemCode)) {
                transferSystemCode = "xw";
            } else if (SystemEnum.SYCXT.getCode().equals(systemCode) || SystemEnum.CYCXT.getCode().equals(systemCode)) {
                transferSystemCode = "hy";
            } else if (SystemEnum.TYPT.getCode().equals(systemCode)) {
                transferSystemCode = "pl";
            }else if (SystemEnum.YYPT.getCode().equals(systemCode)) {
                transferSystemCode = "yy";
            }

            GeneratePaymentDataProcessDTO params = new GeneratePaymentDataProcessDTO();
            params.setSystemCode(systemCode);
            params.setInitDate(initDate);
            params.setTransferSystemCode(transferSystemCode);
            return repaymentSerivice.get(transferSystemCode).generateRepaymentPlanService(params);
        } catch (Exception e) {
            log.error("生成偿还计划异常!",e);
            return R.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "生成XIRR偿还计划")
    @PostMapping("/generateXIRRRepaymentPlan")
    public R<String> generateXIRRRepaymentPlan(@RequestBody @Valid GenerateXirrPaymentDataProcessDTO params) {
        return repaymentPlanService.generateXirrPaymentDataProcess(params);
    }

    @ApiOperation(value = "备份偿还计划")
    @PostMapping("/repaymentPlanBackup")
    public R repaymentPlanBackup() {
        try {
            // 保存现有偿还计划数据
            repaymentPlanProvisionService.repaymentPlanDataBackup();
            return R.ok();
        } catch (Exception e) {
            log.error("备份偿还计划异常!",e);
            return R.fail(e.getMessage());
        }
    }
}
