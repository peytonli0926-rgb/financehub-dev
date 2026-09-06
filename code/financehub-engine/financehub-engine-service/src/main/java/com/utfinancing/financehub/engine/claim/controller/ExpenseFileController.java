package com.utfinancing.financehub.engine.claim.controller;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.claim.model.dto.ExpenseTypeDataSyncDTO;
import com.utfinancing.financehub.engine.claim.service.IExpenseFileService;
import com.utfinancing.financehub.engine.file.service.FilezService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * @Author : robjiang
 * @Date : Create in 2024-09-10
 * @Description :   费用附件下载
 * @Modified :
 */
@Api(tags = "费用附件下载接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/integration/expenseFile")
public class ExpenseFileController {

    private final IExpenseFileService expenseFileService;

    /**
     * 同步费用类型的数据
     */
    @PostMapping("/expenseTypeDataSync")
    @ApiOperation(value = "费用类型数据同步")
    public R<String> expenseTypeDataSync() throws Exception {
        expenseFileService.expenseTypeDataSync();
//        expenseFileService.expenseTypeDataSyncTest();
        return R.ok("费用类型数据同步完成!");
    }

}



