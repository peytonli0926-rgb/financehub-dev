package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.ManualVoucherQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ManualVoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CostGpsAndBraceleteFeeExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ManualVoucherExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ManualVoucherVO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
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
import com.utfinancing.financehub.engine.finance.service.IManualVoucherService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : bruyang
 * @Date : Create in 2024-01-03
 * @Description :   ManualVoucher控制器实现类
 * @Modified :
 */
@Api(tags = "手工凭证表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/manual-voucher")
public class ManualVoucherController {

    private final IManualVoucherService  manualVoucherService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ManualVoucherDTO dto) {
        return R.ok(manualVoucherService.saveManualVoucher(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ManualVoucherDTO dto) {
        return R.ok(manualVoucherService.updateManualVoucher(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(manualVoucherService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ManualVoucherDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(manualVoucherService.getManualVoucherDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ManualVoucherVO>> page(@RequestBody @Valid ManualVoucherQueryDTO queryDTO) {
        return R.ok(manualVoucherService.selectPage(queryDTO));
    }

}



