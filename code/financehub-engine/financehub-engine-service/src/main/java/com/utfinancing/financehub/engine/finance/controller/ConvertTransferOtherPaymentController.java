package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferOtherPaymentEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferOtherPaymentDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferOtherPaymentQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferOtherPaymentExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferOtherPaymentVO;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferOtherPaymentService;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Api(tags = "资产转让 - 其他 - 转付")
@RestController
@RequestMapping("/finance/converter-transfer-other-payment")
public class ConvertTransferOtherPaymentController {

    private final IConvertTransferOtherPaymentService transferOtherPaymentService;

    public ConvertTransferOtherPaymentController(IConvertTransferOtherPaymentService transferOtherPaymentService) {
        this.transferOtherPaymentService = transferOtherPaymentService;
    }

    @ApiOperation("下载导入模板")
    @PostMapping("/download-template")
    public void downloadTemplate(@ApiIgnore HttpServletResponse response) {
        ExcelExportUtil.export(response, Collections.emptyList(), ConvertTransferOtherPaymentDTO.class, "资产转让-其他-转付");
    }


    @ApiOperation("导入实付")
    @PostMapping("/import-payment")
    public R<Boolean> importPayment(@RequestBody MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelUtil<ConvertTransferOtherPaymentDTO> util = new ExcelUtil<>(ConvertTransferOtherPaymentDTO.class);
        List<ConvertTransferOtherPaymentDTO> dtos;
        try {
            dtos = util.importExcel(inputStream);
        } catch (Exception e) {
            return R.fail("excel 解析失败");
        }

        transferOtherPaymentService.importPayment(dtos);

        return R.ok(Boolean.TRUE);
    }

    @ApiOperation("导入计划")
    @PostMapping("/import-plan")
    public R<Boolean> importPlan(@RequestBody MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelUtil<ConvertTransferOtherPaymentDTO> util = new ExcelUtil<>(ConvertTransferOtherPaymentDTO.class);
        List<ConvertTransferOtherPaymentDTO> dtos;
        try {
            dtos = util.importExcel(inputStream);
        } catch (Exception e) {
            return R.fail("excel 解析失败");
        }

        transferOtherPaymentService.importPlan(dtos);

        return R.ok(Boolean.TRUE);
    }

    @ApiOperation("分页列表")
    @PostMapping("/page")
    public R<IPage<ConvertTransferOtherPaymentVO>> page(@RequestBody ConvertTransferOtherPaymentQueryDTO dto) {
        IPage<ConvertTransferOtherPaymentVO> convert = transferOtherPaymentService.lambdaQuery()
                .in(StringUtils.isNotEmpty(dto.getContractCodes()), ConvertTransferOtherPaymentEntity::getContractCode, dto.getContractCodes())
                .between(Objects.nonNull(dto.getAccountStartDate()) && Objects.nonNull(dto.getAccountEndDate()), ConvertTransferOtherPaymentEntity::getAccountDate, dto.getAccountStartDate(), dto.getAccountEndDate())
                .ge(Objects.nonNull(dto.getAccountStartDate()) && Objects.isNull(dto.getAccountEndDate()), ConvertTransferOtherPaymentEntity::getAccountDate, dto.getAccountStartDate())
                .le(Objects.isNull(dto.getAccountStartDate()) && Objects.nonNull(dto.getAccountEndDate()), ConvertTransferOtherPaymentEntity::getAccountDate, dto.getAccountEndDate()).page(new Page<>(dto.getPageNum(), dto.getPageSize()))
                .convert(ConvertTransferOtherPaymentVO::new);
        return R.ok(convert);
    }

    @ApiOperation("导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody ConvertTransferOtherPaymentQueryDTO dto, @ApiIgnore HttpServletResponse response) {
        List<ConvertTransferOtherPaymentExcelVO> convert = transferOtherPaymentService.lambdaQuery()
                .between(Objects.nonNull(dto.getAccountStartDate()) && Objects.nonNull(dto.getAccountEndDate()), ConvertTransferOtherPaymentEntity::getAccountDate, dto.getAccountStartDate(), dto.getAccountEndDate())
                .ge(Objects.nonNull(dto.getAccountStartDate()) && Objects.isNull(dto.getAccountEndDate()), ConvertTransferOtherPaymentEntity::getAccountDate, dto.getAccountStartDate())
                .le(Objects.isNull(dto.getAccountStartDate()) && Objects.nonNull(dto.getAccountEndDate()), ConvertTransferOtherPaymentEntity::getAccountDate, dto.getAccountEndDate())
                .list()
                .stream()
                .map(ConvertTransferOtherPaymentExcelVO::new)
                .collect(Collectors.toList());
        ExcelExportUtil.export(response, convert, ConvertTransferOtherPaymentExcelVO.class, "资产转让-其他-转付");
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(transferOtherPaymentService.generateVoucher(ids));
    }


    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(transferOtherPaymentService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(transferOtherPaymentService.withdraw(ids));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(transferOtherPaymentService.delete(ids));
    }
}
