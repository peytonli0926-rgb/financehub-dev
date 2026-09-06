package com.utfinancing.financehub.etl.financial.service;

import com.utfinancing.financehub.etl.financial.entity.*;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeVoucherEntryInnerDTO;
import com.utfinancing.financehub.etl.middle.entity.EasVoucherHeadEntity;
import io.swagger.models.auth.In;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public interface IKingdeeDataSyncService {

    /**
     * 币种-全量
     */
    Boolean syncCurrencyAll();

    /**
     * 同步签约主体-全量
     */
    Boolean syncOrgCompanyAll();


    /**
     * 同步有会计期间-全量
     */
    Boolean syncPeriodAll();

    /**
     * 同步银行账户-全量
     * @return
     */
    Boolean syncBankAccountAll();

    /**
     * 同步凭证类型-全量
     * @return
     */
    Boolean syncVoucherTypeAll();

    /**
     * 同步会计科目-全量
     * @return
     */
    Boolean syncAccountAll();


    /**
     * 同步职员-全量
     * @return
     */
    Boolean syncPersonAll();

    /**
     * 同步金融机构-全量
     * @return
     */
    Boolean syncBankAll();

    /**
     * 同步成本中心-全量
     * @return
     */
    Boolean syncCostCenterAll();

    /**
     * 同步自定义核算项目-全量
     * @return
     */
    Boolean syncGeneralAsstAll();

    Boolean syncOneContract();

    //同步金蝶全量数据
    Boolean syncKingdeeCustomerAll();

    /**
     * 同步金蝶凭证数据
     * @param periodCode
     * @param voucherDate
     * @return
     */
    Long syncVoucherByDate(Integer periodCode, String voucherDate);

    Long syncVoucherByDateV2(Integer periodCode, String voucherDate, CountDownLatch countDownLatch);

    /**
     * 根据期间同步凭证头
     * @param periodCode
     * @return
     */
    Boolean syncVoucherByPeriod(String periodCode);


    Boolean syncFinhubVoucherHeadV2(String periodCode);

    Boolean syncFinhubVoucherHeadByDayV2(Integer periodCode, String voucherDate);


    Boolean syncKingdeeVoucherEntryByPeriod(String periodCode);

    void asyncFinHubVoucherBalanceDetailV2(CountDownLatch countDownLatch, Map<String, List<KingdeeVoucherEntryEntity>> contractEntryListMap, List<String> groupKeyList, Map<String, AccountEntity> accountFundMap, List<AccountEntity> accountEntityList, Integer periodCode);

    /**
     * 根据期间同步凭证分录
     * @param periodCode
     * @return
     */
    Long syncVoucherEntryByPeriod(Integer periodCode, String voucherDate);


    /**
     * 同步财务中台凭证+余额
     * @return
     */
    Long syncFinhubVoucherBalance(String periodCodes,String contractCodes);


    Boolean syncKingdeeContractBalance(Integer periodCode,String contractCodes);

    //同步金蝶辅助帐余额表
    Boolean syncKingdeeAssistBalance(String periodCodes, String orgId);

    Boolean saveKingdeeAccountBalance(Integer periodCode,String contractCodes);

    Boolean syncKingdeeContractBalance80001(Integer periodCode,String contractCodes);


    /**
     * 线程池异步同步财务中台凭证+余额
     */
    void asyncFinHubVoucherBalance(CountDownLatch countDownLatch, Map<String, List<KingdeeVoucherEntryInnerDTO>> voucherEntryDTOMap, List<String> keyList, Integer periodCode, String voucherDate, Map<String, AccountEntity> accountFundMap, List<AccountEntity> accountEntityList);


    Boolean syncFinHubVoucherDetail(Integer periodCode);

    /**
     * 同步中台凭证明细V2
     * @param periodCodes
     * @return
     */
    Boolean syncFinHubVoucherDetailV2(String periodCodes,String contractCodes);


    Boolean syncFinHubVoucherDetailByDayV2(Integer periodCode, String voucherDate,String contractCodes);


    /**
     * 根据中间表同步凭证
     */
    Boolean syncFinHubVoucherDetailFromMiddleTable(String voucherDate,CountDownLatch countDownLatch);


    /**
     * 根据中间表同步凭证
     */
    Boolean syncFinHubVoucherDetailFromMiddleTablePeriodCodeV3(String periodCodes);


    //同步金蝶客户名称
    Boolean syncKingdeeClientNameAll();

    //异步同步中台凭证
    void asyncFinHubVoucherDetail(List<KingdeeVoucherEntity> voucherEntityList, Map<String, AccountEntity> accountFundMap, List<AccountEntity> accountEntityList, Integer periodCode, boolean isLastPage);

    void executeFinhubVoucherEntry(Map.Entry<String, List<KingdeeVoucherEntryEntity>> entryEntityEntry,
                                   CountDownLatch countDownLatch,
                                   Map<String, AccountEntity> accountFundMap,
                                   VoucherEntity voucherEntity,
                                   List<VoucherEntryEntity> finhubEntryList,
                                   String businessCode,
                                   String systemCode,
                                   List<AccountEntity> accountEntityList,
                                   String orgId,
                                   LocalDateTime voucherDate,
                                   Integer periodCode);


    void batchSaveAccountAssistBalance(CountDownLatch countDownLatch, List<AccountAssistBalanceEntity> accountAssistBalanceEntityList, String percent, Integer periodCode);


    int executeMonth(CountDownLatch totalCountDownLatch, String periodCodeStr, String orgId);
}
