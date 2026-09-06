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
import com.utfinancing.financehub.engine.finance.service.ITaReclassificationDetailService;
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

import com.utfinancing.financehub.engine.finance.service.ITaReclassificationService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : wenbin
 * @Date : Create in 2024-05-24
 * @Description :   TaReclassification控制器实现类
 * @Modified :
 */
@Api(tags = "ta重分类")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/ta-reclassification")
public class TaReclassificationController {

    private final ITaReclassificationService  taReclassificationService;
    private final ITaReclassificationDetailService iTaReclassificationDetailService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody TaReclassificationDTO dto) {
        return R.ok(taReclassificationService.saveTaReclassification(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody TaReclassificationDTO dto) {
        return R.ok(taReclassificationService.updateTaReclassification(id, dto));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<TaReclassificationDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(taReclassificationService.getTaReclassificationDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<TaReclassificationVO>> page(@RequestBody @Valid TaReclassificationQueryDTO queryDTO) {
        return R.ok(taReclassificationService.selectPage(queryDTO));
    }


    @ApiOperation(value = "ta重分类导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid TaReclassificationQueryDTO queryDTO) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("ta重分类.xlsx", "utf8"));

            Map<String, List<?>> map = new HashMap<>();
            List<TaReclassificationVO> list1 = taReclassificationService.selectList(queryDTO);
            List<Long> taReclassificationIdList = list1.stream().map(TaReclassificationVO::getId).collect(Collectors.toList());
            TaReclassificationDetailQueryDTO taReclassificationDetailQueryDTO = new TaReclassificationDetailQueryDTO();
            taReclassificationDetailQueryDTO.setTaReclassificationIdList(taReclassificationIdList);
            List<TaReclassificationDetailVO> list2 = iTaReclassificationDetailService.selectList(taReclassificationDetailQueryDTO);
            String sheet1 = "1";
            String sheet2 = "2";
            map.put(sheet1, BeanUtil.copyToList(list1, TaReclassificationExcelVO.class));
            map.put(sheet2, BeanUtil.copyToList(list2, TaReclassificationDetailExcelVO.class));
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, TaReclassificationExcelVO.class);
            mapClass.put(sheet2, TaReclassificationDetailExcelVO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "汇总信息");
            sheetName.put(sheet2, "ta重分类明细");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            log.error("ta重分类导出失败", e);
            throw new ServiceException("ta重分类导出失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(taReclassificationService.generateVoucherBatch(ids, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("ta重分类导入模板.xlsx", "utf8"));

            Map<String, List<?>> map = new HashMap<>();
            String sheet1 = "1";
            map.put(sheet1, new ArrayList<>());
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.IMPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, TaReclassificationExcelDTO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "ta重分类明细");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "上传")
    @PostMapping("/importFile")
    public R<Boolean> importFile(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(taReclassificationService.importFile(file));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
//        taReclassificationService.batchDeleteVoucher(ids);
        return R.ok(taReclassificationService.submitBatch(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(taReclassificationService.withdraw(ids));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(taReclassificationService.delete(ids));
    }

    @ApiOperation(value = "同步TA数据")
    @PostMapping("/syncData")
    public R<Boolean> syncData(@RequestParam @ApiParam(value = "重分类日期") String businessDate) {
        return R.ok(taReclassificationService.syncData(businessDate));
    }


}



