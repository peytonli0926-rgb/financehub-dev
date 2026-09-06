package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelManySheetUtil;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.AssetAbsTransferPaymentEntity;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsRedeemDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsTransferPaymentDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsTransferPaymentQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsTransferPaymentDTO;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.utfinancing.financehub.engine.finance.service.IAssetAbsTransferPaymentService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description :   AssetAbsTransferPayment控制器实现类
 * @Modified :
 */
@Api(tags = "资产转付")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/asset-abs-transfer-payment")
public class AssetAbsTransferPaymentController {

    private final IAssetAbsTransferPaymentService  assetAbsTransferPaymentService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody AssetAbsTransferPaymentDTO dto) {
        return R.ok(assetAbsTransferPaymentService.saveAssetAbsTransferPayment(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody AssetAbsTransferPaymentDTO dto) {
        return R.ok(assetAbsTransferPaymentService.updateAssetAbsTransferPayment(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(assetAbsTransferPaymentService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<AssetAbsTransferPaymentDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(assetAbsTransferPaymentService.getAssetAbsTransferPaymentDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<AssetAbsTransferPaymentVO>> page(@RequestBody @Valid AssetAbsTransferPaymentQueryDTO queryDTO) {
        return R.ok(assetAbsTransferPaymentService.selectPage(queryDTO));
    }

    @ApiOperation(value = "批量删除接口")
    @PostMapping("/deleteByIds")
    public R<Boolean> deleteByIds(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(assetAbsTransferPaymentService.deleteByIds(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(assetAbsTransferPaymentService.withdraw(ids));
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/downLoad")
    public void downLoad(HttpServletResponse response) {
        try {
            ExcelUtil<AssetAbsTransferPaymentExcelVO> util = new ExcelUtil<AssetAbsTransferPaymentExcelVO>(AssetAbsTransferPaymentExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("转付模板.xlsx", "utf8"));
            util.exportExcel(response, Lists.newArrayList(), "转付模板");
        } catch (UnsupportedEncodingException e) {
            log.error("exportTemplate error", e);
            throw new ServiceException("下载模板失败，失败原因:"+e.getMessage());
        }
    }

    @ApiOperation(value = "汇总页导入")
    @PostMapping("/importTemplate")
    public R<Boolean> importTemplate(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) throws IOException {
        return R.ok(assetAbsTransferPaymentService.importTemplate(file));
    }

    @ApiOperation(value = "详情分页查询")
    @PostMapping("/detailPage")
    public R<IPage<AssetAbsTransferPaymentDetailVO>> detailPage(@RequestBody @Valid AssetAbsTransferPaymentDetailQueryDTO queryDTO) {
        return R.ok(assetAbsTransferPaymentService.detailPage(queryDTO));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(assetAbsTransferPaymentService.submit(ids));
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(assetAbsTransferPaymentService.generateVoucher(ids, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response,@RequestBody @ApiParam(value = "id集合") List<Long> idList) {
        try {
            if (CollectionUtil.isEmpty(idList)) {
                throw new ServiceException("请至少勾选一条数据导出");
            }
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("转付.xlsx", "utf8"));
            Map<String, List<?>> map = new HashMap<>();
            List<AssetAbsTransferPaymentSummarExcelVO> summarExcelVOList = assetAbsTransferPaymentService.selectPaymentList(idList);
            List<AssetAbsTransferPaymentDetailExcelVO> detailExcelVOList = assetAbsTransferPaymentService.selectPaymentDetailList(idList);
            String sheet1 = "1";
            String sheet2 = "2";
            map.put(sheet1, summarExcelVOList);
            map.put(sheet2, detailExcelVOList);
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, AssetAbsTransferPaymentSummarExcelVO.class);
            mapClass.put(sheet2, AssetAbsTransferPaymentDetailExcelVO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "转付汇总");
            sheetName.put(sheet2, "转付详情");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            log.error("export error", e);
            throw new ServiceException("导出数据失败，失败原因："+e.getMessage());
        }
    }

    @ApiOperation(value = "更新核销状态")
    @PostMapping("/updateProcessStatus")
    public R<Boolean> updateProcessStatus(@RequestBody CommonApproveDTO approveDTO) {
        return R.ok(assetAbsTransferPaymentService.updateProcessStatus(approveDTO));
    }

}



