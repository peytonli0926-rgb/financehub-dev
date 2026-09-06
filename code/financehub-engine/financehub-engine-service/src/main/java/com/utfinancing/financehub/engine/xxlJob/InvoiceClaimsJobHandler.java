package com.utfinancing.financehub.engine.xxlJob;

import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.engine.constants.DicDataConstant;
import com.utfinancing.financehub.engine.finance.service.IInvoiceClaimService;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;

@Component
public class InvoiceClaimsJobHandler {

    @Resource
    private IInvoiceClaimService invoiceClaimService;

    /**
     * 生成开票认领数据
     * 每隔十分钟执行一次，一次执行1000条数据
     * @throws ParseException
     */
    @XxlJob(value = "generateInvoiceSystemVoucher")
    public void generateInvoicingSystemVoucher() throws ParseException {
        String jobParam = XxlJobHelper.getJobParam();
        Boolean skipRepeatDataCheck = JSONObject.parseObject(jobParam).getBoolean(DicDataConstant.SKIP_REPEAT_DATA_CHECK);

        XxlJobHelper.log("财务中台校验发票系统电子和纸质 重复数据开始");
        invoiceClaimService.checkInvoiceData();
        XxlJobHelper.log("财务中台校验发票系统电子和纸质 重复数据结束");
        XxlJobHelper.log("财务中台修改发票系统电子和纸质 合同编号开始");
        invoiceClaimService.updateInvoiceDataContractCode();
        XxlJobHelper.log("财务中台修改发票系统电子和纸质 合同编号结束");
        XxlJobHelper.log("财务中台生成发票系统电子和纸质凭证开始");
        invoiceClaimService.invoiceGenerateVoucher(skipRepeatDataCheck);
        XxlJobHelper.log("财务中台生成发票系统电子和纸质凭证结束");
    }
}
