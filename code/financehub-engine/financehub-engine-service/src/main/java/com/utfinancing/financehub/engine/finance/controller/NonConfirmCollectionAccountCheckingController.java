package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.utfinancing.financehub.common.core.dto.DropDownDTO;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.NonConfirmCollectionAccountCheckingVO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.hthx.common.enums.ResultEnum;
import com.utfinancing.financehub.engine.hthx.utils.HthxLargeExcelDataExportUtils;
import com.utfinancing.financehub.engine.hthx.utils.StringUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.utfinancing.financehub.engine.finance.service.INonConfirmCollectionAccountCheckingService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author : robjiang
 * @Date : Create in 2024-04-22
 * @Description :   NonConfirmCollectionAccountChecking控制器实现类
 * @Modified :
 */
@Api(tags = "未确认收款明细-C01 对账表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/non-confirm-collection-account-checking")
public class NonConfirmCollectionAccountCheckingController {

    private final INonConfirmCollectionAccountCheckingService  nonConfirmCollectionAccountCheckingService;

    private final IOrgCompanyService orgCompanyService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody NonConfirmCollectionAccountCheckingDTO dto) {
        return R.ok(nonConfirmCollectionAccountCheckingService.saveNonConfirmCollectionAccountChecking(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody NonConfirmCollectionAccountCheckingDTO dto) {
        return R.ok(nonConfirmCollectionAccountCheckingService.updateNonConfirmCollectionAccountChecking(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(nonConfirmCollectionAccountCheckingService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<NonConfirmCollectionAccountCheckingDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(nonConfirmCollectionAccountCheckingService.getNonConfirmCollectionAccountCheckingDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<NonConfirmCollectionAccountCheckingVO>> page(@RequestBody @Valid NonConfirmCollectionAccountCheckingQueryDTO queryDTO) {
        return R.ok(nonConfirmCollectionAccountCheckingService.selectPage(queryDTO));
    }

    /**
     * @description:未确认收款-对账表-导出按钮
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "C01 对账表-导出按钮")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid QueryNonConfirmAccountCheckingInputDTO queryDTO) throws IOException {
        ExcelUtil<AccountCheckingExportDTO> util = new ExcelUtil<AccountCheckingExportDTO>(AccountCheckingExportDTO.class);
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("对账表.xlsx", "utf8"));
        List<AccountCheckingExportDTO> dataList = nonConfirmCollectionAccountCheckingService.queryExportData(queryDTO);
        util.exportExcel(response, dataList, "对账表", "");
    }

    /**
     * @description:未确认收款-对账表-分页查询
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "C01 对账表-对账表分页查询")
    @PostMapping("/queryNonConfirmAccountChecking")
    public R<IPage<QueryNonConfirmAccountCheckingOutputDTO>> queryNonConfirmAccountChecking(
            @RequestBody @Valid QueryNonConfirmAccountCheckingInputDTO queryDTO) {
        return R.ok(nonConfirmCollectionAccountCheckingService.queryNonConfirmAccountChecking(queryDTO));
    }

    /**
     * @description:未确认收款-对账表-确认对账按钮
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "C01 对账表-确认对账")
    @PostMapping("/confirmAccountingChecking")
    public R<String> confirmAccountingChecking(@RequestBody @Valid ConfirmAccountingCheckingDTO queryDTO) {
        return R.ok(nonConfirmCollectionAccountCheckingService.confirmAccountingChecking(queryDTO));
    }

    @ApiOperation(value = "C01 对账表-批量修改按钮-模板下载")
    @PostMapping("/batchModify")
    public void batchModify(HttpServletResponse response) throws IOException {
        ExcelUtil<BatchModifyTemplateDTO> util = new ExcelUtil<BatchModifyTemplateDTO>(BatchModifyTemplateDTO.class);

        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader(
                "Content-Disposition", "attachment; filename=" + URLEncoder.encode("对账表-批量修改模板.xlsx", "utf8"));
        List<OrgCompanyVO> companyVOList = orgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        List<String> orgNameList = companyVOList.stream().map(OrgCompanyVO::getOrgName).collect(Collectors.toList());

        List<DropDownDTO> dropDownDTOList = new ArrayList<>();
        DropDownDTO collectionAccountsBank = new DropDownDTO();
        collectionAccountsBank.setDataList(orgNameList);
        collectionAccountsBank.setFirstRow(1);
        collectionAccountsBank.setLastRow(1);
        if (CollectionUtils.isNotEmpty(collectionAccountsBank.getDataList())) {
            dropDownDTOList.add(collectionAccountsBank);
        }

        DropDownDTO systemNameDropDown = new DropDownDTO();
        List<String> systemNameList = Arrays.stream(SystemEnum.values()).map(SystemEnum::getDesc).collect(Collectors.toList());
        systemNameDropDown.setDataList(systemNameList);
        systemNameDropDown.setFirstRow(2);
        systemNameDropDown.setLastRow(2);
        if (CollectionUtils.isNotEmpty(systemNameDropDown.getDataList())) {
            dropDownDTOList.add(systemNameDropDown);
        }
        util.exportExcel(response, new ArrayList<>(), "批量修改", "", dropDownDTOList);
    }

    /**
     * @description:未确认收款-对账表-批量修改按钮
     * @author: zhangli.chen
     **/
    @ApiOperation(value = "C01 对账表-批量修改按钮-上传")
    @PostMapping("/batchModifyUpload")
    public R<String> batchModifyUpload(MultipartFile file) throws IOException {
        ExcelUtil<BatchModifyTemplateDTO> util =
                new ExcelUtil<BatchModifyTemplateDTO>(BatchModifyTemplateDTO.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<BatchModifyTemplateDTO> list = util.importExcel(inputStream);
            return R.ok(nonConfirmCollectionAccountCheckingService.batchModifyUpload(list));
        } catch (Exception e) {
            log.error("对账表-批量修改上传报错", e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @ApiOperation(value = "C01 对账表-上传非租结果按钮-模板下载")
    @PostMapping("/uploadNonLeaseResult")
    public void uploadNonLeaseResult(HttpServletResponse response) throws IOException {
        ExcelUtil<UploadNonLeaseResultTemplateDTO> util =
                new ExcelUtil<UploadNonLeaseResultTemplateDTO>(UploadNonLeaseResultTemplateDTO.class);

        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader(
                "Content-Disposition", "attachment; filename=" + URLEncoder.encode("对账表-上传非租结果模板.xlsx", "utf8"));
        List<OrgCompanyVO> companyVOList = orgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        List<String> orgNameList = companyVOList.stream().map(OrgCompanyVO::getOrgName).collect(Collectors.toList());

        List<DropDownDTO> dropDownDTOList = new ArrayList<>();
        DropDownDTO collectionAccountsBank = new DropDownDTO();
        collectionAccountsBank.setDataList(orgNameList);
        collectionAccountsBank.setFirstRow(1);
        collectionAccountsBank.setLastRow(1);
        if (CollectionUtils.isNotEmpty(collectionAccountsBank.getDataList())) {
            dropDownDTOList.add(collectionAccountsBank);
        }

        util.exportExcel(response, new ArrayList<>(), "上传非租结果", "", dropDownDTOList);
    }

    @ApiOperation(value = "C01 对账表-上传非租结果按钮-上传")
    @PostMapping("/nonLeaseResultUpload")
    public R<String> nonLeaseResultUpload(NonLeaseResultUploadDTO params) throws IOException {
        ExcelUtil<UploadNonLeaseResultTemplateDTO> util =
                new ExcelUtil<UploadNonLeaseResultTemplateDTO>(UploadNonLeaseResultTemplateDTO.class);
        InputStream inputStream = params.getFile().getInputStream();
        try {
            List<UploadNonLeaseResultTemplateDTO> list = util.importExcel(inputStream);
            String uploadMessage = nonConfirmCollectionAccountCheckingService.nonLeaseResultUpload(list, params.getIsWaring());
            if(StringUtils.isNotEmpty(uploadMessage)){
                return R.fail(uploadMessage);
            }else{
                return R.ok(ResultEnum.COMMON_FILE_UPLOAD_SUCCESS.getMessage());
            }
        } catch (Exception e) {
            log.error("对账表-批量修改上传报错", e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

}



