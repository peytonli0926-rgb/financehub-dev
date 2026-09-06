package com.utfinancing.financehub.etl.xxljob;

import cn.hutool.db.DaoTemplate;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.etl.financial.model.dto.InvoiceClaimQueryDTO;
import com.utfinancing.financehub.etl.financial.service.InvoiceService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.xxljob.InvoiceClaimJobHandler</li>
 * <li>CreateTime : 2023/11/15 16:39</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Component
public class InvoiceClaimJobHandler {

    @Resource
    private InvoiceService invoiceService;

    /**
     * 同步开票认领数据
     * 每隔两小时执行一次，一次读取当日数据
     * @throws ParseException
     */
    @XxlJob(value = "syncInvoicingSystem")
    public void syncInvoicingSystem() throws ParseException {
        String businessDate = XxlJobHelper.getJobParam();
        InvoiceClaimQueryDTO queryDTO = new InvoiceClaimQueryDTO();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date doucmentDate = new Date();
        if (StringUtils.isNotEmpty(businessDate)) {
            doucmentDate = sdf.parse(businessDate);
        }
        queryDTO.setDocumentDate(doucmentDate);
        XxlJobHelper.log("财务中台同步发票系统电子和纸质发票数据开始参数："+ JSON.toJSONString(queryDTO));
        invoiceService.syncInvoicingSystem(queryDTO);
        XxlJobHelper.log("财务中台同步发票系统电子和纸质发票数据结束");
    }

    /**
     * 生成开票认领数据
     * 每隔十分钟执行一次，一次执行1000条数据
     * @throws ParseException
     */
    @XxlJob(value = "generateInvoicingSystemVoucher")
    public void generateInvoicingSystemVoucher() throws ParseException {
        XxlJobHelper.log("财务中台生成发票系统电子和纸质凭证开始");
        invoiceService.invoiceGenerateVoucher();
        XxlJobHelper.log("财务中台生成发票系统电子和纸质凭证结束");
    }
}
