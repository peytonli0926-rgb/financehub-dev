package com.utfinancing.financehub.etl.financial.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.utfinancing.financehub.etl.financial.service.ICheckSqlService;


/**
 * @Author : jnc
 * @Date : Create in 2024-03-29
 * @Description :   CheckSql控制器实现类
 * @Modified :
 */
@Api(tags = "对账对接其他业务系统Controller")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/financial/check-sql")
public class CheckSqlController {

    private final ICheckSqlService  checkSqlService;

    @GetMapping("/common/saveToTmp")
    @ApiOperation(value = "将与中台对账的对接系统在期间内数据保存到中台临时表")
    public R<String> saveCommonToTmp(@RequestParam(value = "periodCode", required = true)Integer periodCode,
//                                      @RequestParam(value = "systemCode", required = true)String systemCode,
                                      @RequestParam(value = "sqlMark", required = true)String sqlMark) {
        return R.ok(checkSqlService.saveCommonToTmp(periodCode, sqlMark));
    }
}



