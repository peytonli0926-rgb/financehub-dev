package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentThirdStageRecordExcel;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentThirdStageRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentThirdStageRecordDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SpecialContractOverdueRecordExcel;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentThirdStageRecordVO;
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
import com.utfinancing.financehub.engine.finance.service.IImpairmentThirdStageRecordService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : robjiang
 * @Date : Create in 2025-12-26
 * @Description :   ImpairmentThirdStageRecord控制器实现类
 * @Modified :
 */
@Api(tags = "减值三阶段上传记录")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/impairment-third-stage-record")
public class ImpairmentThirdStageRecordController {

    private final IImpairmentThirdStageRecordService  impairmentThirdStageRecordService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ImpairmentThirdStageRecordDTO dto) {
        return R.ok(impairmentThirdStageRecordService.saveImpairmentThirdStageRecord(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ImpairmentThirdStageRecordDTO dto) {
        return R.ok(impairmentThirdStageRecordService.updateImpairmentThirdStageRecord(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(impairmentThirdStageRecordService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ImpairmentThirdStageRecordDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(impairmentThirdStageRecordService.getImpairmentThirdStageRecordDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ImpairmentThirdStageRecordVO>> page(@RequestBody @Valid ImpairmentThirdStageRecordQueryDTO queryDTO) {
        return R.ok(impairmentThirdStageRecordService.selectPage(queryDTO));
    }

    @ApiOperation(value = "减值三阶段模板下载")
    @PostMapping("/templateDownload")
    public void templateDownload(HttpServletResponse response) throws IOException {
        ExcelUtil<ImpairmentThirdStageRecordExcel> util = new ExcelUtil<>(ImpairmentThirdStageRecordExcel.class);
        util.importTemplateExcel(response, "sheet1");
    }

    @ApiOperation(value = "减值三阶段上传记录文件上传")
    @PostMapping("/importData")
    public R<String> importData(MultipartFile file) throws Exception {
        ExcelUtil<ImpairmentThirdStageRecordExcel> util = new ExcelUtil<>(ImpairmentThirdStageRecordExcel.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<ImpairmentThirdStageRecordExcel> list = util.importExcel(inputStream);
            return impairmentThirdStageRecordService.importData(list);
        } catch (Exception e) {
            log.error("收益计提上传报错", e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}



