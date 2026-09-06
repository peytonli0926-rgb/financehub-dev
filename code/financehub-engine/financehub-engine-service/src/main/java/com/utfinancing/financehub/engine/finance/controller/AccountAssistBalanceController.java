package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.AccountAssistBalanceVO;
import com.utfinancing.financehub.engine.finance.model.vo.AccountAssistCurrentBalanceSheetVO;
import com.utfinancing.financehub.engine.finance.model.vo.AccountBalanceSheetVO;
import com.utfinancing.financehub.engine.finance.model.vo.AccountCurrentBalanceSheetVO;
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
import java.util.Map;

import com.utfinancing.financehub.engine.finance.service.IAccountAssistBalanceService;


/**
 * @Author : lixin
 * @Date : Create in 2024-01-05
 * @Description :   AccountAssistBalance控制器实现类
 * @Modified :
 */
@Api(tags = "科目辅助帐余额表")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/account-assist-balance")
public class AccountAssistBalanceController {

    private final IAccountAssistBalanceService  accountAssistBalanceService;

    @ApiOperation(value = "核算项目余额表-分页查询")
    @PostMapping("/assistBalancePage")
    public R<IPage<AccountAssistBalanceVO>> assistBalancePage(@RequestBody @Valid AccountAssistBalanceQueryDTO queryDTO) {
        return R.ok(accountAssistBalanceService.assistBalancePage(queryDTO));
    }

    @ApiOperation(value = "科目余额表-分页查询")
    @PostMapping("/accountBalancePage")
    public R<IPage<AccountBalanceSheetVO>> accountBalancePage(@RequestBody @Valid AccountBalanceSheetQueryDTO queryDTO) {
        return R.ok(accountAssistBalanceService.accountBalancePage(queryDTO));
    }

    @ApiOperation(value = "导出科目余额数据")
    @PostMapping("/exportAccountBalance")
    public void export(HttpServletResponse response, @RequestBody @Valid AccountBalanceSheetQueryDTO queryDTO) {
        try {
            List<AccountBalanceSheetExcelExportDTO> list = accountAssistBalanceService.listByCondition(queryDTO);
            ExcelUtil<AccountBalanceSheetExcelExportDTO> util = new ExcelUtil<AccountBalanceSheetExcelExportDTO>(AccountBalanceSheetExcelExportDTO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("科目余额表.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, AccountBalanceSheetExcelExportDTO.class), "科目余额");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:"+e.getMessage());
        }
    }

    @ApiOperation(value = "核算项目余额表/科目余额表-本期发生额/本期发生额合同明细-分页查询")
    @PostMapping("/assist/currentBalancePage")
    public R<IPage<AccountAssistCurrentBalanceSheetVO>> currentBalancePage(@RequestBody @Valid AccountAssistCurrentBalanceSheetQueryDTO queryDTO) {
        return R.ok(accountAssistBalanceService.currentBalancePage(queryDTO));
    }

    @ApiOperation(value = "导出核算项目余额表/科目余额表-本期发生额/本期发生额合同明细")
    @PostMapping("/exportCurrentBalance")
    public void exportCurrentBalance(HttpServletResponse response, @RequestBody @Valid AccountAssistCurrentBalanceSheetQueryDTO queryDTO) {
        try {
            List<AccountAssistCurrentBalanceSheetVO> list = accountAssistBalanceService.getCurrentBalanceList(queryDTO);
            String targetType = queryDTO.getTargetType();
            if(StringUtils.equals("account", targetType)){
                ExcelUtil<AccountCurrentBalanceSheetVO> util = new ExcelUtil<AccountCurrentBalanceSheetVO>(AccountCurrentBalanceSheetVO.class);
                response.setContentType("application/octet-stream; charset=utf-8");
                response.setHeader(
                        "Content-Disposition", "attachment; filename=" + URLEncoder.encode("本期发生额数据.xlsx", "utf8"));
                util.exportExcel(response, BeanUtil.copyToList(list, AccountCurrentBalanceSheetVO.class), "本期发生额");
            }else{
                String sheetName = StringUtils.equals("assist", targetType)?"本期发生额":"本期发生额合同明细";
                ExcelUtil<AccountAssistCurrentBalanceSheetVO> util = new ExcelUtil<AccountAssistCurrentBalanceSheetVO>(AccountAssistCurrentBalanceSheetVO.class);
                response.setContentType("application/octet-stream; charset=utf-8");
                response.setHeader(
                        "Content-Disposition", "attachment; filename=" + URLEncoder.encode(sheetName+"数据.xlsx", "utf8"));
                util.exportExcel(response, BeanUtil.copyToList(list, AccountAssistCurrentBalanceSheetVO.class), sheetName);
            }

        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:"+e.getMessage());
        }
    }

    @ApiOperation(value = "导出核算项目余额表")
    @PostMapping("/exportAssistBalance")
    public void exportAssistBalance(HttpServletResponse response, @RequestBody @Valid AccountAssistBalanceQueryDTO queryDTO) {
        try {
            List<AccountAssistBalanceVO> list = accountAssistBalanceService.getAssistBalanceData(queryDTO);
            ExcelUtil<AccountAssistBalanceVO> util = new ExcelUtil<AccountAssistBalanceVO>(AccountAssistBalanceVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("核算项目余额表.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, AccountAssistBalanceVO.class), "核算项目余额表");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("下载失败，失败原因:"+e.getMessage());
        }
    }

    @ApiOperation(value = "生成核算项目余额表excel")
    @PostMapping("/assistBalance/excel")
    public R<Map<String, String>> generateAssistBalanceExcel(@RequestBody @Valid AccountAssistBalanceQueryDTO queryDTO) {
        return R.ok(accountAssistBalanceService.generateAssistBalanceExcel(queryDTO));
    }

    @ApiOperation(value = "核算项目余额表-上传文件查询")
    @PostMapping("/assistBalance/fileList")
    public R<IPage<FileRecordEntity>> selectAssistBalanceFileList(@RequestBody @Valid FileRecordQueryDTO queryDTO) {
        return R.ok(accountAssistBalanceService.selectAssistBalanceFileList(queryDTO));
    }
}



