package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherCopyDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDetailDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherDetailExportDTO;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherExportVo;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherVO;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationDetailsExcelDTO;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationDetailsQueryDTO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationDetailsVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.utfinancing.financehub.engine.finance.service.IVoucherService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


/**
 * @Author : lixin
 * @Date : Create in 2023-09-01
 * @Description :   Voucher控制器实现类
 * @Modified :
 */
@Api(tags = "凭证")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/voucher")
public class VoucherController {

    private final IVoucherService voucherService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody VoucherDTO dto) {
        return R.ok(voucherService.saveVoucher(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody VoucherDTO dto) {
        return R.ok(voucherService.updateVoucher(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(voucherService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<VoucherDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(voucherService.getVoucherDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<VoucherVO>> page(@RequestBody @Valid VoucherQueryDTO queryDTO) {
        return R.ok(voucherService.selectPage(queryDTO));
    }


    @ApiOperation(value = "凭证查询")
    @PostMapping("/query")
    public R<IPage<VoucherDetailDTO>> query(@RequestBody @Valid VoucherQueryDTO queryDTO) {
        return R.ok(voucherService.queryVoucherPage(queryDTO));
    }


    @ApiOperation(value = "凭证号分页列表")
    @PostMapping("/voucherNumberPage")
    public R<IPage<VoucherVO>> voucherNumberPage(@RequestBody @Valid VoucherQueryDTO queryDTO) {
        return R.ok(voucherService.voucherNumberPage(queryDTO));
    }

    @ApiOperation(value = "下载")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid VoucherQueryDTO queryDTO) {
        try {
            ExcelUtil<VoucherDetailExportDTO> util = new ExcelUtil<VoucherDetailExportDTO>(VoucherDetailExportDTO.class);
            List<VoucherDetailExportDTO> exportDTOList = voucherService.selectAllVoucerDetails(queryDTO);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("凭证导出.xlsx", "utf8"));
            util.exportExcel(response, exportDTOList, "凭证导出");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("导出失败失败原因：" + e.getMessage());
        }
    }

    @ApiOperation(value = "凭证汇总分页查询")
    @PostMapping("/summaryByPage")
    public R<IPage<VoucherDetailDTO>> summaryByPage(@RequestBody @Valid VoucherQueryDTO queryDTO) {
        return R.ok(voucherService.summaryByPage(queryDTO));

    }

    @ApiOperation(value = "凭证汇总下载")
    @PostMapping("/summary/export")
    public void summaryExport(HttpServletResponse response, @RequestBody @Valid VoucherQueryDTO queryDTO) {
        try {
            ExcelUtil<VoucherExportVo> util = new ExcelUtil<>(VoucherExportVo.class);
            List<VoucherExportVo> exportDTOList = voucherService.summaryExport(queryDTO);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("凭证导出.xlsx", "utf8"));
            util.exportExcel(response, exportDTOList, "凭证导出");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("导出失败失败原因：" + e.getMessage());
        }
    }

    @ApiOperation(value = "冲销")
    @PostMapping("/writeOff")
    public R<Boolean> writeOff(@RequestBody @Valid List<VoucherCopyDTO> copyDTOList) {
        return R.ok(voucherService.writeOff(copyDTOList));
    }

    @ApiOperation(value = "复制")
    @PostMapping("/copy")
    public R<Boolean> copy(@RequestBody @Valid List<VoucherCopyDTO> copyDTOList) {
        return R.ok(voucherService.copy(copyDTOList));
    }

}



