package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeePlanNewQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeePlanNewDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeAllocationExcelVo;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeePlanNewVO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeePlanVO;
import com.utfinancing.financehub.engine.utils.excel.ServiceFeeDynamicColumnsExcelUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

import com.utfinancing.financehub.engine.finance.service.IServiceFeePlanNewService;


/**
 * @Author : le
 * @Date : Create in 2025-11-10
 * @Description :   ServiceFeePlanNew控制器实现类
 * @Modified :
 */
@Api(tags = "服务费分摊计划-新")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/service-fee-plan-new")
public class ServiceFeePlanNewController {

    private final IServiceFeePlanNewService serviceFeePlanNewService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ServiceFeePlanNewDTO dto) {
        return R.ok(serviceFeePlanNewService.saveServiceFeePlanNew(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ServiceFeePlanNewDTO dto) {
        return R.ok(serviceFeePlanNewService.updateServiceFeePlanNew(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(serviceFeePlanNewService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ServiceFeePlanNewDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(serviceFeePlanNewService.getServiceFeePlanNewDTOById(id));
    }

    @ApiOperation(value = "单月详情查询")
    @PostMapping("/detail/plan")
    public R<List<ServiceFeePlanNewVO>> selectDetailPlanList(@RequestBody @Valid ServiceFeeDetailsQueryDTO queryDTO) {
        return R.ok(serviceFeePlanNewService.selectDetailPlanList(queryDTO));
    }


    @ApiOperation(value = "财务管理部-导出所有分摊")
    @PostMapping("/exportAllAllocationsPlan")
    public void exportAllAllocationsPlan(HttpServletResponse response, @RequestBody ServiceFeeQueryDTO queryDTO) {
        try {
            List<ServiceFeeAllocationExcelVo> list = serviceFeePlanNewService.getAllAllocationsPlanList(queryDTO);
            ServiceFeeDynamicColumnsExcelUtils.exportToExcel(list, response);
        } catch (IOException e) {
            throw new ServiceException("下载失败，失败原因:" + e.getMessage());
        }
    }

}



