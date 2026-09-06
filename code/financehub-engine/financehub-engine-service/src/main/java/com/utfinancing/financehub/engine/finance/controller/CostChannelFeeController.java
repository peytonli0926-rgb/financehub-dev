package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.CostMainCategoryExpenseTypeEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.CostChannelFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CostChannelFeeDTO;
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
import com.utfinancing.financehub.engine.finance.service.ICostChannelFeeService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : bruyang
 * @Date : Create in 2023-12-13
 * @Description :   CostChannelFee控制器实现类
 * @Modified :
 */
@Api(tags = "成本类费用Api")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/cost-channel-fee")
public class CostChannelFeeController {

    private final ICostChannelFeeService  costChannelFeeService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody CostChannelFeeDTO dto) {
        return R.ok(costChannelFeeService.saveCostChannelFee(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody CostChannelFeeDTO dto) {
        return R.ok(costChannelFeeService.updateCostChannelFee(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(costChannelFeeService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<CostChannelFeeDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(costChannelFeeService.getCostChannelFeeDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<CostChannelFeeVO>> page(@RequestBody @Valid CostChannelFeeQueryDTO queryDTO) {
        return R.ok(costChannelFeeService.selectPage(queryDTO));
    }

    @ApiOperation(value = "经销商/收车费下载模板")
    @PostMapping("/export")
    public void export(HttpServletResponse response,@ApiParam(value = "文件类型(1:支付Excel，2：税率分摊Excel", required = true)
    @RequestParam(value = "fileType", required = true) final String fileType) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            if ("1".equals(fileType)) {
                response.setHeader(
                        "Content-Disposition", "attachment; filename=" + URLEncoder.encode("支付.xlsx", "utf8"));
                ExcelUtil<CostChannelFeeExcelVO> util = new ExcelUtil<CostChannelFeeExcelVO>(CostChannelFeeExcelVO.class);
                util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), CostChannelFeeExcelVO.class), "支付");
            } else {
                response.setHeader(
                        "Content-Disposition", "attachment; filename=" + URLEncoder.encode("税率分摊.xlsx", "utf8"));
                ExcelUtil<CostChannelFeeTaxExcelVO> util = new ExcelUtil<CostChannelFeeTaxExcelVO>(CostChannelFeeTaxExcelVO.class);
                util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), CostChannelFeeTaxExcelVO.class), "税率分摊");
            }
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("导出模板失败，失败原因："+e.getMessage());
        }
    }

    @ApiOperation(value = "导入")
    @PostMapping("/importTemplate")
    public R<Boolean> importTemplate(@ApiParam(value = "导入文件", required = true)
                                         @RequestParam(value = "file", required = true) final MultipartFile file,
                                     @ApiParam(value = "费用大类(1:GPS,2:手环设备款,3:经销商服务费、外部渠道费、海通渠道费,4:收车费、抵押费、解抵押费", required = true)
                                     @RequestParam(value = "expenseType", required = true) final String expenseType,
                                     @ApiParam(value = "文件类型(1:支付Excel，2：税率分摊Excel", required = true)
                                         @RequestParam(value = "fileType", required = true) final String fileType) {
        return R.ok(costChannelFeeService.importTemplate(file,expenseType,fileType));
    }


    @ApiOperation(value = "生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody List<Long> idList) {
        return R.ok(costChannelFeeService.generateVoucher(idList, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "GPS/手环设备/收车费款下载模板")
    @PostMapping("/gpsAndBraceletExport")
    public void gpsAndBraceletExport(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                        "Content-Disposition", "attachment; filename=" + URLEncoder.encode("模板.xlsx", "utf8"));
            ExcelUtil<CostGpsAndBraceleteFeeExcelVO> util = new ExcelUtil<CostGpsAndBraceleteFeeExcelVO>(CostGpsAndBraceleteFeeExcelVO.class);
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), CostGpsAndBraceleteFeeExcelVO.class), "模板");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("导出模板失败，失败原因："+e.getMessage());
        }
    }

    /**
     * 导出包含成本类所有费用类型
     * @param response
     * @param queryDTO
     */
    @ApiOperation(value = "导出")
    @PostMapping("/exportFile")
    public void export(HttpServletResponse response, @RequestBody @Valid CostChannelFeeQueryDTO queryDTO) {
        try {
            //按照费用大类导出不同类型的详情数据
            List<CostChannelFeeVO> list = costChannelFeeService.listByCondition(queryDTO);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("导出文件.xlsx", "utf8"));
            if (CostMainCategoryExpenseTypeEnum.GPS.getCode().equals(queryDTO.getExpenseMainCategoryType())
                || CostMainCategoryExpenseTypeEnum.GRACELETE.getCode().equals(queryDTO.getExpenseMainCategoryType())) {
                ExcelUtil<CostGpsAndBraceleteFeeExportExcelVO> util = new ExcelUtil<CostGpsAndBraceleteFeeExportExcelVO>(CostGpsAndBraceleteFeeExportExcelVO.class);
                util.exportExcel(response, BeanUtil.copyToList(list, CostGpsAndBraceleteFeeExportExcelVO.class), "详情");
            } else if (CostMainCategoryExpenseTypeEnum.SERVICE.getCode().equals(queryDTO.getExpenseMainCategoryType())
                    || CostMainCategoryExpenseTypeEnum.COLLECT_FEE.getCode().equals(queryDTO.getExpenseMainCategoryType())) {
                ExcelUtil<CostChannelFeeExportExcelVO> util = new ExcelUtil<CostChannelFeeExportExcelVO>(CostChannelFeeExportExcelVO.class);
                util.exportExcel(response, BeanUtil.copyToList(list, CostChannelFeeExportExcelVO.class), "详情");
            }
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("导出文件失败");
        }
    }


    @ApiOperation(value = "批量删除")
    @PostMapping("/deleteByIds")
    public R<Boolean> deleteByIds(@RequestBody @ApiParam(value = "ID集合") List<Long> idList) {
        return R.ok(costChannelFeeService.deleteByIds(idList));
    }

    @ApiOperation(value = "提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@ApiParam(value = "id集合") @RequestBody List<Long> idList) {
        return R.ok(costChannelFeeService.submit(idList));
    }

    @ApiOperation(value = "撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@ApiParam(value = "id集合") @RequestBody List<Long> idList) {
        return R.ok(costChannelFeeService.withdraw(idList));
    }

    @ApiOperation(value = "更新状态")
    @PostMapping("/updateProcessStatus")
    public R<Boolean> updateProcessStatus(@RequestBody CommonApproveDTO approveDTO) {
        return R.ok(costChannelFeeService.updateProcessStatus(approveDTO));
    }

}



