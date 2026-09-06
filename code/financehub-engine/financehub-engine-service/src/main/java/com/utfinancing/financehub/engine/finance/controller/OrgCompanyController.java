package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanySaveDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;


/**
 * @Author : hzhao
 * @Date : Create in 2023-10-12
 * @Description :   OrgCompany控制器实现类
 * @Modified :
 */
@Api(tags = "签约主体表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/org-company")
public class OrgCompanyController {

    private final IOrgCompanyService  orgCompanyService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody OrgCompanySaveDTO dto) {
        return R.ok(orgCompanyService.saveOrgCompany(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody OrgCompanySaveDTO dto) {
        return R.ok(orgCompanyService.updateOrgCompany(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(orgCompanyService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<OrgCompanyDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(orgCompanyService.getOrgCompanyDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<OrgCompanyVO>> page(@RequestBody @Valid OrgCompanyQueryDTO queryDTO) {
        return R.ok(orgCompanyService.selectPage(queryDTO));
    }

    @ApiOperation(value = "根据条件查询签约主体列表")
    @PostMapping("/selectByCondition")
    public R<List<OrgCompanyVO>> selectByCondition(@RequestBody @Valid OrgCompanyQueryDTO queryDTO) {
        return R.ok(orgCompanyService.selectByCondition(queryDTO));
    }

}



