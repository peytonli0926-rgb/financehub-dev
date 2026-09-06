package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartPaymentEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartPaymentDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartPaymentDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartPaymentGenerateDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartPaymentQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartPaymentDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartPaymentExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartPaymentVO;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferThirdPartPaymentService;
import com.utfinancing.financehub.engine.utils.ExcelExportUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.ServerException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Api(tags = "资产转让 - 第三方转付")
@RequestMapping("/finance/convert-transfer-third-payment")
public class ConvertTransferThirdPartPaymentController {

    private final IConvertTransferThirdPartPaymentService convertTransferThirdPartPaymentService;

    public ConvertTransferThirdPartPaymentController(IConvertTransferThirdPartPaymentService convertTransferThirdPartPaymentService) {
        this.convertTransferThirdPartPaymentService = convertTransferThirdPartPaymentService;
    }

    @PostMapping("/page")
    @ApiOperation("分页列表")
    public R<IPage<ConvertTransferThirdPartPaymentVO>> page(@RequestBody ConvertTransferThirdPartPaymentQueryDTO dto) {
        IPage<ConvertTransferThirdPartPaymentVO> page = convertTransferThirdPartPaymentService
                .lambdaQuery()
                .eq(StringUtils.isNotEmpty(dto.getBatch()), ConvertTransferThirdPartPaymentEntity::getBatch, dto.getBatch())
                .eq(Objects.nonNull(dto.getAccountDate()), ConvertTransferThirdPartPaymentEntity::getPaymentDate, dto.getAccountDate())
                .page(new Page<>(dto.getPageNum(), dto.getPageSize()))
                .convert(ConvertTransferThirdPartPaymentVO::form);
        return R.ok(page);
    }

    @ApiOperation("导出")
    @PostMapping("/export")
    public void export(@RequestBody @Valid ConvertTransferThirdPartPaymentQueryDTO dto, @ApiIgnore HttpServletResponse response) {
        List<ConvertTransferThirdPartPaymentExcelVO> vos = convertTransferThirdPartPaymentService
                .lambdaQuery()
                .eq(StringUtils.isNotEmpty(dto.getBatch()), ConvertTransferThirdPartPaymentEntity::getBatch, dto.getBatch())
                .eq(Objects.nonNull(dto.getAccountDate()), ConvertTransferThirdPartPaymentEntity::getPaymentDate, dto.getAccountDate())
                .list()
                .stream()
                .map(ConvertTransferThirdPartPaymentExcelVO::form)
                .collect(Collectors.toList());
        ExcelExportUtil.export(response, vos, ConvertTransferThirdPartPaymentExcelVO.class, "转付第三方");
    }

    @ApiOperation("生成支付信息")
    @PostMapping("/generate-payment")
    public R<Boolean> generate(@Valid @RequestBody ConvertTransferThirdPartPaymentGenerateDTO dto) {
        convertTransferThirdPartPaymentService.generate(dto);

        return R.ok(true);
    }

    @ApiOperation("下载导入模板")
    @PostMapping("/get-import-template")
    public void template(@ApiIgnore HttpServletResponse response) {
        ExcelExportUtil.export(response, Collections.emptyList(), ConvertTransferThirdPartPaymentDTO.class, "转付第三方-转付上传模板");
    }


    @ApiOperation("导入支付信息")
    @PostMapping("/import-payment")
    public R<Boolean> importPayment(@RequestBody MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelUtil<ConvertTransferThirdPartPaymentDTO> util = new ExcelUtil<>(ConvertTransferThirdPartPaymentDTO.class);
        List<ConvertTransferThirdPartPaymentDTO> dtos;
        try {
            dtos = util.importExcel(inputStream);
        } catch (Exception e) {
            throw new ServerException("上传支付信息excel解析失败：" + e.getMessage());
        }

        convertTransferThirdPartPaymentService.importPartPayment(dtos);

        return R.ok(true);
    }

    @ApiOperation("生成凭证")
    @PostMapping("/generate-voucher")
    public R<Boolean> generateVoucher(@RequestBody List<Long> paymentIds) {
        convertTransferThirdPartPaymentService.generateVoucher(paymentIds);
        return R.ok(true);
    }

    @ApiOperation("提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody List<Long> paymentIds) {
        convertTransferThirdPartPaymentService.submit(paymentIds);
        return R.ok(true);
    }

    @ApiOperation("撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody List<Long> paymentIds) {
        convertTransferThirdPartPaymentService.withdraw(paymentIds);
        return R.ok(true);
    }

    @ApiOperation("查看详细")
    @PostMapping("detail")
    public R<IPage<ConvertTransferThirdPartPaymentDetailVO>> detail(@Valid @RequestBody ConvertTransferThirdPartPaymentDetailQueryDTO dto) {
        IPage<ConvertTransferThirdPartPaymentDetailVO> page = convertTransferThirdPartPaymentService.detailPage(dto.getPaymentId(), dto.getPageNum(), dto.getPageSize());
        return R.ok(page);
    }

    @ApiOperation("详细导出")
    @PostMapping("detail/export")
    public void detailExport(@Valid @RequestBody ConvertTransferThirdPartPaymentDetailQueryDTO dto, @ApiIgnore HttpServletResponse response) {
        List<ConvertTransferThirdPartPaymentDetailVO> page = convertTransferThirdPartPaymentService.detailExport(dto.getPaymentId());
        ExcelExportUtil.export(response, page, ConvertTransferThirdPartPaymentDetailVO.class,"第三方转付支付信息明细");
    }


}
