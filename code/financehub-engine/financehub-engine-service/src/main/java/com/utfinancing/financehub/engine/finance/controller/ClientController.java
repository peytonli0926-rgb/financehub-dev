package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.ClientQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ClientDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ClientVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IClientService;


/**
 * @Author : lixin
 * @Date : Create in 2023-09-13
 * @Description :   Client控制器实现类
 * @Modified :
 */
@Api(tags = "客户")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/client")
public class ClientController {

    private final IClientService  clientService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ClientDTO dto) {
        return R.ok(clientService.saveClient(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ClientDTO dto) {
        return R.ok(clientService.updateClient(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(clientService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ClientDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(clientService.getClientDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ClientVO>> page(@RequestBody @Valid ClientQueryDTO queryDTO) {
        return R.ok(clientService.selectPage(queryDTO));
    }

}



