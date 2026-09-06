package com.utfinancing.financehub.etl.api;

import com.utfinancing.financehub.common.core.constant.ServiceNameConstants;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.api.factory.RemoteCheckDataFallbackFactory;
import com.utfinancing.financehub.etl.api.factory.RemoteKingdeeEasFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(contextId = "remoteCheckDataService",
        value = ServiceNameConstants.ETL_SERVICE,
        fallbackFactory = RemoteCheckDataFallbackFactory.class)
public interface RemoteCheckDataService {

    /**
     * 将对应期间的金蝶科目余额数据放到临时表
     * @param periodCode 期间
     * @return
     */
    @GetMapping("/financial/check/kingdee/saveToTmp")
    public R<String> saveToKingdeeTmp(@RequestParam(value = "periodCode", required = true)String periodCode);

    /**
     * 将对应期间的金蝶中间表科目发生额数据放到临时表
     * @param periodCode 期间
     * @return
     */
    @GetMapping("/financial/check/middle/saveToTmp")
    public R<String> saveToMiddleTmp(@RequestParam(value = "periodCode", required = true)String periodCode);

    /**
     * 根据传来的db code和sql mark将对应period的查询结果存储到零食表
     * @param periodCode 期间
     * @param sqlMark sql标志，也就是业务场景
     * @return
     */
    @GetMapping("/financial/check-sql/common/saveToTmp")
    public R<String> saveToCommonTmp(@RequestParam(value = "periodCode", required = true)Integer periodCode,
//                                     @RequestParam(value = "systemCode", required = true)String systemCode,
                                     @RequestParam(value = "sqlMark", required = true)String sqlMark);
}
