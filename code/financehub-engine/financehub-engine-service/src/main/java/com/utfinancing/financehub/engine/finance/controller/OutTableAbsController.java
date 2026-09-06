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
import com.utfinancing.financehub.engine.finance.entity.OutTableContractDetailEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.utfinancing.financehub.engine.finance.service.IOutTableAbsService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : bruyang
 * @Date : Create in 2024-03-11
 * @Description :   OutTableAbs控制器实现类
 * @Modified :
 */
@Api(tags = "出表ABS")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/out-table-abs")
public class OutTableAbsController {

    private final IOutTableAbsService  outTableAbsService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody OutTableAbsDTO dto) {
        return R.ok(outTableAbsService.saveOutTableAbs(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody OutTableAbsDTO dto) {
        return R.ok(outTableAbsService.updateOutTableAbs(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(outTableAbsService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<OutTableAbsDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(outTableAbsService.getOutTableAbsDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<OutTableAbsVO>> page(@RequestBody @Valid OutTableAbsQueryDTO queryDTO) {
        return R.ok(outTableAbsService.selectPage(queryDTO));
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/downLoad")
    public void downLoad(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("出表abs.xlsx", "utf8"));
            Map<String, List<?>> map = new HashMap<>();
            String sheet1 = "1";
            String sheet2 = "2";
            map.put(sheet1, new ArrayList<>());
            map.put(sheet2, new ArrayList<>());
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, OutTableAbsExcelVO.class);
            mapClass.put(sheet2, OutTableContractDetailExcelVO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "出表abs基本信息");
            sheetName.put(sheet2, "出表合同详情");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            log.error("export error", e);
            throw new ServiceException("下载出表模板失败，失败原因："+e.getMessage());
        }
    }

    @ApiOperation(value = "导入")
    @PostMapping("/importTemplate")
    public R<Boolean> importTemplate(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) throws IOException {
        return R.ok(outTableAbsService.importTemplate(file));
    }

    @ApiOperation(value = "详情分页查询")
    @PostMapping("/detailPage")
    public R<IPage<OutTableContractDetailVO>> detailPage(@RequestBody @Valid OutTableContractDetailQueryDTO queryDTO) {
        return R.ok(outTableAbsService.detailPage(queryDTO));
    }

    @ApiOperation(value = "批量删除接口")
    @PostMapping("/deleteByIds")
    public R<Boolean> deleteByIds(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(outTableAbsService.deleteByIds(ids));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(outTableAbsService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(outTableAbsService.withdraw(ids));
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(outTableAbsService.generateVoucher(ids, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "更新核销状态")
    @PostMapping("/updateProcessStatus")
    public R<Boolean> updateProcessStatus(@RequestBody CommonApproveDTO approveDTO) {
        return R.ok(outTableAbsService.updateProcessStatus(approveDTO));
    }


    @ApiOperation(value = "导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response,@RequestBody @ApiParam(value = "id集合") List<Long> idList) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("出表abs.xlsx", "utf8"));
            Map<String, List<?>> map = new HashMap<>();
            List<OutTableAbsVO> tableAbsExcelVOList = outTableAbsService.selectTableAbsList(idList);
            List<OutTableContractDetailVO> tableContractDetailEntityList = outTableAbsService.selectTableContractDetailList(idList);
            String sheet1 = "1";
            String sheet2 = "2";
            map.put(sheet1, BeanUtil.copyToList(tableAbsExcelVOList, OutTableAbsExcelVO.class));
            map.put(sheet2, BeanUtil.copyToList(tableContractDetailEntityList,OutTableContractDetailExportExcelVO.class));
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, OutTableAbsExcelVO.class);
            mapClass.put(sheet2, OutTableContractDetailExportExcelVO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "出表abs基本信息");
            sheetName.put(sheet2, "出表合同详情");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            log.error("export error", e);
            throw new ServiceException("导出数据失败，失败原因："+e.getMessage());
        }
    }

    @ApiOperation(value = "出表合同租金计划分页接口")
    @PostMapping("/rentPlanPage")
    public R<IPage<OutTableRentPlanVO>> rentPlanPage(@RequestBody @Valid OutTableAbsQueryDTO queryDTO) {
        return R.ok(outTableAbsService.rentPlanPage(queryDTO));
    }

    @ApiOperation(value = "出表合同租金计划导出")
    @PostMapping("/rentPlanExport")
    public void rentPlanExport(HttpServletResponse response, @RequestBody @Valid OutTableAbsQueryDTO queryDTO) {
        try {
            List<OutTableRentPlanVO> list = outTableAbsService.rentPlanList(queryDTO);
            ExcelUtil<OutTableRentPlanVO> util = new ExcelUtil<OutTableRentPlanVO>(OutTableRentPlanVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("出表合同租金计划.xlsx", "utf8"));
            util.exportExcel(response, list, "出表合同租金计划");
        } catch (UnsupportedEncodingException e) {
            log.error("导出详情页失败", e);
            throw new ServiceException("出表合同租金计划导出失败，失败原因:"+e.getMessage());
        }
    }

    @ApiOperation(value = "校验分页查询")
    @PostMapping("/checkPage")
    public R<IPage<ContractBalanceVO>> checkPage(@RequestBody @Valid CheckPageQueryDTO queryDTO) {
        return R.ok(outTableAbsService.selectCheckPage(queryDTO));
    }

}



