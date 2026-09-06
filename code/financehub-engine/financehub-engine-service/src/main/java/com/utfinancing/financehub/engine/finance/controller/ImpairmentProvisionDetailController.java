package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import com.utfinancing.financehub.engine.finance.service.IImpairmentProvisionDetailService;


/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description :   ImpairmentProvisionDetail控制器实现类
 * @Modified :
 */
@Api(tags = "减值计提明细")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/impairment-provision-detail")
public class ImpairmentProvisionDetailController {

    private final IImpairmentProvisionDetailService  impairmentProvisionDetailService;

    /**
     * @description:减值计提-查询详情-分页查询
     **/
    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ImpairmentProvisionDetailVO>> page(@RequestBody @Valid ImpairmentProvisionDetailQueryDTO queryDTO) {
        return R.ok(impairmentProvisionDetailService.selectPage(queryDTO));
    }

    /**
     * @description:减值计提-查询详情-查询汇总数据
     **/
    @ApiOperation(value = "查询汇总数据")
    @PostMapping("/summary")
    public R<ImpairmentProvisionDetailSummaryVO> summary(@RequestBody @Valid ImpairmentProvisionDetailQueryDTO queryDTO) {
        return R.ok(impairmentProvisionDetailService.summary(queryDTO));
    }

    /**
     * @description:减值计提-查询详情-导出
     **/
    @ApiOperation(value = "减值计提明细导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid ImpairmentProvisionDetailQueryDTO queryDTO) {
        try {
            impairmentProvisionDetailService.exportProvisionDetailExcel(response,queryDTO);
        } catch (Exception e) {
            log.error("减值计提-明细提导出失败", e);
            throw new ServiceException("减值计提-明细导出失败，失败原因:" + e.getMessage());
        }
    }


}



