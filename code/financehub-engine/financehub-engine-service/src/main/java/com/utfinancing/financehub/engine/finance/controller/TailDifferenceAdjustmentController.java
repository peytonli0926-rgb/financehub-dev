package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentDTO;
import com.utfinancing.financehub.engine.finance.model.vo.TailDifferenceAdjustmentDetailExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.TailDifferenceAdjustmentDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.TailDifferenceAdjustmentVO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationDetailsExcelDTO;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationDetailsQueryDTO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationDetailsVO;
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
import com.utfinancing.financehub.engine.finance.service.ITailDifferenceAdjustmentService;


/**
 * @Author : bruyang
 * @Date : Create in 2024-03-05
 * @Description :   TailDifferenceAdjustment控制器实现类
 * @Modified :
 */
@Api(tags = "尾差调整")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/tail-difference-adjustment")
public class TailDifferenceAdjustmentController {

    private final ITailDifferenceAdjustmentService  tailDifferenceAdjustmentService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody TailDifferenceAdjustmentDTO dto) {
        return R.ok(tailDifferenceAdjustmentService.saveTailDifferenceAdjustment(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody TailDifferenceAdjustmentDTO dto) {
        return R.ok(tailDifferenceAdjustmentService.updateTailDifferenceAdjustment(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(tailDifferenceAdjustmentService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<TailDifferenceAdjustmentDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(tailDifferenceAdjustmentService.getTailDifferenceAdjustmentDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<TailDifferenceAdjustmentVO>> page(@RequestBody @Valid TailDifferenceAdjustmentQueryDTO queryDTO) {
        return R.ok(tailDifferenceAdjustmentService.selectPage(queryDTO));
    }

    @ApiOperation(value = "生成尾差调整信息")
    @PostMapping("/initData")
    public R<Boolean> initData(@RequestBody @Valid TailDifferenceAdjustmentQueryDTO queryDTO) {
        return R.ok(tailDifferenceAdjustmentService.initData(queryDTO));
    }

    @ApiOperation(value = "批量删除接口")
    @PostMapping("/deleteByIds")
    public R<Boolean> deleteByIds(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(tailDifferenceAdjustmentService.deleteByIds(ids));
    }

    @ApiOperation(value = "批量提交")
    @PostMapping("/submit")
    public R<Boolean> submit(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(tailDifferenceAdjustmentService.submit(ids));
    }

    @ApiOperation(value = "批量撤回")
    @PostMapping("/withdraw")
    public R<Boolean> withdraw(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(tailDifferenceAdjustmentService.withdraw(ids));
    }

    @ApiOperation(value = "批量生成凭证")
    @PostMapping("/generateVoucher")
    public R<Boolean> generateVoucher(@RequestBody @ApiParam(value = "id集合") List<Long> ids){
        return R.ok(tailDifferenceAdjustmentService.insertVoucher(ids, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "更新核销状态")
    @PostMapping("/updateProcessStatus")
    public R<Boolean> updateProcessStatus(@RequestBody CommonApproveDTO approveDTO) {
        return R.ok(tailDifferenceAdjustmentService.updateProcessStatus(approveDTO));
    }

    @ApiOperation(value = "导出尾差详情")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid List<Long> idList) {
        try {
            List<TailDifferenceAdjustmentDetailVO> list = tailDifferenceAdjustmentService.listByConditionByIdList(idList);
            ExcelUtil<TailDifferenceAdjustmentDetailExcelVO> util = new ExcelUtil<TailDifferenceAdjustmentDetailExcelVO>(TailDifferenceAdjustmentDetailExcelVO.class);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment; filename=" + URLEncoder.encode("尾差调整详情导出.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, TailDifferenceAdjustmentDetailExcelVO.class), "尾差调整详情");
        } catch (UnsupportedEncodingException e) {
            log.error("exportDetailInfos error", e);
            throw new ServiceException("导出尾差调整失败，失败原因:"+e.getMessage());
        }
    }

}



