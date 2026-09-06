package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.entity.CheckCommonDataResultEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-04-01
 * @Description : CheckCommonDataResult服务类接口
 * @Modified :
 */
public interface ICheckCommonDataResultService extends IService<CheckCommonDataResultEntity> {

    void clearTableData(String executeDateCode, String sqlMark, Integer periodCode);
}
