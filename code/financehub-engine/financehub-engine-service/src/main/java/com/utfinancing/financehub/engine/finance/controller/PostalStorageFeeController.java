package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.PostalStorageFeeDetailsVO;
import com.utfinancing.financehub.engine.finance.model.vo.PostalStorageFeeVO;
import com.utfinancing.financehub.engine.finance.service.IPostalStorageFeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @Author : hzhao
 * @Date : Create in 2023-11-17
 * @Description :   PostalStorageFee控制器实现类
 * @Modified :
 */
@Api(tags = "邮储手续费")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/postal-storage-fee")
public class PostalStorageFeeController {

    private final IPostalStorageFeeService postalStorageFeeService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody PostalStorageFeeDTO dto) {
        return R.ok(postalStorageFeeService.savePostalStorageFee(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody PostalStorageFeeDTO dto) {
        return R.ok(postalStorageFeeService.updatePostalStorageFee(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(postalStorageFeeService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<PostalStorageFeeDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(postalStorageFeeService.getPostalStorageFeeDTOById(id));
    }

    @ApiOperation(value = "生成分摊信息")
    @PostMapping("/generate")
    public R generate(@RequestBody @Valid PostalStorageFeeQueryDTO queryDTO) {
        postalStorageFeeService.generate(queryDTO);
        return R.ok();
    }

    @ApiOperation(value = "生成凭证")
    @PostMapping("/voucher")
    public R voucher(@RequestBody List<Long> ids) {
        postalStorageFeeService.voucher(ids);
        return R.ok();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<PostalStorageFeeVO>> page(@RequestBody @Valid PostalStorageFeeQueryDTO queryDTO) {
        return R.ok(postalStorageFeeService.selectPage(queryDTO));
    }

    @ApiOperation(value = "详情分页查询")
    @PostMapping("/detail/page")
    public R<IPage<PostalStorageFeeDetailsVO>> selectDetailPage(@RequestBody @Valid PostalStorageFeeDetailsQueryDTO queryDTO) {
        return R.ok(postalStorageFeeService.selectDetailPage(queryDTO));
    }

    @ApiOperation(value = "详情-修改")
    @PostMapping("/detail/update")
    public R detailUpdate(@RequestBody @Valid PostalStorageFeeDetailsUpdateDTO saveDTO) {
        postalStorageFeeService.detailUpdate(saveDTO);
        return R.ok();
    }

    @ApiOperation(value = "提交")
    @PostMapping("/submit")
    public R submit(@RequestBody List<Long> ids) {
        postalStorageFeeService.submit(ids);
        return R.ok();
    }

    @ApiOperation(value = "撤回")
    @PostMapping("/withdraw")
    public R withdraw(@RequestBody List<Long> ids) {
        postalStorageFeeService.withdraw(ids);
        return R.ok();
    }

    @ApiOperation(value = "复核通过")
    @PostMapping("/check/pass")
    public R pass(@RequestBody List<Long> ids) {
        postalStorageFeeService.pass(ids);
        return R.ok();
    }

    @ApiOperation(value = "复核失败")
    @PostMapping("/check/fail")
    public R fail(@RequestBody List<Long> ids) {
        postalStorageFeeService.fail(ids);
        return R.ok();
    }

}



