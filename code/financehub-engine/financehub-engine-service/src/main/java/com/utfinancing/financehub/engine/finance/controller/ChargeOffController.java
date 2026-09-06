package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ChargeOffVO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationExcelDTO;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationPaybackQueryDTO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackDetailsVO;
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
import java.util.Map;

import com.utfinancing.financehub.engine.finance.service.IChargeOffService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : bruyang
 * @Date : Create in 2024-02-27
 * @Description :   ChargeOff控制器实现类
 * @Modified :
 */
@Api(tags = "Charge Off-Api")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/charge-off")
public class ChargeOffController {

    private final IChargeOffService  chargeOffService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ChargeOffDTO dto) {
        return R.ok(chargeOffService.saveChargeOff(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ChargeOffDTO dto) {
        return R.ok(chargeOffService.updateChargeOff(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(chargeOffService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ChargeOffDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(chargeOffService.getChargeOffDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ChargeOffVO>> page(@RequestBody @Valid ChargeOffQueryDTO queryDTO) {
        return R.ok(chargeOffService.selectPage(queryDTO));
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/download")
    public void download(HttpServletResponse response,@RequestBody @Valid ChargeOffQueryDTO queryDTO) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            String type =queryDTO.getType();
            if (StringUtils.isEmpty(type)) {
                throw new ServiceException("下载模板类型不可以为空");
            }
            if ("0".equals(type)) {
                ExcelUtil<ChargeOffAddExcelDTO> util = new ExcelUtil<ChargeOffAddExcelDTO>(ChargeOffAddExcelDTO.class);
                response.setHeader(
                        "Content-Disposition", "attachment; filename=" + URLEncoder.encode("ChargeOff新增模板.xlsx", "utf8"));
                util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), ChargeOffAddExcelDTO.class), "ChargeOff新增模板");
            }else if ("1".equals(type)) {
                ExcelUtil<ChargeOffModifyExcelDTO> util = new ExcelUtil<ChargeOffModifyExcelDTO>(ChargeOffModifyExcelDTO.class);
                response.setHeader(
                        "Content-Disposition", "attachment; filename=" + URLEncoder.encode("ChargeOff修改模板.xlsx", "utf8"));
                util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), ChargeOffModifyExcelDTO.class), "ChargeOff修改模板");
            }
        } catch (UnsupportedEncodingException e) {
           throw new ServiceException("下载模板失败");
        }
    }


    @ApiOperation(value = "导入")
    @PostMapping("/importTemplate")
    public R<Boolean> importTemplate(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file,@ApiParam(value = "导出模板类型（0：新增模板，1：修改模板）") @RequestParam(value = "type",required = true) @Valid @NotNull String type) {
        return R.ok(chargeOffService.importTemplate(file,type));
    }


    @ApiOperation(value = "批量删除接口")
    @PostMapping("/deleteByIds")
    public R<Boolean> deleteByIds(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(chargeOffService.deleteByIds(ids));
    }

    @ApiOperation(value = "提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@ApiParam(value = "id集合") @RequestBody List<Long> idList) {
        return R.ok(chargeOffService.submit(idList));
    }

    @ApiOperation(value = "撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@ApiParam(value = "id集合") @RequestBody List<Long> idList) {
        return R.ok(chargeOffService.withdraw(idList));
    }

    @ApiOperation(value = "更新核销状态")
    @PostMapping("/updateProcessStatus")
    public R<Boolean> updateProcessStatus(@RequestBody CommonApproveDTO approveDTO) {
        return R.ok(chargeOffService.updateProcessStatus(approveDTO));
    }

    /**
     * @description:ChargeOff汇总-列表查询
     **/
    @ApiOperation(value = "ChargeOff汇总分页接口")
    @PostMapping("/summaryPage")
    public R<IPage<ChargeOffVO>> summaryPage(@RequestBody ChargeOffQueryDTO dto) {
        return R.ok(chargeOffService.summaryPage(dto));
    }

    /**
     * @description:ChargeOff汇总-列表-查询详情
     **/
    @ApiOperation(value = "ChargeOff详情分页接口")
    @PostMapping("/summaryDetailPage")
    public R<IPage<ChargeOffVO>> summaryDetailPage(@RequestBody @Valid ChargeOffDetailQueryDTO dto) {
        return R.ok(chargeOffService.summaryDetailPage(dto));
    }

    /**
     * @description:ChargeOff汇总-导出
     **/
    @ApiOperation(value = "Charge Off导出")
    @PostMapping("/export")
    public  R<Map<String, String>> export(HttpServletResponse response, @RequestBody @Valid ChargeOffQueryDTO queryDTO) {
        return R.ok(chargeOffService.summaryList(queryDTO));
    }

    @ApiOperation(value = "按照会计期间固化Charge Off汇总报表")
    @PostMapping("/summaryReportByPeriodCode")
    public  R summaryReportByPeriodCode(@RequestParam(value = "periodCode",required = true) @Valid @NotNull int periodCode) {
        chargeOffService.summaryReportByPeriodCode(periodCode);
        return R.ok();
    }

}



