package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.service.IClientService;
import com.utfinancing.financehub.engine.finance.service.IContractBalanceService;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.InputStream;
import java.util.List;
import java.util.Map;


/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description :   数据导入接口
 * @Modified :
 */
@Api(tags = "数据导入")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/import")
public class ImportController {

    private final IClientService clientService;
    private final IContractService contractService;
    private final IContractBalanceService contractBalanceService;
    private final IRuleService iRuleService;

    @ApiOperation(value = "合同数据上传")
    @PostMapping("/importData")
    public R importData(MultipartFile file,String sheetName) throws Exception {
        ExcelUtil<ImportClientExcel> util = new ExcelUtil<ImportClientExcel>(ImportClientExcel.class);
        ExcelUtil<ImportContractExcel> util2 = new ExcelUtil<ImportContractExcel>(ImportContractExcel.class);
        ExcelUtil<ImportContractBalanceExcel> util3 = new ExcelUtil<ImportContractBalanceExcel>(ImportContractBalanceExcel.class);
        InputStream inputStream = file.getInputStream();
        InputStream inputStream2 = file.getInputStream();
        InputStream inputStream3 = file.getInputStream();
        try {
//            List<ImportClientExcel> list = util.importExcel("客户", inputStream, 0);
//            clientService.importData(list);
            List<ImportContractExcel> list2 = util2.importExcel(sheetName, inputStream2, 0);
            if (CollectionUtils.isNotEmpty(list2)) {
                contractService.importData2(list2);
            }
            List<ImportContractBalanceExcel> list3 = util3.importExcel(sheetName, inputStream3, 0);
            if (CollectionUtils.isNotEmpty(list3)) {
                contractBalanceService.importData(list3);
            }
            return R.ok();
        } catch (Exception e) {
            log.error("导入异常--",e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(inputStream2);
            IOUtils.closeQuietly(inputStream3);
        }
    }
    @ApiOperation(value = "财务入库合同初始化数据")
    @PostMapping("/importData1")
    public R importData1(MultipartFile file,String sheetName) throws Exception {
//        ExcelUtil<ImportContractExcel1> util1 = new ExcelUtil<ImportContractExcel1>(ImportContractExcel1.class);
        ExcelUtil<ImportContractBalanceExcel1> util3 = new ExcelUtil<ImportContractBalanceExcel1>(ImportContractBalanceExcel1.class);
        InputStream inputStream2 = file.getInputStream();
        InputStream inputStream3 = file.getInputStream();
        try {
            // 期初数据导入 合同+合同余额
//            List<ImportContractExcel1> list = util1.importExcel(sheetName, inputStream2, 0);
//            if (CollectionUtils.isNotEmpty(list)) {
//                contractService.importData1(list);
//                contractBalanceService.importData1(list);
//            }
            // 导入凭证数据
            List<ImportContractBalanceExcel1> list3 = util3.importExcel(sheetName, inputStream3, 0);
            if (CollectionUtils.isNotEmpty(list3)) {
                list3.forEach(e->{
                    ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
                    commonDTO.setBusinessCode("ZLYW");
                    commonDTO.setContractCode(e.getContractCode());
                    commonDTO.setSceneCode("RKSH");
                    commonDTO.setOrgId(e.getOrgId());
                    commonDTO.setCurrencyType("CNY");
                    commonDTO.setSystemCode("CYCXT");
                    Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);
                    commonMap.put("contractStatus", "财务入库");
                    commonMap.put("gpsProcedureAmount", 0);
                    commonMap.put("implementMarginAdjustAmount", 0);
                    commonMap.put("recycleCarAmount", 0);
                    commonMap.put("receivableLeaseBalance", e.getReceivableLeaseBalance());
                    commonMap.put("residualBalance", e.getResidualBalance());
                    commonMap.put("receivableOuttaxBalance", e.getReceivableOuttaxBalance());
                    commonMap.put("unrealizedRevenueBalance", e.getUnrealizedRevenueBalance());
                    commonMap.put("receivableMarginBalance", e.getReceivableMarginBalance());
                    commonMap.put("receiveCost", e.getReceiveCost());
                    commonMap.put("provisionBalance", e.getProvisionBalance());
                    iRuleService.executeRule(commonMap);
                });
            }
            return R.ok();
        } catch (Exception e) {
            log.error("财务入库合同初始化数据导入异常--",e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream2);
            IOUtils.closeQuietly(inputStream3);
        }
    }

    @ApiOperation(value = "保证金合同数据上传")
    @PostMapping("/importData2")
    public R importData2(MultipartFile file,String sheetName) throws Exception {
//        ExcelUtil<ImportClientExcel> util = new ExcelUtil<ImportClientExcel>(ImportClientExcel.class);
        ExcelUtil<ImportContractExcel> util2 = new ExcelUtil<ImportContractExcel>(ImportContractExcel.class);
        ExcelUtil<ImportContractBalanceExcel> util3 = new ExcelUtil<ImportContractBalanceExcel>(ImportContractBalanceExcel.class);
//        InputStream inputStream = file.getInputStream();
        InputStream inputStream2 = file.getInputStream();
        InputStream inputStream3 = file.getInputStream();
        try {
//            List<ImportClientExcel> list = util.importExcel("客户", inputStream, 0);
//            clientService.importData(list);
            List<ImportContractExcel> list2 = util2.importExcel(sheetName, inputStream2, 0);
            if (CollectionUtils.isNotEmpty(list2)) {
                contractService.importData(list2);
            }
            List<ImportContractBalanceExcel> list3 = util3.importExcel(sheetName, inputStream3, 0);
            if (CollectionUtils.isNotEmpty(list3)) {
                contractBalanceService.importDataMargin(list3);
            }
            return R.ok();
        } catch (Exception e) {
            log.error("保证金导入异常--",e);
            return R.fail(e.getMessage());
        } finally {
//            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(inputStream2);
            IOUtils.closeQuietly(inputStream3);
        }
    }

}



