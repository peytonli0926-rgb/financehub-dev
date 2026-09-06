package com.utfinancing.financehub.engine.rule.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.rule.model.dto.RawTransactionDataDTO;
import com.utfinancing.financehub.engine.rule.model.dto.RawTransactionDataQueryDTO;
import com.utfinancing.financehub.engine.rule.model.vo.RawTransactionDataVO;
import com.utfinancing.financehub.engine.rule.service.IRawTransactionDataService;
import com.utfinancing.financehub.engine.rule.entity.RawTransactionDataEntity;
import com.utfinancing.financehub.engine.rule.entity.InterfaceDataEntity;
import com.utfinancing.financehub.engine.xxlJob.DataExecutionJobHandler;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.utfinancing.financehub.engine.rule.service.IInterfaceDataService;

import java.util.List;
import java.util.Map;


/**
 * @Author : lixin
 * @Date : Create in 2023-09-14
 * @Description :   InterfaceData控制器实现类
 * @Modified :
 */
@Api(tags = "接口数据")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rule/interface")
public class InterfaceDataController {

    private final IInterfaceDataService  interfaceDataService;
    private final IRawTransactionDataService rawTransactionDataService;
    private final RemoteDictService remoteDictService;
    private final DataExecutionJobHandler dataExecutionJobHandler;


    @ApiOperation(value = "金额类型列表")
    @GetMapping("/listDictData")
    public R<List<SysDictData>> listDictData() throws Exception {
        return R.ok(remoteDictService.listDictData("sys_cash_type").getData());
    }

    @ApiOperation(value = "获取原始数据")
    @GetMapping("/getRawData/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<RawTransactionDataDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(rawTransactionDataService.getRawTransationDataDTOById(id));
    }

    @ApiOperation(value = "分页查询原始数据")
    @PostMapping("/pageRawData")
    public R<IPage<RawTransactionDataVO>> page(@RequestBody @Valid RawTransactionDataQueryDTO queryDTO) {
        return R.ok(rawTransactionDataService.selectPage(queryDTO));
    }

    @PostMapping("/saveRawData")
    @ApiOperation(value = "新增原始数据")
    public R<Long> save(@Valid @RequestBody JSONObject jsonObject) {
        RawTransactionDataEntity entity = rawTransactionDataService.saveRawData(null, jsonObject);
        dataExecutionJobHandler.executeRawData(entity.getId());
        return R.ok(entity.getId());
    }

    @PostMapping("/retryRawData/{id}")
    @ApiOperation(value = "重新执行接口原始单据")
    public R<Boolean> retry(@PathVariable("id") @Valid @NotNull Long id) {
        dataExecutionJobHandler.validateRetryRawData(id);
        dataExecutionJobHandler.executeRawData(id);
        return R.ok(Boolean.TRUE);
    }

    @GetMapping("/getVoucherInterfaceDataId/{id}")
    @ApiOperation(value = "获取原始单据生成凭证所关联的接口数据ID")
    public R<Long> getVoucherInterfaceDataId(@PathVariable("id") @Valid @NotNull Long id) {
        InterfaceDataEntity entity = interfaceDataService.lambdaQuery()
                .eq(InterfaceDataEntity::getInterfaceId, id)
                .orderByDesc(InterfaceDataEntity::getCreateTime)
                .last("limit 1")
                .one();
        return R.ok(entity == null ? null : entity.getId());
    }

}



