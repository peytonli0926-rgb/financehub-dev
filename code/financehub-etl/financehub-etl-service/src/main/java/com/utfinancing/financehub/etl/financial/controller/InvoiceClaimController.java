package com.utfinancing.financehub.etl.financial.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.model.dto.CourtCostVerificationDTO;
import com.utfinancing.financehub.etl.financial.model.dto.ContractQueryInfoDTO;
import com.utfinancing.financehub.etl.financial.model.dto.InvoiceClaimQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.InvoiceClaimDTO;
import com.utfinancing.financehub.etl.financial.model.vo.ContractInvoiceClaimVO;
import com.utfinancing.financehub.etl.financial.model.vo.InvoiceClaimExcelVO;
import com.utfinancing.financehub.etl.financial.model.vo.InvoiceClaimVO;
import com.utfinancing.financehub.etl.financial.service.InvoiceService;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceMiddleQueryDTO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import com.utfinancing.financehub.etl.financial.service.IInvoiceClaimService;


/**
 * @Author : bruyang
 * @Date : Create in 2023-11-10
 * @Description :   InvoiceClaim控制器实现类
 * @Modified :
 */
@Api(tags = "开票认领")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/financial/invoice-claim")
public class InvoiceClaimController {

    private final InvoiceService invoiceService;

    private final IInvoiceClaimService invoiceClaimService;

    @ApiOperation(value = "开票认领同步数据")
    @PostMapping("/syncInvoicingSystem")
    public R<Boolean> syncInvoicingSystem(@RequestBody @Valid InvoiceClaimQueryDTO queryDTO) {
        return R.ok(invoiceService.syncInvoicingSystem(queryDTO));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<InvoiceClaimVO>> page(@RequestBody @Valid InvoiceClaimQueryDTO queryDTO) {
        return R.ok(invoiceClaimService.selectPage(queryDTO));
    }


    @ApiOperation(value = "导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid InvoiceClaimQueryDTO queryDTO) {
        try {
            List<InvoiceClaimVO> list = invoiceClaimService.listByCondition(queryDTO);
            ExcelUtil<InvoiceClaimExcelVO> util = new ExcelUtil<InvoiceClaimExcelVO>(InvoiceClaimExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("开票认领.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, InvoiceClaimExcelVO.class), "开票认领详情");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("导出失败，失败原因："+e.getMessage());
        }
    }

    @ApiOperation(value = "合同分页查询开票认领接口")
    @PostMapping("/selectContractInvoiceByPage")
    public R<IPage<ContractInvoiceClaimVO>> selectContractInvoiceByPage(@RequestBody @Valid ContractQueryInfoDTO queryDTO) {
        return R.ok(invoiceClaimService.selectContractInvoiceByPage(queryDTO));
    }

    @ApiOperation(value = "合同查询开票认领List")
    @PostMapping("/selectContractInvoiceList")
    public R<List<ContractInvoiceClaimVO>> selectContractInvoiceList(@RequestBody @Valid ContractQueryInfoDTO queryDTO) {
        return R.ok(invoiceClaimService.selectContractInvoiceList(queryDTO));
    }

    @ApiOperation(value = "合同下载开票认领接口")
    @PostMapping("/download")
    public void download(HttpServletResponse response, @RequestBody @Valid ContractQueryInfoDTO queryDTO) {
        try {
            List<ContractInvoiceClaimVO> list = invoiceClaimService.selectContractInvoiceClaimByCondition(queryDTO);
            ExcelUtil<ContractInvoiceClaimVO> util = new ExcelUtil<ContractInvoiceClaimVO>(ContractInvoiceClaimVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("开票认领.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, ContractInvoiceClaimVO.class), "开票认领详情");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("导出失败，失败原因："+e.getMessage());
        }
    }

    @ApiOperation(value = "开票认领生成凭证")
    @PostMapping("/invoiceGenerateVoucher")
    public R<Boolean> invoiceGenerateVoucher() {
        return R.ok(invoiceService.invoiceGenerateVoucher());
    }

    @ApiOperation(value = "开票认领同步数据根据开始结束时间批量跑")
    @PostMapping("/syncInvoicingSystemByDay")
    public R<Boolean> syncInvoicingSystemByDay(@RequestBody @Valid InvoiceClaimQueryDTO queryDTO) {
        return R.ok(invoiceService.syncInvoicingSystemByDay(queryDTO));
    }

}



