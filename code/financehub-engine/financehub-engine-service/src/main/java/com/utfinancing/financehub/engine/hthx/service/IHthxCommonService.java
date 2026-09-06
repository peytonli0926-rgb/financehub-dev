package com.utfinancing.financehub.engine.hthx.service;

import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;

import java.util.List;

public interface IHthxCommonService {

    /**
     * @description: 判断当前时间是否可以执行调度任务
     * @author: zhangli.chen
     * @date 2025/02/24 17:47
     * @return boolean true:可以执行 false:不可以执行
     **/
    public boolean checkTaskExecutionDate();

    /**
     * @description: 实时请求获取参数类型配置信息
     * @author: zhangli.chen
     **/
    public List<SysDictData> getDictTypeDataByRealTime (String dictType,String dictLabel);


}
