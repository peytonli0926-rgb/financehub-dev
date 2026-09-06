package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.enums.CollectionTypeEnum;
import com.utfinancing.financehub.engine.enums.FundCurrencyTypeEnum;
import com.utfinancing.financehub.engine.finance.entity.FundEbankTransactionDataEntity;
import com.utfinancing.financehub.engine.finance.model.dto.FundEbankTransactionDataQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundEbankTransactionDataDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundSystemBalanceQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundEbankTransactionDataVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.utfinancing.financehub.engine.finance.service.IFundEbankTransactionDataService;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @Author : hzhao
 * @Date : Create in 2023-10-17
 * @Description :   FundEbankTransactionData控制器实现类
 * @Modified :
 */
@ApiIgnore
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/fund-ebank-transaction-data")
public class FundEbankTransactionDataController {

    private final IFundEbankTransactionDataService  fundEbankTransactionDataService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody FundEbankTransactionDataDTO dto) {
        return R.ok(fundEbankTransactionDataService.saveFundEbankTransactionData(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody FundEbankTransactionDataDTO dto) {
        return R.ok(fundEbankTransactionDataService.updateFundEbankTransactionData(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(fundEbankTransactionDataService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<FundEbankTransactionDataDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(fundEbankTransactionDataService.getFundEbankTransactionDataDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<FundEbankTransactionDataVO>> page(@RequestBody @Valid FundEbankTransactionDataQueryDTO queryDTO) {
        return R.ok(fundEbankTransactionDataService.selectPage(queryDTO));
    }

//    @ApiOperation(value = "测试mq")
//    @PostMapping("/testMq")
//    public void testMq() {
//        fundEbankTransactionDataService.saveRawData(getJsonArray());
//    }
//
//    private JSONArray getJsonArray() {
////        String test = "[{\"bankAmount\":5965,\"bankSummary\":\"退调解费退调解费\",\"businessDate\":\"2024-11-08 00:00:00\",\"businessOperation\":\"0\",\"clientAccountsBank\":\"中国建设银行股份有限公司天津于家堡支行\",\"clientAccountsBankNo\":\"12050110663300000271\",\"clientName\":\"天津市滨海新区公信调解中心\",\"collectionAccountsBank\":\"工行市分行二营一般户-1587\",\"collectionAccountsBankNo\":\"1001190719016271587\",\"collectionType\":\"VC_SHOUKLX01\",\"comment\":\"退调解费\",\"currencyType\":\"currency_type1\",\"ebankNumber\":\"2024110801000275\",\"operationDate\":\"2024-11-11 10:18:53\",\"orderId\":\"2024110801000275\",\"transactionType\":\"collection\"}]";
//
//        String filePath = "C:\\temp\\20241111.txt"; // 替换为你的文件路径
//
//        StringBuffer josn = new StringBuffer();
//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                josn = josn.append(line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return JSONArray.parse(josn.toString());
//    }
}



