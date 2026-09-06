package com.utfinancing.financehub.etl.api.factory;

import com.utfinancing.financehub.etl.api.RemoteCheckDataService;
import com.utfinancing.financehub.etl.api.RemoteKingdeeEasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
@Component
public class RemoteCheckDataFallbackFactory implements FallbackFactory<RemoteCheckDataService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteCheckDataFallbackFactory.class);

    @Override
    public RemoteCheckDataService create(Throwable cause) {
        log.error("业务系统对账失败:{}", cause.getMessage());

        return null;
    }
}
