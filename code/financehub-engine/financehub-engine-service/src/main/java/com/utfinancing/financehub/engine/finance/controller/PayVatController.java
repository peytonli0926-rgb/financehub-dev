package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelManySheetUtil;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.InvoiceClaimDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PayVatQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PayVatDTO;
import com.utfinancing.financehub.engine.finance.model.vo.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.utfinancing.financehub.engine.finance.service.IPayVatService;


/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description :   PayVat控制器实现类
 * @Modified :
 */
@Api(tags = "应交增值税")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/pay-vat")
public class PayVatController {

    private final IPayVatService payVatService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody PayVatDTO dto) {
        return R.ok(payVatService.savePayVat(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody PayVatDTO dto) {
        return R.ok(payVatService.updatePayVat(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(payVatService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<PayVatDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(payVatService.getPayVatDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<PayVatVO>> page(@RequestBody @Valid PayVatQueryDTO queryDTO) {
        return R.ok(payVatService.selectPage(queryDTO));
    }

    /**
     * @description:增值税-应交增值税对账-数据同步
     **/
    @ApiOperation(value = "更新数据")
    @PostMapping("/payVatGetContract")
    public R<Boolean> payVatGetContract(@RequestParam("period") Integer period) {
        String msg = payVatService.payVatGetContract(period);
        return R.ok(Boolean.TRUE, msg);
    }

    @ApiOperation(value = "应交增值税导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid PayVatQueryDTO queryDTO) {
        try {
            List<PayVatVO> list = payVatService.selectList(queryDTO);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("应交增值税.xlsx", "utf8"));
            Map<String, List<?>> map = new HashMap<>();
            String sheet1 = "1";
            map.put(sheet1, BeanUtil.copyToList(list, PayVatExcelVO.class));
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, PayVatExcelVO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "应交增值税");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            log.error("应交增值税导出失败", e);
            throw new ServiceException("应交增值税导出失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(payVatService.generateVoucher(ids, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        // 1.先删除凭证
        payVatService.batchDeleteVoucher(ids);
        return R.ok(payVatService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(payVatService.withdraw(ids));
    }

    @PostMapping("/updateComments")
    @ApiOperation(value = "修改备注")
    public R<Void> updateComments(@RequestBody InvoiceClaimDTO dto) {
        payVatService.updateComments(dto);
        return R.ok();
    }
}



