package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.entity.PayableInsuranceReportDataEntity ;

import java.util.List;

/**
 * @description: 应付保险费
 * @author: zhangli.chen
 **/
public interface PayableInsuranceReportDataMapper extends BaseMapper<PayableInsuranceReportDataEntity> {

    List<PayableInsuranceReportDataEntity> listByContractCode(List<String> contractCodeList);
}
