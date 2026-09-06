package com.utfinancing.financehub.etl.platform.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanEntity;
import com.utfinancing.financehub.etl.financial.model.dto.RepaymentPlanSaveDTO;
import com.utfinancing.financehub.etl.platform.model.QueryRepaymentPlanDTO;
import com.utfinancing.financehub.etl.platform.model.SelectReceiveRepaymentDTO;
import com.utfinancing.financehub.etl.platform.model.SelectRepaymentFromTYPTInputDTO;
import com.utfinancing.financehub.etl.platform.service.IPlatformDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 23/01/2024
 */
@Api(tags = "对接统一平台业务数据")
@Slf4j
@RestController
@RequestMapping("/platform")
public class PlatformDataController {

    @Resource
    private IPlatformDataService platformDataService;


    @PostMapping("/queryRepaymentPlan")
    @ApiOperation(value = "查询最新的偿还计划")
    public R<List<RepaymentPlanSaveDTO>> queryRepaymentPlan(@RequestBody @Valid QueryRepaymentPlanDTO params) {
        return R.ok(platformDataService.getRepaymentPlanFromTYPT(params));
    }

    @PostMapping("/queryReceivedRepaymentPlan")
    @ApiOperation(value = "查询最新的回笼数据")
    public R<List<SelectReceiveRepaymentDTO>> queryReceivedRepaymentPlan(@RequestBody @Valid QueryRepaymentPlanDTO params) {
        if (StringUtils.isEmpty(params.getContractCode())) {
            return R.ok(new ArrayList<>());
        }
        SelectRepaymentFromTYPTInputDTO dto = new SelectRepaymentFromTYPTInputDTO();
        dto.setContractCode(params.getContractCode());
        return R.ok(platformDataService.selectReceiveRepayment(dto));
    }
}
