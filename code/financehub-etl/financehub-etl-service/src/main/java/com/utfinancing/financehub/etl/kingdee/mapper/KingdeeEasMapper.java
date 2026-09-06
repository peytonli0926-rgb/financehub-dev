package com.utfinancing.financehub.etl.kingdee.mapper;

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
public interface KingdeeEasMapper {

    Integer getCurrentPeriodCode(@Param("orgId")String orgId);

    List<OptionDTO> selectAllPerson();

    List<OptionDTO> selectAllBank();

    List<OptionDTO> selectAllCostCenter();

    List<OptionDTO> selectAllGeneralAsst();

    List<OptionDTO> selectContractData();

    List<Map<String, String>> getCurrentPeriodCodeAll();

    List<KingdeeHybVoucherEntity> selectKingdeeHybVoucher(@Param("periodCode") String periodCode, @Param("voucherDate")String voucherDate);


}
