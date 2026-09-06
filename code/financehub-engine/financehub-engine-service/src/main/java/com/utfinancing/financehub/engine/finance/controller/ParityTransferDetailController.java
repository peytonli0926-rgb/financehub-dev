package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.ParityTransferDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ParityTransferDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ParityTransferDetailVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IParityTransferDetailService;


/**
 * @Author : bruyang
 * @Date : Create in 2024-04-02
 * @Description :   ParityTransferDetail控制器实现类
 * @Modified :
 */
@Api(tags = "平价转让详情")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/parity-transfer-detail")
public class ParityTransferDetailController {

    private final IParityTransferDetailService  parityTransferDetailService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ParityTransferDetailDTO dto) {
        return R.ok(parityTransferDetailService.saveParityTransferDetail(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ParityTransferDetailDTO dto) {
        return R.ok(parityTransferDetailService.updateParityTransferDetail(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(parityTransferDetailService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ParityTransferDetailDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(parityTransferDetailService.getParityTransferDetailDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ParityTransferDetailVO>> page(@RequestBody @Valid ParityTransferDetailQueryDTO queryDTO) {
        return R.ok(parityTransferDetailService.selectPage(queryDTO));
    }

}



