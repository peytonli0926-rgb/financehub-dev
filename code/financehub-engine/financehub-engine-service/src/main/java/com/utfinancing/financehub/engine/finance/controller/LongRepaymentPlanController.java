package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.LongReceivableRegisterQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LongRepaymentPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.LongRepaymentPlanDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LongReceivableRegisterExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.LongReceivableRegisterVO;
import com.utfinancing.financehub.engine.finance.model.vo.LongRepaymentPlanExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.LongRepaymentPlanVO;
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
import com.utfinancing.financehub.engine.finance.service.ILongRepaymentPlanService;


/**
 * @Author : wenbin
 * @Date : Create in 2024-04-12
 * @Description :   LongRepaymentPlan控制器实现类
 * @Modified :
 */
@Api(tags = "长期应收款-偿还计划")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/long-repayment-plan")
public class LongRepaymentPlanController {

    private final ILongRepaymentPlanService  longRepaymentPlanService;

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<LongRepaymentPlanVO>> page(@RequestBody @Valid LongRepaymentPlanQueryDTO queryDTO) {
        return R.ok(longRepaymentPlanService.selectPage(queryDTO));
    }

    @ApiOperation(value = "偿还计划导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid LongRepaymentPlanQueryDTO queryDTO) {
        try {
            List<LongRepaymentPlanVO> list = longRepaymentPlanService.selectList(queryDTO);
            ExcelUtil<LongRepaymentPlanExcelVO> util = new ExcelUtil<LongRepaymentPlanExcelVO>(LongRepaymentPlanExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("长期应收款登记-偿还计划.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, LongRepaymentPlanExcelVO.class), "长期应收款登记-偿还计划");
        } catch (UnsupportedEncodingException e) {
            log.error("长期应收款登记-偿还计划导出失败", e);
            throw new ServiceException("长期应收款登记-偿还计划导出失败，失败原因:" + e.getMessage());
        }
    }

}



