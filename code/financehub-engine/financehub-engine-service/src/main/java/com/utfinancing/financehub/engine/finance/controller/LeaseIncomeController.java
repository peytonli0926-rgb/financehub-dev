package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeDetailsVO;
import com.utfinancing.financehub.engine.finance.model.vo.LeaseIncomeVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.service.ILeaseIncomeDetailsService;
import com.utfinancing.financehub.engine.finance.service.ILeaseIncomeService;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;


/**
 * @Author : hzhao
 * @Date : Create in 2023-11-10
 * @Description :   LeaseIncome控制器实现类
 * @Modified :
 */
@Api(tags = "租赁收益")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/lease-income")
public class LeaseIncomeController {

    private final ILeaseIncomeService leaseIncomeService;

    private final ILeaseIncomeDetailsService leaseIncomeDetailsService;

    private final IRepaymentPlanService repaymentPlanService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody LeaseIncomeDTO dto) {
        return R.ok(leaseIncomeService.saveLeaseIncome(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody LeaseIncomeDTO dto) {
        return R.ok(leaseIncomeService.updateLeaseIncome(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        leaseIncomeService.deleteByIds(Collections.singletonList(id));
        return R.ok(Boolean.TRUE);
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<LeaseIncomeDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(leaseIncomeService.getLeaseIncomeDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<LeaseIncomeVO>> page(@RequestBody @Valid LeaseIncomeQueryDTO queryDTO) {
        return R.ok(leaseIncomeService.selectPage(queryDTO));
    }

    @ApiOperation(value = "汇总分页查询-导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid LeaseIncomeQueryDTO queryDTO) {
        try {
            if (CollectionUtils.isEmpty(queryDTO.getIdList())) {
                return;
            }
            LeaseIncomeDetailsQueryDTO leaseIncomeDetailsQueryDTO = new LeaseIncomeDetailsQueryDTO();
            leaseIncomeDetailsQueryDTO.setLeaseIncomeIdList(queryDTO.getIdList());
            leaseIncomeDetailsQueryDTO.setPageNum(1);
            leaseIncomeDetailsQueryDTO.setPageSize(Integer.MAX_VALUE);
            leaseIncomeDetailsQueryDTO.setBusinessDate(DateUtils.format(queryDTO.getBusinessDate(), "yyyy-MM"));
            leaseIncomeDetailsQueryDTO.setSystemCodeList(queryDTO.getSystemCodeList());
            leaseIncomeDetailsQueryDTO.setProcessStatusList(queryDTO.getProcessStatusList());
            List<LeaseIncomeDetailsVO> list = leaseIncomeService.selectDetailList(leaseIncomeDetailsQueryDTO);
            ExcelUtil<LeaseIncomeDetailsExcel> util = new ExcelUtil<LeaseIncomeDetailsExcel>(LeaseIncomeDetailsExcel.class);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("收益计提.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, LeaseIncomeDetailsExcel.class), "收益计提");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "生成本月收益计提")
    @PostMapping("/generate")
    public R generate(@RequestBody @Valid LeaseIncomeQueryDTO queryDTO) {
        try {
            // D字母开始的合同，直接变更回笼状态
            repaymentPlanService.updateRepaymentPlanForSpecialContract(queryDTO);
            // 生成计提
            Integer planDatePeriod = Integer.parseInt(DateUtils.format(queryDTO.getBusinessDate(), "yyyyMM"));
            queryDTO.setPlanDatePeriod(planDatePeriod);
            leaseIncomeService.generateAsync(queryDTO);

            // 校验计提结果
            leaseIncomeDetailsService.leaseIncomeVadation();
            return R.ok(null, "计提任务已开启！");
        } catch (Exception e) {
            log.error("生成本月收益计提报错", e);
            return R.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "生成本月收益计提汇总")
    @PostMapping("/generateLeaseIncome")
    public R generateLeaseIncome(@RequestBody @Valid LeaseIncomeQueryDTO queryDTO) {
        try {
            leaseIncomeService.generateLeaseIncome(queryDTO);
            return R.ok();
        } catch (Exception e) {
            log.error("生成本月收益计提汇总报错", e);
            return R.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "模板下载")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<LeaseIncomeImport> util = new ExcelUtil<LeaseIncomeImport>(LeaseIncomeImport.class);
        util.importTemplateExcel(response, "sheet1");
    }

    @ApiOperation(value = "上传")
    @PostMapping("/importData")
    public R<String> importData(MultipartFile file) throws Exception {
        ExcelUtil<LeaseIncomeImport> util = new ExcelUtil<LeaseIncomeImport>(LeaseIncomeImport.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<LeaseIncomeImport> list = util.importExcel(inputStream);
            return leaseIncomeService.importData(list);
        } catch (Exception e) {
            log.error("收益计提上传报错", e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @ApiOperation(value = "单月分页查询")
    @PostMapping("/detail/page")
    public R<IPage<LeaseIncomeDetailsVO>> selectDetailPage(@RequestBody @Valid LeaseIncomeDetailsQueryDTO queryDTO) {
        return R.ok(leaseIncomeService.selectDetailPage(queryDTO));
    }

    @ApiOperation(value = "单月收益计提明细-导出")
    @PostMapping("/detail/export")
    public void detailExport(HttpServletResponse response, @RequestBody @Valid LeaseIncomeDetailsQueryDTO queryDTO) {
        try {
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(Integer.MAX_VALUE);
            IPage<LeaseIncomeDetailsVO> list = leaseIncomeService.selectDetailPage(queryDTO);
            ExcelUtil<LeaseIncomeDetailsExcel> util = new ExcelUtil<LeaseIncomeDetailsExcel>(LeaseIncomeDetailsExcel.class);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("单月收益计提明细.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list.getRecords(), LeaseIncomeDetailsExcel.class), "单月收益计提明细");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "单月详情查询")
    @PostMapping("/detail/plan")
    public R<List<RepaymentPlanVO>> selectDetailPlanList(@RequestBody @Valid LeaseIncomeDetailsQueryDTO queryDTO) {
        return R.ok(leaseIncomeService.selectDetailPlanList(queryDTO));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public R delete(@RequestBody List<Long> ids) {
        leaseIncomeService.deleteByIds(ids);
        return R.ok();
    }

    @ApiOperation(value = "提交")
    @PostMapping("/submit")
    public R submit(@RequestBody List<Long> ids) {
        leaseIncomeService.submit(ids);
        return R.ok();
    }

    @ApiOperation(value = "撤回")
    @PostMapping("/withdraw")
    public R withdraw(@RequestBody List<Long> ids) {
        leaseIncomeService.withdraw(ids);
        return R.ok();
    }

    @ApiOperation(value = "复核通过")
    @PostMapping("/check/pass")
    public R pass(@RequestBody Long id) {
        CommonApproveDTO dto = new CommonApproveDTO();
        dto.setDocumentId(id);
        leaseIncomeService.pass(dto);
        return R.ok();
    }

    @ApiOperation(value = "复核失败")
    @PostMapping("/check/fail")
    public R fail(@RequestBody Long id) {
        CommonApproveDTO dto = new CommonApproveDTO();
        dto.setDocumentId(id);
        leaseIncomeService.fail(dto);
        return R.ok();
    }

    @ApiOperation(value = "生成凭证")
    @PostMapping("/voucher")
    public R voucher(@RequestBody List<Long> ids) {
        leaseIncomeService.multiThreadGenVoucher(ids, YesOrNoEnum.NO.getCode());
        return R.ok();
    }

}



