package com.utfinancing.financehub.etl.financial.service;

import java.util.concurrent.CountDownLatch;

/**
 * <ul>
 * <li>Project : financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.financial.service.MiddleVoucherEas2Service</li>
 * <li>CreateTime : 2024/04/07 17:18</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public interface MiddleVoucherEas2Service {

    /**
     * 按照会计期间年月同步金蝶中间库数据到Eas2
     * @param periodCodes
     * @return
     */
    Boolean syncVoucherToEas2ByPeriodCodes(String periodCodes) throws Exception;

    /**
     * 按照会计期间年月同步金蝶中间库数据到Eas2
     * @param periodCodes
     * @return
     */
    Boolean syncStageVoucherToEas2ByPeriodCodes(String periodCodes) throws Exception;

    /**
     * 按照会计期间日期同步金蝶中间库数据到Eas2
     * @param voucherDate
     */
    Boolean syncVoucherToEas2ByVoucherDate(String voucherDate);

    void syncVoucherToEas2ByVoucherDate2(String day, CountDownLatch countDownLatch) throws Exception;

    void syncStageVoucherToEas2ByVoucherDate2(String day, CountDownLatch countDownLatch) throws Exception;

    /**
     * 按照会计期间日期同步中台凭证数据到Eas2
     * @param voucherDate
     */
    Boolean syncFinhubVoucherToEas2ByVoucherDate(String voucherDate);

    /**
     * 按照会计期间日期同步中台凭证数据到Eas2
     * @param voucherDate
     */
    Boolean syncSubmitFinhubVoucherToEas2ByVoucherDate(String voucherDate);

    /**
     * 按照会计期间年月同步中台暂存，复核，过账数据到Eas2
     * @param periodCodes
     * @return
     */
    Boolean syncFinhubVoucherToEas2ByPeriodCodes(String periodCodes) throws Exception;

    /**
     * 按照会计期间年月同步中台暂存，复核，过账数据到Eas2
     * @param periodCodes
     * @return
     */
    Boolean syncSubmitFinhubVoucherToEas2ByPeriodCodes(String periodCodes) throws Exception;


    void syncFinhubVoucherToEas2ByVoucherDate2(String day, CountDownLatch countDownLatch) throws Exception;

    /**
     * 按照会计期间日期同步中台凭证数据到Eas2
     * @param voucherDate
     */
    void syncSubmitFinhubVoucherToEas2ByVoucherDate2(String voucherDate, CountDownLatch countDownLatch) throws Exception;

    /**
     * 按照会计期间日期同步中台凭证数据到Eas2
     * @param voucherDate
     */
    Boolean syncNoSummaryFinhubVoucherToEas2ByVoucherDate(String voucherDate,boolean isEntryFlag);

    /**
     * 按照会计期间年月同步中台暂存，复核，过账数据到Eas2
     * @param periodCodes
     * @return
     */
    Boolean syncNoSummaryFinhubVoucherToEas2ByPeriodCodes(String periodCodes) throws Exception;

    void syncNoSummaryFinhubVoucherToEas2ByVoucherDate2(String day, CountDownLatch countDownLatch,boolean isEntryFlag);

    /**
     * 按照会计期间日期同步中台凭证数据到Eas2
     * @param voucherDate
     */
    Boolean syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate(String voucherDate,boolean isEntryFlag);

    /**
     * 按照会计期间年月同步中台暂存，复核，过账数据到Eas2
     * @param periodCodes
     * @return
     */
    Boolean syncNoSummarySubmitFinhubVoucherToEas2ByPeriodCodes(String periodCodes) throws Exception;

    /**
     * 按照会计期间日期同步中台凭证数据到Eas2
     * @param voucherDate
     */
    void syncNoSummarySubmitFinhubVoucherToEas2ByVoucherDate2(String voucherDate, CountDownLatch countDownLatch,boolean isEntryFlag);

    /**
     * 同步金蝶恒运宝凭证数据到中台
     * @param voucherDate
     * @throws Exception
     */
    boolean syncKingDeeVoucherByVoucherDate(String voucherDate);

    /**
     * 同步金蝶恒运宝凭证数据到中台
     * @param voucherDate
     * @throws Exception
     */
    boolean syncKingDeeMiddleVoucherByVoucherDate(String voucherDate);


    void syncFinhubVoucherEntryToEas2ByPeriodCodes(String periodCodes);

    void syncFinhubVoucherEntryToEas2ByVoucherDate(String voucherDate);

    void syncAllFinhubVoucherToEas2ByVoucherDate(String voucherDate);

}
