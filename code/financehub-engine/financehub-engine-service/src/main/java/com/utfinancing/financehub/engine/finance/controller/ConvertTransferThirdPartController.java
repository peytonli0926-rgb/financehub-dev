package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelManySheetUtil;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartDetailEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartDetailDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartDetailExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartVO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferThirdPartDetailService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferThirdPartService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.utils.ExcelExportUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Api(tags = "资产转让 - 第三方转让")
@RequestMapping("/finance/convert-transfer-third")
public class ConvertTransferThirdPartController {

    private final IConvertTransferThirdPartService convertTransferThirdPartService;

    private final IConvertTransferThirdPartDetailService convertTransferThirdPartDetailService;

    private final IOrgCompanyService orgCompanyService;

    public ConvertTransferThirdPartController(IConvertTransferThirdPartService convertTransferThirdPartService, IConvertTransferThirdPartDetailService convertTransferThirdPartDetailService, IOrgCompanyService orgCompanyService) {
        this.convertTransferThirdPartService = convertTransferThirdPartService;
        this.convertTransferThirdPartDetailService = convertTransferThirdPartDetailService;
        this.orgCompanyService = orgCompanyService;
    }

    @ApiOperation("分页列表")
    @PostMapping("/page")
    public R<IPage<ConvertTransferThirdPartVO>> page(@RequestBody ConvertTransferThirdPartQueryDTO dto) {
        Page<ConvertTransferThirdPartEntity> page = convertTransferThirdPartService
                .lambdaQuery()
                .between(Objects.nonNull(dto.getAccountStartDate()) && Objects.nonNull(dto.getAccountEndDate()), ConvertTransferThirdPartEntity::getAccountDate, dto.getAccountStartDate(), dto.getAccountEndDate())
                .ge(Objects.nonNull(dto.getAccountStartDate()) && Objects.isNull(dto.getAccountEndDate()), ConvertTransferThirdPartEntity::getAccountDate, dto.getAccountStartDate())
                .le(Objects.isNull(dto.getAccountStartDate()) && Objects.nonNull(dto.getAccountEndDate()), ConvertTransferThirdPartEntity::getAccountDate, dto.getAccountEndDate())
                .eq(StringUtils.isNotEmpty(dto.getBatch()), ConvertTransferThirdPartEntity::getBatch, dto.getBatch())
                .in(!CollectionUtils.isEmpty(dto.getTransferParty()), ConvertTransferThirdPartEntity::getTransferParty, dto.getTransferParty())
                .page(new Page<>(dto.getPageNum(), dto.getPageSize()));

        Map<String, String> collect = orgCompanyService.selectAllOrgIdAndName()
                .stream()
                .collect(Collectors.toMap(OrgCompanyVO::getOrgId, OrgCompanyVO::getOrgName, (l, r) -> r));

        for (ConvertTransferThirdPartEntity record : page.getRecords()) {
            record.setTransferParty(collect.getOrDefault(record.getTransferParty(), record.getTransferParty()));
            record.setTransfereeParty(collect.getOrDefault(record.getTransfereeParty(), record.getTransfereeParty()));
        }

        return R.ok(page.convert(ConvertTransferThirdPartVO::from));
    }

    @ApiOperation("导入模板")
    @PostMapping("/template")
    public void downloadTemplate(HttpServletResponse response) {
        List<ConvertTransferThirdPartDTO> thirdPartVOS = Collections.emptyList();
        List<ConvertTransferThirdPartDetailDTO> detailVOS = Collections.emptyList();
        String fileName = "资产转让第三方-模板.xlsx";
        ExcelExportUtil.setResponse(response, fileName);
        Map<String, List<?>> dataMap = new HashMap<>();
        dataMap.put("thirdPartVOS", thirdPartVOS);
        dataMap.put("detailVOS", detailVOS);
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("thirdPartVOS", "汇总");
        nameMap.put("detailVOS", "明细");
        Map<String, Class> classMap = new HashMap<>();
        classMap.put("thirdPartVOS", ConvertTransferThirdPartDTO.class);
        classMap.put("detailVOS", ConvertTransferThirdPartDetailDTO.class);
        ExcelManySheetUtil util = new ExcelManySheetUtil(dataMap, fileName, Excel.Type.EXPORT);
        util.exportManySheetExcel(response, nameMap, classMap);
    }

    private void writeExcel(
            HttpServletResponse response,
            List<ConvertTransferThirdPartExcelVO> thirdPartVOS,
            List<ConvertTransferThirdPartDetailExcelVO> detailVOS, String fileName) {
        ExcelExportUtil.setResponse(response, fileName);
        Map<String, List<?>> dataMap = new HashMap<>();
        dataMap.put("thirdPartVOS", thirdPartVOS);
        dataMap.put("detailVOS", detailVOS);
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("thirdPartVOS", "汇总");
        nameMap.put("detailVOS", "明细");
        Map<String, Class> classMap = new HashMap<>();
        classMap.put("thirdPartVOS", ConvertTransferThirdPartExcelVO.class);
        classMap.put("detailVOS", ConvertTransferThirdPartDetailExcelVO.class);
        ExcelManySheetUtil util = new ExcelManySheetUtil(dataMap, fileName, Excel.Type.EXPORT);
        util.exportManySheetExcel(response, nameMap, classMap);
    }

    @ApiOperation("导出")
    @PostMapping("/export")
    public void export(@RequestBody ConvertTransferThirdPartQueryDTO dto, @ApiIgnore HttpServletResponse response) {
        List<ConvertTransferThirdPartEntity> list = convertTransferThirdPartService
                .lambdaQuery()
                .between(Objects.nonNull(dto.getAccountStartDate()) && Objects.nonNull(dto.getAccountEndDate()), ConvertTransferThirdPartEntity::getAccountDate, dto.getAccountStartDate(), dto.getAccountEndDate())
                .ge(Objects.nonNull(dto.getAccountStartDate()) && Objects.isNull(dto.getAccountEndDate()), ConvertTransferThirdPartEntity::getAccountDate, dto.getAccountStartDate())
                .le(Objects.isNull(dto.getAccountStartDate()) && Objects.nonNull(dto.getAccountEndDate()), ConvertTransferThirdPartEntity::getAccountDate, dto.getAccountEndDate())
                .eq(StringUtils.isNotEmpty(dto.getBatch()), ConvertTransferThirdPartEntity::getBatch, dto.getBatch())
                .in(!CollectionUtils.isEmpty(dto.getTransferParty()), ConvertTransferThirdPartEntity::getTransferParty, dto.getTransferParty())
                .list();
        List<ConvertTransferThirdPartExcelVO> thirdPartVOS = list
                .stream()
                .map(ConvertTransferThirdPartExcelVO::from)
                .collect(Collectors.toList());

        List<Long> transferIdList = list.stream().map(ConvertTransferThirdPartEntity::getId).collect(Collectors.toList());
        List<ConvertTransferThirdPartDetailExcelVO> detailVOS = convertTransferThirdPartDetailService.lambdaQuery()
                .in(!CollectionUtils.isEmpty(transferIdList), ConvertTransferThirdPartDetailEntity::getTransferId, transferIdList)
                .list()
                .stream()
                .map(ConvertTransferThirdPartDetailExcelVO::from)
                .collect(Collectors.toList());
        Map<String, String> collect = orgCompanyService.selectAllOrgIdAndName()
                .stream()
                .collect(Collectors.toMap(OrgCompanyVO::getOrgId, OrgCompanyVO::getOrgName, (l, r) -> r));
        for (ConvertTransferThirdPartExcelVO record : thirdPartVOS) {
            record.setTransferParty(collect.getOrDefault(record.getTransferParty(), record.getTransferParty()));
            record.setTransfereeParty(collect.getOrDefault(record.getTransfereeParty(), record.getTransfereeParty()));
        }
        for (ConvertTransferThirdPartDetailExcelVO record : detailVOS) {
            record.setOrgId(collect.getOrDefault(record.getOrgId(), record.getOrgId()));
        }

        writeExcel(response, thirdPartVOS, detailVOS, "资产转让第三方.xlxs");
    }

    @ApiOperation("导入")
    @PostMapping("/import")
    public R<Boolean> importExcel(@RequestBody MultipartFile file) throws IOException {
        List<ConvertTransferThirdPartDTO> thirdPartDTOS;
        List<ConvertTransferThirdPartDetailDTO> detailDTOS;
        try {
            thirdPartDTOS = new ExcelUtil<>(ConvertTransferThirdPartDTO.class)
                    .importExcel("汇总", file.getInputStream(), 0);
            detailDTOS = new ExcelUtil<>(ConvertTransferThirdPartDetailDTO.class)
                    .importExcel("明细", file.getInputStream(), 0);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
        convertTransferThirdPartService.importFromData(thirdPartDTOS, detailDTOS);
        return R.ok(true);
    }


    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(convertTransferThirdPartService.generateVoucher(ids));
    }


    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(convertTransferThirdPartService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(convertTransferThirdPartService.withdraw(ids));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(convertTransferThirdPartService.delete(ids));
    }

    @ApiOperation(value = "转让批次下拉列表")
    @PostMapping("/batchList")
    public R<List<String>> batchList() {
        List<String> batchList = convertTransferThirdPartService.lambdaQuery()
                .select(Collections.singletonList(ConvertTransferThirdPartEntity::getBatch))
                .list()
                .stream()
                .map(ConvertTransferThirdPartEntity::getBatch)
                .collect(Collectors.toList());
        return R.ok(batchList);
    }

}
