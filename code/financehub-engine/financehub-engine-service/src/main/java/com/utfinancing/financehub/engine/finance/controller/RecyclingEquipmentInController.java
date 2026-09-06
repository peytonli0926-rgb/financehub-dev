package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInCheckVO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInVO;
import com.utfinancing.financehub.engine.finance.service.IRecyclingEquipmentInDetailService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.utfinancing.financehub.engine.finance.service.IRecyclingEquipmentInService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description :   RecyclingEquipmentIn控制器实现类
 * @Modified :
 */
@Api(tags = "回收设备财务入库接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/recycling-equipment")
public class RecyclingEquipmentInController {

    private final IRecyclingEquipmentInService  recyclingEquipmentInService;

    @Resource
    private IRecyclingEquipmentInDetailService recyclingEquipmentInDetailService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody RecyclingEquipmentInDTO dto) {
        return R.ok(recyclingEquipmentInService.saveRecyclingEquipmentIn(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody RecyclingEquipmentInDTO dto) {
        return R.ok(recyclingEquipmentInService.updateRecyclingEquipmentIn(id, dto));
    }

    /**
     * @description: 债务重组业务-回收设备-财务入库-删除按钮
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "删除汇总数据")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "回收设备入库数据id集合") List<Long> ids) {
        return R.ok(recyclingEquipmentInService.removeSummaryAndDetail(ids));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<RecyclingEquipmentInDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(recyclingEquipmentInService.getRecyclingEquipmentInDTOById(id));
    }

    /**
     * @description: 债务重组业务-回收设备-财务入库-首页列表查询
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "入库汇总分页查询")
    @PostMapping("/page")
    public R<IPage<RecyclingEquipmentInVO>> page(@RequestBody @Valid RecyclingEquipmentInQueryDTO queryDTO) {
        return R.ok(recyclingEquipmentInService.selectPage(queryDTO));
    }

    @ApiOperation(value = "入库详细分页查询")
    @PostMapping("/detailPage")
    public R<IPage<RecyclingEquipmentInDetailVO>> detailPage(@RequestBody @Valid RecyclingEquipmentInDetailQueryDTO queryDTO) {
        return R.ok(recyclingEquipmentInDetailService.selectPage(queryDTO));
    }

    @ApiOperation(value = "下载回收设备财务入库详细表模板")
    @PostMapping("/exportTemplate")
    public void export(HttpServletResponse response) {
        try {
            ExcelUtil<RecyclingEquipmentInDetailExcelDTO> util = new ExcelUtil<RecyclingEquipmentInDetailExcelDTO>(RecyclingEquipmentInDetailExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("回收设备财务入库模板.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), RecyclingEquipmentInDetailExcelDTO.class), "回收设备财务入库模板");
        } catch (UnsupportedEncodingException e) {
            log.error("export error", e);
        }
    }

    /**
     * @description: 债务重组业务-回收设备-财务入库-上传-导入
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "导入回收设备财务入库模板数据")
    @PostMapping("/importTemplate")
    public R<Boolean> importTemplate(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(recyclingEquipmentInService.importTemplate(file));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "回收设备入库数据id集合") List<Long> ids){
        return R.ok(recyclingEquipmentInService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "回收设备入库数据id集合") List<Long> ids){
        return R.ok(recyclingEquipmentInService.withdraw(ids));
    }

    /**
     * @description: 债务重组业务-回收设备-财务入库-多行勾选-批量生成凭证
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "回收设备入库数据id集合") List<Long> ids){
        return R.ok(recyclingEquipmentInService.generateVoucher(ids, YesOrNoEnum.NO.getCode()));
    }


    @ApiOperation(value = "导出汇总数据")
    @PostMapping("/exportSum")
    public void exportSum(HttpServletResponse response,@RequestBody @Valid RecyclingEquipmentInQueryDTO queryDTO) {
        try {
            List<RecyclingEquipmentInVO> list = recyclingEquipmentInService.listByCondition(queryDTO);
            ExcelUtil<RecyclingEquipmentInExcelDTO> util = new ExcelUtil<RecyclingEquipmentInExcelDTO>(RecyclingEquipmentInExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("设备回收入库.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, RecyclingEquipmentInExcelDTO.class), "设备回收入库");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:"+e.getMessage());
        }
    }

    /**
     * @description: 债务重组业务-回收设备-财务入库-查看详情-导出
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "导出详细数据")
    @PostMapping("/exportDetail")
    public void exportDetail(HttpServletResponse response,@RequestBody @Valid RecyclingEquipmentInDetailQueryDTO queryDTO) {
        try {
            List<RecyclingEquipmentInDetailExportExcelDTO> list = recyclingEquipmentInDetailService.listByCondition(queryDTO);
            ExcelUtil<RecyclingEquipmentInDetailExportExcelDTO> util = new ExcelUtil<RecyclingEquipmentInDetailExportExcelDTO>(RecyclingEquipmentInDetailExportExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("设备回收入库详细.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, RecyclingEquipmentInDetailExportExcelDTO.class), "设备回收入库详细");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:"+e.getMessage());
        }
    }

    @ApiOperation(value = "科目余额校验")
    @PostMapping("/remainBalance/check")
    public R<List<RecyclingEquipmentInCheckVO>> checkRemainBalance(@RequestBody @ApiParam(value = "回收设备入库数据id") Map<String, String> param){
        return R.ok(recyclingEquipmentInService.checkRemainBalance(Long.valueOf(param.get("id"))));
    }

}



