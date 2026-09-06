package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.TransferRegisterExcelDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TransferRegisterQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TransferRegisterDTO;
import com.utfinancing.financehub.engine.finance.model.vo.TransferRegisterExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.TransferRegisterVO;
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
import com.utfinancing.financehub.engine.finance.service.ITransferRegisterService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : wenbin
 * @Date : Create in 2024-04-03
 * @Description :   TransferRegister控制器实现类
 * @Modified :
 */
@Api(tags = "转入登记")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/transfer-register")
public class TransferRegisterController {

    private final ITransferRegisterService  transferRegisterService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody TransferRegisterDTO dto) {
        return R.ok(transferRegisterService.saveTransferRegister(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody TransferRegisterDTO dto) {
        return R.ok(transferRegisterService.updateTransferRegister(id, dto));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<TransferRegisterDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(transferRegisterService.getTransferRegisterDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<TransferRegisterVO>> page(@RequestBody @Valid TransferRegisterQueryDTO queryDTO) {
        return R.ok(transferRegisterService.selectPage(queryDTO));
    }

    @ApiOperation(value = "转入登记导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid TransferRegisterQueryDTO queryDTO) {
        try {
            List<TransferRegisterExcelVO> list = transferRegisterService.selectList(queryDTO);
            ExcelUtil<TransferRegisterExcelVO> util = new ExcelUtil<TransferRegisterExcelVO>(TransferRegisterExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("转入登记.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, TransferRegisterExcelVO.class), "转入登记");
        } catch (UnsupportedEncodingException e) {
            log.error("转入登记提导出失败", e);
            throw new ServiceException("转入登记导出失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<TransferRegisterExcelDTO> util = new ExcelUtil<TransferRegisterExcelDTO>(TransferRegisterExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("转入登记导入模板.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), TransferRegisterExcelDTO.class), "转入登记");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:"+e.getMessage());
        }
    }

    @ApiOperation(value = "上传")
    @PostMapping("/importFile")
    public R<Boolean> importFile(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(transferRegisterService.importFile(file));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(transferRegisterService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(transferRegisterService.withdraw(ids));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(transferRegisterService.delete(ids));
    }

}



