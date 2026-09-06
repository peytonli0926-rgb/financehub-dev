package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionUploadTaskQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.utfinancing.financehub.engine.finance.service.IImpairmentProvisionService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description :   ImpairmentProvision控制器实现类
 * @Modified :
 */
@Api(tags = "减值计提")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/impairment-provision")
public class ImpairmentProvisionController {

    private final IImpairmentProvisionService  impairmentProvisionService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ImpairmentProvisionDTO dto) {
        return R.ok(impairmentProvisionService.saveImpairmentProvision(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ImpairmentProvisionDTO dto) {
        return R.ok(impairmentProvisionService.updateImpairmentProvision(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(impairmentProvisionService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ImpairmentProvisionDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(impairmentProvisionService.getImpairmentProvisionDTOById(id));
    }

   /**
    * @description:减值计提-首页列表-分页查询
    **/
    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ImpairmentProvisionVO>> page(@RequestBody @Valid ImpairmentProvisionQueryDTO queryDTO) {
        return R.ok(impairmentProvisionService.selectPage(queryDTO));
    }

    /**
     * @description:减值计提-首页列表-导出按钮
     **/
    @ApiOperation(value = "减值计提导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid ImpairmentProvisionQueryDTO queryDTO) {
        try {
            List<ImpairmentProvisionVO> list = impairmentProvisionService.selectList(queryDTO);
            ExcelUtil<ImpairmentProvisionExcelVO> util = new ExcelUtil<ImpairmentProvisionExcelVO>(ImpairmentProvisionExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("减值计提汇总表.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, ImpairmentProvisionExcelVO.class), "减值计提汇总表");
        } catch (UnsupportedEncodingException e) {
            log.error("减值计提导出失败", e);
            throw new ServiceException("减值计提导出失败，失败原因:" + e.getMessage());
        }
    }

    /**
     * @description:减值计提-首页-生成凭证
     **/
    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(Boolean.TRUE,impairmentProvisionService.generateVoucherAsync(ids, YesOrNoEnum.NO.getCode()));
    }

    /**
     * @description:减值计提-首页列表-提交-仅支持单项提交
     **/
    @ApiOperation(value = "提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(Boolean.TRUE,impairmentProvisionService.submit(ids));
    }

    /**
     * @description:减值计提-首页列表-撤回按钮
     **/
    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(impairmentProvisionService.withdraw(ids));
    }

    /**
     * @description:减值计提-首页列表-上传
     **/
    @ApiOperation(value = "上传")
    @PostMapping("/importFile")
    public R<Boolean> importFile(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file,
                                 @RequestParam @ApiParam(value = "excel模板类型") @Valid @NotNull String excelType) {
        return R.ok(Boolean.TRUE,impairmentProvisionService.importFile(file,excelType));
    }

    /**
     * @description:减值计提-首页列表-上传按钮-获取上传EXCEL列表
     **/
    @ApiOperation(value = "获取上传excel列表")
    @GetMapping("/getUploadExcelList")
    public R<List<ImpairmentProvisionExcelTypeVO>> getUploadExcelList() {
        return R.ok(impairmentProvisionService.getUploadExcelList());
    }

    /**
     * @description:减值计提-首页列表-查看本月减值报告按钮
     **/
    @ApiOperation(value = "查看本月减值报告")
    @GetMapping("/getImpairmentReport")
    public R<List<ImpairmentProvisionDetailReportVO>> getImpairmentReport() {
        return R.ok(impairmentProvisionService.getImpairmentReport());
    }

    /**
     * @description:减值计提-首页列表-导出减值清单-查询待下载的待下载的EXCEL文件类型
     **/
    @ApiOperation(value = "获取导出减值清单excel列表")
    @GetMapping("/getExportExcelList")
    public R<List<ImpairmentProvisionExcelTypeVO>> getExportExcelList() {
        return R.ok(impairmentProvisionService.getExportExcelList());
    }


    /**
     * @description:减值计提-首页列表-导出减值清单-导出具体减值清单文件
     **/
    @ApiOperation(value = "导出减值清单")
    @PostMapping("/exportImpairmentList")
    public void exportImpairmentList(HttpServletResponse response, @RequestBody @ApiParam(value = "导出excel模板类型") @Valid @NotNull String excelType) {
        impairmentProvisionService.exportImpairmentList(response,excelType);
    }

    /**
     * @description:减值计提-首页列表-冲销按钮
     **/
    @ApiOperation(value = "批量冲销")
    @PostMapping("/writeOff")
    public R<Boolean> writeOff(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(impairmentProvisionService.writeOff(ids));
    }

    /**
     * @description:减值计提-首页列表-查看任务按钮
     **/
    @ApiOperation(value = "查询任务")
    @PostMapping("/queryUpdateTask")
    public R<IPage<ImpairmentProvisionUploadTaskVO>> queryUpdateTask(@RequestBody @Valid ImpairmentProvisionUploadTaskQueryDTO queryDTO) {
        return R.ok(impairmentProvisionService.queryUpdateTask(queryDTO));
    }

    /**
     * @description:减值计提-首页-传送明细凭证至金蝶
     **/
    @ApiOperation(value = "传送明细凭证至金蝶")
    @PostMapping("/pushDetailVouchers")
    public R<Boolean> pushDetailVouchers(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(Boolean.TRUE,impairmentProvisionService.pushDetailVouchers(ids));
    }

    /**
     * @description:减值计提-首页-异步推送汇总凭证
     **/
    @ApiOperation(value = "异步推送汇总凭证")
    @PostMapping("/asyncPushSummaryVoucher")
    public R<Boolean> asyncPushSummaryVoucher(@RequestBody CommonApproveDTO approveDTO) {
        impairmentProvisionService.asyncPushSummaryVoucher(approveDTO);
        return R.ok();
    }

}



