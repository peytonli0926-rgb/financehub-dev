package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.LeaseIncomeImport;
import com.utfinancing.financehub.engine.finance.model.dto.SpecialContractOverdueRecordExcel;
import com.utfinancing.financehub.engine.finance.model.dto.SpecialContractOverdueRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SpecialContractOverdueRecordDTO;
import com.utfinancing.financehub.engine.finance.model.vo.SpecialContractOverdueRecordVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.ISpecialContractOverdueRecordService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : robjiang
 * @Date : Create in 2025-11-21
 * @Description :   SpecialContractOverdueRecord控制器实现类
 * @Modified :
 */
@Api(tags = "特殊合同逾期上传记录")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/special-contract-overdue-record")
public class SpecialContractOverdueRecordController {

    private final ISpecialContractOverdueRecordService  specialContractOverdueRecordService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody SpecialContractOverdueRecordDTO dto) {
        return R.ok(specialContractOverdueRecordService.saveSpecialContractOverdueRecord(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody SpecialContractOverdueRecordDTO dto) {
        return R.ok(specialContractOverdueRecordService.updateSpecialContractOverdueRecord(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(specialContractOverdueRecordService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<SpecialContractOverdueRecordDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(specialContractOverdueRecordService.getSpecialContractOverdueRecordDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<SpecialContractOverdueRecordVO>> page(@RequestBody @Valid SpecialContractOverdueRecordQueryDTO queryDTO) {
        return R.ok(specialContractOverdueRecordService.selectPage(queryDTO));
    }

    @ApiOperation(value = "特殊逾期合同模板下载")
    @PostMapping("/templateDownload")
    public void templateDownload(HttpServletResponse response) throws IOException {
        ExcelUtil<SpecialContractOverdueRecordExcel> util = new ExcelUtil<>(SpecialContractOverdueRecordExcel.class);
        util.importTemplateExcel(response, "sheet1");
    }

    @ApiOperation(value = "特殊逾期合同文件上传")
    @PostMapping("/importData")
    public R<String> importData(MultipartFile file) throws Exception {
        ExcelUtil<SpecialContractOverdueRecordExcel> util = new ExcelUtil<>(SpecialContractOverdueRecordExcel.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<SpecialContractOverdueRecordExcel> list = util.importExcel(inputStream);
            return specialContractOverdueRecordService.importData(list);
        } catch (Exception e) {
            log.error("收益计提上传报错", e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}



