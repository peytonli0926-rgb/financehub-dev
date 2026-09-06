package com.utfinancing.financehub.engine.finance.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferOtherDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferOtherPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferOtherQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferOtherExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferOtherPlanVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferOtherVO;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferOtherService;
import com.utfinancing.financehub.engine.utils.ExcelExportUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Api(tags = "资产转让 - 其他 - 转让")
@RestController
@RequestMapping("/finance/converter-transfer-other")
public class ConvertTransferOtherController {

    private final IConvertTransferOtherService transferOtherService;

    public ConvertTransferOtherController(IConvertTransferOtherService transferOtherService) {
        this.transferOtherService = transferOtherService;
    }

    @ApiOperation("下载导入模板")
    @PostMapping("/download-template")
    public void downloadTemplate(@ApiIgnore HttpServletResponse response) {
        ExcelExportUtil.export(response, Collections.emptyList(), ConvertTransferOtherDTO.class, "资产转让-其他-转让");
    }

    @ApiOperation("导入基本信息")
    @PostMapping("/importExcel")
    public R<Boolean> importExcel(@RequestBody MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelUtil<ConvertTransferOtherDTO> util = new ExcelUtil<>(ConvertTransferOtherDTO.class);
        List<ConvertTransferOtherDTO> dtos;
        try {
            dtos = util.importExcel(inputStream);
        } catch (Exception e) {
            return R.fail("excel 解析失败");
        }

        transferOtherService.importExcel(dtos);

        return R.ok(Boolean.TRUE);
    }

    @ApiOperation("分页列表")
    @PostMapping("/page")
    public R<IPage<ConvertTransferOtherVO>> page(@RequestBody ConvertTransferOtherQueryDTO dto) {
        IPage<ConvertTransferOtherVO> convert = transferOtherService.pageQuery(dto);
        return R.ok(convert);
    }

    @ApiOperation("导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody ConvertTransferOtherQueryDTO dto, @ApiIgnore HttpServletResponse response) {
        List<ConvertTransferOtherExcelVO> convert = transferOtherService.listQuery(dto);
        ExcelExportUtil.export(response, convert, ConvertTransferOtherExcelVO.class, "资产转让-其他-转让");
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(transferOtherService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(transferOtherService.withdraw(ids));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(transferOtherService.delete(ids));
    }


    @ApiOperation("租金计划-列表")
    @PostMapping("/plan")
    public R<IPage<ConvertTransferOtherPlanVO>> planPage(@RequestBody ConvertTransferOtherPlanQueryDTO dto) {
        return R.ok(transferOtherService.planPage(dto));
    }

    @ApiOperation("租金计划-列表")
    @PostMapping("/plan-export")
    public void planExport(@RequestBody ConvertTransferOtherPlanQueryDTO dto, @ApiIgnore HttpServletResponse response) {
        dto.setPageNum(-1);
        IPage<ConvertTransferOtherPlanVO> page = transferOtherService.planPage(dto);
        ExcelExportUtil.export(response, page.getRecords(), ConvertTransferOtherPlanVO.class, "资产转让-其他-合同租金计划");
    }
}
