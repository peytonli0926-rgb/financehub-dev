package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.PostalStorageFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PostalStorageFeeDetailsDTO;
import com.utfinancing.financehub.engine.finance.model.vo.PostalStorageFeeDetailsVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IPostalStorageFeeDetailsService;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @Author : hzhao
 * @Date : Create in 2023-11-17
 * @Description :   PostalStorageFeeDetails控制器实现类
 * @Modified :
 */
@ApiIgnore
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/postal-storage-fee-details")
public class PostalStorageFeeDetailsController {

    private final IPostalStorageFeeDetailsService  postalStorageFeeDetailsService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody PostalStorageFeeDetailsDTO dto) {
        return R.ok(postalStorageFeeDetailsService.savePostalStorageFeeDetails(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody PostalStorageFeeDetailsDTO dto) {
        return R.ok(postalStorageFeeDetailsService.updatePostalStorageFeeDetails(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(postalStorageFeeDetailsService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<PostalStorageFeeDetailsDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(postalStorageFeeDetailsService.getPostalStorageFeeDetailsDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<PostalStorageFeeDetailsVO>> page(@RequestBody @Valid PostalStorageFeeDetailsQueryDTO queryDTO) {
        return R.ok(postalStorageFeeDetailsService.selectPage(queryDTO));
    }

}



