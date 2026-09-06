package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.CheckCommonDataQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.CheckCommonDataDTO;
import com.utfinancing.financehub.etl.financial.model.vo.CheckCommonDataVO;
import com.utfinancing.financehub.etl.financial.entity.CheckCommonDataEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-30
 * @Description : CheckCommonData服务类接口
 * @Modified :
 */
public interface ICheckCommonDataService extends IService<CheckCommonDataEntity> {

    void clearTableData(String executeDateCode, String sqlMark, Integer periodCode);
}
