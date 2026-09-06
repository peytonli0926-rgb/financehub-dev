package com.utfinancing.financehub.etl.financial.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.financial.model.dto.SendEas2ResultQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.SendEas2ResultDTO;
import com.utfinancing.financehub.etl.financial.model.vo.SendEas2ResultVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.etl.financial.service.ISendEas2ResultService;


/**
 * @Author : bruyang
 * @Date : Create in 2024-04-10
 * @Description :   SendEas2Result控制器实现类
 * @Modified :
 */
@Api(tags = "记录发送EAS2数据是否成功表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/financial/send-eas2-result")
public class SendEas2ResultController {

    private final ISendEas2ResultService  sendEas2ResultService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody SendEas2ResultDTO dto) {
        return R.ok(sendEas2ResultService.saveSendEas2Result(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody SendEas2ResultDTO dto) {
        return R.ok(sendEas2ResultService.updateSendEas2Result(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sendEas2ResultService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<SendEas2ResultDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(sendEas2ResultService.getSendEas2ResultDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<SendEas2ResultVO>> page(@RequestBody @Valid SendEas2ResultQueryDTO queryDTO) {
        return R.ok(sendEas2ResultService.selectPage(queryDTO));
    }

}



