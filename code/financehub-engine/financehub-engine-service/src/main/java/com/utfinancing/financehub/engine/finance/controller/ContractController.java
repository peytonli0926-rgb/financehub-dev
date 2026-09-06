package com.utfinancing.financehub.engine.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.dw.service.ITInfoPerformanceAttributionSyncService;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.VehicleBusinessModelEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.finance.service.IVehicleBusinessModelService;


/**
 * @Author : lixin
 * @Date : Create in 2023-09-13
 * @Description :   Contract控制器实现类
 * @Modified :
 */
@Api(tags = "合同")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/contract")
public class ContractController {

    private final IContractService contractService;
    private final IVehicleBusinessModelService vehicleBusinessModelService;

    private final ITInfoPerformanceAttributionSyncService itInfoPerformanceAttributionSyncService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ContractDTO dto) {
        return R.ok(contractService.saveContract(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ContractDTO dto) {
        return R.ok(contractService.updateContract(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(contractService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ContractDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(contractService.getContractDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ContractVO>> page(@RequestBody @Valid ContractQueryDTO queryDTO) {
        return R.ok(contractService.selectPage(queryDTO));
    }

    @ApiOperation(value = "乘用车单合同查询导出")
    @PostMapping("/vehicleExport")
    public void vehicleExport(HttpServletResponse response, @RequestBody @Valid ContractQueryDTO queryDTO) {
        try {
            ExcelUtil<VehicleContractExcelVO> util = new ExcelUtil<>(VehicleContractExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename="
                    + URLEncoder.encode("乘用车单合同查询.xlsx", "utf8"));
            util.exportExcel(response, vehicleBusinessModelService.export(queryDTO), "单合同查询");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("导出乘用车合同数据失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "获取合同详情（合同交易结构）")
    @GetMapping("/getContractDetail/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ContractDetailDTO> getContractDetail(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(contractService.getContractDetail(id));
    }

    @ApiOperation(value = "乘用车合同全生命周期")
    @GetMapping("/vehicleLifecycle/{id}")
    public R<VehicleLifecycleVO> vehicleLifecycle(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(vehicleBusinessModelService.getLifecycle(id));
    }

    @ApiOperation(value = "合同分页查询交叉销售分成")
    @PostMapping("/salesBonusPage")
    public R<IPage<ContractTInfoPerformanceAttributionVO>> salesBonusPage(@RequestBody ContractQueryInfoDTO queryDTO) {
        return R.ok(contractService.salesBonusPage(queryDTO));
    }

    @ApiOperation(value = "合同分页查询回笼计划")
    @PostMapping("/selectPageByContractCode")
    public R<IPage<ContractRepaymentPlanVO>> selectPageByContractCode(@RequestBody @Valid ContractQueryInfoDTO queryDTO) {
        return R.ok(contractService.selectPageByContractCode(queryDTO));
    }

    @ApiOperation(value = "合同回笼计划下载")
    @PostMapping("/repayPlanDownload")
    public void repayPlanDownload(HttpServletResponse response, @RequestBody @Valid ContractQueryInfoDTO queryDTO) {
        try {
            List<ContractRepaymentPlanVO> list = contractService.selectByContractCode(queryDTO);
            ExcelUtil<ContractRepaymentPlanVO> util = new ExcelUtil<ContractRepaymentPlanVO>(ContractRepaymentPlanVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("回笼计划.xlsx", "utf8"));
            util.exportExcel(response, list, "回笼计划");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("导出失败，失败原因：" + e.getMessage());
        }
    }

    @ApiOperation(value = "合同分页查询回笼计划最新版本日期")
    @GetMapping("/selectPageByContractCode/{id}")
    public R<List<String>> getLatestVersionDate(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(contractService.getLatestVersionDate(id));
    }

    @ApiOperation(value = "分页查询（合同科目余额信息）")
    @PostMapping("/accountBalanceByPage")
    public R<IPage<ContractAccountBalanceVO>> accountBalanceByPage(@RequestBody @Valid ContractQueryInfoDTO queryDTO) {
        return R.ok(contractService.accountBalanceByPage(queryDTO));
    }


    @ApiOperation(value = "线上和线下合同分页查询")
    @PostMapping("/allContractPage")
    public R<IPage<ContractVO>> allContractPage(@RequestBody @Valid ContractQueryDTO queryDTO) {
        return R.ok(contractService.allContractPage(queryDTO));
    }


    @ApiOperation(value = "根据合同编码模糊查询合同信息")
    @PostMapping("/likeQueryContractByCode")
    public R<List<ContractEntity>> likeQueryContractByCode(@RequestBody @Valid String contractCode) {
        return R.ok(contractService.getContractDTOByCode(contractCode));
    }


    @ApiOperation(value = "分页查询（合同交易信息）")
    @PostMapping("/transactionByPage")
    public R<IPage<ContractTransactionVO>> transactionByPage(@RequestBody @Valid ContractQueryInfoDTO queryDTO) {
        return R.ok(contractService.transactionByPage(queryDTO));
    }

    @ApiOperation(value = "合同汇总页导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid ContractQueryDTO queryDTO) {
        try {
            List<ContractExcelVO> contractExcelVOList = contractService.export(queryDTO);
            ExcelUtil<ContractExcelVO> util = new ExcelUtil<ContractExcelVO>(ContractExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("合同汇总.xlsx", "utf8"));
            util.exportExcel(response, contractExcelVOList, "合同汇总");
        } catch (UnsupportedEncodingException e) {
            log.error("exportDetailInfos error", e);
            throw new ServiceException("导出合同数据失败，失败原因：" + e.getMessage());
        }
    }

    @ApiOperation(value = "根据合同编码和机构id查询合同信息")
    @PostMapping("/queryContractInfo")
    public R<ContractDTO> queryContractInfo(@RequestBody @Valid QueryContractInfoDTO params) {
        return R.ok(contractService.getContractDTOByCode(params.getContractCode(), params.getOrgId()));
    }


    @ApiOperation(value = "合同基本信息同步")
    @PostMapping("/basicDataSync")
    public void basicDataSync(@RequestParam("leaseDateStart") String leaseDateStart) {
        contractService.basicDataSync(leaseDateStart);
    }


    @ApiOperation(value = "刷新A合同基本信息")
    @PostMapping("/updateContractABasicDataSync")
    public void updateContractABasicDataSync() {
        contractService.updateContractABasicDataSync();
    }


    @ApiOperation(value = "同步合同丢失数据")
    @GetMapping("/SyncLossData")
    public void syncLossData(@RequestParam("startData") String startData) {
        contractService.syncLossData(startData);
    }

}



