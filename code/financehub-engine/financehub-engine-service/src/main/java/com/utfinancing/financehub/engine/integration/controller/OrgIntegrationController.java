package com.utfinancing.financehub.engine.integration.controller;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.integration.model.dto.*;
import com.utfinancing.financehub.engine.integration.service.IOrgIntegrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 08/10/2023
 */
@Api(tags = "组织机构集成接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/integration/org")
public class OrgIntegrationController {

    @Autowired
    private IOrgIntegrationService orgIntegrationService;

    @ApiOperation(value = "查询部门全量信息")
    @PostMapping("/queryAll")
    public R<List<OrgDTO>> queryAll() {
        return R.ok(orgIntegrationService.queryAll());
    }

    @ApiOperation(value = "查询一级部门信息")
    @PostMapping("/queryFirst")
    public R<List<OrgDTO>> queryFirst() {
        return R.ok(orgIntegrationService.queryFirst());
    }

    @ApiOperation(value = "多条件查询部门列表")
    @PostMapping("/queryOrgList")
    public R<List<OrgDTO>> queryOrgList(@RequestBody @Valid OrgQueryDTO queryDTO) {
        return R.ok(orgIntegrationService.queryOrgList(queryDTO));
    }

    @ApiOperation(value = "根据部门编码查询岗位列表")
    @PostMapping("/queryOrgPositionList")
    public R<List<PositionDTO>> queryOrgPositionList(@RequestParam String orgCode) {
        return R.ok(orgIntegrationService.queryOrgPositionList(orgCode));
    }

    @ApiOperation(value = "根据部门编码查询员工列表")
    @PostMapping("/queryOrgStaffList")
    public R<List<StaffDTO>> queryOrgStaffList(@RequestParam String orgCode) {
        return R.ok(orgIntegrationService.queryOrgStaffList(orgCode));
    }

    @ApiOperation(value = "批量查询岗位信息")
    @PostMapping("/queryPositionList")
    public R<List<PositionDTO>> queryPositionList(@RequestBody @Valid PositionQueryDTO queryDTO) {
        return R.ok(orgIntegrationService.queryPositionList(queryDTO));
    }

    @ApiOperation(value = "批量查询员工信息")
    @PostMapping("/queryStaffList")
    public R<List<StaffDTO>> queryStaffList(@RequestBody @Valid StaffQueryDTO queryDTO) {
        return R.ok(orgIntegrationService.queryStaffList(queryDTO));
    }

}
