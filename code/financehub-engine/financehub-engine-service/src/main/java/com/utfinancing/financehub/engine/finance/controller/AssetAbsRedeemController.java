package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelManySheetUtil;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsRedeemDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsRedeemQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsRedeemDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckPageQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.utfinancing.financehub.engine.finance.service.IAssetAbsRedeemService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : bruyang
 * @Date : Create in 2024-03-15
 * @Description :   AssetAbsRedeem控制器实现类
 * @Modified :
 */
@Api(tags = "资产转让ABS-赎回")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/asset-abs-redeem")
public class AssetAbsRedeemController {

    private final IAssetAbsRedeemService  assetAbsRedeemService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody AssetAbsRedeemDTO dto) {
        return R.ok(assetAbsRedeemService.saveAssetAbsRedeem(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody AssetAbsRedeemDTO dto) {
        return R.ok(assetAbsRedeemService.updateAssetAbsRedeem(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(assetAbsRedeemService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<AssetAbsRedeemDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(assetAbsRedeemService.getAssetAbsRedeemDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<AssetAbsRedeemVO>> page(@RequestBody @Valid AssetAbsRedeemQueryDTO queryDTO) {
        return R.ok(assetAbsRedeemService.selectPage(queryDTO));
    }

    @ApiOperation(value = "下载模板")
    @PostMapping("/downLoad")
    public void downLoad(HttpServletResponse response) {
        try {
            ExcelUtil<AssetAbsRedeemExcelVO> util = new ExcelUtil<AssetAbsRedeemExcelVO>(AssetAbsRedeemExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("赎回模板.xlsx", "utf8"));
            util.exportExcel(response, Lists.newArrayList(), "赎回模板");
        } catch (UnsupportedEncodingException e) {
            log.error("exportTemplate error", e);
        }
    }

    @ApiOperation(value = "汇总页导入")
    @PostMapping("/importTemplate")
    public R<Boolean> importTemplate(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) throws IOException {
        return R.ok(assetAbsRedeemService.importTemplate(file));
    }

    @ApiOperation(value = "详情分页查询")
    @PostMapping("/detailPage")
    public R<IPage<AssetAbsRedeemDetailVO>> detailPage(@RequestBody @Valid AssetAbsRedeemDetailQueryDTO queryDTO) {
        return R.ok(assetAbsRedeemService.detailPage(queryDTO));
    }

    @ApiOperation(value = "批量删除接口")
    @PostMapping("/deleteByIds")
    public R<Boolean> deleteByIds(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(assetAbsRedeemService.deleteByIds(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(assetAbsRedeemService.withdraw(ids));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(assetAbsRedeemService.submit(ids));
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(assetAbsRedeemService.generateVoucher(ids, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "汇总导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid List<Long> idList) {
        try {
            if (CollectionUtil.isEmpty(idList)) {
                throw new ServiceException("请至少勾选一条数据导出");
            }
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("转付.xlsx", "utf8"));
            Map<String, List<?>> map = new HashMap<>();
            AssetAbsRedeemQueryDTO queryDTO = new AssetAbsRedeemQueryDTO();
            queryDTO.setIdList(idList);
            List<AssetAbsRedeemVO> redeemVOList = assetAbsRedeemService.selectByCondition(queryDTO);
            List<AssetAbsRedeemDetailExcelVO> detailExcelVOList = assetAbsRedeemService.selectDetailByRedeemIdList(idList);
            String sheet1 = "1";
            String sheet2 = "2";
            map.put(sheet1, BeanUtil.copyToList(redeemVOList,AssetAbsRedeemExportExcelVO.class));
            map.put(sheet2, BeanUtil.copyToList(detailExcelVOList,AssetAbsRedeemDetailExcelVO.class));
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, AssetAbsRedeemExportExcelVO.class);
            mapClass.put(sheet2, AssetAbsRedeemDetailExcelVO.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "赎回汇总");
            sheetName.put(sheet2, "赎回详情");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (UnsupportedEncodingException e) {
            log.error("export error", e);
            throw new ServiceException("导出数据失败，失败原因："+e.getMessage());
        }
    }

    @ApiOperation(value = "更新核销状态")
    @PostMapping("/updateProcessStatus")
    public R<Boolean> updateProcessStatus(@RequestBody CommonApproveDTO approveDTO) {
        return R.ok(assetAbsRedeemService.updateProcessStatus(approveDTO));
    }


    @ApiOperation(value = "详情页导出")
    @GetMapping("/detailExport/{assetAbsRedeemId}")
    public void detailExport(HttpServletResponse response, @PathVariable("assetAbsRedeemId") @Valid @NotNull Long assetAbsRedeemId) {
        try {
            List<AssetAbsRedeemDetailExcelVO> list = assetAbsRedeemService.selectDetailByRedeemId(assetAbsRedeemId);
            ExcelUtil<AssetAbsRedeemDetailExcelVO> util = new ExcelUtil<AssetAbsRedeemDetailExcelVO>(AssetAbsRedeemDetailExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("赎回详情.xlsx", "utf8"));
            util.exportExcel(response, list, "赎回详情");
        } catch (UnsupportedEncodingException e) {
            log.error("导出详情页失败", e);
            throw new ServiceException("详情页导出失败，失败原因:"+e.getMessage());
        }
    }

    @ApiOperation(value = "详情页导入")
    @PostMapping("/detailImport")
    public R<Boolean> detailImport(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file,@ApiParam(name = "汇总表Id",required = true) @RequestParam(value = "assetAbsRedeemId",required = true) @Valid @NotNull Long assetAbsRedeemId) throws IOException {
        return R.ok(assetAbsRedeemService.detailImport(file,assetAbsRedeemId));
    }

    @ApiOperation(value = "校验分页查询")
    @PostMapping("/checkPage")
    public R<IPage<ContractBalanceVO>> checkPage(@RequestBody @Valid CheckPageQueryDTO queryDTO) {
        return R.ok(assetAbsRedeemService.selectCheckPage(queryDTO));
    }


}



