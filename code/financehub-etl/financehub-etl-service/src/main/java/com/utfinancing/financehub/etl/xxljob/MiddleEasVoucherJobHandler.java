package com.utfinancing.financehub.etl.xxljob;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.etl.financial.service.MiddleVoucherEas2Service;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.xxljob.MiddleEasVoucherJobHandler</li>
 * <li>CreateTime : 2024/04/07 15:42</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Slf4j
@Component
public class MiddleEasVoucherJobHandler {

    @Resource
    MiddleVoucherEas2Service middleVoucherEas2Service;


    @XxlJob(value = "syncVoucherToEas2ByVoucherDate")
    public void syncVoucherToEas2ByVoucherDate() {
        String businessDate = XxlJobHelper.getJobParam();
        if (StringUtils.isEmpty(businessDate)) {
            businessDate = DateUtil.format(new Date(),"yyyy-MM-dd");
        }
        XxlJobHelper.log("金蝶中间库同步凭证数据到Eas2系统参数：{}",businessDate);
        log.info("金蝶中间库同步凭证数据到Eas2系统参数：{}",businessDate);
        middleVoucherEas2Service.syncVoucherToEas2ByVoucherDate(businessDate);
        log.info("金蝶中间库同步凭证数据到Eas2系统结束");
        XxlJobHelper.log("金蝶中间库同步凭证数据到Eas2系统结束");
    }

    @XxlJob(value = "syncFinhubVoucherToEas2ByVoucherDate")
    public void syncFinhubVoucherToEas2ByVoucherDate() {
        String businessDate = XxlJobHelper.getJobParam();
        if (StringUtils.isEmpty(businessDate)) {
            businessDate = DateUtil.format(new Date(),"yyyy-MM-dd");
        }
        XxlJobHelper.log("同步中台（暂存，复核，过账）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步：{}",businessDate);
        log.info("同步中台（暂存，复核，过账）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步：{}",businessDate);
        middleVoucherEas2Service.syncFinhubVoucherToEas2ByVoucherDate(businessDate);
        log.info("同步中台（暂存，复核，过账）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步结束");
        XxlJobHelper.log("金蝶中间库同步凭证数据到Eas2系统结束");
    }

    @XxlJob(value = "syncSubmitFinhubVoucherToEas2ByVoucherDate")
    public void syncSubmitFinhubVoucherToEas2ByVoucherDate() {
        String businessDate = XxlJobHelper.getJobParam();
        if (StringUtils.isEmpty(businessDate)) {
            businessDate = DateUtil.format(new Date(),"yyyy-MM-dd");
        }
        XxlJobHelper.log("同步中台（提交）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步：{}",businessDate);
        log.info("同步中台（提交）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步：{}",businessDate);
        middleVoucherEas2Service.syncSubmitFinhubVoucherToEas2ByVoucherDate(businessDate);
        log.info("同步中台（提交）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步结束");
        XxlJobHelper.log("金蝶中间库同步凭证数据到Eas2系统结束");
    }

    @XxlJob(value = "syncNoSummaryFinhubVoucherToEas2ByVoucherDate")
    public void syncNoSummaryFinhubVoucherToEas2ByVoucherDate() {
        String businessDate = XxlJobHelper.getJobParam();
        if (StringUtils.isEmpty(businessDate)) {
            businessDate = DateUtil.format(new Date(),"yyyy-MM-dd");
        }
        XxlJobHelper.log("不汇总同步中台（暂存，复核，过账）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步：{}",businessDate);
        log.info("不汇总同步中台（暂存，复核，过账）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步：{}",businessDate);
        middleVoucherEas2Service.syncNoSummaryFinhubVoucherToEas2ByVoucherDate(businessDate,false);
        log.info("不汇总同步中台（暂存，复核，过账）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步结束");
        XxlJobHelper.log("不汇总同步中台（暂存，复核，过账）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步结束");
    }

    @XxlJob(value = "syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate")
    public void syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate() {
        String businessDate = XxlJobHelper.getJobParam();
        if (StringUtils.isEmpty(businessDate)) {
            businessDate = DateUtil.format(new Date(),"yyyy-MM-dd");
        }
        XxlJobHelper.log("不汇总同步中台（提交）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步：{}",businessDate);
        log.info("不汇总同步中台（提交）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步：{}",businessDate);
        middleVoucherEas2Service.syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate(businessDate,false);
        log.info("不汇总同步中台（提交）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步结束");
        XxlJobHelper.log("不汇总同步中台（提交）凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步结束");
    }


    @XxlJob(value = "syncKingDeeVoucherByVoucherDate")
    public void syncKingDeeVoucherByVoucherDate() {
        String voucherDate = XxlJobHelper.getJobParam();
        if (StringUtils.isEmpty(voucherDate)) {
            voucherDate = DateUtil.format(new Date(),"yyyy-MM-dd");
        }
        XxlJobHelper.log("定时任务金蝶同步恒运宝数据日期：{}",voucherDate);
        log.info("定时任务金蝶同步恒运宝数据日期：{}",voucherDate);
        middleVoucherEas2Service.syncKingDeeVoucherByVoucherDate(voucherDate);
        log.info("定时任务金蝶同步恒运宝结束，日期：{}",voucherDate);
        XxlJobHelper.log("定时任务金蝶同步恒运宝结束");
    }

    @XxlJob(value = "syncKingDeeMiddleVoucherByVoucherDate")
    public void syncKingDeeMiddleVoucherByVoucherDate() {
        String voucherDate = XxlJobHelper.getJobParam();
        if (StringUtils.isEmpty(voucherDate)) {
            voucherDate = DateUtil.format(new Date(),"yyyy-MM-dd");
        }
        XxlJobHelper.log("定时任务金蝶同步贵安，现代物流数据日期：{}",voucherDate);
        log.info("定时任务金蝶同步贵安，现代物流数据日期：{}",voucherDate);
        middleVoucherEas2Service.syncKingDeeMiddleVoucherByVoucherDate(voucherDate);
        log.info("定时任务金蝶同步贵安，现代物流结束，日期：{}",voucherDate);
        XxlJobHelper.log("定时任务金蝶同步贵安，现代物流结束");
    }

    @XxlJob(value = "syncFinhubVoucherEntryToEas2ByVoucherDate")
    public void syncFinhubVoucherEntryToEas2ByVoucherDate() {
        String businessDate = XxlJobHelper.getJobParam();
        if (StringUtils.isEmpty(businessDate)) {
            businessDate = DateUtil.format(new Date(),"yyyy-MM-dd");
        }
        XxlJobHelper.log("中台分录凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步：{}",businessDate);
        log.info("中台分录凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步：{}",businessDate);
        middleVoucherEas2Service.syncFinhubVoucherEntryToEas2ByVoucherDate(businessDate);
        log.info("中台分录凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步结束");
        XxlJobHelper.log("中台分录凭证表到EAS2系统-按照会计时间(yyyy-MM-dd)同步结束");
    }

    @XxlJob(value = "syncAllFinhubVoucherToEas2ByVoucherDate")
    public void syncAllFinhubVoucherToEas2ByVoucherDate() {
        String businessDate = XxlJobHelper.getJobParam();
        if (StringUtils.isEmpty(businessDate)) {
            businessDate = DateUtil.format(new Date(),"yyyy-MM-dd");
        }
        XxlJobHelper.log("汇总同步所有中台凭证到EAS2系统-按照记账日期（yyyy-MM-dd）同步：{}",businessDate);
        log.info("汇总同步所有中台凭证到EAS2系统-按照记账日期（yyyy-MM-dd）同步：{}",businessDate);
        middleVoucherEas2Service.syncAllFinhubVoucherToEas2ByVoucherDate(businessDate);
        log.info("汇总同步所有中台凭证到EAS2系统-按照记账日期（yyyy-MM-dd）同步结束");
        XxlJobHelper.log("汇总同步所有中台凭证到EAS2系统-按照记账日期（yyyy-MM-dd）同步结束");
    }

}
