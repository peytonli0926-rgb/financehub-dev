package com.utfinancing.financehub.engine.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelManySheetUtil;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractRepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractStructureVO;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractVO;
import com.utfinancing.financehub.engine.finance.service.IOfflineContractRepaymentPlanService;
import com.utfinancing.financehub.engine.finance.service.IOfflineContractService;
import com.utfinancing.financehub.engine.finance.service.IOfflineContractStructureService;
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
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description :   OfflineContract控制器实现类
 * @Modified :
 */
@Api(tags = "线下合同")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/offline-contract")
public class OfflineContractController {

    private final IOfflineContractService offlineContractService;
    private final IOfflineContractRepaymentPlanService offlineContractRepaymentPlanService;
    private final IOfflineContractStructureService offlineContractStructureService;

//    @PostMapping("/save")
//    @ApiOperation(value = "新增")
//    public R<Long> save(@Valid @RequestBody OfflineContractDTO dto) {
//        return R.ok(offlineContractService.saveOfflineContract(dto));
//    }
//
//    @PostMapping("/update/{id}")
//    @ApiOperation(value = "修改")
//    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
//    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody OfflineContractDTO dto) {
//        return R.ok(offlineContractService.updateOfflineContract(id, dto));
//    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public R delete(@RequestBody List<Long> ids) {
        offlineContractService.deleteByIds(ids);
        return R.ok();
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<OfflineContractDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(offlineContractService.getOfflineContractDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<OfflineContractVO>> page(@RequestBody @Valid OfflineContractQueryDTO queryDTO) {
        return R.ok(offlineContractService.selectPage(queryDTO));
    }

    @ApiOperation(value = "导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid OfflineContractQueryDTO queryDTO) {
        try {
            List<OfflineContractVO> contractList = offlineContractService.selectList(queryDTO);
            if (CollectionUtils.isEmpty(contractList)) {
                return;
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("线下合同导出.xlsx", "utf8"));
            List<OfflineContractExcel> contractStatusRecordExcels = BeanUtil.copyToList(contractList, OfflineContractExcel.class);
            List<String> contractCodeList = new ArrayList<>();

            List<OfflineContractStructureDTO> contractStructureDTOS = new ArrayList<>();
            contractList.forEach(e -> {
                OfflineContractStructureDTO dto = offlineContractStructureService.getOfflineContractStructureDTOByContractCode(e.getContractCode(), e.getContractCodeM());
                if (null != dto) {
                    contractStructureDTOS.add(dto);
                }
                if (StringUtils.isBlank(e.getContractCodeM())) {
                    contractCodeList.add(e.getContractCode());
                }
            });
            List<OfflineContractStructureExcel> offlineContractStructureExcels = BeanUtil.copyToList(contractStructureDTOS, OfflineContractStructureExcel.class);

            OfflineContractRepaymentPlanQueryDTO planQueryDTO = new OfflineContractRepaymentPlanQueryDTO();
            planQueryDTO.setContractCodeList(contractCodeList);
            List<OfflineContractRepaymentPlanVO> offlineContractRepaymentPlanVOS = offlineContractRepaymentPlanService.selectList(planQueryDTO);

            List<OfflineContractRepaymentPlanExcel> offlineContractRepaymentPlanExcels = BeanUtil.copyToList(offlineContractRepaymentPlanVOS, OfflineContractRepaymentPlanExcel.class);

            Map<String, List<?>> map = new HashMap<>();
            String sheet1 = "1";
            String sheet2 = "2";
            String sheet3 = "3";
            map.put(sheet1, contractStatusRecordExcels);
            map.put(sheet2, offlineContractStructureExcels);
            map.put(sheet3, offlineContractRepaymentPlanExcels);
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, OfflineContractExcel.class);
            mapClass.put(sheet2, OfflineContractStructureExcel.class);
            mapClass.put(sheet3, OfflineContractRepaymentPlanExcel.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "基本信息");
            sheetName.put(sheet2, "交易结构");
            sheetName.put(sheet3, "租金计划");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "详情-交易结构")
    @PostMapping("/detail/structure")
    public R<OfflineContractStructureDTO> detailStructure(@RequestBody OfflineContractStructureQueryDTO queryDTO) {
        OfflineContractDTO offlineContractDTOById = offlineContractService.getOfflineContractDTOById(queryDTO.getId());
        OfflineContractStructureDTO offlineContractStructureVO = new OfflineContractStructureDTO();
        if (null != offlineContractDTOById) {
            offlineContractStructureVO = offlineContractStructureService.getOfflineContractStructureDTOByContractCode(offlineContractDTOById.getContractCode(), offlineContractDTOById.getContractCodeM());
        }
        return R.ok(offlineContractStructureVO);
    }

    @ApiOperation(value = "详情-交易结构-导出")
    @PostMapping("/detail/structure/export")
    public void detailStructureExport(HttpServletResponse response, @RequestBody OfflineContractStructureQueryDTO queryDTO) {
        OfflineContractDTO offlineContractDTOById = offlineContractService.getOfflineContractDTOById(queryDTO.getId());
        OfflineContractStructureDTO offlineContractStructureVO = new OfflineContractStructureDTO();
        if (null != offlineContractDTOById) {
            offlineContractStructureVO = offlineContractStructureService.getOfflineContractStructureDTOByContractCode(offlineContractDTOById.getContractCode(), offlineContractDTOById.getContractCodeM());
        }
        List<OfflineContractStructureDTO> list = new ArrayList<>();
        list.add(offlineContractStructureVO);
        ExcelUtil<OfflineContractStructureExcel> util = new ExcelUtil<OfflineContractStructureExcel>(OfflineContractStructureExcel.class);
        util.exportExcel(response, BeanUtil.copyToList(list, OfflineContractStructureExcel.class), "交易结构数据");
    }

    @ApiOperation(value = "详情-租金计划")
    @PostMapping("/detail/plan")
    public R<List<OfflineContractRepaymentPlanVO>> detailPlan(@RequestBody OfflineContractRepaymentPlanQueryDTO queryDTO) {
        OfflineContractDTO offlineContractDTOById = offlineContractService.getOfflineContractDTOById(queryDTO.getId());
        List<OfflineContractRepaymentPlanVO> contractRepaymentPlanVOS = new ArrayList<>();
        if (null != offlineContractDTOById && StringUtils.isBlank(offlineContractDTOById.getContractCodeM())) {
            queryDTO.setContractCode(offlineContractDTOById.getContractCode());
            contractRepaymentPlanVOS = offlineContractRepaymentPlanService.selectList(queryDTO);
        }
        return R.ok(contractRepaymentPlanVOS);
    }

    @ApiOperation(value = "详情-租金计划-导出")
    @PostMapping("/detail/detail/export")
    public void detailPlanExport(HttpServletResponse response, @RequestBody OfflineContractRepaymentPlanQueryDTO queryDTO) {
        OfflineContractDTO offlineContractDTOById = offlineContractService.getOfflineContractDTOById(queryDTO.getId());
        List<OfflineContractRepaymentPlanVO> list = new ArrayList<>();
        if (null != offlineContractDTOById && StringUtils.isBlank(offlineContractDTOById.getContractCodeM())) {
            queryDTO.setContractCode(offlineContractDTOById.getContractCode());
            list = offlineContractRepaymentPlanService.selectList(queryDTO);
        }
        ExcelUtil<OfflineContractRepaymentPlanExcel> util = new ExcelUtil<OfflineContractRepaymentPlanExcel>(OfflineContractRepaymentPlanExcel.class);
        util.exportExcel(response, BeanUtil.copyToList(list, OfflineContractRepaymentPlanExcel.class), "租金计划数据");
    }

    @ApiOperation(value = "模板下载")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("线下合同导入模板.xlsx", "utf8"));

            Map<String, List<?>> map = new HashMap<>();
            String sheet1 = "1";
            String sheet2 = "2";
            String sheet3 = "3";
            map.put(sheet1, new ArrayList<>());
            map.put(sheet2, new ArrayList<>());
            map.put(sheet3, new ArrayList<>());
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.IMPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, OfflineContractExcel.class);
            mapClass.put(sheet2, OfflineContractStructureExcel.class);
            mapClass.put(sheet3, OfflineContractRepaymentPlanExcel.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "基本信息");
            sheetName.put(sheet2, "交易结构");
            sheetName.put(sheet3, "租金计划");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "上传")
    @PostMapping("/importData")
    public R importData(MultipartFile file) throws Exception {
        ExcelUtil<OfflineContractExcel> util = new ExcelUtil<OfflineContractExcel>(OfflineContractExcel.class);
        ExcelUtil<OfflineContractStructureExcel> util2 = new ExcelUtil<OfflineContractStructureExcel>(OfflineContractStructureExcel.class);
        ExcelUtil<OfflineContractRepaymentPlanExcel> util3 = new ExcelUtil<OfflineContractRepaymentPlanExcel>(OfflineContractRepaymentPlanExcel.class);
        InputStream inputStream = file.getInputStream();
        InputStream inputStream2 = file.getInputStream();
        InputStream inputStream3 = file.getInputStream();
        try {
            List<OfflineContractExcel> contractExcels = util.importExcel("基本信息", inputStream, 0);
            List<OfflineContractStructureExcel> contractStructureExcels = util2.importExcel("交易结构", inputStream2, 0);
            List<OfflineContractRepaymentPlanExcel> repaymentPlanExcels = util3.importExcel("租金计划", inputStream3, 0);
            offlineContractService.importData(contractExcels, contractStructureExcels, repaymentPlanExcels);
            return R.ok();
        } catch (Exception e) {
            log.error("线下合同导入上传报错", e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(inputStream2);
            IOUtils.closeQuietly(inputStream3);
        }
    }

    @ApiOperation(value = "提交")
    @PostMapping("/submit")
    public R submit(@RequestBody List<Long> ids) {
        // 1.先删除凭证
        offlineContractService.batchDeleteVoucher(ids);
        return R.ok(offlineContractService.submit(ids));
    }

    @ApiOperation(value = "撤回")
    @PostMapping("/withdraw")
    public R withdraw(@RequestBody List<Long> ids) {
        return R.ok(offlineContractService.withdraw(ids));
    }

    @ApiOperation(value = "复核通过")
    @PostMapping("/check/pass")
    public R pass(@RequestBody List<Long> ids) {
        return R.ok(offlineContractService.pass(ids));
    }

    @ApiOperation(value = "复核失败")
    @PostMapping("/check/fail")
    public R fail(@RequestBody List<Long> ids) {
        return R.ok(offlineContractService.fail(ids));
    }

    @ApiOperation(value = "生成凭证")
    @PostMapping("/voucher")
    public R voucher(@RequestBody List<Long> ids) {
        return R.ok(offlineContractService.voucher(ids, YesOrNoEnum.NO.getCode()));
    }

    @ApiOperation(value = "测试审批")
    @PostMapping("/updateProcessStatus")
    public R updateProcessStatus(@RequestBody CommonApproveDTO approveDTO) {
        offlineContractService.updateProcessStatus(approveDTO);
        return R.ok();
    }


}



