package com.utfinancing.financehub.engine.verification.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.verification.model.dto.*;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostDetailsVO;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostReportFormVO;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostVO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationDetailsVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.utfinancing.financehub.engine.verification.service.ICourtCostService;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


/**
 * @Author : bruyang
 * @Date : Create in 2023-10-23
 * @Description :   CourtCost控制器实现类
 * @Modified :
 */
@Api(tags = "诉讼费接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/court-cost")
public class CourtCostController {

    private final ICourtCostService  courtCostService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody CourtCostDTO dto) {
        return R.ok(courtCostService.saveCourtCost(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody CourtCostDTO dto) {
        return R.ok(courtCostService.updateCourtCost(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(courtCostService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<CourtCostDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(courtCostService.getCourtCostDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<CourtCostVO>> page(@RequestBody @Valid CourtCostQueryDTO queryDTO) {
        return R.ok(courtCostService.selectPage(queryDTO));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/deleteByIds")
    public R<Boolean> deleteByIds(@RequestBody @ApiParam(value = "诉讼费id集合") List<Long> ids){
        return R.ok(courtCostService.deleteByIds(ids));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "诉讼费id集合") List<Long> ids){
        return R.ok(courtCostService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "诉讼费id集合") List<Long> ids){
        return R.ok(courtCostService.withdraw(ids));
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "诉讼费id集合") List<Long> ids){
        return R.ok(courtCostService.generateVoucher(ids, YesOrNoEnum.NO.getCode()));
    }


    @ApiOperation(value = "上传")
    @PostMapping("/importFile")
    public R<Boolean> importFile(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(courtCostService.importFile(file));
    }

    @ApiOperation(value = "下载")
    @PostMapping("/export")
    public void export(HttpServletResponse response,@RequestBody @Valid CourtCostDetailsQueryDTO queryDTO) {
        try {
            List<CourtCostDetailsVO> list = courtCostService.listByCondition(queryDTO);
            ExcelUtil<CourtCostDetailsExcelDTO> util = new ExcelUtil<CourtCostDetailsExcelDTO>(CourtCostDetailsExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("诉讼费转费用.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, CourtCostDetailsExcelDTO.class), "诉讼费转费用");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:"+e.getMessage());
        }
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<CourtCostDetailsExcelDTO> util = new ExcelUtil<CourtCostDetailsExcelDTO>(CourtCostDetailsExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("诉讼费转费用.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), CourtCostDetailsExcelDTO.class), "诉讼费转费用");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:"+e.getMessage());
        }
    }

    @ApiOperation(value = "详情分页查询")
    @PostMapping("/details/page")
    public R<IPage<CourtCostDetailsVO>> selectDetailPage(@RequestBody @Valid CourtCostDetailsQueryDTO queryDTO) {
        return R.ok(courtCostService.selectDetailPage(queryDTO));
    }

    @ApiOperation(value = "报表分页查询")
    @PostMapping("/reportForm/page")
    public R<IPage<CourtCostReportFormVO>> selectReportFormPage(@RequestBody @Valid CourtCostDetailsQueryDTO queryDTO) {
        return R.ok(courtCostService.selectReportFormPage(queryDTO));
    }

    @ApiOperation(value = "报表详情")
    @PostMapping("/reportForm/details")
    public R<List<CourtCostReportFormVO>> reportFormDetails(@RequestBody @Valid CourtCostDetailsQueryDTO queryDTO) {
        return R.ok(courtCostService.reportFormDetails(queryDTO));
    }

    @ApiOperation(value = "报表下载")
    @PostMapping("/reportForm/export")
    public void reportFormExport(HttpServletResponse response, @RequestBody @Valid CourtCostDetailsQueryDTO queryDTO) {
        try {
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(5000);
            List<CourtCostReportFormVO> list = courtCostService.selectReportFormPage(queryDTO).getRecords();
            ExcelUtil<CourtCostReportFormVO> util = new ExcelUtil<CourtCostReportFormVO>(CourtCostReportFormVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("诉讼费报表.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, CourtCostReportFormVO.class), "诉讼费报表");
        } catch (UnsupportedEncodingException e) {
            log.error("error", e);
        }
    }

    @ApiOperation(value = "更新诉讼费状态")
    @PostMapping("/updateProcessStatus")
    public R<Boolean> updateProcessStatus(@RequestBody CommonApproveDTO approveDTO) {
        return R.ok(courtCostService.updateProcessStatus(approveDTO));
    }

}



