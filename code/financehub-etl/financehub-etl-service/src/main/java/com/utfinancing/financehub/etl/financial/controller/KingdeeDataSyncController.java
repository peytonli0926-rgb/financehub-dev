package com.utfinancing.financehub.etl.financial.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.http.HttpUtil;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.etl.financial.model.dto.InvoiceClaimDTO;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherAmountSyncDTO;
import com.utfinancing.financehub.etl.financial.service.*;
import com.utfinancing.financehub.etl.kingdee.model.dto.TGLVoucherInitDTO;
import com.utfinancing.financehub.etl.kingdee.service.ITGlVoucherService;
import com.utfinancing.financehub.etl.middle.service.IEasVoucherHeadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 金蝶数据同步Controller
 * @Author : lixin
 * @Date : Create in 12/11/2023
 */
@Api(tags = "金蝶数据同步")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/financial/kingdee")
public class KingdeeDataSyncController {

    private final IKingdeeDataSyncService kingdeeDataSyncService;
    private final IEasVoucherHeadService easVoucherHeadService;
    private final IVoucherService voucherService;
    private final ITGlVoucherService itGlVoucherService;

    private final IOutstandingAmountService outstandingAmountService;

    private final IVoucherAmountInitService voucherAmountInitService;
    private final MiddleVoucherEas2Service middleVoucherEas2Service;

    @PostMapping("/syncCurrencyAll")
    @ApiOperation(value = "同步币种-全量")
    public R<Boolean> syncCurrencyAll() {
        return R.ok(kingdeeDataSyncService.syncCurrencyAll());
    }

    @PostMapping("/syncOrgCompanyAll")
    @ApiOperation(value = "同步签约主体-全量")
    public R<Boolean> syncOrgCompanyAll() {
        return R.ok(kingdeeDataSyncService.syncOrgCompanyAll());
    }

    @PostMapping("/syncPeriodAll")
    @ApiOperation(value = "同步会计期间-全量")
    public R<Boolean> syncPeriodAll() {
        return R.ok(kingdeeDataSyncService.syncPeriodAll());
    }

    @PostMapping("/syncBankAccountAll")
    @ApiOperation(value = "同步银行账户-全量")
    public R<Boolean> syncBankAccountAll() {
        return R.ok(kingdeeDataSyncService.syncBankAccountAll());
    }

    @PostMapping("/syncVoucherTypeAll")
    @ApiOperation(value = "同步凭证类型-全量")
    public R<Boolean> syncVoucherTypeAll() {
        return R.ok(kingdeeDataSyncService.syncVoucherTypeAll());
    }

    @PostMapping("/syncAccountAll")
    @ApiOperation(value = "同步会计科目-全量")
    public R<Boolean> syncAccountAll() {
        return R.ok(kingdeeDataSyncService.syncAccountAll());
    }

    @PostMapping("/syncPersonAll")
    @ApiOperation(value = "同步职员-全量")
    public R<Boolean> syncPersonAll() {
        return R.ok(kingdeeDataSyncService.syncPersonAll());
    }

    @PostMapping("/syncBankAll")
    @ApiOperation(value = "同步金融机构-全量")
    public R<Boolean> syncBankAll() {
        return R.ok(kingdeeDataSyncService.syncBankAll());
    }

    @PostMapping("/syncCostCenterAll")
    @ApiOperation(value = "同步成本中心-全量")
    public R<Boolean> syncCostCenterAll() {
        return R.ok(kingdeeDataSyncService.syncCostCenterAll());
    }

    @PostMapping("/syncGeneralAsstAll")
    @ApiOperation(value = "同步自定义核算项目-全量")
    public R<Boolean> syncGeneralAsstAll() {
        return R.ok(kingdeeDataSyncService.syncGeneralAsstAll());
    }

    @PostMapping("/syncKingdeeCustomerAll")
    @ApiOperation(value = "同步金蝶客户数据-全量")
    public R<Boolean> syncKingdeeCustomerAll() {
        return R.ok(kingdeeDataSyncService.syncKingdeeCustomerAll());
    }

    @PostMapping("/saveKingdeeAccountBalance")
    @ApiOperation(value = "保存金蝶原始合同余额表数据")
    public R<Boolean> saveKingdeeAccountBalance(@RequestParam("periodCode") Integer periodCode,@RequestParam("contractCodes") String contractCodes) {
        return R.ok(kingdeeDataSyncService.saveKingdeeAccountBalance(periodCode,contractCodes));
    }

    @PostMapping("/syncKingdeeContractBalance")
    @ApiOperation(value = "同步金蝶201812合同余额表")
    public R<Boolean> syncKingdeeContractBalance(@RequestParam("periodCode") Integer periodCode,@RequestParam("contractCodes") String contractCodes) {
        return R.ok(kingdeeDataSyncService.syncKingdeeContractBalance(periodCode,contractCodes));
    }

    @PostMapping("/syncKingdeeContractBalance80001")
    @ApiOperation(value = "同步80001机构合同余额表")
    public R<Boolean> syncKingdeeContractBalance80001(@RequestParam("periodCode") Integer periodCode,@RequestParam("contractCodes") String contractCodes) {
        return R.ok(kingdeeDataSyncService.syncKingdeeContractBalance80001(periodCode,contractCodes));
    }


    @PostMapping("/syncVoucherByPeriod")
    @ApiOperation(value = "同步金蝶凭证头-根据会计期间")
    public R<Boolean> syncVoucherByPeriod(@RequestParam("periodCode")String periodCode) {
        return R.ok(kingdeeDataSyncService.syncVoucherByPeriod(periodCode));
    }

    @PostMapping("/syncKingdeeVoucherEntryByPeriod")
    @ApiOperation(value = "同步金蝶凭证分录-根据会计期间")
    public R<Boolean> syncKingdeeVoucherEntryByPeriod(@RequestParam("periodCode")String periodCode) {
        return R.ok(kingdeeDataSyncService.syncKingdeeVoucherEntryByPeriod(periodCode));
    }

    @PostMapping("/syncVoucherEntryByPeriod")
    @ApiOperation(value = "生成中台凭证分录-根据会计期间(2019,2020)")
    public R<Long> syncVoucherEntryByPeriod(@RequestParam("periodCodes")String periodCodes,@RequestParam("contractCodes")String contractCodes) {
        return R.ok(kingdeeDataSyncService.syncFinhubVoucherBalance(periodCodes,contractCodes));
    }


    @PostMapping("/syncFinHubVoucherDetailV2")
    @ApiOperation(value = "生产中台凭证明细-多会计区间V2(2021,2022,2023)")
    public R<Boolean> syncFinHubVoucherDetailV2(@RequestParam("periodCodes")String periodCodes,@RequestParam("contractCodes")String contractCodes) {
        return R.ok(kingdeeDataSyncService.syncFinHubVoucherDetailV2(periodCodes,contractCodes));
    }

    @PostMapping("/syncVoucherByPeriodV2")
    @ApiOperation(value = "同步金蝶凭证头-根据会计期间-V2")
    public R<Boolean> syncVoucherByPeriodV2(@RequestParam("periodCodes")String periodCodes) {
        return R.ok(kingdeeDataSyncService.syncFinhubVoucherHeadV2(periodCodes));
    }

//    @PostMapping("/syncFinhubVoucherHeadByDayV2")
//    @ApiOperation(value = "同步金蝶凭证头-按天-未过账-V2")
//    public R<Boolean> syncFinhubVoucherHeadByDayV2(@RequestParam("periodCode")Integer periodCode, @RequestParam("voucherDate")String voucherDate) {
//        return R.ok(kingdeeDataSyncService.syncFinhubVoucherHeadByDayV2(periodCode, voucherDate));
//    }
//
//    @PostMapping("/syncFinHubVoucherDetailByDayV2")
//    @ApiOperation(value = "生产中台凭证明细-未过账-按天V2")
//    public R<Boolean> syncFinHubVoucherDetailByDayV2(@RequestParam("periodCode")Integer periodCode, @RequestParam("voucherDate")String voucherDate) {
//        return R.ok(kingdeeDataSyncService.syncFinHubVoucherDetailByDayV2(periodCode, voucherDate));
//    }

    @PostMapping("/syncFinHubVoucherDetailFromMiddleTableV3")
    @ApiOperation(value = "根据中间表同步凭证-未过账-按天V3")
    public R<Boolean> syncFinHubVoucherDetailFromMiddleTableV3(@RequestParam("voucherDate")String voucherDate) {
        return R.ok(kingdeeDataSyncService.syncFinHubVoucherDetailFromMiddleTable(voucherDate,null));
    }

    @PostMapping("/syncFinHubVoucherDetailFromMiddleTablePeriodCodeV3")
    @ApiOperation(value = "根据中间表同步凭证-未过账-按月")
    public R<Boolean> syncFinHubVoucherDetailFromMiddleTablePeriodCodeV3(@RequestParam("periodCodes")String periodCodes) {
        return R.ok(kingdeeDataSyncService.syncFinHubVoucherDetailFromMiddleTablePeriodCodeV3(periodCodes));
    }

    @PostMapping("/exportMidVoucherEntryByDate")
    @ApiOperation(value = "导出中间表凭证数据-按天")
    public R<String> exportMidVoucherEntryByDate(@RequestParam("voucherDate")String voucherDate) {
        return R.ok(easVoucherHeadService.exportMidVoucherEntryByDate(voucherDate));
    }

    @PostMapping("/exportFinhubVoucherEntryByDate")
    @ApiOperation(value = "导出财务中台凭证数据-按天")
    public R<String> exportFinhubVoucherEntryByDate(@RequestParam("voucherDate")String voucherDate) {
        return R.ok(voucherService.exportFinhubVoucherEntryByDate(voucherDate));
    }

    @PostMapping("/exportMidVoucherAsstacttByDate")
    @ApiOperation(value = "导出中间表辅助账-按天")
    public R<String> exportMidVoucherAsstacttByDate(@RequestParam("voucherDate")String voucherDate) {
        return R.ok(easVoucherHeadService.exportMidVoucherAsstacttByDate(voucherDate));
    }

    @PostMapping("/exportMidVoucherIncloudAsscateByDate")
    @ApiOperation(value = "导出中间表凭证数据-按天-V2")
    public R<String> exportMidVoucherIncloudAsscateByDate(@RequestParam("voucherDate")String voucherDate) {
        return R.ok(easVoucherHeadService.exportMidVoucherIncloudAsscateByDate(voucherDate));
    }

    @PostMapping("/syncKingdeeClientNameAll")
    @ApiOperation(value = "同步中台所有客户名称")
    public R<Boolean> syncKingdeeClientNameAll() {
        return R.ok(kingdeeDataSyncService.syncKingdeeClientNameAll());
    }


    @PostMapping("/exportFinhubData")
    @ApiOperation(value = "导出财务中台数据")
    public R<String> exportFinhubData(@RequestBody String statement, @RequestHeader("columns")String columns) {
        return R.ok(voucherService.exportFinhubData(statement, columns));
    }

    @PostMapping("/exportMiddleData")
    @ApiOperation(value = "导出中间库数据")
    public R<String> exportMiddleData(@RequestBody String statement, @RequestHeader("columns")String columns) {
        return R.ok(easVoucherHeadService.exportMiddleData(statement, columns));
    }

    @PostMapping("/exportKingdeeData")
    @ApiOperation(value = "导出金蝶数据")
    public R<String> exportKingdeeData(@RequestBody String statement, @RequestHeader("columns")String columns) {
        return R.ok(itGlVoucherService.exportKingdeeData(statement, columns));
    }

    /**
     * 以下是同步中台辅助帐余额表的接口
     */
    @PostMapping("/syncKingdeeAssistBalance201812")
    @ApiOperation(value = "辅助帐余额表-201812")
    public R<Boolean> syncKingdeeAssistBalance201812(@RequestParam("periodCodes")String periodCodes, @RequestParam("orgId") String orgId) {
        if ("ALL".equals(orgId)){
            orgId = null;
        }
        return R.ok(kingdeeDataSyncService.syncKingdeeAssistBalance(periodCodes, orgId));
    }

    /**
     * 未实现收益数据同步
     */
    @PostMapping("/outstandingAmountSync")
    @ApiOperation(value = "未实现收益数据同步")
    public R<Boolean> outstandingAmountSync(@RequestParam("period")String period) {
        return outstandingAmountService.outstandingAmountInit(period);
    }

    /**
     * 金蝶202311期合同的借方金额
     */
    @PostMapping("/voucherAmountSync")
    @ApiOperation(value = "金蝶202311期合同的借方金额")
    public R<List<TGLVoucherInitDTO>> voucherAmountSync(@RequestBody TGLVoucherInitDTO dto) {
        return voucherAmountInitService.voucherAmountInit(dto);
    }

    @PostMapping("/syncVoucherToEas2ByPeriodCodes")
    @ApiOperation(value = "同步金蝶中间表凭证到EAS2系统-按照会计期间同步")
    public R<Boolean> syncVoucherToEas2ByPeriodCodes(@RequestParam("periodCodes")String periodCodes) throws Exception {
        return R.ok(middleVoucherEas2Service.syncVoucherToEas2ByPeriodCodes(periodCodes));
    }

    @PostMapping("/syncStageVoucherToEas2ByPeriodCodes")
    @ApiOperation(value = "同步金蝶中间表暂存凭证到EAS2系统-按照会计期间同步")
    public R<Boolean> syncStageVoucherToEas2ByPeriodCodes(@RequestParam("periodCodes")String periodCodes) throws Exception {
        return R.ok(middleVoucherEas2Service.syncStageVoucherToEas2ByPeriodCodes(periodCodes));
    }

    @PostMapping("/syncVoucherToEas2ByVoucherDate")
    @ApiOperation(value = "同步金蝶中间表凭证到EAS2系统-按照会计时间(yyyy-MM-dd)同步")
    public R<Boolean> syncVoucherToEas2ByVoucherDate(@RequestParam(value = "voucherDate",required = true)String voucherDate) {
        return R.ok(middleVoucherEas2Service.syncVoucherToEas2ByVoucherDate(voucherDate));
    }

    @PostMapping("/syncFinhubVoucherToEas2ByVoucherDate")
    @ApiOperation(value = "汇总同步中台（暂存，复核，过账）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步")
    public R<Boolean> syncFinhubVoucherToEas2ByVoucherDate(@RequestParam(value = "voucherDate",required = true)String voucherDate) {
        return R.ok(middleVoucherEas2Service.syncFinhubVoucherToEas2ByVoucherDate(voucherDate));
    }

    @PostMapping("/syncFinhubVoucherToEas2ByPeriodCodes")
    @ApiOperation(value = "汇总同步中台（暂存，复核，过账）凭证表到EAS2系统-按照会计期间同步")
    public R<Boolean> syncFinhubVoucherToEas2ByPeriodCodes(@RequestParam("periodCodes")String periodCodes) throws Exception {
        return R.ok(middleVoucherEas2Service.syncFinhubVoucherToEas2ByPeriodCodes(periodCodes));
    }

    @PostMapping("/syncSubmitFinhubVoucherToEas2ByVoucherDate")
    @ApiOperation(value = "汇总同步中台（提交）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步")
    public R<Boolean> syncSubmitFinhubVoucherToEas2ByVoucherDate(@RequestParam(value = "voucherDate",required = true)String voucherDate) {
        return R.ok(middleVoucherEas2Service.syncSubmitFinhubVoucherToEas2ByVoucherDate(voucherDate));
    }

    @PostMapping("/syncSubmitFinhubVoucherToEas2ByPeriodCodes")
    @ApiOperation(value = "汇总同步中台（提交）凭证表到EAS2系统-按照会计期间同步")
    public R<Boolean> syncSubmitFinhubVoucherToEas2ByPeriodCodes(@RequestParam("periodCodes")String periodCodes) throws Exception {
        return R.ok(middleVoucherEas2Service.syncSubmitFinhubVoucherToEas2ByPeriodCodes(periodCodes));
    }

    @PostMapping("/syncNoSummaryFinhubVoucherToEas2ByVoucherDate")
    @ApiOperation(value = "不汇总同步中台（暂存，复核，过账）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步")
    public R<Boolean> syncNoSummaryFinhubVoucherToEas2ByVoucherDate(@RequestParam(value = "voucherDate",required = true)String voucherDate) {
        return R.ok(middleVoucherEas2Service.syncNoSummaryFinhubVoucherToEas2ByVoucherDate(voucherDate,false));
    }

    @PostMapping("/syncNoSummaryFinhubVoucherToEas2ByPeriodCodes")
    @ApiOperation(value = "不汇总同步中台（暂存，复核，过账）凭证表到EAS2系统-按照会计期间同步")
    public R<Boolean> syncNoSummaryFinhubVoucherToEas2ByPeriodCodes(@RequestParam("periodCodes")String periodCodes) throws Exception {
        return R.ok(middleVoucherEas2Service.syncNoSummaryFinhubVoucherToEas2ByPeriodCodes(periodCodes));
    }

    @PostMapping("/syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate")
    @ApiOperation(value = "不汇总同步中台（提交）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步")
    public R<Boolean> syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate(@RequestParam(value = "voucherDate",required = true)String voucherDate) {
        return R.ok(middleVoucherEas2Service.syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate(voucherDate,false));
    }

    @PostMapping("/syncNoSummarySubmitFinhubVoucherToEas2ByPeriodCodes")
    @ApiOperation(value = "不汇总同步中台（提交）凭证表到EAS2系统-按照会计期间同步")
    public R<Boolean> syncNoSummarySubmitFinhubVoucherToEas2ByPeriodCodes(@RequestParam("periodCodes")String periodCodes) throws Exception {
        return R.ok(middleVoucherEas2Service.syncSubmitFinhubVoucherToEas2ByPeriodCodes(periodCodes));
    }

    @PostMapping("/syncKingDeeVoucherByVoucherDate")
    @ApiOperation(value = "同步金蝶恒运宝凭证到中台")
    public R<Boolean> syncKingDeeVoucherByVoucherDate(@RequestParam(value = "voucherDate",required = true)String voucherDate) {
        return R.ok(middleVoucherEas2Service.syncKingDeeVoucherByVoucherDate(voucherDate));
    }

    @PostMapping("/syncKingDeeMiddleVoucherByVoucherDate")
    @ApiOperation(value = "同步金蝶中间表贵安，现代物流凭证到中台")
    public R<Boolean> syncKingDeeMiddleVoucherByVoucherDate(@RequestParam(value = "voucherDate",required = true)String voucherDate) {
        return R.ok(middleVoucherEas2Service.syncKingDeeMiddleVoucherByVoucherDate(voucherDate));
    }

    @PostMapping("/syncFinhubVoucherEntryToEas2ByPeriodCodes")
    @ApiOperation(value = "同步中台凭证分录到EAS2系统-按照会计期间同步")
    public R syncFinhubVoucherEntryToEas2ByPeriodCodes(@RequestParam("periodCodes")String periodCodes) throws Exception {
        middleVoucherEas2Service.syncFinhubVoucherEntryToEas2ByPeriodCodes(periodCodes);
        return R.ok();
    }

    @PostMapping("/syncFinhubVoucherEntryToEas2ByVoucherDate")
    @ApiOperation(value = "单条同步中台凭证分录到EAS2系统-按照记账日期（yyyy-MM-dd）同步")
    public R syncFinhubVoucherEntryToEas2ByVoucherDate(@RequestParam(value = "voucherDate",required = true)String voucherDate) throws Exception {
        middleVoucherEas2Service.syncFinhubVoucherEntryToEas2ByVoucherDate(voucherDate);
        return R.ok();
    }


    @PostMapping("/syncAllFinhubVoucherToEas2ByVoucherDate")
    @ApiOperation(value = "汇总同步所有中台凭证到EAS2系统-按照记账日期（yyyy-MM-dd）同步")
    public R syncAllFinhubVoucherToEas2ByVoucherDate(@RequestParam(value = "voucherDate",required = true)String voucherDate) throws Exception {
        middleVoucherEas2Service.syncAllFinhubVoucherToEas2ByVoucherDate(voucherDate);
        return R.ok();
    }
}
