package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.PayableInsuranceDetailsVO;
import com.utfinancing.financehub.engine.finance.model.vo.PayableInsuranceVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.utfinancing.financehub.engine.finance.service.IPayableInsuranceService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : hzhao
 * @Date : Create in 2023-10-24
 * @Description :   PayableInsurance控制器实现类
 * @Modified :
 */
@Api(tags = "应付保险费表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/payable-insurance")
public class PayableInsuranceController {

    private final IPayableInsuranceService  payableInsuranceService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody PayableInsuranceDTO dto) {
        return R.ok(payableInsuranceService.savePayableInsurance(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody PayableInsuranceDTO dto) {
        return R.ok(payableInsuranceService.updatePayableInsurance(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(payableInsuranceService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<PayableInsuranceDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(payableInsuranceService.getPayableInsuranceDTOById(id));
    }

    @ApiOperation(value = "生成信息")
    @PostMapping("/generate")
    public R generate(@RequestBody @Valid PayableInsuranceQueryDTO queryDTO) {
        return R.ok(payableInsuranceService.generate(queryDTO));
    }

    @ApiOperation(value = "生成凭证")
    @PostMapping("/voucher")
    public R voucher(@RequestBody List<Long> ids) {
        payableInsuranceService.voucher(ids, YesOrNoEnum.NO.getCode());
        return R.ok();
    }
    @ApiOperation(value = "测试删除凭证")
    @PostMapping("/batchDeleteVoucher")
    public R batchDeleteVoucher(@RequestBody List<Long> ids) {
        payableInsuranceService.batchDeleteVoucher(ids);
        return R.ok();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<PayableInsuranceVO>> page(@RequestBody PayableInsuranceQueryDTO queryDTO) {
        return R.ok(payableInsuranceService.selectPage(queryDTO));
    }

    @ApiOperation(value = "按照查询条件-导出")
    @PostMapping("/export")
    public R<Map<String,String>> export(HttpServletResponse response, @RequestBody PayableInsuranceQueryDTO queryDTO) {
//        exportUtil(response, queryDTO);
        return R.ok(payableInsuranceService.export(queryDTO));
    }

    private void exportUtil(HttpServletResponse response, PayableInsuranceQueryDTO queryDTO) {
        try {
            List<PayableInsuranceDetailsVO> list = payableInsuranceService.selectDetailList(queryDTO);

            ExcelUtil<PayableInsuranceDetailExcel> util = new ExcelUtil<PayableInsuranceDetailExcel>(PayableInsuranceDetailExcel.class);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("应付保险费.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, PayableInsuranceDetailExcel.class), "应付保险费");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "详情分页查询")
    @PostMapping("/detail/page")
    public R<IPage<PayableInsuranceDetailsVO>> selectDetailPage(@RequestBody PayableInsuranceDetailsQueryDTO queryDTO) {
        return R.ok(payableInsuranceService.selectDetailPage(queryDTO));
    }

    @ApiOperation(value = "导出详情")
    @PostMapping("/detail/export")
    public void detailExport(HttpServletResponse response, @RequestBody PayableInsuranceQueryDTO queryDTO) {
        exportUtil(response, queryDTO);
    }

    @ApiOperation(value = "模板下载")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<PayableInsuranceDetailImport> util = new ExcelUtil<PayableInsuranceDetailImport>(PayableInsuranceDetailImport.class);
        util.importTemplateExcel(response, "sheet1");
    }

    @ApiOperation(value = "上传")
    @PostMapping("/importData")
    public R importData(MultipartFile file) throws Exception {
        ExcelUtil<PayableInsuranceDetailImport> util = new ExcelUtil<PayableInsuranceDetailImport>(PayableInsuranceDetailImport.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<PayableInsuranceDetailImport> list = util.importExcel(inputStream);
            payableInsuranceService.importData(list);
            return R.ok();
        } catch (Exception e) {
            log.error("应付保险费上传报错",e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @ApiOperation(value = "提交")
    @PostMapping("/submit")
    public R submit(@RequestBody List<Long> ids) {
        // 1.先删除凭证
        payableInsuranceService.batchDeleteVoucher(ids);
        // 2.再提交
        return R.ok(payableInsuranceService.submit(ids));
    }

    @ApiOperation(value = "撤回")
    @PostMapping("/withdraw")
    public R withdraw(@RequestBody List<Long> ids) {
        return R.ok(payableInsuranceService.withdraw(ids));
    }

    @ApiOperation(value = "复核通过")
    @PostMapping("/check/pass")
    public R pass(@RequestBody List<Long> ids) {
        return R.ok(payableInsuranceService.pass(ids));
    }

    @ApiOperation(value = "复核失败")
    @PostMapping("/check/fail")
    public R fail(@RequestBody List<Long> ids) {
        return R.ok(payableInsuranceService.fail(ids));
    }


}



