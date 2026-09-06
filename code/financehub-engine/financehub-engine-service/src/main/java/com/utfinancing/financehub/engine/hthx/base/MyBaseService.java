package com.utfinancing.financehub.engine.hthx.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import com.utfinancing.financehub.engine.hthx.page.PageParam;
import com.utfinancing.financehub.engine.hthx.page.PageResult;


public interface MyBaseService extends IService {

    <T> PageResult selectByPage(String funcName, T mapper, PageParam pageParam) throws Exception;

}
