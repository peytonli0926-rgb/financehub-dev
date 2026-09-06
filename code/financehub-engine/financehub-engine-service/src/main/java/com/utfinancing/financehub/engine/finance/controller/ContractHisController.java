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
import com.utfinancing.financehub.engine.finance.model.vo.ContractHisVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanHisVO;
import com.utfinancing.financehub.engine.finance.service.IContractHisService;
import com.utfinancing.financehub.engine.finance.service.IRepaymentPlanHisService;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author : hzhao
 * @Date : Create in 2023-11-01
 * @Description :   ContractHis控制器实现类
 * @Modified :
 */
@Api(tags = "合同修改")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/finance/contract-his")
public class ContractHisController {

    private final IContractHisService contractHisService;
    private final IRepaymentPlanHisService repaymentPlanHisService;


    @PostMapping("/save")
    @ApiOperation(value = "新增")
    public R<Long> save(@Valid @RequestBody ContractHisDTO dto) {
        return R.ok(contractHisService.saveContractHis(dto));
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<Long> update(@PathVariable("id") @Valid @NotNull Long id, @Valid @RequestBody ContractHisDTO dto) {
        return R.ok(contractHisService.updateContractHis(id, dto));
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, type = "long", dataTypeClass = Long.class)
    public R<ContractHisDTO> get(@PathVariable("id") @Valid @NotNull Long id) {
        return R.ok(contractHisService.getContractHisDTOById(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    public R<IPage<ContractHisVO>> page(@RequestBody ContractHisQueryDTO queryDTO) {
        return R.ok(contractHisService.selectPage(queryDTO));
    }

    @ApiOperation(value = "导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody @Valid ContractHisQueryDTO queryDTO) {
        try {
            List<ContractHisVO> contractList = contractHisService.selectList(queryDTO);
            if (CollectionUtils.isEmpty(contractList)) {
                return;
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("合同修改导出.xlsx", "utf8"));
            List<ContractHisExcel> contractHisInfoExcels = BeanUtil.copyToList(contractList, ContractHisExcel.class);
            List<ContractHisStructureExcel> contractHisStructureExcels = BeanUtil.copyToList(contractList, ContractHisStructureExcel.class);
            ContractHisQueryDTO contractHisQueryDTO = new ContractHisQueryDTO();
            contractHisQueryDTO.setContractCodeList(contractList.stream().map(e->e.getContractCode()).collect(Collectors.toList()));
            List<RepaymentPlanHisVO> contractRepaymentPlanVOS = repaymentPlanHisService.selectLastGroupList(contractHisQueryDTO);
            List<ContractHisRepaymentPlanExcel> contractHisRepaymentPlanExcels = BeanUtil.copyToList(contractRepaymentPlanVOS, ContractHisRepaymentPlanExcel.class);

            Map<String, List<?>> map = new HashMap<>();
            String sheet1 = "1";
            String sheet2 = "2";
            String sheet3 = "3";
            map.put(sheet1, contractHisInfoExcels);
            map.put(sheet2, contractHisStructureExcels);
            map.put(sheet3, contractHisRepaymentPlanExcels);
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.EXPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, ContractHisExcel.class);
            mapClass.put(sheet2, ContractHisStructureExcel.class);
            mapClass.put(sheet3, ContractHisRepaymentPlanExcel.class);
            Map<String, String> sheetName = new HashMap<>();
            sheetName.put(sheet1, "基本信息");
            sheetName.put(sheet2, "交易结构");
            sheetName.put(sheet3, "租金计划");
            util.exportManySheetExcel(response, sheetName, mapClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "模板下载")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("合同修改导入模板.xlsx", "utf8"));

            Map<String, List<?>> map = new HashMap<>();
            String sheet1 = "1";
            String sheet2 = "2";
            String sheet3 = "3";
            map.put(sheet1, new ArrayList<>());
            map.put(sheet2, new ArrayList<>());
            map.put(sheet3, new ArrayList<>());
            ExcelManySheetUtil util = new ExcelManySheetUtil(map, StringUtils.EMPTY, Excel.Type.IMPORT);
            Map<String, Class> mapClass = new HashMap<>();
            mapClass.put(sheet1, ContractHisExcel.class);
            mapClass.put(sheet2, ContractHisStructureExcel.class);
            mapClass.put(sheet3, ContractHisRepaymentPlanExcel.class);
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
        ExcelUtil<ContractHisExcel> util = new ExcelUtil<ContractHisExcel>(ContractHisExcel.class);
        ExcelUtil<ContractHisStructureExcel> util2 = new ExcelUtil<ContractHisStructureExcel>(ContractHisStructureExcel.class);
        ExcelUtil<ContractHisRepaymentPlanExcel> util3 = new ExcelUtil<ContractHisRepaymentPlanExcel>(ContractHisRepaymentPlanExcel.class);
        InputStream inputStream = file.getInputStream();
        InputStream inputStream2 = file.getInputStream();
        InputStream inputStream3 = file.getInputStream();
        try {
            List<ContractHisExcel> contractExcels = util.importExcel("基本信息", inputStream, 0);
            List<ContractHisStructureExcel> contractStructureExcels = util2.importExcel("交易结构", inputStream2, 0);
            List<ContractHisRepaymentPlanExcel> repaymentPlanExcels = util3.importExcel("租金计划", inputStream3, 0);
            contractHisService.importData(contractExcels, contractStructureExcels, repaymentPlanExcels);
            return R.ok();
        } catch (Exception e) {
            log.error("修改合同信息上传报错",e);
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(inputStream2);
            IOUtils.closeQuietly(inputStream3);
        }
    }

    @ApiOperation(value = "详情-交易结构")
    @PostMapping("/detail/structure")
    public R<List<ContractHisVO>> detailStructure(@RequestBody ContractHisQueryDTO queryDTO) {
        List<ContractHisVO> contractHisVOS = contractHisService.selectList(queryDTO);
        return R.ok(contractHisVOS);
    }

    @ApiOperation(value = "详情-交易结构-导出")
    @PostMapping("/detail/structure/export")
    public void detailStructureExport(HttpServletResponse response, @RequestBody ContractHisQueryDTO queryDTO) {
        try {
            List<ContractHisVO> contractHisVOS = contractHisService.selectList(queryDTO);
            if (CollectionUtils.isEmpty(contractHisVOS)) {
                return;
            }
            ExcelUtil<ContractHisStructureExcel> util = new ExcelUtil<ContractHisStructureExcel>(ContractHisStructureExcel.class);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("合同修改详情导出.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(contractHisVOS, ContractHisStructureExcel.class), "交易结构数据");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "详情-租金计划")
    @PostMapping("/detail/plan")
    public R<List<RepaymentPlanHisVO>> detailPlan(@RequestBody ContractHisQueryDTO queryDTO) {
        List<ContractHisVO> contractHisVOS = contractHisService.selectList(queryDTO);
        if (CollectionUtils.isEmpty(contractHisVOS)) {
            return R.ok();
        }
        ContractHisQueryDTO contractHisQueryDTO = new ContractHisQueryDTO();
        contractHisQueryDTO.setContractCodeList(contractHisVOS.stream().map(e->e.getContractCode()).collect(Collectors.toList()));
        List<RepaymentPlanHisVO> contractRepaymentPlanVOS = repaymentPlanHisService.selectLastGroupList(contractHisQueryDTO);
        return R.ok(contractRepaymentPlanVOS);
    }

    @ApiOperation(value = "详情-租金计划-导出")
    @PostMapping("/detail/plan/export")
    public void detailPlanExport(HttpServletResponse response, @RequestBody ContractHisQueryDTO queryDTO) {
        try {
            List<ContractHisVO> contractHisVOS = contractHisService.selectList(queryDTO);
            if (CollectionUtils.isEmpty(contractHisVOS)) {
                return;
            }
            ContractHisQueryDTO contractHisQueryDTO = new ContractHisQueryDTO();
            contractHisQueryDTO.setContractCodeList(contractHisVOS.stream().map(e->e.getContractCode()).collect(Collectors.toList()));
            List<RepaymentPlanHisVO> list = repaymentPlanHisService.selectLastGroupList(contractHisQueryDTO);
            ExcelUtil<ContractHisRepaymentPlanExcel> util = new ExcelUtil<ContractHisRepaymentPlanExcel>(ContractHisRepaymentPlanExcel.class);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("合同修改详情导出.xlsx", "utf8"));
            util.exportExcel(response, BeanUtil.copyToList(list, ContractHisRepaymentPlanExcel.class), "租金计划数据");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public R delete(@RequestBody List<Long> ids) {
        contractHisService.deleteByIds(ids);
        return R.ok();
    }

    @ApiOperation(value = "提交")
    @PostMapping("/submit")
    public R submit(@RequestBody List<Long> ids) {
        contractHisService.submit(ids);
        ;
        return R.ok();
    }

    @ApiOperation(value = "撤回")
    @PostMapping("/withdraw")
    public R withdraw(@RequestBody List<Long> ids) {
        contractHisService.withdraw(ids);
        return R.ok();
    }

    @ApiOperation(value = "复核通过")
    @PostMapping("/check/pass")
    public R pass(@RequestBody List<Long> ids) {
        contractHisService.pass(ids);
        return R.ok();
    }

    @ApiOperation(value = "复核失败")
    @PostMapping("/check/fail")
    public R fail(@RequestBody List<Long> ids) {
        contractHisService.fail(ids);
        return R.ok();
    }

    @ApiOperation(value = "生成偿还计划修改凭证")
    @PostMapping("/voucher")
    public R voucher(@RequestBody List<Long> ids) {
        contractHisService.voucher(ids, YesOrNoEnum.NO.getCode());
        return R.ok();
    }

    @ApiOperation(value = "测试审批")
    @PostMapping("/updateProcessStatus")
    public R updateProcessStatus(@RequestBody CommonApproveDTO approveDTO) {
        contractHisService.updateProcessStatus(approveDTO);
        return R.ok();
    }

}



