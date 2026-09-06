package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsExcel;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeImport;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsVO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeePlanVO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeVO;
import com.utfinancing.financehub.engine.finance.service.IServiceFeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;


/**
 * @Author : hzhao
 * @Date : Create in 2023-11-07
 * @Description :   ServiceFee控制器实现类
 * @Modified :
 */
@Api(tags = "服务费分摊表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/service-fee")
public class ServiceFeeController {

    private final IServiceFeeService serviceFeeService;

    @ApiOperation(value = "汇总分页查询")
    @PostMapping("/page")
    public R<IPage<ServiceFeeVO>> page(@RequestBody @Valid ServiceFeeQueryDTO queryDTO) {
        return R.ok(serviceFeeService.selectPage(queryDTO));
    }

    @ApiOperation(value = "汇总分页查询-导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid ServiceFeeQueryDTO queryDTO) {
        try {
            if (CollectionUtils.isEmpty(queryDTO.getIdList())) {
                return;
            }
            if (queryDTO.getBusinessStartDate()==null){
                queryDTO.setBusinessStartDate(queryDTO.getBusinessDate());
                queryDTO.setBusinessEndDate(queryDTO.getBusinessDate());
            }
            ServiceFeeDetailsQueryDTO serviceFeeDetailsQueryDTO = new ServiceFeeDetailsQueryDTO();
            serviceFeeDetailsQueryDTO.setServiceFeeIdList(queryDTO.getIdList());
            serviceFeeDetailsQueryDTO.setBusinessDate(queryDTO.getBusinessDate());
            serviceFeeDetailsQueryDTO.setBusinessStartDate(queryDTO.getBusinessStartDate());
            serviceFeeDetailsQueryDTO.setBusinessEndDate(DateUtil.endOfDay(DateUtil.endOfMonth(queryDTO.getBusinessEndDate())));
            List<ServiceFeeDetailsVO> list = serviceFeeService.selectExportDetailList(serviceFeeDetailsQueryDTO);
            ExcelUtil<ServiceFeeDetailsExcel> util = new ExcelUtil<>(ServiceFeeDetailsExcel.class);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("服务费分摊.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, ServiceFeeDetailsExcel.class), "服务费分摊");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "测算")
    @PostMapping("/measurement")
    public R measurement(@RequestBody @Valid ServiceFeeQueryDTO queryDTO) {
        try {

            return serviceFeeService.measurement(queryDTO);
        } catch (Exception e) {
            log.error("ServiceFeeController measurement error", e);
            return R.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "模板下载")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<ServiceFeeImport> util = new ExcelUtil<ServiceFeeImport>(ServiceFeeImport.class);
        util.importTemplateExcel(response, "sheet1");
    }

    @ApiOperation(value = "上传")
    @PostMapping("/importData")
    public R importData(MultipartFile file) throws Exception {
        ExcelUtil<ServiceFeeImport> util = new ExcelUtil<ServiceFeeImport>(ServiceFeeImport.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<ServiceFeeImport> list = util.importExcel(inputStream);
            return serviceFeeService.importData(list);
        } catch (Exception e) {
            log.error("服务费分摊上传报错", e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @ApiOperation(value = "单月分页查询")
    @PostMapping("/detail/page")
    public R<IPage<ServiceFeeDetailsVO>> selectDetailPage(@RequestBody @Valid ServiceFeeDetailsQueryDTO queryDTO) {
        return R.ok(serviceFeeService.selectDetailPage(queryDTO));
    }


    @ApiOperation(value = "单月详情查询")
    @PostMapping("/detail/plan")
    public R<List<ServiceFeePlanVO>> selectDetailPlanList(@RequestBody @Valid ServiceFeeDetailsQueryDTO queryDTO) {
        return R.ok(serviceFeeService.selectDetailPlanList(queryDTO));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public R delete(@RequestBody List<Long> ids) {
        serviceFeeService.deleteByIds(ids);
        return R.ok();
    }

    @ApiOperation(value = "提交")
    @PostMapping("/submit")
    public R submit(@RequestBody List<Long> ids) {
        // 1.先删除凭证
        serviceFeeService.batchDeleteVoucher(ids);
        serviceFeeService.submit(ids);
        return R.ok();
    }

    @ApiOperation(value = "撤回")
    @PostMapping("/withdraw")
    public R withdraw(@RequestBody List<Long> ids) {
        serviceFeeService.withdraw(ids);
        return R.ok();
    }

    @ApiOperation(value = "生成凭证")
    @PostMapping("/voucher")
    public R voucher(@RequestBody List<Long> ids) {
        serviceFeeService.voucher(ids, YesOrNoEnum.NO.getCode());
        return R.ok();
    }

    @ApiOperation(value = "冲销凭证")
    @PostMapping("/reversal/voucher")
    public R reversalVoucher(@RequestBody List<Long> ids) {
        serviceFeeService.reversalVoucher(ids);
        return R.ok();
    }

}



