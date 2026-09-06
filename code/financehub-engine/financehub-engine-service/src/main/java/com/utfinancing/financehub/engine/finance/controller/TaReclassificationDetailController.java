package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelManySheetUtil;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.TaReclassificationDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TaReclassificationDetailDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TaReclassificationQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.utfinancing.financehub.engine.finance.service.ITaReclassificationDetailService;


/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description :   TaReclassificationDetail控制器实现类
 * @Modified :
 */
@Api(tags = "ta重分类明细表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/ta-reclassification-detail")
public class TaReclassificationDetailController {

    private final ITaReclassificationDetailService  taReclassificationDetailService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody TaReclassificationDetailDTO dto) {
        return R.ok(taReclassificationDetailService.saveTaReclassificationDetail(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody TaReclassificationDetailDTO dto) {
        return R.ok(taReclassificationDetailService.updateTaReclassificationDetail(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(taReclassificationDetailService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<TaReclassificationDetailDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(taReclassificationDetailService.getTaReclassificationDetailDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<TaReclassificationDetailVO>> page(@RequestBody @Valid TaReclassificationDetailQueryDTO queryDTO) {
        return R.ok(taReclassificationDetailService.selectPage(queryDTO));
    }

    @ApiOperation(value = "ta重分类-下载明细表")
    @PostMapping("/exportDetail")
    public void exportDetail(HttpServletResponse response, @RequestBody @Valid TaReclassificationDetailQueryDTO queryDTO) {
        try {
            List<TaReclassificationDetailVO> list1 = taReclassificationDetailService.selectList(queryDTO);
            ExcelUtil<TaReclassificationDetailExcelVO> util = new ExcelUtil<TaReclassificationDetailExcelVO>(TaReclassificationDetailExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("ta重分类明细.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list1, TaReclassificationDetailExcelVO.class), "ta重分类明细");
        } catch (UnsupportedEncodingException e) {
            log.error("ta重分类导出失败", e);
            throw new ServiceException("ta重分类导出失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "ta重分类-下载统计表")
    @PostMapping("/exportSummary")
    public void exportSummary(HttpServletResponse response, @RequestBody @Valid TaReclassificationDetailQueryDTO queryDTO) {
        try {
            List<TaReclassificationDetailVO> list1 = taReclassificationDetailService.selectList(queryDTO);
            ExcelUtil<TaReclassificationDetailExcelVO> util = new ExcelUtil<TaReclassificationDetailExcelVO>(TaReclassificationDetailExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("ta重分类明细.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list1, TaReclassificationDetailExcelVO.class), "ta重分类明细");
        } catch (UnsupportedEncodingException e) {
            log.error("ta重分类导出失败", e);
            throw new ServiceException("ta重分类导出失败，失败原因:" + e.getMessage());
        }
    }
}



