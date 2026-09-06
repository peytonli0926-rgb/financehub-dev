package com.utfinancing.financehub.engine.fallback;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.api.HthxFundCoreService;
import com.utfinancing.financehub.engine.model.dto.HthxFundEbankQueryDTO;
import com.utfinancing.financehub.engine.model.dto.HthxFundEbankTransactionDataDTO;
import com.utfinancing.financehub.engine.model.dto.HthxOnlineBankCrossCheckDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 应用模块名称: 资金系统Feign调用降级工厂类
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/2/24 14:19
 */

@Slf4j
@Component
public class HthxFundCoreServiceFallbackFactory implements FallbackFactory<HthxFundCoreService> {

    /**
     * @description: 查询传入的日期是否是工作日
     */
    @Override
    public HthxFundCoreService create(Throwable cause) {
        return new HthxFundCoreService() {
            @Override
            public R<String> queryDayIsWorkday(String queryDate) {
                return R.fail("fund core service [queryDayIsWorkday] exception!");
            }

            /**
             * @return R<List < HthxFundEbankTransactionDataDTO>>
             * @description: 查询网银信息
             * @author: zhangli.chen
             * @date 2025/08/11 17:33
             */
            @Override
            public R<List<HthxFundEbankTransactionDataDTO>> queryWyxx(HthxFundEbankQueryDTO request) {
                return R.fail("fund core service [queryWyxx] exception!");
            }

            /**
             * @return R<List < HthxOnlineBankCrossCheckDataDTO>>
             * @description: 根据恒运网银查询网银勾稽信息
             * @author: zhangli.chen
             * @date 2025/08/11 19:27
             * @param: list
             */
            @Override
            public R<List<HthxOnlineBankCrossCheckDataDTO>> queryGjxx(HthxFundEbankQueryDTO request) {
                return R.fail("fund core service [queryGjxx] exception!");
            }
        };
    }


}
