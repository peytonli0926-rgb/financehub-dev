package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.DropDownDTO;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionFileUploadRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.hthx.utils.HthxLargeExcelDataExportUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : robjiang
 * @Date : Create in 2024-03-22
 * @Description :   NonConfirmCollectionSum控制器实现类
 * @Modified :
 */
@Api(tags = "未确认收款汇总")
@Slf4j
@RestController
@RequestMapping("/finance/non-confirm-collection-sum")
public class NonConfirmCollectionSumController {

    @Resource
    private INonConfirmCollectionSumService  nonConfirmCollectionSumService;

    @Resource
    private IFundBusinessSystemEbankMappingService fundBusinessSystemEbankMappingService;


    @Resource
    private INonConfirmCollectionFileUploadRecordService nonConfirmCollectionFileUploadRecordService;

    @Resource
    private IOrgCompanyService orgCompanyService;

    @Resource
    private  IRecyclingEquipmentInService recyclingEquipmentInService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody NonConfirmCollectionSumDTO dto) {
        return R.ok(nonConfirmCollectionSumService.saveNonConfirmCollectionSum(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody NonConfirmCollectionSumDTO dto) {
        return R.ok(nonConfirmCollectionSumService.updateNonConfirmCollectionSum(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(nonConfirmCollectionSumService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<NonConfirmCollectionSumDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(nonConfirmCollectionSumService.getNonConfirmCollectionSumDTOById(id));
    }

    /**
     * @description:未确认收款-汇总表-列表页面查询
     **/
    @ApiOperation(value = "B01 汇总页面-分页查询")
    @PostMapping("/page")
    public R page(@RequestBody @Valid NonConfirmCollectionSumQueryDTO queryDTO) {
        return R.ok(nonConfirmCollectionSumService.selectPageByCon(queryDTO));
    }

    /**
     * @description:未确认收款-汇总表-导出按钮
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "B01 汇总页面-导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid NonConfirmCollectionSumQueryDTO queryDTO) {
        try {
            List<SelectNonConfirmCollectionSumByPageDTO> list = nonConfirmCollectionSumService.selectByCon(queryDTO);
            ExcelUtil<SelectNonConfirmCollectionSumExcel> util = new ExcelUtil<>(SelectNonConfirmCollectionSumExcel.class);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("未确认收款汇总表.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, SelectNonConfirmCollectionSumExcel.class), "未确认收款汇总表");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "B01 汇总页面-认领-弹框初始化查询")
    @PostMapping("/claimQuery")
    public R<ClaimQueryResultDTO> claimQuery(@RequestBody @Valid ClaimQueryDTO queryDTO) {
        return nonConfirmCollectionSumService.claimQuery(queryDTO);
    }

    @ApiOperation(value = "B01 汇总页面-认领-确认")
    @PostMapping("/claimConfirm")
    public R claimConfirm(@RequestBody @Valid ClaimConfirmDTO claimConfirmDTO, @RequestHeader(value= Constants.X_CONFIRM_KEY, required = false) String confirmKey) {
        return nonConfirmCollectionSumService.claimConfirm(claimConfirmDTO,confirmKey);
    }

    @ApiOperation(value = "B01 汇总页面-认领-批量认领-导出模板")
    @PostMapping("/batchClaimConfirmTemplateDownload")
    public void batchClaimConfirmTemplateDownload(HttpServletResponse response) throws UnsupportedEncodingException {
        ExcelUtil<BatchClaimConfirmTemplateDownloadDTO> util =
                new ExcelUtil<BatchClaimConfirmTemplateDownloadDTO>(BatchClaimConfirmTemplateDownloadDTO.class);

        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader(
                "Content-Disposition", "attachment; filename=" + URLEncoder.encode("未确认收款批量认领模板.xlsx", "utf8"));
        List<OrgCompanyVO> companyVOList = orgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        List<String> orgNameList = companyVOList.stream().map(OrgCompanyVO::getOrgName).collect(Collectors.toList());

        List<DropDownDTO> dropDownDTOList = new ArrayList<>();
        DropDownDTO collectionAccountsBank = new DropDownDTO();
        collectionAccountsBank.setDataList(orgNameList);
        collectionAccountsBank.setFirstRow(0);
        collectionAccountsBank.setLastRow(0);
        if (CollectionUtils.isNotEmpty(collectionAccountsBank.getDataList())) {
            dropDownDTOList.add(collectionAccountsBank);
        }

        DropDownDTO orgNames = new DropDownDTO();
        orgNames.setDataList(orgNameList);
        orgNames.setFirstRow(1);
        orgNames.setLastRow(1);
        if (CollectionUtils.isNotEmpty(orgNames.getDataList())) {
            dropDownDTOList.add(orgNames);
        }

        util.exportExcel(response, new ArrayList<>(), "未确认收款批量认领模板", "", dropDownDTOList);
    }

    @ApiOperation(value = "B01 汇总页面-认领-批量认领-导入")
    @PostMapping("/batchClaimConfirm")
    public R<String> batchClaimConfirm(MultipartFile file) throws Exception {
        return nonConfirmCollectionSumService.batchClaimConfirm(file);
    }

    @ApiOperation(value = "B01 汇总页面-认领-批量认领-导出错误文件")
    @PostMapping("/downloadErrFile")
    public R<String> downloadErrFile(HttpServletResponse response) throws Exception {
        NonConfirmCollectionFileUploadRecordEntity entity = nonConfirmCollectionFileUploadRecordService.getLastRecord();
        if (entity == null) {
            return R.fail("未查询到最新的错误文件!");
        }

        File file = new File(entity.getErrorFileUrl());
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStream fis = new BufferedInputStream(fileInputStream);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();
        // 设置response的Header
        response.setCharacterEncoding("UTF-8");
        //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
        //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        // 告知浏览器文件的大小
        response.addHeader("Content-Length", "" + file.length());
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        outputStream.write(buffer);
        outputStream.flush();
        return R.ok();
    }

    @ApiOperation(value = "B01 汇总页面-冲销-确认")
    @PostMapping("/writeOffConfirm")
    public R<String> writeOffConfirm(@RequestBody @Valid WriteOffConfirmDTO writeOffConfirmDTO) {
        return nonConfirmCollectionSumService.writeOffConfirm(writeOffConfirmDTO);
    }


    @ApiOperation(value = "B01 汇总页面-修改网银编号-弹框初始化查询")
    @PostMapping("/modifyEbankNoQuery")
    public R<ModifyEbankNoQueryDTO> modifyEbankNoQuery(@RequestBody @Valid ModifyEbankNoQueryDTO params) {
        return nonConfirmCollectionSumService.modifyEbankNoQuery(params);
    }

    @ApiOperation(value = "B01 汇总页面-修改网银编号-确认")
    @PostMapping("/modifyEbankNoConfirm")
    public R<String> modifyEbankNoConfirm(@RequestBody @Valid ModifyEbankNoConfirmDTO params) {
        return nonConfirmCollectionSumService.modifyEbankNoConfirm(params);
    }

    @ApiOperation(value = "B01 汇总页面-批量修改入账日期-模板下载")
    @PostMapping("/modifyIncomeDateTemplateDownload")
    public void modifyIncomeDateTemplateDownload(HttpServletResponse response) throws IOException {
        ExcelUtil<ModifyIncomeDateTemplateDownloadExcel> util = new ExcelUtil<ModifyIncomeDateTemplateDownloadExcel>(
                ModifyIncomeDateTemplateDownloadExcel.class);
        util.importTemplateExcel(response, "sheet1");
    }

    @ApiOperation(value = "B01 汇总页面-批量修改入账日期-上传")
    @PostMapping("/modifyIncomeDateUpload")
    public R modifyIncomeDateUpload(MultipartFile file) throws Exception {
        ExcelUtil<ModifyIncomeDateTemplateDownloadExcel> util = new ExcelUtil<ModifyIncomeDateTemplateDownloadExcel>(
                ModifyIncomeDateTemplateDownloadExcel.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<ModifyIncomeDateTemplateDownloadExcel> list = util.importExcel(inputStream);
            nonConfirmCollectionSumService.batchModifyIncomeDate(list);
            return R.ok();
        } catch (Exception e) {
            log.error("批量修改入账日期上传报错", e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * @description:未确认收款-汇总表-手工调整余额-模板下载
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "B01 汇总页面-手工调整余额-模板下载")
    @PostMapping("/handsAdjustBalanceTemplateDownload")
    public void handsAdjustBalanceTemplateDownload(HttpServletResponse response) throws IOException {
        ExcelUtil<HandsAdjustBalanceTemplateDownloadExcel> util = new ExcelUtil<>(HandsAdjustBalanceTemplateDownloadExcel.class);
        util.importTemplateExcel(response, "手工调整余额");
    }

    /**
     * @description:未确认收款-汇总表-手工调整余额-确认上传
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "B01 汇总页面-手工调整余额-上传")
    @PostMapping("/handsAdjustBalanceUpload")
    public R<String> handsAdjustBalanceUpload(MultipartFile file) throws Exception {
        ExcelUtil<HandsAdjustBalanceTemplateDownloadExcel> util = new ExcelUtil<HandsAdjustBalanceTemplateDownloadExcel>(
                HandsAdjustBalanceTemplateDownloadExcel.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<HandsAdjustBalanceTemplateDownloadExcel> list = util.importExcel(inputStream);
            return nonConfirmCollectionSumService.handsAdjustBalance(list);
        } catch (Exception e) {
            log.error("手工调整余额上传报错:{}", e.getMessage());
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 未确认收款同步
     */
    @ApiOperation(value = "未确认收款同步")
    @PostMapping("/nonConfirmCollectionSync")
    public void nonConfirmCollectionSync() {
        fundBusinessSystemEbankMappingService.selectNonConfirmCollectionFromFundSystem();
    }

    @ApiOperation(value = "未确认收款-手工调整余额-下载模板")
    @PostMapping("/exportTemplate")
    public void export(HttpServletResponse response) {
        try {
            ExcelUtil<RecyclingEquipmentInDetailExcelDTO> util = new ExcelUtil<RecyclingEquipmentInDetailExcelDTO>(RecyclingEquipmentInDetailExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("手工调整余额模板.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), RecyclingEquipmentInDetailExcelDTO.class), "手工调整余额模板");
        } catch (UnsupportedEncodingException e) {
            log.error("export error:{}", e.getMessage());
        }
    }

    /**
     * @description: 未确认收款-导入回收设备财务入库模板数据-上传
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "导入回收设备财务入库模板数据")
    @PostMapping("/importTemplate")
    public R<Boolean> importTemplate(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(recyclingEquipmentInService.importTemplate(file));
    }


    /**
     * @description:未确认收款-汇总表-上传线下网银-模板下载
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "汇总表-上传线下网银-模板下载")
    @PostMapping("/offlineOnlineBankTemplateDownload")
    public void offlineOnlineBankTemplateDownload(HttpServletResponse response){
        ExcelUtil<HthxOfflineOnlineBankTemplateExcel> util = new ExcelUtil<>(HthxOfflineOnlineBankTemplateExcel.class);
        util.importTemplateExcel(response, "上传线下网银");
    }

    /**
     * @description:未确认收款-汇总表-上传线下网银-确认上传
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "未确认收款-汇总表-上传线下网银-确认上传")
    @PostMapping("/offlineOnlineBankManualUpload")
    public R offlineOnlineBankManualUpload(MultipartFile file, @RequestHeader(value= Constants.X_CONFIRM_KEY, required = false) String confirmKey) throws Exception {
        ExcelUtil<HthxOfflineOnlineBankTemplateExcel> util = new ExcelUtil<HthxOfflineOnlineBankTemplateExcel>(HthxOfflineOnlineBankTemplateExcel.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<HthxOfflineOnlineBankTemplateExcel> onlineBankTemplateExcelList = util.importExcel(inputStream);
            return nonConfirmCollectionSumService.offlineOnlineBankManualUpload(onlineBankTemplateExcelList,confirmKey);
        } catch (Exception e) {
            log.error("上传线下网银上传报错:{}", e.getMessage());
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }






}



