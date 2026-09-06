package com.utfinancing.financehub.etl.api;

import com.utfinancing.financehub.common.core.constant.ServiceNameConstants;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.api.factory.RemoteKingdeeEasFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@FeignClient(contextId = "remoteKingdeeEasService",
        value = ServiceNameConstants.ETL_SERVICE,
        fallbackFactory = RemoteKingdeeEasFallbackFactory.class)
public interface RemoteKingdeeEasService {

    /**
     * 查询总账当前会计期间
     * @param orgId 签约主体
     * @return
     */
    @GetMapping("/kingdee/eas/getCurrentPeriodCode")
    public R<Integer> getCurrentPeriodCode(@RequestParam(value = "orgId", required = true)String orgId);

    /**
     * 获取金蝶所有当前会计期间
     * @return
     */
    @GetMapping("/kingdee/eas/getCurrentPeriodCodeAll")
    public R<List<Map<String,String>>> getCurrentPeriodCodeAll();

}
