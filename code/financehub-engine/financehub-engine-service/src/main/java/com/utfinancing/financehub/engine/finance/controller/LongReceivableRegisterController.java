package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelManySheetUtil;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.service.ILongApportionService;
import com.utfinancing.financehub.engine.finance.service.ILongRepaymentPlanService;
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

import com.utfinancing.financehub.engine.finance.service.ILongReceivableRegisterService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : wenbin
 * @Date : Create in 2024-04-11
 * @Description :   LongReceivableRegister控制器实现类
 * @Modified :
 */
@Api(tags = "长期应收款登记")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/long-receivable-register")
public class LongReceivableRegisterController {

    private final ILongReceivableRegisterService  longReceivableRegisterService;
    private final ILongRepaymentPlanService iLongRepaymentPlanService;
    private final ILongApportionService iLongApportionService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody LongReceivableRegisterDTO dto) {
        return R.ok(longReceivableRegisterService.saveLongReceivableRegister(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody LongReceivableRegisterDTO dto) {
        return R.ok(longReceivableRegisterService.updateLongReceivableRegister(id, dto));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<LongReceivableRegisterDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(longReceivableRegisterService.getLongReceivableRegisterDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<LongReceivableRegisterVO>> page(@RequestBody @Valid LongReceivableRegisterQueryDTO queryDTO) {
        return R.ok(longReceivableRegisterService.selectPage(queryDTO));
    }

    @ApiOperation(value = "长期应收款登记导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid LongReceivableRegisterQueryDTO queryDTO) {
        try {
            // List<LongReceivableRegisterVO> list = longReceivableRegisterService.selectList(queryDTO);
            // ExcelUtil<LongReceivableRegisterExcelVO> util = new ExcelUtil<LongReceivableRegisterExcelVO>(LongReceivableRegisterExcelVO.class);
            // response.setContentType("application/octet-stream; charset=utf-8");
            // response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("长期应收款登记.xlsx", "utf8"));
            // util.exportExcel(response, BeanUtil.copyToList(list, LongReceivableRegisterExcelVO.class), "长期应收款登记");
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("长期应收款登记.xlsx", "utf8"));

            Map<String, List<?>> map = new HashMap<>();
            List<LongReceivableRegisterVO> list1 = longReceivableRegisterService.selectList(queryDTO);
            List<Long> longRegistIdList = list1.stream().map(LongReceivableRegisterVO::getId).collect(Collectors.toList());
            LongRepaymentPlanQueryDTO longRepaymentPlanQueryDTO = new LongRepaymentPlanQueryDTO();
            longRepaymentPlanQueryDTO.setLongRegisterIdList(longRegistIdList);
            List<LongRepaymentPlanVO> list2 = iLongRepaymentPlanService.selectList(longRepaymentPlanQueryDTO);
            LongApportionQueryDTO longApportionQueryDTO = new LongApportionQueryDTO();
            longApportionQueryDTO.setLongRegisterIdList(longRegistIdList);
            List<LongApportionVO> list3 = iLongApportionService.selectList(longApportionQueryDTO);
            String sheet1 = "1";
            String sheet2 = "2";
            String sheet3 = "3";
            map.put(sheet1, BeanUtil.copyToList(list1,LongReceivableRegisterExcelVO.class));
            map.put(sheet2, BeanUtil.copyToList(list2,LongRepaymentPlanExcelVO.class));
            map.put(sheet3, BeanUtil.copyToList(list3,LongApportionExcelVO.class));
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, LongReceivableRegisterExcelVO.class);
            mapClass.put(sheet2, LongRepaymentPlanExcelVO.class);
            mapClass.put(sheet3, LongApportionExcelVO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "基础信息");
            sheetName.put(sheet2, "偿还计划");
            sheetName.put(sheet3, "分摊表");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            log.error("长期应收款登记导出失败", e);
            throw new ServiceException("长期应收款登记导出失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(longReceivableRegisterService.generateVoucher(ids, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("长期应收款登记导入模板.xlsx", "utf8"));

            Map<String, List<?>> map = new HashMap<>();
            String sheet1 = "1";
            String sheet2 = "2";
            String sheet3 = "3";
            map.put(sheet1, new ArrayList<>());
            map.put(sheet2, new ArrayList<>());
            map.put(sheet3, new ArrayList<>());
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.IMPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, LongReceivableRegisterExcelDTO.class);
            mapClass.put(sheet2, LongRepaymentPlanExcelDTO.class);
            mapClass.put(sheet3, LongApportionExcelDTO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "基础信息");
            sheetName.put(sheet2, "偿还计划");
            sheetName.put(sheet3, "分摊表");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "上传")
    @PostMapping("/importFile")
    public R<Boolean> importFile(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(longReceivableRegisterService.importFile(file));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        // 1.先删除凭证
        longReceivableRegisterService.batchDeleteVoucher(ids);
        return R.ok(longReceivableRegisterService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(longReceivableRegisterService.withdraw(ids));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(longReceivableRegisterService.delete(ids));
    }

    @ApiOperation(value = "分摊")
    @PostMapping("/apportion")
    public R<Boolean> apportion(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(longReceivableRegisterService.apportion(ids));
    }

}



