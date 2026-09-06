package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.utfinancing.financehub.etl.financial.entity.KingdeeHybVoucherEntity;
import com.utfinancing.financehub.etl.financial.entity.KingdeeMiddleVoucherEntity;
import com.utfinancing.financehub.etl.financial.model.vo.KingdeeHybVoucherVO;
import com.utfinancing.financehub.etl.kingdee.model.dto.OptionDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
@DS("slave_kingdee")
public interface IKingdeeEasService {


    Integer getCurrentPeriodCode(String orgId);

    List<OptionDTO> selectAllPerson();

    List<OptionDTO> selectAllBank();

    List<OptionDTO> selectAllCostCenter();

    List<OptionDTO> selectAllGeneralAsst();

    List<OptionDTO> selectContractData();

    List<Map<String, String>> getCurrentPeriodCodeAll();

    List<KingdeeHybVoucherEntity> selectKingdeeHybVoucher(String periodCode, String voucherDate);


}
