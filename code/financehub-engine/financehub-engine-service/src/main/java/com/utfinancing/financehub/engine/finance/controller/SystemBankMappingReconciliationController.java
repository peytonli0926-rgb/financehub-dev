package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.NoParamPageQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SystemBankMappingReconciliationDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SystemBankMappingReconciliationQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.SystemBankMappingReconciliationDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.SystemBankMappingReconciliationNoVO;
import com.utfinancing.financehub.engine.finance.model.vo.SystemBankMappingReconciliationVO;
import com.utfinancing.financehub.engine.finance.service.IFundBusinessSystemEbankMappingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 2999.99批扣映射表
 */
@Api(tags = "2999.99批扣对账")
@RestController
@RequestMapping("/finance/system-bank-mapping")
public class SystemBankMappingReconciliationController {

    private final IFundBusinessSystemEbankMappingService systemBankMappingReconciliationService;

    public SystemBankMappingReconciliationController(IFundBusinessSystemEbankMappingService systemBankMappingReconciliationService) {
        this.systemBankMappingReconciliationService = systemBankMappingReconciliationService;
    }

    @ApiOperation("已勾稽分页列表")
    @PostMapping("filter")
    public R<IPage<SystemBankMappingReconciliationVO>> systemBankMappingReconciliationFilter(@RequestBody SystemBankMappingReconciliationQueryDTO queryDto) {

        return R.ok(systemBankMappingReconciliationService.systemBankMappingReconciliationFilter(queryDto));
    }
    @ApiOperation("已勾稽列表导出")
    @PostMapping("filter/export")
    public void systemBankMappingReconciliationFilterExport(@RequestBody SystemBankMappingReconciliationQueryDTO dto, @ApiIgnore HttpServletResponse response) throws UnsupportedEncodingException {
        // 不分页标识
        dto.setPageNum(-1);
        IPage<SystemBankMappingReconciliationVO> reconciliationVOIPage = systemBankMappingReconciliationService.systemBankMappingReconciliationFilter(dto);

        ExcelUtil<SystemBankMappingReconciliationVO> excelUtil = new ExcelUtil<>(SystemBankMappingReconciliationVO.class);
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader(
                "Content-Disposition", "attachment; filename=" + URLEncoder.encode("2999.99批扣-已勾稽数据导出.xlsx", "utf-8"));
        excelUtil.exportExcel(response, reconciliationVOIPage.getRecords(), "2999.99批扣-已勾稽数据");
    }
    @ApiOperation("已勾稽 明细 分页列表")
    @PostMapping("detail")
    public R<IPage<SystemBankMappingReconciliationDetailVO>> systemBankMappingReconciliationDetail(@Validated @RequestBody SystemBankMappingReconciliationDetailQueryDTO dto) {
        return R.ok(systemBankMappingReconciliationService.systemBankMappingReconciliationDetailByMatchNumber(dto));
    }

    @ApiOperation("已勾稽 明细 导出")
    @PostMapping("detail/export")
    public void systemBankMappingReconciliationDetailExport(@Validated @RequestBody SystemBankMappingReconciliationDetailQueryDTO dto, @ApiIgnore HttpServletResponse response) throws UnsupportedEncodingException {
        // 不分页标识
        dto.setPageNum(-1);
        IPage<SystemBankMappingReconciliationDetailVO> detailVOIPage = systemBankMappingReconciliationService.systemBankMappingReconciliationDetailByMatchNumber(dto);


        ExcelUtil<SystemBankMappingReconciliationDetailVO> excelUtil = new ExcelUtil<>(SystemBankMappingReconciliationDetailVO.class);
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader(
                "Content-Disposition", "attachment; filename=" + URLEncoder.encode(dto.getMatchNumber() + "-批扣详情.xlsx", "utf-8"));
        excelUtil.exportExcel(response, detailVOIPage.getRecords(), dto.getMatchNumber() + "-批扣详情");
    }

    @ApiOperation("未勾稽分页列表")
    @PostMapping("filter/no")
    public R<IPage<SystemBankMappingReconciliationNoVO>> systemBankMappingReconciliationNoVO(@RequestBody NoParamPageQueryDTO dto) {
        return R.ok(systemBankMappingReconciliationService.systemBankMappingReconciliationNoVO(dto));
    }

    @ApiOperation("未勾稽列表导出")
    @PostMapping("filter/no/export")
    public void systemBankMappingReconciliationNoVOExport(@RequestBody NoParamPageQueryDTO dto, @ApiIgnore HttpServletResponse response) throws UnsupportedEncodingException {
        dto.setPageNum(-1);
        IPage<SystemBankMappingReconciliationNoVO> detailVOIPage = systemBankMappingReconciliationService.systemBankMappingReconciliationNoVO(dto);
        ExcelUtil<SystemBankMappingReconciliationNoVO> excelUtil = new ExcelUtil<>(SystemBankMappingReconciliationNoVO.class);
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader(
                "Content-Disposition", "attachment; filename=" + URLEncoder.encode("未勾稽数据.xlsx", "utf-8"));
        excelUtil.exportExcel(response, detailVOIPage.getRecords(), "未勾稽数据");
    }
}
