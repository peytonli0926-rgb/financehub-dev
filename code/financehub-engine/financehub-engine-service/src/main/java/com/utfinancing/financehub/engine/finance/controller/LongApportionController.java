package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.model.dto.LongApportionQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.LongApportionExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.LongApportionVO;
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
import com.utfinancing.financehub.engine.finance.service.ILongApportionService;


/**
 * @Author : wenbin
 * @Date : Create in 2024-04-12
 * @Description :   LongApportion控制器实现类
 * @Modified :
 */
@Api(tags = "长期应收款-分摊表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/long-apportion")
public class LongApportionController {

    private final ILongApportionService  longApportionService;

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<LongApportionVO>> page(@RequestBody @Valid LongApportionQueryDTO queryDTO) {
        return R.ok(longApportionService.selectPage(queryDTO));
    }

    @ApiOperation(value = "分摊表导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid LongApportionQueryDTO queryDTO) {
        try {
            List<LongApportionVO> list = longApportionService.selectList(queryDTO);
            ExcelUtil<LongApportionExcelVO> util = new ExcelUtil<LongApportionExcelVO>(LongApportionExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("长期应收款登记-分摊表.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, LongApportionExcelVO.class), "长期应收款登记-分摊表");
        } catch (UnsupportedEncodingException e) {
            log.error("长期应收款登记-分摊表导出失败", e);
            throw new ServiceException("长期应收款登记-分摊表导出失败，失败原因:" + e.getMessage());
        }
    }

}



