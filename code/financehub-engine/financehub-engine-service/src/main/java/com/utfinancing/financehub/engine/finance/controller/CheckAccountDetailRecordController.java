package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.enums.CheckTypeEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import com.utfinancing.financehub.engine.finance.service.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Author : jnc
 * @Date : Create in 2024-03-20
 * @Description :   CheckAccountDetailRecord控制器实现类
 * @Modified :
 */
@Api(tags = "对账功能controller")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/check-account-detail-record")
public class CheckAccountDetailRecordController {

    private final ICheckAccountDetailRecordService  checkAccountDetailRecordService;

    @Resource
    ICheckAccountDetailResultService checkAccountDetailResultService;

    @Resource
    ICheckAccountDetailResultHisService checkAccountDetailResultHisService;

    @Resource
    ICheckAccountKingdeeResultService checkAccountKingdeeResultService;

    @Resource
    ICheckAccountKingdeeResultHisService checkAccountKingdeeResultHisService;

    @Resource
    ICheckAccountMiddleResultService checkAccountMiddleResultService;

    @Resource
    ICheckAccountMiddleResultHisService checkAccountMiddleResultHisService;

    @Resource
    ICheckCommonFinanceDataResultService checkCommonFinanceDataResultService;

    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody CheckAccountDetailRecordDTO dto) {
        return R.ok(checkAccountDetailRecordService.saveCheckAccountDetailRecord(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody CheckAccountDetailRecordDTO dto) {
        return R.ok(checkAccountDetailRecordService.updateCheckAccountDetailRecord(id, dto));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Boolean> delete(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(checkAccountDetailRecordService.removeById(id));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<CheckAccountDetailRecordDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(checkAccountDetailRecordService.getCheckAccountDetailRecordDTOById(id));
    }

    @ApiOperation(value = "批次号查询")
    @PostMapping("/page")
    public R<IPage<CheckAccountDetailRecordVO>> page(@RequestBody @Valid CheckAccountDetailRecordQueryDTO queryDTO) {
        return R.ok(checkAccountDetailRecordService.selectPage(queryDTO));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "科目余额与明细余额对账界面触发")
    @PostMapping("/check/detail/{periodCode}")
    @ApiImplicitParam(paramType = "path", name = "periodCode", value = "periodCode", required = true, type = "int", dataTypeClass = Integer.class)
    public R<String> checkDetail(@PathVariable("periodCode") @Valid @NotNull Integer periodCode) {
        checkAccountDetailRecordService.checkDetail(periodCode, CheckTypeEnum.MANUAL.getCode());
        return R.ok(null, "正在对帐，请等待执行完毕");
    }

//    @ApiOperation(value = "查询账期对应的记录")
//    @PostMapping("/record/{periodCode}/{target}")
//    public R<CheckAccountDetailRecordVO> findRecord(@PathVariable("periodCode") @Valid @NotNull Integer periodCode, @PathVariable("target") @Valid @NotNull String target) {
//        return R.ok(checkAccountDetailRecordService.findRecord(periodCode, target));
//    }

    @ApiOperation(value = "分页查询科目余额与明细余额对账实时表数据-已经合并实时表和历史表分页查询")
    @PostMapping("/resultDetailPage")
    public R<IPage<CheckAccountDetailResultVO>> resultDetailPage(@RequestBody @Valid CheckAccountDetailResultQueryDTO queryDTO) {
        Integer periodCode = queryDTO.getPeriodCode();
        if(ObjUtil.isEmpty(periodCode)){
            throw new ServiceException("期间不能为空");
        }
        IPage<CheckAccountDetailResultVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        if(isCurrentMonth(periodCode)){
            page = checkAccountDetailResultService.selectDetailPage(queryDTO);
        }else{
            IPage<CheckAccountDetailResultHisVO> tmpPage = checkAccountDetailResultHisService.selectDetailHisPage(BeanUtil.copyProperties(queryDTO, CheckAccountDetailResultHisQueryDTO.class));
            List<CheckAccountDetailResultHisVO> tmpList = tmpPage.getRecords();
            List<CheckAccountDetailResultVO> list = BeanUtil.copyToList(tmpList, CheckAccountDetailResultVO.class);
            page.setRecords(list);
            page.setPages(tmpPage.getPages());
            page.setTotal(tmpPage.getTotal());
            page.setCurrent(tmpPage.getCurrent());
        }
        return R.ok(page);
    }

//    @ApiOperation(value = "分页查询科目余额与明细余额对账历史表数据")
//    @PostMapping("/resultDetailHisPage")
//    public R<IPage<CheckAccountDetailResultHisVO>> resultDetailHisPage(@RequestBody @Valid CheckAccountDetailResultHisQueryDTO queryDTO) {
//        return R.ok(checkAccountDetailResultHisService.selectDetailHisPage(queryDTO));
//    }


    @ApiOperation(value = "金蝶科目余额与中台科目余额对账 界面触发")
    @PostMapping("/check/kingdee/{periodCode}")
    @ApiImplicitParam(paramType = "path", name = "periodCode", value = "periodCode", required = true, type = "int", dataTypeClass = Integer.class)
    public R<String> checkKingdee(@PathVariable("periodCode") @Valid @NotNull Integer periodCode) {
        checkAccountDetailRecordService.checkKingdee(periodCode, CheckTypeEnum.MANUAL.getCode());
        return R.ok(null, "正在对帐，请等待执行完毕");
    }

    @ApiOperation(value = "金蝶中间表科目余额与中台科目余额对账 界面触发")
    @PostMapping("/check/middle/{periodCode}")
    @ApiImplicitParam(paramType = "path", name = "periodCode", value = "periodCode", required = true, type = "int", dataTypeClass = Integer.class)
    public R<String> checkMiddle(@PathVariable("periodCode") @Valid @NotNull Integer periodCode) {
        checkAccountDetailRecordService.checkKingdeeMiddle(periodCode, CheckTypeEnum.MANUAL.getCode());
        return R.ok(null, "正在对帐，请等待执行完毕");
    }

    @ApiOperation(value = "分页查询科目余额与金蝶科目余额对账实时表数据-已经合并实时表和历史表分页查询")
    @PostMapping("/resultKingdeePage")
    public R<IPage<CheckAccountKingdeeResultVO>> resultKingdeePage(@RequestBody @Valid CheckAccountKingdeeResultQueryDTO queryDTO) {
        Integer periodCode = queryDTO.getPeriodCode();
        if(ObjUtil.isEmpty(periodCode)){
            throw new ServiceException("期间不能为空");
        }
        IPage<CheckAccountKingdeeResultVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        if(isCurrentMonth(periodCode)){
            page = checkAccountKingdeeResultService.selectKingdeePage(queryDTO);
        }else{
            IPage<CheckAccountKingdeeResultHisVO> tmpPage = checkAccountKingdeeResultHisService.selectKingdeeHisPage(BeanUtil.copyProperties(queryDTO, CheckAccountKingdeeResultHisQueryDTO.class));
            List<CheckAccountKingdeeResultHisVO> tmpList = tmpPage.getRecords();
            List<CheckAccountKingdeeResultVO> list = BeanUtil.copyToList(tmpList, CheckAccountKingdeeResultVO.class);
            page.setRecords(list);
            page.setPages(tmpPage.getPages());
            page.setTotal(tmpPage.getTotal());
            page.setCurrent(tmpPage.getCurrent());
        }

        return R.ok(page);
    }

//    @ApiOperation(value = "分页查询科目余额与金蝶科目余额对账历史表数据")
//    @PostMapping("/resultKingdeeHisPage")
//    public R<IPage<CheckAccountKingdeeResultHisVO>> resultKingdeeHisPage(@RequestBody @Valid CheckAccountKingdeeResultHisQueryDTO queryDTO) {
//        return R.ok(checkAccountKingdeeResultHisService.selectKingdeeHisPage(queryDTO));
//    }

    @ApiOperation(value = "分页查询科目发生额与金蝶中间表发生额实时表数据-已经合并实时表和历史表分页查询")
    @PostMapping("/resultMiddlePage")
    public R<IPage<CheckAccountMiddleResultVO>> resultMiddlePage(@RequestBody @Valid CheckAccountMiddleResultQueryDTO queryDTO) {
        Integer periodCode = queryDTO.getPeriodCode();
        if(ObjUtil.isEmpty(periodCode)){
            throw new ServiceException("期间不能为空");
        }
        IPage<CheckAccountMiddleResultVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        if(isCurrentMonth(periodCode)){
            page = checkAccountMiddleResultService.selectMiddlePage(queryDTO);
        }else{
            IPage<CheckAccountMiddleResultHisVO> tmpPage = checkAccountMiddleResultHisService.selectMiddleHisPage(BeanUtil.copyProperties(queryDTO, CheckAccountMiddleResultHisQueryDTO.class));
            List<CheckAccountMiddleResultHisVO> tmpList = tmpPage.getRecords();
            List<CheckAccountMiddleResultVO> list = BeanUtil.copyToList(tmpList, CheckAccountMiddleResultVO.class);
            page.setRecords(list);
            page.setPages(tmpPage.getPages());
            page.setTotal(tmpPage.getTotal());
            page.setCurrent(tmpPage.getCurrent());
        }

//        checkAccountMiddleResultService.selectMiddlePage(queryDTO)
        return R.ok(page);
    }

//    @ApiOperation(value = "分页查询科目发生额与金蝶中间表发生额历史表数据")
//    @PostMapping("/resultMiddleHisPage")
//    public R<IPage<CheckAccountMiddleResultHisVO>> resultMiddleHisPage(@RequestBody @Valid CheckAccountMiddleResultHisQueryDTO queryDTO) {
//        return R.ok(checkAccountMiddleResultHisService.selectMiddleHisPage(queryDTO));
//    }

    @ApiOperation(value = "其他系统与中台数据统一 界面触发")
    @PostMapping("/check/common")
    public R<String> checkCommon(@RequestBody @Valid CheckCommonQueryDTO queryDTO) {
        checkAccountDetailRecordService.checkCommon(queryDTO.getPeriodCode(), queryDTO.getBusinessType(), CheckTypeEnum.MANUAL.getCode());
        return R.ok(null, "正在对帐，请等待执行完毕");
    }

    @ApiOperation(value = "分页查询业务系统与中台结果表对账数据")
    @PostMapping("/resultCommonPage")
    public R<IPage<Map<String, Object>>> resultCommonPage(@RequestBody @Valid CheckCommonFinanceDataResultQueryDTO queryDTO) {
        return R.ok(checkCommonFinanceDataResultService.selectCommonPage(queryDTO));
    }

    private static boolean isCurrentMonth(Integer periodCode) {
        boolean currentMonth = Integer.valueOf(DateUtil.format(DateUtil.toLocalDateTime(new Date()), "yyyyMM")).compareTo(periodCode) ==0;
        return currentMonth;
    }

    private static <T> IPage<T> getIPage(int pageNum, int pageSize, List<T> list, long totalSize) {
        IPage<T> page = new Page<>(pageNum, pageSize);
        page.setRecords(list);
        page.setTotal(totalSize);
        return page;
    }

    @ApiOperation(value = "判断账期是否已存在对账数据")
    @PostMapping("/check/existed")
    public R<Boolean> checkPeriodExist(@RequestBody @Valid CheckCommonQueryDTO queryDTO) {
        return R.ok(checkAccountDetailRecordService.checkPeriodExist(queryDTO));
    }
}

