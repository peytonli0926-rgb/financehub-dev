package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelManySheetUtil;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.service.IRentRegisterDetailService;
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
import java.util.stream.Collectors;

import com.utfinancing.financehub.engine.finance.service.IRentRegisterService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : wenbin
 * @Date : Create in 2024-04-07
 * @Description :   RentRegister控制器实现类
 * @Modified :
 */
@Api(tags = "出租登记")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/rent-register")
public class RentRegisterController {

    private final IRentRegisterService rentRegisterService;
    private final IRentRegisterDetailService rentRegisterDetailService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody RentRegisterDTO dto) {
        return R.ok(rentRegisterService.saveRentRegister(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody RentRegisterDTO dto) {
        return R.ok(rentRegisterService.updateRentRegister(id, dto));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<RentRegisterDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(rentRegisterService.getRentRegisterDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<RentRegisterVO>> page(@RequestBody @Valid RentRegisterQueryDTO queryDTO) {
        return R.ok(rentRegisterService.selectPage(queryDTO));
    }

    @ApiOperation(value = "出租登记导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid RentRegisterQueryDTO queryDTO) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("出租登记.xlsx", "utf8"));
            Map<String, List<?>> map = new HashMap<>();
            List<RentRegisterVO> list1 = rentRegisterService.selectList(queryDTO);
            List<RentRegisterDetailExcelVO> list2 = Lists.newArrayList();
            List<Long> rentRegisterIdList = list1.stream().map(RentRegisterVO::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(rentRegisterIdList)) {
                RentRegisterDetailQueryDTO detailQueryDTO = new RentRegisterDetailQueryDTO();
                detailQueryDTO.setRentRegisterIdList(rentRegisterIdList);
                list2 = rentRegisterDetailService.selectList(detailQueryDTO);
            }
            String sheet1 = "1";
            String sheet2 = "2";
            map.put(sheet1, BeanUtil.copyToList(list1,RentRegisterExcelVO.class));
            map.put(sheet2, list2);
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, RentRegisterExcelVO.class);
            mapClass.put(sheet2, RentRegisterDetailExcelVO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "基本信息");
            sheetName.put(sheet2, "租金计划");
            util.exportManySheetExcel(response, sheetName, mapClass);

        } catch (UnsupportedEncodingException e) {
            log.error("出租登记提导出失败", e);
            throw new ServiceException("出租登记导出失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "下载基本信息模板")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<RentRegisterExcelDTO> util = new ExcelUtil<RentRegisterExcelDTO>(RentRegisterExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("出租登记基本信息导入模板.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), RentRegisterExcelDTO.class), "出租登记基本信息");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "下载租金计划模板")
    @PostMapping("/exportTemplateDetail")
    public void exportTemplateDetail(HttpServletResponse response) {
        try {
            ExcelUtil<RentRegisterDetailExcelDTO> util = new ExcelUtil<RentRegisterDetailExcelDTO>(RentRegisterDetailExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("出租登记租金计划导入模板.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), RentRegisterDetailExcelDTO.class), "出租登记租金计划");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "上传基本信息")
    @PostMapping("/importFile")
    public R<Boolean> importFile(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(rentRegisterService.importFile(file));
    }

    @ApiOperation(value = "上传租金计划表")
    @PostMapping("/importFileForDetail")
    public R<Boolean> importFileForDetail(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(rentRegisterService.importFileForDetail(file));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(rentRegisterService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(rentRegisterService.withdraw(ids));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(rentRegisterService.delete(ids));
    }

    @ApiOperation(value = "分摊")
    @PostMapping("/apportion")
    public R<Boolean> apportion(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(rentRegisterService.apportion(ids));
    }

}



