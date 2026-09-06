package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelManySheetUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.service.ITaOtherPayableDetailService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.utfinancing.financehub.engine.finance.service.ITaOtherPayableService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description :   TaOtherPayable控制器实现类
 * @Modified :
 */
@Api(tags = "ta其他应付款汇总")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/ta-other-payable")
public class TaOtherPayableController {

    private final ITaOtherPayableService taOtherPayableService;
    private final ITaOtherPayableDetailService taOtherPayableDetailService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody TaOtherPayableDTO dto) {
        return R.ok(taOtherPayableService.saveTaOtherPayable(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody TaOtherPayableDTO dto) {
        return R.ok(taOtherPayableService.updateTaOtherPayable(id, dto));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<TaOtherPayableDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(taOtherPayableService.getTaOtherPayableDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<TaOtherPayableVO>> page(@RequestBody @Valid TaOtherPayableQueryDTO queryDTO) {
        return R.ok(taOtherPayableService.selectPage(queryDTO));
    }


    @ApiOperation(value = "ta其他应付款导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid TaOtherPayableQueryDTO queryDTO) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("ta其他应付款.xlsx", "utf8"));

            Map<String, List<?>> map = new HashMap<>();
            List<TaOtherPayableVO> list1 = taOtherPayableService.selectList(queryDTO);
            List<Long> longRegistIdList = list1.stream().map(TaOtherPayableVO::getId).collect(Collectors.toList());
            TaOtherPayableDetailQueryDTO taOtherPayableDetailQueryDTO = new TaOtherPayableDetailQueryDTO();
            taOtherPayableDetailQueryDTO.setTaOtherPayableIdList(longRegistIdList);
            List<TaOtherPayableDetailVO> list2 = taOtherPayableDetailService.selectList(taOtherPayableDetailQueryDTO);
            String sheet1 = "1";
            String sheet2 = "2";
            map.put(sheet1, BeanUtil.copyToList(list1, TaOtherPayableExcelVO.class));
            map.put(sheet2, BeanUtil.copyToList(list2, TaOtherPayableDetailExcelVO.class));
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, TaOtherPayableExcelVO.class);
            mapClass.put(sheet2, TaOtherPayableDetailExcelVO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "汇总信息");
            sheetName.put(sheet2, "ta其他应付款明细");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            log.error("ta其他应付款导出失败", e);
            throw new ServiceException("ta其他应付款导出失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(taOtherPayableService.generateVoucher(ids, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("ta其他应付款导入模板.xlsx", "utf8"));

            Map<String, List<?>> map = new HashMap<>();
            String sheet1 = "1";
            map.put(sheet1, new ArrayList<>());
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.IMPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, TaOtherPayableDetailExcelDTO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "ta其他应付款明细");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "上传")
    @PostMapping("/importFile")
    public R<Boolean> importFile(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(taOtherPayableService.importFile(file));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        taOtherPayableService.batchDeleteVoucher(ids);
        return R.ok(taOtherPayableService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(taOtherPayableService.withdraw(ids));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(taOtherPayableService.delete(ids));
    }

}



