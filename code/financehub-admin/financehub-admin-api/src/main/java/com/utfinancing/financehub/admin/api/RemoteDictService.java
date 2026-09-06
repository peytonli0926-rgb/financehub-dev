package com.utfinancing.financehub.admin.api;

import com.utfinancing.financehub.admin.api.factory.RemoteDictFallbackFactory;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.ServiceNameConstants;
import com.utfinancing.financehub.common.core.dto.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(contextId = "remoteDictService",
        value = ServiceNameConstants.ADMIN_SERVICE,
        fallbackFactory = RemoteDictFallbackFactory.class)
public interface RemoteDictService {

    @GetMapping("/dict/data/type/{dictType}")
    public R<List<SysDictData>> listDictData(@PathVariable("dictType") String dictType);


}
