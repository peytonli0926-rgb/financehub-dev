package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.BusinessClaimRepaymentRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.BusinessClaimRepaymentRecordDTO;
import com.utfinancing.financehub.engine.finance.model.vo.BusinessClaimRepaymentRecordVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IBusinessClaimRepaymentRecordService;


/**
 * @Author : robjiang
 * @Date : Create in 2024-03-21
 * @Description :   BusinessClaimRepaymentRecord控制器实现类
 * @Modified :
 */
@Api(tags = "业务系统对还款认领记录")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/business-claim-repayment-record")
public class BusinessClaimRepaymentRecordController {

    private final IBusinessClaimRepaymentRecordService  businessClaimRepaymentRecordService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody BusinessClaimRepaymentRecordDTO dto) {
        return R.ok(businessClaimRepaymentRecordService.saveBusinessClaimRepaymentRecord(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody BusinessClaimRepaymentRecordDTO dto) {
        return R.ok(businessClaimRepaymentRecordService.updateBusinessClaimRepaymentRecord(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(businessClaimRepaymentRecordService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<BusinessClaimRepaymentRecordDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(businessClaimRepaymentRecordService.getBusinessClaimRepaymentRecordDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<BusinessClaimRepaymentRecordVO>> page(@RequestBody @Valid BusinessClaimRepaymentRecordQueryDTO queryDTO) {
        return R.ok(businessClaimRepaymentRecordService.selectPage(queryDTO));
    }

    @ApiOperation(value = "抽取认领记录")
    @PostMapping("/extractClaimPaymentRecord")
    public void extractClaimPaymentRecord() {
        businessClaimRepaymentRecordService.extractClaimPaymentRecord();
    }
}



