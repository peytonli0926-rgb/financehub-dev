package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.DropDownDTO;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.ManualQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ManualDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ManualVO;
import com.utfinancing.financehub.engine.finance.model.vo.ManualVoucherExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ManualVoucherVO;
import com.utfinancing.financehub.engine.finance.service.ExcelDropDownService;
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
import com.utfinancing.financehub.engine.finance.service.IManualService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : bruyang
 * @Date : Create in 2024-01-10
 * @Description :   Manual控制器实现类
 * @Modified :
 */
@Api(tags = "手工表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/manual")
public class ManualController {

    private final IManualService  manualService;
    private final ExcelDropDownService excelDropDownService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ManualDTO dto) {
        return R.ok(manualService.saveManual(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ManualDTO dto) {
        return R.ok(manualService.updateManual(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(manualService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ManualDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(manualService.getManualDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ManualVO>> page(@RequestBody @Valid ManualQueryDTO queryDTO) {
        return R.ok(manualService.selectPage(queryDTO));
    }

    @ApiOperation(value = "撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@ApiParam(value = "id集合") @RequestBody List<Long> idList) {
        return R.ok(manualService.withdraw(idList));
    }

    @ApiOperation(value = "提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@ApiParam(value = "id集合") @RequestBody List<Long> idList) {
        return R.ok(manualService.submit(idList));
    }

    @ApiOperation(value = "更新状态")
    @PostMapping("/updateStatus")
    public R<Boolean> updateStatus(@RequestBody CommonApproveDTO approveDTO) {
        return R.ok(manualService.updateStatus(approveDTO));
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("模板.xlsx", "utf8"));
            ExcelUtil<ManualVoucherExcelVO> util = new ExcelUtil<ManualVoucherExcelVO>(ManualVoucherExcelVO.class);
            List<DropDownDTO> dropDownDTOList = excelDropDownService.getManualExcelDropDown();
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), ManualVoucherExcelVO.class), "模板","",dropDownDTOList);
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("导出模板失败，失败原因："+e.getMessage());
        }
    }

    @ApiOperation(value = "导入")
    @PostMapping("/importTemplate")
    public R<String> importTemplate(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(manualService.importTemplate(file));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/deleteByIds")
    public R<Boolean> deleteByIds(@RequestBody List<Long> idList) {
        return R.ok(manualService.deleteByIds(idList));
    }


    @ApiOperation(value = "冲销")
    @PostMapping("/writeOff")
    public R<Boolean> writeOff(@ApiParam(value = "id集合") @RequestBody List<Long> idList) {
        return R.ok(manualService.writeOff(idList));
    }

    @ApiOperation(value = "复制")
    @PostMapping("/copy")
    public R<Boolean> copy(@ApiParam(value = "id集合") @RequestBody List<Long> idList) {
        return R.ok(manualService.copy(idList));
    }

    @PostMapping("/extenalDataCheck")
    @ApiOperation(value = "外部手工数据校验")
    public R<String> extenalDataCheck(@Valid @RequestBody ManualDTO dto) {
        return R.ok(manualService.extenalDataCheck(dto));
    }

    @ApiOperation(value = "导入前校验接口")
    @PostMapping("/importTemplateCheck")
    public R<String> importTemplateCheck(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(manualService.importTemplateCheck(file));
    }


}



