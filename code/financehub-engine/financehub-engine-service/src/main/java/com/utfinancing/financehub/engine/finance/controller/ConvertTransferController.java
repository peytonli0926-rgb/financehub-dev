package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelManySheetUtil;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferDetailExcelDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferExcelDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferPlanExcelDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferDetailExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferVO;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferDetailService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferService;
import com.utfinancing.financehub.engine.utils.ExcelExportUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description :   ConvertTransfer控制器实现类
 * @Modified :
 */
@Api(tags = "资产转让-折价转让-汇总")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/convert-transfer")
public class ConvertTransferController {

    private final IConvertTransferService convertTransferService;
    private final IConvertTransferDetailService iConvertTransferDetailService;

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ConvertTransferVO>> page(@RequestBody @Valid ConvertTransferQueryDTO queryDTO) {
        return R.ok(convertTransferService.selectPage(queryDTO));
    }

    @ApiOperation(value = "导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid ConvertTransferQueryDTO queryDTO) throws UnsupportedEncodingException {
        ExcelExportUtil.setResponse(response, "资产转让-折价转让.xlsx");
        try {
            Map<String, List<?>> map = new HashMap<>();
            List<ConvertTransferExcelVO> list1 = convertTransferService.selectList(queryDTO);
            List<ConvertTransferDetailExcelVO> list2 = iConvertTransferDetailService.selectList(queryDTO.getIdList());
            String sheet1 = "1";
            String sheet2 = "2";
            map.put(sheet1, list1);
            map.put(sheet2, list2);
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, ConvertTransferExcelVO.class);
            mapClass.put(sheet2, ConvertTransferDetailExcelVO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "折价转让基本信息");
            sheetName.put(sheet2, "折价转让详情");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (Exception e) {
            log.error("export error", e);
            throw new ServiceException("导出数据失败，失败原因：" + e.getMessage());
        }
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("折价转让导入模板.xlsx", "utf8"));
            Map<String, List<?>> map = new HashMap<>();
            String sheet1 = "1";
            String sheet2 = "2";
            String sheet3 = "3";
            map.put(sheet1, new ArrayList<>());
            map.put(sheet2, new ArrayList<>());
            map.put(sheet3, new ArrayList<>());
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, ConvertTransferExcelDTO.class);
            mapClass.put(sheet2, ConvertTransferDetailExcelDTO.class);
            mapClass.put(sheet3, ConvertTransferPlanExcelDTO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "折价转让基本信息");
            sheetName.put(sheet2, "折价转让详情");
            sheetName.put(sheet3, "折价转让租金计划");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            log.error("export error", e);
            throw new ServiceException("下载出表模板失败，失败原因：" + e.getMessage());
        }
    }


    @ApiOperation(value = "上传")
    @PostMapping("/importFile")
    public R<Boolean> importFile(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) throws IOException {
        return R.ok(convertTransferService.importFile(file));
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(convertTransferService.generateVoucher(ids));
    }


    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(convertTransferService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(convertTransferService.withdraw(ids));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(convertTransferService.delete(ids));
    }

    @ApiOperation(value = "转让批次下拉列表")
    @PostMapping("/batchList")
    public R<List<String>> batchList() {
        return R.ok(convertTransferService.batchList());
    }


}



