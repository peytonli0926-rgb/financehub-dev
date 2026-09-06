package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.LongIncomeConfirmQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LongIncomeConfirmDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LongReceivableRegisterQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LongIncomeConfirmExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.LongIncomeConfirmVO;
import com.utfinancing.financehub.engine.finance.model.vo.LongReceivableRegisterExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.LongReceivableRegisterVO;
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

import com.utfinancing.financehub.engine.finance.service.ILongIncomeConfirmService;


/**
 * @Author : wenbin
 * @Date : Create in 2024-04-15
 * @Description :   LongIncomeConfirm控制器实现类
 * @Modified :
 */
@Api(tags = "长期应收款-收入确认")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/long-income-confirm")
public class LongIncomeConfirmController {

    private final ILongIncomeConfirmService longIncomeConfirmService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody LongIncomeConfirmDTO dto) {
        return R.ok(longIncomeConfirmService.saveLongIncomeConfirm(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody LongIncomeConfirmDTO dto) {
        return R.ok(longIncomeConfirmService.updateLongIncomeConfirm(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(longIncomeConfirmService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<LongIncomeConfirmDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(longIncomeConfirmService.getLongIncomeConfirmDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<LongIncomeConfirmVO>> page(@RequestBody @Valid LongIncomeConfirmQueryDTO queryDTO) {
        return R.ok(longIncomeConfirmService.selectPage(queryDTO));
    }

    @ApiOperation(value = "长期应收款-收入确认导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid LongIncomeConfirmQueryDTO queryDTO) {
        try {
            List<LongIncomeConfirmVO> list = longIncomeConfirmService.selectList(queryDTO);
            ExcelUtil<LongIncomeConfirmExcelVO> util = new ExcelUtil<LongIncomeConfirmExcelVO>(LongIncomeConfirmExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("长期应收款-收入确认.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, LongIncomeConfirmExcelVO.class), "长期应收款-收入确认");
        } catch (UnsupportedEncodingException e) {
            log.error("长期应收款-收入确认导出失败", e);
            throw new ServiceException("长期应收款-收入确认导出失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(longIncomeConfirmService.generateVoucher(ids, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        // 1.先删除凭证
        longIncomeConfirmService.batchDeleteVoucher(ids);
        return R.ok(longIncomeConfirmService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(longIncomeConfirmService.withdraw(ids));
    }

}



