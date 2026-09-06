package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferPlanEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferPlanDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferPlanVO;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferPlanService;
import com.utfinancing.financehub.engine.utils.ExcelExportUtil;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;


/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description :   ConvertTransferPlan控制器实现类
 * @Modified :
 */
@Api(tags = "资产转让-折价转让-租金计划")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/convert-transfer-plan")
public class ConvertTransferPlanController {

    private final IConvertTransferPlanService convertTransferPlanService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ConvertTransferPlanDTO dto) {
        return R.ok(convertTransferPlanService.saveConvertTransferPlan(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ConvertTransferPlanDTO dto) {
        return R.ok(convertTransferPlanService.updateConvertTransferPlan(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(convertTransferPlanService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ConvertTransferPlanDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(convertTransferPlanService.getConvertTransferPlanDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ConvertTransferPlanVO>> page(@RequestBody @Valid ConvertTransferPlanQueryDTO queryDTO) {
        return R.ok(convertTransferPlanService.selectPage(queryDTO));
    }

    @ApiOperation(value = "导出")
    @PostMapping("/export")
    public void export(@RequestBody @Valid ConvertTransferPlanQueryDTO queryDTO, @ApiIgnore HttpServletResponse response) throws UnsupportedEncodingException {
        List<ConvertTransferPlanEntity> list = convertTransferPlanService.lambdaQuery()
                .eq(ConvertTransferPlanEntity::getConvertTransferId, queryDTO.getConvertTransferId())
                .eq(Objects.nonNull(queryDTO.getOldContractCode()), ConvertTransferPlanEntity::getOldContractCode, queryDTO.getOldContractCode())
                .list();
        ExcelExportUtil.export(response, ListBeanUtil.copyList(list, ConvertTransferPlanVO.class), ConvertTransferPlanVO.class,  "租金计划");

    }

}



