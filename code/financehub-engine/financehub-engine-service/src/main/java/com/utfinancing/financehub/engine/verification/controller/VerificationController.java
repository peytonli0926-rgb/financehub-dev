package com.utfinancing.financehub.engine.verification.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.CheckPageQueryDTO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.model.dto.CourtCostVerificationDTO;
import com.utfinancing.financehub.engine.verification.model.dto.*;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationDetailsVO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackDetailsVO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackVO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationVO;
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
import java.util.Map;

import com.utfinancing.financehub.engine.verification.service.IVerificationService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : bruyang
 * @Date : Create in 2023-10-11
 * @Description :   Verification控制器实现类
 * @Modified :
 */
@Api(tags = "核销接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/verification")
public class VerificationController {

    private final IVerificationService  verificationService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody VerificationDTO dto) {
        return R.ok(verificationService.saveVerification(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody VerificationDTO dto) {
        return R.ok(verificationService.updateVerification(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(verificationService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<VerificationDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(verificationService.getVerificationDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<VerificationVO>> page(@RequestBody @Valid VerificationQueryDTO queryDTO) {
        return R.ok(verificationService.selectPage(queryDTO));
    }

    @ApiOperation(value = "核销批量删除接口")
    @PostMapping("/deleteByIds")
    public R<Boolean> deleteByIds(@RequestBody @ApiParam(value = "核销表id集合") List<Long> ids){
        return R.ok(verificationService.deleteByIds(ids));
    }

    @ApiOperation(value = "下载核销模板")
    @PostMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            ExcelUtil<VerificationExcelDTO> util = new ExcelUtil<VerificationExcelDTO>(VerificationExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("核销模板.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), VerificationExcelDTO.class), "核销模板");
        } catch (UnsupportedEncodingException e) {
            log.error("export error", e);
        }
    }

    @ApiOperation(value = "导入核销模板数据")
    @PostMapping("/importTemplate")
    public R<Boolean> importTemplate(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(verificationService.importTemplate(file));
    }

    @ApiOperation(value = "生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@ApiParam(value = "核销id集合") @RequestBody List<Long> idList) {
        return R.ok(verificationService.generateVoucher(idList));
    }

    @ApiOperation(value = "提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@ApiParam(value = "核销id集合") @RequestBody List<Long> idList) {
        return R.ok(verificationService.submit(idList));
    }

    @ApiOperation(value = "撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@ApiParam(value = "核销id集合") @RequestBody List<Long> idList) {
        return R.ok(verificationService.withdraw(idList));
    }

    @ApiOperation(value = "根据详情id删除详情")
    @PostMapping("/details/delete/{detailId}")
    @ApiImplicitParam(paramType = "path", name = "detailId", value = "detailId", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> deleteDetailByDetailId(@PathVariable("detailId") @Valid @NotNull Long detailId) {
        return R.ok(verificationService.deleteDetailByDetailId(detailId));
    }

    @ApiOperation(value = "详情分页查询")
    @PostMapping("/details/page")
    public R<IPage<VerificationDetailsVO>> selectDetailPage(@RequestBody @Valid VerificationDetailsQueryDTO queryDTO) {
        return R.ok(verificationService.selectDetailPage(queryDTO));
    }


    @ApiOperation(value = "下载核销详情模板")
    @PostMapping("/details/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<VerificationDetailsExcelDTO> util = new ExcelUtil<VerificationDetailsExcelDTO>(VerificationDetailsExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("核销详情模板.xlsx", "utf8"));
            util.exportExcel(response,Lists.newArrayList(), "核销详情模板");
        } catch (UnsupportedEncodingException e) {
            log.error("exportTemplate error", e);
        }
    }

    @ApiOperation(value = "导出核销详情")
    @PostMapping("/details/exportDetailInfos")
    public void export(HttpServletResponse response, @RequestBody @Valid VerificationDetailsQueryDTO queryDTO) {
        try {
            List<VerificationDetailsVO> list = verificationService.listByCondition(queryDTO);
            ExcelUtil<VerificationDetailsExcelDTO> util = new ExcelUtil<VerificationDetailsExcelDTO>(VerificationDetailsExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("核销详情.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, VerificationDetailsExcelDTO.class), "核销详情");
        } catch (UnsupportedEncodingException e) {
            log.error("exportDetailInfos error", e);
        }
    }

    @ApiOperation(value = "导入核销详情")
    @PostMapping("/details/importDetailInfos")
    public R<Long> importDetailInfos(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file,
                                     @ApiParam(value = "核销id", required = true) @RequestParam(value = "verificationId", required = true) final Long verificationId) {
        return R.ok(verificationService.importDetailInfos(file,verificationId));
    }

    @ApiOperation(value = "核销回款分页")
    @PostMapping("/payback/page")
    public R<IPage<VerificationPaybackVO>> selectPaybackPage(@RequestBody @Valid VerificationPaybackQueryDTO queryDTO) {
        return R.ok(verificationService.selectPaybackPage(queryDTO));
    }

    @ApiOperation(value = "核销回款详情分页")
    @PostMapping("/payback/details/page")
    public R<IPage<VerificationPaybackDetailsVO>> selectPaybackDetailsPage(@RequestBody @Valid VerificationPaybackQueryDTO queryDTO) {
        return R.ok(verificationService.selectPaybackDetailsPage(queryDTO));
    }

//    @Deprecated
//    @ApiOperation(value = "核销回款导出")
//    @PostMapping("/payback/export")
//    public void paybackExport(HttpServletResponse response, @RequestBody @Valid List<VerificationPaybackQueryDTO> queryDTOList) {
//        try {
//            List<VerificationPaybackDetailsVO> list = verificationService.listPaybackByCondition(queryDTOList);
//            ExcelUtil<VerificationPaybackDetailsVO> util = new ExcelUtil<VerificationPaybackDetailsVO>(VerificationPaybackDetailsVO.class);
//            response.setContentType("application/octet-stream; charset=utf-8");
//            response.setHeader(
//                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("核销回款详情.xlsx", "utf8"));
//            util.exportExcel(response, list, "核销回款");
//        } catch (UnsupportedEncodingException e) {
//            log.error("paybackExport error", e);
//        }
//    }

    @ApiOperation(value = "核销回款异步导出")
    @PostMapping("/payback/export")
    public  R<Map<String, String>> export(@RequestBody @Valid VerificationPaybackQueryDTO queryDTO) {
        return R.ok(verificationService.exportSummary(queryDTO));
    }

    @ApiOperation(value = "核销回款校验")
    @PostMapping("/payback/verification")
    public R<VerificationPaybackDetailsVO> verification(@RequestBody @Valid VerificationPaybackQueryDTO queryDTO) {
        return R.ok(verificationService.verification(queryDTO));
    }

    @ApiOperation(value = "更新核销状态")
    @PostMapping("/updateProcessStatus")
    public R<Boolean> updateProcessStatus(@RequestBody CommonApproveDTO approveDTO) {
        return R.ok(verificationService.updateProcessStatus(approveDTO));
    }

    @PostMapping("/updateContractAmount")
    @ApiOperation(value = "开票认领更新合同转回拨备金额")
    public R<Boolean> updateContractAmount(@RequestBody List<CourtCostVerificationDTO> dtoList){
        return R.ok(verificationService.updateContractAmount(dtoList));
    }

    @PostMapping("/checkPage")
    @ApiOperation(value = "校验分页查询")
    public R<IPage<VerificationCheckDTO>> checkPage(@RequestBody @Valid CheckPageQueryDTO queryDTO){
        return R.ok(verificationService.checkPage(queryDTO));
    }




}



