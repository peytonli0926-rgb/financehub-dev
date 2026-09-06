package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.MarginTypeEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.MarginContractBalanceVO;
import com.utfinancing.financehub.engine.finance.service.IMarginContractBalanceService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


/**
 * @Author : hzhao
 * @Date : Create in 2023-09-24
 * @Description :   MarginContractBalance控制器实现类
 * @Modified :
 */
@Api(tags = "保证金合同余额表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/margin-contract-balance")
public class MarginContractBalanceController {

    private final IMarginContractBalanceService marginContractBalanceService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody MarginContractBalanceDTO dto) {
        return R.ok(marginContractBalanceService.saveMarginContractBalance(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody MarginContractBalanceDTO dto) {
        return R.ok(marginContractBalanceService.updateMarginContractBalance(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(marginContractBalanceService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<MarginContractBalanceDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(marginContractBalanceService.getMarginContractBalanceDTOById(id));
    }

    @ApiOperation(value = "单月-分页查询")
    @PostMapping("/page")
    public R<IPage<MarginContractBalanceVO>> page(@RequestBody MarginContractBalanceQueryDTO queryDTO) {
        return R.ok(marginContractBalanceService.selectPage(queryDTO));
    }

    @ApiOperation(value = "单月导出")
    @PostMapping("/export")
    public R<Map<String,String>> export(@RequestBody @Valid MarginContractBalanceQueryDTO queryDTO) {
            return R.ok(marginContractBalanceService.export(queryDTO));
    }

    @ApiOperation(value = "汇总-分页查询")
    @PostMapping("/summary/page")
    public R<IPage<MarginContractBalanceVO>> summaryPage(@RequestBody @Valid MarginContractBalanceQueryDTO queryDTO) {
        return R.ok(marginContractBalanceService.selectSummaryPage(queryDTO));
    }

    @ApiOperation(value = "生成重分类信息")
    @PostMapping("/generate/reclassification")
    public R<Boolean> generateReclassification(@RequestBody @Valid MarginContractBalanceQueryDTO queryDTO) {
        queryDTO.setMarginType(MarginTypeEnum.RECLASSIFICATION.getCode());
        marginContractBalanceService.generate(queryDTO);
        return R.ok(true);
    }

    @ApiOperation(value = "生成利息计提信息")
    @PostMapping("/generate/interest-provision")
    public R<Boolean> generate(@RequestBody @Valid MarginContractBalanceQueryDTO queryDTO) {
        queryDTO.setMarginType(MarginTypeEnum.INTEREST_PROVISION.getCode());
        marginContractBalanceService.generate(queryDTO);
        return R.ok(true);
    }

    @ApiOperation(value = "生成重分类凭证")
    @PostMapping("/voucher/reclassification")
    public R voucherReclassification(@RequestBody MarginContractBalanceCheckDTO checkDTO) {
        marginContractBalanceService.voucher(checkDTO, MarginTypeEnum.RECLASSIFICATION, YesOrNoEnum.NO.getCode());
        return R.ok();
    }

    @ApiOperation(value = "生成利息计提凭证")
    @PostMapping("/voucher/interest-provision")
    public R voucher(@RequestBody MarginContractBalanceCheckDTO checkDTO) {
        marginContractBalanceService.voucher(checkDTO, MarginTypeEnum.INTEREST_PROVISION,YesOrNoEnum.NO.getCode());
        return R.ok();
    }

    @ApiOperation(value = "提交")
    @PostMapping("/submmit")
    public R<String> submmit(@RequestBody MarginContractBalanceCheckDTO checkDTO) {
        return R.ok(marginContractBalanceService.submmit(checkDTO));
    }

    @ApiOperation(value = "撤回")
    @PostMapping("/withdraw")
    public R withdraw(@RequestBody MarginContractBalanceCheckDTO checkDTO) {
        return R.ok(marginContractBalanceService.withdraw(checkDTO));
    }

    @ApiOperation(value = "复核通过")
    @PostMapping("/check/pass")
    public R pass(@RequestBody MarginContractBalanceCheckDTO checkDTO) {
        return R.ok(marginContractBalanceService.pass(checkDTO));
    }

    @ApiOperation(value = "复核失败")
    @PostMapping("/check/fail")
    public R fail(@RequestBody MarginContractBalanceCheckDTO checkDTO) {
        return R.ok(marginContractBalanceService.fail(checkDTO));
    }

    @ApiOperation(value = "录入批量新增")
    @PostMapping("/save/batch")
    public R saveBatch(@RequestBody List<MarginContractBalanceSaveDTO> dto) {
        return R.ok(marginContractBalanceService.saveMarginContractBalanceBatch(dto));
    }

    @ApiOperation(value = "录入信息列表查询")
    @PostMapping("/enter-list")
    public R<List<MarginContractBalanceVO>> enterList() {
        return R.ok(marginContractBalanceService.enterList());
    }

    @ApiOperation(value = "更新状态")
    @PostMapping("/updateProcessStatus")
    public R<Boolean> updateProcessStatus(@RequestBody CommonApproveDTO approveDTO) {
        return R.ok(marginContractBalanceService.updateProcessStatus(approveDTO));
    }

}



