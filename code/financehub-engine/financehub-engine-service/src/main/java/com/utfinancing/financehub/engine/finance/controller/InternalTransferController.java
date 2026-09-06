package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionVO;
import com.utfinancing.financehub.engine.finance.model.vo.InternalTransferVO;
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
import com.utfinancing.financehub.engine.finance.service.IInternalTransferService;
import org.springframework.web.multipart.MultipartFile;


/**
 * 资产转让-平价转让-内部调拨
 *
 * @Author : wenbin
 * @Date : Create in 2024-04-17
 * @Description :   InternalTransfer控制器实现类
 * @Modified :
 */
@Api(tags = "资产转让-内部调拨")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/internal-transfer")
public class InternalTransferController {

    private final IInternalTransferService  internalTransferService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody InternalTransferDTO dto) {
        return R.ok(internalTransferService.saveInternalTransfer(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody InternalTransferDTO dto) {
        return R.ok(internalTransferService.updateInternalTransfer(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(internalTransferService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<InternalTransferDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(internalTransferService.getInternalTransferDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<InternalTransferVO>> page(@RequestBody @Valid InternalTransferQueryDTO queryDTO) {
        return R.ok(internalTransferService.selectPage(queryDTO));
    }

    @ApiOperation(value = "生成支付信息")
    @PostMapping("/generatePayment")
    public R<Boolean> generatePayment(@RequestBody @Valid InternalTransferGenerateDTO queryDTO) {
        return R.ok(internalTransferService.generatePayment(queryDTO));
    }

    @ApiOperation(value = "内部调拨导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid InternalTransferQueryDTO queryDTO) {
        try {
            List<InternalTransferVO> list = internalTransferService.selectList(queryDTO);
            ExcelUtil<ImpairmentProvisionExcelVO> util = new ExcelUtil<ImpairmentProvisionExcelVO>(ImpairmentProvisionExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("内部调拨.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, ImpairmentProvisionExcelVO.class), "内部调拨");
        } catch (UnsupportedEncodingException e) {
            log.error("内部调拨导出失败", e);
            throw new ServiceException("内部调拨导出失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<InternalTransferExcelDTO> util = new ExcelUtil<InternalTransferExcelDTO>(InternalTransferExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("内部调拨导入模板.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), InternalTransferExcelDTO.class), "内部调拨");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "上传")
    @PostMapping("/importFile")
    public R<Boolean> importFile(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(internalTransferService.importFile(file));
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(internalTransferService.generateVoucher(ids, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(internalTransferService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(internalTransferService.withdraw(ids));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(internalTransferService.delete(ids));
    }

}



