package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferContractFeeDetailEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferContractFeeEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferContractFeeDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferContractFeeDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferContractFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferContractFeeDetailExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferContractFeeDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferContractFeeExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferContractFeeVO;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferContractFeeDetailService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferContractFeeService;
import com.utfinancing.financehub.engine.utils.ExcelExportUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Api(tags = "资产转让 - 转让合同费")
@RestController
@RequestMapping("finance/convert-transfer-contract-fee")
public class ConvertTransferContractFeeController {

    private final IConvertTransferContractFeeService transferContractFeeService;

    private final IConvertTransferContractFeeDetailService transferContractFeeDetailService;


    public ConvertTransferContractFeeController(IConvertTransferContractFeeService transferContractFeeService, IConvertTransferContractFeeDetailService transferContractFeeDetailService) {
        this.transferContractFeeService = transferContractFeeService;
        this.transferContractFeeDetailService = transferContractFeeDetailService;
    }

    @ApiOperation("下载导入模板")
    @PostMapping("/template")
    public void template(@ApiIgnore HttpServletResponse response) {
        ExcelExportUtil.export(response, Collections.emptyList(), ConvertTransferContractFeeDTO.class, "资产转让-转让合同费用");
    }

    @ApiOperation(value = "上传")
    @PostMapping("/importFile")
    public R<Boolean> importFile(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        List<ConvertTransferContractFeeDTO> dtos;
        try {
            ExcelUtil<ConvertTransferContractFeeDTO> util = new ExcelUtil<>(ConvertTransferContractFeeDTO.class);
            dtos = util.importExcel(file.getInputStream());
        }catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.ok(transferContractFeeService.importFile(dtos));
    }

    @ApiOperation("分页列表")
    @PostMapping("/page")
    public R<IPage<ConvertTransferContractFeeVO>> page(@RequestBody ConvertTransferContractFeeQueryDTO dto) {
        IPage<ConvertTransferContractFeeVO> page = transferContractFeeService.pageQuery(dto);
        return R.ok(page);
    }

    @ApiOperation("导出")
    @PostMapping("/export")
    public void export(@RequestBody ConvertTransferContractFeeQueryDTO dto, @ApiIgnore HttpServletResponse response) {
        List<ConvertTransferContractFeeExcelVO> vos = transferContractFeeService.export(dto.getAccountDate(), dto.getOrgIdList());
        ExcelExportUtil.export(response, vos, ConvertTransferContractFeeExcelVO.class, "资产转让-转让合同费用");
    }


    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(transferContractFeeService.generateVoucher(ids));
    }


    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(transferContractFeeService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(transferContractFeeService.withdraw(ids));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(transferContractFeeService.delete(ids));
    }



    @ApiOperation("详细 - 分页列表")
    @PostMapping("/detail")
    public R<IPage<ConvertTransferContractFeeDetailVO>> transferDetail(@RequestBody ConvertTransferContractFeeDetailQueryDTO dto){
        IPage<ConvertTransferContractFeeDetailVO> page = transferContractFeeDetailService.lambdaQuery()
                .eq(Objects.nonNull(dto.getTransferId()), ConvertTransferContractFeeDetailEntity::getTransferId, dto.getTransferId())
                .eq(Objects.nonNull(dto.getTransferFeeType()),ConvertTransferContractFeeDetailEntity::getTransferFeeType, dto.getTransferFeeType())
                .like(StringUtils.isNotEmpty(dto.getContractCode()),ConvertTransferContractFeeDetailEntity::getContractCode, dto.getContractCode())
                .page(new Page<>(dto.getPageNum(), dto.getPageSize()))
                .convert(ConvertTransferContractFeeDetailVO::new);
        return R.ok(page);
    }

    @ApiOperation("详细 - 导出")
    @PostMapping("/detail/export")
    public void detailExport(@RequestBody ConvertTransferContractFeeDetailQueryDTO dto, @ApiIgnore HttpServletResponse response) {
        List<ConvertTransferContractFeeDetailExcelVO> vos = transferContractFeeDetailService.export(dto.getTransferId(), dto.getTransferFeeType(), dto.getContractCode());
        ExcelExportUtil.export(response, vos, ConvertTransferContractFeeDetailExcelVO.class, "资产转让-转让合同费用明细");
    }

}
