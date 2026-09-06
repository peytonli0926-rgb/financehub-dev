package com.utfinancing.financehub.engine.finance.controller;

import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.OrgClaimEbankNoQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgClaimEbankNoDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgClaimEbankNoVO;
import com.xxl.job.core.context.XxlJobHelper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IOrgClaimEbankNoService;


/**
 * @Author : robjiang
 * @Date : Create in 2025-06-06
 * @Description :   OrgClaimEbankNo控制器实现类
 * @Modified :
 */
@Api(tags = "机构认领的网银编号")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/org-claim-ebank-no")
public class OrgClaimEbankNoController {

    private final IOrgClaimEbankNoService  orgClaimEbankNoService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody OrgClaimEbankNoDTO dto) {
        return R.ok(orgClaimEbankNoService.saveOrgClaimEbankNo(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody OrgClaimEbankNoDTO dto) {
        return R.ok(orgClaimEbankNoService.updateOrgClaimEbankNo(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(orgClaimEbankNoService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<OrgClaimEbankNoDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(orgClaimEbankNoService.getOrgClaimEbankNoDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<OrgClaimEbankNoVO>> page(@RequestBody @Valid OrgClaimEbankNoQueryDTO queryDTO) {
        return R.ok(orgClaimEbankNoService.selectPage(queryDTO));
    }

    @ApiOperation(value = "业务系统认领网银编号数据同步")
    @GetMapping("/orgClaimEbankNoSync/{date}")
    public void orgClaimEbankNoSync(@PathVariable("date") @Valid @NotNull String date) {
        log.info("同步机构认领的网银编号, 任务启动");
        orgClaimEbankNoService.orgClaimEbankNoSync(DateUtils.parseDate(date));
        log.info("同步机构认领的网银编号, 任务结束");
    }
}



