package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInVO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentOutDetailVO;
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
import com.utfinancing.financehub.engine.finance.service.IRecyclingEquipmentOutDetailService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description :   RecyclingEquipmentOutDetail控制器实现类
 * @Modified :
 */
@Api(tags = "回收设备财务出库详细接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/recycling-equipment-out")
public class RecyclingEquipmentOutDetailController {

    private final IRecyclingEquipmentOutDetailService  recyclingEquipmentOutDetailService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody RecyclingEquipmentOutDetailDTO dto) {
        return R.ok(recyclingEquipmentOutDetailService.saveRecyclingEquipmentOutDetail(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody RecyclingEquipmentOutDetailDTO dto) {
        return R.ok(recyclingEquipmentOutDetailService.updateRecyclingEquipmentOutDetail(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "回收设备入库数据id集合") List<Long> ids) {
        return R.ok(recyclingEquipmentOutDetailService.removeDetail(ids));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<RecyclingEquipmentOutDetailDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(recyclingEquipmentOutDetailService.getRecyclingEquipmentOutDetailDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<RecyclingEquipmentOutDetailVO>> page(@RequestBody @Valid RecyclingEquipmentOutDetailQueryDTO queryDTO) {
        return R.ok(recyclingEquipmentOutDetailService.selectPage(queryDTO));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "下载回收设备财务出库详细表模板")
    @PostMapping("/exportTemplate")
    public void export(HttpServletResponse response) {
        try {
            ExcelUtil<RecyclingEquipmentOutDetailExcelDTO> util = new ExcelUtil<RecyclingEquipmentOutDetailExcelDTO>(RecyclingEquipmentOutDetailExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("回收设备财务出库模板.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), RecyclingEquipmentOutDetailExcelDTO.class), "回收设备财务出库模板");
        } catch (UnsupportedEncodingException e) {
            log.error("export error", e);
        }
    }

    @ApiOperation(value = "导入回收设备财务出库模板数据")
    @PostMapping("/importTemplate")
    public R<Boolean> importTemplate(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(recyclingEquipmentOutDetailService.importTemplate(file));
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "回收设备出库数据id集合") List<Long> ids){
        return R.ok(recyclingEquipmentOutDetailService.generateVoucher(ids, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "回收设备出库数据id集合") List<Long> ids){
        return R.ok(recyclingEquipmentOutDetailService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "回收设备出库数据id集合") List<Long> ids){
        return R.ok(recyclingEquipmentOutDetailService.withdraw(ids));
    }

    @ApiOperation(value = "导出明细数据")
    @PostMapping("/exportDetail")
    public void export(HttpServletResponse response,@RequestBody @Valid RecyclingEquipmentOutDetailQueryDTO queryDTO) {
        try {
            List<RecyclingEquipmentOutDetailVO> list = recyclingEquipmentOutDetailService.listByCondition(queryDTO);
            ExcelUtil<RecyclingEquipmentOutDetailExportExcelDTO> util = new ExcelUtil<RecyclingEquipmentOutDetailExportExcelDTO>(RecyclingEquipmentOutDetailExportExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("设备回收出库.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, RecyclingEquipmentOutDetailExportExcelDTO.class), "设备回收出库");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:"+e.getMessage());
        }
    }
}



