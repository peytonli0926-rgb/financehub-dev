package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferDetailDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferCheckVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferDetailVO;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description :   ConvertTransferDetail控制器实现类
 * @Modified :
 */
@Api(tags = "资产转让-折价转让-详情")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/convert-transfer-detail")
public class ConvertTransferDetailController {

    private final IConvertTransferDetailService  convertTransferDetailService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ConvertTransferDetailDTO dto) {
        return R.ok(convertTransferDetailService.saveConvertTransferDetail(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ConvertTransferDetailDTO dto) {
        return R.ok(convertTransferDetailService.updateConvertTransferDetail(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(convertTransferDetailService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ConvertTransferDetailDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(convertTransferDetailService.getConvertTransferDetailDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ConvertTransferDetailVO>> page(@RequestBody @Valid ConvertTransferDetailQueryDTO queryDTO) {
        return R.ok(convertTransferDetailService.selectPage(queryDTO));
    }

    @ApiOperation("科目校验")
    @PostMapping("/check")
    public R<IPage<ConvertTransferCheckVO>> check(@RequestBody ConvertTransferDetailQueryDTO queryDTO) {
        return R.ok(convertTransferDetailService.check(queryDTO));
    }

}



