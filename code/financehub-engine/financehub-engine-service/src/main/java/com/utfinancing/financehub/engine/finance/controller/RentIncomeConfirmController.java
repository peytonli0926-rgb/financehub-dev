package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RentIncomeConfirmQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.RentIncomeConfirmDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionVO;
import com.utfinancing.financehub.engine.finance.model.vo.RentIncomeConfirmExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.RentIncomeConfirmVO;
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

import com.utfinancing.financehub.engine.finance.service.IRentIncomeConfirmService;


/**
 * @Author : wenbin
 * @Date : Create in 2024-04-10
 * @Description :   RentIncomeConfirm控制器实现类
 * @Modified :
 */
@Api(tags = "租金收入确认")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/rent-income-confirm")
public class RentIncomeConfirmController {

    private final IRentIncomeConfirmService rentIncomeConfirmService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody RentIncomeConfirmDTO dto) {
        return R.ok(rentIncomeConfirmService.saveRentIncomeConfirm(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody RentIncomeConfirmDTO dto) {
        return R.ok(rentIncomeConfirmService.updateRentIncomeConfirm(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(rentIncomeConfirmService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<RentIncomeConfirmDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(rentIncomeConfirmService.getRentIncomeConfirmDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<RentIncomeConfirmVO>> page(@RequestBody @Valid RentIncomeConfirmQueryDTO queryDTO) {
        return R.ok(rentIncomeConfirmService.selectPage(queryDTO));
    }

    @ApiOperation(value = "租金收入确认导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid RentIncomeConfirmQueryDTO queryDTO) {
        try {
            List<RentIncomeConfirmVO> list = rentIncomeConfirmService.selectList(queryDTO);
            ExcelUtil<RentIncomeConfirmExcelVO> util = new ExcelUtil<RentIncomeConfirmExcelVO>(RentIncomeConfirmExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("租金收入确认.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, RentIncomeConfirmExcelVO.class), "租金收入确认");
        } catch (UnsupportedEncodingException e) {
            log.error("租金收入确认导出失败", e);
            throw new ServiceException("租金收入确认导出失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "批量生成收入确认凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(rentIncomeConfirmService.generateVoucher(ids, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "批量生成结转凭证")
    @PostMapping("/generateCarryForwardVoucher")
    public R<Boolean> generateCarryForwardVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(rentIncomeConfirmService.generateCarryForwardVoucher(ids, YesOrNoEnum.NO.getCode()));
    }


    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(rentIncomeConfirmService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(rentIncomeConfirmService.withdraw(ids));
    }

}



