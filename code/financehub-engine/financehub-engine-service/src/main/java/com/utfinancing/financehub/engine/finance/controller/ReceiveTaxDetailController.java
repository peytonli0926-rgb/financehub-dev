package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxDetailExcelDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ReceiveTaxDetailVO;
import com.utfinancing.financehub.engine.verification.model.dto.CourtCostDetailsExcelDTO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IReceiveTaxDetailService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description :   ReceiveTaxDetail控制器实现类
 * @Modified :
 */
@Api(tags = "应交销项税发票明细")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/receive-tax-detail")
public class ReceiveTaxDetailController {

    private final IReceiveTaxDetailService  receiveTaxDetailService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ReceiveTaxDetailDTO dto) {
        return R.ok(receiveTaxDetailService.saveReceiveTaxDetail(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ReceiveTaxDetailDTO dto) {
        return R.ok(receiveTaxDetailService.updateReceiveTaxDetail(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(receiveTaxDetailService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ReceiveTaxDetailDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(receiveTaxDetailService.getReceiveTaxDetailDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ReceiveTaxDetailVO>> page(@RequestBody @Valid ReceiveTaxDetailQueryDTO queryDTO) {
        return R.ok(receiveTaxDetailService.selectPage(queryDTO));
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<ReceiveTaxDetailExcelDTO> util = new ExcelUtil<ReceiveTaxDetailExcelDTO>(ReceiveTaxDetailExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("应交销项税发票明细.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), ReceiveTaxDetailExcelDTO.class), "应交销项税发票明细");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:"+e.getMessage());
        }
    }

    @ApiOperation(value = "上传")
    @PostMapping("/importFile")
    public R<Boolean> importFile(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(receiveTaxDetailService.importFile(file));
    }

}



