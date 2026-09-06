package com.utfinancing.financehub.admin.api.factory;

import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 数据字典服务降级处理
 *
 * @author ruoyi
 */
@Component
public class RemoteDictFallbackFactory implements FallbackFactory<RemoteDictService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteDictFallbackFactory.class);

    @Override
    public RemoteDictService create(Throwable throwable) {
        log.error("数据字典服务调用失败:{}", throwable.getMessage());
        return new RemoteDictService() {
            @Override
            public R<List<SysDictData>> listDictData(@PathVariable String dictType){
                return null;
            }
        };

    }
}
