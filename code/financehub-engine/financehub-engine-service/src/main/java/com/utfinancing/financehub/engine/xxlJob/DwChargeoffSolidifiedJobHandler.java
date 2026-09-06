package com.utfinancing.financehub.engine.xxlJob;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.constants.DicDataConstant;
import com.utfinancing.financehub.engine.finance.model.dto.ChargeOffQueryDTO;
import com.utfinancing.financehub.engine.finance.service.IChargeOffService;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.etl.api.KingdeeDataSyncFacade;
import com.utfinancing.financehub.etl.model.dto.OrgPeriodDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum.ExcelColumnNames.periodCode;

@Slf4j
@Component
@AllArgsConstructor
public class DwChargeoffSolidifiedJobHandler {
    @Autowired
    private IChargeOffService chargeOffService;
    @Autowired
    private final KingdeeDataSyncFacade kingdeeDataSyncFacade;

    @XxlJob(value = "dwChargeoffSolidified")
    public void dwChargeoffSolidified() {
        MDC.put("PID", UUID.fastUUID().toString(true));
        String jobParam = XxlJobHelper.getJobParam();
        Integer currentPeriod = JSONObject.parseObject(jobParam).getInteger("periodCode");
        Integer lstPeriod = null;
        if (currentPeriod != null) {
            DateTime lastMonth = DateUtil.offsetMonth(DateUtil.parse(currentPeriod.toString(), "yyyyMM"), -1);
            lstPeriod = Integer.valueOf(DateUtil.format(lastMonth, "yyyyMM"));
        } else {
            R<List<OrgPeriodDTO>> listR = kingdeeDataSyncFacade.queryKingdeeAccountingPeriod();
            DateTime periodCodeDate = DateUtil.parse(listR.getData().get(0).getPeriodCode(), "yyyyMM");
            currentPeriod = Integer.valueOf(DateUtil.format(periodCodeDate, "yyyyMM"));
            lstPeriod = Integer.valueOf(DateUtil.format(DateUtil.offsetMonth(periodCodeDate, -1), "yyyyMM"));
        }
        ChargeOffQueryDTO chargeOffQueryDTO = new ChargeOffQueryDTO();
        chargeOffQueryDTO.setPeriodCode(currentPeriod);
        XxlJobHelper.log("数仓固化chargeOff数据开始-当期");
        chargeOffService.DwChargeoffSolidified(chargeOffQueryDTO);
        XxlJobHelper.log("数仓固化chargeOff数据结束-当期");
        XxlJobHelper.log("数仓固化chargeOff数据开始-上期");
        chargeOffQueryDTO.setPeriodCode(lstPeriod);
        chargeOffService.DwChargeoffSolidified(chargeOffQueryDTO);
        XxlJobHelper.log("数仓固化chargeOff数据结束-上期");
    }
}
