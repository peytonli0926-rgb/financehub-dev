package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.PayVatQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReceiveTaxDTO;
import com.utfinancing.financehub.engine.finance.model.vo.PayVatExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.PayVatVO;
import com.utfinancing.financehub.engine.finance.model.vo.ReceiveTaxExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ReceiveTaxVO;
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
import com.utfinancing.financehub.engine.finance.service.IReceiveTaxService;


/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description :   ReceiveTax控制器实现类
 * @Modified :
 */
@Api(tags = "应交销项税")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/receive-tax")
public class ReceiveTaxController {

    private final IReceiveTaxService  receiveTaxService;
/*

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ReceiveTaxDTO dto) {
        return R.ok(receiveTaxService.saveReceiveTax(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ReceiveTaxDTO dto) {
        return R.ok(receiveTaxService.updateReceiveTax(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(receiveTaxService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ReceiveTaxDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(receiveTaxService.getReceiveTaxDTOById(id));
    }
*/

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ReceiveTaxVO>> page(@RequestBody @Valid ReceiveTaxQueryDTO queryDTO) {
        return R.ok(receiveTaxService.selectPage(queryDTO));
    }

    @ApiOperation(value = "应交销项税导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid ReceiveTaxQueryDTO queryDTO) {
        try {
            List<ReceiveTaxVO> list = receiveTaxService.selectList(queryDTO);
            ExcelUtil<ReceiveTaxExcelVO> util = new ExcelUtil<ReceiveTaxExcelVO>(ReceiveTaxExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("应交销项税.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, ReceiveTaxExcelVO.class), "应交销项税");
        } catch (UnsupportedEncodingException e) {
            log.error("应交销项税导出失败", e);
            throw new ServiceException("应交销项税导出失败，失败原因:" + e.getMessage());
        }
    }

}



