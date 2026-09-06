package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.DiscountedTransferInternalEntity;
import com.utfinancing.financehub.engine.finance.model.dto.DiscountedTransferQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.InternalTransferExcelDTO;
import com.utfinancing.financehub.engine.finance.model.dto.InternalTransferGenerateDTO;
import com.utfinancing.financehub.engine.finance.model.dto.InternalTransferQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.InternalTransferVO;
import com.utfinancing.financehub.engine.finance.service.IDiscountedTransferInternalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;


/**
 * @Author : linglong
 * @Date : Create in 2024-12-19
 * @Description :   资产转让-折价转让-内部调拨
 * @Modified :
 */
@Api(tags = "资产转让-折价转让-内部调拨")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/discounted-transfer-internal")
public class DiscountedTransferInternalController {

private final IDiscountedTransferInternalService discountedTransferInternalService;

    @ApiOperation(value = "生成支付信息")
    @PostMapping("/generatePayment")
    public R<Boolean> generatePayment(@RequestBody @Valid InternalTransferGenerateDTO queryDTO) {
        return R.ok(discountedTransferInternalService.generatePayment(queryDTO));
    }


    @ApiOperation("分页列表")
    @PostMapping("/page")
    public R<IPage<InternalTransferVO>> page(@RequestBody @Valid DiscountedTransferQueryDTO queryDTO) {
        Page<DiscountedTransferInternalEntity> paged = discountedTransferInternalService
                .lambdaQuery()
                .in(CollectionUtil.isNotEmpty(queryDTO.getBatchList()),DiscountedTransferInternalEntity::getBatch, queryDTO.getBatchList())
                .apply(Objects.nonNull(queryDTO.getPaymentDate()),"to_char(payment_date, 'yyyy-MM-dd')={0}", DateUtil.format(queryDTO.getPaymentDate(),"yyyy-MM-dd"))
                .page(new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize()));
        return R.ok(paged.convert(entity -> BeanUtil.copyProperties(entity, InternalTransferVO.class)));
    }

    @ApiOperation(value = "内部调拨导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid DiscountedTransferQueryDTO queryDTO) {
        try {
            List<DiscountedTransferInternalEntity> entities = discountedTransferInternalService.lambdaQuery()
                    .in(CollectionUtil.isNotEmpty(queryDTO.getBatchList()),DiscountedTransferInternalEntity::getBatch, queryDTO.getBatchList())
                    .apply(Objects.nonNull(queryDTO.getPaymentDate()), "to_char(payment_date, 'yyyy-MM-dd')={0}", DateUtil.format(queryDTO.getPaymentDate(), "yyyy-MM-dd"))
                    .list();

            ExcelUtil<ImpairmentProvisionExcelVO> util = new ExcelUtil<ImpairmentProvisionExcelVO>(ImpairmentProvisionExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("内部调拨.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(entities, ImpairmentProvisionExcelVO.class), "内部调拨");
        } catch (UnsupportedEncodingException e) {
            log.error("内部调拨导出失败", e);
            throw new ServiceException("内部调拨导出失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "下载支付信息模板")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<InternalTransferExcelDTO> util = new ExcelUtil<InternalTransferExcelDTO>(InternalTransferExcelDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("内部调拨导入模板.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(Lists.newArrayList(), InternalTransferExcelDTO.class), "内部调拨");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:" + e.getMessage());
        }
    }

    @ApiOperation(value = "上传")
    @PostMapping("/importFile")
    public R<Boolean> importFile(@ApiParam(value = "导入文件", required = true) @RequestParam(value = "file", required = true) final MultipartFile file) {
        return R.ok(discountedTransferInternalService.importFile(file));
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(discountedTransferInternalService.generateVoucher(ids, YesOrNoEnum.NO));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(discountedTransferInternalService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(discountedTransferInternalService.withdraw(ids));
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/delete")
    public R<Boolean> delete(@RequestBody @ApiParam(value = "id集合") List<Long> ids) {
        return R.ok(discountedTransferInternalService.delete(ids));
    }
}



