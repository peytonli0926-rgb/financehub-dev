package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanProvisionEntity;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeDetailsExcel;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RepaymentPlanExcel;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeDetailsVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.service.ILeaseIncomeService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanProvisionService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.util.List;


/**
 * @Author : robjiang
 * @Date : Create in 2024-01-30
 * @Description :   RepaymentPlanProvision控制器实现类
 * @Modified :
 */
@Api(tags = "偿还计划测算表-计提用")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/repayment-plan-provision")
public class RepaymentPlanProvisionController {

    private final IRepaymentPlanProvisionService  repaymentPlanProvisionService;

    private final ILeaseIncomeService leaseIncomeService;

    @ApiOperation(value = "计提偿还计划-导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody LeaseIncomeDetailsQueryDTO queryDTO) {
        try {
            if (StringUtils.isEmpty(queryDTO.getContractCode())) {
                return;
            }

            List<RepaymentPlanVO> repaymentPlanVOList = leaseIncomeService.selectDetailPlanList(queryDTO);
            ExcelUtil<RepaymentPlanExcel> util = new ExcelUtil(RepaymentPlanExcel.class);
            response.setHeader("Content-Disposition", "attachment; filename=" +
                    URLEncoder.encode("收益计提-" + queryDTO.getContractCode() + ".xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(repaymentPlanVOList, RepaymentPlanExcel.class), "收益计提");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}



