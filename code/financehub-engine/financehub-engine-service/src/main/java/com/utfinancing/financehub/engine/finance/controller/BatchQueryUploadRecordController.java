package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.BatchQueryDataVO;
import com.utfinancing.financehub.engine.finance.service.IBatchQueryUploadNewService;
import com.utfinancing.financehub.engine.hthx.page.PageResult;
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
import java.util.Map;

import com.utfinancing.financehub.engine.finance.service.IBatchQueryUploadRecordService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : jnc
 * @Date : Create in 2024-05-14
 * @Description :   BatchQueryUploadRecord控制器实现类
 * @Modified :
 */
@Api(tags = "批量查询")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/batch-query-upload-record")
public class BatchQueryUploadRecordController {

    private final IBatchQueryUploadRecordService  batchQueryUploadRecordService;

    @Autowired
    IBatchQueryUploadNewService batchQueryUploadNewService;

    @ApiOperation(value = "下载批量查询上传文件模板")
    @PostMapping("/exportTemplate")
    public void export(HttpServletResponse response) {
        try {
            ExcelUtil<BatchQueryUploadExcelDTO> util = new ExcelUtil<BatchQueryUploadExcelDTO>(BatchQueryUploadExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("批量查询模板.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), BatchQueryUploadExcelDTO.class), "批量查询模板");
        } catch (UnsupportedEncodingException e) {
            log.error("export error", e);
        }
    }

    /**
     * @description:查询功能-批量查询-上传按钮
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "导入批量查询模板数据")
    @PostMapping("/importTemplate")
    public R<Boolean> importTemplate(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(batchQueryUploadNewService.importTemplate(file));
    }

    /**
     * @description:查询功能-批量查询-列表查询
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "批量查询-分页查询")
    @PostMapping("/page")
    public R page(@RequestBody @Valid BatchQueryUploadQueryDTO queryDTO) {
        try {
            return R.ok(batchQueryUploadNewService.selectPage(queryDTO));
        }catch (Exception e){
            log.info("批量查询功能列表异常：{}",e.getMessage());
            return R.fail(e.getMessage());
        }
    }

    /**
     * @description:查询功能-批量查询-下载-下载原表+下载详情
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "生成批量查询数据excel")
    @PostMapping("/data/export")
    public R<Map<String, String>> generateBatchQueryReportExcel(@RequestBody @Valid BatchQueryUploadQueryDTO queryDTO) {
        return R.ok(batchQueryUploadNewService.generateBatchQueryReportExcel(queryDTO));
    }

    @ApiOperation(value = "批量查询-上传文件查询")
    @PostMapping("/fileList")
    public R<IPage<FileRecordEntity>> selectBatchQueryFileList(@RequestBody @Valid FileRecordQueryDTO queryDTO) {
        return R.ok(batchQueryUploadRecordService.selectBatchQueryFileList(queryDTO));
    }

    @ApiOperation(value = "批量查询-表头查询")
    @PostMapping("/header")
    public R<List<Map<String, String>>> selectHeaderList() {
        return R.ok(batchQueryUploadNewService.selectHeaderList());
    }
}



