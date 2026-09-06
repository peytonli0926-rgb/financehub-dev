package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.BankAccountVO;
import com.utfinancing.financehub.engine.finance.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;


/**
 * @Author : lixin
 * @Date : Create in 2024-01-31
 * @Description :   金蝶基础数据选项接口
 * @Modified :
 */
@Api(tags = "金蝶基础数据选项接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/kingdee/option")
public class KingdeeBaseOptionController {

    private final IKingdeePersonService kingdeePersonService;
    private final IKingdeeBankService kingdeeBankService;
    private final IKingdeeCostcenterService kingdeeCostcenterService;
    private final IKingdeeGeneralAsstService kingdeeGeneralAsstService;

    @PostMapping("/queryPerson")
    @ApiOperation(value = "搜索职员")
    public R<IPage<KingdeePersonDTO>> queryPerson(@RequestBody @Valid KingdeeOptionQueryDTO queryDTO) {
        return R.ok(kingdeePersonService.selectPage(queryDTO));
    }


    @PostMapping("/queryBank")
    @ApiOperation(value = "搜索金融机构")
    public R<IPage<KingdeeBankDTO>> queryBank(@RequestBody @Valid KingdeeOptionQueryDTO queryDTO) {
        return R.ok(kingdeeBankService.selectPage(queryDTO));
    }

    @PostMapping("/queryCostcenter")
    @ApiOperation(value = "搜索成本中心")
    public R<IPage<KingdeeCostcenterDTO>> queryCostcenter(@RequestBody @Valid KingdeeOptionQueryDTO queryDTO) {
        return R.ok(kingdeeCostcenterService.selectPage(queryDTO));
    }

    @PostMapping("/queryGeneralAsst")
    @ApiOperation(value = "搜索自定义核算项目")
    public R<IPage<KingdeeGeneralAsstDTO>> queryGeneralAsst(@RequestBody @Valid KingdeeOptionQueryDTO queryDTO) {
        return R.ok(kingdeeGeneralAsstService.selectPage(queryDTO));
    }

}



