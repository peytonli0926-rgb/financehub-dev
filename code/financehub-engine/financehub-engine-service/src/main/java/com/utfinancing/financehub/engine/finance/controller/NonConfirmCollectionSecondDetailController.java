package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.NonConfirmCollectionSecondDetailVO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.INonConfirmCollectionSecondDetailService;


/**
 * @Author : robjiang
 * @Date : Create in 2024-03-22
 * @Description :   NonConfirmCollectionSecondDetail控制器实现类
 * @Modified :
 */
@Api(tags = "未确认收款明细")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/non-confirm-collection-second-detail")
public class NonConfirmCollectionSecondDetailController {

    private final INonConfirmCollectionSecondDetailService  nonConfirmCollectionSecondDetailService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody NonConfirmCollectionSecondDetailDTO dto) {
        return R.ok(nonConfirmCollectionSecondDetailService.saveNonConfirmCollectionSecondDetail(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody NonConfirmCollectionSecondDetailDTO dto) {
        return R.ok(nonConfirmCollectionSecondDetailService.updateNonConfirmCollectionSecondDetail(id, dto));
    }

//    @ApiOperation(value = "删除")
//    @PostMapping("/delete/{id}")
//    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
//    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
//        return R.ok(nonConfirmCollectionSecondDetailService.removeById(id));
//    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<NonConfirmCollectionSecondDetailDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(nonConfirmCollectionSecondDetailService.getNonConfirmCollectionSecondDetailDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<NonConfirmCollectionSecondDetailVO>> page(@RequestBody @Valid NonConfirmCollectionSecondDetailQueryDTO queryDTO) {
        return R.ok(nonConfirmCollectionSecondDetailService.selectPage(queryDTO));
    }

    /**
     * @description:未确认收款-汇总表-手工处理按钮
     **/
    @ApiOperation(value = "B02 查看手工处理页面-页面初始化查询/检索")
    @PostMapping("/queryManualProcess")
    public R<List<ManualProcessListDTO>> queryManualProcess(@RequestBody @Valid QueryManualProcessDTO params) {
        return R.ok(nonConfirmCollectionSecondDetailService.queryManualProcess(params));
    }

    @ApiOperation(value = "B02 查看手工处理页面-撤回")
    @PostMapping("/recall")
    public R<String> nonConfirmCollectionDetailRecall(@RequestBody @Valid NonConfirmCollectionDetailRecallInputDTO params) {
        if (params.getDetailIds() == null || params.getDetailIds().isEmpty()) {
            return R.fail("请选择待撤回的记录!");
        }

        return nonConfirmCollectionSecondDetailService.nonConfirmCollectionDetailRecall(params);
    }

    @ApiOperation(value = "B02 查看手工处理页面-删除")
    @PostMapping("/delete")
    public R<String> nonConfirmCollectionDetailDelete(@RequestBody @Valid NonConfirmCollectionDetailDeleteInputDTO params) {
        if (params.getDetailIds() == null || params.getDetailIds().isEmpty()) {
            return R.fail("请选择待删除的记录!");
        }

        return nonConfirmCollectionSecondDetailService.nonConfirmCollectionDetailDelete(params);
    }

    /**
     * @description: 未确认收款列表-查看详情
     **/
    @ApiOperation(value = "B03 查看详情页面（第二层）-页面初始化查询/检索")
    @PostMapping("/queryDetailPageData")
    public R<List<QueryDetailListDataDTO>> queryDetailPageData(@RequestBody @Valid QueryDetailDataDTO params) {
        return R.ok(nonConfirmCollectionSecondDetailService.queryDetailPageData(params));
    }

    /**
     * @description: 未确认收款列表-查看详情-查看明细-第三层详情页面数据查询
     **/
    @ApiOperation(value = "B04 明细页面（第三层）_查看详情或汇总页面下钻-页面初始化查询/检索")
    @PostMapping("/queryThirdDetailPageData")
    public R<List<QueryThirdDetailPageListDataDTO>> queryThirdDetailPageData(@RequestBody @Valid QueryThirdDetailPageDataDTO params) {
        return R.ok(nonConfirmCollectionSecondDetailService.queryThirdDetailPageData(params));
    }

    @ApiOperation(value = "B05 明细页面（第三层）_页签进入-页面初始化查询/检索")
    @PostMapping("/queryThirdDetailDataByPage")
    public R<IPage<QueryThirdDetailPageListDataDTO>> queryThirdDetailDataByPage(@RequestBody @Valid QueryThirdDetailPageDataDTO params) {
        return R.ok(nonConfirmCollectionSecondDetailService.queryThirdDetailDataByPage(params));
    }

    @ApiOperation(value = "审核通过验证")
    @PostMapping("/auditPass")
    public void pass(@RequestBody @Valid CommonApproveDTO approveDTO) {
        nonConfirmCollectionSecondDetailService.auditPass(approveDTO);
    }

    @ApiOperation(value = "B06 未确认收款下载表")
    @PostMapping("/downloadTable")
    public void downloadTable(HttpServletResponse response, @RequestBody @Valid QueryThirdDetailPageDataDTO queryDTO) {
        try {
            List<SelectDownloadDataDTO> list = nonConfirmCollectionSecondDetailService.queryDownloadData(queryDTO);
            ExcelUtil<SelectDownloadDataDTO> util = new ExcelUtil<>(SelectDownloadDataDTO.class);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("未确认收款下载表.xlsx", "utf8"));
            util.exportExcel(response, list, "未确认收款下载表");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}



