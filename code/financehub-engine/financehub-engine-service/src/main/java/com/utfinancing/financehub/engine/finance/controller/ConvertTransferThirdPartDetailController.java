package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartDetailEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartCheckVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartDetailVO;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferThirdPartDetailService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferThirdPartService;
import com.utfinancing.financehub.engine.utils.ExcelExportUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "资产转让 - 第三方转让 - 明细")
@RequestMapping("/finance/convert-transfer-third-detail")
public class ConvertTransferThirdPartDetailController {


    private final IConvertTransferThirdPartDetailService convertTransferThirdPartDetailService;

    public ConvertTransferThirdPartDetailController(IConvertTransferThirdPartService convertTransferThirdPartService, IConvertTransferThirdPartDetailService convertTransferThirdPartDetailService) {
        this.convertTransferThirdPartDetailService = convertTransferThirdPartDetailService;
    }

    @PostMapping("/page")
    @ApiOperation("分页列表")
    public R<IPage<ConvertTransferThirdPartDetailVO>> page(@RequestBody ConvertTransferThirdPartDetailQueryDTO dto) {
        Page<ConvertTransferThirdPartDetailEntity> page = convertTransferThirdPartDetailService
                .lambdaQuery()
                .eq(ConvertTransferThirdPartDetailEntity::getTransferId, dto.getTransferId())
                .page(new Page<>(dto.getPageNum(), dto.getPageSize()));
        return R.ok(page.convert(ConvertTransferThirdPartDetailVO::from));
    }

    @ApiOperation("导出")
    @PostMapping("/export")
    public void export(@RequestBody ConvertTransferThirdPartDetailQueryDTO dto, @ApiIgnore HttpServletResponse response) {
        List<ConvertTransferThirdPartDetailVO> detailVOS = convertTransferThirdPartDetailService.lambdaQuery()
                .eq(ConvertTransferThirdPartDetailEntity::getTransferId, dto.getTransferId())
                .list()
                .stream()
                .map(ConvertTransferThirdPartDetailVO::from)
                .collect(Collectors.toList());
        ExcelExportUtil.export(response, detailVOS, ConvertTransferThirdPartDetailVO.class, "资产转让第三方-明细");
    }

    @ApiOperation("科目校验")
    @PostMapping("/check")
    public R<IPage<ConvertTransferThirdPartCheckVO>> check(@Valid @RequestBody ConvertTransferThirdPartDetailQueryDTO dto) {
        return R.ok(convertTransferThirdPartDetailService.check(dto));
    }
}
