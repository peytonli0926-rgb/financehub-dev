package com.utfinancing.financehub.etl.api.factory;

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
public class RemoteKingdeeEasFallbackFactory  implements FallbackFactory<RemoteKingdeeEasService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteKingdeeEasFallbackFactory.class);

    @Override
    public RemoteKingdeeEasService create(Throwable cause) {
        log.error("金蝶EAS服务调用失败:{}", cause.getMessage());

        return null;
    }
}
