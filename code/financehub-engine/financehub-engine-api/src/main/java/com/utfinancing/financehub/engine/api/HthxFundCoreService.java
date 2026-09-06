package com.utfinancing.financehub.engine.api;


import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.fallback.HthxFundCoreServiceFallbackFactory;
import com.utfinancing.financehub.engine.model.dto.HthxFundEbankQueryDTO;
import com.utfinancing.financehub.engine.model.dto.HthxFundEbankTransactionDataDTO;
import com.utfinancing.financehub.engine.model.dto.HthxOnlineBankCrossCheckDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * @description: 资金系统Feign调用服务配置
 * @author: zhangli.chen
 * @date 2025/02/24 14:17
 * @param: null
 * @return null
 **/
@FeignClient(value = "fund-core-service",url = "${third.service.fund-url}", fallbackFactory = HthxFundCoreServiceFallbackFactory.class)
public interface HthxFundCoreService {

    /**
     * @description: 查询传入的日期是否是工作日
     * @author: zhangli.chen
     * @date 2025/02/24 14:23
     * @param: queryDate
     * @return R
     **/
    @GetMapping("/queryDayIsWorkday/{queryDate}")
    R<String> queryDayIsWorkday(@PathVariable("queryDate") String queryDate);

    /**
     * @description: 查询网银信息
     * @author: zhangli.chen
     * @date 2025/08/11 17:33
     * @return R<List<HthxFundEbankTransactionDataDTO>>
     **/
    @PostMapping("/queryWyxx")
    R<List<HthxFundEbankTransactionDataDTO>> queryWyxx(@RequestBody HthxFundEbankQueryDTO request);

    /**
     * @description: 根据恒运网银查询网银勾稽信息
     * @author: zhangli.chen
     * @date 2025/08/11 19:27
     * @param: list
     * @return R<List<HthxOnlineBankCrossCheckDataDTO>>
     **/
    @PostMapping("/queryGjxx")
    R<List<HthxOnlineBankCrossCheckDataDTO>> queryGjxx(@RequestBody HthxFundEbankQueryDTO request);


}
