package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ContractStatusRecordVO;
import com.utfinancing.financehub.engine.finance.service.IContractStatusRecordService;
import com.utfinancing.financehub.engine.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


/**
 * @Author : hzhao
 * @Date : Create in 2023-10-09
 * @Description :   ContractStatusRecord控制器实现类
 * @Modified :
 */
@Api(tags = "特殊合同状态")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/contract-status-record")
public class ContractStatusRecordController {

    private final IContractStatusRecordService  contractStatusRecordService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ContractStatusRecordSaveDTO dto) {
        return R.ok(contractStatusRecordService.saveContractStatusRecord(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ContractStatusRecordSaveDTO dto) {
        return R.ok(contractStatusRecordService.updateContractStatusRecord(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(contractStatusRecordService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ContractStatusRecordDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(contractStatusRecordService.getContractStatusRecordDTOById(id));
    }


    /**
     * @description:特殊合同状态-列表-查询
     **/
    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ContractStatusRecordVO>> page(@RequestBody @Valid ContractStatusRecordQueryDTO queryDTO) {
        return R.ok(contractStatusRecordService.selectPage(queryDTO));
    }

    /**
     * @description:特殊合同状态-导出按钮
     **/
    @ApiOperation(value = "特殊合同状态导出,按照筛选条件")
    @PostMapping("/export")
    public R<Map<String, String>> export(HttpServletResponse response, @RequestBody @Valid ContractStatusRecordQueryDTO queryDTO) {
        return R.ok(contractStatusRecordService.export(queryDTO));
    }

    @ApiOperation(value = "获取详情")
    @PostMapping("/detail")
    public R<IPage<ContractStatusRecordVO>> detail(@RequestBody @Valid ContractStatusRecordQueryDTO queryDTO) {
        return R.ok(contractStatusRecordService.pageDetail(queryDTO));
    }

    @ApiOperation(value = "特殊合同状态-详情导出")
    @PostMapping("/detail/export")
    public void exportDetail(HttpServletResponse response, @RequestBody @Valid ContractStatusRecordQueryDTO queryDTO) {
        List<ContractStatusRecordVO> list = contractStatusRecordService.listDetail(queryDTO);
        ExcelUtil<ContractStatusRecordExcel> util = new ExcelUtil<ContractStatusRecordExcel>(ContractStatusRecordExcel.class);
        util.exportExcel(response, BeanUtil.copyToList(list, ContractStatusRecordExcel.class), "特殊合同状态数据");
    }

    @ApiOperation(value = "特殊合同状态模板下载")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<ContractStatusRecordSaveDTO> util = new ExcelUtil<ContractStatusRecordSaveDTO>(ContractStatusRecordSaveDTO.class);
        util.importTemplateExcel(response, "特殊合同状态数据");
    }

    /**
     * @description:特殊合同状态-上传-确定按钮
     **/
    @ApiOperation(value = "特殊合同状态上传")
    @PostMapping("/importData")
    public R<String> importData(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
        ExcelUtil<ContractStatusRecordSaveDTO> util = new ExcelUtil<ContractStatusRecordSaveDTO>(ContractStatusRecordSaveDTO.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<ContractStatusRecordSaveDTO> list = util.importExcel(inputStream);
            return R.ok(contractStatusRecordService.importData(list, UserUtils.getStaffName()));
        } catch (Exception e) {
            log.error("特殊合同状态上传报错",e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @ApiOperation(value = "特殊合同状态上传")
    @PostMapping("/importDataToContract")
    public R importDataToContract(MultipartFile file) throws Exception {
        ExcelUtil<ContractStatusRecordSaveDTO> util = new ExcelUtil<ContractStatusRecordSaveDTO>(ContractStatusRecordSaveDTO.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<ContractStatusRecordSaveDTO> list = util.importExcel(inputStream);
            contractStatusRecordService.importData(list);
            return R.ok();
        } catch (Exception e) {
            log.error("特殊合同状态上传报错",e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @ApiOperation(value = "提交")
    @PostMapping("/submit")
    public R submit(@RequestBody List<Long> ids) {
        return R.ok(contractStatusRecordService.submit(ids));
    }

    @ApiOperation(value = "撤回")
    @PostMapping("/withdraw")
    public R withdraw(@RequestBody List<Long> ids) {
        return R.ok(contractStatusRecordService.withdraw(ids));
    }

    @ApiOperation(value = "复核通过")
    @PostMapping("/check/pass")
    public R pass(@RequestBody List<Long> ids) {
        return R.ok(contractStatusRecordService.pass(ids));
    }

    @ApiOperation(value = "复核失败")
    @PostMapping("/check/fail")
    public R fail(@RequestBody List<Long> ids) {
        return R.ok(contractStatusRecordService.fail(ids));
    }

    @ApiOperation(value = "批量删除接口")
    @PostMapping("/deleteByIds")
    public R<Boolean> deleteByIds(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(contractStatusRecordService.deleteByIds(ids));
    }

    @ApiOperation(value = "特殊合同状态-下载文件查询")
    @PostMapping("/fileList")
    public R<IPage<FileRecordEntity>> selectSpecialContractFileList(@RequestBody @Valid FileRecordQueryDTO queryDTO) {
        return R.ok(contractStatusRecordService.selectSpecialContractFileList(queryDTO));
    }

}