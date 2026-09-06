package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelManySheetUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.utfinancing.financehub.engine.finance.service.IParityTransferService;
import org.springframework.web.multipart.MultipartFile;


/**
 * 资产转让 - 平价转让 - 转让
 * @Author : bruyang
 * @Date : Create in 2024-04-02
 * @Description :   ParityTransfer控制器实现类
 * @Modified :
 */
@Api(tags = "平价转让")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/parity-transfer")
public class ParityTransferController {

    private final IParityTransferService  parityTransferService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ParityTransferDTO dto) {
        return R.ok(parityTransferService.saveParityTransfer(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ParityTransferDTO dto) {
        return R.ok(parityTransferService.updateParityTransfer(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(parityTransferService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ParityTransferDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(parityTransferService.getParityTransferDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ParityTransferVO>> page(@RequestBody @Valid ParityTransferQueryDTO queryDTO) {
        return R.ok(parityTransferService.selectPage(queryDTO));
    }

    @ApiOperation(value = "转让批次下拉列表")
    @PostMapping("/batchList")
    public R<List<String>> batchList(){
        return R.ok(parityTransferService.batchList());
    }

    @ApiOperation(value = "导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @ApiParam(value = "id集合") List<Long> idList) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("平价转让.xlsx", "utf8"));
            Map<String, List<?>> map = new HashMap<>();
            List<ParityTransferExportVO> parityTransferExportList = parityTransferService.selectPartityTransferList(idList);
            List<ParityTransferDetailExportVO> parityTransferDetailExportVOList = parityTransferService.selectPartityTransferDetailList(idList);
            String sheet1 = "1";
            String sheet2 = "2";
            map.put(sheet1, parityTransferExportList);
            map.put(sheet2, parityTransferDetailExportVOList);
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, ParityTransferExportVO.class);
            mapClass.put(sheet2, ParityTransferDetailExportVO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "平价转让基本信息");
            sheetName.put(sheet2, "平价转让详情");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            log.error("export error", e);
            throw new ServiceException("导出数据失败，失败原因："+e.getMessage());
        }
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/downLoad")
    public void downLoad(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("平价转让.xlsx", "utf8"));
            Map<String, List<?>> map = new HashMap<>();
            String sheet1 = "1";
            String sheet2 = "2";
            map.put(sheet1, new ArrayList<>());
            map.put(sheet2, new ArrayList<>());
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, ParityTransferExcelVO.class);
            mapClass.put(sheet2, ParityTransferDetailExcelVO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "平价转让基本信息");
            sheetName.put(sheet2, "平价转让详情");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            log.error("export error", e);
            throw new ServiceException("下载出表模板失败，失败原因："+e.getMessage());
        }
    }

    @ApiOperation(value = "导入")
    @PostMapping("/importTemplate")
    public R<Boolean> importTemplate(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) throws IOException {
        return R.ok(parityTransferService.importTemplate(file));
    }
    @ApiOperation(value = "下载支付信息模板")
    @PostMapping("/downLoadPaymentInfo")
    public void downLoadPaymentInfo(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("平价转让支付信息.xlsx", "utf8"));
            Map<String, List<?>> map = new HashMap<>();
            String sheet1 = "1";
            map.put(sheet1, new ArrayList<>());
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, ParityTransferPaymentInfoExcelDTO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "平价转让支付信息");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            log.error("export error", e);
            throw new ServiceException("下载出表模板失败，失败原因："+e.getMessage());
        }
    }

    @ApiOperation(value = "上传支付信息")
    @PostMapping("/importPaymentInfo")
    public R<Boolean> importPaymentInfo(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) throws IOException {
        return R.ok(parityTransferService.importPaymentInfo(file));
    }

    @ApiOperation(value = "批量删除接口")
    @PostMapping("/deleteByIds")
    public R<Boolean> deleteByIds(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(parityTransferService.deleteByIds(ids));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(parityTransferService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(parityTransferService.withdraw(ids));
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(parityTransferService.generateVoucher(ids, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "更新核销状态")
    @PostMapping("/updateProcessStatus")
    public R<Boolean> updateProcessStatus(@RequestBody CommonApproveDTO approveDTO) {
        return R.ok(parityTransferService.updateProcessStatus(approveDTO));
    }

    @ApiOperation(value = "校验分页查询")
    @PostMapping("/checkPage")
    public R<IPage<ParityTransferCheckVO>> checkPage(@RequestBody @Valid CheckPageQueryDTO queryDTO) {
        return R.ok(parityTransferService.selectCheckPage(queryDTO));
    }

    @ApiOperation(value = "详情分页查询")
    @PostMapping("/detailPage")
    public R<IPage<ParityTransferDetailVO>> detailPage(@RequestBody @Valid ParityTransferDetailQueryDTO queryDTO) {
        return R.ok(parityTransferService.detailPage(queryDTO));
    }
}



