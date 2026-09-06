package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.utfinancing.financehub.etl.kingdee.model.dto.OptionDTO;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
//@DS("slave_kingdee")
public interface IKingdeeCheckService {

    String saveKingdeeDataToTmp(String periodCode);

}
