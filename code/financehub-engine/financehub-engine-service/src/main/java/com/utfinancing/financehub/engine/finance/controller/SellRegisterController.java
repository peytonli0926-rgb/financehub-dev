package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.SellRegisterExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.SellRegisterVO;
import com.utfinancing.financehub.engine.finance.model.vo.TransferRegisterExcelVO;
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
import com.utfinancing.financehub.engine.finance.service.ISellRegisterService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : wenbin
 * @Date : Create in 2024-04-03
 * @Description :   SellRegister控制器实现类
 * @Modified :
 */
@Api(tags = "出售登记")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/sell-register")
public class SellRegisterController {

    private final ISellRegisterService  sellRegisterService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody SellRegisterDTO dto) {
        return R.ok(sellRegisterService.saveSellRegister(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody SellRegisterDTO dto) {
        return R.ok(sellRegisterService.updateSellRegister(id, dto));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<SellRegisterDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sellRegisterService.getSellRegisterDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<SellRegisterVO>> page(@RequestBody @Valid SellRegisterQueryDTO queryDTO) {
        return R.ok(sellRegisterService.selectPage(queryDTO));
    }

    @ApiOperation(value = "出售登记导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid SellRegisterQueryDTO queryDTO) {
        try {
            List<SellRegisterVO> list = sellRegisterService.selectList(queryDTO);
            ExcelUtil<SellRegisterExcelVO> util = new ExcelUtil<SellRegisterExcelVO>(SellRegisterExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("出售登记.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, SellRegisterExcelVO.class), "出售登记");
        } catch (UnsupportedEncodingException e) {
            log.error("出售登记提导出失败", e);
            throw new ServiceException("出售登记导出失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<SellRegisterExcelDTO> util = new ExcelUtil<SellRegisterExcelDTO>(SellRegisterExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("出售登记导入模板.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), SellRegisterExcelDTO.class), "出售登记");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:"+e.getMessage());
        }
    }

    @ApiOperation(value = "上传")
    @PostMapping("/importFile")
    public R<Boolean> importFile(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(sellRegisterService.importFile(file));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(sellRegisterService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(sellRegisterService.withdraw(ids));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(sellRegisterService.delete(ids));
    }

}



